package com.appers.ayvaz.thetravelingsalesman.models;

import android.content.Context;
import android.util.Log;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by D on 025 02 25.
 */
public class ExpenseReportManager {
    private ExpenseReport mTotal;
    private static ExpenseReportManager instance;
    private Context mContext;

    public static ExpenseReportManager get(Context context) {
        if (instance == null) {
            instance = new ExpenseReportManager(context);
        }

        return instance;
    }

    private ExpenseReportManager(Context context) {
        this.mContext = context.getApplicationContext();
    }


}
