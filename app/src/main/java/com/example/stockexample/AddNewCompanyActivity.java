package com.example.stockexample;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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


public class AddNewCompanyActivity extends ActionBarActivity {

    private EditText newCompany_et;
    private Button addCompany_btn;
    private String newCompanyString;
    private MyCompany newCompany;
    private ArrayList<MyCompany> compList;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_company);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
        }

        Intent prevIntent = getIntent();
        compList = prevIntent.getExtras().getParcelableArrayList("companyListListView");

        newCompany_et = (EditText) findViewById(R.id.add_company_edittext);
        addCompany_btn = (Button) findViewById(R.id.add_button);

        newCompany_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    newCompanyString = newCompany_et.getText().toString();
                    if (newCompanyString.equals("")) {
                        Toast.makeText(getApplicationContext(), "Please enter a valid company symbol.", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    else {
                        makeUrl();
                        return true;
                    }
                }
                return false;
            }
        });

        addCompany_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newCompanyString = newCompany_et.getText().toString();
                if (newCompanyString.equals(""))
                    Toast.makeText(getApplicationContext(), "Please enter a valid company symbol.", Toast.LENGTH_SHORT).show();
                else
                    makeUrl();
            }
        });
    }

    private void addNewCompany() {
        Intent intent = new Intent(AddNewCompanyActivity.this, CompanyListActivity.class);
        if (compList.contains(newCompany)) {
            Toast toast = Toast.makeText(getApplicationContext(), "Company is already in list. Please enter a new company.", Toast.LENGTH_SHORT);
            LinearLayout layout = (LinearLayout) toast.getView();
            if (layout.getChildCount() > 0) {
                TextView tv = (TextView) layout.getChildAt(0);
                tv.setGravity(Gravity.CENTER);
            }
            toast.show();
            return;
        }
        compList.add(newCompany);
        intent.putExtra("companyListAdd", compList);
        startActivity(intent);
    }

    private void makeUrl() {
        String urlString1 = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quote%20where%20symbol%20in%20(%22";
        String urlString2 = "%22)&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
        String urlStringFinal = urlString1 + newCompanyString + urlString2;
        new HttpAsyncTask().execute(urlStringFinal);
    }

    private void verifyCompany(String result) throws JSONException {
        JSONObject reader = new JSONObject(result);
        JSONObject query = reader.getJSONObject("query");
        JSONObject results = query.getJSONObject("results");
        JSONObject quote = results.getJSONObject("quote");
        String stockExchange = quote.getString("StockExchange");
        if (stockExchange.equals("null")) {
            Toast.makeText(getApplicationContext(), "Please enter a valid company.", Toast.LENGTH_LONG).show();
        }

        else {
            String name = quote.getString("Name");
            String symbol = quote.getString("symbol");
            String stock = quote.getString("Change");
            newCompany = new MyCompany(symbol, name, stock);
            addNewCompany();
        }
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
                verifyCompany(result);
            }
            catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddNewCompanyActivity.this, CompanyListActivity.class);
        intent.putExtra("companyListAdd", compList);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_new_company, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

}
