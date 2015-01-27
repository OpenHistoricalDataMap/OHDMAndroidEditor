package android.ohdm.de.editor.Geometry.ExtendedOverlay;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExtendedPolylineOverlay extends Polyline implements ExtendedOverlayClickPublisher, Serializable {

    private static final long serialVersionUID = 4209360273818925922L;

    private boolean editable = false;
    private boolean clickable = false;

    private List<ExtendedOverlayClickListener> listeners = new ArrayList<ExtendedOverlayClickListener>();

    public ExtendedPolylineOverlay(Context context){
        super(context);
        Log.i("ExtendedPolyLineOverlay","created!");
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    @Override
    public boolean onSingleTapConfirmed(final MotionEvent event, final MapView mapView){


        if(!isClickable()) return false;

        final Projection pj = mapView.getProjection();
        GeoPoint eventPos = (GeoPoint) pj.fromPixels((int)event.getX(), (int)event.getY());
        //TODO: tolerance erhöhen damit es einfacher zu klicken ist?
        double tolerance = mPaint.getStrokeWidth();
        boolean tapped = isCloseTo(eventPos, tolerance, mapView);

        if(tapped){
            notifyListeners();
        }

        return tapped;
    }

    private void notifyListeners(){
        for(ExtendedOverlayClickListener listener : listeners){
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
