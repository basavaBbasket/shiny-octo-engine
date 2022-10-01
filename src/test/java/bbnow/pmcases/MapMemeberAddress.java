package bbnow.pmcases;

import admin.pages.HomePage;
import admin.pages.Login;
import admin.pages.MapMemberAddressPage;
import api.warehousecomposition.planogram_FC.AdminCookie;
import api.warehousecomposition.planogram_FC.EligibleSkuMethods;
import com.bigbasket.automation.Config;
import com.bigbasket.automation.mapi.mapi_4_1_0.MemberCookie;
import com.bigbasket.automation.mapi.mapi_4_1_0.OrderPlacement;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import framework.Settings;
import io.vertx.core.json.JsonObject;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapMemeberAddress extends BaseTest {

    @DescriptionProvider(slug = "Map Member Address", description = "1. Fetch member id from member_member table and get email,mobile from creds \n" +
            "            2. Place BBNow Order\n" +
            "            3. Login Admin and go to map member Address Page. \n" +
            "            4. Verify Adress detail for given memebr wrt orderId,memberId,email and mobile. \n" ,author = "tushar")

    @Test(groups = {"map-member-address","bbnow"})
    public void mapMemberAddress() throws InterruptedException {

        AutomationReport report = getInitializedReport(this.getClass(), true);

        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName, Config.bbnowConfig.getString("bbnow_stores[1].member_sheet_name"));
        String member_id,email, orderId;
        JsonObject jsonObj_id = new JsonObject(AutomationUtilities.executeDatabaseQuery(serverName, "select id from member_member where mobile_no="+creds[0] +";"));
        member_id = String.valueOf(jsonObj_id.getJsonArray("rows").getJsonObject(0).getInteger("id"));
        email = creds[1];

        report.log("Starting  order placement.", true);
        String entrycontext= Config.bbnowConfig.getString("entry_context");
        String entrycontextid=Config.bbnowConfig.getString("entry_context_id");
        String clientUserSheetName = Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_client_user_sheet_name");
        String areaName = Config.bbnowConfig.getString("bbnow_stores[1].area");
        int fcId = Config.bbnowConfig.getInt("bbnow_stores[1].fc_id");
        String searchTerm = Config.bbnowConfig.getString("bbnow_stores[1].category_slug1");
        String searchType = Config.bbnowConfig.getString("bbnow_stores[1].search_type2");

        String[] adminCred = AutomationUtilities.getUniqueAdminUser(serverName, "admin-superuser-mfa");
        String adminUserName = adminCred[0];
        String adminPassword = adminCred[1];
        Map<String, String> adminCookie = AdminCookie.getMemberCookie(adminUserName, adminPassword, report);

        String cred[] = AutomationUtilities.getUniqueLoginCredential(serverName, clientUserSheetName);
        Map<String,String> memberCookie = MemberCookie.getMemberCookieForCustomAddress(cred[0],areaName,report);
        String addressIdSelectedForOrder = memberCookie.get("address_id");
        report.log("member Cookie"+ memberCookie,true);

        int skuID =  EligibleSkuMethods.fetchSkuVisibleAndAvailableInPrimaryBin(memberCookie,adminCookie,
                fcId,entrycontext,Integer.parseInt(entrycontextid),areaName,searchTerm,searchType,report);

        HashMap<String, Integer> skuMap = new HashMap<>();
        skuMap.put(String.valueOf(skuID), 2);
        orderId = OrderPlacement.placeBBNowOrder("bbnow" , 10,cred[0], areaName, skuMap, true, false, report);

        report.log("Order Id: " + orderId,true);
        report.log("member Mobile no: "+creds[0],true);
        report.log("member Id:"+ member_id,true);
        report.log("email: "+ email , true);

        WebDriver driver = report.driver;
        launchSite("admin", driver);
        report.log("Credentials:"+cred,true);
        report.log("Creds for admin login: "+adminUserName+" "+adminPassword,true);
        Login login = new Login(driver,report);
        HomePage homePage = login.doAdminLogin(adminUserName,adminPassword);
        MapMemberAddressPage mapMemberAddressPage = homePage.navigateToMapMemberAddress();
        ArrayList<String> id = new ArrayList<>();
        id.add(orderId);
        id.add(member_id);
        id.add(email);
        id.add(creds[0]);
        mapMemberAddressPage.checkMemberAddress(id,addressIdSelectedForOrder);


    }

}
