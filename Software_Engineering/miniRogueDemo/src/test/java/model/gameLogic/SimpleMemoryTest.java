package model.gameLogic;

import model.characters.Player;
import enums.CharacterClass;
import utilities.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple memory tests for GameLogic.
 * Focuses on basic memory management rather than complex stress testing.
 * Appropriate for a school project.
 */
@DisplayName("Simple Memory Tests")
class SimpleMemoryTest {

    private GameLogic gameLogic;
    private Player testPlayer;

    @BeforeEach
    void setUp() {
        testPlayer = new Player("TestPlayer", CharacterClass.WARRIOR, new Position(0, 0));
        gameLogic = new GameLogic(testPlayer);
    }

    /**
     * Tests basic memory usage after creating GameLogic.
     */
    @Test
    @DisplayName("Basic Memory Usage")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testBasicMemoryUsage() {
        // Record initial memory
        long initialMemory = getCurrentMemoryUsage();
        
        // Perform basic operations
        gameLogic.get_game_state();
        gameLogic.is_paused();
        gameLogic.getProjectiles();
        gameLogic.get_current_enemies();
        
        // Force garbage collection
        System.gc();
        
        // Record final memory
        long finalMemory = getCurrentMemoryUsage();
        long memoryIncrease = finalMemory - initialMemory;
        
        // Basic check - memory increase should be reasonable (less than 50MB)
        assertTrue(memoryIncrease < 50 * 1024 * 1024, 
            "Memory increase should be reasonable, got: " + formatMemoryUsage(memoryIncrease));
    }

    /**
     * Tests that creating multiple GameLogic instances doesn't cause obvious memory leaks.
     */
    @Test
    @DisplayName("Multiple GameLogic Instances")
    @Timeout(value = 15, unit = TimeUnit.SECONDS)
    void testMultipleGameLogicInstances() {
        long initialMemory = getCurrentMemoryUsage();
        
        // Create multiple GameLogic instances
        for (int i = 0; i < 10; i++) {
            Player player = new Player("TestPlayer" + i, CharacterClass.WARRIOR, new Position(0, 0));
            GameLogic logic = new GameLogic(player);
            
            // Perform some operations
            logic.get_game_state();
            logic.is_paused();
            logic.getProjectiles();
            logic.get_current_enemies();
        }
        
        // Force garbage collection
        System.gc();
        
        long finalMemory = getCurrentMemoryUsage();
        long memoryIncrease = finalMemory - initialMemory;
        
        // Memory increase should be reasonable
        assertTrue(memoryIncrease < 100 * 1024 * 1024, 
            "Memory increase should be reasonable after creating multiple instances, got: " + formatMemoryUsage(memoryIncrease));
    }

    /**
     * Tests that observer operations don't cause obvious memory leaks.
     */
    @Test
    @DisplayName("Observer Memory Test")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testObserverMemoryTest() {
        long initialMemory = getCurrentMemoryUsage();
        
        // Add and remove observers multiple times
        for (int i = 0; i < 50; i++) {
            MockGameObserver observer = new MockGameObserver("Observer" + i);
            gameLogic.add_observer(observer);
            gameLogic.notify_observers("test_event", "test_data");
            gameLogic.remove_observer(observer);
        }
        
        // Force garbage collection
        System.gc();
        
        long finalMemory = getCurrentMemoryUsage();
        long memoryIncrease = finalMemory - initialMemory;
        
        // Memory increase should be reasonable
        assertTrue(memoryIncrease < 50 * 1024 * 1024, 
            "Memory increase should be reasonable after observer operations, got: " + formatMemoryUsage(memoryIncrease));
    }

    /**
     * Tests that rapid state changes don't cause obvious memory leaks.
     */
    @Test
    @DisplayName("Rapid State Changes Memory Test")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRapidStateChangesMemoryTest() {
        long initialMemory = getCurrentMemoryUsage();
        
        // Perform rapid state changes
        for (int i = 0; i < 100; i++) {
            gameLogic.pause_game();
            gameLogic.resume_game();
            gameLogic.pause_game();
        }
        
        // Force garbage collection
        System.gc();
        
        long finalMemory = getCurrentMemoryUsage();
        long memoryIncrease = finalMemory - initialMemory;
        
        // Memory increase should be reasonable
        assertTrue(memoryIncrease < 50 * 1024 * 1024, 
            "Memory increase should be reasonable after rapid state changes, got: " + formatMemoryUsage(memoryIncrease));
    }

    /**
     * Gets the current memory usage in bytes.
     */
    private long getCurrentMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }

    /**
     * Formats memory usage in a human-readable format.
     */
    private String formatMemoryUsage(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.1f KB", bytes / 1024.0);
        } else {
            return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
        }
    }
} 