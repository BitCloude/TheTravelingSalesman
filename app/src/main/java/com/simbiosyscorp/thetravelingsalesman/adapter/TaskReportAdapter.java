package com.simbiosyscorp.thetravelingsalesman.adapter;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.simbiosyscorp.thetravelingsalesman.R;
import com.simbiosyscorp.thetravelingsalesman.models.Client;
import com.simbiosyscorp.thetravelingsalesman.models.Task;
import com.simbiosyscorp.thetravelingsalesman.ui.ReportTaskActivity;
import com.simbiosyscorp.thetravelingsalesman.utils.DateTimeHelper;
import com.simbiosyscorp.thetravelingsalesman.utils.MyCsvWriter;
import com.simbiosyscorp.thetravelingsalesman.utils.ReportExportUtils;

import org.joda.time.LocalDate;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TaskReportAdapter extends RecyclerView.Adapter<TaskReportAdapter.ViewHolder> {

    public final static int SORT_BY_TITLE = 0;
    public final static int SORT_BY_DATETIME = 1;
    public final static int SORT_BY_TIME = 2;
    private final static int TYPE_CLIENT = 0;
    private final static int TYPE_DATE = 1;
    private final String DEBUG_TAG = "TaskReportAdapter: ";
    List<Task> mTasks;
    private int mSortMode;
    private int[] mOrders = new int[3];
    private Comparator[] comparators = {new Task.TitleComparator(), new Task.DateTimeComparator(),
            new Task.TimeComparator()};
    private Context mContext;

    public TaskReportAdapter(List<Task> taskList, Context context) {
        mTasks = taskList;
        mSortMode = SORT_BY_DATETIME;
        mContext = context;
        sort(mSortMode, false);

    }

    public void clearOrders() {

        for (int i = 0; i < mOrders.length; i++) {
            mOrders[i] = 0;
        }
    }

    public int[] getOrders() {
        return mOrders.clone();
    }

    public void setData(List<Task> list) {
        mTasks = list;
        sort(mSortMode, false);
    }

    public void sort(int sortMode, boolean asc) {
        mSortMode = sortMode;

        for (int i = 0; i < 3; i++) {
            if (sortMode == i) {
                mOrders[i] = asc ? 1 : 2;
            } else {
                mOrders[i] = 0;
            }
        }

        Comparator<Task> comparator = comparators[sortMode];

        if (asc) {
            Collections.sort(mTasks, comparator);
        } else {
            Collections.sort(mTasks, Collections.reverseOrder(comparator));
        }

        notifyDataSetChanged();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_report_task_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task task = mTasks.get(position);
        holder.setTask(task);


        if (position == 0 ||
                !DateTimeHelper.isSameYear(mTasks.get(position - 1).getStartTime(),
                        task.getStartTime())) {
//            int year1 = new LocalDate(task.getStartTime()).getYear();
//
//            Log.i(DEBUG_TAG, "This: " + year1);
//
//            if (position > 0) {
//                int year2 = new LocalDate(mTasks.get(position - 1)).getYear();
//                Log.i(DEBUG_TAG, "Previous: " + year2);
//            } 
            // if not same year, add year divider

            holder.showYear();
        } else {
            holder.hideYear();
        }
    }


    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    public void saveReport(Client client) {
        if (mTasks.size() == 0) {
            Toast.makeText(mContext, R.string.report_nothing_to_save, Toast.LENGTH_SHORT).show();
            return;
        }

        final File file = ReportExportUtils.getFileNameByTime(mContext,
                ReportExportUtils.TYPE_TASK, client);

        if (file == null) {
            Log.i(DEBUG_TAG, "FILE NULL");
            return;
        }

        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                .setMessage(mContext.getString(R.string.save_report, file.getPath()))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new GenerateTaskReport().execute(file);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        alertDialog.show();

    }



    private class GenerateTaskReport extends AsyncTask<File, Integer, Boolean> {

        private ProgressDialog progressDialog;
        private File mFile;

        @Override
        protected void onPreExecute() {
            if (mTasks.size() > 40) {
                progressDialog = new ProgressDialog(mContext);
                progressDialog.setTitle(R.string.saving);
                progressDialog.setMax(mTasks.size());
                progressDialog.show();
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (progressDialog != null) {
                progressDialog.setProgress(values[0]);
            }

        }

        @Override
        protected Boolean doInBackground(File... params) {
            final String DIVIDER = ", ";
            final String NEW_LINE = "\n";
            if (params == null || params.length != 1) {
                Log.i(DEBUG_TAG, "file path not included");
                return null;
            }

            mFile = params[0];

            try (MyCsvWriter writer = new MyCsvWriter(params[0])) {
                String[] header = {
                        mContext.getString(R.string.client_name),
                        mContext.getString(R.string.task_title),
                        mContext.getString(R.string.location),
                        mContext.getString(R.string.start_time),
                        mContext.getString(R.string.end_time),
                        mContext.getString(R.string.description)
                };

                writer.addRow(header);

                String[] row = new String[header.length];

                for (int i = 0; i < mTasks.size(); i++) {
                    publishProgress(i);
                    Task t = mTasks.get(i);
                    if (t.getClient() != null) {
                        row[0] = t.getClient().toString();
                    } else {
                        row[0] = "";
                    }

                    row[1] = t.getTitle();
                    row[2] = t.getLocation();
                    row[3] = DateTimeHelper.getFullTime(t.getStartTime());
                    row[4] = DateTimeHelper.getFullTime(t.getEndTime());
                    row[5] = t.getNotes();

                    writer.addRow(row);
                }

                writer.flush();
                writer.close();

                return true;

            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            if (progressDialog != null) {
                progressDialog.dismiss();
            }

            int id = 10;

//            Toast.makeText(mContext, R.string.report_saved, Toast.LENGTH_LONG).show();


            if (result) {
                NotificationManager mNotificationManager = (NotificationManager) mContext
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);

                mBuilder.setSmallIcon(R.drawable.ic_save)
                        .setContentTitle(mContext.getString(R.string.message_finish_export))
                        .setContentText(mFile.getPath())
                        .setContentInfo("Open the report")
                        .setAutoCancel(true);

                Intent resultIntent = ReportExportUtils.getOpenIntent(mFile, mContext);
                if (resultIntent == null) {
                    return;
                }

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
                stackBuilder.addParentStack(ReportTaskActivity.class);
                stackBuilder.addNextIntent(resultIntent);


                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );

                mBuilder.setContentIntent(resultPendingIntent);
                mNotificationManager.notify(id, mBuilder.build());
            } else {
                Toast.makeText(mContext, R.string.message_export_failed, Toast.LENGTH_SHORT)
                        .show();
            }


        }
    }

    // view holder for client view
    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.titleHeader)
        TextView taskTitle;
        @Bind(R.id.taskDate)
        TextView taskDate;
        @Bind(R.id.taskTime)
        TextView taskTime;
        @Bind(R.id.yearChanged)
        LinearLayout yearChanged;
        @Bind(R.id.year)
        TextView year;


        private Task mTask;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }

        void setTask(Task task) {
            mTask = task;
            setView();

        }


        public void setView() {

            taskTitle.setText(mTask.getTitle());
            taskDate.setText(DateTimeHelper.formatShortDate(mTask.getStartTime()));
            taskTime.setText(DateTimeHelper.formatTime(mTask.getStartTime()));

        }

        public void showYear() {
            int y = new LocalDate(mTask.getStartTime()).getYear();
            year.setText(Integer.toString(y));
            yearChanged.setVisibility(View.VISIBLE);
        }

        public void hideYear() {
            yearChanged.setVisibility(View.GONE);
        }


    }


}

