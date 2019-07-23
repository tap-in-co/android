package com.tapin.tapin.utils;

public class URLs {

    public static String BASE_URL = "https://tapforall.com/staging/tap-in/businessinfo/";
    public static String SERVER_URL = "https://tapforall.com/staging/tap-in/";    // Home
    public static String HOME = SERVER_URL + "businessinfo/index.php?businessID=0";

    public static String PROFILE = SERVER_URL + "profilesystem/consumerprofile.php";

    public static String REWARD = SERVER_URL + "include/model.php";
    public static String AD_RESTAURANT = SERVER_URL + "include/model.php";
    //https://tapforall.com/staging/tap-in/include/model.php?cmd=set_device_token_for_business&business_name="Bardo"&device_token="1000000003"

    public static final String MAIN_URL = "https://tapforall.com/staging/tap-in/include/model.php?";

    public static String IMAGE_URL = "https://tapforall.com/staging/tap-in/customer_files/icons/";
    public static String IMAGE_URL1 = "https://tapforall.com/staging/tap-in/customer_files/";

    public static String IMAGE_URL_LIVE = "https://tapforall.com/merchants/tap-in/customer_files/icons/";

    //    public static final String GET_HOME_DATA = "https://tapforall.com/staging/tap-in/businessinfo/index.php?cmd=getBusinessInfoWithConsumerRating&businessID=0";
    public static final String GET_HOME_DATA = MAIN_URL + "business_id=0&cmd=getBusinessInfoWithConsumerRating";

    public static final String BUSINESS_MENU = MAIN_URL;

    public static final String BUSINESS_DELIVERY_INFO = MAIN_URL;

    public static final String GET_CARDS_INFO = MAIN_URL;

    public static final String MAIN_BASE_URL = MAIN_URL;

}