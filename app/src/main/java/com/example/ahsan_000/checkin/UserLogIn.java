package com.example.ahsan_000.checkin;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ahsan_000 on 20-Nov-15.
 */
public class UserLogIn extends Thread
{
    static String stream;

    public UserLogIn()
    {
        stream = null;
    }

    public String GetHTTPData(String URLString)
    {
        try
        {
            URL url = new URL(URLString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            if(urlConnection.getResponseCode() == 200)
            {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = r.readLine()) != null) {
                    sb.append(line);
                }
                stream = sb.toString();

                urlConnection.disconnect();

            }
            else
                return null;

        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return stream;
    }
}

