package lib;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.Cookies;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.cookie.Cookie;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class ApiCoreRequests {
    @Step("Make a POST-request with uncorrect email: without @")
    public Response makePostRequest () {
        Map<String, String> userData = new HashMap<>();
        userData.put("email", "vinkotov_example.com");
        userData.put("password", "123");
        userData.put("username", "learnqa");
        userData.put("firstName", "learnqa");
        userData.put("lastName", "learnqa");

        return given()
                .filter(new AllureRestAssured())
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")

                .andReturn();
    }

    @Step("Make a POST-request with all required field")
    public Response makePostRequestRequiredField (Map<String, String> userData) {

        return given()
                .filter(new AllureRestAssured())
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")

                .andReturn();
    }

    @Step("Make a POST-request with short username")
    public Response makePostRequestUsernameShort ( Map<String, String> userData) {

        return given()
                .filter(new AllureRestAssured())
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")

                .andReturn();
    }

    @Step("Make a POST-request with long username")
    public Response makePostRequestUsernameLong (Map<String, String> userData) {

        return given()
                .filter(new AllureRestAssured())
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")

                .andReturn();
    }

    public String getRandomStr(){
        Random random = new Random();
        String outStr = random.ints(48,122+1)
                .filter(i-> (i <= 57 || i >=65) && (i <= 90 || i >= 97))
                .limit(251)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return outStr;
    }

    @Step("Authorization as other user:login")
    public Response makeGetRequestAuthAsSameUser (String url, Map<String, String> authData) {

        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .andReturn();

    }

    @Step("Authorization as other user: getting user details")
    public Response makeGetRequestAuthAsOtherUser (String url, String cookie, String header) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", header))
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();

    }


    @Step("Make a PUT-request to change username")
    public Response makePutRequestChangeUsername (String url, Map<String, String> editData) {
        return given()
                .filter(new AllureRestAssured())
                .body(editData)
                .put(url)
                .andReturn();
    }

    @Step("Make a POST-request to generate user")
    public JsonPath makePostRequestGenerateUser (Map<String, String> userData) {
        return RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();
    }

    @Step("Make a POST-request to generate 2nd user")
    public JsonPath makePostRequestGenerateSecondUser (Map<String, String> userData2) {
        return RestAssured
                .given()
                .body(userData2)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();
    }

    @Step("Make a POST-request to login as 1st User")
    public Response makePostRequestLoginUser (String url, Map<String, String> authData) {
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .andReturn();

    }

    @Step("Make a POST-request to login as 2st User")
    public Response makePostRequestLogin2User (String url, Map<String, String> authData2) {
        return given()
                .filter(new AllureRestAssured())
                .body(authData2)
                .post(url)
                .andReturn();

    }


    @Step("Make a PUT-request to change firstName")
    public Response makePutRequestChangeFirstName (String url, Map<String, String> editData, String cookieValue, String headerValue) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", headerValue))
                .cookie("auth_sid", cookieValue)
                .body(editData)
                .put(url)
                .andReturn();
    }

    @Step("Make a GET-request to get userdata")
    public Response makeGetRequestGetUserData (String url, String cookieValue, String headerValue) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", headerValue))
                .cookie("auth_sid", cookieValue)
                .get(url)
                .andReturn();

    }

    @Step("Make a PUT-request to change email")
    public JsonPath makePutRequestToChangeEmail (String url, Map<String, String> editData, String cookieValue, String headerValue) {
        return RestAssured
                .given()
                .header(new Header("x-csrf-token", headerValue))
                .cookie("auth_sid", cookieValue)
                .body(editData)
                .put(url)
                .jsonPath();
    }

    @Step("Make a PUT-request to change firstname")
    public JsonPath makePutRequestToChangeFirstname (String url, Map<String, String> editData, String cookieValue, String headerValue) {
        return RestAssured
                .given()
                .header(new Header("x-csrf-token", headerValue))
                .cookie("auth_sid", cookieValue)
                .body(editData)
                .put(url)
                .jsonPath();
    }

}
