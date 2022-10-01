package bbnow.schema_validation.order;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.order.external.GetOrderDetailsApi;
import msvc.order.internal.OrderListApi;
import org.testng.annotations.Test;

import java.util.ArrayList;

public class OrderList extends BaseTest {

    @DescriptionProvider(author = "Pranay", description = "This testcase validates response schema for OrderList api.",slug = "Validate OrderList api")
    @Test(groups = {"bbnow","bbnow-schema-validation","api-schema-validation","test2"})
    public void orderListTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);
        String xCaller = "bb-b2c";//todo
        String xService="bb-b2c";//todo
        String xEcId = "10";//todo
        String ec = "bb-b2c";//todo
    int bb_decoded_mid=20051770;
        ArrayList<Integer> eclist=new ArrayList<Integer>();
        eclist.add(100);
        eclist.add(10);
        OrderListApi orderListApi =new OrderListApi(xService,xCaller,xEcId,ec,bb_decoded_mid,report);
        Response response=orderListApi.getorderList("schema//order-svc//order-list-200.json",eclist);

         report.log("Status code: " +  response.getStatusCode(),true);
        report.log("orderlist response: " + response.prettyPrint(),true);
    }
}
