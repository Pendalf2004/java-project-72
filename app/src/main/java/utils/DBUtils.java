package utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import repository.UrlRepository;

import java.io.*;
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
        if (!hikariConfig.getJdbcUrl().startsWith("jdbc:h2")) {
            hikariConfig.setDriverClassName(org.postgresql.Driver.class.getName());
        }
        dataConfig = new HikariDataSource(hikariConfig);
        String query = "";
        try (var queryFile = ClassLoader.getSystemClassLoader().getResourceAsStream("DBCreationQuery")) {
            query = new BufferedReader(new InputStreamReader(queryFile))
                .lines().collect(Collectors.joining("\n"));}
        finally {
            try (var connection = dataConfig.getConnection();
                 var statement = connection.createStatement()) {
                statement.execute(query);
            }
            finally {
                UrlRepository.dataConfig = dataConfig;
                //CheckRepository.dataConfig = dataConfig;
        }

        }
    }

    public static String getDbConfig() {
        return System.getenv().getOrDefault(
                "JDBC_DATABASE_URL", "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;");
    }

}
