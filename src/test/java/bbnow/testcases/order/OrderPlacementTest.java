package bbnow.testcases.order;


import com.bigbasket.automation.Config;
import com.bigbasket.automation.mapi.mapi_4_1_0.OrderPlacement;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import org.testng.annotations.Test;

public class OrderPlacementTest extends BaseTest {

    @DescriptionProvider(author = "Tushar", description = "This testcase places one simple bbnow order using WALLET prepaid", slug = "OrderPlacement using WALLET ")
    @Test(groups = {"bbnow" , "regression", "regression","bbnow-orderplacement"})
    public void OrderPlacementWallet() {
        AutomationReport report = getInitializedReport(this.getClass(), false);
        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName, Config.bbnowConfig.getString("bbnow_stores[1].member_sheet_name"));

        report.log("Starting testcase for order placement.", true);
        String searchTerm1 = Config.bbnowConfig.getString("bbnow_stores[1].search_term1");
        String searchTerm2 = Config.bbnowConfig.getString("bbnow_stores[1].search_term2");
        String[] searchTerms = {searchTerm1, searchTerm2};
        String areaName = Config.bbnowConfig.getString("bbnow_stores[1].area");

        String orderId = OrderPlacement.placeBBNowOrder("bbnow", 10, creds[0], areaName, 2, "ps", searchTerms, true, false, report);
        report.log("Order Id: " + orderId, true);
    }

    @DescriptionProvider(author = "Tushar", description = "This testcase places one simple bbnow order using NETBANKING", slug = "OrderPlacement using NETBANKING ")
    @Test(groups = {"bbnow", "regression", "regression"})
    public void OrderPlacementNetbanking() {
        AutomationReport report = getInitializedReport(this.getClass(), false);
        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName, Config.bbnowConfig.getString("bbnow_stores[1].member_sheet_name"));

        report.log("Starting testcase for order placement.", true);
        String searchTerm1 = Config.bbnowConfig.getString("bbnow_stores[1].search_term1");
        String searchTerm2 = Config.bbnowConfig.getString("bbnow_stores[1].search_term2");
        String[] searchTerms = {searchTerm1, searchTerm2};
        String areaName = Config.bbnowConfig.getString("bbnow_stores[1].area");

        OrderPlacement.placeBBNowOrder("bbnow", 10, creds[0], areaName, 1,"ps", searchTerms, false, false, report);
    }

}
