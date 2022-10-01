package bbnow.testcases.order;

import api.warehousecomposition.planogram_FC.AdminCookie;
import bbnow.schema_validation.order.OrderCount;
import com.bigbasket.automation.Config;
import com.bigbasket.automation.mapi.mapi_4_1_0.OrderPlacement;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import com.bigbasket.automation.utilities.Libraries;
import framework.BaseTest;
import groovy.json.JsonParser;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import msvc.order.external.OrderTrackingApi;
import msvc.order.internal.OrderCountAPI;
import msvc.order.internal.OrderListApi;
import msvc.order.internal.OrderStatusAPI;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;


public class OrderStatusUpdate extends BaseTest {

    @DescriptionProvider(author = "tushar", description = "Checks Order Status API", slug = " ")
    @Test(groups = {"sa1","bbnow" , "regression","bbnow-status"})
    public void checkOrderStausAtDifferentLevels()
    {
        AutomationReport report = getInitializedReport(this.getClass(), false);

        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName, Config.bbnowConfig.getString("bbnow_stores[0].member_sheet_name"));

        String member_id;


        JsonObject jsonObj_id = new JsonObject(AutomationUtilities.executeDatabaseQuery(serverName, "select id from member_member where mobile_no="+creds[0] +";"));
        member_id = String.valueOf(jsonObj_id.getJsonArray("rows").getJsonObject(0).getInteger("id"));
        report.log("Starting testcase for order placement.", true);
        String searchTerm1 = Config.bbnowConfig.getString("bbnow_stores[0].search_term1");
        String searchTerm2 = Config.bbnowConfig.getString("bbnow_stores[0].search_term2");
        String[] searchTerms = {searchTerm1,searchTerm2};
        String areaName = Config.bbnowConfig.getString("bbnow_stores[0].area");

        String orderId = OrderPlacement.placeBBNowOrder("bbnow" , 10 , creds[0], areaName, 2, searchTerms, true, false, report);
        report.log("Order Id: " + orderId,true);
        report.log("member Id:" + member_id,true);


        //String orderId = "1001089203";
        String entrycontextid=Config.bbnowConfig.getString("entry_context_id");
        String entrycontext=Config.bbnowConfig.getString("entry_context");
        String xService = "123";       // can be any valid string
        String xCaller = "hertz-svc";  // can be any valid string
        String[] cred = AutomationUtilities.getUniqueAdminUser(serverName,"admin-superuser-mfa");
        String adminUser = cred[0];
        String adminPassword = cred[1];


        report.log("ORDER DETAILS API",true);
        Map<String , String> cookie = AdminCookie.getMemberCookie(adminUser,adminPassword , report);
        report.log(cookie.toString(),true);
        OrderCountAPI orderCountAPI =  new OrderCountAPI(entrycontext,entrycontextid,xService,xCaller,cookie,report);
        Response response = orderCountAPI.getOrderDetailsApi(orderId);


        JsonArray arr = new JsonArray(response.body().asString());
        JsonObject jsonObj = arr.getJsonObject(0);

        //report.log("Case 1: verifying new fields in order detials api", true);
        JsonObject detailsJson =  jsonObj.getJsonObject("details");
        Assert.assertEquals(true,detailsJson.containsKey("destination_location"));
        report.log("destination_location",true);
        //Assert.assertEquals(true,detailsJson.containsKey("container_labels"));
        //report.log("container label",true);
        JsonObject etaJson = jsonObj.getJsonObject("eta");
        Assert.assertEquals(true,etaJson.containsKey("threshold_name"));
        report.log("eta_thresold",true);
        Assert.assertEquals(true,etaJson.containsKey("order_eta"));
        report.log("eta",true);
        Assert.assertEquals(true,etaJson.containsKey("dg_percentage"));
        report.log("dg percentage",true);
        Assert.assertEquals(true,etaJson.containsKey("dg_time_buffer"));
        report.log("dg time buffer",true);


        //report.log("Case 2: Staus time details",true);
        JsonObject statusTimeJson = jsonObj.getJsonObject("status_time_details");
        Assert.assertEquals(true,statusTimeJson.containsKey("payment_pending_time"));
        report.log("payment pending details",true);
        Assert.assertEquals(true,statusTimeJson.containsKey("payment_pending_by"));
        report.log("payment pending by", true);


        List<Integer> sku = new ArrayList<>();
        JsonArray itemDetailsJson = jsonObj.getJsonArray("item_details");
        for(int i = 0; i< itemDetailsJson.size() ; i++)
        {
            JsonObject itemJson = itemDetailsJson.getJsonObject(i);
            sku.add(itemJson.getInteger("sku_id"));
        }
        report.log("sku id: "+sku,true);



        report.log("ORDER LIST API",true);
        int bb_mid = Integer.parseInt(member_id);
        OrderListApi orderListApi = new OrderListApi(xService,xCaller,entrycontextid,entrycontext,bb_mid,report);
        ArrayList<Integer> ecList = new ArrayList<>();
        ecList.add(10);
        Response responseOfOrderDetailApi = orderListApi.getorderList(ecList);
        JsonObject orderListJsonObject = new JsonObject(responseOfOrderDetailApi.body().asString());
        JsonArray orderListJsonArray = orderListJsonObject.getJsonArray("orders");
        JsonObject orderListJsonObjForOrderKey = orderListJsonArray.getJsonObject(0);

        JsonArray json_orderArr = orderListJsonObjForOrderKey.getJsonArray("orders");
        JsonObject json_orderObj = json_orderArr.getJsonObject(0);
        boolean can_addon = json_orderObj.getBoolean("can_addon");
        Assert.assertEquals(false,can_addon);
        report.log("verify can_addon key in order list is set to false",true);

        Assert.assertEquals(true, orderListJsonArray.size()>0);
        report.log("number of orders: "+ orderListJsonArray.size(),true);

        //Assert.assertEquals(true,json_orderObj.containsKey("container_labels"));
        //report.log("verifying bag id",true);

        Assert.assertEquals(true,json_orderObj.containsKey("sa_id"));
        report.log("sa_id",true);

        JsonObject slot_json = json_orderObj.getJsonObject("slot");
        Assert.assertEquals(true,slot_json.containsKey("date"));
        report.log("slot date",true);

        Assert.assertEquals(true,json_orderObj.containsKey("state"));
        report.log("order status",true);



        report.log("ORDER TRACKING API",true);
        OrderTrackingApi orderTrackingApi = new OrderTrackingApi(xService,xCaller,entrycontextid,entrycontext,orderId,report);
        Response orderTrackingResponse = orderTrackingApi.getLiveOrderTrackingData();

        JsonObject orderTrack = new JsonObject(orderTrackingResponse.body().asString());

        JsonObject orderSectionJson = orderTrack.getJsonObject("order");

        Assert.assertEquals(true,orderSectionJson.containsKey("container_message"));
        report.log("container_message",true);

        Assert.assertEquals(true,orderSectionJson.containsKey("supported_order_status"));
        report.log("supported_order_status",true);

        JsonObject memberSectionJson = orderTrack.getJsonObject("member");
        Assert.assertEquals(true,memberSectionJson.containsKey("first_name"));
        Assert.assertEquals(true,memberSectionJson.containsKey("last_name"));
        Assert.assertEquals(true,memberSectionJson.containsKey("id"));
        Assert.assertEquals(true,memberSectionJson.containsKey("city_name"));
        Assert.assertEquals(true,memberSectionJson.containsKey("lat"));
        Assert.assertEquals(true,memberSectionJson.containsKey("lng"));
        Assert.assertEquals(true,memberSectionJson.containsKey("pin"));
        Assert.assertEquals(true,memberSectionJson.containsKey("contact_no"));
        report.log("member_details",true);


        Assert.assertEquals(true,orderTrack.containsKey("polling_interval"));
        report.log("polling_interval",true);



        /**
         String orderId = "1001096691";
         ArrayList<Integer> sku = new ArrayList<Integer>();
         sku.add(169055);
         //sku.add(243337);
         report.log("ORDER STATUS API",true);
         **/

        OrderStatusAPI orderStatusAPI = new OrderStatusAPI(entrycontext,entrycontextid,xService,xCaller,report);
        // OrderStatusAPI orderStatusAPI = new OrderStatusAPI("bbnow","10","monolith","monolith",report);
        orderStatusAPI.changeOrderStatus("packed",orderId,sku);
        String query = "select additional_info from order_detail where id="+orderId+";";
        JsonObject jsonObjec=new JsonObject(AutomationUtilities.executeMicroserviceDatabaseQuery(AutomationUtilities.getOrderDBName(), query,report));
        String c = jsonObjec.getJsonArray("rows").getJsonObject(0).getString("additional_info");
        JSONObject additon_info = new JSONObject(c);
        JSONArray bag_ids = (JSONArray) additon_info.get("container_labels");
        report.log("Bag id length: "+bag_ids.length(),true);
        Assert.assertEquals(true,bag_ids.length()>0);
        report.log("query:"+jsonObjec,true);

        Response changeStatusToBinned = orderStatusAPI.changeOrderStatus("binned",orderId,sku);
        Assert.assertEquals(changeStatusToBinned.getStatusCode(),200);

        String queryForModLog = "select additional_info from order_modificationlog where order_id="+orderId+";";
        JsonObject mod_log=new JsonObject(AutomationUtilities.executeMicroserviceDatabaseQuery(AutomationUtilities.getOrderDBName(), queryForModLog,report));
        report.log("mod_log"+mod_log,true);
    }


}
