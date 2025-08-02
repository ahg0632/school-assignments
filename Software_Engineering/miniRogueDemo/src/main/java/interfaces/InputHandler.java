package interfaces;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Interface for input handling components.
 * This establishes a contract for all input processing.
 */
public interface InputHandler {
    
    /**
     * Processes a key press event.
     * 
     * @param e The key event to process
     */
    void handleKeyPressed(KeyEvent e);
    
    /**
     * Processes a key release event.
     * 
     * @param e The key event to process
     */
    void handleKeyReleased(KeyEvent e);
    
    /**
     * Processes a mouse click event.
     * 
     * @param e The mouse event to process
     */
    void handleMouseClicked(MouseEvent e);
    
    /**
     * Processes a mouse press event.
     * 
     * @param e The mouse event to process
     */
    void handleMousePressed(MouseEvent e);
    
    /**
     * Processes a mouse release event.
     * 
     * @param e The mouse event to process
     */
    void handleMouseReleased(MouseEvent e);
    
    /**
     * Processes a mouse movement event.
     * 
     * @param e The mouse event to process
     */
    void handleMouseMoved(MouseEvent e);
    
    /**
     * Processes a mouse drag event.
     * 
     * @param e The mouse event to process
     */
    void handleMouseDragged(MouseEvent e);
    
    /**
     * Updates the input handler state.
     * Called each frame to process continuous input.
     */
    void update();
    
    /**
     * Checks if the input handler is active.
     * 
     * @return true if the handler should process input, false otherwise
     */
    boolean isActive();
} 