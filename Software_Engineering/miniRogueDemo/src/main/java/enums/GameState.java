package enums;

/**
 * Enumeration defining all possible game states in Mini Rogue Demo.
 * Used by GameLogic to manage game flow and transitions.
 */
public enum GameState {
    MAIN_MENU("mainMenu"),
    CLASS_SELECTION("classSelection"),
    PLAYING("playing"),
    BATTLE("battle"),
    INVENTORY("inventory"),
    PAUSED("paused"),
    GAME_OVER("gameOver"),
    VICTORY("victory");

    private final String stateName;

    GameState(String stateName) {
        this.stateName = stateName;
    }

    public String get_state_name() {
        return stateName;
    }
} 