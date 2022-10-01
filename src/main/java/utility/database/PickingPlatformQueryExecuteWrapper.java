package utility.database;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.utilities.AutomationUtilities;

public class PickingPlatformQueryExecuteWrapper extends WebSettings {
    public static String executeMicroserviceDataBaseQuery(String query) {
        return AutomationUtilities.executeMicroserviceDatabaseQuery(AutomationUtilities.getPickingPlatformDBName(), query);
    }
}
