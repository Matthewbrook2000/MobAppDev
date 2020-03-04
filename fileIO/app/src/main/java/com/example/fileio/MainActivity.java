package com.example.fileio;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity {

    private static final String FILE_LOCATION = Environment.getExternalStorageDirectory().getAbsolutePath() + "/textedit.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        EditText et = (EditText) findViewById(R.id.ET1);

        if (item.getItemId() == R.id.saveText) {
            PrintWriter pw=null;
            try {
                File file = new File(FILE_LOCATION);
                System.out.println("****************** debug file="+file.getAbsolutePath());
                pw = new PrintWriter(new FileWriter(file));

                pw.println(et.getText().toString());

            } catch (IOException e) {
                System.out.println("I/O Error: " + e);
            } finally{
                if (pw!=null) pw.close();
            }
            return true;
        }
        if (item.getItemId() == R.id.loadText) {
            try {
                File file = new File(FILE_LOCATION);
                BufferedReader reader = new BufferedReader(new FileReader(FILE_LOCATION));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    et.setText(line);
                }
            } catch (IOException e) {
                System.out.println("ERROR: " + e);

            }
            return true;
        }

        return false;
    }
}
