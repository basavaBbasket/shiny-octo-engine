package bbnow.testcases.picking;

import api.warehousecomposition.planogram_FC.AdminCookie;
import api.warehousecomposition.planogram_FC.internal.IQApp;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import framework.Settings;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class NegativeTestCasesRelatedToPicking extends BaseTest {
    @DescriptionProvider(slug = "orderId field not passed to ordercontainer api", description = "orderId field not passed to ordercontainer api \n" +
            "Assert: 400 status code should be returned with error msg \"Bad request body\"", author = "vinay")
    @Test(groups = {"bbnow", "regression", "dlphase2", "pickingFlow"})
    public void orderIdFiledNotPassedToOrderContainerApi() {
        AutomationReport report = getInitializedReport(this.getClass(), false);

        int fcId = Integer.parseInt(Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_fcid"));

        String[] adminCred = AutomationUtilities.getUniqueAdminUser(serverName, "admin-superuser-mfa");
        String adminUserName = adminCred[0];
        String adminPassword = adminCred[1];
        Map<String, String> cookie = AdminCookie.getMemberCookie(adminUserName, adminPassword, report);

        IQApp app = new IQApp(report);
        app.cookie.updateCookie(cookie);

        String bagLabel = String.valueOf(Instant.now().toEpochMilli());
        report.log("Container bag label: " + bagLabel, true);
        HashMap<String, Object> orderBagLinkBody = new HashMap<>();
        orderBagLinkBody.put("container_label", bagLabel);
        Response response = app.orderBagLinking(fcId, orderBagLinkBody, true);

        Assert.assertTrue(response.getStatusCode() == 400, "Status code is not 400, when order id field is missing");
        report.log("Status code is 400 is returned, when order id field is missing", true);

        Assert.assertTrue(response.path("errors[0].msg").toString().contains("Bad request body"), "Bad request body, msg is not displayed" +
                " when the order id field is missing in request body");
        report.log("Bad request body, msg is displayed, when the order id field is missing in request body", true);

    }


    @DescriptionProvider(slug = "empty orderId is passed to ordercontainer api", description = "empty orderId is passed to ordercontainer api \n" +
            "Assert: 400 status code should be returned with error msg \"Bad request body\"", author = "vinay")
    @Test(groups = {"bbnow", "regression", "dlphase2", "pickingFlow"})
    public void emptyOrderIdIsPassedToOrderContainerApi() {
        AutomationReport report = getInitializedReport(this.getClass(), false);

        int fcId = Integer.parseInt(Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_fcid"));

        String[] adminCred = AutomationUtilities.getUniqueAdminUser(serverName, "admin-superuser-mfa");
        String adminUserName = adminCred[0];
        String adminPassword = adminCred[1];
        Map<String, String> cookie = AdminCookie.getMemberCookie(adminUserName, adminPassword, report);

        IQApp app = new IQApp(report);
        app.cookie.updateCookie(cookie);

        String bagLabel = String.valueOf(Instant.now().toEpochMilli());
        report.log("Container bag label: " + bagLabel, true);
        HashMap<String, Object> orderBagLinkBody = new HashMap<>();
        orderBagLinkBody.put("order_id", null);
        orderBagLinkBody.put("container_label", bagLabel);
        Response response = app.orderBagLinking(fcId, orderBagLinkBody, true);

        Assert.assertTrue(response.getStatusCode() == 400, "Status code is not 400, when empty order id is passed");
        report.log("Status code is 400 is returned, when empty order id is passed", true);

        Assert.assertTrue(response.path("errors[0].msg").toString().contains("Bad request body"), "Bad request body, msg is not displayed" +
                " when the empty order id is passed");
        report.log("Bad request body, msg is displayed, when the empty order id is passed in request body", true);

    }

}
