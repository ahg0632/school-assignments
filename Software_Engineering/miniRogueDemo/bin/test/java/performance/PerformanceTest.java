package performance;

import model.gameLogic.GameLogic;
import model.characters.Player;
import model.map.Map;
import view.GameView;
import controller.MainController;
import enums.CharacterClass;
import enums.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.util.concurrent.TimeUnit;

/**
 * Performance tests for the miniRogueDemo game.
 * Focuses on basic performance metrics and memory management.
 * Appropriate for school project scope - no stress testing.
 */
public class PerformanceTest {
    
    private GameLogic gameLogic;
    private GameView gameView;
    private MainController controller;
    private Player player;
    private Map map;
    
    @BeforeEach
    void setUp() {
        // Initialize game components for performance testing
        player = new Player("TestPlayer", CharacterClass.WARRIOR, new utilities.Position(5, 5));
        gameLogic = new GameLogic(player);
        gameView = new GameView();
        controller = new MainController(gameLogic, gameView);
        
        // Create a test map
        map = new Map(1, model.map.Map.FloorType.REGULAR);
        // Note: GameLogic manages map internally, no need to set it explicitly
    }
    
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testGameInitializationPerformance() {
        // Test that game initialization completes within reasonable time
        long startTime = System.currentTimeMillis();
        
        // Initialize game components
        gameLogic.startGame();
        controller.initialize();
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // Should complete within 2 seconds for a school project
        assert duration < 2000 : "Game initialization took too long: " + duration + "ms";
    }
    
    @Test
    @Timeout(value = 3, unit = TimeUnit.SECONDS)
    void testMapGenerationPerformance() {
        // Test map generation performance
        long startTime = System.currentTimeMillis();
        
        Map newMap = new Map(1, model.map.Map.FloorType.REGULAR);
        newMap.generate_dungeon();
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // Map generation should complete within 1 second
        assert duration < 1000 : "Map generation took too long: " + duration + "ms";
    }
    
    @Test
    @Timeout(value = 2, unit = TimeUnit.SECONDS)
    void testPlayerMovementPerformance() {
        // Test player movement performance
        long startTime = System.currentTimeMillis();
        
        // Perform multiple movement operations
        for (int i = 0; i < 100; i++) {
            player.setMoveDirection(1, 0); // Move right
            player.update_movement(map);
            player.setMoveDirection(0, 1); // Move down
            player.update_movement(map);
            player.setMoveDirection(-1, 0); // Move left
            player.update_movement(map);
            player.setMoveDirection(0, -1); // Move up
            player.update_movement(map);
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // 100 movement operations should complete within 500ms
        assert duration < 500 : "Player movement took too long: " + duration + "ms";
    }
    
    @Test
    @Timeout(value = 3, unit = TimeUnit.SECONDS)
    void testCombatPerformance() {
        // Test combat system performance
        long startTime = System.currentTimeMillis();
        
        // Simulate combat operations
        for (int i = 0; i < 50; i++) {
            player.attack(player); // Attack self for testing
            player.take_damage(10);
            player.add_mp(5); // Use add_mp instead of heal
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // 50 combat operations should complete within 1 second
        assert duration < 1000 : "Combat operations took too long: " + duration + "ms";
    }
    
    @Test
    @Timeout(value = 2, unit = TimeUnit.SECONDS)
    void testStateTransitionPerformance() {
        // Test game state transition performance
        long startTime = System.currentTimeMillis();
        
        // Perform multiple state transitions
        for (int i = 0; i < 20; i++) {
            controller.handleStateTransition(GameState.PLAYING);
            controller.handleStateTransition(GameState.PAUSED);
            controller.handleStateTransition(GameState.MAIN_MENU);
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // 20 state transitions should complete within 500ms
        assert duration < 500 : "State transitions took too long: " + duration + "ms";
    }
    
    @Test
    @Timeout(value = 3, unit = TimeUnit.SECONDS)
    void testMemoryUsage() {
        // Test basic memory usage patterns
        Runtime runtime = Runtime.getRuntime();
        
        // Force garbage collection to get baseline
        System.gc();
        long initialMemory = runtime.totalMemory() - runtime.freeMemory();
        
        // Perform operations that might allocate memory
        for (int i = 0; i < 10; i++) {
            Map testMap = new Map(1, model.map.Map.FloorType.REGULAR);
            testMap.generate_dungeon();
            
            Player testPlayer = new Player("TestPlayer", CharacterClass.MAGE, new utilities.Position(0, 0));
            testPlayer.gain_experience(1000);
        }
        
        // Force garbage collection again
        System.gc();
        long finalMemory = runtime.totalMemory() - runtime.freeMemory();
        long memoryIncrease = finalMemory - initialMemory;
        
        // Memory increase should be reasonable (less than 10MB for school project)
        assert memoryIncrease < 10 * 1024 * 1024 : 
            "Memory usage increased too much: " + (memoryIncrease / 1024 / 1024) + "MB";
    }
    
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testRenderingPerformance() {
        // Test basic rendering performance
        long startTime = System.currentTimeMillis();
        
        // Simulate rendering operations
        for (int i = 0; i < 60; i++) { // Simulate 1 second at 60 FPS
            gameView.repaint();
            try {
                Thread.sleep(16); // ~60 FPS
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // Rendering should complete within 2 seconds
        assert duration < 2000 : "Rendering took too long: " + duration + "ms";
    }
} 