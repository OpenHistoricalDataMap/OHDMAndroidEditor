package android.ohdm.de.editor.Geometry;


import android.ohdm.de.editor.Geometry.PolyObject.PolyObject;
import android.ohdm.de.editor.Geometry.PolyObject.PolyObjectClickListener;
import android.ohdm.de.editor.Geometry.PolyObject.PolyObjectFactory;
import android.ohdm.de.editor.Geometry.PolyObject.PolyObjectType;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.List;

public class PolyObjectManager implements PolyObjectClickListener {

    private List<PolyObject> polyObjectList = new ArrayList<PolyObject>();
    private PolyObject activeObject = null;

    private MapView map;

    public PolyObjectManager(MapView map){
        this.map = map;
    }

    public void addObject(PolyObject polyObject){
        polyObject.subscribe(this);
        polyObjectList.add(polyObject);
        map.getOverlays().add(polyObject.getOverlay());
    }

    private void removeObject(PolyObject polyObject){
        map.getOverlays().remove(polyObject.getOverlay());
        polyObjectList.remove(polyObject);
        map.invalidate();
    }

    public void setObjectsClickable(boolean clickable){
        for(PolyObject object : polyObjectList){
            object.setClickable(clickable);
        }
        map.invalidate();
    }

    public void deselectActiveObject(){

        if(activeObject != null) {
            this.activeObject.setSelected(false);
        }

        map.invalidate();
    }

    @Override
    public void onClick(PolyObject polyObject) {
        deselectActiveObject();
        activeObject = polyObject;
        activeObject.setSelected(true);
        map.invalidate();
    }

    public void removeSelectedObject() {
        if(activeObject != null){
            removeObject(activeObject);
        }
    }

    public List<PolyObject> getPolyObjectList(){
        return polyObjectList;
    }

    public boolean setSelectedObjectEditable(boolean editable) {

        if(activeObject != null){
            activeObject.setEditing(editable);
            return true;
        }

        return false;
    }

    public void createAndAddPolyObject(PolyObjectType type) {
        activeObject = PolyObjectFactory.buildObject(type, map);
        activeObject.setEditing(true);
        addObject(activeObject);
    }
    
    public void addPointToSelectedPolyObject(GeoPoint point){
        activeObject.addPoint(point);
    }

    public void removeLastPointFromSelectedPolyObject() {
        activeObject.removeLastPoint();
    }
}
