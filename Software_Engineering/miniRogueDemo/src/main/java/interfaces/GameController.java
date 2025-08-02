package interfaces;

/**
 * Core interface for all controller components in the Mini Rogue Demo.
 * Defines the contract for handling user input and coordinating MVC components.
 */
public interface GameController {
    /**
     * MANDATORY: Handle user input events
     *
     * @param input String representing the user input
     */
    void handle_input(String input);

    /**
     * Overloaded: Handle user input events with additional data
     *
     * @param input String representing the user input
     * @param data  Additional data for the input
     */
    default void handle_input(String input, Object data) {
        handle_input(input); // Default: fallback to original
    }

    /**
     * MANDATORY: Get the model this controller manages
     *
     * @return The GameModel instance
     */
    GameModel get_model();

    /**
     * MANDATORY: Set the model for this controller
     *
     * @param model The GameModel to manage
     */
    void set_model(GameModel model);

    /**
     * MANDATORY: Get the view this controller manages
     *
     * @return The GameView instance
     */
    GameView get_view();

    /**
     * MANDATORY: Set the view for this controller
     *
     * @param view The GameView to manage
     */
    void set_view(GameView view);

    /**
     * MANDATORY: End the game
     *
     * @param victory True if the player won, false if they lost
     * @param killer  The name of the entity that killed the player
     */
    void end_game(boolean victory, String killer);
} 