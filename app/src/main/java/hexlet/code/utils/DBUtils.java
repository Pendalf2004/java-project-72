package hexlet.code.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import hexlet.code.repository.BaseDB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class DBUtils {
    public static HikariDataSource dataSource;

    public static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.parseInt(port);
    }

    public static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = DBUtils.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates/jte", classLoader);
        TemplateEngine templateEngine = TemplateEngine.create(codeResolver, ContentType.Html);
        return templateEngine;
    }

    public static String getDbConfig() {
        return System.getenv().getOrDefault(
                "JDBC_DATABASE_URL",
                //"jdbc:postgresql://localhost/postgres?password=password&user=postgres");
                "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;");
    }

    public static void createDB() throws SQLException, IOException {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(getDbConfig());
        if (hikariConfig.getJdbcUrl().startsWith("jdbc:postgresql")) { //почему-то для postgre не
            // подгружаются драйвера автоматически
            hikariConfig.setDriverClassName(org.postgresql.Driver.class.getName());
        }
        dataSource = new HikariDataSource(hikariConfig);
        BaseDB.dataSource = dataSource;

        String query = "";
        try (var queryFile = ClassLoader.getSystemClassLoader().getResourceAsStream("schema.sql")) {
            query = new BufferedReader(new InputStreamReader(queryFile))
                    .lines()
                    .collect(Collectors.joining("\n"));
        } finally {
            try (var connection = dataSource.getConnection();
                 var statement = connection.createStatement()) {
                statement.execute(query);
            }
        }
    }
}
