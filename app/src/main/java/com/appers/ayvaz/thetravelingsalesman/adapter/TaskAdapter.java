package com.appers.ayvaz.thetravelingsalesman.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v7.widget.RecyclerView;
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private final static int TYPE_CLIENT = 0;
    private final static int TYPE_DATE = 1;
    private boolean withYear = false;
    List<Task> mTasks;



    /** Start action mode **/
    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    public void toggleSelection(int pos) {
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        }
        else {
            selectedItems.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public void selectAll() {
        for (int i = 0; i < getItemCount(); i++) {
            selectedItems.put(i, true);
        }
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items =
                new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    public boolean removeData(int position, Context context) {
        return false;
    }

    /** End of action mode **/


    public TaskAdapter(List<Task> taskList) {
        mTasks = taskList;
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
        holder.itemView.setActivated(selectedItems.get(position, false));

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
    public int getItemViewType(int position) {
        if (mTasks.get(position).getClient() == null) {
            return TYPE_CLIENT;
        }
        return TYPE_DATE;
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }


    // view holder for client view
    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.task_title)        TextView taskTitle;
        @Bind(R.id.task_detail)        TextView taskDetail;
        @Bind(R.id.from_date)        TextView fromDate;
        @Bind(R.id.to_date)        TextView toDate;
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
//            itemView.setOnClickListener(this);
//            itemView.setOnCreateContextMenuListener(this)

        }

        void setTask(Task task) {
            mTask = task;
            setView();

        }


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
            String title = (client == null ? "" : client.toString() + ": ") + mTask.getTitle();
            taskTitle.setText(title);
            taskDetail.setText(mTask.getLocation());
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

