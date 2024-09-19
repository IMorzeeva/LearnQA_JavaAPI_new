package tests;


import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserEditTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    public void testEditJustCreatedTest() {

        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests
                .makePostRequestGenerateUser(userData);

        String userId = responseCreateAuth.getString("id");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));


        Response responseGetAuth = apiCoreRequests
                .makePostRequestLoginUser("https://playground.learnqa.ru/api/user/login", authData);

        //EDIT
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
                .body(editData)
                .put("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        //GET

        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
                .get("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        System.out.println(responseUserData.asString());

        Assertions.assertJsonByName(responseUserData, "firstName", newName);

    }

    @Description("Edit user without auth")
    @DisplayName("Edit user without auth")
    @Test
    public void testEditUserWithoutAuthTest() {

        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests
                .makePostRequestGenerateUser(userData);

        String userId = responseCreateAuth.getString("id");
        System.out.println(userId);

        //EDIT
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests
                .makePutRequestChangeUsername("https://playground.learnqa.ru/api/user/" + userId, editData);

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequestLoginUser("https://playground.learnqa.ru/api/user/login", authData);

        //CHECK-GET


        Response responseUserData = apiCoreRequests
                .makeGetRequestGetUserData("https://playground.learnqa.ru/api/user/" + userId,
                        getCookie(responseGetAuth, "auth_sid"),
                        getHeader(responseGetAuth, "x-csrf-token"));


        System.out.println(responseUserData.asString());

        String expectedField = "firstName";
        Assertions.assertJsonHasField(responseUserData, expectedField);
        Assertions.assertJsonByName(responseUserData, "firstName", "learnqa");
    }

    @Description("Edit user data: authorization as other user")
    @DisplayName("Edit user data auth as other user")
    @Test
    public void testEditUserWithOtherUser() {

        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests
                .makePostRequestGenerateUser(userData);

        String userId = responseCreateAuth.getString("id");

        //GEERATE 2nd USER

        Map<String, String> userData2 = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth2 = apiCoreRequests
                .makePostRequestGenerateSecondUser(userData2);

        String userId2 = responseCreateAuth2.getString("id");

        //LOGIN AS 1ST USER
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequestLoginUser("https://playground.learnqa.ru/api/user/login", authData);


        //EDIT 2nd USER
        String newName = "Changed Name";
        Map<String, String> editData2 = new HashMap<>();
        editData2.put("firstName", newName);

        Response responseEditUser = apiCoreRequests
                .makePutRequestChangeFirstName("https://playground.learnqa.ru/api/user/" + userId2, editData2,
                getCookie(responseGetAuth, "auth_sid"),
                getHeader(responseGetAuth, "x-csrf-token"));


        //LOGIN AS 2nd USER
        Map<String, String> authData2 = new HashMap<>();
        authData2.put("email", userData2.get("email"));
        authData2.put("password", userData2.get("password"));

        Response responseGetAuth2 = apiCoreRequests
                .makePostRequestLogin2User("https://playground.learnqa.ru/api/user/login", authData2);


        //CHECK-GET

        Response responseUserData = apiCoreRequests
                .makeGetRequestGetUserData("https://playground.learnqa.ru/api/user/" + userId2,
                        getCookie(responseGetAuth2, "auth_sid"),
                        getHeader(responseGetAuth2, "x-csrf-token"));


        System.out.println(responseUserData.asString());

        String expectedField = "firstName";
        Assertions.assertJsonHasField(responseUserData, expectedField);
        Assertions.assertJsonByName(responseUserData, "firstName", "learnqa");
    }

    @Description("Edit user data: change email to uncorrect email without @")
    @DisplayName("Edit email without @")
    @Test
    public void testEditEmailUncorrect() {

        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests
                .makePostRequestGenerateUser(userData);

        String userId = responseCreateAuth.getString("id");
        String userEmail = userData.get("email");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequestLoginUser("https://playground.learnqa.ru/api/user/login", authData);

        //EDIT
        String newEmail = "changed_example.com";
        Map<String, String> editData = new HashMap<>();
        editData.put("email", newEmail);

        JsonPath responseEditUser = apiCoreRequests
                .makePutRequestToChangeEmail("https://playground.learnqa.ru/api/user/" + userId, editData,
                        getCookie(responseGetAuth, "auth_sid"),
                        getHeader(responseGetAuth, "x-csrf-token"));

        String message = responseEditUser.get("error");
        System.out.println(message);

        assertEquals(message, "Invalid email format");

        //GET

        Response responseUserData = apiCoreRequests
                .makeGetRequestGetUserData("https://playground.learnqa.ru/api/user/" + userId,
                        getCookie(responseGetAuth, "auth_sid"),
                        getHeader(responseGetAuth, "x-csrf-token"));

        System.out.println(responseUserData.asString());

        Assertions.assertJsonByName(responseUserData, "email", userEmail);
    }

    @Description("Edit firstname: new firstname is too short")
    @DisplayName("New firstname is too short")
    @Test
    public void testEditFirstNameShort() {

        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests
                .makePostRequestGenerateUser(userData);

        String userId = responseCreateAuth.getString("id");
        String firstName = userData.get("firstName");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequestLoginUser("https://playground.learnqa.ru/api/user/login", authData);

        //EDIT
        String newFirstName = "k";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newFirstName);

        JsonPath responseEditUser = apiCoreRequests
                .makePutRequestToChangeFirstname("https://playground.learnqa.ru/api/user/" + userId, editData,
                        getCookie(responseGetAuth, "auth_sid"),
                        getHeader(responseGetAuth, "x-csrf-token"));


        String message = responseEditUser.get("error");
        System.out.println(message);

        assertEquals(message, "The value for field `firstName` is too short");

        //GET

        Response responseUserData = apiCoreRequests
                .makeGetRequestGetUserData("https://playground.learnqa.ru/api/user/" + userId,
                        getCookie(responseGetAuth, "auth_sid"),
                        getHeader(responseGetAuth, "x-csrf-token"));

        System.out.println(responseUserData.asString());

        Assertions.assertJsonByName(responseUserData, "firstName", firstName);

    }
}
