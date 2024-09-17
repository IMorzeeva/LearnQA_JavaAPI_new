package tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.ApiCoreRequests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;


import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;

@Epic("Authorization cases")
@Feature("Authorization")

public class UserRegisterTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    public void TestCreateUserWithExistingEmail() {
        String email = "vinkotov@example.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("password", "123");
        userData.put("username", "learnqa");
        userData.put("firstName", "learnqa");
        userData.put("lastName", "learnqa");

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest();


        System.out.println(responseCreateAuth.asString());
        System.out.println(responseCreateAuth.statusCode());

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");

    }

    @Test
    @Description("This test unsuccessfully user registration with uncorrect email")
    @DisplayName("Test negative test: uncorrect email")
    public void testCreateUserWithUncorrectEmail() {

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest();

        System.out.println(responseCreateAuth.asString());
        System.out.println(responseCreateAuth.statusCode());

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Invalid email format");

    }

    private static Stream<Arguments> authData() {
        return Stream.of(
                Arguments.of("", "123", "learnqa", "learnqa", "learnqa"),
                Arguments.of("vinkotov@example.com", "", "learnqa", "learnqa", "learnqa"),
                Arguments.of("vinkotov@example.com", "123", "", "learnqa", "learnqa"),
                Arguments.of("vinkotov@example.com", "123", "learnqa", "", "learnqa"),
                Arguments.of("vinkotov@example.com", "123", "learnqa", "learnqa", "")
        );
    }

    @Description("This test checks user registration without required fields")
    @DisplayName("Test negative test: no required fields")
    @ParameterizedTest
    @MethodSource("authData")
    void testWithoutRequiredField(String email, String password, String username,
                                  String firstName, String lastName) {
        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("password", password);
        userData.put("username", username);
        userData.put("firstName", firstName);
        userData.put("lastName", lastName);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequestRequiredField(userData);

        System.out.println(responseCreateAuth.asString());
        System.out.println(responseCreateAuth.statusCode());

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextContains(responseCreateAuth, "field is too short");


    }

    @Description("This test checks the lenght of the field username: too short")
    @DisplayName("Test negative test: Username is too short")
    @Test
    public void testCreateUserUsernameTooShort() {
        Map<String, String> userData = new HashMap<>();
        userData.put("email", "vinkotov@example.com");
        userData.put("password", "123");
        userData.put("username", "l");
        userData.put("firstName", "learnqa");
        userData.put("lastName", "learnqa");

        Response responseCreateAuth = apiCoreRequests
                .makePostRequestUsernameShort(userData);

        System.out.println(responseCreateAuth.asString());
        System.out.println(responseCreateAuth.statusCode());

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'username' field is too short");

    }

    @Description("This test checks the lenght of the field username: too long")
    @DisplayName("Username is too long")
    @Test
    public void testCreateUserUsernameTooLong() {
        String outStr = apiCoreRequests.getRandomStr();
        Map<String, String> userData = new HashMap<>();
        userData.put("email", "vinkotov2@example.com");
        userData.put("password", "123");
        userData.put("username", outStr);
        userData.put("firstName", "learnqa");
        userData.put("lastName", "learnqa");

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        System.out.println(responseCreateAuth.asString());
        System.out.println(responseCreateAuth.statusCode());

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'username' field is too long");

    }
}
