package com.appers.ayvaz.thetravelingsalesman;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.appers.ayvaz.thetravelingsalesman.adapter.TaskAdapter;
import com.appers.ayvaz.thetravelingsalesman.models.Task;
import com.appers.ayvaz.thetravelingsalesman.models.TaskManager;
import com.appers.ayvaz.thetravelingsalesman.view.DividerItemDecoration;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TaskSearchResultActivity extends AppCompatActivity {

    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;

    TaskAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list);

        ButterKnife.bind(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));

        handleIntent();

    }

    private void doMySearch(CharSequence query) {

        List<Task> result = TaskManager.get(this).search(query);

        if (mAdapter == null) {
            mAdapter = new TaskAdapter(result);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setData(result);
            mAdapter.notifyDataSetChanged();
        }



    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent();
    }

    private void handleIntent() {
        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }
}
