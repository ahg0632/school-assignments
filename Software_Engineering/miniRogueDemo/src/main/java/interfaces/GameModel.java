package interfaces;

/**
 * Core interface for all game model components in the Mini Rogue Demo.
 * Defines the contract for game state management and observer notifications.
 */
public interface GameModel {
    /**
     * MANDATORY: Method called by model implementations to notify
     * interested observers of state changes.
     *
     * @param event String describing the change event
     * @param data Object containing event-specific data
     */
    void notify_observers(String event, Object data);

    /**
     * MANDATORY: Add an observer to receive model change notifications
     *
     * @param observer The GameObserver to add
     */
    void add_observer(GameObserver observer);

    /**
     * MANDATORY: Remove an observer from notifications
     *
     * @param observer The GameObserver to remove
     */
    void remove_observer(GameObserver observer);
} 