package android.ohdm.de.editor.geometry.PolyObject;

import android.ohdm.de.editor.OHDMMapView;

import org.apache.commons.lang3.NotImplementedException;

public class PolyObjectFactory {

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
                throw new NotImplementedException(type+" not implemented");
        }
        return polyObject;
    }
}
