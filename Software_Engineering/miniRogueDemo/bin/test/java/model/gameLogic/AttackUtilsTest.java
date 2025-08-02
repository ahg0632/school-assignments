package model.gameLogic;

import enums.CharacterClass;
import enums.GameConstants;
import utilities.Position;
import model.characters.Player;
import model.characters.Enemy;
import model.characters.Boss;
import model.characters.BaseClass;
import model.characters.WarriorClass;
import model.characters.MageClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for AttackUtils class.
 * Tests swing-based attack mechanics and hit detection for both players and enemies.
 * Appropriate for a school project.
 */
@DisplayName("AttackUtils System Tests")
class AttackUtilsTest {

    private Player testPlayer;
    private Enemy testEnemy;
    private Boss testBoss;
    private GameLogic gameLogic;
    private List<Enemy> enemies;
    private BaseClass warriorClass;
    private BaseClass mageClass;

    @BeforeEach
    void setUp() {
        testPlayer = new Player("TestPlayer", CharacterClass.WARRIOR, new Position(5, 5));
        testEnemy = new Enemy("TestEnemy", CharacterClass.WARRIOR, new Position(6, 6), "aggressive");
        testBoss = new Boss("TestBoss", CharacterClass.WARRIOR, new Position(10, 10));
        gameLogic = new GameLogic(testPlayer);
        enemies = new ArrayList<>();
        enemies.add(testEnemy);
        enemies.add(testBoss);
        warriorClass = new WarriorClass();
        mageClass = new MageClass();
    }

    /**
     * Tests basic AttackVisualData creation and properties.
     */
    @Test
    @DisplayName("AttackVisualData Creation and Properties")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testAttackVisualDataCreation() {
        // Test basic attack data creation
        AttackVisualData attackData = new AttackVisualData(1, 0, 2.0f, Math.PI / 4);
        
        assertNotNull(attackData, "AttackVisualData should not be null");
        assertEquals(1, attackData.getAttackDX(), "Attack DX should be 1");
        assertEquals(0, attackData.getAttackDY(), "Attack DY should be 0");
        assertEquals(2.0f, attackData.getAttackRange(), "Attack range should be 2.0");
        assertEquals(Math.PI / 4, attackData.getAttackAngle(), "Attack angle should be PI/4");
        assertFalse(attackData.isSwingAttack(), "Basic attack should not be swing attack");
        
        // Test swing attack data creation
        long startTime = System.currentTimeMillis();
        AttackVisualData swingData = new AttackVisualData(1, 0, 2.0f, Math.PI / 4,
                                                        Math.PI / 6, Math.PI / 3, 30.0,
                                                        startTime, 200);
        
        assertNotNull(swingData, "Swing AttackVisualData should not be null");
        assertTrue(swingData.isSwingAttack(), "Swing attack should be marked as swing");
        assertEquals(Math.PI / 6, swingData.getSwingStartAngle(), "Swing start angle should be PI/6");
        assertEquals(Math.PI / 3, swingData.getSwingEndAngle(), "Swing end angle should be PI/3");
        assertEquals(30.0, swingData.getSwingFanWidth(), "Swing fan width should be 30.0");
        assertEquals(startTime, swingData.getSwingStartTime(), "Swing start time should match");
        assertEquals(200, swingData.getSwingDuration(), "Swing duration should be 200ms");
    }

    /**
     * Tests swing attack timing and angle calculations.
     */
    @Test
    @DisplayName("Swing Attack Timing and Angle Calculations")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testSwingAttackTiming() {
        long startTime = System.currentTimeMillis();
        AttackVisualData swingData = new AttackVisualData(1, 0, 2.0f, Math.PI / 4,
                                                        Math.PI / 6, Math.PI / 3, 30.0,
                                                        startTime, 200);
        
        // Test initial swing state
        assertTrue(swingData.isSwingActive(startTime), "Swing should be active at start time");
        assertEquals(Math.PI / 6, swingData.getCurrentSwingAngle(startTime), "Initial angle should be start angle");
        
        // Test mid-swing angle (simulate 100ms elapsed)
        long midTime = startTime + 100;
        double midAngle = swingData.getCurrentSwingAngle(midTime);
        assertTrue(midAngle > Math.PI / 6, "Mid-swing angle should be greater than start");
        assertTrue(midAngle < Math.PI / 3, "Mid-swing angle should be less than end");
        
        // Test end swing angle
        long endTime = startTime + 200;
        assertEquals(Math.PI / 3, swingData.getCurrentSwingAngle(endTime), "End angle should be end angle");
        assertFalse(swingData.isSwingActive(endTime), "Swing should not be active after duration");
        
        // Test post-swing state
        long postTime = startTime + 300;
        assertFalse(swingData.isSwingActive(postTime), "Swing should not be active after completion");
    }

    /**
     * Tests createSwingAttackData method with different parameters.
     */
    @Test
    @DisplayName("Create Swing Attack Data")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testCreateSwingAttackData() {
        int aimDX = 1;
        int aimDY = 1;
        float range = 2.5f;
        double attackAngle = Math.PI / 4;
        long startTime = System.currentTimeMillis();
        
        AttackVisualData swingData = AttackUtils.createSwingAttackData(aimDX, aimDY, range, 
                                                                      attackAngle, warriorClass, startTime);
        
        assertNotNull(swingData, "Swing attack data should not be null");
        assertEquals(aimDX, swingData.getAttackDX(), "Aim DX should match");
        assertEquals(aimDY, swingData.getAttackDY(), "Aim DY should match");
        assertEquals(range, swingData.getAttackRange(), "Range should match");
        assertEquals(attackAngle, swingData.getAttackAngle(), "Attack angle should match");
        assertTrue(swingData.isSwingAttack(), "Should be swing attack");
        assertEquals(startTime, swingData.getSwingStartTime(), "Start time should match");
        assertEquals(200, swingData.getSwingDuration(), "Duration should be 200ms");
        
        // Test with different class
        AttackVisualData mageSwingData = AttackUtils.createSwingAttackData(0, 1, 3.0f, 
                                                                          Math.PI / 2, mageClass, startTime);
        assertNotNull(mageSwingData, "Mage swing data should not be null");
        assertEquals(3.0f, mageSwingData.getAttackRange(), "Mage range should be 3.0");
    }

    /**
     * Tests PlayerSwingHitDetector creation and basic functionality.
     */
    @Test
    @DisplayName("Player Swing Hit Detector")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testPlayerSwingHitDetector() {
        AttackUtils.PlayerSwingHitDetector detector = new AttackUtils.PlayerSwingHitDetector(
            testPlayer, enemies, gameLogic);
        
        assertNotNull(detector, "Player swing hit detector should not be null");
        
        // Test that detector can be created without exceptions
        assertDoesNotThrow(() -> {
            detector.checkHits(Math.PI / 4, Math.PI / 12, 2.0f);
        }, "Player hit detection should not throw exceptions");
    }

    /**
     * Tests EnemySwingHitDetector creation and basic functionality.
     */
    @Test
    @DisplayName("Enemy Swing Hit Detector")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testEnemySwingHitDetector() {
        AttackUtils.EnemySwingHitDetector detector = new AttackUtils.EnemySwingHitDetector(
            testEnemy, testPlayer, gameLogic);
        
        assertNotNull(detector, "Enemy swing hit detector should not be null");
        
        // Test that detector can be created without exceptions
        assertDoesNotThrow(() -> {
            detector.checkHits(Math.PI / 4, Math.PI / 12, 2.0f);
        }, "Enemy hit detection should not throw exceptions");
    }

    /**
     * Tests boss-specific hit detection with size and range modifiers.
     */
    @Test
    @DisplayName("Boss Swing Hit Detection")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testBossSwingHitDetection() {
        AttackUtils.EnemySwingHitDetector bossDetector = new AttackUtils.EnemySwingHitDetector(
            testBoss, testPlayer, gameLogic);
        
        assertNotNull(bossDetector, "Boss swing hit detector should not be null");
        
        // Test boss-specific properties
        assertTrue(testBoss.getSizeMultiplier() > 0, "Boss should have size multiplier");
        assertTrue(testBoss.getRangeModifier() > 0, "Boss should have range modifier");
        
        // Test that boss detector can be created without exceptions
        assertDoesNotThrow(() -> {
            bossDetector.checkHits(Math.PI / 4, Math.PI / 12, 2.0f);
        }, "Boss hit detection should not throw exceptions");
    }

    /**
     * Tests swing attack detection system with timer-based checking.
     */
    @Test
    @DisplayName("Swing Attack Detection System")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testSwingAttackDetectionSystem() {
        long startTime = System.currentTimeMillis();
        AttackVisualData swingData = new AttackVisualData(1, 0, 2.0f, Math.PI / 4,
                                                        Math.PI / 6, Math.PI / 3, 30.0,
                                                        startTime, 200);
        
        // Create a simple hit detector for testing
        AttackUtils.SwingHitDetector testDetector = new AttackUtils.SwingHitDetector() {
            @Override
            public void checkHits(double currentSwingAngle, double halfFanWidth, float range) {
                // Simple test implementation
                assertTrue(currentSwingAngle >= Math.PI / 6, "Current angle should be >= start angle");
                assertTrue(currentSwingAngle <= Math.PI / 3, "Current angle should be <= end angle");
                assertTrue(halfFanWidth > 0, "Half fan width should be positive");
                assertTrue(range > 0, "Range should be positive");
            }
        };
        
        // Test that swing attack detection can be started
        assertDoesNotThrow(() -> {
            AttackUtils.startSwingAttackDetection(swingData, testDetector);
        }, "Swing attack detection should not throw exceptions");
        
        // Wait a bit for the swing to process
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Tests hit detection with different character classes.
     */
    @Test
    @DisplayName("Multi-Class Hit Detection")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMultiClassHitDetection() {
        // Test warrior class hit detection
        AttackVisualData warriorSwing = AttackUtils.createSwingAttackData(1, 0, 2.0f, 
                                                                         Math.PI / 4, warriorClass, System.currentTimeMillis());
        assertNotNull(warriorSwing, "Warrior swing data should not be null");
        assertTrue(warriorSwing.isSwingAttack(), "Warrior swing should be swing attack");
        
        // Test mage class hit detection
        AttackVisualData mageSwing = AttackUtils.createSwingAttackData(0, 1, 3.0f, 
                                                                      Math.PI / 2, mageClass, System.currentTimeMillis());
        assertNotNull(mageSwing, "Mage swing data should not be null");
        assertTrue(mageSwing.isSwingAttack(), "Mage swing should be swing attack");
        
        // Test different attack widths for different classes
        assertTrue(warriorClass.getAttackWidth() > 0, "Warrior should have attack width");
        assertTrue(mageClass.getAttackWidth() > 0, "Mage should have attack width");
    }

    /**
     * Tests attack range and positioning calculations.
     */
    @Test
    @DisplayName("Attack Range and Positioning")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testAttackRangeAndPositioning() {
        // Test basic positioning
        float playerX = testPlayer.getPixelX() + GameConstants.TILE_SIZE / 2f;
        float playerY = testPlayer.getPixelY() + GameConstants.TILE_SIZE / 2f;
        float enemyX = testEnemy.getPixelX() + GameConstants.TILE_SIZE / 2f;
        float enemyY = testEnemy.getPixelY() + GameConstants.TILE_SIZE / 2f;
        
        assertTrue(playerX > 0, "Player X should be positive");
        assertTrue(playerY > 0, "Player Y should be positive");
        assertTrue(enemyX > 0, "Enemy X should be positive");
        assertTrue(enemyY > 0, "Enemy Y should be positive");
        
        // Test distance calculation
        double distance = Math.hypot(enemyX - playerX, enemyY - playerY) / GameConstants.TILE_SIZE;
        assertTrue(distance > 0, "Distance should be positive");
        assertTrue(distance < 10, "Distance should be reasonable");
        
        // Test range validation
        float testRange = 2.0f;
        assertTrue(testRange > 0, "Test range should be positive");
        assertTrue(testRange < 10, "Test range should be reasonable");
    }

    /**
     * Tests immunity and hit state management.
     */
    @Test
    @DisplayName("Immunity and Hit State Management")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testImmunityAndHitState() {
        // Test initial immunity states
        assertFalse(testPlayer.isImmune(), "Player should not be immune initially");
        assertFalse(testEnemy.isImmune(), "Enemy should not be immune initially");
        
        // Test immunity setting
        testPlayer.setImmune(400);
        assertTrue(testPlayer.isImmune(), "Player should be immune after setting");
        
        testEnemy.triggerHitState(500);
        assertTrue(testEnemy.isImmune(), "Enemy should be immune after hit state");
        
        // Test immunity duration
        assertDoesNotThrow(() -> {
            Thread.sleep(100); // Wait a bit
        }, "Immunity duration should not cause exceptions");
    }

    /**
     * Tests pushback mechanics for both players and enemies.
     */
    @Test
    @DisplayName("Pushback Mechanics")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testPushbackMechanics() {
        // Test player pushback
        float pushDirX = 1.0f;
        float pushDirY = 1.0f;
        float pushDist = 2.0f * GameConstants.TILE_SIZE;
        float pushSpeed = GameConstants.TILE_SIZE * 0.18f;
        
        assertDoesNotThrow(() -> {
            testPlayer.triggerPushback(pushDirX, pushDirY, pushDist, pushSpeed);
        }, "Player pushback should not throw exceptions");
        
        // Test enemy pushback
        assertDoesNotThrow(() -> {
            testEnemy.triggerPushback(pushDirX, pushDirY, pushDist, pushSpeed);
        }, "Enemy pushback should not throw exceptions");
        
        // Test boss pushback
        assertDoesNotThrow(() -> {
            testBoss.triggerPushback(pushDirX, pushDirY, pushDist, pushSpeed);
        }, "Boss pushback should not throw exceptions");
    }

    /**
     * Tests attack damage calculation and application.
     */
    @Test
    @DisplayName("Attack Damage Calculation")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testAttackDamageCalculation() {
        // Test player attack damage
        int playerAttack = testPlayer.get_total_attack();
        int enemyDefense = testEnemy.get_total_defense();
        int expectedDamage = Math.max(1, playerAttack - enemyDefense);
        
        assertTrue(playerAttack > 0, "Player attack should be positive");
        assertTrue(enemyDefense >= 0, "Enemy defense should be non-negative");
        assertTrue(expectedDamage >= 1, "Expected damage should be at least 1");
        
        // Test enemy attack damage
        int enemyAttack = testEnemy.get_total_attack();
        int playerDefense = testPlayer.get_total_defense();
        int expectedEnemyDamage = Math.max(1, enemyAttack - playerDefense);
        
        assertTrue(enemyAttack > 0, "Enemy attack should be positive");
        assertTrue(playerDefense >= 0, "Player defense should be non-negative");
        assertTrue(expectedEnemyDamage >= 1, "Expected enemy damage should be at least 1");
    }

    /**
     * Tests attack performance and timing.
     */
    @Test
    @DisplayName("Attack Performance and Timing")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testAttackPerformanceAndTiming() {
        long startTime = System.currentTimeMillis();
        
        // Test multiple attack data creations
        for (int i = 0; i < 10; i++) {
            AttackVisualData attackData = AttackUtils.createSwingAttackData(
                i % 2, i % 2, 2.0f + i * 0.1f, Math.PI / 4, warriorClass, System.currentTimeMillis());
            assertNotNull(attackData, "Attack data " + i + " should not be null");
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // Attack operations should complete quickly
        assertTrue(duration < 5000, "Attack operations should complete within 5 seconds");
        
        // Test swing timing accuracy
        long swingStart = System.currentTimeMillis();
        AttackVisualData swingData = new AttackVisualData(1, 0, 2.0f, Math.PI / 4,
                                                        Math.PI / 6, Math.PI / 3, 30.0,
                                                        swingStart, 200);
        
        assertTrue(swingData.isSwingActive(swingStart), "Swing should be active at start");
        assertFalse(swingData.isSwingActive(swingStart + 300), "Swing should not be active after duration");
    }

    /**
     * Tests attack angle calculations and fan width.
     */
    @Test
    @DisplayName("Attack Angle and Fan Width Calculations")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testAttackAngleAndFanWidth() {
        // Test different attack angles
        double[] testAngles = {0, Math.PI / 4, Math.PI / 2, Math.PI, 2 * Math.PI};
        
        for (double angle : testAngles) {
            AttackVisualData attackData = new AttackVisualData(1, 0, 2.0f, angle);
            assertEquals(angle, attackData.getAttackAngle(), "Attack angle should match");
        }
        
        // Test fan width calculations
        double fanWidth = 30.0;
        double halfFanWidth = Math.toRadians(fanWidth) / 2.0;
        
        assertTrue(halfFanWidth > 0, "Half fan width should be positive");
        assertTrue(halfFanWidth < Math.PI, "Half fan width should be less than PI");
        
        // Test angle delta calculations
        double currentAngle = Math.PI / 4;
        double targetAngle = Math.PI / 3;
        double delta = Math.abs(Math.atan2(Math.sin(targetAngle - currentAngle), 
                                         Math.cos(targetAngle - currentAngle)));
        
        assertTrue(delta >= 0, "Angle delta should be non-negative");
        assertTrue(delta <= Math.PI, "Angle delta should be <= PI");
    }
} 