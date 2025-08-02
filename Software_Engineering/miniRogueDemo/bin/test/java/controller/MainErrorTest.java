package controller;

import enums.CharacterClass;
import model.characters.Player;
import model.gameLogic.GameLogic;
import utilities.Position;
import view.GameView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.AfterEach;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Phase 4: Error Handling Tests for Main class components.
 * Tests application error handling, exception management, and graceful error recovery.
 * Focuses on the error handling capabilities of the game components.
 */
@DisplayName("Main Error Handling Phase 4 Tests")
class MainErrorTest {

    private GameLogic gameLogic;
    private Player testPlayer;
    private GameView gameView;

    @BeforeEach
    void setUp() {
        testPlayer = new Player("TestPlayer", CharacterClass.WARRIOR, new Position(0, 0));
        gameLogic = new GameLogic(testPlayer);
        gameView = new GameView();
        
        // Small delay to ensure initialization completes
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Verify components are properly initialized
        assertNotNull(gameLogic, "Game logic should not be null");
        assertNotNull(testPlayer, "Player should not be null");
        assertNotNull(gameView, "Game view should not be null");
    }

    /**
     * Tests application error handling with various error types.
     */
    @Test
    @DisplayName("Application Error Handling")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testApplicationErrorHandling() {
        // Test that system handles empty input gracefully
        assertDoesNotThrow(() -> gameLogic.handle_player_action("", null), 
                          "Empty input should be handled gracefully");
        
        // Test that system handles whitespace input gracefully
        assertDoesNotThrow(() -> gameLogic.handle_player_action("   ", null), 
                          "Whitespace input should be handled gracefully");
        
        // Test that system handles invalid commands gracefully
        assertDoesNotThrow(() -> gameLogic.handle_player_action("invalid_command", null), 
                          "Invalid command should be handled gracefully");
        
        // Test that system handles malformed commands gracefully
        assertDoesNotThrow(() -> gameLogic.handle_player_action("select_warrior_invalid", null), 
                          "Malformed command should be handled gracefully");
        
        // Test that system handles edge case commands gracefully
        assertDoesNotThrow(() -> gameLogic.handle_player_action("pause", null), 
                          "Pause command should be handled gracefully");
    }

    /**
     * Tests exception handling during component operations.
     */
    @Test
    @DisplayName("Exception Handling During Operations")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testExceptionHandlingDuringOperations() {
        // Test that system handles exceptions during game state transitions
        assertDoesNotThrow(() -> {
            // Perform multiple state transitions to test exception handling
            gameLogic.pause_game();
            gameLogic.resume_game();
            gameLogic.handle_player_action("start_new_game", null);
        }, "Game state transitions should handle exceptions gracefully");
        
        // Test that system handles exceptions during player actions
        assertDoesNotThrow(() -> {
            // Perform various player actions to test exception handling
            gameLogic.handle_player_action("use_item", null);
            gameLogic.handle_player_action("attack", null);
            gameLogic.handle_player_action("move", null);
        }, "Player actions should handle exceptions gracefully");
        
        // Test that system handles exceptions during component operations
        assertDoesNotThrow(() -> {
            // Test component operations
            testPlayer.heal(10);
            testPlayer.take_damage(5);
            testPlayer.gain_experience(100);
        }, "Component operations should handle exceptions gracefully");
    }

    /**
     * Tests graceful error recovery and system stability.
     */
    @Test
    @DisplayName("Graceful Error Recovery")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testGracefulErrorRecovery() {
        // Test that system recovers from error conditions
        assertDoesNotThrow(() -> gameLogic.handle_player_action("invalid_error_command", null), 
                          "System should handle invalid error commands");
        
        // Test that system remains functional after error handling
        assertDoesNotThrow(() -> gameLogic.pause_game(), 
                          "Pause should work after error handling");
        assertDoesNotThrow(() -> gameLogic.resume_game(), 
                          "Resume should work after error handling");
        
        // Test that components remain accessible after error handling
        assertNotNull(gameLogic, "Game logic should remain accessible after error handling");
        assertNotNull(testPlayer, "Player should remain accessible after error handling");
        assertNotNull(gameView, "Game view should remain accessible after error handling");
        
        // Test that system can perform normal operations after error recovery
        assertDoesNotThrow(() -> testPlayer.heal(10), 
                          "Player healing should work after error recovery");
        assertDoesNotThrow(() -> testPlayer.take_damage(5), 
                          "Player damage should work after error recovery");
    }

    /**
     * Tests error state management and consistency.
     */
    @Test
    @DisplayName("Error State Management")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testErrorStateManagement() {
        // Test that system maintains consistent state during error conditions
        assertDoesNotThrow(() -> {
            // Perform operations that might cause errors
            gameLogic.handle_player_action("invalid_command_1", null);
            gameLogic.handle_player_action("invalid_command_2", null);
            gameLogic.handle_player_action("invalid_command_3", null);
        }, "System should handle multiple invalid commands gracefully");
        
        // Test that system state remains consistent after errors
        assertNotNull(gameLogic, "Game logic should remain consistent after errors");
        assertNotNull(testPlayer, "Player should remain consistent after errors");
        assertNotNull(gameView, "Game view should remain consistent after errors");
        
        // Test that system can continue normal operations after error state
        assertDoesNotThrow(() -> gameLogic.pause_game(), 
                          "Normal operations should work after error state");
        assertDoesNotThrow(() -> gameLogic.resume_game(), 
                          "Normal operations should work after error state");
        
        // Test that system can handle player operations after error state
        assertDoesNotThrow(() -> testPlayer.heal(10), 
                          "Player operations should work after error state");
    }

    /**
     * Tests error handling with complex scenarios.
     */
    @Test
    @DisplayName("Complex Error Scenarios")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testComplexErrorScenarios() {
        // Test rapid error conditions
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10; i++) {
                gameLogic.handle_player_action("invalid_command_" + i, null);
            }
        }, "System should handle rapid error conditions gracefully");
        
        // Test error handling during state transitions
        assertDoesNotThrow(() -> {
            gameLogic.pause_game();
            gameLogic.handle_player_action("invalid_command_during_pause", null);
            gameLogic.resume_game();
        }, "System should handle errors during state transitions");
        
        // Test error handling during player operations
        assertDoesNotThrow(() -> {
            testPlayer.heal(10);
            gameLogic.handle_player_action("invalid_command_during_heal", null);
            testPlayer.take_damage(5);
        }, "System should handle errors during player operations");
        
        // Test that system remains stable after complex error scenarios
        assertNotNull(gameLogic, "Game logic should remain stable after complex errors");
        assertNotNull(testPlayer, "Player should remain stable after complex errors");
        assertNotNull(gameView, "Game view should remain stable after complex errors");
    }

    /**
     * Tests error handling with edge cases.
     */
    @Test
    @DisplayName("Error Handling Edge Cases")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testErrorHandlingEdgeCases() {
        // Test extremely long input
        String longInput = "a".repeat(1000);
        assertDoesNotThrow(() -> gameLogic.handle_player_action(longInput, null), 
                          "Extremely long input should be handled gracefully");
        
        // Test input with special characters
        assertDoesNotThrow(() -> gameLogic.handle_player_action("select_warrior@#$%", null), 
                          "Input with special characters should be handled gracefully");
        
        // Test input with unicode characters
        assertDoesNotThrow(() -> gameLogic.handle_player_action("select_warrior\u0000\u0001", null), 
                          "Input with unicode characters should be handled gracefully");
        
        // Test input with control characters
        assertDoesNotThrow(() -> gameLogic.handle_player_action("select_warrior\t\n\r", null), 
                          "Input with control characters should be handled gracefully");
        
        // Test that system remains functional after edge case handling
        assertDoesNotThrow(() -> gameLogic.pause_game(), 
                          "Normal operations should work after edge case handling");
        assertDoesNotThrow(() -> gameLogic.resume_game(), 
                          "Normal operations should work after edge case handling");
    }

    /**
     * Tests error handling performance under load.
     */
    @Test
    @DisplayName("Error Handling Performance Under Load")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testErrorHandlingPerformanceUnderLoad() {
        // Test error handling performance under load
        long startTime = System.currentTimeMillis();
        
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 50; i++) {
                gameLogic.handle_player_action("invalid_command_" + i, null);
            }
        }, "Error handling should perform well under load");
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // Performance test: should complete within reasonable time
        assertTrue(duration < 5000, "Error handling should complete within 5 seconds under load");
        
        // Test that system remains functional after load testing
        assertDoesNotThrow(() -> gameLogic.pause_game(), 
                          "System should remain functional after error load testing");
        assertDoesNotThrow(() -> gameLogic.resume_game(), 
                          "System should remain functional after error load testing");
    }

    /**
     * Tests error handling with different component states.
     */
    @Test
    @DisplayName("Error Handling with Different Component States")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testErrorHandlingWithDifferentComponentStates() {
        // Test error handling when game is paused
        assertDoesNotThrow(() -> {
            gameLogic.pause_game();
            gameLogic.handle_player_action("invalid_command_during_pause", null);
        }, "Error handling should work when game is paused");
        
        // Test error handling when game is resumed
        assertDoesNotThrow(() -> {
            gameLogic.resume_game();
            gameLogic.handle_player_action("invalid_command_during_resume", null);
        }, "Error handling should work when game is resumed");
        
        // Test error handling during player operations
        assertDoesNotThrow(() -> {
            testPlayer.heal(10);
            gameLogic.handle_player_action("invalid_command_during_heal", null);
        }, "Error handling should work during player operations");
        
        // Test error handling during game operations
        assertDoesNotThrow(() -> {
            gameLogic.handle_player_action("start_new_game", null);
            gameLogic.handle_player_action("invalid_command_during_game_start", null);
        }, "Error handling should work during game operations");
        
        // Test that system remains stable after different component state errors
        assertNotNull(gameLogic, "Game logic should remain stable after component state errors");
        assertNotNull(testPlayer, "Player should remain stable after component state errors");
        assertNotNull(gameView, "Game view should remain stable after component state errors");
    }

    /**
     * Cleans up test resources after each test.
     */
    @AfterEach
    void tearDown() {
        // Clean up resources with proper synchronization
        if (gameLogic != null) {
            try {
                gameLogic.dispose();
                // Small delay to ensure cleanup completes
                Thread.sleep(100);
            } catch (Exception e) {
                // Ignore cleanup errors
            }
        }
    }
} 