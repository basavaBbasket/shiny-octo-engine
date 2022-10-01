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
    @Test(groups = {"bbnow-schema-validation","bbnow"})
    public void fcReceivingListApiTest()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String status = "receiving_complete";
        String source_fc_id = "2";
        String order_type = "longtail";
        String order_id = "3";
        String creation_ts = "30-09-2021 17:51:53";



        FcReceivingListApi fcReceivingListApi = new FcReceivingListApi(status,source_fc_id,order_type,order_id,creation_ts,report);
        Response response = fcReceivingListApi.getFcReceivingList("schema//wio//fc-receiving-list-api-200.json");
        report.log("Status code: " +  response.getStatusCode(),true);
    }

}
