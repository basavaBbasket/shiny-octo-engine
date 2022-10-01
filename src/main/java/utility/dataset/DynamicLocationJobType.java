package utility.dataset;

public enum DynamicLocationJobType {
    PICKING("picking"),
    GRN_STACKING("grn-stacking"),
    PRN_STACKING("prn-stacking"),
    RTV_TI_STACKING("rtv-ti-stacking"),
    MANUAL_TI_STACKING("manual-ti-stacking"),
    STOCK_MOVEMENT("stock-movement"),
    CYCLE_COUNT("cycle-count"),
    WRITE_OFF("write-off");
    private final String value;

    private DynamicLocationJobType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
