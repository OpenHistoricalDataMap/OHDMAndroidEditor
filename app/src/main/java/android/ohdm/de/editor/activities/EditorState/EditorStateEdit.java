package android.ohdm.de.editor.activities.EditorState;

import android.content.Context;
import android.ohdm.de.editor.R;
import android.ohdm.de.editor.activities.MainActivity;
import android.ohdm.de.editor.geometry.PolyObjectManager;
import android.view.View;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;

public class EditorStateEdit implements EditorState {

    private PolyObjectManager polyObjectManager;
    private Context context;
    private EditorStateContext editorStateContext;

    EditorStateEdit(PolyObjectManager polyObjectManager, Context context, EditorStateContext editorStateContext){
        this.polyObjectManager = polyObjectManager;
        this.context = context;
        this.editorStateContext = editorStateContext;
    }

    @Override
    public void change() {

        ((MainActivity)this.context).changeEditButtonsVisibility(View.INVISIBLE);
        polyObjectManager.setObjectsClickable(false);
        polyObjectManager.setSelectedObjectEditable(true);

        if (polyObjectManager.isSelectedObjectEditable()) {

            ((MainActivity)this.context).changeAddButtonsVisibility(View.VISIBLE);
        }
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
        polyObjectManager.setActiveObjectEditable(false);
        polyObjectManager.deselectActiveObject();
        ((MainActivity)this.context).changeAddButtonsVisibility(View.INVISIBLE);
        editorStateContext.setState(State.VIEW);
    }

    @Override
    public void buttonEditDelete() {
        if (!polyObjectManager.removeSelectedCornerPoint()) {
            Toast.makeText(((MainActivity) this.context), R.string.no_edit_point_selected_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void buttonAddUndo() {
        polyObjectManager.removeLastPointFromSelectedPolyObject();
    }
}
