package view.input;

import java.awt.event.KeyEvent;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for KeyBindingManager class.
 * Tests key binding management, default bindings, and key categorization.
 * Follows MVC architecture and simple solutions approach.
 */
@DisplayName("KeyBindingManager Tests")
class KeyBindingManagerTest {

    private KeyBindingManager keyBindingManager;

    @BeforeEach
    void setUp() {
        keyBindingManager = new KeyBindingManager();
    }

    /**
     * Tests default key bindings initialization.
     */
    @Test
    @DisplayName("Default Key Bindings Initialization")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testDefaultKeyBindingsInitialization() {
        // Test movement bindings
        assertEquals(KeyEvent.VK_W, keyBindingManager.getKeyCode(KeyBindingManager.KeyCategory.MOVEMENT, "MOVE_UP"));
        assertEquals(KeyEvent.VK_S, keyBindingManager.getKeyCode(KeyBindingManager.KeyCategory.MOVEMENT, "MOVE_DOWN"));
        assertEquals(KeyEvent.VK_A, keyBindingManager.getKeyCode(KeyBindingManager.KeyCategory.MOVEMENT, "MOVE_LEFT"));
        assertEquals(KeyEvent.VK_D, keyBindingManager.getKeyCode(KeyBindingManager.KeyCategory.MOVEMENT, "MOVE_RIGHT"));
        
        // Test aiming bindings
        assertEquals(KeyEvent.VK_UP, keyBindingManager.getKeyCode(KeyBindingManager.KeyCategory.AIMING, "AIM_UP"));
        assertEquals(KeyEvent.VK_DOWN, keyBindingManager.getKeyCode(KeyBindingManager.KeyCategory.AIMING, "AIM_DOWN"));
        assertEquals(KeyEvent.VK_LEFT, keyBindingManager.getKeyCode(KeyBindingManager.KeyCategory.AIMING, "AIM_LEFT"));
        assertEquals(KeyEvent.VK_RIGHT, keyBindingManager.getKeyCode(KeyBindingManager.KeyCategory.AIMING, "AIM_RIGHT"));
        
        // Test combat bindings
        assertEquals(KeyEvent.VK_SPACE, keyBindingManager.getKeyCode(KeyBindingManager.KeyCategory.COMBAT, "ATTACK"));
        
        // Test system bindings
        assertEquals(KeyEvent.VK_ESCAPE, keyBindingManager.getKeyCode(KeyBindingManager.KeyCategory.SYSTEM, "PAUSE"));
        
        // Test debug bindings
        assertEquals(KeyEvent.VK_1, keyBindingManager.getKeyCode(KeyBindingManager.KeyCategory.DEBUG, "DEBUG_1"));
        assertEquals(KeyEvent.VK_2, keyBindingManager.getKeyCode(KeyBindingManager.KeyCategory.DEBUG, "DEBUG_2"));
        assertEquals(KeyEvent.VK_3, keyBindingManager.getKeyCode(KeyBindingManager.KeyCategory.DEBUG, "DEBUG_3"));
        assertEquals(KeyEvent.VK_4, keyBindingManager.getKeyCode(KeyBindingManager.KeyCategory.DEBUG, "DEBUG_4"));
    }

    /**
     * Tests setting and getting custom key bindings.
     */
    @Test
    @DisplayName("Setting and Getting Custom Key Bindings")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testSettingAndGettingCustomKeyBindings() {
        // Test setting custom movement key
        keyBindingManager.setKeyCode(KeyBindingManager.KeyCategory.MOVEMENT, "MOVE_UP", KeyEvent.VK_I);
        assertEquals(KeyEvent.VK_I, keyBindingManager.getKeyCode(KeyBindingManager.KeyCategory.MOVEMENT, "MOVE_UP"));
        
        // Test setting custom aiming key
        keyBindingManager.setKeyCode(KeyBindingManager.KeyCategory.AIMING, "AIM_UP", KeyEvent.VK_NUMPAD8);
        assertEquals(KeyEvent.VK_NUMPAD8, keyBindingManager.getKeyCode(KeyBindingManager.KeyCategory.AIMING, "AIM_UP"));
        
        // Test setting custom attack key
        keyBindingManager.setKeyCode(KeyBindingManager.KeyCategory.COMBAT, "ATTACK", KeyEvent.VK_CONTROL);
        assertEquals(KeyEvent.VK_CONTROL, keyBindingManager.getKeyCode(KeyBindingManager.KeyCategory.COMBAT, "ATTACK"));
    }

    /**
     * Tests invalid key binding requests.
     */
    @Test
    @DisplayName("Invalid Key Binding Requests")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testInvalidKeyBindingRequests() {
        // Test invalid category
        assertEquals(-1, keyBindingManager.getKeyCode(KeyBindingManager.KeyCategory.MOVEMENT, "INVALID_ACTION"));
        
        // Test invalid action in valid category
        assertEquals(-1, keyBindingManager.getKeyCode(KeyBindingManager.KeyCategory.MOVEMENT, "NONEXISTENT"));
        
        // Test setting key for invalid action (should not throw exception)
        assertDoesNotThrow(() -> {
            keyBindingManager.setKeyCode(KeyBindingManager.KeyCategory.MOVEMENT, "INVALID_ACTION", KeyEvent.VK_X);
        }, "Setting key for invalid action should not throw exception");
    }

    /**
     * Tests key binding checks.
     */
    @Test
    @DisplayName("Key Binding Checks")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testKeyBindingChecks() {
        // Test movement key checks
        assertTrue(keyBindingManager.isMovementKey(KeyEvent.VK_W));
        assertTrue(keyBindingManager.isMovementKey(KeyEvent.VK_A));
        assertTrue(keyBindingManager.isMovementKey(KeyEvent.VK_S));
        assertTrue(keyBindingManager.isMovementKey(KeyEvent.VK_D));
        assertFalse(keyBindingManager.isMovementKey(KeyEvent.VK_X));
        
        // Test aiming key checks
        assertTrue(keyBindingManager.isAimingKey(KeyEvent.VK_UP));
        assertTrue(keyBindingManager.isAimingKey(KeyEvent.VK_DOWN));
        assertTrue(keyBindingManager.isAimingKey(KeyEvent.VK_LEFT));
        assertTrue(keyBindingManager.isAimingKey(KeyEvent.VK_RIGHT));
        assertFalse(keyBindingManager.isAimingKey(KeyEvent.VK_X));
        
        // Test attack key checks
        assertTrue(keyBindingManager.isAttackKey(KeyEvent.VK_SPACE));
        assertFalse(keyBindingManager.isAttackKey(KeyEvent.VK_X));
        
        // Test pause key checks
        assertTrue(keyBindingManager.isPauseKey(KeyEvent.VK_ESCAPE));
        assertFalse(keyBindingManager.isPauseKey(KeyEvent.VK_X));
        
        // Test debug key checks
        assertTrue(keyBindingManager.isDebugKey(KeyEvent.VK_1));
        assertTrue(keyBindingManager.isDebugKey(KeyEvent.VK_2));
        assertTrue(keyBindingManager.isDebugKey(KeyEvent.VK_3));
        assertTrue(keyBindingManager.isDebugKey(KeyEvent.VK_4));
        assertFalse(keyBindingManager.isDebugKey(KeyEvent.VK_5));
    }

    /**
     * Tests getting category bindings.
     */
    @Test
    @DisplayName("Getting Category Bindings")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testGettingCategoryBindings() {
        // Test movement category bindings
        Map<String, Integer> movementBindings = keyBindingManager.getCategoryBindings(KeyBindingManager.KeyCategory.MOVEMENT);
        assertNotNull(movementBindings);
        assertEquals(4, movementBindings.size());
        assertEquals(KeyEvent.VK_W, movementBindings.get("MOVE_UP"));
        assertEquals(KeyEvent.VK_S, movementBindings.get("MOVE_DOWN"));
        assertEquals(KeyEvent.VK_A, movementBindings.get("MOVE_LEFT"));
        assertEquals(KeyEvent.VK_D, movementBindings.get("MOVE_RIGHT"));
        
        // Test aiming category bindings
        Map<String, Integer> aimingBindings = keyBindingManager.getCategoryBindings(KeyBindingManager.KeyCategory.AIMING);
        assertNotNull(aimingBindings);
        assertEquals(4, aimingBindings.size());
        assertEquals(KeyEvent.VK_UP, aimingBindings.get("AIM_UP"));
        assertEquals(KeyEvent.VK_DOWN, aimingBindings.get("AIM_DOWN"));
        assertEquals(KeyEvent.VK_LEFT, aimingBindings.get("AIM_LEFT"));
        assertEquals(KeyEvent.VK_RIGHT, aimingBindings.get("AIM_RIGHT"));
        
        // Test combat category bindings
        Map<String, Integer> combatBindings = keyBindingManager.getCategoryBindings(KeyBindingManager.KeyCategory.COMBAT);
        assertNotNull(combatBindings);
        assertEquals(1, combatBindings.size());
        assertEquals(KeyEvent.VK_SPACE, combatBindings.get("ATTACK"));
        
        // Test system category bindings
        Map<String, Integer> systemBindings = keyBindingManager.getCategoryBindings(KeyBindingManager.KeyCategory.SYSTEM);
        assertNotNull(systemBindings);
        assertEquals(1, systemBindings.size());
        assertEquals(KeyEvent.VK_ESCAPE, systemBindings.get("PAUSE"));
        
        // Test debug category bindings
        Map<String, Integer> debugBindings = keyBindingManager.getCategoryBindings(KeyBindingManager.KeyCategory.DEBUG);
        assertNotNull(debugBindings);
        assertEquals(4, debugBindings.size());
        assertEquals(KeyEvent.VK_1, debugBindings.get("DEBUG_1"));
        assertEquals(KeyEvent.VK_2, debugBindings.get("DEBUG_2"));
        assertEquals(KeyEvent.VK_3, debugBindings.get("DEBUG_3"));
        assertEquals(KeyEvent.VK_4, debugBindings.get("DEBUG_4"));
    }

    /**
     * Tests resetting to default bindings.
     */
    @Test
    @DisplayName("Resetting to Default Bindings")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testResettingToDefaultBindings() {
        // Change some bindings
        keyBindingManager.setKeyCode(KeyBindingManager.KeyCategory.MOVEMENT, "MOVE_UP", KeyEvent.VK_I);
        keyBindingManager.setKeyCode(KeyBindingManager.KeyCategory.AIMING, "AIM_UP", KeyEvent.VK_NUMPAD8);
        keyBindingManager.setKeyCode(KeyBindingManager.KeyCategory.COMBAT, "ATTACK", KeyEvent.VK_CONTROL);
        
        // Verify changes
        assertEquals(KeyEvent.VK_I, keyBindingManager.getKeyCode(KeyBindingManager.KeyCategory.MOVEMENT, "MOVE_UP"));
        assertEquals(KeyEvent.VK_NUMPAD8, keyBindingManager.getKeyCode(KeyBindingManager.KeyCategory.AIMING, "AIM_UP"));
        assertEquals(KeyEvent.VK_CONTROL, keyBindingManager.getKeyCode(KeyBindingManager.KeyCategory.COMBAT, "ATTACK"));
        
        // Reset to defaults
        keyBindingManager.resetToDefaults();
        
        // Verify defaults are restored
        assertEquals(KeyEvent.VK_W, keyBindingManager.getKeyCode(KeyBindingManager.KeyCategory.MOVEMENT, "MOVE_UP"));
        assertEquals(KeyEvent.VK_UP, keyBindingManager.getKeyCode(KeyBindingManager.KeyCategory.AIMING, "AIM_UP"));
        assertEquals(KeyEvent.VK_SPACE, keyBindingManager.getKeyCode(KeyBindingManager.KeyCategory.COMBAT, "ATTACK"));
    }

    /**
     * Tests key name retrieval.
     */
    @Test
    @DisplayName("Key Name Retrieval")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testKeyNameRetrieval() {
        // Test key name retrieval for various keys
        assertEquals("W", KeyBindingManager.getKeyName(KeyEvent.VK_W));
        assertEquals("A", KeyBindingManager.getKeyName(KeyEvent.VK_A));
        assertEquals("S", KeyBindingManager.getKeyName(KeyEvent.VK_S));
        assertEquals("D", KeyBindingManager.getKeyName(KeyEvent.VK_D));
        assertEquals("Up", KeyBindingManager.getKeyName(KeyEvent.VK_UP));
        assertEquals("Down", KeyBindingManager.getKeyName(KeyEvent.VK_DOWN));
        assertEquals("Left", KeyBindingManager.getKeyName(KeyEvent.VK_LEFT));
        assertEquals("Right", KeyBindingManager.getKeyName(KeyEvent.VK_RIGHT));
        assertEquals("Space", KeyBindingManager.getKeyName(KeyEvent.VK_SPACE));
        assertEquals("Escape", KeyBindingManager.getKeyName(KeyEvent.VK_ESCAPE));
        assertEquals("1", KeyBindingManager.getKeyName(KeyEvent.VK_1));
        assertEquals("2", KeyBindingManager.getKeyName(KeyEvent.VK_2));
        assertEquals("3", KeyBindingManager.getKeyName(KeyEvent.VK_3));
        assertEquals("4", KeyBindingManager.getKeyName(KeyEvent.VK_4));
    }

    /**
     * Tests key binding conflicts.
     */
    @Test
    @DisplayName("Key Binding Conflicts")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testKeyBindingConflicts() {
        // Test that we can set the same key for different actions
        keyBindingManager.setKeyCode(KeyBindingManager.KeyCategory.MOVEMENT, "MOVE_UP", KeyEvent.VK_X);
        keyBindingManager.setKeyCode(KeyBindingManager.KeyCategory.AIMING, "AIM_UP", KeyEvent.VK_X);
        
        // Both should now be bound to X
        assertEquals(KeyEvent.VK_X, keyBindingManager.getKeyCode(KeyBindingManager.KeyCategory.MOVEMENT, "MOVE_UP"));
        assertEquals(KeyEvent.VK_X, keyBindingManager.getKeyCode(KeyBindingManager.KeyCategory.AIMING, "AIM_UP"));
        
        // Both should be detected as movement and aiming keys
        assertTrue(keyBindingManager.isMovementKey(KeyEvent.VK_X));
        assertTrue(keyBindingManager.isAimingKey(KeyEvent.VK_X));
    }

    /**
     * Tests invalid key codes.
     */
    @Test
    @DisplayName("Invalid Key Codes")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testInvalidKeyCodes() {
        // Test setting invalid key codes
        assertDoesNotThrow(() -> {
            keyBindingManager.setKeyCode(KeyBindingManager.KeyCategory.MOVEMENT, "MOVE_UP", -1);
        }, "Setting invalid key code should not throw exception");
        
        assertDoesNotThrow(() -> {
            keyBindingManager.setKeyCode(KeyBindingManager.KeyCategory.MOVEMENT, "MOVE_UP", 99999);
        }, "Setting invalid key code should not throw exception");
        
        // Test getting key name for invalid key code
        assertDoesNotThrow(() -> {
            String name = KeyBindingManager.getKeyName(-1);
            assertNotNull(name, "Key name should not be null for invalid key code");
        }, "Getting key name for invalid key code should not throw exception");
    }

    /**
     * Tests category enumeration values.
     */
    @Test
    @DisplayName("Category Enumeration Values")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testCategoryEnumerationValues() {
        // Test that all expected categories exist
        assertNotNull(KeyBindingManager.KeyCategory.MOVEMENT);
        assertNotNull(KeyBindingManager.KeyCategory.AIMING);
        assertNotNull(KeyBindingManager.KeyCategory.COMBAT);
        assertNotNull(KeyBindingManager.KeyCategory.SYSTEM);
        assertNotNull(KeyBindingManager.KeyCategory.DEBUG);
        
        // Test category names
        assertEquals("MOVEMENT", KeyBindingManager.KeyCategory.MOVEMENT.name());
        assertEquals("AIMING", KeyBindingManager.KeyCategory.AIMING.name());
        assertEquals("COMBAT", KeyBindingManager.KeyCategory.COMBAT.name());
        assertEquals("SYSTEM", KeyBindingManager.KeyCategory.SYSTEM.name());
        assertEquals("DEBUG", KeyBindingManager.KeyCategory.DEBUG.name());
    }

    /**
     * Tests that category bindings are independent.
     */
    @Test
    @DisplayName("Category Bindings Independence")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testCategoryBindingsIndependence() {
        // Test that changing bindings in one category doesn't affect others
        keyBindingManager.setKeyCode(KeyBindingManager.KeyCategory.MOVEMENT, "MOVE_UP", KeyEvent.VK_X);
        
        // Movement should be changed
        assertEquals(KeyEvent.VK_X, keyBindingManager.getKeyCode(KeyBindingManager.KeyCategory.MOVEMENT, "MOVE_UP"));
        
        // Aiming should remain unchanged
        assertEquals(KeyEvent.VK_UP, keyBindingManager.getKeyCode(KeyBindingManager.KeyCategory.AIMING, "AIM_UP"));
        
        // Combat should remain unchanged
        assertEquals(KeyEvent.VK_SPACE, keyBindingManager.getKeyCode(KeyBindingManager.KeyCategory.COMBAT, "ATTACK"));
    }
} 