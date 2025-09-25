package hexlet.code;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import org.jsoup.Jsoup;

import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import kong.unirest.Unirest;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class App {

    // Один экземпляр репозитория на всё приложение
    private static final UrlRepository urlRepository = new UrlRepository(DataSourceProvider.getDataSource());
    private static final UrlCheckRepository checkRepo = new UrlCheckRepository();


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
                })
                .post("/urls/{id}/checks", ctx -> {
                    Long urlId = Long.parseLong(ctx.pathParam("id"));
                    Url url = urlRepository.findById(urlId);
                    if (url == null) {
                        ctx.status(404).result("URL не найден");
                        return;
                    }

                    HttpResponse<String> response = Unirest.get(url.getName()).asString();

                    UrlCheck check = new UrlCheck();
                    check.setUrlId(urlId);
                    check.setStatusCode(response.getStatus());

                    Document doc = Jsoup.parse(response.getBody());
                    check.setTitle(doc.title());
                    Element h1 = doc.selectFirst("h1");
                    check.setH1(h1 != null ? h1.text() : "");
                    Element desc = doc.selectFirst("meta[name=description]");
                    check.setDescription(desc != null ? desc.attr("content") : "");
                    check.setCreatedAt(LocalDateTime.now());

                    checkRepo.save(check);
                    ctx.redirect("/urls/" + urlId);
                });
    }

    public static void main(String[] args) {
        Javalin app = getApp();
        app.start(7070);
    }

}
