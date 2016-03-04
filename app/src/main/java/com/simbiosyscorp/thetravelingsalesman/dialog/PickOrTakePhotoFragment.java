package com.simbiosyscorp.thetravelingsalesman.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.simbiosyscorp.thetravelingsalesman.R;
import com.simbiosyscorp.thetravelingsalesman.ui.ClientEditFragment;


public class PickOrTakePhotoFragment extends DialogFragment {


    public static final int PICK = 0;
    public static final int CAPTURE = 1;


    public PickOrTakePhotoFragment() {
        // Required empty public constructor
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        String areYouSure = getString(R.string.r_u_sure);
        builder.setItems(R.array.pick_or_take_a_photo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    sendResult(PICK);
                } else {
                    sendResult(CAPTURE);
                }
            }
        })
        ;

        return builder.create();
    }

    private void sendResult(int result) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(ClientEditFragment.EXTRA_PICK_OR_CAPTURE, result);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }

}
