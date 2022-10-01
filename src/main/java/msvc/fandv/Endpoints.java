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
}

