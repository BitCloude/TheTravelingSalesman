package com.appers.ayvaz.thetravelingsalesman;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

public class ReportsActivity extends NavigationDrawerActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        setTitle(R.string.reports);
    }


    public void gotoExpenses(View view) {
        startActivity(new Intent(this, ReportExpenseActivity.class));
    }

    public void gotoTasks(View view) {
        startActivity(ReportTaskActivity.newIntent(this, ReportTaskActivity.REPORT_TYPE_TASK));
    }


}
