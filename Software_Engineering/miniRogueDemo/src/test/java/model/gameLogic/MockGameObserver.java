package model.gameLogic;

import interfaces.GameObserver;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Mock implementation of GameObserver for testing purposes.
 * Provides controlled behavior for testing observer pattern thread safety.
 */
public class MockGameObserver implements GameObserver {
    
    private final AtomicInteger notificationCount = new AtomicInteger(0);
    private final String observerId;
    
    /**
     * Creates a mock game observer with a default ID.
     */
    public MockGameObserver() {
        this.observerId = "MockObserver-" + System.currentTimeMillis();
    }
    
    /**
     * Creates a mock game observer with a specific ID.
     * 
     * @param observerId The ID for this observer
     */
    public MockGameObserver(String observerId) {
        this.observerId = observerId;
    }
    
    /**
     * Handles model change notifications.
     * Increments the notification count and simulates some processing time.
     * 
     * @param event The event that occurred
     * @param data The data associated with the event
     */
    @Override
    public void on_model_changed(String event, Object data) {
        notificationCount.incrementAndGet();
        
        // Simulate some processing time to increase chance of race conditions
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Gets the number of notifications received by this observer.
     * 
     * @return The notification count
     */
    public int getNotificationCount() {
        return notificationCount.get();
    }
    
    /**
     * Resets the notification count to zero.
     */
    public void resetNotificationCount() {
        notificationCount.set(0);
    }
    
    /**
     * Gets the observer ID.
     * 
     * @return The observer ID
     */
    public String getObserverId() {
        return observerId;
    }
    
    @Override
    public String toString() {
        return "MockGameObserver{id='" + observerId + "', notifications=" + notificationCount.get() + "}";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MockGameObserver that = (MockGameObserver) obj;
        return observerId.equals(that.observerId);
    }
    
    @Override
    public int hashCode() {
        return observerId.hashCode();
    }
} 