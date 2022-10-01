package api.warehousecomposition.planogram_FC;

import api.warehousecomposition.planogram_FC.internal.Helper;
import com.bigbasket.automation.mapi.mapi_4_1_0.ProductSearch;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.joda.time.DateTime;
import org.testng.Assert;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class EligibleSkuMethods extends Helper {
    private static AtomicInteger SKU_LIST_IN_PRIMARY_BIN_COUNTER = new AtomicInteger(0);

    private static String categoryTree="/ui-svc/v1/category-tree";

    /**
     * provides the sku which has inventory in primary bin
     *
     * @param adminCookie admin login cookie
     * @param fcId        fcId
     * @param report      report instance
     * @return skuId
     */
    @Deprecated
    public static int skuAvailableInPrimaryBin(Map<String, String> adminCookie, int fcId, IReport report) {
        report.log("Current skulist present in primary bin" + Arrays.toString(skuListInPrimaryBin), true);
        int skuID = skuListInPrimaryBin[SKU_LIST_IN_PRIMARY_BIN_COUNTER.getAndIncrement() % NUM_OF_PRODUCTS_TO_SET_IN_GLOBAL_SKULIST_IN_PRIMARY_BIN];
        if (!InventoryMethods.isPrimaryBinMappedForSku(adminCookie, skuID, fcId, report)) {
            Assert.fail("No sku Available in primary bin for skuId: " + skuID);
        }
        report.log("Sku " + skuID + " available in primary bin", true);
        return skuID;
    }

    /**
     * Provides the sku which is visible and has mapped to primary bin
     *
     * @param memberCookie
     * @param adminCookie
     * @param fcId
     * @param entryContext
     * @param entryContextId
     * @param areaName
     * @param searchType     takes the value "ps" or "pc" to perform the search. "pc" is for category search, "ps" is for user search
     * @param searchTerm if searchtype is "ps" then can use random search term otherwise need to specify the correct category slug
     * @param report
     * @return
     */
    public static int fetchSkuVisibleAndAvailableInPrimaryBin(Map<String, String> memberCookie, Map<String, String> adminCookie,
                                                              int fcId, String entryContext, int entryContextId, String areaName,
                                                              String searchTerm, String searchType, IReport report) {

        Assert.assertTrue(searchType.equalsIgnoreCase("ps") || searchType.equalsIgnoreCase("pc"), "" +
                "Search type should be either 'ps' or 'pc'");
        List<String> availableSkus = ProductSearch.getAvailableProductFromSearch(memberCookie,searchType, searchTerm, areaName,
                entryContext, entryContextId, report);
        report.log("List of skus visible : " + availableSkus.toString(), true);
        boolean isSkuAvailable = false;
        int skuId = -999;

        for (int i = 0; i < availableSkus.size(); i++) {
            skuId = Integer.parseInt(availableSkus.get(i));
            if (InventoryMethods.isPrimaryBinMappedForSku(adminCookie, skuId, fcId, report)) {
                report.log("Sku " + skuId + " Visible and available in primary bin", true);
                isSkuAvailable = true;
                break;
            }
        }
        if (!isSkuAvailable) {
            Assert.fail("Out of the below visible sku list, none are available in primary bin " +
                    "\n Visible sku list is : " + availableSkus.toString());
        }
        report.log("Sku Visible and Available in primary bin is : "+skuId, true);
        System.out.println("Sku Visible and Available in primary bin is : "+skuId);
        return skuId;
    }

    /**
     * Provides the sku which is visible and has mapped to primary bin
     *
     * @param memberCookie
     * @param adminCookie
     * @param fcId
     * @param entryContext
     * @param entryContextId
     * @param areaName
     * @param searchType     takes the value "ps" or "pc" to perform the search. "pc" is for category search, "ps" is for user search
     * @param searchTermList if searchtype is "ps" then can use random search term otherwise need to specify the correct category slug
     * @param report
     * @return
     */

    public static int fetchSkuVisibleAndAvailableInPrimaryBin(Map<String, String> memberCookie, Map<String, String> adminCookie,
                                                              int fcId, String entryContext, int entryContextId, String areaName,
                                                              List<String> searchTermList, String searchType, IReport report) {

        Assert.assertTrue(searchType.equalsIgnoreCase("ps") || searchType.equalsIgnoreCase("pc"), "" +
                "Search type should be either 'ps' or 'pc'");

        report.log("Slugs: "+searchTermList,true);

        boolean isSkuAvailable = false;
        int skuId = -999;

        for(String searchTerm: searchTermList) {
            if (!isSkuAvailable) {
                List<String> availableSkus = ProductSearch.getAvailableProductFromSearch(memberCookie, searchType, searchTerm, areaName,
                        entryContext, entryContextId, report);
                report.log("List of skus visible : " + availableSkus.toString(), true);


                for (int i = 0; i < availableSkus.size(); i++) {
                    skuId = Integer.parseInt(availableSkus.get(i));
                    if (InventoryMethods.isPrimaryBinMappedForSku(adminCookie, skuId, fcId, report)) {
                        report.log("Sku " + skuId + " Visible and available in primary bin", true);
                        isSkuAvailable = true;
                        break;
                    }
                }

            }
        }
        if (!isSkuAvailable) {
            Assert.fail("Out of the below visible sku list, none are available in primary bin " );
        }
        report.log("Sku Visible and Available in primary bin is : "+skuId, true);
        System.out.println("Sku Visible and Available in primary bin is : "+skuId);
        return skuId;
    }



    /**
     * Provides the sku list which is visible and has mapped to primary bin
     *
     * @param memberCookie
     * @param adminCookie
     * @param fcId
     * @param entryContext
     * @param entryContextId
     * @param areaName
     * @param searchType type should be either "ps" or "pc"
     * @param searchTerm if searchtype is "ps" then can use random search term otherwise need to specify the correct category slug
     * @param report
     * @return
     */
    public static List<Integer> fetchListOfSkuVisibleAndAvailableInPrimaryBin(Map<String, String> memberCookie, Map<String, String> adminCookie,
                                                                              int fcId, String entryContext, int entryContextId, String areaName,
                                                                              String searchType,String searchTerm, IReport report) {
        List<String> availableSkus = ProductSearch.getAvailableProductFromSearch(memberCookie,searchType, searchTerm, areaName,
                entryContext, entryContextId, report);
        report.log("List of skus visible : " + availableSkus.toString(), true);
        System.out.println("List of skus visible : " + availableSkus.toString());
        boolean isSkuAvailable = false;
        int skuId = -999;
        List<Integer> visibleAndAvailableSkus = new ArrayList<>();
        for (int i = 0; i < availableSkus.size(); i++) {
            skuId = Integer.parseInt(availableSkus.get(i));
            if (InventoryMethods.isPrimaryBinMappedForSku(adminCookie, skuId, fcId, report,false)) {
                System.out.println("Sku " + skuId + " Visible and available in primary bin");
                visibleAndAvailableSkus.add(skuId);
                isSkuAvailable = true;
            }
        }
        if (!isSkuAvailable) {
            Assert.fail("Out of the below visible sku list, none are available in primary bin " +
                    "\n Visible sku list is : " + availableSkus.toString());
        }
        report.log("List of skus visible and available : " + visibleAndAvailableSkus.toString(), true);
        System.out.println("List of skus visible and available : " + visibleAndAvailableSkus.toString());
        return visibleAndAvailableSkus;
    }

    public static List<Integer> fetchListOfSkuVisibleAndAvailableInPrimaryBin(Map<String, String> memberCookie, Map<String, String> adminCookie,
                                                                              int fcId, String entryContext, int entryContextId, String areaName,
                                                                              String searchType,List<String> searchTermList, IReport report) {
        List<Integer> visibleAndAvailableSkus = new ArrayList<>();
        boolean isSkuAvailable = false;
        for(String searchTerm: searchTermList) {
            List<String> availableSkus = ProductSearch.getAvailableProductFromSearch(memberCookie, searchType, searchTerm, areaName,
                    entryContext, entryContextId, report);
            report.log("List of skus visible : " + availableSkus.toString(), true);
            System.out.println("List of skus visible : " + availableSkus.toString());

            int skuId = -999;

            for (int i = 0; i < availableSkus.size(); i++) {
                skuId = Integer.parseInt(availableSkus.get(i));
                if (InventoryMethods.isPrimaryBinMappedForSku(adminCookie, skuId, fcId, report, false)) {
                    System.out.println("Sku " + skuId + " Visible and available in primary bin");
                    visibleAndAvailableSkus.add(skuId);
                    isSkuAvailable = true;
                }
            }

        }
        if (!isSkuAvailable) {
            Assert.fail("Out of the below visible sku list, none are available in primary bin ");
        }
        report.log("List of skus visible and available : " + visibleAndAvailableSkus.toString(), true);
        System.out.println("List of skus visible and available : " + visibleAndAvailableSkus.toString());
        return visibleAndAvailableSkus;
    }

    /**
     * Provides the sku list by pc search which is visible and has mapped to primary bin
     *
     * @param memberCookie logged in user member cookie
     * @param adminCookie logged in user admin cookie
     * @param fcId  fcID
     * @param entryContext  entryContext
     * @param entryContextId entryContextID
     * @param areaName location for setting current address
     * @param report
     * @return list of visible and mapped to primary bin skus
     */

    public static int fetchListOfVisibleSkuAndAvailableInPrimaryBin(Map<String, String> memberCookie, Map<String, String> adminCookie,
                                                                    int fcId, String entryContext, int entryContextId, String areaName, IReport report) throws Exception {

        List<String> availableSkus = new ArrayList<>();
        List<String> categoryList=getProductCategoryList(memberCookie,report,entryContext, String.valueOf(entryContextId));
        for(String searchTerm: categoryList){
            availableSkus = ProductSearch.getAvailableSkuFromSearch(memberCookie,"pc",searchTerm , areaName, entryContext, entryContextId, report);
            if(availableSkus.size()>0)
                break;
        }
        report.log("List of skus visible : " + availableSkus.toString(), true);
        boolean isSkuAvailable = false;
        int skuId = -999;

        for (int i = 0; i < availableSkus.size(); i++) {
            skuId = Integer.parseInt(availableSkus.get(i));
            if (InventoryMethods.isPrimaryBinMappedForSku(adminCookie, skuId, fcId, report)) {
                report.log("Sku " + skuId + " Visible and available in primary bin", true);
                isSkuAvailable = true;
                break;
            }
        }
        if (!isSkuAvailable) {
            Assert.fail("Out of the below visible sku list, none are available in primary bin " +
                    "\n Visible sku list is : " + availableSkus.toString());
        }
        report.log("Sku Visible and Available in primary bin is : "+skuId, true);
        System.out.println("Sku Visible and Available in primary bin is : "+skuId);
        return skuId;
    }

    private static List<String> getProductCategoryList(Map<String, String> memberCookie, IReport report, String entryContext, String entryContextId) throws Exception {

        List<String> categoryList = new ArrayList<>();

        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Content-Type","application/json");
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Entry-Context-Id",entryContextId);
        requestHeader.put("X-Entry-Context",entryContext);
        requestHeader.put("X-TimeStamp", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));


        Response response = RestAssured.given().spec(getSimpleRequestSpecification(msvcServerName,report))
                .headers(requestHeader)
                .cookies(memberCookie)
                .get(serverName+ categoryTree)
                .then().log().all()
                .extract().response();
        if (response.getStatusCode() != 200)
            throw new Exception("Got Incorrect status code"+response.getStatusCode());
        Assert.assertTrue(response.getStatusCode() == 200, "Incorrect Status code");

        JSONObject jsonObject = new JSONObject(response.asString());
        int categorySize = jsonObject.getJSONArray("categories").length();
        for (int i = 0; i < categorySize; i++) {
            String pc = jsonObject.getJSONArray("categories").getJSONObject(i).getString("slug");
            categoryList.add(pc);
        }
        return categoryList;
    }
}
