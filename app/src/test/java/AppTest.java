import io.javalin.Javalin;
import lombok.SneakyThrows;
import model.UrlModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import io.javalin.testtools.JavalinTest;
import repository.URLRepository;
import utils.NamedRoutes;

class AppTest {
    Javalin app;

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
    public void testUrlListPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlList());
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @SneakyThrows
    @Test
    public void testUrlDetailesPage() {
        var testURL = new UrlModel("https://ya.ru/");
        URLRepository.addURL(testURL);
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlPath(Long.valueOf(1)));
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("ya.ru");
        });
    }

}
