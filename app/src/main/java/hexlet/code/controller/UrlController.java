package hexlet.code.controller;

import hexlet.code.datatemplate.paths.ListPage;
import hexlet.code.datatemplate.paths.UrlPage;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import lombok.SneakyThrows;
import hexlet.code.CheckRepository;
import hexlet.code.UrlRepository;

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

}
