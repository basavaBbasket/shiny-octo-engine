package api.warehousecomposition.planogram_FC.internal;

public interface Endpoints {

    interface DynamicLocationWarehouse {
        String GRN_STACKING_JOB_RESOURCE = "/warehousecomposition/planogram/api/v1/warehouses/%s/grn-stacking/jobs/";
        String STACKING_ACK_RESOURCE = "/warehousecomposition/planogram/api/v1/warehouses/%s/grn-stacking/jobs/%s/";
        String TRANSFER_IN_RESOURCE = "/warehousecomposition/planogram/api/v1/warehouses/%s/transferin/%s";
        String ALTERNATE_BIN_RESOURCE = "/warehousecomposition/planogram/api/v1/warehouses/%s/grn-stacking/jobs/%s/alternate-location/";
        String GRN_STACKING_COMPLETE_RESOURCE = "/warehousecomposition/planogram/api/v1/warehouses/%s/grn-stacking/jobs/%s/complete";
        String STOCK_INFO_RESOURCE = "/warehousecomposition/planogram/api/v1/warehouses/%s/skus/%s/stock-info";
        String WRITE_OFF_RESOURCE = "/warehousecomposition/planogram/api/v1/warehouses/%s/write-off/jobs";
        String UNSTACKED_BATCHES_RESOURCE = "/planogram/api/v1/warehouses/%s/grn-stacking/sku/%s/unstacked-batch/";
        String PICKING_JOB_RESOURCE = "/warehousecomposition/planogram/api/v1/warehouses/%s/picking/jobs";
        String PICKING_ACK_RESOURCE = "/warehousecomposition/planogram/api/v1/warehouses/%s/picking/jobs/%s";
        String PICKING_COMPLETE_RESOURCE = "/warehousecomposition/planogram/api/v1/warehouses/%s/picking/jobs/%s/complete";
    }

    interface WareHouseCompositionFCPlanogram {
        String API_VERSION_1 = "v1";
        String TRANSFER_IN_GRN = "/warehousecomposition/admin/planogram/"+API_VERSION_1+"/fcs/%s/transferin/grn";
        String TASK_LEVEL_PICKING = "/warehousecomposition/admin/planogram/"+ API_VERSION_1 +"/fcs/1/picking/jobs/566";
        String STOCK_INFO = "/warehousecomposition/admin/planogram/"+ API_VERSION_1 +"/fcs/%s/skus/%s/stock-info";
        String GRN_STACKING_UNSTACKED_BATCH = "/warehousecomposition/admin/planogram/"+ API_VERSION_1 +"/fcs/%s/grn-stacking/sku/%s/unstacked-batch/";
        String GRN_STACKING_CREATE_JOB = "/warehousecomposition/admin/planogram/"+ API_VERSION_1 +"/fcs/%s/grn-stacking/jobs/";
        String GRN_STACKING_ALTERNATE_BIN = "/warehousecomposition/admin/planogram/"+ API_VERSION_1 +"/fcs/%s/grn-stacking/jobs/%s/alternate-location/";
        String GRN_STACKING_ACK = "/warehousecomposition/admin/planogram/"+ API_VERSION_1 +"/fcs/%s/grn-stacking/jobs/%s/";
        String GRN_STACKING_COMPLETE = "/warehousecomposition/admin/planogram/"+ API_VERSION_1 +"/fcs/%s/grn-stacking/jobs/%s/complete";
        String PICKING_CREATE_JOB = "/warehousecomposition/admin/planogram/"+API_VERSION_1+"/fcs/%s/picking/jobs";
        String JOB_ASSIGNMENT = "/warehousecomposition/admin/"+API_VERSION_1+"/fcs/%s/jobs";
        String PICKING_ACK = "/warehousecomposition/admin/planogram/"+ API_VERSION_1 +"/fcs/%s/picking/jobs/%s";
        String PICK_JOB_COMPLETE = "/warehousecomposition/admin/planogram/"+API_VERSION_1+"/fcs/%s/picking/jobs/%s/complete";
        String WRITE_OFF = "/warehousecomposition/admin/planogram/"+ API_VERSION_1 +"/fcs/%s/write-off/jobs";
        String BIN_RECOMENDATION="/warehousecomposition/admin/planogram/"+API_VERSION_1+"/fcs/%s/delivery/binrecommendation";
        String ORDER_BIN_MAPPING="/warehousecomposition/admin/planogram/"+API_VERSION_1+"/fcs/%s/delivery/orderbinmapping";
        String STOCK_MOVEMENT_LOCATION_DETAIL_USING_BINID = "/warehousecomposition/admin/planogram/"+API_VERSION_1+"/fcs/%s/stock-movement/skus/%s/location/";
        String STOCK_MOVEMENT_LOCATION_DETAIL_USING_LOCATIONID = "/warehousecomposition/admin/planogram/"+API_VERSION_1+"/fcs/%s/stock-movement/skus/%s/location/";
        String STOCK_MOVEMENT_CREATE_JOB_FOR_BIN = "/warehousecomposition/admin/planogram/"+API_VERSION_1+"/fcs/%s/stock-movement/picking/jobs/";
        String STOCK_MOVEMENT_STACK_ACK = "/warehousecomposition/admin/planogram/"+API_VERSION_1+"/fcs/%s/stock-movement/stacking/jobs/%s/";
        String STOCK_MOVEMENT_COMPLETE = "/warehousecomposition/admin/planogram/"+API_VERSION_1+"/fcs/%s/stock-movement/jobs/%s/complete/";

    }

    interface PlanogramInternal {
        String API_VERSION_1 = "v1";
        String PLANOGRAM_ALERTS ="/planogram/internal/"+API_VERSION_1+"/alerts?fc_ids=%s&alert_type=%s";
        String PLANOGRAM_JOBS ="/planogram/internal/"+API_VERSION_1+"/fcs/%s/jobs/%s";
    }

    interface WareHouseCompositionFCPickingPlatform{
        String API_VERSION_1 = "v1";
        String ORDER_BAG_LINKING = "/warehousecomposition/admin/pickingplatform/"+API_VERSION_1+"/fcs/%s/ordercontainer";
        String PICK_JOB_COMPLETE = "/warehousecomposition/admin/pickingplatform/"+API_VERSION_1+"/fcs/%s/pickcomplete";
        String ORDER_CONTAINER_INFO = "/warehousecomposition/admin/pickingplatform/"+API_VERSION_1+"/ordercontainerinfo?order_id=%s";
    }
    interface WareHouseCompositionFCPickingPlatformInternal{
        String API_VERSION_1 = "v1";
        String PERFORM_PICKING_OPERATION ="/pickingplatform/internal/"+API_VERSION_1+"/perform_picking_operation";
    }

    interface StoreAppLogin{
        String EXTERNAL_APP_LOGIN = "/external-app/login/";
        String MFA_AUTHENTICATE = "/external-app/mfa-authenticate/";
        String RESET_USER_PASSWORD = "/external-app/reset-user-password/";
    }

    interface HubOps{
        String API_VERSION_1 = "v1";
        String CEE_CHECKIN_REQUEST ="/hubops/admin/dapi/"+API_VERSION_1+"/cee_checkin_request";
    }
}


