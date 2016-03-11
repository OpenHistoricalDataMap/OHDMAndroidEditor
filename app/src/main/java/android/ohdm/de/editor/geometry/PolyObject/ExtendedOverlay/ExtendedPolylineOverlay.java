package android.ohdm.de.editor.geometry.PolyObject.ExtendedOverlay;

import android.content.Context;
import android.view.MotionEvent;

import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the Output of the PolyLine to the MapView and adds EvenListener.
 */
public class ExtendedPolylineOverlay extends Polyline implements ExtendedOverlayClickPublisher, Serializable {

    private static final long serialVersionUID = 4209360273818925922L;

    private boolean clickable = false;

    private List<ExtendedOverlayClickListener> listeners = new ArrayList<ExtendedOverlayClickListener>();

    /**
     * Constructor.
     *
     * @param context Context
     */
    public ExtendedPolylineOverlay(Context context){
        super(context);
    }

    /**
     * Getter clickable.
     *
     * @return clickable boolean
     */
    public boolean isClickable() {
        return clickable;
    }

    /**
     * Setter clickable.
     *
     * @param clickable boolean
     */
    public void setClickable(boolean clickable) {
        this.clickable = clickable;
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
    public boolean onSingleTapConfirmed(final MotionEvent event, final MapView mapView)
    {
        if(!isClickable())
        {
            return false;
        }

        final Projection pj = mapView.getProjection();
        GeoPoint eventPos = (GeoPoint) pj.fromPixels((int)event.getX(), (int)event.getY());
        //TODO: tolerance erhöhen damit es einfacher zu klicken ist?
        double tolerance = mPaint.getStrokeWidth();
        boolean tapped = isCloseTo(eventPos, tolerance, mapView);

        if(tapped)
        {
            notifyListeners();
        }

        return tapped;
    }

    /**
     * Triggers an onClick-Event.
     *
     */
    private void notifyListeners()
    {
        for(ExtendedOverlayClickListener listener : listeners)
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
