package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


@Epic("Authorization cases")
@Feature("User View")
public class UserGetTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Tag("Test")
    @Description("Получить данные о пользователе не авторизуясь")
    @DisplayName("Test get data user on auth")
    public void testCreateUserDataNotAuth() {
        Response responseUserData = apiCoreRequests.makeGetRequest(baseUrl + "/user/2");

        String[] expectedFields = {"username"};
        String[] unexpectedFields = {"email", "firstName", "lastName"};

        Assertions.assertJsonHasField(responseUserData, expectedFields);
        Assertions.assertJsonHasNotField(responseUserData, unexpectedFields);
    }

    @Test
    @Tag("Test")
    @Description("Получить данные о пользователе авторизовавшись им")
    @DisplayName("Test get data auth user")
    public void testCreateUserDetailsAuthAsSameUser() {
        Map<String, String> userData = new HashMap<>();
        userData.put("email", "vinkotov@example.com");
        userData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests.makePostRequest(baseUrl + "/user/login", userData);

        String cookie = this.getCookie(responseGetAuth, "auth_sid");
        String token = this.getHeader(responseGetAuth, "x-csrf-token");

        Response responseUserData = apiCoreRequests.makeGetRequest(baseUrl + "/user/2", token, cookie);

        String[] expectedFields = {"email", "username", "firstName", "lastName"};

        Assertions.assertJsonHasField(responseUserData, expectedFields);
    }

    @Test
    @Tag("Test")
    @Description("Получить данные о пользователе авторизовавшись другим пользователем")
    @DisplayName("Test Get data user, auth another")
    public void testCreateUserDetailsAuthUser() {
        Map<String, String> userData = new HashMap<>();
        userData.put("email", "vinkotov@example.com");
        userData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests.makePostRequest(baseUrl + "/user/login", userData);

        String cookie = this.getCookie(responseGetAuth, "auth_sid");
        String token = this.getHeader(responseGetAuth, "x-csrf-token");

        Response responseUserData = apiCoreRequests.makeGetRequest(baseUrl + "/user/3", token, cookie);

        String[] expectedFields = {"username"};
        String[] unexpectedFields = {"email", "firstName", "lastName"};

        Assertions.assertJsonHasField(responseUserData, expectedFields);
        Assertions.assertJsonHasNotField(responseUserData, unexpectedFields);
    }
}
