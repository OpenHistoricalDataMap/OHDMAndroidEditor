package android.ohdm.de.editor.activities.EditorState;

import org.osmdroid.util.GeoPoint;

/**
 * Interface.
 */
public interface EditorState
{
    enum State {
        ADD, SELECT, EDIT, VIEW
    }

    void change();
    void singleTap(GeoPoint geoPoint);
    void buttonAddCancel();
    void buttonAddAccept();
    void buttonEditDelete();
    void buttonAddUndo();
    void longPress(GeoPoint geoPoint);
}

