package hexlet.code;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import kong.unirest.Unirest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.net.URI;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class App {

    private static final UrlRepository urlRepository = new UrlRepository(DataSourceProvider.getDataSource());
    private static final UrlCheckRepository checkRepo = new UrlCheckRepository();

    private static TemplateEngine createTemplateEngine() {
        ResourceCodeResolver resolver = new ResourceCodeResolver("templates", App.class.getClassLoader());
        return TemplateEngine.create(resolver, ContentType.Html);
    }

    public static Javalin getApp() {
        DbInitializer.init();

        return Javalin.create(config -> {
                    config.bundledPlugins.enableDevLogging();
                    config.fileRenderer(new JavalinJte(createTemplateEngine()));
                })
                // Главная страница и список сайтов объединены
                .get("/", App::renderUrls)
                .get("/urls", ctx -> ctx.redirect("/")) // редирект на /
                // Страница конкретного сайта с проверками
                .get("/urls/{id}", ctx -> {
                    long id = Long.parseLong(ctx.pathParam("id"));
                    Url url = urlRepository.findById(id);
                    if (url == null) {
                        ctx.status(404).result("URL не найден");
                        return;
                    }
                    List<UrlCheck> checks = checkRepo.findAllByUrlId(id);
                    ctx.render("urls/show.jte", Map.of("url", url, "checks", checks));
                })

                .post("/urls", ctx -> {
                    String urlValue = ctx.formParam("url");
                    try {
                        URI uri = new URI(urlValue);
                        URL parsedUrl = uri.toURL();
                        String normalizedUrl = parsedUrl.getPort() == -1
                                ? parsedUrl.getProtocol() + "://" + parsedUrl.getHost()
                                : parsedUrl.getProtocol() + "://" + parsedUrl.getHost() + ":" + parsedUrl.getPort();

                        if (urlRepository.existsByName(normalizedUrl)) {
                            ctx.sessionAttribute("flash", "Страница уже существует");
                            ctx.redirect("/");
                            return;
                        }

                        Url url = new Url();
                        url.setName(normalizedUrl);
                        urlRepository.save(url);

                        ctx.sessionAttribute("flash", "Страница успешно добавлена");
                    } catch (Exception e) {
                        ctx.sessionAttribute("flash", "Некорректный URL");
                    }
                    ctx.redirect("/");
                })

                .post("/urls/{id}/checks", ctx -> {
                    long id = Long.parseLong(ctx.pathParam("id"));
                    Url url = urlRepository.findById(id);
                    if (url == null) {
                        ctx.status(404).result("URL не найден");
                        return;
                    }

                    try {
                        var response = Unirest.get(url.getName()).asString();
                        var doc = Jsoup.parse(response.getBody());

                        UrlCheck check = new UrlCheck();
                        check.setUrlId(url.getId());
                        check.setStatusCode(response.getStatus());
                        check.setTitle(doc.title());

                        Element h1 = doc.selectFirst("h1");
                        check.setH1(h1 != null ? h1.text() : null);

                        Element desc = doc.selectFirst("meta[name=description]");
                        check.setDescription(desc != null ? desc.attr("content") : null);

                        check.setCreatedAt(LocalDateTime.now());
                        checkRepo.save(check);

                        ctx.sessionAttribute("flash", "Проверка выполнена!");
                    } catch (Exception e) {
                        ctx.sessionAttribute("flash", "Ошибка при проверке");
                    }

                    ctx.redirect("/urls/" + id);
                });
    }

    private static void renderUrls(io.javalin.http.Context ctx) throws SQLException {
        List<Url> urls = urlRepository.findAll();
        List<UrlDto> urlDtos = new ArrayList<>();
        for (Url url : urls) {
            UrlCheck lastCheck = checkRepo.findLastByUrlId(url.getId());
            urlDtos.add(new UrlDto(url, lastCheck));
        }
        ctx.render("index.jte", Map.of("urls", urlDtos));
    }

    public static void main(String[] args) {
        Javalin app = getApp();
        app.start(7070);
    }
}
