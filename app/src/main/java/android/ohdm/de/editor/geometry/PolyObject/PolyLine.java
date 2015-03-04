package android.ohdm.de.editor.geometry.PolyObject;

import android.content.Context;
import android.graphics.Color;
import android.ohdm.de.editor.geometry.PolyObject.ExtendedOverlay.ExtendedPointOverlay;
import android.ohdm.de.editor.geometry.PolyObject.ExtendedOverlay.ExtendedPolylineOverlay;
import android.view.View;

import org.osmdroid.bonuspack.overlays.OverlayWithIW;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PolyLine extends PolyObject implements Serializable {

    private static final long serialVersionUID = 4209360273818925922L;

    private static final int FILL_COLOR = Color.argb(128, 0, 0, 255);
    private static final int FILL_COLOR_SELECTED = Color.argb(128, 255, 0, 0);
    private static final int FILL_COLOR_EDIT = Color.argb(128, 0, 255, 0);

    private transient ExtendedPolylineOverlay polyline;
    private transient List<ExtendedPointOverlay> cornerPoints = new ArrayList<ExtendedPointOverlay>();
    private transient ExtendedPointOverlay activeCornerPoint;
    private transient Map<ExtendedPointOverlay,GeoPoint> pointOverlayMap= new HashMap<ExtendedPointOverlay,GeoPoint>();
    private transient MapView view;

    private List<GeoPoint> points = new ArrayList<GeoPoint>();

    public PolyLine(MapView view){
        super(PolyObjectType.POLYLINE);
        this.internId = UUID.randomUUID();
        this.view = view;
        create(view.getContext());
    }

    @Override
    protected void create(Context context) {
        polyline = new ExtendedPolylineOverlay(context);

        polyline.subscribe(this);

        polyline.setColor(FILL_COLOR);
        polyline.setWidth(4);
        polyline.setPoints(points);
    }

    @Override
    public OverlayWithIW getOverlay() {
        return polyline;
    }

    @Override
    public void setPoints(List<GeoPoint> points) {
        this.points = points;
        polyline.setPoints(points);

        for(GeoPoint point : this.points){
            ExtendedPointOverlay cornerPoint = new ExtendedPointOverlay(view.getContext());

            List<GeoPoint> pointPoints = new ArrayList<GeoPoint>();
            pointPoints.add(point);
            cornerPoint.setPoints(pointPoints);

            cornerPoint.setClickable(true);
            cornerPoint.subscribe(this);

            cornerPoints.add(cornerPoint);

            pointOverlayMap.put(cornerPoint,point);
        }
    }

    public List<GeoPoint> getPoints(){
        return this.points;
    }

    @Override
    public void removeLastPoint() {
        if(!points.isEmpty()){
            points.remove(points.size()-1);
            ExtendedPointOverlay removePoint = cornerPoints.get(cornerPoints.size() - 1);
            view.getOverlays().remove(removePoint);
            cornerPoints.remove(removePoint);
            deselectActiveCornerPoint();
        }
        polyline.setPoints(this.points);
    }

    @Override
    public void addPoint(GeoPoint geoPoint) {

        if (activeCornerPoint == null) {

            points.add(geoPoint);
            polyline.setPoints(this.points);

            ExtendedPointOverlay cornerPoint = new ExtendedPointOverlay(view.getContext());

            List<GeoPoint> pointPoints = new ArrayList<GeoPoint>();
            pointPoints.add(geoPoint);
            cornerPoint.setPoints(pointPoints);

            cornerPoint.setClickable(true);
            cornerPoint.subscribe(this);

            cornerPoints.add(cornerPoint);

            pointOverlayMap.put(cornerPoint,geoPoint);

            view.getOverlays().add(cornerPoint);
        } else {
            List<GeoPoint> activeCornerPointPoints = activeCornerPoint.getPoints();
            activeCornerPointPoints.add(geoPoint);
            activeCornerPoint.setPoints(activeCornerPointPoints);

            GeoPoint oldPoint = pointOverlayMap.get(activeCornerPoint);

            for(int i=0; i<points.size(); i++){
                if(points.get(i) == oldPoint){
                    pointOverlayMap.put(activeCornerPoint,geoPoint);
                    points.set(i,geoPoint);
                    polyline.setPoints(this.points);
                    break;
                }
            }
        }
    }

    @Override
    public void setClickable(boolean clickable) {
        polyline.setClickable(clickable);
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;

        if(this.selected) {
            polyline.setColor(FILL_COLOR_SELECTED);
        }else {
            polyline.setColor(FILL_COLOR);
        }
    }

    @Override
    public void setEditing(boolean editing) {
        this.editing = editing;

        if (editing) {
            polyline.setColor(FILL_COLOR_EDIT);

            for (ExtendedPointOverlay point : this.cornerPoints) {
                view.getOverlays().add(point);
            }

        } else {
            polyline.setColor(FILL_COLOR);

            for (ExtendedPointOverlay point : cornerPoints) {
                view.getOverlays().remove(point);
            }

            deselectActiveCornerPoint();
        }
    }

    private void deselectActiveCornerPoint() {
        if (activeCornerPoint != null) {
            activeCornerPoint.setFillColor(FILL_COLOR);
            activeCornerPoint = null;
            view.invalidate();
        }
    }

    @Override
    public void removeSelectedCornerPoint() {
        if(activeCornerPoint != null){
            view.getOverlays().remove(activeCornerPoint);
            cornerPoints.remove(activeCornerPoint);

            GeoPoint removePoint = pointOverlayMap.get(activeCornerPoint);
            points.remove(removePoint);

            polyline.setPoints(points);
            activeCornerPoint = null;
        }
    }

    @Override
    public void onClick(Object clickObject) {

        if (clickObject instanceof ExtendedPointOverlay) {

            if(activeCornerPoint == (ExtendedPointOverlay)clickObject){
                deselectActiveCornerPoint();
            }else{
                deselectActiveCornerPoint();
                activeCornerPoint = (ExtendedPointOverlay) clickObject;
                activeCornerPoint.setFillColor(FILL_COLOR_EDIT);
                view.invalidate();
            }
        } else {
            for (PolyObjectClickListener listener : listeners) {
                listener.onClick(this);
            }
        }
    }
}
