package com.example.justin.editor.maps;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

/**
 * Created by Justin on 24.11.2017.
 */

public class TileProviderFactory {

    public static GeoServerTileProvider getGeoServerTileProvider() {
/**
 * http://ohm.f4.htw-berlin.de:8080/geoserver/ohdm_t/wms?
 * SERVICE=WMS
 * VERSION=1.3.0&
 * REQUEST=GetMap&
 * FORMAT=image%2Fpng&
 * TRANSPARENT=true&
 * LAYERS=ohdm_t%3Aboundaries_admin_2
 * &format=image%2Fpng&date=2017-01-01&
 * WIDTH=256&
 * HEIGHT=256&
 * CRS=EPSG%3A3857&
 * STYLES=
 * &BBOX=1467590.943075385%2C6887893.4928338025%2C1487158.82231639%2C6907461.372074808
 *
 *
 * http://ohm.f4.htw-berlin.de:8080/geoserver/ohdm_p/wms?SERVICE=WMS&VERSION=1.3.0&REQUEST=GetMap&FORMAT=image/png&TRANSPARENT=true&LAYERS=ohdm_p:boundaries_admin_9&format=image/png&date=2017-01-01&WIDTH=256&HEIGHT=256&CRS=EPSG:3857&STYLES=&BBOX=13.0.2686.0.4400
 * http://ohm.f4.htw-berlin.de:8080/geoserver/ohdm_p/wms?SERVICE=WMS&VERSION=1.3.0&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&LAYERS=ohdm_t%3Aboundaries_admin_2&format=image%2Fpng&date=2017-01-01&WIDTH=256&HEIGHT=256&CRS=EPSG%3A3857&STYLES=&BBOX=1467590.943075385%2C6887893.4928338025%2C1487158.82231639%2C6907461.372074808
 * http%3A%2F%2Fohm.f4.htw-berlin.de%3A8080%2Fgeoserver%2Fohdm_p%2Fwms%3FSERVICE%3DWMS%26VERSION%3D1.3.0%26REQUEST%3DGetMap%26FORMAT%3Dimage%2Fpng%26TRANSPARENT%3Dtrue%26LAYERS%3Dohdm_p%3Aboundaries_admin_9%26format%3Dimage%2Fpng%26date%3D2017-01-01%26WIDTH%3D256%26HEIGHT%3D256%26CRS%3DEPSG%3A3857%26STYLES%3D%26BBOX%3D13+2685+4402
 * */
        String baseURL = "http://ohm.f4.htw-berlin.de:8080/geoserver/ohdm_p/wms?";
        String version = "1.3.0";
        String request = "GetMap";
        String format = "image/png";
        String srs = "EPSG:3857";
        String service = "WMS";
        String width = "256";
        String height = "256";
        String styles = "";
        String layers = "ohdm_p:boundaries_admin_9";
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
                "&BBOX=%f,%f,%f,%f";



        GeoServerTileProvider tileProvider =
                new GeoServerTileProvider(256,256) {

                    @Override
                    public synchronized URL getTileUrl(int x, int y, int zoom) {
                        try {

                            double[] bbox = getBoundingBox(x, y, zoom);

                            String s = String.format(Locale.US, URL_STRING, bbox[MINX],
                                    bbox[MINY], bbox[MAXX], bbox[MAXY]);

                            Log.d("GeoServerTileURL", s);

                            URL url = null;

                            try {
                                url = new URL(s);
                            }
                            catch (MalformedURLException e) {
                                throw new AssertionError(e);
                            }

                            return url;
                        }
                        catch (RuntimeException e) {
                            Log.d("GeoServerTileException",
                                    "getTile x=" + x + ", y=" + y +
                                            ", zoomLevel=" + zoom +
                                            " raised an exception", e);
                            throw e;
                        }

                    }
                };
        return tileProvider;
    }

}
