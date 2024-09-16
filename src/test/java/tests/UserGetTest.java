package tests;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserGetTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Description("Получить данные о пользователе не авторизуясь")
    @DisplayName("Test get data user on auth")
    public void testCreateUserDataNotAuth() {
        Response responseUserData = RestAssured
                .get("https://playground.learnqa.ru/api/user/2")
                .thenReturn();

        String[] expectedFields = {"username"};
        String[] unexpectedFields = {"email", "firstName", "lastName"};

        Assertions.assertJsonHasField(responseUserData, expectedFields);
        Assertions.assertJsonHasNotField(responseUserData, unexpectedFields);
    }

    @Test
    @Description("Получить данные о пользователе авторизовавшись им")
    @DisplayName("Test get data auth user")
    public void testCreateUserDetailsAuthAsSameUser() {
        Map<String, String> userData = new HashMap<>();
        userData.put("email", "vinkotov@example.com");
        userData.put("password", "1234");

        Response responseGetAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/login")
                .thenReturn();

        String cookie = this.getCookie(responseGetAuth, "auth_sid");
        String header = this.getHeader(responseGetAuth, "x-csrf-token");

        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", header)
                .cookies("auth_sid", cookie)
                .get("https://playground.learnqa.ru/api/user/2")
                .thenReturn();

        String[] expectedFields = {"email", "username", "firstName", "lastName"};

        Assertions.assertJsonHasField(responseUserData, expectedFields);
    }

    @Test
    @Description("Получить данные о пользователе авторизовавшись другим пользователем")
    @DisplayName("Test Get data user, auth another")
    public void testCreateUserDetailsAuthUser() {
        Map<String, String> userData = new HashMap<>();
        userData.put("email", "vinkotov@example.com");
        userData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login", userData);

        String cookie = this.getCookie(responseGetAuth, "auth_sid");
        String header = this.getHeader(responseGetAuth, "x-csrf-token");

        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", header)
                .cookies("auth_sid", cookie)
                .get("https://playground.learnqa.ru/api/user/3")
                .thenReturn();

        String[] expectedFields = {"username"};
        String[] unexpectedFields = {"email", "firstName", "lastName"};

        Assertions.assertJsonHasField(responseUserData, expectedFields);
        Assertions.assertJsonHasNotField(responseUserData, unexpectedFields);
    }
}
