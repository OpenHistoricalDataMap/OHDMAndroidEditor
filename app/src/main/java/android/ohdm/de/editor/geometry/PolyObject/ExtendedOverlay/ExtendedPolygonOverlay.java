package android.ohdm.de.editor.geometry.PolyObject.ExtendedOverlay;

import android.content.Context;
import android.view.MotionEvent;

import org.osmdroid.bonuspack.overlays.Polygon;
import org.osmdroid.views.MapView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the Output of the PolyGon to the MapView and adds EvenListener.
 */
public class ExtendedPolygonOverlay extends Polygon implements ExtendedOverlayClickPublisher, Serializable {

    private static final long serialVersionUID = 0L;

    private boolean clickable = false;

    private List<ExtendedOverlayClickListener> listeners = new ArrayList<ExtendedOverlayClickListener>();

    /**
     * Constructor.
     *
     * @param context Context
     */
    public ExtendedPolygonOverlay(Context context)
    {
        super(context);
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
        boolean tapped = super.contains(event);

        if(tapped) {
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
