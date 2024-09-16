package lib;

import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;

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


}
