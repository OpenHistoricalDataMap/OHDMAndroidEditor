package com.example.justin.editor.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.justin.editor.config.Entry;

/**
 * Created by Justin on 18.12.2017.
 */


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String TAG = "DATABASE";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "";

        sql += "CREATE TABLE entries (\n" +
                "    name VARCHAR(30),\n" +
                "    checked INTEGER\n" +
                "); ";

        db.execSQL(sql);

        for(String layer : LAYERS)
        {
            sql = "INSERT INTO entries (name, checked)\n" +
                    "VALUES ('"+layer+"', 0); ";
            db.execSQL(sql);
        }


        Log.d(TAG,"Database created");
    }

    public Entry[] getEntries()
    {
        Cursor  cursor = getWritableDatabase().rawQuery("select * from entries",null);
        Entry[] entries = new Entry[cursor.getCount()];
        cursor.moveToFirst();

        for(int i = 0; i< cursor.getCount();i++)
        {
            entries[i] = new Entry(cursor.getString(0),(cursor.getInt(1) == 1));
            cursor.moveToNext();
        }

        return entries;
    }

    public void saveEntries(Entry[] entries)
    {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*
            switch(oldVersion)
            case 1: upgradeToVersion2(); // kein break !!!!
            case 2: upgradeToVersion3();
            case 3: upgradeToVersion4();
         */
    }

    public static final String[] LAYERS=
            {"ohdm_t%3Aboundaries_admin_2",
                    "ohdm_t%3Aboundaries_admin_3",
                    "ohdm_t%3Aboundaries_admin_4",
                    "ohdm_t%3Aboundaries_admin_5",
                    "ohdm_t%3Aboundaries_admin_6",
                    "ohdm_t%3Aboundaries_admin_7",
                    "ohdm_t%3Aboundaries_admin_8",
                    "ohdm_t%3Aboundaries_admin_9",
                    "ohdm_t%3Aboundaries_admin_10",
                    "ohdm_t%3Ahighway_huge_lines",
                    "ohdm_t%3Ahighway_primary_lines",
                    "ohdm_t%3Ahighway_secondary_lines",
                    "ohdm_t%3Ahighway_small_lines",
                    "ohdm_t%3Ahighway_tertiary_lines",
                    "ohdm_t%3Ahighway_path_lines",
                    "ohdm_t%3Arailway_lines",
                    "ohdm_t%3Ashop_points",
                    "ohdm_t%3Apublic_transport_points",
                    "ohdm_t%3Anatural_points",
                    "ohdm_t%3Aaeroway_points",
                    "ohdm_t%3Acraft_points",
                    "ohdm_t%3Abuilding_polygons",
                    "ohdm_t%3Anatural_polygons",
                    "ohdm_t%3Amilitary_polygons",
                    "ohdm_t%3Awaterway_polygons",
                    "ohdm_t%3Ageological_polygons",
                    "ohdm_t%3Aaeroway_polygons",
                    "ohdm_t%3Aemergency_polygons",
                    "ohdm_t%3Alanduse_brown",
                    "ohdm_t%3Alanduse_commercialetc",
                    "ohdm_t%3Alanduse_freegreenandwood",
                    "ohdm_t%3Alanduse_gardeningandfarm",
                    "ohdm_t%3Alanduse_grey",
                    "ohdm_t%3Alanduse_industrial",
                    "ohdm_t%3Alanduse_military",
                    "ohdm_t%3Alanduse_residentaletc",
                    "ohdm_t%3Alanduse_transport",
                    "ohdm_t%3Alanduse_water",
                    "ohdm_t%3Abuilding_polygons_label",
                    "ohdm_t%3Anatural_polygons_label",
                    "ohdm_t%3Amilitary_polygons_label",
                    "ohdm_t%3Awaterway_polygons_label",
                    "ohdm_t%3Ageological_polygons_label",
                    "ohdm_t%3Aaeroway_polygons_label",
                    "ohdm_t%3Aemergency_polygons_label",
                    "ohdm_t%3Alanduse_brown_label",
                    "ohdm_t%3Alanduse_commercialetc_label",
                    "ohdm_t%3Alanduse_freegreenandwood_label_label",
                    "ohdm_t%3Alanduse_gardeningandfarm_label",
                    "ohdm_t%3Alanduse_grey_label",
                    "ohdm_t%3Alanduse_industrial_label",
                    "ohdm_t%3Alanduse_military_label",
                    "ohdm_t%3Alanduse_residentaletc_label",
                    "ohdm_t%3Alanduse_transport_label",
                    "ohdm_t%3Alanduse_water_label"
            };
}
