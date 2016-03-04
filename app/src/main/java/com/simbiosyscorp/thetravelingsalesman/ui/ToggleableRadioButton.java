package com.simbiosyscorp.thetravelingsalesman.ui;

import android.content.Context;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by Ayvaz on 2/8/2016.
 */
public class ToggleableRadioButton extends RadioButton {

    // Implement necessary constructors

    public ToggleableRadioButton(Context context) {
        super(context);
    }

    @Override
    public void toggle() {
        if(isChecked()) {
            if(getParent() instanceof RadioGroup) {
                ((RadioGroup)getParent()).clearCheck();
            }
        } else {
            setChecked(true);
        }
    }
}
