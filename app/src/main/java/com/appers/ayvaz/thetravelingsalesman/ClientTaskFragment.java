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
    private int mMode;
//    private PassClient mPassClient;


    /*interface PassClient{
        Client getClient();
    }*/

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
        List<TaskII> list;

        if (mMode == BY_CLIENT) {
            list = TaskManager.get(getContext()).query(mClientId);
        } else {
            list = TaskManager.get(getContext()).query(mDate);
//            list = TaskManager.get(getContext()).queryByDate(mDate);

        }

        TaskAdapter adapter = new TaskAdapter(list);
        mRecyclerView.setAdapter(adapter);

    }


   /* @Override
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
    }*/



    @Override
    public void onResume() {
        super.onResume();
//        updateUI();
    }

    @Override
    public void updateUI(Client client) {
        mClient = client;
        updateUI();
    }

    public void updateUI(LocalDate date) {
        mDate = date;
        updateUI();
    }

    public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

        private final static int TYPE_CLIENT = 0;
        private final static int TYPE_DATE = 1;
        List<TaskII> list;
        public TaskAdapter(List<TaskII> taskList) {
            list = taskList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_task_item, parent, false);
/*            if (viewType == TYPE_DATE) {
                return new ViewHolderDate(view);
            }*/
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            TaskII task = list.get(position);
            holder.setTask(task);

        }

        @Override
        public int getItemViewType(int position) {
            if (list.get(position).getClient() == null) {
                return TYPE_CLIENT;
            }
            return TYPE_DATE;
        }

        @Override
        public int getItemCount() {
            return list.size();
        }


        // view holder for client view
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            private TaskII mTask;

            void setTask(TaskII task) {
                mTask = task;
                setView();
            }

            @Bind(R.id.task_title) TextView taskTitle;
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
//                taskClient.setText(mTask.getClient().toString());
                fromDate.setText(DateTimeHelper.formatMed(mTask.getStartTime()));
                toDate.setText(DateTimeHelper.formatMed(mTask.getEndTime()));
                taskTitle.setText(mTask.getTitle());

                if (!mTask.hasAlarm()) {
                    buttonReminder.setImageDrawable(null);
                }

                if (!mTask.hasAttendee()) {
                    buttonAttendee.setImageDrawable(null);
                }

                if (!mTask.hasNotes()) {
                    buttonExtra.setImageDrawable(null);
                }
                    taskDetail.setText(mTask.getNotes());



            }
        }

        public class ViewHolderDate extends ViewHolder implements View.OnClickListener {
            private TaskII mTask;

            void setTask(TaskII task) {
                mTask = task;
                setView();
            }

            public ViewHolderDate(View itemView) {
                super(itemView);
            }




            public void setView() {
                taskTitle.setText(mTask.getClient().toString());
                fromDate.setText(DateTimeHelper.formatMed(mTask.getStartTime()));
                toDate.setText(DateTimeHelper.formatMed(mTask.getEndTime()));
                taskDetail.setText(mTask.getTitle());

                if (!mTask.hasAlarm()) {
                    buttonReminder.setImageDrawable(null);
                }

                if (!mTask.hasAttendee()) {
                    buttonAttendee.setImageDrawable(null);
                }

                if (!mTask.hasNotes()) {
                    buttonExtra.setImageDrawable(null);
                }


            }
        }
    }


}


