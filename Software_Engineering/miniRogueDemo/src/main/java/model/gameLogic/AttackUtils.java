package model.gameLogic;

import model.characters.Character;
import model.characters.Player;
import model.characters.Enemy;
import model.characters.BaseClass;
import enums.GameConstants;
import java.util.Timer;
import java.util.TimerTask;
import java.util.List;

/**
 * Utility class for shared attack logic between players and enemies
 */
public class AttackUtils {
    
    /**
     * Create swing-based attack visual data with standard parameters
     */
    public static AttackVisualData createSwingAttackData(int aimDX, int aimDY, float range, 
                                                        double attackAngle, BaseClass attackerClass, long startTime) {
        double centerAngle = attackAngle;
        int totalWidth = attackerClass.getAttackWidth(); // Total swing width in degrees
        double swingFanWidth = 30.0; // 30-degree fan that rotates
        double swingStartAngle = centerAngle - Math.toRadians(totalWidth / 2.0);
        double swingEndAngle = centerAngle + Math.toRadians(totalWidth / 2.0);
        long swingDuration = 200; // 200ms swing duration
        
        return new AttackVisualData(
            aimDX, aimDY, range, attackAngle,
            swingStartAngle, swingEndAngle, swingFanWidth,
            startTime, swingDuration
        );
    }
    
    /**
     * Create static bow attack visual data for Rangers (no swinging motion)
     */
    public static AttackVisualData createStaticBowData(int aimDX, int aimDY, double attackAngle, long startTime) {
        // For Ranger bows, we want a static angle that doesn't change over time
        // Use a very short duration so the bow appears briefly but doesn't slide
        long bowDuration = 150; // 150ms display duration
        
        return new AttackVisualData(
            aimDX, aimDY, 1.0f, attackAngle,
            attackAngle, attackAngle, 0.0, // Same start and end angle = no sliding
            startTime, bowDuration
        );
    }
    
    /**
     * Start swing attack detection with continuous hit checking
     */
    public static void startSwingAttackDetection(AttackVisualData swingData, 
                                                SwingHitDetector hitDetector) {
        // Create a timer to check for hits during the swing
        Timer swingTimer = new Timer();
        swingTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                checkSwingHitDetection(swingData, hitDetector);
            }
        }, 0, 16); // Check every 16ms (60fps) during swing
        
        // Stop the timer after swing duration
        Timer stopTimer = new Timer();
        stopTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                swingTimer.cancel();
                stopTimer.cancel();
            }
        }, swingData.getSwingDuration());
    }
    
    /**
     * Check for hits during swing attack
     */
    private static void checkSwingHitDetection(AttackVisualData swingData, 
                                              SwingHitDetector hitDetector) {
        if (!swingData.isSwingActive(System.currentTimeMillis())) {
            return; // Swing finished
        }
        
        long currentTime = System.currentTimeMillis();
        double currentSwingAngle = swingData.getCurrentSwingAngle(currentTime);
        double swingFanWidth = swingData.getSwingFanWidth();
        double halfFanWidth = Math.toRadians(swingFanWidth) / 2.0;
        
        // Delegate hit detection to the provided detector
        hitDetector.checkHits(currentSwingAngle, halfFanWidth, swingData.getAttackRange());
    }
    
    /**
     * Interface for swing hit detection (allows different behavior for players vs enemies)
     */
    public interface SwingHitDetector {
        void checkHits(double currentSwingAngle, double halfFanWidth, float range);
    }
    
    /**
     * Player swing hit detector
     */
    public static class PlayerSwingHitDetector implements SwingHitDetector {
        private final Player player;
        private final List<Enemy> enemies;
        private final GameLogic gameLogic;
        
        public PlayerSwingHitDetector(Player player, List<Enemy> enemies, GameLogic gameLogic) {
            this.player = player;
            this.enemies = enemies;
            this.gameLogic = gameLogic;
        }
        
        @Override
        public void checkHits(double currentSwingAngle, double halfFanWidth, float range) {
            float playerX = player.getPixelX() + GameConstants.TILE_SIZE / 2f;
            float playerY = player.getPixelY() + GameConstants.TILE_SIZE / 2f;
            
            for (Enemy enemy : enemies) {
                if (enemy.isImmune()) continue;
                
                float enemyX = enemy.getPixelX() + GameConstants.TILE_SIZE / 2f;
                float enemyY = enemy.getPixelY() + GameConstants.TILE_SIZE / 2f;
                double dist = Math.hypot(enemyX - playerX, enemyY - playerY) / GameConstants.TILE_SIZE;
                
                if (dist > range + 0.25) continue;
                
                // Check if enemy is within the current swing fan
                double angle = Math.atan2(enemyY - playerY, enemyX - playerX);
                double delta = Math.abs(Math.atan2(Math.sin(angle - currentSwingAngle), Math.cos(angle - currentSwingAngle)));
                
                if (delta <= halfFanWidth) {
                    // Enemy is hit by current swing position
                    handlePlayerHitEnemy(enemy, playerX, playerY, enemyX, enemyY, range);
                }
            }
        }
        
        private void handlePlayerHitEnemy(Enemy enemy, float playerX, float playerY, 
                                        float enemyX, float enemyY, float range) {
            // Trigger pushback on enemy
            float pushDirX = enemyX - playerX;
            float pushDirY = enemyY - playerY;
            float pushDist = (range + 0.2f) * GameConstants.TILE_SIZE;
            float pushSpeed = GameConstants.TILE_SIZE * 0.18f;
            enemy.triggerPushback(pushDirX, pushDirY, pushDist, pushSpeed);
            // Set immunity after pushback ends
            enemy.triggerHitState(Enemy.BASE_HIT_STATE_DURATION);
            // --- Apply actual damage ---
            int rawDamage = player.get_total_attack();
            int actualDamage = Math.max(1, rawDamage - enemy.get_total_defense());
            boolean alive = enemy.take_damage(actualDamage);
            if (!alive) {
                // Handle loot drops for both enemies and bosses
                gameLogic.handleEnemyDeath(enemy);
                
                if (enemy.isBoss()) {
                    // Boss defeated - special handling
                    gameLogic.notify_observers("BOSS_DEFEATED", enemy);
                } else {
                    // Regular enemy defeated - add log message
                    String enemyClass = enemy.get_character_class().toString().toLowerCase();
                    gameLogic.notify_observers("LOG_MESSAGE", "Enemy " + enemyClass + " defeated!");
                    player.increment_enemies_slain();
                }
            }
        }
    }
    
    /**
     * Enemy swing hit detector
     */
    public static class EnemySwingHitDetector implements SwingHitDetector {
        private final Enemy enemy;
        private final Player player;
        private final GameLogic gameLogic;
        
        public EnemySwingHitDetector(Enemy enemy, Player player, GameLogic gameLogic) {
            this.enemy = enemy;
            this.player = player;
            this.gameLogic = gameLogic;
        }
        
        @Override
        public void checkHits(double currentSwingAngle, double halfFanWidth, float range) {
            if (player.isImmune()) return;
            
            float enemyX = enemy.getPixelX() + GameConstants.TILE_SIZE / 2f;
            float enemyY = enemy.getPixelY() + GameConstants.TILE_SIZE / 2f;
            float playerX = player.getPixelX() + GameConstants.TILE_SIZE / 2f;
            float playerY = player.getPixelY() + GameConstants.TILE_SIZE / 2f;
            double dist = Math.hypot(playerX - enemyX, playerY - enemyY) / GameConstants.TILE_SIZE;
            
            // Adjust range based on entity size and boss modifiers
            float sizeMultiplier = 1.0f;
            float rangeModifier = 1.0f;
            if (enemy instanceof model.characters.Boss) {
                sizeMultiplier = ((model.characters.Boss) enemy).getSizeMultiplier();
                rangeModifier = ((model.characters.Boss) enemy).getRangeModifier();
            }
            float adjustedRange = range * sizeMultiplier * rangeModifier;
            
            if (dist > adjustedRange + 0.25) return;
            
            // Check if player is within the current swing fan
            double angle = Math.atan2(playerY - enemyY, playerX - enemyX);
            double delta = Math.abs(Math.atan2(Math.sin(angle - currentSwingAngle), Math.cos(angle - currentSwingAngle)));
            
            if (delta <= halfFanWidth) {
                // Player is hit by current swing position
                handleEnemyHitPlayer(playerX, playerY, enemyX, enemyY, range);
            }
        }
        
        private void handleEnemyHitPlayer(float playerX, float playerY, 
                                        float enemyX, float enemyY, float range) {
            // --- Apply actual damage ---
            int rawDamage = enemy.get_total_attack();
            int actualDamage = Math.max(1, rawDamage - player.get_total_defense());
            boolean alive = player.take_damage(actualDamage);
            gameLogic.notify_observers("PLAYER_DAMAGED", actualDamage); // Trigger damage flash effect
            if (!alive) {
                // Store the enemy that killed the player
                gameLogic.setLastAttackingEnemy(enemy);
                player.increment_enemies_slain();
            } else {
                // Store the enemy that attacked the player (in case they die later)
                gameLogic.setLastAttackingEnemy(enemy);
            }

            // Always reset chase timer on hit
            enemy.setChaseEndTime(System.currentTimeMillis() + 3000);
            // Trigger pushback on player
            float pushDirX = playerX - enemyX;
            float pushDirY = playerY - enemyY;
            float pushDist = (range + 0.2f) * GameConstants.TILE_SIZE;
            float pushSpeed = GameConstants.TILE_SIZE * 0.18f;
            player.triggerPushback(pushDirX, pushDirY, pushDist, pushSpeed);
            // Set immunity after pushback ends
            player.setImmune(400); // Reduced from 800ms (base immunity period)
            
            // Start fallback after successful attack
            enemy.startFallbackState();
            
            // Schedule celebratory state to start after fallback completes (25% chance)
            if (new java.util.Random().nextInt(4) == 0) {
                Timer celebratoryTimer = new Timer();
                celebratoryTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (enemy.is_alive() && !enemy.isDying()) {
                            enemy.startCelebratoryState();
                        }
                    }
                }, 1200); // Start after fallback duration
            }
        }
    }
} 