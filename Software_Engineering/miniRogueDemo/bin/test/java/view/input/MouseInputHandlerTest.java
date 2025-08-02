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

import java.awt.event.MouseEvent;
import java.awt.Point;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for MouseInputHandler class.
 * Tests mouse event processing, aiming, attack, and movement functionality.
 * Follows MVC architecture and simple solutions approach.
 */
@DisplayName("MouseInputHandler Tests")
class MouseInputHandlerTest {

    private MouseInputHandler mouseHandler;
    private InputManager inputManager;
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
        mouseHandler = new MouseInputHandler(inputManager);
    }

    /**
     * Tests mouse attack in aiming mode.
     */
    @Test
    @DisplayName("Mouse Attack in Aiming Mode")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMouseAttackInAimingMode() {
        // Enable mouse aiming mode
        inputManager.setMouseAimingMode(true);
        
        // Test left mouse button press
        MouseEvent pressEvent = new MouseEvent(gameView, MouseEvent.MOUSE_PRESSED, 
                                             System.currentTimeMillis(), 0, 100, 100, 1, false, 1);
        assertTrue(mouseHandler.processMouseEvent(pressEvent), "Mouse press should be handled in aiming mode");
        assertTrue(inputManager.getMouseAttackHeld(), "Mouse attack should be held");
        
        // Test left mouse button release
        MouseEvent releaseEvent = new MouseEvent(gameView, MouseEvent.MOUSE_RELEASED, 
                                               System.currentTimeMillis(), 0, 100, 100, 1, false, 1);
        assertTrue(mouseHandler.processMouseEvent(releaseEvent), "Mouse release should be handled in aiming mode");
        assertFalse(inputManager.getMouseAttackHeld(), "Mouse attack should not be held after release");
    }

    /**
     * Tests that mouse attack is ignored when not in aiming mode.
     */
    @Test
    @DisplayName("Mouse Attack Ignored When Not in Aiming Mode")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMouseAttackIgnoredWhenNotInAimingMode() {
        // Disable mouse aiming mode
        inputManager.setMouseAimingMode(false);
        
        // Test left mouse button press (should not be handled)
        MouseEvent pressEvent = new MouseEvent(gameView, MouseEvent.MOUSE_PRESSED, 
                                             System.currentTimeMillis(), 0, 100, 100, 1, false, 1);
        assertFalse(mouseHandler.processMouseEvent(pressEvent), "Mouse press should not be handled outside aiming mode");
        assertFalse(inputManager.getMouseAttackHeld(), "Mouse attack should not be held outside aiming mode");
    }

    /**
     * Tests mouse movement for aiming.
     */
    @Test
    @DisplayName("Mouse Movement for Aiming")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMouseMovementForAiming() {
        // Enable mouse aiming mode
        inputManager.setMouseAimingMode(true);
        
        // Test mouse movement
        MouseEvent moveEvent = new MouseEvent(gameView, MouseEvent.MOUSE_MOVED, 
                                            System.currentTimeMillis(), 0, 150, 100, 0, false);
        assertDoesNotThrow(() -> {
            mouseHandler.processMouseMoved(moveEvent);
        }, "Mouse movement should not throw exceptions");
        
        // Test mouse movement with different positions
        MouseEvent moveEvent2 = new MouseEvent(gameView, MouseEvent.MOUSE_MOVED, 
                                             System.currentTimeMillis(), 0, 100, 150, 0, false);
        assertDoesNotThrow(() -> {
            mouseHandler.processMouseMoved(moveEvent2);
        }, "Mouse movement should not throw exceptions");
    }

    /**
     * Tests mouse movement when not in aiming mode.
     */
    @Test
    @DisplayName("Mouse Movement When Not in Aiming Mode")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMouseMovementWhenNotInAimingMode() {
        // Disable mouse aiming mode
        inputManager.setMouseAimingMode(false);
        
        // Test mouse movement (should still work but not affect aiming)
        MouseEvent moveEvent = new MouseEvent(gameView, MouseEvent.MOUSE_MOVED, 
                                            System.currentTimeMillis(), 0, 100, 100, 0, false);
        assertDoesNotThrow(() -> {
            mouseHandler.processMouseMoved(moveEvent);
        }, "Mouse movement should not throw exceptions even when not in aiming mode");
    }

    /**
     * Tests right mouse button (should not be handled).
     */
    @Test
    @DisplayName("Right Mouse Button Handling")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRightMouseButtonHandling() {
        // Enable mouse aiming mode
        inputManager.setMouseAimingMode(true);
        
        // Test right mouse button (should not be handled)
        MouseEvent rightClick = new MouseEvent(gameView, MouseEvent.MOUSE_PRESSED, 
                                              System.currentTimeMillis(), 0, 100, 100, 1, false, 3);
        assertFalse(mouseHandler.processMouseEvent(rightClick), "Right mouse button should not be handled");
    }

    /**
     * Tests middle mouse button (should not be handled).
     */
    @Test
    @DisplayName("Middle Mouse Button Handling")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMiddleMouseButtonHandling() {
        // Enable mouse aiming mode
        inputManager.setMouseAimingMode(true);
        
        // Test middle mouse button (should not be handled)
        MouseEvent middleClick = new MouseEvent(gameView, MouseEvent.MOUSE_PRESSED, 
                                               System.currentTimeMillis(), 0, 100, 100, MouseEvent.BUTTON2, false);
        assertFalse(mouseHandler.processMouseEvent(middleClick), "Middle mouse button should not be handled");
    }

    /**
     * Tests mouse click events (should not be handled).
     */
    @Test
    @DisplayName("Mouse Click Event Handling")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMouseClickEventHandling() {
        // Enable mouse aiming mode
        inputManager.setMouseAimingMode(true);
        
        // Test mouse click (should not be handled)
        MouseEvent clickEvent = new MouseEvent(gameView, MouseEvent.MOUSE_CLICKED, 
                                              System.currentTimeMillis(), 0, 100, 100, 1, false, 1);
        assertFalse(mouseHandler.processMouseEvent(clickEvent), "Mouse click should not be handled");
    }

    /**
     * Tests mouse drag events (should not be handled).
     */
    @Test
    @DisplayName("Mouse Drag Event Handling")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMouseDragEventHandling() {
        // Enable mouse aiming mode
        inputManager.setMouseAimingMode(true);
        
        // Test mouse drag (should not be handled)
        MouseEvent dragEvent = new MouseEvent(gameView, MouseEvent.MOUSE_DRAGGED, 
                                            System.currentTimeMillis(), 0, 100, 100, 1, false, 1);
        assertFalse(mouseHandler.processMouseEvent(dragEvent), "Mouse drag should not be handled");
    }

    /**
     * Tests null event handling.
     */
    @Test
    @DisplayName("Null Event Handling")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testNullEventHandling() {
        // Test that null events are handled gracefully
        assertFalse(mouseHandler.processMouseEvent(null), "Null mouse event should return false");
        
        assertDoesNotThrow(() -> {
            mouseHandler.processMouseMoved(null);
        }, "Null mouse movement should not throw exceptions");
    }

    /**
     * Tests mouse input without player set.
     */
    @Test
    @DisplayName("Mouse Input Without Player")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMouseInputWithoutPlayer() {
        // Create input manager without player
        InputManager standaloneInputManager = new InputManager(gameView);
        MouseInputHandler standaloneHandler = new MouseInputHandler(standaloneInputManager);
        
        // Enable mouse aiming mode
        standaloneInputManager.setMouseAimingMode(true);
        
        // Test that mouse events are not handled without player (current implementation behavior)
        MouseEvent pressEvent = new MouseEvent(gameView, MouseEvent.MOUSE_PRESSED, 
                                             System.currentTimeMillis(), 0, 100, 100, 1, false, 1);
        assertFalse(standaloneHandler.processMouseEvent(pressEvent), "Mouse press should not be handled without player");
        
        MouseEvent moveEvent = new MouseEvent(gameView, MouseEvent.MOUSE_MOVED, 
                                            System.currentTimeMillis(), 0, 100, 100, 0, false);
        assertDoesNotThrow(() -> {
            standaloneHandler.processMouseMoved(moveEvent);
        }, "Mouse movement should not throw exceptions without player");
    }

    /**
     * Tests mouse wheel events (should not be handled).
     */
    @Test
    @DisplayName("Mouse Wheel Event Handling")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMouseWheelEventHandling() {
        // Enable mouse aiming mode
        inputManager.setMouseAimingMode(true);
        
        // Test mouse wheel (should not be handled)
        MouseEvent wheelEvent = new MouseEvent(gameView, MouseEvent.MOUSE_WHEEL, 
                                             System.currentTimeMillis(), 0, 100, 100, 0, false);
        assertFalse(mouseHandler.processMouseEvent(wheelEvent), "Mouse wheel should not be handled");
    }

    /**
     * Tests mouse enter/exit events (should not be handled).
     */
    @Test
    @DisplayName("Mouse Enter/Exit Event Handling")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMouseEnterExitEventHandling() {
        // Enable mouse aiming mode
        inputManager.setMouseAimingMode(true);
        
        // Test mouse enter (should not be handled)
        MouseEvent enterEvent = new MouseEvent(gameView, MouseEvent.MOUSE_ENTERED, 
                                             System.currentTimeMillis(), 0, 100, 100, 0, false);
        assertFalse(mouseHandler.processMouseEvent(enterEvent), "Mouse enter should not be handled");
        
        // Test mouse exit (should not be handled)
        MouseEvent exitEvent = new MouseEvent(gameView, MouseEvent.MOUSE_EXITED, 
                                            System.currentTimeMillis(), 0, 100, 100, 0, false);
        assertFalse(mouseHandler.processMouseEvent(exitEvent), "Mouse exit should not be handled");
    }

    /**
     * Tests multiple mouse button states.
     */
    @Test
    @DisplayName("Multiple Mouse Button States")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMultipleMouseButtonStates() {
        // Enable mouse aiming mode
        inputManager.setMouseAimingMode(true);
        
        // Test left button press
        MouseEvent leftPress = new MouseEvent(gameView, MouseEvent.MOUSE_PRESSED, 
                                             System.currentTimeMillis(), 0, 100, 100, 1, false, 1);
        assertTrue(mouseHandler.processMouseEvent(leftPress), "Left button press should be handled");
        assertTrue(inputManager.getMouseAttackHeld(), "Mouse attack should be held");
        
        // Test right button press (should not affect left button state)
        MouseEvent rightPress = new MouseEvent(gameView, MouseEvent.MOUSE_PRESSED, 
                                              System.currentTimeMillis(), 0, 100, 100, 1, false, 3);
        assertFalse(mouseHandler.processMouseEvent(rightPress), "Right button press should not be handled");
        assertTrue(inputManager.getMouseAttackHeld(), "Mouse attack should still be held after right button press");
        
        // Test left button release
        MouseEvent leftRelease = new MouseEvent(gameView, MouseEvent.MOUSE_RELEASED, 
                                               System.currentTimeMillis(), 0, 100, 100, 1, false, 1);
        assertTrue(mouseHandler.processMouseEvent(leftRelease), "Left button release should be handled");
        assertFalse(inputManager.getMouseAttackHeld(), "Mouse attack should not be held after release");
    }

    /**
     * Tests mouse movement with different coordinate systems.
     */
    @Test
    @DisplayName("Mouse Movement with Different Coordinates")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMouseMovementWithDifferentCoordinates() {
        // Enable mouse aiming mode
        inputManager.setMouseAimingMode(true);
        
        // Test mouse movement at origin
        MouseEvent originMove = new MouseEvent(gameView, MouseEvent.MOUSE_MOVED, 
                                              System.currentTimeMillis(), 0, 0, 0, 0, false);
        assertDoesNotThrow(() -> {
            mouseHandler.processMouseMoved(originMove);
        }, "Mouse movement at origin should not throw exceptions");
        
        // Test mouse movement at large coordinates
        MouseEvent largeMove = new MouseEvent(gameView, MouseEvent.MOUSE_MOVED, 
                                             System.currentTimeMillis(), 0, 1000, 1000, 0, false);
        assertDoesNotThrow(() -> {
            mouseHandler.processMouseMoved(largeMove);
        }, "Mouse movement at large coordinates should not throw exceptions");
        
        // Test mouse movement at negative coordinates
        MouseEvent negativeMove = new MouseEvent(gameView, MouseEvent.MOUSE_MOVED, 
                                                System.currentTimeMillis(), 0, -100, -100, 0, false);
        assertDoesNotThrow(() -> {
            mouseHandler.processMouseMoved(negativeMove);
        }, "Mouse movement at negative coordinates should not throw exceptions");
    }
} 