package bbnow.testcases.member.address;

import bbnow.testcases.member.DataProviderClass;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import msvc.jhulk.CurrentDeliveryApi;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Map;

public class GetCurrentDeliveryAddress extends BaseTest {

    @DescriptionProvider(author = "Rakesh", description = "This TestCase verifies that applicable EC_IDs are part of the respsonse and validates the schema ", slug = "Verify current delivery api response")
    @Test(groups = {"bbnow" , "regression", "regression"}, dataProviderClass = DataProviderClass.class, dataProvider = "get-membersource-membertype-combinations")
    public void verifyCurrentDeliveryApi(Map<String, Object> dataMap) {

        String source = (String) dataMap.get("src");
        int srcId = (int) dataMap.get("src_id");

        String memberType = (String) dataMap.get("member_type");
        int memberTypeId = (int) dataMap.get("member_type_id");

        int memberId = Integer.parseInt((String) dataMap.get("member_id"));

        AutomationReport report = getInitializedReport(this.getClass(), "Source: " + source + ", MemberType:" + memberType, false, Map.class);

        String EntryContext = "bbnow";
        String EntryContextId = "10";
        String xCaller = "Test";
        String xService = "123";

        CurrentDeliveryApi currentDeliveryApi = new CurrentDeliveryApi(EntryContext, EntryContextId, xCaller, xService, report);
        Response response = currentDeliveryApi.getCurrentDelivery(String.valueOf(memberId));

        Assert.assertEquals(response.getStatusCode(), 200, "Incorrect status code returned by api. ");


        JsonPath responseBody = response.jsonPath();
        Map<Object,Object> jsonMap = responseBody.get("member");

        ArrayList<Integer> fetched_ecID = (ArrayList<Integer>) jsonMap.get("ec_ids");
        report.log("EC IDs returned by api: " + fetched_ecID.toString(), true);

        ArrayList<Integer> ecIds = DataProviderClass.getEcIdList(srcId, memberTypeId);
        report.log("Expected EC IDs: " + ecIds, true);

        Assert.assertEquals(fetched_ecID, ecIds, "Fetched ecIds from api is not equal to expected ecIds");
    }
}
