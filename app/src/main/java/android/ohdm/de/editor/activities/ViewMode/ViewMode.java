package android.ohdm.de.editor.activities.ViewMode;

import org.osmdroid.util.GeoPoint;

public interface ViewMode {

    public enum Mode {
        ADD, SELECT, EDIT, VIEW
    }

    public void change();
    public void singleTap(GeoPoint geoPoint);
    public void buttonAddCancel();
    public void buttonAddAccept();
    public void buttonEditDelete();
    public void buttonAddUndo();
    public void onStop();
}
