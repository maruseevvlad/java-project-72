import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import hexlet.code.*;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppFullIntegrationTest {

    private DataSource dataSource;
    private static MockWebServer mockServer;

    private final UrlRepository urlRepo = new UrlRepository(DataSourceProvider.getDataSource());
    private final UrlCheckRepository checkRepo = new UrlCheckRepository(DataSourceProvider.getDataSource());

//    @BeforeEach
//    void setUp() {
//        dataSource = new org.h2.jdbcx.JdbcDataSource();
//        ((org.h2.jdbcx.JdbcDataSource) dataSource).setURL("jdbc:h2:mem:project;
//        DB_CLOSE_DELAY=-1;
//        DB_CLOSE_ON_EXIT=FALSE");
//        ((org.h2.jdbcx.JdbcDataSource) dataSource).setUser("sa");
//        ((org.h2.jdbcx.JdbcDataSource) dataSource).setPassword("");
//
//        try (Connection conn = dataSource.getConnection();
//             Statement stmt = conn.createStatement()) {
//            stmt.execute("CREATE TABLE IF NOT EXISTS urls (id INT PRIMARY KEY, name VARCHAR(255))");
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @BeforeAll
    void startMockServer() throws Exception {
        mockServer = new MockWebServer();
        mockServer.start();
    }

    @AfterAll
    void stopMockServer() throws Exception {
        if (mockServer != null) {
            mockServer.shutdown();
        }
    }

//    @BeforeEach
//    public void cleanDb() throws SQLException {
//        try (Connection connection = dataSource.getConnection();
//             Statement statement = connection.createStatement()) {
//            statement.execute("SET REFERENTIAL_INTEGRITY FALSE");
//            statement.execute("DELETE FROM urls");
//            statement.execute("SET REFERENTIAL_INTEGRITY TRUE");
//        }
//    }

    private static TemplateEngine createTemplateEngine() {
        ResourceCodeResolver codeResolver = new ResourceCodeResolver(
                "templates",
                App.class.getClassLoader()
        );
        return TemplateEngine.create(codeResolver, gg.jte.ContentType.Html);
    }

    @Test
    void testAddUrlAndShowListAndPage() {
        Javalin app = App.getApp();
        
        JavalinTest.test(app, (server, client) -> {
            Request post = new Request.Builder()
                    .url(client.getOrigin() + "/urls")
                    .post(new FormBody.Builder()
                            .add("url", "https://example.com")
                            .build())
                    .build();
            client.request(post);

            List<Url> urls = urlRepo.findAll();
            assertEquals(1, urls.size());
            Url url = urls.get(0);
            assertEquals("https://example.com", url.getName());

            Response listResp = client.get("/urls");
            String listBody = listResp.body().string();
            assertTrue(listBody.contains("https://example.com"));

            Response showResp = client.get("/urls/" + url.getId());
            String showBody = showResp.body().string();
            assertTrue(showBody.contains("https://example.com"));
        });
    }

    @Test
    void testShowUrlNotFound() {
        JavalinTest.test(App.getApp(), (server, client) -> {
            Response resp = client.get("/urls/9999");
            assertEquals(404, resp.code());
            assertTrue(resp.body().string().contains("URL не найден"));
        });
    }

    @Test
    void testUrlCheckCreation() throws Exception {
        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(
                        "<html><head><title>Test</title>"
                        +   "<meta name=\"description\" content=\"desc\"></head>"
                        +   "<body><h1>H1</h1></body></html>"
                ));

        JavalinTest.test(App.getApp(), (server, client) -> {
            String siteUrl = mockServer.url("/").toString();

            Request postUrl = new Request.Builder()
                    .url(client.getOrigin() + "/urls")
                    .post(new FormBody.Builder()
                            .add("url", siteUrl)
                            .build())
                    .build();
            client.request(postUrl);

            Url url = urlRepo.findAll().get(0);

            Request checkReq = new Request.Builder()
                    .url(client.getOrigin() + "/urls/" + url.getId() + "/checks")
                    .post(new FormBody.Builder().build())
                    .build();
            Response checkResp = client.request(checkReq);
            assertTrue(checkResp.isSuccessful() || checkResp.code() == 302);

            UrlCheck check = checkRepo.findAllByUrlId(url.getId()).get(0);
            assertEquals(Integer.valueOf(200), check.getStatusCode());
            assertEquals("Test", check.getTitle());
            assertEquals("H1", check.getH1());
            assertEquals("desc", check.getDescription());
        });
    }

    @Test
    void testAddDuplicateUrl() {
        JavalinTest.test(App.getApp(), (server, client) -> {
            Request post1 = new Request.Builder()
                    .url(client.getOrigin() + "/urls")
                    .post(new FormBody.Builder()
                            .add("url", "https://example.com")
                            .build())
                    .build();
            client.request(post1);

            Request post2 = new Request.Builder()
                    .url(client.getOrigin() + "/urls")
                    .post(new FormBody.Builder()
                            .add("url", "https://example.com")
                            .build())
                    .build();
            Response resp2 = client.request(post2);
            assertTrue(resp2.isSuccessful() || resp2.code() == 302);

            List<Url> urls = urlRepo.findAll();
            assertEquals(1, urls.size(), "Дублирование URL не должно создавать новую запись");
        });
    }
}
