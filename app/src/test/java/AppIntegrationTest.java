import hexlet.code.*;
import io.javalin.testtools.JavalinTest;
import okhttp3.FormBody;
import okhttp3.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class AppIntegrationTest {

    private UrlRepository repo;

    @BeforeEach
    void setUp() throws Exception {
        // Инициализация схемы и очистка перед каждым тестом
        DbInitializer.init();
        try (Connection conn = DataSourceProvider.getDataSource().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("TRUNCATE TABLE urls");
        }
        repo = new UrlRepository();
    }

    @Test
    void testGetRoot() {
        JavalinTest.test(App.getApp(), (server, client) -> {
            var response = client.get("/");
            assertEquals(200, response.code());
            assertTrue(response.body().string().contains("Введите адрес"));
        });
    }

    @Test
    void testPostUrlAndList() throws Exception {
        JavalinTest.test(App.getApp(), (server, client) -> {
            // POST-запрос
            var request = new Request.Builder()
                    .url(client.getOrigin() + "/urls")
                    .post(new FormBody.Builder().add("url", "https://example.com").build())
                    .build();
            client.request(request);

            // Проверяем в БД
            var urls = repo.findAll();
            assertEquals(1, urls.size());
            assertEquals("https://example.com", urls.get(0).getName());

            // Проверяем отображение на странице
            var response = client.get("/urls");
            String body = response.body().string();
            assertTrue(body.contains("https://example.com"));
        });
    }

    @Test
    void testShowUrl() throws Exception {
        JavalinTest.test(App.getApp(), (server, client) -> {
            // Создаем URL через POST
            var postRequest = new Request.Builder()
                    .url(client.getOrigin() + "/urls")
                    .post(new FormBody.Builder().add("url", "https://google.com").build())
                    .build();
            client.request(postRequest);

            // Получаем id созданного URL из репозитория
            var urls = repo.findAll();
            Url url = urls.get(0);

            // Проверяем GET /urls/{id}
            var response = client.get("/urls/" + url.getId());
            assertEquals(200, response.code());
            assertTrue(response.body().string().contains("https://google.com"));
        });
    }

    @Test
    void testShowUrlNotFound() {
        JavalinTest.test(App.getApp(), (server, client) -> {
            var response = client.get("/urls/9999");
            assertEquals(404, response.code());
            assertTrue(response.body().string().contains("URL не найден"));
        });
    }
}
