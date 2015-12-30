package com.appers.ayvaz.thetravelingsalesman;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.appers.ayvaz.thetravelingsalesman.model.Task;
import com.appers.ayvaz.thetravelingsalesman.model.TaskList;

import java.util.List;
import java.util.UUID;

public class TaskPagerActivity extends AppCompatActivity {

    private static final String EXTRA_TASK_ID = "com.appers.ayvaz.thetravelingsalesman.task_id";
    private ViewPager mViewPager;
    private List<Task> mTasks;



    public static Intent newIntent(Context packageContext, UUID taskId) {
        Intent intent = new Intent(packageContext, TaskPagerActivity.class);
        intent.putExtra(EXTRA_TASK_ID, taskId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_pager);
        setTitle("Edit Task");
        UUID taskId = (UUID) getIntent().getSerializableExtra(EXTRA_TASK_ID);
        mViewPager = (ViewPager) findViewById(R.id.activity_task_pager_view_pager);

        mTasks = TaskList.get(this).getTasks();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Task task = mTasks.get(position);
                return TaskFragment.newInstance(task.getId());
            }

            @Override
            public int getCount() {
                return mTasks.size();
            }
        });
    }
}
