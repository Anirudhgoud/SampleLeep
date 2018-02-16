package com.goleep.driverapp.constants;

import static com.goleep.driverapp.BuildConfig.BASE_URL;

/**
 * Created by vishalm on 08/02/18.
 */

public class UrlConstants {
    private UrlConstants(){

    }
    public static final String LOGIN_URL = BASE_URL + "/login";
    public static final String FORGOT_PASSWORD_URL = BASE_URL + "/forgot_password";
    public static final String LOGOUT_URL = BASE_URL + "/logout";
    public static final String DRIVERS_URL = BASE_URL + "/drivers";
    public static final String SUMMARY_URL = BASE_URL + "/summary";
}
