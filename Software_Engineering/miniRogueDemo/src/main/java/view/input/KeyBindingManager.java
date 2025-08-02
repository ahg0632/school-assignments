package view.input;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages configurable key bindings for the game.
 * Allows for customizable controls while maintaining default mappings.
 */
public class KeyBindingManager {
    
    // Default key bindings
    private static final int DEFAULT_MOVE_UP = KeyEvent.VK_W;
    private static final int DEFAULT_MOVE_DOWN = KeyEvent.VK_S;
    private static final int DEFAULT_MOVE_LEFT = KeyEvent.VK_A;
    private static final int DEFAULT_MOVE_RIGHT = KeyEvent.VK_D;
    private static final int DEFAULT_AIM_UP = KeyEvent.VK_UP;
    private static final int DEFAULT_AIM_DOWN = KeyEvent.VK_DOWN;
    private static final int DEFAULT_AIM_LEFT = KeyEvent.VK_LEFT;
    private static final int DEFAULT_AIM_RIGHT = KeyEvent.VK_RIGHT;
    private static final int DEFAULT_ATTACK = KeyEvent.VK_SPACE;
    private static final int DEFAULT_PAUSE = KeyEvent.VK_ESCAPE;
    private static final int DEFAULT_DEBUG_1 = KeyEvent.VK_1;
    private static final int DEFAULT_DEBUG_2 = KeyEvent.VK_2;
    private static final int DEFAULT_DEBUG_3 = KeyEvent.VK_3;
    private static final int DEFAULT_DEBUG_4 = KeyEvent.VK_4;
    
    // Key binding categories
    public enum KeyCategory {
        MOVEMENT,
        AIMING,
        COMBAT,
        SYSTEM,
        DEBUG
    }
    
    // Current key bindings
    private final Map<KeyCategory, Map<String, Integer>> keyBindings;
    
    /**
     * Constructor for KeyBindingManager
     */
    public KeyBindingManager() {
        this.keyBindings = new HashMap<>();
        initializeDefaultBindings();
    }
    
    /**
     * Initialize default key bindings
     */
    private void initializeDefaultBindings() {
        // Movement bindings
        Map<String, Integer> movementBindings = new HashMap<>();
        movementBindings.put("MOVE_UP", DEFAULT_MOVE_UP);
        movementBindings.put("MOVE_DOWN", DEFAULT_MOVE_DOWN);
        movementBindings.put("MOVE_LEFT", DEFAULT_MOVE_LEFT);
        movementBindings.put("MOVE_RIGHT", DEFAULT_MOVE_RIGHT);
        keyBindings.put(KeyCategory.MOVEMENT, movementBindings);
        
        // Aiming bindings
        Map<String, Integer> aimingBindings = new HashMap<>();
        aimingBindings.put("AIM_UP", DEFAULT_AIM_UP);
        aimingBindings.put("AIM_DOWN", DEFAULT_AIM_DOWN);
        aimingBindings.put("AIM_LEFT", DEFAULT_AIM_LEFT);
        aimingBindings.put("AIM_RIGHT", DEFAULT_AIM_RIGHT);
        keyBindings.put(KeyCategory.AIMING, aimingBindings);
        
        // Combat bindings
        Map<String, Integer> combatBindings = new HashMap<>();
        combatBindings.put("ATTACK", DEFAULT_ATTACK);
        keyBindings.put(KeyCategory.COMBAT, combatBindings);
        
        // System bindings
        Map<String, Integer> systemBindings = new HashMap<>();
        systemBindings.put("PAUSE", DEFAULT_PAUSE);
        keyBindings.put(KeyCategory.SYSTEM, systemBindings);
        
        // Debug bindings
        Map<String, Integer> debugBindings = new HashMap<>();
        debugBindings.put("DEBUG_1", DEFAULT_DEBUG_1);
        debugBindings.put("DEBUG_2", DEFAULT_DEBUG_2);
        debugBindings.put("DEBUG_3", DEFAULT_DEBUG_3);
        debugBindings.put("DEBUG_4", DEFAULT_DEBUG_4);
        keyBindings.put(KeyCategory.DEBUG, debugBindings);
    }
    
    /**
     * Get key code for a specific action
     * @param category Key category
     * @param action Action name
     * @return Key code for the action
     */
    public int getKeyCode(KeyCategory category, String action) {
        Map<String, Integer> categoryBindings = keyBindings.get(category);
        if (categoryBindings != null) {
            Integer keyCode = categoryBindings.get(action);
            if (keyCode != null) {
                return keyCode;
            }
        }
        return -1; // Invalid key code
    }
    
    /**
     * Set key code for a specific action
     * @param category Key category
     * @param action Action name
     * @param keyCode New key code
     */
    public void setKeyCode(KeyCategory category, String action, int keyCode) {
        Map<String, Integer> categoryBindings = keyBindings.get(category);
        if (categoryBindings != null) {
            categoryBindings.put(action, keyCode);
        }
    }
    
    /**
     * Check if a key code is bound to a specific action
     * @param category Key category
     * @param action Action name
     * @param keyCode Key code to check
     * @return true if the key code is bound to the action
     */
    public boolean isKeyBound(KeyCategory category, String action, int keyCode) {
        return getKeyCode(category, action) == keyCode;
    }
    
    /**
     * Get all bindings for a category
     * @param category Key category
     * @return Map of action names to key codes
     */
    public Map<String, Integer> getCategoryBindings(KeyCategory category) {
        Map<String, Integer> categoryBindings = keyBindings.get(category);
        if (categoryBindings != null) {
            return new HashMap<>(categoryBindings);
        }
        return new HashMap<>();
    }
    
    /**
     * Reset all bindings to default
     */
    public void resetToDefaults() {
        keyBindings.clear();
        initializeDefaultBindings();
    }
    
    /**
     * Get key name for a key code
     * @param keyCode Key code
     * @return Human-readable key name
     */
    public static String getKeyName(int keyCode) {
        return KeyEvent.getKeyText(keyCode);
    }
    
    /**
     * Check if a key code is a movement key
     * @param keyCode Key code to check
     * @return true if it's a movement key
     */
    public boolean isMovementKey(int keyCode) {
        Map<String, Integer> movementBindings = keyBindings.get(KeyCategory.MOVEMENT);
        return movementBindings != null && movementBindings.containsValue(keyCode);
    }
    
    /**
     * Check if a key code is an aiming key
     * @param keyCode Key code to check
     * @return true if it's an aiming key
     */
    public boolean isAimingKey(int keyCode) {
        Map<String, Integer> aimingBindings = keyBindings.get(KeyCategory.AIMING);
        return aimingBindings != null && aimingBindings.containsValue(keyCode);
    }
    
    /**
     * Check if a key code is an attack key
     * @param keyCode Key code to check
     * @return true if it's an attack key
     */
    public boolean isAttackKey(int keyCode) {
        return getKeyCode(KeyCategory.COMBAT, "ATTACK") == keyCode;
    }
    
    /**
     * Check if a key code is a pause key
     * @param keyCode Key code to check
     * @return true if it's a pause key
     */
    public boolean isPauseKey(int keyCode) {
        return getKeyCode(KeyCategory.SYSTEM, "PAUSE") == keyCode;
    }
    
    /**
     * Check if a key code is a debug key
     * @param keyCode Key code to check
     * @return true if it's a debug key
     */
    public boolean isDebugKey(int keyCode) {
        Map<String, Integer> debugBindings = keyBindings.get(KeyCategory.DEBUG);
        return debugBindings != null && debugBindings.containsValue(keyCode);
    }
} 