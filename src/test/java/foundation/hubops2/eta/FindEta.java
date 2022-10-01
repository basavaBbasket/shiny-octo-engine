package foundation.hubops2.eta;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import foundation.hubops2.FindEtaApi;
import io.restassured.response.Response;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

import static foundation.hubops2.LoadProperties.configHubops2;

public class FindEta extends BaseTest {

    @DescriptionProvider(slug = "find-eta api test",
            description = "Verify find-eta api is working or not",
            author = "rakesh")
    @Test(dataProviderClass = DataProviderClass.class, dataProvider = "slots-eta-data-provider", groups = {"bb2foundation", "hubops2", "slots"})
    public void findEtaApiTest(Map<String, Object> data) {
        AutomationReport report = getInitializedReport(this.getClass(), data.get("name").toString(), false, Map.class);
        String pathIdQuery = String.format(configHubops2.getProperty("path_id_sql"), data.get("src_fc"), data.get("dest_fc"));
        int pathId = new JsonObject(AutomationUtilities.executeMicroserviceDatabaseQuery(AutomationUtilities.getEnvironmentFromServerName(serverName) + "-path", pathIdQuery))
                .getJsonArray("results")
                .getJsonArray(0).getInteger(0);
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("sa_id", data.get("sa_id"))
                .put("dm_ids", new JsonArray().add(data.get("dm_id")))
                .put("path_id", pathId)
                .put("priority_slot_available", false)
                .put("lmd_fc_id", data.get("dest_fc"));

        FindEtaApi findEtaApi = new FindEtaApi(report);
        Response response = findEtaApi.initiateFindEtaRequest(new JsonArray().add(jsonObject).toString());
        Assert.assertEquals(response.statusCode(), 200, "Incorrect status code received for find-eta api.");
    }

}
