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
public class Lms_POST {
    Properties prop;
    String path;
    int RowNum=0;
    public Lms_POST() throws Exception {
        prop = PropertyFileReader.readPropertiesFile();
        path = prop.getProperty(CONST_EXCELFILEPATH);

    }

    @DataProvider
    public Object[][] getData() throws IOException {
        int rowCount = ExcelUtil.getRowCount(path,CONST_LMSPOSTSHEET);
        int colCount = ExcelUtil.getCellCount(path,CONST_LMSPOSTSHEET,rowCount);
        String getpostData[][] = new String[rowCount][colCount];
        for (int i=1;i<=rowCount;i++){
            for(int j=0;j<colCount;j++) {
                getpostData[i-1][j]=ExcelUtil.getCellData(path,CONST_LMSPOSTSHEET,i,j);
            }
        }

        return getpostData;

    }


    @Test(dataProvider = "getData")
    public void Post_Method(String Scenario,String body,String programId,String Status_code_expected) throws IOException {
        RestAssured.baseURI=prop.getProperty(CONST_URL);
        RestAssured.basePath=prop.getProperty(CONST_PATH);
        Response response = given().auth().preemptive().basic(prop.getProperty(CONST_USERNAME),prop.getProperty(CONST_PWD)).header("Content-Type", "application/json").body(body).post(RestAssured.baseURI+RestAssured.basePath);
        System.out.println(response.asString());

        System.out.println(Status_code_expected);

        int status_code_actual = response.getStatusCode();

        Assert.assertEquals(Integer.parseInt(Status_code_expected),status_code_actual);
        //Validating input with output
        if (Status_code_expected.equals(Success_Status)){
            Assert.assertEquals(body.contains(response.jsonPath().get(PROG_Name)),true);
            Assert.assertEquals(body.contains(response.jsonPath().get(PROG_desc)),true);
            Assert.assertEquals(body.contains(response.jsonPath().get(Online_Status).toString()),true);
            given().auth().preemptive().basic(prop.getProperty(CONST_USERNAME),prop.getProperty(CONST_PWD)).when().
                    delete(RestAssured.baseURI+RestAssured.basePath+"/" +response.jsonPath().get(PROG_ID));
        }

    }

}


