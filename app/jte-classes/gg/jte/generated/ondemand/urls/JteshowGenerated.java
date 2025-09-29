package gg.jte.generated.ondemand.urls;
import hexlet.code.Url;
import hexlet.code.UrlCheck;
import java.util.List;
import java.time.format.DateTimeFormatter;
@SuppressWarnings("unchecked")
public final class JteshowGenerated {
	public static final String JTE_NAME = "urls/show.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,3,5,5,5,5,12,12,12,12,19,19,19,20,20,20,21,21,21,36,36,38,38,38,39,39,39,40,40,40,41,41,41,42,42,42,43,43,43,45,45,49,50,50,50,50,58,58,58,5,6,6,6,6};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, Url url, List<UrlCheck> checks) {
		jteOutput.writeContent("\r\n<!DOCTYPE html>\r\n<html lang=\"ru\">\r\n<head>\r\n    <meta charset=\"UTF-8\">\r\n    <title>");
		jteOutput.setContext("title", null);
		jteOutput.writeUserContent(url.getName());
		jteOutput.writeContent("</title>\r\n    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\">\r\n</head>\r\n<body class=\"container mt-5\">\r\n\r\n<h1>Информация о URL</h1>\r\n\r\n<p><b>ID:</b> ");
		jteOutput.setContext("p", null);
		jteOutput.writeUserContent(url.getId());
		jteOutput.writeContent("</p>\r\n<p><b>Имя:</b> ");
		jteOutput.setContext("p", null);
		jteOutput.writeUserContent(url.getName());
		jteOutput.writeContent("</p>\r\n<p><b>Дата добавления:</b> ");
		jteOutput.setContext("p", null);
		jteOutput.writeUserContent(url.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		jteOutput.writeContent("</p>\r\n\r\n<h2 class=\"mt-4\">Проверки сайта</h2>\r\n<table class=\"table table-bordered\">\r\n    <thead>\r\n    <tr>\r\n        <th>ID</th>\r\n        <th>Статус-код</th>\r\n        <th>H1</th>\r\n        <th>Title</th>\r\n        <th>Description</th>\r\n        <th>Дата проверки</th>\r\n    </tr>\r\n    </thead>\r\n    <tbody>\r\n    ");
		for (UrlCheck check : checks) {
			jteOutput.writeContent("\r\n        <tr>\r\n            <td>");
			jteOutput.setContext("td", null);
			jteOutput.writeUserContent(check.getId());
			jteOutput.writeContent("</td>\r\n            <td>");
			jteOutput.setContext("td", null);
			jteOutput.writeUserContent(check.getStatusCode());
			jteOutput.writeContent("</td>\r\n            <td>");
			jteOutput.setContext("td", null);
			jteOutput.writeUserContent(check.getH1());
			jteOutput.writeContent("</td>\r\n            <td>");
			jteOutput.setContext("td", null);
			jteOutput.writeUserContent(check.getTitle());
			jteOutput.writeContent("</td>\r\n            <td>");
			jteOutput.setContext("td", null);
			jteOutput.writeUserContent(check.getDescription());
			jteOutput.writeContent("</td>\r\n            <td>");
			jteOutput.setContext("td", null);
			jteOutput.writeUserContent(check.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			jteOutput.writeContent("</td>\r\n        </tr>\r\n    ");
		}
		jteOutput.writeContent("\r\n    </tbody>\r\n</table>\r\n\r\n");
		jteOutput.writeContent("\r\n<form action=\"/urls/");
		jteOutput.setContext("form", "action");
		jteOutput.writeUserContent(url.getId());
		jteOutput.setContext("form", null);
		jteOutput.writeContent("/checks\" method=\"post\" class=\"mt-3\">\r\n    <button type=\"submit\" class=\"btn btn-primary\">Проверить сайт</button>\r\n</form>\r\n\r\n<a href=\"/urls\" class=\"btn btn-secondary mt-3\">Назад к списку сайтов</a>\r\n\r\n</body>\r\n</html>\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		Url url = (Url)params.get("url");
		List<UrlCheck> checks = (List<UrlCheck>)params.get("checks");
		render(jteOutput, jteHtmlInterceptor, url, checks);
	}
}
