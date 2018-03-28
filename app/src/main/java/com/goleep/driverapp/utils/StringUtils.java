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
    static NumberFormat currencyFormatter;
    static NumberFormat numberFormatter;

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
        if (currencyFormatter == null)
            currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        currencyFormatter.setMaximumFractionDigits(0);
        String moneyString = currencyFormatter.format(amountString);
        if (amountString != null) {
            return moneyString;
        }
        return currencyFormatter.format(0);
    }

    public static String numberToDisplay(int number) {
        if (numberFormatter == null)
            numberFormatter = NumberFormat.getNumberInstance();
        numberFormatter.setMaximumFractionDigits(0);
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

    public static String addZeroToSingleCharacter(String str) {
        if (str.length() == 1)
            return "0" + str;
        else return str;
    }
}
