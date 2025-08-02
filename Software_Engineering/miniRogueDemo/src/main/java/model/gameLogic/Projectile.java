package model.gameLogic;

import model.characters.Player;
import model.characters.Enemy;
import model.characters.Character;
import enums.GameConstants;
import java.awt.Color;
import java.awt.Graphics2D;

public class Projectile {
    private float x, y; // Center position in pixels
    private float dx, dy; // Normalized direction vector
    private float speed; // Tiles per second
    private float distanceTraveled; // In tiles
    private float maxDistance; // In tiles
    private float radius; // In pixels
    private Character owner; // Changed from Player to Character to support both Player and Enemy
    private boolean active = true;
    private Color color = Color.CYAN;

    public Projectile(float x, float y, float dx, float dy, float speed, float maxDistance, float radius, Character owner) {
        this.x = x;
        this.y = y;
        float len = (float)Math.sqrt(dx*dx + dy*dy);
        this.dx = len == 0 ? 0 : dx / len;
        this.dy = len == 0 ? 0 : dy / len;
        this.speed = speed;
        this.maxDistance = maxDistance;
        this.radius = radius;
        this.owner = owner;
        this.distanceTraveled = 0f;
    }

    public void update(float deltaTime, model.map.Map map, java.util.List<Enemy> enemies) {
        if (!active) return;
        float moveDist = speed * deltaTime; // tiles
        float movePx = moveDist * GameConstants.TILE_SIZE;
        float nextX = x + dx * movePx;
        float nextY = y + dy * movePx;
        // Check wall collision
        int tileX = (int)(nextX / GameConstants.TILE_SIZE);
        int tileY = (int)(nextY / GameConstants.TILE_SIZE);
        if (!map.is_valid_move(new utilities.Position(tileX, tileY))) {
            active = false;
            return;
        }
        // Check collision based on owner type
        if (owner instanceof Player) {
            // Player projectile - check enemy collision
            for (Enemy enemy : enemies) {
                if (enemy.isImmune()) continue;
                float ex = enemy.getPixelX() + GameConstants.TILE_SIZE / 2f;
                float ey = enemy.getPixelY() + GameConstants.TILE_SIZE / 2f;
                float dist = (float)Math.hypot(nextX - ex, nextY - ey);
                if (dist <= radius + GameConstants.TILE_SIZE/2f - 4) { // enemy radius fudge
                    active = false;
                    // --- Apply actual damage ---
                    int rawDamage = ((Player)owner).get_total_attack();
                    int actualDamage = Math.max(1, rawDamage - enemy.get_total_defense());
                    boolean alive = enemy.take_damage(actualDamage);
                    if (owner instanceof Player && ((Player)owner).getGameLogic() != null) {
                        if (!alive) {
                            // Handle loot drops for enemies killed by projectiles
                            ((Player)owner).getGameLogic().handleEnemyDeath(enemy);
                            if (owner instanceof Player) {
                                ((Player)owner).increment_enemies_slain();
                            }
                        }
                    }
                    // Trigger pushback on enemy (distance 0)
                    enemy.triggerPushback(dx, dy, 0f, GameConstants.TILE_SIZE * 0.18f);
                    enemy.triggerHitState(model.characters.Enemy.BASE_HIT_STATE_DURATION);
                    break;
                }
            }
        } else if (owner instanceof Enemy) {
            // Enemy projectile - check player collision
            Player player = ((Enemy)owner).getGameLogic().get_player();
            if (player != null && !player.isImmune()) {
                float px = player.getPixelX() + GameConstants.TILE_SIZE / 2f;
                float py = player.getPixelY() + GameConstants.TILE_SIZE / 2f;
                float dist = (float)Math.hypot(nextX - px, nextY - py);
                if (dist <= radius + GameConstants.TILE_SIZE/2f - 4) { // player radius fudge
                    active = false;
                    // --- Apply actual damage ---
                    int rawDamage = ((Enemy)owner).get_total_attack();
                                int actualDamage = Math.max(1, rawDamage - player.get_total_defense());
            boolean alive = player.take_damage(actualDamage);
            ((Enemy)owner).getGameLogic().notify_observers("PLAYER_DAMAGED", actualDamage); // Trigger damage flash effect
                    if (!alive) {
                        // Store the enemy that killed the player
                        ((Enemy)owner).getGameLogic().setLastAttackingEnemy((Enemy)owner);
                    } else {
                        // Store the enemy that attacked the player (in case they die later)
                        ((Enemy)owner).getGameLogic().setLastAttackingEnemy((Enemy)owner);
                    }
                    // Always reset chase timer on hit
                    ((Enemy)owner).setChaseEndTime(System.currentTimeMillis() + 3000);
                    // Start celebratory state for enemy (25% chance) - no fallback for projectiles
                    if (((Enemy)owner).getRandom().nextInt(4) == 0) {
                        ((Enemy)owner).startCelebratoryState();
                    }
                    // Trigger pushback on player (distance 0)
                    player.triggerPushback(dx, dy, 0f, GameConstants.TILE_SIZE * 0.18f);
                    player.setImmune(400); // Reduced from 800ms (base immunity period)
                }
            }
        }
        // Move
        x = nextX;
        y = nextY;
        distanceTraveled += moveDist;
        if (distanceTraveled >= maxDistance) {
            active = false;
        }
    }

    // Static arrow image cache
    private static java.awt.image.BufferedImage arrowImage = null;
    
    // Static searing ray image cache for Mage projectiles
    private static java.awt.image.BufferedImage[] searingRayImages = new java.awt.image.BufferedImage[5];
    
    private java.awt.image.BufferedImage getArrowImage() {
        if (arrowImage == null) {
            try {
                arrowImage = javax.imageio.ImageIO.read(
                    getClass().getClassLoader().getResourceAsStream("images/items/arrow.png"));
            } catch (java.io.IOException | IllegalArgumentException ex) {
                System.err.println("Failed to load arrow image: " + ex.getMessage());
                return null;
            }
        }
        return arrowImage;
    }
    
    private java.awt.image.BufferedImage getSearingRayImage(int frame) {
        if (searingRayImages[frame] == null) {
            try {
                searingRayImages[frame] = javax.imageio.ImageIO.read(
                    getClass().getClassLoader().getResourceAsStream("images/items/searing_ray_" + frame + ".png"));
            } catch (java.io.IOException | IllegalArgumentException ex) {
                System.err.println("Failed to load searing ray image " + frame + ": " + ex.getMessage());
                return null;
            }
        }
        return searingRayImages[frame];
    }
    
    public void render(Graphics2D g2d) {
        // Check if owner is a Ranger (either Player or Enemy)
        boolean isRanger = false;
        boolean isMage = false;
        if (owner instanceof Player) {
            isRanger = ((Player)owner).getPlayerClassOOP() instanceof model.characters.RangerClass;
            isMage = ((Player)owner).getPlayerClassOOP() instanceof model.characters.MageClass;
        } else if (owner instanceof Enemy) {
            isRanger = ((Enemy)owner).getEnemyClassOOP() instanceof model.characters.RangerClass;
            isMage = ((Enemy)owner).getEnemyClassOOP() instanceof model.characters.MageClass;
        }
        
        if (isRanger) {
            // Draw arrow image for Ranger projectiles
            java.awt.image.BufferedImage arrowImg = getArrowImage();
            if (arrowImg != null) {
                double angle = Math.atan2(dy, dx);
                int arrowSize = 24; // Size of the arrow image
                
                java.awt.geom.AffineTransform old = g2d.getTransform();
                g2d.translate(x, y);
                g2d.rotate(angle + Math.PI/4); // Add 45 degrees (π/4 radians) clockwise rotation
                
                // Draw the arrow image with the top pointing in the direction of travel
                g2d.drawImage(arrowImg, -arrowSize/2, -arrowSize/2, arrowSize, arrowSize, null);
                
                g2d.setTransform(old);
            } else {
                // Fallback to simple arrow if image loading fails
                double angle = Math.atan2(dy, dx);
                int length = 20;
                int width = 4;
                g2d.setColor(Color.CYAN);
                java.awt.geom.AffineTransform old = g2d.getTransform();
                g2d.translate(x, y);
                g2d.rotate(angle);
                g2d.fillOval(-length/2, -width/2, length, width);
                g2d.setTransform(old);
            }
        } else if (isMage) {
            // Draw animated searing ray for Mage projectiles
            // Calculate animation frame based on travel progress (0-4)
            double progress = distanceTraveled / maxDistance;
            int frame = Math.min(4, (int)(progress * 5)); // 5 frames (0-4)
            
            java.awt.image.BufferedImage searingRayImg = getSearingRayImage(frame);
            if (searingRayImg != null) {
                double angle = Math.atan2(dy, dx);
                int raySize = 32; // Size of the searing ray image
                
                java.awt.geom.AffineTransform old = g2d.getTransform();
                g2d.translate(x, y);
                g2d.rotate(angle);
                
                // Draw the searing ray image
                g2d.drawImage(searingRayImg, -raySize/2, -raySize/2, raySize, raySize, null);
                
                g2d.setTransform(old);
            } else {
                // Fallback to simple projectile if image loading fails
                g2d.setColor(Color.ORANGE);
                g2d.fillOval((int)(x - radius), (int)(y - radius), (int)(2*radius), (int)(2*radius));
            }
        } else if (owner instanceof Enemy) {
            // Draw enemy projectiles in red (for non-Ranger, non-Mage enemies)
            g2d.setColor(Color.RED);
            g2d.fillOval((int)(x - radius), (int)(y - radius), (int)(2*radius), (int)(2*radius));
        } else {
            // Default projectile rendering (for non-Ranger, non-Mage players)
            g2d.setColor(getColor());
            g2d.fillOval((int)(x - radius), (int)(y - radius), (int)(2*radius), (int)(2*radius));
        }
    }

    public boolean isActive() { return active; }
    public float getX() { return x; }
    public float getY() { return y; }
    public float getRadius() { return radius; }
    public Color getColor() { return color; }
    public Character getOwner() { return owner; }
    
    // Additional getters for rendering
    public float getDx() { return dx; }
    public float getDy() { return dy; }
    public float getDistanceTraveled() { return distanceTraveled; }
    public float getMaxDistance() { return maxDistance; }
} 