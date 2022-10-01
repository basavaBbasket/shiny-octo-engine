package msvc.catalogfc;

public interface Endpoints {
    interface Suppliers {
        String SUPPLIER_VERSION = "v1";
        String GET_SUPPLIER_INFO = "/catalog_fc/internal/" + SUPPLIER_VERSION + "/suppliers";
    }

    interface VisibilityInternal {
        String API_VERSION = "v1";
        String CATALOG_VISIBILITY = "/catalog_visibility/internal/" + API_VERSION + "/visibility";
    }
}
