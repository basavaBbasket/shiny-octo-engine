package msvc.fandv;

import com.bigbasket.automation.reports.AutomationReport;
import io.restassured.response.Response;
import io.vertx.core.json.JsonObject;
import org.testng.Assert;
import utility.api.FnvAPI;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class StoreAssociateLogin {
    Map<String, String>  cookie;
        String channel;
        String tracker;
        String caller;

    String timeStamp;
    String applabel;
    String csurftoken;

        public StoreAssociateLogin(
                Map<String, String>  cookie,
                String channel,
                String tracker,
                String caller,
                String timeStamp,
                String applabel,
                String csurftoken){
            this.channel = channel;
            this.tracker = tracker;
            this.caller = caller;
            this.timeStamp = timeStamp;
            this.applabel = applabel;
            this.csurftoken = csurftoken;
            this.cookie = cookie;
        }
        public Response validateStoreAssociateLogin(String serverName,String encoded_data, AutomationReport report) throws IOException {
            String endpoint = String.format(Endpoints.BB_StoreAssociate_Login);
            Map<String, String> requestHeader = new HashMap<>();
            Properties prop=new Properties();
            prop.load(new FileInputStream("src//main//resources//fandvconfig.properties"));

            requestHeader.put("Content-Type","application/json");
            requestHeader.put("X-Channel",channel);
            requestHeader.put("X-Tracker",tracker);
            requestHeader.put("X-Caller",caller);
            requestHeader.put("X-TimeStamp",timeStamp);
            requestHeader.put("X-APPLABEL",applabel);
            requestHeader.put("x-csurftoken",csurftoken);
            JsonObject body= new JsonObject();
            body.put("encoded_data",encoded_data);
            Response response = FnvAPI.postWithHeaderAndCookies(serverName + endpoint, requestHeader,cookie,body.toString(),report,"Validate StoreAssociateLogin");
            Assert.assertEquals(response.statusCode(),200,"Invalid StatusCode for StoreAssociateLogin API");
            return response;
        }

    }


