package com.appers.ayvaz.thetravelingsalesman.view;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.appers.ayvaz.thetravelingsalesman.ClientActivity;
import com.appers.ayvaz.thetravelingsalesman.R;
import com.appers.ayvaz.thetravelingsalesman.models.Client;
import com.appers.ayvaz.thetravelingsalesman.models.ClientManager;

import java.util.List;

import butterknife.Bind;

public class ClientPickActivity extends AppCompatActivity {

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.searchView)
    SearchView mSearchView;
    private ClientPickAdapter mAdapter;
    public static final String EXTRA_CLIENT_ID = "extra_client_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_pick);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));

        List<Client> allClients = ClientManager.get(this).getClients();
        mAdapter = new ClientPickAdapter(allClients);
        mRecyclerView.setAdapter(mAdapter);
    }

    public class ClientPickAdapter extends RecyclerView.Adapter<ClientPickAdapter.ViewHolder> {

        List<Client> mClients;


        public ClientPickAdapter(List<Client> items) {
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
                Intent data = new Intent();
                data.putExtra(EXTRA_CLIENT_ID, mItem.getId());
                ((Activity) v.getContext()).setResult(RESULT_OK, data);
            }
        }
    }

}
