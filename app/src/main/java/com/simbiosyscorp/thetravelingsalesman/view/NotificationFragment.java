package com.simbiosyscorp.thetravelingsalesman.view;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.simbiosyscorp.thetravelingsalesman.R;
import com.simbiosyscorp.thetravelingsalesman.adapter.TaskAdapter;
import com.simbiosyscorp.thetravelingsalesman.models.Client;
import com.simbiosyscorp.thetravelingsalesman.models.ClientManager;
import com.simbiosyscorp.thetravelingsalesman.models.Task;
import com.simbiosyscorp.thetravelingsalesman.models.TaskManager;

import java.util.List;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.emptyView) View mEmptyView;
    @Bind(R.id.emptyTextView)
    TextView mEmptyTextView;
    @Bind(R.id.addNew)
    Button mAddNew;


    private TaskAdapter mAdapter;
    private int mSelected;


    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        ButterKnife.bind(this, view);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAddNew.setVisibility(View.GONE);

        registerForContextMenu(mRecyclerView);

        updateUI();
        return view;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        mSelected = mAdapter.getSelected();

        switch (item.getItemId()) {
            case R.id.action_delete:
                alertDeletion();
                return true;

            case R.id.action_view_client:
                startActivity(ClientInfoActivity.newIntent(getActivity(),
                        mAdapter.getSelectedClient()));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void alertDeletion() {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setMessage(getString(R.string.r_u_sure, "Task"))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean result = mAdapter.delete(mSelected);

                        Toast.makeText(getActivity(),
                                getString(R.string.task_delete_result,
                                        result ? "" : "not"), Toast.LENGTH_SHORT)
                                .show();

                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        dialog.show();

    }




    private void updateUI() {
        new GetUpcomingTaskTask().execute();
    }

    private class GetUpcomingTaskTask extends AsyncTask<Void, Void, List<Task>> {

        @Override
        protected List<Task> doInBackground(Void... params) {
            return TaskManager.get(getActivity()).query(null);

        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(List<Task> tasks) {
            if (tasks.isEmpty()) {
                mEmptyView.setVisibility(View.VISIBLE);
                mEmptyTextView.setText(getActivity().getString(R.string.emptyList, "task"));
                /*mAddNew.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), LandingActivity.class);
                        startActivity(intent);
                    }
                });*/
            } else {
                mEmptyView.setVisibility(View.GONE);
            }


            if (mAdapter == null) {
                mAdapter = new TaskAdapter(tasks, getActivity());
                mRecyclerView.setAdapter(mAdapter);
            } else {
                mAdapter.setData(tasks);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

}
