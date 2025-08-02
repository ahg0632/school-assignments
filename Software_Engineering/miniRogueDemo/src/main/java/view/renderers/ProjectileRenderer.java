package view.renderers;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.logging.Logger;

import model.gameLogic.Projectile;
import model.characters.Player;
import model.characters.Enemy;
import model.characters.RangerClass;
import model.characters.MageClass;

/**
 * Responsible for rendering projectiles in the game.
 * Follows Single Responsibility Principle - only handles projectile rendering.
 * 
 * This class extracts projectile rendering logic from Projectile.java to improve
 * code organization and maintainability.
 */
public class ProjectileRenderer {
    private static final Logger LOGGER = Logger.getLogger(ProjectileRenderer.class.getName());
    
    // Projectile constants
    private static final int ARROW_SIZE = 24;
    private static final int RAY_SIZE = 32;
    private static final int DEFAULT_LENGTH = 20;
    private static final int DEFAULT_WIDTH = 4;
    
    // Colors
    private static final Color ENEMY_PROJECTILE_COLOR = Color.RED;
    private static final Color DEFAULT_PROJECTILE_COLOR = Color.CYAN;
    private static final Color FALLBACK_PROJECTILE_COLOR = Color.ORANGE;
    
    // Image caches
    private static BufferedImage arrowImage = null;
    private static BufferedImage[] searingRayImages = new BufferedImage[5];
    
    /**
     * Render a projectile
     * 
     * @param g2d Graphics context
     * @param projectile The projectile to render
     */
    public void renderProjectile(Graphics2D g2d, Projectile projectile) {
        if (projectile == null || !projectile.isActive()) return;
        
        // Determine projectile type based on owner
        ProjectileType type = getProjectileType(projectile);
        
        switch (type) {
            case RANGER_ARROW:
                renderRangerArrow(g2d, projectile);
                break;
            case MAGE_RAY:
                renderMageRay(g2d, projectile);
                break;
            case ENEMY_PROJECTILE:
                renderEnemyProjectile(g2d, projectile);
                break;
            case DEFAULT_PROJECTILE:
                renderDefaultProjectile(g2d, projectile);
                break;
        }
    }
    
    /**
     * Determine the type of projectile based on its owner
     */
    private ProjectileType getProjectileType(Projectile projectile) {
        Object owner = projectile.getOwner();
        
        if (owner instanceof Player) {
            Player player = (Player) owner;
            if (player.getPlayerClassOOP() instanceof RangerClass) {
                return ProjectileType.RANGER_ARROW;
            } else if (player.getPlayerClassOOP() instanceof MageClass) {
                return ProjectileType.MAGE_RAY;
            }
        } else if (owner instanceof Enemy) {
            Enemy enemy = (Enemy) owner;
            if (enemy.getEnemyClassOOP() instanceof RangerClass) {
                return ProjectileType.RANGER_ARROW;
            } else if (enemy.getEnemyClassOOP() instanceof MageClass) {
                return ProjectileType.MAGE_RAY;
            } else {
                return ProjectileType.ENEMY_PROJECTILE;
            }
        }
        
        return ProjectileType.DEFAULT_PROJECTILE;
    }
    
    /**
     * Render Ranger arrow projectile
     */
    private void renderRangerArrow(Graphics2D g2d, Projectile projectile) {
        BufferedImage arrowImg = getArrowImage();
        if (arrowImg != null) {
            // Calculate angle and transform
            double angle = Math.atan2(projectile.getDy(), projectile.getDx());
            
            AffineTransform old = g2d.getTransform();
            g2d.translate(projectile.getX(), projectile.getY());
            g2d.rotate(angle + Math.PI/4); // Add 45 degrees clockwise rotation
            
            // Draw the arrow image
            g2d.drawImage(arrowImg, -ARROW_SIZE/2, -ARROW_SIZE/2, ARROW_SIZE, ARROW_SIZE, null);
            
            g2d.setTransform(old);
        } else {
            // Fallback to simple arrow
            renderFallbackArrow(g2d, projectile);
        }
    }
    
    /**
     * Render Mage searing ray projectile
     */
    private void renderMageRay(Graphics2D g2d, Projectile projectile) {
        // Calculate animation frame based on travel progress
        double progress = projectile.getDistanceTraveled() / projectile.getMaxDistance();
        int frame = Math.min(4, (int)(progress * 5)); // 5 frames (0-4)
        
        BufferedImage searingRayImg = getSearingRayImage(frame);
        if (searingRayImg != null) {
            double angle = Math.atan2(projectile.getDy(), projectile.getDx());
            
            AffineTransform old = g2d.getTransform();
            g2d.translate(projectile.getX(), projectile.getY());
            g2d.rotate(angle);
            
            // Draw the searing ray image
            g2d.drawImage(searingRayImg, -RAY_SIZE/2, -RAY_SIZE/2, RAY_SIZE, RAY_SIZE, null);
            
            g2d.setTransform(old);
        } else {
            // Fallback to simple projectile
            renderFallbackProjectile(g2d, projectile);
        }
    }
    
    /**
     * Render enemy projectile
     */
    private void renderEnemyProjectile(Graphics2D g2d, Projectile projectile) {
        g2d.setColor(ENEMY_PROJECTILE_COLOR);
        g2d.fillOval((int)(projectile.getX() - projectile.getRadius()), 
                     (int)(projectile.getY() - projectile.getRadius()), 
                     (int)(2 * projectile.getRadius()), 
                     (int)(2 * projectile.getRadius()));
    }
    
    /**
     * Render default projectile
     */
    private void renderDefaultProjectile(Graphics2D g2d, Projectile projectile) {
        g2d.setColor(projectile.getColor() != null ? projectile.getColor() : DEFAULT_PROJECTILE_COLOR);
        g2d.fillOval((int)(projectile.getX() - projectile.getRadius()), 
                     (int)(projectile.getY() - projectile.getRadius()), 
                     (int)(2 * projectile.getRadius()), 
                     (int)(2 * projectile.getRadius()));
    }
    
    /**
     * Render fallback arrow (when image loading fails)
     */
    private void renderFallbackArrow(Graphics2D g2d, Projectile projectile) {
        double angle = Math.atan2(projectile.getDy(), projectile.getDx());
        
        g2d.setColor(DEFAULT_PROJECTILE_COLOR);
        AffineTransform old = g2d.getTransform();
        g2d.translate(projectile.getX(), projectile.getY());
        g2d.rotate(angle);
        g2d.fillOval(-DEFAULT_LENGTH/2, -DEFAULT_WIDTH/2, DEFAULT_LENGTH, DEFAULT_WIDTH);
        g2d.setTransform(old);
    }
    
    /**
     * Render fallback projectile (when image loading fails)
     */
    private void renderFallbackProjectile(Graphics2D g2d, Projectile projectile) {
        g2d.setColor(FALLBACK_PROJECTILE_COLOR);
        g2d.fillOval((int)(projectile.getX() - projectile.getRadius()), 
                     (int)(projectile.getY() - projectile.getRadius()), 
                     (int)(2 * projectile.getRadius()), 
                     (int)(2 * projectile.getRadius()));
    }
    
    /**
     * Get arrow image (cached)
     */
    private BufferedImage getArrowImage() {
        if (arrowImage == null) {
            try {
                arrowImage = javax.imageio.ImageIO.read(
                    getClass().getClassLoader().getResourceAsStream("images/items/arrow.png"));
            } catch (Exception e) {
                LOGGER.warning("Failed to load arrow image: " + e.getMessage());
                return null;
            }
        }
        return arrowImage;
    }
    
    /**
     * Get searing ray image for specific frame (cached)
     */
    private BufferedImage getSearingRayImage(int frame) {
        if (frame < 0 || frame >= searingRayImages.length) {
            return null;
        }
        
        if (searingRayImages[frame] == null) {
            try {
                searingRayImages[frame] = javax.imageio.ImageIO.read(
                    getClass().getClassLoader().getResourceAsStream("images/items/searing_ray_" + frame + ".png"));
            } catch (Exception e) {
                LOGGER.warning("Failed to load searing ray image " + frame + ": " + e.getMessage());
                return null;
            }
        }
        return searingRayImages[frame];
    }
    
    /**
     * Clear image caches
     */
    public void clearImageCaches() {
        arrowImage = null;
        searingRayImages = new BufferedImage[5];
    }
    
    /**
     * Projectile types for rendering
     */
    private enum ProjectileType {
        RANGER_ARROW,
        MAGE_RAY,
        ENEMY_PROJECTILE,
        DEFAULT_PROJECTILE
    }
} 