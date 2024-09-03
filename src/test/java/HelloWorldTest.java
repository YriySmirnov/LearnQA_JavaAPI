import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class HelloWorldTest {

    @Test
    public void testRestAssured() {
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();
        int statusCod = response.getStatusCode();

        while (statusCod == 301) {
            String location = response.getHeader("Location");
            System.out.println(location);
            response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .get(location)
                    .andReturn();
            statusCod = response.getStatusCode();
            System.out.println(statusCod);
        }
    }
}
