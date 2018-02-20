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

    public static Date dateFrom(String dateString, String dateFormatString){
        if(dateString == null || dateFormatString == null){
            return  null;
        }
        DateFormat format = new SimpleDateFormat(dateFormatString);
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
