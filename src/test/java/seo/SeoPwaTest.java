package seo;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SeoPwaTest extends BaseTest {

    @DescriptionProvider(
            author = "Sameer",
            description = "This case tests SEO for PWA browsers")
    @Test(
            groups = {"seoProd"},
            dataProvider = "getDataProviderPwa",
            dataProviderClass = SeoSuiteConfig.class)
    public void SeoPwa(SeoValidator pwaValidators) {
        List<String> browserOptions = new ArrayList<>();
        String expectedUserAgent = pwaValidators.getUserAgents();
        String userAgentDescription = pwaValidators.getUserAgentsDescription();
        browserOptions.add("--user-agent=" + expectedUserAgent);
        String serverURL = pwaValidators
                .getUrl().replace("serverName", trimmedServerName);
        List<String> testName = Arrays.asList(serverURL.split("/"));
        AutomationReport report = getInitializedReport(
                this.getClass(),
                "User Agent: " + userAgentDescription + " | " + testName.get(testName.size()-1),
                true,
                browserOptions,
                pwaValidators.getClass());

        report.log("Testing for: " + serverURL, true);
        WebDriver driver = report.driver;
        WebDriverWait wait = new WebDriverWait(driver, 20);

        JavascriptExecutor jsDriver = (JavascriptExecutor) driver;
        report.log("opening webpage in browser", true);
        driver.get(serverURL);
        wait.until(
                (ExpectedCondition<Boolean>) driver1 -> jsDriver.executeScript("return document.readyState")
                        .equals("complete"));
        report.log("webpage loaded successfully", true);

        String userAgent = jsDriver.executeScript("return navigator.userAgent").toString();
        Assert.assertEquals(userAgent, expectedUserAgent, StringUtils.difference(userAgent, expectedUserAgent));

        SeoHandler seoHandler = new SeoHandler(driver, report);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(seoHandler.titleCheck(pwaValidators.getExpectedTitle()), "Title validation failed");
        softAssert.assertTrue(seoHandler.metaDataCheck(pwaValidators.getExpectedMeta()), "Meta data validation failed");
        softAssert.assertTrue(seoHandler.canonicalHrefCheck(serverURL), "Canonical Href validation failed");
        softAssert.assertTrue(seoHandler.canonicalParentCheck(), "Canonical parent validation failed");
        if(!userAgentDescription.contains("Google bot")) {
            softAssert.assertTrue(seoHandler.gtmKeyCheck(pwaValidators.getExpectedGTMKey()), "GTM validation failed");
            softAssert.assertTrue(seoHandler.googleAnalyticsCheck(pwaValidators.getExpectedGoogleAnalyticsKey()), "Google analytics key validation failed");
        }
        report.log("Asserting results", true);
        softAssert.assertAll("Webpage validation failed for\n" + userAgent + "\n\n");
    }

}
