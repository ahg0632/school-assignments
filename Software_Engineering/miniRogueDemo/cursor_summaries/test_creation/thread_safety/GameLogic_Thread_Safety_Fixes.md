# GameLogic Thread Safety Fixes - Required Code Changes

## Problem Analysis

Based on the comprehensive project analysis, **BattleLogic is essentially unused** in the actual game. The real thread safety issues are in **GameLogic**, which handles all combat mechanics including multiple enemies, bosses, and real-time combat.

### **Current Combat System (GameLogic):**
- **Multiple enemies + boss** active simultaneously
- **Real-time combat** with continuous updates
- **Dynamic enemy creation** and management
- **Projectile system** with concurrent updates
- **Observer notifications** from multiple threads

## Required Code Changes

### 1. Add Thread Synchronization to GameLogic

**Problem**: Multiple enemies, projectiles, and game state updates happening simultaneously without proper synchronization.

**Solution**: Add proper synchronization using locks and volatile variables.

```java
// Add these imports to GameLogic.java
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.atomic.AtomicBoolean;

// Add these fields to GameLogic class
private final ReentrantLock gameStateLock = new ReentrantLock();
private final ReadWriteLock enemyLock = new ReentrantReadWriteLock();
private final Object observerLock = new Object();
private volatile boolean pauseStatus; // Make volatile for thread safety
private volatile GameState gameState; // Make volatile for thread safety
```

### 2. Fix Enemy Management Thread Safety

**Current Problem**: Multiple enemies updating simultaneously, enemies being added/removed while iterating.

**Required Changes**:

```java
/**
 * MANDATORY: Update enemy positions and AI behavior (Thread-safe)
 */
private void update_enemy_positions() {
    enemyLock.writeLock().lock();
    try {
        // Use iterator to safely remove dead enemies
        Iterator<Enemy> enemyIterator = currentEnemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            if (enemy.shouldBeDeleted()) {
                enemyIterator.remove();
                continue;
            }
            if (enemy.is_alive() && !enemy.isDying()) {
                enemy.perform_turn();
                // Simple AI: move towards player if within aggro range and player is not invisible
                if (player.get_position().distance_to(enemy.get_position())
                    <= enemy.get_aggro_range() && !player.is_invisibility_effect_active()) {
                    move_enemy_towards_player(enemy);
                }
                // Handle enemy attack logic (only if player is not invisible)
                if (!player.is_invisibility_effect_active()) {
                    handle_enemy_attack_logic(enemy);
                }
            }
        }
    } finally {
        enemyLock.writeLock().unlock();
    }
}

/**
 * MANDATORY: Add enemy to current enemies list (Thread-safe)
 */
public void addEnemy(Enemy enemy) {
    enemyLock.writeLock().lock();
    try {
        currentEnemies.add(enemy);
        notify_observers("ENEMY_ADDED", enemy);
    } finally {
        enemyLock.writeLock().unlock();
    }
}

/**
 * MANDATORY: Remove enemy from current enemies list (Thread-safe)
 */
public void removeEnemy(Enemy enemy) {
    enemyLock.writeLock().lock();
    try {
        currentEnemies.remove(enemy);
        notify_observers("ENEMY_REMOVED", enemy);
    } finally {
        enemyLock.writeLock().unlock();
    }
}

/**
 * MANDATORY: Get current enemies list (Thread-safe)
 */
public List<Enemy> get_current_enemies() { 
    enemyLock.readLock().lock();
    try {
        return new ArrayList<>(currentEnemies);
    } finally {
        enemyLock.readLock().unlock();
    }
}
```

### 3. Fix Game State Management Thread Safety

**Current Problem**: Game state can be modified by multiple threads simultaneously.

**Required Changes**:

```java
/**
 * MANDATORY: Update game state (Thread-safe)
 */
public void update_game_state() {
    gameStateLock.lock();
    try {
        long now = System.currentTimeMillis();
        float deltaTime = (now - lastUpdateTime) / 1000f;
        lastUpdateTime = now;
        
        if (!pauseStatus && !npcDialogue) {
            // Check floor transition first
            checkFloorTransition();
            
            // Only update game entities if not transitioning
            if (!isFloorTransitioning) {
                update_enemy_positions();
                update_upgrader();
                
                // Update projectiles (thread-safe)
                update_projectiles(deltaTime);
                
                check_victory_condition();
                check_death_condition();
                // Passive mana regen for any class with MP
                player.passiveManaRegen();
            }
            
            notify_observers("GAME_STATE_UPDATED", gameState);
        }
    } finally {
        gameStateLock.unlock();
    }
}

/**
 * MANDATORY: Update projectiles (Thread-safe)
 */
private void update_projectiles(float deltaTime) {
    Iterator<Projectile> it = projectiles.iterator();
    while (it.hasNext()) {
        Projectile p = it.next();
        p.update(deltaTime, currentMap, currentEnemies);
        if (!p.isActive()) it.remove();
    }
}
```

### 4. Fix Observer Pattern Thread Safety

**Current Problem**: Observer notifications are not synchronized, causing concurrent modification exceptions.

**Required Changes**:

```java
// MANDATORY: Observer pattern implementation (Thread-safe)
@Override
public void notify_observers(String event, Object data) {
    synchronized (observerLock) {
        // Create a copy of the observers list to avoid concurrent modification
        List<GameObserver> observersCopy = new ArrayList<>(observers);
        for (GameObserver observer : observersCopy) {
            try {
                observer.on_model_changed(event, data);
            } catch (Exception e) {
                // Log observer error but don't fail the entire notification
                System.err.println("Observer notification failed: " + e.getMessage());
            }
        }
    }
}

@Override
public void add_observer(GameObserver observer) {
    synchronized (observerLock) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }
}

@Override
public void remove_observer(GameObserver observer) {
    synchronized (observerLock) {
        observers.remove(observer);
    }
}
```

### 5. Fix Enemy Attack Logic Thread Safety

**Current Problem**: Multiple enemies can attack simultaneously, causing race conditions.

**Required Changes**:

```java
/**
 * Handle enemy attack logic with wind-up phase (Thread-safe)
 */
private void handle_enemy_attack_logic(Enemy enemy) {
    if (enemy == null || !enemy.is_alive()) return;
    
    // Only allow attack if enemy is currently chasing the player (aggroed)
    if (!enemy.isChasingPlayer()) {
        return;
    }
    // Check if enemy is in celebratory state - if so, don't attack
    if (enemy.isInCelebratoryState()) {
        return;
    }
    // Check if enemy is showing detection notification - if so, don't attack
    if (enemy.isShowingDetectionNotification()) {
        return;
    }
    
    // Check if enemy is in hit state - if so, don't attack
    if (enemy.isInHitState()) {
        return;
    }
    
    // Handle wind-up state
    if (enemy.isInWindUpState()) {
        long now = System.currentTimeMillis();
        if (now - enemy.getWindUpStartTime() >= enemy.getWindUpDuration()) {
            // Wind-up complete, execute attack
            executeEnemyAttack(enemy);
            enemy.setInWindUpState(false);
        }
        return; // Don't start new wind-up while in wind-up state
    }
    
    // Calculate tile positions for LOS check
    int enemyTileX = (int)(enemy.getPixelX() / GameConstants.TILE_SIZE);
    int enemyTileY = (int)(enemy.getPixelY() / GameConstants.TILE_SIZE);
    int playerTileX = (int)(player.getPixelX() / GameConstants.TILE_SIZE);
    int playerTileY = (int)(player.getPixelY() / GameConstants.TILE_SIZE);
    
    // Check line of sight (LOS) before attacking
    if (!utilities.Collision.hasLineOfSight(currentMap, enemyTileX, enemyTileY, playerTileX, playerTileY)) {
        return; // LOS blocked by wall, do not attack
    }

    // Check attack cooldown
    long now = System.currentTimeMillis();
    float attackSpeed = enemy.getEnemyClassOOP().getAttackSpeed();
    if (now - enemy.getLastAttackTime() < (1000.0f / attackSpeed)) {
        return; // Still in cooldown
    }
    
    // Calculate distance to player (accounting for entity size)
    float px = enemy.getPixelX() + GameConstants.TILE_SIZE / 2f;
    float py = enemy.getPixelY() + GameConstants.TILE_SIZE / 2f;
    float tx = player.getPixelX() + GameConstants.TILE_SIZE / 2f;
    float ty = player.getPixelY() + GameConstants.TILE_SIZE / 2f;
    float dist = (float)Math.hypot(tx - px, ty - py) / GameConstants.TILE_SIZE;
    
    // Adjust range based on entity size and boss modifiers
    float sizeMultiplier = 1.0f;
    float rangeModifier = 1.0f;
    
    if (enemy instanceof Boss) {
        sizeMultiplier = ((Boss) enemy).getSizeMultiplier();
        rangeModifier = ((Boss) enemy).getRangeModifier();
    }
    
    float adjustedRange = enemy.getEnemyClassOOP().getRange() * rangeModifier;
    float adjustedSize = GameConstants.TILE_SIZE * sizeMultiplier;
    
    // Check if player is in range for attack
    boolean canProjectile = enemy.getEnemyClassOOP().hasProjectile() && dist <= adjustedRange;
    boolean canMelee = enemy.getEnemyClassOOP().hasMelee() && dist <= 1.5f; // Melee range
    
    if (canProjectile || canMelee) {
        startEnemyWindUp(enemy);
    }
}
```

### 6. Fix Player Action Handling Thread Safety

**Current Problem**: Player actions can be processed while game state is being updated.

**Required Changes**:

```java
/**
 * MANDATORY: Handle player actions (Thread-safe)
 */
public void handle_player_action(String action, Object data) {
    gameStateLock.lock();
    try {
        switch (action.toLowerCase()) {
            case "move":
                handle_player_movement((String) data);
                break;
            case "attack":
                handle_attack_action();
                break;
            case "use_item":
                handle_item_usage((Item) data);
                break;
            case "open_inventory":
                open_inventory();
                break;
            case "interact":
                handle_tile_interaction((utilities.Tile) data);
                break;
            default:
                System.err.println("Unknown player action: " + action);
        }
    } finally {
        gameStateLock.unlock();
    }
}
```

### 7. Fix Pause/Resume Thread Safety

**Current Problem**: Pause state can be modified by multiple threads.

**Required Changes**:

```java
/**
 * MANDATORY: Pause the game (Thread-safe)
 */
public void pause_game() {
    gameStateLock.lock();
    try {
        gameState = GameState.PAUSED;
        pauseStatus = true;
        notify_observers("GAME_PAUSED", null);
    } finally {
        gameStateLock.unlock();
    }
}

/**
 * MANDATORY: Resume the game (Thread-safe)
 */
public void resume_game() {
    gameStateLock.lock();
    try {
        gameState = GameState.PLAYING;
        pauseStatus = false;
        notify_observers("GAME_RESUMED", null);
    } finally {
        gameStateLock.unlock();
    }
}
```

### 8. Add Resource Cleanup Method

**Current Problem**: No proper cleanup when GameLogic is destroyed.

**Required Changes**:

```java
/**
 * MANDATORY: Cleanup resources when GameLogic is no longer needed
 */
public void dispose() {
    gameStateLock.lock();
    try {
        // Stop game update timer
        if (gameUpdateTimer != null) {
            gameUpdateTimer.cancel();
            gameUpdateTimer = null;
        }
        
        // Stop notification timer
        if (notificationTimer != null) {
            notificationTimer.cancel();
            notificationTimer = null;
        }
        
        // Clear all enemies
        enemyLock.writeLock().lock();
        try {
            currentEnemies.clear();
            currentBoss = null;
        } finally {
            enemyLock.writeLock().unlock();
        }
        
        // Clear projectiles
        projectiles.clear();
        
        // Clear observers
        synchronized (observerLock) {
            observers.clear();
        }
        
        // Reset game state
        gameState = GameState.MAIN_MENU;
        pauseStatus = false;
        victoryStatus = false;
        deathStatus = false;
        
    } finally {
        gameStateLock.unlock();
    }
}
```

## Implementation Priority

1. **High Priority**: Fix enemy management (race conditions in update_enemy_positions)
2. **High Priority**: Fix observer pattern thread safety
3. **Medium Priority**: Fix game state management
4. **Medium Priority**: Fix player action handling
5. **Low Priority**: Optimize performance (consider using read-write locks for better concurrency)

## Testing After Implementation

After implementing these fixes, the following scenarios should work correctly:

1. **Multiple enemies + boss** fighting simultaneously without race conditions
2. **Real-time combat** with proper synchronization
3. **Observer notifications** without concurrent modification exceptions
4. **Pause/resume** functionality working correctly
5. **Resource cleanup** when switching floors or ending game

## Performance Considerations

- The `ReadWriteLock` for enemies provides better performance than full synchronization
- `volatile` variables provide visibility guarantees without full synchronization overhead
- The observer pattern synchronization is minimal and shouldn't impact performance significantly
- Game state lock is only held briefly during updates

## Backward Compatibility

All public method signatures remain the same, ensuring backward compatibility with existing code that uses GameLogic.

## Conclusion

The thread safety fixes should target **GameLogic**, not BattleLogic, because:

1. ✅ **GameLogic handles all combat** - Multiple enemies, boss, real-time updates
2. ✅ **Dynamic enemy management** - Enemies created/destroyed during gameplay
3. ✅ **Observer notifications** - Multiple threads notifying simultaneously
4. ❌ **BattleLogic is unused** - Only used for fleeing, not actual combat

This analysis provides the correct foundation for implementing thread safety in the actual combat system. 