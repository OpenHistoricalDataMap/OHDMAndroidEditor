package com.example.justin.editor.json;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;
import java.util.List;
import com.example.justin.editor.maps.Map;

import static java.lang.System.out;

/**
 * Created by Justin on 01.03.2018.
 */

public class jsonService {
    private String type;
    private Map map;

    public jsonService(Map map){
        this.map = map;
    }

    public String createJson(List<GeoPoint> pts, String type) throws Exception{
        this.type = type;
        JSONArray features = new JSONArray();
        JSONArray coor = new JSONArray();
        for (GeoPoint point : pts){
            JSONArray coordinates = new JSONArray();
            coordinates.put(point.getLongitude());
            coordinates.put(point.getLatitude());
            coor.put(coordinates);
        }
        JSONObject geometry = new JSONObject();
        geometry.put("type", this.type);
        geometry.put("coordinates", coor);

        JSONObject prop = new JSONObject();
        prop.put("name", "test");

        JSONObject feature = new JSONObject();
        feature.put("type", "Feature");
        feature.put("properties", prop);
        feature.put("geometry", geometry);
        features.put(feature);
        JSONObject featureCollection = new JSONObject();
        featureCollection.put("type", "FeatureCollection");
        featureCollection.put("features", features);
        //Toast.makeText((Context) map, "success", Toast.LENGTH_LONG).show();

        return featureCollection.toString();
    }
    public String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/EditorOHDM";

}
