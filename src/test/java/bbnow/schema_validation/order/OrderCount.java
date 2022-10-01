package bbnow.schema_validation.order;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.order.internal.OrderCountAPI;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class OrderCount extends BaseTest {
    @DescriptionProvider(author = "Himanshu", description = "This testcase validates response schema for orders count api.",slug = "Validate order count api")
    @Test(groups = {"bbnow","bbnow-schema-validation","api-schema-validation"})
    public void orderCountApiTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);
        String expectedResponseSchemaPath = "schema//order-svc//order-count-200.json";

        // Header Variables
        String xEntryContext = "bbnow"; //TODO
        String xEntryContextId = "10"; //TODO
        String xService = "123"; //TODO
        String xCaller = "123"; //TODO

        Map<String,String> cookie = new HashMap<String, String>() {{
            put("_bb_locSrc", "default");
            put("_bb_loid", "302");
            put("_bb_nhid", "1723");
            put("_bb_vid", "Mzk1MzQ1NzIxNQ==");
            put("_bb_dsid", "1720");
            put("bb_home_cache", "a16a5193.3630.visitor");
            put("_bb_tc", "0");
            put("_bb_rdt", "\"MzE0MDY1MTU0OQ==.0\"");
        }}; //TODO

        // Body Params
        String sa_id = "3"; //TODO
        String slot_date = "2021-11-17"; //TODO
        String dm_id = "3"; //TODO
        String[] order_statuses = {"open", "cancelled"}; //TODO

        OrderCountAPI orderCountAPI = new OrderCountAPI(xEntryContext, xEntryContextId, xService, xCaller, cookie, report);
        Response response = orderCountAPI.GetOrderCountAPI(sa_id, slot_date, dm_id, order_statuses, expectedResponseSchemaPath);
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("Orders Count Response: " + response.prettyPrint(),true);
    }
}
