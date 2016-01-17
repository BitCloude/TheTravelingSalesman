package com.appers.ayvaz.thetravelingsalesman.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.appers.ayvaz.thetravelingsalesman.ClientActivity;
import com.appers.ayvaz.thetravelingsalesman.R;
import com.appers.ayvaz.thetravelingsalesman.models.Client;

import org.w3c.dom.Text;

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
        holder.setView(mClients.get(position));
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
        public ImageButton mLinkedIn;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mName = (TextView) view.findViewById(R.id.client_name);
            mCompanyPhone = (TextView) view.findViewById(R.id.client_company);
            mEmail = (TextView) view.findViewById(R.id.client_email);
            mStar = (ImageButton) view.findViewById(R.id.favorite);
            mLinkedIn = (ImageButton) view.findViewById(R.id.linkedInButt);
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

        public void setView(Client item) {
            mItem = item;
            mName.setText(mItem.toString());
            mCompanyPhone.setText(mItem.getSecondRow());
            mEmail.setText(mItem.getEmail());
            mStar.setImageResource(mItem.isStared() ? R.drawable.ic_star_yellow_500_24dp :
                    R.drawable.ic_star_outline_grey_500_18dp);
            if (TextUtils.isEmpty(item.getLinkedIn())) {
                mLinkedIn.setVisibility(View.INVISIBLE);
            }
            mView.setOnClickListener(this);
        }
    }
}
