package com.example.criminalintent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import java.text.DateFormat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DATE_DIALOG = "DialogDate";
    private static final String TIME_DIALOG = "DialogTime";

    public static final String TIME_FORMAT = "hh:mm: a z";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;

    private Crime mCrime;
    private EditText mTitleField;
    private Button mButtonDate;
    private Button mButtonTime;
    private CheckBox mCheckSolvedBox;
    private Button firstButton;
    private Button lastButton;

    private boolean mIsLargeLayout;
    private Callbacks mCallbacks;

    public interface Callbacks {
        void onCrimeUpdate(Crime crime);
        void onCrimeSelected(Crime crime);
    }

    public static CrimeFragment newInstanse(UUID crimeId) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIsLargeLayout = getResources().getBoolean(R.bool.large_layout);

        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
                updateCrime();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mButtonDate = (Button) v.findViewById(R.id.crime_date);
        updateDate();
        mButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getFragmentManager();

                if (mIsLargeLayout) {
                    DatePickerFragment dialog = new DatePickerFragment().newInstance(mCrime.getDate());
                    dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                    dialog.show(fragmentManager, DATE_DIALOG);
                } else {
                    Intent intent = DatePickerActivity.newIntent(getContext(), mCrime.getTime());
                    startActivityForResult(intent, REQUEST_DATE);
                }
            }
        });

        mButtonTime = (Button) v.findViewById(R.id.crime_time);
        updateTime();
        mButtonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsLargeLayout) {
                    FragmentManager fragment = getFragmentManager();
                    TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getTime());
                    dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                    dialog.show(fragment, TIME_DIALOG);
                } else {
                    Intent intent = TimePickerActivity.newIntent(getContext(), mCrime.getTime());
                    startActivityForResult(intent, REQUEST_TIME);
                }
            }
        });

        mCheckSolvedBox = (CheckBox) v.findViewById(R.id.crime_solved);
        mCheckSolvedBox.setChecked(mCrime.isSolved());
        mCheckSolvedBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
                updateCrime();
            }
        });




        firstButton = v.findViewById(R.id.firstButton);
        lastButton = v.findViewById(R.id.lastButton);


        firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CrimePagerActivity) getActivity()).goToFistPage();
            }
        });

        lastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CrimePagerActivity) getActivity()).goToLastPage();
            }
        });


        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
            updateCrime();
        }
        if (requestCode == REQUEST_TIME) {
            Date time = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mCrime.setTime(time);
            updateTime();
            updateCrime();
        }
    }

    private void updateCrime() {
        mCallbacks.onCrimeUpdate(mCrime);
    }
    private void selectCrime(){
        mCallbacks.onCrimeSelected(mCrime);
    }

    private void updateDate() {
        mButtonDate.setText(mCrime.getDate().toString());
    }

    private void updateTime() {
        DateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
        mButtonTime.setText(timeFormat.format(mCrime.getTime()));

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
}
