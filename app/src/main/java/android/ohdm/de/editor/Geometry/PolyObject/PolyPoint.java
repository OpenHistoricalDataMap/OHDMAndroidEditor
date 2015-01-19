package android.ohdm.de.editor.Geometry.PolyObject;

import android.content.Context;
import android.ohdm.de.editor.Geometry.ExtendedOverlay.ExtendedMarkerOverlay;
import android.ohdm.de.editor.R;

import org.osmdroid.bonuspack.overlays.OverlayWithIW;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.List;

public class PolyPoint extends PolyObject {

    private static final long serialVersionUID = 0L;

    private transient ExtendedMarkerOverlay marker;
    private List<GeoPoint> points = new ArrayList<GeoPoint>();
    private transient MapView context;

    private transient boolean selected;
    private transient boolean editing;

    private transient List<PolyObjectClickListener> listeners = new ArrayList<PolyObjectClickListener>();

    public PolyPoint(MapView mapView){
        super(PolyObjectType.POINT);
        this.context = mapView;
        create(context.getContext());
    }

    @Override
    protected void create(Context context1) {

        marker = new ExtendedMarkerOverlay(this.context);
        marker.setIcon(context.getResources().getDrawable(R.drawable.ic_action_new));
        marker.setTitle("marker");
        marker.subscribe(this);
    }

    @Override
    public OverlayWithIW getOverlay() {
        return marker;
    }

    @Override
    public void setPoints(List<GeoPoint> points) {
        this.points = points;

        if(this.points.size()>0){
            marker.setPosition(points.get(points.size() - 1));
        }
    }

    public List<GeoPoint> getPoints(){
        return this.points;
    }

    @Override
    public void removeLastPoint() {
        if(points.size()>1){
            points.remove(points.size()-1);
            marker.setPosition(points.get(points.size() - 1));
        }
    }

    @Override
    public void addPoint(GeoPoint geoPoint) {
        points.add(geoPoint);
        marker.setPosition(geoPoint);
    }

    @Override
    public boolean isClickable() {
        return marker.isClickable();
    }

    @Override
    public void setClickable(boolean clickable) {
        marker.setClickable(clickable);
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;

        if(selected){
            marker.setIcon(context.getResources().getDrawable(R.drawable.ic_action_edit));
        }else{
            marker.setIcon(context.getResources().getDrawable(R.drawable.ic_action_accept));
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
            marker.setIcon(context.getResources().getDrawable(R.drawable.ic_action_new));
        }else{
            marker.setIcon(context.getResources().getDrawable(R.drawable.ic_action_accept));
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
