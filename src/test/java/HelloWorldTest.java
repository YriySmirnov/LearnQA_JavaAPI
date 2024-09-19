import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;

public class HelloWorldTest {
    static String user_agent;
    static final Map<String, String> expected_values =  new HashMap<>();

    @BeforeAll
    static void before(){
        user_agent = "Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";

        expected_values.put("platform", "Mobile");
        expected_values.put("browser", "No");
        expected_values.put("device", "Android");
    }

    @ParameterizedTest
    @ValueSource(strings={"platform", "browser", "device"})
    public void testRestAssured(String param) {
        Response response = RestAssured
                    .given()
                    .header("user-agent", user_agent)
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .thenReturn();

        Assertions.assertEquals(expected_values.get(param), response.jsonPath().getString(param));
    }
}
