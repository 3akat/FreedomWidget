package com.android.apps.widget.Freedom;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TimePicker;

class CustomTimePickerDialog extends TimePickerDialog {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================
    public CustomTimePickerDialog(Context context, OnTimeSetListener callBack, int hourOfDay, int minute,
                                  boolean is24HourView) {
        super(context, callBack, hourOfDay, minute, is24HourView);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        super.onTimeChanged(view, hourOfDay, minute);
        setTitle("Time");
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

}