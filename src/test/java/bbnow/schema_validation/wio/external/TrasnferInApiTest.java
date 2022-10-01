package bbnow.schema_validation.wio.external;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.wio.external.TransferInApi;
import msvc.wio.internal.fcreturn.internal.FcReturnDetails;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class TrasnferInApiTest extends BaseTest {

    @DescriptionProvider(author = "Pranay", description = "This testcase validates TrasnferIn Api .",slug = "Validate TrasnferIn api")
    @Test(groups = {"bbnow" , "regression","dl2","bbnowvalidation","dl2validation","api-schema-validation"})
    public void trasnferInApiTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String xCaller = "1";//todo
        String Userid = "1";//todo


        String keys="stacking_details";
        String transferinid="1";



        TransferInApi transferInApi=new TransferInApi(Userid,xCaller,report);
        Response response = transferInApi.getTransferInApi("schema//wio//transfer-in-200.json",keys,transferinid);
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("Fc return api: " + response.prettyPrint(),true);
    }
}
