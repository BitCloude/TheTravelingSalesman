package com.simbiosyscorp.thetravelingsalesman.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {
    private static final String ARG_TIME = "time";

    public static final String EXTRA_TIME =
            "com.appers.avyaz.thetravelingsalesman.task.time";




    public static TimePickerFragment newInstance(Calendar cal) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, cal);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        Calendar c = (Calendar) getArguments().getSerializable(ARG_TIME);
        if (c == null) c = GregorianCalendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();

        int time = hourOfDay * 60 + minute;
        intent.putExtra(EXTRA_TIME, time);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }
}