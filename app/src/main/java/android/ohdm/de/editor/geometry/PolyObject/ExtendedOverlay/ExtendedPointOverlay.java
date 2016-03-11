package android.ohdm.de.editor.geometry.PolyObject.ExtendedOverlay;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;

import org.osmdroid.bonuspack.overlays.Polygon;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the Output of the PolyPoint to the MapView and adds EvenListener.
 */
public class ExtendedPointOverlay extends Polygon implements ExtendedOverlayClickPublisher
{
    private static final int FILL_COLOR = Color.argb(128, 0, 0, 255);
    private boolean clickable = false;

    private List<ExtendedOverlayClickListener> listeners = new ArrayList<ExtendedOverlayClickListener>();

    /**
     * Constructor.
     *
     * @param context Context
     */
    public ExtendedPointOverlay(Context context)
    {
        super(context);
        setFillColor(FILL_COLOR);
        setStrokeWidth(4);
    }

    /**
     * Getter clickable.
     *
     * @return clickable boolean
     */
    public boolean isClickable()
    {
        return clickable;
    }

    /**
     * Setter clickable.
     *
     * @param clickable boolean
     */
    public void setClickable(boolean clickable)
    {
        this.clickable = clickable;
    }


    /**
     * Draws Points with Circle on MapView.
     *
     * @param points List<GeoPoint>
     */
    @Override
    public void setPoints(final List<GeoPoint> points)
    {
        if (points.size() >= 1)
        {
            super.setPoints(Polygon.pointsAsCircle(points.get(points.size() - 1), 5));
        }
    }

    /**
     * Handles Tap-Events.
     *
     * @param event MotionEvent
     * @param mapView MapView
     *
     * @return boolean
     */
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

    /**
     * Triggers an onClick-Event.
     *
     */
    private void notifyListeners()
    {
        for (ExtendedOverlayClickListener listener : listeners)
        {
            listener.onClick(this);
        }
    }

    /**
     * Adds Listener to the list.
     *
     * @param listener ExtendedOverlayClickListener
     */
    @Override
    public void subscribe(ExtendedOverlayClickListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes Listener from the list.
     *
     * @param listener ExtendedOverlayClickListener
     */
    @Override
    public void remove(ExtendedOverlayClickListener listener) {
        listeners.remove(listener);
    }
}
