import hexlet.code.*;
import io.javalin.testtools.JavalinTest;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UrlCheckIntegrationTest {

    private static MockWebServer mockServer;
    private final UrlRepository urlRepo = new UrlRepository();
    private final UrlCheckRepository checkRepo = new UrlCheckRepository();

    @BeforeAll
    static void startServer() throws Exception {
        mockServer = new MockWebServer();
        mockServer.start();
    }

    @AfterAll
    static void stopServer() throws Exception {
        if (mockServer != null) {
            mockServer.shutdown();
        }
    }

    @Test
    void testCreateUrlCheck() throws Exception {
        // инициализация БД (обязательно)
        DbInitializer.init();
        try (Connection conn = DataSourceProvider.getDataSource().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("TRUNCATE TABLE url_checks");
            stmt.execute("TRUNCATE TABLE urls");
        }

        // подготовим фейковую страницу
        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("<html><head><title>Test</title><meta name=\"description\" content=\"desc\"></head><body><h1>H1</h1></body></html>"));

        // создаём сайт через API (чтобы сервер и тест использовали одну БД)
        JavalinTest.test(App.getApp(), (server, client) -> {
            // POST /urls — добавляем сайт, указывая адрес mockServer
            String siteUrl = mockServer.url("/").toString();
            Request post = new Request.Builder()
                    .url(client.getOrigin() + "/urls")
                    .post(new FormBody.Builder().add("url", siteUrl).build())
                    .build();
            client.request(post);

            // получаем id добавленного URL
            List<Url> urls = urlRepo.findAll();
            assertEquals(1, urls.size());
            Url url = urls.get(0);

            // выполняем проверку через endpoint
            Request checkReq = new Request.Builder()
                    .url(client.getOrigin() + "/urls/" + url.getId() + "/checks")
                    .post(new FormBody.Builder().build())
                    .build();
            Response response = client.request(checkReq);
            assertTrue(response.isSuccessful() || response.code() == 302); // редирект допустим

            // убеждаемся, что проверка сохранилась
            List<UrlCheck> checks = checkRepo.findAllByUrlId(url.getId());
            assertEquals(1, checks.size());
            UrlCheck check = checks.get(0);
            assertEquals(Integer.valueOf(200), check.getStatusCode());
            assertEquals("Test", check.getTitle());
            assertEquals("H1", check.getH1());
            assertEquals("desc", check.getDescription());
        });
    }
}
