package com.goleep.driverapp.utils;

import android.content.Context;

import com.goleep.driverapp.helpers.uimodels.Customer;
import com.goleep.driverapp.helpers.uimodels.Location;

import java.text.NumberFormat;
import java.util.Locale;

import static com.goleep.driverapp.utils.DateTimeUtils.TWELVE_HOUR_TIME_FORMAT;
import static com.goleep.driverapp.utils.DateTimeUtils.TWENTY_FOUR_HOUR_TIME_FORMAT;

/**
 * Created by vishalm on 13/03/18.
 */

public class StringUtils {
    private final static NumberFormat currencyFormatter;
    private final static NumberFormat numberFormatter;


    static {
        currencyFormatter = NumberFormat.getInstance();
        numberFormatter = NumberFormat.getNumberInstance();
        currencyFormatter.setMaximumFractionDigits(2);
        numberFormatter.setMaximumFractionDigits(0);
    }

    private StringUtils() {
    }

    public static String getAddress(Location location) {
        return location == null ? "" : location.getAddressLine1() + ",\n" + location.getAddressLine2() + ",\n" + location.getCity() + ", " + location.getState() + " " + location.getPincode();
    }

    public static String getAddress(Location location, Customer defaultLocation) {
        return location == null ? (defaultLocation == null ? "" : defaultLocation.getArea()) : location.getAddressLine1() + ",\n" + location.getAddressLine2() + ",\n" + location.getCity() + ", " + location.getState() + " " + location.getPincode();
    }

    public static String getAddress(String line1, String line2) {
        boolean isLine1Null = line1 == null || line1.equals("null");
        String addressLine1 = isLine1Null ? "" : line1;
        String addressLine2 = (line2 == null || line2.equals("null")) ? "" : line2;
        String separator = isLine1Null ? "" : ", ";
        return addressLine1 + separator + addressLine2;
    }

    public static String getFullAddress(String line1, String line2, String city, String state,
                                        String pincode){
        String address = "";
        if (line1 != null && !line1.equals("null")) {
            address = line1;
        }
        if (line2 != null && !line2.equals("null")) {
            if (line1 != null) {
                address += ", ";
            }
            address = address + line2;
        }
        if(city != null && !city.isEmpty() && !city.equals("null"))
            address += ", "+city;
        if(state != null && !state.isEmpty() && !state.equals("null"))
            address += ", "+state;
        if(pincode != null && !pincode.isEmpty() && !pincode.equals("null"))
            address += ", "+pincode;
        return address;
    }

    public static String amountToDisplay(Float amountString, Context context) {
        return (amountString != null ? AppUtils.userCurrencySymbol(context)+currencyFormatter.format(amountString) :
                AppUtils.userCurrencySymbol(context)+currencyFormatter.format(0));
    }

    public static String numberToDisplay(int number) {
        return numberFormatter.format(number);
    }

    public static String timeToDisplay(String timeString) {
        if (timeString != null) {
            String[] times = timeString.split(" - ");
            if (times.length == 2) {
                String startTime = DateTimeUtils.convertdDate(times[0].trim(),
                        TWENTY_FOUR_HOUR_TIME_FORMAT, TWELVE_HOUR_TIME_FORMAT);
                String endTime = DateTimeUtils.convertdDate(times[1].trim(),
                        TWENTY_FOUR_HOUR_TIME_FORMAT, TWELVE_HOUR_TIME_FORMAT);
                return startTime + " - " + endTime;
            }
        }
        return "-";
    }

    public static String formatToOneDecimal(int value) {
        String formattedString = String.valueOf(value);
        if (value > 1000) {
            formattedString = String.format(Locale.ENGLISH, "%.1f", (value / 1000.0)) + "k";
        }
        return formattedString;
    }

    public static String dateToDisplay(String dateString) {
        return (dateString == null) ? "-" : DateTimeUtils.convertdDate(dateString,
                DateTimeUtils.ORDER_SERVER_DATE_FORMAT, DateTimeUtils.ORDER_DISPLAY_DATE_FORMAT_COMMA);

    }

    public static String toString(String string, String nullDefault){
        return (string == null || string.equals("null")) ? nullDefault : string;
    }

}
