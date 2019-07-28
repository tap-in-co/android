package com.tapin.tapin.utils

import com.tapin.tapin.BuildConfig

object UrlGenerator {
    private const val STAGING_URL = "https://tapforall.com/staging/tap-in/"
    private const val PROD_URL = "https://tapforall.com/tap-in/"

    fun getBaseUrl(): String = if (!BuildConfig.DEBUG) PROD_URL else STAGING_URL

    fun getMainUrl(): String = "${getBaseUrl()}include/model.php?"

    fun getCorporateDomainApi(domain: String) =
        "${getMainUrl()}cmd=getCorpsForDomain&domain=$domain"

    fun getRestaurantsApi(isCorpOrder: Boolean = false, corporateOrderMerchantIds: String) =
        if (isCorpOrder) {
            //"https://tapforall.com/merchants/tap-in/include/model.php?cmd=get_all_businesses_for_set&ids=$corporateOrderMerchantIds"
            "${getMainUrl()}cmd=get_all_businesses_for_set&ids=$corporateOrderMerchantIds"
        } else {
            // Individual
            "${getMainUrl()}business_id=0&cmd=getBusinessInfoWithConsumerRating"
        }

    fun getProfileApi(): String = "${getBaseUrl()}profilesystem/consumerprofile.php"

    fun getRewardApi(): String = "${getBaseUrl()}include/model.php"

    fun getImageBaseApi(): String = "${getBaseUrl()}customer_files/"

    fun getImageIconsBaseApi(): String = "${getBaseUrl()}customer_files/icons/"

    fun getConsumerCardDetails(consumerId: String) =
        "${getMainUrl()}cmd=get_consumer_default_cc&consumer_id=$consumerId"

    // All old urls, to be deleted at clean up time
    //var BASE_URL = "https://tapforall.com/staging/tap-in/businessinfo/"
    //var HOME = STAGING_URL + "businessinfo/index.php?businessID=0"

    //var PROFILE = STAGING_URL + "profilesystem/consumerprofile.php"//https://tapforall.com/staging/tap-in/profilesystem/consumerprofile.php

    //var REWARD = STAGING_URL + "include/model.php"
    //var AD_RESTAURANT = STAGING_URL + "include/model.php"
    //https://tapforall.com/staging/tap-in/include/model.php?cmd=set_device_token_for_business&business_name="Bardo"&device_token="1000000003"

    //private const val MAIN_URL = "https://tapforall.com/staging/tap-in/include/model.php?"

    //var IMAGE_URL = "https://tapforall.com/staging/tap-in/customer_files/icons/"
    //var IMAGE_URL1 = "https://tapforall.com/staging/tap-in/customer_files/"

    //var IMAGE_URL_LIVE = "https://tapforall.com/merchants/tap-in/customer_files/icons/"

    //    public static final String GET_HOME_DATA = "https://tapforall.com/staging/tap-in/businessinfo/index.php?cmd=getBusinessInfoWithConsumerRating&businessID=0";
    //val GET_HOME_DATA = MAIN_URL + "business_id=0&cmd=getBusinessInfoWithConsumerRating"

    //val BUSINESS_MENU = MAIN_URL

    //val BUSINESS_DELIVERY_INFO = MAIN_URL

    //val GET_CARDS_INFO = MAIN_URL

    //val MAIN_BASE_URL = MAIN_URL
}