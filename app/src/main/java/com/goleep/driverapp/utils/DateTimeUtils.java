package com.goleep.driverapp.utils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by anurag on 19/02/18.
 */

public class DateTimeUtils {

    public static String convertdDate(String dateString, String fromFormatString, String toFormatString){
        DateFormat fromFormat = new SimpleDateFormat(fromFormatString);
        fromFormat.setLenient(false);
        DateFormat toFormat = new SimpleDateFormat(toFormatString);
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


}
