package utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.SQLException;

public class DB {

    public static String getDbConfig() {
        return System.getenv().getOrDefault(
                "JDBC_DATABASE_URL", "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;");
    }


    public void createDB() throws SQLException {

        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(getDbConfig());

        var dataSource = new HikariDataSource(hikariConfig);
        var createURLTable =
                "CREATE TABLE users " +
                "(id        BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                "name       VARCHAR(255), " +
                "created_at TIMESTAMP NOT NULL DEFAULT NOW());";

        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(createURLTable);
        }


    }
}
