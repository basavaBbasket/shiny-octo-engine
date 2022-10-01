package api.warehousecomposition.planogram_FC;

import api.warehousecomposition.planogram_FC.internal.Helper;
import api.warehousecomposition.planogram_FC.internal.IQApp;
import com.bigbasket.automation.reports.IReport;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import utility.database.DynamicLocationDBQueries;

import java.util.HashMap;
import java.util.Map;

import static msvc.DynamicLocationGeneralMethods.getPlanogramIDForGivenFcID;
public class DeliveryBinMethods extends Helper {

    public static Response perfomOrderBinMapping(int fcId, int orderId, String eventType, Map<String, String> adminCookie, IReport report) {
        IQApp iqApp = new IQApp(report);
        iqApp.cookie.updateCookie(adminCookie);

        Assert.assertTrue(eventType.equalsIgnoreCase("emptying") || eventType.equalsIgnoreCase("binning") || eventType.equalsIgnoreCase("delivery"), "" +
                "" + eventType + " is not a valid event Type \nValid event types are binning,emptying,delivery");

        String binLoc = null;
        if (eventType.equalsIgnoreCase("binning")) {
            report.log("Calling delivery bins recommend api to for binning", true);
            System.out.println("Calling delivery bins recommend api to for binning");
            Response response = iqApp.binRecommendationApi(fcId);
            binLoc = response.path("bin_loc").toString();
        } else if (eventType.equalsIgnoreCase("emptying")) {
            report.log("Fetching binlocation to perform emptying event", true);
            System.out.println("Fetching binlocation to perform emptying event");
            binLoc = fetchDeliveryBinLocationLinkedForGivenOrder(fcId, orderId, report);
        }

        String orderBinMappingBody = null;
        Response response = null;
        if (!eventType.equalsIgnoreCase("delivery")) {
            JSONObject jsonObject = PickingMethods.fetchContainerLabelLinkedForGivenOrder(orderId, report);
            String[] containerLabelsLinkedToOrder = new String[jsonObject.getInt("numRows")];
            for (int i = 0; i < jsonObject.getInt("numRows"); i++) {
                containerLabelsLinkedToOrder[i] = jsonObject.getJSONArray("rows").getJSONObject(i).getString("container_label");
            }
            orderBinMappingBody = orderBinMappingApiBody(eventType, orderId, binLoc, containerLabelsLinkedToOrder);
            response = iqApp.orderBinMappingApi(fcId, orderBinMappingBody);
        } else {
            JSONObject body = new JSONObject();
            body.put("event_type", eventType).put("order_id", orderId);
            orderBinMappingBody = body.toString();
            response = iqApp.orderBinMappingApi(fcId, orderBinMappingBody);
        }
        return response;
    }

    /**
     * fetchs the delivery bin location linked with the given order
     *
     * @param orderId
     * @param report
     * @return
     */
    public static String fetchDeliveryBinLocationLinkedForGivenOrder(int fcId, int orderId, IReport report) {
        String query = "select frsb.binloc from fc_rack_shelf_bin frsb LEFT JOIN bin_request_id_mapping brm " +
                "ON frsb.id=brm.bin_id LEFT JOIN fc_planogram fp ON brm.planogram_id=fp.id where fp.fc_id=" + fcId + " " +
                "and brm.request_id=" + orderId + ";";
        report.log("Fetching the delivery bin location mapped for the order\n " + query, true);
        System.out.println("Fetching the delivery bin location mapped for the order\n" + query);
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No delivery bin location returned \n" + query);
        }
        return jsonObject.getJSONArray("rows").getJSONObject(0).getString("binloc");
    }


    public static boolean isOrderBinMapped(int fcId,int order_id, IReport report){
        int planogramID = getPlanogramIDForGivenFcID(fcId);
        boolean mappingstatus = false;
        String query="select * from bin_request_id_mapping where planogram_id="+planogramID+" and request_id="+order_id+";";
        report.log("Checking the mapping between order and bin post binning in bin_request_id_mapping table \n Query: " + query, true);
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            mappingstatus=false;
            Assert.fail("No data returned from bin_request_id_mapping no mapping found between bin and order id \n " + query);
        }
        else
            mappingstatus=true;
        return  mappingstatus;
    }

    public static Map<String, Object> getOrderBinMappingDetails(int fcId, int order_id, IReport report){
        int planogramID = getPlanogramIDForGivenFcID(fcId);
        Map<String, Object> orderbinmappingdetails = new HashMap<>();
        String query="select * from bin_request_id_mapping where planogram_id="+planogramID+" and request_id="+order_id+";";
        report.log("Checking the mapping between order and bin post binning in bin_request_id_mapping table \n Query: " + query, true);
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0)
            Assert.fail("No data returned from bin_request_id_mapping no mapping found between bin and order id \n " + query);

        else {
            orderbinmappingdetails.put("bin_id",jsonObject.getJSONArray("rows").getJSONObject(0).getInt("bin_id"));
            orderbinmappingdetails.put("is_occupied",jsonObject.getJSONArray("rows").getJSONObject(0).getInt("is_occupied"));
            orderbinmappingdetails.put("created_on",jsonObject.getJSONArray("rows").getJSONObject(0).getString("created_on"));
            orderbinmappingdetails.put("reason_id",jsonObject.getJSONArray("rows").getJSONObject(0).getInt("reason_id"));

        }
        return orderbinmappingdetails;
    }

    public static boolean entryInFc_Sku_Stock (Object bin_id,Object created_on,IReport report){

        boolean entryInFc_Sku_Stock = false;
        String query="select * from fc_sku_stock where bin_id="+bin_id+" and updated_on like \""+created_on+"%\";";
        report.log("Checking the entry in Fc_Sku_Stock table \n Query: " + query, true);
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            entryInFc_Sku_Stock=false;
        }
        else
            entryInFc_Sku_Stock=true;

        return  entryInFc_Sku_Stock;
    }

}
