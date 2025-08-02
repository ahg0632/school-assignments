package interfaces;

import java.awt.Graphics2D;

/**
 * Interface for objects that can be rendered in the game.
 * This establishes a contract for all renderable entities.
 */
public interface Renderable {
    
    /**
     * Renders this object using the provided graphics context.
     * 
     * @param g2d The graphics context to render with
     */
    void render(Graphics2D g2d);
    
    /**
     * Checks if this object should be rendered.
     * 
     * @return true if the object should be rendered, false otherwise
     */
    boolean isVisible();
    
    /**
     * Gets the rendering priority of this object.
     * Lower values are rendered first (behind other objects).
     * 
     * @return the rendering priority (0 = background, higher = foreground)
     */
    int getRenderPriority();
} 