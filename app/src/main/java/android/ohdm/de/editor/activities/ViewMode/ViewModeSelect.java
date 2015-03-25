package android.ohdm.de.editor.activities.ViewMode;

import android.content.Context;
import android.ohdm.de.editor.R;
import android.ohdm.de.editor.activities.MainActivity;
import android.ohdm.de.editor.geometry.PolyObjectManager;
import android.view.View;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;

public class ViewModeSelect implements ViewMode {

    private PolyObjectManager polyObjectManager;
    private Context context;
    private ViewModeContext viewModeContext;

    ViewModeSelect(PolyObjectManager polyObjectManager, Context context, ViewModeContext viewModeContext) {
        this.polyObjectManager = polyObjectManager;
        this.context = context;
        this.viewModeContext = viewModeContext;
    }

    @Override
    public void change() {

        polyObjectManager.setObjectsClickable(true);
        polyObjectManager.setActiveObjectEditable(false);
        polyObjectManager.setSelectedObjectEditable(false);

        ((MainActivity) this.context).changeAddButtonsVisibility(View.INVISIBLE);
        ((MainActivity) this.context).changeEditButtonsVisibility(View.VISIBLE);
    }

    @Override
    public void singleTap(GeoPoint geoPoint) {

    }

    @Override
    public void buttonAddCancel() {

        ((MainActivity) this.context).changeEditButtonsVisibility(View.INVISIBLE);

        polyObjectManager.setObjectsClickable(false);
        polyObjectManager.deselectActiveObject();
        viewModeContext.setState(Mode.VIEW);
    }

    @Override
    public void buttonAddAccept() {
        viewModeContext.setState(Mode.EDIT);
    }

    @Override
    public void buttonEditDelete() {
            if(!polyObjectManager.removeSelectedObject()){

                Toast.makeText(((MainActivity) this.context),R.string.no_area_selected_error, Toast.LENGTH_SHORT).show();
            }else{
                viewModeContext.setState(Mode.VIEW);
            }
    }

    @Override
    public void buttonAddUndo() {

    }

    @Override
    public void onStop() {

    }
}
