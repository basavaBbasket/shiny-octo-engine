package api.mapi;

public interface MapiRoutes {

    String MAPI_VERSION = "/mapi/v3.5.1";

    String USER_AGENT = "Mozilla/5.0 (Linux; Android 8.0.0; Pixel 2 XL Build/OPD1.170816.004) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Mobile Safari/537.36";


    interface MemberRoutes {

        //Request to register device
        String REGISTER_DEVICE = MAPI_VERSION + "/register/device/";

        //Request for user login
        String MAPI_LOGIN = MAPI_VERSION + "/member/login/";

        //Request to generate otp
        String GENERATE_OTP = MAPI_VERSION + "/member/login/otp";

        //Request for login in with otp
        String OTP_LOGIN = MAPI_VERSION + "/member/login/otp/";

        //Add item to cart
        String SAVE_FOR_LATER = MAPI_VERSION + "/member/add-to-sfl/";

        //Sign up
        String MEMBER_CREATE = MAPI_VERSION + "/member/create";

        //Request Member wallet AMount
        String WALLET_AMOUNT = MAPI_VERSION + "/member/wallet/";

        //Did You Forget API in basket
        String DID_YOU_FORGET_API = MAPI_VERSION + "/member/dyf/";

    }

    interface OrderRoutes {

        //Request for order cancellation
        String ORDER_CANCEL = MAPI_VERSION + "/order/cancel/";

        //Request for order invoice
        String ORDER_INVOICE = MAPI_VERSION + "/order/invoice/";
    }

    interface CartRoutes {

        //Request for clear basket
        String CLEAR_BASKET = MAPI_VERSION + "/c-empty/";

        //Add item to cart
        String CART_ADD_ITEM = MAPI_VERSION + "/c-incr-i/";

        //Remove an item from cart
        String DELETE_FROM_CART = MAPI_VERSION + "/c-set-i/";

    }

    interface CatalogRoutes{

        //Create Solicitation ID For RnR
        String RNR_SOLICITATION_ID_GENERATION = "/catalog_svc/rnr/api/external/v1/test/check_order_delivery/";

        //Activate Deactivate Category Ids
        String RNR_ACTIVATE_DEACTIVATE_CATEGORY_ID = "/catalog_svc/rnr/api/external/v1/test/change_category_rating_display/";

    }

    interface AddressRoutes {

        //Address List
        String ADDRESS_LIST = MAPI_VERSION + "/address/list";

        //Set Current Address
        String SET_CURRENT_ADDRESS = MAPI_VERSION + "/set-current-address/";

    }

    interface ProductRoutes {

        //Product List
        String PRODUCT_LIST = MAPI_VERSION + "/product/list/";

    }

}
