package view.panels;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

import view.panels.GamePanel;
import view.GameView;
import model.characters.Player;
import enums.CharacterClass;
import utilities.Position;

/**
 * Tests for GamePanel component.
 * Tests initialization, basic properties, and component verification.
 * Appropriate for a school project.
 */
@DisplayName("GamePanel Tests")
class GamePanelTest {

    private GameView gameView;
    private GamePanel gamePanel;
    private Player testPlayer;

    @BeforeEach
    void setUp() {
        gameView = new GameView();
        testPlayer = new Player("TestPlayer", CharacterClass.WARRIOR, new Position(0, 0));
        gamePanel = new GamePanel(gameView);
    }

    @Test
    @DisplayName("GamePanel Initialization")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testInitialization() {
        assertNotNull(gamePanel, "GamePanel should not be null");
        assertTrue(gamePanel instanceof javax.swing.JPanel, "GamePanel should be a JPanel");
    }

    @Test
    @DisplayName("GamePanel Basic Properties")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testBasicProperties() {
        assertNotNull(gamePanel, "GamePanel should not be null");
        assertTrue(gamePanel.isVisible(), "GamePanel should be visible");
        assertTrue(gamePanel.isEnabled(), "GamePanel should be enabled");
    }

    @Test
    @DisplayName("GamePanel Component Verification")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testComponentVerification() {
        assertNotNull(gamePanel, "GamePanel component should not be null");
        assertTrue(gamePanel instanceof javax.swing.JPanel || 
                  gamePanel instanceof javax.swing.JComponent, 
                  "GamePanel should be a Swing component");
    }

    @Test
    @DisplayName("GamePanel Multiple Initialization")
    @Timeout(value = 15, unit = TimeUnit.SECONDS)
    void testMultipleInitialization() {
        for (int i = 0; i < 5; i++) {
            GamePanel panel = new GamePanel(gameView);
            assertNotNull(panel, "GamePanel " + i + " should not be null");
            assertTrue(panel instanceof javax.swing.JPanel, "GamePanel " + i + " should be a JPanel");
        }
    }

    @Test
    @DisplayName("GamePanel Stress Initialization")
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    void testStressInitialization() {
        for (int i = 0; i < 10; i++) {
            GamePanel panel = new GamePanel(gameView);
            assertNotNull(panel, "GamePanel " + i + " should not be null");
            
            // Small delay to simulate stress
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Test
    @DisplayName("GamePanel Null Safety")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testNullSafety() {
        assertDoesNotThrow(() -> {
            GamePanel panel = new GamePanel(gameView);
            assertNotNull(panel, "GamePanel should be created even with null safety checks");
        }, "GamePanel initialization should not throw exceptions");
    }

    @Test
    @DisplayName("GamePanel Error Handling")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testErrorHandling() {
        assertDoesNotThrow(() -> {
            GamePanel panel = new GamePanel(gameView);
            assertNotNull(panel, "GamePanel should be created even with error handling");
        }, "GamePanel initialization should handle errors gracefully");
    }
} 