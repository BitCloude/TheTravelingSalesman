package com.simbiosyscorp.thetravelingsalesman.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.simbiosyscorp.thetravelingsalesman.R;
import com.simbiosyscorp.thetravelingsalesman.utils.LoginUtils;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "LoginActivity";
    private static final String PREF_NAME = "loginInfomation";

    private final String KEY_USERNAME = "username";
    private final String KEY_PASSWORD = "password";

    private SharedPreferences mSharedPreferences;

    private int mBackCount = 0;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
//    private UserLoginTask mAuthTask = null;

    // UI references.
    @Bind(R.id.username) EditText mUserNameView;
    @Bind(R.id.password) EditText mPasswordView;
//    @Bind(R.id.passwordConfirmation) EditText mPasswordConfirmView;
    @Bind(R.id.sign_in_button) Button mSignInButton;
//    @Bind(R.id.login_progress) View mProgressView;
    @Bind(R.id.login_form) View mLoginFormView;
//    @Bind(R.id.passwordConfirmationLayout) View mConfirmation;
    @Bind(R.id.laterButton) Button mLaterButton;
    @Bind(R.id.forgotButton) Button mForgotButton;
    private boolean mModeRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mSharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        // if not registered, go to register mode


        if (noPassword()) {
            // no password, start application
            enterApplication();
            return;
        }



        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mModeRegister) {
                    createUser();

                } else {
                    attemptLogin();
                }

            }
        });


        if (mModeRegister) {
            mSignInButton.setText(android.R.string.ok);
            mPasswordView.setVisibility(View.GONE);
            mLaterButton.setVisibility(View.VISIBLE);
            mLaterButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setFirstTime(false);
                    finish();
                }
            });
            mForgotButton.setVisibility(View.GONE);
        } else {
            mPasswordView.setVisibility(View.VISIBLE);
            mUserNameView.setText(mSharedPreferences.getString(KEY_USERNAME, "(No name)"));
            mUserNameView.setEnabled(false);
            mSignInButton.setText(R.string.action_sign_in_short);
            mLaterButton.setVisibility(View.GONE);
            mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (!mModeRegister && (id == R.id.login || id == EditorInfo.IME_NULL)) {
                        attemptLogin();
                        return true;
                    }

//                if (mModeRegister && !TextUtils.isEmpty(textView.getText().toString())){
//                    mConfirmation.setVisibility(View.VISIBLE);
//                }

                    return false;
                }
            });
            mForgotButton.setVisibility(View.VISIBLE);
            mForgotButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                }
            });
        }


    }

    private void setFirstTime(Boolean bool) {
        mSharedPreferences.edit().putBoolean(LoginUtils.KEY_FIRST_TIME, bool)
                .apply();
    }

    private void createUser() {
        String name = mUserNameView.getText().toString();
        if (!LoginUtils.isNameValid(name)) {
            mUserNameView.setError(getString(R.string.error_invalid_username));
            mUserNameView.requestFocus();
            return;
        }
        saveUsername(name);

        new AlertDialog.Builder(LoginActivity.this)
                .setTitle(getString(R.string.welcome, name))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setFirstTime(false);
                        Intent intent = new Intent(LoginActivity.this, SettingsActivity.class);
                        startActivity(intent);
                    }
                }).setNegativeButton(R.string.not_now, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setFirstTime(false);
                enterApplication();
            }
        })
                .setMessage(R.string.message_create_password)
                .create().show();


    }

    private void saveUsername(String name) {
        mSharedPreferences.edit()
                .putString(LoginUtils.KEY_USERNAME, name)
                .apply();
    }

//    private void populateAutoComplete() {
//        getLoaderManager().initLoader(0, null, this);
//    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        mSignInButton.setClickable(false);
        // Reset errors.

        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
//        String username = mUserNameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;
        focusView = mPasswordView;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !LoginUtils.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            if (isValidated()) {
                unlock();
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                focusView.requestFocus();
            }
        }

        mSignInButton.setClickable(true);
    }

    private boolean isValidated() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, 0);
        String stored = sharedPreferences.getString(KEY_PASSWORD, "");
        String decrypted = LoginUtils.decrypt(stored);

        return !TextUtils.isEmpty(decrypted) &&
                decrypted.equals(mPasswordView.getText().toString());
    }

    private boolean isPasswordConfirmed(String confirm) {

        return !TextUtils.isEmpty(confirm) &&
                confirm.equals(mPasswordView.getText().toString());
    }



    private void enterApplication() {
        Intent intent = new Intent(this, LandingActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (noPassword()) {
            finish();
        }
    }

    private boolean noPassword() {
        mModeRegister = mSharedPreferences.getBoolean(LoginUtils.KEY_FIRST_TIME, true);
        return !mModeRegister &&
                TextUtils.isEmpty(mSharedPreferences.getString(KEY_PASSWORD, ""));

    }

    private void unlock() {
        mSharedPreferences.edit().putBoolean(LoginUtils.KEY_LOCKED, false).apply();
    }




    @Override
    protected void onStart() {
        super.onStart();
        mBackCount = 0;
    }


    private void lock() {
        mSharedPreferences.edit().putBoolean(LoginUtils.KEY_LOCKED, true).apply();
    }

    @Override
    public void onBackPressed() {
        if (mBackCount == 0) {
            ++mBackCount;
            Toast.makeText(this, R.string.message_exit, Toast.LENGTH_SHORT).show();
        } else {
            finishAffinity();
            lock();
        }


    }
}

