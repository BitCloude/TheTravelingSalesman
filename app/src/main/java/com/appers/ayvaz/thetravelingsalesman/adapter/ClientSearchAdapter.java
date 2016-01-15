package com.appers.ayvaz.thetravelingsalesman.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appers.ayvaz.thetravelingsalesman.ClientActivity;
import com.appers.ayvaz.thetravelingsalesman.R;
import com.appers.ayvaz.thetravelingsalesman.models.Client;

import java.util.List;


public class ClientSearchAdapter extends RecyclerView.Adapter<ClientSearchAdapter.ViewHolder> {

    List<Client> mClients;


    public ClientSearchAdapter(List<Client> items) {
        mClients = items;
    }

    public void setClients(List<Client> clients) {
        mClients = clients;
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
        holder.mName.setText(holder.mItem.toString());
        holder.mView.setOnClickListener(holder);
    }

    @Override
    public int getItemCount() {
        return mClients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final TextView mName;
        public Client mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mName = (TextView) view.findViewById(R.id.client_name);
        }


        @Override
        public void onClick(View v) {
            Intent intent = ClientActivity.newIntent(v.getContext(), mItem.getId());
            v.getContext().startActivity(intent);
        }
    }
}
