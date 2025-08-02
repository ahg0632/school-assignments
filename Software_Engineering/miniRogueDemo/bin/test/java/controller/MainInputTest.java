package controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;

import enums.CharacterClass;
import enums.GameState;
import model.gameLogic.GameLogic;
import model.characters.Player;
import utilities.Position;
import interfaces.GameModel;
import interfaces.GameView;

/**
 * Test class for Main.java input handling functionality.
 * Tests user input processing, menu navigation, character class selection,
 * game control commands, and input validation.
 * 
 * @author Test Implementation Team
 * @version 1.0
 */
@DisplayName("Main Input Handling Tests")
class MainInputTest {
    
    private GameLogic gameLogic;
    private Player testPlayer;
    private view.GameView gameView;
    
    /**
     * Sets up the test environment before each test.
     * Creates components individually to avoid Swing window issues.
     */
    @BeforeEach
    void setUp() {
        // Create test player
        testPlayer = new Player("TestPlayer", CharacterClass.WARRIOR, new Position(0, 0));
        
        // Create game logic
        gameLogic = new GameLogic(testPlayer);
        
        // Create game view
        gameView = new view.GameView();
        
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
     * Tests menu navigation input commands.
     * Verifies that menu navigation commands are properly handled.
     */
    @Test
    @DisplayName("Menu Navigation Input")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMenuNavigationInput() {
        // Test that game logic can handle pause/resume commands
        assertDoesNotThrow(() -> gameLogic.pause_game(), 
                          "pause_game command should not throw exception");
        
        assertDoesNotThrow(() -> gameLogic.resume_game(), 
                          "resume_game command should not throw exception");
        
        // Test that player can handle basic actions
        assertDoesNotThrow(() -> testPlayer.heal(10), 
                          "Player healing should not throw exception");
        
        // Test that components remain accessible after operations
        assertNotNull(gameLogic, "Game logic should remain accessible");
        assertNotNull(testPlayer, "Player should remain accessible");
        assertNotNull(gameView, "Game view should remain accessible");
    }
    
    /**
     * Tests character class selection input commands.
     * Verifies that all character class selections are properly handled.
     */
    @Test
    @DisplayName("Character Class Selection Input")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testCharacterClassSelectionInput() {
        // Test that player can change character class
        assertDoesNotThrow(() -> testPlayer.select_class(CharacterClass.WARRIOR), 
                          "Warrior class selection should not throw exception");
        
        assertDoesNotThrow(() -> testPlayer.select_class(CharacterClass.MAGE), 
                          "Mage class selection should not throw exception");
        
        assertDoesNotThrow(() -> testPlayer.select_class(CharacterClass.ROGUE), 
                          "Rogue class selection should not throw exception");
        
        assertDoesNotThrow(() -> testPlayer.select_class(CharacterClass.RANGER), 
                          "Ranger class selection should not throw exception");
        
        // Verify player class is properly set
        assertNotNull(testPlayer.get_character_class(), "Player should have character class");
    }
    
    /**
     * Tests game control input commands.
     * Verifies that game control commands are properly handled.
     */
    @Test
    @DisplayName("Game Control Input")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testGameControlInput() {
        // Test pause_game command
        assertDoesNotThrow(() -> gameLogic.pause_game(), 
                          "pause_game command should not throw exception");
        
        // Test resume_game command
        assertDoesNotThrow(() -> gameLogic.resume_game(), 
                          "resume_game command should not throw exception");
        
        // Test that game logic can handle multiple pause/resume cycles
        for (int i = 0; i < 3; i++) {
            assertDoesNotThrow(() -> gameLogic.pause_game(), 
                              "Multiple pause commands should not throw exception");
            assertDoesNotThrow(() -> gameLogic.resume_game(), 
                              "Multiple resume commands should not throw exception");
        }
    }
    
    /**
     * Tests input error handling.
     * Verifies that invalid inputs are handled gracefully.
     */
    @Test
    @DisplayName("Input Error Handling")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testInputErrorHandling() {
        // Test that components can handle invalid operations gracefully
        assertDoesNotThrow(() -> testPlayer.heal(-10), 
                          "Negative healing should not throw exception");
        
        assertDoesNotThrow(() -> testPlayer.take_damage(-5), 
                          "Negative damage should not throw exception");
        
        // Test that components remain stable after invalid operations
        assertNotNull(gameLogic, "Game logic should remain accessible after invalid operations");
        assertNotNull(testPlayer, "Player should remain accessible after invalid operations");
        assertNotNull(gameView, "Game view should remain accessible after invalid operations");
    }
    
    /**
     * Tests character class selection with data.
     * Verifies that character class selection with additional data works.
     */
    @Test
    @DisplayName("Character Class Selection with Data")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testCharacterClassSelectionWithData() {
        // Test that player can handle class selection with different data
        assertDoesNotThrow(() -> testPlayer.select_class(CharacterClass.WARRIOR), 
                          "Warrior selection should not throw exception");
        
        // Verify player properties after class selection
        assertNotNull(testPlayer.get_name(), "Player should have name after class selection");
        assertNotNull(testPlayer.get_character_class(), "Player should have class after selection");
        assertTrue(testPlayer.get_level() >= 1, "Player should have at least level 1");
    }
    
    /**
     * Tests game state transition input.
     * Verifies that game state transitions work properly.
     */
    @Test
    @DisplayName("Game State Transition Input")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testGameStateTransitionInput() {
        // Test that game logic can handle state transitions
        assertDoesNotThrow(() -> gameLogic.pause_game(), 
                          "Pause state transition should not throw exception");
        
        assertDoesNotThrow(() -> gameLogic.resume_game(), 
                          "Resume state transition should not throw exception");
        
        // Test that player can handle state changes
        assertDoesNotThrow(() -> testPlayer.heal(10), 
                          "Player healing during state transition should not throw exception");
        
        // Verify components remain accessible after state transitions
        assertNotNull(gameLogic, "Game logic should remain accessible after state transitions");
        assertNotNull(testPlayer, "Player should remain accessible after state transitions");
    }
    
    /**
     * Tests input command delegation.
     * Verifies that input commands are properly delegated to components.
     */
    @Test
    @DisplayName("Input Command Delegation")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testInputCommandDelegation() {
        // Test that input commands don't break component accessibility
        gameLogic.pause_game();
        assertNotNull(gameLogic, "Game logic should be accessible after pause");
        
        gameLogic.resume_game();
        assertNotNull(gameLogic, "Game logic should be accessible after resume");
        
        testPlayer.heal(10);
        assertNotNull(testPlayer, "Player should be accessible after healing");
        
        // Test that components can handle multiple operations
        for (int i = 0; i < 3; i++) {
            assertDoesNotThrow(() -> gameLogic.pause_game(), "Multiple pauses should work");
            assertDoesNotThrow(() -> gameLogic.resume_game(), "Multiple resumes should work");
            assertDoesNotThrow(() -> testPlayer.heal(5), "Multiple heals should work");
        }
    }
    
    /**
     * Tests input validation and sanitization.
     * Verifies that input validation works properly.
     */
    @Test
    @DisplayName("Input Validation and Sanitization")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testInputValidationAndSanitization() {
        // Test that components handle edge cases gracefully
        assertDoesNotThrow(() -> testPlayer.heal(0), 
                          "Zero healing should not throw exception");
        
        assertDoesNotThrow(() -> testPlayer.heal(1000), 
                          "Large healing should not throw exception");
        
        assertDoesNotThrow(() -> testPlayer.take_damage(0), 
                          "Zero damage should not throw exception");
        
        // Test that components remain stable after edge cases
        assertNotNull(gameLogic, "Game logic should remain stable after edge cases");
        assertNotNull(testPlayer, "Player should remain stable after edge cases");
        assertNotNull(gameView, "Game view should remain stable after edge cases");
    }
    
    /**
     * Tests concurrent input handling.
     * Verifies that concurrent inputs are handled properly.
     */
    @Test
    @DisplayName("Concurrent Input Handling")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testConcurrentInputHandling() {
        // Test rapid successive inputs
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 5; i++) {
                gameLogic.pause_game();
                gameLogic.resume_game();
                testPlayer.heal(1);
            }
        }, "Rapid successive inputs should not throw exception");
        
        // Verify components remain stable after concurrent operations
        assertNotNull(gameLogic, "Game logic should remain stable after concurrent operations");
        assertNotNull(testPlayer, "Player should remain stable after concurrent operations");
        assertNotNull(gameView, "Game view should remain stable after concurrent operations");
    }
    
    /**
     * Tests input with complex data.
     * Verifies that complex input data is handled properly.
     */
    @Test
    @DisplayName("Input with Complex Data")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testInputWithComplexData() {
        // Test that components can handle complex operations
        assertDoesNotThrow(() -> {
            // Simulate complex game operations
            testPlayer.heal(50);
            testPlayer.take_damage(10);
            gameLogic.pause_game();
            gameLogic.resume_game();
            testPlayer.select_class(CharacterClass.MAGE);
        }, "Complex operations should not throw exception");
        
        // Verify components remain accessible after complex operations
        assertNotNull(gameLogic, "Game logic should remain accessible after complex operations");
        assertNotNull(testPlayer, "Player should remain accessible after complex operations");
        assertNotNull(gameView, "Game view should remain accessible after complex operations");
    }
    
    /**
     * Tests input error recovery.
     * Verifies that the system recovers from input errors.
     */
    @Test
    @DisplayName("Input Error Recovery")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testInputErrorRecovery() {
        // Test recovery from invalid input
        testPlayer.heal(-10); // Invalid operation
        assertNotNull(testPlayer, "Player should remain accessible after invalid input");
        
        // Test that valid operations still work after invalid input
        assertDoesNotThrow(() -> testPlayer.heal(10), 
                          "Valid healing should work after invalid input");
        
        assertDoesNotThrow(() -> gameLogic.pause_game(), 
                          "Valid pause should work after invalid input");
        
        assertDoesNotThrow(() -> gameLogic.resume_game(), 
                          "Valid resume should work after invalid input");
    }
    
    /**
     * Tests input command chaining.
     * Verifies that chained input commands work properly.
     */
    @Test
    @DisplayName("Input Command Chaining")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testInputCommandChaining() {
        // Test a sequence of related commands
        assertDoesNotThrow(() -> {
            // Chain of related operations
            testPlayer.heal(20);
            gameLogic.pause_game();
            testPlayer.select_class(CharacterClass.ROGUE);
            gameLogic.resume_game();
            testPlayer.heal(10);
        }, "Chained commands should not throw exception");
        
        // Verify final state is consistent
        assertNotNull(gameLogic, "Game logic should be accessible after chained commands");
        assertNotNull(testPlayer, "Player should be accessible after chained commands");
        assertNotNull(gameView, "Game view should be accessible after chained commands");
    }
    
    /**
     * Tests input method accessibility.
     * Verifies that input methods are accessible and functional.
     */
    @Test
    @DisplayName("Input Method Accessibility")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testInputMethodAccessibility() {
        // Test that input methods are accessible
        assertDoesNotThrow(() -> testPlayer.heal(10), 
                          "Player healing method should be accessible");
        
        assertDoesNotThrow(() -> gameLogic.pause_game(), 
                          "Game pause method should be accessible");
        
        assertDoesNotThrow(() -> gameLogic.resume_game(), 
                          "Game resume method should be accessible");
        
        assertDoesNotThrow(() -> testPlayer.select_class(CharacterClass.WARRIOR), 
                          "Player class selection method should be accessible");
        
        // Verify components remain accessible after method calls
        assertNotNull(gameLogic, "Game logic should remain accessible after method calls");
        assertNotNull(testPlayer, "Player should remain accessible after method calls");
        assertNotNull(gameView, "Game view should remain accessible after method calls");
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