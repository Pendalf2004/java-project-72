import Controller.Controller;
import gg.jte.CodeResolver;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.DirectoryCodeResolver;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import utils.NamedRoutes;

import java.nio.file.Path;

public class App {

    public static void main(String[] args) {
        var page = getApp();
        page.start();
    }

    private static TemplateEngine createTemplateEngine() {
        CodeResolver codeResolver =
                new DirectoryCodeResolver(Path.of(System.getProperty("user.dir") + "/app/src/main/java/jte/"));
        TemplateEngine templateEngine = TemplateEngine.create(codeResolver, ContentType.Html);
        return templateEngine;
    }

    public static Javalin getApp() {
        var renderPage = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
        });


        renderPage.get(NamedRoutes.root(), Controller::showRoot);

        return renderPage;
    }
}
