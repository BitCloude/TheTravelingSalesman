package com.simbiosyscorp.thetravelingsalesman.ui;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.simbiosyscorp.thetravelingsalesman.R;
import com.simbiosyscorp.thetravelingsalesman.utils.LoginUtils;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    private static final String DEBUG_TAG = "Settings Fragment: ";
    SharedPreferences mSharedPreferences;

    @Bind(R.id.username)
    EditText mUsername;
    @Bind(R.id.changePasswordContainer)
    View mChangePasswordContainer;
    @Bind(R.id.passwordConfirmation)
    EditText mPwConfirm;
    @Bind(R.id.password)
    EditText mOldPassword;
    @Bind(R.id.newPassword)
    EditText mNewPassword;
    @Bind(R.id.changePassword)
    Switch mChangePassword;
    @Bind(R.id.spinner)
    Spinner mSecurityQuestions;
    @Bind(R.id.customQuestion)
    EditText mCustomQuestion;
    @Bind(R.id.answer)
    EditText mAnswer;
    @Bind(R.id.changeQuestionSwitch) Switch mChangeQuestionSwitch;
    @Bind(R.id.changeQuestionContainer) View mChangeQuestionContainer;
    private boolean mChangeQA;

    private View mFocusView;
    private boolean mChangePw;
    private ArrayAdapter<CharSequence> mAdapter;
    private int mSelectedQ = 0;
    private boolean mNoPassword;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mSharedPreferences = context.getSharedPreferences(LoginUtils.PREF_NAME, 0);
        mNoPassword = TextUtils.isEmpty(mSharedPreferences.getString(LoginUtils.KEY_PASSWORD, ""));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        ButterKnife.bind(this, view);

        setHasOptionsMenu(true);

        getActivity().setTitle(R.string.title_activity_settings);

        mUsername.setText(getUserName());


        mAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.security_questions, R.layout.spinner_item);

        mAdapter.setDropDownViewResource(R.layout.view_spinner_item);
        mSecurityQuestions.setAdapter(mAdapter);
        mSecurityQuestions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == parent.getCount() - 1) {
                    mCustomQuestion.setVisibility(View.VISIBLE);
                } else {
                    mCustomQuestion.setVisibility(View.GONE);
                }
                mSelectedQ = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // if there's no password, always show change password option and security question option
        if (mNoPassword) {
            mChangePw = true;
            mChangePassword.setVisibility(View.GONE);
            mChangePasswordContainer.setVisibility(View.VISIBLE);
            mOldPassword.setVisibility(View.GONE);

            mChangeQuestionSwitch.setVisibility(View.GONE);
            mChangeQuestionContainer.setVisibility(View.VISIBLE);
            mChangeQA = true;
        } else {

            mChangePassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mChangePw = isChecked;
                    int visibility = isChecked ? View.VISIBLE : View.GONE;
                    mChangePasswordContainer.setVisibility(visibility);


                }
            });

            mChangeQuestionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mChangeQuestionContainer.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                    mChangeQA = isChecked;
                }
            });
        }



        return view;
    }

    private String getUserName() {
        if (mSharedPreferences == null) {
            return "";
        }

        return mSharedPreferences.getString(LoginUtils.KEY_USERNAME, "");
    }

    private boolean isOldPasswordValid() {
        if (mNoPassword) {
            return true;
        }

        String enteredOld = mOldPassword.getText().toString();

        String oldPw = mSharedPreferences.getString(LoginUtils.KEY_PASSWORD, "");

        boolean valid = enteredOld.equals(LoginUtils.decrypt(oldPw));

        if (!valid) {
            mOldPassword.setError(getString(R.string.error_incorrect_password));
            mFocusView = mOldPassword;
        }

        return valid;
    }

    private boolean isQuestionValid() {
        if (mSelectedQ != mAdapter.getCount() - 1) {
            return true;
        }
        String question = mCustomQuestion.getText().toString();
        if (TextUtils.isEmpty(question)) {
            mCustomQuestion.setError(getString(R.string.error_invalid_question_empty));
            mFocusView = mCustomQuestion;
            return false;
        }

        if (question.length() > LoginUtils.MAX_QUESTION_CHAR) {
            mCustomQuestion.setError(getString(R.string.error_invalid_question,
                    LoginUtils.MAX_QUESTION_CHAR));
            mFocusView = mCustomQuestion;

            return false;

        }

        return true;
    }

    private boolean isAnswerValid() {
        String answer = mAnswer.getText().toString();
        int min = LoginUtils.MIN_ANSWER_CHAR;
        int max = LoginUtils.MAX_ANSWER_CHAR;

        if (TextUtils.isEmpty(answer)
                || answer.length() < min
                || answer.length() > max) {
            mAnswer.setError(getString(R.string.error_invalid_answer));
            mFocusView = mAnswer;
            return false;
        }

        return true;
    }

    private boolean isNewPasswordValid() {
        String newPass = mNewPassword.getText().toString();
        String newConfirm = mPwConfirm.getText().toString();

        boolean error = false;
        if (!TextUtils.isEmpty(newPass)) {
            if (newPass.length() < 4 || newPass.length() > 16) {
                mNewPassword.setError(getString(R.string.error_invalid_password));
                mFocusView = mNewPassword;
                error = true;
            }

            if (!newConfirm.equals(newPass)) {
                mPwConfirm.setError(getString(R.string.error_invalid_confirm_password));
                mFocusView = mPwConfirm;
                error = true;
            }
        }

        if (!LoginUtils.isNameValid(mUsername.getText().toString())) {
            mUsername.setError(getString(R.string.error_invalid_username));
            error = true;
            mFocusView = mUsername;
        }


        return !error;
    }

    private void saveChanges() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();



        // save username
        editor.putString(LoginUtils.KEY_USERNAME, mUsername.getText().toString());

        // save new password, if changed
        String newPw = mNewPassword.getText().toString();
        if (!TextUtils.isEmpty(newPw)) {
            editor.putString(LoginUtils.KEY_PASSWORD, LoginUtils.encrypt(newPw));
        }




        // save security question and answer, if changed and password is not empty
        String newAnswer = mAnswer.getText().toString();

        if (!TextUtils.isEmpty(newAnswer) && (!TextUtils.isEmpty(newPw) || !TextUtils.isEmpty(
                mSharedPreferences.getString(LoginUtils.KEY_PASSWORD, "")))) {

            editor.putString(LoginUtils.KEY_QUESTION, getQuestion());
            String encryptedAnswer = LoginUtils.encrypt(newAnswer);
            editor.putString(LoginUtils.KEY_ANSWER, encryptedAnswer);
        }


        Log.i(DEBUG_TAG, "End of editor");

        editor.apply();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_ok) {
            boolean error = false;

            if (!isOldPasswordValid()) {
                error = true;
                Log.i(DEBUG_TAG, "oldpassword");
            }

            if (mChangePw && !isNewPasswordValid()) {
                error = true;
                Log.i(DEBUG_TAG, "newpassword");
            }

            // security question is required if password is not empty
            if (!TextUtils.isEmpty(mNewPassword.getText().toString())
            && mChangeQA && (!isQuestionValid() || !isAnswerValid())) {
                error = true;
                Log.i(DEBUG_TAG, "question");
            }

            if (error) {
                mFocusView.requestFocus();
                Log.i(DEBUG_TAG, "error");
                return false;
            }

            saveChanges();
            Toast.makeText(getActivity(), R.string.message_settings_changes,
                    Toast.LENGTH_LONG).show();
            getActivity().finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_settings, menu);
    }

    public String getQuestion() {
        if (mSelectedQ < mAdapter.getCount() - 1) {
            return mAdapter.getItem(mSelectedQ).toString();
        } else {
            return mCustomQuestion.getText().toString();
        }
    }
}
