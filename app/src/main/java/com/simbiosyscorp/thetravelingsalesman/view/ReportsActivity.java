package com.simbiosyscorp.thetravelingsalesman.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.simbiosyscorp.thetravelingsalesman.R;

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

    @Override
    protected void onStart() {
        super.onStart();
        checkMenu(R.id.nav_reports);
    }
}
