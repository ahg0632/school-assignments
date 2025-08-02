package model.gameLogic;

import java.util.concurrent.TimeUnit;

/**
 * Utility class for memory monitoring and leak detection in tests.
 * Provides helper methods for tracking memory usage and detecting memory leaks.
 */
public class MemoryMonitoringUtils {

    private static final long DEFAULT_MEMORY_LEAK_THRESHOLD = 10 * 1024 * 1024; // 10MB

    /**
     * Gets the current memory usage in bytes.
     *
     * @return Current memory usage in bytes
     */
    public static long getCurrentMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }

    /**
     * Forces garbage collection to clean up memory.
     * Uses System.gc() for memory cleanup.
     */
    public static void forceGarbageCollection() {
        System.gc();
        // Removed System.runFinalization() as it's deprecated
        // Modern JVM garbage collectors handle cleanup more efficiently
    }

    /**
     * Detects if a memory leak has occurred based on initial and current memory usage.
     *
     * @param initialMemory Initial memory usage in bytes
     * @param currentMemory Current memory usage in bytes
     * @param threshold Memory leak threshold in bytes
     * @return true if memory leak is detected, false otherwise
     */
    public static boolean detectMemoryLeak(long initialMemory, long currentMemory, long threshold) {
        return (currentMemory - initialMemory) > threshold;
    }

    /**
     * Formats memory usage in human-readable format (B, KB, MB).
     *
     * @param bytes Memory usage in bytes
     * @return Formatted memory usage string
     */
    public static String formatMemoryUsage(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.1f KB", bytes / 1024.0);
        } else {
            return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
        }
    }

    /**
     * Gets the default memory leak threshold (10MB).
     *
     * @return Default memory leak threshold in bytes
     */
    public static long getDefaultMemoryLeakThreshold() {
        return DEFAULT_MEMORY_LEAK_THRESHOLD;
    }

    /**
     * Waits for a specified duration and forces garbage collection.
     *
     * @param duration Duration to wait
     * @param unit Time unit for the duration
     * @throws InterruptedException if interrupted while waiting
     */
    public static void waitAndForceGC(long duration, TimeUnit unit) throws InterruptedException {
        Thread.sleep(unit.toMillis(duration));
        forceGarbageCollection();
    }

    /**
     * Monitors memory usage over time and returns peak usage.
     *
     * @param duration Duration to monitor
     * @param unit Time unit for the duration
     * @return Peak memory usage during monitoring period
     * @throws InterruptedException if interrupted while monitoring
     */
    public static long monitorMemoryUsage(long duration, TimeUnit unit) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + unit.toMillis(duration);
        long peakMemory = getCurrentMemoryUsage();

        while (System.currentTimeMillis() < endTime) {
            long currentMemory = getCurrentMemoryUsage();
            if (currentMemory > peakMemory) {
                peakMemory = currentMemory;
            }
            Thread.sleep(100); // Check every 100ms
        }

        return peakMemory;
    }
} 