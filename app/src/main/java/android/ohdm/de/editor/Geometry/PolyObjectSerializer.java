package android.ohdm.de.editor.Geometry;

import android.content.Context;
import android.ohdm.de.editor.Geometry.PolyObject.PolyObject;
import android.ohdm.de.editor.Geometry.PolyObject.PolyObjectFactory;
import android.ohdm.de.editor.Geometry.PolyObject.PolyObjectType;
import android.util.Log;

import org.osmdroid.views.MapView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class PolyObjectSerializer {

    private static final String TAG = "PolyObjectSerializer";
    private static final String SER_KEY = "de.ohdm.editor.ser";

    public static void serialize(PolyObjectManager polyObjectManager, MapView mapView) {

        try {
            FileOutputStream fos = mapView.getContext().openFileOutput(SER_KEY, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);

            os.writeObject(polyObjectManager.getPolyObjectList());
            os.close();

        } catch (IOException e) {
            Log.e(TAG, "seriaizing: " + e.toString());
        }
    }

    public static PolyObjectManager deserialize(MapView map) {

        PolyObjectManager polyObjectManager = new PolyObjectManager(map);

        try {
            FileInputStream fis = map.getContext().openFileInput(SER_KEY);
            ObjectInputStream is = new ObjectInputStream(fis);

            List<PolyObject> loadedPolyObjectList = (List<PolyObject>) is.readObject();

            for (PolyObject loadedObject : loadedPolyObjectList) {

                PolyObject polyObject = null;

                switch (loadedObject.getType()) {
                    case POLYLINE:
                        polyObject = PolyObjectFactory.buildObject(PolyObjectType.POLYLINE, map);
                        polyObject.setPoints(loadedObject.getPoints());
                        polyObjectManager.addObject(polyObject);
                        break;
                    case POLYGON:
                        polyObject = PolyObjectFactory.buildObject(PolyObjectType.POLYGON, map);
                        polyObject.setPoints(loadedObject.getPoints());
                        polyObjectManager.addObject(polyObject);
                        break;
                    case POINT:
                        polyObject = PolyObjectFactory.buildObject(PolyObjectType.POINT, map);
                        polyObject.setPoints(loadedObject.getPoints());
                        polyObjectManager.addObject(polyObject);
                        break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "deserializing: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "deserializing: " + e.toString());
        }

        return polyObjectManager;
    }
}
