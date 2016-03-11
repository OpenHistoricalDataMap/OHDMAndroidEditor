package android.ohdm.de.editor.geometry.PolyObject.ExtendedOverlay;

/**
 * Interface.
 */
public interface ExtendedOverlayClickPublisher {

    void subscribe(ExtendedOverlayClickListener listener);
    void remove(ExtendedOverlayClickListener listener);
}
