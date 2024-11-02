package repository;

import lombok.SneakyThrows;
import model.UrlModel;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UrlRepository extends BaseDB {

    protected static List<UrlModel> urls = new ArrayList<UrlModel>();


    public static void addURL(UrlModel url) throws SQLException {
        String sql = "INSERT INTO urls (address) VALUES (?)";
        try (var conn = dataConfig.getConnection();
             var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, url.getAddress());
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                url.setId(generatedKeys.getLong(1));
                url.setCreated(generatedKeys.getTimestamp(2));
            } else {
                throw new SQLException("Database have not returned an id or createdAt after saving an entity");
            }
            urls.add(url);
        }
    }

    @SneakyThrows
    public static Optional<UrlModel> findById(Long id) {
        String sql = "SELECT * FROM urls WHERE id = ?";
        try (var conn = dataConfig.getConnection();
             var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            var queryResult = preparedStatement.executeQuery();
            if (queryResult.next()) {
                var url = new UrlModel(queryResult.getString("address"));
                url.setCreated(queryResult.getTimestamp("created_at"));
                url.setId(id);
                return Optional.of(url);
            }
            return Optional.empty();
        }
    }

    public static List<UrlModel> getAll() throws SQLException {
        String sql = "SELECT * FROM urls;";
        try (var conn = dataConfig.getConnection();
            var preparedStatement = conn.prepareStatement(sql)) {
            var urlList = preparedStatement.executeQuery();
            var result = new ArrayList<UrlModel>();
            while (urlList.next()) {
                var url = new UrlModel(urlList.getString("address"));
                url.setId(urlList.getLong("id"));
                url.setCreated(urlList.getTimestamp("created_at"));
                result.add(url);
            }
            return result;
        }
    }

    public static List<UrlModel> findByName(String address) {
        return urls.stream()
                .filter(entity -> entity.getAddress().contains(address))
                .toList();
    }

    public static List<UrlModel> withLatestCheck() {
        var result = new ArrayList<UrlModel>();
        String sql = "SELECT DISTINCT ON (urls.id)" +
                "                    urls.id, " +
                "                    urls.address, " +
                "                    urls.created_at AS created, " +
                "                    UrlCheck.created_at AS last_check, " +
                "                    UrlCheck.statusCode  AS status_code" +
                "                FROM urls " +
                "                LEFT JOIN UrlCheck ON " +
                "                    (UrlCheck.urlId = urls.id)" +
                "                ORDER BY urls.created_at ASC;";
        try (var conn = dataConfig.getConnection();
            var preparedStatement = conn.prepareStatement(sql)) {
            var urlList = preparedStatement.executeQuery();
            while (urlList.next()) {
                var url = new UrlModel(urlList.getString("address"));
                url.setId(urlList.getLong("id"));
                url.setCreated(urlList.getTimestamp("created"));
                url.setLastCheckTime(urlList.getTimestamp("last_check"));
                url.setStatusCode(urlList.getInt("status_code"));
                result.add(url);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}
