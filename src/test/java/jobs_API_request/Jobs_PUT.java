package jobs_API_request;

import static Util.jobsUtilConstants.CONST_EXCELFILEPATH;
import static Util.jobsUtilConstants.CONST_JOBSPUTSHEET;
import static Util.jobsUtilConstants.CONST_PATH;
import static Util.jobsUtilConstants.CONST_URL;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Properties;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import Util.ExcelUtil;
import Util.PropertyFileReader;
import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

public class Jobs_PUT {
    Properties prop;

    public Jobs_PUT() throws Exception {
        prop = PropertyFileReader.readPropertiesFile("jobs");

    }
    @DataProvider
    public Object[][] putData() throws IOException {
        String path = prop.getProperty(CONST_EXCELFILEPATH);
        int rowCount = ExcelUtil.getRowCount(path,CONST_JOBSPUTSHEET);
        int colCount = ExcelUtil.getCellCount(path,CONST_JOBSPUTSHEET,rowCount);
        String getprogData[][] = new String[rowCount][colCount];

        for (int i=1;i<=rowCount;i++){
            for(int j=0;j<colCount;j++) {
                getprogData[i-1][j]=ExcelUtil.getCellData(path,CONST_JOBSPUTSHEET,i,j);

            }
        }

        return getprogData;

    }

    @Test(dataProvider="putData")
    public void updateJob(String Job_Id,String Job_Title,String Job_Company_Name,String Job_Location,
                      String Job_Type,String Job_Posted_time,String Job_Description,String StatusCode_expected) throws IOException, Throwable {
        //Specify base URI
        RestAssured.baseURI=prop.getProperty(CONST_URL);
        RestAssured.basePath=prop.getProperty(CONST_PATH);

        Response response= given().queryParam("Job Id", Job_Id).queryParam("Job Title", Job_Title).queryParam("Job Company Name", Job_Company_Name).
    			queryParam("Job Location",Job_Location).queryParam("Job Type",Job_Type).queryParam("Job Posted time", Job_Posted_time).
    			when().put(RestAssured.baseURI+RestAssured.basePath);
        String responsebody=response.getBody().asString();
        Assert.assertEquals(response.getStatusCode(),Integer.parseInt(StatusCode_expected));
        String responseBody = response.getBody().asPrettyString();
     	System.out.println("The Response Body is :"+responseBody);
     	Reporter.log("The Response Body is :"+responseBody);
		   if (Job_Id!=null) {
		   Assert.assertEquals(responsebody.contains(Job_Id),true,"Job Id assertion");
		   }
		   if (Job_Title!=null) {
		       Assert.assertEquals(responsebody.contains(Job_Title),true,"Job Title assertion");
		   }
		   if (Job_Company_Name!=null) {
		       Assert.assertEquals(responsebody.contains(Job_Company_Name),true,"Company Name assertion");
		   }
		   if (Job_Location!=null) {
		       Assert.assertEquals(responsebody.contains(Job_Location),true,"Job location assertion");
		   }
		   if (Job_Type!=null) {
		       Assert.assertEquals(responsebody.contains(Job_Type),true,"Job type assertion");
		   }
		   if (Job_Posted_time!=null) {
		       Assert.assertEquals(responsebody.contains(Job_Posted_time),true,"Job posted time assertion");
		   }
      
      responseBody = responseBody.replace("NaN", "null");
     

     assertThat("Schema Validation Failed",responseBody, JsonSchemaValidator.matchesJsonSchemaInClasspath("JSON_SCHEMAS\\JobsPutResponse.json"));

        
	 }
	 
}


