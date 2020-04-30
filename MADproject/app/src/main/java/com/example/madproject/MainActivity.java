package com.example.madproject;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LocationListener {

    MapView mv;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        setContentView(R.layout.activity_main);

        LocationManager mgr=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);

        mv = findViewById(R.id.map1);

        mv.setMultiTouchControls(true);
        mv.getController().setZoom(16.0);
        mv.getController().setCenter(new GeoPoint(51.05,-0.72));
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
            Intent intent = new Intent(this,SaveNewPTS.class);
            startActivity(intent);
            return true;
        } else if(item.getItemId() == R.id.preferences)
        {
            Intent intent = new Intent(this,Preferences.class);
            startActivity(intent);
            return true;
        } else if(item.getItemId() == R.id.loadpts)
        {
            Intent intent = new Intent(this,LoadPTS.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent intent)
    {
        ItemizedIconOverlay<OverlayItem> items;

        if(requestCode==0)
        {

            if (resultCode==RESULT_OK)
            {
                Bundle extras=intent.getExtras();
                String name = extras.getString("com.example.name");
                String type = extras.getString("com.example.type");
                String ppn = extras.getString("com.example.ppn");
//                               System.out.println("****************************************8");
//                System.out.println(name);
//                System.out.println(type);
//                System.out.println(ppn);
//                System.out.println("****************************************8");
// cannot yet press on marker
                items = new ItemizedIconOverlay<OverlayItem>(this, new ArrayList<OverlayItem>(), null);
                OverlayItem newmark = new OverlayItem(name, "type" + type + "price" + ppn, mv.getMapCenter());
                items.addItem(newmark);
                mv.getOverlays().add(items);
            }
        }
    }

    public void onLocationChanged(Location newLoc)
    {
        mv.getController().setCenter(new GeoPoint(newLoc.getLatitude(), newLoc.getLongitude()));
    }

    public void onProviderDisabled(String provider){}

    public void onProviderEnabled(String provider){}

    public void onStatusChanged(String provider,int status,Bundle extras){}
}
