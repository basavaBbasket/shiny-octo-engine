package msvc.order.internal;

public interface Endpoints {

    String ORDER_VERSION="v1";
    String PAYMENT_STATUS = "/order/"+ORDER_VERSION+"/payment/";
    String ORDERS = "/order/internal/"+ORDER_VERSION+"/orders/";
    String ORDER_LIST="/order/"+ORDER_VERSION+"/orders/list";
    String CREATE_PO="/order/internal/"+ORDER_VERSION+"/potentialorder";
    String UPDATE_PAYMENT_STATUS="/order/internal/"+ORDER_VERSION+"/order/payment_status";
}
