package com.example.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;


public class TimePickerFragment extends DialogFragment {

    private static final String ARG_TIME = "Time";
    public static final String EXTRA_TIME = "com.example.criminalintent.time";

    private TimePicker mTimePicker;

    public static TimePickerFragment newInstance(Date time) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, time);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);

        final Date time = (Date) getArguments().getSerializable(ARG_TIME);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        int hours = calendar.get(calendar.HOUR_OF_DAY);
        final int minute = calendar.get(calendar.MINUTE);


        mTimePicker = (TimePicker) v.findViewById(R.id.dialog_time_piker);
        mTimePicker.setCurrentHour(hours);
        mTimePicker.setCurrentMinute(minute);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Time of Crime")
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int hours = mTimePicker.getCurrentHour();
                                int minutes = mTimePicker.getCurrentMinute();

                                Calendar calendar1 = Calendar.getInstance();
                                calendar.setTime(time);
                                calendar.set(Calendar.HOUR_OF_DAY, hours);
                                calendar.set(Calendar.MINUTE, minutes);
                                Date time = calendar.getTime();
                                sendResult(Activity.RESULT_OK, time);
                            }
                        })
                .create();
    }

    private void sendResult(int resultCode, Date time) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, time);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
