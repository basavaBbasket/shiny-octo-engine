package foundation.path_apis;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.AutomationReport;
import com.fasterxml.jackson.databind.ObjectMapper;
import foundation.RequestMethods;
import io.restassured.response.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PathDetailsAPIs extends WebSettings {
    private AutomationReport report;
    private String APIEndPoint = "/path/internal/v1/path/";

    public PathDetailsAPIs(AutomationReport report) {
        this.report = report;
    }

    /**
     * Making get request to path APIs to fetch the path details for a given path_id
     * @param path_id The path _id for which details have be fetched
     * @return Restassured response after calling the path API
     * @throws Exception
     */
    public Response requestPathDetails(int path_id) throws Exception {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Entry-Context", "bigbasket-b2c");
        headerMap.put("X-Entry-Context-Id", "100");
        RequestMethods request = new RequestMethods(report,msvcServerName,
                APIEndPoint+path_id+"/active-frozen-path");
        Response response = request.getRequestAPIResponse(headerMap);
        if(response.getStatusCode()!=200)
            throw new Exception("Got Incorrect status code.");
        return response;
    }

    /**
     * Extract the details of path for which the path API was called
     * @param response response received after calling the path API
     * @return Hashmap of the details available for the given path id.
     * @throws IOException
     */
    public HashMap<String,Object> parsePathResponse(Response response) throws IOException {
        String responseBody = response.body().asString();
        HashMap<String,Object> result  =  new ObjectMapper().readValue( responseBody, HashMap.class);
        return result;
    }



}
