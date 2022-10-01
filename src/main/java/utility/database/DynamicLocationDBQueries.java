package utility.database;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.utilities.AutomationUtilities;

public class DynamicLocationDBQueries extends WebSettings {
    public static String executeMicroserviceDataBaseQuery(String query) {
        return AutomationUtilities.executeMicroserviceDatabaseQuery(AutomationUtilities.getDynamicLocationDB(), query);
    }

    public static String getRecordFromAerospikeByPk(String set,String primaryKey) {
        return AutomationUtilities.getAerospikeRecordByPk(serverName,AutomationUtilities.getAerospikeNameSpace(),set,primaryKey);
    }

    public static String updateAerospikeRecordForPickingRecobinByPk(String set, String primaryKey, String record) {
        return AutomationUtilities.updateAerospikeRecordByPk(serverName,AutomationUtilities.getAerospikeNameSpace(),set,primaryKey,record);
    }
}
