package foundation.hubops2;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.AutomationReport;
import foundation.RequestMethods;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FindSlotsApi extends WebSettings {
    private AutomationReport report;
    private String apiPath = "/slot/internal/v1/find-slots";

    public FindSlotsApi(AutomationReport report){
        this.report = report;
    }

    public Response initiateFindSlotsRequest(String body){
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Entry-Context", "bigbasket-b2c");
        headerMap.put("X-Entry-Context-Id", "100");
        RequestMethods request = new RequestMethods(report,msvcServerName, apiPath);
        Response response = request.postRequestAPIResponse(headerMap,body);
        return response;
    }
}
