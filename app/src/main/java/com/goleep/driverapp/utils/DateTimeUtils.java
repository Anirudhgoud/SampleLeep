package com.goleep.driverapp.utils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by anurag on 19/02/18.
 */

public class DateTimeUtils {

    public static final DateFormat ORDER_SERVER_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateFormat ORDER_DISPLAY_DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy");
    public static final DateFormat ORDER_DISPLAY_DATE_FORMAT_COMMA = new SimpleDateFormat("dd MMM, yyyy");
    public static final DateFormat REQUEST_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    public static final DateFormat TWENTY_FOUR_HOUR_TIME_FORMAT = new SimpleDateFormat("HH:mm");
    public static final DateFormat TWELVE_HOUR_TIME_FORMAT = new SimpleDateFormat("hh:mma");

    public static String convertdDate(String dateString, DateFormat fromFormat, DateFormat toFormat){
        fromFormat.setLenient(false);
        toFormat.setLenient(false);
        Date date = null;
        try {
            date = fromFormat.parse(dateString);
            return toFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String convertedDate(Date date, DateFormat requiredDateFormat) {
        requiredDateFormat.setLenient(false);
        return requiredDateFormat.format(date);
    }

    public static Date dateFrom(String dateString, DateFormat dateFormat){
        if(dateString == null || dateFormat == null){
            return  null;
        }
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
