package hexlet.code;

import hexlet.code.controller.ChecksController;
import hexlet.code.controller.RootController;
import hexlet.code.controller.UrlController;
import hexlet.code.repository.BaseDB;
import hexlet.code.repository.UrlRepository;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import hexlet.code.utils.DBUtils;
import hexlet.code.utils.NamedRoutes;

import java.io.IOException;
import java.sql.SQLException;


public class App {
    public static void main(String[] args) throws SQLException, IOException {
        var hikariConfig = new HikariConfig();


        hikariConfig.setJdbcUrl(DBUtils.getDbConfig());

        var dataConfig = new HikariDataSource(hikariConfig);
        BaseDB.dataConfig = dataConfig;
        UrlRepository.dataConfig = dataConfig;

        DBUtils.createDB();
        var page = getApp();
        page.start(DBUtils.getPort());
    }

    public static Javalin getApp() {

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
