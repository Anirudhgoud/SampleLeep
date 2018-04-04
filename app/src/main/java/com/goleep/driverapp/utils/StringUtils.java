package com.goleep.driverapp.utils;

import java.text.NumberFormat;
import java.util.Currency;
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
        currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        numberFormatter = NumberFormat.getNumberInstance();
        currencyFormatter.setMaximumFractionDigits(0);
        numberFormatter.setMaximumFractionDigits(0);
    }

    private StringUtils() {
    }


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

    public static String amountToDisplay(Float amountString) {
        return (amountString != null ? currencyFormatter.format(amountString) : currencyFormatter.format(0));
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
}
