package com.sam_chordas.android.stockhawk.widget;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nkt1001 on 01.07.2016.
 */
public class WidgetService extends RemoteViewsService {
    private static final String TAG = WidgetService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        ListItem[] dataArray;
        JSONObject data=null;
        String myJson = intent.getStringExtra(StockWidget.EXTRA_STOCKS);

        Log.d(TAG, "onGetViewFactory: " + myJson);
        try {
            data = new JSONObject(myJson).getJSONObject("query").optJSONObject("results");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (data == null) return (new ListProvider(this.getApplicationContext(), intent));

        JSONObject object = data.optJSONObject("quote");

        if (object == null) {
            JSONArray array = data.optJSONArray("quote");
            dataArray = new Gson().fromJson(array.toString(), ListItem[].class);
            intent.putExtra(ListProvider.EXTRA_DATA, dataArray);
            return (new ListProvider(this.getApplicationContext(), intent));
        }

        dataArray = new ListItem[]{new Gson().fromJson(object.toString(), ListItem.class)};
        intent.putExtra(ListProvider.EXTRA_DATA, dataArray);

        return (new ListProvider(this.getApplicationContext(), intent));
    }
}
