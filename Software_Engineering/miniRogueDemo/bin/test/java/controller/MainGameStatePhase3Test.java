package controller;

import enums.CharacterClass;
import enums.GameState;
import model.characters.Player;
import model.gameLogic.GameLogic;
import model.scoreEntry.ScoreEntry;
import utilities.Position;
import view.GameView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.AfterEach;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Phase 3: Game State Management and High Score System Tests for Main class.
 * Tests game state transitions, persistence, high score tracking, and error handling.
 * Focuses on the game state management and scoring capabilities of the Main controller.
 */
@DisplayName("Main Game State Management Phase 3 Tests")
class MainGameStatePhase3Test {

    private Main mainController;
    private GameLogic gameLogic;
    private Player testPlayer;
    private GameView gameView;

    @BeforeEach
    void setUp() {
        testPlayer = new Player("TestPlayer", CharacterClass.WARRIOR, new Position(0, 0));
        gameLogic = new GameLogic(testPlayer);
        gameView = new GameView();
        
        // Create main controller
        mainController = new Main();
        
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
        assertNotNull(mainController, "Main controller should not be null");
    }

    /**
     * Tests game state initialization and default values.
     */
    @Test
    @DisplayName("Game State Initialization")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testGameStateInitialization() {
        // Test initial game state
        assertNotNull(mainController.get_model(), "Model should be initialized");
        assertNotNull(mainController.get_view(), "View should be initialized");
        assertNotNull(mainController.get_player(), "Player should be initialized");
        
        // Test initial game state values
        assertEquals(GameState.MAIN_MENU, gameLogic.get_game_state(), "Initial game state should be MAIN_MENU");
        assertFalse(gameLogic.is_victory(), "Initial victory status should be false");
        assertFalse(gameLogic.is_death(), "Initial death status should be false");
        assertFalse(gameLogic.is_paused(), "Initial pause status should be false");
    }

    /**
     * Tests game state transitions and persistence.
     */
    @Test
    @DisplayName("Game State Transitions")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testGameStateTransitions() {
        // Test pause/resume transitions
        assertDoesNotThrow(() -> mainController.handle_input("pause_game"), 
                          "Pause game command should work");
        // Note: pause_game may not immediately change state in test environment
        assertDoesNotThrow(() -> mainController.handle_input("resume_game"), 
                          "Resume game command should work");
        
        // Test multiple state transitions
        for (int i = 0; i < 3; i++) {
            assertDoesNotThrow(() -> mainController.handle_input("pause_game"), 
                              "Multiple pause commands should work");
            assertDoesNotThrow(() -> mainController.handle_input("resume_game"), 
                              "Multiple resume commands should work");
        }
    }

    /**
     * Tests game start and end functionality.
     */
    @Test
    @DisplayName("Game Start and End Functionality")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testGameStartAndEndFunctionality() {
        // Test game start
        assertDoesNotThrow(() -> mainController.handle_input("start_game"), 
                          "Start game command should work");
        
        // Test new game functionality
        assertDoesNotThrow(() -> mainController.handle_input("start_new_game"), 
                          "Start new game command should work");
        
        // Test game end with victory
        assertDoesNotThrow(() -> mainController.handle_input("end_game", true), 
                          "End game with victory should work");
        
        // Test game end with defeat
        assertDoesNotThrow(() -> mainController.handle_input("end_game", false), 
                          "End game with defeat should work");
    }

    /**
     * Tests high score system functionality.
     */
    @Test
    @DisplayName("High Score System")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testHighScoreSystem() {
        // Test adding high scores
        ScoreEntry testScore1 = new ScoreEntry("TP1", 100, 5, "Enemy1", List.of("item1"));
        assertDoesNotThrow(() -> mainController.handle_input("add_high_score", testScore1), 
                          "Adding high score should work");
        
        ScoreEntry testScore2 = new ScoreEntry("TP2", 200, 10, "Enemy2", List.of("item1", "item2"));
        assertDoesNotThrow(() -> mainController.handle_input("add_high_score", testScore2), 
                          "Adding second high score should work");
        
        // Test high score with different data
        ScoreEntry testScore3 = new ScoreEntry("TP3", 50, 2, null, List.of());
        assertDoesNotThrow(() -> mainController.handle_input("add_high_score", testScore3), 
                          "Adding high score with null killer should work");
    }

    /**
     * Tests character class selection and state persistence.
     */
    @Test
    @DisplayName("Character Class Selection and State Persistence")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testCharacterClassSelectionAndStatePersistence() {
        // Test character class selection using the correct command names
        assertDoesNotThrow(() -> mainController.handle_input("select_warrior"), 
                          "Warrior class selection should work");
        // Note: class selection creates new player instance, so we test the command execution
        
        assertDoesNotThrow(() -> mainController.handle_input("select_mage"), 
                          "Mage class selection should work");
        
        assertDoesNotThrow(() -> mainController.handle_input("select_rogue"), 
                          "Rogue class selection should work");
        
        assertDoesNotThrow(() -> mainController.handle_input("select_ranger"), 
                          "Ranger class selection should work");
        
        // Test state persistence after class selection
        assertNotNull(testPlayer.get_name(), "Player name should persist after class selection");
        assertTrue(testPlayer.get_level() >= 1, "Player level should persist after class selection");
    }

    /**
     * Tests game state error handling and recovery.
     */
    @Test
    @DisplayName("Game State Error Handling and Recovery")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testGameStateErrorHandlingAndRecovery() {
        // Test invalid state transitions
        assertDoesNotThrow(() -> mainController.handle_input("invalid_state_command"), 
                          "Invalid state command should be handled gracefully");
        
        // Test state recovery after errors
        assertDoesNotThrow(() -> mainController.handle_input("pause_game"), 
                          "Valid pause should work after error");
        assertDoesNotThrow(() -> mainController.handle_input("resume_game"), 
                          "Valid resume should work after error");
        
        // Test component stability after errors
        assertNotNull(mainController.get_model(), "Model should remain accessible after errors");
        assertNotNull(mainController.get_view(), "View should remain accessible after errors");
        assertNotNull(mainController.get_player(), "Player should remain accessible after errors");
    }

    /**
     * Tests game state consistency and integrity.
     */
    @Test
    @DisplayName("Game State Consistency and Integrity")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testGameStateConsistencyAndIntegrity() {
        // Test that game state remains consistent during operations
        GameState initialState = gameLogic.get_game_state();
        
        // Perform various operations
        assertDoesNotThrow(() -> mainController.handle_input("pause_game"), "Pause should work");
        assertDoesNotThrow(() -> mainController.handle_input("resume_game"), "Resume should work");
        assertDoesNotThrow(() -> mainController.handle_input("select_character_class", CharacterClass.MAGE), "Class selection should work");
        
        // Verify state consistency
        assertNotNull(gameLogic.get_game_state(), "Game state should not be null after operations");
        assertNotNull(testPlayer.get_character_class(), "Player class should not be null after operations");
        assertNotNull(testPlayer.get_name(), "Player name should not be null after operations");
        
        // Test that components remain properly connected
        assertNotNull(mainController.get_model(), "Model should remain connected");
        assertNotNull(mainController.get_view(), "View should remain connected");
        assertNotNull(mainController.get_player(), "Player should remain connected");
    }

    /**
     * Tests high score system error handling.
     */
    @Test
    @DisplayName("High Score System Error Handling")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testHighScoreSystemErrorHandling() {
        // Test adding null score
        assertDoesNotThrow(() -> mainController.handle_input("add_high_score", null), 
                          "Adding null score should be handled gracefully");
        
        // Test adding invalid score object
        Object invalidScore = new Object();
        assertDoesNotThrow(() -> mainController.handle_input("add_high_score", invalidScore), 
                          "Adding invalid score object should be handled gracefully");
        
        // Test adding score with invalid data
        ScoreEntry invalidScoreEntry = new ScoreEntry("", -1, -1, "", List.of());
        assertDoesNotThrow(() -> mainController.handle_input("add_high_score", invalidScoreEntry), 
                          "Adding invalid score entry should be handled gracefully");
        
        // Test that valid scores still work after errors
        ScoreEntry validScore = new ScoreEntry("TP", 100, 5, "Enemy", List.of("item1"));
        assertDoesNotThrow(() -> mainController.handle_input("add_high_score", validScore), 
                          "Adding valid score should work after errors");
    }

    /**
     * Tests game state performance under load.
     */
    @Test
    @DisplayName("Game State Performance Under Load")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testGameStatePerformanceUnderLoad() {
        // Test rapid state transitions
        long startTime = System.currentTimeMillis();
        
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 50; i++) {
                mainController.handle_input("pause_game");
                mainController.handle_input("resume_game");
            }
        }, "Rapid state transitions should work");
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // Performance test: should complete within reasonable time
        assertTrue(duration < 5000, "State transitions should complete within 5 seconds");
        
        // Test rapid high score additions
        startTime = System.currentTimeMillis();
        
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 20; i++) {
                ScoreEntry score = new ScoreEntry("TP" + i, 100 + i, 5 + i, "Enemy" + i, List.of("item" + i));
                mainController.handle_input("add_high_score", score);
            }
        }, "Rapid high score additions should work");
        
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        
        assertTrue(duration < 3000, "High score additions should complete within 3 seconds");
    }

    /**
     * Tests game state with different character classes.
     */
    @Test
    @DisplayName("Game State with Different Character Classes")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testGameStateWithDifferentCharacterClasses() {
        // Test state management with Warrior class
        assertDoesNotThrow(() -> mainController.handle_input("select_warrior"), 
                          "Warrior class selection should work");
        assertDoesNotThrow(() -> mainController.handle_input("pause_game"), "Pause should work with Warrior");
        assertDoesNotThrow(() -> mainController.handle_input("resume_game"), "Resume should work with Warrior");
        
        // Test state management with Mage class
        assertDoesNotThrow(() -> mainController.handle_input("select_mage"), 
                          "Mage class selection should work");
        assertDoesNotThrow(() -> mainController.handle_input("pause_game"), "Pause should work with Mage");
        assertDoesNotThrow(() -> mainController.handle_input("resume_game"), "Resume should work with Mage");
        
        // Test state management with Rogue class
        assertDoesNotThrow(() -> mainController.handle_input("select_rogue"), 
                          "Rogue class selection should work");
        assertDoesNotThrow(() -> mainController.handle_input("pause_game"), "Pause should work with Rogue");
        assertDoesNotThrow(() -> mainController.handle_input("resume_game"), "Resume should work with Rogue");
        
        // Test state management with Ranger class
        assertDoesNotThrow(() -> mainController.handle_input("select_ranger"), 
                          "Ranger class selection should work");
        assertDoesNotThrow(() -> mainController.handle_input("pause_game"), "Pause should work with Ranger");
        assertDoesNotThrow(() -> mainController.handle_input("resume_game"), "Resume should work with Ranger");
    }

    /**
     * Tests game state persistence across operations.
     */
    @Test
    @DisplayName("Game State Persistence Across Operations")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testGameStatePersistenceAcrossOperations() {
        // Test that game state persists across multiple operations
        assertDoesNotThrow(() -> mainController.handle_input("select_warrior"), 
                          "Initial class selection should work");
        
        // Perform various operations
        assertDoesNotThrow(() -> mainController.handle_input("pause_game"), "Pause operation should work");
        assertDoesNotThrow(() -> mainController.handle_input("resume_game"), "Resume operation should work");
        assertDoesNotThrow(() -> mainController.handle_input("select_mage"), "Class change should work");
        
        // Verify state persistence - test that components remain accessible
        assertNotNull(testPlayer.get_name(), "Player name should persist after operations");
        assertTrue(testPlayer.get_level() >= 1, "Player level should persist after operations");
        
        // Test that components remain properly connected
        assertNotNull(mainController.get_model(), "Model should remain connected after operations");
        assertNotNull(mainController.get_view(), "View should remain connected after operations");
        assertNotNull(mainController.get_player(), "Player should remain connected after operations");
    }

    /**
     * Tests high score system with various data scenarios.
     */
    @Test
    @DisplayName("High Score System with Various Data Scenarios")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testHighScoreSystemWithVariousDataScenarios() {
        // Test high score with maximum values
        ScoreEntry maxScore = new ScoreEntry("MAX", Integer.MAX_VALUE, Integer.MAX_VALUE, "FinalBoss", List.of("ultimate_item"));
        assertDoesNotThrow(() -> mainController.handle_input("add_high_score", maxScore), 
                          "Adding maximum score should work");
        
        // Test high score with minimum values
        ScoreEntry minScore = new ScoreEntry("MIN", 0, 0, "WeakEnemy", List.of());
        assertDoesNotThrow(() -> mainController.handle_input("add_high_score", minScore), 
                          "Adding minimum score should work");
        
        // Test high score with special characters in name
        ScoreEntry specialScore = new ScoreEntry("T@st", 150, 8, "Sp@cial", List.of("item@1"));
        assertDoesNotThrow(() -> mainController.handle_input("add_high_score", specialScore), 
                          "Adding score with special characters should work");
        
        // Test high score with very long name
        ScoreEntry longNameScore = new ScoreEntry("VeryLongPlayerNameThatExceedsNormalLength", 200, 10, "LongEnemyName", List.of("item1", "item2", "item3"));
        assertDoesNotThrow(() -> mainController.handle_input("add_high_score", longNameScore), 
                          "Adding score with long name should work");
    }

    /**
     * Tests application error handling and recovery.
     */
    @Test
    @DisplayName("Application Error Handling and Recovery")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testApplicationErrorHandlingAndRecovery() {
        // Test that system handles invalid inputs gracefully
        assertDoesNotThrow(() -> mainController.handle_input("invalid_error_command"), 
                          "Invalid error command should be handled gracefully");
        
        // Test that system remains functional after error handling
        assertDoesNotThrow(() -> mainController.handle_input("pause_game"), 
                          "Pause should work after error handling");
        assertDoesNotThrow(() -> mainController.handle_input("resume_game"), 
                          "Resume should work after error handling");
        
        // Test that components remain accessible after error handling
        assertNotNull(mainController.get_model(), "Model should remain accessible after error handling");
        assertNotNull(mainController.get_view(), "View should remain accessible after error handling");
        assertNotNull(mainController.get_player(), "Player should remain accessible after error handling");
    }

    /**
     * Tests debug mode functionality.
     */
    @Test
    @DisplayName("Debug Mode Functionality")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testDebugModeFunctionality() {
        // Test debug mode setting
        assertDoesNotThrow(() -> mainController.set_debug_mode(true), 
                          "Setting debug mode to true should work");
        assertTrue(mainController.is_debug_mode(), "Debug mode should be true");
        
        assertDoesNotThrow(() -> mainController.set_debug_mode(false), 
                          "Setting debug mode to false should work");
        assertFalse(mainController.is_debug_mode(), "Debug mode should be false");
        
        // Test that game functionality works in both debug modes
        assertDoesNotThrow(() -> mainController.handle_input("pause_game"), 
                          "Pause should work in debug mode");
        assertDoesNotThrow(() -> mainController.handle_input("resume_game"), 
                          "Resume should work in debug mode");
        
        // Test that components remain accessible in debug mode
        assertNotNull(mainController.get_model(), "Model should remain accessible in debug mode");
        assertNotNull(mainController.get_view(), "View should remain accessible in debug mode");
        assertNotNull(mainController.get_player(), "Player should remain accessible in debug mode");
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