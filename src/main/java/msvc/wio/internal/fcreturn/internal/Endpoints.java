package msvc.wio.internal.fcreturn.internal;

public interface Endpoints {
    String FCRETURN_VERSION = "1.0.0";

    interface FcReturn {
        String FC_ENDPOINT = "/trailblazers/warehouseinboundoutbound/internal/" + FCRETURN_VERSION + "/fcs/%s/fcreturn";
    }
}
