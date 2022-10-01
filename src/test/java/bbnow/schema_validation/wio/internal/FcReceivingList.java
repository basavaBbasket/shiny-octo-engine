package bbnow.schema_validation.wio.internal;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.wio.internal.FcReceivingListApi;
import org.testng.annotations.Test;


// Row-3
public class FcReceivingList extends BaseTest {

    @DescriptionProvider(author = "Tushar", description = "This testcase list down all the FCs received from.",slug = "FC Received List")
    @Test(groups = {"bbnow" , "regression","bbnow-schema-validation"})
    public void fcReceivingListApiTest()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String UserId = "1";//todo
        String X_Caller ="1";//todo

        //Query Params
        String status = "pending_receiving";//todo
        String source_fc_id = "1";//todo
        String order_type = "long_tail";//todo
        String order_id = "11";//todo
        String creation_ts = "27-10-2021%2017%3A09%3A27";//todo
        String fc_id = "1";//todo


        FcReceivingListApi fcReceivingListApi = new FcReceivingListApi(UserId , X_Caller, status,source_fc_id,order_type,order_id,creation_ts,report);
        Response response = fcReceivingListApi.getFcReceivingList("schema//wio//fc-receiving-list-api-200.json" , fc_id);
        report.log("Status code: " +  response.getStatusCode(),true);
    }

}
