package com.example.stockexample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class CompanyListActivity extends ActionBarActivity {

    private MyAdapter companyAdapter;
    private ArrayList<MyCompany> companyList;
    private ListView companyListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_list);

        if (savedInstanceState != null)
            companyList = savedInstanceState.getParcelableArrayList("myCompanyList");

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
        }

        companyListView = (ListView) findViewById(R.id.company_list_view);

        Intent intent = getIntent();
        if (intent.hasExtra("companyListAdd")) {
            companyList = intent.getExtras().getParcelableArrayList("companyListAdd");
            companyAdapter = new MyAdapter(this, companyList);
            companyListView.setAdapter(companyAdapter);
        }
        else if (intent.hasExtra("companyListMain")) {
            companyList = intent.getExtras().getParcelableArrayList("companyListMain");
            companyAdapter = new MyAdapter(this, companyList);
            companyListView.setAdapter(companyAdapter);
        }
        else if (intent.hasExtra("companyListInfo")) {
            companyList = intent.getExtras().getParcelableArrayList("companyListInfo");
            companyAdapter = new MyAdapter(this, companyList);
            companyListView.setAdapter(companyAdapter);
        }
        else {
            companyList = new ArrayList<MyCompany>();
            MyCompany aapl = new MyCompany("AAPL", "", "");
            MyCompany cvx = new MyCompany("CVX", "", "");
            MyCompany goog = new MyCompany("GOOG", "", "");
            MyCompany mmm = new MyCompany("MMM", "", "");
            MyCompany pg = new MyCompany("PG", "", "");
            companyList.add(aapl);
            companyList.add(cvx);
            companyList.add(goog);
            companyList.add(mmm);
            companyList.add(pg);
            String urlString = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quote%20where%20symbol%20in%20(%22aapl%22%2C%22cvx%22%2C%22goog%22%2C%22mmm%22%2C%22pg%22)&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
            new HttpAsyncTask().execute(urlString);
        }

        companyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CompanyListActivity.this, CompanyInformationActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("CompanyListInfo", companyList);
                startActivity(intent);
            }
        });

        companyListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                removeItemFromList(position);
                return true;
            }
        });
    }

    private void initializeCompany(String url) throws JSONException {
        JSONObject reader = new JSONObject(url);
        JSONObject query = reader.getJSONObject("query");
        JSONObject results = query.getJSONObject("results");
        JSONArray quote = results.getJSONArray("quote");
        for (int i = 0; i < companyList.size(); i++) {
            JSONObject company = quote.getJSONObject(i);
            String name = company.getString("Name");
            String stock = company.getString("Change");
            companyList.get(i).setCompanyName(name);
            companyList.get(i).setCompanyStock(stock);
        }
        companyAdapter = new MyAdapter(this, companyList);
        companyListView.setAdapter(companyAdapter);
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
            }
            catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
        }
    }

    private void removeItemFromList(int position) {
        final int deletePosition = position;
        AlertDialog.Builder alert = new AlertDialog.Builder(CompanyListActivity.this);
        alert.setTitle("Delete");
        alert.setMessage("Do you want to delete this item?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                companyList.remove(deletePosition);
                companyAdapter.notifyDataSetChanged();
                companyAdapter.notifyDataSetInvalidated();
            }
        });
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CompanyListActivity.this, MainActivity.class);
        intent.putExtra("companyListBack", companyList);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList("myCompanyList", companyList);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_company_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Intent intent = new Intent(CompanyListActivity.this, AddNewCompanyActivity.class);
            intent.putExtra("companyListListView", companyList);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_refresh) {
            makeUrl();
        }
        else if (id == R.id.action_map) {
            Intent intent = new Intent(CompanyListActivity.this, MapsActivity.class);
            intent.putExtra("CompanyList", companyList);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    private void makeUrl() {
        String urlString1 = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quote%20where%20symbol%20in%20(%22";
        String urlString2 = "%22)&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
        String urlCompanies = "";
        for (int i = 0; i < companyList.size() - 1; i++) {
            urlCompanies = urlCompanies + companyList.get(i).getCompanySymbol() + "%22%2C%22";
        }
        String urlLastCompany = companyList.get(companyList.size() - 1).getCompanySymbol();
        String urlStringFinal = urlString1 + urlCompanies + urlLastCompany + urlString2;
        new HttpAsyncTask().execute(urlStringFinal);
    }
}
