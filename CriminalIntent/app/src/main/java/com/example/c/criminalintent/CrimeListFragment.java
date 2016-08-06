package com.example.c.criminalintent;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.c.criminalintent.Data.Crime;
import com.example.c.criminalintent.Data.CrimeLab;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CrimeListFragment extends Fragment {
    private ArrayList<Crime> mCrimes;
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;

    public CrimeListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCrimes = CrimeLab.get(getActivity()).getCrimes();

        // 액티비티와 달리 이 프래그먼트가 메뉴를 갖고 있구나 하고 알려줘야 한다.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                // 싱글턴 객체 가져와서 넣어줌
                CrimeLab.get(getActivity()).add(crime);

                Intent intent = new Intent(getActivity(), CrimePagerActivity.class);
                intent.putExtra(CrimeFragment.EXTRA_ID, crime.getId());
                startActivity(intent);
                return true;
            case R.id.menu_item_show_subtitle:
                int count = CrimeLab.get(getActivity()).getCrimes().size();

                // 임의로 설정된 스트링 포멧에 맞춰서 반환할수 있다.
                String str = getString(R.string.subtitle_format, count);

                // 서브타이틀에 바로 접근할수 없어서 현재 상속받은 액티비티를 가져와서 접근한다.
                AppCompatActivity activity = (AppCompatActivity) getActivity();
                activity.getSupportActionBar().setSubtitle(str); // null을 넣으면 사라진다.
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 뷰홀더
    class CrimeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTitleTextView;
        TextView mDateView;
        CheckBox mSolvedCheckBox;
        Crime mCrime;

        public CrimeViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_list_item_titleTextView);
            mDateView = (TextView) itemView.findViewById(R.id.crime_list_item_dateTextView);
            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.crime_list_item_solvedCheckBox);
        }

        public void bindCrime(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(crime.getTitle());
            mDateView.setText(crime.getDate().toString());
            mSolvedCheckBox.setChecked(crime.isSolved());
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), CrimePagerActivity.class);
            intent.putExtra(CrimeFragment.EXTRA_ID, mCrime.getId());
            startActivity(intent);
        }
    }

    class CrimeAdapter extends RecyclerView.Adapter<CrimeViewHolder> {

        @Override
        public CrimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View v = inflater.inflate(R.layout.list_item_crime, parent, false);
            CrimeViewHolder holder = new CrimeViewHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(CrimeViewHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = (RecyclerView) v.findViewById(R.id.crime_recycler_view);
        // 리사이클러 뷰는 매니저를 어떤걸 쓰냐에 따라 모양이 달라질 수 있다. (리니어는 리스트뷰 형태와 동일)
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new CrimeAdapter();
        mCrimeRecyclerView.setAdapter(mAdapter);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }
}
