package com.example.stockexample;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button goButton = (Button) findViewById(R.id.go_button);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isConnected()) {
                    Toast toast = Toast.makeText(getApplicationContext(), "No internet connectivity. Please connect before continuing!", Toast.LENGTH_SHORT);
                    LinearLayout layout = (LinearLayout) toast.getView();
                    if (layout.getChildCount() > 0) {
                        TextView tv = (TextView) layout.getChildAt(0);
                        tv.setGravity(Gravity.CENTER);
                    }
                    toast.show();
                    return;
                }

                Intent prevIntent = getIntent();
                if (prevIntent.hasExtra("companyListBack")) {
                    Intent intent = new Intent(MainActivity.this, CompanyListActivity.class);
                    intent.putExtra("companyListMain", prevIntent.getExtras().getStringArrayList("companyListBack"));
                    startActivity(intent);
                } else if (prevIntent.hasExtra("companyListUp")) {
                    Intent intent = new Intent(MainActivity.this, CompanyListActivity.class);
                    intent.putExtra("companyListMain", prevIntent.getExtras().getStringArrayList("companyListUp"));
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this, CompanyListActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
