package com.example.asynctask;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;


public class TestTask2 extends AsyncTask<Void, Void, String> {

    Context parent;

    public TestTask2(Context p) {

        parent = p;
    }

    public String doInBackground(Void... unused) {
        String message = "Successfully downloaded!";
        try {
            throw new IOException();// Network communication would go here;

        } catch (IOException e) {
            message = e.toString();
        }
        return message;
    }

    public void onPostExecute(String message) {
        new AlertDialog.Builder(parent).setMessage(message).
                setPositiveButton("OK", null).show();
    }

}
