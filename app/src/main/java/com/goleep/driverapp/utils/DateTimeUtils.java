package com.goleep.driverapp.utils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by anurag on 19/02/18.
 */

public class DateTimeUtils {
    private DateTimeUtils() {

    }

    public static final DateFormat ORDER_SERVER_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateFormat ORDER_DISPLAY_DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy");
    public static final DateFormat ORDER_DISPLAY_DATE_FORMAT_COMMA = new SimpleDateFormat("dd MMM, yyyy");
    public static final DateFormat REQUEST_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    public static final DateFormat TWENTY_FOUR_HOUR_TIME_FORMAT = new SimpleDateFormat("HH:mm");
    public static final DateFormat TWELVE_HOUR_TIME_FORMAT = new SimpleDateFormat("hh:mma");

    public static String convertdDate(String dateString, DateFormat fromFormat, DateFormat toFormat) {
        if (dateString == null) return "-";
        fromFormat.setLenient(false);
        toFormat.setLenient(false);
        Date date;
        try {
            date = fromFormat.parse(dateString);
            return toFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "-";
    }

    public static String convertedDate(Date date, DateFormat requiredDateFormat) {
        if (date == null || requiredDateFormat == null) return "";
        requiredDateFormat.setLenient(false);
        return requiredDateFormat.format(date);
    }

    public static Date dateFrom(String dateString, DateFormat dateFormat) {
        if (dateString == null || dateFormat == null) return null;
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String currentDateToDisplay() {
        return DateTimeUtils.convertedDate(new Date(), DateTimeUtils.ORDER_DISPLAY_DATE_FORMAT_COMMA);
    }

    public static String currentTimeToDisplay() {
        return DateTimeUtils.convertedDate(new Date(), DateTimeUtils.TWELVE_HOUR_TIME_FORMAT);
    }

    public static String timeDurationIn12HrFormat(String timeDurationIn24HrFormatString){
        if (timeDurationIn24HrFormatString == null) return "-";
        String[] times = timeDurationIn24HrFormatString.split(" - ");
        if(times.length == 2){
            String startTime = DateTimeUtils.convertdDate(times[0].trim(), TWENTY_FOUR_HOUR_TIME_FORMAT, TWELVE_HOUR_TIME_FORMAT);
            String endTime = DateTimeUtils.convertdDate(times[1].trim(), TWENTY_FOUR_HOUR_TIME_FORMAT, TWELVE_HOUR_TIME_FORMAT);
            return startTime + " - " + endTime;
        }
        return "-";
    }
}
