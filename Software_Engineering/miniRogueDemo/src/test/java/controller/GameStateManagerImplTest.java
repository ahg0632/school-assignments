package controller;

import enums.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;

import java.util.Stack;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for GameStateManagerImpl class.
 * Tests state transitions, state history management, and state query methods.
 * Appropriate for a school project.
 */
@DisplayName("GameStateManagerImpl Tests")
class GameStateManagerImplTest {

    private GameStateManagerImpl stateManager;

    @BeforeEach
    void setUp() {
        stateManager = new GameStateManagerImpl();
    }

    /**
     * Tests state transitions and validation.
     */
    @Test
    @DisplayName("State Transitions and Validation")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testStateTransitions() {
        // Test initial state
        assertEquals(GameState.MAIN_MENU, stateManager.getCurrentState(), 
                   "Initial state should be MAIN_MENU");
        
        // Test state transitions
        stateManager.setState(GameState.PLAYING);
        assertEquals(GameState.PLAYING, stateManager.getCurrentState(), 
                   "State should transition to PLAYING");
        
        stateManager.setState(GameState.PAUSED);
        assertEquals(GameState.PAUSED, stateManager.getCurrentState(), 
                   "State should transition to PAUSED");
        
        stateManager.setState(GameState.GAME_OVER);
        assertEquals(GameState.GAME_OVER, stateManager.getCurrentState(), 
                   "State should transition to GAME_OVER");
        
        stateManager.setState(GameState.MAIN_MENU);
        assertEquals(GameState.MAIN_MENU, stateManager.getCurrentState(), 
                   "State should transition back to MAIN_MENU");
    }

    /**
     * Tests state history management.
     */
    @Test
    @DisplayName("State History Management")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testStateHistory() {
        // Test that state history is maintained
        GameState initialState = stateManager.getCurrentState();
        
        stateManager.setState(GameState.PLAYING);
        assertEquals(GameState.PLAYING, stateManager.getCurrentState(), 
                   "Current state should be PLAYING");
        
        stateManager.setState(GameState.PAUSED);
        assertEquals(GameState.PAUSED, stateManager.getCurrentState(), 
                   "Current state should be PAUSED");
        
        // Test that previous state is maintained
        assertEquals(GameState.PLAYING, stateManager.getPreviousState(), 
                   "Previous state should be PLAYING");
        
        // Test state history stack
        Stack<GameState> history = stateManager.getStateHistory();
        assertNotNull(history, "State history should not be null");
    }

    /**
     * Tests state query methods.
     */
    @Test
    @DisplayName("State Query Methods")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testStateQueries() {
        // Test initial state queries
        assertTrue(stateManager.isInState(GameState.MAIN_MENU), 
                  "Should be in MAIN_MENU state initially");
        assertFalse(stateManager.isPlaying(), "Should not be playing initially");
        assertFalse(stateManager.isPaused(), "Should not be paused initially");
        assertTrue(stateManager.isInMenu(), "Should be in menu initially");
        assertFalse(stateManager.isGameOver(), "Should not be game over initially");
        
        // Test state queries after transitions
        stateManager.setState(GameState.PLAYING);
        assertTrue(stateManager.isInState(GameState.PLAYING), 
                  "Should be in PLAYING state");
        assertTrue(stateManager.isPlaying(), "Should be playing");
        assertFalse(stateManager.isPaused(), "Should not be paused");
        assertFalse(stateManager.isInMenu(), "Should not be in menu");
        assertFalse(stateManager.isGameOver(), "Should not be game over");
        
        stateManager.setState(GameState.PAUSED);
        assertTrue(stateManager.isInState(GameState.PAUSED), 
                  "Should be in PAUSED state");
        assertFalse(stateManager.isPlaying(), "Should not be playing");
        assertTrue(stateManager.isPaused(), "Should be paused");
        assertFalse(stateManager.isInMenu(), "Should not be in menu");
        assertFalse(stateManager.isGameOver(), "Should not be game over");
        
        stateManager.setState(GameState.GAME_OVER);
        assertTrue(stateManager.isInState(GameState.GAME_OVER), 
                  "Should be in GAME_OVER state");
        assertFalse(stateManager.isPlaying(), "Should not be playing");
        assertFalse(stateManager.isPaused(), "Should not be paused");
        assertFalse(stateManager.isInMenu(), "Should not be in menu");
        assertTrue(stateManager.isGameOver(), "Should be game over");
        
        stateManager.setState(GameState.MAIN_MENU);
        assertTrue(stateManager.isInState(GameState.MAIN_MENU), 
                  "Should be in MAIN_MENU state");
        assertFalse(stateManager.isPlaying(), "Should not be playing");
        assertFalse(stateManager.isPaused(), "Should not be paused");
        assertTrue(stateManager.isInMenu(), "Should be in menu");
        assertFalse(stateManager.isGameOver(), "Should not be game over");
    }

    /**
     * Tests state reversion functionality.
     */
    @Test
    @DisplayName("State Reversion")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testStateRevert() {
        // Test initial state (no previous state to revert to)
        assertFalse(stateManager.revertToPreviousState(), 
                   "Should not be able to revert when no previous state exists");
        
        // Test state reversion after transitions
        stateManager.setState(GameState.PLAYING);
        assertEquals(GameState.PLAYING, stateManager.getCurrentState(), 
                   "Current state should be PLAYING");
        
        stateManager.setState(GameState.PAUSED);
        assertEquals(GameState.PAUSED, stateManager.getCurrentState(), 
                   "Current state should be PAUSED");
        
        // Test reversion
        assertTrue(stateManager.revertToPreviousState(), 
                  "Should be able to revert to previous state");
        assertEquals(GameState.PLAYING, stateManager.getCurrentState(), 
                   "Should revert to PLAYING state");
        
        // Test multiple reversions
        assertTrue(stateManager.revertToPreviousState(), 
                  "Should be able to revert again");
        assertEquals(GameState.MAIN_MENU, stateManager.getCurrentState(), 
                   "Should revert to MAIN_MENU state");
        
        // Test that no more reversions are possible
        assertFalse(stateManager.revertToPreviousState(), 
                   "Should not be able to revert when no more previous states exist");
    }



    /**
     * Tests edge cases and boundary conditions.
     */
    @Test
    @DisplayName("Edge Cases and Boundary Conditions")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testEdgeCases() {
        // Test setting same state (should not change anything)
        GameState currentState = stateManager.getCurrentState();
        stateManager.setState(currentState);
        assertEquals(currentState, stateManager.getCurrentState(), 
                   "Setting same state should not change current state");
        
        // Test setting null state
        assertDoesNotThrow(() -> {
            stateManager.setState(null);
        }, "Setting null state should not throw exception");
        
        // Test that current state remains unchanged when setting null
        assertEquals(currentState, stateManager.getCurrentState(), 
                   "Current state should remain unchanged when setting null");
        
        // Test state queries with null
        assertFalse(stateManager.isInState(null), 
                   "Should not be in null state");
    }

    /**
     * Tests state history clearing.
     */
    @Test
    @DisplayName("State History Clearing")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testStateHistoryClearing() {
        // Set up some state transitions
        stateManager.setState(GameState.PLAYING);
        stateManager.setState(GameState.PAUSED);
        stateManager.setState(GameState.GAME_OVER);
        
        // Verify that previous state exists
        assertNotNull(stateManager.getPreviousState(), 
                    "Previous state should exist before clearing");
        
        // Clear history
        stateManager.clearHistory();
        
        // Verify that previous state is cleared
        assertNull(stateManager.getPreviousState(), 
                  "Previous state should be null after clearing history");
        
        // Verify that current state is maintained
        assertEquals(GameState.GAME_OVER, stateManager.getCurrentState(), 
                   "Current state should remain unchanged after clearing history");
    }

    /**
     * Tests complex state transition sequences.
     */
    @Test
    @DisplayName("Complex State Transition Sequences")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testComplexStateTransitions() {
        // Test a complex sequence of state transitions
        stateManager.setState(GameState.PLAYING);
        stateManager.setState(GameState.PAUSED);
        stateManager.setState(GameState.PLAYING);
        stateManager.setState(GameState.GAME_OVER);
        stateManager.setState(GameState.MAIN_MENU);
        
        // Verify final state
        assertEquals(GameState.MAIN_MENU, stateManager.getCurrentState(), 
                   "Final state should be MAIN_MENU");
        
        // Verify state queries
        assertTrue(stateManager.isInMenu(), "Should be in menu");
        assertFalse(stateManager.isPlaying(), "Should not be playing");
        assertFalse(stateManager.isPaused(), "Should not be paused");
        assertFalse(stateManager.isGameOver(), "Should not be game over");
        
        // Test reversions through the sequence
        assertTrue(stateManager.revertToPreviousState());
        assertEquals(GameState.GAME_OVER, stateManager.getCurrentState(), 
                   "Should revert to GAME_OVER");
        
        assertTrue(stateManager.revertToPreviousState());
        assertEquals(GameState.PLAYING, stateManager.getCurrentState(), 
                   "Should revert to PLAYING");
        
        assertTrue(stateManager.revertToPreviousState());
        assertEquals(GameState.PAUSED, stateManager.getCurrentState(), 
                   "Should revert to PAUSED");
        
        assertTrue(stateManager.revertToPreviousState());
        assertEquals(GameState.PLAYING, stateManager.getCurrentState(), 
                   "Should revert to PLAYING");
        
        assertTrue(stateManager.revertToPreviousState());
        assertEquals(GameState.MAIN_MENU, stateManager.getCurrentState(), 
                   "Should revert to MAIN_MENU");
    }

    /**
     * Tests state manager initialization.
     */
    @Test
    @DisplayName("State Manager Initialization")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testStateManagerInitialization() {
        // Test that state manager is properly initialized
        assertNotNull(stateManager, "StateManager should not be null");
        assertEquals(GameState.MAIN_MENU, stateManager.getCurrentState(), 
                   "Initial state should be MAIN_MENU");
        assertNull(stateManager.getPreviousState(), 
                  "Initial previous state should be null");
        
        // Test that initial state queries are correct
        assertTrue(stateManager.isInState(GameState.MAIN_MENU), 
                  "Should be in MAIN_MENU state initially");
        assertTrue(stateManager.isInMenu(), "Should be in menu initially");
        assertFalse(stateManager.isPlaying(), "Should not be playing initially");
        assertFalse(stateManager.isPaused(), "Should not be paused initially");
        assertFalse(stateManager.isGameOver(), "Should not be game over initially");
    }
} 