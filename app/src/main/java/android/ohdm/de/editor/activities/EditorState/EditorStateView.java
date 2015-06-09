package android.ohdm.de.editor.activities.EditorState;

import android.content.Context;
import android.ohdm.de.editor.activities.MainActivity;
import android.ohdm.de.editor.geometry.PolyObjectManager;
import android.view.View;

import org.osmdroid.util.GeoPoint;

public class EditorStateView implements EditorState {

    private PolyObjectManager polyObjectManager;
    private Context context;
    private EditorStateContext editorStateContext;

    EditorStateView(PolyObjectManager polyObjectManager, Context context, EditorStateContext editorStateContext){
        this.polyObjectManager = polyObjectManager;
        this.context = context;
        this.editorStateContext = editorStateContext;
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
    public void longPress(GeoPoint geoPoint) {

    }
}
