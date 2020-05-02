package com.example.madproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LocationListener {

    MapView mv;
    ItemizedIconOverlay<OverlayItem> items;
    ItemizedIconOverlay.OnItemGestureListener<OverlayItem> markerGestureListener;
    private final String FILE_LOCATION = Environment.getExternalStorageDirectory().getAbsolutePath() ;
    private final String FILENAME= "savedpts.txt";
    private String currentFileLocation = FILE_LOCATION+"/"+FILENAME;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        setContentView(R.layout.activity_main);

        String filenm = FILENAME;

        currentFileLocation = FILE_LOCATION +"/"+ filenm;
        LocationManager mgr=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);

        mv = findViewById(R.id.map1);

        mv.setMultiTouchControls(true);
        mv.getController().setZoom(16.0);
        mv.getController().setCenter(new GeoPoint(51.05,-0.72));
        // set up save to save the current pts (probably along the lines of creating a global array variable which all pts are added to when they are created and toString)
        //set up load to load all pts (file I/O task has details on breaking down strings that have been read)
        // set up prefs to auto save (set a pref to the option, then when a marker is created have an IF statement checking the pref if its on then save it, also in onResume have an if statement checking the pref if it is on save all to file)
        //
//SAVE AS NAME,DESC,PRICE,LON,LAT
//        items = new ItemizedIconOverlay<OverlayItem>(this, new ArrayList<OverlayItem>(), markerGestureListener);
//        OverlayItem fernhurst = new OverlayItem("Fernhurst", "the village of Fernhurst", new GeoPoint(51.2682, -0.8508));
//        items.addItem(fernhurst);
//        mv.getOverlays().add(items);

    }
    @Override
    public void onStart()
    {
        super.onStart();
        markerGestureListener = new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>()
        {
            public boolean onItemLongPress(int i, OverlayItem item)
            {
                Toast.makeText(MainActivity.this, item.getSnippet(), Toast.LENGTH_SHORT).show();
                return true;
            }

            public boolean onItemSingleTapUp(int i, OverlayItem item)
            {
                Toast.makeText(MainActivity.this, item.getSnippet(), Toast.LENGTH_SHORT).show();
                return true;
            }
        };

    }

    public void onResume()
    {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String filenm = prefs.getString("filenm", "textedit.txt");

        currentFileLocation = FILE_LOCATION +"/"+ filenm;
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.addnewpts)
        {
            Intent intent = new Intent(this,AddNewPTS.class);
            startActivityForResult(intent,0);
            return true;
        } else if(item.getItemId() == R.id.savepts)
        {
            PrintWriter pw=null;
            try {
                File file = new File(currentFileLocation);
                System.out.println("****************** debug file="+file.getAbsolutePath());
                pw = new PrintWriter(new FileWriter(file));

                pw.println(et.getText().toString()); //change this to print the details of markers

            } catch (IOException e) {
                new AlertDialog.Builder(this).setMessage ("Error loading").setPositiveButton("Dismiss", null).show();
            } finally{
                if (pw!=null) pw.close();
            }
            return true;
        } else if(item.getItemId() == R.id.loadpts)
        {
            try {
                File file = new File(currentFileLocation);
                BufferedReader reader = new BufferedReader(new FileReader(currentFileLocation));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    et.setText(line);               //change this to display all markers
                }
            } catch (IOException e) {
                new AlertDialog.Builder(this).setMessage ("Error loading").setPositiveButton("Dismiss", null).show();

            }
            return true;
        } else if(item.getItemId() == R.id.preferences)
        {
            Intent intent = new Intent(this,Preferences.class);
            startActivity(intent);              //figure out how to automatically save pts to file
            return true;
        }
        return false;
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent intent)
    {

        if(requestCode==0)
        {

            if (resultCode==RESULT_OK)
            {

                Bundle extras=intent.getExtras();
                String name = extras.getString("com.example.name");
                String type = extras.getString("com.example.type");
                String ppn = extras.getString("com.example.ppn");
                items = new ItemizedIconOverlay<OverlayItem>(this, new ArrayList<OverlayItem>(), markerGestureListener);
                OverlayItem newmark = new OverlayItem(name, "type" + type + "price" + ppn, mv.getMapCenter());
                items.addItem(newmark);
                mv.getOverlays().add(items);

            }
        }
    }

    public void onDestroy()
    {
        super.onDestroy();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.commit();
    }

    public void onLocationChanged(Location newLoc)
    {
        mv.getController().setCenter(new GeoPoint(newLoc.getLatitude(), newLoc.getLongitude()));
    }

    public void onProviderDisabled(String provider){}

    public void onProviderEnabled(String provider){}

    public void onStatusChanged(String provider,int status,Bundle extras){}
}
