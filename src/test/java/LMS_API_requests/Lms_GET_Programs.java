package LMS_API_requests;

import Util.ExcelUtil;
import Util.JSON_Schema_Validation;
import Util.PropertyFileReader;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Properties;


import static Util.LMSUtilConstants.*;
import static io.restassured.RestAssured.given;

public class Lms_GET_Programs {
    Properties prop;
    public Lms_GET_Programs() throws Exception {
        prop = PropertyFileReader.readPropertiesFile("LMS");

    }
    @DataProvider
    public Object[][] getData() throws IOException {
        String path = prop.getProperty(CONST_EXCELFILEPATH);
        int rowCount = ExcelUtil.getRowCount(path,CONST_LMSGETSHEET);
        int colCount = ExcelUtil.getCellCount(path,CONST_LMSGETSHEET,rowCount);
        String getprogData[][] = new String[rowCount][colCount];

        for (int i=1;i<=rowCount;i++){
            for(int j=0;j<colCount;j++) {
                getprogData[i-1][j]=ExcelUtil.getCellData(path,CONST_LMSGETSHEET,i,j);

            }
        }

        return getprogData;

    }

    @Test
    public void getallPrograms() {
        RestAssured.baseURI=prop.getProperty(CONST_URL);
        RestAssured.basePath=prop.getProperty(CONST_PATH);
        Response response = given().auth().basic(prop.getProperty(CONST_USERNAME),prop.getProperty(CONST_PWD)).when().
                get(RestAssured.baseURI+RestAssured.basePath);
        int status_code = response.statusCode();
        System.out.println(response.asPrettyString());
        Reporter.log("Response is :" + response.asPrettyString() );
        Assert.assertEquals(status_code,200,"Status Code Validation");

    }


    @Test(dataProvider = "getData")
    public void get_program_id(String Scenario,String programId, String StatusCode) {
        RestAssured.baseURI=prop.getProperty(CONST_URL);
        RestAssured.basePath=prop.getProperty(CONST_PATH);
        Response response = given().auth().basic(prop.getProperty(CONST_USERNAME),prop.getProperty(CONST_PWD)).when().
                get(RestAssured.baseURI+RestAssured.basePath+ "/" + programId);
        System.out.println("Response is :" +response.asPrettyString());
        Reporter.log("Scenario is :"+Scenario+ ".And Response is :" + response.asPrettyString() );
        try {
            if(StatusCode.equals(Success_Status)) {
                int pid = response.jsonPath().get(PROG_ID);
                Assert.assertEquals(pid, Integer.parseInt(programId),"Validation of ProgramId");
                JSON_Schema_Validation.cls_JSON_SchemaValidation(response,
                        prop.getProperty(CONST_PostSchemaFilePath));
            }
        } catch(Exception e ){
            System.out.println("ProgramID is entered is not valid");
            Reporter.log("ProgramID is entered is not valid");
        }


        int status_code_actual = response.getStatusCode();
        System.out.println(" The Program ID to be retrieved is :" +programId +" for Scenario :" +Scenario);
        Reporter.log(" The Program ID to be retrieved is :" +programId +" for Scenario :" +Scenario);
        Assert.assertEquals(response.statusCode(),Integer.parseInt(StatusCode),"Status Code Validation");


    }

}

