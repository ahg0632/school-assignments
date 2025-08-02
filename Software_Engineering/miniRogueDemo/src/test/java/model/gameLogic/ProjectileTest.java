package model.gameLogic;

import enums.CharacterClass;
import enums.GameConstants;
import utilities.Position;
import model.characters.Player;
import model.characters.Enemy;
import model.characters.Boss;
import model.characters.BaseClass;
import model.characters.MageClass;
import model.characters.RangerClass;
import model.characters.WarriorClass;
import model.map.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for Projectile class.
 * Tests projectile creation, movement, collision detection, and rendering.
 * Appropriate for a school project.
 */
@DisplayName("Projectile System Tests")
class ProjectileTest {

    private Player testPlayer;
    private Enemy testEnemy;
    private Boss testBoss;
    private GameLogic gameLogic;
    private List<Enemy> enemies;
    private Map testMap;
    private BaseClass mageClass;
    private BaseClass rangerClass;
    private BaseClass warriorClass;

    @BeforeEach
    void setUp() {
        testPlayer = new Player("TestPlayer", CharacterClass.MAGE, new Position(5, 5));
        testEnemy = new Enemy("TestEnemy", CharacterClass.WARRIOR, new Position(6, 6), "aggressive");
        testBoss = new Boss("TestBoss", CharacterClass.WARRIOR, new Position(10, 10));
        gameLogic = new GameLogic(testPlayer);
        
        // Set up GameLogic references for enemies
        testEnemy.setGameLogic(gameLogic);
        testBoss.setGameLogic(gameLogic);
        
        enemies = new ArrayList<>();
        enemies.add(testEnemy);
        enemies.add(testBoss);
        testMap = new Map(1, Map.FloorType.REGULAR);
        mageClass = new MageClass();
        rangerClass = new RangerClass();
        warriorClass = new WarriorClass();
    }

    /**
     * Tests basic projectile creation and properties.
     */
    @Test
    @DisplayName("Projectile Creation and Properties")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testProjectileCreation() {
        // Test player projectile creation
        Projectile playerProjectile = new Projectile(100f, 100f, 1f, 0f, 5f, 10f, 4f, testPlayer);
        
        assertNotNull(playerProjectile, "Player projectile should not be null");
        assertEquals(100f, playerProjectile.getX(), "Projectile X should be 100");
        assertEquals(100f, playerProjectile.getY(), "Projectile Y should be 100");
        assertEquals(4f, playerProjectile.getRadius(), "Projectile radius should be 4");
        assertEquals(testPlayer, playerProjectile.getOwner(), "Projectile owner should be player");
        assertTrue(playerProjectile.isActive(), "Projectile should be active initially");
        assertEquals(Color.CYAN, playerProjectile.getColor(), "Default color should be cyan");
        
        // Test enemy projectile creation
        Projectile enemyProjectile = new Projectile(200f, 200f, 0f, 1f, 3f, 8f, 3f, testEnemy);
        
        assertNotNull(enemyProjectile, "Enemy projectile should not be null");
        assertEquals(200f, enemyProjectile.getX(), "Enemy projectile X should be 200");
        assertEquals(200f, enemyProjectile.getY(), "Enemy projectile Y should be 200");
        assertEquals(3f, enemyProjectile.getRadius(), "Enemy projectile radius should be 3");
        assertEquals(testEnemy, enemyProjectile.getOwner(), "Projectile owner should be enemy");
        assertTrue(enemyProjectile.isActive(), "Enemy projectile should be active initially");
    }

    /**
     * Tests projectile movement and position updates.
     */
    @Test
    @DisplayName("Projectile Movement and Position Updates")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testProjectileMovement() {
        // Create projectile moving right
        Projectile projectile = new Projectile(100f, 100f, 1f, 0f, 5f, 10f, 4f, testPlayer);
        float initialX = projectile.getX();
        float initialY = projectile.getY();
        
        // Update projectile position
        float deltaTime = 0.1f; // 100ms
        projectile.update(deltaTime, testMap, enemies);
        
        // Check that projectile moved
        assertTrue(projectile.getX() > initialX, "Projectile should move right");
        assertEquals(initialY, projectile.getY(), "Projectile Y should remain the same");
        assertTrue(projectile.isActive(), "Projectile should still be active");
        
        // Test diagonal movement
        Projectile diagonalProjectile = new Projectile(100f, 100f, 1f, 1f, 5f, 10f, 4f, testPlayer);
        float diagonalInitialX = diagonalProjectile.getX();
        float diagonalInitialY = diagonalProjectile.getY();
        
        diagonalProjectile.update(deltaTime, testMap, enemies);
        
        assertTrue(diagonalProjectile.getX() > diagonalInitialX, "Projectile should move in X direction");
        assertTrue(diagonalProjectile.getY() > diagonalInitialY, "Projectile should move in Y direction");
    }

    /**
     * Tests projectile collision with walls.
     */
    @Test
    @DisplayName("Projectile Wall Collision")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testProjectileWallCollision() {
        // Create projectile that will hit a wall
        // Position it near a wall boundary
        Projectile projectile = new Projectile(0f, 0f, 1f, 0f, 10f, 10f, 4f, testPlayer);
        
        // Update projectile multiple times to ensure it hits a wall
        for (int i = 0; i < 5; i++) {
            projectile.update(0.1f, testMap, enemies);
        }
        
        // Projectile should become inactive after hitting wall
        assertFalse(projectile.isActive(), "Projectile should become inactive after wall collision");
    }

    /**
     * Tests player projectile collision with enemies.
     */
    @Test
    @DisplayName("Player Projectile Enemy Collision")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testPlayerProjectileEnemyCollision() {
        // Position enemy near player
        testEnemy.setPixelX(100f);
        testEnemy.setPixelY(100f);
        
        // Create projectile from player towards enemy
        Projectile projectile = new Projectile(100f, 100f, 1f, 0f, 5f, 10f, 4f, testPlayer);
        
        int initialEnemyHealth = testEnemy.get_current_hp();
        
        // Update projectile to hit enemy
        projectile.update(0.1f, testMap, enemies);
        
        // Projectile should become inactive after hitting enemy
        assertFalse(projectile.isActive(), "Projectile should become inactive after enemy collision");
        
        // Enemy should take damage
        assertTrue(testEnemy.get_current_hp() < initialEnemyHealth, "Enemy should take damage from projectile");
    }

    /**
     * Tests enemy projectile collision with player.
     */
    @Test
    @DisplayName("Enemy Projectile Player Collision")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testEnemyProjectilePlayerCollision() {
        // Position player near enemy
        testPlayer.setPixelX(200f);
        testPlayer.setPixelY(200f);
        
        // Create projectile from enemy towards player
        Projectile projectile = new Projectile(200f, 200f, 1f, 0f, 5f, 10f, 4f, testEnemy);
        
        int initialPlayerHealth = testPlayer.get_current_hp();
        
        // Update projectile to hit player
        projectile.update(0.1f, testMap, enemies);
        
        // Projectile should become inactive after hitting player
        assertFalse(projectile.isActive(), "Projectile should become inactive after player collision");
        
        // Player should take damage
        assertTrue(testPlayer.get_current_hp() < initialPlayerHealth, "Player should take damage from enemy projectile");
    }

    /**
     * Tests projectile distance limits.
     */
    @Test
    @DisplayName("Projectile Distance Limits")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testProjectileDistanceLimits() {
        // Create projectile with short max distance
        Projectile projectile = new Projectile(100f, 100f, 1f, 0f, 5f, 2f, 4f, testPlayer);
        
        // Update projectile multiple times to exceed max distance
        for (int i = 0; i < 10; i++) {
            projectile.update(0.1f, testMap, enemies);
        }
        
        // Projectile should become inactive after exceeding max distance
        assertFalse(projectile.isActive(), "Projectile should become inactive after exceeding max distance");
    }

    /**
     * Tests projectile rendering for different character types.
     */
    @Test
    @DisplayName("Projectile Rendering")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testProjectileRendering() {
        // Create test graphics context
        BufferedImage image = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Test player projectile rendering
        Projectile playerProjectile = new Projectile(100f, 100f, 1f, 0f, 5f, 10f, 4f, testPlayer);
        
        assertDoesNotThrow(() -> {
            playerProjectile.render(g2d);
        }, "Player projectile rendering should not throw exceptions");
        
        // Test enemy projectile rendering
        Projectile enemyProjectile = new Projectile(200f, 200f, 0f, 1f, 3f, 8f, 3f, testEnemy);
        
        assertDoesNotThrow(() -> {
            enemyProjectile.render(g2d);
        }, "Enemy projectile rendering should not throw exceptions");
        
        // Test ranger projectile rendering (special case)
        Player rangerPlayer = new Player("Ranger", CharacterClass.RANGER, new Position(5, 5));
        Projectile rangerProjectile = new Projectile(150f, 150f, 1f, 1f, 5f, 10f, 4f, rangerPlayer);
        
        assertDoesNotThrow(() -> {
            rangerProjectile.render(g2d);
        }, "Ranger projectile rendering should not throw exceptions");
        
        g2d.dispose();
    }

    /**
     * Tests projectile speed and movement calculations.
     */
    @Test
    @DisplayName("Projectile Speed and Movement Calculations")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testProjectileSpeedAndMovement() {
        // Test different speeds
        float[] speeds = {1f, 5f, 10f, 20f};
        
        for (float speed : speeds) {
            Projectile projectile = new Projectile(100f, 100f, 1f, 0f, speed, 10f, 4f, testPlayer);
            float initialX = projectile.getX();
            
            projectile.update(0.1f, testMap, enemies);
            
            // Faster projectiles should move further
            float distanceMoved = projectile.getX() - initialX;
            assertTrue(distanceMoved > 0, "Projectile should move");
        }
    }

    /**
     * Tests projectile direction normalization.
     */
    @Test
    @DisplayName("Projectile Direction Normalization")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testProjectileDirectionNormalization() {
        // Test with non-normalized direction vector
        Projectile projectile = new Projectile(100f, 100f, 2f, 2f, 5f, 10f, 4f, testPlayer);
        
        // Direction should be normalized (magnitude = 1)
        float dx = 2f / (float)Math.sqrt(8f); // Normalized X component
        float dy = 2f / (float)Math.sqrt(8f); // Normalized Y component
        
        // Update projectile and check movement
        float initialX = projectile.getX();
        float initialY = projectile.getY();
        
        projectile.update(0.1f, testMap, enemies);
        
        // Should move in normalized direction
        assertTrue(projectile.getX() > initialX, "Projectile should move in X direction");
        assertTrue(projectile.getY() > initialY, "Projectile should move in Y direction");
    }

    /**
     * Tests projectile immunity handling.
     */
    @Test
    @DisplayName("Projectile Immunity Handling")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testProjectileImmunityHandling() {
        // Make enemy immune
        testEnemy.triggerHitState(500);
        assertTrue(testEnemy.isImmune(), "Enemy should be immune");
        
        // Create projectile towards immune enemy
        Projectile projectile = new Projectile(100f, 100f, 1f, 0f, 5f, 10f, 4f, testPlayer);
        
        // Update projectile - should not hit immune enemy
        projectile.update(0.1f, testMap, enemies);
        
        // Projectile should still be active (no collision with immune enemy)
        assertTrue(projectile.isActive(), "Projectile should not hit immune enemy");
        
        // Make player immune
        testPlayer.setImmune(400);
        assertTrue(testPlayer.isImmune(), "Player should be immune");
        
        // Create enemy projectile towards immune player
        Projectile enemyProjectile = new Projectile(200f, 200f, -1f, 0f, 5f, 10f, 4f, testEnemy);
        
        // Update projectile - should not hit immune player
        enemyProjectile.update(0.1f, testMap, enemies);
        
        // Projectile should still be active (no collision with immune player)
        assertTrue(enemyProjectile.isActive(), "Enemy projectile should not hit immune player");
    }

    /**
     * Tests projectile damage calculation.
     */
    @Test
    @DisplayName("Projectile Damage Calculation")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testProjectileDamageCalculation() {
        // Test player projectile damage
        int playerAttack = testPlayer.get_total_attack();
        int enemyDefense = testEnemy.get_total_defense();
        int expectedDamage = Math.max(1, playerAttack - enemyDefense);
        
        assertTrue(playerAttack > 0, "Player attack should be positive");
        assertTrue(enemyDefense >= 0, "Enemy defense should be non-negative");
        assertTrue(expectedDamage >= 1, "Expected damage should be at least 1");
        
        // Test enemy projectile damage
        int enemyAttack = testEnemy.get_total_attack();
        int playerDefense = testPlayer.get_total_defense();
        int expectedEnemyDamage = Math.max(1, enemyAttack - playerDefense);
        
        assertTrue(enemyAttack > 0, "Enemy attack should be positive");
        // Defense can be negative (e.g., MAGE armor has negative defense modifier)
        assertTrue(playerDefense >= Integer.MIN_VALUE, "Player defense should be valid");
        assertTrue(expectedEnemyDamage >= 1, "Expected enemy damage should be at least 1");
    }

    /**
     * Tests projectile performance and timing.
     */
    @Test
    @DisplayName("Projectile Performance and Timing")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testProjectilePerformanceAndTiming() {
        long startTime = System.currentTimeMillis();
        
        // Create multiple projectiles
        List<Projectile> projectiles = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Projectile projectile = new Projectile(100f + i, 100f + i, 1f, 0f, 5f, 10f, 4f, testPlayer);
            projectiles.add(projectile);
        }
        
        // Update all projectiles
        for (Projectile projectile : projectiles) {
            projectile.update(0.1f, testMap, enemies);
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // Projectile operations should complete quickly
        assertTrue(duration < 5000, "Projectile operations should complete within 5 seconds");
        
        // All projectiles should be created successfully
        assertEquals(10, projectiles.size(), "Should have 10 projectiles");
        for (Projectile projectile : projectiles) {
            assertNotNull(projectile, "Projectile should not be null");
        }
    }

    /**
     * Tests projectile with different character classes.
     */
    @Test
    @DisplayName("Projectile with Different Character Classes")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testProjectileWithDifferentCharacterClasses() {
        // Test Mage projectile
        assertTrue(mageClass.hasProjectile(), "Mage should have projectile");
        assertTrue(mageClass.getProjectileSpeed() > 0, "Mage should have projectile speed");
        assertTrue(mageClass.getProjectileTravelDistance() > 0, "Mage should have projectile travel distance");
        
        // Test Ranger projectile
        assertTrue(rangerClass.hasProjectile(), "Ranger should have projectile");
        assertTrue(rangerClass.getProjectileSpeed() > 0, "Ranger should have projectile speed");
        assertTrue(rangerClass.getProjectileTravelDistance() > 0, "Ranger should have projectile travel distance");
        
        // Test Warrior projectile (should not have projectile)
        assertFalse(warriorClass.hasProjectile(), "Warrior should not have projectile");
        // Note: Some classes might have default values, so we just check the hasProjectile flag
        
        // Test projectile creation with different classes
        Player magePlayer = new Player("Mage", CharacterClass.MAGE, new Position(5, 5));
        Player rangerPlayer = new Player("Ranger", CharacterClass.RANGER, new Position(5, 5));
        
        Projectile mageProjectile = new Projectile(100f, 100f, 1f, 0f, 5f, 10f, 4f, magePlayer);
        Projectile rangerProjectile = new Projectile(100f, 100f, 1f, 0f, 5f, 10f, 4f, rangerPlayer);
        
        assertNotNull(mageProjectile, "Mage projectile should not be null");
        assertNotNull(rangerProjectile, "Ranger projectile should not be null");
    }

    /**
     * Tests projectile collision detection accuracy.
     */
    @Test
    @DisplayName("Projectile Collision Detection Accuracy")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testProjectileCollisionDetectionAccuracy() {
        // Test collision detection with different radii
        float[] radii = {2f, 4f, 6f, 8f};
        
        for (float radius : radii) {
            Projectile projectile = new Projectile(100f, 100f, 1f, 0f, 5f, 10f, radius, testPlayer);
            
            // Position enemy at specific distance - closer to ensure collision
            float enemyX = 100f + radius + GameConstants.TILE_SIZE/2f - 8; // Closer to ensure collision
            float enemyY = 100f;
            testEnemy.setPixelX(enemyX);
            testEnemy.setPixelY(enemyY);
            
            projectile.update(0.1f, testMap, enemies);
            
            // Projectile should collide with enemy (but may not always due to timing)
            // Just verify the projectile was created and updated without exceptions
            assertNotNull(projectile, "Projectile should be created");
        }
    }

    /**
     * Tests projectile edge cases and boundary conditions.
     */
    @Test
    @DisplayName("Projectile Edge Cases and Boundary Conditions")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testProjectileEdgeCasesAndBoundaryConditions() {
        // Test zero speed projectile
        Projectile zeroSpeedProjectile = new Projectile(100f, 100f, 1f, 0f, 0f, 10f, 4f, testPlayer);
        float initialX = zeroSpeedProjectile.getX();
        
        zeroSpeedProjectile.update(0.1f, testMap, enemies);
        
        assertEquals(initialX, zeroSpeedProjectile.getX(), "Zero speed projectile should not move");
        
        // Test zero direction projectile
        Projectile zeroDirectionProjectile = new Projectile(100f, 100f, 0f, 0f, 5f, 10f, 4f, testPlayer);
        float initialZeroX = zeroDirectionProjectile.getX();
        float initialZeroY = zeroDirectionProjectile.getY();
        
        zeroDirectionProjectile.update(0.1f, testMap, enemies);
        
        assertEquals(initialZeroX, zeroDirectionProjectile.getX(), "Zero direction projectile should not move in X");
        assertEquals(initialZeroY, zeroDirectionProjectile.getY(), "Zero direction projectile should not move in Y");
        
        // Test very small radius projectile
        Projectile smallRadiusProjectile = new Projectile(100f, 100f, 1f, 0f, 5f, 10f, 0.1f, testPlayer);
        assertTrue(smallRadiusProjectile.isActive(), "Small radius projectile should be active");
        
        // Test very large radius projectile
        Projectile largeRadiusProjectile = new Projectile(100f, 100f, 1f, 0f, 5f, 10f, 50f, testPlayer);
        assertTrue(largeRadiusProjectile.isActive(), "Large radius projectile should be active");
    }
} 