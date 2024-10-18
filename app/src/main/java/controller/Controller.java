package controller;

import io.javalin.http.Context;

public class Controller {
    public static void showRoot(Context ctx) {
        ctx.render("index.jte");
    }
}
