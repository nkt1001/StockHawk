package com.sam_chordas.android.stockhawk.ui;


import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.db.chart.listener.OnEntryClickListener;
import com.db.chart.model.LineSet;
import com.db.chart.view.LineChartView;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.HistoricalItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class LineGraphFragment extends Fragment {

    enum ChartType {
        OPEN(0), CLOSE(1), HIGH(2), LOW(3);

        ChartType(int number) {
            mNumber = number;
        }

        private int mNumber;
        public int getNumber(){return mNumber;}
        public static ChartType getByNumber(int number) {
            for (ChartType type : values())
                if (number == type.getNumber()) return type;

            return null;
        }
    }

    private static final String ARG = "param1";
    private static final String TAG = LineGraphFragment.class.getSimpleName();

    private LineChartView mLineChart;
    private HistoricalItem[] mItems;
    private ArrayList<LineSet> mChartEntry;
    private ArrayList<Float[]> mFloatDataArray;
    private int[] mBorderValues;
    private String[] mLabels;
    private SimpleDateFormat mSDF = new SimpleDateFormat("yy-M-d", Locale.getDefault());

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
            mItems = (HistoricalItem[]) getArguments().getParcelableArray(ARG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.activity_line_graph, container, false);
        mLineChart = (LineChartView) view.findViewById(R.id.linechart);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


//        int min = Integer.MAX_VALUE;
//        int max = Integer.MIN_VALUE;
//        Log.d(TAG, "onStart: ");

//
//        for (int i = mItems.length-1; i >= 0; i--) {
//            HistoricalItem item = mItems[i];
//
//            openData.addPoint(item.getDate(), Float.parseFloat(item.getOpen()));
//            highData.addPoint(item.getDate(), Float.parseFloat(item.getHigh()));
//            lowData.addPoint(item.getDate(), Float.parseFloat(item.getLow()));
//            closeData.addPoint(item.getDate(), Float.parseFloat(item.getClose()));
//
//
//            if (min > Float.parseFloat(item.getLow())) {
//                min = Math.round(Float.parseFloat(item.getLow()));
//            }
//            if (max < Float.parseFloat(item.getHigh())) {
//                max = Math.round(Float.parseFloat(item.getHigh()));
//            }
//        }
//
////        lineSet.setDashed(new float[]{100.0f, 50.0f});

        adjustLineChart(initChartValue());
        Log.d(TAG, "onStart: " + mLineChart.getWidth());

//        int[] borderValues = getBorderValues(min, max, 1);

//        mLineChart.addData(openData);
//        mLineChart.addData(highData);
//        mLineChart.addData(lowData);
//        mLineChart.addData(closeData);



    }

    private void adjustLineChart(List<LineSet> lineSets) {
        mLineChart.setLabelsColor(ContextCompat.getColor(getActivity(), android.R.color.white));
        mLineChart.setAxisBorderValues(mBorderValues[0], mBorderValues[1], mBorderValues[2]);

        mLineChart.setOnEntryClickListener(new OnEntryClickListener() {
            @Override
            public void onClick(int setIndex, int entryIndex, Rect rect) {
                float value = mLineChart.getData().get(setIndex).getValue(entryIndex);
                Toast.makeText(getActivity(), "Value = [" + value + "], Date = [" + mLabels[entryIndex]
                        + "], Type = [" + ChartType.getByNumber(setIndex) + "]", Toast.LENGTH_SHORT).show();
            }
        });

        for (LineSet set : lineSets) {
            adjustLineSet(set, lineSets.indexOf(set));
            mLineChart.addData(set);
        }
    }

    private void adjustLineSet(LineSet set, int id) {
        //        LineSet openData = new LineSet();
//        openData.setColor(ContextCompat.getColor(getActivity(), R.color.material_green_700));
//        openData.setThickness(3);
//
//        LineSet highData = new LineSet();
//        highData.setColor(ContextCompat.getColor(getActivity(), R.color.material_red_700));
//        highData.setThickness(3);
//
//        LineSet lowData = new LineSet();
//        lowData.setColor(ContextCompat.getColor(getActivity(), R.color.material_purple_700));
//        lowData.setThickness(3);
//
//        LineSet closeData = new LineSet();
//        closeData.setColor(ContextCompat.getColor(getActivity(), R.color.material_blue_700));
//        closeData.setThickness(3);
        switch (ChartType.getByNumber(id)){
            case OPEN:
                set.setColor(ContextCompat.getColor(getActivity(), R.color.material_green_700));
                set.setThickness(3);
                break;
            case CLOSE:
                set.setColor(ContextCompat.getColor(getActivity(), R.color.material_blue_700));
                set.setThickness(3);
                break;
            case HIGH:
                set.setColor(ContextCompat.getColor(getActivity(), R.color.material_red_700));
                set.setThickness(3);
                break;
            case LOW:
                set.setColor(ContextCompat.getColor(getActivity(), R.color.material_purple_700));
                set.setThickness(3);
                break;
        }
    }

    private List<LineSet> initChartValue() {

        if (mItems.length == 0) return null;

        List<LineSet> dataSet = Arrays.asList(new LineSet(), new LineSet(), new LineSet(), new LineSet());

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        int step = 1;

        mLabels = new String[mItems.length];

        String label;
        ArrayList<Integer> showingLabelIndex = calcIndexList(mItems.length);

        for (int i = 0; i < mItems.length; i++) {
            HistoricalItem item = mItems[mItems.length-i-1];

            try {
                mLabels[i] = ((StockHistoryActivity)getActivity()).getSimpleDateFormat()
                        .format(((StockHistoryActivity)getActivity()).getDefaultFormat().parse(item.getDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            label = "";
            if (showingLabelIndex == null || showingLabelIndex.contains(i)) {
                try {
                    label = mSDF.format(((StockHistoryActivity)getActivity()).getSimpleDateFormat().parse(mLabels[i]));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "initChartValue: " + label);
            }

            for (ChartType type : ChartType.values()) {
                switch (type) {
                    case OPEN:
                        dataSet.get(ChartType.OPEN.getNumber()).addPoint(label, Float.parseFloat(item.getOpen()));
                        break;
                    case CLOSE:
                        dataSet.get(ChartType.CLOSE.getNumber()).addPoint(label, Float.parseFloat(item.getClose()));
                        break;
                    case HIGH:
                        dataSet.get(ChartType.HIGH.getNumber()).addPoint(label, Float.parseFloat(item.getHigh()));
                        break;
                    case LOW:
                        dataSet.get(ChartType.LOW.getNumber()).addPoint(label, Float.parseFloat(item.getLow()));
                        break;
                }
            }

            if (min > Float.parseFloat(item.getLow())) {
                min = Math.round(Float.parseFloat(item.getLow()));
            }
            if (max < Float.parseFloat(item.getHigh())) {
                max = Math.round(Float.parseFloat(item.getHigh()));
            }
        }

        mBorderValues = getBorderValues(min-1, max+1, step);

        return dataSet;
    }

    private ArrayList<Integer> calcIndexList(int arrayLength) {
        Log.d(TAG, "calcIndexList: " + arrayLength);

        //no need to calculate list
        if (arrayLength <= 5) return null;

        ArrayList<Integer> list = new ArrayList<>();
        boolean isEven = arrayLength % 2 == 0;

        int maxLabelNum = isEven ? 4 : 5;

        int div = Math.round((float)(arrayLength)/maxLabelNum);

        int index = 0;

        while (index < arrayLength) {
            index += div;
            list.add(index);
        }

        Log.d(TAG, "calcIndexList: " + list);

        return list;
    }

    private int[] getBorderValues(int min, int max, int step) {

        int dif = max - min;
        if ((dif % step == 0) && (dif / step < 13)) {

            return new int[] {min, max, step};
        }

        return getBorderValues(min, max, ++step);
    }

    @Override
    public void onResume() {
        super.onResume();

        mLineChart.show();
    }
}
