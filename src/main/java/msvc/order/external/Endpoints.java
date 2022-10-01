package msvc.order.external;

public interface Endpoints {
    String ORDER_VERSION="v1";

    String TRACK_ORDER= "/order/"+ORDER_VERSION+"/order/%s/track";
    String ORDER_DETAILS="/order/"+ORDER_VERSION+"/order/%s/detail";
    String INITIATE_CEE_CALL="/order/"+ORDER_VERSION+"/order/%s/initiate-cee-call";
    String CREATE_ORDER="/order/"+ORDER_VERSION+"/order";

}
