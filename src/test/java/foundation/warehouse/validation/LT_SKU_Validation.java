package foundation.warehouse.validation;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import foundation.catalog_apis.CatalogVisiblityAPIs;
import foundation.path_apis.PathDetailsAPIs;
import io.restassured.response.Response;
import io.vertx.core.json.JsonArray;
import org.apache.commons.io.IOUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LT_SKU_Validation extends BaseTest {

    @DescriptionProvider(slug = "Path & Path-type validation of LT Sku", author = "Robin", description = "Path and path type validation of LT SKUs", authorEmail = "rajnish.robin@bigbasket.com")
    @Test(dataProvider = "data-provider", groups = {"bb2foundation", "bb2catalog99"}, invocationCount = 1)
    public void LT_SKU_Validation_test(Map<String, Integer> data) throws Exception {
        AutomationReport report = getInitializedReport(this.getClass(), "SkuId: " + data.get("sku_id").toString(), false, Map.class);

        ArrayList<Integer> sku_list = new ArrayList<>();
        sku_list.add(data.get("sku_id"));
        ArrayList<Integer> sa_list = new ArrayList<>();
        sa_list.add(data.get("sa_id"));

        CatalogVisiblityAPIs catalogVisiblityAPIs = new CatalogVisiblityAPIs(report);
        String visiblityBody = catalogVisiblityAPIs.createCatalogVisiblityBody(sku_list, sa_list);
        Response visiblityResponse = catalogVisiblityAPIs.requestSKUPathInfo(visiblityBody);
        int path_id = catalogVisiblityAPIs.fetchPathIdFormResponse(visiblityResponse);

        PathDetailsAPIs pathDetailsAPIs = new PathDetailsAPIs(report);
        Response pathResponse = pathDetailsAPIs.requestPathDetails(path_id);
        HashMap<String, Object> pathDetails = pathDetailsAPIs.parsePathResponse(pathResponse);

        Assert.assertEquals(pathDetails.get("path_type").toString(), data.get("path_type").toString(), "Path type is not Whats Expected");
        Assert.assertEquals(pathDetails.get("src_fc").toString(), data.get("src_fc").toString(), "src_fc is not Whats Expected");
        Assert.assertEquals(pathDetails.get("dest_fc").toString(), data.get("dest_fc").toString(), "dest_fc is not Whats Expected");
    }

    @DataProvider(parallel = true, name = "data-provider")
    public Object[] dataProviderMethod() throws IOException {
        //Input stream to load file.
        InputStream inputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("foundation//warehouse//dsSkuValidationConfigObject.json");

        //Creating JsonObject from string.
        String jsonStr = IOUtils.toString(inputStream, "UTF-8");
        JsonArray jsonArray = new JsonArray(jsonStr);

        Object[] obj = new Object[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
            Map<Object, Object> dataMap = new HashMap<>();
            dataMap.put("sku_id", jsonArray.getJsonObject(i).getInteger("sku_id"));
            dataMap.put("sa_id", jsonArray.getJsonObject(i).getInteger("sa_id"));
            dataMap.put("src_fc", jsonArray.getJsonObject(i).getInteger("src_fc"));
            dataMap.put("dest_fc", jsonArray.getJsonObject(i).getInteger("dest_fc"));
            dataMap.put("path_type", jsonArray.getJsonObject(i).getInteger("path_type"));
            obj[i] = dataMap;
        }
        return obj;
    }
}

