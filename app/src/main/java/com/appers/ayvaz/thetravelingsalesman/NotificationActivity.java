package com.appers.ayvaz.thetravelingsalesman;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class NotificationActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new NotificationFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

}
