package msvc.wio.internal.stockchange;

public interface Endpoints {
    String FC_VERSION = "v1";

    interface StockChange {
        String STOCKCHANGE_ENDPOINT = "/planogram/internal/"+ FC_VERSION +"/fcs/%s/stock-change";

    }
}
