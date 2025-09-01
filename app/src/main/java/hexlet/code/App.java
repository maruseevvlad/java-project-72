package hexlet.code;

import io.javalin.Javalin;

public class App {
    public static Javalin getApp() {
        return Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
        }).get("/", ctx -> ctx.result("Hello World"));
    }

    public static void main(String[] args) {
        Javalin app = getApp();
        app.start(7070);
    }
    //Подъем с properdonom
}
