package bbnow.schema_validation.wio.external;

import api.warehousecomposition.planogram_FC.AdminCookie;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.wio.external.GetReturningCeeListApi;
import msvc.wio.external.UpdateReturnReceivingApi;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.UUID;

public class UpdateReturnReceiving extends BaseTest {
    @DescriptionProvider(author = "Tushar", description = "This testcase validates the schema of API",slug = "Update Return Receving List")
    @Test(groups = {"bbnow-schema-validation","api-schema-validation"})
    public void getReturnCeeListTest()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String xCaller = "1";//todo
        String xTracker = UUID.randomUUID().toString();
        String UserId = "1";//todo
        String contentType = "application/json";

        String adminUserName = "anindra.m"; //todo
        String adminPassword = "password";  //todo

        Map<String , String> cookie = AdminCookie.getMemberCookie(adminUserName,adminPassword , report);
        String bbadminauthtoken = "BBADMINAUTHTOKEN=";
        bbadminauthtoken += cookie.get("BBADMINAUTHTOKEN");




        UpdateReturnReceivingApi updateReturnReceivingApi = new UpdateReturnReceivingApi(bbadminauthtoken,xTracker,xCaller,UserId,contentType,report);
        Response response = updateReturnReceivingApi.putUpdateReturnReceiving("schema//wio//update-return-receiving-200.json");
        report.log("Status code: " +  response.getStatusCode(),true);
    }
}
