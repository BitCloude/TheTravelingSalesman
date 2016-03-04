package com.simbiosyscorp.thetravelingsalesman.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.simbiosyscorp.thetravelingsalesman.R;

public class NotificationActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new NotificationFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_activity_notification);

    }

}
