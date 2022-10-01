package TMS.configAndUtilities;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import com.bigbasket.automation.utilities.AutomationUtilities;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.testng.Assert;

public class TmsDBQueries extends WebSettings {
    private static RequestSpecification spec;

    public static String getMemberData(String dbName , String query) {
        return AutomationUtilities.executeMicroserviceDatabaseQuery(dbName, query);
    }

    public static String getSkuData(String dbName,String query) {
        return AutomationUtilities.executeMicroserviceDatabaseQuery(dbName,query );
    }

    public static String getOrderDetails(String dbName,String query) {
        return AutomationUtilities.executeMicroserviceDatabaseQuery(dbName,query );
    }

    public static Response  updateSlotInstanceDetails(String dbName, String query) {
        return AutomationUtilities.updateDatabaseQuery(dbName,query );
    }
    public static String getCromaServiceabilityDBName() {
        String env = AutomationUtilities.getEnvironmentFromServerName(serverName);
        String erviceabilityDB = env + "-SERVICEABILITY-CROMA";
        System.out.println("SERVICEABILITY-Croma DB: " + erviceabilityDB);
        return erviceabilityDB;
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

    public int getlmdfcidfrompincode( int pincode, IReport report){
        int said=getsaidfrompincode(pincode,report);
        int lmdfcid=getlmdfcidfromsaid(said,report);
        return  lmdfcid;
    }

}
