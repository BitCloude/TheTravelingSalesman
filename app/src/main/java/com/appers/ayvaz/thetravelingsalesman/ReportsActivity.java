package com.appers.ayvaz.thetravelingsalesman;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

public class ReportsActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup viewStub = (ViewGroup) findViewById(R.id.view_stub);
        getLayoutInflater().inflate(R.layout.activity_reports, viewStub);

        setTitle(R.string.reports);
    }


    public void gotoExpenses(View view) {

    }

    public void gotoTasks(View view) {
        startActivity(ReportTaskActivity.newIntent(this, ReportTaskActivity.REPORT_TYPE_TASK));
    }


}
