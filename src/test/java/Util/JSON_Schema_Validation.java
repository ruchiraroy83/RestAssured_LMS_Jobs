package Util;

import org.hamcrest.Matchers;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class JSON_Schema_Validation {
    public static ValidatableResponse _resp;


    public static void cls_JSON_SchemaValidation(Response response, String fileName) {
        _resp = response.then();
        _resp.assertThat().body(Matchers.notNullValue())
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(fileName));
    }
}
