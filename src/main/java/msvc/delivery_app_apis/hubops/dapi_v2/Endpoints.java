package msvc.delivery_app_apis.hubops.dapi_v2;

public interface Endpoints {
    String DAAPI_VERSION="v2";
    String VERIFY_LOGIN ="/hubops/dapi/"+DAAPI_VERSION+"/verify-login";
    String CEE_CURRENT="/hubops/dapi/"+DAAPI_VERSION+"/cee/current?include=%s";
    String UPDATE_CEE_CURRENT="/hubops/dapi/"+DAAPI_VERSION+"/cee/current";
    String GET_CONFIG="/hubops/dapi/"+DAAPI_VERSION+"/get-config/";
    String CEE_CHECKIN="/hubops/dapi/"+DAAPI_VERSION+"/cee-check-in";
    String ROUTE_ASSIGHMENT="/hubops/dapi/"+DAAPI_VERSION+"/cee/current/route_assignment/";
    String GET_ORDERS="/hubops/dapi/"+DAAPI_VERSION+"/get-orders";
    String CEE_LOCATION_DATA="/hubops/dapi/"+DAAPI_VERSION+"/cee-location-data";
    String CALL_REQUEST="/hubops/dapi/"+DAAPI_VERSION+"/order/%s/call_request";
}
