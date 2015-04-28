package android.ohdm.de.editor.geometry.PolyObject.ExtendedOverlay;

import android.graphics.Color;
import android.ohdm.de.editor.OHDMMapView;
import android.ohdm.de.editor.ZoomSubscriber;
import android.view.MotionEvent;

import org.osmdroid.bonuspack.overlays.Polygon;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.List;

public class EditPoint extends Polygon implements ExtendedOverlayClickPublisher,ZoomSubscriber {

    public static final int FILL_COLOR = Color.argb(128, 116, 116, 116);
    private boolean clickable = false;
    private OHDMMapView mapView;
    private transient int radius = 5;
    private transient List<GeoPoint> points;

    private List<ExtendedOverlayClickListener> listeners = new ArrayList<ExtendedOverlayClickListener>();

    public EditPoint(OHDMMapView mapView) {
        super(mapView.getContext());

        this.mapView = mapView;
        setFillColor(FILL_COLOR);
        setStrokeWidth(4);

        //TODO: maybe it is better to subscribe the EditPoints to the ZoomPublisher only if needed
        //concretly only if they are really seen. this would be before they are added
        //seee getOverlays.add() in PolyObject's setEditing()
        mapView.subscribe(this);
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    @Override
    public void setPoints(final List<GeoPoint> points) {

        this.points = points;

        radius = (int)((double)mapView.getBoundingBox().getDiagonalLengthInMeters()*0.03);

        if (this.points != null && this.points.size() >= 1) {

            super.setPoints(Polygon.pointsAsCircle(this.points.get(this.points.size() - 1), radius));
        }
    }

    @Override
    public List<GeoPoint> getPoints(){
        return this.points;
    }

    @Override
    public boolean onSingleTapConfirmed(final MotionEvent event, final MapView mapView) {

        boolean tapped = super.contains(event);

        if (tapped) {
            if (isClickable()) {
                notifyListeners();
            }
        }

        return tapped;
    }

    private void notifyListeners() {
        for (ExtendedOverlayClickListener listener : listeners) {
            listener.onClick(this);
        }
    }

    @Override
    public void subscribe(ExtendedOverlayClickListener listener) {
        listeners.add(listener);
    }

    @Override
    public void remove(ExtendedOverlayClickListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onZoom() {
        setPoints(this.points);
//        mapView.invalidate();
    }
}
