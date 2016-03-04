package com.simbiosyscorp.thetravelingsalesman.adapter;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simbiosyscorp.thetravelingsalesman.R;
import com.simbiosyscorp.thetravelingsalesman.models.Client;
import com.simbiosyscorp.thetravelingsalesman.models.Task;
import com.simbiosyscorp.thetravelingsalesman.models.TaskManager;
import com.simbiosyscorp.thetravelingsalesman.ui.TaskListActivity;
import com.simbiosyscorp.thetravelingsalesman.utils.DateTimeHelper;

import org.joda.time.LocalDate;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private final String DEBUG_TAG = "TaskAdapter: ";

    private int mSelected = -1;
    private boolean withYear = false;
    private Context mContext;

    List<Task> mTasks;


    public boolean delete(int position) {
        Log.i(DEBUG_TAG, "Selected: " + position);

        if (position < 0 || position > mTasks.size()) {
            return false;
        }

        boolean result = TaskManager.get(mContext).delete(mTasks.get(position).getEventID());
        if (result) {
            mTasks.remove(position);
            notifyItemChanged(position);
        }

        return result;
    }

    public void clearData() {
        for (int i = 0; i < mTasks.size(); i++) {
            mTasks.remove(i);
        }
    }




    public TaskAdapter(List<Task> taskList, Context context) {
        mTasks = taskList;
        mContext = context;
        sort();
    }

    public void setShowYear(boolean b) {
        withYear = b;
    }
    public void setData(List<Task> list) {
        mTasks = list;
        sort();
    }

    private void sort() {
        Collections.sort(mTasks, Collections.reverseOrder(new Task.DateTimeComparator()));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_task_item, parent, false);

        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task task = mTasks.get(position);
        holder.setTask(task);


        if (!withYear) {
            return;
        }

        if (position == 0 ||
                !DateTimeHelper.isSameYear(mTasks.get(position - 1).getStartTime(),
                        task.getStartTime())) {
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



    public boolean setClient(int position, Client client) {

        if (position < 0 || position > mTasks.size()) {
            return false;
        }

        Task task = mTasks.get(position);
        task.setClient(client);

        return TaskManager.get(mContext).updateTask(task);


    }

    public int getSelected() {
        return mSelected;
    }

    public UUID getSelectedClient() {
        return mTasks.get(mSelected).getClientID();
    }

    // view holder for client view
    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener, View.OnClickListener{
        @Bind(R.id.task_title)        TextView taskTitle;
        @Bind(R.id.task_detail)        TextView taskDetail;
        @Bind(R.id.from_date)        TextView fromDate;
        @Bind(R.id.to_date)        TextView toDate;
        @Bind(R.id.clientName) TextView clientName;
        @Bind(R.id.buttonAttendee)        ImageView buttonAttendee;
        @Bind(R.id.buttonReminder)        ImageView buttonReminder;
        @Bind(R.id.buttonExtra)        ImageView buttonExtra;

        @Bind(R.id.yearChanged)
        LinearLayout yearChanged;
        @Bind(R.id.year)            TextView year;

        private Task mTask;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

        }



        void setTask(Task task) {
            mTask = task;
            setView();

        }


        @Override
        public void onClick(View v) {

            Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, mTask.getEventID());
            Calendar start = Calendar.getInstance();
            start.setTime(mTask.getStartTime());
            long startMillis = start.getTimeInMillis();


            Intent intent = new Intent(Intent.ACTION_VIEW)
                    .setData(uri)
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis);

            if (mTask.getEndTime() != null) {
                Calendar end = Calendar.getInstance();
                end.setTime(mTask.getEndTime());
                long endMillis = end.getTimeInMillis();
                intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis);
            }


            v.getContext().startActivity(intent);
        }

        public void setView() {

            fromDate.setText(DateTimeHelper.formatShortDate(mTask.getStartTime()));
            if (mTask.getEndTime() == null) {
                toDate.setText("");
            } else {
                toDate.setText(DateTimeHelper.formatShortDate(mTask.getEndTime()));
            }


            if (!mTask.hasAlarm()) {
                buttonReminder.setImageDrawable(null);
            }

            if (!mTask.hasAttendee()) {
                buttonAttendee.setImageDrawable(null);
            }

            if (!mTask.hasNotes()) {
                buttonExtra.setImageDrawable(null);
            }
//            taskDetail.setText(mTask.getNotes());
            setTitleDetail();


        }

        protected void setTitleDetail() {
            Client client = mTask.getClient();
//            String title = (client == null ? "" : client.toString() + ": ") + mTask.getTitle();
//            taskTitle.setText(title);
            taskDetail.setText(mTask.getLocation());
            taskTitle.setText(mTask.getTitle());
            if (client != null) {
                clientName.setText(client.toString());
                clientName.setVisibility(View.VISIBLE);
            } else {
                clientName.setVisibility(View.GONE);
            }
        }

        public void showYear() {
            int y = new LocalDate(mTask.getStartTime()).getYear();
            year.setText(y+"");
            yearChanged.setVisibility(View.VISIBLE);
        }

        public void hideYear() {
            yearChanged.setVisibility(View.GONE);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            if (v.getContext() instanceof TaskListActivity) {
                ((Activity) v.getContext()).getMenuInflater()
                        .inflate(R.menu.menu_task_context, menu);


                menu.findItem(R.id.action_view_client).setVisible(mTask.getClient() != null);

                menu.setHeaderTitle(mTask.getTitle());


            } else {
                ((Activity) v.getContext()).getMenuInflater()
                        .inflate(R.menu.menu_client_task_context, menu);

                menu.setHeaderTitle(mTask.getTitle());
            }

            mSelected = getAdapterPosition();
            Log.i(DEBUG_TAG, "selected: " + mSelected);

        }





}


}



