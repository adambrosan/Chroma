package com.seldetronics.abrosans.chroma;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.seldetronics.abrosans.chroma.MainActivity;

import java.util.ArrayList;

import java.util.ArrayList;

public class MainActivity extends Activity {
    public Chroma_Device defaultdevice;
    public Chroma_Device selecteddevice;
    private ListView list;
    private ArrayAdapter<Chroma_Device> adapter;
    private ArrayList<Chroma_Device> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        defaultdevice = new Chroma_Device("Chromaticity Light");
        final Intent chrom_intent = new Intent(this, Chromaticity_Control.class);

        list = (ListView) findViewById(R.id.listView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parentView, View childView, int position, long id) {

                selecteddevice = (Chroma_Device) list.getItemAtPosition(position);


                if (selecteddevice.Type == "ClearSwitch") {
                }
                if (selecteddevice.Type == "Chromaticity Light") {
                    startActivity(chrom_intent);
                }
                /*
                switch ((int)id) {
                    case 0: {
                        startActivity(switch_intent);
                        break;
                    }
                    case 1: {
                        startActivity(chrom_intent);
                        break;
                    }
                }*/


            }
        });
        arrayList = new ArrayList<Chroma_Device>();
        // Adapter: You need three parameters 'the context, id of the layout (it will be where the data is shown),
        // and the array that contains the data
        // adapter = new ArrayAdapter<ClearHome_Device>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
        // adapter = new ArrayAdapter<ClearHome_Device>(getApplicationContext(), R.layout.device_list, arrayList);
        adapter = new ArrayAdapter<Chroma_Device>(this, R.layout.device_list, R.id.textview1, arrayList);
        list.setAdapter(adapter);

        arrayList.add(defaultdevice);
        arrayList.add(new Chroma_Device("ClearSwitch"));
        // next thing you have to do is check if your adapter has changed
        adapter.notifyDataSetChanged();

    }


    public void device_clicked() {


    }
}

