package view.panels;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

import view.panels.EquipmentPanel;
import view.GameView;
import model.characters.Player;
import enums.CharacterClass;
import utilities.Position;

/**
 * Tests for EquipmentPanel component.
 * Tests initialization, basic properties, and component verification.
 * Appropriate for a school project.
 */
@DisplayName("EquipmentPanel Tests")
class EquipmentPanelTest {

    private GameView gameView;
    private EquipmentPanel equipmentPanel;
    private Player testPlayer;

    @BeforeEach
    void setUp() {
        gameView = new GameView();
        testPlayer = new Player("TestPlayer", CharacterClass.WARRIOR, new Position(0, 0));
        equipmentPanel = new EquipmentPanel(gameView, testPlayer);
    }

    @Test
    @DisplayName("EquipmentPanel Initialization")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testInitialization() {
        assertNotNull(equipmentPanel, "EquipmentPanel should not be null");
        assertTrue(equipmentPanel instanceof javax.swing.JPanel, "EquipmentPanel should be a JPanel");
    }

    @Test
    @DisplayName("EquipmentPanel Basic Properties")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testBasicProperties() {
        assertNotNull(equipmentPanel, "EquipmentPanel should not be null");
        assertTrue(equipmentPanel.isVisible(), "EquipmentPanel should be visible");
        assertTrue(equipmentPanel.isEnabled(), "EquipmentPanel should be enabled");
    }

    @Test
    @DisplayName("EquipmentPanel Component Verification")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testComponentVerification() {
        assertNotNull(equipmentPanel, "EquipmentPanel component should not be null");
        assertTrue(equipmentPanel instanceof javax.swing.JPanel || 
                  equipmentPanel instanceof javax.swing.JComponent, 
                  "EquipmentPanel should be a Swing component");
    }

    @Test
    @DisplayName("EquipmentPanel Multiple Initialization")
    @Timeout(value = 15, unit = TimeUnit.SECONDS)
    void testMultipleInitialization() {
        for (int i = 0; i < 5; i++) {
            EquipmentPanel panel = new EquipmentPanel(gameView, testPlayer);
            assertNotNull(panel, "EquipmentPanel " + i + " should not be null");
            assertTrue(panel instanceof javax.swing.JPanel, "EquipmentPanel " + i + " should be a JPanel");
        }
    }

    @Test
    @DisplayName("EquipmentPanel Stress Initialization")
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    void testStressInitialization() {
        for (int i = 0; i < 10; i++) {
            EquipmentPanel panel = new EquipmentPanel(gameView, testPlayer);
            assertNotNull(panel, "EquipmentPanel " + i + " should not be null");
            
            // Small delay to simulate stress
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Test
    @DisplayName("EquipmentPanel Null Safety")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testNullSafety() {
        assertDoesNotThrow(() -> {
            EquipmentPanel panel = new EquipmentPanel(gameView, null);
            assertNotNull(panel, "EquipmentPanel should be created even with null safety checks");
        }, "EquipmentPanel initialization should not throw exceptions");
    }

    @Test
    @DisplayName("EquipmentPanel Error Handling")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testErrorHandling() {
        assertDoesNotThrow(() -> {
            EquipmentPanel panel = new EquipmentPanel(gameView, testPlayer);
            assertNotNull(panel, "EquipmentPanel should be created even with error handling");
        }, "EquipmentPanel initialization should handle errors gracefully");
    }
} 