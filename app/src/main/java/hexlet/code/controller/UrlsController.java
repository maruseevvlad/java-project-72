package hexlet.code.controller;

import hexlet.code.dto.UrlPage;
import hexlet.code.dto.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.Constants;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.net.URI;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static io.javalin.rendering.template.TemplateUtil.model;


public class UrlsController {
    public static void create(Context ctx) throws SQLException {
        String inputUrl = ctx.formParam("url").trim().toLowerCase();
        URL url;
        try {
            URI uri = new URI(inputUrl);
            url = uri.toURL();
        } catch (Exception e) {
            ctx.sessionAttribute(Constants.FLASH, "Некорректный URL");
            ctx.sessionAttribute(Constants.FLASH_TYPE, "danger");
            ctx.redirect(NamedRoutes.rootPath());
            return;
        }
        String parsedUrl = String.format("%s://%s%s",
                url.getProtocol(),
                url.getHost(),
                url.getPort() == -1 ? "" : ":" + url.getPort()
        );

        if (UrlRepository.find(parsedUrl).isEmpty()) {
            Url newUrl = new Url(parsedUrl);
            UrlRepository.save(newUrl);
            ctx.sessionAttribute(Constants.FLASH, "Страница успешно добавлена");
            ctx.sessionAttribute(Constants.FLASH_TYPE, "success");
            ctx.redirect(NamedRoutes.urlsPath());
        } else {
            ctx.sessionAttribute(Constants.FLASH, "Страница уже существует");
            ctx.sessionAttribute(Constants.FLASH_TYPE, "info");
            ctx.redirect(NamedRoutes.rootPath());
        }
    }

    public static void index(Context ctx) throws SQLException {
        List<Url> urls = UrlRepository.getEntities();
        Map<Long, UrlCheck> lastChecks = UrlCheckRepository.findLastChecks();
        UrlsPage page = new UrlsPage(urls, lastChecks);
        page.setFlash(ctx.consumeSessionAttribute(Constants.FLASH));
        page.setFlashType(ctx.consumeSessionAttribute(Constants.FLASH_TYPE));
        ctx.render("urls/index.jte", model("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id).orElseThrow(() -> new NotFoundResponse("Page not found"));
        var urlCheck = UrlCheckRepository.find(id);
        var page = new UrlPage(url, urlCheck);
        page.setFlash(ctx.consumeSessionAttribute(Constants.FLASH));
        page.setFlashType(ctx.consumeSessionAttribute(Constants.FLASH_TYPE));
        ctx.render("urls/show.jte", model("page", page));
    }

    public static void check(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        Url url = UrlRepository.find(id).orElseThrow(() -> new NotFoundResponse("Url not found"));
        try {
            HttpResponse<String> response = Unirest.get(url.getName()).asString();
            var doc = Jsoup.parse(response.getBody());

            int statusCode  = response.getStatus();
            String title = doc.title();

            Element elemH1 = doc.selectFirst("h1");
            String h1 = elemH1 == null ? "" : elemH1.text();

            Element elemMeta = doc.selectFirst("meta[name=description]");
            String meta = elemMeta == null ? "" : elemMeta.attr("content");

            UrlCheck urlCheck = new UrlCheck(statusCode, title, h1, meta, id);
            UrlCheckRepository.save(urlCheck);
            ctx.sessionAttribute(Constants.FLASH, "Страница успешно проверена");
            ctx.sessionAttribute(Constants.FLASH_TYPE, "success");
            ctx.redirect(NamedRoutes.urlPath(id));
        } catch (Exception e) {
            ctx.sessionAttribute(Constants.FLASH, "Некорректный адрес");
            ctx.sessionAttribute(Constants.FLASH_TYPE, "danger");
            ctx.redirect(NamedRoutes.urlPath(id));
        }
    }
}
