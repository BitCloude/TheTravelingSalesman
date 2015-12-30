package com.appers.ayvaz.thetravelingsalesman;

import java.text.DateFormat;
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
        return getDateFormat().format(date);
    }

    public static String formatTime(Date date) {
        return getTimeFormat().format(date);
    }
}
