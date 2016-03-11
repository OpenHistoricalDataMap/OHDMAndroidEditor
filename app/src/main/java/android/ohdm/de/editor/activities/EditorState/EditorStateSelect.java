package android.ohdm.de.editor.activities.EditorState;

import android.content.Context;
import android.ohdm.de.editor.R;
import android.ohdm.de.editor.activities.MainActivity;
import android.ohdm.de.editor.geometry.PolyObjectManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;

/**
 * State to select existing PolyObject.
 */
public class EditorStateSelect implements EditorState {

    private static final String TAG = "EditorStateSelect";
    private PolyObjectManager polyObjectManager;
    private Context context;
    private EditorStateContext editorStateContext;

    /**
     * Constructor.
     *
     * @param polyObjectManager PolyObjectManager
     * @param context Context
     * @param editorStateContext EditorStateContext
     */
    EditorStateSelect(PolyObjectManager polyObjectManager, Context context, EditorStateContext editorStateContext)
    {
        this.polyObjectManager = polyObjectManager;
        this.context = context;
        this.editorStateContext = editorStateContext;
    }

    /**
     * Sets Select-State settings.
     */
    @Override
    public void change()
    {
        Log.d(TAG,"change()");
        polyObjectManager.setObjectsClickable(true);
        polyObjectManager.setSelectedObjectEditable(false);

        ((MainActivity) this.context).changeAddButtonsVisibility(View.INVISIBLE);
        ((MainActivity) this.context).changeEditButtonsVisibility(View.VISIBLE);
    }

    /**
     * Empty.
     *
     * @param geoPoint GeoPoint
     */
    @Override
    public void singleTap(GeoPoint geoPoint) {

    }

    /**
     * Changes to the View-State.
     */
    @Override
    public void buttonAddCancel()
    {
        ((MainActivity) this.context).changeEditButtonsVisibility(View.INVISIBLE);

        polyObjectManager.setObjectsClickable(false);
        polyObjectManager.deselectActiveObject();
        editorStateContext.setState(State.VIEW);
    }

    /**
     * If Object selected changes to the Edit-State.
     */
    @Override
    public void buttonAddAccept()
    {
        if(polyObjectManager.hasActiveObject())
        {
            editorStateContext.setState(State.EDIT);
        }else
        {
            editorStateContext.setState(State.VIEW);
        }
    }

    /**
     * Deletes selected Object from the MapView.
     */
    @Override
    public void buttonEditDelete()
    {
            if(!polyObjectManager.removeSelectedObject())
            {
                Toast.makeText(this.context,R.string.no_area_selected_error, Toast.LENGTH_SHORT).show();
            }else
            {
                editorStateContext.setState(State.VIEW);
            }
    }

    /**
     * Empty.
     */
    @Override
    public void buttonAddUndo() {

    }


    /**
     * Searches for near by PolyObjects.
     *
     * @param geoPoint GeoPoint
     */
    @Override
    public void longPress(GeoPoint geoPoint)
    {
        Toast.makeText(this.context,"Long Press", Toast.LENGTH_SHORT).show();

        ((MainActivity) this.context).downloadNearByPolyObjects(geoPoint);
    }
}
