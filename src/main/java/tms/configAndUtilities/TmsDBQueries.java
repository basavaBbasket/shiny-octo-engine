package tms.configAndUtilities;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import com.bigbasket.automation.utilities.AutomationUtilities;

import groovyjarjarantlr.build.Tool;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class TmsDBQueries extends WebSettings {
    private static RequestSpecification spec;
    private static AtomicInteger OFFSET_COUNTER = new AtomicInteger(0);
    private static int TMS_UPPERBOUND_FOR_OFFSET = 18;
    String env = AutomationUtilities.getEnvironmentFromServerName(serverName);
    public static String getMemberData(String dbName , String query) {
        return AutomationUtilities.executeMicroserviceDatabaseQuery(dbName, query);
    }

    public static String getCeeData(String dbName , String query) {
        return AutomationUtilities.executeMicroserviceDatabaseQuery(dbName, query);
    }


    public static int getUpperBoundForOffset(String dbName, String query){
       JSONObject jsonObject = new JSONObject(AutomationUtilities.executeMicroserviceDatabaseQuery(dbName, query));
       int size = jsonObject.getJSONArray("rows").getJSONObject(0).getInt("count(*)");
       return (size > threadCount)? threadCount:size;
    }

    public static String getSkuData(String dbName,String query) {
        return AutomationUtilities.executeMicroserviceDatabaseQuery(dbName,query );
    }

    public static String getOrderDetails(String dbName,String query) {
        return AutomationUtilities.executeMicroserviceDatabaseQuery(dbName,query );
    }

    public static String getCromaOrderDbName(){
        String env = AutomationUtilities.getEnvironmentFromServerName(serverName);
        String orderDB = env + "-ORDER-CROMA";
        System.out.println("Order-Croma DB: " + orderDB);
        return orderDB;
    }

    /**
     * This method will return lat long for the order from order detail table
     * @param orderId
     * @param report
     * @return
     * @throws ParseException
     */
    public ArrayList getLatLongOrderDetails(String orderId, IReport report) throws ParseException {
        String checkOrderDetailsDbQuery = "select order_location from order_detail where id = "+ orderId +";";

        report.log("Fetching lat long from order details tab \n Query Executed: " + checkOrderDetailsDbQuery,true);
        JSONObject jsonObject = new JSONObject(AutomationUtilities.executeMicroserviceDatabaseQuery(getCromaOrderDbName(),checkOrderDetailsDbQuery));
        if (jsonObject.getInt("numRows") == 0) {
            report.log("No id returned ", true);
            Assert.fail("No id returned  for Query: " + checkOrderDetailsDbQuery);
        }
        String orderLocation= jsonObject.getJSONArray("rows").getJSONObject(0).getString("order_location");
        JSONParser parser = new JSONParser();
        report.log("order location details"+orderLocation,true);


        JSONObject latLongObject=new JSONObject(orderLocation);
        ArrayList<BigDecimal> latLong=new ArrayList<>();
        latLong.add(latLongObject.getBigDecimal("lat"));
        latLong.add(latLongObject.getBigDecimal("lon"));

        return latLong;
    }

    public static Response  updateSlotInstanceDetails(String dbName, String query) {
        return AutomationUtilities.updateDatabaseQuery(dbName,query );
    }
    public static String getCromaServiceabilityDBName() {
        String env = AutomationUtilities.getEnvironmentFromServerName(serverName);
        String serviceabilityDB = env + "-SERVICEABILITY-CROMA";
        System.out.println("SERVICEABILITY-Croma DB: " + serviceabilityDB);
        return serviceabilityDB;
    }

    public static String getCromaHertzDBName() {
        String env = AutomationUtilities.getEnvironmentFromServerName(serverName);
        String hertzDb = env + "-CROMA-HERTZ";
        System.out.println("Croma HERTZ DB: " + hertzDb);
        return hertzDb;
    }

    public  int getsaidfrompincode(int pincode, IReport report) {
        String query = "select serviceability_area_id from pincode_serviceability where pincode="+pincode+" limit 1;";
        report.log("Fetching the serviceability_area_id \n Query Executed: " + query,true);
        JSONObject jsonObject = new JSONObject(AutomationUtilities.executeMicroserviceDatabaseQuery(getCromaServiceabilityDBName(),query));
        if (jsonObject.getInt("numRows") == 0) {
            report.log("No id returned ", true);
            Assert.fail("No id returned  for Query: " + query);
        }
        int said = jsonObject.getJSONArray("rows").getJSONObject(0).getInt("serviceability_area_id");
        report.log("Current id of the serviceability_area_id :" + pincode + " is : " + said,true);
        return said;

    }

    public  int getlmdfcidfromsaid(int said, IReport report) {
        String query = "select lmd_fc_id from serviceability_area where id="+said+";";
        report.log("Fetching the lmdfcid \n Query Executed: " + query,true);
        JSONObject jsonObject = new JSONObject(AutomationUtilities.executeMicroserviceDatabaseQuery(getCromaServiceabilityDBName(),query));
        if (jsonObject.getInt("numRows") == 0) {
            report.log("No id returned ", true);
            Assert.fail("No id returned  for Query: " + query);
        }
        int lmd_fc_id = jsonObject.getJSONArray("rows").getJSONObject(0).getInt("lmd_fc_id");
        report.log("Current id of the said :" + said + " is : " + lmd_fc_id,true);
        return lmd_fc_id;
    }

    /**
     * this method will return the lmdfcid from pincode
     * @param pincode
     * @param report
     * @return
     */
    public int getLmdFcIdFromPincode( int pincode, IReport report){
        int said=getsaidfrompincode(pincode,report);
        int lmdfcid=getlmdfcidfromsaid(said,report);
        return  lmdfcid;
    }

    /**
     * This method will return thw route batch id which is created after running the route batch creation cron
     * @param orderid
     * @param report
     * @return
     */
    public  int getRouteBatchId(int orderid,IReport report){
        String query="select routing_batch_id from order_data where order_id="+orderid+";";
        report.log("Fetching the routing batch id from order_id \n Query Executed: " + query,true);
        JSONObject jsonObject = new JSONObject(AutomationUtilities.executeMicroserviceDatabaseQuery(getCromaServiceabilityDBName(),query));
        if (jsonObject.getInt("numRows") == 0) {
            report.log("No id returned ", true);
            Assert.fail("No id returned  for Query: " + query);
        }
        int routing_batch_id = jsonObject.getJSONArray("rows").getJSONObject(0).getInt("routing_batch_id");
        report.log("Current routing_batch_id of the order  is : " + routing_batch_id,true);
        return routing_batch_id;
    }

    /**
     * This method will return route id from route order table after running the route batch creation cron
     * @param orderid
     * @param report
     * @return
     */
    public  int getRouteId(int orderid,IReport report){
        String query="select route_id from route_order where order_id="+orderid+";";
        report.log("Fetching the route id from route_order table \n Query Executed: " + query,true);
        JSONObject jsonObject = new JSONObject(AutomationUtilities.executeMicroserviceDatabaseQuery(getCromaServiceabilityDBName(),query));
        if (jsonObject.getInt("numRows") == 0) {
            report.log("No id returned ", true);
            Assert.fail("No id returned  for Query: " + query);
        }
        int routeid = jsonObject.getJSONArray("rows").getJSONObject(0).getInt("route_id");
        report.log("Current route id for order is : " + routeid,true);
        return routeid;
    }


    /**
     * This method will allow user to logout the cee
     * @param ceeId
     * @param report
     */
    public void logOutCee(int ceeId,IReport report){


        String query="update cee set is_logged_in=0 where id="+ceeId+";";
        report.log("Performing the logout for the cee"+ceeId+" \n Query Executed: " + query,true);
        String response = AutomationUtilities.executeMicroserviceDatabaseQuery(getCromaHertzDBName(), query);
        report.log(response,true);

    }


    /**
     * This method will fetch cee id who is not resigned(employement status is 2 ) and is active is true
     * @param saId
     * @param dmId
     * @param password
     * @param report
     * @return
     */
    public int getCeeId(int saId, int dmId,String password,IReport report){

        TMS_UPPERBOUND_FOR_OFFSET = TmsDBQueries.getUpperBoundForOffset(getCromaHertzDBName(),"select count(*) from cee");
        String query="select id from cee where sa_id="+saId+" and dm_id="+dmId+" and employment_status=2 and is_active=1 limit 1 offset "+OFFSET_COUNTER.getAndIncrement()%TMS_UPPERBOUND_FOR_OFFSET+";";
        report.log("Fetching the cee id for given said "+saId+" and dm_id "+dmId+" from cee \n Query Executed: " + query,true);
        JSONObject jsonObject = new JSONObject(AutomationUtilities.executeMicroserviceDatabaseQuery(getCromaHertzDBName(),query));
        if (jsonObject.getInt("numRows") == 0) {
            report.log("No id returned ", true);
            Assert.fail("No id returned  for Query: " + query);
        }
        int ceeId = jsonObject.getJSONArray("rows").getJSONObject(0).getInt("id");
        report.log("Current cee id is : " + ceeId,true);
        String updatePasswordQuery="update cee set password=\""+password+"\" where id="+ceeId+";";
         AutomationUtilities.updateMicroServiceDatabaseQuery(getCromaHertzDBName(),updatePasswordQuery);
        return ceeId;
    }

    /**
     * this method will return a string statisfing the condition lmd_fcid:date (yy-mm-dd) format
     * @param lmdFcId
     * @return
     */
    public String getQrCode(String lmdFcId){
        String qrString=lmdFcId+":"+new SimpleDateFormat("yyyy-MM-dd")
                .format(new DateTime(new Date()).toDate());
        return qrString;
    }


}
