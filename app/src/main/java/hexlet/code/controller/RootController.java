package hexlet.code.controller;

import hexlet.code.datatemplate.paths.RootPage;
import io.javalin.http.Context;
import lombok.SneakyThrows;
import hexlet.code.repository.UrlRepository;
import hexlet.code.utils.NamedRoutes;
import hexlet.code.model.UrlModel;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

import static io.javalin.rendering.template.TemplateUtil.model;

public class RootController {

    public static void showRoot(Context ctx) throws SQLException {
        var inputData = new RootPage(UrlRepository.getAll());
        inputData.setMsg(ctx.consumeSessionAttribute("msg"));
        ctx.render("index.jte", model("data", inputData));
    }

    @SneakyThrows
    public static void getNewURL(Context ctx) {
        String message = "";
        try {
            if (ctx.formParam("url").isEmpty() || ctx.formParam("url").isEmpty()) {
                throw new NullPointerException();
            }
            URI uriPath = new URI(ctx.formParam("url"));
            String urlPath = uriPath.getScheme() + "://"
                    + uriPath.getAuthority();
            if (UrlRepository.findByName(urlPath).isEmpty()) {
                UrlRepository.addURL(new UrlModel(urlPath));
                message = "Страница успешно добавлена";
            } else {
                message = "Страница уже существует";
            }

        } catch (URISyntaxException | NullPointerException | IllegalArgumentException e) {
            message = "Некорректный URL";
        } finally {
            ctx.sessionAttribute("msg", message);
            ctx.redirect(NamedRoutes.root());
        }
    }

}
