package foundation.hubops2.eta;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import org.testng.annotations.Test;

import java.util.Map;

public class HeaderFindEta extends BaseTest {

    @DescriptionProvider(slug = "header api find-eta test",
            description = "Verify header api is returning correct eta.",
            author = "rakesh")
    @Test(groups = {"bb2foundation", "hubops2"})
    public void findEtaApiTest(Map<String, Object> data) {
        AutomationReport report = getInitializedReport(this.getClass(), data.get("name").toString(), false, Map.class);
        AutomationUtilities.executeMicroserviceDatabaseQuery("HQA-PAYMENT_SERVICE","query");
        //call bigbasket homepage to set basic cookies.

        //find a member email in the given contact hub

        //generate otp for the member from web
        //fetch otp for member
        //login using the otp
        // get address list /mapi/v3.5.1/address/list/?send_partial=0
        //find the address belonging to given contact hub, is partial false. Get address id
        //use address id & call /mapi/v3.5.2/set-current-address/ to get latest cookies.
        //Use latest cookies to call header api & get eta data
        //verify all the etas using regex
    }


}
