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
        if (date == null || date.equals("")) {
            return null;
        }
        Long timestamp = Long.parseLong(date);
        return fromMillis(timestamp);

    }

    public static Date fromMillis(long millis) {
        if (millis == 0) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }
}
