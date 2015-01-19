package android.ohdm.de.editor.Geometry.ExtendedOverlay;

public interface ExtendedOverlayClickPublisher {

    public void subscribe(ExtendedOverlayClickListener listener);
    public void remove(ExtendedOverlayClickListener listener);
}
