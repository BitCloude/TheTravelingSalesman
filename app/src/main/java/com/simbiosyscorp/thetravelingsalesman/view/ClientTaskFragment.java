package com.simbiosyscorp.thetravelingsalesman.view;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import org.joda.time.LocalDate;

import java.util.List;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClientTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientTaskFragment extends Fragment implements ClientActivity.ClientChanged,
        TaskListActivity.OnDateChanged {

    private static final int REQUEST_CLIENT = 0;
    private static final String DEBUG_TAG = "ClientTaskFragment";
    private Client mClient;
    private LocalDate mDate;
    private UUID mClientId;
    private static final String ARG_MODE = "arg_mode";
    private static final String ARG_CLIENT_ID = "client_id";
    private static final String ARG_DATE_TIME = "date_time";
    public static final int BY_CLIENT = 0;
    public static final int BY_DATE = 1;

    private int mMode;
    private TaskAdapter mAdapter;
    private int mSelected;
    @Bind(R.id.emptyView) View mEmptyView;
    @Bind(R.id.emptyTextView)
    TextView mEmptyTextView;
    @Bind(R.id.addNew)
    Button mAddNewButton;

    public ClientTaskFragment() {
        // Required empty public constructor
    }

    public static ClientTaskFragment newInstance(UUID clientId) {
        ClientTaskFragment fragment = new ClientTaskFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CLIENT_ID, clientId);
        fragment.setArguments(args);
        return fragment;
    }

    public static ClientTaskFragment newInstance(LocalDate date) {
        ClientTaskFragment fragment = new ClientTaskFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE_TIME, date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        if ((args.getSerializable(ARG_CLIENT_ID)) != null) {
            mClientId = (UUID) args.getSerializable(ARG_CLIENT_ID);
            mMode = BY_CLIENT;
        } else if (args.getSerializable(ARG_DATE_TIME) != null ) {
            mDate = (LocalDate) args.getSerializable(ARG_DATE_TIME);
            mMode = BY_DATE;
        }
    }

    @Bind (R.id.recycler_view) RecyclerView mRecyclerView;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);

        Context context = view.getContext();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setHasFixedSize(true);
        registerForContextMenu(mRecyclerView);
        updateUI();


        return view;
    }

    private void updateUI() {
        List<Task> list;



        if (mMode == BY_CLIENT) {
            list = TaskManager.get(getContext()).query(mClientId);
        } else {
//            list = TaskManager.get(getContext()).query(mDate);
            list = TaskManager.get(getContext()).queryInstance(mDate);

        }

        displayEmptyView(list.isEmpty());

        if (mAdapter == null) {
            mAdapter = new TaskAdapter(list, getActivity());
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setData(list);
            mAdapter.notifyDataSetChanged();
        }


    }

    private void displayEmptyView(boolean isEmpty) {
        if (isEmpty) {
            mEmptyView.setVisibility(View.VISIBLE);
            mEmptyTextView.setText(getActivity().getString(R.string.emptyList, "task"));
            mAddNewButton.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.GONE);
        }
    }





    @Override
    public void onResume() {
        super.onResume();
//        updateUI();
    }

    @Override
    public void updateUI(Client client) {
        if (mMode == BY_DATE) {
            Log.i(DEBUG_TAG, "mode == by_date");
            return;
        }
        mClient = client;
        updateUI();
    }

    public void updateUI(LocalDate date) {
        if (mMode == BY_CLIENT) {
            Log.i(DEBUG_TAG, "mode == by_client");
            return;
        }
        mDate = date;
        updateUI();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        mSelected = mAdapter.getSelected();

        switch (item.getItemId()) {
            case R.id.action_delete:
                alertDeletion(mSelected);
                return true;

            case R.id.action_change_client:
                startActivityForResult(new Intent(getActivity(), ClientPickActivity.class),
                        REQUEST_CLIENT);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }

    private void alertDeletion(final int selected) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setMessage(getString(R.string.r_u_sure, "Task"))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean result = mAdapter.delete(selected);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CLIENT) {
            UUID id = UUID.fromString(data.getStringExtra(ClientPickActivity.EXTRA_CLIENT_ID));
            Client client = ClientManager.get(getActivity()).getClient(id);
            boolean s = mAdapter.setClient(mSelected, client);
            Log.i(DEBUG_TAG, "update " + (s ? "success" : "failed"));
            mAdapter.notifyItemChanged(mSelected);
        }
    }
}


