package dynamicLocation_rel1_FC.stacking;

import api.warehousecomposition.planogram_FC.AdminCookie;
import api.warehousecomposition.planogram_FC.EligibleSkuMethods;
import api.warehousecomposition.planogram_FC.StackingMethods;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import framework.Settings;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class StackingFlow extends BaseTest {
    @DescriptionProvider(slug = "Stacking Flow", description = "Test case completes the stacking flow." +
            "\nCreates Grn Stacking Job -> Stacking Ack -> Stacking Complete ", author = "vinay")
    @Test(groups = {"bbnow" , "regression", "dlphase2", "stackingFlow", "earlyrelease"})
    public void validateStackingFlow() {
        AutomationReport report = getInitializedReport(this.getClass(), false);

        int totalQty = Integer.parseInt(Settings.dlConfig.getProperty("total_quantity"));
        int numOfContainers = Integer.parseInt(Settings.dlConfig.getProperty("num_of_containers"));
        int looseQty = Integer.parseInt(Settings.dlConfig.getProperty("loose_quantity"));
        int fcId = Integer.parseInt(Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_fcid"));

        String[] adminCred = AutomationUtilities.getUniqueAdminUser(serverName, "admin-superuser-mfa");
        String adminUserName = adminCred[0];
        String adminPassword = adminCred[1];
        Map<String, String> cookie = AdminCookie.getMemberCookie(adminUserName, adminPassword, report);

        HashMap<String, Object> stackingBody = new HashMap<>();
        stackingBody.put("total_quantity", totalQty);
        stackingBody.put("num_of_containers", numOfContainers);
        stackingBody.put("loose_quantity", looseQty);


        int skuId = EligibleSkuMethods.skuAvailableInPrimaryBin(cookie, fcId, report);

        StackingMethods.grnStackingFlow(fcId, skuId, adminUserName, stackingBody, cookie, report);
    }
}
