package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class AppTest {
    private static Javalin app;
    private static MockWebServer mockServer;

    public static String readFixture(String fixtureName) {
        try (InputStream is = AppTest.class.getClassLoader().getResourceAsStream("fixtures/" + fixtureName)) {
            if (is == null) {
                throw new FileNotFoundException("Фикстура " + fixtureName + " не найдена.");
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при чтении фикстуры", e);
        }
    }


    @BeforeAll
    static void beforeAll() throws IOException {
        mockServer = new MockWebServer();
        MockResponse response = new MockResponse.Builder()
                .body(readFixture("index.html"))
                .build();
        mockServer.enqueue(response);
        mockServer.start();
    }

    @AfterAll
    static void afterAll() {
        mockServer.close();
    }

    @BeforeEach
    final void setUp() throws IOException, SQLException {
        app = App.getApp();
    }

    @Test
    void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.rootPath());
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Бесплатно проверяйте сайты на SEO пригодность");
        });
    }

    @Test
    void testIndexPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlsPath());
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    void testUrlsPage() {
        JavalinTest.test(app, (server, client) -> {
            var url = new Url("https://www.example.com");
            UrlRepository.save(url);
            var response = client.get(NamedRoutes.urlPath(url.getId()));
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    void testUrlNotFound() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlPath(9999L));
            assertThat(response.code()).isEqualTo(404);
        });
    }

    @Test
    void testCreateUrl() {
        var url = "https://www.example.com";
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=" + url;
            var response = client.post(NamedRoutes.urlsPath(), requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://www.example.com");
            var urlFromRepo = UrlRepository.find(url).get();
            assertThat(urlFromRepo.getName()).isEqualTo(url);
        });
    }

    @Test
    void testCreateInvalidUrl() {
        var url = "asd";
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=" + url;
            var response = client.post(NamedRoutes.urlsPath(), requestBody);
            assertThat(response.code()).isEqualTo(200);
            var urlFromRepo = UrlRepository.find(url);
            assertThat(urlFromRepo).isEmpty();
        });
    }

    @Test
    void testCreateUrlCheck() {
        String url = mockServer.url("/").toString().replaceAll("/$", "");
        System.out.println(url);
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=" + url;
            assertThat(client.post(NamedRoutes.urlsPath(), requestBody).code()).isEqualTo(200);

            var urlFromRepo = UrlRepository.find(url).orElse(null);
            assertThat(urlFromRepo).isNotNull();

            client.post(NamedRoutes.urlCheckPath(urlFromRepo.getId()));
            assertThat(client.get(NamedRoutes.urlPath(urlFromRepo.getId())).code()).isEqualTo(200);

            UrlCheck urlCheckFromRepo = UrlCheckRepository.findLastChecks()
                    .get(urlFromRepo.getId());
            assertThat(urlCheckFromRepo).isNotNull();
            assertThat(urlCheckFromRepo.getStatusCode()).isEqualTo(200);
            assertThat(urlCheckFromRepo.getTitle()).isEqualTo("Example Page");
            assertThat(urlCheckFromRepo.getDescription()).isEqualTo("test page for education project");
            assertThat(urlCheckFromRepo.getH1()).isEqualTo("Example heading 1");
        });
    }
}
