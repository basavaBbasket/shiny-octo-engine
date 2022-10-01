package bbnow.testcases.cart;

import com.bigbasket.automation.Config;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import msvc.basketsvc.PerformCartServices;
import org.testng.annotations.Test;

public class PerformCartServicesTest extends BaseTest{
    @DescriptionProvider(author = "Shruti", description = "This testcase validates response for different Cart Services.",slug = "Validate Response when items are added,deleted and updated in cart")
    @Test(groups = {"bbnow"})
    public void addBulkItemToCartTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);
        String entrycontextid= Config.bbnowConfig.getString("entry_context_id");
        String entrycontext=Config.bbnowConfig.getString("entry_context");
        PerformCartServices cartObj=new PerformCartServices(entrycontextid,entrycontext,report);
        cartObj.cartServices();
    }}