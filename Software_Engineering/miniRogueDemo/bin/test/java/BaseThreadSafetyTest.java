import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Base abstract class for thread safety tests.
 * Provides common thread safety testing patterns and utilities.
 * Extend this class to create component-specific thread safety tests.
 */
public abstract class BaseThreadSafetyTest {

    protected static final int DEFAULT_THREAD_COUNT = 4;
    protected static final int DEFAULT_TIMEOUT_SECONDS = 15;
    protected static final long DEFAULT_MEMORY_THRESHOLD = 100000000; // 100MB

    protected AtomicBoolean deadlockDetected;
    protected AtomicInteger successCount;
    protected ExecutorService executor;

    @BeforeEach
    void setUp() {
        deadlockDetected = new AtomicBoolean(false);
        successCount = new AtomicInteger(0);
        executor = Executors.newFixedThreadPool(DEFAULT_THREAD_COUNT);
    }

    /**
     * Abstract method that subclasses must implement to perform their specific concurrent operations.
     * @param threadId The ID of the current thread
     * @throws InterruptedException if the thread is interrupted
     */
    protected abstract void performConcurrentOperation(int threadId) throws InterruptedException;

    /**
     * Abstract method to set up component-specific test data.
     */
    protected abstract void setUpComponent();

    /**
     * Abstract method to clean up component-specific resources.
     */
    protected abstract void tearDownComponent();

    /**
     * Tests basic thread safety with parameterized thread counts.
     */
    @ParameterizedTest
    @ValueSource(ints = {2, 4, 6, 8})
    @DisplayName("Basic Thread Safety")
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    void testBasicThreadSafety(int threadCount) throws InterruptedException {
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch completionLatch = new CountDownLatch(threadCount);
        AtomicBoolean deadlockDetected = new AtomicBoolean(false);
        AtomicInteger successCount = new AtomicInteger(0);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        try {
            setUpComponent();

            // Submit tasks that perform concurrent operations
            for (int i = 0; i < threadCount; i++) {
                final int threadId = i;
                executor.submit(() -> {
                    try {
                        startLatch.await(); // Wait for all threads to start simultaneously
                        
                        // Perform component-specific concurrent operations
                        performConcurrentOperation(threadId);
                        
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

            // Wait for all threads to complete
            boolean completed = completionLatch.await(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);

            // Assertions
            assertTrue(completed, "All threads should complete within timeout");
            assertFalse(deadlockDetected.get(), "No deadlocks should occur");
            assertEquals(threadCount, successCount.get(), "All threads should complete successfully");
            
        } finally {
            executor.shutdown();
            tearDownComponent();
        }
    }

    /**
     * Tests memory leak detection under concurrent access.
     */
    @Test
    @DisplayName("Memory Leak Detection")
    @Timeout(value = 25, unit = TimeUnit.SECONDS)
    void testMemoryLeakDetection() throws InterruptedException {
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch completionLatch = new CountDownLatch(DEFAULT_THREAD_COUNT);
        AtomicBoolean memoryLeakDetected = new AtomicBoolean(false);
        ExecutorService executor = Executors.newFixedThreadPool(DEFAULT_THREAD_COUNT);

        try {
            setUpComponent();

            // Record initial memory usage
            long initialMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

            // Submit memory-intensive concurrent operations
            for (int i = 0; i < DEFAULT_THREAD_COUNT; i++) {
                final int threadId = i;
                executor.submit(() -> {
                    try {
                        startLatch.await();
                        
                        // Perform memory-intensive operations
                        for (int j = 0; j < 50; j++) {
                            performConcurrentOperation(threadId);
                            
                            // Check memory usage periodically
                            long currentMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                            if (currentMemory - initialMemory > DEFAULT_MEMORY_THRESHOLD) {
                                memoryLeakDetected.set(true);
                            }
                            
                            Thread.sleep(10);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        completionLatch.countDown();
                    }
                });
            }

            startLatch.countDown();
            boolean completed = completionLatch.await(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);

            // Assertions
            assertTrue(completed, "Memory leak test should complete within timeout");
            assertFalse(memoryLeakDetected.get(), "No memory leaks should be detected");
            
        } finally {
            executor.shutdown();
            tearDownComponent();
        }
    }

    /**
     * Tests state consistency under concurrent access.
     */
    @Test
    @DisplayName("State Consistency")
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    void testStateConsistency() throws InterruptedException {
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch completionLatch = new CountDownLatch(DEFAULT_THREAD_COUNT);
        AtomicBoolean stateInconsistencyDetected = new AtomicBoolean(false);
        ExecutorService executor = Executors.newFixedThreadPool(DEFAULT_THREAD_COUNT);

        try {
            setUpComponent();

            // Submit tasks that modify state concurrently
            for (int i = 0; i < DEFAULT_THREAD_COUNT; i++) {
                final int threadId = i;
                executor.submit(() -> {
                    try {
                        startLatch.await();
                        
                        // Perform state-modifying operations
                        for (int j = 0; j < 30; j++) {
                            performConcurrentOperation(threadId);
                            
                            // Check state consistency
                            if (!isStateConsistent()) {
                                stateInconsistencyDetected.set(true);
                            }
                            
                            Thread.sleep(5);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        completionLatch.countDown();
                    }
                });
            }

            startLatch.countDown();
            boolean completed = completionLatch.await(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);

            // Assertions
            assertTrue(completed, "State consistency test should complete within timeout");
            assertFalse(stateInconsistencyDetected.get(), "State should remain consistent under concurrent access");
            
        } finally {
            executor.shutdown();
            tearDownComponent();
        }
    }

    /**
     * Abstract method to check if the component's state is consistent.
     * Subclasses should implement this to verify their specific state consistency.
     * @return true if state is consistent, false otherwise
     */
    protected abstract boolean isStateConsistent();

    /**
     * Utility method to create a CountDownLatch for coordinating thread starts.
     */
    protected CountDownLatch createStartLatch() {
        return new CountDownLatch(1);
    }

    /**
     * Utility method to create a CountDownLatch for coordinating thread completions.
     */
    protected CountDownLatch createCompletionLatch(int threadCount) {
        return new CountDownLatch(threadCount);
    }

    /**
     * Utility method to check if memory usage is within acceptable limits.
     */
    protected boolean isMemoryUsageAcceptable(long initialMemory) {
        long currentMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        return (currentMemory - initialMemory) <= DEFAULT_MEMORY_THRESHOLD;
    }
} 