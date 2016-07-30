package com.example.c.criminalintent;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_crime_list, container, false);
        crimeListView = (ListView) v.findViewById(R.id.crimeListView);
        ArrayAdapter<Crime> adapter = new ArrayAdapter<Crime>(getActivity(),
                            android.R.layout.simple_list_item_1, mCrimes);
        crimeListView.setAdapter(adapter);

        crimeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Crime c = mCrimes.get(position);
                Log.d("crime", c.getTitle() + "clicked");
            }
        });
        return v;
    }

}
