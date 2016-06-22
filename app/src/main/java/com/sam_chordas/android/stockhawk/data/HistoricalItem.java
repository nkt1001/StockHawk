package com.sam_chordas.android.stockhawk.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nkt1001 on 20.06.2016.
 */
public class HistoricalItem implements Parcelable {
    @SerializedName("Symbol")
    @Expose
    private String symbol;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("Open")
    @Expose
    private String open;
    @SerializedName("High")
    @Expose
    private String high;
    @SerializedName("Low")
    @Expose
    private String low;
    @SerializedName("Close")
    @Expose
    private String close;
    @SerializedName("Volume")
    @Expose
    private String volume;
    @SerializedName("Adj_Close")
    @Expose
    private String adjClose;

    protected HistoricalItem(Parcel in) {
        symbol = in.readString();
        date = in.readString();
        open = in.readString();
        high = in.readString();
        low = in.readString();
        close = in.readString();
        volume = in.readString();
        adjClose = in.readString();
    }

    public static final Creator<HistoricalItem> CREATOR = new Creator<HistoricalItem>() {
        @Override
        public HistoricalItem createFromParcel(Parcel in) {
            return new HistoricalItem(in);
        }

        @Override
        public HistoricalItem[] newArray(int size) {
            return new HistoricalItem[size];
        }
    };

    /**
     *
     * @return
     * The symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     *
     * @param symbol
     * The Symbol
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     *
     * @return
     * The date
     */
    public String getDate() {
        return date;
    }

    /**
     *
     * @param date
     * The Date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     *
     * @return
     * The open
     */
    public String getOpen() {
        return open;
    }

    /**
     *
     * @param open
     * The Open
     */
    public void setOpen(String open) {
        this.open = open;
    }

    /**
     *
     * @return
     * The high
     */
    public String getHigh() {
        return high;
    }

    /**
     *
     * @param high
     * The High
     */
    public void setHigh(String high) {
        this.high = high;
    }

    /**
     *
     * @return
     * The low
     */
    public String getLow() {
        return low;
    }

    /**
     *
     * @param low
     * The Low
     */
    public void setLow(String low) {
        this.low = low;
    }

    /**
     *
     * @return
     * The close
     */
    public String getClose() {
        return close;
    }

    /**
     *
     * @param close
     * The Close
     */
    public void setClose(String close) {
        this.close = close;
    }

    /**
     *
     * @return
     * The volume
     */
    public String getVolume() {
        return volume;
    }

    /**
     *
     * @param volume
     * The Volume
     */
    public void setVolume(String volume) {
        this.volume = volume;
    }

    /**
     *
     * @return
     * The adjClose
     */
    public String getAdjClose() {
        return adjClose;
    }

    /**
     *
     * @param adjClose
     * The Adj_Close
     */
    public void setAdjClose(String adjClose) {
        this.adjClose = adjClose;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(symbol);
        dest.writeString(date);
        dest.writeString(open);
        dest.writeString(high);
        dest.writeString(low);
        dest.writeString(close);
        dest.writeString(volume);
        dest.writeString(adjClose);
    }
}

