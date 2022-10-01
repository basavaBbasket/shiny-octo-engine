package framework.testng;

import com.bigbasket.automation.BaseSettings;
import framework.Settings;
import org.apache.log4j.Logger;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.bigbasket.automation.utilities.Libraries.isProdEnvironment;

public class SuiteListener extends Settings implements ITestListener, ISuiteListener, IExecutionListener, IAlterSuiteListener {

    public static int runningTestCaseNumber;
    public static int totalTestCases;
    public static int totalTestCasesPassed;
    public static int totalTestCasesFailed;
    public static int totalTestCasesSkipped;
    Logger logger = Logger.getLogger(SuiteListener.class);
    long initTime;

    public static String getCurrentTime() {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        String getTime = df.format(date);
        return getTime;
    }

    @Override
    public void alter(List<XmlSuite> suites) {
        for (XmlSuite suite : suites) {
            suite.setThreadCount(BaseSettings.threadCount);
        }
    }

    @Override
    public void onExecutionStart() {
        try {
            verifyProductionEligibility();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onExecutionFinish() {
    }

    @Override
    public void onStart(ISuite suite) {
        // TODO Auto-generated method stub
        totalTestCases = suite.getAllMethods().size();
        System.out.println("Test suite started at : " + getCurrentTime());
        System.out.println("Total test cases : " + totalTestCases);
        runningTestCaseNumber = 0;
        totalTestCasesPassed = 0;
        totalTestCasesFailed = 0;
        totalTestCasesSkipped = 0;
    }

    @Override
    public void onFinish(ISuite suite) {
        // TODO Auto-generated method stub
        System.out.println("Test suite finished at : " + getCurrentTime());
        System.out.println("Total number of test cases executed : "
                + (totalTestCasesPassed + totalTestCasesFailed + totalTestCasesSkipped));
        System.out.println("Total test cases passed  : " + totalTestCasesPassed);
        System.out.println("Total test cases failed  : " + totalTestCasesFailed);
        System.out.println("Total test cases skipped  : " + totalTestCasesSkipped);

    }

    @Override
    public void onTestStart(ITestResult result) {
        // TODO Auto-generated method stub

        runningTestCaseNumber++;
        System.out.println("Test case running number is : " + runningTestCaseNumber
                + " out of total number of test cases : " + totalTestCases);
        System.out.println("Test case running is : " + result.getName() + ", which started at : " + getCurrentTime());
        System.out.println("###################################################################");
        System.out.println("Test results so far as below. ");
        System.out.println("Total test cases passed till now : " + totalTestCasesPassed);
        System.out.println("Total test cases failed till now : " + totalTestCasesFailed);
        System.out.println("Total test cases skipped till now : " + totalTestCasesSkipped);
        System.out.println("###################################################################");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        // TODO Auto-generated method stub
        System.out.println(result.getName() + " is passed. It passed at : " + getCurrentTime());
        totalTestCasesPassed++;
    }

    @Override
    public void onTestFailure(ITestResult result) {
        // TODO Auto-generated method stub
        System.out.println(result.getName() + " is failed. It failed at time : " + getCurrentTime());

        totalTestCasesFailed++;
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // TODO Auto-generated method stub
        System.out.println(result.getName() + " is skipped. It skipped at : " + getCurrentTime());
        totalTestCasesSkipped++;
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStart(ITestContext context) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onFinish(ITestContext context) {
        // TODO Auto-generated method stub

    }

    public void verifyProductionEligibility() throws Exception {
        List eligibleProdSuites = Arrays.asList(Settings.config.getProperty("prodTags").split(","));
        List suitesToExecute = Arrays.asList(suiteName.split(","));
        if (isProdEnvironment(serverName)){
            if (!eligibleProdSuites.containsAll(suitesToExecute)){
                logger.error("CRITICAL. Cannot run all suites on production environment.");
                logger.error("Eligible suites for production : " + eligibleProdSuites.toString());
                logger.error("Suites to run: " + suitesToExecute.toString());
                System.exit(0);
                throw new Exception("Please do not try to test in production server");
            }
        }
    }

}
