package controller;

import io.javalin.http.Context;

import io.javalin.http.NotFoundResponse;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import model.CheckModel;
import model.UrlModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import repository.CheckRepository;
import repository.UrlRepository;
import utils.NamedRoutes;

public class ChecksController {

    public static void check(Context ctx) {
        var urlId = ctx.pathParamAsClass("id", Long.class).get();
        UrlModel url = UrlRepository.findById(urlId)
                .orElseThrow(() -> new NotFoundResponse("No such url"));
        var check = new CheckModel(urlId);
        try {
            HttpResponse<String> response = Unirest.get(url.getAddress()).asString();
            int statusCode = response.getStatus();
            var responseBody = response.getBody();
            check.setStatusCode(statusCode);
            Document document = Jsoup.parse(responseBody);
            check.setTitle(document.title());
            check.setH1(document.select("h1").text());
            check.setDescription(document.select("meta").attr("content"));
            CheckRepository.addCheck(check);
        } catch (Exception e) {
            throw new UnirestException(e.getMessage(), e);
        } finally {
            ctx.redirect(NamedRoutes.urlPath(urlId));
        }
    }
}
