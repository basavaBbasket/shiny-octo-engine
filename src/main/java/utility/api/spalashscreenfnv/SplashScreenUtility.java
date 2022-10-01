package utility.api.spalashscreenfnv;

import com.bigbasket.automation.reports.AutomationReport;
import io.restassured.response.Response;
import msvc.fandv.*;
import org.json.JSONObject;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.fandv.*;
import org.json.JSONObject;
import org.testng.annotations.Test;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import static com.bigbasket.automation.BaseSettings.serverName;

/**
 *  Its a utility where all the API's related to Splashscreen of FnV are called sequentially.
 *
 *  Device Details
 *  Customer Config
 *  Register Device
 *  get Location Detail
 *  Set Current Address
 *  Header
 *  Category Tree
 *  Product Details
 *  Translate Language
 *  Store Associate Login
 */

public class SplashScreenUtility {
    public static  Map<String,String> newCookie = new HashMap<>();

    public static void splashscreenAPI(AutomationReport report) throws IOException{
    Properties prop=new Properties();
    prop.load(new FileInputStream("src//main//resources//fandvconfig.properties"));
    String channel = prop.getProperty("channel");
    String tracker = UUID.randomUUID().toString();
    String deviceId = prop.getProperty("deviceDetailsDeviceName");
    String caller = prop.getProperty("caller");
    String timeStamp = prop.getProperty("timeStamp");
    String applabel = prop.getProperty("applabel");

    DeviceDetails deviceDetails = new DeviceDetails( channel, tracker);
    Response deviceDetailsresponse = deviceDetails.validateDeviceDetails(serverName,"schema//fandv//GetDeviceDetails-200.json", deviceId, report);
    CustomerConfig customerConfig = new CustomerConfig(channel,tracker);
    Response customerConfigResponse = customerConfig.validateConfigDetails(serverName,"schema//fandv//customerConfig-200.json",report);
    String imei = prop.getProperty("imei");
    RegisterDevice registerDevice = new RegisterDevice(channel,tracker);
    Response registerDeviceResponse = registerDevice.validateRegisterDevice(serverName,"schema//fandv//registerDevice-200.json",imei,report);
    Map<String,String> cookie = registerDeviceResponse.getCookies();
    String visitorId=new JSONObject(new JSONObject(registerDeviceResponse.getBody().asString()).getJSONObject("response").toString()).getString("visitor_id");
    newCookie.put("_bb_vid",visitorId);
    for(String cookieName:cookie.keySet()){
        newCookie.put(cookieName,cookie.get(cookieName).replace("\"",  ""));
    }
    Double lat=new JSONObject(deviceDetailsresponse.getBody().asString()).getDouble("latitude");
    Double longitude=new JSONObject(deviceDetailsresponse.getBody().asString()).getDouble("longitude");
    Map<String,String> queryParams = new HashMap<>();
        queryParams.put("referrer",prop.getProperty("referrer"));
        queryParams.put("lat",String.valueOf(lat));
        queryParams.put("lng",String.valueOf(longitude));
    GetLocationDetail getLocationDetail = new GetLocationDetail(newCookie,channel,tracker);
    Response getLocationDetailResponse = getLocationDetail.validateLocationDetail(serverName,queryParams,"schema//fandv//getLocationDetail-200.json",report);
    String homeAddress = prop.getProperty("homeAddress");
    SetCurrentAddress setCurrentAddress = new SetCurrentAddress(newCookie,channel,tracker);
    Response SetCurrentAddressResponse = setCurrentAddress.validateSetCurrentAddress(serverName,homeAddress,report);
        report.log("Status code: " +  SetCurrentAddressResponse.getStatusCode(),true);
        String visAddress=SetCurrentAddressResponse.getCookie("_bb_visaddr");
        newCookie.put("_bb_visaddr",visAddress);
        newCookie.put("_bb_source","app");
    Header header = new Header(newCookie,channel,tracker);
    Response headerResponse = header.Header(serverName,"schema//fandv//header-200.json",report);
    String csurfToken=headerResponse.getCookie("csurftoken");
        newCookie.put("csurfToken",csurfToken);
    CategoryTree categoryTree = new CategoryTree(newCookie,csurfToken,channel,tracker);
    Response response = categoryTree.CategoryTree(serverName,"schema//fandv//CategoryTree-200.json",report);
    ProductDetails productDetails = new ProductDetails(newCookie,csurfToken,channel,tracker);
    Response PDResponse = productDetails.ProductDetails(serverName,"schema//fandv//ProductDetails-200.json",report);
    TranslateLanguages translateLanguages = new TranslateLanguages(newCookie,csurfToken,channel,tracker);
    Response translateLanguageresponse = translateLanguages.TranslateLanguages(serverName,"schema//fandv//translate-languages-200.json",report);
    newCookie.put("_bb_home_cache",prop.getProperty("_bb_home_cache"));
    StoreAssociateLogin storeAssociateLogin= new StoreAssociateLogin(newCookie,channel,tracker,caller,timeStamp,applabel,csurfToken);
    Response storeAssociateLoginResponse=storeAssociateLogin.validateStoreAssociateLogin(serverName,prop.getProperty("encoded_data"),report);
    }

}
