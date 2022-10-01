package bbnow.schema_validation.stitch.internal;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.order.external.GetOrderDetailsApi;
import msvc.stitch.internal.AveragePickTimeRoadTime;
import org.testng.annotations.Test;

public class AveragePickRoadTimeTest extends BaseTest {

    @DescriptionProvider(author = "Pranay", description = "This testcase validates response schema for Average Pick Road Time api.",slug = "Validate Average Pick Road Time api")
    @Test(groups = {"bbnow","bbnow-schema-validation","api-schema-validation","unstable"})
    public void orderDetailsTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);

        AveragePickTimeRoadTime averagePickTimeRoadTime=new AveragePickTimeRoadTime(report);
          Response response= averagePickTimeRoadTime.getAveragePickTimeRoadTime("schema//stitch//internal//average-pick-road-time-200.json");
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("Average Pick Road Time response: " + response.prettyPrint(),true);
        response= averagePickTimeRoadTime.getAveragePickTimeRoadTime();
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("Average Pick Road Time response: " + response.prettyPrint(),true);

    }
}
