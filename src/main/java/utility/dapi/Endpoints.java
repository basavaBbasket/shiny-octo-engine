package utility.dapi;

public interface Endpoints {

    String DAAPI_VERSION_1="v1";
    String DAAPI_VERSION_2="v2";
    String CHECK_CEE_ID ="/hubops/dapi/"+DAAPI_VERSION_1+"/check-bb2-cee-id/%s";
    String VERIFY_LOGIN = "/hubops/dapi/"+DAAPI_VERSION_2+"/verify-login";
    String UPDATE_GCM = "/hubops/dapi/"+DAAPI_VERSION_1+"/update-gcm";
    String CEE_CURRENT = "/hubops/dapi/"+DAAPI_VERSION_2+"/cee/current";
    String CHECK_AUTH = "/hubops/dapi/"+DAAPI_VERSION_1+"/check-auth";
    String GET_CONFIG = "/hubops/dapi/"+DAAPI_VERSION_1+"/get-config";
    String CEE_CHECK_IN = "/hubops/dapi/"+DAAPI_VERSION_2+"/cee-check-in";
    String CEE_LOCATION_DATA = "/hubops/dapi/"+DAAPI_VERSION_2+"/cee-location-data";
    String ROUTE_ASSIGNMENT = "/hubops/dapi/"+DAAPI_VERSION_2+"/cee/current/route_assignment";
    String COLLECT_ORDERS = "/hubops/dapi/"+DAAPI_VERSION_2+"/route/%s/orders";
    String GET_ORDERS = "/hubops/dapi/"+DAAPI_VERSION_2+"/get-orders";
    String CEE_TRIP_STATUS = "/hubops/dapi/"+DAAPI_VERSION_1+"/cee-trip-status";

}
