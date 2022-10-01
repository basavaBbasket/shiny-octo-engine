package bbnow.schema_validation.order;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.order.internal.ValidatePaymentsApi;
import org.testng.annotations.Test;
//import sun.jvm.hotspot.debugger.cdbg.BaseClass;

public class ValidataPayments extends BaseTest {

    @DescriptionProvider(author = "Tushar", description = "This testcase validates response schema for payments.",slug = "Validate payment api")
    @Test(groups = {"t11","bbnow-payments","bbnow-schema-validation","bbnow"})
    public void validataPaymentsApiTest()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);


        String xEntryContext = "bb-b2c";
        String xEntryContextId = "100";
        String ContentType = "application/json";
       // String TimeStamp = "2020-08-19 14:42:41";
        String xTracker = "sync-280670-19992647-bbd-kk16da3f9fn1";
        String Service = "123";
        String bbDecodedMid= "785956";
        String xCaller = "123";


        ValidatePaymentsApi validatePaymentsApi = new ValidatePaymentsApi(xEntryContext, xEntryContextId ,ContentType,xTracker,Service,bbDecodedMid, xCaller, report);
        Response response = validatePaymentsApi.postValidatePaymentsData("schema//order-svc//validata-payments-api-200.json");
        report.log("Status code: " +  response.getStatusCode(),true);
    }


}
