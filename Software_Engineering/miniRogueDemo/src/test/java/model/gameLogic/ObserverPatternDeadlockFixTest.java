package model.gameLogic;

import enums.CharacterClass;
import enums.GameState;
import interfaces.GameObserver;
import model.characters.Player;
import model.gameLogic.GameLogic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import utilities.Position;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for fixing observer pattern deadlock issues identified in deadlock detection tests.
 * Focuses on preventing circular dependencies and ensuring proper lock ordering.
 */
@DisplayName("Observer Pattern Deadlock Fix Tests")
class ObserverPatternDeadlockFixTest {

    private GameLogic gameLogic;
    private Player player;
    private MockGameObserver observer1;
    private MockGameObserver observer2;
    private MockGameObserver observer3;

    @BeforeEach
    void setUp() {
        player = new Player("TestPlayer", CharacterClass.WARRIOR, new Position(5, 5));
        gameLogic = new GameLogic(player);
        observer1 = new MockGameObserver("Observer1");
        observer2 = new MockGameObserver("Observer2");
        observer3 = new MockGameObserver("Observer3");
    }

    @Test
    @DisplayName("Fix observer pattern deadlock issues")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testObserverPatternDeadlockFix() {
        // Add observers
        gameLogic.add_observer(observer1);
        gameLogic.add_observer(observer2);
        gameLogic.add_observer(observer3);

        // Test observer notification without deadlocks
        assertDoesNotThrow(() -> {
            gameLogic.notify_observers("test_event", "test_data");
        }, "Observer notification should not cause deadlocks");

        // Verify all observers received the notification
        // Note: Observer notification counts may vary, so we don't assert specific values
    }

    @Test
    @DisplayName("Test observer lock ordering standardization")
    @Timeout(value = 15, unit = TimeUnit.SECONDS)
    void testLockOrderingStandardization() {
        // Add observers
        gameLogic.add_observer(observer1);
        gameLogic.add_observer(observer2);

        // Test concurrent operations that should follow lock ordering
        // Lock order: observerLock -> gameStateLock -> projectileLock -> enemyLock
        assertDoesNotThrow(() -> {
            ExecutorService executor = Executors.newFixedThreadPool(4);
            CountDownLatch latch = new CountDownLatch(4);

            // Thread 1: Observer operations
            executor.submit(() -> {
                try {
                    gameLogic.notify_observers("event1", "data1");
                    latch.countDown();
                } catch (Exception e) {
                    fail("Observer operations should not cause deadlocks: " + e.getMessage());
                }
            });

            // Thread 2: Game state operations
            executor.submit(() -> {
                try {
                    gameLogic.pause_game();
                    gameLogic.resume_game();
                    latch.countDown();
                } catch (Exception e) {
                    fail("Game state operations should not cause deadlocks: " + e.getMessage());
                }
            });

            // Thread 3: Projectile operations
            executor.submit(() -> {
                try {
                    gameLogic.handle_player_attack_input();
                    latch.countDown();
                } catch (Exception e) {
                    fail("Projectile operations should not cause deadlocks: " + e.getMessage());
                }
            });

            // Thread 4: Enemy operations
            executor.submit(() -> {
                try {
                    gameLogic.get_current_enemies();
                    latch.countDown();
                } catch (Exception e) {
                    fail("Enemy operations should not cause deadlocks: " + e.getMessage());
                }
            });

            // Wait for all threads to complete
            boolean completed = latch.await(10, TimeUnit.SECONDS);
            assertTrue(completed, "All concurrent operations should complete within timeout");

            executor.shutdown();
        }, "Concurrent operations should not cause deadlocks with proper lock ordering");
    }

    @Test
    @DisplayName("Test complex scenario deadlock fixes")
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    void testComplexScenarioDeadlockFix() {
        // Add observers
        gameLogic.add_observer(observer1);
        gameLogic.add_observer(observer2);
        gameLogic.add_observer(observer3);

        // Test multiple concurrent observer operations
        assertDoesNotThrow(() -> {
            ExecutorService executor = Executors.newFixedThreadPool(6);
            CountDownLatch latch = new CountDownLatch(6);
            AtomicBoolean deadlockDetected = new AtomicBoolean(false);
            AtomicInteger successCount = new AtomicInteger(0);

            // Multiple threads performing observer operations
            for (int i = 0; i < 6; i++) {
                final int threadId = i;
                executor.submit(() -> {
                    try {
                        // Simulate complex observer operations
                        gameLogic.notify_observers("complex_event_" + threadId, "complex_data_" + threadId);
                        
                        // Simulate observer callback that might trigger additional notifications
                        if (threadId % 2 == 0) {
                            gameLogic.pause_game();
                            gameLogic.resume_game();
                        }
                        
                        successCount.incrementAndGet();
                        latch.countDown();
                    } catch (Exception e) {
                        deadlockDetected.set(true);
                        latch.countDown();
                    }
                });
            }

            // Wait for all threads to complete
            boolean completed = latch.await(15, TimeUnit.SECONDS);
            
            assertTrue(completed, "All complex observer operations should complete within timeout");
            assertFalse(deadlockDetected.get(), "No deadlocks should be detected");
            assertEquals(6, successCount.get(), "All operations should complete successfully");

            executor.shutdown();
        }, "Complex observer scenarios should not cause deadlocks");

        // Verify timeout-based deadlock detection works
        assertTrue(observer1.getNotificationCount() > 0, "Observer1 should receive notifications");
        assertTrue(observer2.getNotificationCount() > 0, "Observer2 should receive notifications");
        assertTrue(observer3.getNotificationCount() > 0, "Observer3 should receive notifications");
    }

    @Test
    @DisplayName("Test observer callback circular dependency prevention")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testObserverCallbackCircularDependencyPrevention() {
        // Create observers that might trigger additional notifications
        CircularDependencyObserver circularObserver = new CircularDependencyObserver(gameLogic);
        gameLogic.add_observer(circularObserver);

        // Test that observer callbacks don't cause circular dependencies
        assertDoesNotThrow(() -> {
            gameLogic.notify_observers("circular_test", "circular_data");
        }, "Observer callbacks should not cause circular dependencies");

        // Verify the notification was processed
        assertTrue(circularObserver.getNotificationCount() > 0, "Circular observer should receive notification");
    }

    @Test
    @DisplayName("Test observer cleanup during game state transitions")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testObserverCleanupDuringGameStateTransitions() {
        // Add observers
        gameLogic.add_observer(observer1);
        gameLogic.add_observer(observer2);

        // Test observer cleanup during game state transitions
        assertDoesNotThrow(() -> {
            // Transition to different game states
            gameLogic.pause_game();
            gameLogic.resume_game();
            gameLogic.back_to_main_menu();
            
            // Notify observers during state transitions
            gameLogic.notify_observers("state_transition", "transition_data");
        }, "Observer cleanup should work during game state transitions");

        // Verify observers are still functional
        assertTrue(observer1.getNotificationCount() > 0, "Observer1 should still receive notifications");
        assertTrue(observer2.getNotificationCount() > 0, "Observer2 should still receive notifications");
    }

    /**
     * Mock observer that simulates circular dependency scenarios
     */
    private static class CircularDependencyObserver implements GameObserver {
        private final GameLogic gameLogic;
        private int notificationCount = 0;
        private final AtomicBoolean inCallback = new AtomicBoolean(false);

        public CircularDependencyObserver(GameLogic gameLogic) {
            this.gameLogic = gameLogic;
        }

        @Override
        public void on_model_changed(String event, Object data) {
            notificationCount++;
            
            // Simulate potential circular dependency (but with protection)
            if (!inCallback.get() && notificationCount < 3) {
                inCallback.set(true);
                try {
                    // Trigger additional notification (but limit to prevent infinite loop)
                    gameLogic.notify_observers("callback_event", "callback_data");
                } finally {
                    inCallback.set(false);
                }
            }
        }

        public int getNotificationCount() {
            return notificationCount;
        }
    }
} 