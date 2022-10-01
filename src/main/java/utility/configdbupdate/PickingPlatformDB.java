package utility.configdbupdate;

public enum PickingPlatformDB {
    UPDATE_ORDER_STATUS("update picking_request set order_status = \"cancelled\" " +
            "where created_on < DATE_SUB(CURDATE(), INTERVAL 0 Day) and order_status='open';"),
    UPDATE_CONTAINER_STATUS("update container set status=\"Available\" " +
            "where status=\"In-use\" and updated_on<DATE_SUB(CURDATE(),INTERVAL 0 Day);");

    private final String value;

    PickingPlatformDB(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
