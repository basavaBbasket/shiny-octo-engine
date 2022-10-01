package msvc.delivery_app_apis.hubops;


public interface Endpoints {
    String HUBOPS_VERSION="v1";

    String CEE_ATTRIBUTES_FOR_ORDER_IDS="/hubops/internal/"+HUBOPS_VERSION+"/cee-attributes-for-order-ids/";
    String LMD_STORE_METRICS="/hubops/internal/"+HUBOPS_VERSION+"/sa/%s/dm/%s/lmd-metrics";
    String LMD_STORE_METRICS_OTOR="/hubops/internal/"+HUBOPS_VERSION+"/sa/%s/dm/%s/lmd-metrics?otor=%s";
    String LMD_DATA="/hubops/internal/"+HUBOPS_VERSION+"/order/%s/lmd-data";

}
