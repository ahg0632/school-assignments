package model.gameLogic;

import model.characters.Player;
import model.characters.Enemy;
import enums.CharacterClass;
import utilities.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple tests for GameLogic basic functionality.
 * Focuses on core game operations rather than complex stress testing.
 * Appropriate for a school project.
 */
@DisplayName("Simple GameLogic Tests")
class SimpleGameLogicTest {

    private GameLogic gameLogic;
    private Player testPlayer;

    @BeforeEach
    void setUp() {
        testPlayer = new Player("TestPlayer", CharacterClass.WARRIOR, new Position(0, 0));
        gameLogic = new GameLogic(testPlayer);
    }

    /**
     * Tests basic game state management.
     */
    @Test
    @DisplayName("Basic Game State Management")
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testBasicGameStateManagement() {
        // Test initial state
        assertFalse(gameLogic.is_paused(), "Game should not be paused initially");
        
        // Test pause functionality
        gameLogic.pause_game();
        assertTrue(gameLogic.is_paused(), "Game should be paused after pause_game()");
        
        // Test resume functionality
        gameLogic.resume_game();
        assertFalse(gameLogic.is_paused(), "Game should not be paused after resume_game()");
        
        // Test pause/resume functionality
        gameLogic.pause_game();
        assertTrue(gameLogic.is_paused(), "Game should be paused after pause_game()");
        gameLogic.resume_game();
        assertFalse(gameLogic.is_paused(), "Game should not be paused after resume_game()");
    }

    /**
     * Tests basic observer pattern functionality.
     */
    @Test
    @DisplayName("Basic Observer Pattern")
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testBasicObserverPattern() {
        MockGameObserver observer = new MockGameObserver("TestObserver");
        
        // Test adding observer
        gameLogic.add_observer(observer);
        
        // Test notification
        gameLogic.notify_observers("test_event", "test_data");
        
        // Test removing observer
        gameLogic.remove_observer(observer);
        
        // Verify observer was notified (if it has a way to track this)
        // This depends on the MockGameObserver implementation
    }

    /**
     * Tests basic projectile management.
     */
    @Test
    @DisplayName("Basic Projectile Management")
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testBasicProjectileManagement() {
        // Test getting projectiles (should not throw exception)
        assertNotNull(gameLogic.getProjectiles(), "getProjectiles() should not return null");
        
        // Test that we can access projectile list without issues
        // This is a basic functionality test
    }

    /**
     * Tests basic enemy management.
     */
    @Test
    @DisplayName("Basic Enemy Management")
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testBasicEnemyManagement() {
        // Test getting enemies (should not throw exception)
        assertNotNull(gameLogic.get_current_enemies(), "get_current_enemies() should not return null");
        
        // Test that we can access enemy list without issues
        // This is a basic functionality test
    }

    /**
     * Tests basic game state transitions.
     */
    @Test
    @DisplayName("Basic Game State Transitions")
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testBasicGameStateTransitions() {
        // Test initial game state
        assertNotNull(gameLogic.get_game_state(), "get_game_state() should not return null");
        
        // Test that game state is accessible
        // This verifies basic functionality without complex concurrency
    }

    /**
     * Tests that the game can handle basic operations without crashing.
     */
    @Test
    @DisplayName("Basic Game Operations")
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testBasicGameOperations() {
        // Test that basic operations don't throw exceptions
        assertDoesNotThrow(() -> {
            gameLogic.get_game_state();
            gameLogic.is_paused();
            gameLogic.getProjectiles();
            gameLogic.get_current_enemies();
        }, "Basic game operations should not throw exceptions");
    }

    /**
     * Tests that the game can handle multiple rapid state changes.
     */
    @Test
    @DisplayName("Rapid State Changes")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRapidStateChanges() {
        // Test multiple rapid state changes
        for (int i = 0; i < 10; i++) {
            gameLogic.pause_game();
            gameLogic.resume_game();
        }
        
        // Final state should be resumed
        assertFalse(gameLogic.is_paused(), "Game should be resumed after rapid state changes");
    }

    /**
     * Tests that the game can handle multiple observers.
     */
    @Test
    @DisplayName("Multiple Observers")
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testMultipleObservers() {
        MockGameObserver observer1 = new MockGameObserver("Observer1");
        MockGameObserver observer2 = new MockGameObserver("Observer2");
        MockGameObserver observer3 = new MockGameObserver("Observer3");
        
        // Add multiple observers
        gameLogic.add_observer(observer1);
        gameLogic.add_observer(observer2);
        gameLogic.add_observer(observer3);
        
        // Test notification
        gameLogic.notify_observers("multi_observer_test", "test_data");
        
        // Remove observers
        gameLogic.remove_observer(observer1);
        gameLogic.remove_observer(observer2);
        gameLogic.remove_observer(observer3);
        
        // Test that removal doesn't cause issues
        gameLogic.notify_observers("after_removal_test", "test_data");
    }
} 