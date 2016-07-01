package com.sam_chordas.android.stockhawk.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class StockWidget extends AppWidgetProvider {

    private static final String TAG = StockWidget.class.getSimpleName();

    public static final String EXTRA_STOCKS = "EXTRA_STOCKS";
    private StringBuilder mStoredSymbols = new StringBuilder();

    private RemoteViews updateWidgetListView(Context context,
                                             int appWidgetId, String data) {


        //which layout to show on widget
        RemoteViews remoteViews = new RemoteViews(
                context.getPackageName(), R.layout.stock_widget);

        //RemoteViews Service needed to provide adapter for ListView
        Intent svcIntent = new Intent(context, WidgetService.class);
        //passing app widget id to that RemoteViews Service
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        //setting a unique Uri to the intent
        //don't know its purpose to me right now
        svcIntent.setData(Uri.parse(
                svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

        svcIntent.putExtra(EXTRA_STOCKS, data);

        //setting adapter to listview of the widget
//        remoteViews.setRemoteAdapter(appWidgetId, R.id.listViewWidget,
//                svcIntent);

        remoteViews.setRemoteAdapter(R.id.listViewWidget, svcIntent);
        //setting an empty view in case of no data
        remoteViews.setEmptyView(R.id.listViewWidget, R.id.empty_view);
        return remoteViews;


    }

    private String buildUrl(Context context) {
        Log.d(TAG, "buildUrl: ");
        StringBuilder urlStringBuilder = new StringBuilder();
        try{
            // Base URL for the Yahoo query
            urlStringBuilder.append("https://query.yahooapis.com/v1/public/yql?q=");
            urlStringBuilder.append(URLEncoder.encode("select * from yahoo.finance.quotes where symbol "
                    + "in (", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Cursor initQueryCursor = context.getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                new String[]{"Distinct " + QuoteColumns.SYMBOL}, null,
                null, null);
        if (initQueryCursor.getCount() == 0 || initQueryCursor == null){
            // Init task. Populates DB with quotes for the symbols seen below
            try {
                urlStringBuilder.append(
                        URLEncoder.encode("\"YHOO\",\"AAPL\",\"GOOG\",\"MSFT\")", "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (initQueryCursor != null){
            DatabaseUtils.dumpCursor(initQueryCursor);
            initQueryCursor.moveToFirst();
            for (int i = 0; i < initQueryCursor.getCount(); i++){
                mStoredSymbols.append("\""+
                        initQueryCursor.getString(initQueryCursor.getColumnIndex("symbol"))+"\",");
                initQueryCursor.moveToNext();
            }
            mStoredSymbols.replace(mStoredSymbols.length() - 1, mStoredSymbols.length(), ")");
            try {
                urlStringBuilder.append(URLEncoder.encode(mStoredSymbols.toString(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        // finalize the URL for the API query.
        urlStringBuilder.append("&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables."
                + "org%2Falltableswithkeys&callback=");

        return urlStringBuilder.toString();
    }

    void fetchData(final Context context, final AppWidgetManager appWidgetManager, final String url, final int[] args) throws IOException {
        final OkHttpClient client = new OkHttpClient();
        Log.d(TAG, "fetchData: ");
        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        final Handler handler = new Handler(handlerThread.getLooper());

        handler.post(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                try {
                    String data = client.newCall(request).execute().body().string();
                    final int N = args.length;

                    for (int i = 0; i < N; ++i) {

                        RemoteViews remoteViews = updateWidgetListView(context, args[i], data);

                        appWidgetManager.updateAppWidget(args[i],
                            remoteViews);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//                Toast.makeText(context, "Internet connection error", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//                Log.d(TAG, "onResponse: ");
//                final int N = args.length;
//
//                for (int i = 0; i < N; ++i) {
//
//                    RemoteViews remoteViews = updateWidgetListView(context,
//                            args[i], response.body().string());
//
//                    appWidgetManager.updateAppWidget(args[i],
//                            remoteViews);
//                }
//            }
//        });
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "onUpdate: ");
        // There may be multiple widgets active, so update all of them
        String url = buildUrl(context);
        try {
            fetchData(context, appWidgetManager, url, appWidgetIds);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

