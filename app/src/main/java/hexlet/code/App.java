package hexlet.code;

import hexlet.code.controller.ChecksController;
import hexlet.code.controller.RootController;
import hexlet.code.controller.UrlController;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import lombok.SneakyThrows;

import hexlet.code.utils.DBUtils;
import hexlet.code.utils.NamedRoutes;


public class App {

    @SneakyThrows
    public static void main(String[] args) {
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
        renderPage.post(NamedRoutes.root(), RootController::getNewURL);

        renderPage.get(NamedRoutes.urlList(), UrlController::showList);
        renderPage.get(NamedRoutes.urlPath("{id}"), UrlController::showURL);

        renderPage.post(NamedRoutes.checkPath("{id}"), ChecksController::check);
        return renderPage;
    }
}
