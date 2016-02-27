package com.appers.ayvaz.thetravelingsalesman;


import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.appers.ayvaz.thetravelingsalesman.adapter.TaskReportAdapter;
import com.appers.ayvaz.thetravelingsalesman.models.Client;
import com.appers.ayvaz.thetravelingsalesman.models.ClientManager;
import com.appers.ayvaz.thetravelingsalesman.models.Task;
import com.appers.ayvaz.thetravelingsalesman.models.TaskManager;
import com.appers.ayvaz.thetravelingsalesman.utils.DateTimeHelper;
import com.appers.ayvaz.thetravelingsalesman.utils.EventUtility;
import com.appers.ayvaz.thetravelingsalesman.utils.ReportExportUtils;
import com.appers.ayvaz.thetravelingsalesman.view.DividerItemDecoration;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ReportTaskFragment extends Fragment {

    private static final String DEBUG_TAG = "ReportTaskFragment: ";
    private static final int REQUEST_CLIENT = 0;
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
    @Bind(R.id.applyButton)
    Button mApplyButton;
    @Bind(R.id.client_name)
    Button mClientName;
    @Bind(R.id.clearClient)
    ImageButton mClearClient;
    private int UNSORTED_ICON = R.drawable.ic_dark_sortable;
    private int ASC_ICON = R.drawable.ic_dark_sorted_asc;
    private int DESC_ICON = R.drawable.ic_dark_sorted_desc;
    private int[] sortable_icons = {UNSORTED_ICON, ASC_ICON, DESC_ICON};
    private Calendar mStartDateSet, mEndDateSet, mStartDate, mEndDate;
    private Client mClient;
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
        mStartDate.add(Calendar.MONTH, -2);
        mEndDate = Calendar.getInstance();
        mEndDate.add(Calendar.MONTH, 2);

        mStartDateSet = Calendar.getInstance();
        mEndDateSet = Calendar.getInstance();

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

        setHasOptionsMenu(true);

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

        mClearClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClient = null;
                updateUI();
            }
        });


        mStartButton.setOnClickListener(new PickDateButtonListener());
        mEndButton.setOnClickListener(new PickDateButtonListener());

        mApplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mStartDate.setTime(mStartDateSet.getTime());
                mEndDate.setTime(mEndDateSet.getTime());
                new GetTask().execute(mStartDate, mEndDate);

            }
        });



        mClientName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), ClientPickActivity.class),
                        REQUEST_CLIENT);
            }
        });


        return view;
    }

    private void showStartTime() {
        mStartButton.setText(DateTimeHelper.formatMed(mStartDateSet.getTime()));
    }

    private void showEndTime() {
        mEndButton.setText(DateTimeHelper.formatMed(mEndDateSet.getTime()));
    }

    private void updateIcon() {
        int[] orders = mAdapter.getOrders();
        for (int i = 0; i < 3; i++) {
            mHeaderIcons[i].setImageResource(sortable_icons[orders[i]]);
        }
    }

    private void updateUI() {

        if (mClient != null) {
            mClientName.setText(mClient.toString());
            mClearClient.setVisibility(View.VISIBLE);
        } else {
            mClientName.setText(R.string.client_not_selected);
            mClearClient.setVisibility(View.INVISIBLE);
        }

        showStartTime();
        showEndTime();

//        if (lastEventId != EventUtility.getLastEventId(getContext().getContentResolver())) {
            new GetTask().execute(mStartDate, mEndDate);
//        }


    }

    @Override
    public void onPause() {
        super.onPause();
        lastEventId = EventUtility.getLastEventId(getContext().getContentResolver());
    }

    @Override
    public void onResume() {
        super.onResume();

        //reset button time
        mStartDateSet.setTime(mStartDate.getTime());
        mEndDateSet.setTime(mEndDate.getTime());

        // set client
        if (mClient != null) {
            mClient = ClientManager.get(getActivity()).getClient(mClient.getId());
        }

        updateUI();

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_report, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                mAdapter.saveReport(mClient);
                return true;

            case R.id.action_send:
                selectAndShare();
                return true;

            case R.id.action_open_folder:
                ReportExportUtils.openReportFolder(getActivity());
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }



    private void selectAndShare() {
        final File dir = ReportExportUtils.getReportDir(getActivity());
        if (dir == null) {
            return;
        }

        final String[] fileList = dir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.startsWith(ReportExportUtils.TASK_PREFIX);
            }
        });

        Log.i(DEBUG_TAG, fileList.length + " files found");

        new AlertDialog.Builder(getActivity()).setTitle(R.string.select_report)
                .setItems(fileList,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ReportExportUtils.shareFile(getActivity(), new File(dir, fileList[which]));
                            }
                        })
                .create().show();
    }

    private class PickDateButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            DatePickerDialog.OnDateSetListener onDateSetListener;
            Calendar date;

            if (v.getId() == R.id.startDateButton) {
                onDateSetListener = new OnStartDateSetListener();
                date = mStartDateSet;
            } else {
                onDateSetListener = new OnEndDateSetListener();
                date = mEndDateSet;
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
            mStartDateSet.set(year, monthOfYear, dayOfMonth);
            if (!mStartDateSet.before(mEndDateSet)) {
                mEndDateSet.set(mStartDateSet.get(Calendar.YEAR), mStartDateSet.get(Calendar.MONTH),
                        mStartDateSet.get(Calendar.DAY_OF_MONTH));
                showEndTime();
            }
            showStartTime();
        }
    }

    private class OnEndDateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mEndDateSet.set(year, monthOfYear, dayOfMonth);
            if (!mStartDateSet.before(mEndDateSet)) {
                mStartDateSet.set(mEndDateSet.get(Calendar.YEAR), mEndDateSet.get(Calendar.MONTH),
                        mEndDateSet.get(Calendar.DAY_OF_MONTH));
                showStartTime();
            }

            showEndTime();

        }
    }

    private class GetTask extends AsyncTask<Calendar, Void, List<Task>> {

        @Override
        protected List<Task> doInBackground(Calendar... params) {

            return TaskManager.get(getContext()).query(mClient == null ? null : mClient.getId(),
                    params[0], params[1]);


        }

        @Override
        protected void onPreExecute() {
            mProgressBarContainer.setVisibility(View.VISIBLE);
//            mRecyclerView.setVisibility(View.GONE);
        }

        @Override
        protected void onPostExecute(List<Task> tasks) {
            if (mAdapter == null) {
                mAdapter = new TaskReportAdapter(tasks, getActivity());
                mRecyclerView.setAdapter(mAdapter);
            } else {
                mAdapter.setData(tasks);
                mAdapter.notifyDataSetChanged();
            }

            mProgressBarContainer.setVisibility(View.GONE);
            mAdapter.clearOrders();
            updateIcon();
//            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }


}
