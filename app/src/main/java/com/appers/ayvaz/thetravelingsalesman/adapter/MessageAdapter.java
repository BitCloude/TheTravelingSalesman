package com.appers.ayvaz.thetravelingsalesman.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appers.ayvaz.thetravelingsalesman.R;
import com.appers.ayvaz.thetravelingsalesman.Model.MyMessage;

/**
 * Created by D on 030 12/30.
 */
public class MessageAdapter extends
        CursorRecyclerViewAdapter<MessageAdapter.ViewHolder> {

    public MessageAdapter(Context context, Cursor cursor){
        super(context, cursor);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        //// TODO: 030 12/30 set other field 
        MyMessage myListItem = MyMessage.fromCursor(cursor, 0);
        viewHolder.mBody.setText(myListItem.getBody());
        viewHolder.mTime.setText(myListItem.getTime().toString());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sms_item_sent, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mBody ;
       public TextView mTime ;
        public ViewHolder(View view) {
            super(view);
            mBody = (TextView) view.findViewById(R.id.body);
            mTime = (TextView) view.findViewById(R.id.time);
        }
    }




}