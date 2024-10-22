import controller.Controller;
import gg.jte.CodeResolver;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.DirectoryCodeResolver;
import gg.jte.resolve.ResourceCodeResolver;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import lombok.SneakyThrows;
import utils.DB;
import utils.NamedRoutes;

import java.nio.file.Path;
import java.sql.SQLException;

public class App {

    @SneakyThrows
    public static void main(String[] args) {
        DB.createDB();
        var page = getApp();
        page.start(getPort());
    }

    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates/jte", classLoader);
        TemplateEngine templateEngine = TemplateEngine.create(codeResolver, ContentType.Html);
        return templateEngine;
    }

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.valueOf(port);
    }

    public static Javalin getApp() {
        var renderPage = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
        });

        renderPage.get(NamedRoutes.root(), Controller::showRoot);
        renderPage.post(NamedRoutes.root(), Controller::getNewURL);

        return renderPage;
    }
}
