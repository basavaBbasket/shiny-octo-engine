package tms.api.DAPI;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.IReport;
import io.restassured.response.Response;

import java.io.IOException;

public class DapiUtils extends WebSettings {

    /**
     *This method perform the delivery flow till checkin using the following parameters
     * @param report
     * @param xTenant
     * @param xProject
     * @param ceeId
     * @param password
     * @param deviceId
     * @param appVersion
     * @param registrationId
     * @param latitude
     * @param longitude
     * @throws IOException
     * @throws InterruptedException
     */
    public static Response dapiTillCheckin(AutomationReport report, String xTenant, String xProject,int ceeId,String password,String deviceId,String appVersion,String registrationId,String latitude, String longitude,String qrCode) throws IOException, InterruptedException {
        DapiAppForTMS dapiAppForTMS=new DapiAppForTMS(report,xTenant,xProject);
        dapiAppForTMS.login(ceeId,password,deviceId,appVersion,registrationId);
        Response getStoresResponse = dapiAppForTMS.getStores("has_route_assigned,stores,pool_status");
        report.log("getstores reponse"+getStoresResponse.prettyPrint(),true);
        int sa_id = getStoresResponse.path("sa_id");
        int dm_id = getStoresResponse.path("dm_id");
        dapiAppForTMS.selectStore(sa_id,dm_id);
       return dapiAppForTMS.checkIn(latitude,longitude,qrCode);



    }

}
