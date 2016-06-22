package com.sam_chordas.android.stockhawk.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.HistoricalItem;


public class LineGraphFragment extends Fragment {

    private static final String ARG = "param1";

    private String mItems;


    public LineGraphFragment() {
        // Required empty public constructor
    }

    public static LineGraphFragment newInstance(HistoricalItem[] items) {
        LineGraphFragment fragment = new LineGraphFragment();
        Bundle args = new Bundle();
        args.putParcelableArray(ARG, items);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mItems = getArguments().getString(ARG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_line_graph, container, false);
    }

}
