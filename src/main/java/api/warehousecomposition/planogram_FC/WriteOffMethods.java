package api.warehousecomposition.planogram_FC;

import api.warehousecomposition.planogram_FC.internal.Helper;
import api.warehousecomposition.planogram_FC.internal.IQApp;
import com.bigbasket.automation.reports.IReport;

import java.util.HashMap;
import java.util.Map;

public class WriteOffMethods extends Helper {

    /**
     * writes off the qty from the specified location
     *
     * @param fcId       fcId
     * @param skuId      skuId
     * @param userName   admin username
     * @param pwd        password
     * @param binLoc     binlocation to write off from
     * @param locationId locationId
     * @param qty        qty to writeoff
     * @param reason     reason for write off
     * @param report     report instance
     */
    public static void writeOffFlow(int fcId, int skuId, String userName, String pwd, String binLoc, int locationId, int qty, String reason, IReport report) {
        HashMap<String, Object> writeOffBody = new HashMap<>();
        writeOffBody.put("bin_location", binLoc);
        writeOffBody.put("location_id", locationId);
        writeOffBody.put("sku_id", skuId);
        writeOffBody.put("quantity", qty);
        writeOffBody.put("reason_id", 1);
        writeOffBody.put("reason", reason);

        new IQApp(report)
                .iqAppLogin(userName, pwd)
                .mfaAuthenticate(userName)
                .writeOffAPI(fcId, writeOffBody);
    }

    /**
     * writes off the qty from the specified location
     *
     * @param fcId       fcId
     * @param skuId      skuId
     * @param binLoc     binlocation to write off from
     * @param locationId locationId
     * @param qty        qty to writeoff
     * @param reason     reason for write off
     * @param adminCookie login cookie
     * @param report     report instance
     */
    public static void writeOffFlow(int fcId, int skuId, String binLoc, int locationId, int qty, String reason, Map<String, String> adminCookie, IReport report) {
        HashMap<String, Object> writeOffBody = new HashMap<>();
        writeOffBody.put("bin_location", binLoc);
        writeOffBody.put("location_id", locationId);
        writeOffBody.put("sku_id", skuId);
        writeOffBody.put("quantity", qty);
        writeOffBody.put("reason_id", 1);
        writeOffBody.put("reason", reason);

        IQApp app = new IQApp(report);
        app.cookie.updateCookie(adminCookie);
        app.writeOffAPI(fcId, writeOffBody);
    }
}
