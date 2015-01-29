package com.example.stockexample;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class CompanyNewsActivity extends ActionBarActivity {

    private String companySymbol;
    private ArrayList companyList;
    private ListView companyNewsListView;
    private ArrayAdapter<String> companyNewsAdapter;
    private ArrayList<String> headlinesList = new ArrayList<String>();
    private ArrayList<String> urlList = new ArrayList<String>();
    private XmlPullParser parser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_news);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
        }

        companyNewsListView = (ListView) findViewById(R.id.company_news_list_view);

        Intent intent = getIntent();
        companySymbol = intent.getStringExtra("CompanySymbol");
        companyList = intent.getParcelableArrayListExtra("CompanyList");

        String url = "http://finance.yahoo.com/rss/headline?s=" + companySymbol;
        new DownloadXMLTask().execute(url);

        System.out.println("SIZE OF HEADLINES: " + headlinesList.size());
        System.out.println("SIZE OF URLS: " + urlList.size());

        for (int i = 0; i < headlinesList.size(); i++) {
            System.out.println(headlinesList.get(i));
        }
        for (int i = 0; i < urlList.size(); i++) {
            System.out.println(urlList.get(i));
        }

        companyNewsAdapter = new ArrayAdapter<String>(this, R.layout.news_row, headlinesList);
        companyNewsListView.setAdapter(companyNewsAdapter);

        companyNewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CompanyNewsActivity.this, WebViewActivity.class);
                intent.putExtra("CompanySymbol", companySymbol);
                intent.putExtra("CompanyListNews", companyList);
                intent.putExtra("NewsUrl", urlList.get(position));
                startActivity(intent);
            }
        });
    }

    private class DownloadXMLTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
           try {
               return loadXmlFromNetwork(urls[0]);
           }
           catch (IOException e) {
               return getResources().getString(R.string.connection_error);
           }
           catch (XmlPullParserException e) {
               return getResources().getString(R.string.xml_error);
           }
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }

    private String loadXmlFromNetwork(String url) throws IOException, XmlPullParserException {
        InputStream stream = null;
        try {
            stream = downloadUrl(url);
            parseXml(stream);
        }
        finally {
            if (stream != null) {
                stream.close();
            }
        }
        return null;
    }

    private InputStream downloadUrl(String url) throws IOException {
        URL url2 = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) url2.openConnection();
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(15000);
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.connect();
        return connection.getInputStream();
    }

    private void parseXml(InputStream in) throws XmlPullParserException, IOException {
        try {
            parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            readFeed();
        } finally {
            in.close();
        }
    }

    private void readFeed() throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "channel");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("item")) {
                readItem();
            }
            else {
                skip();
            }
        }
    }

    private void readItem() throws XmlPullParserException, IOException {
        System.out.println("READITEM");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("link")) {
                System.out.println("IT'S A LINK");
                urlList.add(readLink());
            }
            else if (name.equals("title")) {
                System.out.println("IT'S A TITLE");
                headlinesList.add(readTitle());
            }
            else {
                skip();
            }
        }
    }

    private String readTitle() throws IOException, XmlPullParserException {
        System.out.println("READTITLE");
        parser.require(XmlPullParser.START_TAG, null, "title");
        String title = readText();
        parser.require(XmlPullParser.END_TAG, null, "title");
        return title;
    }

    private String readLink() throws IOException, XmlPullParserException {
        System.out.println("READLINK");
        parser.require(XmlPullParser.START_TAG, null, "link");
        String link = readText();
        parser.require(XmlPullParser.END_TAG, null, "link");
        return link;
    }

    private String readText() throws IOException, XmlPullParserException {
        System.out.println("READTEXT");
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip() throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_company_news, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CompanyNewsActivity.this, CompanyInformationActivity.class);
        intent.putExtra("CompanyListNews", companyList);
        intent.putExtra("CompanySymbol", companySymbol);
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
