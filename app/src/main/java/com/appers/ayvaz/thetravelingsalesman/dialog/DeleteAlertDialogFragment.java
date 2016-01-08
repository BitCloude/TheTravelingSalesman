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
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        sendResult(Activity.RESULT_CANCELED);
                    }
                });

        return builder.create();
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
