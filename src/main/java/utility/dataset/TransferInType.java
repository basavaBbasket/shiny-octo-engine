package utility.dataset;

public enum TransferInType {
    grn("grn"), //grn
    AAA("aaa"), //negative test
    RTV("rtv-ti"),
    PRN("prn-ti"),
    MANUAL("manual-ti"),
    VENDOR("vendor-prn");
    private final String value;

    private TransferInType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}