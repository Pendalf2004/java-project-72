package hexlet.code.controller;

import hexlet.code.datatemplate.paths.ListPage;
import hexlet.code.datatemplate.paths.UrlPage;
import hexlet.code.model.UrlModel;
import hexlet.code.utils.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import lombok.SneakyThrows;
import hexlet.code.repository.CheckRepository;
import hexlet.code.repository.UrlRepository;

import java.net.URI;
import java.net.URISyntaxException;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlController {

    public static void showList(Context ctx) {
        var inputData = new ListPage(UrlRepository.withLatestCheck());
        ctx.render("paths/UrlList.jte", model("urlListData", inputData));
    }

    @SneakyThrows
    public static void showURL(Context ctx) {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.findById(id)
                    .orElseThrow(() -> new NotFoundResponse("No url with provided Id"));
        var checks = CheckRepository.findAllByUrlId(url.getId());
        var inputData = new UrlPage(url, checks);
        ctx.render("paths/urlDetails.jte", model("urlDetails", inputData));

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
