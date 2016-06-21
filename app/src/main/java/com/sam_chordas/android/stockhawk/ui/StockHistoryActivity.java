package com.sam_chordas.android.stockhawk.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.HistoricalItem;
import com.sam_chordas.android.stockhawk.rest.DividerItemDecoration;
import com.sam_chordas.android.stockhawk.rest.HistoryRecyclerAdapter;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class StockHistoryActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String KEY_END_DATE = "KEY_END_DATE";
    public static final String KEY_START_DATE = "KEY_START_DATE";
    public static final String EXTRA_SYMBOL = "EXTRA_SYMBOL";
    private static final String TAG = StockHistoryActivity.class.getSimpleName();
    private Button btnStart;
    private Button btnEnd;
    private ImageButton btnSearch;
    private RecyclerView recyclerHistory;
    private ProgressBar progressBar;
    private Calendar now;
    private OkHttpClient client = new OkHttpClient();
    private SimpleDateFormat simpleDateFormat;
    private final String urlTemplate = "select * from yahoo.finance.historicaldata where symbol = \"%s\" and startDate = \"%s\" and endDate = \"%s\"";
    private SharedPreferences prefs;
    private int mIdButton;
    private String start;
    private String end;
    private LinearLayout searchBar;
    private boolean isShowing=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_history);

        btnStart = (Button) findViewById(R.id.buttonStart);
        btnEnd = (Button) findViewById(R.id.buttonEnd);
        recyclerHistory = (RecyclerView) findViewById(R.id.recyclerHistory);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSearch = (ImageButton) findViewById(R.id.buttonSearch);
        searchBar = (LinearLayout) findViewById(R.id.search_bar);

        now = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        start = getStartDate();
        end = getEndDate();


        btnStart.setText(start);
        btnSearch.setContentDescription(start);
        btnEnd.setText(end);
        btnEnd.setContentDescription(end);
    }

    @Override
    protected void onResume() {
        super.onResume();

//        getSupportFragmentManager().beginTransaction().add(R.id.search_container, searchFragment).commit();
    }

    @NonNull
    private String getEndDate() {
        return prefs.getString(KEY_END_DATE, getString(R.string.set_end_time));
    }

    @NonNull
    private String getStartDate() {
        return prefs.getString(KEY_START_DATE, getString(R.string.set_start_time));
    }

    public void onButtonClicked(View v) {

        switch (v.getId()) {
            case R.id.buttonStart:
                mIdButton = R.id.buttonStart;
                showDateDialog(mIdButton);
                break;
            case R.id.buttonEnd:
                mIdButton = R.id.buttonEnd;
                showDateDialog(mIdButton);
                break;
            default:
                turnOnProgress();
                findStocks();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_activity, menu);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (searchBar.getVisibility() == View.GONE) {
//        if (!isShowing) {
            Log.d(TAG, "onOptionsItemSelected: gone");

            isShowing = true;

            searchBar.setVisibility(View.VISIBLE);
            searchBar.setAlpha(0.0f);

            searchBar.animate()
                    .setDuration(300)
                    .translationY(0.0f)
                    .alpha(1.0f).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    Log.d(TAG, "onAnimationStart: ");
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    Log.d(TAG, "onAnimationEnd: ");
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    Log.d(TAG, "onAnimationCancel: ");
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    Log.d(TAG, "onAnimationRepeat: ");
                }
            });
        } else {
            Log.d(TAG, "onOptionsItemSelected: visible");

            isShowing = false;

            searchBar.animate()
                    .setDuration(300)
                    .translationY(-searchBar.getHeight())
                    .alpha(0.0f)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            searchBar.setVisibility(View.GONE);
                        }
                    });
        }
        Toast.makeText(StockHistoryActivity.this, "hello", Toast.LENGTH_SHORT).show();
        return true;
    }

    private void showDateDialog(int id) {

        Calendar calendar = Calendar.getInstance();
        if (id == R.id.buttonStart) {
            try {
                calendar.setTime(simpleDateFormat.parse(getStartDate()));
            } catch (ParseException e) {
                e.printStackTrace();
                calendar = now;
            }
        } else if (id == R.id.buttonEnd) {
            try {
                calendar.setTime(simpleDateFormat.parse(getEndDate()));
            } catch (ParseException e) {
                e.printStackTrace();
                calendar = now;
            }
        }
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show(getFragmentManager(), "date picker dialog");
    }

    //    select * from yahoo.finance.historicaldata where symbol = "YHOO" and startDate = "20010-09-11" and endDate = "2010-03-10"
    private void findStocks() {
        String start = getStartDate();
        String end = getEndDate();
        String symbol = getIntent().getStringExtra(EXTRA_SYMBOL);

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

        try {
            JSONArray bids = new JSONObject(response).getJSONObject("query").getJSONObject("results").getJSONArray("quote");
            HistoricalItem[] results = new Gson().fromJson(bids.toString(), HistoricalItem[].class);

            initRecycler(results);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        turnOffProgress();
    }

    private void initRecycler(final HistoricalItem[] results) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerHistory.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerHistory.setAdapter(new HistoryRecyclerAdapter(getApplicationContext(), results));
                recyclerHistory.addItemDecoration(new DividerItemDecoration(getApplicationContext()));

            }
        });
    }

    private void showToast() {
        Toast.makeText(StockHistoryActivity.this, "Error(((", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int i, int i1, int i2) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(i, i1, i2);

        String chosenDate = simpleDateFormat.format(calendar.getTime());

        Button button = ((Button)findViewById(mIdButton));
        if (button != null) button.setText(chosenDate);

        saveChoice(chosenDate);
    }

    private void saveChoice(String chosenDate) {
        String prefKey = mIdButton == R.id.buttonStart ? KEY_START_DATE :
                (mIdButton == R.id.buttonEnd ? KEY_END_DATE : null);

        if (null == prefKey) return;

        prefs.edit().putString(prefKey, chosenDate).apply();
    }

    private void turnOnProgress(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }
    private void turnOffProgress(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        });

    }
}
