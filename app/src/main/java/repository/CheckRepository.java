package repository;

import model.CheckModel;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CheckRepository extends BaseDB {
    protected static List<CheckModel> checks = new ArrayList<CheckModel>();

    public static void addCheck(CheckModel check) throws SQLException {
        String sql =
                "INSERT INTO UrlCheck (urlId, statusCode, title, h1, description) VALUES (?, ?, ?, ?, ?)";
        try (var conn = dataConfig.getConnection();
             var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, check.getUrlId());
            preparedStatement.setInt(2, check.getStatusCode());
            preparedStatement.setString(3, check.getTitle());
            preparedStatement.setString(4, check.getH1());
            preparedStatement.setString(5, check.getDescription());
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                check.setId(generatedKeys.getLong(1));
                if (dataConfig.getJdbcUrl().startsWith("jdbc:h2")) {
                    check.setCreatedAt(generatedKeys.getTimestamp(2));
                } else {
                    check.setCreatedAt(generatedKeys.getTimestamp(7));
                }

            } else {
                throw new SQLException("Database have not returned an id or createdAt after saving an entity");
            }
            checks.add(check);
        }
    }

    public static List<CheckModel> findAllByUrlId(Long urlId) throws SQLException {
        String query = "SELECT * FROM UrlCheck "
                + "WHERE urlId = " + urlId
                + "ORDER BY id DESC;";
        try (var conn = dataConfig.getConnection();
            var preparedStatement = conn.prepareStatement(query)) {
            var checksList = preparedStatement.executeQuery();
            var result = new ArrayList<CheckModel>();
            while (checksList.next()) {
                var check = new CheckModel(checksList.getLong("urlId"));
                check.setId(checksList.getLong("id"));
                check.setTitle(checksList.getString("title"));
                check.setH1(checksList.getString("h1"));
                check.setDescription(checksList.getString("description"));
                check.setStatusCode(checksList.getInt("statusCode"));
                check.setCreatedAt(checksList.getTimestamp("created_at"));
                result.add(check);
            }
            return result;
        }
    }

}
