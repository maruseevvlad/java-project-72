package gg.jte.generated.ondemand;
import hexlet.code.UrlDto;
import java.time.format.DateTimeFormatter;
@SuppressWarnings("unchecked")
public final class JteindexGenerated {
	public static final String JTE_NAME = "index.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,2,2,2,25,25,25,27,27,27,27,27,27,27,28,28,30,30,30,31,31,31,34,34,37,37,42,42,42,2,2,2,2};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.List<UrlDto> urls) {
		jteOutput.writeContent("\r\n<!DOCTYPE html>\r\n<html lang=\"ru\">\r\n<head>\r\n    <meta charset=\"UTF-8\">\r\n    <title>Сохранение адреса</title>\r\n    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\">\r\n</head>\r\n<body class=\"container mt-5\">\r\n\r\n<h1 class=\"mb-4\">Сохранение адреса</h1>\r\n\r\n<form action=\"/urls\" method=\"post\" class=\"mb-3\">\r\n    <div class=\"mb-3\">\r\n        <label for=\"urlInput\" class=\"form-label\">Введите адрес</label>\r\n        <input type=\"text\" class=\"form-control\" id=\"urlInput\" name=\"url\" required>\r\n    </div>\r\n    <button type=\"submit\" class=\"btn btn-primary\">Сохранить</button>\r\n</form>\r\n\r\n<h2 class=\"mt-4\">Сохранённые адреса</h2>\r\n<ul class=\"list-group\">\r\n    ");
		for (hexlet.code.UrlDto urlDto : urls) {
			jteOutput.writeContent("\r\n        <li class=\"list-group-item\">\r\n            <a href=\"/urls/");
			jteOutput.setContext("a", "href");
			jteOutput.writeUserContent(urlDto.getId());
			jteOutput.setContext("a", null);
			jteOutput.writeContent("\">");
			jteOutput.setContext("a", null);
			jteOutput.writeUserContent(urlDto.getName());
			jteOutput.writeContent("</a>\r\n            ");
			if (urlDto.getLastCheck() != null) {
				jteOutput.writeContent("\r\n                <span class=\"text-muted\">\r\n                Последняя проверка: ");
				jteOutput.setContext("span", null);
				jteOutput.writeUserContent(urlDto.getLastCheck().getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
				jteOutput.writeContent("\r\n                (код: ");
				jteOutput.setContext("span", null);
				jteOutput.writeUserContent(urlDto.getLastCheck().getStatusCode());
				jteOutput.writeContent(")\r\n            </span>\r\n\r\n            ");
			}
			jteOutput.writeContent("\r\n        </li>\r\n    <br>\r\n    ");
		}
		jteOutput.writeContent("\r\n</ul>\r\n\r\n</body>\r\n</html>\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		java.util.List<UrlDto> urls = (java.util.List<UrlDto>)params.get("urls");
		render(jteOutput, jteHtmlInterceptor, urls);
	}
}
