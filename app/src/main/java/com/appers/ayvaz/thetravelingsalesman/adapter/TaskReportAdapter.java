package com.appers.ayvaz.thetravelingsalesman.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appers.ayvaz.thetravelingsalesman.R;
import com.appers.ayvaz.thetravelingsalesman.models.Client;
import com.appers.ayvaz.thetravelingsalesman.models.Task;
import com.appers.ayvaz.thetravelingsalesman.utils.DateTimeHelper;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TaskReportAdapter extends RecyclerView.Adapter<TaskReportAdapter.ViewHolder> {

    private final String DEBUG_TAG = "TaskReportAdapter: ";
    private final static int TYPE_CLIENT = 0;
    private final static int TYPE_DATE = 1;

    public final static int SORT_BY_TITLE = 0;
    public final static int SORT_BY_DATETIME = 1;
    public final static int SORT_BY_TIME = 2;

    List<Task> mTasks;
    private int mSortMode;
    private int[] mOrders = new int[3];
    private Comparator[] comparators = {new Task.TitleComparator(), new Task.DateTimeComparator(),
    new Task.TimeComparator()};

    public TaskReportAdapter(List<Task> taskList) {
        mTasks = taskList;
        mSortMode = SORT_BY_DATETIME;
        sort(mSortMode, false);

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


    // view holder for client view
    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.taskTitle)        TextView taskTitle;
        @Bind(R.id.taskDate)        TextView taskDate;
        @Bind(R.id.taskTime)        TextView taskTime;
        @Bind(R.id.yearChanged)        LinearLayout yearChanged;
        @Bind(R.id.year)            TextView year;


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

