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

/**
 * Handles saving and reading PolyObjects from a File.
 */
public class PolyObjectSerializer {

    private static final String TAG = "PolyObjectSerializer";
    private static final String SER_KEY = "de.ohdm.editor.ser";

    private PolyObjectSerializer()
    {

    }

    /**
     * Saves PolyObjects to a local file.
     *
     * @param polyObjectManager PolyObjectManager
     * @param mapView OHDMMapView
     */
    public static void serialize(PolyObjectManager polyObjectManager, OHDMMapView mapView)
    {
        try
        {
            FileOutputStream fos = mapView.getContext().openFileOutput(SER_KEY, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);

            os.writeObject(polyObjectManager.getPolyObjectList());
            os.close();

        }
        catch (IOException e) {
            Log.e(TAG, "serializing: " + e.toString());
        }
    }

    /**
     * Reads PolyObjects from a file.
     *
     * @param map OHDMMapView
     *
     * @return PolyObjectManager
     */
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
                    default:
                        Log.e(TAG,"Not a supported PolyObject type");
                        throw new RuntimeException("Not a supported PolyObject type");
                }

                if(polyObject != null) {
                    polyObject.setPoints(loadedObject.getPoints());
                    polyObject.setTags(loadedObject.getTags());
                    polyObject.setId(loadedObject.getId());
                    polyObjectManager.addObject(polyObject);
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
