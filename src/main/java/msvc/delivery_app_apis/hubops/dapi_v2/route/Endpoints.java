package msvc.delivery_app_apis.hubops.dapi_v2.route;


public interface Endpoints {
    String DAAPI_VERSION="v2";

    String COLLECT_ORDER="/hubops/dapi/"+DAAPI_VERSION+"/route/%s/orders";

}
