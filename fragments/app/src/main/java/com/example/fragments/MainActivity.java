package com.example.fragments;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PersonDetailsFragment personDetailsFrag = (PersonDetailsFragment)getFragmentManager().findFragmentById(R.id.personDetailsFrag);
        personDetailsFrag.setText("Details on a famous person will appear here!");
    }
}
