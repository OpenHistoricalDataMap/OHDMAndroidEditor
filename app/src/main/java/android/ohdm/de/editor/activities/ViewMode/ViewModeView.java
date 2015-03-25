package android.ohdm.de.editor.activities.ViewMode;

import android.content.Context;
import android.ohdm.de.editor.activities.MainActivity;
import android.ohdm.de.editor.geometry.PolyObjectManager;
import android.view.View;

import org.osmdroid.util.GeoPoint;

public class ViewModeView implements ViewMode{

    private PolyObjectManager polyObjectManager;
    private Context context;
    private ViewModeContext viewModeContext;

    ViewModeView(PolyObjectManager polyObjectManager, Context context, ViewModeContext viewModeContext){
        this.polyObjectManager = polyObjectManager;
        this.context = context;
        this.viewModeContext = viewModeContext;
    }

    @Override
    public void change() {

        polyObjectManager.setObjectsClickable(false);
        polyObjectManager.setActiveObjectEditable(false);
        polyObjectManager.setSelectedObjectEditable(false);
        polyObjectManager.deselectActiveObject();

        ((MainActivity)this.context).changeAddButtonsVisibility(View.INVISIBLE);
        ((MainActivity)this.context).changeEditButtonsVisibility(View.INVISIBLE);
    }

    @Override
    public void singleTap(GeoPoint geoPoint) {

    }

    @Override
    public void buttonAddCancel() {

    }

    @Override
    public void buttonAddAccept() {

    }

    @Override
    public void buttonEditDelete() {

    }

    @Override
    public void buttonAddUndo() {

    }

    @Override
    public void onStop() {

    }
}
