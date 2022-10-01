package msvc.wio.internal.fcreturn.internal;

public interface Endpoints {
    String FCRETURN_VERSION = "v1";

    interface FcReturn {
        String FC_ENDPOINT = "/warehouseinboundoutbound/internal/" + FCRETURN_VERSION + "/fcs/%s/fcreturn";
    }
}
