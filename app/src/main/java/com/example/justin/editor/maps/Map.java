package com.example.justin.editor.maps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.justin.editor.R;
import com.example.justin.editor.listener.Gps;

import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.PathOverlay;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.TilesOverlay;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Map extends AppCompatActivity {
    public static final String[] LAYERS =
            {/*"ohdm_t%3Aboundaries_admin_2",
            "ohdm_t%3Aboundaries_admin_3",
            "ohdm_t%3Aboundaries_admin_4'",
            "ohdm_t%3Aboundaries_admin_5",
            "ohdm_t%3Aboundaries_admin_6",
            "ohdm_t%3Aboundaries_admin_7",
            "ohdm_t%3Aboundaries_admin_8",
            "ohdm_t%3Aboundaries_admin_9",
            "ohdm_t%3Aboundaries_admin_10",
            "ohdm_t%3Ahighway_huge_lines",
            "ohdm_t%3Ahighway_primary_lines",
            "ohdm_t%3Ahighway_secondary_lines",
            "ohdm_t%3Ahighway_small_lines",
            "ohdm_t%3Ahighway_tertiary_lines",
            "ohdm_t%3Ahighway_path_lines",
            "ohdm_t%3Arailway_lines",
            "ohdm_t%3Ashop_points",
            "ohdm_t%3Apublic_transport_points",
            "ohdm_t%3Anatural_points",
            "ohdm_t%3Aaeroway_points",
            "ohdm_t%3Acraft_points",
            "ohdm_t%3Abuilding_polygons",
            "ohdm_t%3Anatural_polygons",
            "ohdm_t%3Amilitary_polygons",
            "ohdm_t%3Awaterway_polygons",
            "ohdm_t%3Ageological_polygons",
            "ohdm_t%3Aaeroway_polygons",
            "ohdm_t%3Aemergency_polygons",
            "ohdm_t%3Alanduse_brown",
            "ohdm_t%3Alanduse_commercialetc",
            "ohdm_t%3Alanduse_freegreenandwood",
            "ohdm_t%3Alanduse_gardeningandfarm",
            "ohdm_t%3Alanduse_grey",
            "ohdm_t%3Alanduse_industrial",
            "ohdm_t%3Alanduse_military",
            "ohdm_t%3Alanduse_residentaletc",
            "ohdm_t%3Alanduse_transport",
            "ohdm_t%3Alanduse_water",
            "ohdm_t%3Abuilding_polygons_label",
            "ohdm_t%3Anatural_polygons_label",
            "ohdm_t%3Amilitary_polygons_label",
            "ohdm_t%3Awaterway_polygons_label",
            "ohdm_t%3Ageological_polygons_label",
            "ohdm_t%3Aaeroway_polygons_label",
            "ohdm_t%3Aemergency_polygons_label",
            "ohdm_t%3Alanduse_brown_label",
            "ohdm_t%3Alanduse_commercialetc_label",
            "ohdm_t%3Alanduse_freegreenandwood_label_label",
            "ohdm_t%3Alanduse_gardeningandfarm_label",
            "ohdm_t%3Alanduse_grey_label",
            "ohdm_t%3Alanduse_industrial_label",
            "ohdm_t%3Alanduse_military_label",
            "ohdm_t%3Alanduse_residentaletc_label",
            "ohdm_t%3Alanduse_transport_label",
            "ohdm_t%3Alanduse_water_label",*/
            "ohdm_t%3AStacked"
            };

    private MapView mMapView;
    private Button btnst;
    private MapController mMapController;
    private static final double[] TILE_ORIGIN = {-20037508.34789244, 20037508.34789244};
    private static final double MAP_SIZE = 20037508.34789244 * 2;
    double mIncr = 0.01;
    private boolean gpsActive;
    private double posX = 0, posY = 0;
    Gps gps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        gps = new Gps(this);
        gpsActive = false;
        mMapView = (MapView) findViewById(R.id.mapview);
        btnst = (Button) findViewById(R.id.btn_start_stop);

        List tilesSources = new ArrayList<TilesOverlay>();

        mMapController = (MapController) mMapView.getController();
        mMapController.setZoom(13);
        mMapView.setTileSource(getTilesource(LAYERS[0]));

        for (int i = 0; i < LAYERS.length; i++) {
            MapTileProviderBasic tileProvider = new MapTileProviderBasic(getApplicationContext());
            tileProvider.setTileSource(getTilesource(LAYERS[i]));
            TilesOverlay tilesOverlay = new TilesOverlay(tileProvider, this.getBaseContext());
            tilesOverlay.setLoadingBackgroundColor(Color.TRANSPARENT);

            mMapView.getOverlays().add(tilesOverlay);
        }


        //mMapView.setTileSource(getTilesource("ohdm_t%3Anatural_polygons"));
        mMapView.setBuiltInZoomControls(true);
        GeoPoint gPt = new GeoPoint(52.520007, 13.404953999999975);
        mMapController.setCenter(gPt);

    }

    List<GeoPoint> pts = new ArrayList<>();
    List<GeoPoint> ptssave = new ArrayList<>();

    //
    //* URL = new URL();
    //* ULL.setAction(POST);
    //* Webaction.doURL(url);
    //

    public void setOverlayPoints(GeoPoint gpt1) {
        //GeoPoint gPt0 = new GeoPoint(52.4559828, 13.378583);
        //GeoPoint gPt1 = new GeoPoint(gPt0.getLatitudeE6()+ mIncr, gPt0.getLongitudeE6());
        //GeoPoint gPt2 = new GeoPoint(gPt0.getLatitudeE6()+ mIncr, gPt0.getLongitudeE6() + mIncr);
        //GeoPoint gPt3 = new GeoPoint(gPt0.getLatitudeE6(), gPt0.getLongitudeE6() + mIncr);
        //mMapController.setCenter(gPt0);
        pts.add(gpt1);
        //print(""+pts);
    }

    Polyline line = new Polyline();

    public void drawOverlay() {
        /**pts.add(new GeoPoint(52.4557948, 13.383958));
        pts.add(new GeoPoint(52.4557948 + mIncr, 13.383958));
        pts.add(new GeoPoint(52.4557948 + mIncr, 13.383958 + mIncr));
        pts.add(new GeoPoint(52.4557948, 13.383958 + mIncr));
        pts.add(new GeoPoint(52.4557948, 13.383958));**/

        print(""+pts);

        line.setWidth(5f);
        line.setColor(Color.RED);

        line.setPoints(pts);
        line.setGeodesic(true);
        mMapView.getOverlayManager().add(line);
    }

    protected double[] getBoundingBox(int x, int y, int zoom) {
        double tileSize = MAP_SIZE / Math.pow(2, zoom);
        double minx = TILE_ORIGIN[0] + x * tileSize;
        double maxx = TILE_ORIGIN[0] + (x + 1) * tileSize;
        double miny = TILE_ORIGIN[1] - (y + 1) * tileSize;
        double maxy = TILE_ORIGIN[1] - y * tileSize;

        double[] bbox = new double[4];
        bbox[0] = minx;
        bbox[1] = miny;
        bbox[2] = maxx;
        bbox[3] = maxy;
        return bbox;
    }

    private OnlineTileSourceBase getTilesource(String map) {
        String baseURL = "http://ohm.f4.htw-berlin.de:8080/geoserver/ohdm_t/wms?";
        String version = "1.3.0";
        String request = "GetMap";
        String format = "image%2Fpng";
        String srs = "EPSG%3A3857";
        String service = "WMS";
        String width = "256";
        String height = "256";
        String styles = "";
        String layers = map;
        String date = "2017-01-01";

        final String URL_STRING = baseURL +
                "SERVICE=" + service +
                "&VERSION=" + version +
                "&REQUEST=" + request +
                "&FORMAT=" + format +
                "&TRANSPARENT=true" +
                "&LAYERS=" + layers +
                "&format=" + format +
                "&date=" + date +
                "&WIDTH=" + width +
                "&HEIGHT=" + height +
                "&CRS=" + srs +
                "&STYLES=" + styles +
                "&BBOX=";
        String[] urlArray = {URL_STRING};
        return new OnlineTileSourceBase("USGS_TOPO", 0, 18, 256, "", urlArray) {
            @Override
            public String getTileURLString(MapTile aTile) {

                double[] bbox = getBoundingBox(aTile.getX(), aTile.getY(), aTile.getZoomLevel());

                // String url = getBaseUrl() + aTile.getY() + "%2C" + aTile.getX() + "%2C"  + aTile.getZoomLevel() + mImageFilenameEnding;

                String url = getBaseUrl() + bbox[0] + "%2C" + bbox[1] + "%2C" + bbox[2] + "%2C" + bbox[3] + mImageFilenameEnding;
                // http://ohm.f4.htw-berlin.de:8080/geoserver/ohdm_p/wms?SERVICE=WMS&VERSION=1.3.0&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&LAYERS=ohdm_t%3Aboundaries_admin_2&format=image%2Fpng&date=2017-01-01&WIDTH=256&HEIGHT=256&CRS=EPSG%3A3857&STYLES=&BBOX=1467590.943075385%2C6887893.4928338025%2C1487158.82231639%2C6907461.372074808
                // http://ohm.f4.htw-berlin.de:8080/geoserver/ohdm_p/wms?SERVICE=WMS&VERSION=1.3.0&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&LAYERS=ohdm_p%3Aboundaries_admin_2&format=image%2Fpng&date=2017-01-01&WIDTH=256&HEIGHT=256&CRS=EPSG%3A3857&STYLES=&BBOX=2682%2C4397%2C13
                Log.d("GENERATED URL", url);
                return url;
// url = "http://ohm.f4.htw-berlin.de:8080/geoserver/ohdm_p/wms?SERVICE=WMS&VERSION=1.3.0&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&LAYERS=ohdm_p%3Aboundaries_admin_2&format=image%2Fpng&date=2017-01-01&WIDTH=256&HEIGHT=256&CRS=EPSG%3A3857&STYLES=&BBOX=1467590.943075385%2C6887893.4928338025%2C1487158.82231639%2C6907461.372074808";

            }
        };
    }

    public void start_stop_gps(View v) {

        if(gpsActive)
        {
            print("stopping GPS");
            LocationManager locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
            locationManager.removeUpdates(gps);
            gpsActive = false;
            btnst.setText("start");
        }
        else{
            print("starting GPS");
            LocationManager locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,"No Permission",Toast.LENGTH_LONG).show();
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gps);
            gpsActive = true;
            btnst.setText("stop");
            try {
                drawOverlay();
            }catch (Exception e){
                print(e.getMessage());
            }
        }
        //gps.onLocationChanged();
    }

    public void save(View v)
    {
        print("save");
        ptssave = pts;
        pts.clear();
        drawOverlay();
        refreshMap();
        //mMapView.getOverlayManager().remove(line);
    }

    private void print(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
    public void setGPS(double lon, double lat)
    {
        this.posX = lon;
        this.posY = lat;
        setCamera(lat,lon);
        setOverlayPoints(new GeoPoint(lat, lon));
        //setOverlayPoints(new GeoPoint(52.4557948, 13.383958));
        //setOverlayPoints(new GeoPoint(52.4557948 + mIncr, 13.383958));
        drawOverlay();
        print("test");
    }

    public void setCamera(double x, double y)
    {
        this.mMapController.setCenter(new GeoPoint(x,y));
    }

    public void refreshMap()
    {
        this.mMapView.invalidate();
    }
}
