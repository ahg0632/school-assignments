package view.input;

import java.awt.event.MouseEvent;
import java.awt.Point;
import javax.swing.SwingUtilities;
import model.characters.Player;
import enums.GameConstants;

/**
 * Handles mouse input events for the game.
 * Extracted from GamePanel.java to follow OOD principles.
 */
public class MouseInputHandler {
    
    private final InputManager inputManager;
    
    /**
     * Constructor for MouseInputHandler
     * @param inputManager The parent InputManager
     */
    public MouseInputHandler(InputManager inputManager) {
        this.inputManager = inputManager;
    }
    
    /**
     * Process mouse event
     * @param e MouseEvent to process
     * @return true if event was handled
     */
    public boolean processMouseEvent(MouseEvent e) {
        if (e == null) {
            return false; // Return false for null events
        }
        // Left-click attack in mouse aiming mode (continuous)
        if (inputManager.isMouseAimingMode() && (e.getButton() == MouseEvent.BUTTON1)) {
            Player player = inputManager.getPlayer();
            if (player != null) {
                if (e.getID() == MouseEvent.MOUSE_PRESSED) {
                    inputManager.setMouseAttackHeld(true);
                    if (canAttack()) {
                        player.getGameLogic().handle_player_attack_input();
                    }
                    return true; // Only handle press events
                } else if (e.getID() == MouseEvent.MOUSE_RELEASED) {
                    inputManager.setMouseAttackHeld(false);
                    return true; // Only handle release events
                }
            }
        }
        
        return false;
    }
    
    /**
     * Process mouse movement
     * @param e MouseEvent to process
     */
    public void processMouseMoved(MouseEvent e) {
        if (e == null) {
            return; // Return early for null events
        }
        // Mouse aiming mode: only mouse sets aim
        if (inputManager.isMouseAimingMode()) {
            Player player = inputManager.getPlayer();
            if (player != null) {
                Point mouse = e.getPoint();
                float px = player.getPixelX() + GameConstants.TILE_SIZE / 2f;
                float py = player.getPixelY() + GameConstants.TILE_SIZE / 2f;
                float dx = mouse.x - px;
                float dy = mouse.y - py;
                // Normalize to -1, 0, or 1 for keyboard compatibility
                int aimDX = (Math.abs(dx) > Math.abs(dy)) ? (dx > 0 ? 1 : -1) : 0;
                int aimDY = (Math.abs(dy) >= Math.abs(dx)) ? (dy > 0 ? 1 : -1) : 0;
                player.setAimDirection(aimDX, aimDY);
            }
        }
        
        // Handle stats panel hover (this would need to be implemented based on UI structure)
        handleStatsPanelHover(e);
    }
    
    /**
     * Handle stats panel hover detection
     * @param e MouseEvent to process
     */
    private void handleStatsPanelHover(MouseEvent e) {
        // This method would contain the stats panel hover logic
        // For now, it's a placeholder that can be implemented when needed
        // The original logic was:
        // - Calculate stats panel bounds
        // - Check if mouse is within bounds
        // - Update hover state
        // - Trigger repaint
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