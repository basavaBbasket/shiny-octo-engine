package msvc;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import com.bigbasket.automation.utilities.AutomationUtilities;
import com.opencsv.CSVReader;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import msvc.planogram.TransferInGeneralMethods;
import msvc.planogram.external.warehouseID.*;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.testng.Assert;
import utility.database.DynamicLocationDBQueries;
import utility.dataset.DynamicLocationJobType;
import utility.dataset.TransferInType;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DynamicLocationGeneralMethods extends WebSettings {
    private static Logger logger = Logger.getLogger(DynamicLocationGeneralMethods.class);

    public static String STACKING_WAREHOUSEID = "BBMUM001";
    public static String STACKING_CITY = "Mumbai";
    public static String STORE_TYPE = "DC";

    public static String CYCLECOUNT_USERID = "komal";
    public static String STACKER_USERID = "3412";
    public static String STACKER_USERNAME = "sbp1";
    public static String STACKER_USERID_2 = "1234";
    public static String STACKER_USERID_3 = "komal";
    public static int GLOBAL_REPLENISHMENT_X = 50;
    public static int GLOBAL_REPLENISHMENT_Y = 40;
    public static int GLOBAL_REPLENISHMENT_Z = 10;
    public static int SKUID1_DATAMODIFIED = 263593;  //Data modified for this sku in hqa
    public static int SKUID2_DATAMODIFIED = 100401160; //Data modified for this sku in hqa

    public static int SKUID3 = 40001610; //HQA
    public static int SKUID4 = 40025066; //HQA

    public static int BULK_SKUID_1 = 10000203;
    public static int CHILD_SKUID_1 = 10000204;

    private static int looseQty = 48;
    private static int containerQty = 162;
    private static String mfgDate = "2020-06-12";
    private static String expDate = "2020-05-27";
    private static boolean isBundlepack = true;
    private static double cp = 91.50;
    private static double mrp = 93.50;
    private static double eretailmrp = 122.50;
    private static double gst = 4.1;
    private static double cess = 4.4;
    private static int containerID = 1;
    private static String containerTag = "tag1";
    private static String containerType = "type1";
    private static boolean unknownDimension = true;
    private static String rtvId = "1";

    /**
     * +
     * This util takes file path,columnnum and rownum and returns the value present in that cell
     *
     * @param filePath
     * @param rowNum
     * @param columnNum
     * @return
     * @throws IOException
     */
    public static String readCSVForGivenRowCol(String filePath, int rowNum, int columnNum) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(filePath));
        String[] nextLine = null;
        int count = 0;
        boolean flag = false;
        while ((nextLine = reader.readNext()) != null) {
            if (nextLine != null) {
                //Verifying the read data here
                if (count == rowNum) {
                    System.out.println("Cell value at row : " + rowNum + " col : " + columnNum + "\nValue:" + nextLine[columnNum]);
                    flag = true;
                    break;
                }
                count++;
            }
        }
        if (!flag) {
            Assert.fail("index out of bound, check row, column value");
        }
        return nextLine[columnNum];
    }

    /**
     * Checks whether the entry for the given rackname exists
     *
     * @param rackName
     * @return
     */
    public static boolean checkWhetherRackNameExists(String rackName) {
        boolean flag = false;
        String query = "select * from rack_variant where rack_variant_name=\"" + rackName + "\";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            System.out.println("Rack Variant : " + rackName + " does not exist");
        } else {
            flag = true;
            System.out.println("Rack Variant : " + rackName + " exists");
        }
        return flag;
    }

    /**
     * Deletes the entries from rack_shelf_bin, rack_shelf and rack_variant for the given rack name
     *
     * @param rackName
     * @param report
     */
    public static void deleteRackNameFromDB(String rackName, IReport report) {
        String query = "delete from rack_shelf_bin where rack_shelf_id in " +
                "(select id from rack_shelf where rack_variant_id in " +
                "(select id from rack_variant where rack_variant_name = \"" + rackName + "\") );";
        String responseStr = DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query);
        report.log("Deleting the entry from rack_shelf_bin for rack_name: " + rackName + " " +
                "\n Query executed : " + query, true);
        query = "delete from rack_shelf where rack_variant_id =" +
                "(select id from rack_variant where rack_variant_name = \"" + rackName + "\");";
        responseStr = DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query);
        report.log("Deleting the entry from rack_shelf for rack_name: " + rackName + " " +
                "\n Query executed : " + query, true);
        query = "delete from rack_variant where rack_variant_name = \"" + rackName + "\";";
        responseStr = DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query);
        report.log("Deleting the entry from rack_variant for rack_name: " + rackName + " " +
                "\n Query executed : " + query, true);
        query = "select * from rack_variant where rack_variant_name = \"" + rackName + "\";";
        responseStr = DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query);
        report.log("Getting the entry from rack_variant for rack_name: " + rackName + " " +
                "\n Query executed : " + query, true);
        JSONObject jsonObject = new JSONObject(responseStr);
        if (jsonObject.getInt("numRows") != 0) {
            Assert.fail("RackVariant with name " + rackName + " is not deleted from db, cannot do the rackupload with same name");
        }

    }

    /**
     * Checks Whether the rack variant upload is sucessful
     *
     * @param rackName
     * @param report
     * @return
     */
    public static boolean checkRackVariantUploadSucessful(String rackName, IReport report) {
        try {
            System.out.println("Waiting for 2sec before querying the DB");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String query = "select * from rack_variant where rack_variant_name = \"" + rackName + "\";";
        String responseStr = DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query);
        JSONObject jsonObject = new JSONObject(responseStr);
        if (jsonObject.getInt("numRows") != 0) {
            report.log("RackVariant with name " + rackName + " is uploaded sucessful", true);
            return true;
        } else {
            report.log("RackVariant with name " + rackName + " is not uploaded sucessful \n" +
                    "No data returned for query: " + query, true);
            return false;
        }
    }

    /**
     * Checks whether the given rack type exists
     *
     * @param rackType
     * @param report
     * @return
     */
    public static boolean checkWhetherRackTypeExists(String rackType, IReport report) {
        boolean flag = false;
        String query = "select * from racktype where racktype_name=\"" + rackType + "\";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            System.out.println("Rack Type : " + rackType + " does not exist");
            report.log("Rack Type : " + rackType + " does not exist", true);
        } else {
            flag = true;
            System.out.println("Rack Type : " + rackType + " exists");
            report.log("Rack Type : " + rackType + " exist", true);
        }
        return flag;
    }

    /**
     * Deletes the given racktype from DB, if it is not used by any rackvariant
     *
     * @param rackType
     * @param report
     */
    public static void deleteRackTypeFromDB(String rackType, IReport report) {
        String query = "delete from racktype where racktype_name=\"" + rackType + "\";";
        String responseStr = DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query);
        report.log("Deleting the entry from racktype table for racktype_name: " + rackType + " " +
                "\n Query executed : " + query, true);
        query = "select * from racktype where racktype_name = \"" + rackType + "\";";
        responseStr = DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query);
        report.log("Getting the entry from racktype table for racktype_name: " + rackType + " " +
                "\n Query executed : " + query, true);
        JSONObject jsonObject = new JSONObject(responseStr);
        if (jsonObject.getInt("numRows") != 0) {
            Assert.fail("Racktype with name " + rackType + " is not deleted from db, cannot add the rack name with same name");
        }

    }


    /**
     * Checks whether the entry is created in rackvariant, rackshelf and rackshelfbin table and returns boolean value
     *
     * @param rackName
     * @return
     */
    public static boolean checkWhetherEntryIsCreatedInRackVariantRackShelfRackShelfBin(String rackName, IReport report) {
        boolean flag = false;
        report.log("Checking whether the entry is created in rackvariant, rackshelf and rackshelfbin table as part of upload", true);
        String query = "select id from rack_shelf_bin where rack_shelf_id in (select id from rack_shelf where rack_variant_id = \n" +
                "(select id from rack_variant where rack_variant_name=\"" + rackName + "\"));";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            report.log("Rack Variant : " + rackName + " does not exist, upload didn't created entry", true);
        } else {
            flag = true;
            report.log("Rack Variant : " + rackName + " exists", true);
            //Assert.fail("Rack Variant : " + rackName + " exists");
        }
        return flag;
    }

    /**
     * Fetchs the numberofpickingshelfs for the given rack name
     *
     * @param rackName
     * @param report
     * @return
     */
    public static int fetchNumberOfPickingShelfsForGivenRackName(String rackName, IReport report) {
        report.log("Fetching number of picking shelfs for given rackname", true);
        String query = "select num_of_picking_shelves from rack_variant where rack_variant_name=\"" + rackName + "\";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            report.log("Rack Variant : " + rackName + " does not exist", true);
            Assert.fail("Rack Variant : " + rackName + " exists \n no entries returned from table");
        } else {
            report.log("Rack Variant : " + rackName + " exists", true);
        }
        int numberPickingShelfs = 0;
        numberPickingShelfs = jsonObject.getJSONArray("rows").getJSONObject(0).getInt("num_of_picking_shelves");
        report.log("Number of picking shelfs for the rack name: " + rackName + " is : " + numberPickingShelfs, true);
        return numberPickingShelfs;
    }

    /**
     * Fetchs the total number of shelfs for the given rack name
     *
     * @param rackName
     * @param report
     * @return
     */
    public static int fetchTotalNumberOfShelfsForGivenRackName(String rackName, IReport report) {
        report.log("Fetching total number of shelfs for given rackname", true);
        String query = "select num_of_shelves from rack_variant where rack_variant_name=\"" + rackName + "\";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            report.log("Rack Variant : " + rackName + " does not exist", true);
            Assert.fail("Rack Variant : " + rackName + " exists \n no entries returned from table \n Query Executed" + query);
        } else {
            report.log("Rack Variant : " + rackName + " exists", true);
        }
        int numOfShelfs = 0;
        numOfShelfs = jsonObject.getJSONArray("rows").getJSONObject(0).getInt("num_of_shelves");
        report.log("Total Number of shelfs for the rack name: " + rackName + " is : " + numOfShelfs, true);
        return numOfShelfs;
    }

    /**
     * Checks whether thier is a hole for a given rack and returns boolean value
     *
     * @param rackName
     * @param report
     * @return
     */
    public static boolean checkWhetherTheirIsAHoleInGivenRack(String rackName, IReport report) {
        report.log("Checking whether thier is a hole in the rack: " + rackName, true);
        String query = "select shelf_name from rack_shelf where rack_variant_id = " +
                "(select id from rack_variant where rack_variant_name=\"" + rackName + "\") and is_hole = 1;";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            report.log("No holes in the rack " + rackName, true);
            return false;
        } else {
            report.log("Rack Variant : " + rackName + " exists", true);
        }
        String shelfNameWithHole = null;
        shelfNameWithHole = jsonObject.getJSONArray("rows").getJSONObject(0).getString("shelf_name");
        report.log("Shelfname with hole is " + shelfNameWithHole, true);
        return true;
    }

    /**
     * Returns the response of the upload for the given uploadName
     *
     * @param uploadName
     * @param status
     * @param report
     * @return
     */
    public static String getUploadResponseFromUploadManager(String uploadName, String status, IReport report) {
        //status codes - "validation_failed"
        report.log(" Fetching latest upload response for  " + uploadName, true);
        String query = "select s3_download_path from upload_manager_uploadmanager " +
                "where upload_name=\"" + uploadName + "\" and status=\"" + status + "\" order by id DESC limit 1;";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No entry returned from table upload_manager_uploadmanager\n Query executed" +
                    " " + query);
        }
        String s3ResponsePath = jsonObject.getJSONArray("rows").getJSONObject(0).getString("s3_download_path");
        Response response = RestAssured.given()
                .log()
                .all()
                .get(s3ResponsePath)
                .then()
                .log()
                .all()
                .extract().response();
        report.log("Latest " + uploadName + " response is " + response.getBody().asString(), true);

        System.out.println("Status Code : " + response.statusLine());
        validateResponse(response);
        return response.asString();

    }

    private static void validateResponse(Response response) {
        Assert.assertTrue(response.getStatusCode() == 200, response.getStatusLine() + "\n");
        String status = response.getStatusLine();
        Assert.assertTrue(status.contains("0") || status.contains("OK"), response.getStatusLine() + "\n");
    }

    /**
     * check whether planogram exists for given warehouse
     *
     * @param whCode
     * @param report
     * @return boolean
     */
    public static boolean checkIfPlanogramExists(String whCode, IReport report) {
        boolean flag = false;
        String query = "select * from fc_planogram where wh_code = \"" + whCode + "\";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            System.out.println("planogram doesn't exist for warehouse " + whCode);
            report.log("planogram doesn't exist for warehouse " + whCode, true);
        } else {
            flag = true;
            System.out.println("planogram already exist for warehouse " + whCode);
            report.log("planogram already exist for warehouse " + whCode, true);
        }
        return flag;
    }

    /**
     * deletes planogram for given warehouse
     *
     * @param whCode
     * @param report
     * @return
     */

    public static void deletePlanogramFromDB(String whCode, IReport report) {
        int fc_planogram_id;
        String query = "select id from fc_planogram where wh_code = \"" + whCode + "\";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 1) {
            fc_planogram_id = jsonObject.getJSONArray("rows").getJSONObject(0).getInt("id");
            report.log("planogram id = " + fc_planogram_id, true);
        } else {
            System.out.println("no record found for given warehouse code" + whCode);
            report.log("no record found for given warehouse code", true);
            return;
        }

        report.log("Deleting fc_rack_shelf_bin" +
                "\n Query executed : " + query, true);
        query = "delete from fc_rack_shelf_bin where fc_rack_shelf_id in (select id from fc_rack_shelf where fc_rack_id in (select id from fc_rack where fc_aisle_id in (select id from fc_aisle where fc_floor_id in (select id from fc_floor where fc_planogram_id = " + fc_planogram_id + "))));";
        String response = DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query);

        report.log("Deleting fc_rack_shelf" +
                "\n Query executed : " + query, true);
        query = "delete from fc_rack_shelf where fc_rack_id in (select id from fc_rack where fc_aisle_id in (select id from fc_aisle where fc_floor_id in (select id from fc_floor where fc_planogram_id = " + fc_planogram_id + ")));";
        response = DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query);

        report.log("Deleting fc_rack" +
                "\n Query executed : " + query, true);
        query = "delete from fc_rack where fc_aisle_id in (select id from fc_aisle where fc_floor_id in (select id from fc_floor where fc_planogram_id = " + fc_planogram_id + "));";
        response = DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query);

        report.log("Deleting fc_aisle" +
                "\n Query executed : " + query, true);
        query = "delete from fc_aisle where fc_floor_id in (select id from fc_floor where fc_planogram_id = " + fc_planogram_id + ");";
        response = DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query);

        report.log("Deleting fc_floor" +
                "\n Query executed : " + query, true);
        query = "delete from fc_floor where fc_planogram_id = " + fc_planogram_id + ";";
        response = DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query);

        report.log("Deleting planogram" +
                "\n Query executed : " + query, true);
        query = "delete from fc_planogram where id = " + fc_planogram_id + ";";
        response = DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query);

    }

    /**
     * check whether planogram upload is successful
     *
     * @param whCode
     * @param report
     * @return boolean
     */
    public static boolean checkPlanogramUploadSuccessful(String whCode, IReport report) {
        try {
            System.out.println("Waiting for 2sec before querying the DB");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String query = "select * from fc_planogram where wh_code = \"" + whCode + "\";";
        String responseStr = DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query);
        report.log("checking entry is present in DB \n" + query, true);
        JSONObject jsonObject = new JSONObject(responseStr);
        if (jsonObject.getInt("numRows") != 0) {
            report.log("planogram for warehouse " + whCode + " is uploaded successfully", true);
            return true;
        } else {
            report.log("planogram for warehouse " + whCode + " is not uploaded \n" +
                    "No data returned for query: " + query, true);
            return false;
        }
    }

    /**
     * Fetching the batchID for given skuID and planogramID
     *
     * @param skuID
     * @param planogramID
     * @param report
     * @return
     */
    public static int getBatchIdForGivenSkuIDPlanogramID(int skuID, int planogramID, IReport report) {
        String query = "select batch_id from stacking_batch_info where sku_id = " + skuID + " and planogram_id =" + planogramID + " order by id DESC limit 1;";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        report.log("Fetching batchID \n Executing Query: " + query, true);
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the table stacking_batch_info for query " + query);
        }
        int batchID = 0;
        for (int i = 0; i < jsonObject.getInt("numRows"); i++) {
            batchID += jsonObject.getJSONArray("rows").getJSONObject(i).getInt("batch_id");
        }
        report.log("BatchID for the given skuID: " + skuID + " PlanogramId : "
                + planogramID + " is : " + batchID, true);
        return batchID;
    }

    /**
     * Fetching the warehouseID for given planogramID
     *
     * @param planogramID
     * @return
     */
    public static String getWareHouseIDForGivenPlanogramID(int planogramID) {
        String query = "select wh_code from fc_planogram where id=" + planogramID + ";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the table fc_planogram for query " + query);
        }
        return jsonObject.getJSONArray("rows").getJSONObject(0).getString("wh_code");
    }

    /**
     * Fetching the planogramID for given warehouseID
     *
     * @param warehouseID
     * @return
     */
    public static int getPlanogramIDForGivenWareHouseID(String warehouseID) {
        String query = "select id from fc_planogram where wh_code=\"" + warehouseID + "\";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the table fc_planogram for query " + query);
        }
        return jsonObject.getJSONArray("rows").getJSONObject(0).getInt("id");
    }

    /**
     * Fetchs the skuID for the given SKUID for Stacking Workflow
     *
     * @param warehouseID
     * @return
     */
    public static int getSkuIDForStackingWorkFlow(String warehouseID, IReport report) {
        //TODO to write logic for fetching sku
        /*
        Conditions to satisify
        1.SKUBIN mapping should be present
        2. Volume should be present in bin location
        3. Unstacked qty should be present for the sku (check and perform transferin if needed if
        minimum 100 unstacked qty is not present)
        4. Update mbq for the sku
         */
        int skuID = 40092231;
        int dcID = getDCIDForGivenWareHouseID(warehouseID, report);
        DynamicLocationGeneralMethods.updateMBQAndMBQDate(10, skuID, dcID, report);
        return skuID;
    }


    /**
     * Fetching the unstacked_quantity for given skuID and planogramID
     *
     * @param skuID
     * @param planogramID
     * @param report
     * @return
     */
    public static int getUnstackedQtyForGivenSkuIDPlanogramID(int skuID, int planogramID, TransferInType transferInType, IReport report) {
        String query = "select sum(quantity)-sum(stacked_quantity) as unstacked_qty from transferin_sku_info where sku_id=" + skuID + " and planogram_id=" + planogramID + "" +
                " and transferin_type_id=" + DynamicLocationGeneralMethods.getTransferinIDForGivenTransferinType(transferInType, report) + ";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        report.log("Fetching Unstacked Qty \n Executing Query: " + query, true);
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the table transferin_sku_info for query " + query);
        }
        double unstackedQty = 0;
        unstackedQty = jsonObject.getJSONArray("rows").getJSONObject(0).getDouble("unstacked_qty");
        report.log("UnstackedQty for the given skuID: " + skuID + " warehouseID  "
                + DynamicLocationGeneralMethods.getWareHouseIDForGivenPlanogramID(planogramID) + " is: " + unstackedQty, true);

        return (int) unstackedQty;
    }


    /**
     * Gives the entire row details from job table for given jobID
     *
     * @param jobID
     * @param report
     * @return
     */
    public static JSONObject getDetailsFromJobTable(int jobID, IReport report) {
        String query = "select * from job where id = " + jobID + ";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        report.log("Fetching job details \n Executing Query: " + query, true);
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the table job for query " + query);
        }
        report.log("Job Details returned is: " + jsonObject.toString(), true);
        return jsonObject;
    }

    //TODO import dependencies enable the util
    /* *//**
     * Does admin login and navigate to planogram admin page
     *
     * @param driver
     * @param report
     *//*
    public void doLoginNavigateToPlanogramAdmin(WebDriver driver, IReport report) {
        //Admin Login
        report.log("Launching the admin",true);
        launchSite("admin", driver);
        Login login = new admin.pages.Login(driver, report);
        HomePage homePage = login.loginAdmin("su");


        //Navigating to planogram ui admin page
        report.log( "Navigating to planogram ui admin page", true);
        Microservices microservices = homePage.clickOnMicroservicesURLs();
        PlanogramMicroservices planogramMicroservices = microservices.clickOnPlanogram();
        PlanogramAdminUI planogramAdminUI = planogramMicroservices.clickOnPlanogramLink();
        if (driver.getTitle().contains("bigbasket Admin")) {
            login.loginAdmin("su");
        }
    }*/

    /**
     * Gives the entire row details from task table for given jobID
     *
     * @param jobID
     * @param report
     * @return
     */
    public static JSONObject getDetailsFromTaskTable(int jobID, IReport report) {
        String query = "select * from task where job_id = " + jobID + ";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        report.log("Fetching task details \n Executing Query: " + query, true);
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the table task for query " + query);
        }
        report.log("Task Details returned is: " + jsonObject.toString(), true);
        return jsonObject;
    }

    /**
     * Gives the entire row details from stacking job table for given jobID
     *
     * @param jobID
     * @param report
     * @return
     */
    public static JSONObject getDetailsFromStackingJobTable(int jobID, IReport report) {
        String query = "select * from stacking_job where job_id = " + jobID + ";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        report.log("Fetching Stacking job details \n Executing Query: " + query, true);
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the table Stacking job for query " + query);
        }
        report.log("Stacking Job Details returned is: " + jsonObject.toString(), true);
        return jsonObject;
    }

    /**
     * Gives the entire row details from Stacking task table for given jobID
     *
     * @param jobID
     * @param report
     * @return
     */
    public static JSONObject getDetailsFromStackingTaskTable(int jobID, IReport report) {
        String query = "select * from stacking_task where stacking_job_id=(select id from stacking_job where job_id=" + jobID + ");";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        report.log("Fetching Stacking task details \n Executing Query: " + query, true);
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the table Stacking task for query " + query);
        }
        report.log("Stacking Task Details returned is: " + jsonObject.toString(), true);
        return jsonObject;
    }


    /**
     * Fetches the planogramID for given jobID
     *
     * @param jobID
     * @param report
     */
    public static int getPlanogramIDForGivenJobId(int jobID, IReport report) {
        report.log("Fetching planogramID for given JOBID: " + jobID, true);
        String query = "select planogram_id from job where id=" + jobID + ";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        report.log("Fetching PlanogramID \n Executing Query: " + query, true);
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the job table for query " + query);
        }
        int planogramID = jsonObject.getJSONArray("rows").getJSONObject(0).getInt("planogram_id");
        report.log("PlanogramID fetched is : " + planogramID, true);
        return planogramID;
    }

    /**
     * Gives planogram bin key combination
     *
     * @param jobID
     * @param report
     * @return
     */
    public static String[] getplanogramBinKeyCombinationsForGivenJobID(int jobID, IReport report) {
        report.log("Getting the Planogram Bin Combination For Given JobID " + jobID, true);
        int planogramID = getPlanogramIDForGivenJobId(jobID, report);
        report.log("Fetching the number of tasks present for given JobID", true);
        JSONObject jsonObject = new JSONObject(getDetailsFromTaskTable(jobID, report));
        int numTask = jsonObject.getInt("numRows");
        String[] planogramBinKey = new String[numTask];
        for (int i = 0; i < numTask; i++) {
            planogramBinKey[i] = planogramID + "_" + jsonObject.getJSONArray("rows").getJSONObject(i).getInt("bin_id");
        }
        report.log("Planogram Key Combination: " + planogramBinKey, true);
        return planogramBinKey;
    }

    /**
     * check whether planogram upload is successful
     *
     * @param whCode
     * @param report
     * @return boolean
     */
    public static boolean checkSkuBinMappingUploadSuccessful(String whCode, int sku[], IReport report) {
        try {
            System.out.println("Waiting for 2sec before querying the DB");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int fc_planogram_id = 0;
        String query = "select id from fc_planogram where wh_code = \"" + whCode + "\";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 1) {
            fc_planogram_id = jsonObject.getJSONArray("rows").getJSONObject(0).getInt("id");
            report.log("planogram id = " + fc_planogram_id, true);
        } else {
            System.out.println("no record found for given warehouse code" + whCode);
            report.log("no record found for given warehouse code" +
                    "\n No data returned for query" + query, true);
        }

        query = "select * from static_sku_bin_mapping where planogram_id = " + fc_planogram_id + " and sku_id in (" + sku[0] + "," + sku[1] + ");";

        String responseStr = DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query);
        jsonObject = new JSONObject(responseStr);
        if (jsonObject.getInt("numRows") != 0) {
            report.log("sku bin mapping  is uploaded successfully", true);
            return true;
        } else {
            report.log("sku bin mapping is not uploaded\n " +
                    "No data returned for query: " + query, true);
            return false;
        }
    }

    public static boolean checkLocalReplenishmentUploadSuccessful(String whCode, int sku[], float x, float y, float z, IReport report) {
        try {
            System.out.println("Waiting for 2sec before querying the DB");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int fc_planogram_id = 0;
        String query = "select id from fc_planogram where wh_code = \"" + whCode + "\";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 1) {
            fc_planogram_id = jsonObject.getJSONArray("rows").getJSONObject(0).getInt("id");
            report.log("planogram id = " + fc_planogram_id, true);
        } else {
            System.out.println("no record found for given warehouse code" + whCode);
            report.log("no record found for given warehouse code" +
                    "\n No data returned for query: " + query, true);
        }

        query = "select * from fc_replenishment where planogram_id = " + fc_planogram_id + " and sku_id in (" + sku[0] + ") and maximum_quantity_X = " + x + " and threshold_with_default_priority_Y = " + y + " and threshold_with_highest_priority_Z = " + z + ";";

        String responseStr = DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query);
        jsonObject = new JSONObject(responseStr);
        if (jsonObject.getInt("numRows") != 0) {
            report.log("local replenishment is uploaded successfully", true);
            return true;
        } else {
            report.log("local replishment is not uploaded", true);
            return false;
        }

    }

    /**
     * Gives job_reco_expiry_min for given job type
     *
     * @param jobType
     * @param report
     * @return
     */
    public static int getJobRecoExpiryInMinForGivenJobType(DynamicLocationJobType jobType, IReport report) {
        report.log("Getting the job reco expiry in min for job type " + jobType, true);
        String query = "select job_reco_expiry_min from job_type where job_type_name=\"" + jobType.getValue() + "\";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the job_type table for query " + query);
        }
        int jobRecoExpiryMin = jsonObject.getJSONArray("rows").getJSONObject(0).getInt("job_reco_expiry_min");
        report.log("Job reco expiry min for grn-stacking job type is  " + jobRecoExpiryMin, true);
        return jobRecoExpiryMin;
    }

    /**
     * Fetches the volume of the given skuID
     *
     * @param skuID
     * @param report
     * @return
     */
    public static int getVolumeForGivenSKU(int skuID, IReport report) {
        report.log("Fetching the volume of the skuID: " + skuID, true);
        String query = "select length,depth,height,stackable from wh_productdimension where product_description_id=" + skuID + ";";
        JSONObject jsonObject = new JSONObject(AutomationUtilities.executeDatabaseQuery(serverName, query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the wh_productdimension table for query \n Couldn't fetch the dimension of the sku " + query);
        }
        int length = (int) jsonObject.getJSONArray("rows").getJSONObject(0).getDouble("length");
        int depth = (int) jsonObject.getJSONArray("rows").getJSONObject(0).getDouble("depth");
        int height = (int) jsonObject.getJSONArray("rows").getJSONObject(0).getDouble("height");
        int volume = length * depth * height;
        report.log("Volume of the skuid " + skuID + " is : " + volume, true);
        return volume;
    }

    /**
     * Updates the mbq for given skuid,dcID with the mentioned value
     *
     * @param mbq
     * @param skuId
     * @param dcId
     * @param report
     */
    public static void updateMBQAndMBQDate(int mbq, int skuId, int dcId, IReport report) {
        Date date = new Date();
        String modifiedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

        String query = "update wh_productmbq set mbq = " + mbq + ",mbq_date=\"" + modifiedDate + "\" where dc_id=" + dcId + " and product_id in " +
                "(select id from product_product where product_description_id = " + skuId + " and city_id = " +
                "(select city_id from warehouse_dc where id =" + dcId + ")) and" +
                " line_of_business_id=(select id from wh_lineofbusiness where name = \"bigbasket\") order by mbq_date DESC limit 1;";
        AutomationUtilities.updateDatabaseQuery(serverName, query);
        report.log("Updated MBQ and MBQ Date for lineofbusiness bigbasket via query " + query, true);

        query = "update wh_productmbq set mbq = " + mbq + ",mbq_date=\"" + modifiedDate + "\" where dc_id=" + dcId + " and product_id in " +
                "(select id from product_product where product_description_id = " + skuId + " and city_id = " +
                "(select city_id from warehouse_dc where id =" + dcId + ")) and" +
                " line_of_business_id=(select id from wh_lineofbusiness where name = \"bb-instant\") order by mbq_date DESC limit 1;";
        AutomationUtilities.updateDatabaseQuery(serverName, query);
        report.log("Updated MBQ and MBQ Date for lineofbusiness bb-instant via query " + query, true);

    }


    /**
     * Fetching the dcID for given warehouseID
     *
     * @param warehouseID
     * @return
     */
    public static int getDCIDForGivenWareHouseID(String warehouseID, IReport report) {
        report.log("Fetching DCID for given warehouseID " + warehouseID, true);
        String query = "select dc_id from fc_planogram where wh_code=\"" + warehouseID + "\";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the table fc_planogram for query " + query);
        }
        int dcID = jsonObject.getJSONArray("rows").getJSONObject(0).getInt("dc_id");
        report.log("DCID fetched for warehouseID is: " + warehouseID + " is : " + dcID, true);
        return dcID;
    }


    /**
     * Gets the MBQ for given skuID and DCID
     *
     * @param skuId
     * @param dcId
     * @param report
     */
    public static int getMBQForGivenSKUIDDCID(int skuId, int dcId, IReport report) {
        Date date = new Date();
        String modifiedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        int mbqCount = 0;

        String query1 = "select mbq from wh_productmbq where product_id in \n" +
                "(select id from product_product where product_description_id = " + skuId + " and city_id = \n" +
                "(select city_id from warehouse_dc where id =" + dcId + ")) and dc_id = " + dcId + " and line_of_business_id =" +
                "(select id from wh_lineofbusiness where name = \"bigbasket\") order by mbq_date DESC limit 1;";
        JSONObject jsonObject = new JSONObject(AutomationUtilities.executeDatabaseQuery(serverName, query1));
        if (jsonObject.getInt("numRows") == 0) {
            report.log("No data returned from  query " + query1, true);
        } else {
            mbqCount = (int) jsonObject.getJSONArray("rows").getJSONObject(0).getDouble("mbq");
        }

        String query2 = "select mbq from wh_productmbq where product_id in \n" +
                "(select id from product_product where product_description_id = " + skuId + " and city_id = \n" +
                "(select city_id from warehouse_dc where id =" + dcId + ")) and dc_id = " + dcId + " and line_of_business_id =" +
                "(select id from wh_lineofbusiness where name = \"bb-instant\") order by mbq_date DESC limit 1;";
        jsonObject = new JSONObject(AutomationUtilities.executeDatabaseQuery(serverName, query2));
        if (jsonObject.getInt("numRows") == 0) {
            report.log("No data returned from  query " + query2, true);
        } else {
            mbqCount += (int) jsonObject.getJSONArray("rows").getJSONObject(0).getDouble("mbq");
        }

        report.log("Total Mbq for given skuID " + skuId + " for dcID " + dcId + " is " + mbqCount, true);
        return mbqCount;
    }

    /**
     * Checks whether the local replenishement values present for given skuID and planogramID
     *
     * @param planogramID
     * @param skuId
     * @param report
     * @return
     */
    public static boolean checkWhetherLocalReplenishmentPresent(int planogramID, int skuId, IReport report) {
        report.log("Checking whether the fc replenishment value present for the given skuID:  " + skuId + " and planogramID: " + planogramID, true);
        String query = "select * from fc_replenishment where planogram_id= " + planogramID + " and sku_id= " + skuId + " and is_active = 1;";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            report.log("FcReplenishment not present for skuID: " + skuId + " and planogramID: " + planogramID, true);
            return false;
        }
        report.log("FcReplenishment is present for skuID: " + skuId + " and planogramID: " + planogramID, true);
        int x = (int) jsonObject.getJSONArray("rows").getJSONObject(0).getDouble("maximum_quantity_X");
        int y = (int) jsonObject.getJSONArray("rows").getJSONObject(0).getDouble("threshold_with_default_priority_Y");
        int z = (int) jsonObject.getJSONArray("rows").getJSONObject(0).getDouble("threshold_with_highest_priority_Z");
        report.log("FcReplenishment values are : X% : " + x + " Y% : " + y + " Z% : " + z, true);
        return true;
    }


    /**
     * Checks and deletes the local replenishment for the given skuID and planogramID
     *
     * @param planogramID
     * @param skuID
     * @param report
     */
    public static void checkAndDeleteLocalReplenishmentIfExists(int planogramID, int skuID, IReport report) {
        boolean flag = DynamicLocationGeneralMethods.checkWhetherLocalReplenishmentPresent(planogramID, skuID, report);
        if (flag) {
            report.log("Local Replenishment exists for the skuID: " + skuID + " and planogramID: " + planogramID, true);
            deleteLocalReplenishement(planogramID, skuID, report);
        } else {
            report.log("Local Replenishment does not exists for the skuID: " + skuID + " and planogramID: " + planogramID, true);
        }
    }

    /**
     * Deletes the local replenishment for the given skuID and planogramID
     *
     * @param planogramID
     * @param skuID
     * @param report
     */
    public static void deleteLocalReplenishement(int planogramID, int skuID, IReport report) {
        String query = "delete from local_replenishment where planogram_id=22 and sku_id=600;";
        report.log("Deleting the local replenishment for the given planogramID " + planogramID + " and skuID " + skuID, true);
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        boolean flag = checkWhetherLocalReplenishmentPresent(planogramID, skuID, report);
        if (flag) {
            report.log("Local replenishment deleted for sku " + skuID + " planogramID " + planogramID, true);
        } else {
            Assert.fail("Local replenishment not deleted for sku " + skuID + " planogramID " + planogramID);
        }
    }


    /**
     * Updates the global replenishment values with given parameters
     *
     * @param x
     * @param y
     * @param z
     * @param report
     */
    public static void updateGlobalReplenishmentValues(int x, int y, int z, IReport report) {
        String query = "update global_replenishment set threshold_with_default_priority_Y=" + y + "," +
                "maximum_quantity_X=" + x + ",threshold_with_highest_priority_Z=" + z + " where is_active=1;";
        report.log("Updating the global replenishment values X: " + x + " Y: " + y + " Z: " + z + "\n Query: " + query, true);
        DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query);
        report.log("Global Replenishmesnt Values are updated", true);
        getGlobalReplenishMentValues(report);
    }

    /**
     * Fetchs the active global replenishment values
     *
     * @param report
     * @return
     */
    public static int[] getGlobalReplenishMentValues(IReport report) {
        String query = "select maximum_quantity_X,threshold_with_default_priority_Y,threshold_with_highest_priority_Z from global_replenishment where is_active=1;";
        report.log("Fetching global replenishment values \n Query: " + query, true);
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            report.log("No entries returned from global_replenishment table \n Query: " + query, true);
        }
        int[] replenisment = new int[3];
        replenisment[0] = (int) jsonObject.getJSONArray("rows").getJSONObject(0).getDouble("maximum_quantity_x");
        replenisment[1] = (int) jsonObject.getJSONArray("rows").getJSONObject(0).getDouble("threshold_with_default_priority_y");
        replenisment[2] = (int) jsonObject.getJSONArray("rows").getJSONObject(0).getDouble("threshold_with_highest_priority_z");
        report.log("Global Replenishment values are : X% : " + replenisment[0] + " Y% : " + replenisment[1] + " Z% : " + replenisment[2], true);
        return replenisment;
    }


    /**
     * Fetches the latest active bin for given planogram and locationID
     *
     * @param planogramID
     * @param location
     * @param report
     * @return
     */
    public static int getLatestActiveBinForGivenPlanogramAndLocation(int planogramID, int location, IReport report) {
        if (!(location != 1 || location != 2)) {
            Assert.fail("Wrong locatioID sent: Allowed locationID 1(primary) or 2(secondary)");
        }
        String query = "select id from fc_rack_shelf_bin where planogram_id=" + planogramID + " and is_active=1 and location_id=" + location + " order by id DESC limit 1";
        report.log("Fetching the Latest Active bin ID for location: " + location + " Query: " + query, true);
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No entries returned from fc_rack_shelf_bin table \n Query: " + query);
        }
        int binID = jsonObject.getJSONArray("rows").getJSONObject(0).getInt("id");
        report.log("Bin ID is: " + binID, true);
        return binID;
    }

    /**
     * Fetching the latest batchID for given planogram and sku
     *
     * @param planogramID
     * @param skuID
     * @param report
     * @return
     */
    public static int getLatestBatchIDForGivenPlanogramAndSKU(int planogramID, int skuID, IReport report) {
        String query = "select * from stacking_batch_info where sku_id=" + skuID + " and planogram_id=" + planogramID + " order by id DESC limit 1;";
        report.log("Getting the latest batchID for given planogramID and SKUID \n Query Executed: " + query, true);
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {

            report.log("Calling TransferIn API for skuID: " + skuID + " planogram: " + planogramID, true);
            TransferInGeneralMethods transferInGeneralMethods = new TransferInGeneralMethods();
            TransferInAPI transferInAPI = new TransferInAPI(report);
            String warehouseID = getWareHouseIDForGivenPlanogramID(planogramID);
            List<HashMap<String, Object>> looseQtySKUList = new LinkedList<>();
            looseQtySKUList.add(TransferInGeneralMethods.settinglooseSKUInfo(skuID, looseQty, mfgDate, expDate,
                    isBundlepack, cp, mrp, eretailmrp, gst, cess));

            List<HashMap<String, Object>> containerSKUList = new LinkedList<>();
            containerSKUList.add(TransferInGeneralMethods.settingcontainerSKUInfo(containerID, containerTag, containerType, unknownDimension
                    , skuID, containerQty, mfgDate, expDate,
                    isBundlepack, cp, mrp, eretailmrp, gst, cess));

            transferInAPI.getTransferInAPIResponse(warehouseID, TransferInType.grn, rtvId, looseQtySKUList, containerSKUList, transferInGeneralMethods, false);


            query = "select * from stacking_batch_info where sku_id=" + skuID + " and planogram_id=" + planogramID + " order by id DESC limit 1;";
            report.log("Getting the latest batchID for given planogramID and SKUID \n Query Executed: " + query, true);
            jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
            if (jsonObject.getInt("numRows") == 0) {
                Assert.fail("No entries returned from stacking_batch_info table \n Query: " + query);
            }
        }
        int batchID = jsonObject.getJSONArray("rows").getJSONObject(0).getInt("batch_id");
        report.log("BatchID is: " + batchID, true);
        return batchID;
    }


    /**
     * Updating the max sku allowed for given parameter
     *
     * @param maxSkuAllowed
     * @param binID
     * @param report
     */
    public static void updateMaxSKUAllowed(int maxSkuAllowed, int binID, IReport report) {
        String query = "update fc_rack_shelf_bin set max_sku_allowed=" + maxSkuAllowed + " where id = " + binID + ";";
        report.log("Updating max sku allowed " + maxSkuAllowed + " for binID: " + binID + " \n Query: " + query, true);
        DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query);
        report.log("Max allowed sku updated ", true);
    }

    /**
     * Checks and returns true or false, whether the given binlocs are sorted
     *
     * @param binloc1
     * @param binloc2
     * @param report
     * @return
     */
    public static boolean isBinSequncingSorted(String binloc1, String binloc2, IReport report) {
        report.log("checking whether the binloc1 " + binloc1 + " and binloc2  " + binloc2 + " is the proper order", true);
        /*
        B-10-A-3
        B-10-B-3
         */
        String[] binloc1Arr = binloc1.split("-");
        String[] binloc2Arr = binloc2.split("-");
        for (int i = 0; i < binloc1Arr.length; i++) {
            if ((binloc1Arr[i].charAt(0) - 0) > (binloc2Arr[i].charAt(0) - 0)) {
                report.log("Bin sequencing is not sorted", true);
                return false;
            }
        }
        report.log("Bin sequencing is sorted", true);
        return true;
    }

    /**
     * Updates the max alternate bins as per given parameter
     *
     * @param maxAlternateBin
     * @param report
     */
    public static void updateMaxAlternateBins(int maxAlternateBin, IReport report) {
        String query = "update planogram_config set config_value=" + maxAlternateBin + " where config_key=\"MAX_ALTERNATE_BINS\";";
        report.log("Updating the max alternate bin \n Query " + query, true);
        DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query);
        report.log("Max Alternate Bin updated ", true);
    }

    /**
     * Fetchs the job status for the given jobID
     * <p>
     * '1', 'open',
     * '2', 'assigned'
     * '3', 'reassigned'
     * '4', 'completed'
     * '5', 'abandoned'
     *
     * @param jobID
     * @param report
     * @return
     */
    public static int getJobStatusForGivenJobId(int jobID, IReport report) {
        String query = "select job_status_id from job where id=" + jobID + ";";
        report.log("Fetching the job status for given jobID: " + jobID + " Query: " + query, true);
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No entries returned from job table \n Query: " + query);
        }
        int jobStatusID = jsonObject.getJSONArray("rows").getJSONObject(0).getInt("job_status_id");
        report.log("Job Status ID: " + jobStatusID, true);
        return jobStatusID;
    }

    /**
     * Fetchs the job status for the given jobID
     *
     * @param jobID
     * @param report
     * @return
     */
    public static String getJobEndedAtValueForGivenJobId(int jobID, IReport report) {
        String query = "select ended_at from job where id=" + jobID + ";";
        report.log("Fetching the job ended time for given jobID: " + jobID + " Query: " + query, true);
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No entries returned from job table \n Query: " + query);
        }
        String jobEndedTime = jsonObject.getJSONArray("rows").getJSONObject(0).getString("ended_at");
        report.log("Job Ended time : " + jobEndedTime, true);
        return jobEndedTime;
    }

    /**
     * Gives the entire row details from task table for given jobID and taskID
     *
     * @param jobID
     * @param taskID
     * @param report
     * @return
     */
    public static JSONObject getDetailsFromTaskTable(int jobID, int taskID, IReport report) {
        String query = "select * from task where job_id=" + jobID + " and id = " + taskID + " ;";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        report.log("Fetching task details \n Executing Query: " + query, true);
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the table task for query " + query);
        }
        report.log("Task Details returned is: " + jsonObject.toString(), true);
        return jsonObject;
    }

    /**
     * Fetchs Stacked qty for given skuID and transferInID
     *
     * @param skuID
     * @param transferInID
     * @return
     */
    public static int getStackedQtyFromtransferInSkuInfoTable(int skuID, int transferInID, IReport report) {
        String query = "select stacked_quantity from transferin_sku_info where sku_id = " + skuID + " and transferin_id = " + transferInID + " ;";
        report.log("Fetching the stacked qty from transferInSKUInfo table \n Query: " + query, true);
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the table transferin_sku_info for query " + query);
        }
        int stackedQTy = (int) jsonObject.getJSONArray("rows").getJSONObject(0).getDouble("stacked_quantity");
        report.log("Stacked Qty for skuID " + skuID + " and transferInID is " + transferInID, true);
        return stackedQTy;
    }

    /**
     * Fetching the stacking batch info if for given job id
     *
     * @param jobID
     * @param report
     * @return
     */
    public static int getStackingBatchInfoIDForGivenJobID(int jobID, IReport report) {
        String query = "select stacking_batch_info_id from stacking_job where job_id=" + jobID + ";";
        report.log("Fetching the stacking job id for the given jobID: " + jobID, true);
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the Stacking_job table for query " + query);
        }
        int stackingBatchInfoID = (int) jsonObject.getJSONArray("rows").getJSONObject(0).getDouble("stacking_batch_info_id");
        report.log("Stacking BatchInfo ID is " + stackingBatchInfoID, true);
        return stackingBatchInfoID;
    }

    /**
     * Fetching the transferInID for given jobID
     *
     * @param jobID
     * @param report
     * @return
     */
    public static int fetchTransferinIDForGivenJobID(int jobID, IReport report) {
        String query = "select transferin_id from stacking_batch_info where id = (select stacking_batch_info_id from stacking_job where job_id=" + jobID + ");";
        report.log("Fetching the transferInID for the given jobID: " + jobID, true);
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the for query " + query);
        }
        int transferinID = (int) jsonObject.getJSONArray("rows").getJSONObject(0).getDouble("transferin_id");
        report.log("Transferin ID for given jobID " + jobID + " is " + transferinID, true);
        return transferinID;
    }

    /**
     * Fetching the tiReservedQty from stacking job table for given jobID
     *
     * @param jobID
     * @param report
     * @return
     */
    public static int fetchTiReservedQtyFromStackingJobTable(int jobID, IReport report) {
        String query = "select ti_reserved_quantity from stacking_job where job_id=" + jobID + ";";
        report.log("Fetching the ti_reserved_quantity for the given jobID: " + jobID + " \nQuery " + query, true);
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the Stacking_job table for query " + query);
        }
        int tiReservedQty = (int) jsonObject.getJSONArray("rows").getJSONObject(0).getDouble("ti_reserved_quantity");
        report.log("ti reserved qty is " + tiReservedQty, true);
        return tiReservedQty;
    }

    /**
     * Fetching the quantity from fc_sku_stock table for given parameters
     *
     * @param planogramID
     * @param skuID
     * @param binID
     * @param report
     * @return
     */
    public static int fetchQuantityFromFcSKUStockTable(int planogramID, int skuID, int binID, IReport report) {
        String query = "select quantity from fc_sku_stock where planogram_id=" + planogramID + " and sku_id=" + skuID + " and bin_id=" + binID + ";";
        report.log("Fetching the Quantity from fc_sku_stock table \n Query" + query, true);
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            report.log("No data returned from the fc_sku_stock table for query " + query, true);
            return 0;
        }
        int quantity = (int) jsonObject.getJSONArray("rows").getJSONObject(0).getDouble("quantity");
        report.log("quantity is " + quantity, true);
        return quantity;
    }

    /**
     * Gives the latest entire row details from task table for given jobID
     *
     * @param jobID
     * @param report
     * @return
     */
    public static JSONObject getLatestDetailsFromTaskTable(int jobID, IReport report) {
        String query = "select * from task where job_id=" + jobID + " order by id DESC limit 1 ;";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        report.log("Fetching task details \n Executing Query: " + query, true);
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the table task for query " + query);
        }
        report.log("Task Details returned is: " + jsonObject.toString(), true);
        return jsonObject;
    }

    /**
     * Gives the entire row details from Stacking task table for given taskID
     *
     * @param taskID
     * @param report
     * @return
     */
    public static JSONObject getDetailsFromStackingTaskTableForGivenTaskID(int taskID, IReport report) {
        String query = "select * from stacking_task where task_id = " + taskID + ";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        report.log("Fetching Stacking task details \n Executing Query: " + query, true);
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the table Stacking task for query " + query);
        }
        report.log("Stacking Task Details returned is: " + jsonObject.toString(), true);
        return jsonObject;
    }


    /**
     * Fetching the quantity from fc_sku_stock table for misc location
     *
     * @param planogramID
     * @param skuID
     * @param report
     * @return
     */
    public static int fetchQuantityFromFcSKUStockTableForMiscLocation(int planogramID, int skuID, IReport report) {
        String query = "select * from fc_sku_stock where  sku_id=" + skuID + " and planogram_id=" + planogramID + " and location_id=3;";
        report.log("Fetching the Quantity from fc_sku_stock table \n Query" + query, true);
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            report.log("No data returned from the fc_sku_stock table for query " + query, true);
            report.log("Zero quantities present in misc location ", true);
            return 0;
        }
        int quantity = (int) jsonObject.getJSONArray("rows").getJSONObject(0).getDouble("quantity");
        report.log("quantity is " + quantity, true);
        return quantity;
    }

    /**
     * Gives the entire row details from task table for given jobID and skuID for misc stacking
     *
     * @param jobID
     * @param skuID
     * @param report
     * @return
     */
    public static JSONObject getDetailsFromTaskTableForMiscStacking(int jobID, int skuID, IReport report) {
        String query = "select * from task where bin_id=0 and location_id=3 and sku_id=" + skuID + " and job_id=" + jobID + ";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        report.log("Fetching task details \n Executing Query: " + query, true);
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the table task for query " + query);
        }
        report.log("Task Details returned is: " + jsonObject.toString(), true);
        return jsonObject;
    }

    /**
     * Fetching the batchID from sku_batch table for given skuID
     *
     * @param skuID
     * @param report
     * @return
     */
    public static int getBatchIDFromSkuBatchTable(int skuID, IReport report) {
        String query = "select id from sku_batch where sku_id = " + skuID + " ;";
        report.log("Fetching the batch id for the sku " + skuID + " \n Query" + query, true);
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the sku_batch table for query " + query);
        }
        int batchID = jsonObject.getJSONArray("rows").getJSONObject(0).getInt("id");
        report.log("Batch ID is " + batchID, true);
        return batchID;
    }

    /**
     * Fetching the unstacked_quantity for given skuID and planogramID from stacking_batch_info table
     * If any job is active then unstacked qty fetched from this table will be less by x qty as compared to that of stock info api value
     *
     * @param skuID
     * @param planogramID
     * @param report
     * @return
     */
    public static int getUnstackedQtyForGivenSkuIDPlanogramIDFromStackingBatchInfoTable(int skuID, int planogramID, TransferInType transferInType, IReport report) {
        String query = "select sum(unstacked_quantity) as unstacked_qty from stacking_batch_info where sku_id=" + skuID + " and planogram_id=" + planogramID + " " +
                "and transferin_type_id=" + DynamicLocationGeneralMethods.getTransferinIDForGivenTransferinType(transferInType, report) + ";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        report.log("Fetching Unstacked Qty \n Executing Query: " + query, true);
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the table stacking_batch_info for query " + query);
        }
        double unstackedQty = 0;
        unstackedQty = jsonObject.getJSONArray("rows").getJSONObject(0).getDouble("unstacked_qty");
        report.log("UnstackedQty for the given skuID: " + skuID + " warehouseID is: "
                + DynamicLocationGeneralMethods.getWareHouseIDForGivenPlanogramID(planogramID), true);

        return (int) unstackedQty;
    }

    /**
     * Fetchs the transferin ID for the given transferin type
     *
     * @param transferInType
     * @param report
     * @return
     */
    public static int getTransferinIDForGivenTransferinType(TransferInType transferInType, IReport report) {
        String query = "select id from transferin_type where transferin_type_name=\"" + transferInType.getValue() + "\";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        report.log("Fetching the transferin ID for given transferin type " + transferInType.getValue() + "\n Query: " + query, true);
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the table transferin_type for query " + query);
        }
        int transferinID = jsonObject.getJSONArray("rows").getJSONObject(0).getInt("id");
        report.log("Transferin ID for the transferin type: " + transferInType + " is " + transferinID, true);
        return transferinID;
    }

    /**
     * Check whether the entry present in the stackingBatchInfo table for given params
     * If any job is active then unstacked qty fetched from this table will be less by x qty as compared to that of stock info api value
     *
     * @param skuID
     * @param planogramID
     * @param report
     * @return
     */
    public static boolean isEntryPresentInStackingBatchInfo(int skuID, int planogramID, TransferInType transferInType, IReport report) {
        String query = "select sum(unstacked_quantity) as unstacked_qty from stacking_batch_info where sku_id=" + skuID + " and planogram_id=" + planogramID + " " +
                "and transferin_type_id=" + DynamicLocationGeneralMethods.getTransferinIDForGivenTransferinType(transferInType, report) + ";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        report.log("Checking whether the entry present in stacking batch info table for given skuID: " + skuID + "" +
                "and planogramID " + planogramID + " \n Executing Query: " + query, true);
        if ((jsonObject.getInt("numRows") == 0)) {
            report.log("No entry present in stacking batch info table", true);
            return false;
        } else if (jsonObject.getJSONArray("rows").getJSONObject(0).get("unstacked_qty").toString().equalsIgnoreCase("null")) {
            return false;
        }
        return true;

    }

    /**
     * checks and returns true if the given bin is primary otherwise false
     *
     * @param binID
     * @param report
     * @return
     */
    public static boolean isGivenBinPrimary(int binID, IReport report) {
        String query = "select location_id from fc_rack_shelf_bin where id = " + binID + ";";
        report.log("Checking whether the binID: " + binID + " is primary \n Query: " + query, true);
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the table fc_rack_shelf_bin for query " + query);
        }
        int locationID = jsonObject.getJSONArray("rows").getJSONObject(0).getInt("location_id");
        report.log("LocationID for binID " + binID + " is " + locationID, true);
        if (locationID == 1) {
            report.log("BinID: " + binID + " is primary location", true);
            return true;
        } else {
            report.log("BinID: " + binID + " is not primary location", true);
            return false;
        }
    }

    /**
     * checks and returns true if the given bin is secondary otherwise false
     *
     * @param binID
     * @param report
     * @return
     */
    public static boolean isGivenBinSecondary(int binID, IReport report) {
        String query = "select location_id from fc_rack_shelf_bin where id = " + binID + ";";
        report.log("Checking whether the binID: " + binID + " is primary \n Query: " + query, true);
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the table fc_rack_shelf_bin for query " + query);
        }
        int locationID = jsonObject.getJSONArray("rows").getJSONObject(0).getInt("location_id");
        report.log("LocationID for binID " + binID + " is " + locationID, true);
        if (locationID == 2) {
            report.log("BinID: " + binID + " is secondary location", true);
            return true;
        } else {
            report.log("BinID: " + binID + " is not secondary location", true);
            return false;
        }
    }

    /**
     * Fetchs the writeoff jobID
     *
     * @param warehouseID
     * @param timestampBeforeApiCall
     * @param report
     * @return
     */
    public static int getWriteOffJobID(String warehouseID, String timestampBeforeApiCall, IReport report) {
        int planogramID = DynamicLocationGeneralMethods.getPlanogramIDForGivenWareHouseID(warehouseID);
        String query = "select * from job where job_type_id= 8 and job_status_id=4 and planogram_id=" + planogramID + " and " +
                "ended_at>='" + timestampBeforeApiCall + "' order by id DESC limit 1;";
        report.log("Getting the writeoff jobiD \n Query: " + query, true);
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the table job, Writeoff job not created " + query);
        }
        int jobID = jsonObject.getJSONArray("rows").getJSONObject(0).getInt("id");
        report.log("Write Off Job ID is " + jobID, true);
        return jobID;
    }

    /**
     * Fetches the writeoff job details
     *
     * @param jobID
     * @param report
     * @return
     */
    public static JSONObject getWriteOffJobDetails(int jobID, IReport report) {
        String query = "select * from write_off_job where job_id=" + jobID + ";";
        report.log("Getting the writeoff job details \n Query: " + query, true);
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the writeoff job table \n " + query);
        }
        report.log("Write off job details " + jsonObject.toString(), true);
        return jsonObject;
    }

    /**
     * Fetches the batch details
     *
     * @param batchID
     * @param report
     * @return
     */
    public static JSONObject getBatchDetailsForGivenBatchID(int batchID, IReport report) {
        String query = "select * from batch_details where batch_id=" + batchID + ";";
        report.log("Getting the batch details \n Query: " + query, true);
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the batch details table \n " + query);
        }
        report.log("Batch  details " + jsonObject.toString(), true);
        return jsonObject;
    }

    /**
     * Fetches the binID from sku bin mapping table
     *
     * @param skuID
     * @param binLoc
     * @param report
     * @return
     */
    public static int getBinIDfromskuBinMappingTable(int skuID, String binLoc, IReport report) {
        String query = "select bin_id from static_sku_bin_mapping where sku_id=" + skuID + " and bin_loc=\"" + binLoc + "\";";
        report.log("Getting the binID \n Query: " + query, true);
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the sku bin mapping table \n " + query);
        }
        int binID = jsonObject.getJSONArray("rows").getJSONObject(0).getInt("bin_id");
        report.log("Bin ID" + binID, true);
        return binID;
    }


    /**
     * Fetches the record from StackingRecobin set for given primary key
     *
     * @param primaryKey
     * @param report
     * @return
     */
    public static JSONObject getRecordFromStackingRecobin(String primaryKey, IReport report) {
        //select * from qas_bigbasket.stackingrecobin where pk='21_1338';
        String query = "select * from " + AutomationUtilities.getAerospikeNameSpace() + ".stackingrecobin where pk=" + primaryKey;
        report.log("Fetching the record from stacking recobin \n Query " + query, true);
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.getRecordFromAerospikeByPk("stackingrecobin", primaryKey));
        if (jsonObject.toString().length() == 0) {
            Assert.fail("No data returned from the set stackingrecobin for primary key " + primaryKey + "\n Query: " + query);
        }
        report.log("Record fetched is  " + jsonObject, true);
        return jsonObject;
    }

    /**
     * Fetches the record from pickingrecobin set for given primary key
     *
     * @param primaryKey
     * @param report
     * @return
     */
    public static JSONObject getRecordFromPickingRecobin(String primaryKey, IReport report) {
        //select * from qas_bigbasket.pickingrecobin where pk='21_1_478873873';
        String query = "select * from " + AutomationUtilities.getAerospikeNameSpace() + ".pickingrecobin where pk=" + primaryKey;
        report.log("Fetching the record from pickingrecobin \n Query " + query, true);
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.getRecordFromAerospikeByPk("pickingrecobin", primaryKey));
        if (jsonObject.toString().length() == 0) {
            Assert.fail("No data returned from the set pickingrecobin for primary key " + primaryKey + "\n Query: " + query);
        }
        report.log("Record fetched is  " + jsonObject, true);
        return jsonObject;
    }


    /**
     * Fetches the wms qoh for given sku and ri
     *
     * @param skuID
     * @param warehouseID
     * @param report
     * @return
     */
    public static int getWmsQohFromStockReservationTable(int skuID, String warehouseID, IReport report) {
        int dcID = getDCIDForGivenWareHouseID(warehouseID, report);
        int riID = getRIIDForGivenDCID(dcID, report);
        String query = "select stock,wms_qoh,transient_quantity,wms_qoh_last_updated_timestamp,request_type,request_id from warehouse_stockreservation where product_description_id = " + skuID + " and reservation_info_id=" + riID + ";";
        report.log("Executing the query: " + query, true);
        JSONObject jsonObject = new JSONObject(AutomationUtilities.executeDatabaseQuery(serverName, query));
        logger.info("Response :" + jsonObject);
        if (jsonObject.getInt("numRows") == 0) {
            System.out.println(" Number of rows returned is 0\n Expected warehouse_stockreservation info \n Query Executed: " + query);
            Assert.fail("Number of rows returned is 0\n Expected warehouse_stockreservation info\n Query Executed: " + query);
        }
        report.log("Query response: " + jsonObject.getJSONArray("rows").getJSONObject(0), true);
        int wmsQoh = (int) jsonObject.getJSONArray("rows").getJSONObject(0).getDouble("wms_qoh");
        report.log("WMS qoh is : " + wmsQoh, true);
        return wmsQoh;
    }

    /**
     * Fetching the ri for given dcID
     *
     * @param dcID
     * @return
     */
    public static int getRIIDForGivenDCID(int dcID, IReport report) {
        String query = "select reservation_info_id from warehouse_dc where id =" + dcID + ";";
        report.log("Fetching RIID for given DCID: " + dcID + " \n Query: " + query, true);
        JSONObject jsonObject = new JSONObject(AutomationUtilities.executeDatabaseQuery(serverName, query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the table warehouse_dc for query " + query);
        }
        int riID = jsonObject.getJSONArray("rows").getJSONObject(0).getInt("reservation_info_id");
        report.log("RIID fetched for DCID is: " + riID + " is : " + dcID, true);
        return riID;
    }

    /**
     * Returns boolean checking bulk child sku mapping exists
     *
     * @param bulkSkuID
     * @param childSkuID
     * @param report
     * @return
     */
    public static boolean bulkChildMappingExists(int bulkSkuID, int childSkuID, IReport report) {
        String query = "select * from bulk_child_sku_map where bulk_sku_id = " + bulkSkuID + " and child_sku_id = " + childSkuID + ";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        report.log("Checking whether bulk child mapping exists \n Executing Query: " + query, true);
        if (jsonObject.getInt("numRows") == 0) {
            report.log("Bulk child mapping does not exists between sku " + bulkSkuID + " and " + childSkuID, true);
            return false;
        } else {
            report.log("Bulk child mapping exists between sku " + bulkSkuID + " and " + childSkuID, true);
            return true;
        }
    }

    /**
     * Returns boolean checking child sku is not in exclusion list
     *
     * @param planogramID
     * @param childSkuID
     * @param report
     * @return
     */
    public static boolean isChildSkuNotInExclusionList(int planogramID, int childSkuID, IReport report) {
        String query = "select * from fc_bulk_child_sku_map_exclusion where planogram_id=" + planogramID + " and child_sku_id=" + childSkuID + ";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        report.log("Checking whether child sku in excluslion list \n Executing Query: " + query, true);
        if (jsonObject.getInt("numRows") == 0) {
            report.log("Child sku " + childSkuID + " is not in exclusion list", true);
            return true;
        } else {
            report.log("Child sku " + childSkuID + " is in exclusion list", true);
            return false;
        }
    }

    /**
     * Fetches the binID from sku bin mapping table
     *
     * @param skuID
     * @param warehouseID
     * @param report
     * @return
     */
    public static int[] getBinIDfromskuBinMappingForGivenSkuIDAndWarehouseID(int skuID, String warehouseID, IReport report) {
        int planogramID = getPlanogramIDForGivenWareHouseID(warehouseID);
        String query = "select bin_id from static_sku_bin_mapping where planogram_id=" + planogramID + " and sku_id=" + skuID + "; ";
        report.log("Getting the binID \n Query: " + query, true);
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the sku bin mapping table \n " + query);
        }
        int binID[] = new int[jsonObject.getInt("numRows")];
        for (int i = 0; i < binID.length; i++) {
            binID[i] = jsonObject.getJSONArray("rows").getJSONObject(i).getInt("bin_id");
        }
        report.log("All mapped bins for skuID " + skuID + " is: " + Arrays.toString(binID), true);
        return binID;
    }

    /**
     * Updating the max sku allowed for given parameter
     *
     * @param binID
     * @param report
     */
    public static int getMaxSkuAllowedFromRackShelfBin(int binID, IReport report) {
        String query = "select max_sku_allowed from fc_rack_shelf_bin where id = " + binID + ";";
        report.log("Fetching the max sku allowed for the binID: " + binID + " \n Query: " + query, true);
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the table fc_rack_shelf_bin for query " + query);
        }
        int maxSkuAllowed = jsonObject.getJSONArray("rows").getJSONObject(0).getInt("max_sku_allowed");
        report.log("Max sku allowed for the bin  " + binID + " is : " + maxSkuAllowed, true);
        return maxSkuAllowed;
    }


    /**
     * Checks whether the requested qty is present in primary bin
     *
     * @param skuID
     * @param warehouseID
     * @param report
     * @param reqQty
     */
    public static void checkWhetherRequestedQtyPresentInPrimaryBin(int skuID, String warehouseID, IReport report, int reqQty) {
        StockInfo stockInfo = new StockInfo(report);
        Response response = stockInfo.callStockInfoApi(String.valueOf(skuID), warehouseID, false);
        JSONObject jsonObject = new JSONObject(response.asString());
        int totalQohInPrimary = jsonObject.getJSONObject("primary_qoh").getInt("total_qoh");
        report.log("Total Qoh in Primary : " + totalQohInPrimary, true);
        report.log("Total requested qty : " + reqQty, true);
        Assert.assertTrue(reqQty < totalQohInPrimary, "RequestQty " + reqQty + " is more than or equal to total primary qoh: " + totalQohInPrimary);
    }


    /**
     * Gives the entire row details from Picking job table for given jobID
     *
     * @param jobID
     * @param report
     * @return
     */
    public static JSONObject getDetailsFromPickingJobTable(int jobID, IReport report) {
        String query = "select * from picking_job where job_id = " + jobID + ";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        report.log("Fetching picking job details \n Executing Query: " + query, true);
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the table picking job for query " + query);
        }
        report.log("picking Job Details returned is: " + jsonObject.toString(), true);
        return jsonObject;
    }

    /**
     * Gives the entire row details from Picking task table for given jobID
     *
     * @param jobID
     * @param report
     * @return
     */
    public static JSONObject getDetailsFromPickingTaskTable(int jobID, IReport report) {
        String query = "select * from picking_task where picking_job_id in (select id from picking_job where job_id=" + jobID + ");";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        report.log("Fetching Picking task details \n Executing Query: " + query, true);
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the table Picking task for query " + query);
        }
        report.log("Picking Task Details returned is: " + jsonObject.toString(), true);
        return jsonObject;
    }

    public static void updateJobRecoExpiryForGivenJobType(int jobRecoExpiryMin, int jobExpiryMin, DynamicLocationJobType jobType, IReport report) {
        String query = "update job_type set job_reco_expiry_min=" + jobRecoExpiryMin + ",job_expiry_min=" + jobExpiryMin + "  where job_type_name=\"" + jobType.getValue() + "\";";
        report.log("Updating the job type  " + jobType.getValue() + " \n Query: " + query, true);
        DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query);
    }

    /**
     * Fetches the binID from sku bin mapping table
     *
     * @param skuID
     * @param warehouseID
     * @param report
     * @param locationID
     * @return
     */
    public static int[] getBinIDfromskuBinMappingForGivenSkuIDAndWarehouseIDAndLocationID(int skuID, String warehouseID, int locationID, IReport report) {
        int planogramID = getPlanogramIDForGivenWareHouseID(warehouseID);
        String query = "select id from fc_rack_shelf_bin where id in (select bin_id from static_sku_bin_mapping where planogram_id=" + planogramID + " " +
                "and sku_id=" + skuID + ") and location_id=" + locationID + ";";
        report.log("Getting the binID \n Query: " + query, true);
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the sku bin mapping table \n " + query);
        }
        int binID[] = new int[jsonObject.getInt("numRows")];
        for (int i = 0; i < binID.length; i++) {
            binID[i] = jsonObject.getJSONArray("rows").getJSONObject(i).getInt("id");
        }
        report.log("All mapped bins for skuID " + skuID + " is: " + Arrays.toString(binID), true);
        return binID;
    }

    /**
     * Fetches the binlocation for the given binID
     *
     * @param binID
     * @param report
     * @return
     */
    public static String getBinLocationForGivenBinID(int binID, IReport report) {
        String query = "select binloc from fc_rack_shelf_bin where id=" + binID + ";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        report.log("Fetching bin location \n Executing Query: " + query, true);
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the table fc_rack_shelf_bin for query " + query);
        }
        String binLoc = jsonObject.getJSONArray("rows").getJSONObject(0).getString("binloc");
        report.log("BinLocation is " + binLoc, true);
        return binLoc;
    }

    /**
     * Gives the entire row details from Fc Sku Price Details table for given warehouseID and skuID
     *
     * @param skuID
     * @param warehouseID
     * @param report
     * @return
     */
    public static JSONObject getDetailsFromFcSkuPriceDetailsTable(int skuID, String warehouseID, IReport report) {
        int planogramID = getPlanogramIDForGivenWareHouseID(warehouseID);
        String query = "select * from fc_sku_price_details where planogram_id=" + planogramID + " and sku_id=" + skuID + ";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        report.log("Fetching Fc sku price details \n Executing Query: " + query, true);
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the table fc_sku_price_details for query " + query);
        }
        report.log("fc_sku_price_details Details returned is: " + jsonObject.toString(), true);
        return jsonObject;
    }

    /**
     * Gives the entire row details from Fc Sku stock Details table for given warehouseID, skuID and binID
     *
     * @param skuID
     * @param warehouseID
     * @param binID
     * @param report
     * @return
     */
    public static JSONObject getDetailsFromFcSkuStockTable(int skuID, String warehouseID, int binID, IReport report) {
        int planogramID = getPlanogramIDForGivenWareHouseID(warehouseID);
        String query = "select * from fc_sku_stock where sku_id=" + skuID + " and planogram_id=" + planogramID + " and bin_id=" + binID + ";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        report.log("Fetching Fc sku Stock details \n Executing Query: " + query, true);
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the table fc_sku_stock for query " + query);
        }
        report.log("fc_sku_stock Details returned is: " + jsonObject.toString(), true);
        return jsonObject;
    }

    /**
     * Gives the entire row details from crate_stock Details table for given taskID
     *
     * @param taskID
     * @param report
     * @return
     */
    public static JSONObject getDetailsFromCrateStockTable(int taskID, IReport report) {
        String query = "select * from crate_stock where task_id=" + taskID + ";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        report.log("Fetching crate_stock details \n Executing Query: " + query, true);
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the table crate_stock for query " + query);
        }
        report.log("crate_stock Details returned is: " + jsonObject.toString(), true);
        return jsonObject;
    }

    /**
     * Gives the details from underpick_reason Details table
     *
     * @param report
     * @return
     */
    public static JSONObject getDetailsFromUnderPickReasonTable(IReport report) {
        String query = "select * from underpick_reason;";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        report.log("Fetching underpick_reason details \n Executing Query: " + query, true);
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the table underpick_reason for query " + query);
        }
        report.log("underpick_reason Details returned is: " + jsonObject.toString(), true);
        return jsonObject;
    }

    /**
     * Adding stocks to primaryLocation
     *
     * @param warehouseId
     * @param batchID
     * @param stackQtyInSecOrPriorMisc
     * @param skuID
     * @param report
     */
    public static void performStackingToPrimaryLocation(String warehouseId, int batchID, int stackQtyInSecOrPriorMisc, int skuID, IReport report) {
        report.log("Performing Stacking for warehouseID : " + warehouseId + " BatchID: " + batchID + " QtyStacking: " + stackQtyInSecOrPriorMisc, true);
        GrnStackingJob grnStackingJob = new GrnStackingJob(report);
        int planogramID = DynamicLocationGeneralMethods.getPlanogramIDForGivenWareHouseID(warehouseId);
        String createStackingApiResponseStr = grnStackingJob.callGrnStackingJobApi(warehouseId, DynamicLocationGeneralMethods.STACKER_USERID,
                DynamicLocationGeneralMethods.STACKER_USERNAME,
                batchID, stackQtyInSecOrPriorMisc, 0, stackQtyInSecOrPriorMisc
                , false);

        JSONObject jsonObject = new JSONObject(createStackingApiResponseStr);
        report.log("Fetching details from GrnStackingJob API response : " +
                createStackingApiResponseStr, true);
        int jobID = jsonObject.getInt("job_id");
        report.log("JobID created is : " + jobID, true);
        int taskID = jsonObject.getJSONArray("path").getJSONObject(0).getInt("task_id");
        int binID = jsonObject.getJSONArray("path").getJSONObject(0).getInt("bin_id");
        int locationID = jsonObject.getJSONArray("path").getJSONObject(0).getInt("location_id");

        if (locationID == 1) {
            StackingAck stackingAck = new StackingAck(report);
            String timeStamp = "2022-08-18T00:00:00Z";
            stackingAck.callStackingAckApi(jobID, warehouseId, taskID, skuID, batchID,
                    binID, locationID, stackQtyInSecOrPriorMisc, 0, 0, "", timeStamp);

        } else {
            AlternateBin alternateBin = new AlternateBin(report);
            String alternateBinResponseStr = alternateBin.callAlternateBinAPI(jobID, warehouseId, skuID, stackQtyInSecOrPriorMisc);
            jsonObject = new JSONObject(alternateBinResponseStr);
            int numOfBinRecommended = jsonObject.getJSONArray("path").length();
            int alternateBinID = binID;
            report.log("Number of alternate bin recommended: " + numOfBinRecommended, true);
            for (int j = 0; j < numOfBinRecommended; j++) {
                if (jsonObject.getJSONArray("path").getJSONObject(j).getInt("location_id") != 2) {
                    alternateBinID = jsonObject.getJSONArray("path").getJSONObject(j).getInt("bin_id");
                    locationID = jsonObject.getJSONArray("path").getJSONObject(j).getInt("location_id");
                    break;
                }
            }
            if (alternateBinID == binID) {
                report.log("NOT STACKED IN PRIMARY BIN FOR SKUID: " + skuID + " PLANOGRAMID: " + planogramID +
                        " No primary bin satisifying the conditions available", true);
            }
            report.log("Alternate Bin ID is : " + alternateBinID, true);

            StackingAck stackingAck = new StackingAck(report);
            String timeStamp = "2022-08-18T00:00:00Z";
            stackingAck.callStackingAckApi(jobID, warehouseId, 0, skuID, batchID,
                    alternateBinID, locationID, stackQtyInSecOrPriorMisc, 0, 0, "", timeStamp);
        }

        GrnStackingCompleteAPI grnStackingCompleteAPI = new GrnStackingCompleteAPI(report);
        grnStackingCompleteAPI.callCompleteApi(jobID, warehouseId);
    }

    /**
     * Gives the details from understack_reason Details table
     *
     * @param report
     * @return
     */
    public static JSONObject getDetailsFromUnderStackReasonTable(IReport report) {
        String query = "select * from understack_reason where is_active=1;";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        report.log("Fetching understack_reason details \n Executing Query: " + query, true);
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the table understack_reason for query " + query);
        }
        report.log("understack_reason Details returned is: " + jsonObject.toString(), true);
        return jsonObject;
    }

    /**
     * Gives the details from write_off_reasons Details table
     *
     * @param report
     * @return
     */
    public static JSONObject getDetailsFromWriteOffReasonTable(IReport report) {
        String query = "select * from write_off_reasons;";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        report.log("Fetching write_off_reasons details \n Executing Query: " + query, true);
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the table write_off_reasons for query " + query);
        }
        report.log("write_off_reasons Details returned is: " + jsonObject.toString(), true);
        return jsonObject;
    }


    /**
     * Gives the details from planogram_locations Details table
     *
     * @param report
     * @return
     */
    public static JSONObject getDetailsFromPlanogramLocationTable(IReport report) {
        String query = "select * from planogram_locations;";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        report.log("Fetching planogram_locations details \n Executing Query: " + query, true);
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the table planogram_locations for query " + query);
        }
        report.log("planogram_locations Details returned is: " + jsonObject.toString(), true);
        return jsonObject;
    }


    /**
     * Fetches the TQ for given sku and ri
     *
     * @param skuID
     * @param warehouseID
     * @param report
     * @return
     */
    public static int getTQFromStockReservationTable(int skuID, String warehouseID, IReport report) {
        int dcID = getDCIDForGivenWareHouseID(warehouseID, report);
        int riID = getRIIDForGivenDCID(dcID, report);
        String query = "select stock,wms_qoh,transient_quantity,wms_qoh_last_updated_timestamp,request_type,request_id from warehouse_stockreservation where product_description_id = " + skuID + " and reservation_info_id=" + riID + ";";
        report.log("Executing the query: " + query, true);
        JSONObject jsonObject = new JSONObject(AutomationUtilities.executeDatabaseQuery(serverName, query));
        logger.info("Response :" + jsonObject);
        if (jsonObject.getInt("numRows") == 0) {
            System.out.println(" Number of rows returned is 0\n Expected warehouse_stockreservation info \n Query Executed: " + query);
            Assert.fail("Number of rows returned is 0\n Expected warehouse_stockreservation info\n Query Executed: " + query);
        }
        report.log("Query response: " + jsonObject.getJSONArray("rows").getJSONObject(0), true);
        int wmsQoh = (int) jsonObject.getJSONArray("rows").getJSONObject(0).getDouble("transient_quantity");
        report.log("transient_quantity is : " + wmsQoh, true);
        return wmsQoh;
    }

    /**
     * Formula
     * sum of all quantities for sku in fc_sku_stock table (primary, secondary,misc)
     * +
     * total of unstacked qty taken from transferin_sku_info table(quantity-stacked qty)
     * -
     * TQ in stock reservation table
     *
     * @param warehouseID
     * @param skuID
     * @param report
     * @return
     */
    public static int calculateQohValueAsPerFormula(String warehouseID, int skuID, IReport report) {
        /*
     select sum(quantity) as quantity from fc_sku_stock where planogram_id=1 and sku_id=40001610;
select sum(quantity)-sum(stacked_quantity) as quantity from transferin_sku_info where planogram_id=1 and sku_id=40001610;

         */
        int totalCount = 0;
        int planogramID = DynamicLocationGeneralMethods.getPlanogramIDForGivenWareHouseID(warehouseID);
        String query = " select sum(quantity) as quantity from fc_sku_stock where planogram_id=" + planogramID + " and sku_id=" + skuID + ";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        report.log("Fetching total qunatity from all locations from fc_sku_stock table \n Executing Query: " + query, true);
        if (jsonObject.getInt("numRows") == 0) {
            report.log("No data returned from the table fc_sku_stock table for query " + query, true);
        }
        totalCount = (int) jsonObject.getJSONArray("rows").getJSONObject(0).getDouble("quantity");

        query = "select sum(quantity)-sum(stacked_quantity) as quantity from transferin_sku_info where planogram_id=" + planogramID + " and sku_id=" + skuID + ";";
        jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        report.log("Fetching total qunatity from transferin_sku_info table \n Executing Query: " + query, true);
        if (jsonObject.getInt("numRows") == 0) {
            report.log("No data returned from the table fc_sku_stock table for query " + query, true);
        }
        totalCount += (int) jsonObject.getJSONArray("rows").getJSONObject(0).getDouble("quantity");


        int tq = DynamicLocationGeneralMethods.getTQFromStockReservationTable(skuID, warehouseID, report);
        totalCount = totalCount - tq;
        report.log("Total QOH calculated as per formula is : " + totalCount, true);

        return totalCount;

    }


    /**
     * Writes of quanitity in primary location for given binloc,skuID and warehouseID
     *
     * @param skuID
     * @param primaryBinLocation
     * @param warehouseID
     * @param report
     */
    public static void checkAndWriteOfAnyQtyPresentInPrimaryLoc(int skuID, String primaryBinLocation, String warehouseID, IReport report) {

        StockInfo stockInfo = new StockInfo(report);
        Response response = stockInfo.callStockInfoApi(String.valueOf(skuID), warehouseID, true);
        JSONObject jsonObject = new JSONObject(response.asString());
        int totalQohInPrimary = jsonObject.getJSONObject("primary_qoh").getInt("total_qoh");
        report.log("Total Qoh in Primary : " + totalQohInPrimary, true);


        WriteOff writeOff = new WriteOff(report);

        //Writing off quantity from primary location

        if (totalQohInPrimary > 0) {
            writeOff.callWriteOffApi(primaryBinLocation, 1, skuID, totalQohInPrimary, 1, "Rain Damage", warehouseID, false);
        }

    }

    /**
     * Writes of quanitity in secondary location for given binloc,skuID and warehouseID
     *
     * @param skuID
     * @param secBinLocation
     * @param warehouseID
     * @param report
     */
    public static void checkAndWriteOfAnyQtyPresentInSecondaryLoc(int skuID, String secBinLocation, String warehouseID, IReport report) {

        StockInfo stockInfo = new StockInfo(report);
        Response response = stockInfo.callStockInfoApi(String.valueOf(skuID), warehouseID, true);
        JSONObject jsonObject = new JSONObject(response.asString());
        int totalQohInSecondary = jsonObject.getJSONObject("secondary_qoh").getInt("total_qoh");
        report.log("Total Qoh in Secondary : " + totalQohInSecondary, true);


        WriteOff writeOff = new WriteOff(report);

        //Writing off quantity from primary location

        if (totalQohInSecondary > 0) {
            writeOff.callWriteOffApi(secBinLocation, 2, skuID, totalQohInSecondary, 1, "Rain Damage", warehouseID, false);
        }

    }


    /**
     * Fetchs the job type id for the given jobID
     * <p>
     * '1', 'picking',
     * '2', 'grn-stacking'
     * '3', 'prn-stacking'
     * '4', 'rtv-ti-stacking'
     * '5', 'manual-ti-stacking'
     * '6', 'stock-movement'
     * '7', 'cycle-count'
     * '8', 'write-off'
     *
     * @param jobID
     * @param report
     * @return
     */
    public static int getJobTypeIDForGivenJobId(int jobID, IReport report) {
        String query = "select job_type_id from job where id=" + jobID + ";";
        report.log("Fetching the job type for given jobID: " + jobID + " Query: " + query, true);
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No entries returned from job table \n Query: " + query);
        }
        int jobStatusID = jsonObject.getJSONArray("rows").getJSONObject(0).getInt("job_type_id");
        report.log("Job Type ID: " + jobStatusID, true);
        return jobStatusID;
    }

    public static int getMaxSkuAllowedFromRackShelfBinUsingBinLoc(String binLoc, int planogramID,
                                                                  IReport report) {
        String query = "select max_sku_allowed from fc_rack_shelf_bin where binloc = \"" + binLoc + "\" and planogram_id = " + planogramID + ";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        report.log("Fetching bin location \n Executing Query: " + query, true);
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the table fc_rack_shelf_bin for query " + query);
        }
        int maxSku = jsonObject.getJSONArray("rows").getJSONObject(0).getInt("max_sku_allowed");
        report.log("max sku is " + maxSku, true);
        return maxSku;
    }

    /**
     * Checks whether the fc replenishement values present for given skuID and planogramID
     *
     * @param planogramID
     * @param skuId
     * @param report
     * @return
     */
    public static boolean checkWhetherFCReplenishmentPresent(int planogramID, int skuId, IReport report) {
        report.log("Checking whether the fc replenishment value present for the given skuID:  " + skuId + " and planogramID: " + planogramID, true);
        String query = "select * from fc_replenishment where planogram_id= " + planogramID + " and sku_id= " + skuId + " and is_active = 1;";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            report.log("fcReplenishment not present for skuID: " + skuId + " and planogramID: " + planogramID, true);
            return false;
        }
        report.log("fcReplenishment is present for skuID: " + skuId + " and planogramID: " + planogramID, true);
        int x = (int) jsonObject.getJSONArray("rows").getJSONObject(0).getDouble("maximum_quantity_X");
        int y = (int) jsonObject.getJSONArray("rows").getJSONObject(0).getDouble("threshold_with_default_priority_Y");
        int z = (int) jsonObject.getJSONArray("rows").getJSONObject(0).getDouble("threshold_with_highest_priority_Z");
        report.log("fcReplenishment values are : X% : " + x + " Y% : " + y + " Z% : " + z, true);
        return true;
    }

    /**
     * Deletes the fc replenishment for the given skuID and planogramID
     *
     * @param planogramID
     * @param skuID
     * @param report
     */
    public static void deleteFCReplenishement(int planogramID, int skuID, IReport report) {
        String query = "delete from fc_replenishment where planogram_id=" + planogramID + " and sku_id=" + skuID + ";";
        report.log("Deleting the fc replenishment for the given planogramID " + planogramID + " and skuID " + skuID, true);
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        boolean flag = checkWhetherFCReplenishmentPresent(planogramID, skuID, report);
        if (flag) {
            report.log("fc replenishment deleted for sku " + skuID + " planogramID " + planogramID, true);
        } else {
            Assert.fail("fc replenishment not deleted for sku " + skuID + " planogramID " + planogramID);
        }
    }

    //TODO to migrate dependencies from selenium repo
    /* */

    /**
     * Running abandon cron
     *
//     * @param cronContext
     * @param report
     *//*
    public static void runJobAbandonCron(CronContext cronContext, IReport report) {
        String hostIP =null;
        UserHostDataSet userHostDataSet;
        if (serverName.contains("hqa") || serverName.contains("uat")) {
            //CronSuiteGeneralMethods.runCron(cronContext, report, UserHostDataSet.PLANOGRAM_ADMIN);
            userHostDataSet = UserHostDataSet.PLANOGRAM_ADMIN;
        } else {
            // CronSuiteGeneralMethods.runCron(cronContext, report, UserHostDataSet.PLANOGRAM_ADMIN_QAS);
            userHostDataSet= UserHostDataSet.PLANOGRAM_ADMIN_QAS;
        }

        //CronSuiteGeneralMethods.runCron(cronContext, report, userHostDataSet.getHost(), userHostDataSet.getNamespace(), userHostDataSet.getUser());
        //CronContext cronContext, IReport report, String hostIP, String namespace, String user

        hostIP = userHostDataSet.getHost();
        String namespace = userHostDataSet.getNamespace();
        String user = userHostDataSet.getUser();
        Instant instant = Instant.now();
        long timeStampSecInUnix = instant.getEpochSecond();
        String cronName = CronSuiteGeneralMethods.getCronName(cronContext.getCronName());
        String podName = "a-" + cronName + "-" + timeStampSecInUnix;
        String nameSpace;

        report.log( "Creating the cron with podname: " + podName, true);
        if (serverName.contains("hqa")) {
            nameSpace = namespace + "-hqa";
        } else if (serverName.contains("uat")) {
            nameSpace = namespace + "-uat";
        } else {
            nameSpace = namespace + "-qa";
        }

        logger.info("Creating cron in k8s");
        String response = AutomationUtilities.executeCommands(user, hostIP,
                "kubectl create job --from=cronjob/" + cronName + " " + podName + " -n " + nameSpace + "");

    }*/
    public static int getNoOfEntriesForChildSkuInExlusionList(int planogramID, int skuId, IReport report) {
        report.log("Checking entry in exclusion for the given skuID:  " + skuId + " and planogramID: " + planogramID, true);
        String query = "select * from fc_bulk_child_sku_map_exclusion where planogram_id= " + planogramID + " and child_sku_id= " + skuId + ";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        int num = jsonObject.getInt("numRows");
        if (num == 0) {
            report.log("entry not present in exlusion list for skuID: " + skuId + " and planogramID: " + planogramID, true);
            return 0;
        }
        report.log(num + " entry present in exlusion list for skuID: " + skuId + " and planogramID: " + planogramID, true);
        return num;
    }

    public static void deleteChildSkuEntryFromExclusionList(int planogramID, int skuID, IReport report) {
        String query = "delete from fc_bulk_child_sku_map_exclusion where planogram_id=" + planogramID + " and child_sku_id=" + skuID + ";";
        report.log("Deleting fc_bulk_child_sku_map_exclusion entry for the given planogramID " + planogramID + " and skuID " + skuID, true);
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));

        if (getNoOfEntriesForChildSkuInExlusionList(planogramID, skuID, report) == 0)
            report.log("exclusion entry deleted for sku " + skuID + " planogramID " + planogramID, true);
        else
            Assert.fail("exclusion entry not deleted for sku " + skuID + " planogramID " + planogramID);
    }

    public static JSONObject getDBObjectForChildSkuInExlusionList(int planogramID, int skuId, IReport report) {
        report.log("Checking entry in exclusion for the given skuID:  " + skuId + " and planogramID: " + planogramID, true);
        String query = "select * from fc_bulk_child_sku_map_exclusion where planogram_id= " + planogramID + " and child_sku_id = " + skuId + ";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        int num = jsonObject.getInt("numRows");
        if (num == 0) {
            report.log("entry not present in exlusion list for skuID: " + skuId + " and planogramID: " + planogramID, true);
            Assert.fail("No data returned from the table write_off_reasons for query " + query);
        }
        report.log(num + " entry present in exlusion list for skuID: " + skuId + " and planogramID: " + planogramID, true);
        return jsonObject.getJSONArray("rows").getJSONObject(0);
    }

    public static boolean isParentSkuPresentInBulkChildMapping(String parentSku, IReport report) {
        report.log("Checking entry in bulk child mapping table for the given parent skuID:  " + parentSku, true);
        String query = "select * from bulk_child_sku_map where bulk_sku_id= " + parentSku + ";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        int num = jsonObject.getInt("numRows");
        if (num == 0) {
            report.log("entry not present in bulk child table for skuID: " + parentSku, true);
            return false;
        }
        report.log(num + " entry present in bulk child table for skuID: " + parentSku, true);
        return true;

    }

    public static boolean isChildSkuPresentInBulkChildMapping(String childSku, int planogramID, IReport report) {
        report.log("Checking entry in bulk child mapping table for the given parent skuID:  " + childSku, true);
        String query = "select * from bulk_child_sku_map where child_sku_id= " + childSku + ";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        int num = jsonObject.getInt("numRows");
        if (num == 0) {
            report.log("entry not present in bulk child table for skuID: " + childSku, true);
            return false;
        }
        report.log(num + " entry present in bulk child table for skuID: " + childSku, true);
        return true;

    }

    public static int getNoOfEntriesInStaticBinMapping(String skuId, int planogramID, IReport report) {
        report.log("Checking entry in static bin mapping for the given skuID:  " + skuId + " and planogramID: " + planogramID, true);
        String query = "select * from static_sku_bin_mapping where planogram_id= " + planogramID + " and sku_id = " + skuId + ";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        int num = jsonObject.getInt("numRows");
        if (num == 0) {
            report.log("entry not present in static bin mapping table for skuID: " + skuId + " and planogramID: " + planogramID, true);
            return 0;
        }
        report.log(num + " entry present in static bin mapping for skuID: " + skuId + " and planogramID: " + planogramID, true);
        return num;
    }

    /**
     * Fetching the quantity from fc_sku_stock table for given parameters
     *
     * @param planogramID
     * @param skuID
     * @param binID
     * @param report
     * @return
     */
    public static Double fetchQuantityValueFromFcSKUStockTable(int planogramID, int skuID, int binID, IReport report) {
        String query = "select quantity from fc_sku_stock where planogram_id=" + planogramID + " and sku_id=" + skuID + " and bin_id=" + binID + ";";
        report.log("Fetching the Quantity from fc_sku_stock table \n Query" + query, true);
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            report.log("No data returned from the fc_sku_stock table for query " + query, true);
            return 0.0;
        }
        Double quantity = jsonObject.getJSONArray("rows").getJSONObject(0).getDouble("quantity");
        report.log("quantity is " + quantity, true);
        return quantity;
    }

    /**
     * Fetches the wms qoh for given sku and ri
     *
     * @param skuID
     * @param warehouseID
     * @param report
     * @return
     */
    public static Double getWmsQohValueFromStockReservationTable(int skuID, String warehouseID, IReport report) {
        int dcID = getDCIDForGivenWareHouseID(warehouseID, report);
        int riID = getRIIDForGivenDCID(dcID, report);
        String query = "select stock,wms_qoh,transient_quantity,wms_qoh_last_updated_timestamp,request_type,request_id from warehouse_stockreservation where product_description_id = " + skuID + " and reservation_info_id=" + riID + ";";
        report.log("Executing the query: " + query, true);
        JSONObject jsonObject = new JSONObject(AutomationUtilities.executeDatabaseQuery(serverName, query));
        logger.info("Response :" + jsonObject);
        if (jsonObject.getInt("numRows") == 0) {
            System.out.println(" Number of rows returned is 0\n Expected warehouse_stockreservation info \n Query Executed: " + query);
            Assert.fail("Number of rows returned is 0\n Expected warehouse_stockreservation info\n Query Executed: " + query);
        }
        report.log("Query response: " + jsonObject.getJSONArray("rows").getJSONObject(0), true);
        Double wmsQoh = jsonObject.getJSONArray("rows").getJSONObject(0).getDouble("wms_qoh");
        report.log("WMS qoh is : " + wmsQoh, true);
        return wmsQoh;
    }

    /**
     * Insert child sku in the exclusion list, if child sku is not excluded already
     *
     * @param childSkuID
     * @param planogramID
     * @param report
     */
    public static void insertChildSkuInExclusionListIfNotExists(int childSkuID, int planogramID, IReport report) {
        if (DynamicLocationGeneralMethods.isChildSkuNotInExclusionList(planogramID, childSkuID, report)) {
            String query = "insert into fc_bulk_child_sku_map_exclusion (planogram_id,child_sku_id,created_on,updated_on,created_by,updated_by) values(" + planogramID + "," + childSkuID + ",current_timestamp(),current_timestamp(),\"AUTOMATION\",\"AUTOMATION\");";
            report.log("insert child sku to exclusion list \n Query: " + query, true);
            DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query);
            report.log("inserted child sku to exclusion list ", true);
        } else {
            report.log("Child sku " + childSkuID + " is already existing in exclusion list, no insert done", true);
        }
    }

    /**
     * Updates the max alternate bins as per given parameter
     *
     * @param maxAlternateBin
     */
    public static void updateMaxAlternateBin(int maxAlternateBin) {
        String query = "update planogram_config set config_value=" + maxAlternateBin + " where config_key=\"MAX_ALTERNATE_BINS\";";
        System.out.println("Updating the max alternate bin \n Query " + query);
        DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query);
        System.out.println("Max Alternate Bin updated ");
    }

    public static void waitForUploadToComplete() {
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateCycleCountRequestJobTableStatusToComplete(int cycleCountRequestID, IReport report) {
        report.log("Updating the cyclecount request , job and task to complete for cyclecountrequestID : " + cycleCountRequestID, true);
        String updateCycleCountRequestTable = "update cycle_count_request set cycle_count_status_id = 4 where id = " + cycleCountRequestID + ";";
        System.out.println("Updating the cycle count status id in cycle_count_request table \n Query " + updateCycleCountRequestTable);
        DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(updateCycleCountRequestTable);
        System.out.println("Updated Cycle Count Request Table ");

        String updateCycleCountJobTable = "update cycle_count_job set cycle_count_status_id = 4 where cycle_count_request_id  = " + cycleCountRequestID + ";";
        System.out.println("Updating the cycle count status id in cycle_count_job table \n Query " + updateCycleCountRequestTable);
        DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(updateCycleCountJobTable);
        System.out.println("Updated Cycle Count Job Table ");

    }
//TODO import all the depenencies from selenium repo to resolve conflict
    /* *//**
     * Updates cycle count request and job table to complete status and active users end time will be update to current time
     * @param planogramID
     * @param userID
     * @param cycleCountRequestID
     * @param report
     *//*
    public static void updateCycleCountRequestAndUserTable(int planogramID,String userID,int cycleCountRequestID,IReport report){
        updateCycleCountRequestJobTableStatusToComplete(cycleCountRequestID, report);
        CycleCount.updateTheEndTimeForUser(userID,report,planogramID);
    }*/

//TODO import all the depenencies from selenium repo to resolve conflict
    /* *//**
     * Does admin login and navigate to planogram admin page
     *
     * @param driver
     * @param report
     *//*
    public void reLoginNavigateToPlanogramAdmin(WebDriver driver, IReport report) {
        //Admin Login
      *//*  report.log("Launching the admin");
        launchSite("admin", driver);*//*
        Login login = new admin.pages.Login(driver, report);
        HomePage homePage = login.loginAdmin("su");


        //Navigating to rackVariantupload page
        report.log( "Navigating to rackVariantupload page", true);
        Microservices microservices = homePage.clickOnMicroservicesURLs();
        PlanogramMicroservices planogramMicroservices = microservices.clickOnPlanogram();
        PlanogramAdminUI planogramAdminUI = planogramMicroservices.clickOnPlanogramLink();
        if (driver.getTitle().contains("bigbasket Admin")) {
            login.loginAdmin("su");
        }
    }*/

    /**
     * Writes of quanitity in primary location for given binloc,skuID and warehouseID
     *
     * @param skuID
     * @param primaryBinLocation
     * @param warehouseID
     * @param totalQtyToWriteOffFromPrimary
     * @param report
     */
    public static void checkAndWriteOfAnyQtyPresentInPrimaryLoc(int skuID, String primaryBinLocation, String warehouseID, int totalQtyToWriteOffFromPrimary, IReport report) {
        StockInfo stockInfo = new StockInfo(report);
        Response response = stockInfo.callStockInfoApi(String.valueOf(skuID), warehouseID, true);
        JSONObject jsonObject = new JSONObject(response.asString());
        int totalQohInPrimary = jsonObject.getJSONObject("primary_qoh").getInt("total_qoh");
        report.log("Total Qoh in Primary : " + totalQohInPrimary, true);

        if (totalQtyToWriteOffFromPrimary > totalQohInPrimary) {

            report.log("Skipping writeoff as the qty mentioned to write off is greater than the current qty present in primary location", true);
            report.log("Requested qty to write off:  " + totalQtyToWriteOffFromPrimary + "\nCurrent qty present in Primary loc: " + totalQohInPrimary, true);
        } else {
            WriteOff writeOff = new WriteOff(report);
            //Writing off quantity from primary location

            if (totalQohInPrimary > 0) {
                writeOff.callWriteOffApi(primaryBinLocation, 1, skuID, totalQtyToWriteOffFromPrimary, 1, "Rain Damage", warehouseID, false);
            }
        }
    }


    /**
     * Fetches the record from pickingrecobin set for given primary key
     *
     * @param primaryKey
     * @param report
     * @return
     */
    public static JSONObject updateAerospikeRecordForPickingRecobin(int locationId, int skuId, int planogramId, String binLoc, int qty, int binId, int batchId, String primaryKey, IReport report) {
        String record = preparePickingRecobinBody(locationId, skuId, planogramId, binLoc, qty, binId, batchId);
        report.log("Updating the picking recobin record", true);
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.updateAerospikeRecordForPickingRecobinByPk(
                "pickingrecobin", primaryKey, record));
        if (!jsonObject.toString().contains("updated bin record sucessfully")) {
            System.out.println("Aerospike record update failed");
        }
        report.log("Record fetched is  " + jsonObject, true);
        return jsonObject;
    }

    /*
   {
   "planogram_id": 1,
   "bins": {
       "81": {
           "bin_loc": "B-10-A-3",
           "active_tasks": null,
           "quantity": 36.0,
           "bin_id": 81,
           "batch_id": 1
       }
   },
   "sku_id": 40001610,
   "location_id": 1
}
    */
    private static String preparePickingRecobinBody(int locationId, int skuId, int planogramId, String binLoc, int qty, int binId, int batchId) {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("planogram_id", planogramId);
        jsonObjectBuilder.add("sku_id", skuId);
        jsonObjectBuilder.add("location_id", locationId);

        JsonObjectBuilder binObject = Json.createObjectBuilder();
        binObject.add("bin_loc", binLoc);
        binObject.add("active_tasks", JsonValue.NULL);
        binObject.add("quantity", qty);
        binObject.add("bin_id", binId);
        binObject.add("batch_id", batchId);

        JsonObjectBuilder binsObject = Json.createObjectBuilder();
        binsObject.add(String.valueOf(binId), binObject);
        jsonObjectBuilder.add("bins", binsObject);

        JsonObjectBuilder recordObject = Json.createObjectBuilder();
        recordObject.add("record", jsonObjectBuilder);
        JsonObject jsonObject = recordObject.build();

        System.out.println("Body: " + jsonObject);
        return jsonObject.toString();
    }

    /**
     * Fetches the binlocation for the given binID
     *
     * @param planogramID
     * @param report
     * @return
     */
    public static String[] getBinLocationForGivenplanogramID(int planogramID, IReport report) {
        String query = "select binloc from fc_rack_shelf_bin where planogram_id=" + planogramID + ";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        report.log("Fetching bin location \n Executing Query: " + query, true);
        int numBinLocs = jsonObject.getInt("numRows");
        if (numBinLocs == 0) {
            Assert.fail("No data returned from the table fc_rack_shelf_bin for query " + query);
        }
        String[] binLocList = new String[numBinLocs];
        for (int i = 0; i < numBinLocs; i++) {
            binLocList[i] = jsonObject.getJSONArray("rows").getJSONObject(i).getString("binloc");
        }

        report.log("BinLocationList is " + Arrays.toString(binLocList), true);
        return binLocList;
    }

    /**
     * check whether sku bin mappling upload is successful
     *
     * @param whCode
     * @param report
     * @return boolean
     */
    public static boolean checkSkuBinMappingUploadIsSuccessful(String whCode, ArrayList<String> skuIdList, int numOfRowsAddedInSkuBinMapping, IReport report) {
        try {
            System.out.println("Waiting for 2sec before querying the DB");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int fc_planogram_id = 0;
        String query = "select id from fc_planogram where wh_code = \"" + whCode + "\";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 1) {
            fc_planogram_id = jsonObject.getJSONArray("rows").getJSONObject(0).getInt("id");
            report.log("planogram id = " + fc_planogram_id, true);
        } else {
            System.out.println("no record found for given warehouse code" + whCode);
            report.log("no record found for given warehouse code" +
                    "\n No data returned for query" + query, true);
        }

        StringBuilder tempStrBuilder = new StringBuilder();
        for (int i = 0; i < numOfRowsAddedInSkuBinMapping - 1; i++) {
            tempStrBuilder.append(skuIdList.get(i));
            tempStrBuilder.append(",");
        }
        tempStrBuilder.append(skuIdList.get(numOfRowsAddedInSkuBinMapping - 1));
        query = "select sku_id,bin_loc from static_sku_bin_mapping where planogram_id = " + fc_planogram_id + " and sku_id in (" + tempStrBuilder.toString() + ");";

        String responseStr = DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query);
        jsonObject = new JSONObject(responseStr);
        int numRows = jsonObject.getInt("numRows");
        if (numRows != 0 && numRows == numOfRowsAddedInSkuBinMapping) {
            report.log("sku bin mapping  is uploaded successfully", true);
            return true;
        } else {
            report.log("sku bin mapping is not uploaded\n " +
                    "No data returned for query: " + query, true);
            return false;
        }
    }


    /**
     * Fetching the planogramID for given fcId
     *
     * @param fcID
     * @return
     */
    public static int getPlanogramIDForGivenFcID(int fcID) {
        String query = "select id from fc_planogram where fc_id=\"" + fcID + "\";";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the table fc_planogram for query " + query);
        }
        return jsonObject.getJSONArray("rows").getJSONObject(0).getInt("id");
    }

    /**
     * Fetches the binID from sku bin mapping table
     *
     * @param skuID
     * @param fcId
     * @param report
     * @return
     */
    public static int[] getBinIDfromskuBinMappingForGivenSkuIDAndfcID(int skuID, int fcId, IReport report) {
        int planogramID = getPlanogramIDForGivenFcID(fcId);
        String query = "select bin_id from static_sku_bin_mapping where planogram_id=" + planogramID + " and sku_id=" + skuID + "; ";
        report.log("Getting the binID \n Query: " + query, true);
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned from the sku bin mapping table \n " + query);
        }
        int binID[] = new int[jsonObject.getInt("numRows")];
        for (int i = 0; i < binID.length; i++) {
            binID[i] = jsonObject.getJSONArray("rows").getJSONObject(i).getInt("bin_id");
        }
        report.log("All mapped bins for skuID " + skuID + " is: " + Arrays.toString(binID), true);
        return binID;
    }



    /**
     * Fetches the primary/secondary bin location based on given location id
     *
     * @param skuID
     * @param fcId
     * @param locationId
     * @param report
     * @return
     */
    public static String[] getBinLocationForGivenSkuIDAndfcID(int skuID, int fcId,int locationId, IReport report) {
        if(!(locationId == 1 || locationId ==2)) {
        Assert.fail("Not a valid location ID, Expected Value is 1 - primary location, 2 - secondary location");
        }
        int planogramID = getPlanogramIDForGivenFcID(fcId);
        String query = "select frsb.binloc from fc_rack_shelf_bin frsb " +
                "LEFT JOIN static_sku_bin_mapping ssbm ON frsb.id=ssbm.bin_id " +
                "where ssbm.planogram_id = "+planogramID+" and ssbm.sku_id = "+skuID+" and frsb.location_id ="+locationId+"; ";
        report.log("Getting the binlocation \n Query: " + query, true);
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned  \n " + query);
        }
        String binLocation[] = new String[jsonObject.getInt("numRows")];
        for (int i = 0; i < binLocation.length; i++) {
            binLocation[i] = jsonObject.getJSONArray("rows").getJSONObject(i).getString("binloc");
        }
        report.log("All mapped binLocation for skuID " + skuID + " and locationID "+locationId+" is: " + Arrays.toString(binLocation), true);
        return binLocation;
    }

    public static int getpickingjobid( IReport report) {
        String query = "select id from job where job_type_id=1 limit 1";
        report.log("Fetching the picking job id \n " + query, true);
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No picking job id  returned \n" + query);
        }
        return jsonObject.getJSONArray("rows").getJSONObject(0).getInt("id");
    }
}