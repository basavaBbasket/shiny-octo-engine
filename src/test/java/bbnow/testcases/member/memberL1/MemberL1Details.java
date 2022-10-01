package bbnow.testcases.member.memberL1;

import bbnow.testcases.member.DataProviderClass;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import msvc.jhulk.MemberL1DetailsApi;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Map;

public class MemberL1Details extends BaseTest {

    @DescriptionProvider(author = "tushar", description = "This testCase fetches l1 details for a member of different source & member type combination & validates applicable ec ids", slug = "Fetch member L1 details")
    @Test(groups = {"bbnow", "regression"}, dataProviderClass = DataProviderClass.class, dataProvider = "get-membersource-membertype-combinations")
    public void getMemberL1Details(Map<String, Object> dataMap) {

        String source = (String) dataMap.get("src");
        int srcId = (int) dataMap.get("src_id");

        String memberType = (String) dataMap.get("member_type");
        int memberTypeId = (int) dataMap.get("member_type_id");

        int memberId = Integer.parseInt((String) dataMap.get("member_id"));

        AutomationReport report = getInitializedReport(this.getClass(), "Source: " + source + ", MemberType:" + memberType, false, Map.class);

        String EntryContext = "bbnow";
        String EntryContextId = "10";
        String xCaller = "JPlaces";

        MemberL1DetailsApi addNewKeyApi = new MemberL1DetailsApi(EntryContext, EntryContextId, xCaller, report);
        Response response = addNewKeyApi.getMemberL1Details(String.valueOf(memberId));

        Assert.assertEquals(response.getStatusCode(), 200);

        JsonPath responseBody = response.jsonPath();
        ArrayList<Integer> fetched_ecID = responseBody.get("ec_ids");
        report.log("EC IDs returned by api: " + fetched_ecID.toString(), true);

        ArrayList<Integer> ecIds = DataProviderClass.getEcIdList(srcId, memberTypeId);
        report.log("Expected EC IDs: " + ecIds, true);

        Assert.assertEquals(fetched_ecID, ecIds, "Fetched ecIds from api is not equal to expected ecIds");
    }


}
