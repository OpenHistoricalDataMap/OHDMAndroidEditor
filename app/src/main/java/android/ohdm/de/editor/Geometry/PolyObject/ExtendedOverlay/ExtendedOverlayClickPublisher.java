package android.ohdm.de.editor.Geometry.PolyObject.ExtendedOverlay;

public interface ExtendedOverlayClickPublisher {

    public void subscribe(ExtendedOverlayClickListener listener);
    public void remove(ExtendedOverlayClickListener listener);
}
