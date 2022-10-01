package msvc.jhulk;

public interface Endpoints {
    String API_VERSION = "v1";
    String MEMBER_L1_DETAILS = "/jhulk/internal/" + API_VERSION + "/member/l1/%s";
    String CURRENT_DELIVERY = "/jhulk/internal/" + API_VERSION +"/member/current-delivery";

}
