package bbnow.schema_validation.order;

import com.bigbasket.automation.Config;
import com.bigbasket.automation.mapi.mapi_4_1_0.OrderPlacement;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.order.internal.OrderListApi;
import org.testng.annotations.Test;

import java.util.ArrayList;

public class OrderList extends BaseTest {

    @DescriptionProvider(author = "Pranay", description = "This testcase validates response schema for OrderList api.",slug = "Validate OrderList api")
    @Test(groups = {"bbnow" , "regression","bbnow-schema-validation","api-schema-validation","test2"})
    public void orderListTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);
        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName, Config.bbnowConfig.getString("bbnow_stores[1].member_sheet_name"));

        report.log("Starting testcase for order placement.", true);
        String searchTerm1 = Config.bbnowConfig.getString("bbnow_stores[1].search_term1");
        String searchTerm2 = Config.bbnowConfig.getString("bbnow_stores[1].search_term2");
        String[] searchTerms = {searchTerm1,searchTerm2};
        String areaName = Config.bbnowConfig.getString("bbnow_stores[1].area");

        OrderPlacement.placeBBNowOrder("bbnow" , 10 , creds[0], areaName, 1, "ps",searchTerms, true, false, report);

        String xCaller = "bb-b2c";//todo
        String xService="bb-b2c";//todo
        String xEcId = "10";//todo
        String ec = "bb-b2c";//todo
        ArrayList<Integer> eclist=new ArrayList<Integer>();
        eclist.add(100);
        eclist.add(10);
        int bb_decoded_mid = Integer.parseInt(AutomationUtilities.getMemberId(serverName, creds[1]));
        OrderListApi orderListApi =new OrderListApi(xService,xCaller,xEcId,ec,bb_decoded_mid,report);
        Response response=orderListApi.getorderList("schema//order-svc//order-list-200.json",eclist);

         report.log("Status code: " +  response.getStatusCode(),true);
        report.log("orderlist response: " + response.prettyPrint(),true);
    }
}
