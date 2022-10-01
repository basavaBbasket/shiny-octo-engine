package bbnow.schema_validation.listing_assembler;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.listing_assembler.ListingAssemblerApi;
import org.testng.annotations.Test;

public class ListingAssembler extends BaseTest {

    @DescriptionProvider(author = "Tushar", description = "This testcase validates response schema for listing assembler api.",slug = "shorlist api")
    @Test(groups = {"bbnow","test_3","bbnow-payments","bbnow-schema-validation"})
    public void ListingAssemblerApiTest()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);

        ListingAssemblerApi listingAssemblerApi = new ListingAssemblerApi(report);
        Response response = listingAssemblerApi.getListingAssembler("schema//listing_assembler//listing-assembler-api-200.json");
        report.log("Status code: " +  response.getStatusCode(),true);
    }


}

