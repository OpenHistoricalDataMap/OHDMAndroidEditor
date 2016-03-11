package android.ohdm.de.editor.geometry.PolyObject;

import android.content.Context;
import android.graphics.Color;
import android.ohdm.de.editor.OHDMMapView;
import android.ohdm.de.editor.geometry.PolyObject.ExtendedOverlay.EditPoint;
import android.ohdm.de.editor.geometry.PolyObject.ExtendedOverlay.ExtendedPolylineOverlay;

import org.osmdroid.bonuspack.overlays.OverlayWithIW;
import org.osmdroid.util.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Basic Element that can be drawn on the MapView.
 *
 */
public class PolyLine extends PolyObject implements Serializable {

    private static final long serialVersionUID = 4209360273818925922L;

    private static final int FILL_COLOR = Color.argb(128, 0, 0, 255);
    private static final int FILL_COLOR_SELECTED = Color.argb(128, 255, 0, 0);
    private static final int FILL_COLOR_EDIT = Color.argb(128, 0, 255, 0);

    private static final int LINE_WIDTH = 4;

    private transient ExtendedPolylineOverlay polyline;
    private transient OHDMMapView view;

    private transient List<EditPoint> editPoints = new ArrayList<EditPoint>();
    private transient EditPoint activeEditPoint;
    private transient Map<EditPoint,GeoPoint> pointOverlayMap= new HashMap<EditPoint,GeoPoint>();

    private List<GeoPoint> points = new ArrayList<GeoPoint>();

    /**
     * Constructor.
     *
     * @param view OHDMMapView there the PolyLine will be created.
     */
    public PolyLine(OHDMMapView view){
        super(PolyObjectType.POLYLINE);
        this.internId = UUID.randomUUID();
        this.view = view;
        create(view.getContext());
    }

    /**
     * Draws the PolyLine on the MapView.
     *
     * @param context Context Interface to global information about an application environment.
     */
    @Override
    protected void create(Context context) {
        polyline = new ExtendedPolylineOverlay(context);

        polyline.subscribe(this);

        polyline.setColor(FILL_COLOR);
        polyline.setWidth(LINE_WIDTH);
        polyline.setPoints(points);
    }

    /**
     * //TODO needed to implement Icons on the map
     *
     * @return polyline ExtendedPolylineOverlay
     */
    @Override
    public OverlayWithIW getOverlay() {
        return polyline;
    }

    /**
     * Goes through the given list and adds them to the PolyLine.
     *
     * @param points List<GeoPoints>
     */
    @Override
    public void setPoints(List<GeoPoint> points) {
        this.points = points;
        polyline.setPoints(points);

        for(GeoPoint point : this.points){
            EditPoint editPoint = new EditPoint(view);

            List<GeoPoint> pointPoints = new ArrayList<GeoPoint>();
            pointPoints.add(point);
            editPoint.setPoints(pointPoints);

            editPoint.setClickable(true);
            editPoint.subscribe(this);

            editPoints.add(editPoint);

            pointOverlayMap.put(editPoint,point);
        }
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
            EditPoint removePoint = editPoints.get(editPoints.size() - 1);
            view.getOverlays().remove(removePoint);
            editPoints.remove(removePoint);
            deselectActiveCornerPoint();
        }
        polyline.setPoints(this.points);
    }

    /**
     * Adds a Point to the MapView or if the Point already exists, then
     * the old one will be replaced this the new one.
     *
     * @param geoPoint GeoPoint
     */
    @Override
    public void addPoint(GeoPoint geoPoint) {

        if (activeEditPoint == null) {

            points.add(geoPoint);
            polyline.setPoints(this.points);

            createAndAddEditPoint(geoPoint);

        } else {
            List<GeoPoint> activeCornerPointPoints = activeEditPoint.getPoints();
            activeCornerPointPoints.add(geoPoint);
            activeEditPoint.setPoints(activeCornerPointPoints);

            GeoPoint oldPoint = pointOverlayMap.get(activeEditPoint);

            for(int i=0; i<points.size(); i++){
                if(points.get(i) == oldPoint){
                    pointOverlayMap.put(activeEditPoint,geoPoint);
                    points.set(i,geoPoint);
                    polyline.setPoints(this.points);
                    break;
                }
            }
        }
    }


    /**
     * Puts the GeoPoint on the MapView und adds EventListener.
     *
     * @param geoPoint GeoPoint
     */
    private void createAndAddEditPoint(GeoPoint geoPoint) {
        EditPoint editPoint = new EditPoint(view);

        List<GeoPoint> pointPoints = new ArrayList<GeoPoint>();
        pointPoints.add(geoPoint);
        editPoint.setPoints(pointPoints);

        editPoint.setClickable(true);
        editPoint.subscribe(this);

        editPoints.add(editPoint);

        pointOverlayMap.put(editPoint,geoPoint);

        view.getOverlays().add(editPoint);
    }


    /**
     * Sets oder removes the ClickEvent on the PolyLine.
     *
     * @param clickable boolean
     */
    @Override
    public void setClickable(boolean clickable) {
        polyline.setClickable(clickable);
    }

    /**
     * Changes the appearance of the PolyGon if selected or not selected.
     *
     * @param selected boolean
     */
    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;

        if(this.selected) {
            polyline.setColor(FILL_COLOR_SELECTED);
        }else {
            polyline.setColor(FILL_COLOR);
        }
    }


    /**
     * Sets all Points to the state editable or
     * removes all editPoints from the MapView.
     *
     * @param editing boolean
     */
    @Override
    public void setEditing(boolean editing) {
        this.editing = editing;

        if (editing) {
            polyline.setColor(FILL_COLOR_EDIT);

            for (EditPoint point : this.editPoints) {
                point.setPoints(point.getPoints());
                view.getOverlays().add(point);
            }

        } else {

            for (EditPoint point : editPoints) {
                view.getOverlays().remove(point);
            }

            deselectActiveCornerPoint();

            setSelected(this.selected);
        }
    }

    /**
     * Deselects active EditPoint on the MapView.
     *
     */
    private void deselectActiveCornerPoint() {
        if (activeEditPoint != null) {
            activeEditPoint.setFillColor(EditPoint.FILL_COLOR);
            activeEditPoint = null;
            view.invalidate();
        }
    }

    /**
     * Removes selected Point from the MapView.
     *
     * @return true  boolean if selected Point exists
     *         false boolean if selected Point not exists
     */
    @Override
    public boolean removeSelectedEditPoint() {
        if(activeEditPoint != null){
            view.getOverlays().remove(activeEditPoint);
            editPoints.remove(activeEditPoint);

            GeoPoint removePoint = pointOverlayMap.get(activeEditPoint);
            points.remove(removePoint);

            polyline.setPoints(points);
            activeEditPoint = null;
            return true;
        }
        return false;
    }

    /**
     * Manages the ClickEvent on the Object.
     *
     * @param clickObject Object
     */
    @Override
    public void onClick(Object clickObject) {

        if (clickObject instanceof EditPoint) {

            if(activeEditPoint == clickObject){
                deselectActiveCornerPoint();
            }else{
                deselectActiveCornerPoint();
                activeEditPoint = (EditPoint) clickObject;
                activeEditPoint.setFillColor(FILL_COLOR_EDIT);
                view.invalidate();
            }
        } else {
            for (PolyObjectClickListener listener : listeners) {
                listener.onClick(this);
            }
        }
    }
}
