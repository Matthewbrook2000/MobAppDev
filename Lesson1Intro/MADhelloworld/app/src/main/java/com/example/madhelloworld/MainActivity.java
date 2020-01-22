package com.example.madhelloworld;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv=new TextView(this);
        setContentView(R.layout.activity_main);
        tv.setText("Hello World!");
        setContentView(tv);
    }
}
