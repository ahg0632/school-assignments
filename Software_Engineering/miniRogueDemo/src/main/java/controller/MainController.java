package controller;

import interfaces.GameStateManager;
import interfaces.InputHandler;
import model.gameLogic.GameLogic;
import view.GameView;
import view.input.InputManager;
import view.input.KeyboardInputHandler;
import view.input.MouseInputHandler;
import view.input.KeyBindingManager;
import enums.GameState;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Main controller class that coordinates between Model, View, and Input handling.
 * Implements proper MVC architecture by separating concerns.
 */
public class MainController implements InputHandler {
    
    private final GameLogic gameLogic;
    private final GameView gameView;
    private final GameStateManager stateManager;
    private final InputManager inputManager;
    private final KeyboardInputHandler keyboardHandler;
    private final MouseInputHandler mouseHandler;
    private final KeyBindingManager keyBindings;
    
    public MainController(GameLogic gameLogic, GameView gameView) {
        this.gameLogic = gameLogic;
        this.gameView = gameView;
        this.stateManager = new GameStateManagerImpl();
        this.keyBindings = new KeyBindingManager();
        
        // Create input manager first
        this.inputManager = new InputManager(gameView);
        
        // Create input handlers with the input manager
        this.keyboardHandler = new KeyboardInputHandler(inputManager);
        this.mouseHandler = new MouseInputHandler(inputManager);
        
        // Set up observers
        gameLogic.add_observer((interfaces.GameObserver)gameView);
        gameView.setController(this);
    }
    
    /**
     * Initializes the controller and starts the game loop.
     */
    public void initialize() {
        stateManager.setState(GameState.MAIN_MENU);
        // Note: inputManager.initialize() method doesn't exist yet
    }
    
    /**
     * Updates the controller state.
     * Called each frame to process game logic and input.
     */
    public void update() {
        // Update input manager (if update method exists)
        // inputManager.update();
        
        // Process game logic based on current state
        if (stateManager.isPlaying()) {
            gameLogic.update();
        }
        
        // Update view
        gameView.update();
    }
    
    /**
     * Handles game state transitions.
     */
    public void handleStateTransition(GameState newState) {
        if (newState == null) {
            return; // Return early for null state transitions
        }
        stateManager.setState(newState);
        
        switch (newState) {
            case PLAYING:
                startGame();
                break;
            case PAUSED:
                pauseGame();
                break;
            case MAIN_MENU:
                returnToMenu();
                break;
            case GAME_OVER:
                endGame();
                break;
            default:
                // Handle other states as needed
                break;
        }
    }
    
    private void startGame() {
        gameLogic.startGame();
    }
    
    private void pauseGame() {
        gameLogic.pauseGame();
    }
    
    private void returnToMenu() {
        gameLogic.resetGame();
    }
    
    private void endGame() {
        gameLogic.endGame();
    }
    
    // InputHandler implementation - delegate to input handlers
    @Override
    public void handleKeyPressed(KeyEvent e) {
        if (!isActive()) return;
        keyboardHandler.processKeyEvent(e);
    }
    
    @Override
    public void handleKeyReleased(KeyEvent e) {
        if (!isActive()) return;
        keyboardHandler.processKeyEvent(e);
    }
    
    @Override
    public void handleMouseClicked(MouseEvent e) {
        if (!isActive()) return;
        mouseHandler.processMouseEvent(e);
    }
    
    @Override
    public void handleMousePressed(MouseEvent e) {
        if (!isActive()) return;
        mouseHandler.processMouseEvent(e);
    }
    
    @Override
    public void handleMouseReleased(MouseEvent e) {
        if (!isActive()) return;
        mouseHandler.processMouseEvent(e);
    }
    
    @Override
    public void handleMouseMoved(MouseEvent e) {
        if (!isActive()) return;
        mouseHandler.processMouseEvent(e);
    }
    
    @Override
    public void handleMouseDragged(MouseEvent e) {
        if (!isActive()) return;
        mouseHandler.processMouseEvent(e);
    }
    

    
    @Override
    public boolean isActive() {
        return !stateManager.isGameOver();
    }
    
    // Getters for components
    public GameLogic getGameLogic() {
        return gameLogic;
    }
    
    public GameView getGameView() {
        return gameView;
    }
    
    public GameStateManager getStateManager() {
        return stateManager;
    }
    
    public InputManager getInputManager() {
        return inputManager;
    }
    
    public KeyBindingManager getKeyBindings() {
        return keyBindings;
    }
} 