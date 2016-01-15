package com.appers.ayvaz.thetravelingsalesman;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.appers.ayvaz.thetravelingsalesman.adapter.CursorRecyclerViewAdapter;
import com.appers.ayvaz.thetravelingsalesman.models.Client;
import com.appers.ayvaz.thetravelingsalesman.models.ClientManager;
import com.appers.ayvaz.thetravelingsalesman.view.DividerItemDecoration;

import java.util.List;

import butterknife.Bind;

public class ClientSearchActivity extends AppCompatActivity {

    @Bind(R.id.searchResult)
    RecyclerView mRecyclerView;
    ClientAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_search);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            List<Client> result = ClientManager.get(this).getSearchResult(query);
            mAdapter = new ClientAdapter(result);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.addItemDecoration(new DividerItemDecoration(ClientSearchActivity.this,
                    DividerItemDecoration.VERTICAL_LIST));
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setAdapter(mAdapter);

            //use the query to search your data somehow
        }
    }

    private class ClientSearchAdapter extends CursorRecyclerViewAdapter<ClientSearchAdapter.ViewHolder> {

        public ClientSearchAdapter(Context context, Cursor cursor) {
            super(context, cursor);
        }

        @Override
        public void onBindViewHolder(ClientSearchAdapter.ViewHolder viewHolder, Cursor cursor) {

        }

        @Override
        public ClientSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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

    private class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ViewHolder> {

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
}
