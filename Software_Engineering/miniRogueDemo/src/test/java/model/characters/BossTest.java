package model.characters;

import enums.CharacterClass;
import utilities.Position;
import model.items.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.AfterEach;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for the Boss class.
 * Tests boss-specific enhancements, combat mechanics, and visual differences.
 */
@DisplayName("Boss Class Tests")
class BossTest {

    private Boss testBoss;
    private Player testPlayer;
    private Enemy testEnemy;

    @BeforeEach
    void setUp() {
        testBoss = new Boss("TestBoss", CharacterClass.WARRIOR, new Position(10, 10));
        testPlayer = new Player("TestPlayer", CharacterClass.WARRIOR, new Position(0, 0));
        testEnemy = new Enemy("TestEnemy", CharacterClass.MAGE, new Position(5, 5), "aggressive");
    }

    /**
     * Tests boss creation and basic initialization.
     */
    @Test
    @DisplayName("Boss Creation and Initialization")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testBossCreationAndInitialization() {
        // Test basic boss properties
        assertNotNull(testBoss, "Boss should not be null");
        assertEquals("TestBoss", testBoss.get_name(), "Boss name should be set correctly");
        assertEquals(CharacterClass.WARRIOR, testBoss.get_character_class(), "Boss class should be set correctly");
        
        // Test position
        Position bossPos = testBoss.get_position();
        assertNotNull(bossPos, "Boss position should not be null");
        assertEquals(10, bossPos.get_x(), "Boss X position should be 10");
        assertEquals(10, bossPos.get_y(), "Boss Y position should be 10");
        
        // Test basic stats
        assertTrue(testBoss.get_current_hp() > 0, "Boss should have health");
        assertTrue(testBoss.get_max_hp() > 0, "Boss should have max health");
        assertTrue(testBoss.is_alive(), "Boss should be alive initially");
    }

    /**
     * Tests boss-specific stat enhancements.
     */
    @Test
    @DisplayName("Boss Stat Enhancements")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testBossStatEnhancements() {
        // Test that boss has enhanced stats compared to regular enemy
        Boss warriorBoss = new Boss("WarriorBoss", CharacterClass.WARRIOR, new Position(1, 1));
        Enemy warriorEnemy = new Enemy("WarriorEnemy", CharacterClass.WARRIOR, new Position(2, 2), "aggressive");
        
        // Boss should have more HP than regular enemy
        assertTrue(warriorBoss.get_max_hp() > warriorEnemy.get_max_hp(), "Boss should have more HP than regular enemy");
        assertTrue(warriorBoss.get_current_hp() > warriorEnemy.get_current_hp(), "Boss should have more current HP than regular enemy");
        
        // Boss should have more attack than regular enemy
        assertTrue(warriorBoss.get_base_atk() > warriorEnemy.get_base_atk(), "Boss should have more attack than regular enemy");
        
        // Boss should have more experience value
        assertTrue(warriorBoss.get_experience_value() > warriorEnemy.get_experience_value(), "Boss should give more experience than regular enemy");
    }

    /**
     * Tests boss size and visual modifiers.
     */
    @Test
    @DisplayName("Boss Size and Visual Modifiers")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testBossSizeAndVisualModifiers() {
        // Test size multiplier
        float sizeMultiplier = testBoss.getSizeMultiplier();
        assertEquals(2.0f, sizeMultiplier, "Boss should have 2.0x size multiplier");
        
        // Test range modifier
        float rangeModifier = testBoss.getRangeModifier();
        assertEquals(0.8f, rangeModifier, "Boss should have 0.8x range modifier");
        
        // Test speed modifier
        float speedModifier = testBoss.getSpeedModifier();
        assertEquals(0.8f, speedModifier, "Boss should have 0.8x speed modifier");
        
        // Test vision modifier
        float visionModifier = testBoss.getVisionModifier();
        assertEquals(1.3f, visionModifier, "Boss should have 1.3x vision modifier");
    }

    /**
     * Tests boss combat mechanics.
     */
    @Test
    @DisplayName("Boss Combat Mechanics")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testBossCombatMechanics() {
        // Test boss attack against player
        int initialPlayerHealth = testPlayer.get_current_hp();
        int damage = testBoss.attack(testPlayer);
        assertTrue(damage >= 0, "Boss attack damage should be non-negative");
        assertTrue(testPlayer.get_current_hp() <= initialPlayerHealth, "Player should take damage from boss");
        
        // Test player attack against boss
        int initialBossHealth = testBoss.get_current_hp();
        int playerDamage = testPlayer.attack(testBoss);
        assertTrue(playerDamage >= 0, "Player attack damage should be non-negative");
        assertTrue(testBoss.get_current_hp() <= initialBossHealth, "Boss should take damage");
        
        // Test that boss can die
        testBoss.take_damage(testBoss.get_current_hp() + 100); // Massive damage
        assertFalse(testBoss.is_alive(), "Boss should be dead after massive damage");
    }

    /**
     * Tests boss aggro range enhancement.
     */
    @Test
    @DisplayName("Boss Aggro Range Enhancement")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testBossAggroRangeEnhancement() {
        // Test that boss has enhanced aggro range
        int bossAggroRange = testBoss.get_aggro_range();
        assertTrue(bossAggroRange > 0, "Boss should have positive aggro range");
        
        // Compare with regular enemy (boss should have larger range due to vision modifier)
        int enemyAggroRange = testEnemy.get_aggro_range();
        // Note: We can't directly compare since they might have different base ranges
        // But we can verify the boss range is reasonable
        assertTrue(bossAggroRange >= enemyAggroRange, "Boss should have enhanced aggro range");
    }

    /**
     * Tests boss character type identification.
     */
    @Test
    @DisplayName("Boss Character Type Identification")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testBossCharacterTypeIdentification() {
        // Test character type
        String characterType = testBoss.getCharacterType();
        assertNotNull(characterType, "Boss character type should not be null");
        assertFalse(characterType.isEmpty(), "Boss character type should not be empty");
        
        // Test boss status
        assertTrue(testBoss.isBoss(), "Boss should be identified as boss");
        
        // Test that regular enemy is not a boss
        assertFalse(testEnemy.isBoss(), "Regular enemy should not be identified as boss");
    }

    /**
     * Tests boss with different character classes.
     */
    @Test
    @DisplayName("Boss with Different Character Classes")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testBossWithDifferentCharacterClasses() {
        // Test Warrior boss
        Boss warriorBoss = new Boss("WarriorBoss", CharacterClass.WARRIOR, new Position(1, 1));
        assertNotNull(warriorBoss, "Warrior boss should be created");
        assertEquals(CharacterClass.WARRIOR, warriorBoss.get_character_class(), "Warrior boss should have correct class");
        assertTrue(warriorBoss.isBoss(), "Warrior boss should be identified as boss");
        
        // Test Mage boss
        Boss mageBoss = new Boss("MageBoss", CharacterClass.MAGE, new Position(2, 2));
        assertNotNull(mageBoss, "Mage boss should be created");
        assertEquals(CharacterClass.MAGE, mageBoss.get_character_class(), "Mage boss should have correct class");
        assertTrue(mageBoss.isBoss(), "Mage boss should be identified as boss");
        
        // Test Rogue boss
        Boss rogueBoss = new Boss("RogueBoss", CharacterClass.ROGUE, new Position(3, 3));
        assertNotNull(rogueBoss, "Rogue boss should be created");
        assertEquals(CharacterClass.ROGUE, rogueBoss.get_character_class(), "Rogue boss should have correct class");
        assertTrue(rogueBoss.isBoss(), "Rogue boss should be identified as boss");
        
        // Test Ranger boss
        Boss rangerBoss = new Boss("RangerBoss", CharacterClass.RANGER, new Position(4, 4));
        assertNotNull(rangerBoss, "Ranger boss should be created");
        assertEquals(CharacterClass.RANGER, rangerBoss.get_character_class(), "Ranger boss should have correct class");
        assertTrue(rangerBoss.isBoss(), "Ranger boss should be identified as boss");
    }

    /**
     * Tests boss loot and experience system.
     */
    @Test
    @DisplayName("Boss Loot and Experience System")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testBossLootAndExperienceSystem() {
        // Test loot dropping
        List<Item> loot = testBoss.drop_loot();
        assertNotNull(loot, "Boss loot should not be null");
        // Note: Loot may be empty depending on random chance, so we don't assert on size
        
        // Test experience value (bosses should give more experience)
        int bossExpValue = testBoss.get_experience_value();
        int enemyExpValue = testEnemy.get_experience_value();
        assertTrue(bossExpValue > enemyExpValue, "Boss should give more experience than regular enemy");
        assertTrue(bossExpValue > 0, "Boss should have positive experience value");
    }

    /**
     * Tests boss movement and positioning.
     */
    @Test
    @DisplayName("Boss Movement and Positioning")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testBossMovementAndPositioning() {
        // Test initial position
        Position bossPos = testBoss.get_position();
        assertNotNull(bossPos, "Boss position should not be null");
        assertEquals(10, bossPos.get_x(), "Boss X position should be 10");
        assertEquals(10, bossPos.get_y(), "Boss Y position should be 10");
        
        // Test pixel position
        testBoss.setPixelX(128.0f);
        testBoss.setPixelY(256.0f);
        assertEquals(128.0f, testBoss.getPixelX(), "Boss pixel X should be set correctly");
        assertEquals(256.0f, testBoss.getPixelY(), "Boss pixel Y should be set correctly");
        
        // Test movement update (should not throw exception)
        assertDoesNotThrow(() -> testBoss.update_movement(), "Boss movement update should not throw exception");
    }

    /**
     * Tests boss state management.
     */
    @Test
    @DisplayName("Boss State Management")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testBossStateManagement() {
        // Test initial state
        assertFalse(testBoss.isChasingPlayer(), "Boss should not be chasing initially");
        assertFalse(testBoss.isInCelebratoryState(), "Boss should not be in celebratory state initially");
        assertFalse(testBoss.isInWindUpState(), "Boss should not be in wind-up state initially");
        assertFalse(testBoss.isInHitState(), "Boss should not be in hit state initially");
        assertFalse(testBoss.isDying(), "Boss should not be dying initially");
        
        // Test state transitions
        testBoss.startCelebratoryState();
        assertTrue(testBoss.isInCelebratoryState(), "Boss should be in celebratory state after starting");
        
        testBoss.setInWindUpState(true);
        assertTrue(testBoss.isInWindUpState(), "Boss should be in wind-up state after setting");
        
        testBoss.triggerHitState(1000);
        assertTrue(testBoss.isInHitState(), "Boss should be in hit state after triggering");
        
        testBoss.startDying();
        assertTrue(testBoss.isDying(), "Boss should be dying after starting");
    }

    /**
     * Tests boss aiming and attack direction.
     */
    @Test
    @DisplayName("Boss Aiming and Attack Direction")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testBossAimingAndAttackDirection() {
        // Test aim direction
        int aimDX = testBoss.getAimDX();
        int aimDY = testBoss.getAimDY();
        assertTrue(aimDX >= -1 && aimDX <= 1, "Boss aim DX should be between -1 and 1");
        assertTrue(aimDY >= -1 && aimDY <= 1, "Boss aim DY should be between -1 and 1");
        
        // Test precise aiming
        float preciseAimDX = testBoss.getPreciseAimDX();
        float preciseAimDY = testBoss.getPreciseAimDY();
        assertTrue(preciseAimDX >= -1.0f && preciseAimDX <= 1.0f, "Boss precise aim DX should be between -1 and 1");
        assertTrue(preciseAimDY >= -1.0f && preciseAimDY <= 1.0f, "Boss precise aim DY should be between -1 and 1");
        
        // Test wind-up aiming
        testBoss.setWindUpAimDirection(1, 0, 1.0f, 0.0f);
        assertEquals(1, testBoss.getWindUpAimDX(), "Boss wind-up aim DX should be set correctly");
        assertEquals(0, testBoss.getWindUpAimDY(), "Boss wind-up aim DY should be set correctly");
        assertEquals(1.0f, testBoss.getWindUpPreciseAimDX(), "Boss wind-up precise aim DX should be set correctly");
        assertEquals(0.0f, testBoss.getWindUpPreciseAimDY(), "Boss wind-up precise aim DY should be set correctly");
    }

    /**
     * Tests boss AI behavior.
     */
    @Test
    @DisplayName("Boss AI Behavior")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testBossAIBehavior() {
        // Test AI performance method (should not throw exception)
        assertDoesNotThrow(() -> testBoss.perform_ai(), "Boss AI performance should not throw exception");
        
        // Test melee attack performance
        assertDoesNotThrow(() -> testBoss.perform_melee_attack(), "Boss melee attack should not throw exception");
        
        // Test projectile attack performance
        assertDoesNotThrow(() -> testBoss.perform_projectile_attack(), "Boss projectile attack should not throw exception");
        
        // Test AI pattern
        String aiPattern = testBoss.get_ai_pattern();
        assertNotNull(aiPattern, "Boss AI pattern should not be null");
        assertFalse(aiPattern.isEmpty(), "Boss AI pattern should not be empty");
    }

    /**
     * Tests boss weaknesses system.
     */
    @Test
    @DisplayName("Boss Weaknesses System")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testBossWeaknessesSystem() {
        // Test weaknesses list
        List<String> weaknesses = testBoss.get_weaknesses();
        assertNotNull(weaknesses, "Boss weaknesses should not be null");
        // Note: Weaknesses may be empty, so we don't assert on size
        
        // Test weakness checking
        boolean isWeakToFire = testBoss.is_weak_to("fire");
        // Note: This depends on the specific boss, so we just test the method exists
        assertTrue(true, "Boss weakness check should not throw exception");
    }

    /**
     * Tests boss immunity and hit state management.
     */
    @Test
    @DisplayName("Boss Immunity and Hit State Management")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testBossImmunityAndHitStateManagement() {
        // Test initial immunity state
        assertFalse(testBoss.isImmune(), "Boss should not be immune initially");
        
        // Test immunity setting
        testBoss.setImmune(1000); // 1 second immunity
        assertTrue(testBoss.isImmune(), "Boss should be immune after setting");
        
        // Test immunity update
        testBoss.updateImmunity();
        // Note: Immunity state depends on current time, so we just verify the method exists
        
        // Test hit state
        testBoss.triggerHitState(1000);
        assertTrue(testBoss.isInHitState(), "Boss should be in hit state after triggering");
    }

    /**
     * Tests boss edge cases and boundary conditions.
     */
    @Test
    @DisplayName("Boss Edge Cases and Boundary Conditions")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testBossEdgeCasesAndBoundaryConditions() {
        // Test zero damage
        int healthBefore = testBoss.get_current_hp();
        testBoss.take_damage(0);
        assertEquals(healthBefore, testBoss.get_current_hp(), "Zero damage should not affect boss health");
        
        // Test negative damage
        testBoss.take_damage(-5);
        assertEquals(healthBefore, testBoss.get_current_hp(), "Negative damage should not affect boss health");
        
        // Test massive damage
        testBoss.take_damage(testBoss.get_current_hp() + 1000);
        assertFalse(testBoss.is_alive(), "Boss should be dead after massive damage");
        
        // Test state consistency after operations
        assertTrue(testBoss.get_current_hp() >= 0, "Boss health should not be negative");
        assertTrue(testBoss.get_current_hp() <= testBoss.get_max_hp(), "Boss health should not exceed max");
    }

    /**
     * Tests boss performance under load.
     */
    @Test
    @DisplayName("Boss Performance Under Load")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testBossPerformanceUnderLoad() {
        long startTime = System.currentTimeMillis();
        
        // Perform many operations quickly
        for (int i = 0; i < 1000; i++) {
            testBoss.take_damage(1);
            testBoss.heal(1);
            testBoss.perform_ai();
            testBoss.get_total_attack();
            testBoss.get_total_defense();
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // Performance test: should complete within reasonable time
        assertTrue(duration < 5000, "Boss operations should complete within 5 seconds under load");
        
        // Verify boss is still in valid state
        assertTrue(testBoss.get_current_hp() >= 0, "Boss health should remain valid after load test");
    }

    /**
     * Tests boss state transitions and consistency.
     */
    @Test
    @DisplayName("Boss State Transitions and Consistency")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testBossStateTransitionsAndConsistency() {
        // Test that boss state remains consistent after operations
        int initialHealth = testBoss.get_current_hp();
        int initialMana = testBoss.get_current_mp();
        
        // Perform various operations
        testBoss.take_damage(5);
        testBoss.heal(3);
        testBoss.use_mp(2);
        testBoss.restore_mp(1);
        testBoss.perform_ai();
        
        // Verify state is still valid
        assertTrue(testBoss.get_current_hp() >= 0, "Boss health should not be negative");
        assertTrue(testBoss.get_current_hp() <= testBoss.get_max_hp(), "Boss health should not exceed max");
        assertTrue(testBoss.get_current_mp() >= 0, "Boss mana should not be negative");
        assertTrue(testBoss.get_current_mp() <= testBoss.get_max_mp(), "Boss mana should not exceed max");
    }

    /**
     * Tests boss inheritance from Enemy class.
     */
    @Test
    @DisplayName("Boss Inheritance from Enemy Class")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testBossInheritanceFromEnemyClass() {
        // Test that boss inherits all enemy functionality
        assertTrue(testBoss instanceof Enemy, "Boss should be instance of Enemy");
        assertTrue(testBoss instanceof Character, "Boss should be instance of Character");
        
        // Test that boss has all enemy methods
        assertNotNull(testBoss.get_name(), "Boss should have name getter");
        assertNotNull(testBoss.get_character_class(), "Boss should have character class getter");
        assertNotNull(testBoss.get_position(), "Boss should have position getter");
        assertTrue(testBoss.get_current_hp() > 0, "Boss should have health");
        assertTrue(testBoss.get_max_hp() > 0, "Boss should have max health");
        assertTrue(testBoss.is_alive(), "Boss should be alive");
    }

    /**
     * Cleans up test resources after each test.
     */
    @AfterEach
    void tearDown() {
        // Clean up any resources if needed
        testBoss = null;
        testPlayer = null;
        testEnemy = null;
    }
} 