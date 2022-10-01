package bbnow.schema_validation.planogram.external;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import msvc.planogram.external.fcID.StackingAlternateBinAPI;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class StackingAlternateBin extends BaseTest {
    @DescriptionProvider(author = "Vinay", description = "This testcase validates response schema for Stacking Alternate Bin api.",slug = "Validate Stacking Alternate Bin api")
    @Test(groups = {"bbnow","bbnow-schema-validation","api-schema-validation","unstable"})
    public void StackingAlternateBinApiTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);
        String expectedResponseSchemaPath = "schema//planogram//stacking-alternate-bin-200.json";
        // Header Variables
        String xCaller = "IQ-APP"; //TODO

        // Cookie
        Map<String,String> cookie = new HashMap<String, String>() {{
            put("_bb_tc", "0");
            put("_bb_vid", "Mzk1MzQ1NzIxNQ==");
            put("_bb_rdt", "MzEyOTAwNTc2NA==.0");
            put("_bb_rd", "6");
            put("_ga", "GA1.2.404602991.1629276765");
            put("bigbasket.com", "7ea2c7c5-85e6-4a5d-87db-49d7d92076ff");
            put("_bb_locSrc", "default");
            put("_bb_cid", "1");
            put("_bb_aid", "MzAwNDkxOTI2MA==");
            put("_sp_van_encom_hid", "1722");
            put("_bb_hid", "1723");
            put("_sp_bike_hid", "1720");
            put("_bb_uid", "MzE0OTkzMTg2Mw==");
            put("BBADMINAUTHTOKEN", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjaGFmZiI6IkwzMk9WTXQwMC1kM3J3IiwidWlkIjoxMDY4MSwidGltZSI6MTYzNzgzMDI4OS4zMzgxMzJ9.d17vKSh35Kb2KT2ylpqzUoDT9MesvTJZ_96TevYPnlA");
            put("_client_version", "2021.10.25.55624");
            put("csrftoken", "3L31lHFQ76piWSdAZ4587qkDVulzPvrbuUOx3AOzF1PcPblWrvsp0clUUsDnftD2");
            put("sessionid", "8hqga8cv3p1cfqzxyqxsza5wy76zluob");
            put("csurftoken", "ka2hMQ==.MzE0OTkzMTg2Mw==.1637842755062.IBWJHR90mbk0zzAgWaDw4ubWSrwvh+mKKja+7ZYJ9i4=");
            put("ts", "2021-11-25 16:13:16.729");
        }}; //TODO

        JsonObject body= new JsonObject();
        JsonArray jsonArray = new JsonArray().add(
                new JsonObject()
                        .put("sku_id", "10000103")
                        .put("pending_quantity", "5")
        );
        body.put("skus", jsonArray);

        StackingAlternateBinAPI stackingAlternateBinAPI = new StackingAlternateBinAPI(xCaller, report);
        Response response = stackingAlternateBinAPI.PostStackingAlternateBinAPI(body, cookie, expectedResponseSchemaPath);
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("Orders Count Response: " + response.prettyPrint(),true);
    }
}