package gg.jte.generated.ondemand;
import hexlet.code.Url;
import java.time.format.DateTimeFormatter;
@SuppressWarnings("unchecked")
public final class JteindexGenerated {
	public static final String JTE_NAME = "index.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,2,2,2,27,27,27,29,29,29,29,29,29,29,30,30,30,33,33,38,38,38,2,3,3,3,3};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, String name, java.util.List<Url> urls) {
		jteOutput.writeContent("\r\n\r\n<!DOCTYPE html>\r\n<html lang=\"ru\">\r\n<head>\r\n    <meta charset=\"UTF-8\">\r\n    <title>Сохранение адреса</title>\r\n    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\">\r\n</head>\r\n<body class=\"container mt-5\">\r\n\r\n<h1 class=\"mb-4\">Сохранение адреса</h1>\r\n\r\n<form action=\"/urls\" method=\"post\" class=\"mb-3\">\r\n    <div class=\"mb-3\">\r\n        <label for=\"urlInput\" class=\"form-label\">Введите адрес</label>\r\n        <input type=\"text\" class=\"form-control\" id=\"urlInput\" name=\"url\" required>\r\n    </div>\r\n    <button type=\"submit\" class=\"btn btn-primary\">Сохранить</button>\r\n</form>\r\n\r\n<h2 class=\"mt-4\">Сохранённые адреса</h2>\r\n<ul class=\"list-group\">\r\n    ");
		for (Url url : urls) {
			jteOutput.writeContent(" {\r\n    <li class=\"list-group-item\">\r\n        <a href=\"/urls/");
			jteOutput.setContext("a", "href");
			jteOutput.writeUserContent(url.getId());
			jteOutput.setContext("a", null);
			jteOutput.writeContent("\">");
			jteOutput.setContext("a", null);
			jteOutput.writeUserContent(url.getName());
			jteOutput.writeContent("</a>\r\n        <span class=\"text-muted\">(");
			jteOutput.setContext("span", null);
			jteOutput.writeUserContent(url.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			jteOutput.writeContent(")</span>\r\n    </li>\r\n    }\r\n    ");
		}
		jteOutput.writeContent("\r\n</ul>\r\n\r\n</body>\r\n</html>\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		String name = (String)params.get("name");
		java.util.List<Url> urls = (java.util.List<Url>)params.get("urls");
		render(jteOutput, jteHtmlInterceptor, name, urls);
	}
}
