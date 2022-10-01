package bbnow.schema_validation.ui_assembler.header;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.replenishment.internal.GetPoDetails;
import msvc.ui_assembler.header.SendDoorInfoApi;
import org.testng.annotations.Test;

public class SendDoorInfoApiTest extends BaseTest {

    @DescriptionProvider(author = "Pranay", description = "This testcase validates response schema for SendDoorInfo api.",slug = "Validate SendDoorInfo api")
    @Test(groups = {"bbnow","dl2","eta","bbnow-schema-validation","dl2-schema-validation","api-schema-validation"})
    public void sendDoorInfoApiTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String cookie = "_bb_vid=MzkzMzE2MTI0NA==; _bb_source=app; ts=\"2021-11-30 15:23:46.015\"; csrftoken=TVnBZqinPWDnR1R5A07VI6L6QsqEHTH7OxiSklcE5P3wx5Rc03xjT3q3INbf7lgz; _bb_aid=Mjk5NjYwNTYxNg==; _bb_cid=1; csurftoken=HN50nA.MzkzMzE2MTI0NA==.1638265981511.sgydz1lMnJY0fKk8cARpoGRImOH8Na9pM80AhznduUY=; _bb_visaddr=\"fEJlbGF0dXIgQ29sb255LCBLcmlzaG5hcmFqYXB1cmF8MTIuOTk2NzAxMDM4NjA2NDM5fDc3Ljc1ODE5Njg5MDM1NDE2fDU2MDA2N3w=\"; _bb_dsid=974; _bb_loid=137; _bb_nhid=1290; _bb_bhid=; _bb_dsevid=1291; csurftoken=H4B-2g.MzkzMzE2MTI0NA==.1638355436065.pWHXB2VyfO9qMmoERofpQSP8xnoixEYiKvMYbJVFE0Y=; ts=2021-12-01%2013:11:17.272; BBAUTHTOKEN=; _bb_aid=\"Mjk5ODgzMDU1OA==\"; _bb_cid=6; _bb_hid=1157; _bb_mid=; _bb_rd=6; _bb_rdt=\"MzE0MTkyMjA2OA==.0\"; _bb_tc=0; _bb_vid=\"MzkzMjk5OTY2NQ==\"; _client_version=2721; _sp_bike_hid=969; _sp_van_encom_hid=1194; sessionid=o55aawyxacyv7m9xmb13twr11ib1k2or; ts=\"2021-11-30 05:24:32.121\"; csrftoken=0Hp3IYPYdn1dDsYjVm1MZNI0CriNdM8yz8YLyD5yPVhO9jfMVjq1yivahVxrQiy3; ts=2021-12-07%2013:02:24.205; csurftoken=r2o-og.MzkzMzE2MTI0NA==.1638862344074.nN6IVRLGqTbXD//AH52OzOvlGCEoWfFeOdAjuxgLmtw=; _bb_vid=\"Mzk1NDA1MzM1Ng==\"; _bb_tc=0; _bb_rdt=\"MzE0NDc3NjE4NQ==.0\"; _bb_rd=6; _client_version=2734; ts=\"2021-12-06 17:12:32.327\"; _bb_cid=14; sessionid=7k4phlilcwb2kibgq00rbibrdy8xg2wc; _bb_aid=\"MzAwMTc3OTY5OQ==\"";//todo
        String xcaller="listing-svc";//todo
        String entrycontextid="10";//todo
        String entrycontext="bbnow";//todo
        String xChannel="BB-IOS";
        String bb_decoded_aid="1234";
        boolean send_door_info=true;

        SendDoorInfoApi sendDoorInfoApi=new SendDoorInfoApi(xChannel,xcaller,entrycontextid,entrycontext,cookie,report);
        Response response=sendDoorInfoApi.sendDoorInfoApi("schema//ui_assembler//header//send-door-info-api-200.json",send_door_info);

        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("sendDoorInfoApi  response: " + response.prettyPrint(),true);
        response= sendDoorInfoApi.sendDoorInfoApi(send_door_info);
        report.log("sendDoorInfoApi  response: " + response.prettyPrint(),true);
    }

}
