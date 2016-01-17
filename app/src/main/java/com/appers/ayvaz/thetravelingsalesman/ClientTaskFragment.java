package com.appers.ayvaz.thetravelingsalesman;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.*;

import com.appers.ayvaz.thetravelingsalesman.adapter.TaskAdapter;
import com.appers.ayvaz.thetravelingsalesman.models.Client;
import com.appers.ayvaz.thetravelingsalesman.models.Task;
import com.appers.ayvaz.thetravelingsalesman.models.TaskManager;
import com.appers.ayvaz.thetravelingsalesman.view.DividerItemDecoration;


import org.joda.time.LocalDate;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClientTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientTaskFragment extends Fragment implements ClientActivity.ClientChanged,
        TaskListActivity.OnDateChanged {

    private Client mClient;
    private LocalDate mDate;
    private UUID mClientId;
    private static final String ARG_MODE = "arg_mode";
    private static final String ARG_CLIENT_ID = "client_id";
    private static final String ARG_DATE_TIME = "date_time";
    public static final int BY_CLIENT = 0;
    public static final int BY_DATE = 1;
    private static final String TAG = "ClientTaskFragment";
    private int mMode;

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

        TaskAdapter adapter = new TaskAdapter(list);
        mRecyclerView.setAdapter(adapter);

    }




    @Override
    public void onResume() {
        super.onResume();
//        updateUI();
    }

    @Override
    public void updateUI(Client client) {
        if (mMode == BY_DATE) {
            Log.i(TAG, "mode == by_date");
            return;
        }
        mClient = client;
        updateUI();
    }

    public void updateUI(LocalDate date) {
        if (mMode == BY_CLIENT) {
            Log.i(TAG, "mode == by_client");
            return;
        }
        mDate = date;
        updateUI();
    }




}


