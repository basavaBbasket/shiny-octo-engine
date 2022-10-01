package api.warehousecomposition.planogram_FC;

import api.warehousecomposition.planogram_FC.internal.IQApp;
import com.bigbasket.automation.reports.IReport;

import java.util.Map;

public class AdminCookie {
    /**
     * Use this method to get the cookie for the given username & password
     *
     * @param userName username
     * @param password password
     * @param report   report object
     * @return cookie map
     */
    public static Map<String, String> getMemberCookie(String userName, String password, IReport report) {
        IQApp app = new IQApp(report)
                .iqAppLogin(userName, password)
                .mfaAuthenticate(userName);
        return app.cookie.allCookies;
    }
}
