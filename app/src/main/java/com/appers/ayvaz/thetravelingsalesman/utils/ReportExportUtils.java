package com.appers.ayvaz.thetravelingsalesman.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.appers.ayvaz.thetravelingsalesman.R;
import com.appers.ayvaz.thetravelingsalesman.models.Client;
import com.appers.ayvaz.thetravelingsalesman.models.Expense;
import com.appers.ayvaz.thetravelingsalesman.models.ExpenseReport;
import com.appers.ayvaz.thetravelingsalesman.models.Task;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

/**
 * Created by D on 027 01 27.
 */
public class ReportExportUtils {
    // max possible name confliction
    private static final int MAX_COUNT = 60;
    public static final String TASK_PREFIX = "task_";
    public static final String EXPENSE_PREFIX = "expense_";
    private static final String EXTENSION = ".csv";
    public static final int TYPE_EXPENSE = 0;
    public static final int TYPE_TASK = TYPE_EXPENSE + 1;
    private static final String DEBUG_TAG = "ReportExportUtils: ";

    public static File getReportDir(Context context) {
        File externalFilesDir = context.getExternalFilesDir(null);

        if (externalFilesDir == null) {
            return null;
        }

        File reportFolder = new File(externalFilesDir, "reports");

        return reportFolder;
    }

    public static File getFileNameByTime(Context context, int type, Client client) {

        File reportFolder = getReportDir(context);

        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy_MM_dd_HHmmss");
        String prefix = type == TYPE_EXPENSE ? EXPENSE_PREFIX : TASK_PREFIX;


        if (client != null) {
            prefix += client.toString() + "_";
        }

        String str = prefix + fmt.print(today);

        File file = new File(reportFolder, str + EXTENSION);

        if (file.exists()) {
            Log.i(DEBUG_TAG, "overwriting");
        }

        Log.i("......", "Path: " + file.getPath());
        return file;
    }

    public static String formatMoney(double value) {
        String pattern = "###,###.##";
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        return  myFormatter.format(value);
    }

    public static String formatMoneyNumberOnly(double value) {
        NumberFormat formatter = new DecimalFormat("#0.00");
        return formatter.format(value);
    }

    public static Intent getOpenIntent(File file, Context context) {
        if (!file.exists()) {
            return null;
        }

        Uri fileUri = Uri.fromFile(file);
        Intent viewIntent = new Intent(Intent.ACTION_VIEW);
        if (fileUri != null) {
            viewIntent.setDataAndType(fileUri, "text/csv");
            if (viewIntent.resolveActivity(context.getPackageManager()) != null) {
                String title = context.getResources().getString(R.string.open_title);
                return Intent.createChooser(viewIntent, title);
            } else {
                return null;
            }
        } else {
            Log.i(DEBUG_TAG, "null Uri");
            return null;
        }

    }

    public static void shareFile(Context context, File file) {

        if (!file.exists()) {
            return;
        }

        Uri fileUri = Uri.fromFile(file);
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        if (fileUri != null) {

            // Put the Uri and MIME type in the result Intent
            sendIntent.setType(

                    "text/csv"
                    //MimeTypeMap.getSingleton().getMimeTypeFromExtension("csv")
            );

            sendIntent.putExtra(android.content.Intent.EXTRA_EMAIL, "");
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, "");
            sendIntent.putExtra(Intent.EXTRA_STREAM, fileUri);


            // Verify the original intent will resolve to at least one activity
            if (sendIntent.resolveActivity(context.getPackageManager()) != null) {
                String title = context.getResources().getString(R.string.chooser_title);
                Intent chooser = Intent.createChooser(sendIntent, title);
                context.startActivity(chooser);
            }
        } else {
            sendIntent.setDataAndType(null, "");

        }

    }

    public static void openReportFolder(Context context) {
        Intent intent = new Intent();
        intent.setDataAndType(Uri.fromFile(getReportDir(context)),
                "resource/folder");

        if (intent.resolveActivityInfo(context.getPackageManager(), 0) == null) {
            Toast.makeText(context, R.string.message_no_file_explorer, Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        context.startActivity(intent);
    }



}
