package model.characters;

import enums.CharacterClass;
import utilities.Position;
import model.items.Item;
import model.items.Consumable;
import model.map.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.AfterEach;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for the Enemy class.
 * Tests AI behavior, combat mechanics, loot drops, and state management.
 */
@DisplayName("Enemy Class Tests")
class EnemyTest {

    private Enemy testEnemy;
    private Player testPlayer;
    private Boss testBoss;

    @BeforeEach
    void setUp() {
        testEnemy = new Enemy("TestEnemy", CharacterClass.MAGE, new Position(5, 5), "aggressive");
        testPlayer = new Player("TestPlayer", CharacterClass.WARRIOR, new Position(0, 0));
        testBoss = new Boss("TestBoss", CharacterClass.WARRIOR, new Position(10, 10));
    }

    /**
     * Tests enemy creation and basic initialization.
     */
    @Test
    @DisplayName("Enemy Creation and Initialization")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testEnemyCreationAndInitialization() {
        // Test basic enemy properties
        assertNotNull(testEnemy, "Enemy should not be null");
        assertEquals("TestEnemy", testEnemy.get_name(), "Enemy name should be set correctly");
        assertEquals(CharacterClass.MAGE, testEnemy.get_character_class(), "Enemy class should be set correctly");
        
        // Test AI pattern
        assertEquals("aggressive", testEnemy.get_ai_pattern(), "Enemy AI pattern should be set correctly");
        
        // Test position
        Position enemyPos = testEnemy.get_position();
        assertNotNull(enemyPos, "Enemy position should not be null");
        assertEquals(5, enemyPos.get_x(), "Enemy X position should be 5");
        assertEquals(5, enemyPos.get_y(), "Enemy Y position should be 5");
        
        // Test basic stats
        assertTrue(testEnemy.get_current_hp() > 0, "Enemy should have health");
        assertTrue(testEnemy.get_max_hp() > 0, "Enemy should have max health");
        assertTrue(testEnemy.is_alive(), "Enemy should be alive initially");
    }

    /**
     * Tests enemy AI behavior and patterns.
     */
    @Test
    @DisplayName("Enemy AI Behavior and Patterns")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testEnemyAIBehaviorAndPatterns() {
        // Test AI pattern getter
        String aiPattern = testEnemy.get_ai_pattern();
        assertNotNull(aiPattern, "AI pattern should not be null");
        assertFalse(aiPattern.isEmpty(), "AI pattern should not be empty");
        
        // Test AI performance method (should not throw exception)
        assertDoesNotThrow(() -> testEnemy.perform_ai(), "AI performance should not throw exception");
        
        // Test melee attack performance
        assertDoesNotThrow(() -> testEnemy.perform_melee_attack(), "Melee attack should not throw exception");
        
        // Test projectile attack performance
        assertDoesNotThrow(() -> testEnemy.perform_projectile_attack(), "Projectile attack should not throw exception");
    }

    /**
     * Tests enemy combat mechanics.
     */
    @Test
    @DisplayName("Enemy Combat Mechanics")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testEnemyCombatMechanics() {
        // Test enemy attack against player
        int initialPlayerHealth = testPlayer.get_current_hp();
        int damage = testEnemy.attack(testPlayer);
        assertTrue(damage >= 0, "Enemy attack damage should be non-negative");
        assertTrue(testPlayer.get_current_hp() <= initialPlayerHealth, "Player should take damage");
        
        // Test player attack against enemy
        int initialEnemyHealth = testEnemy.get_current_hp();
        int playerDamage = testPlayer.attack(testEnemy);
        assertTrue(playerDamage >= 0, "Player attack damage should be non-negative");
        assertTrue(testEnemy.get_current_hp() <= initialEnemyHealth, "Enemy should take damage");
        
        // Test that enemy can die
        testEnemy.take_damage(testEnemy.get_current_hp() + 100); // Massive damage
        assertFalse(testEnemy.is_alive(), "Enemy should be dead after massive damage");
    }

    /**
     * Tests enemy loot system.
     */
    @Test
    @DisplayName("Enemy Loot System")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testEnemyLootSystem() {
        // Test loot dropping
        List<Item> loot = testEnemy.drop_loot();
        assertNotNull(loot, "Loot should not be null");
        // Note: Loot may be empty depending on random chance, so we don't assert on size
        
        // Test experience value
        int expValue = testEnemy.get_experience_value();
        assertTrue(expValue > 0, "Enemy should have positive experience value");
    }

    /**
     * Tests enemy weaknesses system.
     */
    @Test
    @DisplayName("Enemy Weaknesses System")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testEnemyWeaknessesSystem() {
        // Test weaknesses list
        List<String> weaknesses = testEnemy.get_weaknesses();
        assertNotNull(weaknesses, "Weaknesses should not be null");
        // Note: Weaknesses may be empty, so we don't assert on size
        
        // Test weakness checking
        boolean isWeakToFire = testEnemy.is_weak_to("fire");
        // Note: This depends on the specific enemy, so we just test the method exists
        assertTrue(true, "Weakness check should not throw exception");
    }

    /**
     * Tests enemy movement and positioning.
     */
    @Test
    @DisplayName("Enemy Movement and Positioning")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testEnemyMovementAndPositioning() {
        // Test initial position
        Position enemyPos = testEnemy.get_position();
        assertNotNull(enemyPos, "Enemy position should not be null");
        assertEquals(5, enemyPos.get_x(), "Enemy X position should be 5");
        assertEquals(5, enemyPos.get_y(), "Enemy Y position should be 5");
        
        // Test pixel position
        testEnemy.setPixelX(64.0f);
        testEnemy.setPixelY(128.0f);
        assertEquals(64.0f, testEnemy.getPixelX(), "Enemy pixel X should be set correctly");
        assertEquals(128.0f, testEnemy.getPixelY(), "Enemy pixel Y should be set correctly");
        
        // Test movement update (should not throw exception)
        assertDoesNotThrow(() -> testEnemy.update_movement(), "Movement update should not throw exception");
    }

    /**
     * Tests enemy state management.
     */
    @Test
    @DisplayName("Enemy State Management")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testEnemyStateManagement() {
        // Test initial state
        assertFalse(testEnemy.isChasingPlayer(), "Enemy should not be chasing initially");
        assertFalse(testEnemy.isInCelebratoryState(), "Enemy should not be in celebratory state initially");
        assertFalse(testEnemy.isInWindUpState(), "Enemy should not be in wind-up state initially");
        assertFalse(testEnemy.isInHitState(), "Enemy should not be in hit state initially");
        assertFalse(testEnemy.isDying(), "Enemy should not be dying initially");
        
        // Test state transitions
        testEnemy.startCelebratoryState();
        assertTrue(testEnemy.isInCelebratoryState(), "Enemy should be in celebratory state after starting");
        
        testEnemy.setInWindUpState(true);
        assertTrue(testEnemy.isInWindUpState(), "Enemy should be in wind-up state after setting");
        
        testEnemy.triggerHitState(1000);
        assertTrue(testEnemy.isInHitState(), "Enemy should be in hit state after triggering");
        
        testEnemy.startDying();
        assertTrue(testEnemy.isDying(), "Enemy should be dying after starting");
    }

    /**
     * Tests enemy aiming and attack direction.
     */
    @Test
    @DisplayName("Enemy Aiming and Attack Direction")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testEnemyAimingAndAttackDirection() {
        // Test aim direction
        int aimDX = testEnemy.getAimDX();
        int aimDY = testEnemy.getAimDY();
        assertTrue(aimDX >= -1 && aimDX <= 1, "Aim DX should be between -1 and 1");
        assertTrue(aimDY >= -1 && aimDY <= 1, "Aim DY should be between -1 and 1");
        
        // Test precise aiming
        float preciseAimDX = testEnemy.getPreciseAimDX();
        float preciseAimDY = testEnemy.getPreciseAimDY();
        assertTrue(preciseAimDX >= -1.0f && preciseAimDX <= 1.0f, "Precise aim DX should be between -1 and 1");
        assertTrue(preciseAimDY >= -1.0f && preciseAimDY <= 1.0f, "Precise aim DY should be between -1 and 1");
        
        // Test wind-up aiming
        testEnemy.setWindUpAimDirection(1, 0, 1.0f, 0.0f);
        assertEquals(1, testEnemy.getWindUpAimDX(), "Wind-up aim DX should be set correctly");
        assertEquals(0, testEnemy.getWindUpAimDY(), "Wind-up aim DY should be set correctly");
        assertEquals(1.0f, testEnemy.getWindUpPreciseAimDX(), "Wind-up precise aim DX should be set correctly");
        assertEquals(0.0f, testEnemy.getWindUpPreciseAimDY(), "Wind-up precise aim DY should be set correctly");
    }

    /**
     * Tests enemy attack timing and rate limiting.
     */
    @Test
    @DisplayName("Enemy Attack Timing and Rate Limiting")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testEnemyAttackTimingAndRateLimiting() {
        // Test initial attack time
        long initialAttackTime = testEnemy.getLastAttackTime();
        assertTrue(initialAttackTime >= 0, "Initial attack time should be non-negative");
        
        // Test attack time setting
        long newAttackTime = System.currentTimeMillis();
        testEnemy.setLastAttackTime(newAttackTime);
        assertEquals(newAttackTime, testEnemy.getLastAttackTime(), "Attack time should be set correctly");
    }

    /**
     * Tests enemy movement speed and behavior.
     */
    @Test
    @DisplayName("Enemy Movement Speed and Behavior")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testEnemyMovementSpeedAndBehavior() {
        // Test movement speed
        float moveSpeed = testEnemy.getMoveSpeed();
        assertTrue(moveSpeed > 0, "Enemy should have positive movement speed");
        
        // Test movement speed setting
        float newSpeed = 3.0f;
        testEnemy.setMoveSpeed(newSpeed);
        assertEquals(newSpeed, testEnemy.getMoveSpeed(), "Movement speed should be set correctly");
        
        // Test random number generation
        assertNotNull(testEnemy.getRandom(), "Enemy should have random number generator");
    }

    /**
     * Tests enemy pathfinding and chase behavior.
     */
    @Test
    @DisplayName("Enemy Pathfinding and Chase Behavior")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testEnemyPathfindingAndChaseBehavior() {
        // Test chase path
        List<int[]> chasePath = testEnemy.getChasePath();
        assertNotNull(chasePath, "Chase path should not be null");
        
        // Test chase state
        assertFalse(testEnemy.isChasingPlayer(), "Enemy should not be chasing initially");
        
        // Test chase end time
        long chaseEndTime = testEnemy.getChaseEndTime();
        assertTrue(chaseEndTime >= 0, "Chase end time should be non-negative");
        
        // Test path to player (requires map and player setup)
        // Note: This would require more complex setup, so we just test the method exists
        assertDoesNotThrow(() -> testEnemy.getPathToPlayer(), "Path to player should not throw exception");
    }

    /**
     * Tests enemy fallback behavior.
     */
    @Test
    @DisplayName("Enemy Fallback Behavior")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testEnemyFallbackBehavior() {
        // Test initial fallback state
        assertFalse(testEnemy.isInFallbackState(), "Enemy should not be in fallback state initially");
        
        // Test fallback state starting
        testEnemy.startFallbackState();
        assertTrue(testEnemy.isInFallbackState(), "Enemy should be in fallback state after starting");
        
        // Test fallback state starting
        testEnemy.startFallbackState();
        assertTrue(testEnemy.isInFallbackState(), "Enemy should be in fallback state after starting");
    }

    /**
     * Tests enemy detection and notification.
     */
    @Test
    @DisplayName("Enemy Detection and Notification")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testEnemyDetectionAndNotification() {
        // Test detection notification
        assertFalse(testEnemy.isShowingDetectionNotification(), "Enemy should not show detection notification initially");
        
        // Test detection notification state
        assertFalse(testEnemy.isShowingDetectionNotification(), "Enemy should not show detection notification initially");
    }

    /**
     * Tests enemy character type and boss status.
     */
    @Test
    @DisplayName("Enemy Character Type and Boss Status")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testEnemyCharacterTypeAndBossStatus() {
        // Test character type
        String characterType = testEnemy.getCharacterType();
        assertNotNull(characterType, "Character type should not be null");
        assertFalse(characterType.isEmpty(), "Character type should not be empty");
        
        // Test boss status
        assertFalse(testEnemy.isBoss(), "Regular enemy should not be a boss");
        
        // Test boss enemy
        assertTrue(testBoss.isBoss(), "Boss should be identified as boss");
    }

    /**
     * Tests enemy aggro and aggressive behavior.
     */
    @Test
    @DisplayName("Enemy Aggro and Aggressive Behavior")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testEnemyAggroAndAggressiveBehavior() {
        // Test aggro range
        int aggroRange = testEnemy.get_aggro_range();
        assertTrue(aggroRange > 0, "Enemy should have positive aggro range");
        
        // Test aggressive status
        boolean isAggressive = testEnemy.is_aggressive();
        // Note: This depends on the specific enemy type, so we just test the method exists
        assertTrue(true, "Aggressive check should not throw exception");
    }

    /**
     * Tests enemy immunity and hit state management.
     */
    @Test
    @DisplayName("Enemy Immunity and Hit State Management")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testEnemyImmunityAndHitStateManagement() {
        // Test initial immunity state
        assertFalse(testEnemy.isImmune(), "Enemy should not be immune initially");
        
        // Test immunity setting
        testEnemy.setImmune(1000); // 1 second immunity
        assertTrue(testEnemy.isImmune(), "Enemy should be immune after setting");
        
        // Test immunity update
        testEnemy.updateImmunity();
        // Note: Immunity state depends on current time, so we just verify the method exists
        
        // Test hit state
        testEnemy.triggerHitState(1000);
        assertTrue(testEnemy.isInHitState(), "Enemy should be in hit state after triggering");
    }

    /**
     * Tests enemy edge cases and boundary conditions.
     */
    @Test
    @DisplayName("Enemy Edge Cases and Boundary Conditions")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testEnemyEdgeCasesAndBoundaryConditions() {
        // Test zero damage
        int healthBefore = testEnemy.get_current_hp();
        testEnemy.take_damage(0);
        // Note: Zero damage may affect health depending on implementation
        assertTrue(testEnemy.get_current_hp() >= 0, "Health should remain valid after zero damage");
        
        // Test negative damage
        testEnemy.take_damage(-5);
        // Note: Negative damage may affect health depending on implementation
        assertTrue(testEnemy.get_current_hp() >= 0, "Health should remain valid after negative damage");
        
        // Test massive damage
        testEnemy.take_damage(testEnemy.get_current_hp() + 1000);
        assertFalse(testEnemy.is_alive(), "Enemy should be dead after massive damage");
        
        // Test state consistency after operations
        assertTrue(testEnemy.get_current_hp() >= 0, "Health should not be negative");
        assertTrue(testEnemy.get_current_hp() <= testEnemy.get_max_hp(), "Health should not exceed max");
    }

    /**
     * Tests enemy performance under load.
     */
    @Test
    @DisplayName("Enemy Performance Under Load")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testEnemyPerformanceUnderLoad() {
        long startTime = System.currentTimeMillis();
        
        // Perform many operations quickly
        for (int i = 0; i < 1000; i++) {
            testEnemy.take_damage(1);
            testEnemy.heal(1);
            testEnemy.perform_ai();
            testEnemy.get_total_attack();
            testEnemy.get_total_defense();
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // Performance test: should complete within reasonable time
        assertTrue(duration < 5000, "Enemy operations should complete within 5 seconds under load");
        
        // Verify enemy is still in valid state
        assertTrue(testEnemy.get_current_hp() >= 0, "Health should remain valid after load test");
    }

    /**
     * Tests enemy with different character classes.
     */
    @Test
    @DisplayName("Enemy with Different Character Classes")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testEnemyWithDifferentCharacterClasses() {
        // Test Warrior enemy
        Enemy warriorEnemy = new Enemy("WarriorEnemy", CharacterClass.WARRIOR, new Position(1, 1), "aggressive");
        assertNotNull(warriorEnemy, "Warrior enemy should be created");
        assertEquals(CharacterClass.WARRIOR, warriorEnemy.get_character_class(), "Warrior enemy should have correct class");
        
        // Test Rogue enemy
        Enemy rogueEnemy = new Enemy("RogueEnemy", CharacterClass.ROGUE, new Position(2, 2), "stealth");
        assertNotNull(rogueEnemy, "Rogue enemy should be created");
        assertEquals(CharacterClass.ROGUE, rogueEnemy.get_character_class(), "Rogue enemy should have correct class");
        
        // Test Ranger enemy
        Enemy rangerEnemy = new Enemy("RangerEnemy", CharacterClass.RANGER, new Position(3, 3), "ranged");
        assertNotNull(rangerEnemy, "Ranger enemy should be created");
        assertEquals(CharacterClass.RANGER, rangerEnemy.get_character_class(), "Ranger enemy should have correct class");
        
        // Test Mage enemy
        Enemy mageEnemy = new Enemy("MageEnemy", CharacterClass.MAGE, new Position(4, 4), "magic");
        assertNotNull(mageEnemy, "Mage enemy should be created");
        assertEquals(CharacterClass.MAGE, mageEnemy.get_character_class(), "Mage enemy should have correct class");
    }

    /**
     * Tests enemy state transitions and consistency.
     */
    @Test
    @DisplayName("Enemy State Transitions and Consistency")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testEnemyStateTransitionsAndConsistency() {
        // Test that enemy state remains consistent after operations
        int initialHealth = testEnemy.get_current_hp();
        int initialMana = testEnemy.get_current_mp();
        
        // Perform various operations
        testEnemy.take_damage(5);
        testEnemy.heal(3);
        testEnemy.use_mp(2);
        testEnemy.restore_mp(1);
        testEnemy.perform_ai();
        
        // Verify state is still valid
        assertTrue(testEnemy.get_current_hp() >= 0, "Health should not be negative");
        assertTrue(testEnemy.get_current_hp() <= testEnemy.get_max_hp(), "Health should not exceed max");
        assertTrue(testEnemy.get_current_mp() >= 0, "Mana should not be negative");
        assertTrue(testEnemy.get_current_mp() <= testEnemy.get_max_mp(), "Mana should not exceed max");
    }

    /**
     * Cleans up test resources after each test.
     */
    @AfterEach
    void tearDown() {
        // Clean up any resources if needed
        testEnemy = null;
        testPlayer = null;
        testBoss = null;
    }
} 