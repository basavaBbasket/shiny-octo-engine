package msvc.fandv;


public interface Endpoints {
    String FANDV_VERSION="v4.1.0";
    String BB_Customer_QR_Login="/mapi/"+FANDV_VERSION+"/member-svc/qr/";
    String BB_Customer_OTP_Request="/mapi/"+FANDV_VERSION+"/member-svc/unified/otp/send/";
    String BB_Customer_Logout="/mapi/"+FANDV_VERSION+"/member/logout/";
    String BB_Device_Details="/store-management/v1/validate-device/replaceDeviceId";
    String BB_Customer_Config="/ui-svc/v1/app-config";
    String BB_Register_Device="/mapi/"+FANDV_VERSION+"/register/device/";
    String BB_Get_Location_Detail="/mapi/"+FANDV_VERSION+"/get-location-detail/";
    String BB_Set_Current_Address="/mapi/"+FANDV_VERSION+"/set-current-address/";
    String BB_Header="/ui-svc/v1/header";
    String BB_Category_Tree="/ui-svc/v1/category-tree";
    String BB_Product_Details="/listing-svc/v1/products?type=pc&slug=exotic-vegetables&page=1";
    String BB_Translate_languages="/translate/v1/languages";
    String BB_Alert="/projectb/external/v1/alert";
    String BB_Price_Details="/offer-svc/v1/price/details";
    String BB_StoreAssociate_Login="/external-app/decrypt-rsa-response/";
    String BB_Get_Customer_OTP_Request="http://jenkins.bigbasket.com/utils/otp/v2?server=https://hqa3.bigbasket.com&mobile=replace_Identifier&ec=101";
    String BB_Customer_OTP_Verification="/mapi/v4.2.0/member-svc/unified/login/";

}