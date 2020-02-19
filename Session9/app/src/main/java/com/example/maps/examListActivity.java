package com.example.maps;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.ListView;
import android.content.Context;

public class examListActivity extends ListActivity
{
    String[] names, details;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        names = new String[] { "Regular", "Hike/Bike Map" };
        details = new String[] { "Standard map", "Map for hiking and biking"};
        MyAdapter adapter = new MyAdapter();
        setListAdapter(adapter);
    }

    public void onListItemClick(ListView lv, View view, int index, long id)
    {
        Intent intent = new Intent();
        Bundle bundle=new Bundle();
        boolean hikebikemap=false;
        if (v.getId()==R.id.btnHikeBikeMap)
        {
            hikebikemap=true;
        }
        bundle.putBoolean("com.example.hikebikemap",hikebikemap);
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish();
    }


    public class MyAdapter extends ArrayAdapter<String>
    {
        public MyAdapter()
        {
            // We have to use ExampleListActivity.this to refer to the outer class (the activity)
            super(examListActivity.this, android.R.layout.simple_list_item_1, names);
        }

        public View getView(int index, View convertView, ViewGroup parent)
        {
            View view = convertView;
            if(view==null)
            {
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.poientry, parent, false);
            }
            TextView title = (TextView)view.findViewById(R.id.poi_name), detail =
                    (TextView)view.findViewById(R.id.poi_desc);
            title.setText(names[index]);
            detail.setText(details[index]);
            return view;
        }
    }
}
