package msvc.planogram.internal;

public interface Endpoints {
      String GET_ALL_FC_ALERTS = "/planogram/internal/v1/fcs/1/alerts";
      String GENRATE_ALERTS = "/planogram/internal/v1/fcs/1/alerts";
      String ASSIGNED_PICKER = "/planogram/internal/v1/fcs/1/jobs/assigned-users";
      String CANCELLED_ORDER_BIN_LIST =  "/planogram/planogram/internal/v1/fcs/1/deliverybin?order_status=cancelled";
      String ORDER_BIN_MAPPING = "/planogram/planogram/internal/v1/fcs/1/orderbinmapping";
      String PICKING_JOB_CREATION = "/planogram/internal/v1/fcs/10/picking/jobs";
      String GET_PICKER_JOB = "/planogram/internal/v1/fcs/10/jobs?user=1&type=picking&status=assigned";
      String PICKER_JOB_ASSIGNMENT = "/pickingplatform/internal/v1/picking/jobs";

}
