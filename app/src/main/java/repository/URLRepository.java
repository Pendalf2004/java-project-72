package repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import model.UrlModel;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class URLRepository extends BaseDB{
    protected static List<UrlModel> URLS = new ArrayList<UrlModel>();

    @SneakyThrows
    public static void add(UrlModel url) {
        url.setId((long) URLS.size() + 1);
        Date date = new Date();
        url.setCreated(date);
        addURL(url);
    }

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
            URLS.add(url);
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
                urlList.getString("address");
                var url = new UrlModel(urlList.getString("address"));
                url.setId(urlList.getLong("id"));
                url.setCreated(urlList.getTimestamp("created_at"));
                result.add(url);
            }
            return result;
        }
    }

    public static void createDB() throws SQLException {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(getDbConfig());
        //hikariConfig.setDriverClassName(org.postgresql.Driver.class.getName());
        dataConfig = new HikariDataSource(hikariConfig);
        var createURLTable =
                "DROP TABLE IF EXISTS urls;" +
                        "CREATE TABLE urls " +
                        "(id        BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                        "address       VARCHAR(255), " +
                        "created_at TIMESTAMP NOT NULL DEFAULT NOW());";

        try (var connection = dataConfig.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(createURLTable);
        }
    }

    public static String getDbConfig() {
        return System.getenv().getOrDefault(
                "JDBC_DATABASE_URL", "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;");
    }

    public static List<UrlModel> findByName(String address) {
        return URLS.stream()
                .filter(entity -> entity.getAddress().contains(address))
                .toList();
    }

}

