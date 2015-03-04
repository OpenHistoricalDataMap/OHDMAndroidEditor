package android.ohdm.de.editor.geometry.PolyObject;

import android.content.Context;
import android.graphics.Color;
import android.ohdm.de.editor.geometry.PolyObject.ExtendedOverlay.ExtendedPointOverlay;
import android.ohdm.de.editor.geometry.PolyObject.ExtendedOverlay.ExtendedPolygonOverlay;

import org.osmdroid.bonuspack.overlays.OverlayWithIW;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PolyGon extends PolyObject {

    private static final long serialVersionUID = 0L;

    private static final int FILL_COLOR = Color.argb(128, 0, 0, 255);
    private static final int FILL_COLOR_SELECTED = Color.argb(128, 255, 0, 0);
    private static final int FILL_COLOR_EDIT = Color.argb(128, 0, 255, 0);

    private transient ExtendedPolygonOverlay polygon;
    private transient MapView view;
    private transient List<ExtendedPointOverlay> editPoints = new ArrayList<ExtendedPointOverlay>();
    private transient ExtendedPointOverlay activeEditPoint;
    private transient Map<ExtendedPointOverlay,GeoPoint> pointOverlayMap= new HashMap<ExtendedPointOverlay,GeoPoint>();

    private List<GeoPoint> points = new ArrayList<GeoPoint>();

    PolyGon(MapView view) {
        super(PolyObjectType.POLYGON);
        this.view = view;
        internId = UUID.randomUUID();
        create(view.getContext());
    }

    @Override
    protected void create(Context context) {
        polygon = new ExtendedPolygonOverlay(context);
        polygon.subscribe(this);
        polygon.setFillColor(FILL_COLOR);
        polygon.setStrokeWidth(4);
        polygon.setPoints(points);
    }

    @Override
    public OverlayWithIW getOverlay() {
        return polygon;
    }

    @Override
    public void setPoints(List<GeoPoint> points) {
        this.points = points;
        polygon.setPoints(points);

        for(GeoPoint point : this.points){
            ExtendedPointOverlay editPoint = new ExtendedPointOverlay(view.getContext());

            List<GeoPoint> pointPoints = new ArrayList<GeoPoint>();
            pointPoints.add(point);
            editPoint.setPoints(pointPoints);

            editPoint.setClickable(true);
            editPoint.subscribe(this);

            editPoints.add(editPoint);

            pointOverlayMap.put(editPoint,point);
        }
    }

    public List<GeoPoint> getPoints() {
        return this.points;
    }

    @Override
    public void removeLastPoint() {
        if (!points.isEmpty()) {
            points.remove(points.size() - 1);
            ExtendedPointOverlay removePoint = editPoints.get(editPoints.size() - 1);
            view.getOverlays().remove(removePoint);
            editPoints.remove(removePoint);
            deselectActiveEditPoint();
        }
        polygon.setPoints(this.points);
    }

    @Override
    public void addPoint(GeoPoint geoPoint) {

        if (activeEditPoint == null) {

            points.add(geoPoint);
            polygon.setPoints(this.points);

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
                    polygon.setPoints(this.points);
                    break;
                }
            }
        }
    }

    private void createAndAddEditPoint(GeoPoint geoPoint){
        ExtendedPointOverlay editPoint = new ExtendedPointOverlay(view.getContext());

        List<GeoPoint> pointPoints = new ArrayList<GeoPoint>();
        pointPoints.add(geoPoint);
        editPoint.setPoints(pointPoints);

        editPoint.setClickable(true);
        editPoint.subscribe(this);

        editPoints.add(editPoint);

        pointOverlayMap.put(editPoint,geoPoint);

        view.getOverlays().add(editPoint);
    }

    @Override
    public void setClickable(boolean clickable) {
        polygon.setClickable(clickable);
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;

        if (selected) {
            polygon.setFillColor(FILL_COLOR_SELECTED);
        } else {
            polygon.setFillColor(FILL_COLOR);
        }
    }

    @Override
    public void removeSelectedEditPoint() {
        if(activeEditPoint != null){
            view.getOverlays().remove(activeEditPoint);
            editPoints.remove(activeEditPoint);

            GeoPoint removePoint = pointOverlayMap.get(activeEditPoint);
            points.remove(removePoint);

            polygon.setPoints(points);
            activeEditPoint = null;
        }
    }

    @Override
    public void setEditing(boolean editing) {
        this.editing = editing;

        if (editing) {
            polygon.setFillColor(FILL_COLOR_EDIT);

            for (ExtendedPointOverlay point : this.editPoints) {
                view.getOverlays().add(point);
            }

        } else {
            polygon.setFillColor(FILL_COLOR);

            for (ExtendedPointOverlay point : this.editPoints) {
                view.getOverlays().remove(point);
            }

            deselectActiveEditPoint();
        }
    }

    private void deselectActiveEditPoint() {
        if (activeEditPoint != null) {
            activeEditPoint.setFillColor(FILL_COLOR);
            activeEditPoint = null;
            view.invalidate();
        }
    }

    @Override
    public void onClick(Object clickObject) {

        if (clickObject instanceof ExtendedPointOverlay) {

            deselectActiveEditPoint();

            if(activeEditPoint != (ExtendedPointOverlay)clickObject){
                deselectActiveEditPoint();
                activeEditPoint = (ExtendedPointOverlay) clickObject;
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
