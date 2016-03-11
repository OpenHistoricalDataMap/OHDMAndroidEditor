package android.ohdm.de.editor;

/**
 * Interface. 
 */
public interface ZoomPublisher {

    void subscribe(ZoomSubscriber subscriber);
    void remove(ZoomSubscriber subscriber);
}
