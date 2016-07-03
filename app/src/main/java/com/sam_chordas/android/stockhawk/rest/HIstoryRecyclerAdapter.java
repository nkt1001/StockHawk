package com.sam_chordas.android.stockhawk.rest;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.HistoricalItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Nkt1001 on 20.06.2016.
 */
public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.ViewHolder> {

    private static final String TAG = HistoryRecyclerAdapter.class.getSimpleName();
    private final HistoricalItem[] mItems;
    private final Context mContext;
    private final String template = "%.2f";
    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat defFormat;

    public HistoryRecyclerAdapter(Context context, HistoricalItem[] items, SimpleDateFormat format, SimpleDateFormat defaultFormat) {
        this.mItems = items;
        this.mContext = context;
        this.dateFormat = format;
        this.defFormat = defaultFormat;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_history, parent, false);
        return new ViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(HistoryRecyclerAdapter.ViewHolder holder, int position) {
        HistoricalItem historicalItem = mItems[position];

        holder.mSymbol.setText(historicalItem.getSymbol());
        try {
            holder.mDate.setText(dateFormat.format(defFormat.parse(historicalItem.getDate())));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        String open = holder.mView.getContext().getString(R.string.open) + " " + String.format(Locale.getDefault(), template, Float.parseFloat(historicalItem.getOpen()));
        String close = holder.mView.getContext().getString(R.string.close) + " " + String.format(Locale.getDefault(), template, Float.parseFloat(historicalItem.getClose()));

        holder.mOpen.setText(setSpan(open));
        holder.mOpen.setContentDescription(open);

        holder.mClose.setText(setSpan(close));
        holder.mClose.setContentDescription(close);

        String high = holder.mView.getContext().getString(R.string.high) + " " + String.format(Locale.getDefault(), template, Float.parseFloat(historicalItem.getHigh()));
        String low = holder.mView.getContext().getString(R.string.low) + " " + String.format(Locale.getDefault(), template, Float.parseFloat(historicalItem.getLow()));

        holder.mHigh.setText(setSpan(high));
        holder.mHigh.setContentDescription(high);

        holder.mLow.setText(setSpan(low));
        holder.mLow.setContentDescription(low);
    }



    @Override
    public int getItemCount() {
        return mItems.length;
    }

    private Spannable setSpan(String text) {
        int color = ContextCompat.getColor(mContext, R.color.mdtp_accent_color);
        Spannable spannable = new SpannableString(text);

        spannable.setSpan(new ForegroundColorSpan(color), 0, text.indexOf(":")+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannable;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView mSymbol;
        public final TextView mDate;
        public final TextView mOpen;
        public final TextView mHigh;
        public final TextView mLow;
        public final TextView mClose;


        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            mSymbol = (TextView) mView.findViewById(R.id.mSymbol);
            mDate = (TextView) mView.findViewById(R.id.mDate);
            mOpen = (TextView) mView.findViewById(R.id.mOpen);
            mHigh = (TextView) mView.findViewById(R.id.mHigh);
            mLow = (TextView) mView.findViewById(R.id.mLow);
            mClose = (TextView) mView.findViewById(R.id.mClose);
        }
    }
}
