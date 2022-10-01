package api.admin_otp;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import msvc.basketsvc.Endpoints;

public class AdminOtp extends WebSettings implements Endpoints {

    IReport report;
    private RequestSpecification requestSpecification;

    public AdminOtp( IReport report ){
        this.report = report;
        this.requestSpecification = getSimpleRequestSpecification(serverName, this.report);
    }

    public String getOTP(String adminUser)
    {
        String endpoint = String.format(api.admin_otp.Endpoints.ADMIN_OTP);


        String otp = RestAssured.given()
                .urlEncodingEnabled(false)
                .queryParam("server",serverName)
                .queryParam("username",adminUser)
                .headers("Authorization","Basic YmJxYWJvdDpxYWJvdHBhc3N3b3Jk")
                .get(endpoint)
                .then()
                .extract()
                .path("otp");


        return otp;


    }

}
