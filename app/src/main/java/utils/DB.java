package utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import model.URLDC;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


public class DB {
    public static HikariDataSource dataConfig;

    @SneakyThrows
    public static String getDbConfig() {
        return System.getenv().getOrDefault(
                "JDBC_DATABASE_URL", "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;");
    }

    public static void createDB() throws SQLException {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(getDbConfig());
        dataConfig = new HikariDataSource(hikariConfig);
        var createURLTable =
                "CREATE TABLE urls " +
                "(id        BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                "address       VARCHAR(255), " +
                "created_at TIMESTAMP NOT NULL DEFAULT NOW());";

        try (var connection = dataConfig.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(createURLTable);
        }
    }

    public static void addURL(URLDC url) throws SQLException {
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
                throw new SQLException("DB have not returned an id or createdAt after saving an entity");
            }
        }
    }

    /*public static List<URLDC> getAll(String url) {
        String sql = "SELECT * FROM urls;";

    }*/
}
