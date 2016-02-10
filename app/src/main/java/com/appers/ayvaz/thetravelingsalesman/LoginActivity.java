package com.appers.ayvaz.thetravelingsalesman;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appers.ayvaz.thetravelingsalesman.utils.LoginUtils;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "LoginActivity";
    private static final String PREF_NAME = "loginInfomation";
    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private final String KEY_USERNAME = "username";
    private final String KEY_PASSWORD = "password";

    private SharedPreferences mSharedPreferences;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
//    private UserLoginTask mAuthTask = null;

    // UI references.
    @Bind(R.id.username) EditText mUserNameView;
    @Bind(R.id.password) EditText mPasswordView;
    @Bind(R.id.passwordConfirmation) EditText mPasswordConfirmView;
    @Bind(R.id.sign_in_button) Button mSignInButton;
    @Bind(R.id.login_progress) View mProgressView;
    @Bind(R.id.login_form) View mLoginFormView;
    @Bind(R.id.passwordConfirmationLayout) View mConfirmation;
    private boolean mModeRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mSharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        // if not registered, go to register mode
        mModeRegister = TextUtils.isEmpty(mSharedPreferences.getString(KEY_USERNAME, ""));

        if (!mModeRegister &&
                TextUtils.isEmpty(mSharedPreferences.getString(KEY_PASSWORD, ""))) {
            // no password, start application
            enterApplication();
            return;
        }



        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (!mModeRegister && (id == R.id.login || id == EditorInfo.IME_NULL)) {
                    attemptLogin();
                    return true;
                }

                if (mModeRegister && !TextUtils.isEmpty(textView.getText().toString())){
                    mConfirmation.setVisibility(View.VISIBLE);
                }

                return false;
            }
        });




        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mModeRegister) {
                    register();
                } else {
                    attemptLogin();
                }

            }
        });


        if (mModeRegister) {

            mSignInButton.setText(R.string.action_register);
        } else {
            mUserNameView.setText(mSharedPreferences.getString(KEY_USERNAME, "(No name)"));
            mUserNameView.setEnabled(false);
            mPasswordConfirmView.setVisibility(View.GONE);
            mSignInButton.setText(R.string.action_sign_in_short);
        }


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
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            if (isValidated()) {
                unlock();
                enterApplication();
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

    private boolean isNameValid(String name) {

        return TextUtils.isEmpty(name) || name.length() < 32;
    }

    private boolean isPasswordValid(String password) {

        return password.length() >= 4 && password.length() <= 16;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }






    private void register() {
        mSignInButton.setClickable(false);



        // Reset errors.
        mUserNameView.setError(null);
        mPasswordView.setError(null);
        mPasswordConfirmView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUserNameView.getText().toString();
        String password = mPasswordView.getText().toString();
        String confirm = mPasswordConfirmView.getText().toString();




        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (mConfirmation.getVisibility() == View.VISIBLE
        && !isPasswordConfirmed(confirm)) {
            mPasswordConfirmView.setError(getString(R.string.error_invalid_confirm_password));
            focusView = mPasswordConfirmView;
            cancel = true;
        }

        // Check for a valid email address.
        if (!isNameValid(username)) {
            mUserNameView.setError(getString(R.string.error_invalid_email));
            focusView = mUserNameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(KEY_USERNAME, username);

            if (!TextUtils.isEmpty(password)) {
                editor.putString(KEY_PASSWORD, LoginUtils.encrypt(password));
            }

            editor.apply();
            showProgress(false);
            unlock();
            Toast.makeText(this, R.string.register_success, Toast.LENGTH_LONG).show();
            enterApplication();

        }

        mSignInButton.setClickable(true);


    }

    private void enterApplication() {
//        Intent intent = new Intent(this, LandingActivity.class);
//        startActivity(intent);
        finish();
    }

    private void unlock() {
        mSharedPreferences.edit().putBoolean(LoginUtils.LOCKED, false).apply();
    }



    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    /*public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {


        private final String mPassword;

        UserLoginTask(String password) {

            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, 0);
            String password = sharedPreferences.getString(KEY_PASSWORD, "");

            return isPasswordConfirmed(password);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }*/
}

