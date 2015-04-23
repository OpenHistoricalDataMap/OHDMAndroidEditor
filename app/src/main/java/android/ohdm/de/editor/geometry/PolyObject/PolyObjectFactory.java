package android.ohdm.de.editor.geometry.PolyObject;

import android.ohdm.de.editor.OHDMMapView;
import android.ohdm.de.editor.geometry.PolyObject.JSONUtilities.JSONReader;

import org.json.JSONObject;

public class PolyObjectFactory {

    private PolyObjectFactory(){

    }

    public static PolyObject buildObject(PolyObjectType type,OHDMMapView view){

        PolyObject polyObject = null;

        switch (type){
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
                throw new RuntimeException(type+" not implemented");
        }
        return polyObject;
    }

    public static PolyObject buildObjectFromJSON(JSONObject jsonObject, OHDMMapView view){
        return JSONReader.getPolyObjectFromJSONObject(jsonObject, view);
    }
}
