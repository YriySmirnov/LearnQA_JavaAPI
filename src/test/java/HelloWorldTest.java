import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class HelloWorldTest {

    @Test
    public void testRestAssured() {
        Map<String, String> userAuth = new HashMap<>();
        userAuth.put("email", "vinkotov@example.com");
        userAuth.put("passwod", "1234");

        Response response = RestAssured
                    .given()
                    .body(userAuth)
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();
        Headers responseHeader = response.getHeaders();
        Assertions.assertEquals("Some secret value", responseHeader.getValue("x-secret-homework-header"), "Header not have homework");
    }
}
