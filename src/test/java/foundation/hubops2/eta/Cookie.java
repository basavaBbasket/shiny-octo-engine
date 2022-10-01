package foundation.hubops2.eta;

import java.util.HashMap;
import java.util.Map;

public class Cookie {

    public Map<String, String> allCookies;

    public Cookie(Map<String,String> cookie){
        this.allCookies = new HashMap<>(cookie);
    }

    public void updateCookie(Map<String, String> newCookie){
        this.allCookies.putAll(new HashMap<>(newCookie));
    }
}
