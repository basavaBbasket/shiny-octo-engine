package test;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.apache.log4j.Logger;

public class TestClass extends BaseTest {

    Logger logger = Logger.getLogger(TestClass.class);

    @DescriptionProvider(slug = "test",description = "just testing", author = "rakesh")
    @Test(groups = "testing-bb2")
    public void testingClass() throws InterruptedException {
        AutomationReport report = getInitializedReport(this.getClass(),false);
        logger.info("Ran 1test case successfully.");
        //launchSite();
        report.log("helloooooo",true);
        Assert.assertTrue(false);
    }
}
