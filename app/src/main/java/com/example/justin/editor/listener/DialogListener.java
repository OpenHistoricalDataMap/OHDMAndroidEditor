package com.example.justin.editor.listener;

import android.content.DialogInterface;

import com.example.justin.editor.maps.Map;

/**
 * Created by Justin on 03.03.2018.
 */

public class DialogListener implements DialogInterface.OnClickListener {
    private String t;
    Map callback;

    public DialogListener(Map callback)
    {
        this.callback = callback;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        switch (which) {
            case 0: // polygon
                t = "Polygon";
                break;
            case 1: // lineString
                t = "LineString";
                break;
            case 2: // point
                t = "Point";
                break;
            default :
                t = "error";
            }

            callback.cont();
    }

    public String getResult(){
        return this.t;
    }
}
