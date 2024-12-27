package hexlet.code;

import hexlet.code.controller.ChecksController;
import hexlet.code.controller.RootController;
import hexlet.code.controller.UrlController;
import hexlet.code.repository.BaseDB;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;

import hexlet.code.utils.DBUtils;
import hexlet.code.utils.NamedRoutes;


public class App {

    @SneakyThrows
    public static void main(String[] args) {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(DBUtils.getDbConfig());

        var dataSource = new HikariDataSource(hikariConfig);
        BaseDB.dataConfig = dataSource;

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
