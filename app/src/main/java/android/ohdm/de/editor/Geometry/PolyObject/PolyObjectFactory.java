package android.ohdm.de.editor.Geometry.PolyObject;

import org.apache.commons.lang3.NotImplementedException;
import org.osmdroid.views.MapView;

public class PolyObjectFactory {

    public static PolyObject buildObject(PolyObjectType type,MapView view){

        PolyObject polyObject = null;

        switch (type){
            case POLYGON:
                polyObject = new PolyGon(view.getContext());
                break;
            case POLYLINE:
                polyObject = new PolyLine(view.getContext());
                break;
            case POINT:
                polyObject = new ExPolyPoint(view.getContext());
                break;
            default:
                throw new NotImplementedException(type+" not implemented");
        }
        return polyObject;
    }
}
