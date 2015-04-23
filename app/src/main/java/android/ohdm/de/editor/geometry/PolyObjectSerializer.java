package android.ohdm.de.editor.geometry;

import android.content.Context;
import android.ohdm.de.editor.OHDMMapView;
import android.ohdm.de.editor.geometry.PolyObject.PolyObject;
import android.ohdm.de.editor.geometry.PolyObject.PolyObjectFactory;
import android.ohdm.de.editor.geometry.PolyObject.PolyObjectType;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class PolyObjectSerializer {

    private static final String TAG = "PolyObjectSerializer";
    private static final String SER_KEY = "de.ohdm.editor.ser";

    private PolyObjectSerializer(){

    }

    public static void serialize(PolyObjectManager polyObjectManager, OHDMMapView mapView) {

        try {
            FileOutputStream fos = mapView.getContext().openFileOutput(SER_KEY, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);

            os.writeObject(polyObjectManager.getPolyObjectList());
            os.close();

        } catch (IOException e) {
            Log.e(TAG, "serializing: " + e.toString());
        }
    }

    public static PolyObjectManager deserialize(OHDMMapView map) {

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
                        break;
                    case POLYGON:
                        polyObject = PolyObjectFactory.buildObject(PolyObjectType.POLYGON, map);
                        break;
                    case POINT:
                        polyObject = PolyObjectFactory.buildObject(PolyObjectType.POINT, map);
                        break;
                }

                polyObject.setPoints(loadedObject.getPoints());
                polyObject.setTags(loadedObject.getTags());
                polyObject.setId(loadedObject.getId());
                polyObjectManager.addObject(polyObject);
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
