package com.appers.ayvaz.thetravelingsalesman;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class ReportExpenseActivity extends SingleFragmentActivityNoNav {




    @Override
    protected Fragment createFragment() {
            return new ReportExpenseFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setTitle(getString(R.string.title_activity_report_expense));

    }



    @Override
    protected void onResume() {
        super.onResume();

    }
}
