package test;

import api.warehousecomposition.planogram_FC.AdminCookie;
import api.warehousecomposition.planogram_FC.E2EMethods;
import api.warehousecomposition.planogram_FC.EligibleSkuMethods;
import api.warehousecomposition.planogram_FC.PlanogramAlertsMethods;
import com.bigbasket.automation.Config;
import com.bigbasket.automation.mapi.mapi_4_1_0.MemberCookie;
import com.bigbasket.automation.mapi.mapi_4_1_0.internal.BigBasketApp;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import com.google.zxing.NotFoundException;
import framework.BaseTest;
import org.testng.annotations.Test;
import utility.OrderUtils;
import utility.TMSUtility;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SampleTestClass extends BaseTest {
    @DescriptionProvider(slug = "dl test", description = "testing", author = "vinay")
    @Test(groups = "dlphase2", enabled = false)
    public void testingClass() throws InterruptedException {
        AutomationReport report = getInitializedReport(this.getClass(), false);
        int entrycontextid = Integer.parseInt(Config.bbnowConfig.getString("entry_context_id"));
        String entrycontext = Config.bbnowConfig.getString("entry_context");
        String clientUserSheetName = Config.bbnowConfig.getString("bbnow_stores[1].member_sheet_name");
        String areaName = Config.bbnowConfig.getString("bbnow_stores[1].area");
        int fcId = Integer.parseInt(Config.bbnowConfig.getString("bbnow_stores[1].fc_id"));
        String searchTerm = Config.bbnowConfig.getString("bbnow_stores[1].category_slug1");
        String searchType = Config.bbnowConfig.getString("bbnow_stores[1].search_type2");

        String cred[] = AutomationUtilities.getUniqueLoginCredential(serverName, clientUserSheetName);
        Map<String, String> memberCookie = MemberCookie.getMemberCookieForCustomAddress(cred[0], areaName, report);

        String[] adminCred = AutomationUtilities.getUniqueAdminUser(serverName, "admin-superuser-mfa");
        String adminUserName = adminCred[0];
        String adminPassword = adminCred[1];
        Map<String, String> adminCookie = AdminCookie.getMemberCookie(adminUserName, adminPassword, report);

        int skuID = EligibleSkuMethods.fetchSkuVisibleAndAvailableInPrimaryBin(memberCookie, adminCookie,
                fcId, entrycontext, entrycontextid, areaName, searchTerm, searchType, report);

        HashMap<String, Integer> skuMap = new HashMap<>();
        skuMap.put(String.valueOf(skuID), 2);

        int orderId = E2EMethods.performBBnowOrderPlacementToBinning(entrycontext, entrycontextid, cred[0], areaName, skuMap,
                false, false, fcId, adminUserName, adminCookie, report);
    }

    @DescriptionProvider(slug = "commonStoreQRCode", description = "commonStoreQRCode", author = "vinay")
    @Test(groups = "commonStoreQRCode", enabled = true)
    public void commonStoreQRCode() throws InterruptedException, IOException, NotFoundException {
        AutomationReport report = getInitializedReport(this.getClass(), false);
        TMSUtility.getCommonStoreQRCode("BBNow_Shanthinikatan-B-SA", "BIKE", report);
    }

    @DescriptionProvider(slug = "ceeForceLogout", description = "ceeForceLogout", author = "vinay")
    @Test(groups = "ceeForceLogout", enabled = true)
    public void ceeForceLogout() throws InterruptedException, IOException, NotFoundException {
        AutomationReport report = getInitializedReport(this.getClass(), false);
        TMSUtility.ceeForceLogoutUsingCeeId("550001", report);
    }

    @DescriptionProvider(slug = "dl test", description = "testing", author = "vinay")
    @Test(groups = "dlphase2", enabled = false)
    public void acceptCeeCheckinRequest() throws InterruptedException, IOException, NotFoundException {
        AutomationReport report = getInitializedReport(this.getClass(), false);
        String[] adminCred = AutomationUtilities.getUniqueAdminUser(serverName, "admin-superuser-mfa");
        String adminUserName = adminCred[0];
        String adminPassword = adminCred[1];
        PlanogramAlertsMethods.actOnCeeCheckinRequest(550025,234,"cee_checkin_request","Accept",
                adminUserName,adminPassword,report);
    }


    @DescriptionProvider(slug = "dl test", description = "testing", author = "vinay")
    @Test(groups = "dlphase2", enabled = false)
    public void orderCancel() throws InterruptedException, IOException, NotFoundException {
        AutomationReport report = getInitializedReport(this.getClass(), false);

        String clientUserSheetName = Config.bbnowConfig.getString("bbnow_stores[1].member_sheet_name");
        String cred[] = AutomationUtilities.getUniqueLoginCredential(serverName, clientUserSheetName);
        String member_id= AutomationUtilities.getMemberIDForGivenID(cred[0],report);

        String entry_context= Config.bbnowConfig.getString("entry_context");
        String entry_context_id=Config.bbnowConfig.getString("entry_context_id");
        OrderUtils.cancelOrder("1001133909",entry_context,entry_context_id,"monolith",report,member_id);
        BigBasketApp app =new BigBasketApp(report);
        app.getAppDataDynamic().emptyCart();
    }

}
