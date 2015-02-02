package android.ohdm.de.ohdmviewer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.ohdm.de.editor.Geometry.PolyObject.PolyObject;
import android.ohdm.de.editor.Geometry.PolyObject.PolyObjectType;
import android.ohdm.de.editor.Geometry.PolyObjectManager;
import android.ohdm.de.editor.Geometry.PolyObjectSerializer;
import android.ohdm.de.editor.JSONReader;
import android.ohdm.de.editor.R;
import android.ohdm.de.editor.WMSTileSource;
import android.ohdm.de.ohdmviewer.GetPolyobjectByIdActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.bonuspack.overlays.MapEventsOverlay;
import org.osmdroid.bonuspack.overlays.MapEventsReceiver;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class MainActivity extends Activity implements MapEventsReceiver {

    private static final String TAG = "MainActivity";
    private static final int DIALOG_REQUEST_CODE = 1747;
    private static final String EXTRA_POLYOBJECTID = "polyobjectid";

    private enum Mode {
        ADD, SELECT, EDIT, VIEW
    }

    private static Mode mode = Mode.VIEW;

    //    private GeoPoint geoPoint = new GeoPoint(52.4581555, 13.5685014);
    private GeoPoint geoPoint = new GeoPoint(52.49688, 13.52400);
    private MapView map;
    private PolyObjectManager polyObjectManager;
    private ITileSource tileSource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        map = createMapView();

        mode = Mode.VIEW;

        polyObjectManager = PolyObjectSerializer.deserialize(map);

        //TODO: just here because location() is buggy
//        map.getController().setCenter(geoPoint);

        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, this);
        map.getOverlays().add(0, mapEventsOverlay);

        Log.i(TAG, "created!");

        location();
    }

    private MapView createMapView() {

        String server_adress = "http://141.45.146.152/cgi-bin/mapserv?map=%2Fmapserver%2Fmapdata%" +
                "2Fberlin.map&&SERVICE=WMS&VERSION=1.3.0&REQUEST=GetMap&FORMAT=ima" +
                "ge%2Fpng&TRANSPARENT=true&LAYERS=postcode&TILED=true&WIDTH=256&HE" +
                "IGHT=256&CRS=EPSG%3A3857&STYLES&BBOX=";

        // Setup base map
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.ohdmmapview); //new RelativeLayout(this);

        MapTileProviderBasic tileProvider = new MapTileProviderBasic(getApplicationContext());

        tileSource = new WMSTileSource("wmsserver", null, 3, 18, 256, ".png",
                new String[]{server_adress});

        tileProvider.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);

        MapView osmv = new MapView(this, 256, new DefaultResourceProxyImpl(this), tileProvider);

        rl.addView(osmv, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));

        //this.setContentView(rl);

        osmv.setBuiltInZoomControls(true);
        osmv.setClickable(true);
        osmv.setMultiTouchControls(true);
        osmv.getController().setZoom(16);
        osmv.getController().setCenter(geoPoint);

        osmv.getTileProvider().clearTileCache();

        return osmv;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mode == Mode.ADD) {
            stopAdd();
        }

        PolyObjectSerializer.serialize(polyObjectManager, map);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Bei Klick auf die Menu-Eintraege reagieren
     */
    @Override
    public boolean onMenuItemSelected(final int featureId, final MenuItem item) {

        //MapView map = (MapView) findViewById(R.id.openmapview);

        switch (item.getItemId()) {
            case R.id.menuItemLocate:

                Log.i(TAG, "setCenter: " + geoPoint.toDoubleString());
                map.getController().setCenter(geoPoint);
                return true;

            case R.id.menuItemAddLine:

                if (mode == Mode.ADD) {
                    stopAdd();
                } else {
                    startAddObject(PolyObjectType.POLYLINE);
                }
                return true;

            case R.id.menuItemAddPolygon:

                if (mode == Mode.ADD) {
                    stopAdd();
                } else {
                    startAddObject(PolyObjectType.POLYGON);
                }
                return true;

            case R.id.menuItemAddPoint:

                if (mode == Mode.ADD) {
                    stopAdd();
                } else {
                    startAddObject(PolyObjectType.POINT);
                }
                return true;

            case R.id.action_edit:

                if (mode == Mode.SELECT) {
                    stopSelect();
                } else if (mode == Mode.ADD) {
                    stopAdd();
                } else {
                    startSelectObject();
                }

                return true;

            case R.id.menuItemLayerOSM:

                map.getTileProvider().setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
                map.invalidate();

                return true;

            case R.id.menuItemLayerOHDM:

                map.getTileProvider().setTileSource(tileSource);
                map.invalidate();

                return true;

            case R.id.action_getpolyobjectbyid:

                Intent intent = new Intent(this, GetPolyobjectByIdActivity.class);
                startActivityForResult(intent, DIALOG_REQUEST_CODE);

                return true;
        }

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == DIALOG_REQUEST_CODE) {

            int polyObjectId = data.getIntExtra(EXTRA_POLYOBJECTID, 0);

            Log.i(TAG, "get data: " + polyObjectId);

            DownloadPolyObjectTask downloadPolyObjectTask = new DownloadPolyObjectTask();
            downloadPolyObjectTask.execute(polyObjectId);
        }
    }

    private void startEdit() {

        changeEditButtonsVisibility(View.INVISIBLE);

        polyObjectManager.setObjectsClickable(false);

        polyObjectManager.setSelectedObjectEditable(true);

        if (polyObjectManager.isSelectedObjectEditable()) {
            map.invalidate();

            changeAddButtonsVisibility(View.VISIBLE);
            mode = Mode.ADD;
        }
    }

    private void startSelectObject() {

        changeEditButtonsVisibility(View.VISIBLE);
        mode = Mode.SELECT;
        polyObjectManager.setObjectsClickable(true);
    }

    private void startAddObject(PolyObjectType type) {

        if (mode == Mode.SELECT) {
            stopSelect();
        }

        changeAddButtonsVisibility(View.VISIBLE);

        polyObjectManager.createAndAddPolyObject(type);

        mode = Mode.ADD;
    }

    private void stopAdd() {

        mode = Mode.VIEW;
        polyObjectManager.setActiveObjectEditable(false);
        polyObjectManager.deselectActiveObject();
        map.invalidate();
        changeAddButtonsVisibility(View.INVISIBLE);
    }

    private void stopSelect() {

        mode = Mode.VIEW;
        changeEditButtonsVisibility(View.INVISIBLE);

        polyObjectManager.setObjectsClickable(false);
        polyObjectManager.deselectActiveObject();
    }

    private void changeAddButtonsVisibility(int visibility) {
        ImageButton buttonAddAccept = (ImageButton) findViewById(R.id.buttonAddAccept);
        ImageButton buttonAddUndo = (ImageButton) findViewById(R.id.buttonAddUndo);
        ImageButton buttonAddDelete = (ImageButton) findViewById(R.id.buttonEditDelete);
        ImageButton buttonAddData = (ImageButton) findViewById(R.id.buttonAddData);

        buttonAddAccept.setVisibility(visibility);
        buttonAddUndo.setVisibility(visibility);
        buttonAddDelete.setVisibility(visibility);
        buttonAddData.setVisibility(visibility);
    }

    private void changeEditButtonsVisibility(int visibility) {
        ImageButton buttonAddAccept = (ImageButton) findViewById(R.id.buttonAddAccept);
        ImageButton buttonAddCancel = (ImageButton) findViewById(R.id.buttonAddCancel);
        ImageButton buttonEditDelete = (ImageButton) findViewById(R.id.buttonEditDelete);

        buttonAddAccept.setVisibility(visibility);
        buttonAddCancel.setVisibility(visibility);
        buttonEditDelete.setVisibility(visibility);
    }

    private void location() {

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        //TODO: buggy, returns null sometimes
        //geoPoint = createGeoPointFromLocation(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                makeUseOfNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    private void makeUseOfNewLocation(Location location) {
        geoPoint = createGeoPointFromLocation(location);
    }

    private GeoPoint createGeoPointFromLocation(Location location) {
        //Log.i(TAG, "getGeoPointFromLocation: " + geoPoint.toDoubleString());

        int lat = (int) (location.getLatitude() * 1E6);
        int lon = (int) (location.getLongitude() * 1E6);

        return new GeoPoint(lat, lon);
    }

    private class DownloadPolyObjectTask extends AsyncTask<Integer, Integer, Long> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), R.string.async_download_start, Toast.LENGTH_SHORT).show();
        }

        protected Long doInBackground(Integer... params) {

            PolyObject loadedPolyObject = JSONReader.getPolyObjectById(params[0], map);

            if (loadedPolyObject != null) {

                map.getController().setCenter(loadedPolyObject.getPoints().get(0));

                polyObjectManager.addObject(loadedPolyObject);
            } else {
                Log.i(TAG, "could not read polyobject from server");
                return -1L;
            }

            return 0L;
        }

        protected void onPostExecute(Long result) {

            if (result == 0) {
                Toast.makeText(getApplicationContext(), R.string.async_download_done, Toast.LENGTH_SHORT).show();
                Log.i("DownloadPolyObjectTask", "done");
            } else {
                Toast.makeText(getApplicationContext(), R.string.async_download_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*
    Tap-Listeners
     */
    @Override
    public boolean singleTapConfirmedHelper(GeoPoint geoPoint) {

        if (mode == Mode.ADD) {
            polyObjectManager.addPointToSelectedPolyObject(geoPoint);
        }

        map.invalidate();

        return true;
    }

    @Override
    public boolean longPressHelper(GeoPoint geoPoint) {
        return false;
    }

    /*
    Button-Listener
     */

    public void buttonAddAccept(View view) {

        switch (mode) {
            case ADD:
                stopAdd();
                break;
            case SELECT:
                startEdit();
                break;
            default:
                Log.e(TAG, "Illegal mode");
        }
    }

    public void buttonAddUndo(View view) {

        polyObjectManager.removeLastPointFromSelectedPolyObject();
        map.invalidate();
    }

    public void buttonAddCancel(View view) {

        if (mode == Mode.SELECT) {
            stopSelect();
        }
        map.invalidate();
    }

    public void buttonEditDelete(View view) {

        if (mode == Mode.SELECT) {
            polyObjectManager.removeSelectedObject();
        } else if (mode == Mode.ADD) {
            polyObjectManager.removeSelectedCornerPoint();
        }
        map.invalidate();
    }

    public void buttonAddData(View view){

        Intent intent = new Intent(this, EditPolyObjectDataActivity.class);
        startActivity(intent);
    }
}