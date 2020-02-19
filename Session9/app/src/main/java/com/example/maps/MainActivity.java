package com.example.maps;

import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuInflater;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class MainActivity extends AppCompatActivity
{

    MapView mv;
    boolean isRecording;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        // This line sets the user agent, a requirement to download OSM maps
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        setContentView(R.layout.activity_main);

        mv = findViewById(R.id.map1);

        mv.setMultiTouchControls(true);
        mv.getController().setZoom(16.0);
        mv.getController().setCenter(new GeoPoint(51.05,-0.72));

        if (savedInstanceState != null)
        {
            isRecording = savedInstanceState.getBoolean ("isRecording");
        }
    }


    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.choosemap)
        {
            // react to the menu item being selected...
            Intent intent = new Intent(this,MapChooseActivity.class);
            startActivityForResult(intent,3);
            return true;
        }
        if(item.getItemId() == R.id.setlocation)
        {
            Intent intent = new Intent(this,SetLocationActivity.class);
            startActivityForResult(intent,1);
            return true;
        }
        if(item.getItemId() == R.id.prefsactivity)
        {
            Intent intent = new Intent(this,MyPrefsActivity.class);
            startActivityForResult(intent,2);
            return true;
        }
        if(item.getItemId() == R.id.examlistactivity)
        {
            Intent intent = new Intent(this,examListActivity.class);
            startActivityForResult(intent,0);
            return true;
        }
        return false;
    }
    protected void onActivityResult(int requestCode,int resultCode,Intent intent)
    {

        if(requestCode==3)
        {

            if (resultCode==RESULT_OK)
            {
                Bundle extras=intent.getExtras();
                boolean hikebikemap = extras.getBoolean("com.example.hikebikemap");
                if(hikebikemap==true)
                {
                    mv.setTileSource(TileSourceFactory.HIKEBIKEMAP);
                }
                else
                {
                    mv.setTileSource(TileSourceFactory.MAPNIK);
                }
            }
        }
        if(requestCode==1)
        {
            if (resultCode==RESULT_OK)
            {
                Bundle extras=intent.getExtras();
                Double latitude = extras.getDouble("com.example.latitude");
                Double longitude = extras.getDouble("com.example.longtitude");

                mv.getController().setCenter(new GeoPoint(latitude, longitude));
            }
        }
    }

    public void onResume()
    {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        double lat = Double.parseDouble ( prefs.getString("lat", "50.9") );
        double lon = Double.parseDouble ( prefs.getString("lon", "-1.4") );
        double zoom = Double.parseDouble( prefs.getString("zoom","16"));
        boolean autodownload = prefs.getBoolean("autodownload", true);
        String mapType = prefs.getString("maptype", "NM");

        mv.getController().setCenter(new GeoPoint(lat, lon));
        mv.getController().setZoom(zoom);

        if("NM".equals(mapType))
        {
            mv.setTileSource(TileSourceFactory.MAPNIK);
        }
        else
        {
            mv.setTileSource(TileSourceFactory.HIKEBIKEMAP);
        }
    }

    public void onDestroy()
    {
        super.onDestroy();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean ("isRecording", isRecording);
        editor.commit();
    }

    public void onSaveInstanceState (Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("isRecording", isRecording);
    }
}