package view.renderers;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

import model.equipment.Weapon;
import model.gameLogic.AttackVisualData;
import utilities.WeaponImageManager;

/**
 * Responsible for rendering weapon-specific visualizations in the game.
 * Follows Single Responsibility Principle - only handles weapon rendering.
 * 
 * This class extracts weapon rendering logic from GamePanel.java to improve
 * code organization and maintainability.
 */
public class WeaponRenderer {
    private static final Logger LOGGER = Logger.getLogger(WeaponRenderer.class.getName());
    
    // Weapon image manager for weapon rendering
    private final WeaponImageManager weaponImageManager;
    
    // Rendering constants
    private static final float DEFAULT_WEAPON_SCALE = 0.8f;
    private static final float ENEMY_TRANSPARENCY = 0.8f;
    private static final float ORBIT_RADIUS = 24.0f;
    
    public WeaponRenderer() {
        this.weaponImageManager = WeaponImageManager.getInstance();
    }
    
    /**
     * Render weapon swing attack animation
     * 
     * @param g2d Graphics context
     * @param x Entity X position
     * @param y Entity Y position
     * @param weapon The weapon being used
     * @param swingData Attack visual data
     * @param isEnemy Whether this is an enemy attack
     */
    public void renderWeaponSwingAttack(Graphics2D g2d, float x, float y, 
                                      Weapon weapon, AttackVisualData swingData, 
                                      boolean isEnemy) {
        if (weapon == null || swingData == null) return;
        
        // Get weapon image
        BufferedImage weaponImage = weaponImageManager.getWeaponImage(weapon.get_name());
        if (weaponImage == null) return;
        
        // Calculate weapon position and rotation
        long currentTime = System.currentTimeMillis();
        double currentSwingAngle = swingData.getCurrentSwingAngle(currentTime);
        float range = calculateWeaponRange(weapon);
        
        // Calculate weapon base position (extend from entity center)
        float weaponBaseX = x + (float)(Math.cos(currentSwingAngle) * range);
        float weaponBaseY = y + (float)(Math.sin(currentSwingAngle) * range);
        
        // Apply transparency for enemies
        if (isEnemy) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ENEMY_TRANSPARENCY));
        }
        
        // Save current transform
        AffineTransform oldTransform = g2d.getTransform();
        
        // Create clean transform for weapon rendering
        AffineTransform weaponTransform = new AffineTransform();
        weaponTransform.translate(weaponBaseX, weaponBaseY);
        weaponTransform.rotate(currentSwingAngle + Math.PI/2); // Rotate 90 degrees for proper alignment
        weaponTransform.scale(DEFAULT_WEAPON_SCALE, DEFAULT_WEAPON_SCALE);
        
        g2d.setTransform(weaponTransform);
        
        // Draw weapon image
        int weaponWidth = weaponImage.getWidth();
        int weaponHeight = weaponImage.getHeight();
        g2d.drawImage(weaponImage, -weaponWidth/2, -weaponHeight/2, weaponWidth, weaponHeight, null);
        
        // Restore transform and composite
        g2d.setTransform(oldTransform);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }
    
    /**
     * Render Ranger bow animation
     * 
     * @param g2d Graphics context
     * @param x Entity X position
     * @param y Entity Y position
     * @param bowData Bow attack visual data
     * @param isEnemy Whether this is an enemy attack
     */
    public void renderRangerBow(Graphics2D g2d, float x, float y, 
                              AttackVisualData bowData, boolean isEnemy) {
        if (bowData == null) return;
        
        // Get bow image (always use Simple Bow for Rangers)
        BufferedImage bowImage = weaponImageManager.getWeaponImage("Simple Bow");
        if (bowImage == null) return;
        
        // Calculate bow position (orbit around entity)
        long currentTime = System.currentTimeMillis();
        double aimAngle = bowData.getCurrentSwingAngle(currentTime);
        float bowX = x + (float)(Math.cos(aimAngle) * ORBIT_RADIUS);
        float bowY = y + (float)(Math.sin(aimAngle) * ORBIT_RADIUS);
        
        // Apply transparency for enemies
        if (isEnemy) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ENEMY_TRANSPARENCY));
        }
        
        // Save current transform
        AffineTransform oldTransform = g2d.getTransform();
        
        // Create clean transform for bow rendering
        AffineTransform bowTransform = new AffineTransform();
        bowTransform.translate(bowX, bowY);
        bowTransform.rotate(aimAngle);
        bowTransform.scale(-1, 1); // Flip horizontally so string faces inward
        
        g2d.setTransform(bowTransform);
        
        // Draw bow image
        int bowWidth = bowImage.getWidth();
        int bowHeight = bowImage.getHeight();
        g2d.drawImage(bowImage, -bowWidth/2, -bowHeight/2, bowWidth, bowHeight, null);
        
        // Restore transform and composite
        g2d.setTransform(oldTransform);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }
    
    /**
     * Calculate weapon range based on weapon type
     * 
     * @param weapon The weapon to calculate range for
     * @return Weapon range in tiles
     */
    private float calculateWeaponRange(Weapon weapon) {
        // Default range values based on weapon type
        switch (weapon.get_weapon_type()) {
            case BLADE:
                return 1.0f; // Melee range
            case DISTANCE:
                return 3.0f; // Long range
            case IMPACT:
                return 1.5f; // Medium melee range
            case MAGIC:
                return 2.5f; // Magic range
            default:
                return 1.0f; // Default melee range
        }
    }
    
    /**
     * Get weapon image for a given weapon
     * 
     * @param weapon The weapon to get image for
     * @return BufferedImage or null if not found
     */
    public BufferedImage getWeaponImage(Weapon weapon) {
        if (weapon == null) return null;
        return weaponImageManager.getWeaponImage(weapon.get_name());
    }
    
    /**
     * Check if a weapon image exists
     * 
     * @param weapon The weapon to check
     * @return true if weapon image exists, false otherwise
     */
    public boolean hasWeaponImage(Weapon weapon) {
        return getWeaponImage(weapon) != null;
    }
} 