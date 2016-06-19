package com.sam_chordas.android.stockhawk.ui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sam_chordas.android.stockhawk.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class StockHistoryActivity extends Activity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private static final String KEY_END_DATE = "KEY_END_DATE";
    private static final String KEY_START_DATE = "KEY_START_DATE";
    private static final String TAG = StockHistoryActivity.class.getSimpleName();
    private Button btnStart;
    private Button btnEnd;
    private RecyclerView recyclerHistory;
    private Calendar now;
    private OkHttpClient client = new OkHttpClient();
    private SimpleDateFormat simpleDateFormat;
    private final String urlTemplate = "select * from yahoo.finance.historicaldata where symbol = \"%s\" and startDate = \"%s\" and endDate = \"%s\"";
    private SharedPreferences prefs;
    private int mIdButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_history);

        btnStart = (Button) findViewById(R.id.buttonStart);
        btnEnd = (Button) findViewById(R.id.buttonEnd);
        recyclerHistory = (RecyclerView) findViewById(R.id.recyclerHistory);

        btnEnd.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        findViewById(R.id.buttonSearch).setOnClickListener(this);

        now = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String start = prefs.getString(KEY_START_DATE, "Set start time");
        String end = prefs.getString(KEY_END_DATE, "Set end time");

        btnStart.setText(start);
        btnEnd.setText(end);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buttonStart:
                mIdButton = R.id.buttonStart;
            case R.id.buttonEnd:
                mIdButton = R.id.buttonEnd;
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this,
                        now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show(getFragmentManager(), "date picker dialog");
                break;
            default:
                findStocks();
        }

    }
//    select * from yahoo.finance.historicaldata where symbol = "YHOO" and startDate = "20010-09-11" and endDate = "2010-03-10"
    private void findStocks() {
        String start = btnStart.getText().toString();
        String end = btnEnd.getText().toString();
        String symbol = "YHOO";

        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("https://query.yahooapis.com/v1/public/yql?q=");
        try {
            urlBuilder.append(URLEncoder.encode(String.format(urlTemplate, symbol, start, end), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        urlBuilder.append("&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=");

        try {
            execute(urlBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void execute(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                showToast();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                showData(response.body().string());
            }
        });
    }

    private void showData(String response) {
        Log.d(TAG, "showData: " + response);
    }

    private void showToast() {
        Toast.makeText(StockHistoryActivity.this, "No net(((", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int i, int i1, int i2) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(i, i1, i2);

        String chosenDate = simpleDateFormat.format(calendar.getTime());

        ((Button)findViewById(mIdButton)).setText(chosenDate);

        saveChoice(chosenDate);
    }

    private void saveChoice(String chosenDate) {
        String prefKey = mIdButton == R.id.buttonStart ? KEY_START_DATE :
                (mIdButton == R.id.buttonEnd ? KEY_END_DATE : null);

        if (null == prefKey) return;

        prefs.edit().putString(prefKey, chosenDate).apply();
    }

}
