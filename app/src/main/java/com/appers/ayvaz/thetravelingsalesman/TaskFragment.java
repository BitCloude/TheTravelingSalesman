package com.appers.ayvaz.thetravelingsalesman;



import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.appers.ayvaz.thetravelingsalesman.Model.Task;
import com.appers.ayvaz.thetravelingsalesman.Model.TaskList;


import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class TaskFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TASK_ID = "task_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";

    private static final int REQUEST_DATE_START = 0;
    private static final int REQUEST_DATE_END = 1;
    private static final int REQUEST_TIME_START = 2;
    private static final int REQUEST_TIME_END = 3;

    private Task mTask;
    private Button mStartDate, mEndDate, mStartTime, mEndTime;
    private EditText mNote, mLocation;
    private View.OnClickListener mDateListener, mTimeListener;

    public TaskFragment() {
        // Required empty public constructor
    }

    public static TaskFragment newInstance(UUID taskId) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK_ID, taskId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            UUID taskId = (UUID) getArguments().getSerializable(ARG_TASK_ID);
            mTask = TaskList.get(getContext()).getTask(taskId);
        }

        mDateListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean start = v.getId() == R.id.startDate;
                DialogFragment dialog = DatePickerFragment
                        .newInstance(start ? mTask.getStartDate() : mTask.getEndDate());
                int requestCode = start ? REQUEST_DATE_START : REQUEST_DATE_END;
                dialog.setTargetFragment(TaskFragment.this, requestCode);
                dialog.show(getFragmentManager(), DIALOG_DATE);
            }
        };

        mTimeListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment;
                int code;
                if (v.getId() == R.id.startTime) {
                    dialogFragment = TimePickerFragment.newInstance(mTask.getStartDate());
                    code = REQUEST_TIME_START;
                } else {
                    dialogFragment = TimePickerFragment.newInstance(mTask.getEndDate());
                    code = REQUEST_TIME_END;
                }
                dialogFragment.setTargetFragment(TaskFragment.this, code);
                dialogFragment.show(getFragmentManager(), DIALOG_TIME);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        mStartDate = (Button) view.findViewById(R.id.startDate);
        mEndDate = (Button) view.findViewById(R.id.endDate);
        updateDate(2);
        mStartTime = (Button) view.findViewById(R.id.startTime);
        mEndTime = (Button) view.findViewById(R.id.endTime);
        updateTime(2);

        mStartDate.setOnClickListener(mDateListener);
        mEndDate.setOnClickListener(mDateListener);
        mStartTime.setOnClickListener(mTimeListener);
        mEndTime.setOnClickListener(mTimeListener);
        mNote = (EditText) view.findViewById(R.id.taskNote);
        mLocation = (EditText) view.findViewById(R.id.taskLocation);
        updateUI();

        return view;
    }

    public void returnResult() {

//        Intent data = new Intent();
//        getActivity().setResult(Activity.RESULT_OK, data);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE_START || requestCode == REQUEST_DATE_END) {
            Calendar date = (Calendar) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            int option;
            if (requestCode == REQUEST_DATE_START) {
                mTask.setStartDate(date);
                if (mTask.getStartDate().after(mTask.getEndDate())) {
                    mTask.setEndDate(date);
                    option = 2;
                } else {
                    option = 0;
                }
            } else {
                if (date.before(mTask.getStartDate())) {
                    makeErrorToast();
                    return;
                }
                mTask.setEndDate(date);
                option = 1;
            }

            updateDate(option);
            return;
        }

        if (requestCode == REQUEST_TIME_START || requestCode == REQUEST_TIME_END) {
            int time = data.getIntExtra(TimePickerFragment.EXTRA_TIME, 0);

            int option;
            if (requestCode == REQUEST_TIME_START) {
                Calendar date = mTask.getStartDate();
                date.set(Calendar.HOUR, time / 60);
                date.set(Calendar.MINUTE, time % 60);
                mTask.setStartDate(date);

                if (mTask.getStartDate().after(mTask.getEndDate())) {
                    mTask.setEndDate(date);
                    option = 2;
                } else {
                    option = 0;
                }

            } else {
                Calendar c = mTask.getEndDate();
                c.set(Calendar.HOUR, time / 60);
                c.set(Calendar.MINUTE, time % 60);
                if (c.before(mTask.getStartDate())) {
                    makeErrorToast();
                    return;
                }
                mTask.setEndDate(c);
                option = 1;
            }

            updateTime(option);

        }
    }


    private void updateDate(int option) {

        if (option != 1) {
            mStartDate.setText(MySingleton.formatMed(mTask.getStartDate().getTime()));
        }

        if (option != 0) {
            mEndDate.setText(MySingleton.formatMed(mTask.getEndDate().getTime()));
        }
    }

    private void updateTime(int option) {
        if (option != 1) {
            mStartTime.setText(MySingleton.formatTime(mTask.getStartDate().getTime()));
        }

        if (option != 0) {
            mEndTime.setText(MySingleton.formatTime(mTask.getEndDate().getTime()));
        }
    }

    private void makeErrorToast() {
        Toast.makeText(getContext(), "End time is earlier than start time", Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_task, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete:
                TaskList.get(getActivity()).deleteTask(mTask.getId());
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void updateUI() {
//        mLocation.setText();
        mNote.setText(mTask.getNote());
    }
}
