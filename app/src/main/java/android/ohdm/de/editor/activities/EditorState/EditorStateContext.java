package android.ohdm.de.editor.activities.EditorState;

import android.content.Context;
import android.ohdm.de.editor.geometry.PolyObjectManager;
import android.util.Log;

import org.osmdroid.util.GeoPoint;

/**
 * Handle for the ADD, SELECT, EDIT and VIEW States.
 */
public class EditorStateContext
{
    private static final String TAG = "EditorStateContext";

    private PolyObjectManager polyObjectManager;
    private Context context;
    private EditorState viewMode;
    private EditorState.State state;

    /**
     * Constructor.
     *
     * @param state EditorState.State
     * @param polyObjectManager PolyObjectManager
     * @param context Context
     */
    public EditorStateContext(EditorState.State state, PolyObjectManager polyObjectManager, Context context) {

        this.polyObjectManager = polyObjectManager;
        this.context = context;

        setState(state);
    }

    /**
     * Setter.
     *
     * @param state EditorState.State
     */
    public void setState(EditorState.State state){

        this.state = state;

        switch (state){
            case VIEW:
                viewMode = new EditorStateView(polyObjectManager,context,this);
                break;
            case SELECT:
                viewMode = new EditorStateSelect(polyObjectManager,context,this);
                break;
            case ADD:
                viewMode = new EditorStateAdd(polyObjectManager,context,this);
                break;
            case EDIT:
                viewMode = new EditorStateEdit(polyObjectManager,context,this);
                break;
            default:
                Log.e(TAG,"Mode is not implemented yet ("+ state +")");
        }

        viewMode.change();
    }

    /**
     * Getter.
     *
     * @return state
     */
    public EditorState.State getState(){
        return this.state;
    }

    /**
     * Handle SileTap-Event.
     *
     * @param geoPoint GeoPoint
     */
    public void singleTap(GeoPoint geoPoint){
        viewMode.singleTap(geoPoint);
    }

    /**
     * Add-Button-Event cancel.
     */
    public void buttonAddCancel(){
        viewMode.buttonAddCancel();
    }

    /**
     * Add-Button-Event accept.
     */
    public void buttonAddAccept(){
        viewMode.buttonAddAccept();
    }

    /**
     * Edit-Button-Event delete.
     */
    public void buttonEditDelete(){
        viewMode.buttonEditDelete();
    }

    /**
     * Add-Button-Event undo.
     */
    public void buttonAddUndo(){
        viewMode.buttonAddUndo();
    }

    /**
     * Handle LongPress-Event.
     *
     * @param geoPoint GeoPoint
     */
    public void longPress(GeoPoint geoPoint) {
        viewMode.longPress(geoPoint);
    }
}

