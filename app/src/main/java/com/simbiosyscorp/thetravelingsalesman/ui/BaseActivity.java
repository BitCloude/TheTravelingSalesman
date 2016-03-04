package com.simbiosyscorp.thetravelingsalesman.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.simbiosyscorp.thetravelingsalesman.utils.LoginUtils;

import java.util.Calendar;

public class BaseActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "Base Activity";
    SharedPreferences mSharedPreferences;
    Calendar mCalendar;
    boolean mLocked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPreferences = getSharedPreferences(LoginUtils.PREF_NAME, 0);

    }



    private boolean isLocked() {
        if (TextUtils.isEmpty(mSharedPreferences.getString(LoginUtils.KEY_PASSWORD, ""))) {
            return false;
        }

        if (mSharedPreferences.getBoolean(LoginUtils.KEY_LOCKED, false)) {
            return true;
        }

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
        if (isFirstTime() || isLocked()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            mLocked = true;
        } else {
            mLocked = false;
        }
    }

    private boolean isFirstTime() {
        return mSharedPreferences.getBoolean(LoginUtils.KEY_FIRST_TIME, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocked = false;
    }


}
