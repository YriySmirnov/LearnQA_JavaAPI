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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static lib.DataGenerator.getRandomEmail;

@Epic("Authorization cases")
@Feature("Authorization")
public class UserRegisterTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Description("Создание пользователя с существующей почтой")
    @DisplayName("Test negative auth replay email")
    public void testCreateUserWithExistingEmail() {
        String email = "vinkotov@example.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");
    }

    @Test
    @Description("Создание пользователя")
    @DisplayName("Test positive auth")
    public void testCreateUserWithSuccessEmail() {
        String email = getRandomEmail();

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodEquals(responseCreateAuth, 200);
        Assertions.assertJsonHasKey(responseCreateAuth, "id");
    }

    @Test
    @Description("Создание пользователе с почтой без @")
    @DisplayName("Test negative auth email without @")
    public void testCreateUserWithEmailError() {
        String email = "vinkotovexample.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Invalid email format");
    }

    @ParameterizedTest
    @Description("Создание пользователе без параметра {data}")
    @DisplayName("Test negative auth")
    @ValueSource(strings = {"email", "password", "username", "firstName", "lastName"})
    public void testCreateUserWrongData(String data) {
        String email = "vinkotov@example.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        userData.remove(data);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The following required params are missed: " + data);
    }

    @Test
    @Description("Создание пользователе с коротким именем")
    @DisplayName("Test negative short name")
    public void testCreateUserWithShortName() {

        Map<String, String> userData = new HashMap<>();
        userData.put("username", "l");
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'username' field is too short");
    }

    @Test
    @Description("Создание пользователе с длинным именем")
    @DisplayName("Test negative long name")
    public void testCreateUserWithLongName() {

        Map<String, String> userData = new HashMap<>();
        userData.put("username", "learnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqalearnqa");
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'username' field is too long");
    }
}
