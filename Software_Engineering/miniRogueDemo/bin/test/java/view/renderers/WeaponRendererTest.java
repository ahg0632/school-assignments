package view.renderers;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import model.equipment.Weapon;
import model.gameLogic.AttackVisualData;
import enums.CharacterClass;

/**
 * Comprehensive test suite for WeaponRenderer class.
 * Tests weapon rendering functionality, image management, and edge cases.
 */
class WeaponRendererTest {

    private WeaponRenderer weaponRenderer;
    private Graphics2D mockGraphics;
    private Weapon testWeapon;
    private AttackVisualData testSwingData;
    private AttackVisualData testBowData;
    
    @BeforeEach
    void setUp() {
        weaponRenderer = new WeaponRenderer();
        
        // Create test objects
        testWeapon = new Weapon("Test Sword", 10, 5, CharacterClass.WARRIOR, 1, 
                               Weapon.WeaponType.BLADE, "test_sword.png", "Blade");
        testSwingData = new AttackVisualData(1, 0, 1.0f, 0.0, 0.0, Math.PI/2, Math.PI/4, 
                                            System.currentTimeMillis(), 1000);
        testBowData = new AttackVisualData(1, 0, 2.0f, Math.PI/4, 0.0, Math.PI/2, Math.PI/4, 
                                         System.currentTimeMillis(), 1000);
        
        // Create a real graphics context for testing
        BufferedImage testImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        mockGraphics = testImage.createGraphics();
    }
    
    @Test
    @DisplayName("Weapon Renderer Constructor")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testWeaponRendererConstructor() {
        assertDoesNotThrow(() -> {
            WeaponRenderer newRenderer = new WeaponRenderer();
        }, "Should create WeaponRenderer instance without throwing exceptions");
    }
    
    @Test
    @DisplayName("Render Weapon Swing Attack - Valid Parameters")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderWeaponSwingAttackValidParameters() {
        assertDoesNotThrow(() -> {
            weaponRenderer.renderWeaponSwingAttack(mockGraphics, 100.0f, 100.0f, 
                                               testWeapon, testSwingData, false);
        }, "Should render weapon swing attack without throwing exceptions");
    }
    
    @Test
    @DisplayName("Render Weapon Swing Attack - Enemy Rendering")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderWeaponSwingAttackEnemyRendering() {
        assertDoesNotThrow(() -> {
            weaponRenderer.renderWeaponSwingAttack(mockGraphics, 100.0f, 100.0f, 
                                               testWeapon, testSwingData, true);
        }, "Should render enemy weapon swing attack without throwing exceptions");
    }
    
    @Test
    @DisplayName("Render Weapon Swing Attack - Null Weapon")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderWeaponSwingAttackNullWeapon() {
        assertDoesNotThrow(() -> {
            weaponRenderer.renderWeaponSwingAttack(mockGraphics, 100.0f, 100.0f, 
                                               null, testSwingData, false);
        }, "Should handle null weapon gracefully");
    }
    
    @Test
    @DisplayName("Render Weapon Swing Attack - Null Swing Data")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderWeaponSwingAttackNullSwingData() {
        assertDoesNotThrow(() -> {
            weaponRenderer.renderWeaponSwingAttack(mockGraphics, 100.0f, 100.0f, 
                                               testWeapon, null, false);
        }, "Should handle null swing data gracefully");
    }
    
    @Test
    @DisplayName("Render Weapon Swing Attack - Different Weapon Types")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderWeaponSwingAttackDifferentWeaponTypes() {
        Weapon.WeaponType[] weaponTypes = {
            Weapon.WeaponType.BLADE,
            Weapon.WeaponType.DISTANCE,
            Weapon.WeaponType.IMPACT,
            Weapon.WeaponType.MAGIC
        };
        
        for (Weapon.WeaponType weaponType : weaponTypes) {
            Weapon weapon = new Weapon("Test Weapon", 10, 5, CharacterClass.WARRIOR, 1, 
                                     weaponType, "test_weapon.png", "Test");
            
            assertDoesNotThrow(() -> {
                weaponRenderer.renderWeaponSwingAttack(mockGraphics, 100.0f, 100.0f, 
                                                   weapon, testSwingData, false);
            }, "Should render " + weaponType + " weapon swing attack without throwing exceptions");
        }
    }
    
    @Test
    @DisplayName("Render Weapon Swing Attack - Different Positions")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderWeaponSwingAttackDifferentPositions() {
        float[][] positions = {
            {0.0f, 0.0f},
            {100.0f, 100.0f},
            {500.0f, 300.0f},
            {-50.0f, -50.0f}
        };
        
        for (float[] position : positions) {
            assertDoesNotThrow(() -> {
                weaponRenderer.renderWeaponSwingAttack(mockGraphics, position[0], position[1], 
                                                   testWeapon, testSwingData, false);
            }, "Should render weapon swing attack at position (" + position[0] + ", " + position[1] + ") without throwing exceptions");
        }
    }
    
    @Test
    @DisplayName("Render Ranger Bow - Valid Parameters")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderRangerBowValidParameters() {
        assertDoesNotThrow(() -> {
            weaponRenderer.renderRangerBow(mockGraphics, 100.0f, 100.0f, testBowData, false);
        }, "Should render ranger bow without throwing exceptions");
    }
    
    @Test
    @DisplayName("Render Ranger Bow - Enemy Rendering")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderRangerBowEnemyRendering() {
        assertDoesNotThrow(() -> {
            weaponRenderer.renderRangerBow(mockGraphics, 100.0f, 100.0f, testBowData, true);
        }, "Should render enemy ranger bow without throwing exceptions");
    }
    
    @Test
    @DisplayName("Render Ranger Bow - Null Bow Data")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderRangerBowNullBowData() {
        assertDoesNotThrow(() -> {
            weaponRenderer.renderRangerBow(mockGraphics, 100.0f, 100.0f, null, false);
        }, "Should handle null bow data gracefully");
    }
    
    @Test
    @DisplayName("Render Ranger Bow - Different Positions")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderRangerBowDifferentPositions() {
        float[][] positions = {
            {0.0f, 0.0f},
            {100.0f, 100.0f},
            {500.0f, 300.0f},
            {-50.0f, -50.0f}
        };
        
        for (float[] position : positions) {
            assertDoesNotThrow(() -> {
                weaponRenderer.renderRangerBow(mockGraphics, position[0], position[1], testBowData, false);
            }, "Should render ranger bow at position (" + position[0] + ", " + position[1] + ") without throwing exceptions");
        }
    }
    
    @Test
    @DisplayName("Get Weapon Image - Valid Weapon")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testGetWeaponImageValidWeapon() {
        assertDoesNotThrow(() -> {
            weaponRenderer.getWeaponImage(testWeapon);
        }, "Should get weapon image without throwing exceptions");
    }
    
    @Test
    @DisplayName("Get Weapon Image - Null Weapon")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testGetWeaponImageNullWeapon() {
        BufferedImage result = weaponRenderer.getWeaponImage(null);
        assertNull(result, "Should return null for null weapon");
    }
    
    @Test
    @DisplayName("Get Weapon Image - Different Weapon Names")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testGetWeaponImageDifferentWeaponNames() {
        String[] weaponNames = {
            "Test Sword",
            "Simple Bow",
            "Magic Staff",
            "Iron Hammer",
            "Dagger",
            "Crossbow"
        };
        
        for (String weaponName : weaponNames) {
            Weapon weapon = new Weapon(weaponName, 10, 5, CharacterClass.WARRIOR, 1, 
                                     Weapon.WeaponType.BLADE, "test_weapon.png", "Blade");
            
            assertDoesNotThrow(() -> {
                weaponRenderer.getWeaponImage(weapon);
            }, "Should get weapon image for " + weaponName + " without throwing exceptions");
        }
    }
    
    @Test
    @DisplayName("Has Weapon Image - Valid Weapon")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testHasWeaponImageValidWeapon() {
        assertDoesNotThrow(() -> {
            weaponRenderer.hasWeaponImage(testWeapon);
        }, "Should check weapon image existence without throwing exceptions");
    }
    
    @Test
    @DisplayName("Has Weapon Image - Null Weapon")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testHasWeaponImageNullWeapon() {
        boolean result = weaponRenderer.hasWeaponImage(null);
        assertFalse(result, "Should return false for null weapon");
    }
    
    @Test
    @DisplayName("Has Weapon Image - Different Weapon Names")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testHasWeaponImageDifferentWeaponNames() {
        String[] weaponNames = {
            "Test Sword",
            "Simple Bow",
            "Magic Staff",
            "Iron Hammer",
            "Dagger",
            "Crossbow"
        };
        
        for (String weaponName : weaponNames) {
            Weapon weapon = new Weapon(weaponName, 10, 5, CharacterClass.WARRIOR, 1, 
                                     Weapon.WeaponType.BLADE, "test_weapon.png", "Blade");
            
            assertDoesNotThrow(() -> {
                weaponRenderer.hasWeaponImage(weapon);
            }, "Should check weapon image existence for " + weaponName + " without throwing exceptions");
        }
    }
    
    @Test
    @DisplayName("Render Weapon Swing Attack - Null Graphics")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderWeaponSwingAttackNullGraphics() {
        // The renderer doesn't handle null graphics gracefully, so we expect an exception
        assertThrows(NullPointerException.class, () -> {
            weaponRenderer.renderWeaponSwingAttack(null, 100.0f, 100.0f, 
                                               testWeapon, testSwingData, false);
        }, "Should throw NullPointerException for null graphics context");
    }
    
    @Test
    @DisplayName("Render Ranger Bow - Null Graphics")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderRangerBowNullGraphics() {
        // The renderer doesn't handle null graphics gracefully, so we expect an exception
        assertThrows(NullPointerException.class, () -> {
            weaponRenderer.renderRangerBow(null, 100.0f, 100.0f, testBowData, false);
        }, "Should throw NullPointerException for null graphics context");
    }
    
    @Test
    @DisplayName("Render Weapon Swing Attack - Extreme Positions")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderWeaponSwingAttackExtremePositions() {
        float[][] extremePositions = {
            {Float.MAX_VALUE, Float.MAX_VALUE},
            {Float.MIN_VALUE, Float.MIN_VALUE},
            {Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY},
            {Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY}
        };
        
        for (float[] position : extremePositions) {
            assertDoesNotThrow(() -> {
                weaponRenderer.renderWeaponSwingAttack(mockGraphics, position[0], position[1], 
                                                   testWeapon, testSwingData, false);
            }, "Should handle extreme position (" + position[0] + ", " + position[1] + ") gracefully");
        }
    }
    
    @Test
    @DisplayName("Render Ranger Bow - Extreme Positions")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRenderRangerBowExtremePositions() {
        float[][] extremePositions = {
            {Float.MAX_VALUE, Float.MAX_VALUE},
            {Float.MIN_VALUE, Float.MIN_VALUE},
            {Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY},
            {Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY}
        };
        
        for (float[] position : extremePositions) {
            assertDoesNotThrow(() -> {
                weaponRenderer.renderRangerBow(mockGraphics, position[0], position[1], testBowData, false);
            }, "Should handle extreme position (" + position[0] + ", " + position[1] + ") gracefully");
        }
    }
    
    @Test
    @DisplayName("Render Weapon Swing Attack - Performance Test")
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    void testRenderWeaponSwingAttackPerformance() {
        long startTime = System.currentTimeMillis();
        
        // Render multiple weapon swings
        for (int i = 0; i < 1000; i++) {
            weaponRenderer.renderWeaponSwingAttack(mockGraphics, 100.0f, 100.0f, 
                                               testWeapon, testSwingData, false);
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        assertTrue(duration < 10000, "Should complete 1000 renders in under 10 seconds");
    }
    
    @Test
    @DisplayName("Render Ranger Bow - Performance Test")
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    void testRenderRangerBowPerformance() {
        long startTime = System.currentTimeMillis();
        
        // Render multiple bow renders
        for (int i = 0; i < 1000; i++) {
            weaponRenderer.renderRangerBow(mockGraphics, 100.0f, 100.0f, testBowData, false);
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        assertTrue(duration < 10000, "Should complete 1000 renders in under 10 seconds");
    }
} 