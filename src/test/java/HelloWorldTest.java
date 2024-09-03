import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.*;

import static java.lang.Thread.sleep;


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
}