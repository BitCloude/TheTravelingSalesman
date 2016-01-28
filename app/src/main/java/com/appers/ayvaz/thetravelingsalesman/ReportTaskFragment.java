package com.appers.ayvaz.thetravelingsalesman;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.appers.ayvaz.thetravelingsalesman.adapter.TaskReportAdapter;
import com.appers.ayvaz.thetravelingsalesman.models.Client;
import com.appers.ayvaz.thetravelingsalesman.models.ClientManager;
import com.appers.ayvaz.thetravelingsalesman.models.Task;
import com.appers.ayvaz.thetravelingsalesman.models.TaskManager;
import com.appers.ayvaz.thetravelingsalesman.utils.DateTimeHelper;
import com.appers.ayvaz.thetravelingsalesman.utils.EventUtility;
import com.appers.ayvaz.thetravelingsalesman.view.DividerItemDecoration;

import org.joda.time.LocalDate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ReportTaskFragment extends Fragment {

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.taskHeader)
    ViewGroup mTaskTitle;
    @Bind(R.id.timeHeader)
    ViewGroup mTaskTime;
    @Bind(R.id.dateHeader)
    ViewGroup mTaskDate;
    @Bind(R.id.titleSorted)
    ImageView mTitleSorted;
    @Bind(R.id.dateSorted)
    ImageView mDateSorted;
    @Bind(R.id.timeSorted)
    ImageView mTimeSorted;

    @Bind(R.id.progressBarContainer)
    FrameLayout mProgressBarContainer;

    @Bind(R.id.startDateButton)
    Button mStartButton;

    @Bind(R.id.endDateButton)
    Button mEndButton;

    @Bind(R.id.applyButton) Button mApplyButton;
    @Bind(R.id.selectClient)    ImageButton mSelectClient;
    @Bind(R.id.client_name)
    TextView mClientName;


    private int UNSORTED_ICON = R.drawable.ic_dark_sortable;
    private int ASC_ICON = R.drawable.ic_dark_sorted_asc;
    private int DESC_ICON = R.drawable.ic_dark_sorted_desc;
    private int[] sortable_icons = {UNSORTED_ICON, ASC_ICON, DESC_ICON};
    private Calendar mStartDate, mEndDate;
    private Client mClient;

    private static final int REQUEST_CLIENT = 0;
    private long lastEventId;
    private ImageView[] mHeaderIcons;

    private TaskReportAdapter mAdapter;

    public ReportTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStartDate = Calendar.getInstance();
        mEndDate = Calendar.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_task, container, false);

        ButterKnife.bind(this, view);
        mHeaderIcons = new ImageView[]{mTitleSorted, mDateSorted, mTimeSorted};

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL_LIST));


        updateUI();

        mTaskTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean asc = mAdapter.getOrders()[0] != 1;

                mAdapter.sort(TaskReportAdapter.SORT_BY_TITLE, asc);

                updateIcon();
            }
        });

        mTaskDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean asc = mAdapter.getOrders()[1] != 1;
                mAdapter.sort(TaskReportAdapter.SORT_BY_DATETIME, asc);
                updateIcon();

            }
        });

        mTaskTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean asc = mAdapter.getOrders()[2] != 1;
                mAdapter.sort(TaskReportAdapter.SORT_BY_TIME, asc);
                updateIcon();
            }
        });

        mStartButton.setText(DateTimeHelper.formatMed(mStartDate.getTime()));
        mEndButton.setText(DateTimeHelper.formatMed(mEndDate.getTime()));
        mStartButton.setOnClickListener(new PickDateButtonListener());
        mEndButton.setOnClickListener(new PickDateButtonListener());

        mApplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStartDate != null && mEndDate != null) {
                    new GetTask().execute(mStartDate, mEndDate);
                }
            }
        });

        mSelectClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), ClientPickActivity.class),
                        REQUEST_CLIENT);
            }
        });

        mClientName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClient != null) {
                    startActivity(ClientInfoActivity.newIntent(getContext(), mClient.getId()));
                }
            }
        });


        return view;
    }

    private class PickDateButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            DatePickerDialog.OnDateSetListener onDateSetListener;
            Calendar date;

            if (v.getId() == R.id.startDateButton) {
                onDateSetListener = new OnStartDateSetListener();
                date = mStartDate;
            } else {
                onDateSetListener = new OnEndDateSetListener();
                date = mEndDate;
            }

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), onDateSetListener,
                    date.get(Calendar.YEAR), date.get(Calendar.MONTH),
                    date.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.show();

        }
    }

    private class OnStartDateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mStartDate.set(year, monthOfYear, dayOfMonth);
            if (!mStartDate.before(mEndDate)) {
                mEndDate.set(mStartDate.get(Calendar.YEAR), mStartDate.get(Calendar.MONTH),
                        mStartDate.get(Calendar.DAY_OF_MONTH));
                showEndTime();
            }
            showStartTime();
        }
    }

    private class OnEndDateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mEndDate.set(year, monthOfYear, dayOfMonth);
            if (!mStartDate.before(mEndDate)) {
                mStartDate.set(mEndDate.get(Calendar.YEAR), mEndDate.get(Calendar.MONTH),
                        mEndDate.get(Calendar.DAY_OF_MONTH));
                showStartTime();
            }

            showEndTime();

        }
    }

    private void showStartTime() {
        mStartButton.setText(DateTimeHelper.formatMed(mStartDate.getTime()));
    }

    private void showEndTime() {
        mEndButton.setText(DateTimeHelper.formatMed(mEndDate.getTime()));
    }

    private void updateIcon() {
        int[] orders = mAdapter.getOrders();
        for (int i = 0; i < 3; i++) {
            mHeaderIcons[i].setImageResource(sortable_icons[orders[i]]);
        }
    }

    private void updateUI() {
        // set client
        if (mClient != null) {
            mClientName.setText(mClient.getFullName());
        }
        // set time
        Calendar begin = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        begin.set(2015, 0, 1);
        end.set(2016, 0, 31);
//        List<Task> tasks = TaskManager.get(getContext()).queryInstance(begin, end, null);
//        if (mAdapter == null) {
//            mAdapter = new TaskReportAdapter(tasks);
//            mRecyclerView.setAdapter(mAdapter);
//        } else {
//            mAdapter.setData(tasks);
//            mAdapter.notifyDataSetChanged();
//        }
        mProgressBarContainer.setVisibility(View.VISIBLE);
        new GetTask().execute(begin, end);

    }

    @Override
    public void onPause() {
        super.onPause();
        lastEventId = EventUtility.getLastEventId(getContext().getContentResolver());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (lastEventId != EventUtility.getLastEventId(getContext().getContentResolver())) {
            updateUI();
        }
    }

    private class GetTask extends AsyncTask<Calendar, Void, List<Task>> {

        @Override
        protected List<Task> doInBackground(Calendar... params) {

            return TaskManager.get(getContext()).queryInstance(params[0], params[1], null);
        }

        @Override
        protected void onPreExecute() {
            mProgressBarContainer.setVisibility(View.VISIBLE);
//            mRecyclerView.setVisibility(View.GONE);
        }

        @Override
        protected void onPostExecute(List<Task> tasks) {
            if (mAdapter == null) {
                mAdapter = new TaskReportAdapter(tasks);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                mAdapter.setData(tasks);
                mAdapter.notifyDataSetChanged();
            }

            mProgressBarContainer.setVisibility(View.GONE);
//            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CLIENT) {
            UUID uuid = UUID.fromString(data.getStringExtra(ClientPickActivity.EXTRA_CLIENT_ID));
            mClient = ClientManager.get(getContext()).getClient(uuid);
            updateUI();
        }
    }
}
