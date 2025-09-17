import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class UrlTest {

    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        return TemplateEngine.create(codeResolver, gg.jte.ContentType.Html);
    }

    private Javalin app;

    @BeforeEach
    public void setUp() throws SQLException {
        // Инициализация тестовой БД
        DbInitializer.init();
        app = App.getApp();
    }

    @Test
    public void testAddUrlToDatabase() throws Exception {
        JavalinTest.test(app, (server, client) -> {
            // Получаем начальное количество URL в БД
            UrlRepository repository = new UrlRepository(DataSourceProvider.getDataSource());
            long initialCount = repository.count();

            // Создаем и отправляем POST-запрос с данными формы
            FormBody formBody = new FormBody.Builder()
                    .add("url", "https://example.com")
                    .build();

            Request request = new Request.Builder()
                    .url(server.url() + "/urls")
                    .post(formBody)
                    .build();

            Response response = client.newCall(request).execute();

            // Проверяем, что запрос завершился успешно (редирект)
            assertEquals(302, response.code());

            // Проверяем, что количество URL в БД увеличилось на 1
            long finalCount = repository.count();
            assertEquals(initialCount + 1, finalCount);

            // Проверяем, что добавленный URL существует в БД
            Url addedUrl = repository.findByName("https://example.com");
            assertNotNull(addedUrl);
            assertEquals("https://example.com", addedUrl.getName());
        });
    }

}

