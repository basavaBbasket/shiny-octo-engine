package msvc.planogram.internal;

public interface Endpoints {

      String API_VERSION = "v1";

      String GET_ALL_FC_ALERTS = "/planogram/internal/" + API_VERSION +"/fcs/%s/alerts";
      String GENRATE_ALERTS = "/planogram/internal/" + API_VERSION + "/fcs/%s/alerts";
      String ASSIGNED_PICKER = "/planogram/internal/" + API_VERSION+"/fcs/%s/jobs/assigned-users";
      String CANCELLED_ORDER_BIN_LIST =  "/planogram/internal/" + API_VERSION + "/fcs/%s/deliverybin";
      String ORDER_BIN_MAPPING = "/planogram/internal/" + API_VERSION + "/fcs/%s/delivery/orderbinmapping";
      String PICKING_JOB_CREATION = "/planogram/internal/" +API_VERSION +  "/fcs/%s/picking/jobs";
      String GET_PICKER_JOB = "/planogram/internal/" +API_VERSION +"/fcs/%s/jobs";
      String PICKER_JOB_ASSIGNMENT = "/pickingplatform/internal/v1/picking/jobs";
      String CHECK_VALID_DELIVERY_BIN = "/planogram/internal/" + API_VERSION +"/fcs/%s/delivery/bin";
      String COUNT_OF_BUSY_PICKERS = "/planogram/internal/" + API_VERSION+ "/fcs/%s/jobs/assigned-users";


}
