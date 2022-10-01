package framework;

import io.vertx.core.json.JsonObject;
import com.bigbasket.automation.Config;
import msvc.eta.admin.UpdateStoreStatus;
import tms.configAndUtilities.TmsDBQueries;
import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.AutomationReportListener;
import com.bigbasket.automation.utilities.AutomationUtilities;
import com.bigbasket.automation.utilities.webdriver.WebDriverUtilities;
import io.restassured.response.Response;
import msvc.catalogfc.visibility.CatalogVisibilityApi;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeSuite;
import utility.configdbupdate.CromaSlotDB;
import utility.configdbupdate.DynamicLocationDB;
import utility.configdbupdate.PickingPlatformDB;
import utility.database.DynamicLocationDBQueries;
import utility.database.PickingPlatformQueryExecuteWrapper;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class BaseTest extends WebSettings {
    public static boolean setSkuListInPrimaryBin;
    public static int MAXIMUM_NO_SKUS_ALLOWED_TO_CHECK_VISIBILITY = 25;
    public static int NUM_OF_PRODUCTS_TO_SET_IN_GLOBAL_SKULIST_IN_PRIMARY_BIN = 5;

    public static  Properties bb2UiProperties = new Properties();

    public static Properties tmsDeliveryAppProperties = new Properties();

    public static Properties adminUsers = new Properties();


    static {
        setSkuListInPrimaryBin = Boolean.parseBoolean(System.getProperty("setSkuListInPrimaryBin","true"));
    }

    Logger logger = Logger.getLogger(BaseTest.class);
    AutomationReportListener obj = new AutomationReportListener();

    public AutomationReport getInitializedReport(Class<?> testClass, boolean setupDriver) {
        AutomationReport report = obj.getInitializedReport(testClass);
        if (setupDriver) {
            WebDriver driver = WebDriverUtilities.getSimpleWebDriver(browser);
            WebDriverUtilities.maximizeDriver(driver);
            report.setWebDriver(driver);
        }
        return report;
    }

    public AutomationReport getInitializedReport(Class<?> testClass, boolean setupDriver, List<String> options) {
        AutomationReport report = obj.getInitializedReport(testClass);
        if (setupDriver) {
            WebDriver driver = WebDriverUtilities.getSimpleWebDriver(browser, options);
            WebDriverUtilities.maximizeDriver(driver);
            report.setWebDriver(driver);
        }
        return report;
    }

    public AutomationReport getInitializedReport(Class<?> testClass, String postFix, boolean setupDriver, List<String> options, Class<?>... class1) {
        AutomationReport report = obj.getInitializedReport(testClass, postFix, class1);
        if (setupDriver) {
            WebDriver driver = WebDriverUtilities.getSimpleWebDriver(browser, options);
            WebDriverUtilities.maximizeDriver(driver);
            report.setWebDriver(driver);
        }
        return report;
    }

    public AutomationReport getInitializedReport(Class<?> testClass, String postFix, boolean setupDriver, Class<?>... class1) {
        AutomationReport report = obj.getInitializedReport(testClass, postFix, class1);
        if (setupDriver) {
            WebDriver driver = WebDriverUtilities.getSimpleWebDriver(browser);
            WebDriverUtilities.maximizeDriver(driver);
            report.setWebDriver(driver);
        }
        return report;
    }

    /**
     * Method to load all list of skus in primary bin
     * This will run as testng before suite.
     */
    public void loadSkuList() {
        if(setSkuListInPrimaryBin) {
            System.out.println("Populating skulist whose inventory is present in primary bin");
            int fcId = Integer.parseInt(Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_fcid"));
            int saId = Integer.parseInt(Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_said"));
            String entryContext = Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_entry_context");
            String entryContextId = Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_entry_context_id");
            loadSkuListFromPrimayBin(fcId, entryContext, entryContextId, saId);
            System.out.println("#######skuList from primary bin populated########");
        }else {
            System.out.println("#######Not setting skuList from primary bin populated, flag is false########");
        }
    }

    /**
     * Generic method to load skuList whose inventory is in primary bin
     */
    private void loadSkuListFromPrimayBin(int fcId, String entryContext, String entryContextId, int saId) {
        int[] saArray = new int[1];
        saArray[0] = saId;

        int[] skuArray = fetchListOfSkusInPrimaryBin(fcId, MAXIMUM_NO_SKUS_ALLOWED_TO_CHECK_VISIBILITY);

        try {
            CatalogVisibilityApi catalogVisibilityApi = new CatalogVisibilityApi(entryContextId, entryContext);
            Response response = catalogVisibilityApi.getSkuPathInfov1(skuArray, saArray);
            int numOfVisibleSku = response.path("sku_id.size()");
            if (numOfVisibleSku < 10) {
                //CANNOT USE ASSERTION INSIDE BEFORE SUITE.
//                Assert.fail("Number visible skus fetched from fc_sku_list is less than 10, failing the suite " +
//                        "\n Visibility api response: " + response.prettyPrint());
            }
            skuListInPrimaryBin = new int[NUM_OF_PRODUCTS_TO_SET_IN_GLOBAL_SKULIST_IN_PRIMARY_BIN];
            for (int i = 0; i < skuListInPrimaryBin.length; i++) {
                skuListInPrimaryBin[i] = response.path("sku_id[" + i + "]");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * fetchs the skus which are in primary bin
     *
     * @param fcId
     * @param numberOfproductsReq
     * @return
     */
    public static int[] fetchListOfSkusInPrimaryBin(int fcId, int numberOfproductsReq) {
        String query = "select sku_id,quantity,bin_id,bin_location,batch_id from fc_sku_stock " +
                "where planogram_id=(select id from fc_planogram where fc_id = " + fcId + ") and " +
                "location_id=1 and quantity>5 order by  id limit " + numberOfproductsReq + ";";
        System.out.println("Fetching the sku list from fc_sku_stock table\n " + query);
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            //This method was being called in before suite & causing exception.
            //Assert.fail("No skus returned table " + query);
        }
        int numProdReturned = jsonObject.getJSONArray("rows").length();
        if (numProdReturned < numberOfproductsReq) {
            System.out.println("Couldn't get the number of products requested, Number of product fetched is : " + numProdReturned);
        }
        int skuList[] = new int[numberOfproductsReq];
        for (int i = 0; i < numberOfproductsReq; i++) {
            skuList[i] = jsonObject.getJSONArray("rows").getJSONObject(i).getInt("sku_id");
        }
        System.out.println("Returned SKU list : " + Arrays.asList(skuList));
        return skuList;
    }


    public void preRequisite() {

        System.out.println("Loading Properties files");
        try {
            bb2UiProperties.load(new FileInputStream("src//test//resources//Config//bb2_ui_config.properties"));
            tmsDeliveryAppProperties.load(new FileInputStream("src//main//resources//tms//dapiConfig.properties"));
            adminUsers.load(new FileInputStream("src//main//resources//adminUsers//adminUser.properties"));

        }
        catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() throws Exception {
        preRequisite();
        initializeSuite();
    }
    public  void initializeSuite() throws Exception {

        if(serverName.contains("prodstaging") || serverName.contains("www.bigbasket.com") )
        {
            // prod pre-req
        }
        else
        {
           // non prod pre-req
           // loadSkuList();

            System.out.println("Executing update queries on the picking platform db (pre-req)");
            if(AutomationUtilities.getEnvironmentFromServerName(serverName)=="uat")
                pickingPlatformDBUpdate();

            System.out.println("Executing update queries on the dynamic location db (pre-req)");
            dynamicLocationDBUpdate();


            System.out.println("Updating slot capacity");
            if(suiteName=="TMS")
                updateSlotInstance();


            System.out.println("Updating store status");


            if(AutomationUtilities.getEnvironmentFromServerName(serverName)=="uat")
                updateStoreStatusToOpen();

        }

    }

    private void updateStoreStatusToOpen() {
        String env = AutomationUtilities.getEnvironmentFromServerName(serverName);
        String etaDbname = env.toUpperCase()+ "-"+"ETA";

        String bbnowEntryContext= Config.bbnowConfig.getString("entry_context");
        String bbnowEntryContextId=Config.bbnowConfig.getString("entry_context_id");
        String meatnowEntryContext= Config.meatnowConfig.getString("entry_context");
        String meatnowEntryContextId=Config.meatnowConfig.getString("entry_context_id");
        int bbnowSaId = Config.bbnowConfig.getInt("bbnow_stores[1].sa_id");
        int meatnowSaId = Config.meatnowConfig.getInt("bbnow_stores[1].sa_id");
        String bbDecodedUid = null ;
        JsonObject jsonObject = new JsonObject(AutomationUtilities.executeDatabaseQuery(serverName, "select * from auth_user where username="+adminUsers.get("userName")+";"));
        try{

            bbDecodedUid= String.valueOf(jsonObject.getJsonArray("rows").getJsonObject(0).getInteger("id"));
            System.out.println("bbDecodedUid"+bbDecodedUid);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        try {
            UpdateStoreStatus.updateStoreStatus("bbnow",bbnowEntryContext,bbnowEntryContextId,bbDecodedUid,Integer.toString(bbnowSaId));
            UpdateStoreStatus.updateStoreStatus("meatnow",meatnowEntryContext,meatnowEntryContextId,bbDecodedUid,Integer.toString(meatnowSaId));
            System.out.println("Stores door open");
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
    }

    //* Loading Resources*//

    public void pickingPlatformDBUpdate(){
        for (PickingPlatformDB query : PickingPlatformDB.values()) {
            PickingPlatformQueryExecuteWrapper.executeMicroserviceDataBaseQuery(query.getValue());
        }
    }

    public void dynamicLocationDBUpdate(){
        for (DynamicLocationDB query : DynamicLocationDB.values()) {
            DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query.getValue());
        }
    }

    public void updateSlotInstance() {
        String env = AutomationUtilities.getEnvironmentFromServerName(serverName);
        String mavericksDbName = env.toUpperCase()+ "-"+"MAVERICKS-CROMA";
        for (CromaSlotDB query : CromaSlotDB.values()) {
            TmsDBQueries.updateSlotInstanceDetails(mavericksDbName,query.getValue());
        }
    }
}