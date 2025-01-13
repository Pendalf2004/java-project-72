package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.controller.ChecksController;
import hexlet.code.controller.RootController;
import hexlet.code.controller.UrlController;
import hexlet.code.repository.BaseDB;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import hexlet.code.utils.DBUtils;
import hexlet.code.utils.NamedRoutes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.stream.Collectors;

import static hexlet.code.utils.DBUtils.getDbConfig;


public class App {
    public static HikariDataSource dataSource;

    public static void main(String[] args) throws SQLException, IOException {
        var page = getApp();
        page.start(DBUtils.getPort());
    }

    public static Javalin getApp() throws SQLException, IOException {
//        DBUtils.createDB();
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

        var renderPage = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(DBUtils.createTemplateEngine()));
        });

        renderPage.get(NamedRoutes.root(), RootController::showRoot);
        renderPage.post(NamedRoutes.urlList(), UrlController::getNewURL);

        renderPage.get(NamedRoutes.urlList(), UrlController::showList);
        renderPage.get(NamedRoutes.urlPath("{id}"), UrlController::showURL);

        renderPage.post(NamedRoutes.checkPath("{id}"), ChecksController::check);
        return renderPage;
    }
}
