package com.example.c.criminalintent;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CrimeListFragment extends Fragment {
    private ArrayList<Crime> mCrimes;
    ListView crimeListView;

    public CrimeListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCrimes = CrimeLab.get(getActivity()).getCrimes();
    }

    // CustomListView (Inner Class)
    class CrimeAdapter extends ArrayAdapter<Crime> {
        public CrimeAdapter() {
            super(getActivity(), 0, mCrimes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_crime, null);
            }

            Crime c = mCrimes.get(position);
            TextView titleTextView = (TextView) convertView.findViewById(R.id.crime_list_item_titleTextView);
            TextView dateTextView = (TextView) convertView.findViewById(R.id.crime_list_item_dateTextView);
            CheckBox solvedCheckBox = (CheckBox) convertView.findViewById(R.id.crime_list_item_solvedCheckBox);

            titleTextView.setText(c.getTitle());
            dateTextView.setText(c.getDate().toString());
            solvedCheckBox.setChecked(c.isSolved());

            return convertView;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_crime_list, container, false);
        crimeListView = (ListView) v.findViewById(R.id.crimeListView);
        CrimeAdapter adapter = new CrimeAdapter();
        crimeListView.setAdapter(adapter);

        crimeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Crime c = mCrimes.get(position);

                Intent intent = new Intent(getActivity(), CrimeActivity.class);
                intent.putExtra("id", c.getId());
                startActivity(intent);
            }
        });
        return v;
    }

}
