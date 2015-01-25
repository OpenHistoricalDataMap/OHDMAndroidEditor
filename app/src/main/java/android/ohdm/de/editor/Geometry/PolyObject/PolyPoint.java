package android.ohdm.de.editor.Geometry.PolyObject;

import android.content.Context;
import android.graphics.Color;
import android.ohdm.de.editor.Geometry.ExtendedOverlay.ExtendedPointOverlay;

import org.osmdroid.bonuspack.overlays.OverlayWithIW;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class PolyPoint extends PolyObject {

    private static final long serialVersionUID = 0L;

    private transient ExtendedPointOverlay point;
    private transient boolean selected = false;
    private transient boolean editing = false;
    private transient List<PolyObjectClickListener> listeners = new ArrayList<PolyObjectClickListener>();
    private List<GeoPoint> points = new ArrayList<GeoPoint>();

    PolyPoint(Context context){
        super(PolyObjectType.POINT);
        create(context);
    }

    @Override
    protected void create(Context context) {
        point = new ExtendedPointOverlay(context);
        point.subscribe(this);

        point.setFillColor(Color.BLUE);
        point.setStrokeWidth(4);
    }

    @Override
    public OverlayWithIW getOverlay() {
        return point;
    }

    @Override
    public void setPoints(List<GeoPoint> points) {
        this.points = points;
        point.setPoints(this.points);
    }

    public List<GeoPoint> getPoints(){
        return this.points;
    }

    @Override
    public void removeLastPoint() {
        if(!points.isEmpty()){
            points.remove(points.size()-1);
        }

        setPoints(this.points);
    }

    @Override
    public void addPoint(GeoPoint geoPoint) {
        points.add(geoPoint);

        setPoints(this.points);
    }

    @Override
    public boolean isClickable() {
        return point.isClickable();
    }

    @Override
    public void setClickable(boolean clickable) {
        point.setClickable(clickable);
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;

        if(selected){
            point.setFillColor(Color.RED);
        }else{
            point.setFillColor(Color.BLUE);
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
            point.setFillColor(Color.GREEN);
        }else{
            point.setFillColor(Color.BLUE);
        }
    }

    @Override
    public void onClick() {
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