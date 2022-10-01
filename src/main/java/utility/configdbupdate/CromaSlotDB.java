package utility.configdbupdate;


public enum CromaSlotDB {

    UPDATE_SLOT_CAPACITY_FOR_CURRENT_DAY("update slot_instance set capacity=1000  where slot_start_date=CURDATE();"),
    UPDATE_SLOT_CAPACITY_FOR_NEXT_DAY("update slot_instance set capacity=1000  where slot_start_date=CURDATE()+1;");

    private final String value;

    CromaSlotDB(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
