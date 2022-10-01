package bbnow.testcases.eta.find_sas;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.eta.internal.FindSasApi;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FindSasWIthInvalidField extends BaseTest {

    @DescriptionProvider(author = "tushar", description = "Api throws 400 when provided with invalid field", slug = "Check Api Throws 400")
    @Test(groups = { "bbnow" , "regression", "regression"})
    public void findSasWithInvalidFieldTest()
    {
        // Providing wrong lat-long

        AutomationReport report = getInitializedReport(this.getClass(),false);

        String xService="Serviceability";//todo
        String entrycontextid="10";
        String entrycontext="bbnow";
        String lng = "abc";//todo
        String lat = "13.026215";//todo


        FindSasApi findSasApi =new FindSasApi(entrycontext,entrycontextid,xService,lng,lat,report);

        Response response = findSasApi.findSAS();
        Assert.assertEquals(response.getStatusCode(), 400);
        report.log("Status Code: "+ response.getStatusCode(),true);


    }

}
