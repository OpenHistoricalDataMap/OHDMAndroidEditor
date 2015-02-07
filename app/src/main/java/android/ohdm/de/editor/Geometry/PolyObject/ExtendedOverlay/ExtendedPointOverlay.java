package android.ohdm.de.editor.Geometry.PolyObject.ExtendedOverlay;

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

    private static final int FILL_COLOR = Color.argb(128, 0, 0, 255);
    private boolean clickable = false;

    private List<ExtendedOverlayClickListener> listeners = new ArrayList<ExtendedOverlayClickListener>();

    public ExtendedPointOverlay(Context context) {
        super(context);
        setFillColor(FILL_COLOR);
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
        if (points.size() >= 1) {
            super.setPoints(Polygon.pointsAsCircle(points.get(points.size() - 1), 5));
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
}
