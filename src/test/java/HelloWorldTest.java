import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.*;


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
}
