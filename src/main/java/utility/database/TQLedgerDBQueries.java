package utility.database;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.utilities.AutomationUtilities;
import com.bigbasket.automation.utilities.Libraries;
import org.apache.log4j.Logger;
import org.json.JSONObject;

public class TQLedgerDBQueries extends WebSettings {

    private static Logger logger = Logger.getLogger(TQLedgerDBQueries.class);

    public int OrderModificationTQLedgerIncrement(String order_id )  {
        String incr="0";
        try {
           String query = "select increased_transient_quantity from warehouse_transientquantityordersplit where order_id="+order_id +" order by created_on DESC limit 1;";
            Thread.sleep(6000);
           String incrQTY = AutomationUtilities.executeDatabaseQuery(serverName, query);
           JSONObject response = new JSONObject(incrQTY);
            incr = response.getJSONArray("results").getJSONArray(0).get(0).toString();

       }
       catch (Exception e){
           logger.info("sleep time out ");

       }
        return Integer.parseInt(incr);
    }


    public String  OrderModificationTQLedgerDecrement(String order_id) {
        String query = "select decreased_transient_quantity from warehouse_transientquantityordersplit where order_id=" + order_id + " order by created_on DESC limit 1;";
        String decrQTY = AutomationUtilities.executeDatabaseQuery(serverName, query);
        JSONObject response = new JSONObject(decrQTY);
        String decr = response.getJSONArray("results").getJSONArray(0).get(0).toString();
        return decr;
    }

    public int OrderCreationTQLEdgerIncrement(String po_id, String sku){
        String query = "select increased_transient_quantity from warehouse_transientquantityordersplit where po_id="+po_id +" AND product_description_id="+sku
                + " order by created_on DESC limit 1;";
        String incrQTY = AutomationUtilities.executeDatabaseQuery(serverName, query);
        JSONObject response = new JSONObject(incrQTY);
        String incr = response.getJSONArray("results").getJSONArray(0).get(0).toString();
        return Integer.parseInt(incr);
    }



}
