package com.appers.ayvaz.thetravelingsalesman.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appers.ayvaz.thetravelingsalesman.R;
import com.appers.ayvaz.thetravelingsalesman.models.Client;

import java.util.List;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ClientSearchAdapter extends RecyclerView.Adapter<ClientSearchAdapter.ViewHolder> {

    private List<Client> mClients;
    private OnClientPickListener mListener;


    public ClientSearchAdapter(List<Client> items, OnClientPickListener listener) {
        mClients = items;
        mListener = listener;
    }



    public void setClients(List<Client> clients) {
        mClients = clients;
    }

    public interface OnClientPickListener {
        void onClientPick(UUID clientId);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_client_search_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mClients.get(position);
        holder.setText();

    }

    @Override
    public int getItemCount() {
        return mClients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {



        public Client mItem;
        @Bind(R.id.client_name) TextView mName;
        @Bind(R.id.client_phone_first) TextView mPhoneFirst;
        @Bind(R.id.client_phone_second) TextView mPhoneSecond;
        @Bind(R.id.client_email) TextView mEmail;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            mListener.onClientPick(mItem.getId());

//            Intent intent = ClientActivity.newIntent(v.getContext(), mItem.getEventID());
//            v.getContext().startActivity(intent);
        }

        public void setText() {
            mName.setText(mItem.getFullName());
            checkAndSet(mItem.getFirstPhone(), mPhoneFirst);
            checkAndSet(mItem.getSecondPhone(), mPhoneSecond);
            checkAndSet(mItem.getEmail(), mEmail);

        }

        private void checkAndSet(String s, TextView textView) {
            if (TextUtils.isEmpty(s)) {
                textView.setVisibility(View.GONE);
            } else {
                textView.setVisibility(View.VISIBLE);
                textView.setText(s);
            }
        }
    }
}
