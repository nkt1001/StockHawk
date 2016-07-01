package com.sam_chordas.android.stockhawk.widget;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;

/**
 * Created by Nkt1001 on 01.07.2016.
 */
public class ListProvider implements RemoteViewsService.RemoteViewsFactory {
    public static final String EXTRA_DATA = "EXTRA_DATA";
    private static final String TAG = ListProvider.class.getSimpleName();
    private ListItem[] mData;
    private Context context = null;

    public ListProvider(Context context, Intent intent) {
        this.context = context;

        mData = (ListItem[]) intent.getParcelableArrayExtra(EXTRA_DATA);
        Log.d(TAG, "ListProvider: " + mData);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.length;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    /*
    *Similar to getView of Adapter where instead of View
    *we return RemoteViews
    *
    */
    @Override
    public RemoteViews getViewAt(int position) {
        Log.d(TAG, "getViewAt() called with: " + "position = [" + position + "]");

        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.line_row);
        ListItem listItem = mData[position];

        remoteView.setTextViewText(R.id.stock_symbol, listItem.getSymbol());
        remoteView.setTextViewText(R.id.bid_price, listItem.getBid());
        remoteView.setTextViewText(R.id.textViewChange, listItem.getChange());

        int color = listItem.getChangeinPercent().contains("+") ? R.color.material_green_700 : R.color.material_red_700;
        remoteView.setTextColor(R.id.textViewChange, ContextCompat.getColor(context, color));

        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }
}
