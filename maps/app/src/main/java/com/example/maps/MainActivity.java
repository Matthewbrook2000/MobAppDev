package com.example.maps;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    MapView mv;


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
        Button b = (Button)findViewById(R.id.btn1);
        b.setOnClickListener(this);
    }

    public void onClick(View view){
        TextView tv = (TextView)findViewById(R.id.tv1);
        EditText et = (EditText)findViewById(R.id.et1);
        TextView tv2 = (TextView)findViewById(R.id.tv2);
        EditText et2 = (EditText)findViewById(R.id.et2);

        Double longitude;
        Double latitude;

        if(et.getText().toString().isEmpty()) {
            new AlertDialog.Builder(this).setPositiveButton("OK", null).setMessage("enter longtitude").show();
            return;
        } else {
            longitude = Double.parseDouble(et.getText().toString());
        }
        if(et2.getText().toString().isEmpty()) {
            new AlertDialog.Builder(this).setPositiveButton("OK", null).setMessage("enter latitude").show();
            return;
        } else {
            latitude = Double.parseDouble(et2.getText().toString());
        }

        mv.getController().setCenter(new GeoPoint(latitude, longitude));

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
            startActivityForResult(intent,0);
            return true;
        }
        if(item.getItemId() == R.id.setlocation)
        {
            Intent intent = new Intent(this,SetLocationActivity.class);
            startActivityForResult(intent,1);
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
}