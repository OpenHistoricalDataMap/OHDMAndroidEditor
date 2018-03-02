package com.example.justin.editor.maps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.justin.editor.R;
import com.example.justin.editor.json.jsonService;
import com.example.justin.editor.listener.Gps;
import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.TilesOverlay;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

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
    private String json;
    Gps gps;
    jsonService js;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        gps = new Gps(this);
        gpsActive = false;
        js = new jsonService(this);
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

        mMapView.setBuiltInZoomControls(true);
        GeoPoint gPt = new GeoPoint(52.520007, 13.404953999999975);
        mMapController.setCenter(gPt);

    }

    List<GeoPoint> pts = new ArrayList<>();
    List<GeoPoint> ptssave = new ArrayList<>();

    public void setOverlayPoints(GeoPoint gpt1) {
        pts.add(gpt1);
    }

    Polyline line = new Polyline();

    public void drawOverlay() {
        //print(""+pts);

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
                String url = getBaseUrl() + bbox[0] + "%2C" + bbox[1] + "%2C" + bbox[2] + "%2C" + bbox[3] + mImageFilenameEnding;
                Log.d("GENERATED URL", url);
                return url;
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
    }

    public void save(View v)
    {
        print("save");
        ptssave = pts;
        try {
            json = js.createJson(ptssave,"LineString");
            showAlert(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        pts.clear();
        drawOverlay();
        refreshMap();
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
        drawOverlay();
    }

    public void setCamera(double x, double y)
    {
        this.mMapController.setCenter(new GeoPoint(x,y));
    }

    public void refreshMap()
    {
        this.mMapView.invalidate();
    }

    public void showAlert(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("JSON");
        builder.setMessage(msg);
        builder.setPositiveButton("Check",null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
