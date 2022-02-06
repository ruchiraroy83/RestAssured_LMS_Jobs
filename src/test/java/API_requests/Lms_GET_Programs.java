package API_requests;

import Util.ExcelUtil;
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


import static Util.UtilConstants.*;
import static io.restassured.RestAssured.given;

public class Lms_GET_Programs {
    Properties prop;
    public Lms_GET_Programs() throws Exception {
        prop = PropertyFileReader.readPropertiesFile();

    }
    @DataProvider
    public Object[][] getData() throws IOException {
        String path = "./src/test/resources/TestData/LMS_InputData.xlsx";
        int rowCount = ExcelUtil.getRowCount(path,CONST_LMSGETSHEET);
        int colCount = ExcelUtil.getCellCount(path,CONST_LMSGETSHEET,rowCount);
        String getprogData[][] = new String[rowCount][colCount];

        for (int i=1;i<=rowCount;i++){
            for(int j=0;j<colCount;j++) {
                getprogData[i-1][j]=ExcelUtil.getCellData(path,CONST_LMSGETSHEET,i,j);
                System.out.println(getprogData);
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


        System.out.println(response.asString());
        Assert.assertEquals(status_code,200);

    }


    @Test(dataProvider = "getData")
    public void get_program_id(String programId, String StatusCode) {
        RestAssured.baseURI=prop.getProperty(CONST_URL);
        RestAssured.basePath=prop.getProperty(CONST_PATH);
        Response response = given().auth().basic(prop.getProperty(CONST_USERNAME),prop.getProperty(CONST_PWD)).when().
                get(RestAssured.baseURI+RestAssured.basePath+ "/" + programId);

        JsonPath jsonpathEvaluator = response.jsonPath();
        int pid = jsonpathEvaluator.get(PROG_ID);
        System.out.println(StatusCode);

        int status_code_actual = response.getStatusCode();
        System.out.println(" The Program ID to be retrieved is :" +programId );
        Reporter.log("ProgramId received from Response " + pid);

        Assert.assertNotNull(pid);
        Assert.assertNotNull(Integer.parseInt(programId)==pid);
        Assert.assertEquals(response.statusCode(),Integer.parseInt(StatusCode));
    }

}

