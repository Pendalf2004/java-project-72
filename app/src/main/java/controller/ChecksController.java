package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.javalin.http.Context;

import io.javalin.http.NotFoundResponse;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import model.CheckModel;
import model.UrlModel;
import repository.CheckRepository;
import repository.UrlRepository;
import utils.NamedRoutes;

import java.sql.SQLException;


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
            CheckRepository.addCheck(check);

/*
            Document document = Jsoup.parse(responseBody);
            String title = document.title();
            String firstH1 = document.select("h1").text();
            String description = document.select("meta[name=description]").attr("content");
            CheckModel urlCheck = new CheckModel();
            urlCheck.setStatusCode(statusCode);
            urlCheck.setH1(firstH1);
            urlCheck.setTitle(title);
            urlCheck.setDescription(description);
            UrlCheckRepository.save(urlCheck, url);
            ctx.sessionAttribute("flash", "Страница успешно проверена");
            ctx.sessionAttribute("flashType", "alert-success");
            ctx.redirect(NamedRoutes.urlPath(urlId));*/
        } catch (UnirestException | SQLException e) {
            throw new UnirestException("Error during URL check: " + e.getMessage(), e);
        }
        finally {
            ctx.redirect(NamedRoutes.urlPath(urlId));
        }
    }
}
