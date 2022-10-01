package bbnow.schema_validation.planogram.external;

import api.warehousecomposition.planogram_FC.AdminCookie;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.planogram.external.fcID.TaskLevelPickingApi;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.UUID;

public class TaskLevelPicking extends BaseTest {
    @DescriptionProvider(author = "Tushar", description = "This testcase validates response schema of api.",slug = "Task Level Picking")
    @Test(groups = {"bbnow" , "regression","bbnow-payments","bbnow-schema-validation","unstable"})

    public void putTaskLevelPickingApiTest()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);
        String xTracker = UUID.randomUUID().toString();
        String bbDecodedUid = "221435";
        String contentType = "application/json";
        String adminUserName = "anindra.m"; //todo
        String adminPassword = "password";  //todo

        Map<String , String> cookie = AdminCookie.getMemberCookie(adminUserName,adminPassword , report);
        String bbadminauthtoken = "BBADMINAUTHTOKEN=";
        bbadminauthtoken += cookie.get("BBADMINAUTHTOKEN");


        TaskLevelPickingApi taskLevelPickingApi= new TaskLevelPickingApi(xTracker , bbDecodedUid, contentType,bbadminauthtoken,report);
        Response response = taskLevelPickingApi.putTaskLevelPickingApi();
        Assert.assertEquals(response.getStatusCode(), 200);
        report.log("Status code: " +  response.getStatusCode(),true);
    }


}
