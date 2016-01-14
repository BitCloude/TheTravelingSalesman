package com.appers.ayvaz.thetravelingsalesman.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.appers.ayvaz.thetravelingsalesman.R;


public class DeleteAlertDialogFragment extends DialogFragment {
    private static final String ARG_OBJECT = "object";
    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;
    private Activity mActivity;

    public static DeleteAlertDialogFragment newInstance(String object) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_OBJECT, object);
        DeleteAlertDialogFragment fragment = new DeleteAlertDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String object = getArguments().getString(ARG_OBJECT);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        String areYouSure = getString(R.string.r_u_sure);
        builder.setMessage(String.format(areYouSure, object))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        sendResult(Activity.RESULT_OK);

                    }
                })
                .setNegativeButton(android.R.string.cancel, null);

        return builder.create();
    }

    private void sendResult(int resultCode) {
        Intent intent = new Intent();
        if (getTargetFragment() != null) {
            getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
            return;
        }

        if (getActivity() != null) {
            getActivity().setResult(Activity.RESULT_OK);
        }



    }

    /* The activity that creates an instance of this dialog fragment must
         * implement this interface in order to receive event callbacks.
         * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);

        public void onDialogNegativeClick(DialogFragment dialog);
    }

   /* @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface

        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }    }

*/
}
