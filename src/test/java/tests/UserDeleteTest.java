package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDeleteTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Story(value = "Delete User")
    @Description("Delete user without login")
    @DisplayName("Delete user without login")
    @Test
    @Owner(value = "Inna Morzeeva")
    public void TestDeleteUserId2() {
        String email = "vinkotov@example.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("password", "1234");

        JsonPath response = apiCoreRequests
                .makeDeleteRequestToDeleteUserWithoutAuth("https://playground.learnqa.ru/api/user/2", userData);


        String message = response.get("error");
        System.out.println(message);

        assertEquals(message, "Auth token not supplied");
    }

    @Description("Delete user: positive test")
    @DisplayName("Delete user success")
    @Test
    @Owner(value = "Inna Morzeeva")
    public void testPositiveDeleteUser() {

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

        //DElETE

        Map<String, String> deleteData = new HashMap<>();
        deleteData.put("email", userData.get("email"));
        deleteData.put("password", userData.get("password"));

        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequestToDeleteUserWithAuth("https://playground.learnqa.ru/api/user/" + userId,
                        deleteData,
                        getCookie(responseGetAuth, "auth_sid"),
                        getHeader(responseGetAuth, "x-csrf-token"));

        System.out.println(responseDeleteUser.asString());
        System.out.println(responseDeleteUser.statusCode());

        Assertions.assertResponseCodeEquals(responseDeleteUser, 200);
        String expectedField = "success";
        Assertions.assertJsonHasField(responseDeleteUser, expectedField);
        Assertions.assertJsonByName(responseDeleteUser, "success", "!");

        //GET

        Response responseUserData = apiCoreRequests
                .makeGetRequestGetUserData("https://playground.learnqa.ru/api/user/" + userId,
                        getCookie(responseGetAuth, "auth_sid"),
                        getHeader(responseGetAuth, "x-csrf-token"));

        System.out.println(responseUserData.asString());
        System.out.println(responseUserData.statusCode());

        Assertions.assertResponseCodeEquals(responseUserData, 404);
        Assertions.assertResponseTextEquals(responseUserData, "User not found");

    }

    @Description("Delete user: auth as other user")
    @DisplayName("Delete user auth as other user: unsuccess")
    @Test
    @Owner(value = "Inna Morzeeva")
    public void testDeleteUserAuthAsOtherUser() {
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

        //DElETE

        Map<String, String> deleteData = new HashMap<>();
        deleteData.put("email", userData.get("email"));
        deleteData.put("password", userData.get("password"));

        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequestToDeleteUserWithAuth("https://playground.learnqa.ru/api/user/" + userId2,
                        deleteData,
                        getCookie(responseGetAuth, "auth_sid"),
                        getHeader(responseGetAuth, "x-csrf-token"));


        Assertions.assertResponseCodeEquals(responseDeleteUser, 400);
        String expectedField = "error";
        Assertions.assertJsonHasField(responseDeleteUser, expectedField);
        Assertions.assertJsonByName(responseDeleteUser, "error", "This user can only delete their own account.");


    }

}
