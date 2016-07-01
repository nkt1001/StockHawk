package com.sam_chordas.android.stockhawk.widget;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nkt1001 on 01.07.2016.
 */
public class ListItem implements Parcelable {
    @SerializedName("symbol")
    @Expose
    private String symbol;
    @SerializedName("Bid")
    @Expose
    private String bid;
    @SerializedName("Change")
    @Expose
    private String change;
    @SerializedName("ChangeinPercent")
    @Expose
    private String changeinPercent;

    protected ListItem(Parcel in) {
        symbol = in.readString();
        bid = in.readString();
        change = in.readString();
        changeinPercent = in.readString();
    }

    public static final Creator<ListItem> CREATOR = new Creator<ListItem>() {
        @Override
        public ListItem createFromParcel(Parcel in) {
            return new ListItem(in);
        }

        @Override
        public ListItem[] newArray(int size) {
            return new ListItem[size];
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
     * The symbol
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     *
     * @return
     * The bid
     */
    public String getBid() {
        return bid;
    }

    /**
     *
     * @param bid
     * The Bid
     */
    public void setBid(String bid) {
        this.bid = bid;
    }

    /**
     *
     * @return
     * The change
     */
    public String getChange() {
        return change;
    }

    /**
     *
     * @param change
     * The Change
     */
    public void setChange(String change) {
        this.change = change;
    }

    /**
     *
     * @return
     * The changeinPercent
     */
    public String getChangeinPercent() {
        return changeinPercent;
    }

    /**
     *
     * @param changeinPercent
     * The ChangeinPercent
     */
    public void setChangeinPercent(String changeinPercent) {
        this.changeinPercent = changeinPercent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(symbol);
        dest.writeString(bid);
        dest.writeString(change);
        dest.writeString(changeinPercent);
    }
}
