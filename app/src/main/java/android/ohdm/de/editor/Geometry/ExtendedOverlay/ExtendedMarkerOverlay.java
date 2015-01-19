package android.ohdm.de.editor.Geometry.ExtendedOverlay;

import android.view.MotionEvent;

import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.views.MapView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExtendedMarkerOverlay extends Marker implements ExtendedOverlayClickPublisher, Serializable {

    private static final long serialVersionUID = 0L;

    private List<ExtendedOverlayClickListener> listeners = new ArrayList<ExtendedOverlayClickListener>();
    private boolean clickable;

    public ExtendedMarkerOverlay(MapView mapView) {
        super(mapView);
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    @Override
    public boolean onSingleTapConfirmed(final MotionEvent event, final MapView mapView) {

        boolean tapped = hitTest(event, mapView);
        if(tapped) {
            if (isClickable()) {
                notifyListeners();
            }
        }

        return tapped;
    }


    private void notifyListeners(){
        for(ExtendedOverlayClickListener listener : listeners){
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
