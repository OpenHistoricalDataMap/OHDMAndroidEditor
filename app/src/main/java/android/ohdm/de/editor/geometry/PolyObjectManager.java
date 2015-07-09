package android.ohdm.de.editor.geometry;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.ohdm.de.editor.OHDMMapView;
import android.ohdm.de.editor.R;
import android.ohdm.de.editor.activities.MainActivity;
import android.ohdm.de.editor.api.ApiConnect;
import android.ohdm.de.editor.geometry.PolyObject.PolyObject;
import android.ohdm.de.editor.geometry.PolyObject.PolyObjectClickListener;
import android.ohdm.de.editor.geometry.PolyObject.PolyObjectFactory;
import android.ohdm.de.editor.geometry.PolyObject.PolyObjectType;
import android.util.Log;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PolyObjectManager implements PolyObjectClickListener {

    private static final String TAG = "PolyObjectManager";

    private Context context; // bekommt von der Activity um Resourcen laden zu können
    private List<PolyObject> polyObjectList = new ArrayList<PolyObject>();
    private PolyObject activeObject = null;

    private OHDMMapView map;

    public PolyObjectManager(OHDMMapView map)
    {
        this.context = map.getContext();
        this.map     = map;
    }

    /**
     * PolyObject wird zu der Liste aller PolyObjecte hinzugefügt.
     *
     * @param polyObject PolyObject
     */
    public void addObject(PolyObject polyObject){

        polyObject.subscribe(this);

        polyObjectList.add(polyObject);
        map.getOverlays().add(polyObject.getOverlay());

        if(polyObject.getPoints().isEmpty())
        {
            Log.d(TAG, "Keine GeoPoints für das Zeichen.");
        }
        else
        {
            ArrayList<OverlayItem> overlays = new ArrayList<OverlayItem>();
            OverlayItem overlay
                    = new OverlayItem("New Overlay", "Test", polyObject.getPoints().get(0));
            Drawable newMarker  = context.getResources().getDrawable(R.drawable.bunker_silo );
            overlay.setMarker(newMarker);
            overlays.add(overlay);
            ItemizedIconOverlay<OverlayItem> itemizedIconOverlay
                    = new ItemizedIconOverlay<OverlayItem>(this.context, overlays, null); // todo: null-> listener hinzufügen

            map.getOverlays().add(itemizedIconOverlay);

            Log.d(TAG, "Icon wird auf der Karte dargestellt.");
        }
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
            activeObject.setSelected(false);
            activeObject = null;
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

    public boolean removeSelectedObject() {

        if(activeObject != null){
            removeObject(activeObject);
            activeObject = null;
            return true;
        }
        return false;
    }

    public List<PolyObject> getPolyObjectList(){
        return polyObjectList;
    }

    public void setActiveObjectEditable(boolean editable) {

        if(activeObject != null){
            activeObject.setEditing(editable);
        }
    }

    public void setSelectedObjectEditable(boolean editable) {

        if(activeObject != null){
            if(activeObject.isSelected()) {
                activeObject.setEditing(editable);
            }
        }
    }

    public boolean isSelectedObjectEditable(){
        if(activeObject != null){
            return activeObject.isEditing();
        }
        return false;
    }

    public boolean hasActiveObject(){
        if(activeObject != null){
            return true;
        }
        return false;
    }

    public void createAndAddPolyObject(PolyObjectType type) {
        activeObject = PolyObjectFactory.buildObject(type, map);
        activeObject.setEditing(true);
        addObject(activeObject);
    }

    /**
     *
     *
     * @return HashMap<String,String>
     */
    public HashMap<String,String> getSelectedPolyObjectTags(){
        if(activeObject != null){
            return activeObject.getTags();
        }

        return new HashMap<String, String>();
    }

    /**
     *
     *
     * @param tags HashMap<String,String>
     */
    public void setSelectedPolyObjectTags(HashMap<String,String> tags){
        if(activeObject != null){
            activeObject.setTags(tags);
        }else{
            Log.d(TAG,"no active object");
        }
    }

    public UUID getSelectedPolyObjectInternId(){
        if(activeObject != null){
            return activeObject.getId();
        }
        return null;
    }

    public void selectPolyObjectByInternId(UUID id){

        for(PolyObject polyObject: polyObjectList){
            if(polyObject.getId().equals(id)){
                activeObject = polyObject;
                activeObject.setSelected(true);
                map.invalidate();
                return;
            }
        }

        Log.d(TAG,"no polyobject found");
    }
    
    public void addPointToSelectedPolyObject(GeoPoint point){
        activeObject.addPoint(point);
        map.invalidate();
    }

    public void removeLastPointFromSelectedPolyObject() {
        if(activeObject != null) {
            activeObject.removeLastPoint();
            map.invalidate();
        }
    }

    public boolean removeSelectedEditPoint() {
        if(activeObject != null) {
            activeObject.removeSelectedEditPoint();
            map.invalidate();
            return true;
        }
        return false;
    }

    public boolean uploadActivePolyObject(){

        if (activeObject != null){
            ApiConnect apiConnect = new ApiConnect(MainActivity.OHDMAPI_SERVER_ADDRESS);

            int responseCode = apiConnect.putPolyObject(activeObject.getAsJSONObject());

            return responseCode == ApiConnect.UPLOAD_RESPONSE_OK;
        }else{
            Log.d(TAG, "no active polyobject to upload");
            return false;
        }
    }

    public void addObjects(PolyObject[] loadedPolyObjects) {
        for(PolyObject polyObject : loadedPolyObjects){
            addObject(polyObject);
        }
    }
}
