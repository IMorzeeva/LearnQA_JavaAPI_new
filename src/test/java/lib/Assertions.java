package lib;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Assertions {
    public static void assertResponseTextEquals(Response Response, String expectedAnswer){
        assertEquals(
                expectedAnswer,
                Response.asString(),
                "Response text is not expected"
        );
    }


        public static void assertResponseCodeEquals(Response Response, int expectedStatusCode){
            assertEquals(
                    expectedStatusCode,
                    Response.statusCode(),
                    "Response status code is not expected"
            );
        }

    public static void assertResponseTextContains(Response Response, String expectedAnswer){
        assertTrue(expectedAnswer.contains("field is too short"));
    }

    public static void assertJsonHasField(Response Response, String expectedFieldName) {
        Response.then().assertThat().body("$", hasKey(expectedFieldName));
    }

    public static void assertJsonHasNotField(Response Response, String unexpectedFieldName){
        Response.then().assertThat().body("$", not(hasKey(unexpectedFieldName)));

    }

    public static void assertJsonHasFields (Response Response, String[] expectedFieldNames){
        for (String expectedFieldName : expectedFieldNames){
            Assertions.assertJsonHasField(Response, expectedFieldName);
        }
    }

    public static void assertJsonNotHasFields (Response Response, String[] unexpectedFieldNames){
        for (String unexpectedFieldName : unexpectedFieldNames){
            Assertions.assertJsonHasNotField(Response, unexpectedFieldName);
        }
    }

    public static void assertJsonByName(Response Response, String name, String expectedValue) {
        Response.then().assertThat().body("$", hasKey(name));

        String value = Response.jsonPath().getString(name);
        assertEquals(expectedValue, value, "JSON value is not equal to expected value");
    }

    public static void assertJsonByMessage(Response Response, String message, String expectedValue) {
        Response.then().assertThat().body("$", hasKey(message));

        String value = Response.jsonPath().getString(message);
        assertEquals(expectedValue, value, "Invalid email format");
    }


    public static void assertJsonByMessage(String message, String invalidEmailFormat) {
    }
}
