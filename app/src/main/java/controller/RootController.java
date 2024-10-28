package controller;

import datatemplate.paths.RootPage;
import io.javalin.http.Context;
import lombok.SneakyThrows;
import model.UrlModel;
import repository.URLRepository;
import utils.NamedRoutes;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;

import static io.javalin.rendering.template.TemplateUtil.model;

public class RootController {

    public static void showRoot(Context ctx) throws SQLException {
        var inputData = new RootPage(URLRepository.getAll());
        inputData.setMsg(ctx.consumeSessionAttribute("msg"));
        ctx.render("index.jte", model("data", inputData));
    }

    @SneakyThrows
    public static void getNewURL(Context ctx) {
        String address;
        String message = "";
        try {
            URI uriPath = new URI(ctx.formParam("url"));
            URL urlAddress = uriPath.toURL();
            String urlPath = uriPath.getScheme() + "://"
                    + uriPath.getAuthority();
            if (URLRepository.findByName(urlPath).isEmpty()) {
                URLRepository.addURL(new UrlModel(urlPath));
                message = "Страница успешно добавлена";
            } else {
                message = "Страница уже существует";
            }

        } catch (URISyntaxException | MalformedURLException | NullPointerException | IllegalArgumentException e) {
            message = "Некорректный URL";
        } finally {
            ctx.sessionAttribute("msg", message);
            ctx.redirect(NamedRoutes.root());
        }
    }
}
