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
 * Simple tests for detecting memory leaks in GameLogic components.
 * Focuses on basic memory management rather than complex stress testing.
 * Appropriate for a school project.
 */
@DisplayName("Simple Memory Leak Detection Tests")
class MemoryLeakDetectionTest {

    private GameLogic gameLogic;
    private Player testPlayer;
    private static final long MEMORY_THRESHOLD = 50 * 1024 * 1024; // 50MB

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
        long initialMemory = getCurrentMemoryUsage();
        
        // Perform basic operations
        gameLogic.get_game_state();
        gameLogic.is_paused();
        gameLogic.getProjectiles();
        gameLogic.get_current_enemies();
        
        // Force garbage collection
        System.gc();
        
        long finalMemory = getCurrentMemoryUsage();
        long memoryIncrease = finalMemory - initialMemory;
        
        // Basic check - memory increase should be reasonable
        assertTrue(memoryIncrease < MEMORY_THRESHOLD, 
            "Memory increase should be reasonable, got: " + formatMemoryUsage(memoryIncrease));
    }

    /**
     * Tests memory usage during observer operations.
     */
    @Test
    @DisplayName("Observer Memory Usage")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testObserverMemoryUsage() {
        long initialMemory = getCurrentMemoryUsage();
        
        // Add and remove observers multiple times
        for (int i = 0; i < 100; i++) {
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
        assertTrue(memoryIncrease < MEMORY_THRESHOLD, 
            "Memory increase should be reasonable after observer operations, got: " + formatMemoryUsage(memoryIncrease));
    }

    /**
     * Tests memory usage during state changes.
     */
    @Test
    @DisplayName("State Changes Memory Usage")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testStateChangesMemoryUsage() {
        long initialMemory = getCurrentMemoryUsage();
        
        // Perform many state changes
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
        assertTrue(memoryIncrease < MEMORY_THRESHOLD, 
            "Memory increase should be reasonable after state changes, got: " + formatMemoryUsage(memoryIncrease));
    }

    /**
     * Tests memory usage over time with multiple operations.
     */
    @Test
    @DisplayName("Memory Consumption Over Time")
    @Timeout(value = 15, unit = TimeUnit.SECONDS)
    void testMemoryConsumptionOverTime() {
        long initialMemory = getCurrentMemoryUsage();
        
        // Perform a mix of operations over time
        for (int i = 0; i < 50; i++) {
            // State operations
            gameLogic.pause_game();
            gameLogic.resume_game();
            
            // Observer operations
            MockGameObserver observer = new MockGameObserver("TimeObserver" + i);
            gameLogic.add_observer(observer);
            gameLogic.notify_observers("time_event", "time_data");
            gameLogic.remove_observer(observer);
            
            // Access operations
            gameLogic.get_game_state();
            gameLogic.getProjectiles();
            gameLogic.get_current_enemies();
        }
        
        // Force garbage collection
        System.gc();
        
        long finalMemory = getCurrentMemoryUsage();
        long memoryIncrease = finalMemory - initialMemory;
        
        // Memory increase should be reasonable
        assertTrue(memoryIncrease < MEMORY_THRESHOLD, 
            "Memory increase should be reasonable over time, got: " + formatMemoryUsage(memoryIncrease));
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