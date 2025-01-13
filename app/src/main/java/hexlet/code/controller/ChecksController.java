package hexlet.code.controller;

import io.javalin.http.Context;

import io.javalin.http.NotFoundResponse;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import hexlet.code.model.CheckModel;
import hexlet.code.model.UrlModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import hexlet.code.repository.CheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.utils.NamedRoutes;

public class ChecksController {

    public static void check(Context ctx) {
        var urlId = ctx.pathParamAsClass("id", Long.class).get();
        UrlModel url = UrlRepository.findById(urlId)
                .orElseThrow(() -> new NotFoundResponse("No such url"));
        var check = new CheckModel(urlId);
        try {
            var urlPath = url.getName();
            HttpResponse<String> response = Unirest.get(urlPath).asString();
            int statusCode = response.getStatus();
            var responseBody = response.getBody();
            check.setStatusCode(statusCode);
            Document document = Jsoup.parse(responseBody);
            check.setTitle(document.title());
            check.setH1(document.select("h1").text());
            var description = document.select("meta").attr("content") == null ? ""
                    : document.select("meta").attr("content");
            check.setDescription(description);
            CheckRepository.addCheck(check);
        } catch (Exception e) {
            ctx.redirect(NamedRoutes.urlList());
        } finally {
            ctx.redirect(NamedRoutes.urlPath(urlId));
        }
    }
}
