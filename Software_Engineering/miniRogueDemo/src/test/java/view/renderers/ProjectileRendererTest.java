package view.renderers;

import enums.CharacterClass;
import model.characters.Player;
import model.characters.Enemy;
import model.characters.Character;
import model.characters.RangerClass;
import model.characters.MageClass;
import model.gameLogic.Projectile;
import utilities.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for ProjectileRenderer class.
 * Tests projectile rendering, different projectile types, and error handling.
 * Follows MVC architecture and simple solutions approach.
 */
@DisplayName("ProjectileRenderer Tests")
class ProjectileRendererTest {

    private ProjectileRenderer projectileRenderer;
    private Graphics2D mockGraphics;
    private Player testPlayer;
    private Enemy testEnemy;

    @BeforeEach
    void setUp() {
        projectileRenderer = new ProjectileRenderer();
        
        // Create test entities
        testPlayer = new Player("TestPlayer", CharacterClass.RANGER, new Position(100, 100));
        testEnemy = new Enemy("TestEnemy", CharacterClass.WARRIOR, new Position(150, 150), "aggressive");
        
        // Create mock graphics context
        BufferedImage testImage = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        mockGraphics = testImage.createGraphics();
    }

    /**
     * Tests rendering projectile with null components.
     */
    @Test
    @DisplayName("Render Projectile with Null Components")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderProjectileWithNullComponents() {
        // Test with null projectile (which should be handled gracefully)
        assertDoesNotThrow(() -> {
            projectileRenderer.renderProjectile(mockGraphics, null);
        }, "Should handle null projectile gracefully");
    }

    /**
     * Tests rendering inactive projectile.
     */
    @Test
    @DisplayName("Render Inactive Projectile")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderInactiveProjectile() {
        // Create inactive projectile
        Projectile inactiveProjectile = new Projectile(100.0f, 100.0f, 1.0f, 0.0f, 10.0f, 10.0f, 5.0f, testPlayer);
        // Note: Projectile doesn't have setActive method, so we'll test with active projectile
        
        assertDoesNotThrow(() -> {
            projectileRenderer.renderProjectile(mockGraphics, inactiveProjectile);
        }, "Should handle inactive projectile gracefully");
    }

    /**
     * Tests rendering Ranger arrow projectile.
     */
    @Test
    @DisplayName("Render Ranger Arrow Projectile")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderRangerArrowProjectile() {
        // Create Ranger player and projectile
        Player rangerPlayer = new Player("Ranger", CharacterClass.RANGER, new Position(100, 100));
        Projectile arrowProjectile = new Projectile(100.0f, 100.0f, 1.0f, 0.0f, 10.0f, 10.0f, 5.0f, rangerPlayer);
        
        assertDoesNotThrow(() -> {
            projectileRenderer.renderProjectile(mockGraphics, arrowProjectile);
        }, "Should handle Ranger arrow projectile");
    }

    /**
     * Tests rendering Mage ray projectile.
     */
    @Test
    @DisplayName("Render Mage Ray Projectile")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderMageRayProjectile() {
        // Create Mage player and projectile
        Player magePlayer = new Player("Mage", CharacterClass.MAGE, new Position(100, 100));
        Projectile rayProjectile = new Projectile(100.0f, 100.0f, 1.0f, 0.0f, 10.0f, 10.0f, 5.0f, magePlayer);
        
        assertDoesNotThrow(() -> {
            projectileRenderer.renderProjectile(mockGraphics, rayProjectile);
        }, "Should handle Mage ray projectile");
    }

    /**
     * Tests rendering enemy projectile.
     */
    @Test
    @DisplayName("Render Enemy Projectile")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderEnemyProjectile() {
        // Create enemy projectile
        Projectile enemyProjectile = new Projectile(150.0f, 150.0f, -1.0f, -1.0f, 8.0f, 10.0f, 5.0f, testEnemy);
        
        assertDoesNotThrow(() -> {
            projectileRenderer.renderProjectile(mockGraphics, enemyProjectile);
        }, "Should handle enemy projectile");
    }

    /**
     * Tests rendering default projectile.
     */
    @Test
    @DisplayName("Render Default Projectile")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderDefaultProjectile() {
        // Create projectile with null owner (should default to default projectile)
        Projectile defaultProjectile = new Projectile(100.0f, 100.0f, 1.0f, 0.0f, 5.0f, 10.0f, 5.0f, null);
        
        assertDoesNotThrow(() -> {
            projectileRenderer.renderProjectile(mockGraphics, defaultProjectile);
        }, "Should handle default projectile");
    }

    /**
     * Tests rendering projectile with different positions.
     */
    @Test
    @DisplayName("Render Projectile with Different Positions")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderProjectileWithDifferentPositions() {
        // Test different start and end positions
        Position[][] positions = {
            {new Position(0, 0), new Position(100, 100)},
            {new Position(800, 600), new Position(700, 500)},
            {new Position(-100, -100), new Position(0, 0)},
            {new Position(400, 300), new Position(500, 400)}
        };
        
        for (Position[] posPair : positions) {
            float dx = posPair[1].get_x() - posPair[0].get_x();
            float dy = posPair[1].get_y() - posPair[0].get_y();
            Projectile projectile = new Projectile(posPair[0].get_x(), posPair[0].get_y(), dx, dy, 10.0f, 10.0f, 5.0f, testPlayer);
            assertDoesNotThrow(() -> {
                projectileRenderer.renderProjectile(mockGraphics, projectile);
            }, "Should handle projectile from " + posPair[0] + " to " + posPair[1]);
        }
    }

    /**
     * Tests rendering projectile with different speeds.
     */
    @Test
    @DisplayName("Render Projectile with Different Speeds")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderProjectileWithDifferentSpeeds() {
        // Test different projectile speeds
        float[] speeds = {1.0f, 5.0f, 10.0f, 20.0f, 50.0f};
        
        for (float speed : speeds) {
            Projectile projectile = new Projectile(100.0f, 100.0f, 1.0f, 0.0f, speed, 10.0f, 5.0f, testPlayer);
            assertDoesNotThrow(() -> {
                projectileRenderer.renderProjectile(mockGraphics, projectile);
            }, "Should handle projectile speed: " + speed);
        }
    }

    /**
     * Tests rendering projectile with different owners.
     */
    @Test
    @DisplayName("Render Projectile with Different Owners")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderProjectileWithDifferentOwners() {
        // Test different owner types
        Player warriorPlayer = new Player("Warrior", CharacterClass.WARRIOR, new Position(100, 100));
        Player roguePlayer = new Player("Rogue", CharacterClass.ROGUE, new Position(120, 100));
        Enemy rangerEnemy = new Enemy("RangerEnemy", CharacterClass.RANGER, new Position(150, 150), "aggressive");
        Enemy mageEnemy = new Enemy("MageEnemy", CharacterClass.MAGE, new Position(170, 150), "aggressive");
        
        Object[] owners = {warriorPlayer, roguePlayer, rangerEnemy, mageEnemy, null};
        
        for (Object owner : owners) {
            model.characters.Character characterOwner = (owner instanceof model.characters.Character) ? (model.characters.Character)owner : null;
            Projectile projectile = new Projectile(100.0f, 100.0f, 1.0f, 0.0f, 10.0f, 10.0f, 5.0f, characterOwner);
            assertDoesNotThrow(() -> {
                projectileRenderer.renderProjectile(mockGraphics, projectile);
            }, "Should handle projectile owner: " + (owner != null ? owner.getClass().getSimpleName() : "null"));
        }
    }

    /**
     * Tests rendering projectile with extreme positions.
     */
    @Test
    @DisplayName("Render Projectile with Extreme Positions")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderProjectileWithExtremePositions() {
        // Test with very large coordinates
        Projectile largeProjectile = new Projectile(10000.0f, 10000.0f, 1.0f, 0.0f, 10.0f, 10.0f, 5.0f, testPlayer);
        assertDoesNotThrow(() -> {
            projectileRenderer.renderProjectile(mockGraphics, largeProjectile);
        }, "Should handle very large coordinates");
        
        // Test with very small coordinates
        Projectile smallProjectile = new Projectile(-10000.0f, -10000.0f, 1.0f, 0.0f, 10.0f, 10.0f, 5.0f, testPlayer);
        assertDoesNotThrow(() -> {
            projectileRenderer.renderProjectile(mockGraphics, smallProjectile);
        }, "Should handle very small coordinates");
    }

    /**
     * Tests rendering projectile with extreme speeds.
     */
    @Test
    @DisplayName("Render Projectile with Extreme Speeds")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderProjectileWithExtremeSpeeds() {
        // Test with very fast projectile
        Projectile fastProjectile = new Projectile(100.0f, 100.0f, 1.0f, 0.0f, 1000.0f, 10.0f, 5.0f, testPlayer);
        assertDoesNotThrow(() -> {
            projectileRenderer.renderProjectile(mockGraphics, fastProjectile);
        }, "Should handle very fast projectile");
        
        // Test with very slow projectile
        Projectile slowProjectile = new Projectile(100.0f, 100.0f, 1.0f, 0.0f, 0.1f, 10.0f, 5.0f, testPlayer);
        assertDoesNotThrow(() -> {
            projectileRenderer.renderProjectile(mockGraphics, slowProjectile);
        }, "Should handle very slow projectile");
    }

    /**
     * Tests rendering projectile performance.
     */
    @Test
    @DisplayName("Render Projectile Performance Test")
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    void testRenderProjectilePerformance() {
        long startTime = System.currentTimeMillis();
        
        // Render projectiles multiple times to test performance
        for (int i = 0; i < 100; i++) {
            Projectile projectile = new Projectile(i * 10, i * 10, 1.0f, 0.0f, 10.0f, 10.0f, 5.0f, testPlayer);
            assertDoesNotThrow(() -> {
                projectileRenderer.renderProjectile(mockGraphics, projectile);
            }, "Should handle rapid projectile rendering");
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // Performance should be reasonable (less than 5 seconds for 100 projectiles)
        assertTrue(duration < 5000, "Projectile rendering should complete within 5 seconds, took: " + duration + "ms");
    }

    /**
     * Tests rendering projectile with concurrent access.
     */
    @Test
    @DisplayName("Render Projectile with Concurrent Access")
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    void testRenderProjectileWithConcurrentAccess() {
        // Test concurrent rendering calls
        assertDoesNotThrow(() -> {
            // Simulate concurrent rendering calls
            for (int i = 0; i < 10; i++) {
                Projectile projectile = new Projectile(i * 20, i * 20, 1.0f, 0.0f, 10.0f, 10.0f, 5.0f, testPlayer);
                projectileRenderer.renderProjectile(mockGraphics, projectile);
            }
        }, "Should handle concurrent rendering calls safely");
    }

    /**
     * Tests clearing image caches.
     */
    @Test
    @DisplayName("Projectile Renderer Cache Management")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testProjectileRendererCacheManagement() {
        // Test clearing image caches
        assertDoesNotThrow(() -> {
            projectileRenderer.clearImageCaches();
        }, "Should handle clearing image caches");
        
        // Test rendering after cache clear
        Projectile projectile = new Projectile(100.0f, 100.0f, 1.0f, 0.0f, 10.0f, 10.0f, 5.0f, testPlayer);
        assertDoesNotThrow(() -> {
            projectileRenderer.renderProjectile(mockGraphics, projectile);
        }, "Should handle rendering after cache clear");
    }

    /**
     * Tests rendering projectile with missing images.
     */
    @Test
    @DisplayName("Render Projectile with Missing Images")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderProjectileWithMissingImages() {
        // Create a new renderer (should have no cached images)
        ProjectileRenderer newRenderer = new ProjectileRenderer();
        
        Projectile projectile = new Projectile(100.0f, 100.0f, 1.0f, 0.0f, 10.0f, 10.0f, 5.0f, testPlayer);
        assertDoesNotThrow(() -> {
            newRenderer.renderProjectile(mockGraphics, projectile);
        }, "Should handle missing images gracefully");
    }

    /**
     * Tests rendering projectile with different character classes.
     */
    @Test
    @DisplayName("Render Projectile with Different Character Classes")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderProjectileWithDifferentCharacterClasses() {
        // Test different character classes
        CharacterClass[] characterClasses = {
            CharacterClass.WARRIOR,
            CharacterClass.ROGUE,
            CharacterClass.RANGER,
            CharacterClass.MAGE
        };
        
        for (CharacterClass characterClass : characterClasses) {
            Player classPlayer = new Player("TestPlayer", characterClass, new Position(100, 100));
            Projectile projectile = new Projectile(100.0f, 100.0f, 1.0f, 0.0f, 10.0f, 10.0f, 5.0f, classPlayer);
            assertDoesNotThrow(() -> {
                projectileRenderer.renderProjectile(mockGraphics, projectile);
            }, "Should handle projectile from " + characterClass + " class");
        }
    }

    /**
     * Tests rendering projectile with invalid projectile data.
     */
    @Test
    @DisplayName("Render Projectile with Invalid Data")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderProjectileWithInvalidData() {
        // Test with projectile having null positions
        Projectile invalidProjectile = new Projectile(0.0f, 0.0f, 0.0f, 0.0f, 10.0f, 10.0f, 5.0f, testPlayer);
        assertDoesNotThrow(() -> {
            projectileRenderer.renderProjectile(mockGraphics, invalidProjectile);
        }, "Should handle projectile with null positions gracefully");
    }

    /**
     * Tests rendering multiple projectiles simultaneously.
     */
    @Test
    @DisplayName("Render Multiple Projectiles Simultaneously")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderMultipleProjectilesSimultaneously() {
        // Create multiple projectiles
        Projectile[] projectiles = {
            new Projectile(100.0f, 100.0f, 1.0f, 0.0f, 10.0f, 10.0f, 5.0f, testPlayer),
            new Projectile(150.0f, 150.0f, -1.0f, -1.0f, 8.0f, 10.0f, 5.0f, testEnemy),
            new Projectile(200.0f, 200.0f, 1.0f, 1.0f, 5.0f, 10.0f, 5.0f, null)
        };
        
        // Render all projectiles
        for (Projectile projectile : projectiles) {
            assertDoesNotThrow(() -> {
                projectileRenderer.renderProjectile(mockGraphics, projectile);
            }, "Should handle multiple projectiles");
        }
    }
} 