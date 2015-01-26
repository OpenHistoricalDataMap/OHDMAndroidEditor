package android.ohdm.de.editor.Geometry.PolyObject;

import android.content.Context;
import android.graphics.Color;
import android.ohdm.de.editor.Geometry.ExtendedOverlay.ExtendedPolygonOverlay;

import org.osmdroid.bonuspack.overlays.OverlayWithIW;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class PolyGon extends PolyObject {

    private static final long serialVersionUID = 0L;

    private transient ExtendedPolygonOverlay polygon;
    private transient Context context;
    private List<GeoPoint> points = new ArrayList<GeoPoint>();
    private transient boolean selected = false;
    private transient boolean editing = false;
    private transient List<PolyObjectClickListener> listeners = new ArrayList<PolyObjectClickListener>();

    PolyGon(Context context){
        super(PolyObjectType.POLYGON);
        this.context = context;
        create(context);
    }

    @Override
    protected void create(Context context) {
        polygon = new ExtendedPolygonOverlay(context);
        polygon.subscribe(this);
        polygon.setFillColor(Color.BLUE);
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
    }

    public List<GeoPoint> getPoints(){
        return this.points;
    }

    @Override
    public void removeLastPoint() {
        if(!points.isEmpty()){
            points.remove(points.size()-1);
        }
        polygon.setPoints(this.points);
    }

    @Override
    public void addPoint(GeoPoint geoPoint) {
        points.add(geoPoint);
        polygon.setPoints(this.points);
    }

    @Override
    public boolean isClickable() {
        return polygon.isClickable();
    }

    @Override
    public void setClickable(boolean clickable) {
        polygon.setClickable(clickable);
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;

        if(selected){
            polygon.setFillColor(Color.RED);
        }else{
            polygon.setFillColor(Color.BLUE);
        }
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public boolean isEditing() {
        return editing;
    }

    @Override
    public void setEditing(boolean editing) {
        this.editing = editing;

        if(editing){
            polygon.setFillColor(Color.GREEN);
        }else{
            polygon.setFillColor(Color.BLUE);
        }
    }

    @Override
    public void removeSelectedCornerPoint() {
        //TODO
    }

    @Override
    public void onClick(Object clickObject) {
        for(PolyObjectClickListener listener : listeners){
            listener.onClick(this);
        }
    }

    @Override
    public void subscribe(PolyObjectClickListener listener) {
        listeners.add(listener);
    }

    @Override
    public void remove(PolyObjectClickListener listener) {
        listeners.remove(listener);
    }
}