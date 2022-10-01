package msvc.planogram.external;

public interface Endpoints {

    interface DynamicLocationWarehouse {
        String GRN_STACKING_JOB_RESOURCE = "/warehousecomposition/planogram/api/v1/warehouses/%s/grn-stacking/jobs/";
        String TRANSFER_IN_RESOURCE = "/warehousecomposition/planogram/api/v1/warehouses/%s/transferin/%s";
        String STACKING_ACK_RESOURCE = "/warehousecomposition/planogram/api/v1/warehouses/%s/grn-stacking/jobs/%s/";
        String ALTERNATE_BIN_RESOURCE = "/warehousecomposition/planogram/api/v1/warehouses/%s/grn-stacking/jobs/%s/alternate-location/";
        String GRN_STACKING_COMPLETE_RESOURCE = "/warehousecomposition/planogram/api/v1/warehouses/%s/grn-stacking/jobs/%s/complete";
        String STOCK_INFO_RESOURCE = "/warehousecomposition/planogram/api/v1/warehouses/%s/skus/%s/stock-info";
        String WRITE_OFF_RESOURCE = "/warehousecomposition/planogram/api/v1/warehouses/%s/write-off/jobs";
        String UNSTACKED_BATCHES_RESOURCE = "/planogram/api/v1/warehouses/%s/grn-stacking/sku/%s/unstacked-batch/";
        String PICKING_JOB_RESOURCE = "/warehousecomposition/planogram/api/v1/warehouses/%s/picking/jobs";
        String PICKING_ACK_RESOURCE = "/warehousecomposition/planogram/api/v1/warehouses/%s/picking/jobs/%s";
        String PICKING_COMPLETE_RESOURCE = "/warehousecomposition/planogram/api/v1/warehouses/%s/picking/jobs/%s/complete";
    }

    interface DynamicLocationFC {
        String GRN_STACKING_JOB_RESOURCE = "/warehousecomposition/planogram/api/v1/warehouses/%s/grn-stacking/jobs/";
        String TRANSFER_IN_RESOURCE = "/warehousecomposition/planogram/api/v1/warehouses/%s/transferin/%s";
        String STACKING_ACK_RESOURCE = "/warehousecomposition/planogram/api/v1/warehouses/%s/grn-stacking/jobs/%s/";
        String ALTERNATE_BIN_RESOURCE = "/warehousecomposition/planogram/api/v1/warehouses/%s/grn-stacking/jobs/%s/alternate-location/";
        String COMPLETE_RESOURCE = "/warehousecomposition/planogram/api/v1/warehouses/%s/grn-stacking/jobs/%s/complete";
        String STOCK_INFO_RESOURCE = "/warehousecomposition/planogram/api/v1/warehouses/%s/skus/%s/stock-info";
        String WRITE_OFF_RESOURCE = "/warehousecomposition/planogram/api/v1/warehouses/%s/write-off/jobs";
    }
}
