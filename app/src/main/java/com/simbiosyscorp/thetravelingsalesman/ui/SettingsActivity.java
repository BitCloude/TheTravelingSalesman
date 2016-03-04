package com.simbiosyscorp.thetravelingsalesman.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.simbiosyscorp.thetravelingsalesman.R;

public class SettingsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new SettingsFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkMenu(R.id.nav_settings);
    }


}
