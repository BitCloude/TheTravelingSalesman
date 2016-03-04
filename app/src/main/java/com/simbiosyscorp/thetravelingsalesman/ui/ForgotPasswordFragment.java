package com.simbiosyscorp.thetravelingsalesman.ui;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.simbiosyscorp.thetravelingsalesman.R;
import com.simbiosyscorp.thetravelingsalesman.utils.LoginUtils;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotPasswordFragment extends Fragment {



    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    @Bind(R.id.question)
    TextView mQuestion;
    @Bind(R.id.answer)
    TextView mAnswer;

    @Bind(R.id.action_ok)
    Button mOkButton;
    private SharedPreferences mSharedPreference;
    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        public void resetPassword();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mSharedPreference = context.getSharedPreferences(LoginUtils.PREF_NAME, 0);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        ButterKnife.bind(this, view);

        String question = mSharedPreference.getString(LoginUtils.KEY_QUESTION, "");
        mQuestion.setText(question);

        final String answer = LoginUtils.decrypt(mSharedPreference.getString(LoginUtils.KEY_ANSWER, ""));

        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String entered = mAnswer.getText().toString();
                if (TextUtils.isEmpty(entered) || !entered.equals(answer)) {
                    mAnswer.setError(getString(R.string.error_incorrect_answer));
                    mAnswer.requestFocus();
                } else {
                    new AlertDialog.Builder(getActivity())
                            .setMessage(R.string.message_recreate_password)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    resetPass();
                                }
                            })
                            .create().show();
                }
            }
        });
        return view;
    }

    private void resetPass() {
        mSharedPreference.edit().remove(LoginUtils.KEY_PASSWORD)
                .remove(LoginUtils.KEY_QUESTION)
                .remove(LoginUtils.KEY_ANSWER)
                .apply();
        mListener.resetPassword();
    }




}
