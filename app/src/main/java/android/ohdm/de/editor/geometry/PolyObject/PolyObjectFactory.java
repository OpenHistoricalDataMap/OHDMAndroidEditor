package android.ohdm.de.editor.geometry.PolyObject;

import android.ohdm.de.editor.OHDMMapView;
import android.ohdm.de.editor.geometry.PolyObject.JSONUtilities.JSONReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 *
 */
public class PolyObjectFactory {

    private static final String TAG = "PolyObjectFactory";

    private PolyObjectFactory() {

    }

    /**
     * Creates a PolyObject(Line,Polygon,Point) depending on give type on the MapView.
     *
     * @param type PolyObjectType
     * @param view OHDMMapView
     *
     * @return PolyObject
     */
    public static PolyObject buildObject(PolyObjectType type, OHDMMapView view) {

        PolyObject polyObject;

        switch (type) {
            case POLYGON:
                polyObject = new PolyGon(view);
                break;
            case POLYLINE:
                polyObject = new PolyLine(view);
                break;
            case POINT:
                polyObject = new PolyPoint(view.getContext());
                break;
            default:
                throw new RuntimeException(type + " not implemented");
        }
        return polyObject;
    }

    /**
     * Creates an PolyObject from a JSONObject.
     *
     * @param jsonObject JSONObject
     * @param view OHDMMapView
     *
     * @return PolyObject
     */
    public static PolyObject buildObjectFromJSON(JSONObject jsonObject, OHDMMapView view) {
        return JSONReader.getPolyObjectFromJSONObject(jsonObject, view);
    }

    /**
     * Creates an PolyObject-Array from a JSONArray.
     *
     * @param jsonObjects JSONArray
     * @param map OHDMMapView
     * @return PolyObject[]
     */
    public static PolyObject[] buildObjectsFromJSON(JSONArray jsonObjects, OHDMMapView map) {

        ArrayList<PolyObject> polyObjects = new ArrayList<PolyObject>();

        for (int i = jsonObjects.length()-1; i >= 0; i--) {

            PolyObject polyObject = JSONReader.getPolyObjectFromJSONObject(jsonObjects.optJSONObject(i), map);

            if(polyObject != null){
                polyObjects.add(polyObject);
            }else{
                Log.d(TAG,"read an null-PolyObject");
            }

        }

        PolyObject[] polyObjectsArray = polyObjects.toArray(new PolyObject[polyObjects.size()]);

        return polyObjectsArray;
    }
}
