package com.sam_chordas.android.stockhawk.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.sam_chordas.android.stockhawk.R;

/**
 * Created by nkt01 on 20.06.2016.
 */
public class SearchFragment extends Fragment {

    private Button btnStart;
    private Button btnEnd;
    private ImageButton btnSearch;
    private SharedPreferences prefs;
    private String start;
    private String end;

    public SearchFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View searchLayout = inflater.inflate(R.layout.fragment_search, container, false);

        btnStart = (Button) searchLayout.findViewById(R.id.buttonStart);
        btnEnd = (Button) searchLayout.findViewById(R.id.buttonEnd);
        btnSearch = (ImageButton) searchLayout.findViewById(R.id.buttonSearch);

        start = prefs.getString(StockHistoryActivity.KEY_START_DATE, getString(R.string.set_start_time));
        end = prefs.getString(StockHistoryActivity.KEY_END_DATE, getString(R.string.set_end_time));

        btnStart.setText(start);
        btnSearch.setContentDescription(start);
        btnEnd.setText(end);
        btnEnd.setContentDescription(end);

        return searchLayout;
    }
}
