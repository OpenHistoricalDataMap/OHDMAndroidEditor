package com.example.justin.editor.config;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.justin.editor.R;
import com.example.justin.editor.database.DatabaseHelper;

import static android.R.attr.entries;


public class Config extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        ListView lv =(ListView) findViewById(R.id.ListView);
        DatabaseHelper dbh = new DatabaseHelper(this,"LAYERS",null,1);
        CustomAdapter ca = new CustomAdapter(this,dbh.getEntries());

        // DataBind ListView with items from ArrayAdapter
        lv.setAdapter(ca);

    }

            public static Entry[] getEntries(String[] layerNames)
            {
                Entry[] entries = new Entry[layerNames.length];

                for(int i = 0; i<layerNames.length; i++)
                {
                    entries[i] = new Entry(layerNames[i],false);
                }

                return entries;
            }
}
