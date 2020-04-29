package com.example.asynctaskpost;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLEncoder;

import android.app.AlertDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    class MyTask extends AsyncTask<String, Void, String> {
        public String doInBackground(String... params) {
            HttpURLConnection conn = null;
            try {
                URL url = new URL("https://hikar.org/course/ws/addhit.php");
                conn = (HttpURLConnection) url.openConnection();

                String song = params[0];
                String artist = params[1];
                String year = params[2];
                song = URLEncoder.encode(song);
                artist = URLEncoder.encode(artist);
                year = URLEncoder.encode(year);
                String postData = "song=" + song + "&artist=" + artist + "&year=" + year;
                //String postData = "song=song123&artist=madonna&year=1930";

                System.out.println("**************** encoded url = " + postData);
                // For POST
                conn.setDoOutput(true);
                conn.setFixedLengthStreamingMode(postData.length());

                OutputStream out = null;
                out = conn.getOutputStream();
                out.write(postData.getBytes());
                if (conn.getResponseCode() == 200) {
                    InputStream in = conn.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String all = "", line;
                    while ((line = br.readLine()) != null)
                        all += line;
                    return all;
                } else {
                    return "HTTP ERROR: " + conn.getResponseCode();
                }
            } catch (IOException e) {
                return e.toString();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }

        public void onPostExecute(String result) {

            new AlertDialog.Builder(MainActivity.this).
                    setMessage("Server sent back: " + result).
                    setPositiveButton("OK", null).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button go = (Button) findViewById(R.id.btnDownload);
        go.setOnClickListener(this);
    }

    public void onClick(View v) {
        MyTask t = new MyTask();
        EditText song = findViewById(R.id.et1);
        EditText title = findViewById(R.id.et2);
        EditText year = findViewById(R.id.et3);

        String song2 = song.getText().toString();
        String title2 = title.getText().toString();
        String year2 = year.getText().toString();
        t.execute(song2, title2, year2);
    }
}

