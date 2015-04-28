package android.ohdm.de.editor;

import android.content.Context;

import org.osmdroid.ResourceProxy;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.List;

public class OHDMMapView extends MapView implements ZoomPublisher{

    private static final String TAG = "OHDMMapView";

    private List<ZoomSubscriber> subscribers;

    public OHDMMapView(Context context, int tileSizePixels, ResourceProxy resourceProxy, MapTileProviderBase aTileProvider) {
        super(context, tileSizePixels, resourceProxy, aTileProvider);

        subscribers = new ArrayList<ZoomSubscriber>();

        setMapListener(new MapListener() {

            @Override
            public boolean onScroll(ScrollEvent event) {
                return false;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {

                for(ZoomSubscriber subscriber : subscribers){
                    subscriber.onZoom();
                }

                return false;
            }
        });
    }

    @Override
    public void subscribe(ZoomSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void remove(ZoomSubscriber subscriber) {
        subscribers.remove(subscriber);
    }
}
