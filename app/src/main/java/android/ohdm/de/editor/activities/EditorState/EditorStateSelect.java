package android.ohdm.de.editor.activities.EditorState;

import android.content.Context;
import android.ohdm.de.editor.R;
import android.ohdm.de.editor.activities.MainActivity;
import android.ohdm.de.editor.geometry.PolyObjectManager;
import android.view.View;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;

public class EditorStateSelect implements EditorState {

    private PolyObjectManager polyObjectManager;
    private Context context;
    private EditorStateContext editorStateContext;

    EditorStateSelect(PolyObjectManager polyObjectManager, Context context, EditorStateContext editorStateContext) {
        this.polyObjectManager = polyObjectManager;
        this.context = context;
        this.editorStateContext = editorStateContext;
    }

    @Override
    public void change() {

        polyObjectManager.setObjectsClickable(true);
        polyObjectManager.setActiveObjectEditable(false);
        polyObjectManager.setSelectedObjectEditable(false);
        polyObjectManager.deselectActiveObject();

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
        editorStateContext.setState(State.VIEW);
    }

    @Override
    public void buttonAddAccept() {
        editorStateContext.setState(State.EDIT);
    }

    @Override
    public void buttonEditDelete() {
            if(!polyObjectManager.removeSelectedObject()){

                Toast.makeText(((MainActivity) this.context),R.string.no_area_selected_error, Toast.LENGTH_SHORT).show();
            }else{
                editorStateContext.setState(State.VIEW);
            }
    }

    @Override
    public void buttonAddUndo() {

    }
}
