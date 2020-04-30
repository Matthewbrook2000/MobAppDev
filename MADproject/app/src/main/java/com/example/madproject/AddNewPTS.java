package com.example.madproject;

import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.content.Intent;
        import android.widget.Button;
        import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AddNewPTS extends AppCompatActivity implements View.OnClickListener
{

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addnewpts);

        Button submit = (Button)findViewById(R.id.addnew);
        submit.setOnClickListener(this);
    }

    public void onClick(View view)
    {
        EditText name = (EditText)findViewById(R.id.et1);
        EditText type = (EditText)findViewById(R.id.et2);
        EditText ppn = (EditText)findViewById(R.id.et3);

        Intent intent = new Intent();
        Bundle bundle=new Bundle();
        String nameString = name.getText().toString ();
        String typeString = type.getText().toString ();
        String ppnString = ppn.getText().toString ();
        bundle.putString("com.example.name",nameString);
        bundle.putString("com.example.type",typeString);
        bundle.putString("com.example.ppn",ppnString);
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish();
    }
}
