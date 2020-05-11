package com.example.madproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener {

    MapView mv;
    ItemizedIconOverlay<OverlayItem> items;
    ItemizedIconOverlay.OnItemGestureListener<OverlayItem> markerGestureListener;
    private final String FILE_LOCATION = Environment.getExternalStorageDirectory().getAbsolutePath() ;
    private final String FILENAME= "savedpts.csv";
    private String currentFileLocation = FILE_LOCATION+"/"+FILENAME;
    List<String> toSavePTS = new ArrayList<>();

    //Set up auto load and auto save to web shit

    // portrait is just the same as usual, create another activity as normal
    // landscape layout is 2 fragments in horizontal orientation, with width of 0 pixels
    // landscape, vary simple, just have one xml file and 2 <fragment>'s inside it
    // use listfragment's to display list of places
    // for the rest follow code

    //make sure the files dont overwrite
    class LoadFromWeb extends AsyncTask<Void,Void,String>
    {
        public String doInBackground(Void... unused)
        {
            HttpURLConnection conn = null;
            try
            {
                URL url = new URL("https://www.hikar.org/course/ws/get.php?year=20&username=user010&format=csv");
                conn = (HttpURLConnection) url.openConnection();
                InputStream in = conn.getInputStream();
                if(conn.getResponseCode() == 200)
                {
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String result = "", line;
                    while((line = br.readLine()) !=null)
                    {
                        String[] ptscomponents = line.split(",");
                        if (ptscomponents.length == 5) {
                                    String name = ptscomponents[0];
                                    String type = ptscomponents[1];
                                    String ppn = ptscomponents[2];
                                    Double lon = Double.parseDouble(ptscomponents[3]);
                                    Double lat = Double.parseDouble(ptscomponents[4]);
                                    items = new ItemizedIconOverlay<OverlayItem>(MainActivity.this, new ArrayList<OverlayItem>(), markerGestureListener);
                                    OverlayItem newmark = new OverlayItem(name, "type" + type + "price" + ppn, new GeoPoint(lat, lon));
                                    items.addItem(newmark);
                                    mv.getOverlays().add(items);
                        }
                    }

                    return result;
                }
                else
                {
                    return "HTTP ERROR: " + conn.getResponseCode();
                }
            }
            catch(IOException e)
            {
                return e.toString();
            }
            finally
            {
                if(conn!=null)
                {
                    conn.disconnect();
                }
            }
        }

        public void onPostExecute(String result)
        {

        }
    }

    class PostToWeb extends AsyncTask<Void,Void,String>
    {
        public String doInBackground(Void... unused)
        {
            HttpURLConnection conn = null;
            try
            {
                URL url = new URL("https://www.hikar.org/course/ws/add.php");
                conn = (HttpURLConnection) url.openConnection();
                for(int i = 0; i < MainActivity.this.toSavePTS.size(); i++) {
                    String[] components = MainActivity.this.toSavePTS.get(i).split(",");
                    String name = components[0];
                    String type = components[1];
                    String ppn = components[2];
                    Double lon = Double.parseDouble(components[3]);
                    Double lat = Double.parseDouble(components[4]);

                    String postData = "username=user010&name=" + name + "&type=" + type +"&price=" + ppn + "&lat=" + lat + "&lon=" + lon + "&year=20";

                    conn.setDoOutput(true);
                    conn.setFixedLengthStreamingMode(postData.length());

                    OutputStream out = null;
                    out = conn.getOutputStream();
                    out.write(postData.getBytes());
                }
                if(conn.getResponseCode() == 200)
                {
                    InputStream in = conn.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String all = "", line;
                    while((line = br.readLine()) !=null)
                        all += line;
                    return all;
                }
                else
                {
                    return "HTTP ERROR: " + conn.getResponseCode();
                }
            }
            catch(IOException e)
            {
                return e.toString();
            }
            finally
            {
                if(conn!=null)
                {
                    conn.disconnect();
                }
            }
        }

        public void onPostExecute(String result)
        {

            new AlertDialog.Builder(MainActivity.this).
                    setMessage("Server sent back: " + result).
                    setPositiveButton("OK", null).show();
        }
    }


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

    }

    public void receiveText (Double lon, Double lat)
    {
        System.out.println("lon = " + lon + "lat = " + lat);
        mv.getController().setCenter(new GeoPoint(lat,lon));   //not moving yet probably onresume resetting it
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
        String filenm = prefs.getString("filenm", "textedit.csv");
        currentFileLocation = FILE_LOCATION +"/"+ filenm;
        boolean autodownload = prefs.getBoolean("autodownload", true);
        if(autodownload){
            PrintWriter pw=null;
            try {
                File file = new File(currentFileLocation);
                System.out.println("****************** debug file="+file.getAbsolutePath());
                pw = new PrintWriter(new FileWriter(file));
                for(int i = 0; i < toSavePTS.size(); i++) {
                    pw.println(toSavePTS.get(i));
                }

            } catch (IOException e) {
                new AlertDialog.Builder(this).setMessage ("Error loading").setPositiveButton("Dismiss", null).show();
            } finally{
                if (pw!=null) pw.close();
            }
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
                for(int i = 0; i < toSavePTS.size(); i++) {
                    pw.println(toSavePTS.get(i));
                }

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
                        String[] components = line.split(",");
                        if(components.length==5)
                        {
                            String name = components[0];
                            String type = components[1];
                            String ppn = components[2];
                            Double lon = Double.parseDouble(components[3]);
                            Double lat = Double.parseDouble(components[4]);
                            items = new ItemizedIconOverlay<OverlayItem>(this, new ArrayList<OverlayItem>(), markerGestureListener);
                            OverlayItem newmark = new OverlayItem(name, "type" + type + "price" + ppn, new GeoPoint(lat, lon));
                            items.addItem(newmark);
                            mv.getOverlays().add(items);
                        }
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
        } else if(item.getItemId() == R.id.loadptsfromweb)
        {
            LoadFromWeb t = new LoadFromWeb();
            t.execute();
            return true;
        } else if(item.getItemId() == R.id.saveptstoweb)
        {
            PostToWeb p = new PostToWeb();
            p.execute();
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
                toSavePTS.add(name + "," + type + "," + ppn + "," + mv.getMapCenter().getLongitude() + "," + mv.getMapCenter().getLatitude());
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
