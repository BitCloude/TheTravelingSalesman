package com.appers.ayvaz.thetravelingsalesman;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appers.ayvaz.thetravelingsalesman.Adapters.TaskAdapter;
import com.appers.ayvaz.thetravelingsalesman.Model.Task;
import com.appers.ayvaz.thetravelingsalesman.Model.TaskList;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.

 */
public class TaskListFragment extends Fragment {


    private TaskAdapter mTaskAdapter;
    private RecyclerView mRecyclerView;
    private static final int REQUEST_TASK = 1;
    public int mPosition;

    public TaskListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        //set up RecyclerView
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setHasFixedSize(true);
        updateUI();

        return view;
    }


    private void updateUI() {
        TaskList taskList = TaskList.get(getActivity());
        List<Task> tasks = taskList.getTasks();

        if (mTaskAdapter == null) {
            mTaskAdapter = new TaskAdapter(tasks, TaskListFragment.this);
            mRecyclerView.setAdapter(mTaskAdapter);
        } else {
            if (mPosition < 0) {
                mTaskAdapter.notifyDataSetChanged();
            } else {
                mTaskAdapter.notifyItemChanged(mPosition);
            }

        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TASK && resultCode == Activity.RESULT_OK) {
            //
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }
    
    
}
