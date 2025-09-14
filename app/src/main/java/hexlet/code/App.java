package hexlet.code;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;

import java.net.URI;
import java.net.URL;
import java.util.Map;

public class App {

    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        return TemplateEngine.create(codeResolver, ContentType.Html);
    }

    public static Javalin getApp() {
        return Javalin.create(config -> {
                    config.bundledPlugins.enableDevLogging();
                    config.fileRenderer(new JavalinJte(createTemplateEngine()));
                })
                .get("/", ctx -> ctx.render("index.jte"))
                .get("/urls", ctx -> ctx.render("urls.jte"))
                .get("/urls/{id}", ctx -> {
                    long id = Long.parseLong(ctx.pathParam("id"));
                    UrlRepository repo = new UrlRepository(DataSourceProvider.getDataSource());
                    Url url = repo.findById(id);

                    if (url == null) {
                        ctx.status(404).result("URL не найден");
                        return;
                    }

                    ctx.render("urls/show.jte", Map.of("url", url));
                })
                .post("/urls", ctx -> {
                    String urlValue = ctx.formParam("url");

                    try {
                        URI uri = new URI(urlValue);
                        URL parsedUrl = uri.toURL();

                        String protocol = parsedUrl.getProtocol();
                        String host = parsedUrl.getHost();
                        int port = parsedUrl.getPort();

                        String normalizedUrl = port == -1
                                ? protocol + "://" + host
                                : protocol + "://" + host + ":" + port;

                        UrlRepository repo = new UrlRepository(DataSourceProvider.getDataSource());

                        if (repo.existsByName(normalizedUrl)) {
                            ctx.sessionAttribute("flash", "Страница уже существует");
                            ctx.redirect("/");
                            return;
                        }

                        Url url = new Url();
                        url.setName(normalizedUrl);
                        repo.save(url);

                        ctx.sessionAttribute("flash", "Страница успешно добавлена");
                        ctx.redirect("/");

                    } catch (Exception e) {
                        ctx.sessionAttribute("flash", "Некорректный URL");
                        ctx.redirect("/");
                    }
                });
    }

    public static void main(String[] args) {
        Javalin app = getApp();
        app.start(7070);
    }

}
