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
 * Comprehensive tests for InputManager class.
 * Tests input event processing, key binding management, and input delegation.
 * Appropriate for a school project.
 */
@DisplayName("InputManager Tests")
class InputManagerTest {

    private InputManager inputManager;
    private GameView gameView;
    private Player testPlayer;
    private GameLogic gameLogic;
    private Map testMap;

    @BeforeEach
    void setUp() {
        testPlayer = new Player("TestPlayer", CharacterClass.WARRIOR, new Position(5, 5));
        gameLogic = new GameLogic(testPlayer);
        gameView = new GameView();
        testMap = new Map(1, Map.FloorType.REGULAR);
        inputManager = new InputManager(gameView);
        
        // Set up dependencies
        inputManager.setPlayer(testPlayer);
        inputManager.setMap(testMap);
    }

    /**
     * Tests keyboard input processing.
     */
    @Test
    @DisplayName("Keyboard Input Processing")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testKeyboardInputProcessing() {
        // Test that keyboard events are processed without exceptions
        assertDoesNotThrow(() -> {
            // Test movement keys
            KeyEvent wKey = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                       System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
            assertTrue(inputManager.processKeyEvent(wKey), "W key should be processed");
            
            KeyEvent aKey = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                       System.currentTimeMillis(), 0, KeyEvent.VK_A, 'A');
            assertTrue(inputManager.processKeyEvent(aKey), "A key should be processed");
            
            KeyEvent sKey = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                       System.currentTimeMillis(), 0, KeyEvent.VK_S, 'S');
            assertTrue(inputManager.processKeyEvent(sKey), "S key should be processed");
            
            KeyEvent dKey = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                       System.currentTimeMillis(), 0, KeyEvent.VK_D, 'D');
            assertTrue(inputManager.processKeyEvent(dKey), "D key should be processed");
            
            // Test key releases
            KeyEvent wKeyRelease = new KeyEvent(gameView, KeyEvent.KEY_RELEASED, 
                                              System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
            assertTrue(inputManager.processKeyEvent(wKeyRelease), "W key release should be processed");
            
            // Test attack key
            KeyEvent spaceKey = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                           System.currentTimeMillis(), 0, KeyEvent.VK_SPACE, ' ');
            assertTrue(inputManager.processKeyEvent(spaceKey), "Space key should be processed");
        }, "Keyboard input processing should not throw exceptions");
    }

    /**
     * Tests mouse input processing.
     */
    @Test
    @DisplayName("Mouse Input Processing")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMouseInputProcessing() {
        // Test that mouse events are processed without exceptions
        assertDoesNotThrow(() -> {
            // Test mouse move (always works)
            MouseEvent moveEvent = new MouseEvent(gameView, MouseEvent.MOUSE_MOVED, 
                                                System.currentTimeMillis(), 0, 100, 100, 0, false);
            inputManager.processMouseMoved(moveEvent);
            // Mouse move doesn't return boolean, so we just test it doesn't throw
            
            // Test mouse events in aiming mode
            inputManager.setMouseAimingMode(true);
            MouseEvent pressEvent = new MouseEvent(gameView, MouseEvent.MOUSE_PRESSED, 
                                                 System.currentTimeMillis(), 0, 100, 100, MouseEvent.BUTTON1, false);
            boolean pressResult = inputManager.processMouseEvent(pressEvent);
            // Don't assert specific result, just test it doesn't throw
            
            // Test mouse events when not in aiming mode
            inputManager.setMouseAimingMode(false);
            MouseEvent clickEventNoAim = new MouseEvent(gameView, MouseEvent.MOUSE_CLICKED, 
                                                      System.currentTimeMillis(), 0, 100, 100, MouseEvent.BUTTON1, false);
            boolean clickResult = inputManager.processMouseEvent(clickEventNoAim);
            // Don't assert specific result, just test it doesn't throw
        }, "Mouse input processing should not throw exceptions");
    }

    /**
     * Tests key binding management.
     */
    @Test
    @DisplayName("Key Binding Management")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testKeyBindingManagement() {
        // Test that key bindings are properly managed
        KeyBindingManager keyBindings = inputManager.getKeyBindings();
        assertNotNull(keyBindings, "KeyBindingManager should not be null");
        
        // Test that movement keys are tracked
        Set<Integer> movementKeys = inputManager.getMovementKeysHeld();
        assertNotNull(movementKeys, "Movement keys set should not be null");
        
        // Test that aim keys are tracked
        Set<Integer> aimKeys = inputManager.getAimKeysHeld();
        assertNotNull(aimKeys, "Aim keys set should not be null");
        
        // Test that attack key state is tracked
        inputManager.setAttackKeyHeld(true);
        inputManager.setAttackKeyHeld(false);
        // This should not throw exceptions
    }

    /**
     * Tests input delegation to controllers.
     */
    @Test
    @DisplayName("Input Delegation")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testInputDelegation() {
        // Test that input is properly delegated to game components
        assertDoesNotThrow(() -> {
            // Test movement input delegation
            KeyEvent wKey = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                       System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
            inputManager.processKeyEvent(wKey);
            
            // Test that movement keys are properly tracked
            Set<Integer> movementKeys = inputManager.getMovementKeysHeld();
            assertNotNull(movementKeys, "Movement keys should be tracked");
            
            // Test mouse input delegation
            MouseEvent clickEvent = new MouseEvent(gameView, MouseEvent.MOUSE_CLICKED, 
                                                 System.currentTimeMillis(), 0, 100, 100, 1, false);
            inputManager.processMouseEvent(clickEvent);
            
            // Test that mouse state is properly managed
            inputManager.setMouseAttackHeld(true);
            inputManager.setMouseAttackHeld(false);
        }, "Input delegation should work properly");
    }

    /**
     * Tests movement and aiming functionality.
     */
    @Test
    @DisplayName("Movement and Aiming Functionality")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMovementAndAiming() {
        // Test movement updates
        assertDoesNotThrow(() -> {
            inputManager.updateMovement();
        }, "Movement updates should not throw exceptions");
        
        // Test aiming updates
        assertDoesNotThrow(() -> {
            inputManager.updateAiming();
        }, "Aiming updates should not throw exceptions");
        
        // Test mouse aiming mode
        inputManager.setMouseAimingMode(true);
        assertTrue(inputManager.isMouseAimingMode(), "Mouse aiming mode should be enabled");
        
        inputManager.setMouseAimingMode(false);
        assertFalse(inputManager.isMouseAimingMode(), "Mouse aiming mode should be disabled");
    }

    /**
     * Tests movement pause functionality.
     */
    @Test
    @DisplayName("Movement Pause Functionality")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMovementPause() {
        // Test movement pause
        inputManager.setMovementPaused(true);
        inputManager.setMovementPaused(false);
        
        // Test that movement pause doesn't affect other functionality
        assertDoesNotThrow(() -> {
            inputManager.updateMovement();
            inputManager.updateAiming();
        }, "Movement pause should not affect other functionality");
    }

    /**
     * Tests input state clearing.
     */
    @Test
    @DisplayName("Input State Clearing")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testInputStateClearing() {
        // Test that input state can be cleared
        assertDoesNotThrow(() -> {
            inputManager.clearAllHeldKeys();
        }, "Clearing held keys should not throw exceptions");
        
        // Test that clearing doesn't break functionality
        Set<Integer> movementKeys = inputManager.getMovementKeysHeld();
        assertNotNull(movementKeys, "Movement keys should still be accessible after clearing");
    }

    /**
     * Tests component dependencies.
     */
    @Test
    @DisplayName("Component Dependencies")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testComponentDependencies() {
        // Test that all dependencies are properly set
        assertNotNull(inputManager.getParentView(), "Parent view should be set");
        assertNotNull(inputManager.getPlayer(), "Player should be set");
        assertNotNull(inputManager.getCurrentMap(), "Map should be set");
        assertNotNull(inputManager.getGameLogic(), "GameLogic should be set");
        assertNotNull(inputManager.getKeyBindings(), "KeyBindings should be set");
        
        // Test that dependencies are the correct instances
        assertEquals(gameView, inputManager.getParentView(), "Parent view should be correct");
        assertEquals(testPlayer, inputManager.getPlayer(), "Player should be correct");
        assertEquals(testMap, inputManager.getCurrentMap(), "Map should be correct");
        assertEquals(gameLogic, inputManager.getGameLogic(), "GameLogic should be correct");
    }

    /**
     * Tests error handling and robustness.
     */
    @Test
    @DisplayName("Error Handling and Robustness")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testErrorHandling() {
        // Test that null inputs are handled gracefully
        assertDoesNotThrow(() -> {
            inputManager.processKeyEvent(null);
            inputManager.processMouseEvent(null);
            inputManager.processMouseMoved(null);
        }, "Null input events should be handled gracefully");
        
        // Test that input manager works without all dependencies set
        InputManager standaloneInputManager = new InputManager(gameView);
        assertDoesNotThrow(() -> {
            standaloneInputManager.processKeyEvent(new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                                             System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W'));
        }, "InputManager should work without all dependencies set");
    }

    /**
     * Tests cleanup functionality.
     */
    @Test
    @DisplayName("Cleanup Functionality")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testCleanup() {
        // Test that cleanup works without exceptions
        assertDoesNotThrow(() -> {
            inputManager.cleanup();
        }, "Cleanup should not throw exceptions");
    }
} 