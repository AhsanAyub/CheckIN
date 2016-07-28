package com.example.ahsan_000.checkin;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserAction extends AppCompatActivity implements View.OnClickListener
{
    public String RoomNumber, CourseName;
    private String uName, url, uPassword, message;
    private TextView setRoom, coursesList;
    private EditText messageBox;

    private char[] convertedMessage = new char[100];

    JSONObject jObject;

    private Button hitDatabase, exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_action);

        init();

        setJSONParsing();
    }

    private void setJSONParsing()
    {
        DownloadJSON obj = new DownloadJSON();
        obj.execute(url);
    }

    public void CheckStatus(String str)
    {
        Context context = getApplicationContext();
        CharSequence text = str;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void init()
    {
        this.hitDatabase = (Button)findViewById(R.id.hitDatabase);
        this.hitDatabase.setOnClickListener(this);

        this.setRoom = (TextView) findViewById(R.id.Room);
        this.setRoom.setOnClickListener(this);

        this.coursesList = (TextView)findViewById(R.id.sectionList);
        this.coursesList.setOnClickListener(this);

        this.exit = (Button)findViewById(R.id.exit);
        this.exit.setOnClickListener(this);

        this.messageBox = (EditText) findViewById(R.id.messageBox);
        this.messageBox.setOnClickListener(this);

        uName = getIntent().getExtras().getString("userName");
        uPassword = getIntent().getExtras().getString("userPassword");

        url = "http://elitefashionbd.com/classroom/message.php?username=" + uName;
    }

    @Override
    public void onClick(View v)
    {
        if(v == hitDatabase)
        {
            String messageURL;
            try {
                message = messageBox.getText().toString();
                message.replaceAll(" ", "%20");
                messageURL = "http://elitefashionbd.com/classroom/feed.php?username=" + uName + "&password=" + uPassword + "&message=" + message;
                CheckStatus(messageURL);

                PostRequest obj = new PostRequest();
                obj.execute(messageURL);

            }catch (Exception e)
            {
                CheckStatus("Access Denied!");
            }

        }
        else if (v == exit)
        {
            Intent i = new Intent(UserAction.this, CheckIN.class);
            i.putExtra("userName", uName);
            i.putExtra("userPassword", uPassword);
            startActivity(i);
        }
        else
            return;
    }

    private class DownloadJSON extends AsyncTask<String, Void, String>
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

                    coursesList.setText(jObject.getString("subject"));
                    setRoom.setText("Room: " + jObject.getString("room"));
                } catch (JSONException ex) {
                    CheckStatus(ex.getMessage()); //It'll generate exception Message
                }catch (Exception e)
                {
                    CheckStatus(e.getMessage());
                }

            }
            else
                CheckStatus("Steam is EMPTY");
        }
    }

    private class PostRequest extends AsyncTask<String, Void, String>
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
                    CheckStatus("Message Posted");
                }catch (Exception e) {
                    CheckStatus(e.getMessage());
                }
            }
            else
                CheckStatus("Steam is EMPTY");
        }
    }
}