package test;

import com.bigbasket.automation.Config;
import com.bigbasket.automation.mapi.mapi_4_1_0.OrderPlacement;
import com.bigbasket.automation.mapi.mapi_4_1_0.internal.BigBasketApp;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.apache.log4j.Logger;

public class TestClass extends BaseTest {

    Logger logger = Logger.getLogger(TestClass.class);

    @DescriptionProvider(slug = "test",description = "just testing", author = "rakesh")
    @Test(groups = "testing-order-placement")
    public void testingClass() throws InterruptedException {
        AutomationReport report = getInitializedReport(this.getClass(),false);
        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName, Config.bbnowConfig.getString("bbnow_stores[0].member_sheet_name"));
        Response response = new BigBasketApp(report).getAppDataDynamic()
                .login(creds[0])
                .getAppDataDynamic()
                .setEntryContext("bbnow",10).getHeaderApi(true);
        System.out.println(response.prettyPrint());

    }

    @DescriptionProvider(slug = "test2",description = "just testing", author = "vinay")
    @Test(groups = "testing-bb3")
    public void testingClass2() throws InterruptedException {
        AutomationReport report = getInitializedReport(this.getClass(),false);
        report.log("helloooooo1",true);
        Assert.assertTrue(false);
    }

    @DescriptionProvider(slug = "test3",description = "just testing", author = "rakesh")
    @Test(groups = "testing-bb3")
    public void testingClass3() throws InterruptedException {
        AutomationReport report = getInitializedReport(this.getClass(),false);
        report.log("helloooooo1",true);
        Assert.assertTrue(true);
    }

    @DescriptionProvider(slug = "test4",description = "just testing", author = "rakesh")
    @Test(groups = "testing-bb3")
    public void testingClass4() throws InterruptedException {
        AutomationReport report = getInitializedReport(this.getClass(),false);
        report.log("helloooooo1",true);
        Assert.assertTrue(true);
    }

    @DescriptionProvider(slug = "test5",description = "just testing", author = "vinay")
    @Test(groups = "testing-bb3")
    public void testingClass5() throws InterruptedException {
        AutomationReport report = getInitializedReport(this.getClass(),false);
        report.log("helloooooo1",true);
        Assert.assertTrue(false);
    }

    @DescriptionProvider(slug = "test6",description = "just testing", author = "rakesh")
    @Test(groups = "testing-bb3")
    public void testingClass6() throws InterruptedException {
        AutomationReport report = getInitializedReport(this.getClass(),false);
        report.log("helloooooo1",true);
        Assert.assertTrue(false);
    }

    @DescriptionProvider(slug = "test7",description = "just testing", author = "vinay")
    @Test(groups = "testing-bb3")
    public void testingClass7() throws InterruptedException {
        AutomationReport report = getInitializedReport(this.getClass(),false);
        report.log("helloooooo1",true);
        Assert.assertTrue(true);
    }
}
