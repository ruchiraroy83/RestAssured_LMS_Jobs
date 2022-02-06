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

public class Lms_DELETE_Programs {
    Properties prop;

    public Lms_DELETE_Programs() throws Exception {
        prop = PropertyFileReader.readPropertiesFile();

    }
    @DataProvider
    public Object[][] deleteData() throws IOException {
        String path = "./src/test/resources/TestData/LMS_InputData.xlsx";
        int rowCount = ExcelUtil.getRowCount(path,CONST_LMSDELETESHEET);
        int colCount = ExcelUtil.getCellCount(path,CONST_LMSDELETESHEET,rowCount);
        String getprogData[][] = new String[rowCount][colCount];

        for (int i=1;i<=rowCount;i++){
            for(int j=0;j<colCount;j++) {
                getprogData[i-1][j]=ExcelUtil.getCellData(path,CONST_LMSDELETESHEET,i,j);
                System.out.println(getprogData);
            }
        }

        return getprogData;

    }

    @Test(dataProvider = "deleteData")
    public void delete_program_id(String programId, String StatusCode) {
        RestAssured.baseURI=prop.getProperty(CONST_URL);
        RestAssured.basePath=prop.getProperty(CONST_PATH);
        Response response = given().auth().basic(prop.getProperty(CONST_USERNAME),prop.getProperty(CONST_PWD)).when().
                delete(RestAssured.baseURI+RestAssured.basePath+ "/" + programId);
        System.out.println(" The Program ID to be deleted is :" +programId );
        System.out.println(" The Response Status Code is :" +response.statusCode());
        Assert.assertEquals(response.statusCode(),Integer.parseInt(StatusCode));
    }
}
