package com.example.ahsan_000.checkin;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class CheckIN extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener{

    private String uName, uPassword, routerSSID;
    private ToggleButton TrackMe;
    private Button LogOut;
    private static int routerID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        init();
    }

    private void init()
    {
        uName = getIntent().getExtras().getString("userName");
        uPassword = getIntent().getExtras().getString("userPassword");

        this.TrackMe = (ToggleButton) findViewById(R.id.TrackMe);
        this.TrackMe.setOnCheckedChangeListener(this);

        this.LogOut = (Button) findViewById(R.id.logout);
        this.LogOut.setOnClickListener(this);
    }

    public void CheckStatus(String str)
    {
        Context context = getApplicationContext();
        CharSequence text = str;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if(isChecked)
        {
            if(routerID > 4)
                routerID = 1;

            routerSSID = "http://elitefashionbd.com/classroom/feed.php?username=" + uName + "&password=" + uPassword + "&routerid=" + routerID;

            routerID += 1;

            sendRouterSSIDInformation obj = new sendRouterSSIDInformation();
            obj.execute(routerSSID);


            Intent i = new Intent(CheckIN.this, UserAction.class);
            i.putExtra("userName", uName);
            i.putExtra("userPassword", uPassword);
            startActivity(i);
        }
        else
            return;
    }

    @Override
    public void onClick(View v)
    {
        if(v == LogOut)
        {
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
            return;
    }

    private class sendRouterSSIDInformation extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings) {
            String stream = null;
            String urlString = strings[0];

            HitURL hitURL = new HitURL();
            stream = hitURL.postHTTPData(urlString);

            return stream;
        }

        @Override
        protected void onPostExecute(String stream) {
            if(stream != null) // Stream has got server generated string result
            {
                try {
                    CheckStatus("Router SSID Info sent");
                }catch (Exception e) {
                    CheckStatus(e.getMessage());
                }
            }
            else
                CheckStatus("Stream is EMPTY");
        }
    }
}