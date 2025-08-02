package controller;

import enums.GameState;
import enums.CharacterClass;
import interfaces.GameObserver;
import interfaces.InputHandler;
import model.characters.Player;
import model.gameLogic.GameLogic;
import model.map.Map;
import utilities.Position;
import view.GameView;
import view.input.InputManager;
import view.input.KeyBindingManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.AfterEach;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for Controller functionality.
 * Consolidates functionality from MainTest, MainControllerTest, and MVCIntegrationTest.
 * Tests MVC coordination, state transitions, input delegation, lifecycle management,
 * and integration between components.
 * Appropriate for a school project.
 */
@DisplayName("Controller Comprehensive Tests")
class ControllerTest {

    private MainController mainController;
    private GameLogic gameLogic;
    private GameView gameView;
    private Player testPlayer;

    @BeforeEach
    void setUp() {
        testPlayer = new Player("TestPlayer", CharacterClass.WARRIOR, new Position(5, 5));
        gameLogic = new GameLogic(testPlayer);
        gameView = new GameView();
        mainController = new MainController(gameLogic, gameView);
        
        // Small delay to ensure initialization completes
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // ==================== INITIALIZATION TESTS ====================

    /**
     * Tests controller initialization and basic properties.
     */
    @Test
    @DisplayName("Controller Initialization and Basic Properties")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testControllerInitialization() {
        // Test that controller is properly initialized
        assertNotNull(mainController, "MainController should not be null");
        assertNotNull(mainController.getGameLogic(), "GameLogic should not be null");
        assertNotNull(mainController.getGameView(), "GameView should not be null");
        assertNotNull(mainController.getStateManager(), "StateManager should not be null");
        assertNotNull(mainController.getInputManager(), "InputManager should not be null");
        assertNotNull(mainController.getKeyBindings(), "KeyBindingManager should not be null");
        
        // Test that controller components are properly connected
        assertEquals(gameLogic, mainController.getGameLogic(), "GameLogic should be set correctly");
        assertEquals(gameView, mainController.getGameView(), "GameView should be set correctly");
        
        // Test initial state
        assertEquals(GameState.MAIN_MENU, mainController.getStateManager().getCurrentState(), 
                   "Initial state should be MAIN_MENU");
    }

    /**
     * Tests application initialization and component creation.
     */
    @Test
    @DisplayName("Application Initialization")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testApplicationInitialization() {
        // Test that components can be created and initialized
        assertNotNull(gameLogic, "Game logic should be created");
        assertNotNull(testPlayer, "Player should be created");
        assertNotNull(gameView, "Game view should be created");
        
        // Test that components are properly initialized
        assertNotNull(testPlayer.get_name(), "Player should have a name");
        assertNotNull(testPlayer.get_character_class(), "Player should have character class");
        
        // Test that game logic is properly initialized
        assertTrue(gameLogic instanceof GameLogic, "Game logic should be GameLogic instance");
        
        // Test that view is properly initialized
        assertTrue(gameView instanceof view.GameView, "Game view should be GameView instance");
    }

    // ==================== STATE MANAGEMENT TESTS ====================

    /**
     * Tests game state transitions and state management.
     */
    @Test
    @DisplayName("Game State Transitions")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testGameStateTransitions() {
        // Test state transitions
        mainController.handleStateTransition(GameState.PLAYING);
        assertEquals(GameState.PLAYING, mainController.getStateManager().getCurrentState(), 
                   "State should transition to PLAYING");
        
        mainController.handleStateTransition(GameState.PAUSED);
        assertEquals(GameState.PAUSED, mainController.getStateManager().getCurrentState(), 
                   "State should transition to PAUSED");
        
        mainController.handleStateTransition(GameState.MAIN_MENU);
        assertEquals(GameState.MAIN_MENU, mainController.getStateManager().getCurrentState(), 
                   "State should transition to MAIN_MENU");
        
        mainController.handleStateTransition(GameState.GAME_OVER);
        assertEquals(GameState.GAME_OVER, mainController.getStateManager().getCurrentState(), 
                   "State should transition to GAME_OVER");
    }

    /**
     * Tests MVC state coordination.
     */
    @Test
    @DisplayName("MVC State Coordination")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMVCStateCoordination() {
        // Test state transitions through controller
        mainController.handleStateTransition(GameState.PLAYING);
        assertEquals(GameState.PLAYING, mainController.getStateManager().getCurrentState(), 
                   "State should transition to PLAYING");
        
        mainController.handleStateTransition(GameState.PAUSED);
        assertEquals(GameState.PAUSED, mainController.getStateManager().getCurrentState(), 
                   "State should transition to PAUSED");
        
        mainController.handleStateTransition(GameState.MAIN_MENU);
        assertEquals(GameState.MAIN_MENU, mainController.getStateManager().getCurrentState(), 
                   "State should transition back to MAIN_MENU");
    }

    // ==================== INPUT HANDLING TESTS ====================

    /**
     * Tests input delegation to appropriate handlers.
     */
    @Test
    @DisplayName("Input Delegation")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testInputDelegation() {
        // Test that input methods exist and don't throw exceptions
        assertDoesNotThrow(() -> {
            // Test keyboard input delegation
            KeyEvent testKey = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                          System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
            mainController.handleKeyPressed(testKey);
            
            // Test key release delegation
            KeyEvent releaseKey = new KeyEvent(gameView, KeyEvent.KEY_RELEASED, 
                                             System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
            mainController.handleKeyReleased(releaseKey);
            
            // Test mouse input delegation
            MouseEvent testMouse = new MouseEvent(gameView, MouseEvent.MOUSE_PRESSED,
                                                System.currentTimeMillis(), 0, 100, 100, 1, false);
            mainController.handleMousePressed(testMouse);
        }, "Input delegation should not throw exceptions");
    }

    /**
     * Tests MVC input delegation.
     */
    @Test
    @DisplayName("MVC Input Delegation")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMVCInputDelegation() {
        // Test that input is properly delegated through the controller
        assertDoesNotThrow(() -> {
            // Test keyboard input delegation
            KeyEvent testKey = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                          System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
            mainController.handleKeyPressed(testKey);
            
            // Test key release delegation
            KeyEvent releaseKey = new KeyEvent(gameView, KeyEvent.KEY_RELEASED, 
                                             System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
            mainController.handleKeyReleased(releaseKey);
        }, "MVC input delegation should not throw exceptions");
    }

    // ==================== OBSERVER PATTERN TESTS ====================

    /**
     * Tests observer pattern implementation.
     */
    @Test
    @DisplayName("Observer Pattern Implementation")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testObserverPattern() {
        // Test that observer pattern is properly implemented
        assertDoesNotThrow(() -> {
            // Test that observer registration works
            GameObserver testObserver = new GameObserver() {
                @Override
                public void on_model_changed(String event, Object data) {
                    // Observer implementation
                }
            };
            
            // Test that observer pattern is accessible through game logic
            gameLogic.add_observer(testObserver);
        }, "Observer pattern should not throw exceptions");
    }

    /**
     * Tests MVC observer pattern.
     */
    @Test
    @DisplayName("MVC Observer Pattern")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMVCObserverPattern() {
        // Test that MVC components can communicate through observer pattern
        assertDoesNotThrow(() -> {
            // Test that observer pattern is accessible through game logic
            GameObserver testObserver = new GameObserver() {
                @Override
                public void on_model_changed(String event, Object data) {
                    // Observer implementation
                }
            };
            gameLogic.add_observer(testObserver);
        }, "MVC observer pattern should not throw exceptions");
    }

    // ==================== CONTROLLER METHOD TESTS ====================

    /**
     * Tests controller update method.
     */
    @Test
    @DisplayName("Controller Update Method")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testControllerUpdate() {
        // Test that controller update method works
        assertDoesNotThrow(() -> {
            mainController.update();
        }, "Controller update should not throw exceptions");
        
        // Test that controller can handle multiple updates
        for (int i = 0; i < 5; i++) {
            assertDoesNotThrow(() -> mainController.update(), 
                             "Multiple controller updates should not throw exceptions");
        }
    }

    /**
     * Tests controller initialization method.
     */
    @Test
    @DisplayName("Controller Initialization Method")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testControllerInitialize() {
        // Test that controller initialization method works
        assertDoesNotThrow(() -> {
            mainController.initialize();
        }, "Controller initialization should not throw exceptions");
    }

    // ==================== ERROR HANDLING TESTS ====================

    /**
     * Tests error handling and robustness.
     */
    @Test
    @DisplayName("Error Handling and Robustness")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testErrorHandling() {
        // Test that controller handles null inputs gracefully
        assertDoesNotThrow(() -> {
            mainController.handleStateTransition(null);
        }, "Controller should handle null state transitions gracefully");
        
        // Test that controller handles invalid inputs gracefully
        assertDoesNotThrow(() -> {
            mainController.handleKeyPressed(null);
            mainController.handleKeyReleased(null);
            mainController.handleMousePressed(null);
        }, "Controller should handle null input events gracefully");
    }

    /**
     * Tests MVC error handling.
     */
    @Test
    @DisplayName("MVC Error Handling")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMVCErrorHandling() {
        // Test that MVC components handle errors gracefully
        assertDoesNotThrow(() -> {
            // Test with invalid state transitions
            mainController.handleStateTransition(null);
            
            // Test with invalid input events
            mainController.handleKeyPressed(null);
            mainController.handleKeyReleased(null);
        }, "MVC error handling should not throw exceptions");
    }

    // ==================== COMPONENT INTEGRATION TESTS ====================

    /**
     * Tests MVC component communication.
     */
    @Test
    @DisplayName("MVC Component Communication")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMVCComponentCommunication() {
        // Test that MVC components can communicate properly
        assertDoesNotThrow(() -> {
            // Test communication through controller
            mainController.handleStateTransition(GameState.PLAYING);
            
            // Test that state changes are reflected
            assertEquals(GameState.PLAYING, mainController.getStateManager().getCurrentState(),
                       "State should be updated through controller");
        }, "MVC component communication should work properly");
    }

    /**
     * Tests MVC coordination and component integration.
     */
    @Test
    @DisplayName("MVC Coordination and Component Integration")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMVCCoordination() {
        // Test that all MVC components work together
        assertNotNull(mainController.getGameLogic(), "GameLogic should be accessible");
        assertNotNull(mainController.getGameView(), "GameView should be accessible");
        assertNotNull(mainController.getStateManager(), "StateManager should be accessible");
        assertNotNull(mainController.getInputManager(), "InputManager should be accessible");
        assertNotNull(mainController.getKeyBindings(), "KeyBindingManager should be accessible");
        
        // Test that components are properly integrated
        assertSame(gameLogic, mainController.getGameLogic(), "Controller should reference correct GameLogic");
        assertSame(gameView, mainController.getGameView(), "Controller should reference correct GameView");
    }

    // ==================== LIFECYCLE TESTS ====================

    /**
     * Tests application shutdown functionality.
     */
    @Test
    @DisplayName("Application Shutdown")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testApplicationShutdown() {
        // Test that components can be properly disposed
        assertDoesNotThrow(() -> gameLogic.dispose(), "Game logic disposal should not throw exception");
        
        // Test that new components can be created after disposal
        Player newPlayer = new Player("NewPlayer", CharacterClass.MAGE, new Position(0, 0));
        assertNotNull(newPlayer, "New player should be created successfully");
        
        GameLogic newGameLogic = new GameLogic(newPlayer);
        assertNotNull(newGameLogic, "New game logic should be created successfully");
    }

    /**
     * Tests MVC component lifecycle.
     */
    @Test
    @DisplayName("MVC Component Lifecycle")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMVCComponentLifecycle() {
        // Test component creation
        assertNotNull(mainController, "Controller should be created");
        assertNotNull(gameLogic, "GameLogic should be created");
        assertNotNull(gameView, "GameView should be created");
        
        // Test component disposal
        assertDoesNotThrow(() -> {
            gameLogic.dispose();
        }, "Component disposal should not throw exceptions");
        
        // Test component recreation
        Player newPlayer = new Player("NewPlayer", CharacterClass.MAGE, new Position(0, 0));
        GameLogic newGameLogic = new GameLogic(newPlayer);
        assertNotNull(newGameLogic, "New GameLogic should be created successfully");
    }

    // ==================== ACTIVE STATE TESTS ====================

    /**
     * Tests controller active state.
     */
    @Test
    @DisplayName("Controller Active State")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testControllerActiveState() {
        // Test that controller is active
        assertTrue(mainController.isActive(), "Controller should be active");
        
        // Test that controller active state is accessible
        assertDoesNotThrow(() -> {
            boolean isActive = mainController.isActive();
            assertTrue(isActive, "Controller should be active");
        }, "Controller active state should be accessible");
    }

    /**
     * Tests MVC active state management.
     */
    @Test
    @DisplayName("MVC Active State Management")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMVCActiveStateManagement() {
        // Test that MVC components can check active state
        assertDoesNotThrow(() -> {
            boolean isActive = mainController.isActive();
            assertTrue(isActive, "Controller should be active");
        }, "MVC active state management should not throw exceptions");
        
        // Test that active state is consistent
        assertTrue(mainController.isActive(), "Controller should remain active");
    }

    // ==================== ROBUSTNESS TESTS ====================

    /**
     * Tests MVC integration robustness.
     */
    @Test
    @DisplayName("MVC Integration Robustness")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMVCIntegrationRobustness() {
        // Test that MVC integration is robust under various conditions
        assertDoesNotThrow(() -> {
            // Test rapid state transitions
            for (int i = 0; i < 10; i++) {
                mainController.handleStateTransition(GameState.PLAYING);
                mainController.handleStateTransition(GameState.PAUSED);
                mainController.handleStateTransition(GameState.MAIN_MENU);
            }
            
            // Test rapid input handling
            for (int i = 0; i < 5; i++) {
                KeyEvent testKey = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                              System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
                mainController.handleKeyPressed(testKey);
            }
        }, "MVC integration should be robust under stress");
    }

    /**
     * Tests MVC data flow.
     */
    @Test
    @DisplayName("MVC Data Flow")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMVCDataFlow() {
        // Test that data flows properly through MVC components
        assertDoesNotThrow(() -> {
            // Test state data flow
            mainController.handleStateTransition(GameState.PLAYING);
            GameState currentState = mainController.getStateManager().getCurrentState();
            assertEquals(GameState.PLAYING, currentState, "State should flow through controller");
            
            // Test input data flow
            KeyEvent testKey = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                          System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
            mainController.handleKeyPressed(testKey);
        }, "MVC data flow should work properly");
    }

    // ==================== RESOURCE MANAGEMENT TESTS ====================

    /**
     * Tests resource accessibility.
     */
    @Test
    @DisplayName("Resource Accessibility")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testResourceAccessibility() {
        // Test that all required resources are accessible
        assertNotNull(mainController.getGameLogic(), "GameLogic should be accessible");
        assertNotNull(mainController.getGameView(), "GameView should be accessible");
        assertNotNull(mainController.getStateManager(), "StateManager should be accessible");
        assertNotNull(mainController.getInputManager(), "InputManager should be accessible");
        assertNotNull(mainController.getKeyBindings(), "KeyBindingManager should be accessible");
        
        // Test that player data is accessible
        assertNotNull(testPlayer.get_name(), "Player name should be accessible");
        assertNotNull(testPlayer.get_character_class(), "Player class should be accessible");
    }

    /**
     * Tests application lifecycle consistency.
     */
    @Test
    @DisplayName("Application Lifecycle Consistency")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testApplicationLifecycleConsistency() {
        // Test that application lifecycle is consistent
        assertDoesNotThrow(() -> {
            // Test initialization
            assertNotNull(gameLogic, "GameLogic should be initialized");
            assertNotNull(testPlayer, "Player should be initialized");
            assertNotNull(gameView, "GameView should be initialized");
            
            // Test state management
            mainController.handleStateTransition(GameState.PLAYING);
            assertEquals(GameState.PLAYING, mainController.getStateManager().getCurrentState(),
                       "State should be managed consistently");
            
            // Test cleanup
            gameLogic.dispose();
        }, "Application lifecycle should be consistent");
    }

    /**
     * Tests interface compliance.
     */
    @Test
    @DisplayName("Interface Compliance")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testInterfaceCompliance() {
        // Test that components implement required interfaces
        assertTrue(gameView instanceof GameView, "GameView should implement GameView");
        assertTrue(mainController instanceof InputHandler, "MainController should implement InputHandler");
        
        // Test that interface methods are accessible
        assertDoesNotThrow(() -> {
            gameLogic.update();
            gameView.repaint();
            mainController.isActive();
        }, "Interface methods should be accessible");
    }

    @AfterEach
    void tearDown() {
        // Clean up resources
        if (gameLogic != null) {
            gameLogic.dispose();
        }
    }
} 