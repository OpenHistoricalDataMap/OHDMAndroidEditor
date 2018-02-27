package com.example.justin.editor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.justin.editor.config.Config;
import com.example.justin.editor.database.DatabaseHelper;
import com.example.justin.editor.maps.Map;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHelper dbh = new DatabaseHelper(this, "LAYERS", null, 1);

    }

    public void config(View v)
    {
        Intent i = new Intent(this,Config.class);
        startActivity(i);
    }

    public void startMap(View v)
    {
        Intent i = new Intent(this,Map.class);
        startActivity(i);
    }

}
