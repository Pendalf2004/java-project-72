package controller;

import datatemplate.paths.ListPage;
import datatemplate.paths.UrlPage;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import repository.UrlRepository;


import java.sql.SQLException;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlController {

    public static void showList(Context ctx) throws SQLException {
        var inputData = new ListPage(UrlRepository.withLatestCheck());
        ctx.render("paths/UrlList.jte", model("urlListData", inputData));
    }

    public static void showURL(Context ctx) {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.findById(id)
                    .orElseThrow(() -> new NotFoundResponse("No url with provided Id"));
        var inputData = new UrlPage(url);
        ctx.render("paths/urlDetails.jte", model("urlDetails", inputData));

    }

}
