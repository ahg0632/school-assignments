package interfaces;

/**
 * Core interface for all view components in the Mini Rogue Demo.
 * Defines the contract for UI components that display game state.
 */
public interface GameView {
    /**
     * MANDATORY: Update the view display based on model changes
     */
    void update_display();

    /**
     * MANDATORY: Get the controller managing this view
     *
     * @return The GameController instance
     */
    GameController get_controller();

    /**
     * MANDATORY: Set the controller for this view
     *
     * @param controller The GameController to set
     */
    void set_controller(GameController controller);

    /**
     * MANDATORY: Initialize view components
     */
    void initialize_components();

    /**
     * MANDATORY: Handle model change notifications
     *
     * @param event The event type
     * @param data The event data
     */
    void on_model_changed(String event, Object data);

    /**
     * MANDATORY: Get the game panel
     *
     * @return The GamePanel instance
     */
    view.panels.GamePanel get_game_panel();

    /**
     * MANDATORY: Show the window
     */
    void show_window();
} 