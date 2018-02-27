package com.example.justin.editor.config;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.justin.editor.R;

/**
 * Created by Justin on 18.12.2017.
 */

public class CustomAdapter extends ArrayAdapter<Entry> {
    private Context context;
    private Entry[] entries;

    public CustomAdapter(Context context, Entry[] entries) {
        super(context, R.layout.row,entries);

        this.context = context;
        this.entries = entries;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.row, parent, false);
        TextView name = (TextView) convertView.findViewById(R.id.textView);
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox);
        name.setText(entries[position].getName());
        if(entries[position].getChecked())
            cb.setChecked(true);
        else
            cb.setChecked(false);
        return convertView;
    }
}
