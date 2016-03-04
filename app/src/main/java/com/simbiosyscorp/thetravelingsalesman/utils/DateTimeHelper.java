package com.simbiosyscorp.thetravelingsalesman.utils;

import org.joda.time.LocalDate;

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

    public static String formatDate(Date date) {

        return getDateFormat().format(date);
    }

    private static DateFormat getMonthDateFormat() {
        return new SimpleDateFormat("MMM dd", Locale.getDefault());
    }

    public static String formatShortDate(Date date) {
        return getMonthDateFormat().format(date);
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

    public static int compare(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);

        int m1 = c1.get(Calendar.HOUR) * 60 + c1.get(Calendar.MINUTE);
        int m2 = c2.get(Calendar.HOUR) * 60 + c2.get(Calendar.MINUTE);

        return m1 - m2;
    }

    public static boolean isSameYear(Date d1, Date d2) {
        LocalDate l1 = new LocalDate(d1);
        LocalDate l2 = new LocalDate(d2);
        return l1.getYear() == l2.getYear();
    }

    public static String getFullTime(Date date) {
        return DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.FULL,
                Locale.getDefault()).format(date);
    }

    public static String formatDateForExport(Date time) {
        return new SimpleDateFormat("yyyy/MMM/dd", Locale.getDefault()).format(time);
    }
}
