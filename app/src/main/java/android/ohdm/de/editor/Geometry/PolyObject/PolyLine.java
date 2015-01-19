package android.ohdm.de.editor.Geometry.PolyObject;

import android.content.Context;
import android.graphics.Color;
import android.ohdm.de.editor.Geometry.ExtendedOverlay.ExtendedPolylineOverlay;

import org.osmdroid.bonuspack.overlays.OverlayWithIW;
import org.osmdroid.util.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PolyLine extends PolyObject implements Serializable {

    private static final long serialVersionUID = 4209360273818925922L;

    private transient ExtendedPolylineOverlay polyline;
    //private Context context;
    private List<GeoPoint> points = new ArrayList<GeoPoint>();
    private transient List<PolyObjectClickListener> listeners = new ArrayList<PolyObjectClickListener>();
    private transient boolean selected = false;
    private transient boolean editing = false;

    public PolyLine(Context context){
        super(PolyObjectType.POLYLINE);
        //this.context = context;
        create(context);
    }

    @Override
    protected void create(Context context) {
        polyline = new ExtendedPolylineOverlay(context);

        polyline.subscribe(this);

        polyline.setColor(Color.BLUE);
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
    }

    public List<GeoPoint> getPoints(){
        return this.points;
    }

    @Override
    public void removeLastPoint() {
        if(!points.isEmpty()){
            points.remove(points.size()-1);
        }
        polyline.setPoints(this.points);
    }

    @Override
    public void addPoint(GeoPoint geoPoint) {
        points.add(geoPoint);
        polyline.setPoints(this.points);
    }

    @Override
    public boolean isClickable() {
        return polyline.isClickable();
    }

    @Override
    public void setClickable(boolean clickable) {
        polyline.setClickable(clickable);
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;

        if(this.selected) {
            polyline.setColor(Color.RED);
        }else {
            polyline.setColor(Color.BLUE);
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

        if(this.editing) {
            polyline.setColor(Color.GREEN);
        }else {
            polyline.setColor(Color.BLUE);
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
