package com.appers.ayvaz.thetravelingsalesman.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.appers.ayvaz.thetravelingsalesman.ClientActivity;
import com.appers.ayvaz.thetravelingsalesman.R;
import com.appers.ayvaz.thetravelingsalesman.modell.Client;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Client}
 *
 */
public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ViewHolder> {

    List<Client> mClients;


    public ClientAdapter(List<Client> items) {
        mClients = items;
    }

    public void setClients(List<Client> clients) {
        mClients = clients;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_client_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mClients.get(position);
        holder.mName.setText(holder.mItem.getFirstName() + " "
                + holder.mItem.getLastName());
        holder.mCompanyPhone.setText(holder.mItem.getCompany() + " - "
                + holder.mItem.getFirstPhone());
        holder.mEmail.setText(holder.mItem.getEmail());
        holder.mStar.setImageResource(holder.mItem.isStared() ? R.drawable.ic_star_yellow_500_24dp :
                R.drawable.ic_star_outline_grey_500_18dp);
        holder.mView.setOnClickListener(holder);


    }

    @Override
    public int getItemCount() {
        return mClients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final View mView;
        public final TextView mName;
        public final TextView mCompanyPhone;
        public final TextView mEmail;
        public Client mItem;
        public final ImageButton mStar;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mName = (TextView) view.findViewById(R.id.client_name);
            mCompanyPhone = (TextView) view.findViewById(R.id.client_company);
            mEmail = (TextView) view.findViewById(R.id.client_email);
            mStar = (ImageButton) view.findViewById(R.id.favorite);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mName.getText() + "'";
        }


        @Override
        public void onClick(View v) {
//            Intent intent = ClientEditActivity.newIntent(v.getContext(), mItem.getId());
            Intent intent = ClientActivity.newIntent(v.getContext(), mItem.getId());
            v.getContext().startActivity(intent);
        }
    }
}
