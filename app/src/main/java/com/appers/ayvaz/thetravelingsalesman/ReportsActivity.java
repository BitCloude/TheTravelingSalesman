package com.appers.ayvaz.thetravelingsalesman;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

public class ReportsActivity extends NavigationDrawerActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.reports);
    }


    public void gotoExpenses(View view) {

    }

    public void gotoTasks(View view) {
        startActivity(ReportTaskActivity.newIntent(this, ReportTaskActivity.REPORT_TYPE_TASK));
    }


}
