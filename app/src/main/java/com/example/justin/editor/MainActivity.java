package com.example.justin.editor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.justin.editor.config.Config;
import com.example.justin.editor.database.DatabaseHelper;
import com.example.justin.editor.maps.Map;

public class MainActivity extends AppCompatActivity {

    private boolean TESTMODE = false;

    Button config;
    TextView msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHelper dbh = new DatabaseHelper(this, "LAYERS", null, 1);

        config = (Button) findViewById(R.id.button3);
        msg = (TextView) findViewById(R.id.msg);

        if(!TESTMODE){
            config.setVisibility(View.INVISIBLE);
            msg.setText("Bei Dieser App handelt es sich momentan nur um einen Prototypen");
        }else{
            config.setVisibility(View.VISIBLE);
        }
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
