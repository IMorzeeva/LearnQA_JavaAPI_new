package lib;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.Cookies;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.apache.http.cookie.Cookie;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ApiCoreRequests {
    @Step("Make a POST-request with uncorrect email: without @")
    public Response makePostRequest () {
        Map<String, String> userData = new HashMap<>();
        userData.put("email", "vinkotov_example.com");
        userData.put("password", "123");
        userData.put("username", "learnqa");
        userData.put("firstName", "learnqa");
        userData.put("lastName", "learnqa");

        return RestAssured.given()
                .filter(new AllureRestAssured())
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")

                .andReturn();
    }

    @Step("Make a POST-request with all required field")
    public Response makePostRequestRequiredField (Map<String, String> userData) {

        return RestAssured.given()
                .filter(new AllureRestAssured())
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")

                .andReturn();
    }

    @Step("Make a POST-request with short username")
    public Response makePostRequestUsernameShort ( Map<String, String> userData) {

        return RestAssured.given()
                .filter(new AllureRestAssured())
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")

                .andReturn();
    }

    @Step("Make a POST-request with long username")
    public Response makePostRequestUsernameLong (Map<String, String> userData) {

        return RestAssured.given()
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

        return RestAssured.given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .andReturn();

    }

    @Step("Authorization as other user: getting user details")
    public Response makeGetRequestAuthAsOtherUser (String url, String cookie, String header) {
        return RestAssured.given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", header))
                .cookie("auth_sid", cookie)
//                .cookies(new Cookies("auth_sid", cookie))
//                .cookies("auth_sid", cookie)
                .get(url)
                .andReturn();

    }



}
