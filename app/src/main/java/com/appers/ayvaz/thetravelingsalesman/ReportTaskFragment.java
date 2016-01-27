package com.appers.ayvaz.thetravelingsalesman;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appers.ayvaz.thetravelingsalesman.adapter.TaskReportAdapter;
import com.appers.ayvaz.thetravelingsalesman.models.Task;
import com.appers.ayvaz.thetravelingsalesman.models.TaskManager;
import com.appers.ayvaz.thetravelingsalesman.utils.EventUtility;
import com.appers.ayvaz.thetravelingsalesman.view.DividerItemDecoration;

import java.util.Calendar;
import java.util.List;

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

    private int UNSORTED_ICON = R.drawable.ic_dark_sortable;
    private int ASC_ICON = R.drawable.ic_dark_sorted_asc;
    private int DESC_ICON = R.drawable.ic_dark_sorted_desc;
    private int[] sortable_icons = {UNSORTED_ICON, ASC_ICON, DESC_ICON};

    private long lastEventId;
    private ImageView[] mHeaderIcons;

    private TaskReportAdapter mAdapter;

    public ReportTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_expense, container, false);

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
        return view;
    }

    private void updateIcon() {
        int[] orders = mAdapter.getOrders();
        for (int i = 0; i < 3; i++) {
            mHeaderIcons[i].setImageResource(sortable_icons[orders[i]]);
        }
    }

    private void updateUI() {
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
            mRecyclerView.setVisibility(View.GONE);
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
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }


}
