package bbnow.schema_validation.planogram.internal;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.planogram.internal.GenrateAlertsApi;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.UUID;

public class GenrateAlerts extends BaseTest {


    @DescriptionProvider(author = "Tushar", description = "This testcase checks status code.", slug = "alert")
    @Test(groups = { "bbnow","bbnow-payments", "bbnow-schema-validation"})
    public void getAllFcAlertTest() {
        AutomationReport report = getInitializedReport(this.getClass(), false);

        String xTracker = UUID.randomUUID().toString();
        String fc_id = "1"; //todo

        GenrateAlertsApi genrateAlertsApi = new GenrateAlertsApi(xTracker, report);
        Response response = genrateAlertsApi.postGenrateAlerts(fc_id);
        Assert.assertEquals(200, response.getStatusCode());
        report.log("Status code: " + response.getStatusCode(), true);
    }
}

