package com.appers.ayvaz.thetravelingsalesman.adapter;

import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appers.ayvaz.thetravelingsalesman.R;
import com.appers.ayvaz.thetravelingsalesman.models.Task;
import com.appers.ayvaz.thetravelingsalesman.utils.DateTimeHelper;

import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private final static int TYPE_CLIENT = 0;
    private final static int TYPE_DATE = 1;
    List<Task> list;
    public TaskAdapter(List<Task> taskList) {
        list = taskList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_task_item, parent, false);
/*            if (viewType == TYPE_DATE) {
                return new ViewHolderDate(view);
            }*/
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task task = list.get(position);
        holder.setTask(task);

    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getClient() == null) {
            return TYPE_CLIENT;
        }
        return TYPE_DATE;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    // view holder for client view
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Task mTask;

        void setTask(Task task) {
            mTask = task;
            setView();
        }

        @Bind(R.id.task_title) TextView taskTitle;
        @Bind(R.id.task_detail) TextView taskDetail;
        @Bind(R.id.from_date) TextView fromDate;
        @Bind(R.id.to_date) TextView toDate;
        @Bind(R.id.buttonAttendee)ImageView buttonAttendee;
        @Bind(R.id.buttonReminder) ImageView buttonReminder;
        @Bind(R.id.buttonExtra) ImageView buttonExtra;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, mTask.getId());
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
//                taskClient.setText(mTask.getClient().toString());
            fromDate.setText(DateTimeHelper.formatMed(mTask.getStartTime()));
            if (mTask.getEndTime() == null) {
                toDate.setText("");
            } else {
                toDate.setText(DateTimeHelper.formatMed(mTask.getEndTime()));
            }



            taskTitle.setText(mTask.getTitle());

            if (!mTask.hasAlarm()) {
                buttonReminder.setImageDrawable(null);
            }

            if (!mTask.hasAttendee()) {
                buttonAttendee.setImageDrawable(null);
            }

            if (!mTask.hasNotes()) {
                buttonExtra.setImageDrawable(null);
            }
            taskDetail.setText(mTask.getNotes());



        }
    }


}

