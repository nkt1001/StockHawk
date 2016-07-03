package com.sam_chordas.android.stockhawk.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.HistoricalItem;
import com.sam_chordas.android.stockhawk.rest.DividerItemDecoration;
import com.sam_chordas.android.stockhawk.rest.HistoryRecyclerAdapter;

public class HistoricalListFragment extends Fragment {

    private static final String ARGS = "args";
    private HistoricalItem[] mItems;

    public HistoricalListFragment() {
    }

    public static HistoricalListFragment newInstance(HistoricalItem[] items) {
        HistoricalListFragment fragment = new HistoricalListFragment();
        Bundle args = new Bundle();
        args.putParcelableArray(ARGS, items);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mItems = (HistoricalItem[]) getArguments().getParcelableArray(ARGS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historicalitem_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new HistoryRecyclerAdapter(getActivity(), mItems,
                    ((StockHistoryActivity)getActivity()).getSimpleDateFormat(), ((StockHistoryActivity)getActivity()).getDefaultFormat()));
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        }
        return view;
    }

}
