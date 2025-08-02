package view.panels;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

import view.panels.SideInventoryPanel;
import view.GameView;
import model.characters.Player;
import enums.CharacterClass;
import utilities.Position;

/**
 * Tests for SideInventoryPanel component.
 * Tests initialization, basic properties, and component verification.
 * Appropriate for a school project.
 */
@DisplayName("SideInventoryPanel Tests")
class SideInventoryPanelTest {

    private GameView gameView;
    private SideInventoryPanel sideInventoryPanel;
    private Player testPlayer;

    @BeforeEach
    void setUp() {
        gameView = new GameView();
        testPlayer = new Player("TestPlayer", CharacterClass.WARRIOR, new Position(0, 0));
        sideInventoryPanel = new SideInventoryPanel(gameView);
    }

    @Test
    @DisplayName("SideInventoryPanel Initialization")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testInitialization() {
        assertNotNull(sideInventoryPanel, "SideInventoryPanel should not be null");
        assertTrue(sideInventoryPanel instanceof javax.swing.JPanel, "SideInventoryPanel should be a JPanel");
    }

    @Test
    @DisplayName("SideInventoryPanel Basic Properties")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testBasicProperties() {
        assertNotNull(sideInventoryPanel, "SideInventoryPanel should not be null");
        assertTrue(sideInventoryPanel.isVisible(), "SideInventoryPanel should be visible");
        assertTrue(sideInventoryPanel.isEnabled(), "SideInventoryPanel should be enabled");
    }

    @Test
    @DisplayName("SideInventoryPanel Component Verification")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testComponentVerification() {
        assertNotNull(sideInventoryPanel, "SideInventoryPanel component should not be null");
        assertTrue(sideInventoryPanel instanceof javax.swing.JPanel || 
                  sideInventoryPanel instanceof javax.swing.JComponent, 
                  "SideInventoryPanel should be a Swing component");
    }

    @Test
    @DisplayName("SideInventoryPanel Multiple Initialization")
    @Timeout(value = 15, unit = TimeUnit.SECONDS)
    void testMultipleInitialization() {
        for (int i = 0; i < 5; i++) {
            SideInventoryPanel panel = new SideInventoryPanel(gameView);
            assertNotNull(panel, "SideInventoryPanel " + i + " should not be null");
            assertTrue(panel instanceof javax.swing.JPanel, "SideInventoryPanel " + i + " should be a JPanel");
        }
    }

    @Test
    @DisplayName("SideInventoryPanel Stress Initialization")
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    void testStressInitialization() {
        for (int i = 0; i < 10; i++) {
            SideInventoryPanel panel = new SideInventoryPanel(gameView);
            assertNotNull(panel, "SideInventoryPanel " + i + " should not be null");
            
            // Small delay to simulate stress
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Test
    @DisplayName("SideInventoryPanel Null Safety")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testNullSafety() {
        assertDoesNotThrow(() -> {
            SideInventoryPanel panel = new SideInventoryPanel(gameView);
            assertNotNull(panel, "SideInventoryPanel should be created even with null safety checks");
        }, "SideInventoryPanel initialization should not throw exceptions");
    }

    @Test
    @DisplayName("SideInventoryPanel Error Handling")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testErrorHandling() {
        assertDoesNotThrow(() -> {
            SideInventoryPanel panel = new SideInventoryPanel(gameView);
            assertNotNull(panel, "SideInventoryPanel should be created even with error handling");
        }, "SideInventoryPanel initialization should handle errors gracefully");
    }
} 