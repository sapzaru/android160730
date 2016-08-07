package com.example.c.criminalintent;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.c.criminalintent.Common.SingleFragmentActivity;
import com.example.c.criminalintent.Data.Crime;

public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        // null이면 핸드폰, 아니면 태블릿
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = new Intent(this, CrimePagerActivity.class);
            intent.putExtra(CrimeFragment.EXTRA_ID, crime.getId());
            startActivity(intent);
        } else {
            Fragment newDetail = CrimeFragment.newInstance(crime.getId());
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }
}
