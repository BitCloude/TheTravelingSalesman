package com.appers.ayvaz.thetravelingsalesman.adapter;

import android.content.Context;
import android.content.Intent;


import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;


import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.appers.ayvaz.thetravelingsalesman.ClientActivity;
import com.appers.ayvaz.thetravelingsalesman.ClientEditActivity;
import com.appers.ayvaz.thetravelingsalesman.R;
import com.appers.ayvaz.thetravelingsalesman.models.Client;
import com.appers.ayvaz.thetravelingsalesman.models.ClientManager;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Client}
 *
 */
public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ViewHolder> {

    List<Client> mClients;

/** For action mode **/
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
        UUID uuid = mClients.get(position).getId();
        mClients.remove(position);
        return  ClientManager.get(context).delete(uuid);
    }

    public void selectAll() {
        for (int i = 0; i < getItemCount(); i++) {
            selectedItems.put(i, true);
        }
        notifyDataSetChanged();
    }

    /** End of action mode **/

    public ClientAdapter(List<Client> items) {
        mClients = items;

    }

    public void setClients(List<Client> clients) {
        mClients = clients;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_client_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.setView(mClients.get(position));
        holder.mItemView.setActivated(selectedItems.get(position, false));
    }

    @Override
    public int getItemCount() {
        return mClients.size();
    }

    public void editSelected(Context context) {
        if (getSelectedItemCount() != 1) {
            return;
        }

        for (int i = 0; i < getItemCount(); i++) {
            if (selectedItems.get(i)) {
                UUID id = mClients.get(i).getId();
                Intent intent = ClientEditActivity.newIntent(context, id);
                context.startActivity(intent);
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder
     {

        public final View mItemView;
        public final TextView mName;
        public final TextView mCompanyPhone;
        public final TextView mEmail;
        public Client mItem;
        public final ImageButton mStar;
        public ImageButton mLinkedIn;


        public ViewHolder(View view) {
            super(view);
            mItemView = view;
            mName = (TextView) view.findViewById(R.id.client_name);
            mCompanyPhone = (TextView) view.findViewById(R.id.client_company);
            mEmail = (TextView) view.findViewById(R.id.client_email);
            mStar = (ImageButton) view.findViewById(R.id.favorite);
            mLinkedIn = (ImageButton) view.findViewById(R.id.linkedInButt);

//            mItemView.setOnClickListener(this);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mName.getText() + "'";
        }



        public void onClick(View v) {
//            Intent intent = ClientEditActivity.newIntent(v.getContext(), mItem.getId());

                // start an instance of CrimePagerActivity
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




        }



    }


}
