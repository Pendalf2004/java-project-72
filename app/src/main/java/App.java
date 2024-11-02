import controller.ChecksController;
import controller.RootController;
import controller.UrlController;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import lombok.SneakyThrows;
import utils.DBUtils;
import utils.NamedRoutes;

public class App {

    @SneakyThrows
    public static void main(String[] args) {
        DBUtils.createDB();
        var page = getApp();
        page.start(DBUtils.getPort());
    }


    @SneakyThrows
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
