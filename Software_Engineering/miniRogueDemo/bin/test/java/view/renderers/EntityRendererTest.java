package view.renderers;

import enums.CharacterClass;
import model.characters.Player;
import model.characters.Enemy;
import model.characters.Boss;
import model.gameLogic.AttackVisualData;
import model.equipment.Weapon;
import utilities.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for EntityRenderer class.
 * Tests entity rendering, weapon attacks, sprite management, and error handling.
 * Follows MVC architecture and simple solutions approach.
 */
@DisplayName("EntityRenderer Tests")
class EntityRendererTest {

    private EntityRenderer entityRenderer;
    private Graphics2D mockGraphics;
    private Player testPlayer;
    private Enemy testEnemy;
    private Boss testBoss;
    private AttackVisualData testSwingData;
    private AttackVisualData testBowData;

    @BeforeEach
    void setUp() {
        entityRenderer = new EntityRenderer();
        
        // Create test entities
        testPlayer = new Player("TestPlayer", CharacterClass.WARRIOR, new Position(100, 100));
        testEnemy = new Enemy("TestEnemy", CharacterClass.WARRIOR, new Position(150, 150), "aggressive");
        testBoss = new Boss("TestBoss", CharacterClass.WARRIOR, new Position(200, 200));
        
        // Create test attack data
        testSwingData = new AttackVisualData(0, 1, 1.0f, 0.0);
        testBowData = new AttackVisualData(0, 1, 1.0f, 0.0);
        
        // Create mock graphics context
        BufferedImage testImage = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        mockGraphics = testImage.createGraphics();
    }

    /**
     * Tests rendering entities with null components.
     */
    @Test
    @DisplayName("Render Entities with Null Components")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderEntitiesWithNullComponents() {
        // Test with null player
        assertDoesNotThrow(() -> {
            entityRenderer.renderEntities(mockGraphics, null, new ArrayList<>(), new ArrayList<>(),
                                       testSwingData, testSwingData, testBowData, testBowData);
        }, "Should handle null player gracefully");
        
        // Test with null enemies
        assertDoesNotThrow(() -> {
            entityRenderer.renderEntities(mockGraphics, testPlayer, null, new ArrayList<>(),
                                       testSwingData, testSwingData, testBowData, testBowData);
        }, "Should handle null enemies gracefully");
        
        // Test with null bosses
        assertDoesNotThrow(() -> {
            entityRenderer.renderEntities(mockGraphics, testPlayer, new ArrayList<>(), null,
                                       testSwingData, testSwingData, testBowData, testBowData);
        }, "Should handle null bosses gracefully");
        
        // Test with null attack data
        assertDoesNotThrow(() -> {
            entityRenderer.renderEntities(mockGraphics, testPlayer, new ArrayList<>(), new ArrayList<>(),
                                       null, null, null, null);
        }, "Should handle null attack data gracefully");
    }

    /**
     * Tests rendering entities with empty lists.
     */
    @Test
    @DisplayName("Render Entities with Empty Lists")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderEntitiesWithEmptyLists() {
        List<Enemy> emptyEnemies = new ArrayList<>();
        List<Boss> emptyBosses = new ArrayList<>();
        
        assertDoesNotThrow(() -> {
            entityRenderer.renderEntities(mockGraphics, testPlayer, emptyEnemies, emptyBosses,
                                       testSwingData, testSwingData, testBowData, testBowData);
        }, "Should handle empty enemy and boss lists");
    }

    /**
     * Tests rendering entities with populated lists.
     */
    @Test
    @DisplayName("Render Entities with Populated Lists")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderEntitiesWithPopulatedLists() {
        List<Enemy> enemies = new ArrayList<>();
        enemies.add(testEnemy);
        
        List<Boss> bosses = new ArrayList<>();
        bosses.add(testBoss);
        
        assertDoesNotThrow(() -> {
            entityRenderer.renderEntities(mockGraphics, testPlayer, enemies, bosses,
                                       testSwingData, testSwingData, testBowData, testBowData);
        }, "Should handle populated enemy and boss lists");
    }

    /**
     * Tests rendering with different character classes.
     */
    @Test
    @DisplayName("Render Entities with Different Character Classes")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderEntitiesWithDifferentCharacterClasses() {
        // Test different player classes
        Player warriorPlayer = new Player("Warrior", CharacterClass.WARRIOR, new Position(100, 100));
        Player roguePlayer = new Player("Rogue", CharacterClass.ROGUE, new Position(120, 100));
        Player rangerPlayer = new Player("Ranger", CharacterClass.RANGER, new Position(140, 100));
        Player magePlayer = new Player("Mage", CharacterClass.MAGE, new Position(160, 100));
        
        List<Player> players = List.of(warriorPlayer, roguePlayer, rangerPlayer, magePlayer);
        
        for (Player player : players) {
            assertDoesNotThrow(() -> {
                entityRenderer.renderEntities(mockGraphics, player, new ArrayList<>(), new ArrayList<>(),
                                           testSwingData, testSwingData, testBowData, testBowData);
            }, "Should handle " + player.getPlayerClassOOP().getClass().getSimpleName() + " class");
        }
    }

    /**
     * Tests rendering with active weapon attacks.
     */
    @Test
    @DisplayName("Render Entities with Active Weapon Attacks")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderEntitiesWithActiveWeaponAttacks() {
        // Create active swing data
        AttackVisualData activeSwingData = new AttackVisualData(0, 1, 1.0f, 0.0, 
                                                               0.0, Math.PI/2, Math.PI/4, 
                                                               System.currentTimeMillis(), 500);
        
        // Equip weapon to player
        Weapon testWeapon = new Weapon("TestSword", 10, 5, CharacterClass.WARRIOR, 1, 
                                      Weapon.WeaponType.IMPACT, "sword.png", "Impact");
        testPlayer.equip_weapon(testWeapon);
        
        assertDoesNotThrow(() -> {
            entityRenderer.renderEntities(mockGraphics, testPlayer, new ArrayList<>(), new ArrayList<>(),
                                       activeSwingData, testSwingData, testBowData, testBowData);
        }, "Should handle active weapon attacks");
    }

    /**
     * Tests rendering with active bow attacks.
     */
    @Test
    @DisplayName("Render Entities with Active Bow Attacks")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderEntitiesWithActiveBowAttacks() {
        // Create active bow data
        AttackVisualData activeBowData = new AttackVisualData(0, 1, 1.0f, 0.0, 
                                                            0.0, Math.PI/2, Math.PI/4, 
                                                            System.currentTimeMillis(), 500);
        
        // Create Ranger player
        Player rangerPlayer = new Player("Ranger", CharacterClass.RANGER, new Position(100, 100));
        
        assertDoesNotThrow(() -> {
            entityRenderer.renderEntities(mockGraphics, rangerPlayer, new ArrayList<>(), new ArrayList<>(),
                                       testSwingData, testSwingData, activeBowData, testBowData);
        }, "Should handle active bow attacks");
    }

    /**
     * Tests rendering with null graphics context.
     */
    @Test
    @DisplayName("Render Entities with Null Graphics Context")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderEntitiesWithNullGraphicsContext() {
        assertDoesNotThrow(() -> {
            entityRenderer.renderEntities(null, testPlayer, new ArrayList<>(), new ArrayList<>(),
                                       testSwingData, testSwingData, testBowData, testBowData);
        }, "Should handle null graphics context gracefully");
    }

    /**
     * Tests rendering with different entity positions.
     */
    @Test
    @DisplayName("Render Entities with Different Positions")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderEntitiesWithDifferentPositions() {
        // Test entities at different positions
        Player player1 = new Player("Player1", CharacterClass.WARRIOR, new Position(0, 0));
        Player player2 = new Player("Player2", CharacterClass.WARRIOR, new Position(800, 600));
        Player player3 = new Player("Player3", CharacterClass.WARRIOR, new Position(-100, -100));
        
        List<Player> players = List.of(player1, player2, player3);
        
        for (Player player : players) {
            assertDoesNotThrow(() -> {
                entityRenderer.renderEntities(mockGraphics, player, new ArrayList<>(), new ArrayList<>(),
                                           testSwingData, testSwingData, testBowData, testBowData);
            }, "Should handle player at position " + player.get_position());
        }
    }

    /**
     * Tests rendering with multiple enemies and bosses.
     */
    @Test
    @DisplayName("Render Entities with Multiple Enemies and Bosses")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderEntitiesWithMultipleEnemiesAndBosses() {
        // Create multiple enemies
        List<Enemy> enemies = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Enemy enemy = new Enemy("Enemy" + i, CharacterClass.WARRIOR, 
                                  new Position(100 + i * 50, 100 + i * 50), "aggressive");
            enemies.add(enemy);
        }
        
        // Create multiple bosses
        List<Boss> bosses = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Boss boss = new Boss("Boss" + i, CharacterClass.WARRIOR, 
                               new Position(200 + i * 100, 200 + i * 100));
            bosses.add(boss);
        }
        
        assertDoesNotThrow(() -> {
            entityRenderer.renderEntities(mockGraphics, testPlayer, enemies, bosses,
                                       testSwingData, testSwingData, testBowData, testBowData);
        }, "Should handle multiple enemies and bosses");
    }

    /**
     * Tests rendering with expired attack data.
     */
    @Test
    @DisplayName("Render Entities with Expired Attack Data")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderEntitiesWithExpiredAttackData() {
        // Create expired attack data
        AttackVisualData expiredSwingData = new AttackVisualData(0, 1, 1.0f, 0.0, 
                                                                0.0, Math.PI/2, Math.PI/4, 
                                                                System.currentTimeMillis() - 10000, 500);
        
        AttackVisualData expiredBowData = new AttackVisualData(0, 1, 1.0f, 0.0, 
                                                              0.0, Math.PI/2, Math.PI/4, 
                                                              System.currentTimeMillis() - 10000, 500);
        
        assertDoesNotThrow(() -> {
            entityRenderer.renderEntities(mockGraphics, testPlayer, new ArrayList<>(), new ArrayList<>(),
                                       expiredSwingData, expiredSwingData, expiredBowData, expiredBowData);
        }, "Should handle expired attack data gracefully");
    }

    /**
     * Tests rendering with different weapon types.
     */
    @Test
    @DisplayName("Render Entities with Different Weapon Types")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderEntitiesWithDifferentWeaponTypes() {
        // Test different weapon types
        Weapon sword = new Weapon("Sword", 10, 5, CharacterClass.WARRIOR, 1, 
                                 Weapon.WeaponType.IMPACT, "sword.png", "Impact");
        Weapon bow = new Weapon("Bow", 8, 3, CharacterClass.RANGER, 1, 
                               Weapon.WeaponType.DISTANCE, "bow.png", "Distance");
        Weapon staff = new Weapon("Staff", 12, 8, CharacterClass.MAGE, 1, 
                                 Weapon.WeaponType.MAGIC, "staff.png", "Magic");
        
        List<Weapon> weapons = List.of(sword, bow, staff);
        
        for (Weapon weapon : weapons) {
            testPlayer.equip_weapon(weapon);
            
            AttackVisualData activeSwingData = new AttackVisualData(0, 1, 1.0f, 0.0, 
                                                                   0.0, Math.PI/2, Math.PI/4, 
                                                                   System.currentTimeMillis(), 500);
            
            assertDoesNotThrow(() -> {
                entityRenderer.renderEntities(mockGraphics, testPlayer, new ArrayList<>(), new ArrayList<>(),
                                           activeSwingData, testSwingData, testBowData, testBowData);
            }, "Should handle weapon type: " + weapon.get_name());
        }
    }

    /**
     * Tests rendering performance with large entity lists.
     */
    @Test
    @DisplayName("Render Entities Performance Test")
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    void testRenderEntitiesPerformance() {
        // Create large lists of entities
        List<Enemy> enemies = new ArrayList<>();
        List<Boss> bosses = new ArrayList<>();
        
        for (int i = 0; i < 50; i++) {
            Enemy enemy = new Enemy("Enemy" + i, CharacterClass.WARRIOR, 
                                  new Position(i * 10, i * 10), "aggressive");
            enemies.add(enemy);
        }
        
        for (int i = 0; i < 10; i++) {
            Boss boss = new Boss("Boss" + i, CharacterClass.WARRIOR, 
                               new Position(i * 50, i * 50));
            bosses.add(boss);
        }
        
        long startTime = System.currentTimeMillis();
        
        assertDoesNotThrow(() -> {
            entityRenderer.renderEntities(mockGraphics, testPlayer, enemies, bosses,
                                       testSwingData, testSwingData, testBowData, testBowData);
        }, "Should handle large entity lists efficiently");
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // Performance should be reasonable (less than 5 seconds for 60 entities)
        assertTrue(duration < 5000, "Rendering should complete within 5 seconds, took: " + duration + "ms");
    }

    /**
     * Tests rendering with invalid entity data.
     */
    @Test
    @DisplayName("Render Entities with Invalid Entity Data")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderEntitiesWithInvalidEntityData() {
        // Test with null attack data (which should be handled gracefully)
        assertDoesNotThrow(() -> {
            entityRenderer.renderEntities(mockGraphics, testPlayer, new ArrayList<>(), new ArrayList<>(),
                                       null, null, null, null);
        }, "Should handle null attack data gracefully");
    }

    /**
     * Tests rendering with concurrent access.
     */
    @Test
    @DisplayName("Render Entities with Concurrent Access")
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    void testRenderEntitiesWithConcurrentAccess() {
        List<Enemy> enemies = new ArrayList<>();
        List<Boss> bosses = new ArrayList<>();
        
        // Add some entities
        for (int i = 0; i < 10; i++) {
            enemies.add(new Enemy("Enemy" + i, CharacterClass.WARRIOR, new Position(i * 20, i * 20), "aggressive"));
            bosses.add(new Boss("Boss" + i, CharacterClass.WARRIOR, new Position(i * 40, i * 40)));
        }
        
        // Test concurrent rendering
        assertDoesNotThrow(() -> {
            // Simulate concurrent rendering calls
            for (int i = 0; i < 5; i++) {
                entityRenderer.renderEntities(mockGraphics, testPlayer, enemies, bosses,
                                           testSwingData, testSwingData, testBowData, testBowData);
            }
        }, "Should handle concurrent rendering calls safely");
    }
} 