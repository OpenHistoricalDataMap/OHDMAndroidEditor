package android.ohdm.de.editor.Geometry.ExtendedOverlay;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;

import org.osmdroid.bonuspack.overlays.Polygon;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.List;

public class ExtendedPointOverlay extends Polygon implements ExtendedOverlayClickPublisher {

    private boolean clickable = false;

    private List<ExtendedOverlayClickListener> listeners = new ArrayList<ExtendedOverlayClickListener>();

    public ExtendedPointOverlay(Context context) {
        super(context);
        setFillColor(Color.BLUE);
        setStrokeWidth(4);
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    @Override
    public void setPoints(final List<GeoPoint> points) {
        super.setPoints(points);

        if (points.size() >= 1) {
            super.setPoints(Polygon.pointsAsCircle(points.get(points.size() - 1), 20));
        }
    }

    @Override
    public boolean onSingleTapConfirmed(final MotionEvent event, final MapView mapView) {

        boolean tapped = super.contains(event);

        if (tapped) {
            if (isClickable()) {
                Log.i("ExtendedPointOverlay", "clicked");
                notifyListeners();
            }
        }

        return tapped;
    }

    private void notifyListeners() {
        for (ExtendedOverlayClickListener listener : listeners) {
            listener.onClick();
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
}
