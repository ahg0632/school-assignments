package view.input;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Point;
import javax.swing.SwingUtilities;
import model.characters.Player;
import model.map.Map;
import model.gameLogic.GameLogic;
import view.GameView;
import enums.GameConstants;

/**
 * Centralized input management system for the game.
 * Handles keyboard and mouse input, movement, aiming, and attack controls.
 * Follows OOD principles and MVC architecture.
 */
public class InputManager {
    
    // Core dependencies
    private GameView parentView;
    private Player player;
    private Map currentMap;
    private GameLogic gameLogic;
    
    // Input state tracking
    private final Set<Integer> movementKeysHeld = new HashSet<>();
    private final Set<Integer> aimKeysHeld = new HashSet<>();
    private boolean attackKeyHeld = false;
    private boolean mouseAttackHeld = false;
    private boolean mouseAimingMode = false;
    private boolean movementPaused = false;
    
    // Mouse aiming state
    private int lastAimDX = 0, lastAimDY = 1; // Default to down
    
    // Timers
    private Timer attackHoldTimer;
    
    // Input handlers
    private KeyboardInputHandler keyboardHandler;
    private MouseInputHandler mouseHandler;
    private KeyBindingManager keyBindings;
    
    /**
     * Constructor for InputManager
     * @param parentView The parent GameView
     */
    public InputManager(GameView parentView) {
        this.parentView = parentView;
        this.keyboardHandler = new KeyboardInputHandler(this);
        this.mouseHandler = new MouseInputHandler(this);
        this.keyBindings = new KeyBindingManager();
        
        initializeTimers();
    }
    
    /**
     * Initialize input timers
     */
    private void initializeTimers() {
        // Timer for continuous attack
        attackHoldTimer = new Timer(10, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleContinuousAttack();
            }
        });
        attackHoldTimer.start();
    }
    
    /**
     * Handle continuous attack logic
     */
    private void handleContinuousAttack() {
        if (mouseAimingMode && player != null) {
            // Always poll the real mouse position for aiming
            Point mouse = java.awt.MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen(mouse, parentView.get_game_panel());
            float px = player.getPixelX() + GameConstants.TILE_SIZE / 2f;
            float py = player.getPixelY() + GameConstants.TILE_SIZE / 2f;
            float dx = mouse.x - px;
            float dy = mouse.y - py;
            int aimDX = (Math.abs(dx) > Math.abs(dy)) ? (dx > 0 ? 1 : -1) : 0;
            int aimDY = (Math.abs(dy) >= Math.abs(dx)) ? (dy > 0 ? 1 : -1) : 0;
            player.setAimDirection(aimDX, aimDY);
        }
        
        if ((attackKeyHeld || mouseAttackHeld) && canAttack()) {
            // When attacking, poll the real mouse position for attack direction
            if (mouseAimingMode && player != null) {
                Point mouse = java.awt.MouseInfo.getPointerInfo().getLocation();
                SwingUtilities.convertPointFromScreen(mouse, parentView.get_game_panel());
                float px = player.getPixelX() + GameConstants.TILE_SIZE / 2f;
                float py = player.getPixelY() + GameConstants.TILE_SIZE / 2f;
                float dx = mouse.x - px;
                float dy = mouse.y - py;
                int aimDX = (Math.abs(dx) > Math.abs(dy)) ? (dx > 0 ? 1 : -1) : 0;
                int aimDY = (Math.abs(dy) >= Math.abs(dx)) ? (dy > 0 ? 1 : -1) : 0;
                player.setAimDirection(aimDX, aimDY);
            }
            // Directly call player.attack() or attack logic here
            if (player != null && player.getGameLogic() != null) {
                player.getGameLogic().handle_player_attack_input();
            }
        }
    }
    
    /**
     * Check if player can attack based on cooldown
     * @return true if attack is available
     */
    private boolean canAttack() {
        if (player == null) return false;
        
        long currentTime = System.currentTimeMillis();
        long lastAttackTime = getLastAttackTime();
        float attackSpeed = getAttackSpeed();
        long cooldown = (long) (1000.0f / attackSpeed);
        
        return (currentTime - lastAttackTime) >= cooldown;
    }
    
    /**
     * Get the last attack time
     * @return last attack timestamp
     */
    private long getLastAttackTime() {
        // This would need to be implemented based on how attack timing is tracked
        // For now, returning a default value
        return 0;
    }
    
    /**
     * Get current attack speed
     * @return attack speed value
     */
    private float getAttackSpeed() {
        if (player == null) return 1.0f;
        return player.getPlayerClassOOP().getAttackSpeed();
    }
    
    /**
     * Process keyboard event
     * @param e KeyEvent to process
     * @return true if event was handled
     */
    public boolean processKeyEvent(KeyEvent e) {
        return keyboardHandler.processKeyEvent(e);
    }
    
    /**
     * Process mouse event
     * @param e MouseEvent to process
     * @return true if event was handled
     */
    public boolean processMouseEvent(MouseEvent e) {
        return mouseHandler.processMouseEvent(e);
    }
    
    /**
     * Process mouse movement
     * @param e MouseEvent to process
     */
    public void processMouseMoved(MouseEvent e) {
        mouseHandler.processMouseMoved(e);
    }
    
    /**
     * Clear all held keys and reset input state
     */
    public void clearAllHeldKeys() {
        // MOVEMENT: Clear both key state AND player direction (fixes stuck movement bug)
        movementKeysHeld.clear();
        if (player != null) {
            player.setMoveDirection(0, 0);  // Stop movement immediately
        }
        
        // AIM: Clear key state but PRESERVE aim direction (keeps aiming direction intact)
        aimKeysHeld.clear();
        // NOTE: We DON'T call player.setAimDirection() here - preserves where player was aiming
        
        // ATTACK: Reset attack key states for consistency
        attackKeyHeld = false;
        mouseAttackHeld = false;
    }
    
    /**
     * Set movement pause state
     * @param paused true to pause movement, false to resume
     */
    public void setMovementPaused(boolean paused) {
        this.movementPaused = paused;
    }
    
    /**
     * Get movement pause state
     * @return true if movement is paused
     */
    public boolean getMovementPaused() {
        return this.movementPaused;
    }
    
    /**
     * Set mouse aiming mode
     * @param enabled true to enable mouse aiming
     */
    public void setMouseAimingMode(boolean enabled) {
        this.mouseAimingMode = enabled;
    }
    
    /**
     * Get mouse aiming mode state
     * @return true if mouse aiming is enabled
     */
    public boolean isMouseAimingMode() {
        return mouseAimingMode;
    }
    
    /**
     * Update player movement based on held keys
     */
    public void updateMovement() {
        if (player == null || movementPaused) return;
        
        int dx = 0, dy = 0;
        if (movementKeysHeld.contains(KeyEvent.VK_W)) dy -= 1;
        if (movementKeysHeld.contains(KeyEvent.VK_S)) dy += 1;
        if (movementKeysHeld.contains(KeyEvent.VK_A)) dx -= 1;
        if (movementKeysHeld.contains(KeyEvent.VK_D)) dx += 1;
        player.setMoveDirection(dx, dy);
    }
    
    /**
     * Update player aim direction based on held keys
     */
    public void updateAiming() {
        if (player == null || mouseAimingMode) return;
        
        int adx = 0, ady = 0;
        if (aimKeysHeld.contains(KeyEvent.VK_UP)) ady -= 1;
        if (aimKeysHeld.contains(KeyEvent.VK_DOWN)) ady += 1;
        if (aimKeysHeld.contains(KeyEvent.VK_LEFT)) adx -= 1;
        if (aimKeysHeld.contains(KeyEvent.VK_RIGHT)) adx += 1;
        
        // If no arrow keys held, do not update aim (keep last direction)
        if (adx != 0 || ady != 0) {
            player.setAimDirection(adx, ady);
        }
    }
    
    /**
     * Set player reference
     * @param player Player instance
     */
    public void setPlayer(Player player) {
        this.player = player;
        if (player != null) {
            this.gameLogic = player.getGameLogic();
        }
    }
    
    /**
     * Set map reference
     * @param map Map instance
     */
    public void setMap(Map map) {
        this.currentMap = map;
    }
    
    /**
     * Get movement keys held set
     * @return Set of held movement key codes
     */
    public Set<Integer> getMovementKeysHeld() {
        return movementKeysHeld;
    }
    
    /**
     * Get aim keys held set
     * @return Set of held aim key codes
     */
    public Set<Integer> getAimKeysHeld() {
        return aimKeysHeld;
    }
    
    /**
     * Set attack key held state
     * @param held true if attack key is held
     */
    public void setAttackKeyHeld(boolean held) {
        this.attackKeyHeld = held;
    }
    
    /**
     * Get attack key held state
     * @return true if attack key is held
     */
    public boolean getAttackKeyHeld() {
        return this.attackKeyHeld;
    }
    
    /**
     * Set mouse attack held state
     * @param held true if mouse attack is held
     */
    public void setMouseAttackHeld(boolean held) {
        this.mouseAttackHeld = held;
    }
    
    /**
     * Get mouse attack held state
     * @return true if mouse attack is held
     */
    public boolean getMouseAttackHeld() {
        return this.mouseAttackHeld;
    }
    
    /**
     * Get parent view
     * @return GameView instance
     */
    public GameView getParentView() {
        return parentView;
    }
    
    /**
     * Get player instance
     * @return Player instance
     */
    public Player getPlayer() {
        return player;
    }
    
    /**
     * Get current map
     * @return Map instance
     */
    public Map getCurrentMap() {
        return currentMap;
    }
    
    /**
     * Get game logic
     * @return GameLogic instance
     */
    public GameLogic getGameLogic() {
        return gameLogic;
    }
    
    /**
     * Get key bindings manager
     * @return KeyBindingManager instance
     */
    public KeyBindingManager getKeyBindings() {
        return keyBindings;
    }
    
    /**
     * Cleanup resources
     */
    public void cleanup() {
        if (attackHoldTimer != null) {
            attackHoldTimer.stop();
        }
    }
} 