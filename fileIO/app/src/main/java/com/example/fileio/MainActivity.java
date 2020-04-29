package com.example.fileio;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
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

    private final String FILE_LOCATION = Environment.getExternalStorageDirectory().getAbsolutePath() ;
    private final String FILENAME= "textedit.txt";
    private String currentFileLocation = FILE_LOCATION+"/"+FILENAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String filenm = prefs.getString("filenm", FILENAME);

        currentFileLocation = FILE_LOCATION +"/"+ filenm;

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
                File file = new File(currentFileLocation);
                System.out.println("****************** debug file="+file.getAbsolutePath());
                pw = new PrintWriter(new FileWriter(file));

                pw.println(et.getText().toString());

            } catch (IOException e) {
                new AlertDialog.Builder(this).setMessage ("Error loading").setPositiveButton("Dismiss", null).show();
            } finally{
                if (pw!=null) pw.close();
            }
            return true;
        }
        if (item.getItemId() == R.id.loadText) {
            try {
                File file = new File(currentFileLocation);
                BufferedReader reader = new BufferedReader(new FileReader(currentFileLocation));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    et.setText(line);
                }
            } catch (IOException e) {
                new AlertDialog.Builder(this).setMessage ("Error loading").setPositiveButton("Dismiss", null).show();

            }
            return true;
        }
        if (item.getItemId() == R.id.preferences) {
            Intent intent = new Intent(this,PrefsActivity.class);
            startActivityForResult(intent,2);
            return true;
        }

        return false;
    }

    public void onResume()
    {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String filenm = prefs.getString("filenm", "textedit.txt");

        currentFileLocation = FILE_LOCATION +"/"+ filenm;
    }

    public void onDestroy()
    {
        super.onDestroy();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.commit();
    }

    public void onSaveInstanceState (Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
    }
}
