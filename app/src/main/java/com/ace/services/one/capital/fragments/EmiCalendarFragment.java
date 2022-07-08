package com.ace.services.one.capital.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ace.services.one.capital.R;
import com.ace.services.one.capital.adapters.EmiCalendarAdapter;

public class EmiCalendarFragment extends Fragment {

    public EmiCalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_emi_calendar, container, false);

        // Link XML Components
        RecyclerView recyclerView = view.findViewById(R.id.emiCalendarRecyclerView);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        EmiCalendarAdapter adapter = new EmiCalendarAdapter(requireContext());
        recyclerView.setAdapter(adapter);

        return view;
    }
}