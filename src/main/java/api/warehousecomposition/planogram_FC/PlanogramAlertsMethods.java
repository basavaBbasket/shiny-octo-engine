package api.warehousecomposition.planogram_FC;

import api.warehousecomposition.planogram_FC.internal.Helper;
import api.warehousecomposition.planogram_FC.internal.IQApp;
import com.bigbasket.automation.reports.IReport;
import io.restassured.response.Response;

public class PlanogramAlertsMethods extends Helper {

    /**
     * Method to accept/reject the cee checkin request for the un roastered cee
     *
     * @param ceeId      unroastered cee id
     * @param fcId       fcId
     * @param alertType  applicable types store_open,store_close,cee_checkin_request,
     *                   logged_in_associates_alert,read_alert_t1,read_alert_t2,
     *                   pending_grn_alert,eta_breach,rts_delay,awol_cee_alert,slacking_cee_alert
     * @param actionType
     * @param userName
     * @param pwd
     * @param report
     * @return
     */
    public static boolean actOnCeeCheckinRequest(int ceeId, int fcId, String alertType, String actionType, String userName, String pwd, IReport report) {
        IQApp app = new IQApp(report);
        Response response = app.iqAppLogin(userName, pwd)
                .mfaAuthenticate(userName)
                .planogramAlerts(fcId, alertType);

        int numOfCeeData = response.path("alert_info.meta.cee_checkin_request.cee_id.size()");
        int jobId = -999;
        String date = null;
        int id = -999;
        boolean alertExists = false;
        for (int i = 0; i < numOfCeeData; i++) {
            if (Integer.parseInt(response.path("alert_info.meta.cee_checkin_request.cee_id["+i+"]").toString()) == ceeId) {
                alertExists = true;
                date = response.path("alert_info.actions.meta.date[" + i + "][0]").toString();
                id = Integer.parseInt(response.path("alert_info.actions.meta.id[" + i + "][0]").toString());
                jobId = Integer.parseInt(response.path("job_id[" + i + "]").toString());
            }
        }
        if (alertExists) {
            report.log("Cee Check In Request Alert exists for cee " + ceeId, true);
            app.ceeCheckInRequest(actionType, date, id);
            app.completePlanogramJob(fcId, jobId);
        } else {
            report.log("Cee Check In Request Alert doesn't exists for cee " + ceeId, false);
        }
        return alertExists;
    }
}
