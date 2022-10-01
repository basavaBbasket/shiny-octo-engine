package foundation.catalog_apis;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.AutomationReport;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import foundation.RequestMethods;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A general class to make catalog visiblity API calls and parse response.
 * The class also contains utility methods.
 */
public class CatalogVisiblityAPIs extends WebSettings {
    private AutomationReport report;
    private String APIEndPoint = "/catalog_visibility/internal/v1/visibility";


    public CatalogVisiblityAPIs(AutomationReport report) {
        this.report = report;
    }

    /**
     * making post request to catalog visiblity API with validating the response code
     * @param body request body containing list of SKUs and SA_ids
     * @return response from catalog visiblity API
     * @throws Exception In case if the response code is not 200
     */
    public Response requestSKUPathInfo(String body) throws Exception {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Entry-Context", "bigbasket-b2c");
        headerMap.put("X-Entry-Context-Id", "100");
        RequestMethods request = new RequestMethods(report,msvcServerName, APIEndPoint);
        Response response = request.postRequestAPIResponse(headerMap,body);
        if(response.getStatusCode()!=200)
            throw new Exception("Got Incorrect status code.");
        return response;
    }


    /**
     * prepare body for catalog visiblity API
     * @param sku_list take the list of sku_id to be checked for visiblity
     * @param sa_ids List of SA_id to be checked for visiblity
     * @return A string containg the complete body to be passed in the catalog visiblity API
     * @throws JsonProcessingException
     */
    public String createCatalogVisiblityBody(ArrayList<Integer> sku_list, ArrayList<Integer> sa_ids) throws JsonProcessingException {
        HashMap<String,Object> requestMap = new HashMap<>();
        requestMap.put("sku_id",sku_list);
        requestMap.put("sa_id",sa_ids);
        ObjectMapper mapperObj = new ObjectMapper();
        String jsonResp = mapperObj.writeValueAsString(requestMap);
        System.out.println(jsonResp);
        return jsonResp;
    }

    /**
     * parse the catalog visiblity response to get the path_id of the SKU
     * @param response response recived from catalog visiblity API
     * @return path_id of the given SKU
     */
    public int fetchPathIdFormResponse(Response response){
        return response.path("visibility[0].path_id[0]");
    }

}

