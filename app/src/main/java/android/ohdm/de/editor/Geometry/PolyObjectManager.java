package android.ohdm.de.editor.Geometry;


import android.ohdm.de.editor.Geometry.PolyObject.PolyObject;
import android.ohdm.de.editor.Geometry.PolyObject.PolyObjectClickListener;

import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.List;

public class PolyObjectManager implements PolyObjectClickListener {

    private List<PolyObject> polyObjectList = new ArrayList<PolyObject>();
    private PolyObject selectedPolyObject = null;

    private MapView map;

    public PolyObjectManager(MapView map){
        this.map = map;
    }

    public void addObject(PolyObject polyObject){
        polyObject.subscribe(this);
        polyObjectList.add(polyObject);
        map.getOverlays().add(polyObject.getOverlay());
    }

    public void removeObject(PolyObject polyObject){
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

    public void setObjectsSelectable(boolean selectable){

        if(!selectable){
            selectedPolyObject = null;
        }

        for(PolyObject object : polyObjectList){
            object.setSelected(selectable);
        }
        map.invalidate();
    }

    public PolyObject getSelectedPolyObject(){
        return selectedPolyObject;
    }

    @Override
    public void onClick(PolyObject polyObject) {
        setObjectsSelectable(false);
        selectedPolyObject = polyObject;
        selectedPolyObject.setSelected(true);
        map.invalidate();
    }

    public void removeSelectedObject() {
        if(selectedPolyObject != null){
            removeObject(selectedPolyObject);
        }
    }

    public List<PolyObject> getPolyObjectList(){
        return polyObjectList;
    }
}
