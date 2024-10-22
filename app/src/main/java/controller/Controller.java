package controller;

import DataTemplate.BasePage;
import io.javalin.http.Context;
import model.URLDC;
import repository.URLS;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static io.javalin.rendering.template.TemplateUtil.model;

public class Controller {

    public static void showRoot(Context ctx) {
        var page = new BasePage();
        ctx.render("index.jte", model("template", page));
    }

    public static void getNewURL (Context ctx) {
        String address;
        String message;
        try {
            URI uriAddress = new URI(ctx.formParam("url"));
            var urlAddress = uriAddress.toURL();
            address = urlAddress.toString();
        } catch (URISyntaxException | MalformedURLException | NullPointerException e) {
            message = "Некорректный URL";
        }

       // URLS.add(url);
        ctx.redirect("/");
    }
}
