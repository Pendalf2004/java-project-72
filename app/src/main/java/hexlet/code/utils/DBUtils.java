package hexlet.code.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import hexlet.code.repository.UrlRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class DBUtils {
    public static HikariDataSource dataConfig;

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

    public static void createDB() throws SQLException, IOException {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(getDbConfig());
        if (hikariConfig.getJdbcUrl().startsWith("jdbc:postgresql")) { //почему-то для postgre не
            // подгружаются драйвера автоматически
            hikariConfig.setDriverClassName(org.postgresql.Driver.class.getName());
        }
        dataConfig = new HikariDataSource(hikariConfig);
        String query = "";
        try (var queryFile = ClassLoader.getSystemClassLoader().getResourceAsStream("schema.sql")) {
            query = new BufferedReader(new InputStreamReader(queryFile))
                    .lines()
                    .collect(Collectors.joining("\n"));
        } finally {
            try (var connection = dataConfig.getConnection();
                 var statement = connection.createStatement()) {
                statement.execute(query);
            } finally {
                UrlRepository.dataConfig = dataConfig;
            }
        }
    }

    public static String getDbConfig() {
        return System.getenv().getOrDefault(
                "JDBC_DATABASE_URL",
                "jdbc:postgresql://localhost/postgres?password=password&user=postgres");
                //"jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;");
    }

}
