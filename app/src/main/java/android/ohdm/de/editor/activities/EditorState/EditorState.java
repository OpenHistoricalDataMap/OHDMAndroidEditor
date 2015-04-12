package android.ohdm.de.editor.activities.EditorState;

import org.osmdroid.util.GeoPoint;

public interface EditorState {

    public enum State {
        ADD, SELECT, EDIT, VIEW
    }

    public void change();
    public void singleTap(GeoPoint geoPoint);
    public void buttonAddCancel();
    public void buttonAddAccept();
    public void buttonEditDelete();
    public void buttonAddUndo();
}
