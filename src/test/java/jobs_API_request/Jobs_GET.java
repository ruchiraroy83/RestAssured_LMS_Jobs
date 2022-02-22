package jobs_API_request;

import static Util.jobsUtilConstants.CONST_PATH;
import static Util.jobsUtilConstants.CONST_URL;
import static io.restassured.RestAssured.given;

import java.util.Properties;

import org.testng.Assert;
import org.testng.annotations.Test;

import Util.PropertyFileReader;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class Jobs_GET {
	Properties prop;

	public Jobs_GET() {
		prop = PropertyFileReader.readPropertiesFile("jobs");

	}

	@Test
	public void getallPrograms() {
		RestAssured.baseURI = prop.getProperty(CONST_URL);
		RestAssured.basePath = prop.getProperty(CONST_PATH);
		Response response = given().when().get(RestAssured.baseURI + RestAssured.basePath);
		int status_code = response.statusCode();

		System.out.println(response.asString());
		Assert.assertEquals(status_code, 200);

	}

}
