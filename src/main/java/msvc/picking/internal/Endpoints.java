package msvc.picking.internal;

public interface Endpoints {
    String API_VERSION = "v1";
    String API_VERSION2="v2";
    String PICKING_DETAILS = "/pickingplatform/internal/" + API_VERSION +"/ordercontainerinfo";
    String PICKING_ACK_API = "/pickingplatform/internal/" + API_VERSION + "/%s/pickcomplete";
    String BAG_IN_USE_VALIDTION = "/pickingplatform/internal/" + API_VERSION + "/%s/ordercontainer";
    String CREATE_ORDER_DETAIL_ENTRIES = "/pickingplatform/internal/" + API_VERSION +"/picking";;
    String FETCH_BAG_ORDER_LINKING  = "/pickingplatform/internal/" + API_VERSION +"/ordercontainerinfo";
    String PICKING_JOB_COMPLETE="/planogram/internal/"+API_VERSION2+"/fcs/%s/picking/jobs/%s/complete";

}
