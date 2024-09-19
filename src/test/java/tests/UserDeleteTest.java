package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


@Epic("Authorization cases")
@Feature("User Delete")
public class UserDeleteTest extends BaseTestCase {

    @Test
    @Description("Удаление не удаляемого пользователя")
    @DisplayName("Test delete auth user")
    public void testDelAuthBlockUserTest() {
        //USER
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");
        String userId = "2";

        //LOGIN
        Response responseGetAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //DELETE
        Response responseDeleteUser = apiCoreRequests.makeDelRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid"));

        Assertions.assertJsonByName(responseDeleteUser, "error", "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");

        //GET
        Response responseUserData = apiCoreRequests.makeGetRequest("https://playground.learnqa.ru/api/user/" + userId, this.getHeader(responseGetAuth, "x-csrf-token"), this.getCookie(responseGetAuth, "auth_sid"));

        Assertions.assertJsonByName(responseUserData, "firstName", "Vitalii");
    }

    @Test
    @Description("Удаление созданного пользователя")
    @DisplayName("Test delete creation user")
    public void testDelCreatedUserTest() {
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        String userId = responseCreateAuth.jsonPath().getString("id");

        //LOGIN
        Response responseGetAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login", userData);

        //DELETE
        Response responseDeleteUser = apiCoreRequests.makeDelRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid"));

        Assertions.assertJsonByName(responseDeleteUser, "success", "!");

        //LOGIN

        responseGetAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login", userData);

        Assertions.assertResponseTextEquals(responseGetAuth, "Invalid username/password supplied");
    }

    @Test
    @Description("Удаление созданного пользователя другим пользователем")
    @DisplayName("Test delete creation user anther")
    public void testDelCreatedUserAntherTest() {
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        String userId = responseCreateAuth.jsonPath().getString("id");

        //GENERATE USER2
        Map<String, String> userData2 = DataGenerator.getRegistrationData();
        userData2.put("email", "2" + userData2.get("email"));

        responseCreateAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/", userData2);

        String userId2 = responseCreateAuth.jsonPath().getString("id");

        //LOGIN
        Response responseGetAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login", userData2);

        //DELETE
        Response responseDeleteUser = apiCoreRequests.makeDelRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid"));

        Assertions.assertJsonByName(responseDeleteUser, "error", "This user can only delete their own account.");

        //LOGIN
        responseGetAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login", userData);

        Response responseUserData = apiCoreRequests.makeGetRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid"));

        String[] expectedFields = {"email", "username", "firstName", "lastName"};

        Assertions.assertJsonHasField(responseUserData, expectedFields);
    }

}
