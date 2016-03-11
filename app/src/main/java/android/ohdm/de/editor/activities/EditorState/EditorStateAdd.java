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
 * State to add Point, Line or Polygon to the MapView.
 */
public class EditorStateAdd implements EditorState {

    private static final String TAG = "EditorStateAdd";
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
    EditorStateAdd(PolyObjectManager polyObjectManager,
                   Context context,
                   EditorStateContext editorStateContext)
    {
        this.polyObjectManager = polyObjectManager;
        this.context = context;
        this.editorStateContext = editorStateContext;
    }

    /**
     * Sets all Add-State settings.
     */
    @Override
    public void change() {

        Log.d(TAG,"change()");
        polyObjectManager.setObjectsClickable(false);
//        polyObjectManager.setActiveObjectEditable(false);
        polyObjectManager.setSelectedObjectEditable(false);

        ((MainActivity)this.context).changeEditButtonsVisibility(View.INVISIBLE);
        ((MainActivity)this.context).changeAddButtonsVisibility(View.VISIBLE);
    }

    /**
     * Handle for Single-Tap-Event.
     *
     * @param geoPoint GeoPoint
     */
    @Override
    public void singleTap(GeoPoint geoPoint)
    {
        polyObjectManager.addPointToSelectedPolyObject(geoPoint);
    }

    /**
     * Empty.
     */
    @Override
    public void buttonAddCancel()
    {

    }

    /**
     * Save the PolyObject created on the MapView.
     */
    @Override
    public void buttonAddAccept() {
        //TODO: alles erledigen durch viewModeContext.setState(Mode.VIEW) und dadurch keine extra methode benötigen?
        polyObjectManager.setActiveObjectEditable(false);
        polyObjectManager.deselectActiveObject();
        ((MainActivity)this.context).changeAddButtonsVisibility(View.INVISIBLE);
        editorStateContext.setState(State.VIEW);
//        map.invalidate();
    }

    /**
     * Delete selected Point.
     */
    @Override
    public void buttonEditDelete()
    {
        if (!polyObjectManager.removeSelectedEditPoint())
        {
            Toast.makeText(this.context, R.string.no_edit_point_selected_error, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Empty.
     */
    @Override
    public void buttonAddUndo()
    {
        polyObjectManager.removeLastPointFromSelectedPolyObject();
    }

    /**
     * Empty.
     *
     * @param geoPoint GeoPoint
     */
    @Override
    public void longPress(GeoPoint geoPoint)
    {

    }
}
