package android.ohdm.de.editor.activities.ViewMode;

import android.content.Context;
import android.ohdm.de.editor.R;
import android.ohdm.de.editor.activities.MainActivity;
import android.ohdm.de.editor.geometry.PolyObjectManager;
import android.view.View;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;

public class ViewModeAdd implements ViewMode{

    private PolyObjectManager polyObjectManager;
    private Context context;
    private ViewModeContext viewModeContext;

    ViewModeAdd(PolyObjectManager polyObjectManager, Context context, ViewModeContext viewModeContext){
        this.polyObjectManager = polyObjectManager;
        this.context = context;
        this.viewModeContext = viewModeContext;
    }

    @Override
    public void change() {

        polyObjectManager.setObjectsClickable(false);
        polyObjectManager.setActiveObjectEditable(false);
        polyObjectManager.setSelectedObjectEditable(false);

        ((MainActivity)this.context).changeEditButtonsVisibility(View.INVISIBLE);
        ((MainActivity)this.context).changeAddButtonsVisibility(View.VISIBLE);
    }

    @Override
    public void singleTap(GeoPoint geoPoint) {
        polyObjectManager.addPointToSelectedPolyObject(geoPoint);
    }

    @Override
    public void buttonAddCancel() {

    }

    @Override
    public void buttonAddAccept() {
        //TODO: alles erledigen durch viewModeContext.setState(Mode.VIEW) und dadurch keine extra methode benötigen?
        polyObjectManager.setActiveObjectEditable(false);
        polyObjectManager.deselectActiveObject();
        ((MainActivity)this.context).changeAddButtonsVisibility(View.INVISIBLE);
        viewModeContext.setState(Mode.VIEW);
//        map.invalidate();
    }

    @Override
    public void buttonEditDelete() {
        if (!polyObjectManager.removeSelectedCornerPoint()) {
            Toast.makeText(((MainActivity)this.context), R.string.no_edit_point_selected_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void buttonAddUndo() {
        polyObjectManager.removeLastPointFromSelectedPolyObject();
    }

    @Override
    public void onStop() {
        polyObjectManager.setActiveObjectEditable(false);
        polyObjectManager.deselectActiveObject();
    }
}
