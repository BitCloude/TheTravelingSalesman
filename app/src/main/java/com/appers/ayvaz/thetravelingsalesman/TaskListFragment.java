package com.appers.ayvaz.thetravelingsalesman;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.appers.ayvaz.thetravelingsalesman.adapter.TaskAdapter;
import com.appers.ayvaz.thetravelingsalesman.Model.Task;
import com.appers.ayvaz.thetravelingsalesman.Model.TaskList;
import com.appers.ayvaz.thetravelingsalesman.view.DividerItemDecoration;
import com.wefika.calendar.CollapseCalendarView;

import org.joda.time.LocalDate;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.

 */
public class TaskListFragment extends Fragment{


    private TaskAdapter mTaskAdapter;
    private RecyclerView mRecyclerView;
    private static final int REQUEST_TASK = 1;
    public int mPosition;
    private CollapseCalendarView mCollapseCalendarView;
    private int scrolledDistance = 0;

    private final int HIDE_THRESHOLD = 80;

    public TaskListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mCollapseCalendarView = (CollapseCalendarView) getActivity().findViewById(R.id.calendar);
        mCollapseCalendarView.setListener(new CollapseCalendarView.OnDateSelect() {
            @Override
            public void onDateSelected(LocalDate localDate) {
                changeRange(localDate);
            }
        });

        //set up RecyclerView
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setHasFixedSize(true);
        /*
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                CalendarManager manager = mCollapseCalendarView.getManager();
                boolean controlsVisible = manager.getState() == CalendarManager.State.MONTH;
                if (scrolledDistance > HIDE_THRESHOLD && controlsVisible){
                    manager.toggleView();
                    mCollapseCalendarView.populateLayout();
                    scrolledDistance = 0;
                }else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible ) {
                    manager.toggleView();
                    mCollapseCalendarView.populateLayout();
                    scrolledDistance = 0;
                }

                if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
                    scrolledDistance += dy;
                }

                Log.i(".................", (dy > 0 ? "up" : "down") +
                        (manager.getState() == CalendarManager.State.WEEK ? "week" : "month"));
            }
        });*/
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

    private void changeRange(LocalDate localDate) {
        // when user click a date, change the adapter
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new_task:
                Task task = new Task();
                TaskList.get(getActivity()).addTask(task);
                Intent intent = TaskActivity.newIntent(getActivity(), task.getId());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_tasks, menu);
    }


}
