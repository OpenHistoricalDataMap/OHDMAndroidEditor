package android.ohdm.de.editor.activities.ViewMode;

import android.content.Context;
import android.ohdm.de.editor.geometry.PolyObjectManager;
import android.util.Log;

import org.osmdroid.util.GeoPoint;

public class ViewModeContext {

    private PolyObjectManager polyObjectManager;
    private Context context;
    private ViewMode viewMode;
    private ViewMode.Mode state;

    public ViewModeContext(ViewMode.Mode mode,PolyObjectManager polyObjectManager, Context context) {

        this.polyObjectManager = polyObjectManager;
        this.context = context;

        setState(mode);
    }

    public void setState(ViewMode.Mode mode){

        this.state = mode;

        switch (mode){
            case VIEW:
                viewMode = new ViewModeView(polyObjectManager,context,this);
                break;
            case SELECT:
                viewMode = new ViewModeSelect(polyObjectManager,context,this);
                break;
            case ADD:
                viewMode = new ViewModeAdd(polyObjectManager,context,this);
                break;
            case EDIT:
                viewMode = new ViewModeEdit(polyObjectManager,context,this);
                break;
            default:
                Log.e("ViewModeContext","Mode is not implemented yet ("+mode+")");
        }

        viewMode.change();
    }

    public ViewMode.Mode getState(){
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

    public void onStop(){
        viewMode.onStop();
    }

    public void buttonAddUndo(){
        viewMode.buttonAddUndo();
    }

}

