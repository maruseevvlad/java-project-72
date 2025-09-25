package gg.jte.generated.ondemand.urls;
import hexlet.code.Url;
@SuppressWarnings("unchecked")
public final class JteshowGenerated {
	public static final String JTE_NAME = "urls/show.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,1,1,1,7,7,7,7,14,14,14,15,15,15,16,16,16,22,22,22,1,1,1,1};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, Url url) {
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
		jteOutput.writeUserContent(url.getCreatedAt().toString());
		jteOutput.writeContent("</p>\r\n\r\n<a href=\"/\" class=\"btn btn-secondary mt-3\">Назад</a>\r\n\r\n</body>\r\n</html>\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		Url url = (Url)params.get("url");
		render(jteOutput, jteHtmlInterceptor, url);
	}
}
