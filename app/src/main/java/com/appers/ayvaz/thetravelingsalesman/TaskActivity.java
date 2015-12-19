package com.appers.ayvaz.thetravelingsalesman;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.appers.ayvaz.thetravelingsalesman.Views.SingleFragmentActivity;

import java.util.UUID;

public class TaskActivity extends SingleFragmentActivity {

    private static final String EXTRA_TASK_ID = "com.appers.ayvaz.thetravelingsalesman.task_id";



    public static Intent newIntent(Context packageContext, UUID taskId) {
        Intent intent = new Intent(packageContext, TaskActivity.class);
        intent.putExtra(EXTRA_TASK_ID, taskId);
        return intent;
    }

    protected Fragment createFragment() {
        UUID taskId = (UUID) getIntent().getSerializableExtra(EXTRA_TASK_ID);
        return TaskFragment.newInstance(taskId);
    }
}
