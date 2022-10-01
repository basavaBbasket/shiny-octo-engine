package utility.configdbupdate;

public enum DynamicLocationDB {
    UPDATE_IS_OCCUPIED_IN_BIN_REQUEST_MAPPING("update bin_request_id_mapping set is_occupied=0 " +
            "where created_on < DATE_SUB(CURDATE(), INTERVAL 1 Day) and is_occupied=1;");

    private final String value;

    DynamicLocationDB(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
