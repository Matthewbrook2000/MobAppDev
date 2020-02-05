package com.example.maps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SetLocationActivity extends AppCompatActivity implements View.OnClickListener{

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_location);

        Button b = (Button)findViewById(R.id.btn1);
        b.setOnClickListener(this);
    }

    public void onClick(View view) {
        TextView tv = (TextView) findViewById(R.id.tv1);
        EditText et = (EditText) findViewById(R.id.et1);
        TextView tv2 = (TextView) findViewById(R.id.tv2);
        EditText et2 = (EditText) findViewById(R.id.et2);

        Double longitude;
        Double latitude;

        if (et.getText().toString().isEmpty()) {
            new AlertDialog.Builder(this).setPositiveButton("OK", null).setMessage("enter longtitude").show();
            return;
        } else {
            longitude = Double.parseDouble(et.getText().toString());
        }
        if (et2.getText().toString().isEmpty()) {
            new AlertDialog.Builder(this).setPositiveButton("OK", null).setMessage("enter latitude").show();
            return;
        } else {
            latitude = Double.parseDouble(et2.getText().toString());
        }

        Intent intent = new Intent();
        Bundle bundle=new Bundle();
        bundle.putDouble("com.example.longtitude",longitude);
        bundle.putDouble("com.example.latitude",latitude);
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish();
    }
}
