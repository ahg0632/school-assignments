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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.awt.event.KeyEvent;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive tests for KeyboardInputHandler class.
 * Tests keyboard event processing, movement, aiming, and attack functionality.
 * Follows MVC architecture and simple solutions approach.
 */
@DisplayName("KeyboardInputHandler Tests")
class KeyboardInputHandlerTest {

    private KeyboardInputHandler keyboardHandler;
    private InputManager inputManager;
    private GameView gameView;
    private Player testPlayer;
    private GameLogic gameLogic;
    private Map testMap;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
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
    }

    /**
     * Tests movement key processing (WASD keys).
     */
    @Test
    @DisplayName("Movement Key Processing")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMovementKeyProcessing() {
        // Test W key (up)
        KeyEvent wKeyPress = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                         System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
        assertTrue(keyboardHandler.processKeyEvent(wKeyPress), "W key press should be handled");
        
        Set<Integer> movementKeys = inputManager.getMovementKeysHeld();
        assertTrue(movementKeys.contains(KeyEvent.VK_W), "W key should be in held keys");
        
        // Test W key release
        KeyEvent wKeyRelease = new KeyEvent(gameView, KeyEvent.KEY_RELEASED, 
                                           System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
        assertTrue(keyboardHandler.processKeyEvent(wKeyRelease), "W key release should be handled");
        assertFalse(movementKeys.contains(KeyEvent.VK_W), "W key should be removed from held keys");
        
        // Test A key (left)
        KeyEvent aKeyPress = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                         System.currentTimeMillis(), 0, KeyEvent.VK_A, 'A');
        assertTrue(keyboardHandler.processKeyEvent(aKeyPress), "A key press should be handled");
        assertTrue(movementKeys.contains(KeyEvent.VK_A), "A key should be in held keys");
        
        // Test S key (down)
        KeyEvent sKeyPress = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                         System.currentTimeMillis(), 0, KeyEvent.VK_S, 'S');
        assertTrue(keyboardHandler.processKeyEvent(sKeyPress), "S key press should be handled");
        assertTrue(movementKeys.contains(KeyEvent.VK_S), "S key should be in held keys");
        
        // Test D key (right)
        KeyEvent dKeyPress = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                         System.currentTimeMillis(), 0, KeyEvent.VK_D, 'D');
        assertTrue(keyboardHandler.processKeyEvent(dKeyPress), "D key press should be handled");
        assertTrue(movementKeys.contains(KeyEvent.VK_D), "D key should be in held keys");
    }

    /**
     * Tests aiming key processing (arrow keys).
     */
    @Test
    @DisplayName("Aiming Key Processing")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testAimingKeyProcessing() {
        // Ensure mouse aiming mode is disabled for keyboard aiming
        inputManager.setMouseAimingMode(false);
        
        // Test UP arrow key
        KeyEvent upKeyPress = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                          System.currentTimeMillis(), 0, KeyEvent.VK_UP, '↑');
        assertTrue(keyboardHandler.processKeyEvent(upKeyPress), "UP key press should be handled");
        
        Set<Integer> aimKeys = inputManager.getAimKeysHeld();
        assertTrue(aimKeys.contains(KeyEvent.VK_UP), "UP key should be in held aim keys");
        
        // Test UP arrow key release
        KeyEvent upKeyRelease = new KeyEvent(gameView, KeyEvent.KEY_RELEASED, 
                                            System.currentTimeMillis(), 0, KeyEvent.VK_UP, '↑');
        assertTrue(keyboardHandler.processKeyEvent(upKeyRelease), "UP key release should be handled");
        assertFalse(aimKeys.contains(KeyEvent.VK_UP), "UP key should be removed from held aim keys");
        
        // Test DOWN arrow key
        KeyEvent downKeyPress = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                            System.currentTimeMillis(), 0, KeyEvent.VK_DOWN, '↓');
        assertTrue(keyboardHandler.processKeyEvent(downKeyPress), "DOWN key press should be handled");
        assertTrue(aimKeys.contains(KeyEvent.VK_DOWN), "DOWN key should be in held aim keys");
        
        // Test LEFT arrow key
        KeyEvent leftKeyPress = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                            System.currentTimeMillis(), 0, KeyEvent.VK_LEFT, '←');
        assertTrue(keyboardHandler.processKeyEvent(leftKeyPress), "LEFT key press should be handled");
        assertTrue(aimKeys.contains(KeyEvent.VK_LEFT), "LEFT key should be in held aim keys");
        
        // Test RIGHT arrow key
        KeyEvent rightKeyPress = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                             System.currentTimeMillis(), 0, KeyEvent.VK_RIGHT, '→');
        assertTrue(keyboardHandler.processKeyEvent(rightKeyPress), "RIGHT key press should be handled");
        assertTrue(aimKeys.contains(KeyEvent.VK_RIGHT), "RIGHT key should be in held aim keys");
    }

    /**
     * Tests that aiming keys are ignored when mouse aiming mode is enabled.
     */
    @Test
    @DisplayName("Aiming Keys Ignored in Mouse Mode")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testAimingKeysIgnoredInMouseMode() {
        // Enable mouse aiming mode
        inputManager.setMouseAimingMode(true);
        
        // Test that arrow keys are not processed when mouse aiming is enabled
        KeyEvent upKeyPress = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                          System.currentTimeMillis(), 0, KeyEvent.VK_UP, '↑');
        assertFalse(keyboardHandler.processKeyEvent(upKeyPress), "UP key should not be handled in mouse mode");
        
        Set<Integer> aimKeys = inputManager.getAimKeysHeld();
        assertFalse(aimKeys.contains(KeyEvent.VK_UP), "UP key should not be in held aim keys in mouse mode");
    }

    /**
     * Tests attack key processing (spacebar).
     */
    @Test
    @DisplayName("Attack Key Processing")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testAttackKeyProcessing() {
        // Test spacebar press
        KeyEvent spaceKeyPress = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                             System.currentTimeMillis(), 0, KeyEvent.VK_SPACE, ' ');
        assertTrue(keyboardHandler.processKeyEvent(spaceKeyPress), "Space key press should be handled");
        assertTrue(inputManager.getAttackKeyHeld(), "Attack key should be held");
        
        // Test spacebar release
        KeyEvent spaceKeyRelease = new KeyEvent(gameView, KeyEvent.KEY_RELEASED, 
                                               System.currentTimeMillis(), 0, KeyEvent.VK_SPACE, ' ');
        assertTrue(keyboardHandler.processKeyEvent(spaceKeyRelease), "Space key release should be handled");
        assertFalse(inputManager.getAttackKeyHeld(), "Attack key should not be held after release");
    }

    /**
     * Tests debug key processing (number keys 1-4).
     */
    @Test
    @DisplayName("Debug Key Processing")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testDebugKeyProcessing() {
        // Test debug key 1
        KeyEvent debug1Key = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                         System.currentTimeMillis(), 0, KeyEvent.VK_1, '1');
        assertTrue(keyboardHandler.processKeyEvent(debug1Key), "Debug key 1 should be handled");
        
        // Test debug key 2
        KeyEvent debug2Key = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                         System.currentTimeMillis(), 0, KeyEvent.VK_2, '2');
        assertTrue(keyboardHandler.processKeyEvent(debug2Key), "Debug key 2 should be handled");
        
        // Test debug key 3
        KeyEvent debug3Key = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                         System.currentTimeMillis(), 0, KeyEvent.VK_3, '3');
        assertTrue(keyboardHandler.processKeyEvent(debug3Key), "Debug key 3 should be handled");
        
        // Test debug key 4
        KeyEvent debug4Key = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                         System.currentTimeMillis(), 0, KeyEvent.VK_4, '4');
        assertTrue(keyboardHandler.processKeyEvent(debug4Key), "Debug key 4 should be handled");
    }

    /**
     * Tests escape key processing.
     */
    @Test
    @DisplayName("Escape Key Processing")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testEscapeKeyProcessing() {
        // Test escape key
        KeyEvent escapeKey = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                         System.currentTimeMillis(), 0, KeyEvent.VK_ESCAPE, 'E');
        assertTrue(keyboardHandler.processKeyEvent(escapeKey), "Escape key should be handled");
    }

    /**
     * Tests that non-game keys are not handled.
     */
    @Test
    @DisplayName("Non-Game Key Handling")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testNonGameKeyHandling() {
        // Test that non-game keys return false
        KeyEvent tabKey = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                      System.currentTimeMillis(), 0, KeyEvent.VK_TAB, '\t');
        assertFalse(keyboardHandler.processKeyEvent(tabKey), "Tab key should not be handled");
        
        KeyEvent enterKey = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                        System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, '\n');
        assertFalse(keyboardHandler.processKeyEvent(enterKey), "Enter key should not be handled");
        
        KeyEvent shiftKey = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                        System.currentTimeMillis(), 0, KeyEvent.VK_SHIFT, 'S');
        assertFalse(keyboardHandler.processKeyEvent(shiftKey), "Shift key should not be handled");
    }

    /**
     * Tests null event handling.
     */
    @Test
    @DisplayName("Null Event Handling")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testNullEventHandling() {
        // Test that null events are handled gracefully
        assertFalse(keyboardHandler.processKeyEvent(null), "Null event should return false");
    }

    /**
     * Tests that input works without player set.
     */
    @Test
    @DisplayName("Input Without Player")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testInputWithoutPlayer() {
        // Create input manager without player
        InputManager standaloneInputManager = new InputManager(gameView);
        KeyboardInputHandler standaloneHandler = new KeyboardInputHandler(standaloneInputManager);
        
        // Test that non-movement keys still work (escape key should work without player)
        KeyEvent escapeKey = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                         System.currentTimeMillis(), 0, KeyEvent.VK_ESCAPE, 'E');
        assertTrue(standaloneHandler.processKeyEvent(escapeKey), "Escape key should be handled without player");
        
        // Test that movement keys are not processed without player (current implementation behavior)
        KeyEvent wKey = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                    System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
        assertFalse(standaloneHandler.processKeyEvent(wKey), "W key should not be handled without player");
        
        Set<Integer> movementKeys = standaloneInputManager.getMovementKeysHeld();
        assertFalse(movementKeys.contains(KeyEvent.VK_W), "W key should not be tracked without player");
    }

    /**
     * Tests multiple key combinations.
     */
    @Test
    @DisplayName("Multiple Key Combinations")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMultipleKeyCombinations() {
        // Press multiple movement keys
        KeyEvent wKey = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                    System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
        KeyEvent dKey = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                    System.currentTimeMillis(), 0, KeyEvent.VK_D, 'D');
        
        keyboardHandler.processKeyEvent(wKey);
        keyboardHandler.processKeyEvent(dKey);
        
        Set<Integer> movementKeys = inputManager.getMovementKeysHeld();
        assertTrue(movementKeys.contains(KeyEvent.VK_W), "W key should be held");
        assertTrue(movementKeys.contains(KeyEvent.VK_D), "D key should be held");
        
        // Release one key
        KeyEvent wKeyRelease = new KeyEvent(gameView, KeyEvent.KEY_RELEASED, 
                                           System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
        keyboardHandler.processKeyEvent(wKeyRelease);
        
        assertFalse(movementKeys.contains(KeyEvent.VK_W), "W key should be released");
        assertTrue(movementKeys.contains(KeyEvent.VK_D), "D key should still be held");
    }

    /**
     * Tests that movement updates are called when keys change.
     */
    @Test
    @DisplayName("Movement Updates on Key Changes")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMovementUpdatesOnKeyChanges() {
        // Test that movement is updated when keys are pressed
        KeyEvent wKey = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                    System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
        keyboardHandler.processKeyEvent(wKey);
        
        // The movement should be updated (we can't easily test the internal call,
        // but we can verify the key is tracked)
        Set<Integer> movementKeys = inputManager.getMovementKeysHeld();
        assertTrue(movementKeys.contains(KeyEvent.VK_W), "W key should be tracked after press");
        
        // Test that movement is updated when keys are released
        KeyEvent wKeyRelease = new KeyEvent(gameView, KeyEvent.KEY_RELEASED, 
                                           System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
        keyboardHandler.processKeyEvent(wKeyRelease);
        
        assertFalse(movementKeys.contains(KeyEvent.VK_W), "W key should be removed after release");
    }

    /**
     * Tests that aiming updates are called when keys change.
     */
    @Test
    @DisplayName("Aiming Updates on Key Changes")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testAimingUpdatesOnKeyChanges() {
        // Ensure mouse aiming is disabled
        inputManager.setMouseAimingMode(false);
        
        // Test that aiming is updated when keys are pressed
        KeyEvent upKey = new KeyEvent(gameView, KeyEvent.KEY_PRESSED, 
                                     System.currentTimeMillis(), 0, KeyEvent.VK_UP, '↑');
        keyboardHandler.processKeyEvent(upKey);
        
        Set<Integer> aimKeys = inputManager.getAimKeysHeld();
        assertTrue(aimKeys.contains(KeyEvent.VK_UP), "UP key should be tracked after press");
        
        // Test that aiming is updated when keys are released
        KeyEvent upKeyRelease = new KeyEvent(gameView, KeyEvent.KEY_RELEASED, 
                                            System.currentTimeMillis(), 0, KeyEvent.VK_UP, '↑');
        keyboardHandler.processKeyEvent(upKeyRelease);
        
        assertFalse(aimKeys.contains(KeyEvent.VK_UP), "UP key should be removed after release");
    }
} 