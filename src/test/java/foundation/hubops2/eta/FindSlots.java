package foundation.hubops2.eta;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import foundation.hubops2.FindSlotsApi;
import io.restassured.response.Response;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

import static foundation.hubops2.LoadProperties.configHubops2;

public class FindSlots extends BaseTest {

    @DescriptionProvider(slug = "find-slots api test",
            description = "Verify find-slots api is working or not",
            author = "rakesh")
    @Test(dataProviderClass = DataProviderClass.class, dataProvider = "slots-eta-data-provider", groups = {"bb2foundation", "hubops2", "slots"})
    public void findSlotsApiTest(Map<String, Object> data) {
        AutomationReport report = getInitializedReport(this.getClass(), data.get("name").toString(), false, Map.class);
        String memberAddressQuery = String.format(configHubops2.getProperty("member_address_sql"), data.get("contact_hub_id"));
        String frozenPathQuery = String.format(configHubops2.getProperty("frozen_path_sql"), data.get("src_fc"), data.get("dest_fc"));
        long memberAddressId = new JsonObject(AutomationUtilities.executeDatabaseQuery(serverName, memberAddressQuery))
                .getJsonArray("results")
                .getJsonArray(0).getLong(0);
        int frozenPathId = new JsonObject(AutomationUtilities.executeMicroserviceDatabaseQuery(AutomationUtilities.getEnvironmentFromServerName(serverName) + "-path", frozenPathQuery))
                .getJsonArray("results")
                .getJsonArray(0).getInteger(0);

        JsonObject jsonObject = new JsonObject();
        jsonObject.put("sa_id", data.get("sa_id"))
                .put("dm_id", data.get("dm_id"))
                .put("member_address_id", memberAddressId)
                .put("lmd_fc_id", data.get("dest_fc"))
                .put("frozen_path", true)
                .put("path_fc_list", new JsonArray().add(new JsonObject().put("path_id", frozenPathId).put("origin_fc_id", data.get("src_fc"))))
                .put("sub_area_ids", new JsonArray().add(1))
                .put("priority_slot_available", false);

        FindSlotsApi findSlotsApi = new FindSlotsApi(report);
        String body = jsonObject.toString();
        Response response = findSlotsApi.initiateFindSlotsRequest(body);
        Assert.assertEquals(response.statusCode(), 200, "Incorrect status code received for find-slots api.");

        JsonArray slots = new JsonArray(response.asString());
        Assert.assertTrue(slots.size() > 0, "No slots found in find-slots.");
    }
}
