package model.gameLogic;

import interfaces.GameModel;
import interfaces.GameObserver;
import enums.GameState;
import enums.GameConstants;
import utilities.Position;
import model.characters.Player;
import model.characters.Enemy;
import model.characters.Boss;
import model.characters.Upgrader;
import model.map.Map;
import model.items.Item;
import model.items.Consumable;
import model.items.KeyItem;
import model.equipment.Equipment;
import model.equipment.Weapon;
import model.equipment.Armor;
import model.gameLogic.AttackUtils;
import model.gameLogic.AttackVisualData;
import model.gameLogic.Projectile;
import interfaces.GameController;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import model.characters.BaseClass;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.awt.Point;
import model.characters.Character;
import model.map.Map.Room;

/**
 * GameLogic class manages overall game state and coordinates between game systems.
 * Handles game flow, player actions, victory/defeat conditions, and game progression.
 */
public class GameLogic implements GameModel {
    // MANDATORY: Core game state attributes
    private GameState gameState;
    private boolean victoryStatus;
    private boolean deathStatus;
    private int playerProgress;
    private boolean pauseStatus;
    private boolean npcDialogue;

    // MANDATORY: Game components
    private Player player;
    private Map currentMap;
    private List<Enemy> currentEnemies;
    private Boss currentBoss;
    private Upgrader currentUpgrader;
    private boolean upgraderGreetedPlayer = false;
    private boolean upgraderWarnedPlayer = false;
    private int currentFloor;
    private int regularFloorCount; // Track regular floor numbers (1, 2, 3, 4, 5...)
    private Random random;

    // MANDATORY: Observer pattern implementation
    private List<GameObserver> observers;
    private final Object observerLock = new Object(); // Thread safety for observer operations
    
    // MANDATORY: Game state thread safety
    private final Object gameStateLock = new Object(); // Thread safety for game state operations
    
    // MANDATORY: Projectile list thread safety
    private final Object projectileLock = new Object(); // Thread safety for projectile operations
    
    // MANDATORY: Enemy list thread safety
    private final Object enemyLock = new Object(); // Thread safety for enemy operations
    private volatile boolean isDisposed = false;
    private final Object disposalLock = new Object();
    private final Object enemyUpdateLock = new Object();
    
    // Performance optimization: Batch item collection notifications
    private ConcurrentLinkedQueue<model.items.Item> pendingItemNotifications = new ConcurrentLinkedQueue<>();
    private Timer notificationTimer;
    private static final int NOTIFICATION_DELAY = 100; // 100ms delay

    // Game state tracking
    private long lastAttackTime = 0;
    private int lastAttackDX = 0;
    private int lastAttackDY = 1;
    private float lastAttackRange = 1;
    private double lastAttackAngle = Math.PI / 2; // Default to down

    private List<Projectile> projectiles = new ArrayList<>();

    private Timer gameUpdateTimer;
    private long lastUpdateTime = System.currentTimeMillis();

    // Track the last enemy that attacked the player for death screen
    private Enemy lastAttackingEnemy = null;
    
    // Floor transition system
    private boolean isFloorTransitioning = false;
    private long floorTransitionStartTime = 0;
    private static final long FLOOR_TRANSITION_DURATION = 2000; // 2 seconds black screen

    // Special floor type tracking
    private model.map.Map.FloorType currentFloorType = model.map.Map.FloorType.REGULAR;
    private int bonusFloorChance = 20; // Starts at 20%, increases by 5% each floor until triggered
    private boolean bonusFloorTriggered = false; // Track if bonus floor was triggered this cycle

    /**
     * MANDATORY: Constructor for GameLogic
     *
     * @param player The player character
     */
    public GameLogic(Player player) {
        this.player = player;
        this.gameState = GameState.MAIN_MENU;
        this.victoryStatus = false;
        this.deathStatus = false;
        this.playerProgress = 0;
        this.pauseStatus = false;
        this.npcDialogue = false;
        this.currentEnemies = new ArrayList<>();
        this.currentFloor = 1;
        this.regularFloorCount = 1;
        this.random = new Random();
        this.observers = new ArrayList<>();
        this.isDisposed = false; // Initialize disposal flag
        
        // Set the GameLogic reference in the player
        player.setGameLogic(this);
        
        // Initialize notification timer for performance optimization
        initializeNotificationTimer();
        
        // Start game update timer for projectiles and logic
        gameUpdateTimer = new Timer(true);
        gameUpdateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Check disposal status before running timer task
                if (!isDisposed) {
                    update_game_state();
                }
            }
        }, 0, 16); // ~60 FPS
    }

    /**
     * Initialize the notification timer for batching item collection events
     */
    private void initializeNotificationTimer() {
        notificationTimer = new Timer(true); // Daemon timer
        notificationTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Check disposal status before running timer task
                if (!isDisposed) {
                    processPendingItemNotifications();
                }
            }
        }, NOTIFICATION_DELAY, NOTIFICATION_DELAY);
    }

    /**
     * Process all pending item notifications in batch
     */
    private void processPendingItemNotifications() {
        // Check disposal status first
        if (isDisposed || pendingItemNotifications.isEmpty()) return;
        
        // Process all pending notifications at once
        while (!pendingItemNotifications.isEmpty() && !isDisposed) {
            model.items.Item item = pendingItemNotifications.poll();
            if (item != null && !isDisposed) {
                notify_observers("ITEM_COLLECTED", item);
            }
        }
    }

    /**
     * MANDATORY: Initialize game components
     */
    private void initialize_game() {
        generate_new_floor();
        place_player_on_map();
        gameState = GameState.PLAYING;
        notify_observers("GAME_INITIALIZED", this);
    }

    /**
     * MANDATORY: Handle player actions and update game state accordingly
     *
     * @param action The action the player is attempting
     * @param data Additional data for the action
     */
    public void handle_player_action(String action, Object data) {
        if (pauseStatus || npcDialogue) return;
        switch (action.toLowerCase()) {
            case "open_inventory":
                open_inventory();
                break;
            case "upgrade_equipment":
                handle_equipment_upgrade((Equipment) data);
                break;
            case "pause":
                toggle_pause();
                break;
            case "use_item":
                handle_item_usage((Item) data);
                break;
            case "start_new_game":
                // Only set CLASS_SELECTION when starting a new game from the main menu
                if (gameState == GameState.MAIN_MENU) {
                    gameState = GameState.CLASS_SELECTION;
                    notify_observers("GAME_STATE_CHANGED", GameState.CLASS_SELECTION);
                }
                break;
            case "class_selected":
                // Start the game after class selection
                initialize_game();
                place_entities_on_map();
                gameState = GameState.PLAYING;
                notify_observers("GAME_STATE_CHANGED", GameState.PLAYING);
                notify_observers("MAP_GENERATED", currentMap);
                break;
            case "resume_game":
                resume_game();
                break;
            default:
                notify_observers("INVALID_ACTION", action);
        }
        // Check game conditions after each action
        check_victory_condition();
        check_death_condition();
    }



    /**
     * MANDATORY: Handle interactions with different tile types
     *
     * @param tile The tile the player just moved onto
     */
    private void handle_tile_interaction(utilities.Tile tile) {
        // Mark tile as explored
        tile.set_explored();
        // Handle items on tile
        if (tile.has_items()) {
            List<Item> items = tile.get_items();
            for (Item item : items) {
                if (player.collect_item(item)) {
                    tile.remove_item(item);
                    notify_item_collected(item); // Use batching
                    
                    // Special message for Floor Key collection
                    if (item instanceof model.items.KeyItem && item.get_name().equals("Floor Key")) {
                        notify_observers("LOG_MESSAGE", "Floor Key collected! Use it to unlock the stairs.");
                    }
                }
            }
        }
        // Handle enemy encounters (DISABLED: no battle logic)
        // if (tile.get_occupant() instanceof Enemy) {
        //     Enemy enemy = (Enemy) tile.get_occupant();
        //     initiate_combat(enemy);
        // } else if (tile.get_occupant() instanceof Boss) {
        //     Boss boss = (Boss) tile.get_occupant();
        //     initiate_boss_battle(boss);
        // }
        // Handle special tiles
        switch (tile.get_tile_type()) {
            case STAIRS:
                // Check if player has Floor Key to unlock the stairs
                if (player_has_floor_key()) {
                    // Proceed to next floor (key will be consumed during transition)
                    if (!isFloorTransitioning) {
                        notify_observers("LOG_MESSAGE", "Floor Key found! Unlocking stairs to next floor...");
                        startFloorTransition();
                    }
                } else {
                    // Display message that Floor Key is needed
                    notify_observers("LOG_MESSAGE", "The stairs are locked! You need to find a Floor Key first.");
                }
                break;
        }
    }

    /**
     * MANDATORY: Handle player attack action
     */
    private void handle_attack_action() {
        // Only allow attack if enough time has passed (attack speed)
        BaseClass baseClass = player.getPlayerClassOOP();
        long now = System.currentTimeMillis();
        float attackSpeed = baseClass != null ? baseClass.getAttackSpeed() : 1.0f;
        if (now - lastAttackTime < (1000.0f / attackSpeed)) return;
        lastAttackTime = now;
        // --- Use aim direction for attacks ---
        int dx, dy;
        float projDx, projDy;
        double attackAngle;
        boolean useMouse = false;
        java.awt.Point mouse = null;
        // Check if mouse aiming mode is enabled
        try {
            java.awt.Window[] windows = java.awt.Window.getWindows();
            for (java.awt.Window w : windows) {
                if (w instanceof view.GameView) {
                    view.GameView gv = (view.GameView)w;
                    if (gv.isMouseAimingMode()) {
                        useMouse = true;
                    }
                }
            }
        } catch (Exception e) {}
        float px = player.getPixelX() + enums.GameConstants.TILE_SIZE / 2f;
        float py = player.getPixelY() + enums.GameConstants.TILE_SIZE / 2f;
        if (useMouse) {
            // Poll the real mouse position for attack direction
            mouse = java.awt.MouseInfo.getPointerInfo().getLocation();
            // Find the GamePanel component to convert coordinates
            javax.swing.SwingUtilities.invokeLater(() -> {}); // ensure EDT
            view.panels.GamePanel gamePanel = null;
            try {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window w : windows) {
                    if (w instanceof view.GameView) {
                        view.GameView gv = (view.GameView)w;
                        gamePanel = gv.get_game_panel();
                    }
                }
            } catch (Exception e) {}
            
            if (gamePanel != null) {
                // Convert mouse from screen coordinates to GamePanel coordinates
                javax.swing.SwingUtilities.convertPointFromScreen(mouse, gamePanel);
                
                // Convert mouse from GamePanel coordinates to map coordinates
                // The camera offset is applied to the map rendering, so we need to subtract it
                float mx = mouse.x - gamePanel.getMapOffsetX();
                float my = mouse.y - gamePanel.getMapOffsetY();
                
                projDx = mx - px;
                projDy = my - py;
                if (projDx == 0 && projDy == 0) projDy = 1;
                // Use raw mouse position for precise aiming - don't normalize for attack angle
                attackAngle = Math.atan2(projDy, projDx);
                // Only normalize for the dx/dy values used by other systems that expect -1,0,1
                dx = (Math.abs(projDx) > Math.abs(projDy)) ? (projDx > 0 ? 1 : -1) : 0;
                dy = (Math.abs(projDy) >= Math.abs(projDx)) ? (projDy > 0 ? 1 : -1) : 0;
            } else {
                // Fallback to player's current aim direction if we can't get the panel
                dx = player.getAimDX();
                dy = player.getAimDY();
                if (dx == 0 && dy == 0) {
                    dx = player.getLastAimDX();
                    dy = player.getLastAimDY();
                }
                if (dx == 0 && dy == 0) {
                    dx = 0;
                    dy = 1; // Default to down
                }
                projDx = dx;
                projDy = dy;
                attackAngle = Math.atan2(dy, dx);
            }
        } else {
            dx = player.getAimDX();
            dy = player.getAimDY();
            if (dx == 0 && dy == 0) {
                dx = player.getLastAimDX();
                dy = player.getLastAimDY();
            }
            if (dx == 0 && dy == 0) {
                dx = 0;
                dy = 1; // Default to down
            }
            projDx = dx;
            projDy = dy;
            attackAngle = Math.atan2(dy, dx);
        }
        lastAttackAngle = attackAngle;
        lastAttackDX = dx;
        lastAttackDY = dy;
        lastAttackRange = baseClass.getRange();
        
        // Swing-based attack: use unified system
        if (baseClass.hasMelee()) {
                    // Use unified swing attack system
        AttackVisualData swingData = AttackUtils.createSwingAttackData(
            dx, dy, lastAttackRange, attackAngle, baseClass, now
        );
        
        // Start swing attack detection using unified system
        AttackUtils.startSwingAttackDetection(swingData, 
            new AttackUtils.PlayerSwingHitDetector(player, currentEnemies, this));
        
        // Notify observers with swing data
        notify_observers("PLAYER_ATTACKED", swingData);
        } else {
            // Non-melee attacks (projectiles) use old system
            notify_observers("PLAYER_ATTACKED", new AttackVisualData(lastAttackDX, lastAttackDY, lastAttackRange, lastAttackAngle));
        }
        
        // If Mage and hasProjectile, check MP and spawn a projectile
        if (baseClass.hasProjectile() && baseClass instanceof model.characters.MageClass) {
            int manaCost = ((model.characters.MageClass) baseClass).getProjectileManaCost();
            if (player.get_current_mp() >= manaCost) {
                player.use_mp(manaCost);
                float speed = baseClass.getProjectileSpeed();
                float moveSpeed = baseClass.getMoveSpeed();
                if (speed < moveSpeed) speed = moveSpeed + 0.1f;
                float maxDist = baseClass.getProjectileTravelDistance();
                float radius = enums.GameConstants.TILE_SIZE / 4f;
                synchronized (projectileLock) {
                    projectiles.add(new Projectile(px, py, projDx, projDy, speed, maxDist, radius, player));
                }
            } else {
                // Not enough MP to cast projectile
                notify_observers("LOG_MESSAGE", "Not enough MP to cast spell!");
            }
        } else if (baseClass.hasProjectile() && baseClass instanceof model.characters.RangerClass) {
            // Ranger: unlimited projectiles, no MP cost
            float speed = baseClass.getProjectileSpeed();
            float moveSpeed = baseClass.getMoveSpeed();
            if (speed < moveSpeed) speed = moveSpeed + 0.1f;
            float maxDist = baseClass.getProjectileTravelDistance();
            float radius = enums.GameConstants.TILE_SIZE / 4f;
            projectiles.add(new Projectile(px, py, projDx, projDy, speed, maxDist, radius, player));
            
            // Create Ranger bow attack visual data (static angle, no sliding)
            model.gameLogic.AttackVisualData bowData = model.gameLogic.AttackUtils.createStaticBowData(
                (int)projDx, (int)projDy, attackAngle, now
            );
            notify_observers("PLAYER_RANGER_BOW_ATTACK", bowData);
        }
    }
    


    /**
     * MANDATORY: Handle item usage
     *
     * @param item The item to use
     */
    private void handle_item_usage(Item item) {
        // Special handling for Upgrade Crystals
        if (item instanceof model.items.KeyItem && item.get_name().equals("Upgrade Crystal")) {
            handle_upgrade_crystal_usage();
            return;
        }
        
        if (player.use_item(item)) {
            // Trigger item flash effect
            if (item instanceof Consumable) {
                Consumable consumable = (Consumable) item;
                notify_observers("ITEM_FLASH", consumable.get_effect_type());
            }
            notify_observers("ITEM_USED", item);
        } else {
            notify_observers("ITEM_USE_FAILED", item);
        }
    }
    
    /**
     * Handle Upgrade Crystal usage - check if upgrader is nearby and trigger upgrade
     */
    private void handle_upgrade_crystal_usage() {
        if (currentUpgrader == null) {
            notify_observers("UPGRADER_MESSAGE", "No upgrader nearby. Find an upgrader to use Upgrade Crystals.");
            return;
        }
        
        if (!currentUpgrader.isVisible()) {
            notify_observers("UPGRADER_MESSAGE", "The upgrader is not visible. Clear the area of enemies first.");
            return;
        }
        
        if (!currentUpgrader.isPlayerInRange(player.get_position())) {
            notify_observers("UPGRADER_MESSAGE", "You are too far from the upgrader. Move closer.");
            return;
        }
        
        // Check if player has the required equipment equipped
        Equipment equipmentToUpgrade = null;
        String equipmentType = "";
        if (currentUpgrader.getUpgraderType() == model.characters.Upgrader.UpgraderType.WEAPON) {
            equipmentToUpgrade = player.get_equipped_weapon();
            equipmentType = "weapon";
        } else {
            equipmentToUpgrade = player.get_equipped_armor();
            equipmentType = "armor";
        }
        
        if (equipmentToUpgrade == null) {
            notify_observers("UPGRADER_MESSAGE", "It appears you don't have " + equipmentType + " on you right now.");
            return;
        }
        
        // Check if player has enough upgrade crystals
        List<Item> upgradeCrystals = player.get_inventory().stream()
            .filter(item -> item instanceof model.items.KeyItem && item.get_name().equals("Upgrade Crystal"))
            .collect(java.util.stream.Collectors.toList());
        
        if (upgradeCrystals.size() < currentUpgrader.getUpgradeCost()) {
            notify_observers("UPGRADER_MESSAGE", "You don't have enough Upgrade Crystals. You need " + 
                           currentUpgrader.getUpgradeCost() + " crystals.");
            return;
        }
        
        // Attempt the upgrade first (without consuming crystals)
        if (currentUpgrader.attemptUpgrade(player)) {
            // Only consume crystals if the upgrade was successful
            int crystalsToConsume = currentUpgrader.getUpgradeCost();
            for (int i = 0; i < crystalsToConsume; i++) {
                if (!upgradeCrystals.isEmpty()) {
                    Item crystalToConsume = upgradeCrystals.get(0);
                    player.use_item(crystalToConsume);
                    upgradeCrystals.remove(0);
                }
            }
            
            Equipment upgradedEquipment = null;
            if (currentUpgrader.getUpgraderType() == model.characters.Upgrader.UpgraderType.WEAPON) {
                upgradedEquipment = player.get_equipped_weapon();
            } else {
                upgradedEquipment = player.get_equipped_armor();
            }
            
            if (upgradedEquipment != null) {
                String message = currentUpgrader.getDisplayName() + ": \"All done, until next time!\"";
                notify_observers("UPGRADER_MESSAGE", message);
                
                // Notify observers of equipment changes to update UI
                notify_observers("WEAPON_EQUIPPED", upgradedEquipment);
                notify_observers("ARMOR_EQUIPPED", upgradedEquipment);
                notify_observers("INVENTORY_CHANGED", null);
                
                // Start the disappearing process
                currentUpgrader.startDisappearingAfterUpgrade();
            }
        } else {
            notify_observers("UPGRADER_MESSAGE", "Upgrade failed. Equipment may be at maximum tier.");
        }
    }

    /**
     * MANDATORY: Apply item effects to player
     *
     * @param item The item whose effects to apply
     */
    public void apply_item_effects(Item item) {
        if (item instanceof Consumable) {
            Consumable consumable = (Consumable) item;
            switch (consumable.get_effect_type().toLowerCase()) {
                case "health":
                    notify_observers("PLAYER_HEALED", consumable.get_potency());
                    break;
                case "mana":
                    notify_observers("PLAYER_MP_RESTORED", consumable.get_potency());
                    break;
                case "experience":
                    notify_observers("EXPERIENCE_GAINED", consumable.get_potency());
                    break;
            }
        }
    }

    /**
     * MANDATORY: Handle equipment upgrade process
     *
     * @param equipment The equipment to upgrade
     */
    private void handle_equipment_upgrade(Equipment equipment) {
        if (player.upgrade_equipment(equipment)) {
            notify_observers("EQUIPMENT_UPGRADED", equipment);
        } else {
            notify_observers("UPGRADE_FAILED", equipment);
        }
    }

    /**
     * MANDATORY: Open inventory interface
     */
    private void open_inventory() {
        gameState = GameState.INVENTORY;
        player.open_inventory();
        notify_observers("GAME_STATE_CHANGED", GameState.INVENTORY);
    }

    /**
     * MANDATORY: Update enemy positions and AI behavior (Thread-safe)
     */
    private void update_enemy_positions() {
        // Check disposal status first
        if (isDisposed || currentEnemies == null) {
            return;
        }
        
        // Use defensive copy to prevent concurrent modification
        List<Enemy> enemiesCopy;
        synchronized (enemyLock) {
            if (isDisposed) {
                return;
            }
            enemiesCopy = new ArrayList<>(currentEnemies);
        }
        
        // Process enemies from the copy
        Iterator<Enemy> enemyIterator = enemiesCopy.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            
            // Check disposal status during iteration
            if (isDisposed) {
                return;
            }
            
            if (enemy.shouldBeDeleted()) {
                // Remove from original list with synchronization
                synchronized (enemyLock) {
                    if (!isDisposed) {
                        currentEnemies.remove(enemy);
                    }
                }
                continue;
            }
            
            if (enemy.is_alive() && !enemy.isDying()) {
                // Simple AI: move towards player if within aggro range and player is not invisible
                if (player != null && !isDisposed && 
                    player.get_position().distance_to(enemy.get_position()) <= enemy.get_aggro_range() && 
                    !player.is_invisibility_effect_active()) {
                    move_enemy_towards_player(enemy);
                }
                
                // Handle enemy attack logic (only if player is not invisible)
                if (player != null && !isDisposed && !player.is_invisibility_effect_active()) {
                    handle_enemy_attack_logic(enemy);
                }
            }
        }
    }
    
    /**
     * Handle enemy attack logic with wind-up phase
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
        if (enemy instanceof model.characters.Boss) {
            sizeMultiplier = ((model.characters.Boss) enemy).getSizeMultiplier();
            rangeModifier = ((model.characters.Boss) enemy).getRangeModifier();
        }
        
        boolean hasProjectile = enemy.getEnemyClassOOP().hasProjectile();
        boolean hasMelee = enemy.getEnemyClassOOP().hasMelee();
        float projRange = hasProjectile ? enemy.getEnemyClassOOP().getProjectileTravelDistance() * sizeMultiplier * rangeModifier : 0f;
        float meleeRange = hasMelee ? enemy.getEnemyClassOOP().getRange() * sizeMultiplier * rangeModifier : 0f;
        boolean canProjectile = hasProjectile && dist <= projRange;
        boolean canMelee = hasMelee && dist <= meleeRange;
        
        // Start wind-up phase if player is in attack range
        if (canProjectile || canMelee) {
            startEnemyWindUp(enemy);
        }
    }
    
    /**
     * Start enemy wind-up phase
     */
    private void startEnemyWindUp(Enemy enemy) {
        enemy.setInWindUpState(true);
        enemy.setWindUpStartTime(System.currentTimeMillis());
        // Add 0.5 seconds to chase timer to prevent returning to idle during wind-up
        enemy.setChaseEndTime(System.currentTimeMillis() + 500); // Add 0.5 seconds
        // Notify observers to start enemy cyan blinking
        notify_observers("ENEMY_WIND_UP_STARTED", enemy);
    }
    
    /**
     * Execute enemy attack after wind-up phase
     */
    private void executeEnemyAttack(Enemy enemy) {
        long now = System.currentTimeMillis();
        enemy.setLastAttackTime(now);
        
        // Use current aim direction (enemy can still aim during wind-up)
        int aimDX = enemy.getAimDX();
        int aimDY = enemy.getAimDY();
        float preciseAimDX = enemy.getPreciseAimDX();
        float preciseAimDY = enemy.getPreciseAimDY();
        
        boolean hasProjectile = enemy.getEnemyClassOOP().hasProjectile();
        boolean hasMelee = enemy.getEnemyClassOOP().hasMelee();
        
        // Calculate distance to player to determine attack type
        float enemyX = enemy.getPixelX() + GameConstants.TILE_SIZE / 2f;
        float enemyY = enemy.getPixelY() + GameConstants.TILE_SIZE / 2f;
        float playerX = player.getPixelX() + GameConstants.TILE_SIZE / 2f;
        float playerY = player.getPixelY() + GameConstants.TILE_SIZE / 2f;
        float dist = (float)Math.hypot(playerX - enemyX, playerY - enemyY) / GameConstants.TILE_SIZE;
        
        // Adjust range based on entity size and boss modifiers
        float sizeMultiplier = 1.0f;
        float rangeModifier = 1.0f;
        if (enemy instanceof model.characters.Boss) {
            sizeMultiplier = ((model.characters.Boss) enemy).getSizeMultiplier();
            rangeModifier = ((model.characters.Boss) enemy).getRangeModifier();
        }
        
        float meleeRange = hasMelee ? enemy.getEnemyClassOOP().getRange() * sizeMultiplier * rangeModifier : 0f;
        float projectileRange = hasProjectile ? enemy.getEnemyClassOOP().getProjectileTravelDistance() * sizeMultiplier * rangeModifier : 0f;
        
        // Use melee if player is in melee range, otherwise use projectile if available
        if (hasMelee && dist <= meleeRange) {
            // Execute melee attack when player is close enough
            handle_enemy_melee_attack(enemy, aimDX, aimDY);
        } else if (hasProjectile && dist <= projectileRange) {
            // Execute projectile attack when player is in projectile range but not melee range
            handle_enemy_projectile_attack(enemy, preciseAimDX, preciseAimDY);
        } else if (hasMelee) {
            // Fallback to melee if no projectile or player is too close for projectile
            handle_enemy_melee_attack(enemy, aimDX, aimDY);
        }
    }

    /**
     * MANDATORY: Move enemy towards player position
     *
     * @param enemy The enemy to move
     */
    private void move_enemy_towards_player(Enemy enemy) {
        Position enemyPos = enemy.get_position();
        Position playerPos = player.get_position();
        int deltaX = Integer.compare(playerPos.get_x(), enemyPos.get_x());
        int deltaY = Integer.compare(playerPos.get_y(), enemyPos.get_y());
        Position newPos = enemyPos.move(deltaX, deltaY);
        if (currentMap.is_valid_move(newPos)) {
            enemy.move_to(newPos.get_x() * enums.GameConstants.TILE_SIZE, newPos.get_y() * enums.GameConstants.TILE_SIZE);
        }
    }

    /**
     * MANDATORY: Generate new dungeon floor
     */
    public void trigger_proc_generation() {
        generate_new_floor();
        place_entities_on_map();
        notify_observers("NEW_FLOOR_GENERATED", currentFloor);
    }

    /**
     * MANDATORY: Generate new floor with procedural generation
     */
    private void generate_new_floor() {
        // Determine floor type before generation
        currentFloorType = determineFloorType();
        
        // Generate new map with floor type
        currentMap = new Map(currentFloor, currentFloorType);
        currentEnemies.clear();
        currentBoss = null;
        currentUpgrader = null; // Clear upgrader when generating new floor
        projectiles.clear(); // Clear projectiles when generating new floor
    }

    /**
     * MANDATORY: Place player on the map at entrance
     */
    private void place_player_on_map() {
        // Get entrance room (first room)
        model.map.Map.Room entranceRoom = currentMap.get_rooms().get(0);
        Position spawnPos = null;
        if (entranceRoom != null) {
            spawnPos = currentMap.get_random_floor_position_in_room(entranceRoom);
        }
        if (spawnPos == null) {
            // Fallback: use map center
            spawnPos = new Position(currentMap.get_width() / 2, currentMap.get_height() / 2);
        }
        // Use pixel-based move_to
        player.move_to(spawnPos.get_x() * enums.GameConstants.TILE_SIZE, spawnPos.get_y() * enums.GameConstants.TILE_SIZE);
        // Update logical tile position as well
        player.syncToTilePosition(spawnPos);
        // Set the entrance tile to the spawn position (green tile)
        currentMap.set_entrance_tile(spawnPos);
        
        // Initial exploration: reveal 3x3 area around player spawn so they can see something
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int nx = spawnPos.get_x() + dx;
                int ny = spawnPos.get_y() + dy;
                utilities.Tile t = currentMap.get_tile(nx, ny);
                if (t != null) t.set_explored();
            }
        }
    }

    /**
     * MANDATORY: Place enemies and items on the map based on floor type
     */
    private void place_entities_on_map() {
        switch (currentFloorType) {
            case BOSS:
                place_entities_boss_floor();
                break;
            case BONUS:
                place_entities_bonus_floor();
                break;
            default:
                place_entities_regular_floor();
                break;
        }
    }
    
    /**
     * Place entities for BOSS floor (fewer enemies, guaranteed boss, fewer items)
     */
    private void place_entities_boss_floor() {
        // Place fewer enemies for boss floor
        List<Position> enemyPositions = currentMap.get_enemy_locations();
        for (Position pos : enemyPositions) {
            Enemy enemy = create_random_enemy(pos);
            enemy.setMap(currentMap);
            enemy.setPlayer(player);
            synchronized (enemyLock) {
                currentEnemies.add(enemy);
            }
            currentMap.place_enemy(enemy, pos);
        }
        
        // Guaranteed boss placement
        Position bossPos = currentMap.get_boss_position();
        if (bossPos != null) {
            currentBoss = create_boss_for_floor(bossPos);
            currentBoss.move_to(bossPos.get_x() * enums.GameConstants.TILE_SIZE, bossPos.get_y() * enums.GameConstants.TILE_SIZE);
            synchronized (enemyLock) {
                currentEnemies.add(currentBoss);
            }
        } else {
            System.err.println("Boss position not found on boss floor!");
        }
        
        // No upgrader on boss floors (only on bonus floors)
        
        // Place fewer items (just Floor Key and maybe 1-2 others)
        place_items_boss_floor();
        
        // Notify player about boss floor
        notify_observers("LOG_MESSAGE", "This floor's boss has the Key, claim it for yourself, Hero!");
    }
    
    /**
     * Place entities for BONUS floor (only upgrader and Floor Key)
     */
    private void place_entities_bonus_floor() {
        // No enemies on bonus floor
        // No boss on bonus floor
        
        // Place upgrader
        place_upgrader_on_map();
        
        // Place only Floor Key
        place_items_bonus_floor();
    }
    
    /**
     * Place entities for regular floor (normal enemy/item distribution)
     */
    private void place_entities_regular_floor() {
        // Place enemies
        List<Position> enemyPositions = currentMap.get_enemy_locations();
        for (Position pos : enemyPositions) {
            Enemy enemy = create_random_enemy(pos);
            enemy.setMap(currentMap);
            enemy.setPlayer(player);
            synchronized (enemyLock) {
                currentEnemies.add(enemy);
            }
            currentMap.place_enemy(enemy, pos);
        }
        
        // No boss on regular floors
        // No upgrader on regular floors (only on bonus floors)
        
        // Place items normally
        place_items_regular_floor();
    }

    /**
     * MANDATORY: Create random enemy for current floor
     *
     * @param position Enemy spawn position
     * @return New Enemy instance
     */
    private Enemy create_random_enemy(Position position) {
        enums.CharacterClass[] classes = enums.CharacterClass.values();
        enums.CharacterClass randomClass = classes[random.nextInt(classes.length)];
        String[] aiPatterns = {"aggressive", "defensive", "magical", "sneaky"};
        String randomAI = aiPatterns[random.nextInt(aiPatterns.length)];
        Enemy enemy = new Enemy("Floor " + currentFloor + " Enemy", randomClass, position, randomAI);
        enemy.setGameLogic(this); // Set the GameLogic reference
        return enemy;
    }

    /**
     * MANDATORY: Create boss for current floor
     *
     * @param position Boss spawn position
     * @return New Boss instance
     */
    private Boss create_boss_for_floor(Position position) {
        enums.CharacterClass[] classes = enums.CharacterClass.values();
        enums.CharacterClass bossClass = classes[random.nextInt(classes.length)];
        Boss boss = new Boss("Floor " + currentFloor + " Boss", bossClass, position);
        boss.setMap(currentMap);
        boss.setPlayer(player);
        boss.setGameLogic(this); // Set the GameLogic reference
        boss.move_to(position.get_x() * enums.GameConstants.TILE_SIZE, position.get_y() * enums.GameConstants.TILE_SIZE);
        return boss;
    }
    
    /**
     * Check if player has a Floor Key in their inventory
     *
     * @return true if player has a Floor Key
     */
    private boolean player_has_floor_key() {
        List<Item> inventory = player.get_inventory();
        for (Item item : inventory) {
            if (item instanceof model.items.KeyItem) {
                model.items.KeyItem keyItem = (model.items.KeyItem) item;
                if (keyItem.get_name().equals("Floor Key")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Consume a Floor Key from player's inventory
     */
    private void consume_floor_key() {
        boolean keyRemoved = player.remove_floor_key();
        if (keyRemoved) {
            notify_observers("LOG_MESSAGE", "Floor Key consumed - stairs unlocked!");
        }
    }

    /**
     * Helper method to find a random walkable position on the map
     */
    private Position find_random_walkable_position() {
        int maxAttempts = 100;
        int attempts = 0;
        Random rand = new Random();
        
        while (attempts < maxAttempts) {
            int x = rand.nextInt(currentMap.get_width());
            int y = rand.nextInt(currentMap.get_height());
            utilities.Tile tile = currentMap.get_tile(x, y);
            
            if (tile != null && tile.is_walkable() && !tile.has_items()) {
                return new Position(x, y);
            }
            attempts++;
        }
        return null; // No walkable position found
    }
    
    /**
     * NEW: Place upgrader on the map
     */
    private void place_upgrader_on_map() {
        // Find upgrader spawn position (center of upgrader room)
        Position upgraderPos = find_upgrader_spawn_position();
        if (upgraderPos == null) {
            return; // No upgrader spawn position found
        }
        
        // Determine upgrader type based on player's equipped equipment
        Upgrader.UpgraderType upgraderType = determine_upgrader_type();
        
        // Create and place the upgrader
        currentUpgrader = new Upgrader(upgraderPos, upgraderType);
        currentUpgrader.calculateUpgradeCost(player);
        currentUpgrader.move_to(upgraderPos.get_x() * enums.GameConstants.TILE_SIZE, upgraderPos.get_y() * enums.GameConstants.TILE_SIZE);
        
    }
    
    /**
     * Find upgrader spawn position in the map (single UPGRADER_SPAWN tile)
     */
    private Position find_upgrader_spawn_position() {
        // Find the single UPGRADER_SPAWN tile
        for (int x = 0; x < currentMap.get_width(); x++) {
            for (int y = 0; y < currentMap.get_height(); y++) {
                utilities.Tile tile = currentMap.get_tile(x, y);
                if (tile != null && tile.get_tile_type() == enums.TileType.UPGRADER_SPAWN) {
                    return new Position(x, y);
                }
            }
        }
        
        return null;
    }
    
    /**
     * Determine which type of upgrader to spawn based on player's equipment
     */
    private Upgrader.UpgraderType determine_upgrader_type() {
        Equipment weapon = player.get_equipped_weapon();
        Equipment armor = player.get_equipped_armor();
        
        int weaponTier = (weapon != null) ? weapon.get_tier() : 0;
        int armorTier = (armor != null) ? armor.get_tier() : 0;
        
        // Favor the lower tier equipment, with some randomness
        if (weaponTier < armorTier) {
            return Upgrader.UpgraderType.WEAPON;
        } else if (armorTier < weaponTier) {
            return Upgrader.UpgraderType.ARMOR;
        } else {
            // Same tier, random choice
            return (random.nextBoolean()) ? Upgrader.UpgraderType.WEAPON : Upgrader.UpgraderType.ARMOR;
        }
    }
    
    /**
     * NEW: Update upgrader visibility and handle interactions
     */
    public void update_upgrader() {
        if (currentUpgrader == null) {
            return;
        }
        
        // Get nearby enemies for field of view detection
        List<Character> nearbyEnemies = get_characters_near_upgrader();
        
        // Update upgrader visibility based on nearby enemies
        currentUpgrader.updateVisibility(nearbyEnemies);
        
        // Remove upgrader only if it has completely disappeared (hasMadeDeal is true)
        if (currentUpgrader.hasMadeDeal()) {
            currentUpgrader = null;
            return;
        }
        
        // Check if player is in field of view range
        boolean playerInFieldOfView = currentUpgrader.isPlayerInRange(player.get_position());
        boolean enemiesInFieldOfView = nearbyEnemies.stream()
            .anyMatch(enemy -> enemy instanceof Enemy || enemy instanceof Boss);
        
        // Handle dialogue messages
        if (playerInFieldOfView && currentUpgrader.isVisible()) {
            if (!upgraderGreetedPlayer) {
                // First time player enters field of view - show greeting
                show_upgrader_greeting();
                upgraderGreetedPlayer = true;
            } else if (enemiesInFieldOfView && !upgraderWarnedPlayer) {
                // Enemy appears while player is in range - show warning
                show_upgrader_warning();
                upgraderWarnedPlayer = true;
            }
            
            // Removed the interaction message to avoid cluttering the dialogue
        } else if (!playerInFieldOfView) {
            // Player left field of view - reset dialogue flags
            upgraderGreetedPlayer = false;
            upgraderWarnedPlayer = false;
        }
    }
    
    /**
     * Show upgrader interaction message
     */
    private void show_upgrader_interaction_message() {
        if (currentUpgrader == null) return;
        
        Equipment equipment = null;
        if (currentUpgrader.getUpgraderType() == Upgrader.UpgraderType.WEAPON) {
            equipment = player.get_equipped_weapon();
        } else {
            equipment = player.get_equipped_armor();
        }
        
        String equipmentName = (equipment != null) ? equipment.get_name() : "No equipment";
        String message = currentUpgrader.getDisplayName() + " offers to upgrade your " + 
                        equipmentName + " for " + currentUpgrader.getUpgradeCost() + " Upgrade Crystals. Press ENTER to accept.";
        
        notify_observers("UPGRADER_INTERACTION", message);
    }
    
    /**
     * Show upgrader greeting message
     */
    private void show_upgrader_greeting() {
        if (currentUpgrader == null) return;
        
        String equipmentType = (currentUpgrader.getUpgraderType() == Upgrader.UpgraderType.WEAPON) ? "weapon" : "armor";
        String greeting = currentUpgrader.getDisplayName() + ": \"Ah, a traveler! I can enhance your " + equipmentType + " for a modest fee of upgrade crystals.\"";
        notify_observers("UPGRADER_GREETING", greeting);
    }
    
    /**
     * Show upgrader warning message
     */
    private void show_upgrader_warning() {
        if (currentUpgrader == null) return;
        
        String warning = currentUpgrader.getDisplayName() + ": \"Shh! Enemies nearby! Stay quiet or we'll both be discovered!\"";
        notify_observers("UPGRADER_WARNING", warning);
    }
    
    /**
     * NEW: Handle upgrader interaction (called when player presses ENTER)
     */
    public void handle_upgrader_interaction() {
        if (currentUpgrader == null || !currentUpgrader.isVisible() || currentUpgrader.hasMadeDeal()) {
            return;
        }
        
        if (!currentUpgrader.isPlayerInRange(player.get_position())) {
            notify_observers("UPGRADER_MESSAGE", "You are too far from the upgrader.");
            return;
        }
        
        // Check if player has the required equipment equipped
        Equipment equipmentToUpgrade = null;
        String equipmentType = "";
        if (currentUpgrader.getUpgraderType() == model.characters.Upgrader.UpgraderType.WEAPON) {
            equipmentToUpgrade = player.get_equipped_weapon();
            equipmentType = "weapon";
        } else {
            equipmentToUpgrade = player.get_equipped_armor();
            equipmentType = "armor";
        }
        
        if (equipmentToUpgrade == null) {
            notify_observers("UPGRADER_MESSAGE", "It appears you don't have " + equipmentType + " on you right now.");
            return;
        }
        
        // Check if player has enough upgrade crystals
        List<Item> upgradeCrystals = player.get_inventory().stream()
            .filter(item -> item instanceof KeyItem && item.get_name().equals("Upgrade Crystal"))
            .collect(java.util.stream.Collectors.toList());
        
        if (upgradeCrystals.size() < currentUpgrader.getUpgradeCost()) {
            notify_observers("UPGRADER_MESSAGE", "You don't have enough Upgrade Crystals. You need " + 
                           currentUpgrader.getUpgradeCost() + " crystals.");
            return;
        }
        
        // Attempt the upgrade first (without consuming crystals)
        if (currentUpgrader.attemptUpgrade(player)) {
            // Only consume crystals if the upgrade was successful
            int crystalsToConsume = currentUpgrader.getUpgradeCost();
            for (int i = 0; i < crystalsToConsume; i++) {
                if (!upgradeCrystals.isEmpty()) {
                    Item crystalToConsume = upgradeCrystals.get(0);
                    player.use_item(crystalToConsume);
                    upgradeCrystals.remove(0);
                }
            }
            
            Equipment upgradedEquipment = null;
            if (currentUpgrader.getUpgraderType() == Upgrader.UpgraderType.WEAPON) {
                upgradedEquipment = player.get_equipped_weapon();
            } else {
                upgradedEquipment = player.get_equipped_armor();
            }
            
            if (upgradedEquipment != null) {
                String message = currentUpgrader.getDisplayName() + ": \"All done, until next time!\"";
                notify_observers("UPGRADER_MESSAGE", message);
                
                // Notify observers of equipment changes to update UI
                notify_observers("WEAPON_EQUIPPED", upgradedEquipment);
                notify_observers("ARMOR_EQUIPPED", upgradedEquipment);
                notify_observers("INVENTORY_CHANGED", null);
                
                // Start the disappearing process instead of immediately removing
                currentUpgrader.startDisappearingAfterUpgrade();
            }
        } else {
            notify_observers("UPGRADER_MESSAGE", "Upgrade failed. Equipment may be at maximum tier.");
        }
    }

    /**
     * NEW: Comprehensive list of all weapons loaded from JSON configuration
     */
    private List<model.equipment.Weapon> getAllWeapons() {
        return utilities.WeaponDefinitionManager.getInstance().getAllWeapons();
    }
    
    /**
     * NEW: Comprehensive list of all armor loaded from JSON configuration
     */
    private List<model.equipment.Armor> getAllArmor() {
        return utilities.WeaponDefinitionManager.getInstance().getAllArmor();
    }
    
    /**
     * NEW: Get class-appropriate armor for enemies and bosses
     * All armor is universal, but we can assign thematically appropriate armor
     */
    private model.equipment.Armor getRandomArmorForClass(enums.CharacterClass characterClass) {
        List<model.equipment.Armor> allArmor = getAllArmor();
        List<model.equipment.Armor> classAppropriateArmor = new ArrayList<>();
        
        switch (characterClass) {
            case WARRIOR:
                // Warriors get heavy/combat armor
                for (model.equipment.Armor armor : allArmor) {
                    String name = armor.get_name().toLowerCase();
                    if (name.contains("combat") || name.contains("dragon") || name.contains("golem") || 
                        name.contains("guard") || name.contains("hero") || name.contains("knight") || 
                        name.contains("gilded")) {
                        classAppropriateArmor.add(armor);
                    }
                }
                break;
            case MAGE:
                // Mages get robes and magical armor
                for (model.equipment.Armor armor : allArmor) {
                    String name = armor.get_name().toLowerCase();
                    if (name.contains("robe") || name.contains("arcane") || name.contains("fancy") || 
                        name.contains("royal") || name.contains("vine") || name.contains("wizard")) {
                        classAppropriateArmor.add(armor);
                    }
                }
                break;
            case ROGUE:
                // Rogues get light/stealth armor
                for (model.equipment.Armor armor : allArmor) {
                    String name = armor.get_name().toLowerCase();
                    if (name.contains("animal") || name.contains("basic") || name.contains("vine")) {
                        classAppropriateArmor.add(armor);
                    }
                }
                break;
            case RANGER:
                // Rangers get medium/leather armor
                for (model.equipment.Armor armor : allArmor) {
                    String name = armor.get_name().toLowerCase();
                    if (name.contains("basic") || name.contains("iced") || name.contains("combat")) {
                        classAppropriateArmor.add(armor);
                    }
                }
                break;
        }
        
        // If no class-appropriate armor found, return random armor
        if (classAppropriateArmor.isEmpty()) {
            return allArmor.get(random.nextInt(allArmor.size()));
        }
        
        return classAppropriateArmor.get(random.nextInt(classAppropriateArmor.size()));
    }
    
    /**
     * NEW: Get random weapon for a specific character class
     */
    private model.equipment.Weapon getRandomWeaponForClass(enums.CharacterClass characterClass) {
        return utilities.WeaponDefinitionManager.getInstance().getRandomWeaponForClass(characterClass);
    }

    /**
     * NEW: Get weapons filtered by character class for thematic spawning
     */
    private List<model.equipment.Weapon> getWeaponsForClass(enums.CharacterClass characterClass) {
        return utilities.WeaponDefinitionManager.getInstance().getWeaponsForClass(characterClass);
    }

    /**
     * MANDATORY: Create random item for floor
     *
     * @return New Item instance
     */
    private Item create_random_item() {
        int roll = random.nextInt(100);
        
        if (roll < 15) {
            // 15% chance for weapons - pick from class-specific weapons
            List<model.equipment.Weapon> classWeapons = getWeaponsForClass(player.get_character_class());
            if (!classWeapons.isEmpty()) {
                return classWeapons.get(random.nextInt(classWeapons.size()));
            } else {
                // Fallback to all weapons if no class-specific weapons found
                List<model.equipment.Weapon> weapons = getAllWeapons();
                return weapons.get(random.nextInt(weapons.size()));
            }
        } else if (roll < 30) {
            // 15% chance for armor - pick random from all armor (universal)
            List<model.equipment.Armor> armor = getAllArmor();
            return armor.get(random.nextInt(armor.size()));
        } else if (roll < 55) {
            // 25% chance for health consumables (increased from 15%)
            int healthRoll = random.nextInt(100);
            if (healthRoll < 50) {
                return new Consumable("Minor Health Potion", 25, "health");
            } else if (healthRoll < 80) {
                return new Consumable("Health Potion", 50, "health");
            } else {
                return new Consumable("Greater Health Potion", 100, "health");
            }
        } else if (roll < 65) {
            // 10% chance for mana consumables - ONLY if player is Mage
            if (player.get_character_class() == enums.CharacterClass.MAGE) {
                int manaRoll = random.nextInt(100);
                if (manaRoll < 70) {
                    return new Consumable("Mana Potion", 30, "mana");
                } else {
                    return new Consumable("Greater Mana Potion", 60, "mana");
                }
            } else {
                // If not Mage, redistribute this 10% to other items
                // We'll add this probability to the next category (experience items)
                return new Consumable("Experience Scroll", 100, "experience");
            }
        } else if (roll < 70) {
            // 5% chance for experience items
            return new Consumable("Experience Scroll", 100, "experience");
        } else if (roll < 75) {
            // 5% chance for lamp (clarity effect)
            return new Consumable("Lamp", 10, "clarity");
        } else if (roll < 77) {
            // 2% chance for vanish cloak (invisibility effect)
            return new Consumable("Vanish Cloak", 8, "invisibility");
        } else if (roll < 80) {
            // 3% chance for swift winds (speed effect)
            return new Consumable("Swift Winds", 5, "swiftness");
        } else if (roll < 82) {
            // 2% chance for immortality amulet
            return new Consumable("Immortality Amulet", 5, "immortality");
        } else {
            // 18% chance for upgrade items (restored to original percentage)
            return new model.items.KeyItem("Upgrade Crystal", "any");
        }
    }

    /**
     * MANDATORY: Advance to next dungeon floor
     */
    private void advance_to_next_floor() {
        // Always increment regular floor count (boss and bonus floors count now)
        regularFloorCount++;
        
        // Update bonus floor chance
        updateBonusFloorChance();
        
        currentFloor++;
        playerProgress = currentFloor - 1;
        
        trigger_proc_generation();
        place_player_on_map();
        // Reset floor transitioning flag
        isFloorTransitioning = false;
        notify_observers("FLOOR_ADVANCED", currentFloor);
    }

    /**
     * MANDATORY: Check victory condition
     */
    public void check_victory_condition() {
        // No victory condition - game continues infinitely
    }

    /**
     * Start floor transition with black screen
     */
    private void startFloorTransition() {
        // Consume the Floor Key when transition starts
        consume_floor_key();
        
        isFloorTransitioning = true;
        floorTransitionStartTime = System.currentTimeMillis();
        // Clear all entities immediately for black screen
        synchronized (enemyLock) {
            currentEnemies.clear();
        }
        currentBoss = null;
        projectiles.clear(); // Clear projectiles when generating new floor
        // Clear the map completely - we'll generate a new one after delay
        currentMap = null;
        notify_observers("FLOOR_TRANSITION_STARTED", null);
        
        // Don't generate new floor immediately - wait for transition to complete
    }
    
    /**
     * Check if floor transition should complete and generate new floor
     */
    private void checkFloorTransition() {
        if (isFloorTransitioning) {
            long elapsed = System.currentTimeMillis() - floorTransitionStartTime;
            if (elapsed >= FLOOR_TRANSITION_DURATION) {
                // Transition complete - generate new floor
                isFloorTransitioning = false;
                
                // Generate new floor (like initial generation)
                advance_to_next_floor();
            }
        }
    }
    
    /**
     * MANDATORY: Handle victory achievement
     */
    private void achieve_victory() {
        if (victoryStatus) return;
        victoryStatus = true;
        gameState = GameState.VICTORY;
        // Stop floor transitioning to prevent further progression
        isFloorTransitioning = false;
        notify_observers("VICTORY_ACHIEVED", playerProgress);
        // Remove direct View/Controller access - let observers handle it
    }

    /**
     * MANDATORY: Check death condition
     */
    public void check_death_condition() {
        if (!player.is_alive()) {
            handle_player_death();
        }
    }

    /**
     * MANDATORY: Handle player death
     */
    private void handle_player_death() {
        if (deathStatus) return;
        deathStatus = true;
        gameState = GameState.GAME_OVER;
        notify_observers("PLAYER_DEATH", getLastKillerName());
        // Remove direct View/Controller access - let observers handle it
    }

    /**
     * MANDATORY: Update overall game state
     */
    public void update_game_state() {
        // Check if disposed before proceeding
        if (isDisposed) {
            return;
        }
        
        long now = System.currentTimeMillis();
        float deltaTime = (now - lastUpdateTime) / 1000f;
        lastUpdateTime = now;
        
        if (!pauseStatus && !npcDialogue) {
            // Check floor transition first
            checkFloorTransition();
            
            // Only update game entities if not transitioning
            if (!isFloorTransitioning) {
                // Thread-safe enemy position updates
                synchronized (enemyUpdateLock) {
                    if (!isDisposed) { // Double-check after acquiring lock
                        update_enemy_positions();
                    }
                }
                
                update_upgrader();
                
                // Update projectiles (Thread-safe)
                synchronized (projectileLock) {
                    if (!isDisposed) {
                        Iterator<Projectile> it = projectiles.iterator();
                        while (it.hasNext()) {
                            Projectile p = it.next();
                            p.update(deltaTime, currentMap, currentEnemies);
                            if (!p.isActive()) it.remove();
                        }
                    }
                }
                
                if (!isDisposed) {
                    check_victory_condition();
                    check_death_condition();
                    // Passive mana regen for any class with MP
                    player.passiveManaRegen();
                }
            }
            
            if (!isDisposed) {
                notify_observers("GAME_STATE_UPDATED", gameState);
            }
        }
    }

    /**
     * MANDATORY: Pause the game (Thread-safe)
     */
    public void pause_game() {
        synchronized (gameStateLock) {
            gameState = GameState.PAUSED;
            pauseStatus = true;  // CRITICAL FIX: Actually set pause status to stop updates
            // Pause all enemies and boss (removed setPaused calls)
            notify_observers("GAME_PAUSED", null);
        }
    }

    /**
     * MANDATORY: Resume the game (Thread-safe)
     */
    public void resume_game() {
        synchronized (gameStateLock) {
            gameState = GameState.PLAYING;
            pauseStatus = false;  // Resume updates when unpaused
            // Resume all enemies and boss (removed setPaused calls)
            notify_observers("GAME_RESUMED", null);
        }
    }

    /**
     * MANDATORY: Toggle pause state (Thread-safe)
     */
    private void toggle_pause() {
        synchronized (gameStateLock) {
            if (pauseStatus) {
                resume_game();
            } else {
                pause_game();
            }
        }
    }

    /**
     * MANDATORY: Handle NPC dialogue
     *
     * @param npcName The NPC being talked to
     * @param dialogue The dialogue content
     */
    public void handle_npc_dialogue(String npcName, String dialogue) {
        npcDialogue = true;
        notify_observers("NPC_DIALOGUE_STARTED", npcName + ": " + dialogue);
    }

    /**
     * MANDATORY: End NPC dialogue
     */
    public void end_npc_dialogue() {
        npcDialogue = false;
        notify_observers("NPC_DIALOGUE_ENDED", null);
    }

    public void back_to_main_menu() {
        synchronized (gameStateLock) {
            gameState = enums.GameState.MAIN_MENU;
            pauseStatus = false;  // CRITICAL FIX: Reset pause state when returning to menu
            // Reset all game state for new game
            currentFloor = 1;
            regularFloorCount = 1;
            currentFloorType = model.map.Map.FloorType.REGULAR;
            bonusFloorChance = 20;
            bonusFloorTriggered = false;
            synchronized (enemyLock) {
                currentEnemies.clear();
            }
            currentBoss = null;
            currentUpgrader = null;
            upgraderGreetedPlayer = false;
            upgraderWarnedPlayer = false;
            projectiles.clear();
            isFloorTransitioning = false;
            victoryStatus = false;
            deathStatus = false;
            playerProgress = 0;
            npcDialogue = false;
            // Reset player state
            if (player != null) {
                player.reset_for_new_game();
            }
            notify_observers("GAME_STATE_CHANGED", enums.GameState.MAIN_MENU);
        }
    }

    /**
     * Notify observers when an item is collected by the player
     */
    public void notify_item_collected(model.items.Item item) {
        pendingItemNotifications.add(item);
    }

    // MANDATORY: Observer pattern implementation
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

    // MANDATORY: Getters (Thread-safe)
    public GameState get_game_state() { 
        synchronized (gameStateLock) {
            return gameState; 
        }
    }
    public boolean is_victory() { 
        synchronized (gameStateLock) {
            return victoryStatus; 
        }
    }
    public boolean is_death() { 
        synchronized (gameStateLock) {
            return deathStatus; 
        }
    }
    public int get_player_progress() { 
        synchronized (gameStateLock) {
            return playerProgress; 
        }
    }
    public boolean is_paused() { 
        synchronized (gameStateLock) {
            return pauseStatus; 
        }
    }
    public boolean is_npc_dialogue_active() { 
        synchronized (gameStateLock) {
            return npcDialogue; 
        }
    }
    public Player get_player() { return player; }
    public Map get_current_map() { return currentMap; }
    public List<Enemy> get_current_enemies() { 
        synchronized (enemyLock) {
            return new ArrayList<>(currentEnemies); 
        }
    }
    public Boss get_current_boss() { return currentBoss; }
    public Upgrader get_current_upgrader() { return currentUpgrader; }
    public int get_current_floor() { return currentFloor; }

    // Removed attackFanTiles, projectileStart, projectileEnd, lastAttackDX, lastAttackDY, lastAttackRange, lastAttackAngle
    public List<Projectile> getProjectiles() { return projectiles; }
    
    /**
     * Handle enemy melee attack
     * @param enemy The enemy performing the attack
     * @param aimDX X direction of aim
     * @param aimDY Y direction of aim
     */
    public void handle_enemy_melee_attack(Enemy enemy, int aimDX, int aimDY) {
        if (enemy == null || !enemy.is_alive()) return;
        
        BaseClass enemyClass = enemy.getEnemyClassOOP();
        if (enemyClass == null || !enemyClass.hasMelee()) return;
        
        // Calculate attack position and angle
        float enemyX = enemy.getPixelX() + GameConstants.TILE_SIZE / 2f;
        float enemyY = enemy.getPixelY() + GameConstants.TILE_SIZE / 2f;
        double attackAngle = Math.atan2(aimDY, aimDX);
        
        // Use unified swing attack system
        long now = System.currentTimeMillis();
        AttackVisualData enemySwingData = AttackUtils.createSwingAttackData(
            aimDX, aimDY, enemyClass.getRange(), attackAngle, enemyClass, now
        );
        
        // Start enemy swing attack detection using unified system
        AttackUtils.startSwingAttackDetection(enemySwingData, 
            new AttackUtils.EnemySwingHitDetector(enemy, player, this));
        
        // Show attack visual with swing data (this will be handled by the observer pattern)
        notify_observers("ENEMY_SWING_ATTACK", new Object[]{enemy, enemySwingData});
    }
    
    /**
     * Handle enemy projectile attack
     * @param enemy The enemy performing the attack
     * @param aimDX X direction of aim (float for precise aiming)
     * @param aimDY Y direction of aim (float for precise aiming)
     */
    public void handle_enemy_projectile_attack(Enemy enemy, float aimDX, float aimDY) {
        if (enemy == null || !enemy.is_alive()) return;
        
        BaseClass enemyClass = enemy.getEnemyClassOOP();
        if (enemyClass == null || !enemyClass.hasProjectile()) return;
        
        // Reset chase timer when attack is triggered (player in range)
        enemy.setChaseEndTime(System.currentTimeMillis() + 5000);
        
        // Check MP cost (ignore for Mage enemies)
        if (enemy.get_character_class() != enums.CharacterClass.MAGE && enemy.get_current_mp() < enemyClass.getBaseMp()) {
            return; // Not enough MP
        }
        
        // Create projectile using precise aiming
        float enemyX = enemy.getPixelX() + GameConstants.TILE_SIZE / 2f;
        float enemyY = enemy.getPixelY() + GameConstants.TILE_SIZE / 2f;
        
        // Use the same radius calculation as player projectiles for consistency
        float radius = enums.GameConstants.TILE_SIZE / 4f;
        Projectile projectile = new Projectile(enemyX, enemyY, aimDX, aimDY, enemyClass.getProjectileSpeed(), enemyClass.getProjectileTravelDistance(), radius, enemy);
        projectiles.add(projectile);
        
        // Use MP (ignore for Mage enemies)
        if (enemy.get_character_class() != enums.CharacterClass.MAGE) {
            enemy.use_mp(enemyClass.getBaseMp());
        }
        
        // Calculate attack angle for visual effect
        double attackAngle = Math.atan2(aimDY, aimDX);
        
        // Check if enemy is a Ranger and send bow attack notification
        if (enemyClass instanceof model.characters.RangerClass) {
            // Create Ranger bow attack visual data for enemies (static angle, no sliding)
            long now = System.currentTimeMillis();
            model.gameLogic.AttackVisualData enemyBowData = model.gameLogic.AttackUtils.createStaticBowData(
                (int)aimDX, (int)aimDY, attackAngle, now
            );
            notify_observers("ENEMY_RANGER_BOW_ATTACK", new Object[]{enemy, enemyBowData});
        } else {
            notify_observers("ENEMY_PROJECTILE_ATTACK", new Object[]{enemy, attackAngle});
        }
    }

    /**
     * Public method to handle player attack input from the view
     */
    public void handle_player_attack_input() {
        handle_attack_action();
    }

    // Helper to get the name of the last enemy that killed the player (if available)
    private String getLastKillerName() {
        if (lastAttackingEnemy != null) {
            return "Enemy:" + lastAttackingEnemy.get_character_class().toString();
        }
        return "Enemy";
    }

    // Setter for lastAttackingEnemy to allow access from Projectile class
    public void setLastAttackingEnemy(Enemy enemy) {
        this.lastAttackingEnemy = enemy;
    }
    
    /**
     * Clean up resources when GameLogic is no longer needed.
     * Prevents memory leaks by stopping timers and clearing references.
     */
    public void dispose() {
        synchronized (disposalLock) {
            if (isDisposed) {
                return; // Already disposed
            }
            
            // Mark as disposed first
            isDisposed = true;
            
            // Stop game update timer with proper synchronization
            if (gameUpdateTimer != null) {
                gameUpdateTimer.cancel();
                gameUpdateTimer.purge();
                gameUpdateTimer = null;
            }
            
            // Stop notification timer with proper synchronization
            if (notificationTimer != null) {
                notificationTimer.cancel();
                notificationTimer.purge(); 
                notificationTimer = null;
            }
            
            // Wait for any ongoing timer tasks to complete
            try {
                Thread.sleep(50); // Small delay to ensure timers stop
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // Break circular reference with player
            if (player != null) {
                player.setGameLogic(null);
                player = null;
            }
            
            // Clear collections with proper synchronization
            if (observers != null) {
                synchronized (observerLock) {
                    observers.clear();
                }
            }
            
            if (currentEnemies != null) {
                synchronized (enemyLock) {
                    currentEnemies.clear();
                }
            }
            
            if (projectiles != null) {
                synchronized (projectileLock) {
                    projectiles.clear();
                }
            }
            
            if (pendingItemNotifications != null) {
                pendingItemNotifications.clear();
            }
            
            // Clear other references
            currentMap = null;
            currentBoss = null;
            lastAttackingEnemy = null;
        }
    }

    /**
     * Get characters near the upgrader for field of view detection
     */
    private List<Character> get_characters_near_upgrader() {
        List<Character> characters = new ArrayList<>();
        
        if (currentUpgrader == null) {
            return characters;
        }
        
        Position upgraderPos = currentUpgrader.get_position();
        
        // Add enemies within field of view range
        for (Enemy enemy : currentEnemies) {
            if (enemy.is_alive()) {
                int distance = Math.abs(upgraderPos.get_x() - enemy.get_position().get_x()) + 
                             Math.abs(upgraderPos.get_y() - enemy.get_position().get_y());
                if (distance <= currentUpgrader.getFieldOfViewRange()) {
                    characters.add(enemy);
                }
            }
        }
        
        // Add boss if within field of view range
        if (currentBoss != null && currentBoss.is_alive()) {
            int distance = Math.abs(upgraderPos.get_x() - currentBoss.get_position().get_x()) + 
                         Math.abs(upgraderPos.get_y() - currentBoss.get_position().get_y());
            if (distance <= currentUpgrader.getFieldOfViewRange()) {
                characters.add(currentBoss);
            }
        }
        
        return characters;
    }

    /**
     * Handle enemy death and potential loot drops
     * 50% chance to drop either their equipped armor or weapon
     * Also grants experience to the player
     */
    public void handleEnemyDeath(Enemy enemy) {
        if (enemy == null || currentMap == null) return;
        
        // Handle boss death (remove from currentBoss reference but don't trigger floor advancement)
        if (enemy.isBoss() && enemy == currentBoss) {
            currentBoss = null;
        }
        
        // Grant experience to the player
        int expGained = calculateExperienceForEnemy(enemy);
        player.gain_experience(expGained);
        
        // Notify observers about experience gained
        String enemyType = enemy.isBoss() ? "Boss" : "Enemy";
        notify_observers("LOG_MESSAGE", "You gained " + expGained + " experience from defeating the " + enemyType + "!");
        
        // Get enemy's position
        Position enemyPos = enemy.get_position();
        if (enemyPos == null) return;
        
        // Bosses always drop Floor Key (100% chance)
        if (enemy.isBoss()) {
            Item floorKey = new model.items.KeyItem("Floor Key", "stairs");
            currentMap.place_item(floorKey, enemyPos);
            notify_observers("LOG_MESSAGE", "Boss dropped Floor Key!");
        }
        
        // 50% chance to drop equipment loot (in addition to Floor Key for bosses)
        if (random.nextInt(100) < 50) {
            // Randomly choose between armor and weapon
            boolean dropArmor = random.nextBoolean();
            model.equipment.Equipment lootItem = null;
            
            if (dropArmor && enemy.get_equipped_armor() != null) {
                // Create a copy of the enemy's armor
                model.equipment.Armor enemyArmor = enemy.get_equipped_armor();
                lootItem = new model.equipment.Armor(
                    enemyArmor.get_name(),
                    enemyArmor.get_defense_value(),
                    enemyArmor.get_atk_defense(),
                    enemyArmor.get_mp_defense(),
                    enemyArmor.get_class_type(),
                    enemyArmor.get_tier(),
                    enemyArmor.get_image_path(),
                    enemyArmor.get_equipment_type_designation()
                );
            } else if (!dropArmor && enemy.get_equipped_weapon() != null) {
                // Create a copy of the enemy's weapon
                model.equipment.Weapon enemyWeapon = enemy.get_equipped_weapon();
                lootItem = new model.equipment.Weapon(
                    enemyWeapon.get_name(),
                    enemyWeapon.get_attack_power(),
                    enemyWeapon.get_mp_power(),
                    enemyWeapon.get_class_type(),
                    enemyWeapon.get_tier(),
                    enemyWeapon.get_weapon_type(),
                    enemyWeapon.get_image_path(),
                    enemyWeapon.get_equipment_type_designation()
                );
            }
            
            // Place the loot on the map if we have an item
            if (lootItem != null) {
                currentMap.place_item(lootItem, enemyPos);
                // Add to item locations for tracking
                if (!currentMap.get_item_locations().contains(enemyPos)) {
                    // Note: We can't directly modify the item locations list since it's returned as a copy
                    // The item will still be placed on the tile and be collectible
                }
                
                // Notify observers about the loot drop
                String itemType = dropArmor ? "armor" : "weapon";
                notify_observers("LOG_MESSAGE", enemyType + " dropped " + itemType + ": " + lootItem.get_name() + "!");
            }
        }
    }
    
    /**
     * Calculate experience gained from defeating an enemy
     * Regular enemies: 50-250 exp, favoring higher numbers on higher floors
     * Bosses: 250-500 exp
     * 
     * @param enemy The enemy that was defeated
     * @return Experience points to grant
     */
    private int calculateExperienceForEnemy(Enemy enemy) {
        if (enemy.isBoss()) {
            // Boss experience: 250-500
            return 250 + random.nextInt(251); // 250 + (0 to 250) = 250-500
        } else {
            // Regular enemy experience: 50-250, favoring higher numbers on higher floors
            int baseExp = 50 + random.nextInt(201); // 50 + (0 to 200) = 50-250
            
            // Apply floor scaling: higher floors give more experience
            // Each floor increases the experience by 10-20%
            float floorMultiplier = 1.0f + (currentFloor - 1) * 0.15f; // 15% increase per floor
            
            return Math.round(baseExp * floorMultiplier);
        }
    }

    /**
     * Determine the type of floor to generate based on regular floor count and chance
     */
    private model.map.Map.FloorType determineFloorType() {
        // Check if this should be a BOSS floor (every 3 floors)
        if (regularFloorCount % 3 == 0 && regularFloorCount > 0) {
            return model.map.Map.FloorType.BOSS;
        }
        
        // Check if this should be a BONUS floor (chance-based, but not in first 3 floors)
        if (regularFloorCount > 3 && !bonusFloorTriggered && random.nextInt(100) < bonusFloorChance) {
            bonusFloorTriggered = true;
            return model.map.Map.FloorType.BONUS;
        }
        
        // Regular floor
        return model.map.Map.FloorType.REGULAR;
    }
    
    /**
     * Update bonus floor chance for next floor
     */
    private void updateBonusFloorChance() {
        if (currentFloorType == model.map.Map.FloorType.BONUS) {
            // Reset chance after bonus floor is generated
            bonusFloorChance = 20;
            bonusFloorTriggered = false;
        } else {
            // Increase chance by 5% for next floor
            bonusFloorChance += 5;
        }
    }
    
    /**
     * Get display text for current floor
     */
    public String getFloorDisplayText() {
        // Always return the regular floor count (boss and bonus floors count now)
        return String.valueOf(regularFloorCount);
    }
    
    /**
     * Get current floor type for UI display
     */
    public model.map.Map.FloorType getCurrentFloorType() {
        return currentFloorType;
    }
    
    /**
     * Updates the game logic.
     * Called by the main controller each frame.
     */
    public void update() {
        update_game_state();
    }
    
    /**
     * Starts the game.
     * Called by the main controller when transitioning to PLAYING state.
     */
    public void startGame() {
        // Game is already started when GameLogic is created
        // This method is for future use if needed
    }
    
    /**
     * Pauses the game.
     * Called by the main controller when transitioning to PAUSED state.
     */
    public void pauseGame() {
        pause_game();
    }
    
    /**
     * Resets the game.
     * Called by the main controller when returning to menu.
     */
    public void resetGame() {
        // Reset game state to initial values
        gameState = GameState.MAIN_MENU;
        victoryStatus = false;
        deathStatus = false;
        playerProgress = 0;
        pauseStatus = false;
        npcDialogue = false;
        currentFloor = 1;
        regularFloorCount = 1;
        isFloorTransitioning = false;
        currentFloorType = model.map.Map.FloorType.REGULAR;
        bonusFloorChance = 20;
        bonusFloorTriggered = false;
        
        // Clear collections
        if (currentEnemies != null) {
            currentEnemies.clear();
        }
        currentBoss = null;
        currentUpgrader = null;
        projectiles.clear();
        
        // Reset player
        if (player != null) {
            player.resetPlayer();
        }
    }
    
    /**
     * Ends the game.
     * Called by the main controller when transitioning to GAME_OVER state.
     */
    public void endGame() {
        // Game ending logic is handled in check_death_condition and achieve_victory
        // This method is for future use if needed
    }
    
    /**
     * Place items for BOSS floor (no Floor Key - bosses drop it instead)
     */
    private void place_items_boss_floor() {
        List<Position> itemPositions = currentMap.get_item_locations();
        
        // Place random items (no Floor Key - bosses drop it instead)
        for (Position pos : itemPositions) {
            Item item = create_random_item();
            currentMap.place_item(item, pos);
        }
    }
    
    /**
     * Place items for BONUS floor (only Floor Key)
     */
    private void place_items_bonus_floor() {
        List<Position> itemPositions = currentMap.get_item_locations();
        
        // Place only Floor Key
        boolean floorKeyPlaced = false;
        for (Position pos : itemPositions) {
            if (!floorKeyPlaced) {
                Item floorKey = new model.items.KeyItem("Floor Key", "stairs");
                currentMap.place_item(floorKey, pos);
                floorKeyPlaced = true;
                break; // Only place Floor Key, no other items
            }
        }
        
        // Fallback for Floor Key if no item positions
        if (!floorKeyPlaced) {
            Position randomPos = find_random_walkable_position();
            if (randomPos != null) {
                Item floorKey = new model.items.KeyItem("Floor Key", "stairs");
                currentMap.place_item(floorKey, randomPos);
            }
        }
    }
    
    /**
     * Place items for regular floor (Floor Key + normal random items)
     */
    private void place_items_regular_floor() {
        List<Position> itemPositions = currentMap.get_item_locations();
        
        // Place Floor Key first
        boolean floorKeyPlaced = false;
        for (Position pos : itemPositions) {
            if (!floorKeyPlaced) {
                Item floorKey = new model.items.KeyItem("Floor Key", "stairs");
                currentMap.place_item(floorKey, pos);
                floorKeyPlaced = true;
            } else {
                // Place random items at remaining positions
                Item item = create_random_item();
                currentMap.place_item(item, pos);
            }
        }
        
        // Fallback for Floor Key if no item positions
        if (!floorKeyPlaced) {
            Position randomPos = find_random_walkable_position();
            if (randomPos != null) {
                Item floorKey = new model.items.KeyItem("Floor Key", "stairs");
                currentMap.place_item(floorKey, randomPos);
            }
        }
    }
} 