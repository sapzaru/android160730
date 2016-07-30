package com.example.c.criminalintent;

import android.support.v4.app.Fragment;

import com.example.c.criminalintent.Common.SingleFragmentActivity;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        UUID crimeId = (UUID) getIntent().getSerializableExtra(CrimeFragment.EXTRA_ID);

        return CrimeFragment.newInstance(crimeId);
    }
}
