import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class HelloWorldTest {

    @Test
    public void testRestAssured() throws InterruptedException {
        Map<String, String> param = new HashMap<>();

        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/longtime_job")

                .jsonPath();

        String token = response.get("token");
        int seconds = response.get("seconds");
        param.put("token", token);

        response = RestAssured
                .given()
                .queryParams(param)
                .get("https://playground.learnqa.ru/api/longtime_job")
                .jsonPath();

        String status = response.get("status");
        if (status.equals("Job is NOT ready")){
            System.out.println(status);
            System.out.println("Job ready of " + seconds + " seconds");
        }

        Thread.sleep(seconds * 1000L);

        response = RestAssured
                .given()
                .queryParams(param)
                .get("https://playground.learnqa.ru/api/longtime_job")
                .jsonPath();

        status = response.get("status");
        if (status.equals("Job is ready")){
            String result = response.get("result");
            System.out.println(status);
            System.out.println(result);
        }
    }
}
