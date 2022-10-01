package seo;

import com.bigbasket.automation.utilities.GoogleSheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.testng.Assert;
import org.testng.annotations.DataProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SeoSuiteConfig {

    HashMap<String, ValueRange> getGSheetData(String testDataRange, String userAgentsRange) {
        String spreadsheetID = "1tYJ3OvBA0x6QEVG9NN0ZnQVWKvUsD-JzybyUUfWeq1M";
        ValueRange testData = null;
        ValueRange userAgents = null;
        try {
            GoogleSheets googleSheets = new GoogleSheets();
            testData = googleSheets
                        .getSheetData(spreadsheetID, testDataRange);
            userAgents = googleSheets
                        .getSheetData(spreadsheetID, userAgentsRange);
        }catch (Exception exception) {
            exception.printStackTrace();
        }

        Assert.assertNotNull(testData,
                "Unable to get google sheet values +" +
                        "Please verify: " + spreadsheetID);
        Assert.assertNotNull(userAgents, "User agent cannot be null " +
                "Please verify: " + spreadsheetID);

        HashMap<String, ValueRange> testDataMap = new HashMap<>();
        testDataMap.put("testData", testData);
        testDataMap.put("userAgents", userAgents);
        return testDataMap;
    }

    List<SeoValidator> prepareTestData(HashMap<String, ValueRange> testDataMap){
        ArrayList<SeoValidator> validatorList = new ArrayList<>();
        for (List<Object> userAgent : testDataMap.get("userAgents").getValues()) {
            System.out.println("Updating data for: " + userAgent.get(1));
            System.out.println(userAgent.get(0) + "\n");
            for (List<Object> testRow: testDataMap.get("testData").getValues()) {
                validatorList.add(
                        new SeoValidator.seoValidatorBuilder(testRow.get(0).toString())
                                .setExpectedTitle(testRow.get(1).toString())
                                .setExpectedMeta(testRow.get(2).toString())
                                .setExpectedCanonicalLink(testRow.get(3).toString())
                                .setExpectedGTMKey(testRow.get(4).toString())
                                .setExpectedGoogleAnalyticsKey(testRow.get(5).toString())
                                .setUserAgents(userAgent.get(0).toString())
                                .setUserAgentsDescription(userAgent.get(1).toString())
                                .build());
            }
        }
        return validatorList;
    }

    List<SeoValidator> getDesktopTests() {
       String userAgentsRange = "userAgentDesktop!A2:B4";
       String testDataRange = "seoDesktop!A2:H37";
       return prepareTestData(getGSheetData(testDataRange, userAgentsRange));
    }

    List<SeoValidator> getPwaTests() {
        String userAgentsRange = "userAgentPwa!A2:B4";
        String testDataRange = "seoPwa!A2:H37";
        return prepareTestData(getGSheetData(testDataRange, userAgentsRange));
    }

    @DataProvider(name="getDataProviderDesktop")
    public Object[] getDataProviderDesktop() {
        return getDesktopTests().toArray();
    }

    @DataProvider(name="getDataProviderPwa")
    public Object[] getDataProviderPwa() {
        return getPwaTests().toArray();
    }
}
