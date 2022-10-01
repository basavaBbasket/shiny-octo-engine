package msvc.catalogfc.supplier;

public interface Endpoints {
    String SUPPLIER_VERSION = "v1";
    interface Suppliers {
        String GET_SUPPLIER_INFO = "/catalog_fc/internal/"+SUPPLIER_VERSION+"/suppliers";
    }
}
