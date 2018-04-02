package com.goleep.driverapp.utils;

import java.util.Locale;

import static com.goleep.driverapp.utils.DateTimeUtils.TWELVE_HOUR_TIME_FORMAT;
import static com.goleep.driverapp.utils.DateTimeUtils.TWENTY_FOUR_HOUR_TIME_FORMAT;

/**
 * Created by vishalm on 13/03/18.
 */

public class StringUtils {
    public static String getAddress(String line1, String line2) {
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
        return address;
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

    public static String amountToDisplay(Float amountString) {
        String currencySymbol = AppUtils.userCurrencySymbol();
        if (amountString != null) {
            return currencySymbol + " " + Math.round(amountString);
        }
        return currencySymbol + " 0";
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

}
