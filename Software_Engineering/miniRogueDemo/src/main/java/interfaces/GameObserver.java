package interfaces;

/**
 * Observer interface for receiving notifications about game model changes.
 * Implements the Observer pattern for loose coupling between model and view.
 */
public interface GameObserver {
    /**
     * MANDATORY: Method called when observed model changes
     *
     * @param event String describing what changed
     * @param data Object containing change-specific information
     */
    void on_model_changed(String event, Object data);
} 