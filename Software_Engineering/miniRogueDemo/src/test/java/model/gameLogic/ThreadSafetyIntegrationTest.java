package model.gameLogic;

import enums.CharacterClass;
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
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for thread safety integration testing between memory leak detection,
 * deadlock detection, and cross-component operations.
 */
@DisplayName("Thread Safety Integration Tests")
class ThreadSafetyIntegrationTest {

    private GameLogic gameLogic;
    private Player player;
    private MockGameObserver observer1;
    private MockGameObserver observer2;

    @BeforeEach
    void setUp() {
        player = new Player("TestPlayer", CharacterClass.WARRIOR, new Position(5, 5));
        gameLogic = new GameLogic(player);
        observer1 = new MockGameObserver("Observer1");
        observer2 = new MockGameObserver("Observer2");
    }

    @Test
    @DisplayName("Test memory leak and deadlock integration")
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    void testMemoryLeakAndDeadlockIntegration() {
        // Add observers
        gameLogic.add_observer(observer1);
        gameLogic.add_observer(observer2);

        // Test concurrent operations that could cause both memory leaks and deadlocks
        assertDoesNotThrow(() -> {
            ExecutorService executor = Executors.newFixedThreadPool(8);
            CountDownLatch latch = new CountDownLatch(8);
            AtomicBoolean deadlockDetected = new AtomicBoolean(false);
            AtomicInteger successCount = new AtomicInteger(0);
            AtomicLong initialMemory = new AtomicLong(MemoryMonitoringUtils.getCurrentMemoryUsage());

            // Multiple threads performing memory-intensive and potentially deadlock-prone operations
            for (int i = 0; i < 8; i++) {
                final int threadId = i;
                executor.submit(() -> {
                    try {
                        // Simulate memory-intensive operations
                        for (int j = 0; j < 100; j++) {
                            gameLogic.notify_observers("memory_intensive_event_" + threadId + "_" + j, 
                                                     "memory_intensive_data_" + threadId + "_" + j);
                            
                            // Simulate game state changes
                            if (j % 10 == 0) {
                                gameLogic.pause_game();
                                gameLogic.resume_game();
                            }
                            
                            // Simulate projectile operations
                            if (j % 20 == 0) {
                                gameLogic.handle_player_attack_input();
                            }
                            
                            // Simulate enemy operations
                            if (j % 30 == 0) {
                                gameLogic.get_current_enemies();
                            }
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
            boolean completed = latch.await(25, TimeUnit.SECONDS);
            
            assertTrue(completed, "All memory-intensive operations should complete within timeout");
            assertFalse(deadlockDetected.get(), "No deadlocks should be detected");
            assertEquals(8, successCount.get(), "All operations should complete successfully");

            // Check for memory leaks
            MemoryMonitoringUtils.forceGarbageCollection();
            long finalMemory = MemoryMonitoringUtils.getCurrentMemoryUsage();
            long memoryIncrease = finalMemory - initialMemory.get();
            
            // Allow for some memory increase but not excessive
            assertTrue(memoryIncrease < 50 * 1024 * 1024, // 50MB threshold
                      "Memory increase should be reasonable: " + MemoryMonitoringUtils.formatMemoryUsage(memoryIncrease));

            executor.shutdown();
        }, "Memory-intensive concurrent operations should not cause deadlocks or excessive memory leaks");

        // Verify observers received notifications
        assertTrue(observer1.getNotificationCount() > 0, "Observer1 should receive notifications");
        assertTrue(observer2.getNotificationCount() > 0, "Observer2 should receive notifications");
    }

    @Test
    @DisplayName("Test cross-component thread safety")
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    void testCrossComponentThreadSafety() {
        // Add observers
        gameLogic.add_observer(observer1);
        gameLogic.add_observer(observer2);

        // Test interactions between GameLogic, Player, Enemy, and Item systems
        assertDoesNotThrow(() -> {
            ExecutorService executor = Executors.newFixedThreadPool(6);
            CountDownLatch latch = new CountDownLatch(6);
            AtomicBoolean deadlockDetected = new AtomicBoolean(false);
            AtomicInteger successCount = new AtomicInteger(0);

            // Thread 1: GameLogic operations
            executor.submit(() -> {
                try {
                    for (int i = 0; i < 50; i++) {
                        gameLogic.notify_observers("game_logic_event_" + i, "game_logic_data_" + i);
                        Thread.sleep(10);
                    }
                    successCount.incrementAndGet();
                    latch.countDown();
                } catch (Exception e) {
                    deadlockDetected.set(true);
                    latch.countDown();
                }
            });

            // Thread 2: Player operations (through GameLogic)
            executor.submit(() -> {
                try {
                    for (int i = 0; i < 50; i++) {
                        gameLogic.handle_player_action("move_up", null);
                        gameLogic.handle_player_action("move_down", null);
                        Thread.sleep(10);
                    }
                    successCount.incrementAndGet();
                    latch.countDown();
                } catch (Exception e) {
                    deadlockDetected.set(true);
                    latch.countDown();
                }
            });

            // Thread 3: Enemy operations (through GameLogic)
            executor.submit(() -> {
                try {
                    for (int i = 0; i < 50; i++) {
                        gameLogic.get_current_enemies();
                        gameLogic.update_game_state();
                        Thread.sleep(10);
                    }
                    successCount.incrementAndGet();
                    latch.countDown();
                } catch (Exception e) {
                    deadlockDetected.set(true);
                    latch.countDown();
                }
            });

            // Thread 4: Item operations (through GameLogic)
            executor.submit(() -> {
                try {
                    for (int i = 0; i < 50; i++) {
                        gameLogic.notify_observers("item_collection", "test_item");
                        gameLogic.notify_observers("item_event_" + i, "item_data_" + i);
                        Thread.sleep(10);
                    }
                    successCount.incrementAndGet();
                    latch.countDown();
                } catch (Exception e) {
                    deadlockDetected.set(true);
                    latch.countDown();
                }
            });

            // Thread 5: Game state operations
            executor.submit(() -> {
                try {
                    for (int i = 0; i < 50; i++) {
                        gameLogic.pause_game();
                        gameLogic.resume_game();
                        Thread.sleep(10);
                    }
                    successCount.incrementAndGet();
                    latch.countDown();
                } catch (Exception e) {
                    deadlockDetected.set(true);
                    latch.countDown();
                }
            });

            // Thread 6: Projectile operations
            executor.submit(() -> {
                try {
                    for (int i = 0; i < 50; i++) {
                        gameLogic.handle_player_attack_input();
                        gameLogic.update_game_state();
                        Thread.sleep(10);
                    }
                    successCount.incrementAndGet();
                    latch.countDown();
                } catch (Exception e) {
                    deadlockDetected.set(true);
                    latch.countDown();
                }
            });

            // Wait for all threads to complete
            boolean completed = latch.await(15, TimeUnit.SECONDS);
            
            assertTrue(completed, "All cross-component operations should complete within timeout");
            assertFalse(deadlockDetected.get(), "No deadlocks should be detected");
            assertEquals(6, successCount.get(), "All operations should complete successfully");

            executor.shutdown();
        }, "Cross-component operations should remain thread-safe");

        // Verify observers received notifications
        assertTrue(observer1.getNotificationCount() > 0, "Observer1 should receive notifications");
        assertTrue(observer2.getNotificationCount() > 0, "Observer2 should receive notifications");
    }

    @Test
    @DisplayName("Test observer-game state integration")
    @Timeout(value = 15, unit = TimeUnit.SECONDS)
    void testObserverGameStateIntegration() {
        // Add observers
        gameLogic.add_observer(observer1);
        gameLogic.add_observer(observer2);

        // Test observer notifications during game state changes
        assertDoesNotThrow(() -> {
            ExecutorService executor = Executors.newFixedThreadPool(4);
            CountDownLatch latch = new CountDownLatch(4);
            AtomicBoolean deadlockDetected = new AtomicBoolean(false);
            AtomicInteger successCount = new AtomicInteger(0);

            // Thread 1: Observer notifications
            executor.submit(() -> {
                try {
                    for (int i = 0; i < 100; i++) {
                        gameLogic.notify_observers("observer_event_" + i, "observer_data_" + i);
                        Thread.sleep(5);
                    }
                    successCount.incrementAndGet();
                    latch.countDown();
                } catch (Exception e) {
                    deadlockDetected.set(true);
                    latch.countDown();
                }
            });

            // Thread 2: Game state transitions
            executor.submit(() -> {
                try {
                    for (int i = 0; i < 100; i++) {
                        gameLogic.pause_game();
                        gameLogic.resume_game();
                        Thread.sleep(5);
                    }
                    successCount.incrementAndGet();
                    latch.countDown();
                } catch (Exception e) {
                    deadlockDetected.set(true);
                    latch.countDown();
                }
            });

            // Thread 3: Observer cleanup during state transitions
            executor.submit(() -> {
                try {
                    for (int i = 0; i < 100; i++) {
                        gameLogic.back_to_main_menu();
                        gameLogic.notify_observers("cleanup_event_" + i, "cleanup_data_" + i);
                        Thread.sleep(5);
                    }
                    successCount.incrementAndGet();
                    latch.countDown();
                } catch (Exception e) {
                    deadlockDetected.set(true);
                    latch.countDown();
                }
            });

            // Thread 4: Mixed operations
            executor.submit(() -> {
                try {
                    for (int i = 0; i < 100; i++) {
                        if (i % 2 == 0) {
                            gameLogic.pause_game();
                            gameLogic.notify_observers("mixed_event_" + i, "mixed_data_" + i);
                        } else {
                            gameLogic.resume_game();
                            gameLogic.notify_observers("mixed_event_" + i, "mixed_data_" + i);
                        }
                        Thread.sleep(5);
                    }
                    successCount.incrementAndGet();
                    latch.countDown();
                } catch (Exception e) {
                    deadlockDetected.set(true);
                    latch.countDown();
                }
            });

            // Wait for all threads to complete
            boolean completed = latch.await(10, TimeUnit.SECONDS);
            
            assertTrue(completed, "All observer-game state operations should complete within timeout");
            assertFalse(deadlockDetected.get(), "No deadlocks should be detected");
            assertEquals(4, successCount.get(), "All operations should complete successfully");

            executor.shutdown();
        }, "Observer notifications during game state changes should not cause deadlocks");

        // Verify observers received notifications
        assertTrue(observer1.getNotificationCount() > 0, "Observer1 should receive notifications");
        assertTrue(observer2.getNotificationCount() > 0, "Observer2 should receive notifications");
    }

    @Test
    @DisplayName("Test concurrent resource access")
    @Timeout(value = 15, unit = TimeUnit.SECONDS)
    void testConcurrentResourceAccess() {
        // Add observers
        gameLogic.add_observer(observer1);
        gameLogic.add_observer(observer2);

        // Test concurrent access to shared resources
        assertDoesNotThrow(() -> {
            ExecutorService executor = Executors.newFixedThreadPool(5);
            CountDownLatch latch = new CountDownLatch(5);
            AtomicBoolean deadlockDetected = new AtomicBoolean(false);
            AtomicInteger successCount = new AtomicInteger(0);

            // Multiple threads accessing shared resources
            for (int i = 0; i < 5; i++) {
                final int threadId = i;
                executor.submit(() -> {
                    try {
                        for (int j = 0; j < 50; j++) {
                            // Access different shared resources
                            switch (threadId % 4) {
                                case 0:
                                    gameLogic.notify_observers("resource_event_" + j, "resource_data_" + j);
                                    break;
                                case 1:
                                    gameLogic.get_current_enemies();
                                    break;
                                case 2:
                                    gameLogic.pause_game();
                                    gameLogic.resume_game();
                                    break;
                                case 3:
                                    gameLogic.handle_player_attack_input();
                                    break;
                            }
                            Thread.sleep(5);
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
            boolean completed = latch.await(10, TimeUnit.SECONDS);
            
            assertTrue(completed, "All concurrent resource access should complete within timeout");
            assertFalse(deadlockDetected.get(), "No deadlocks should be detected");
            assertEquals(5, successCount.get(), "All operations should complete successfully");

            executor.shutdown();
        }, "Concurrent access to shared resources should remain thread-safe");

        // Verify observers received notifications
        assertTrue(observer1.getNotificationCount() > 0, "Observer1 should receive notifications");
        assertTrue(observer2.getNotificationCount() > 0, "Observer2 should receive notifications");
    }
} 