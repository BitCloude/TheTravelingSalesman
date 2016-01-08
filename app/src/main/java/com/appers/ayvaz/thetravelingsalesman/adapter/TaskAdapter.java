package com.appers.ayvaz.thetravelingsalesman.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appers.ayvaz.thetravelingsalesman.R;
import com.appers.ayvaz.thetravelingsalesman.TaskListFragment;
import com.appers.ayvaz.thetravelingsalesman.TaskPagerActivity;
import com.appers.ayvaz.thetravelingsalesman.model.Task;
import com.appers.ayvaz.thetravelingsalesman.utils.DateTimeHelper;

import java.util.List;

/**
 * Created by D on 12/15/2015.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private List<Task> mTasks;
    private TaskListFragment mFragment;

    public TaskAdapter(List<Task> tasks, TaskListFragment fragment) {
        mFragment = fragment;
        mTasks = tasks;
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_task_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskAdapter.ViewHolder holder, int position) {
        holder.mItem = mTasks.get(position);
        holder.mName.setText(holder.mItem.getName());
        holder.mNote.setText(holder.mItem.getNote());

        holder.mToDate.setText(DateTimeHelper.formatMed(holder.mItem.getEndTime().getTime()));
        holder.mFromDate.setText(DateTimeHelper.formatMed(holder.mItem.getBeginTime().getTime()));


    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
    View.OnClickListener{
        public final View mView;
        public final TextView mName;
        public final TextView mFromDate;
        public final TextView mToDate;
        public final TextView mNote;
        public Task mItem;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            mView = view;
            mName = (TextView) view.findViewById(R.id.task_client);
            mFromDate = (TextView) view.findViewById(R.id.from_date);
            mToDate = (TextView) view.findViewById(R.id.to_date);
            mNote = (TextView) view.findViewById(R.id.task_detail);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mName.getText() + "'";
        }

        @Override
        public void onClick(View v) {
            Intent intent = TaskPagerActivity.newIntent(mFragment.getActivity(), mItem.getId());
            mFragment.mPosition = getAdapterPosition();
            mFragment.startActivity(intent);
        }
    }
}
