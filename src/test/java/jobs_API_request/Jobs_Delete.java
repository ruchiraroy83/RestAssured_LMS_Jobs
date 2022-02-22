package jobs_API_request;
import static Util.jobsUtilConstants.CONST_EXCELFILEPATH;
import static Util.jobsUtilConstants.CONST_JOBSDELETESHEET;
import static Util.jobsUtilConstants.CONST_PATH;
import static Util.jobsUtilConstants.CONST_URL;
import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.util.Properties;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import Util.ExcelUtil;
import Util.PropertyFileReader;
import io.restassured.RestAssured;
import io.restassured.response.Response;
public class Jobs_Delete {
	Properties prop;

    public Jobs_Delete() throws Exception {
        prop = PropertyFileReader.readPropertiesFile("jobs");

    }
    @DataProvider
    public Object[][] deleteData() throws IOException {
        String path = prop.getProperty(CONST_EXCELFILEPATH);
        int rowCount = ExcelUtil.getRowCount(path,CONST_JOBSDELETESHEET);
        int colCount = ExcelUtil.getCellCount(path,CONST_JOBSDELETESHEET,rowCount);
        String getprogData[][] = new String[rowCount][colCount];

        for (int i=1;i<=rowCount;i++){
            for(int j=0;j<colCount;j++) {
                getprogData[i-1][j]=ExcelUtil.getCellData(path,CONST_JOBSDELETESHEET,i,j);
            }
        }

        return getprogData;

    }

    @Test(dataProvider = "deleteData")
    public void delete_job_id(String Scenario,String jobId, String StatusCode) {
        RestAssured.baseURI=prop.getProperty(CONST_URL);
        RestAssured.basePath=prop.getProperty(CONST_PATH);
        Response response = given().queryParam("Job Id", jobId).when().delete(RestAssured.baseURI+RestAssured.basePath);
        
        System.out.println(" The Jobs ID to be deleted is :" +jobId );
        System.out.println(" The Response Status Code is :" +response.statusCode());
        Assert.assertEquals(response.statusCode(),Integer.parseInt(StatusCode));
    }
}


