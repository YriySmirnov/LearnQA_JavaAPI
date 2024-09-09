import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class HelloWorldTest {

    @Test
    public void testRestAssured() {
        Map<String, String> userAuth = new HashMap<>();
        userAuth.put("email", "vinkotov@example.com");
        userAuth.put("passwod", "1234");

        Response response = RestAssured
                    .given()
                    .body(userAuth)
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();

        Map<String, String> responseCookie = response.getCookies();
        Assertions.assertEquals("hw_value", responseCookie.get("HomeWork"), "Cookie not correct");
    }
}
