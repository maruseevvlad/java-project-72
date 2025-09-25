import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import hexlet.code.App;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import io.javalin.testtools.JavalinTest;
import okhttp3.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class MainPageTest {

    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        return TemplateEngine.create(codeResolver, gg.jte.ContentType.Html);
    }

    @Test
    public void testRootHandler() throws Exception {
        Javalin app = Javalin.create(config -> { // Вынести в отдельный класс, либо beforeAll
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
        });
        app.get("/", ctx -> ctx.render("index.jte"));

        JavalinTest.test(app, (server, client) -> {
            Response response = client.get("/");
            assertEquals(200, response.code());
            assertEquals("text/html", response.body().contentType().toString());
            assertTrue(response.body().string().contains("<!DOCTYPE html>"));
        });
    }

}
