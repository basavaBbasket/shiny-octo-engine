package bbnow.schema_validation.ui_assembler.header;

import com.bigbasket.automation.Config;
import com.bigbasket.automation.api.bbdaily.RequestAPIs;
import com.bigbasket.automation.mapi.mapi_4_1_0.internal.BigBasketApp;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import com.bigbasket.automation.mapi.mapi_4_1_0.internal.BigBasketApp;
import framework.BaseTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import msvc.eta.admin.CheckStoreStatusApi;
import msvc.ui_assembler.header.Endpoints;
import msvc.ui_assembler.header.SendDoorInfoApi;
import org.joda.time.DateTime;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.*;

public class ValidateSendDoorInfo extends BaseTest {
    @DescriptionProvider(author = "Shruti", description = "This testcase validates different cases for SendDoorInfo api.", slug = "Validate SendDoorInfo api")
    @Test(groups = {"bbnow"})
    public void sendDoorInfoApiTest() {
        AutomationReport report = getInitializedReport(this.getClass(), false);
        String xcaller = "listing-svc";
        String entrycontextid = Config.bbnowConfig.getString("entry_context_id");
        String entrycontext = Config.bbnowConfig.getString("entry_context");
        String xChannel="BB-Android";
        boolean flag= false;

        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName, Config.bbnowConfig.getString("bbnow_stores[1].member_sheet_name"));
        String member_id= AutomationUtilities.getMemberIDForGivenID(creds[0],report);

        BigBasketApp app =new BigBasketApp(report);
        app.getAppDataDynamic();

        SendDoorInfoApi sendDoorInfoApi = new SendDoorInfoApi(app.cookie.allCookies, xChannel,member_id,xcaller, entrycontextid, entrycontext,report);
        Response response = sendDoorInfoApi.sendDoorInfoApi("true");
        JsonArray array =new JsonObject(response.asString()).getJsonArray("ec_doors");
        String ec_doors = String.valueOf(array);
        Assert.assertTrue(ec_doors!=null);
        report.log("ec_doors info is present when x-channel is present " + ec_doors,true);
         for(int i=0;i<array.size();i++)
        {
            String slug= array.getJsonObject(i).getString("slug");
            if(slug.equalsIgnoreCase("bbnow")){
            boolean door_enabled =array.getJsonObject(i).getBoolean("is_door_enabled");
            Assert.assertTrue(door_enabled);
            boolean checkout_enabled= array.getJsonObject(i).getBoolean("is_checkout_enabled");
            Assert.assertTrue(checkout_enabled);
            report.log("when door is open checkout_enabled is true",true);}
        }

        for(int i=0;i<array.size();i++)
        {

                JsonObject door_details =array.getJsonObject(i);
                Assert.assertEquals(true,door_details.containsKey("display_name"));
                Assert.assertEquals(true,door_details.containsKey("door_config"));
                flag= true;

        }
        Assert.assertTrue(flag);
        report.log("Display_name and door_config are present for each EC",true);
        JsonObject slotJson= new JsonObject(response.asString()).getJsonArray("next_available_slots").getJsonObject(0);
        Assert.assertEquals(true, slotJson.containsKey("sa_id"));
        Assert.assertEquals(true, slotJson.containsKey("show_express"));
        Assert.assertEquals(true, slotJson.containsKey("slot_start_timestamp"));
        Assert.assertEquals(true, slotJson.containsKey("bb_star_avail"));
        Assert.assertEquals(true, slotJson.containsKey("short_eta"));
        Assert.assertEquals(true, slotJson.containsKey("medium_eta"));
        Assert.assertEquals(true, slotJson.containsKey("long_eta"));
        Assert.assertEquals(true, slotJson.containsKey("slot_time"));
        Assert.assertEquals(true, slotJson.containsKey("slots_full"));
        report.log("Slot info is present"+ slotJson.toString(),true);

        SendDoorInfoApi sendDoorInfo = new SendDoorInfoApi(app.cookie.allCookies,"", member_id,xcaller, entrycontextid, entrycontext, report);
        Response newresponse = sendDoorInfo.sendDoorInfoApi("true");
        JsonArray ecDoorsjson =new JsonObject(newresponse.asString()).getJsonArray("ec_doors");
        String no_doors = String.valueOf(ecDoorsjson);
        Assert.assertTrue(ecDoorsjson.size()==0);
        report.log("ec_doors info is not present when x-channel is not present door info: " + no_doors,true);

/*Behaviour changed now send_door_info=true by default in code*/
//        sendDoorInfo = new SendDoorInfoApi(app.cookie.allCookies,xChannel, member_id,xcaller, entrycontextid, entrycontext, report);
//        Response doorresponse = sendDoorInfo.sendDoorInfoApi("false");
//        ecDoorsjson =new JsonObject(doorresponse.asString()).getJsonArray("ec_doors");
//        no_doors = String.valueOf(ecDoorsjson);
//        Assert.assertTrue(ecDoorsjson.size()==0);
//        report.log("ec_doors info is not present when send door info is false door info: " + no_doors,true);

//        sendDoorInfo = new SendDoorInfoApi(app.cookie.allCookies,xChannel, member_id,xcaller, entrycontextid, entrycontext, report);
//        Response nodoorresponse = sendDoorInfo.sendDoorInfoApi("aaaa");
//        ecDoorsjson =new JsonObject(nodoorresponse.asString()).getJsonArray("ec_doors");
//        no_doors = String.valueOf(ecDoorsjson);
//        Assert.assertTrue(ecDoorsjson.size()==0);
//        report.log("ec_doors info is not present when send door info is junk value door info: " + no_doors,true);


        Response visitor= sendDoorInfoConfig(report,entrycontext,entrycontextid);
        JsonArray visitorarray =new JsonObject(visitor.asString()).getJsonArray("applicable_ec_doors_for_visitor");
        String visitor_doors = String.valueOf(visitorarray);
        Assert.assertTrue(visitor_doors!=null);
        report.log("applicable visitor doors are: " + visitor_doors,true);

    }

    /**
     * This method makes GET request to sendDoorInfoApi .
     *       send_door_info is set during object creation.
     * @return
     */
       public Response sendDoorInfoConfig(AutomationReport report, String entrycontext, String entrycontextid){

        String endpoint = String.format(Endpoints.SEND_DOOR_INFO_CONFIG);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller", "test-configsvckt");
        requestHeader.put("X-TimeStamp", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Entry-Context", entrycontext);
        requestHeader.put("Content-Type", "application/json");
           Map<String, String> paramsMap = new HashMap<>();
           paramsMap.put("entry-context-id", entrycontextid);
           paramsMap.put("service-name", "ui-assembler");
           report.log("Calling VistiorsDoorInfoApi " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

           RequestAPIs request = new RequestAPIs(msvcServerName, endpoint);
           Response response = request.getRequestAPIResponse(requestHeader,paramsMap);
           report.log("Status code: " + response.getStatusCode(),true);
           return  response;
    }
}