package bbnow.testcases.order;


import com.bigbasket.automation.mapi.mapi_4_1_0.OrderPlacement;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import org.testng.annotations.Test;

public class OrderPlacementTest extends BaseTest {

    @DescriptionProvider(author = "rakesh", description = "This testcase places one simple bbnow order using WALLET prepaid", slug = "OrderPlacement using WALLET ")
    @Test(groups = {"bbnow99", "regression","bbnow-orderplacement"})
    public void OrderPlacementWallet() {
        AutomationReport report = getInitializedReport(this.getClass(), false);
        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName, "bbnow-seegehalli");

        report.log("Starting testcase for order placement.", true);
        String[] searchTerms = {"apple"};
        String bbnowArea = "Seegehalli";
        String orderId = OrderPlacement.placeBBNowOrder(creds[0], bbnowArea, 1, searchTerms, true, false, report);
        report.log("Order Id: " + orderId,true);
    }

    @DescriptionProvider(author = "rakesh", description = "This testcase places one simple bbnow order using NETBANKING", slug = "OrderPlacement using NETBANKING ")
    @Test(groups = {"bbnow", "regression"})
    public void OrderPlacementNetbanking() {
        AutomationReport report = getInitializedReport(this.getClass(), false);
        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName, "bbnow-seegehalli");

        report.log("Starting testcase for order placement.", true);
        String[] searchTerms = {"apple", "rice"};
        String bbnowArea = "Seegehalli";
        OrderPlacement.placeBBNowOrder(creds[0], bbnowArea, 1, searchTerms, false, false, report);
    }

}
