package tms.oms;

import TMS.api.OMS.GetSlotsApi;
import TMS.configAndUtilities.*;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Random;

public class CheckEarliestSlot extends BaseTest {
    @DescriptionProvider(slug = "Earliest slots from get-slots api ", description = "Verify that Earliest slots are returned by get-slots API", author = "tushar")

    @Test(groups = {"TMS","OMS","OMS-slots"})
    public void createOrder() throws IOException, ParseException {

        AutomationReport report = getInitializedReport(this.getClass(), false);
        String env = AutomationUtilities.getEnvironmentFromServerName(serverName);
        Map<String, String> memberData = MemberData.getMemberDetailsMap(env.toUpperCase());
        Map<String ,String> headersData = CreateHeader.createHeader();
        Map<String,String> skuData = SkuData.getSkuDetailsMap(env.toUpperCase());
        Random rand = new Random();
        int external_reference_id=(int)rand.nextInt(1000000000);
        ArrayList<ArrayList<String>> slotsData = GetSlotsApi.getSlotsDates(report,headersData,memberData,skuData,external_reference_id);
        ArrayList<String> slotsDates = new ArrayList<>();
        for(int i = 0 ; i<slotsData.get(0).size() ; i++)
        {
            if(slotsData.get(0).get(i)!=null)
                slotsDates.add(slotsData.get(0).get(i) );
        }
        Assert.assertEquals(true,checkSlotDatesSorted(slotsDates),"Getting earliest slots first");
        report.log("getting earlier slots first",true);

    }

    private boolean checkSlotDatesSorted(ArrayList<String> slotsDates) throws ParseException {

        boolean isSorted = true;
        Date start = new SimpleDateFormat("yyyy-MM-dd")
                .parse(slotsDates.get(0));
        if(slotsDates.size()>1) {
            for (int i = 1; i < slotsDates.size(); i++) {
                Date current = new SimpleDateFormat("yyyy-MM-dd")
                        .parse(slotsDates.get(i));
                if(start.before(current)==false)
                {
                    return false;
                }

            }
        }

        return isSorted;
    }

}
