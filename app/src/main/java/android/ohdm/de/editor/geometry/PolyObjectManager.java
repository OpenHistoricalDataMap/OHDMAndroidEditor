package android.ohdm.de.editor.geometry;


import android.ohdm.de.editor.OHDMMapView;
import android.ohdm.de.editor.activities.MainActivity;
import android.ohdm.de.editor.api.ApiConnect;
import android.ohdm.de.editor.geometry.PolyObject.PolyObject;
import android.ohdm.de.editor.geometry.PolyObject.PolyObjectClickListener;
import android.ohdm.de.editor.geometry.PolyObject.PolyObjectFactory;
import android.ohdm.de.editor.geometry.PolyObject.PolyObjectType;
import android.util.Log;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 *
 */
public class PolyObjectManager implements PolyObjectClickListener {

    private static final String TAG = "PolyObjectManager";

    private List<PolyObject> polyObjectList = new ArrayList<PolyObject>();
    private PolyObject activeObject = null;

    private OHDMMapView map;

    /**
     * Getter for map.
     *
     * @param map OHDMMapView
     */
    public PolyObjectManager(OHDMMapView map)
    {
        this.map = map;
    }

    /**
     * Adds PolyObject to the Map-View and PolyObject-List.
     *
     * @param polyObject PolyObject
     */
    public void addObject(PolyObject polyObject){

        polyObject.subscribe(this);

        polyObjectList.add(polyObject);
        map.getOverlays().add(polyObject.getOverlay());
    }

    /**
     * Removes PolyObject from the Map-View and PolyObject-List.
     *
     * @param polyObject PolyObject
     */
    private void removeObject(PolyObject polyObject){
        map.getOverlays().remove(polyObject.getOverlay());
        polyObjectList.remove(polyObject);
        map.invalidate();
    }

    /**
     * Setter for clickable.
     *
     * @param clickable boolean
     */
    public void setObjectsClickable(boolean clickable){
        for(PolyObject object : polyObjectList){
            object.setClickable(clickable);
        }
        map.invalidate();
    }

    /**
     * Deselects an PolyObject an the Map-View.
     */
    public void deselectActiveObject()
    {
        if(activeObject != null)
        {
            activeObject.setSelected(false);
            activeObject = null;
        }
        map.invalidate();
    }

    /**
     * Deselects currently selected PolyObject and selects the new one.
     *
     * @param polyObject PolyObject
     */
    @Override
    public void onClick(PolyObject polyObject) {
        deselectActiveObject();
        activeObject = polyObject;
        activeObject.setSelected(true);
        map.invalidate();
    }

    /**
     * Deletes selected PolyObject.
     *
     * @return boolean true deleted
     *                 false no selected PolyObject
     */
    public boolean removeSelectedObject()
    {
        if(activeObject != null)
        {
            removeObject(activeObject);
            activeObject = null;
            return true;
        }
        return false;
    }

    /**
     * Getter for List<PolyObject>.
     *
     * @return List<PolyObject>
     */
    public List<PolyObject> getPolyObjectList()
    {
        return polyObjectList;
    }

    /**
     * Sets Edit-Mode true or false of currently active PolyObject.
     *
     * @param editable boolean
     */
    public void setActiveObjectEditable(boolean editable)
    {
        if(activeObject != null)
        {
            activeObject.setEditing(editable);
        }
    }

    /**
     * Sets Edit-Mode true or false of currently selected PolyObject.
     *
     * @param editable boolean
     */
    public void setSelectedObjectEditable(boolean editable) {

        if(activeObject != null)
        {
            if(activeObject.isSelected())
            {
                activeObject.setEditing(editable);
            }
        }
    }

    /**
     * Getter for editable.
     *
     * @return boolean
     */
    public boolean isSelectedObjectEditable(){
        if(activeObject != null) {
            return activeObject.isEditing();
        }
        return false;
    }

    /**
     * Checks if an Object is active or not.
     *
     * @return boolean
     */
    public boolean hasActiveObject()
    {
        if(activeObject != null)
        {
            return true;
        }

        return false;
    }

    /**
     * Creates new PolyObject and adds it to the Map-View.
     *
     * @param type PolyObjectType
     */
    public void createAndAddPolyObject(PolyObjectType type)
    {
        activeObject = PolyObjectFactory.buildObject(type, map);
        activeObject.setEditing(true);
        addObject(activeObject);
    }

    /**
     * Adds the GPSTrack to the MapView.
     *
     * @param type PolyObjectType
     * @param geoPoints List<GeoPoint>
     */
    public void addGPSTrack(PolyObjectType type, List<GeoPoint> geoPoints)
    {
        PolyObject track = PolyObjectFactory.buildObject(type,this.map);
        track.setPoints(geoPoints);
        addObject(track);
    }

    /**
     * Getter for Tags of activeObject.
     *
     * @return HashMap<String,String>
     */
    public HashMap<String,String> getSelectedPolyObjectTags()
    {
        if(activeObject != null){
            return activeObject.getTags();
        }

        return new HashMap<String, String>();
    }

    /**
     * Setter for Tags for activeObject.
     *
     * @param tags HashMap<String,String>
     */
    public void setSelectedPolyObjectTags(HashMap<String,String> tags)
    {
        if(activeObject != null)
        {
            activeObject.setTags(tags);
        }else
        {
            Log.d(TAG,"no active object");
        }
    }

    /**
     * Getter for the InternId of the activeObject.
     *
     * @return UUID
     */
    public UUID getSelectedPolyObjectInternId()
    {
        if(activeObject != null)
        {
            return activeObject.getId();
        }
        return null;
    }

    /**
     * Getter for PolyObject with specific ID.
     *
     * @param id UUID
     */
    public void selectPolyObjectByInternId(UUID id)
    {
        for(PolyObject polyObject: polyObjectList)
        {
            if(polyObject.getId().equals(id))
            {
                activeObject = polyObject;
                activeObject.setSelected(true);
                map.invalidate();

                return;
            }
        }

        Log.d(TAG,"no polyobject found");
    }

    /**
     * Adds Point to selected PolyObject.
     *
     * @param point GeoPoint
     */
    public void addPointToSelectedPolyObject(GeoPoint point)
    {
        activeObject.addPoint(point);
        map.invalidate();
    }

    /**
     * Removes last Point from selected PolyObject.
     *
     */
    public void removeLastPointFromSelectedPolyObject() {
        if(activeObject != null) {
            activeObject.removeLastPoint();
            map.invalidate();
        }
    }

    /**
     * Removes selected EditPoint.
     *
     */
    public boolean removeSelectedEditPoint()
    {
        if(activeObject != null)
        {
            activeObject.removeSelectedEditPoint();
            map.invalidate();

            return true;
        }
        return false;
    }

    /**
     * Upload active PolyObject to the Server.
     *
     * @return boolean
     */
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

    /**
     * Adds PolyObjects.
     *
     * @param loadedPolyObjects PolyObject[]
     */
    public void addObjects(PolyObject[] loadedPolyObjects)
    {
        for(PolyObject polyObject : loadedPolyObjects)
        {
            addObject(polyObject);
        }
    }
}
