package TMS.api.DAPI;

public interface Endpoints {
    String  VERSION_4="v1";
    String VERIFY_LOGIN ="/hubops/dapi/"+VERSION_4+"/verify-login";

    String CEE_CURRENT = "hubops/dapi/"+ VERSION_4+ "/cee/current";
    String CEE_CHECK_IN = "/hubops/dapi/"+VERSION_4+"/cee-check-in";
    String CURRENT_ROUTE = "/hubops/dapi/"+VERSION_4+"/route/current";
    String TRIP_STATUS="/hubops/dapi/"+VERSION_4+"/cee-trip-status";
    String TRAVEL_START="hubops/dapi/"+VERSION_4+"/travel-start";
    String REACHED_CUSTOMER="hubops/dapi/"+VERSION_4+"/reached_customer";
    String VERIFICATION=" hubops/dapi/"+VERSION_4+"/order/%s/verifications";
    String ORDER_STATUS="hubops/dapi/"+VERSION_4+"/order";
    String POST_PAYMENT_RETURNS="hubops/dapi/"+VERSION_4+"/post-payment-returns";
}
