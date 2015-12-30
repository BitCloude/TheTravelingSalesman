package com.appers.ayvaz.thetravelingsalesman.adapter;

import android.content.Context;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appers.ayvaz.thetravelingsalesman.DateTimeHelper;
import com.appers.ayvaz.thetravelingsalesman.R;
import com.appers.ayvaz.thetravelingsalesman.Model.SmsMms;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by D on 030 12/30.
 */
public class MessageAdapterSlow extends RecyclerView.Adapter<MessageAdapterSlow.ViewHolder> {
    List<SmsMms> messageList;
    Context mContext;

    public MessageAdapterSlow(Context context, List<SmsMms> messageList) {
        this.messageList = messageList;
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        return messageList.get(position).getType();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int resId;
        switch (viewType) {
            case SmsMms.INBOX:
                resId = R.layout.sms_item_inbox;
                break;
            case SmsMms.SENT:
                resId = R.layout.sms_item_sent;
                break;
            default:
                resId = R.layout.sms_item_sent;
        }
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(resId, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SmsMms msg = messageList.get(position);
        holder.mBody.setText(msg.getBody());
        holder.mTime.setText(DateTimeHelper.formatMed(msg.getTime()));
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind (R.id.body) TextView mBody;
        @Bind (R.id.time) TextView mTime;

    public ViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}

}
