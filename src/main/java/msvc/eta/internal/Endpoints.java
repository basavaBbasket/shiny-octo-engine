package msvc.eta.internal;

public interface Endpoints {
    String ETA_VERSION="v1";

    String GET_ETA = "/eta/internal/"+ETA_VERSION+"/store/eta?sa_id=%s";
    String GET_ORDER_ETA="/eta/internal/"+ETA_VERSION+"/order/eta?order_id=%s";
}
