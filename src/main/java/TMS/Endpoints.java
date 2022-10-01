package TMS;

public interface Endpoints {

    String  VERSION_1="v1";
    String  VERSION_2 = "v2";

    String TMS_PATH = "";

    String GET_SLOTS="/order/external/"+VERSION_1+"/get-slots";

    String RESERVER_SLOTS = "/order/external/"+VERSION_1+"/reserve-slot";

    String CONFIRM_ORDER = "/order/external/"+ VERSION_2 + "/confirm-order";

    String ORDER_DETAILS_INTERNAL = "/order/internal/"+VERSION_1+"/orders/%s";

    String ORDER_DETAILS_EXTERNAL = "/order/external/"+VERSION_1+"/orders/%s";

    String UPDATE_ORDER_STATUS = "/order/external/"+VERSION_1+"/order";

    String CANCEL_ORDER_INTERNAL = "/order/internal/"+VERSION_1+"/order/%s/cancel" ;

    String CANCEL_ORDER_EXTERNAL = "/order/external/"+VERSION_1+"/order/%s/cancel";


}
