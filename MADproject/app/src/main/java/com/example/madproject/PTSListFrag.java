package com.example.madproject;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.app.ListFragment;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;
        import android.view.View;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class PTSListFrag extends ListFragment
{
    List<String> nameList = new ArrayList<>();
    List<Double> lonList = new ArrayList<>();
    List<Double> latList = new ArrayList<>();

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
                            PTSListFrag.this.nameList.add(name);
                            PTSListFrag.this.lonList.add(lon);
                            PTSListFrag.this.latList.add(lat);
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

    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LoadFromWeb t = new LoadFromWeb();
        t.execute();
        ArrayAdapter<String> adapter = new ArrayAdapter<String> (this.getActivity(),  android.R.layout.simple_list_item_1, this.nameList);
        setListAdapter(adapter);
    }

    public void onListItemClick(ListView lv, View v, int index, long id)
    {
        MainActivity activity = (MainActivity)getActivity();
        activity.receiveText(latList.get(index), lonList.get(index));
    }
}