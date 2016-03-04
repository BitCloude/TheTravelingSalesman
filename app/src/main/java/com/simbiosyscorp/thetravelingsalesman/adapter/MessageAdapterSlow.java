package com.simbiosyscorp.thetravelingsalesman.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simbiosyscorp.thetravelingsalesman.R;
import com.simbiosyscorp.thetravelingsalesman.models.MyMessage;
import com.simbiosyscorp.thetravelingsalesman.utils.CommUtils;
import com.simbiosyscorp.thetravelingsalesman.utils.DateTimeHelper;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by D on 030 12/30.
 */
public class MessageAdapterSlow extends RecyclerView.Adapter<MessageAdapterSlow.ViewHolder> {
    List<MyMessage> messageList;
    Context mContext;

    public MessageAdapterSlow(Context context, List<MyMessage> messageList) {
        this.messageList = messageList;
        mContext = context;
    }

    public void setMessages(List<MyMessage> messages) {
        messageList = messages;
    }



    @Override
    public int getItemViewType(int position) {
        return messageList.get(position).getType();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int resId;
        switch (viewType) {
            case MyMessage.INBOX:
                resId = R.layout.sms_item_inbox;
                break;
            case MyMessage.SENT:
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
        MyMessage msg = messageList.get(position);
        holder.mItem = msg;
        holder.mBody.setText(msg.getBody());
        holder.mTime.setText(DateTimeHelper.formatMed(msg.getTime()));
        holder.mAddress.setText(msg.getAddress());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @Bind (R.id.body) TextView mBody;
        @Bind (R.id.address) TextView mAddress;
        @Bind(R.id.time) TextView mTime;
        MyMessage mItem;

    public ViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

        @Override
        public void onClick(View v) {
            CommUtils.sendText(v.getContext(), mItem.getAddress());
        }
    }

}
