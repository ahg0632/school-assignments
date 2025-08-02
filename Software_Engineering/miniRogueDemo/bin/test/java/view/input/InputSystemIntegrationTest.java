package view.input;

import enums.CharacterClass;
import model.characters.Player;
import model.gameLogic.GameLogic;
import model.map.Map;
import utilities.Position;
import view.GameView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the complete input system.
 * Tests the interaction between InputManager, KeyboardInputHandler, MouseInputHandler, and KeyBindingManager.
 * Follows MVC architecture and simple solutions approach.
 */
@DisplayName("Input System Integration Tests")
class InputSystemIntegrationTest {

    private InputManager inputManager;
    private KeyboardInputHandler keyboardHandler;
    private MouseInputHandler mouseHandler;
    private KeyBindingManager keyBindings;
    private GameView gameView;
    private Player testPlayer;
    private GameLogic gameLogic;
    private Map testMap;

    @BeforeEach
    void setUp() {
        // Create test components
        testPlayer = new Player("TestPlayer", CharacterClass.WARRIOR, new Position(5, 5));
        gameLogic = new GameLogic(testPlayer);
        gameView = new GameView();
        testMap = new Map(1, Map.FloorType.REGULAR);
        inputManager = new InputManager(gameView);
        
        // Set up dependencies
        inputManager.setPlayer(testPlayer);
        inputManager.setMap(testMap);
        keyboardHandler = new KeyboardInputHandler(inputManager);
        mouseHandler = new MouseInputHandler(inputManager);
        keyBindings = inputManager.getKeyBindings();
    }

    /**
     * Tests keyboard and mouse input coordination.
     */
    @Test
    @DisplayName("Keyboard and Mouse Input Coordination")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testKeyboardAndMouseInputCoordination() {
        // Test that keyboard and mouse can work together
        // Press keyboard movement key
        KeyEvent wKey = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                    System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
        keyboardHandler.processKeyEvent(wKey);
        
        // Verify keyboard input is tracked
        Set<Integer> movementKeys = inputManager.getMovementKeysHeld();
        assertTrue(movementKeys.contains(KeyEvent.VK_W), "Keyboard movement should be tracked");
        
        // Enable mouse aiming and test mouse input
        inputManager.setMouseAimingMode(true);
        MouseEvent mousePress = new MouseEvent(gameView, MouseEvent.MOUSE_PRESSED, 
                                             System.currentTimeMillis(), 0, 100, 100, 1, false, 1);
        mouseHandler.processMouseEvent(mousePress);
        
        // Verify mouse input is tracked
        assertTrue(inputManager.getMouseAttackHeld(), "Mouse attack should be tracked");
        
        // Verify both inputs can coexist
        assertTrue(movementKeys.contains(KeyEvent.VK_W), "Keyboard input should persist with mouse input");
        assertTrue(inputManager.getMouseAttackHeld(), "Mouse input should persist with keyboard input");
    }

    /**
     * Tests input state management across components.
     */
    @Test
    @DisplayName("Input State Management Across Components")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testInputStateManagementAcrossComponents() {
        // Test keyboard input state
        KeyEvent wKey = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                    System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
        keyboardHandler.processKeyEvent(wKey);
        
        // Test mouse input state
        inputManager.setMouseAimingMode(true);
        MouseEvent mousePress = new MouseEvent(gameView, MouseEvent.MOUSE_PRESSED, 
                                             System.currentTimeMillis(), 0, 100, 100, 1, false, 1);
        mouseHandler.processMouseEvent(mousePress);
        
        // Test attack key state
        KeyEvent spaceKey = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                        System.currentTimeMillis(), 0, KeyEvent.VK_SPACE, ' ');
        keyboardHandler.processKeyEvent(spaceKey);
        
        // Verify all states are properly managed
        assertTrue(inputManager.getMovementKeysHeld().contains(KeyEvent.VK_W), "Movement key should be held");
        assertTrue(inputManager.getMouseAttackHeld(), "Mouse attack should be held");
        assertTrue(inputManager.getAttackKeyHeld(), "Attack key should be held");
        
        // Test clearing all input states
        inputManager.clearAllHeldKeys();
        
        // Verify all states are cleared
        assertFalse(inputManager.getMovementKeysHeld().contains(KeyEvent.VK_W), "Movement key should be cleared");
        assertFalse(inputManager.getMouseAttackHeld(), "Mouse attack should be cleared");
        assertFalse(inputManager.getAttackKeyHeld(), "Attack key should be cleared");
    }

    /**
     * Tests key binding integration with input handlers.
     */
    @Test
    @DisplayName("Key Binding Integration with Input Handlers")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testKeyBindingIntegrationWithInputHandlers() {
        // Test that key bindings are available but not used by handlers (current implementation)
        // Change a key binding
        keyBindings.setKeyCode(KeyBindingManager.KeyCategory.MOVEMENT, "MOVE_UP", KeyEvent.VK_I);
        
        // Test that the new key is not tracked (handlers use hardcoded keys)
        KeyEvent iKey = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                    System.currentTimeMillis(), 0, KeyEvent.VK_I, 'I');
        keyboardHandler.processKeyEvent(iKey);
        
        // Verify the new key is not tracked
        Set<Integer> movementKeys = inputManager.getMovementKeysHeld();
        assertFalse(movementKeys.contains(KeyEvent.VK_I), "New movement key should not be tracked (hardcoded keys)");
        
        // Test that the original hardcoded key still works
        KeyEvent wKey = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                    System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
        keyboardHandler.processKeyEvent(wKey);
        assertTrue(movementKeys.contains(KeyEvent.VK_W), "Original hardcoded movement key should still work");
    }

    /**
     * Tests mouse aiming mode switching.
     */
    @Test
    @DisplayName("Mouse Aiming Mode Switching")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMouseAimingModeSwitching() {
        // Test keyboard aiming when mouse aiming is disabled
        inputManager.setMouseAimingMode(false);
        
        KeyEvent upKey = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                     System.currentTimeMillis(), 0, KeyEvent.VK_UP, '↑');
        keyboardHandler.processKeyEvent(upKey);
        
        Set<Integer> aimKeys = inputManager.getAimKeysHeld();
        assertTrue(aimKeys.contains(KeyEvent.VK_UP), "Keyboard aiming should work when mouse aiming is disabled");
        
        // Switch to mouse aiming mode
        inputManager.setMouseAimingMode(true);
        
        // Test that keyboard aiming is now ignored
        KeyEvent downKey = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                       System.currentTimeMillis(), 0, KeyEvent.VK_DOWN, '↓');
        keyboardHandler.processKeyEvent(downKey);
        
        assertFalse(aimKeys.contains(KeyEvent.VK_DOWN), "Keyboard aiming should be ignored in mouse mode");
        
        // Test that mouse aiming works
        MouseEvent mouseMove = new MouseEvent(gameView, MouseEvent.MOUSE_MOVED, 
                                            System.currentTimeMillis(), 0, 100, 150, 0, false);
        assertDoesNotThrow(() -> {
            mouseHandler.processMouseMoved(mouseMove);
        }, "Mouse aiming should work in mouse mode");
    }

    /**
     * Tests input delegation to game components.
     */
    @Test
    @DisplayName("Input Delegation to Game Components")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testInputDelegationToGameComponents() {
        // Test that input is properly delegated to player
        KeyEvent wKey = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                    System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
        keyboardHandler.processKeyEvent(wKey);
        
        // Test movement update
        assertDoesNotThrow(() -> {
            inputManager.updateMovement();
        }, "Movement update should not throw exceptions");
        
        // Test aiming update
        inputManager.setMouseAimingMode(false);
        KeyEvent upKey = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                     System.currentTimeMillis(), 0, KeyEvent.VK_UP, '↑');
        keyboardHandler.processKeyEvent(upKey);
        
        assertDoesNotThrow(() -> {
            inputManager.updateAiming();
        }, "Aiming update should not throw exceptions");
    }

    /**
     * Tests input system robustness with missing components.
     */
    @Test
    @DisplayName("Input System Robustness with Missing Components")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testInputSystemRobustnessWithMissingComponents() {
        // Create input manager without player
        InputManager standaloneInputManager = new InputManager(gameView);
        KeyboardInputHandler standaloneKeyboard = new KeyboardInputHandler(standaloneInputManager);
        MouseInputHandler standaloneMouse = new MouseInputHandler(standaloneInputManager);
        
        // Test that input requires player (current implementation behavior)
        KeyEvent wKey = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                    System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
        assertFalse(standaloneKeyboard.processKeyEvent(wKey), "Keyboard input should not work without player");
        
        MouseEvent mousePress = new MouseEvent(gameView, MouseEvent.MOUSE_PRESSED, 
                                             System.currentTimeMillis(), 0, 100, 100, 1, false, 1);
        standaloneInputManager.setMouseAimingMode(true);
        assertFalse(standaloneMouse.processMouseEvent(mousePress), "Mouse input should not work without player");
        
        // Test that movement and aiming updates don't throw exceptions
        assertDoesNotThrow(() -> {
            standaloneInputManager.updateMovement();
            standaloneInputManager.updateAiming();
        }, "Movement and aiming updates should not throw exceptions without player");
    }

    /**
     * Tests input system with movement pause.
     */
    @Test
    @DisplayName("Input System with Movement Pause")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testInputSystemWithMovementPause() {
        // Test input with movement paused
        inputManager.setMovementPaused(true);
        
        // Input should still be tracked even when movement is paused
        KeyEvent wKey = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                    System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
        keyboardHandler.processKeyEvent(wKey);
        
        Set<Integer> movementKeys = inputManager.getMovementKeysHeld();
        assertTrue(movementKeys.contains(KeyEvent.VK_W), "Movement key should be tracked even when paused");
        
        // Movement update should not throw exceptions when paused
        assertDoesNotThrow(() -> {
            inputManager.updateMovement();
        }, "Movement update should not throw exceptions when paused");
        
        // Resume movement
        inputManager.setMovementPaused(false);
        
        // Movement should work normally again
        assertDoesNotThrow(() -> {
            inputManager.updateMovement();
        }, "Movement update should work normally after resuming");
    }

    /**
     * Tests input system cleanup.
     */
    @Test
    @DisplayName("Input System Cleanup")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testInputSystemCleanup() {
        // Test that cleanup works without exceptions
        assertDoesNotThrow(() -> {
            inputManager.cleanup();
        }, "Input system cleanup should not throw exceptions");
        
        // Test that input still works after cleanup
        KeyEvent wKey = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                    System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
        assertTrue(keyboardHandler.processKeyEvent(wKey), "Input should still work after cleanup");
    }

    /**
     * Tests input system with multiple simultaneous inputs.
     */
    @Test
    @DisplayName("Input System with Multiple Simultaneous Inputs")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testInputSystemWithMultipleSimultaneousInputs() {
        // Test multiple keyboard inputs
        KeyEvent wKey = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                    System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
        KeyEvent dKey = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                    System.currentTimeMillis(), 0, KeyEvent.VK_D, 'D');
        KeyEvent spaceKey = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                        System.currentTimeMillis(), 0, KeyEvent.VK_SPACE, ' ');
        
        keyboardHandler.processKeyEvent(wKey);
        keyboardHandler.processKeyEvent(dKey);
        keyboardHandler.processKeyEvent(spaceKey);
        
        // Verify all inputs are tracked
        Set<Integer> movementKeys = inputManager.getMovementKeysHeld();
        assertTrue(movementKeys.contains(KeyEvent.VK_W), "W key should be held");
        assertTrue(movementKeys.contains(KeyEvent.VK_D), "D key should be held");
        assertTrue(inputManager.getAttackKeyHeld(), "Space key should be held");
        
        // Test mouse input with keyboard input
        inputManager.setMouseAimingMode(true);
        MouseEvent mousePress = new MouseEvent(gameView, MouseEvent.MOUSE_PRESSED, 
                                             System.currentTimeMillis(), 0, 100, 100, 1, false, 1);
        mouseHandler.processMouseEvent(mousePress);
        
        // Verify all inputs coexist
        assertTrue(movementKeys.contains(KeyEvent.VK_W), "Keyboard input should persist with mouse input");
        assertTrue(movementKeys.contains(KeyEvent.VK_D), "Multiple keyboard inputs should coexist");
        assertTrue(inputManager.getAttackKeyHeld(), "Keyboard attack should persist with mouse input");
        assertTrue(inputManager.getMouseAttackHeld(), "Mouse attack should coexist with keyboard input");
    }

    /**
     * Tests input system error handling.
     */
    @Test
    @DisplayName("Input System Error Handling")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testInputSystemErrorHandling() {
        // Test null event handling
        assertFalse(keyboardHandler.processKeyEvent(null), "Null keyboard event should return false");
        assertFalse(mouseHandler.processMouseEvent(null), "Null mouse event should return false");
        
        // Test input with invalid key codes
        KeyEvent invalidKey = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                          System.currentTimeMillis(), 0, -1, '?');
        assertFalse(keyboardHandler.processKeyEvent(invalidKey), "Invalid key should return false");
        
        // Test input with invalid mouse button
        MouseEvent invalidMouse = new MouseEvent(gameView, MouseEvent.MOUSE_PRESSED, 
                                               System.currentTimeMillis(), 0, 100, 100, 999, false);
        inputManager.setMouseAimingMode(true);
        assertFalse(mouseHandler.processMouseEvent(invalidMouse), "Invalid mouse button should return false");
    }
} 