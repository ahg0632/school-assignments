package controller;

import interfaces.GameStateManager;
import enums.GameState;
import java.util.Stack;

/**
 * Concrete implementation of GameStateManager.
 * Handles game state transitions and maintains state history.
 */
public class GameStateManagerImpl implements GameStateManager {
    
    private GameState currentState;
    private GameState previousState;
    private final Stack<GameState> stateHistory;
    
    public GameStateManagerImpl() {
        this.currentState = GameState.MAIN_MENU;
        this.previousState = null;
        this.stateHistory = new Stack<>();
    }
    
    @Override
    public GameState getCurrentState() {
        return currentState;
    }
    
    @Override
    public void setState(GameState state) {
        if (state != null && state != currentState) {
            stateHistory.push(previousState);
            previousState = currentState;
            currentState = state;
        }
    }
    
    @Override
    public boolean isInState(GameState state) {
        return currentState == state;
    }
    
    @Override
    public boolean isPlaying() {
        return currentState == GameState.PLAYING;
    }
    
    @Override
    public boolean isPaused() {
        return currentState == GameState.PAUSED;
    }
    
    @Override
    public boolean isInMenu() {
        return currentState == GameState.MAIN_MENU || 
               currentState == GameState.CLASS_SELECTION;
    }
    
    @Override
    public boolean isGameOver() {
        return currentState == GameState.GAME_OVER;
    }
    
    @Override
    public GameState getPreviousState() {
        return previousState;
    }
    
    @Override
    public boolean revertToPreviousState() {
        if (!stateHistory.isEmpty()) {
            currentState = previousState;
            previousState = stateHistory.pop();
            return true;
        }
        return false;
    }
    
    /**
     * Gets the state history stack.
     * 
     * @return the state history stack
     */
    public Stack<GameState> getStateHistory() {
        Stack<GameState> copy = new Stack<>();
        copy.addAll(stateHistory);
        return copy;
    }
    
    /**
     * Clears the state history.
     */
    public void clearHistory() {
        stateHistory.clear();
        previousState = null;
    }
} 