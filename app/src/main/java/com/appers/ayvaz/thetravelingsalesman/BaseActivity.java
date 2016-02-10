package com.appers.ayvaz.thetravelingsalesman;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.appers.ayvaz.thetravelingsalesman.utils.LoginUtils;

import java.util.Calendar;

public class BaseActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "Base Activity";
    SharedPreferences mSharedPreferences;
    Calendar mCalendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPreferences = getSharedPreferences(LoginUtils.PREF_NAME, 0);

    }

    private void lock() {
        mSharedPreferences.edit().putBoolean(LoginUtils.LOCKED, true).apply();
    }

    private boolean isLocked() {
        long timeLeft = mSharedPreferences.getLong(LoginUtils.TIME_LEFT, 0);
        long timeNow = Calendar.getInstance().getTimeInMillis();

        Log.i(DEBUG_TAG, "Delta time:" + (timeNow - timeLeft));
        return timeNow - timeLeft > LoginUtils.MAX_TIME_LEFT;
    }

    private void setLeaveTime() {
        mCalendar = Calendar.getInstance();
        Log.i(DEBUG_TAG, "Left at: " + mCalendar.getTimeInMillis());
        mSharedPreferences.edit().putLong(LoginUtils.TIME_LEFT, mCalendar.getTimeInMillis()).apply();
    }

    @Override
    protected void onStop() {
        super.onStop();
        setLeaveTime();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isLocked()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
