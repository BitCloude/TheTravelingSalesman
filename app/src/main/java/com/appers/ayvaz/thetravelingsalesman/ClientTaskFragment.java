package com.appers.ayvaz.thetravelingsalesman;


import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.*;

import com.appers.ayvaz.thetravelingsalesman.models.Client;
import com.appers.ayvaz.thetravelingsalesman.models.TaskII;
import com.appers.ayvaz.thetravelingsalesman.models.TaskManager;
import com.appers.ayvaz.thetravelingsalesman.utils.DateTimeHelper;
import com.appers.ayvaz.thetravelingsalesman.view.DividerItemDecoration;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClientTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientTaskFragment extends Fragment {

    private Client mClient;
    private Date mDate;
    private static final String ARG_MODE = "arg_mode";
    public static final int BY_CLIENT = 0;
    public static final int BY_DATE = 1;
    private int mMode;
    private PassClient mPassClient;

    interface PassClient{
        Client getClient();
    }

    public ClientTaskFragment() {
        // Required empty public constructor
    }

    public static ClientTaskFragment newInstance(int mode) {
        ClientTaskFragment fragment = new ClientTaskFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MODE, mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMode = getArguments().getInt(ARG_MODE);
    }

    @Bind (R.id.recycler_view) RecyclerView mRecyclerView;
    private TaskAdapter mAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        Context context = view.getContext();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setHasFixedSize(true);
        updateUI();

        return view;
    }

    private void updateUI() {
        if (mMode == BY_CLIENT) {
            List<TaskII> list = TaskManager.get(getContext()).query(mClient);
            mAdapter = new TaskAdapter(list);
            mRecyclerView.setAdapter(mAdapter);
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PassClient) {
            mPassClient = (PassClient) context;
            mClient = mPassClient.getClient();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement PassClient");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPassClient = null;
        mClient = null;
    }



    @Override
    public void onResume() {
        super.onResume();
//        updateUI();
    }

    public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

        List<TaskII> list;
        public TaskAdapter(List<TaskII> taskList) {
            list = taskList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_task_item, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            TaskII task = list.get(position);
            holder.setTask(task);


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            private TaskII mTask;

            void setTask(TaskII task) {
                mTask = task;
                setView();
            }

            @Bind(R.id.task_client) TextView taskClient;
            @Bind(R.id.task_detail) TextView taskDetail;
            @Bind(R.id.from_date) TextView fromDate;
            @Bind(R.id.to_date) TextView toDate;
            @Bind(R.id.buttonAttendee)ImageView buttonAttendee;
            @Bind(R.id.buttonReminder) ImageView buttonReminder;
            @Bind(R.id.buttonExtra) ImageView buttonExtra;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                itemView.setOnClickListener(this);

            }

            @Override
            public void onClick(View v) {
                Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, mTask.getId());
                Intent intent = new Intent(Intent.ACTION_VIEW)
                        .setData(uri);
                v.getContext().startActivity(intent);
            }

            public void setView() {
                taskClient.setText(mTask.getClient().toString());
                fromDate.setText(DateTimeHelper.formatMed(mTask.getStartTime()));
                toDate.setText(DateTimeHelper.formatMed(mTask.getEndTime()));
                if (!mTask.hasAlarm()) {
                    buttonReminder.setImageDrawable(null);
                }

                if (!mTask.hasAttendee()) {
                    buttonAttendee.setImageDrawable(null);
                }

                if (!mTask.hasExtendedProperties()) {
                    buttonExtra.setImageDrawable(null);
                }

                //// TODO: 009 01/09 task detail
            }
        }
    }


}


