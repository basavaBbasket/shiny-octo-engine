package msvc.delivery_app_apis.hubops.dapi_v2;

public interface Endpoints {
    String DAAPI_VERSION="v2";
    String VERIFY_LOGIN ="/hubops/dapi/"+DAAPI_VERSION+"/verify-login";
    String CEE_CURRENT="/hubops/dapi/"+DAAPI_VERSION+"/cee/current?include=%s";
    String UPDATE_CEE_CURRENT="/hubops/dapi/"+DAAPI_VERSION+"/cee/current";
}
