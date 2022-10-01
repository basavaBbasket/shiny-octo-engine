package bbnow.testcases.eta.find_sas;

import bbnow.testcases.member.DataProviderClass;
import com.bigbasket.automation.Config;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import com.bigbasket.automation.utilities.Member;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.eta.admin.CheckStoreStatusApi;
import msvc.eta.internal.FindSasApi;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FindSas extends BaseTest {

    @DescriptionProvider(author = "tushar", description = "This TestCase verifies status and schema of find-sas", slug = "find sas response validation")
    @Test(groups = { "bbnow", "regression"})
    public void findSasTest()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String xService="Serviceability";//todo
        String entrycontextid="10";
        String entrycontext="bbnow";

        /**
         String member_id;
        Member m = AutomationUtilities.getUniqueMember(serverName,);
         String mobile_no = m.getMobile();

         JsonObject jsonObject = new JsonObject(AutomationUtilities.executeDatabaseQuery(serverName, "select id from member_member where mobile_no="+mobile_no +";"));
         if (jsonObject.getInteger("numRows") == 0)
         member_id = null;
         else
         member_id =  String.valueOf(jsonObject.getJsonArray("rows").getJsonObject(0).getInteger("id"));


         **/


        // req params for body
        String lng = "77.619987";//todo
        String lat = "13.026215";//todo

        FindSasApi findSasApi =new FindSasApi(entrycontext,entrycontextid,xService,lng , lat, report);

        Response response = findSasApi.findSAS("schema//eta//internal//find-sas-200.json");
        Assert.assertEquals(response.getStatusCode(), 200);
        report.log("Status Code: "+ response.getStatusCode(),true);


    }


}
