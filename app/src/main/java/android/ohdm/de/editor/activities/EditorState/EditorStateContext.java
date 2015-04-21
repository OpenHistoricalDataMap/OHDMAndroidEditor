package android.ohdm.de.editor.activities.EditorState;

import android.content.Context;
import android.ohdm.de.editor.geometry.PolyObjectManager;
import android.util.Log;

import org.osmdroid.util.GeoPoint;

public class EditorStateContext {

    private static final String TAG = "EditorStateContext";

    private PolyObjectManager polyObjectManager;
    private Context context;
    private EditorState viewMode;
    private EditorState.State state;

    public EditorStateContext(EditorState.State state, PolyObjectManager polyObjectManager, Context context) {

        this.polyObjectManager = polyObjectManager;
        this.context = context;

        setState(state);
    }

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

    public EditorState.State getState(){
        return this.state;
    }

    public void singleTap(GeoPoint geoPoint){
        viewMode.singleTap(geoPoint);
    }

    public void buttonAddCancel(){
        viewMode.buttonAddCancel();
    }

    public void buttonAddAccept(){
        viewMode.buttonAddAccept();
    }

    public void buttonEditDelete(){
        viewMode.buttonEditDelete();
    }

    public void buttonAddUndo(){
        viewMode.buttonAddUndo();
    }

}

