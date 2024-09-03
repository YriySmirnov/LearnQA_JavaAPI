import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

public class HelloWorldTest {

    @Test
    public void testRestAssured() {
        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();

        ArrayList<Map<String, String>> messages = response.get("messages");
        System.out.println(messages.get(1));
    }
}
