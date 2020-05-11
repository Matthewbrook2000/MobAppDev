package com.example.fragstask2;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.app.FragmentManager;
import android.app.Fragment;


public class PersonDetailsActivity extends Activity
{

    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        if (getResources().getConfiguration().orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE)
        {
            finish();
            return;
        }

        setContentView(R.layout.persondetails);

        Intent intent = this.getIntent();
        if(intent!=null)
        {
            String value = intent.getExtras().getString("contents");
            if(value==null)
                value = "Contents not found";
            PersonDetailsFragment frag = (PersonDetailsFragment)getFragmentManager().findFragmentById(R.id.personDetailsFrag);
            frag.setText(value);
        }
    }

}
