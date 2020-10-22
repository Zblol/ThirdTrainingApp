package com.example.criminalintent;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import java.util.Date;

public class TimePickerActivity extends SingleFragmentActivity {

    private static final String EXTRA_CRIME_TIME = "com.example.criminalintent.crime_time";

    public static Intent newIntent(Context packageContext, Date time) {
        Intent intent = new Intent(packageContext, TimePickerFragment.class);
        intent.putExtra(EXTRA_CRIME_TIME, time);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        Date time = (Date) getIntent().getSerializableExtra(EXTRA_CRIME_TIME);
        return TimePickerFragment.newInstance(time);
    }
}
