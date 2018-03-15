package com.goleep.driverapp.utils;

/**
 * Created by vishalm on 13/03/18.
 */

public class StringUtils {
    public static String getAddress(String line1, String line2) {
        String address = "";
        if (line1 != null) {
            address = line1;
        }
        if (line2 != null) {
            if (line1 != null) {
                address += ", ";
            }
            address = address + line2;
        }
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
                String startTime = DateTimeUtils.convertdDate(times[0].trim(), "HH:mm", "hh:mma");
                String endTime = DateTimeUtils.convertdDate(times[1].trim(), "HH:mm", "hh:mma");
                return startTime + " - " + endTime;
            }
        }
        return "-";
    }

}
