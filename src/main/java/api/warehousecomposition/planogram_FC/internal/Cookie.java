package api.warehousecomposition.planogram_FC.internal;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to handle all the Admin related cookies. To make request to planogram apis(dynamic location)
 */
public class Cookie {
    public Map<String, String> allCookies;

    public Cookie(Map<String,String> cookie){
        this.allCookies = new HashMap<>(cookie);
    }

    /**
     * Method to update existing cookie with latest cookie
     * @param newCookie
     */
    public void updateCookie(Map<String, String> newCookie){
        this.allCookies.putAll(new HashMap<>(newCookie));
    }
}
