package bbnow.schema_validation.wio.external;

import api.warehousecomposition.planogram_FC.AdminCookie;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.wio.external.GetReturningCeeListApi;

import org.testng.annotations.Test;

import java.util.Map;
import java.util.UUID;

public class GetReturningCeeList extends BaseTest {
    @DescriptionProvider(author = "Tushar", description = "This testcase validates the schema of API",slug = "Get Returning CEE List")
    @Test(groups = {"bbnow" , "regression","bbnow-schema-validation","api-schema-validation","unstable"})
    public void getReturnCeeListTest()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String xCaller = "1";
        String xTracker = UUID.randomUUID().toString();
        String UserId = "1";
        String fc_id = "1";//todo

        String adminUserName = "anindra.m"; //todo
        String adminPassword = "password";  //todo

        Map<String , String> cookie = AdminCookie.getMemberCookie(adminUserName,adminPassword , report);
        String bbadminauthtoken = "BBADMINAUTHTOKEN=";
        bbadminauthtoken += cookie.get("BBADMINAUTHTOKEN");



        GetReturningCeeListApi getReturningCeeListApi = new GetReturningCeeListApi(xTracker,xCaller,UserId,bbadminauthtoken,report);
        Response response = getReturningCeeListApi.getReturnCeeList("schema//wio//get-returning-cee-list-api-200.json",fc_id);
        report.log("Status code: " +  response.getStatusCode(),true);
    }
}
