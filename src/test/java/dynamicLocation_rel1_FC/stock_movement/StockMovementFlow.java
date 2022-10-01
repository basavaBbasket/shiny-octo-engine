package dynamicLocation_rel1_FC.stock_movement;

import api.warehousecomposition.planogram_FC.AdminCookie;
import api.warehousecomposition.planogram_FC.EligibleSkuMethods;
import api.warehousecomposition.planogram_FC.InventoryMethods;
import api.warehousecomposition.planogram_FC.StockMovementMethods;
import com.bigbasket.automation.Config;
import com.bigbasket.automation.mapi.mapi_4_1_0.MemberCookie;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import framework.Settings;
import io.restassured.response.Response;
import msvc.DynamicLocationGeneralMethods;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Map;

public class StockMovementFlow extends BaseTest {
    @DescriptionProvider(slug = "StockMovement", description = "Test case to check Stock Movement " +
            "from 1. primary to secondary location \n 2. secondary to primary location \n 3. Primary location to Bulk \4 Bulk to Primary location", author = "vinay")
    @Test(groups = {"bbnow", "regression", "dlphase2", "stock_movement", "earlyrelease"})
    public void stockMovementFromPrimaryToSecondaryLocation() {
        AutomationReport report = getInitializedReport(this.getClass(), false);
        int fcId = Integer.parseInt(Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_fcid"));

        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName, Config.bbnowConfig.getString("bbnow_stores[1].member_sheet_name"));
        report.log("Starting  order placement.", true);
        String entrycontext= Config.bbnowConfig.getString("entry_context");
        String entrycontextid=Config.bbnowConfig.getString("entry_context_id");
        String clientUserSheetName = Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_client_user_sheet_name");
        String areaName = Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_area_name");
        //int fcId = Integer.parseInt(Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_fcid"));
        String searchTerm = Config.bbnowConfig.getString("bbnow_stores[1].category_slug1");
        String searchType = Config.bbnowConfig.getString("bbnow_stores[1].search_type2");

        String[] adminCred = AutomationUtilities.getUniqueAdminUser(serverName, "admin-superuser-mfa");
        String adminUserName = adminCred[0];
        String adminPassword = adminCred[1];
        Map<String, String> adminCookie = AdminCookie.getMemberCookie(adminUserName, adminPassword, report);

        String cred[] = AutomationUtilities.getUniqueLoginCredential(serverName, clientUserSheetName);
        Map<String,String> memberCookie = MemberCookie.getMemberCookieForCustomAddress(cred[0],areaName,report);

        int skuID = EligibleSkuMethods.fetchSkuVisibleAndAvailableInPrimaryBin(memberCookie,adminCookie,
                fcId,entrycontext,Integer.parseInt(entrycontextid),areaName,searchTerm,searchType,report);

        report.log("###Performing stock movement from primary to secondary###", true);
        String[] primaryBinLoc = DynamicLocationGeneralMethods.getBinLocationForGivenSkuIDAndfcID(skuID, fcId, 1, report);
        Response stockInfoResponse = InventoryMethods.stockInfoDetails(adminCookie, skuID, fcId, report);
        Double qtyInSecondaryLoc = Double.valueOf(stockInfoResponse.path("secondary_qoh.total_qoh").toString());
        StockMovementMethods.moveStockFromPrimaryToSecondaryLocation(fcId, skuID, primaryBinLoc[0]
                , adminCookie, 1, adminUserName, report);
        stockInfoResponse = InventoryMethods.stockInfoDetails(adminCookie, skuID, fcId, report);
        Double qtyInSecondaryLocAfterStockMovement = Double.valueOf(stockInfoResponse.path("secondary_qoh.total_qoh").toString());
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(qtyInSecondaryLocAfterStockMovement > qtyInSecondaryLoc,
                "Qty is not updated in secondary location post stock movement");
        report.log("Stock Movement from primary to secondary location successful", true);


        report.log("###Performing stock movement from secondary to primary###", true);
        String[] secondaryBinLoc = DynamicLocationGeneralMethods.getBinLocationForGivenSkuIDAndfcID(skuID, fcId, 2, report);
        stockInfoResponse = InventoryMethods.stockInfoDetails(adminCookie, skuID, fcId, report);
        Double qtyInPrimaryLoc = Double.valueOf(stockInfoResponse.path("primary_qoh.total_qoh").toString());
        StockMovementMethods.moveStockFromSecondaryToPrimaryLocation(fcId, skuID, secondaryBinLoc[0]
                , adminCookie, 1, adminUserName, report);
        stockInfoResponse = InventoryMethods.stockInfoDetails(adminCookie, skuID, fcId, report);
        Double qtyInPrimaryLocAfterStockMovement = Double.valueOf(stockInfoResponse.path("primary_qoh.total_qoh").toString());
        softAssert.assertTrue(qtyInPrimaryLocAfterStockMovement > qtyInPrimaryLoc,
                "Qty is not updated in primary location post stock movement");
        report.log("Stock Movement from  secondary to primary location successful", true);


        report.log("###Performing stock movement from primary to bulk###", true);
        stockInfoResponse = InventoryMethods.stockInfoDetails(adminCookie, skuID, fcId, report);
        Double qtyInMiscLoc = Double.valueOf(stockInfoResponse.path("miscellaneous_qoh.total_qoh").toString());
        StockMovementMethods.moveStockFromPrimaryToBulkLocation(fcId, skuID, primaryBinLoc[0]
                , adminCookie, 1, adminUserName, report);
        stockInfoResponse = InventoryMethods.stockInfoDetails(adminCookie, skuID, fcId, report);
        Double qtyInMiscLocAfterStockMovement = Double.valueOf(stockInfoResponse.path("miscellaneous_qoh.total_qoh").toString());
        softAssert.assertTrue(qtyInMiscLocAfterStockMovement > qtyInMiscLoc,
                "Qty is not updated in Misc location post stock movement");
        report.log("Stock Movement from primary to misc location successful", true);


        report.log("###Performing stock movement from bulk to primary###", true);
        stockInfoResponse = InventoryMethods.stockInfoDetails(adminCookie, skuID, fcId, report);
        qtyInPrimaryLoc = Double.valueOf(stockInfoResponse.path("primary_qoh.total_qoh").toString());
        StockMovementMethods.moveStockFromBulkToPrimaryLocation(fcId, skuID, adminCookie, 1, adminUserName, report);
        stockInfoResponse = InventoryMethods.stockInfoDetails(adminCookie, skuID, fcId, report);
        qtyInPrimaryLocAfterStockMovement = Double.valueOf(stockInfoResponse.path("primary_qoh.total_qoh").toString());
        softAssert.assertTrue(qtyInPrimaryLocAfterStockMovement > qtyInPrimaryLoc,
                "Qty is not updated in primary location post stock movement");
        report.log("Stock Movement from  bulk to primary location successful", true);


        softAssert.assertAll();
    }
}
