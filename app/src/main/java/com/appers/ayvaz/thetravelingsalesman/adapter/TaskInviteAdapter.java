package com.appers.ayvaz.thetravelingsalesman.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by D on 12/05/2015.
 */
public class TaskInviteAdapter extends RecyclerView.Adapter<TaskInviteAdapter.ViewHolder> {
    String[] clientList;

    public TaskInviteAdapter() {
        clientList = new String[]{"Pete Cashmore", "John Gruber", "Joe Hewitt"};

    }

    @Override
    public int getItemCount() {
        return clientList.length;
    }

    @Override
    public TaskInviteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(clientList[position]);
    }



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(android.R.id.text1);
        }
    }
}
