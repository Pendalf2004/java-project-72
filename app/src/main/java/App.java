import controller.RootController;
import controller.URLController;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import lombok.SneakyThrows;
import repository.URLRepository;
import utils.NamedRoutes;

public class App {

    @SneakyThrows
    public static void main(String[] args) {
        URLRepository.createDB();
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
        return Integer.parseInt(port);
    }

    @SneakyThrows
    public static Javalin getApp() {
        var renderPage = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
        });
        URLRepository.createDB();

        renderPage.get(NamedRoutes.root(), RootController::showRoot);
        renderPage.post(NamedRoutes.root(), RootController::getNewURL);
        renderPage.get(NamedRoutes.urlList(), URLController::showList);
        renderPage.get(NamedRoutes.urlPath("{id}"), URLController::showURL);
        return renderPage;
    }
}
