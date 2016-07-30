package com.example.c.criminalintent;

import android.support.v4.app.Fragment;

import com.example.c.criminalintent.Common.SingleFragmentActivity;

public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
