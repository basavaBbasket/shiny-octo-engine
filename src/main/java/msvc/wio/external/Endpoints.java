package msvc.wio.external;

public interface Endpoints {
    String API_VERSION = "v1";
    String GET_RETURNING_CEE_LIST = "/warehousecomposition/admin/warehouseinboundoutbound/" + API_VERSION + "/returnreceiving/fc/%s/list";
    String UPDATE_RETURN_RECEIVING = "/warehousecomposition/admin/warehouseinboundoutbound/"+ API_VERSION + "/returnreceiving";
    String TRANSFER_IN="/warehouseinboundoutbound/internal/"+ API_VERSION + "/transferin/%s";
}
