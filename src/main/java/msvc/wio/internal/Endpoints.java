package msvc.wio.internal;

public interface Endpoints {
    String API_VERSION = "v1";
    String FC_RECEIVED_LIST = "/warehouseinboundoutbound/internal/" + API_VERSION + "/fcreceiving/fc/%s/list";
    String FC_RECEIVING_MARK_STATUS_COMPLETE = "/warehouseinboundoutbound/internal/" + API_VERSION +"/fcreceiving/status/complete";

}
