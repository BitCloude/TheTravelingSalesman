package com.appers.ayvaz.thetravelingsalesman;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appers.ayvaz.thetravelingsalesman.Model.Task;
import com.appers.ayvaz.thetravelingsalesman.Model.TaskList;

import java.util.UUID;

public class TaskFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TASK_ID = "task_id";


    private Task mTask;


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
        if (getArguments() != null) {
            UUID taskId = (UUID) getArguments().getSerializable(ARG_TASK_ID);
            mTask = TaskList.get(getContext()).getTask(taskId);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task, container, false);
    }

    public void returnResult() {

//        Intent data = new Intent();
//        getActivity().setResult(Activity.RESULT_OK, data);
    }


}
