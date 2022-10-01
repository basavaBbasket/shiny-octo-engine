package bbnow.testcases.eta.find_sas;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.eta.internal.FindSasApi;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FindSasWithIncompleteRequest extends BaseTest {
    @DescriptionProvider(author = "tushar", description = "Api throws 401 when all required parameters are not passed", slug = "Check Api Throws 401")
    @Test(groups = { "bbnow", "regression"})
    public void findSasWithIncompleteRequestTest()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String xService="Serviceability";//todo
        String entrycontextid="10";
        String entrycontext="bbnow";

        // req params for body
        String lng = "77.619987";//todo
        String lat = "13.026215";//todo

        FindSasApi findSasApi =new FindSasApi(entrycontext,entrycontextid,xService,lng , lat, report);

        Response response = findSasApi.findSASWithIncompleteHeaders();
        Assert.assertEquals(response.getStatusCode(), 401);
        report.log("Status Code: "+ response.getStatusCode(),true);

    }


}
