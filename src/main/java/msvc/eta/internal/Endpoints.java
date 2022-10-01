package msvc.eta.internal;

public interface Endpoints {
    String ETA_VERSION="v1";

    String GET_ETA = "/eta/internal/"+ETA_VERSION+"/store/eta";
    String GET_ORDER_ETA="/eta/"+ETA_VERSION+"/order/eta";
    String FIND_SAS = "/serviceability/internal/" + ETA_VERSION +"/find-sas";
    String FIND_SAS_LIST = "/serviceability/internal/" + ETA_VERSION +"/find-sas/list";

}
