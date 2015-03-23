package android.ohdm.de.editor;

public interface ZoomPublisher {

    void subscribe(ZoomSubscriber subscriber);
    void remove(ZoomSubscriber subscriber);
}
