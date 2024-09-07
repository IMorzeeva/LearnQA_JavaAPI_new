import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Headers;

import java.util.*;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class HelloWorldTest {

    @Test
    public void testTheSecondmessage(){
        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();

        ArrayList<LinkedHashMap<String,String>> messages = response.get("messages");

        System.out.println(messages.get(1).get("message"));
    }

    @Test

    public void testUrlRedirect(){
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();

        response.prettyPrint();
        String locationHeader = response.getHeader("Location");
        System.out.println(locationHeader);

    }

    @Test
    public void testLongRedirect(){
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();

        int statusCode = response.getStatusCode();
        String locationHeader = response.getHeader("Location");

        while (statusCode == 301){
            response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(locationHeader)
                    .andReturn();

            locationHeader = response.getHeader("Location");
            statusCode = response.getStatusCode();

            System.out.println("Status: " + statusCode + " URL:" + locationHeader);
        }
    }

    @Test
    public void testLongTimeJob() {
        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        String token = response.get("token");
        int seconds = response.get("seconds");

        System.out.println("Token receiving:");
        System.out.println("Token: " + token);
        System.out.println("\nStatus checking:");

            JsonPath response1 = RestAssured
                    .given()
                    .queryParam("token", token)
                    .when()
                    .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                    .jsonPath();

            String status = response1.get("status");

        if (status.equals("Job is NOT ready")) {
            {
                try {
                    Thread.sleep(seconds * 1000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            };
            JsonPath response2 = RestAssured
                    .given()
                    .queryParam("token", token)
                    .when()
                    .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                    .jsonPath();

            String status1 = response2.get("status");
            String result = response2.get("result");
            System.out.println("Status: " + status1);
            System.out.println("Result: " + result);
        }
    }

    @Test
    public void passwordChecking() {
        String [] passwords = new String[69];
        passwords[0] = "password";
        passwords[1] = "123456";
        passwords[2] = "12345678";
        passwords[3] = "qwerty";
        passwords[4] = "abc123";
        passwords[5] = "monkey";
        passwords[6] = "1234567";
        passwords[7] = "letmein";
        passwords[8] = "trustno1";
        passwords[9] = "dragon";
        passwords[10] = "baseball";
        passwords[11] = "111111";
        passwords[12] = "iloveyou";
        passwords[13] = "master";
        passwords[14] = "sunshine";
        passwords[15] = "ashley";
        passwords[16] = "bailey";
        passwords[17] = "passw0rd";
        passwords[18] = "shadow";
        passwords[19] = "123123";
        passwords[20] = "654321";
        passwords[21] = "superman";
        passwords[22] = "qazwsx";
        passwords[23] = "michael";
        passwords[24] = "Football";
        passwords[25] = "123456789";
        passwords[26] = "12345";
        passwords[27] = "football";
        passwords[28] = "1234";
        passwords[29] = "1234567890";
        passwords[30] = "princess";
        passwords[31] = "adobe123[a]";
        passwords[32] = "welcome";
        passwords[33] = "login";
        passwords[34] = "admin";
        passwords[35] = "qwerty123";
        passwords[36] = "solo";
        passwords[37] = "1q2w3e4r";
        passwords[38] = "666666";
        passwords[39] = "photoshop[a]";
        passwords[40] = "1qaz2wsx";
        passwords[41] = "qwertyuiop";
        passwords[42] = "mustang";
        passwords[43] = "121212";
        passwords[44] = "starwars";
        passwords[45] = "access";
        passwords[46] = "flower";
        passwords[47] = "555555";
        passwords[48] = "lovely";
        passwords[49] = "7777777";
        passwords[50] = "!@#$%^&*";
        passwords[51] = "jesus";
        passwords[52] = "password1";
        passwords[53] = "hello";
        passwords[54] = "charlie";
        passwords[55] = "888888";
        passwords[56] = "696969";
        passwords[57] = "hottie";
        passwords[58] = "freedom";
        passwords[59] = "aa123456";
        passwords[60] = "ninja";
        passwords[61] = "azerty";
        passwords[62] = "loveme";
        passwords[63] = "whatever";
        passwords[64] = "donald";
        passwords[65] = "batman";
        passwords[66] = "zaq1zaq1";
        passwords[67] = "000000";
        passwords[68] = "123qwe";


String answer = "You are authorized";
int i = 0;

do
{

Map<String, String> body = new HashMap<>();
body.put("login","super_admin");
body.put("password", passwords[i]);

        Response response = RestAssured
                .given()
                .body(body)
                .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                .andReturn();

       String responseCookie = response.getCookie("auth_cookie");
       System.out.println("password: " + passwords[i]);
       System.out.println("auth_cookie: " + responseCookie);

        Map<String, String> cookies = new HashMap<>();
        body.put("login","super_admin");
        cookies.put("Cookie", responseCookie);

        Response responseAuth = RestAssured
                .given()
                .body(body)
                .cookies(cookies)
                .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                .andReturn();

        String message = responseAuth.htmlPath().getString("body");
        System.out.println(message);
        i++;
}

while (answer == "You are authorized");
        System.out.println(answer);


    }
 @Test
   public void testLenghtOfTheMessage(){
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();

        response.prettyPrint();
        String locationHeader = response.getHeader("Location");

        String str = locationHeader;
        int count = str.length();
        assertTrue(count>15, "Count of symbols <15");

     System.out.println("Count of symbols > 15");

    }
}