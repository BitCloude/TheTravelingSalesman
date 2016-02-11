package com.appers.ayvaz.thetravelingsalesman;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Menu;

public class ReportTaskActivity extends SingleFragmentActivity {

    private static final String EXTRA_REPORT_TYPE = "extra_report_type";
    public static final int REPORT_TYPE_TASK = 0;
    public static final int REPORT_TYPE_EXPENSE = 1;

    private int mThisType;
    private int[] ids = new int[2];



    public static Intent newIntent(Context packageContext, int reportType) {
        Intent i = new Intent(packageContext, ReportTaskActivity.class);
        i.putExtra(EXTRA_REPORT_TYPE, reportType);
        return i;
    }


    @Override
    protected Fragment createFragment() {
        if (mThisType == REPORT_TYPE_TASK) {
            return new ReportTaskFragment();
        }

        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mThisType = intent.getIntExtra(EXTRA_REPORT_TYPE, 0);

        setTitle(getString(R.string.title_activity_report_task));

    }



    @Override
    protected void onResume() {
        super.onResume();
        checkMenu(ids[mThisType]);
    }
}
