package hexlet.code;

import hexlet.code.repository.BaseDB;
import hexlet.code.repository.CheckRepository;
import hexlet.code.repository.UrlRepository;
import io.javalin.Javalin;
import lombok.SneakyThrows;
import hexlet.code.model.CheckModel;
import hexlet.code.model.UrlModel;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import io.javalin.testtools.JavalinTest;
import hexlet.code.utils.NamedRoutes;

class AppTest {
    static Javalin app;

    private static final String RES_FOLDER = System.getProperty("user.dir")
            + "/src/test/resources/";

    private static String readHTML(String fileName) throws IOException {
        return Files.readString(Path.of(RES_FOLDER + fileName));
    }

    @BeforeAll
    public static void beforeAll() throws IOException {
        try (MockWebServer server = new MockWebServer()) {
            MockResponse response = new MockResponse()
                    .setBody(readHTML("Test.html"))
                    .setResponseCode(200);
            server.enqueue(response);
            server.start();
        }
    }

    @BeforeEach
    public final void setUp() throws IOException, SQLException {
        app = App.getApp();
    }

    @Test
    public void testRoot() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.root());
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Hello world");
        });
    }

    @Test
    void testStore() {
        MockWebServer mock = new MockWebServer();
        String url = mock.url("/").toString().replaceAll("/$", "");

        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=" + url;
            assertThat(client.post("/urls", requestBody).code()).isEqualTo(200);

            var actualUrl = UrlRepository.findByName(url);
            assertThat(actualUrl).isNotNull();
            System.out.println("\n!!!!!");
            System.out.println(actualUrl);

            System.out.println("\n");
            assertThat(actualUrl.getName().toString()).isEqualTo(url);

            client.post("/urls/" + actualUrl.getId() + "/checks");

            assertThat(client.get("/urls/" + actualUrl.getId()).code())
                    .isEqualTo(200);

            var actualCheck = CheckRepository.findByUrlId(actualUrl.getId());
            assertThat(actualCheck).isNotNull();
            assertThat(actualCheck.getTitle()).isEqualTo("Test title");
            assertThat(actualCheck.getH1()).isEqualTo("Test header");
            assertThat(actualCheck.getDescription()).isEqualTo("test description");
        });
    }

    @Test
    public void testUrlListPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlList());
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @SneakyThrows
    @Test
    public void testUrlDetailsPage() {
        var testURL = new UrlModel("https://ya.ru/");
        UrlRepository.addURL(testURL);
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlPath(Long.valueOf(1)));
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("ya.ru");
        });
    }

    @SneakyThrows
    @Test
    public void testNotFound() {
        JavalinTest.test(app, ((server, client) -> {
            var response = client.get("/urls/5");
            assertThat(response.code()).isEqualTo(404);
        }));
    }

    @SneakyThrows
    @Test
    public void testCheck() {
        try (MockWebServer webServer = new MockWebServer()) {
            String page = webServer.url(NamedRoutes.root()).toString();
            MockResponse html = new MockResponse().setBody(readHTML("Test.html"));
            webServer.enqueue(html);

            var test = new UrlModel(page);
            UrlRepository.addURL(test);
            JavalinTest.test(app, (server, client) -> {
                client.post(NamedRoutes.checkPath(test.getId()));
                CheckModel check = CheckRepository.findAllByUrlId(test.getId()).getFirst();

                assertThat(check.getStatusCode()).isEqualTo(200);
                assertThat(check.getTitle()).isEqualTo("Test title");
                assertThat(check.getH1()).isEqualTo("Test header");
                assertThat(check.getDescription()).isEqualTo("test description");
            });
        }
    }

    @Test
    public void testCreateUrls() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=http://www.ya.ru";
            var response = client.post(NamedRoutes.urlList(), requestBody);
            var check = UrlRepository.findByName("http://www.ya.ru");
            assertThat(response.code()).isEqualTo(200);
            assertThat(check != null);
        });
    }

}
