package com.example.fragstask2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
        import android.os.Bundle;
        import android.content.Intent;
        import android.util.Log;

public class MainActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void receiveText(String value)
    {
        PersonDetailsFragment frag = (PersonDetailsFragment)this.getFragmentManager().findFragmentById(R.id.personDetailsFrag);

        if (frag == null || !frag.isInLayout())
        {
            Intent intent = new Intent (this, PersonDetailsActivity.class);
            intent.putExtra("contents", value);
            startActivity(intent);
        }
        else
        {

            // set its contents
            frag.setText(value);
        }
    }
}