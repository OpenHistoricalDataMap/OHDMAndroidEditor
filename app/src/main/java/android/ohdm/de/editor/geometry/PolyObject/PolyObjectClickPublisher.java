package android.ohdm.de.editor.geometry.PolyObject;

public interface PolyObjectClickPublisher {

    void subscribe(PolyObjectClickListener listener);
    void remove(PolyObjectClickListener listener);
}
