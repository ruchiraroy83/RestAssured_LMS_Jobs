package LMS_API_requests;
import static Util.LMSUtilConstants.CONST_EXCELFILEPATH;
import static Util.LMSUtilConstants.CONST_LMSPUTSHEET;
import static Util.LMSUtilConstants.CONST_PATH;
import static Util.LMSUtilConstants.CONST_PWD;
import static Util.LMSUtilConstants.CONST_PostSchemaFilePath;
import static Util.LMSUtilConstants.CONST_URL;
import static Util.LMSUtilConstants.CONST_USERNAME;
import static Util.LMSUtilConstants.Success_Status;
import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.util.Properties;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import Util.ExcelUtil;
import Util.JSON_Schema_Validation;
import Util.PropertyFileReader;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class Lms_PUT {
    Properties prop;
    String path;
    int RowNum=0;
    public Lms_PUT() throws Exception {
        prop = PropertyFileReader.readPropertiesFile("LMS");
        path = prop.getProperty(CONST_EXCELFILEPATH);

    }

    @DataProvider
    public Object[][] getData() throws IOException {
        int rowCount = ExcelUtil.getRowCount(path,CONST_LMSPUTSHEET);
        int colCount = ExcelUtil.getCellCount(path,CONST_LMSPUTSHEET,rowCount);
        String getpostData[][] = new String[rowCount][colCount];
        for (int i=1;i<=rowCount;i++){
            for(int j=0;j<colCount;j++) {
                getpostData[i-1][j]=ExcelUtil.getCellData(path,CONST_LMSPUTSHEET,i,j);
            }
        }

        return getpostData;

    }


    @SuppressWarnings("unchecked")
	@Test(dataProvider = "getData")
    public void Put_Method(String Scenario,String programId,String programName,String programDesc,String OnlineStat,String Status_code_expected) throws IOException {
        RestAssured.baseURI=prop.getProperty(CONST_URL);
        RestAssured.basePath=prop.getProperty(CONST_PATH);
        JSONObject requestParam=new JSONObject();
//        requestParam.put("programId", Integer.parseInt(programId));
        if (programName!=null) {
        	requestParam.put("programName", programName);
        }
        if (programDesc!=null) {
        	requestParam.put("programDescription", programDesc);
        }

        if(OnlineStat!=null) {
        	requestParam.put("online", Boolean.valueOf(OnlineStat));
        }
        System.out.println(requestParam.toJSONString());
        Response response = given().auth().preemptive().basic(prop.getProperty(CONST_USERNAME),prop.getProperty(CONST_PWD)).header("Content-Type", "application/json").body(requestParam.toJSONString()).put(RestAssured.baseURI+RestAssured.basePath +"/"+Integer.parseInt(programId));
        System.out.println(response.getBody().asPrettyString());
        Reporter.log("For scenario :" + Scenario +". the Response body is :" + response.getBody().asPrettyString());

        Assert.assertEquals(Integer.parseInt(Status_code_expected),response.getStatusCode(),"Status Code Validation");
        //Validating input with output
        if (Status_code_expected.equals(Success_Status)){
            Assert.assertEquals(response.jsonPath().get("programId").toString(),programId,"programId Assertion :");
            if (programName!=null) {
            	Assert.assertEquals(response.jsonPath().get("programName"),programName,"programName Assertion :");
            }
            if (programDesc!=null) {
            	Assert.assertEquals(response.jsonPath().get("programDescription"),programDesc,"programDescription  Assertion :");
            }
           
            if (OnlineStat!=null) {
            	 Assert.assertEquals(response.jsonPath().get("online"), Boolean.valueOf(OnlineStat),"online Assertion :");
            }
            JSON_Schema_Validation.cls_JSON_SchemaValidation(response,
                    prop.getProperty(CONST_PostSchemaFilePath));
           
        }

    }

}
