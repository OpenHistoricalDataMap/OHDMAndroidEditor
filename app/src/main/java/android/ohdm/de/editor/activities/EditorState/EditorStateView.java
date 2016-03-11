package android.ohdm.de.editor.activities.EditorState;

import android.content.Context;
import android.ohdm.de.editor.activities.MainActivity;
import android.ohdm.de.editor.geometry.PolyObjectManager;
import android.util.Log;
import android.view.View;

import org.osmdroid.util.GeoPoint;

/**
 * State shows just the MapView.
 */
public class EditorStateView implements EditorState
{
    private PolyObjectManager polyObjectManager;
    private static final String TAG = "EditorStateView";
    private Context context;
    private EditorStateContext editorStateContext;

    /**
     * Constructor.
     *
     * @param polyObjectManager PolyObjectManager
     * @param context Context
     * @param editorStateContext EditorStateContext
     */
    EditorStateView(PolyObjectManager polyObjectManager,
                    Context context,
                    EditorStateContext editorStateContext)
    {
        this.polyObjectManager  = polyObjectManager;
        this.context            = context;
        this.editorStateContext = editorStateContext;
    }

    /**
     * Sets View-State settings.
     */
    @Override
    public void change()
    {
        Log.d(TAG, "change()");
        polyObjectManager.setObjectsClickable(false);
        polyObjectManager.setActiveObjectEditable(false);
        polyObjectManager.setSelectedObjectEditable(false);
        polyObjectManager.deselectActiveObject();

        ((MainActivity)this.context).changeAddButtonsVisibility(View.INVISIBLE);
        ((MainActivity)this.context).changeEditButtonsVisibility(View.INVISIBLE);
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
     * Empty.
     */
    @Override
    public void buttonAddCancel() {

    }

    /**
     * Empty.
     */
    @Override
    public void buttonAddAccept() {

    }

    /**
     * Empty.
     */
    @Override
    public void buttonEditDelete() {

    }

    /**
     * Empty.
     */
    @Override
    public void buttonAddUndo() {

    }

    /**
     * Empty.
     *
     * @param geoPoint GeoPoint
     */
    @Override
    public void longPress(GeoPoint geoPoint) {

    }
}
