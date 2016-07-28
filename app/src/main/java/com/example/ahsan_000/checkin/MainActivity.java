package com.example.ahsan_000.checkin;

import java.net.*;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    public String uName, uPassword;
    private String url_login;

    private Button logIN, abort;
    public EditText userName, userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init()
    {
        //Button are initialized
        this.logIN = (Button) findViewById(R.id.login);
        this.logIN.setOnClickListener(this);

        this.abort = (Button) findViewById(R.id.abort);
        this.abort.setOnClickListener(this);

        //Edit Texts are hereby initilized
        this.userName = (EditText)findViewById(R.id.userName);
        this.userName.setOnClickListener(this);

        this.userPassword = (EditText) findViewById(R.id.userPassword);
        this.userPassword.setOnClickListener(this);
    }
    @Override
    public void onClick(View v)
    {
        if(v == logIN)
        {
            uName = userName.getText().toString();
            uPassword = userPassword.getText().toString();

            url_login = "http://elitefashionbd.com/classroom/login.php?username=" + uName + "&password=" + uPassword;

            ProcessJSON obj = new ProcessJSON();
            obj.execute(url_login);
        }
        else if(v == abort)
        {
            finish();
        }
        else
            return;
    }

    public void CheckStatus(String str)
    {
        Context context = getApplicationContext();
        CharSequence text = str;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /*public void FetchingJSON()
    {
        try{
            URL url = new URL(url_login);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            String serverOutput;
            String Line="";


            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((serverOutput = br.readLine()) != null) {
                Line += serverOutput;
            }

            jsonObject = new JSONObject(Line);
            Context context = getApplicationContext();
            CharSequence text = uName;
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            //startActivity(new Intent(MainActivity.this, CheckIN.class));

        }catch (Exception ex) {
            //startActivity(new Intent(MainActivity.this, CheckIN.class));
            Context context = getApplicationContext();
            CharSequence text = ex.toString();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        //UserLogIn user = new UserLogIn(url_login, this);


    }*/

    private class ProcessJSON extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings) {
            String stream = null;
            String urlString = strings[0];

            UserLogIn user = new UserLogIn();
            stream = user.GetHTTPData(urlString);

            return stream;
        }

        @Override
        protected void onPostExecute(String stream) {
            if(stream != null) // Stream has got server generated string result
            {
                try{
                    JSONObject jObject = new JSONObject(stream);

                    String UserAuthentication = jObject.getString("status");

                    if (UserAuthentication.equals("1"))
                    {
                        CheckStatus("Log In Successful");
                        Intent i = new Intent(MainActivity.this, CheckIN.class);
                        i.putExtra("userName", uName);
                        i.putExtra("userPassword", uPassword);
                        startActivity(i);

                        //startActivity(new Intent(MainActivity.this, CheckIN.class));
                    }
                    else
                    {
                        CheckStatus("Authentication Failed");
                    }

                } catch (JSONException ex) {
                    CheckStatus(ex.getMessage()); //It'll generate exception Message
                }
            }
            else
                CheckStatus("Steam is EMPTY");

        }

    }
}