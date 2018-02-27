package com.example.justin.editor.config;

/**
 * Created by Justin on 18.12.2017.
 */

public class Entry {
    private String name;
    private boolean checked;

    public Entry()
    {
        this("empty",false);
    }

    public Entry(String name, boolean checked)
    {
        this.name = name;
        this.checked = checked;
    }

    public String getName(){return this.name;}
    public boolean getChecked(){return this.checked;}

    public void setName(String name){this.name = name;}
    public void setChecked(boolean checked){this.checked = checked;}
}
