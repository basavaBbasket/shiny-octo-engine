package bbnow.testcases.cart;

import com.bigbasket.automation.Config;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import msvc.basketsvc.PerformCartServices;
import org.testng.annotations.Test;

public class PerformCartServicesTest extends BaseTest{
    @DescriptionProvider(author = "Shruti", description = "1. Add items in bbnow cart \n"+
            "2. Keep on adding items till maximum allowable units for that item is added \n"+
            "3. Verify cart total weight \n"+
            "4. Remove items from cart \n"+
            "5. Add bulk items in cart \n"+
            "6. Empty the cart",slug = "Validate Response when items are added,deleted and updated in cart")
    @Test(groups = {"bbnow"})
    public void addBulkItemToCartTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);
        String entrycontextid= Config.bbnowConfig.getString("entry_context_id");
        String entrycontext=Config.bbnowConfig.getString("entry_context");
        PerformCartServices cartObj=new PerformCartServices(entrycontextid,entrycontext,report);
        cartObj.cartServices();
    }}