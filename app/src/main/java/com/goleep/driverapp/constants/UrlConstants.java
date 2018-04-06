package com.goleep.driverapp.constants;

import static com.goleep.driverapp.BuildConfig.BASE_URL;

/**
 * Created by vishalm on 08/02/18.
 */

public interface UrlConstants {

    //Login
    String LOGIN_URL = BASE_URL + "/login";
    String LOGOUT_URL = BASE_URL + "/logout";
    String FORGOT_PASSWORD_URL = BASE_URL + "/forgot_password";

    //Dashboard
    String DRIVERS_URL = BASE_URL + "/drivers";
    String SUMMARY_URL = BASE_URL + "/summary";
    String INVENTORIES_URL = BASE_URL + "/inventories";

    //Delivery Orders
    String DELIVERY_ORDERS_URL = BASE_URL + "/delivery_orders";
    String BUSINESS_LOCATIONS_URL = BASE_URL + "/businesses";
    String DELIVER_DELIVERY_ORDER_URL = BASE_URL + "/delivery_orders/deliver";

    //New Sale
    String CONSUMER_LOCATIONS_URL = BASE_URL + "/consumer_locations";

    //Returned Orders
    String RETURNED_ORDERS = BASE_URL + "/return_orders";

    //PickupConfirmation
    String PICKUP_CONFIRMATION = DELIVERY_ORDERS_URL + "/in_transit";

    //Update profile image
    String UPDATE_PROFILE_IMAGE = BASE_URL + "/users/";

    //Reports
    String REPORT_URL = BASE_URL + "/reports";
    //Maps base url
    String DISTANCE_MATRIX_API = "https://maps.googleapis.com/maps/api/distancematrix/json?";
    String LAT_LONG_TO_ADDRESS_URL = "https://maps.googleapis.com/maps/api/geocode/json?";

    //Drop off
    String BUSINESS_CATEGORIES_URL = BASE_URL+"/business_categories";
    String BUSINESSES_URL = BASE_URL+"/businesses";

    //Util
    String COUNTRY_URL = BASE_URL+"/countries";
}
