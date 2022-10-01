package fandVAPITests;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.fandv.*;
import org.json.JSONObject;
import org.testng.annotations.Test;
import utility.api.spalashscreenfnv.SplashScreenUtility;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

public class SpalshscreenFnV extends BaseTest {
    @DescriptionProvider(author = "nijaguna", description = "Runs all the API's related to Splash screen of FnV App .",slug = "Instansiate Splash screen ")
    @Test(groups = {"spalshscreen_fnv" , "regression","fnv_automation",})
    public void integrationTest() throws IOException {
        AutomationReport report = getInitializedReport(this.getClass(), false);
        SplashScreenUtility.splashscreenAPI(report);
    }

}
