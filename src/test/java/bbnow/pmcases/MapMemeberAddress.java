package bbnow.pmcases;

import admin.pages.HomePage;
import admin.pages.Login;
import admin.pages.MapMemberAddressPage;
import com.bigbasket.automation.Config;
import com.bigbasket.automation.mapi.mapi_4_1_0.OrderPlacement;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import io.vertx.core.json.JsonObject;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.openqa.selenium.NoSuchElementException;
import java.util.ArrayList;

public class MapMemeberAddress extends BaseTest {
    @DescriptionProvider(author = "Tushar", description = "This is Admin Case  ", slug = "Map Member Address")
    @Test(groups = {"sat1","bbnow"})
    public void mapMemberAddress() throws InterruptedException {

        AutomationReport report = getInitializedReport(this.getClass(), true);
        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName, Config.bbnowConfig.getString("bbnow_stores[0].member_sheet_name"));

        String member_id,email, orderId;

        JsonObject jsonObj_id = new JsonObject(AutomationUtilities.executeDatabaseQuery(serverName, "select id from member_member where mobile_no="+creds[0] +";"));
        member_id = String.valueOf(jsonObj_id.getJsonArray("rows").getJsonObject(0).getInteger("id"));
        email = creds[1];
        report.log("Starting  order placement.", true);
        String searchTerm1 = Config.bbnowConfig.getString("bbnow_stores[0].search_term1");
        String searchTerm2 = Config.bbnowConfig.getString("bbnow_stores[0].search_term2");
        String[] searchTerms = {searchTerm1,searchTerm2};
        String areaName = Config.bbnowConfig.getString("bbnow_stores[0].area");
        orderId = OrderPlacement.placeBBNowOrder("bbnow" , 10 , creds[0], areaName, 1, searchTerms, true, false, report);
        report.log("Order Id: " + orderId,true);
        report.log("member Mobile no: "+creds[0],true);
        report.log("member Id:"+ member_id,true);
        report.log("email: "+ email , true);

        //member_id = "20013290";
        // orderId = "1001058861";
        // email = cred[1];

        WebDriver driver = report.driver;
        launchSite("admin", driver);
        String[] cred = AutomationUtilities.getUniqueAdminUser(serverName,"admin-superuser-mfa");
        String adminUser = cred[0];
        String adminPassword = cred[1];
        report.log("Credentials:"+cred,true);
        Login login = new Login(driver,report);
        HomePage homePage = login.doAdminLogin(adminUser,adminPassword);
        MapMemberAddressPage mapMemberAddressPage = homePage.navigateToMapMemberAddress();
        ArrayList<String> id = new ArrayList<>();
        id.add(orderId);
        id.add(member_id);
        id.add(email);
        id.add(creds[0]);

        mapMemberAddressPage.checkMemberAddress(id);


    }



}
