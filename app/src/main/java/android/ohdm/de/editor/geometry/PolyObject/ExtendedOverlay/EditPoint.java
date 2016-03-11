package android.ohdm.de.editor.geometry.PolyObject.ExtendedOverlay;

import android.graphics.Color;
import android.ohdm.de.editor.OHDMMapView;
import android.ohdm.de.editor.ZoomSubscriber;
import android.util.Log;
import android.view.MotionEvent;

import org.osmdroid.bonuspack.overlays.Polygon;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the Points of an PolyObject while in EditMode.
 *
 */
public class EditPoint extends Polygon implements ExtendedOverlayClickPublisher,ZoomSubscriber {


    private static final String TAG = "EditPoint";
    public static final int FILL_COLOR = Color.argb(128, 116, 116, 116);
    private boolean clickable = false;
    private transient int radius = 5;
    private transient OHDMMapView mapView;
    private transient List<GeoPoint> points;

    private List<ExtendedOverlayClickListener> listeners = new ArrayList<ExtendedOverlayClickListener>();

    /**
     * Constructor.
     *
     * @param mapView OHDMMapView
     */
    public EditPoint(OHDMMapView mapView)
    {
        super(mapView.getContext());

        this.mapView = mapView;
        setFillColor(FILL_COLOR);
        setStrokeWidth(4);

        //TODO: maybe it is better to subscribe the EditPoints to the ZoomPublisher only if needed
        //TODO: concretly only if they are really seen. this would be before they are added
        //TODO: seee getOverlays.add() in PolyObject's setEditing()
        mapView.subscribe(this);
    }

    /**
     * Return true if EditPoint is clickable.
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
     * Reads the GeoPoints from the List and draws Circle around them.
     *
     * @param points List<GeoPoint>
     */
    @Override
    public void setPoints(final List<GeoPoint> points) {

        this.points = points;
        radius = (int)((double)mapView.getBoundingBox().getDiagonalLengthInMeters()*0.03);

        if (this.points != null && this.points.size() >= 1) {

            GeoPoint editPoint = this.points.get(this.points.size() - 1);
            super.setPoints(Polygon.pointsAsCircle(editPoint, radius));
        }
    }

    /**
     * Calls setPoint() again.
     */
    public void refreshPoints(){
        setPoints(this.points);
    }

    /**
     * Getter List of EditPoints.
     *
     * @return List<GeoPoint>
     */
    @Override
    public List<GeoPoint> getPoints(){
        return this.points;
    }

    /**
     * Notifies if tapped in EditMode.
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

        if (tapped)
        {
            if (isClickable())
            {
                notifyListeners();
            }
        }

        return tapped;
    }

    /**
     * Calls onClick().
     */
    private void notifyListeners()
    {
        for (ExtendedOverlayClickListener listener : listeners)
        {
            listener.onClick(this);
        }
    }

    /**
     * Subscribes Lister.
     *
     * @param listener ExtendedOverlayClickListener
     */
    @Override
    public void subscribe(ExtendedOverlayClickListener listener)
    {
        listeners.add(listener);
    }

    /**
     * Removes Listener.
     *
     * @param listener ExtendedOverlayClickListener
     */
    @Override
    public void remove(ExtendedOverlayClickListener listener)
    {
        listeners.remove(listener);
    }

    /**
     * Redraws the Points if the User tries to zoom.
     */
    @Override
    public void onZoom()
    {
        setPoints(this.points);
    }
}
