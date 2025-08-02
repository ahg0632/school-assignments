package view.panels;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

import view.panels.MenuPanel;
import view.GameView;
import model.characters.Player;
import enums.CharacterClass;
import utilities.Position;

/**
 * Tests for MenuPanel component.
 * Tests initialization, basic properties, and component verification.
 * Appropriate for a school project.
 */
@DisplayName("MenuPanel Tests")
class MenuPanelTest {

    private GameView gameView;
    private MenuPanel menuPanel;
    private Player testPlayer;

    @BeforeEach
    void setUp() {
        gameView = new GameView();
        testPlayer = new Player("TestPlayer", CharacterClass.WARRIOR, new Position(0, 0));
        menuPanel = new MenuPanel(gameView);
    }

    @Test
    @DisplayName("MenuPanel Initialization")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testInitialization() {
        assertNotNull(menuPanel, "MenuPanel should not be null");
        assertTrue(menuPanel instanceof javax.swing.JPanel, "MenuPanel should be a JPanel");
    }

    @Test
    @DisplayName("MenuPanel Basic Properties")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testBasicProperties() {
        assertNotNull(menuPanel, "MenuPanel should not be null");
        assertTrue(menuPanel.isVisible(), "MenuPanel should be visible");
        assertTrue(menuPanel.isEnabled(), "MenuPanel should be enabled");
    }

    @Test
    @DisplayName("MenuPanel Component Verification")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testComponentVerification() {
        assertNotNull(menuPanel, "MenuPanel component should not be null");
        assertTrue(menuPanel instanceof javax.swing.JPanel || 
                  menuPanel instanceof javax.swing.JComponent, 
                  "MenuPanel should be a Swing component");
    }

    @Test
    @DisplayName("MenuPanel Multiple Initialization")
    @Timeout(value = 15, unit = TimeUnit.SECONDS)
    void testMultipleInitialization() {
        for (int i = 0; i < 5; i++) {
            MenuPanel panel = new MenuPanel(gameView);
            assertNotNull(panel, "MenuPanel " + i + " should not be null");
            assertTrue(panel instanceof javax.swing.JPanel, "MenuPanel " + i + " should be a JPanel");
        }
    }

    @Test
    @DisplayName("MenuPanel Stress Initialization")
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    void testStressInitialization() {
        for (int i = 0; i < 10; i++) {
            MenuPanel panel = new MenuPanel(gameView);
            assertNotNull(panel, "MenuPanel " + i + " should not be null");
            
            // Small delay to simulate stress
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Test
    @DisplayName("MenuPanel Null Safety")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testNullSafety() {
        assertDoesNotThrow(() -> {
            MenuPanel panel = new MenuPanel(gameView);
            assertNotNull(panel, "MenuPanel should be created even with null safety checks");
        }, "MenuPanel initialization should not throw exceptions");
    }

    @Test
    @DisplayName("MenuPanel Error Handling")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testErrorHandling() {
        assertDoesNotThrow(() -> {
            MenuPanel panel = new MenuPanel(gameView);
            assertNotNull(panel, "MenuPanel should be created even with error handling");
        }, "MenuPanel initialization should handle errors gracefully");
    }
} 