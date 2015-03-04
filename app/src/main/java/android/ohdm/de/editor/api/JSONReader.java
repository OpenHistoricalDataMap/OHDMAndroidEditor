package android.ohdm.de.editor.api;

import android.ohdm.de.editor.geometry.PolyObject.PolyObject;
import android.ohdm.de.editor.geometry.PolyObject.PolyObjectFactory;
import android.ohdm.de.editor.geometry.PolyObject.PolyObjectType;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.postgis.MultiLineString;
import org.postgis.MultiPoint;
import org.postgis.MultiPolygon;
import org.postgis.PGgeometry;
import org.postgis.Point;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class JSONReader {

    private static final String TAG = "JSONReader";
    private static final String GEOMETRICOBJECT = "geometricObjects";
    private static final String TAGDATES = "tagDates";
    private static final String TAGS = "tags";

    private static final String MULTILINESTRING = "multilinestring";
    private static final String MULTIPOINT = "multipoint";
    private static final String MULTIPOLYGON = "multipolygon";

    public static PolyObject getPolyObjectFromJSONObject(JSONObject jsonObject, MapView mapView) {

        PolyObject polyObject = null;
        PolyObjectType type;
        List<GeoPoint> geoPoints;
        HashMap<String,String> tagDates;

        if(jsonObject != null) {

            try {

                JSONArray geometricObject = jsonObject.getJSONArray(GEOMETRICOBJECT);
                JSONArray tagDatesObject = jsonObject.getJSONArray(TAGDATES);

                //TODO: können auch mehrere Geometrien sein
                JSONObject geom = (JSONObject) geometricObject.get(0);
                JSONObject tags = (JSONObject) tagDatesObject.get(0);

                type = getPolyObjectType(geom);
                geoPoints = getGeoPoints(geom);
                tagDates = getTagDates((JSONObject)tags.get(TAGS));

                polyObject = PolyObjectFactory.buildObject(type, mapView);
                polyObject.setPoints(geoPoints);
                polyObject.setTags(tagDates);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return polyObject;
    }

    private static HashMap<String,String> getTagDates(JSONObject tags) {

        Iterator<String> keys = tags.keys();
        HashMap<String,String> parsedTagDates = new HashMap<String,String>();

        while(keys.hasNext()){
            String key = keys.next();

            try {
                String value = tags.getString(key);
                Log.d(TAG,"key: "+key+", value: "+value);
                parsedTagDates.put(key,value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return parsedTagDates;
    }

    private static PolyObjectType getPolyObjectType(JSONObject geom) {

        PolyObjectType type = null;

        try {
            if (!geom.get(MULTIPOLYGON).toString().equals("null")) {
                type = PolyObjectType.POLYGON;
            } else if (!geom.get(MULTILINESTRING).toString().equals("null")) {
                type = PolyObjectType.POLYLINE;
            } else if (!geom.get(MULTIPOINT).toString().equals("null")) {
                type = PolyObjectType.POINT;
            } else {
                Log.e(TAG, "Unknown Geometric.");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return type;

    }

    private static List<GeoPoint> getGeoPoints(JSONObject geom) {

        List<GeoPoint> geoPoints = null;
        PolyObjectType type = getPolyObjectType(geom);

        try {
            switch (type) {
                case POINT:
                    geoPoints = extractGeoPointsFromMultiPoint(geom.get(MULTIPOINT).toString());
                    break;
                case POLYLINE:
                    geoPoints = extractGeoPointsFromMultiLineString(geom.get(MULTILINESTRING).toString());
                    break;
                case POLYGON:
                    geoPoints = extractGeoPointsFromMultipolygon(geom.get(MULTIPOLYGON).toString());
                    break;
                default:
                    Log.e(TAG, "Unknown PolyObjectType");
            }

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return geoPoints;
    }

    private static List<GeoPoint> extractGeoPointsFromMultiPoint(String input) {

        List<GeoPoint> geoPoints = null;

        PGgeometry geometry;

        try {
            geometry = new PGgeometry(input);
            MultiPoint multipoint = (MultiPoint) geometry.getGeometry();

            //TODO: können auch mehrere points sein (werden hier nicht korrekt als mehrere points behandelt)
            geoPoints = convertPointsToGeoPoints(multipoint.getPoints());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return geoPoints;
    }

    private static List<GeoPoint> extractGeoPointsFromMultiLineString(String input) {

        List<GeoPoint> geoPoints = null;

        PGgeometry geometry;

        try {
            geometry = new PGgeometry(input);
            MultiLineString lineString = (MultiLineString) geometry.getGeometry();

            //TODO: können auch mehrere lines sein
            geoPoints = convertPointsToGeoPoints(lineString.getLines()[0].getPoints());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return geoPoints;
    }

    private static List<GeoPoint> extractGeoPointsFromMultipolygon(String input) {

        List<GeoPoint> geoPoints = null;

        PGgeometry geometry;
        try {
            geometry = new PGgeometry(input);
            MultiPolygon polygon = (MultiPolygon) geometry.getGeometry();

            //TODO: können auch mehrere polygone UND mehrere "ringe" (in den Polygonen) sein
            geoPoints = convertPointsToGeoPoints(polygon.getPolygons()[0].getRing(0).getPoints());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return geoPoints;
    }

    private static List<GeoPoint> convertPointsToGeoPoints(Point[] points){

        List<GeoPoint> geoPoints = new ArrayList<GeoPoint>();

        for (Point point : points) {
            GeoPoint geoPoint = new GeoPoint(point.getY(),point.getX(),point.getZ());
            geoPoints.add(geoPoint);
        }

        return geoPoints;
    }
}
