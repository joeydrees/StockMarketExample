package com.example.stockexample;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class CompanyInformationActivity extends ActionBarActivity {

    private ArrayList<MyCompany> companyList;
    private String name;
    private String symbol;
    private TextView name_tv;
    private TextView symbol_tv;
    private TextView price_tv;
    private TextView change_tv;
    private TextView dayslow_tv;
    private TextView dayshigh_tv;
    private TextView yearslow_tv;
    private TextView yearshigh_tv;
    private TextView marketcap_tv;
    private TextView volume_tv;
    private TextView avgvolume_tv;
    private TextView stockexchange_tv;
    private Button newsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_information);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
        }

        Intent intent = getIntent();
        if (intent.hasExtra("CompanyListInfo") && intent.hasExtra("position")) {
            companyList = intent.getParcelableArrayListExtra("CompanyListInfo");
            int position = intent.getIntExtra("position", -1);
            symbol = companyList.get(position).getCompanySymbol();
        }
        else if (intent.hasExtra("CompanyListNews") && intent.hasExtra("CompanySymbol")) {
            companyList = intent.getParcelableArrayListExtra("CompanyListNews");
            symbol = intent.getStringExtra("CompanySymbol");
        }

        name_tv = (TextView) findViewById(R.id.info_name);
        symbol_tv = (TextView) findViewById(R.id.info_symbol);
        price_tv = (TextView) findViewById(R.id.info_price_edit);
        change_tv = (TextView) findViewById(R.id.info_change_edit);
        dayslow_tv = (TextView) findViewById(R.id.info_todayslow_edit);
        dayshigh_tv = (TextView) findViewById(R.id.info_todayshigh_edit);
        yearslow_tv = (TextView) findViewById(R.id.info_yearslow_edit);
        yearshigh_tv = (TextView) findViewById(R.id.info_yearshigh_edit);
        marketcap_tv = (TextView) findViewById(R.id.info_marketcap_edit);
        volume_tv = (TextView) findViewById(R.id.info_volume_edit);
        avgvolume_tv = (TextView) findViewById(R.id.info_avgvolume_edit);
        stockexchange_tv = (TextView) findViewById(R.id.info_exchange_edit);
        newsButton = (Button) findViewById(R.id.news_button);

        newsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanyInformationActivity.this, CompanyNewsActivity.class);
                intent.putExtra("CompanySymbol", symbol);
                intent.putExtra("CompanyList", companyList);
                startActivity(intent);
            }
        });

        String urlString1 = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quote%20where%20symbol%20in%20(%22";
        String urlString2 = "%22)&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
        String urlStringFinal = urlString1 + symbol + urlString2;
        new HttpAsyncTask().execute(urlStringFinal);
    }

    private void initializeCompany(String url) throws JSONException {
        JSONObject reader = new JSONObject(url);
        JSONObject query = reader.getJSONObject("query");
        JSONObject results = query.getJSONObject("results");
        JSONObject quote = results.getJSONObject("quote");

        name_tv.setText(quote.getString("Name"));
        symbol_tv.setText("(" + quote.getString("Symbol") + ")");
        price_tv.setText(quote.getString("LastTradePriceOnly"));
        change_tv.setText(quote.getString("Change"));
        dayslow_tv.setText(quote.getString("DaysLow"));
        dayshigh_tv.setText(quote.getString("DaysHigh"));
        yearslow_tv.setText(quote.getString("YearLow"));
        yearshigh_tv.setText(quote.getString("YearHigh"));
        marketcap_tv.setText(quote.getString("MarketCapitalization"));
        volume_tv.setText(quote.getString("Volume"));
        avgvolume_tv.setText(quote.getString("AverageDailyVolume"));
        stockexchange_tv.setText(quote.getString("StockExchange"));
    }

    private static String getRequest(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = httpClient.execute(new HttpGet(url));
            inputStream = httpResponse.getEntity().getContent();
            if (inputStream != null) {
                // convert inputstream to string
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while ((line = bufferedReader.readLine()) != null)
                    result += line;
                inputStream.close();
            }
            else
                result = "Did not work!";
        }
        catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            return getRequest(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                initializeCompany(result);
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_company_information, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CompanyInformationActivity.this, CompanyListActivity.class);
        intent.putExtra("companyListInfo", companyList);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
