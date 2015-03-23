package android.ohdm.de.editor.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.ohdm.de.editor.OHDMMapView;
import android.ohdm.de.editor.R;
import android.ohdm.de.editor.WMSTileSource;
import android.ohdm.de.editor.api.ApiConnect;
import android.ohdm.de.editor.api.ApiException;
import android.ohdm.de.editor.api.JSONReader;
import android.ohdm.de.editor.geometry.PolyObject.PolyObject;
import android.ohdm.de.editor.geometry.PolyObject.PolyObjectType;
import android.ohdm.de.editor.geometry.PolyObjectManager;
import android.ohdm.de.editor.geometry.PolyObjectSerializer;
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

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.bonuspack.overlays.MapEventsOverlay;
import org.osmdroid.bonuspack.overlays.MapEventsReceiver;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;

import java.util.HashMap;
import java.util.UUID;

public class MainActivity extends Activity implements MapEventsReceiver{

    private static final String TAG = "MainActivity";
    private static final int ID_DIALOG_REQUEST_CODE = 1747;
    private static final int DATA_DIALOG_REQUEST_CODE = 1748;
    private static final String EXTRA_POLYOBJECTID = "polyobjectid";
    private static final String WMS_SERVER_ADDRESS = "http://141.45.94.68/cgi-bin/mapserv?map=%2Fmapserver%2Fmapdata%2Fohdm.map&&SERVICE=WMS&VERSION=1.3.0&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&LAYERS=administrative&TILED=true&WIDTH=256&HEIGHT=256&CRS=EPSG%3A4326&STYLES&BBOX=";
    private static final String BUNDLE_MAP_ZOOMLEVEL = "map_zoom_level";
    private static final String BUNDLE_MAP_WMS = "map_wms_overlay";
    private static final String BUNDLE_MAP_LONGITUDE = "map_position_lon";
    private static final String BUNDLE_MAP_LATITUDE = "map_position_lat";

    public static final String EXTRA_SELECTED_POLYOBJECT_INTERNID = "polyobject_internid";
    public static final String MAP_DATA = "map_data";
    public static final String OHDMAPI_SERVER_ADDRESS = "http://ohsm.f4.htw-berlin.de:8080/OhdmApi/geographicObject/";

    private enum Mode {
        ADD, SELECT, EDIT, VIEW
    }

    private static Mode mode = Mode.VIEW;

    private OHDMMapView map;
    private PolyObjectManager polyObjectManager;
    private ITileSource wmsTileSource;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        double longitude = 13.52400;
        double latitude = 52.49688;
        GeoPoint startGeoPoint;
        int zoomlevel = 16;
        boolean isWmsOverlayActive = false;

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            zoomlevel = savedInstanceState.getInt(BUNDLE_MAP_ZOOMLEVEL);
            isWmsOverlayActive = savedInstanceState.getBoolean(BUNDLE_MAP_WMS);
            longitude = savedInstanceState.getDouble(BUNDLE_MAP_LONGITUDE);
            latitude = savedInstanceState.getDouble(BUNDLE_MAP_LATITUDE);
        }
        startGeoPoint = new GeoPoint(latitude, longitude);

        map = createMapView(zoomlevel, startGeoPoint, isWmsOverlayActive);

        mode = Mode.VIEW;

        polyObjectManager = PolyObjectSerializer.deserialize(map);

        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, this);
        map.getOverlays().add(0, mapEventsOverlay);

        location();
    }

    private OHDMMapView createMapView(int zoomlevel, GeoPoint geoPoint, boolean isWmsOverlayActive) {

        wmsTileSource = new WMSTileSource("wmsserver", null, 3, 18, 256, ".png",
                new String[]{WMS_SERVER_ADDRESS});

        // Setup base map
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.ohdmmapview);

        MapTileProviderBasic tileProvider = new MapTileProviderBasic(getApplicationContext());

        if (!isWmsOverlayActive) {
            tileProvider.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        } else {
            tileProvider.setTileSource(wmsTileSource);
        }

        OHDMMapView osmv = new OHDMMapView(this, 256, new DefaultResourceProxyImpl(this), tileProvider);

        rl.addView(osmv, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));

        osmv.setBuiltInZoomControls(true);
        osmv.setClickable(true);
        osmv.setMultiTouchControls(true);
        osmv.getController().setZoom(zoomlevel);
        osmv.getController().setCenter(geoPoint);;

//        osmv.getTileProvider().clearTileCache();

        return osmv;
    }


    @Override
    protected void onSaveInstanceState(Bundle state) {

        super.onSaveInstanceState(state);

        boolean isWmsOverlayActive = false;

        if (map.getTileProvider().getTileSource().equals(wmsTileSource)) {
            isWmsOverlayActive = true;
        }

        state.putBoolean(BUNDLE_MAP_WMS, isWmsOverlayActive);
        state.putDouble(BUNDLE_MAP_LONGITUDE, map.getMapCenter().getLongitude());
        state.putDouble(BUNDLE_MAP_LATITUDE, map.getMapCenter().getLatitude());
        state.putInt(BUNDLE_MAP_ZOOMLEVEL, map.getZoomLevel());
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
     * menu click handler
     */
    @Override
    public boolean onMenuItemSelected(final int featureId, final MenuItem item) {

        //MapView map = (MapView) findViewById(R.id.openmapview);

        switch (item.getItemId()) {
            case R.id.menuItemLocate:

                Toast.makeText(getApplicationContext(), R.string.not_implemented, Toast.LENGTH_SHORT).show();
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

                map.getTileProvider().setTileSource(wmsTileSource);
                map.invalidate();

                return true;

            case R.id.action_getpolyobjectbyid:

                Intent intent = new Intent(this, GetPolyobjectByIdActivity.class);
                startActivityForResult(intent, ID_DIALOG_REQUEST_CODE);

                return true;

            case R.id.action_sync:

                UploadPolyObjectTask uploadPolyObjectTask = new UploadPolyObjectTask();
                uploadPolyObjectTask.execute();

                return true;
        }

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK && requestCode == ID_DIALOG_REQUEST_CODE) {

            int polyObjectId = data.getIntExtra(EXTRA_POLYOBJECTID, 0);

            DownloadPolyObjectTask downloadPolyObjectTask = new DownloadPolyObjectTask();
            downloadPolyObjectTask.execute(polyObjectId);

        } else if (resultCode == Activity.RESULT_OK && requestCode == DATA_DIALOG_REQUEST_CODE) {

            UUID selectedObjectId = (UUID) data.getSerializableExtra(EXTRA_SELECTED_POLYOBJECT_INTERNID);
            polyObjectManager.selectPolyObjectByInternId(selectedObjectId);

            HashMap<String, String> resultMap = (HashMap) data.getSerializableExtra(MAP_DATA);
            polyObjectManager.setSelectedPolyObjectTags(resultMap);

        }else if (resultCode == Activity.RESULT_CANCELED && requestCode == ID_DIALOG_REQUEST_CODE){

            int polyObjectId = data.getIntExtra(EXTRA_POLYOBJECTID, 0);

            if(polyObjectId == -1){
                Toast.makeText(getApplicationContext(), R.string.no_real_id_error, Toast.LENGTH_SHORT).show();
            }
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
                //geoPoint = createGeoPointFromLocation(location);
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

    private GeoPoint createGeoPointFromLocation(Location location) {
        //Log.i(TAG, "getGeoPointFromLocation: " + geoPoint.toDoubleString());

        int lat = (int) (location.getLatitude() * 1E6);
        int lon = (int) (location.getLongitude() * 1E6);

        return new GeoPoint(lat, lon);
    }

    /*
    Tap-Listeners
     */
    @Override
    public boolean singleTapConfirmedHelper(GeoPoint geoPoint) {

        if (mode == Mode.ADD) {
            polyObjectManager.addPointToSelectedPolyObject(geoPoint);
            map.invalidate();
        }

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

    public void buttonAddData(View view) {

        Intent intent = new Intent(this, ShowPolyObjectDataActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable(MAP_DATA, polyObjectManager.getSelectedPolyObjectTags());

        extras.putSerializable(EXTRA_SELECTED_POLYOBJECT_INTERNID, polyObjectManager.getSelectedPolyObjectInternId());
        intent.putExtras(extras);

        startActivityForResult(intent, DATA_DIALOG_REQUEST_CODE);
    }

    /**
     * AsyncTasks
     */

    private class UploadPolyObjectTask extends AsyncTask<Integer, Integer, Long> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), R.string.async_upload_start, Toast.LENGTH_SHORT).show();
        }

        protected Long doInBackground(Integer... params) {

            if (!polyObjectManager.writeActivePolyObject()) {
                return -1L;
            }

            return 0L;
        }

        protected void onPostExecute(Long result) {

            if (result == 0) {
                Toast.makeText(getApplicationContext(), R.string.async_upload_done, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), R.string.async_upload_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class DownloadPolyObjectTask extends AsyncTask<Integer, Integer, Long> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), R.string.async_download_start, Toast.LENGTH_SHORT).show();
        }

        protected Long doInBackground(Integer... params) {


            ApiConnect apiConnect = new ApiConnect(OHDMAPI_SERVER_ADDRESS);
            JSONObject jsonObject = null;

            try {
                jsonObject = apiConnect.getJSONObjectById(params[0]);
            } catch (JSONException e) {
                Log.e(TAG, "could not read polyobject from server: "+e.toString());
                return -1L;
            } catch (ApiException e){
                Log.e(TAG,e.toString());
                return -2L;
            }

            PolyObject loadedPolyObject = JSONReader.getPolyObjectFromJSONObject(jsonObject, map);

            if (loadedPolyObject != null) {

                map.getController().setCenter(loadedPolyObject.getPoints().get(0));

                polyObjectManager.addObject(loadedPolyObject);

            } else {
                Log.e(TAG, "could not create polyobject");
                return -1L;
            }

            return 0L;
        }

        protected void onPostExecute(Long result) {

            if (result == 0) {
                Toast.makeText(getApplicationContext(), R.string.async_download_done, Toast.LENGTH_SHORT).show();
            } else if(result == -2){
                Toast.makeText(getApplicationContext(), R.string.async_download_error_server, Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(getApplicationContext(), R.string.async_download_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

}