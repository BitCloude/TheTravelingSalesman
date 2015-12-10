package com.appers.ayvaz.thetravelingsalesman;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appers.ayvaz.thetravelingsalesman.ClientFragment.OnListFragmentInteractionListener;
import com.appers.ayvaz.thetravelingsalesman.Model.Client;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Client} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyClientRecyclerViewAdapter extends RecyclerView.Adapter<MyClientRecyclerViewAdapter.ViewHolder> {

    private final List<Client> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyClientRecyclerViewAdapter(List<Client> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_client_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mName.setText(holder.mItem.getFirstName() + holder.mItem.getLastName());
        holder.mCompanyPhone.setText(mValues.get(position).getCompany());
        holder.mEmail.setText(holder.mItem.getEmail());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mName;
        public final TextView mCompanyPhone;
        public final TextView mEmail;
        public Client mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mName = (TextView) view.findViewById(R.id.client_name);
            mCompanyPhone = (TextView) view.findViewById(R.id.client_company);
            mEmail = (TextView) view.findViewById(R.id.client_email);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mName.getText() + "'";
        }
    }
}
