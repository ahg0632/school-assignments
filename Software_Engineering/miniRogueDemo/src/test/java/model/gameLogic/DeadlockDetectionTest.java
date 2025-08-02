package model.gameLogic;

import interfaces.GameObserver;
import model.characters.Player;
import enums.CharacterClass;
import utilities.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple tests for detecting deadlocks in GameLogic components.
 * Uses timeout-based detection to identify potential deadlock scenarios.
 * Simplified for school project use.
 */
@DisplayName("Simple Deadlock Detection Tests")
class DeadlockDetectionTest {

    private GameLogic gameLogic;
    private Player testPlayer;
    private static final long DEADLOCK_TIMEOUT = 5000; // 5 seconds
    private static final int THREAD_COUNT = 5; // Reduced from 10

    @BeforeEach
    void setUp() {
        testPlayer = new Player("TestPlayer", CharacterClass.WARRIOR, new Position(0, 0));
        gameLogic = new GameLogic(testPlayer);
    }

    /**
     * Tests for deadlocks in observer pattern operations.
     * Multiple threads add/remove observers simultaneously.
     */
    @Test
    @DisplayName("Observer Pattern Deadlock Detection")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testObserverDeadlock() throws InterruptedException {
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch completionLatch = new CountDownLatch(THREAD_COUNT);
        AtomicBoolean deadlockDetected = new AtomicBoolean(false);
        AtomicInteger successCount = new AtomicInteger(0);
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        // Create multiple mock observers
        MockGameObserver[] observers = new MockGameObserver[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            observers[i] = new MockGameObserver("Observer-" + i);
        }

        // Submit tasks that add/remove observers concurrently
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    startLatch.await(); // Wait for all threads to start simultaneously
                    
                    // Perform observer operations that could cause deadlocks
                    for (int j = 0; j < 20; j++) { // Reduced iterations
                        gameLogic.add_observer(observers[threadId]);
                        gameLogic.notify_observers("test_event", "test_data");
                        gameLogic.remove_observer(observers[threadId]);
                        
                        // Small delay to increase chance of race conditions
                        Thread.sleep(1);
                    }
                    successCount.incrementAndGet();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    deadlockDetected.set(true);
                } finally {
                    completionLatch.countDown();
                }
            });
        }

        // Start all threads simultaneously
        startLatch.countDown();

        // Wait for completion with timeout
        boolean completed = completionLatch.await(DEADLOCK_TIMEOUT, TimeUnit.MILLISECONDS);
        
        executor.shutdown();
        boolean terminated = executor.awaitTermination(2, TimeUnit.SECONDS);

        // Assertions
        assertTrue(completed, "Deadlock detected: Observer operations did not complete within timeout");
        assertTrue(terminated, "Thread pool did not terminate properly");
        assertFalse(deadlockDetected.get(), "Deadlock detected during observer operations");
        assertEquals(THREAD_COUNT, successCount.get(), "Not all threads completed successfully");
    }

    /**
     * Tests for deadlocks in game state management operations.
     * Multiple threads perform state transitions simultaneously.
     */
    @Test
    @DisplayName("Game State Management Deadlock Detection")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testGameStateDeadlock() throws InterruptedException {
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch completionLatch = new CountDownLatch(THREAD_COUNT);
        AtomicBoolean deadlockDetected = new AtomicBoolean(false);
        AtomicInteger successCount = new AtomicInteger(0);
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        // Submit tasks that perform state transitions concurrently
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    startLatch.await(); // Wait for all threads to start simultaneously
                    
                    // Perform state operations that could cause deadlocks
                    for (int j = 0; j < 20; j++) { // Reduced iterations
                        gameLogic.pause_game();
                        gameLogic.get_game_state();
                        gameLogic.is_paused();
                        gameLogic.resume_game();
                        gameLogic.get_game_state();
                        gameLogic.pause_game();
                        
                        // Small delay to increase chance of race conditions
                        Thread.sleep(1);
                    }
                    successCount.incrementAndGet();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    deadlockDetected.set(true);
                } finally {
                    completionLatch.countDown();
                }
            });
        }

        // Start all threads simultaneously
        startLatch.countDown();

        // Wait for completion with timeout
        boolean completed = completionLatch.await(DEADLOCK_TIMEOUT, TimeUnit.MILLISECONDS);
        
        executor.shutdown();
        boolean terminated = executor.awaitTermination(2, TimeUnit.SECONDS);

        // Assertions
        assertTrue(completed, "Deadlock detected: Game state operations did not complete within timeout");
        assertTrue(terminated, "Thread pool did not terminate properly");
        assertFalse(deadlockDetected.get(), "Deadlock detected during game state operations");
        assertEquals(THREAD_COUNT, successCount.get(), "Not all threads completed successfully");
    }

    /**
     * Tests for deadlocks in projectile list access.
     * Multiple threads access projectile list simultaneously.
     */
    @Test
    @DisplayName("Projectile List Deadlock Detection")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testProjectileDeadlock() throws InterruptedException {
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch completionLatch = new CountDownLatch(THREAD_COUNT);
        AtomicBoolean deadlockDetected = new AtomicBoolean(false);
        AtomicInteger successCount = new AtomicInteger(0);
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        // Submit tasks that access projectile list concurrently
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    startLatch.await(); // Wait for all threads to start simultaneously
                    
                    // Perform projectile operations that could cause deadlocks
                    for (int j = 0; j < 20; j++) { // Reduced iterations
                        gameLogic.getProjectiles();
                        
                        // Small delay to increase chance of race conditions
                        Thread.sleep(1);
                    }
                    successCount.incrementAndGet();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    deadlockDetected.set(true);
                } finally {
                    completionLatch.countDown();
                }
            });
        }

        // Start all threads simultaneously
        startLatch.countDown();

        // Wait for completion with timeout
        boolean completed = completionLatch.await(DEADLOCK_TIMEOUT, TimeUnit.MILLISECONDS);
        
        executor.shutdown();
        boolean terminated = executor.awaitTermination(2, TimeUnit.SECONDS);

        // Assertions
        assertTrue(completed, "Deadlock detected: Projectile operations did not complete within timeout");
        assertTrue(terminated, "Thread pool did not terminate properly");
        assertFalse(deadlockDetected.get(), "Deadlock detected during projectile operations");
        assertEquals(THREAD_COUNT, successCount.get(), "Not all threads completed successfully");
    }

    /**
     * Tests for deadlocks in enemy list access.
     * Multiple threads access enemy list simultaneously.
     */
    @Test
    @DisplayName("Enemy List Deadlock Detection")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testEnemyDeadlock() throws InterruptedException {
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch completionLatch = new CountDownLatch(THREAD_COUNT);
        AtomicBoolean deadlockDetected = new AtomicBoolean(false);
        AtomicInteger successCount = new AtomicInteger(0);
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        // Submit tasks that access enemy list concurrently
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    startLatch.await(); // Wait for all threads to start simultaneously
                    
                    // Perform enemy operations that could cause deadlocks
                    for (int j = 0; j < 20; j++) { // Reduced iterations
                        gameLogic.get_current_enemies();
                        
                        // Small delay to increase chance of race conditions
                        Thread.sleep(1);
                    }
                    successCount.incrementAndGet();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    deadlockDetected.set(true);
                } finally {
                    completionLatch.countDown();
                }
            });
        }

        // Start all threads simultaneously
        startLatch.countDown();

        // Wait for completion with timeout
        boolean completed = completionLatch.await(DEADLOCK_TIMEOUT, TimeUnit.MILLISECONDS);
        
        executor.shutdown();
        boolean terminated = executor.awaitTermination(2, TimeUnit.SECONDS);

        // Assertions
        assertTrue(completed, "Deadlock detected: Enemy operations did not complete within timeout");
        assertTrue(terminated, "Thread pool did not terminate properly");
        assertFalse(deadlockDetected.get(), "Deadlock detected during enemy operations");
        assertEquals(THREAD_COUNT, successCount.get(), "Not all threads completed successfully");
    }
} 