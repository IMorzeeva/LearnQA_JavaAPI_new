package tests;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class UserGetTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    @Test
    public void testGetUserDataNotAuth(){
        Response responseUserData = RestAssured
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();

//        Assertions.assertJsonHasKey(responseUserData, "username");
//        Assertions.assertJsonHasNotKey(responseUserData, "firstName");
//        Assertions.assertJsonHasNotKey(responseUserData, "lastName");
//        Assertions.assertJsonHasNotKey(responseUserData, "email");
    }

    @Test
    public void testGetUserDetailsAuthAsSameUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();


        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", header)
                .cookies("auth_sid", cookie)
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();


        System.out.println(responseUserData.asString());
//        Assertions.assertJsonHasKey(responseUserData, "username");
//        Assertions.assertJsonHasKey(responseUserData, "firstName");
//        Assertions.assertJsonHasKey(responseUserData, "lastName");
//        Assertions.assertJsonHasKey(responseUserData, "email");
    }
@Description("Getting users details: Authorization as other user")
@DisplayName("User details: auth as other user")
    @Test
    public void testGetUserDetailsAuthAsOtherUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");


    Response responseGetAuth = apiCoreRequests
            .makeGetRequestAuthAsSameUser("https://playground.learnqa.ru/api/user/login", authData);

        String header = getHeader(responseGetAuth, "x-csrf-token");
        String cookie = getCookie(responseGetAuth, "auth_sid");

        Response responseUserData = apiCoreRequests
                .makeGetRequestAuthAsOtherUser("https://playground.learnqa.ru/api/user/3", cookie, header);


        System.out.println(responseUserData.asString());

        String expectedField = "username";
        String[] unexpectedFields = {"firstName", "lastName", "email"};
        Assertions.assertJsonHasField(responseUserData, expectedField);
        Assertions.assertJsonHasNotField(responseUserData, Arrays.toString(unexpectedFields));
//        Assertions.assertJsonHasKey(responseUserData, "username");
//        Assertions.assertJsonHasNotKey(responseUserData, "firstName");
//        Assertions.assertJsonHasNotKey(responseUserData, "lastName");
//        Assertions.assertJsonHasNotKey(responseUserData, "email");
    }

}
