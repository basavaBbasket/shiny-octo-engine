package msvc.payment.internal;

public interface Endpoints {

    String PAYMENT_VERSION="v1";
    String VALIDATE_PAYMENT_SVC = "/payment_internal/"+PAYMENT_VERSION+"/validate-payment-service";
}
