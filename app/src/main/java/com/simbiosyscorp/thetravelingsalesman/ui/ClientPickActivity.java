package com.simbiosyscorp.thetravelingsalesman.ui;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SearchView;

import com.simbiosyscorp.thetravelingsalesman.R;
import com.simbiosyscorp.thetravelingsalesman.adapter.ClientSearchAdapter;
import com.simbiosyscorp.thetravelingsalesman.models.Client;
import com.simbiosyscorp.thetravelingsalesman.models.ClientManager;
import com.simbiosyscorp.thetravelingsalesman.view.DividerItemDecoration;

import java.util.List;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ClientPickActivity extends BaseActivity
        implements ClientSearchAdapter.OnClientPickListener {

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.searchView)
    SearchView mSearchView;
    @Bind(R.id.progressBarContainer)
    FrameLayout mProgressBarContainer;

    private ClientSearchAdapter mAdapter;
    public static final String EXTRA_CLIENT_ID = "extra_client_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_pick);

        ButterKnife.bind(this);

        setTitle(R.string.title_activity_client_pick);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                new GetClientsAsyncTask().execute(newText);
                return true;
            }
        });

        updateUI();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void updateUI() {
        new GetClientsAsyncTask().execute();
    }

    @Override
    public void onClientPick(UUID clientId) {
        Intent data = new Intent();
        data.putExtra(EXTRA_CLIENT_ID, clientId.toString());
        setResult(RESULT_OK, data);
        finish();
    }


    private class GetClientsAsyncTask extends AsyncTask<String, Void, List<Client>> {

        @Override
        protected List<Client> doInBackground(String... params) {
            ClientManager clientManager = ClientManager.get(ClientPickActivity.this);
            if (params != null && params.length > 0) {
                String query = params[0];
                return clientManager.getSearchResult(query);
            }

            return clientManager.getClients();

        }

        @Override
        protected void onPreExecute() {
            mProgressBarContainer.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(List<Client> clients) {
            if (mAdapter == null) {
                mAdapter = new ClientSearchAdapter(clients, ClientPickActivity.this);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                mAdapter.setClients(clients);
                mAdapter.notifyDataSetChanged();
            }

            mProgressBarContainer.setVisibility(View.GONE);

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
