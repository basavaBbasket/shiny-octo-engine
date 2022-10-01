package bbnow.testcases.eta.thresold;

import com.bigbasket.automation.Config;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.reports.IReport;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import framework.Settings;
import io.restassured.response.Response;
import msvc.eta.internal.ThresoldMessage;
import org.joda.time.DateTime;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class getThresholdMessageWithIncompleteHeaders extends BaseTest {

    @DescriptionProvider(author = "Tushar", description = "This testcase checks API throws 401 when request has incomplete set of headers",slug = "API throws 401 when requested with incomplete set of headers")
    @Test(groups = {"bbnow" , "regression"})


    public void getMessageAtDifferentThresholdsWithIncompleteHeaders()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String xcaller="bb-now";// cannot be null
        String entrycontextid= Config.bbnowConfig.getString("entry_context_id");
        String entrycontext=Config.bbnowConfig.getString("entry_context");

        //Query Params
        String sa_id = Config.bbnowConfig.getString("bbnow_stores[1].sa_id");


        String[] thresold = {"Normal" , "T1" , "T2"};

        String order_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new DateTime(new Date()).toDate());

        for(int i = 0; i<=2 ; i++)
        {
            String thres_name = thresold[i];
            getThresholdStatusWithIncompleteHeaders(xcaller,entrycontext,entrycontextid,sa_id,thres_name, order_time,report);
        }
    }

    public  void getThresholdStatusWithIncompleteHeaders(String xcaller,String xEntryContext,String xEntryContextId,String sa_id,String threshold,String order_timeStamp, IReport report)
    {
        ThresoldMessage thresoldMessage = new ThresoldMessage(xcaller,xEntryContext,xEntryContextId,sa_id,threshold,order_timeStamp,report);
        Response response = thresoldMessage.GetMessageWithIncompleteHeaders();
        Assert.assertEquals(response.getStatusCode(),401);
        report.log("Status Code for "+threshold +" :"+ response.getStatusCode(),true);

    }

}
