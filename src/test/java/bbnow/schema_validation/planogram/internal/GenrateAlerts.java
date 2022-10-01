package bbnow.schema_validation.planogram.internal;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.planogram.internal.GenrateAlertsApi;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GenrateAlerts extends BaseTest {


    @DescriptionProvider(author = "Tushar", description = "This testcase checks status code.", slug = "alert")
    @Test(groups = { "bbnow-payments", "bbnow-schema-validation", "bbnow"})
    public void getAllFcAlertTest() {
        AutomationReport report = getInitializedReport(this.getClass(), false);

        String xTracker = "99106072-9999-11ea-bb37-0242ac130002";

        GenrateAlertsApi genrateAlertsApi = new GenrateAlertsApi(xTracker, report);
        Response response = genrateAlertsApi.postGenrateAlerts();
        Assert.assertEquals(200, response.getStatusCode());
        report.log("Status code: " + response.getStatusCode(), true);
    }
}

