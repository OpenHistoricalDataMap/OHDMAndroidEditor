package android.ohdm.de.editor.Geometry.PolyObject;

import android.content.Context;
import android.graphics.Color;
import android.ohdm.de.editor.Geometry.PolyObject.ExtendedOverlay.ExtendedPointOverlay;
import android.ohdm.de.editor.Geometry.TagDates;

import org.osmdroid.bonuspack.overlays.OverlayWithIW;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PolyPoint extends PolyObject {

    private static final long serialVersionUID = 0L;

    private static final int FILL_COLOR = Color.argb(128,0,0,255);
    private static final int FILL_COLOR_SELECTED = Color.argb(128,255,0,0);
    private static final int FILL_COLOR_EDIT = Color.argb(128,0,255,0);

    private transient ExtendedPointOverlay point;
    private transient boolean selected = false;
    private transient boolean editing = false;
    private transient List<PolyObjectClickListener> listeners = new ArrayList<PolyObjectClickListener>();

    private List<GeoPoint> points = new ArrayList<GeoPoint>();

    PolyPoint(Context context){
        super(PolyObjectType.POINT);
        this.internId = UUID.randomUUID();
        create(context);
    }

    @Override
    protected void create(Context context) {
        point = new ExtendedPointOverlay(context);
        point.subscribe(this);

        point.setFillColor(FILL_COLOR);
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
    public void setClickable(boolean clickable) {
        point.setClickable(clickable);
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;

        if(selected){
            point.setFillColor(FILL_COLOR_SELECTED);
        }else{
            point.setFillColor(FILL_COLOR);
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
            point.setFillColor(FILL_COLOR_EDIT);
        }else{
            point.setFillColor(FILL_COLOR);
        }
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

    @Override
    public void removeSelectedCornerPoint() {
        //TODO
    }

    @Override
    public HashMap<TagDates, String> getTags() {
        return null;
    }

    @Override
    public void setTags(HashMap<TagDates, String> tags) {
        //TODO
    }
}