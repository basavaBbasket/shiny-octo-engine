package bbnow.schema_validation.warehouse.inbound_outbound;

import com.bigbasket.automation.Config;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.wio.internal.stockchange.StockChange;
import org.testng.annotations.Test;

public class StockChangeTest extends BaseTest {
    @DescriptionProvider(author = "Pranay", description = "This testcase validates Fc Return api .",slug = "Validate Stock Change api")
    @Test(groups = {"bbnow" , "regression","dl2","catalogfc","bbnowvalidation","dl2validation","api-schema-validation","test1"})
    public void stockChangeApiTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);

        int sku_id =40001610;//todo
        int quantity = 10;//todo
        String locationtype="return";//todo
        String fc_id= Config.bbnowConfig.getString("bbnow_stores[0].fc_id");//todo

        report.log("fc details"+fc_id,true);
        String Cookie=" _bb_cid=1; _bb_vid=\"Mzk1NDA1MzM1Ng==\"; _bb_tc=0; _bb_rdt=\"MzE0NDc3NjE4NQ==.0\"; _bb_rd=6; _client_version=2748; sessionid=c7mtx66jcad0mfz5cl5z52yrz1mwd4lh; ts=\"2021-11-18 16:30:35.220\"";


        StockChange stockChange=new StockChange(fc_id,sku_id,quantity,locationtype,Cookie,report);

        Response response = stockChange.stockChange();
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("Fc return api: " + response.prettyPrint(),true);
    }
}
