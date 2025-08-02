package interfaces;

import enums.GameState;

/**
 * Interface for game state management.
 * This establishes a contract for all game state operations.
 */
public interface GameStateManager {
    
    /**
     * Gets the current game state.
     * 
     * @return the current game state enum
     */
    GameState getCurrentState();
    
    /**
     * Sets the game state.
     * 
     * @param state the new game state
     */
    void setState(GameState state);
    
    /**
     * Checks if the game is in a specific state.
     * 
     * @param state the state to check for
     * @return true if the game is in the specified state, false otherwise
     */
    boolean isInState(GameState state);
    
    /**
     * Checks if the game is currently playing.
     * 
     * @return true if the game is in PLAYING state, false otherwise
     */
    boolean isPlaying();
    
    /**
     * Checks if the game is paused.
     * 
     * @return true if the game is in PAUSED state, false otherwise
     */
    boolean isPaused();
    
    /**
     * Checks if the game is in a menu state.
     * 
     * @return true if the game is in a menu state, false otherwise
     */
    boolean isInMenu();
    
    /**
     * Checks if the game is over.
     * 
     * @return true if the game is in GAME_OVER state, false otherwise
     */
    boolean isGameOver();
    
    /**
     * Gets the previous game state.
     * 
     * @return the previous game state, or null if there was no previous state
     */
    GameState getPreviousState();
    
    /**
     * Reverts to the previous game state.
     * 
     * @return true if reverted successfully, false if no previous state exists
     */
    boolean revertToPreviousState();
} 