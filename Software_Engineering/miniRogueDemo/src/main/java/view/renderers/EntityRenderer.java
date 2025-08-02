package view.renderers;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

import model.characters.Player;
import model.characters.Enemy;
import model.characters.Boss;
import model.characters.Character;
import model.gameLogic.AttackVisualData;
import utilities.WeaponImageManager;

/**
 * Responsible for rendering all entities (players, enemies, bosses) in the game.
 * Follows Single Responsibility Principle - only handles entity rendering.
 * 
 * This class extracts entity rendering logic from GamePanel.java to improve
 * code organization and maintainability.
 */
public class EntityRenderer {
    private static final Logger LOGGER = Logger.getLogger(EntityRenderer.class.getName());
    
    // Weapon image manager for entity weapon rendering
    private final WeaponImageManager weaponImageManager;
    
    // Rendering constants
    private static final int SPRITE_SIZE = 32;
    private static final int HALF_SPRITE = SPRITE_SIZE / 2;
    
    public EntityRenderer() {
        this.weaponImageManager = WeaponImageManager.getInstance();
    }
    
    /**
     * Render all entities in the game
     * 
     * @param g2d Graphics context
     * @param player The player character
     * @param enemies List of enemies
     * @param bosses List of bosses
     * @param playerSwingData Player attack visual data
     * @param enemySwingData Enemy attack visual data
     * @param playerRangerBowData Player Ranger bow visual data
     * @param enemyRangerBowData Enemy Ranger bow visual data
     */
    public void renderEntities(Graphics2D g2d, 
                              Player player, 
                              List<Enemy> enemies, 
                              List<Boss> bosses,
                              AttackVisualData playerSwingData,
                              AttackVisualData enemySwingData,
                              AttackVisualData playerRangerBowData,
                              AttackVisualData enemyRangerBowData) {
        
        // Render player
        if (player != null) {
            renderPlayer(g2d, player, playerSwingData, playerRangerBowData);
        }
        
        // Render enemies
        if (enemies != null) {
            for (Enemy enemy : enemies) {
                renderEnemy(g2d, enemy, enemySwingData, enemyRangerBowData);
            }
        }
        
        // Render bosses
        if (bosses != null) {
            for (Boss boss : bosses) {
                renderBoss(g2d, boss, enemySwingData, enemyRangerBowData);
            }
        }
    }
    
    /**
     * Render the player character
     */
    private void renderPlayer(Graphics2D g2d, Player player, 
                            AttackVisualData swingData, 
                            AttackVisualData rangerBowData) {
        float px = player.get_position().get_x();
        float py = player.get_position().get_y();
        
        // Get player sprite based on character class
        BufferedImage playerSprite = getPlayerSprite(player);
        if (playerSprite != null) {
            g2d.drawImage(playerSprite, (int)(px - HALF_SPRITE), (int)(py - HALF_SPRITE), 
                         SPRITE_SIZE, SPRITE_SIZE, null);
        }
        
        // Render player weapon attack if active
        long currentTime = System.currentTimeMillis();
        if (swingData != null && swingData.isSwingActive(currentTime)) {
            renderWeaponAttack(g2d, px, py, player.get_equipped_weapon(), swingData, false);
        }
        
        // Render player Ranger bow if active
        if (rangerBowData != null && rangerBowData.isSwingActive(currentTime)) {
            renderRangerBow(g2d, px, py, rangerBowData, false);
        }
    }
    
    /**
     * Render an enemy character
     */
    private void renderEnemy(Graphics2D g2d, Enemy enemy, 
                           AttackVisualData swingData, 
                           AttackVisualData rangerBowData) {
        float ex = enemy.get_position().get_x();
        float ey = enemy.get_position().get_y();
        
        // Get enemy sprite
        BufferedImage enemySprite = getEnemySprite(enemy);
        if (enemySprite != null) {
            g2d.drawImage(enemySprite, (int)(ex - HALF_SPRITE), (int)(ey - HALF_SPRITE), 
                         SPRITE_SIZE, SPRITE_SIZE, null);
        }
        
        // Render enemy weapon attack if active
        long currentTime = System.currentTimeMillis();
        if (swingData != null && swingData.isSwingActive(currentTime)) {
            renderWeaponAttack(g2d, ex, ey, enemy.get_equipped_weapon(), swingData, true);
        }
        
        // Render enemy Ranger bow if active
        if (rangerBowData != null && rangerBowData.isSwingActive(currentTime)) {
            renderRangerBow(g2d, ex, ey, rangerBowData, true);
        }
    }
    
    /**
     * Render a boss character
     */
    private void renderBoss(Graphics2D g2d, Boss boss, 
                          AttackVisualData swingData, 
                          AttackVisualData rangerBowData) {
        float bx = boss.get_position().get_x();
        float by = boss.get_position().get_y();
        
        // Get boss sprite
        BufferedImage bossSprite = getBossSprite(boss);
        if (bossSprite != null) {
            g2d.drawImage(bossSprite, (int)(bx - HALF_SPRITE), (int)(by - HALF_SPRITE), 
                         SPRITE_SIZE, SPRITE_SIZE, null);
        }
        
        // Render boss weapon attack if active
        long currentTime = System.currentTimeMillis();
        if (swingData != null && swingData.isSwingActive(currentTime)) {
            renderWeaponAttack(g2d, bx, by, boss.get_equipped_weapon(), swingData, true);
        }
        
        // Render boss Ranger bow if active
        if (rangerBowData != null && rangerBowData.isSwingActive(currentTime)) {
            renderRangerBow(g2d, bx, by, rangerBowData, true);
        }
    }
    
    /**
     * Get player sprite based on character class
     */
    private BufferedImage getPlayerSprite(Player player) {
        // This method will be implemented to load player sprites
        // For now, return null to avoid compilation errors
        // TODO: Implement sprite loading logic
        return null;
    }
    
    /**
     * Get enemy sprite
     */
    private BufferedImage getEnemySprite(Enemy enemy) {
        // This method will be implemented to load enemy sprites
        // For now, return null to avoid compilation errors
        // TODO: Implement sprite loading logic
        return null;
    }
    
    /**
     * Get boss sprite
     */
    private BufferedImage getBossSprite(Boss boss) {
        // This method will be implemented to load boss sprites
        // For now, return null to avoid compilation errors
        // TODO: Implement sprite loading logic
        return null;
    }
    
    /**
     * Render weapon attack animation
     */
    private void renderWeaponAttack(Graphics2D g2d, float x, float y, 
                                  model.equipment.Weapon weapon, 
                                  AttackVisualData swingData, boolean isEnemy) {
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
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
        }
        
        // Save current transform
        AffineTransform oldTransform = g2d.getTransform();
        
        // Create clean transform for weapon rendering
        AffineTransform weaponTransform = new AffineTransform();
        weaponTransform.translate(weaponBaseX, weaponBaseY);
        weaponTransform.rotate(currentSwingAngle + Math.PI/2); // Rotate 90 degrees for proper alignment
        weaponTransform.scale(0.8, 0.8); // Scale weapon to appropriate size
        
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
     * Calculate weapon range based on weapon type
     */
    private float calculateWeaponRange(model.equipment.Weapon weapon) {
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
     * Render Ranger bow animation
     */
    private void renderRangerBow(Graphics2D g2d, float x, float y, 
                               AttackVisualData bowData, boolean isEnemy) {
        if (bowData == null) return;
        
        // Get bow image (always use Simple Bow for Rangers)
        BufferedImage bowImage = weaponImageManager.getWeaponImage("Simple Bow");
        if (bowImage == null) return;
        
        // Calculate bow position (orbit around entity)
        long currentTime = System.currentTimeMillis();
        double aimAngle = bowData.getCurrentSwingAngle(currentTime);
        float orbitRadius = 24;
        float bowX = x + (float)(Math.cos(aimAngle) * orbitRadius);
        float bowY = y + (float)(Math.sin(aimAngle) * orbitRadius);
        
        // Apply transparency for enemies
        if (isEnemy) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
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
} 