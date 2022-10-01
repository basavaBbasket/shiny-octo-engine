package bbnow.schema_validation.bbrover.internal;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.bbrover.internal.MapsEta;
import msvc.delivery_app_apis.hubops.CeeAttributesForOrderIds;
import org.testng.annotations.Test;

import java.util.ArrayList;

public class MapsEtaTest extends BaseTest {
    @DescriptionProvider(author = "Pranay", description = "This testcase validates response schema for MapsEta api.",slug = "Validate MapsEta api")
    @Test(groups = {"bbnow","dl2","eta","bbnow-schema-validation","dl2-schema-validation","api-schema-validation","test2"})
    public void MapsEtaTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);
        String cookie = "_bb_cid=1; _bb_vid=\"Mzk1NDA1MzM1Ng==\"; _bb_tc=0; _bb_rdt=\"MzE0NDc3NjE4NQ==.0\"; _bb_rd=6; _client_version=2748; sessionid=c7mtx66jcad0mfz5cl5z52yrz1mwd4lh; ts=\"2021-11-18 16:30:35.220\"";//todo

        String sourcelat="12.971843262197813";//todo
        String sourcelong="77.67357636987722";//todo
        String destlat="13.043223985371377";//todo
        String destlong="77.56375446073605";//todo
        String profile= "van";//todo
        String provider= "osrm";//todo
        ArrayList requiredAnnotations=new ArrayList();
        requiredAnnotations.add("distance");
        requiredAnnotations.add("duration");//todo
        MapsEta mapsEta=new MapsEta(report);
        Response response = mapsEta.getDistanceDuration("schema//bbrover//maps-eta-200.json",sourcelat,sourcelong,destlat,destlong,profile,provider,requiredAnnotations);
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("MapsEta  response: " + response.prettyPrint(),true);

    }

}
