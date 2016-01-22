package com.appers.ayvaz.thetravelingsalesman.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by D on 011 01/11.
 */
public class TaskCursorAdapter extends CursorRecyclerViewAdapter<TaskCursorAdapter.ViewHolder> {

    private final static int TYPE_CLIENT = 0;
    private final static int TYPE_DATE = 1;
    List<Task> list;

    public TaskCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_task_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        // // TODO: 011 01/11 to something with cursor
        Task task = Task.fromCursor(cursor);
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

        @Bind(R.id.task_title)
        TextView taskTitle;
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
            Intent intent = new Intent(Intent.ACTION_VIEW)
                    .setData(uri);
            v.getContext().startActivity(intent);
        }

        public void setView() {
//                taskClient.setText(mTask.getClient().toString());
            fromDate.setText(DateTimeHelper.formatMed(mTask.getStartTime()));
            toDate.setText(DateTimeHelper.formatMed(mTask.getEndTime()));
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
