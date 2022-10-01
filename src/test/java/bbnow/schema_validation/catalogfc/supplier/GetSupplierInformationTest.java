package bbnow.schema_validation.catalogfc.supplier;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.catalogfc.supplier.GetSupplierInformation;
import org.testng.annotations.Test;
import java.util.ArrayList;

public class GetSupplierInformationTest extends BaseTest {

    @DescriptionProvider(author = "Pranay", description = "This testcase validates response schema for Get Supplier Information api.",slug = "Validate Supplier Information api")
    @Test(groups = {"bbnow","dl2","catalogfc","dl2-schema-validation"})
    public void orderTrackingApiTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String xCaller = "catalog-fc";//todo
        String xEcId = "1";//todo
        String ec = "bigbasket";//todo
        ArrayList<Integer> supplierids=new ArrayList<>();
        supplierids.add(2);
        GetSupplierInformation getSupplierInformation= new GetSupplierInformation(xCaller,xEcId,ec,report);


        Response response = getSupplierInformation.getSupplierDetails("schema//catalog//supplier//supplier-information-200.json",supplierids);
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("Live order tracking response: " + response.prettyPrint(),true);
    }
}
