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

    //Delivery Orders
    String DELIVERY_ORDERS_URL = BASE_URL + "/delivery_orders";
    String CONSUMER_LOCATIONS_URL = BASE_URL + "/locations";
    String BUSINESS_LOCATIONS_URL = BASE_URL + "/businesses";

    //PickupConfirmation
    String PICKUP_CONFIRMATION = DELIVERY_ORDERS_URL + "/in_transit";
}
