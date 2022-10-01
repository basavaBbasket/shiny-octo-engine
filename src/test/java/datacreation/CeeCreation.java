package datacreation;

import admin.pages.Cee;
import admin.pages.HomePage;
import admin.pages.Login;
import admin.pages.TransportManagementSystemPage;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CeeCreation extends BaseTest {

    @DescriptionProvider(author = "Vinay", description = "New Cee Creation", slug = "New Cee Creation")
    @Test(groups = {"cee_creation"})
    public void newCeeCreation() throws IOException {
        AutomationReport report = getInitializedReport(this.getClass(), true);
        WebDriver driver = report.driver;
        launchSite("admin", driver);
        String[] cred = AutomationUtilities.getUniqueAdminUser(serverName, "admin-superuser-mfa");
        String adminUser = cred[0];
        String adminPassword = cred[1];
        report.log("Credentials:" + Arrays.toString(cred), true);
        Login login = new Login(driver, report);
        HomePage homePage = login.doAdminLogin(adminUser, adminPassword);
        TransportManagementSystemPage transportManagementSystemPage = homePage.navigateToTransportManagement();
        Cee cee = transportManagementSystemPage.clickOnCeeLink();

        int numberOfNewCees = Integer.parseInt(System.getProperty("cee_creation_numberOfNewCees", "1"));
        int ceeCityId = Integer.parseInt(System.getProperty("cee_creation_cityId", "1"));
        int ceeSaId = Integer.parseInt(System.getProperty("cee_creation_saId", "10008"));
        int ceeDmId = Integer.parseInt(System.getProperty("cee_creation_dmId", "2"));
        String ceeReportEmailId = System.getProperty("cee_creation_report_emailId", "qabot@bigbasket.com");

        ArrayList<Object[]> ceesUploadData = cee.ceeBulkUpload(numberOfNewCees, ceeCityId, ceeSaId, ceeDmId);
        String ceeBulkUploadFileLocation = cee.ceeBulkUploadFilepath(ceesUploadData);

        cee.clickOnBulkUploadBtn();
        cee.choseFileToDoBulkUpload(ceeBulkUploadFileLocation);
        cee.enterEmailIdToReceiveTheReport(ceeReportEmailId);
        cee.clickOnSubmit();

        System.out.println("Waiting for 10sec before fetching the cee's id from database");
        try {
            Thread.sleep(1000 * 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String query;
        ArrayList<Integer> newCeeArray = new ArrayList<>();
        JSONObject ceeDataObject;
        for (Object[] indCeeData : ceesUploadData) {
            query = "select id from cee where mobile_number = '" + indCeeData[4] + "' ";
            ceeDataObject = new JSONObject(AutomationUtilities.executeMicroserviceDatabaseQuery(AutomationUtilities.getHertzDb(), query, report));
            newCeeArray.add(ceeDataObject.getJSONArray("rows").getJSONObject(0).getInt("id"));
        }
        report.log("Newly created cee id's: " + newCeeArray.toString(), true);

    }
}
