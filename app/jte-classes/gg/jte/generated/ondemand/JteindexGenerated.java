package gg.jte.generated.ondemand;
import hexlet.code.UrlDto;
import java.util.List;
import java.time.format.DateTimeFormatter;
@SuppressWarnings("unchecked")
public final class JteindexGenerated {
	public static final String JTE_NAME = "index.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,4,4,4,4,18,18,18,21,21,21,21,21,21,21,22,22,22,25,25,26,26,26,27,27,27,28,28,31,31,32,34,34,40,40,40,4,5,5,5,5};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, String name, java.util.List<UrlDto> urls) {
		jteOutput.writeContent("\r\n<!doctype html>\r\n<html lang=\"ru\">\r\n<head>\r\n    <meta charset=\"utf-8\" />\r\n    <title>Сайты</title>\r\n    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\">\r\n</head>\r\n<body class=\"container mt-5\">\r\n<h1>Добавленные адреса</h1>\r\n\r\n<ul class=\"list-group mt-3\">\r\n    ");
		for (UrlDto dto : urls) {
			jteOutput.writeContent(" {\r\n        <li class=\"list-group-item d-flex justify-content-between align-items-center\">\r\n            <div>\r\n                <a href=\"/urls/");
			jteOutput.setContext("a", "href");
			jteOutput.writeUserContent(dto.getId());
			jteOutput.setContext("a", null);
			jteOutput.writeContent("\">");
			jteOutput.setContext("a", null);
			jteOutput.writeUserContent(dto.getName());
			jteOutput.writeContent("</a>\r\n                <div class=\"text-muted small\">Добавлено: ");
			jteOutput.setContext("div", null);
			jteOutput.writeUserContent(dto.getCreatedAt() != null ? dto.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "");
			jteOutput.writeContent("</div>\r\n            </div>\r\n            <div>\r\n                ");
			if (dto.getLastCheck() != null) {
				jteOutput.writeContent(" {\r\n                    <div>Последняя проверка: ");
				jteOutput.setContext("div", null);
				jteOutput.writeUserContent(dto.getLastCheck().getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
				jteOutput.writeContent("</div>\r\n                    <div>Код: ");
				jteOutput.setContext("div", null);
				jteOutput.writeUserContent(dto.getLastCheck().getStatusCode());
				jteOutput.writeContent("</div>\r\n                    } ");
			} else {
				jteOutput.writeContent(" {\r\n                    <div class=\"text-muted\">Проверок ещё нет</div>\r\n                    }\r\n                ");
			}
			jteOutput.writeContent("\r\n            </div> ");
			jteOutput.writeContent("\r\n        </li>\r\n    ");
		}
		jteOutput.writeContent("\r\n</ul>\r\n\r\n<a href=\"/\" class=\"btn btn-link mt-3\">Добавить новый</a>\r\n</body>\r\n</html>\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		String name = (String)params.get("name");
		java.util.List<UrlDto> urls = (java.util.List<UrlDto>)params.get("urls");
		render(jteOutput, jteHtmlInterceptor, name, urls);
	}
}
