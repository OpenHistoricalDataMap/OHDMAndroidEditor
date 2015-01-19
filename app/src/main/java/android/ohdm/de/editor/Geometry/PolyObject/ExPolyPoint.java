package android.ohdm.de.editor.Geometry.PolyObject;

import android.content.Context;
import android.graphics.Color;
import android.ohdm.de.editor.Geometry.ExtendedOverlay.ExtendedPolygonOverlay;
import android.util.Log;

import org.osmdroid.bonuspack.overlays.OverlayWithIW;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.List;

public class ExPolyPoint extends PolyObject {

    private static final long serialVersionUID = 0L;

    private transient ExtendedPolygonOverlay polygon;
    private transient MapView mapView;
    private List<GeoPoint> points = new ArrayList<GeoPoint>();
    private List<GeoPoint> borderPoints = new ArrayList<GeoPoint>();
    private transient boolean selected = false;
    private transient boolean editing = false;
    private transient List<PolyObjectClickListener> listeners = new ArrayList<PolyObjectClickListener>();

    ExPolyPoint(MapView mapView){
        super(PolyObjectType.POINT);
        this.mapView = mapView;
        create(mapView.getContext());
    }

    @Override
    protected void create(Context context) {
        polygon = new ExtendedPolygonOverlay(context);
        polygon.subscribe(this);

        polygon.setFillColor(Color.BLUE);
        polygon.setStrokeWidth(4);

        borderPoints = createBorderPointsFromCenter(points);
        polygon.setPoints(borderPoints);
    }

    private List<GeoPoint> createBorderPointsFromCenter(List<GeoPoint> points){

        List<GeoPoint> createdPoints = new ArrayList<GeoPoint>();

        int radiusLon = mapView.getLongitudeSpan()/10;
        int radiusLat = mapView.getLatitudeSpan()/10;

        //TODO: could this cause bugs?
        if(points.isEmpty()) return createdPoints;

        GeoPoint geoPoint = points.get(points.size()-1);

//        GeoPoint tempPoint = new GeoPoint(geoPoint.getLatitudeE6(),geoPoint.getLongitudeE6()+radiusLon);
//        Log.i("ExPoint",tempPoint.toString());
//        createdPoints.add(tempPoint);
//
//        tempPoint = new GeoPoint(geoPoint.getLatitudeE6()+radiusLat,geoPoint.getLongitudeE6());
//        Log.i("ExPoint",tempPoint.toString());
//        createdPoints.add(tempPoint);
//
//        tempPoint = new GeoPoint(geoPoint.getLatitudeE6(),geoPoint.getLongitudeE6()-radiusLon);
//        Log.i("ExPoint",tempPoint.toString());
//        createdPoints.add(tempPoint);
//
//        tempPoint = new GeoPoint(geoPoint.getLatitudeE6()-radiusLat,geoPoint.getLongitudeE6());
//        Log.i("ExPoint",tempPoint.toString());
//        createdPoints.add(tempPoint);

        for (int i = 0; i <= 36; i++){

            double x = geoPoint.getLatitudeE6() + Math.sin(i*10 * (Math.PI / 180)) * radiusLat;
            double y = geoPoint.getLongitudeE6() + Math.cos(i*10) * radiusLon;

            GeoPoint point = new GeoPoint((int)x,(int)y);

            Log.i("ExPoint",point.toString());

            createdPoints.add(point);
        }

        return createdPoints;

    }

    static public List<GeoPoint> getGeodeticPoints(GeoPoint p1, GeoPoint p2, int numberpoints)
    {
        // adapted from page http://maps.forum.nu/gm_flight_path.html
        ArrayList<GeoPoint> fPoints = new ArrayList<GeoPoint>();
        // convert to radians
        double lat1 = (double) p1.getLatitudeE6() / 1E6 * Math.PI / 180;
        double lon1 = (double) p1.getLongitudeE6() / 1E6 * Math.PI / 180;
        double lat2 = (double) p2.getLatitudeE6() / 1E6 * Math.PI / 180;
        double lon2 = (double) p2.getLongitudeE6() / 1E6 * Math.PI / 180;

        double d = 2*Math.asin(Math.sqrt( Math.pow((Math.sin((lat1-lat2)/2)),2) + Math.cos(lat1)*Math.cos(lat2)*Math.pow((Math.sin((lon1-lon2)/2)),2)));
        double bearing = Math.atan2(Math.sin(lon1-lon2)*Math.cos(lat2), Math.cos(lat1)*Math.sin(lat2)-Math.sin(lat1)*Math.cos(lat2)*Math.cos(lon1-lon2))  / -(Math.PI/180);
        bearing = bearing < 0 ? 360 + bearing : bearing;

        for (int n = 0 ; n < numberpoints + 1; n++ ) {
            double f = (1.0/numberpoints) * n;
            double A = Math.sin((1-f)*d)/Math.sin(d);
            double B = Math.sin(f*d)/Math.sin(d);
            double x = A*Math.cos(lat1)*Math.cos(lon1) +  B*Math.cos(lat2)*Math.cos(lon2);
            double y = A*Math.cos(lat1)*Math.sin(lon1) +  B*Math.cos(lat2)*Math.sin(lon2);
            double z = A*Math.sin(lat1)           +  B*Math.sin(lat2);

            double latN = Math.atan2(z,Math.sqrt(Math.pow(x,2)+Math.pow(y,2)));
            double lonN = Math.atan2(y,x);
            fPoints.add(new GeoPoint((int) (latN/(Math.PI/180) * 1E6), (int) (lonN/(Math.PI/180) * 1E6)));
        }

        return fPoints;
    }

    @Override
    public OverlayWithIW getOverlay() {
        return polygon;
    }

    @Override
    public void setPoints(List<GeoPoint> points) {
        this.points = points;

        this.borderPoints = createBorderPointsFromCenter(points);
        polygon.setPoints(this.borderPoints);
    }

    public List<GeoPoint> getPoints(){
        return this.points;
    }

    @Override
    public void removeLastPoint() {
        if(!points.isEmpty()){
            points.remove(points.size()-1);
        }

        this.borderPoints = createBorderPointsFromCenter(this.points);
        polygon.setPoints(this.borderPoints);
    }

    @Override
    public void addPoint(GeoPoint geoPoint) {
        points.add(geoPoint);

        this.borderPoints = createBorderPointsFromCenter(this.points);
        polygon.setPoints(this.borderPoints);
    }

    @Override
    public boolean isClickable() {
        return polygon.isClickable();
    }

    @Override
    public void setClickable(boolean clickable) {
        polygon.setClickable(clickable);
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;

        if(selected){
            polygon.setFillColor(Color.RED);
        }else{
            polygon.setFillColor(Color.BLUE);
        }
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public boolean isEditing() {
        return editing;
    }

    @Override
    public void setEditing(boolean editing) {
        this.editing = editing;

        if(editing){
            polygon.setFillColor(Color.GREEN);
        }else{
            polygon.setFillColor(Color.BLUE);
        }
    }

    @Override
    public void onClick() {
        for(PolyObjectClickListener listener : listeners){
            listener.onClick(this);
        }
    }

    @Override
    public void subscribe(PolyObjectClickListener listener) {
        listeners.add(listener);
    }

    @Override
    public void remove(PolyObjectClickListener listener) {
        listeners.remove(listener);
    }
}