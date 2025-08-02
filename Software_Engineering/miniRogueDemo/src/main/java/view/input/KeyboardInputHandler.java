package view.input;

import java.awt.event.KeyEvent;
import model.characters.Player;

/**
 * Handles keyboard input events for the game.
 * Extracted from GamePanel.java to follow OOD principles.
 */
public class KeyboardInputHandler {
    
    private final InputManager inputManager;
    
    /**
     * Constructor for KeyboardInputHandler
     * @param inputManager The parent InputManager
     */
    public KeyboardInputHandler(InputManager inputManager) {
        this.inputManager = inputManager;
    }
    
    /**
     * Process keyboard event
     * @param e KeyEvent to process
     * @return true if event was handled
     */
    public boolean processKeyEvent(KeyEvent e) {
        if (e == null) {
            return false; // Return false for null events
        }
        boolean handled = false;
        Player player = inputManager.getPlayer();
        
        // --- ESCAPE (PAUSE/RESUME) ---
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE && e.getID() == KeyEvent.KEY_PRESSED) {
            // This would need to be handled by the parent view/controller
            // For now, just mark as handled
            handled = true;
        }
        
        // Debug mode class switching
        if (player != null && e.getID() == KeyEvent.KEY_PRESSED) {
            if (e.getKeyCode() == KeyEvent.VK_1) {
                player.debug_switch_class(1);
                handled = true;
            } else if (e.getKeyCode() == KeyEvent.VK_2) {
                player.debug_switch_class(2);
                handled = true;
            } else if (e.getKeyCode() == KeyEvent.VK_3) {
                player.debug_switch_class(3);
                handled = true;
            } else if (e.getKeyCode() == KeyEvent.VK_4) {
                player.debug_switch_class(4);
                handled = true;
            }
        }
        
        if (player != null && !handled) {
            // --- MOVEMENT (WASD) ---
            boolean movementChanged = false;
            if (e.getKeyCode() == KeyEvent.VK_W ||
                e.getKeyCode() == KeyEvent.VK_A ||
                e.getKeyCode() == KeyEvent.VK_S ||
                e.getKeyCode() == KeyEvent.VK_D) {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    if (inputManager.getMovementKeysHeld().add(e.getKeyCode())) movementChanged = true;
                } else if (e.getID() == KeyEvent.KEY_RELEASED) {
                    if (inputManager.getMovementKeysHeld().remove(e.getKeyCode())) movementChanged = true;
                }
                if (movementChanged) {
                    inputManager.updateMovement();
                }
                handled = true;
            }
            
            // --- AIMING (ARROWS) ---
            if (!inputManager.isMouseAimingMode() &&
                (e.getKeyCode() == KeyEvent.VK_UP ||
                 e.getKeyCode() == KeyEvent.VK_DOWN ||
                 e.getKeyCode() == KeyEvent.VK_LEFT ||
                 e.getKeyCode() == KeyEvent.VK_RIGHT)) {
                boolean aimChanged = false;
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    if (inputManager.getAimKeysHeld().add(e.getKeyCode())) aimChanged = true;
                } else if (e.getID() == KeyEvent.KEY_RELEASED) {
                    if (inputManager.getAimKeysHeld().remove(e.getKeyCode())) aimChanged = true;
                }
                if (aimChanged) {
                    inputManager.updateAiming();
                }
                handled = true;
            }
        }
        
        // --- ATTACK (SPACE) ---
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                inputManager.setAttackKeyHeld(true);
                if (canAttack()) {
                    player.getGameLogic().handle_player_attack_input();
                }
            } else if (e.getID() == KeyEvent.KEY_RELEASED) {
                inputManager.setAttackKeyHeld(false);
            }
            handled = true;
        }
        
        return handled;
    }
    
    /**
     * Check if player can attack based on cooldown
     * @return true if attack is available
     */
    private boolean canAttack() {
        Player player = inputManager.getPlayer();
        if (player == null) return false;
        
        // This is a simplified check - the actual implementation would need
        // to track the last attack time properly
        return true;
    }
} 