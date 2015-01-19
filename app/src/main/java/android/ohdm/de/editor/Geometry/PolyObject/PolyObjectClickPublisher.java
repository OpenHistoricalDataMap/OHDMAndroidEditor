package android.ohdm.de.editor.Geometry.PolyObject;

public interface PolyObjectClickPublisher {

    void subscribe(PolyObjectClickListener listener);
    void remove(PolyObjectClickListener listener);
}
