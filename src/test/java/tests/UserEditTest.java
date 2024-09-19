package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


@Epic("Authorization cases")
@Feature("User Edit")
public class UserEditTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Tag("Test")
    @Description("Изменить данные о пользователе авторизовавшись им")
    @DisplayName("Test edit data auth user")
    public void testEditJustCreatedTest() {
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests.makePostRequest(baseUrl + "/user/", userData);

        String userId = responseCreateAuth.jsonPath().getString("id");

        //LOGIN

        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests.makePostRequest(baseUrl + "/user/login", authData);

        //EDIT
        String newName = "Change Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests.makePutRequest(
                baseUrl + "/user/" + userId,
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid"),
                editData);

        //GET

        Response responseUserData = apiCoreRequests.makeGetRequest(baseUrl + "/user/" + userId, this.getHeader(responseGetAuth, "x-csrf-token"), this.getCookie(responseGetAuth, "auth_sid"));

        Assertions.assertJsonByName(responseUserData, "firstName", newName);
    }

    @Test
    @Tag("Test")
    @Description("Изменить данные о пользователе не авторизовавшись им")
    @DisplayName("Test edit data, not auth user")
    public void testEditJustCreatedNotAuthTest() {
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests.makePostRequest(baseUrl + "/user/", userData);

        String userId = responseCreateAuth.jsonPath().getString("id");

        //EDIT
        String newName = "Change Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests.makePutRequest(
                baseUrl + "/user/" + userId,
                editData);

        //LOGIN

        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests.makePostRequest(baseUrl + "/user/login", authData);

        //GET

        Response responseUserData = apiCoreRequests.makeGetRequest(baseUrl + "/user/" + userId, this.getHeader(responseGetAuth, "x-csrf-token"), this.getCookie(responseGetAuth, "auth_sid"));

        Assertions.assertJsonByName(responseUserData, "firstName", userData.get("firstName"));
    }

    @Test
    @Tag("Test")
    @Description("Изменить данные о пользователе авторизовавшись другим")
    @DisplayName("Test edit data, auth user")
    public void testEditJustCreatedAuthAnotherTest() {
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests.makePostRequest(baseUrl + "/user/", userData);

        String userId = responseCreateAuth.jsonPath().getString("id");

        //LOGIN

        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests.makePostRequest(baseUrl + "/user/login", authData);

        //GENERATE USER2
        Map<String, String> userData2 = DataGenerator.getRegistrationData();
        userData2.put("email", "2"+userData2.get("email"));

        Response responseCreateAuth2 = apiCoreRequests.makePostRequest(baseUrl + "/user/", userData2);

        String userId2 = responseCreateAuth2.jsonPath().getString("id");


        //EDIT
        String newName = "Change Name";
        Map<String, String> editData2 = new HashMap<>();
        editData2.put("firstName", newName);

        Response responseEditUser = apiCoreRequests.makePutRequest(
                baseUrl + "/user/" + userId2,
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid"),
                editData2);

        //LOGIN

        responseGetAuth = apiCoreRequests.makePostRequest(baseUrl + "/user/login", userData2);

        //GET

        Response responseUserData = apiCoreRequests.makeGetRequest(baseUrl + "/user/" + userId2, this.getHeader(responseGetAuth, "x-csrf-token"), this.getCookie(responseGetAuth, "auth_sid"));

        Assertions.assertJsonByName(responseUserData, "firstName", userData2.get("firstName"));
    }

    @Test
    @Tag("Test")
    @Description("Изменить email на email без @ авторизовавшись пользователем")
    @DisplayName("Test edit email without @, auth user")
    public void testEditWithoutSingTest() {
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests.makePostRequest(baseUrl + "/user/", userData);

        String userId = responseCreateAuth.jsonPath().getString("id");

        //LOGIN

        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests.makePostRequest(baseUrl + "/user/login", authData);

        //EDIT
        String newEmail = userData.get("email").replaceAll("@", "");
        Map<String, String> editData = new HashMap<>();
        editData.put("email", newEmail);

        Response responseEditUser = apiCoreRequests.makePutRequest(
                baseUrl + "/user/" + userId,
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid"),
                editData);

        Assertions.assertJsonByName(responseEditUser, "error", "Invalid email format");

        //GET

        Response responseUserData = apiCoreRequests.makeGetRequest(baseUrl + "/user/" + userId, this.getHeader(responseGetAuth, "x-csrf-token"), this.getCookie(responseGetAuth, "auth_sid"));

        Assertions.assertJsonByName(responseUserData, "email", userData.get("email"));
    }


    @Test
    @Tag("Test")
    @Description("Изменить имя пользователя на короткое, авторизовавшись им")
    @DisplayName("Test edit name on shot auth user")
    public void testEditNameNoShotTest() {
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests.makePostRequest(baseUrl + "/user/", userData);

        String userId = responseCreateAuth.jsonPath().getString("id");

        //LOGIN

        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests.makePostRequest(baseUrl + "/user/login", authData);

        //EDIT
        String newName = "u";
        Map<String, String> editData = new HashMap<>();
        editData.put("username", newName);

        Response responseEditUser = apiCoreRequests.makePutRequest(
                baseUrl + "/user/" + userId,
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid"),
                editData);

        Assertions.assertJsonByName(responseEditUser, "error", "The value for field `username` is too short");

        //GET

        Response responseUserData = apiCoreRequests.makeGetRequest(baseUrl + "/user/" + userId, this.getHeader(responseGetAuth, "x-csrf-token"), this.getCookie(responseGetAuth, "auth_sid"));

        Assertions.assertJsonByName(responseUserData, "username", userData.get("username"));
    }
}
