package hexlet.code.controller;

import hexlet.code.datatemplate.paths.RootPage;
import io.javalin.http.Context;
import hexlet.code.repository.UrlRepository;

import java.sql.SQLException;

import static io.javalin.rendering.template.TemplateUtil.model;

public class RootController {

    public static void showRoot(Context ctx) throws SQLException {
        var inputData = new RootPage(UrlRepository.getAll());
        inputData.setMsg(ctx.consumeSessionAttribute("msg"));
        ctx.render("index.jte", model("data", inputData));
    }

}
