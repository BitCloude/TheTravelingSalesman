package com.simbiosyscorp.thetravelingsalesman.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.simbiosyscorp.thetravelingsalesman.R;

public class ForgotPasswordActivity extends AppCompatActivity
implements ForgotPasswordFragment.OnFragmentInteractionListener{

    private FragmentManager mManager;

    protected Fragment createFragment() {
        return new ForgotPasswordFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);

        mManager = getSupportFragmentManager();
        Fragment fragment = mManager.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            mManager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }


    @Override
    public void resetPassword() {
        Fragment fragment = new SettingsFragment();
        mManager.beginTransaction().replace(R.id.fragment_container, fragment)
                .commit();
    }
}
