package android.ohdm.de.editor.geometry.PolyObject.ExtendedOverlay;

public interface ExtendedOverlayClickPublisher {

    void subscribe(ExtendedOverlayClickListener listener);
    void remove(ExtendedOverlayClickListener listener);
}
