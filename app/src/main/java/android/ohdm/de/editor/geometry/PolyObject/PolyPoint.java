package android.ohdm.de.editor.geometry.PolyObject;

import android.content.Context;
import android.graphics.Color;
import android.ohdm.de.editor.geometry.PolyObject.ExtendedOverlay.ExtendedPointOverlay;

import org.osmdroid.bonuspack.overlays.OverlayWithIW;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Basic Element that can be drawn on the MapView.
 *
 */
public class PolyPoint extends PolyObject
{

    private static final long serialVersionUID = 0L;

    private static final int FILL_COLOR = Color.argb(128,0,0,255);
    private static final int FILL_COLOR_SELECTED = Color.argb(128,255,0,0);
    private static final int FILL_COLOR_EDIT = Color.argb(128,0,255,0);

    private transient ExtendedPointOverlay point;
    private List<GeoPoint> points = new ArrayList<GeoPoint>();

    /**
     * Constructor.
     *
     * @param context Context Interface to global information about an application environment.
     */
    PolyPoint(Context context)
    {
        super(PolyObjectType.POINT);
        this.internId = UUID.randomUUID();
        create(context);
    }

    /**
     * Draws the PolyGon on the MapView.
     *
     * @param context Context Interface to global information about an application environment.
     */
    protected void create(Context context)
    {
        point = new ExtendedPointOverlay(context);
        point.subscribe(this);

        point.setFillColor(FILL_COLOR);
        point.setStrokeWidth(4);
    }

    /**
     * //TODO needed to implement Icons on the map
     *
     * @return point ExtendedPointOverlay
     */
    @Override
    public OverlayWithIW getOverlay()
    {
        return point;
    }

    /**
     * Goes through the given list and adds them to the PolyPoint.
     *
     * @param points List<GeoPoints>
     */
    @Override
    public void setPoints(List<GeoPoint> points) {
        this.points = points;
        point.setPoints(this.points);
    }

    /**
     * Getter for points.
     *
     * @return List<GeoPoint>
     */
    public List<GeoPoint> getPoints(){
        return this.points;
    }

    /**
     * Removes the last Point added to the MapView.
     *
     */
    @Override
    public void removeLastPoint() {
        if(!points.isEmpty()){
            points.remove(points.size()-1);
        }

        setPoints(this.points);
    }

    /**
     * Adds a GeoPoint to to PolyPoint.points list.
     *
     * @param geoPoint GeoPoint
     */
    @Override
    public void addPoint(GeoPoint geoPoint) {
        points.add(geoPoint);
        setPoints(this.points);
    }

    /**
     * Sets the ClickEvent on the PolyPoint.
     *
     * @param clickable boolean
     */
    @Override
    public void setClickable(boolean clickable) {
        point.setClickable(clickable);
    }

    /**
     * Changes the appearance of the PolyPoint if selected or not selected.
     *
     * @param selected boolean
     */
    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;

        if(selected){
            point.setFillColor(FILL_COLOR_SELECTED);
        }else{
            point.setFillColor(FILL_COLOR);
        }
    }

    /**
     * Sets Point to the state editable or
     * removes  PolyPoint from the MapView.
     *
     * @param editing boolean
     */
    @Override
    public void setEditing(boolean editing) {
        this.editing = editing;

        if(editing){
            point.setFillColor(FILL_COLOR_EDIT);
        }else{
            point.setFillColor(FILL_COLOR);
        }
    }

    /**
     * Manages the ClickEvent on the Object that was clicked.
     *
     * @param clickObject Object
     */
    @Override
    public void onClick(Object clickObject) {
        for(PolyObjectClickListener listener : listeners){
            listener.onClick(this);
        }
    }

    /**
     * No functionality.
     *
     * @return
     */
    @Override
    public boolean removeSelectedEditPoint()
    {
        //nothing to do here...
        return true;
    }
}