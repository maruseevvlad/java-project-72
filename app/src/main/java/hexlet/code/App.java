package hexlet.code;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;

import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class App {

    // Один экземпляр репозитория на всё приложение
    private static final UrlRepository urlRepository = new UrlRepository(DataSourceProvider.getDataSource());

    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        return TemplateEngine.create(codeResolver, ContentType.Html);
    }

    public static Javalin getApp() {
        DbInitializer.init();

        return Javalin.create(config -> {
                    config.bundledPlugins.enableDevLogging();
                    config.fileRenderer(new JavalinJte(createTemplateEngine()));
                })
                .get("/", ctx -> {
                    List<Url> urls = urlRepository.findAll();
                    ctx.render("index.jte", Map.of("name", "Hexlet", "urls", urls));
                })
                .get("/urls", ctx -> {  // Вернём список URL
                    List<Url> urls = urlRepository.findAll();
                    ctx.render("index.jte", Map.of("name", "Hexlet", "urls", urls));
                })
                .get("/urls/{id}", ctx -> {
                    long id = Long.parseLong(ctx.pathParam("id"));
                    Url url = urlRepository.findById(id);

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

                        if (urlRepository.existsByName(normalizedUrl)) {
                            ctx.sessionAttribute("flash", "Страница уже существует");
                            ctx.redirect("/");
                            return;
                        }

                        Url url = new Url();
                        url.setName(normalizedUrl);
                        urlRepository.save(url);

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
