import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;

public class HelloWorldTest {

    @ParameterizedTest
    @ValueSource(strings={"platform", "browser", "device"})
    public void testRestAssured(String param) {

        Map<String, String> userAuth = new HashMap<>();
        userAuth.put("email", "vinkotov@example.com");
        userAuth.put("passwod", "1234");

        JsonPath response = RestAssured
                    .given()
                    .body(userAuth)
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .jsonPath();

        String expectedParam = response.get(param);

        Map<String, String> responseParam = new HashMap<>();
        responseParam.put(param, expectedParam);
        response = RestAssured
                .given()
                .body(responseParam)
                .post("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .jsonPath();
        String actualParam = response.get(param);

        Assertions.assertEquals(expectedParam, actualParam, "Parameters match");
    }
}
