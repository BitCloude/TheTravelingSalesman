package com.appers.ayvaz.thetravelingsalesman.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by D on 12/24/2015.
 */
public class DateTimeHelper {
    public static DateFormat getDateFormat() {
        return DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
    }

    public static DateFormat getTimeFormat() {
        return DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
    }

    public static String formatMed(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Calendar cc = Calendar.getInstance();
        cc.setTime(new Date());

        DateFormat df;
        if (c.get(Calendar.YEAR) == cc.get(Calendar.YEAR)) {
            df = getMonthDateFormat();
        } else {
            df = getDateFormat();
        }
        return df.format(date);
    }

    private static DateFormat getMonthDateFormat() {
        return new SimpleDateFormat("MMM dd", Locale.getDefault());
    }

    public static String formatTime(Date date) {
        return getTimeFormat().format(date);
    }


    public static Date fromContentResolver(String date) {

        Long timestamp = Long.parseLong(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return calendar.getTime();
    }
}
