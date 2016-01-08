package com.appers.ayvaz.thetravelingsalesman;

import android.content.Intent;
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
        startActivity(new Intent(this, ReportsExpenseActivity.class));
    }

    public void gotoTasks(View view) {
        startActivity(new Intent(this, ReportsTasksActivity.class));
    }

}
