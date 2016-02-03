package com.appers.ayvaz.thetravelingsalesman.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.appers.ayvaz.thetravelingsalesman.models.Client;
import com.appers.ayvaz.thetravelingsalesman.models.Task;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.util.List;

/**
 * Created by D on 027 01 27.
 */
public class ReportExportUtils {
    // max possible name confliction
    private static final int MAX_COUNT = 60;
    private static final String TASK_PREFIX = "task_report_";
    private static final String EXPENSE_PREFIX = "expense_report";
    private static final String EXTENSION = ".csv";
    public static final int TYPE_EXPENSE = 0;
    public static final int TYPE_TASK = TYPE_EXPENSE + 1;
    private static final String DEBUG_TAG = "ReportExportUtils: ";

    public static File getFileNameByTime(Context context, int type, Client client) {
        File externalFilesDir = context.getExternalFilesDir(null);

        if (externalFilesDir == null) {
            return null;
        }

        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy_MM_dd_HHmm");
        String prefix = type == TYPE_EXPENSE ? EXPENSE_PREFIX : TASK_PREFIX;
        if (client != null) {
            prefix += client.toString() + "_";
        }
        String str = prefix + fmt.print(today) + EXTENSION;

        File file = new File(externalFilesDir, str);

        int cnt = 0;

        while (cnt <= MAX_COUNT && file.exists()) {
            ++cnt;
            file = new File(externalFilesDir, str + "_" + cnt);
        }

        if (cnt == MAX_COUNT + 1) {
            Log.i(DEBUG_TAG, "report file name error");
        }

        Log.i("......", "Path: " + file.getPath());
        return file;
    }



}
