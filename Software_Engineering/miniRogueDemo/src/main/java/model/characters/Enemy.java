package model.characters;

import enums.CharacterClass;
import utilities.Position;
import model.items.Item;
import model.items.Consumable;
import model.equipment.Weapon;
import model.equipment.Armor;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import model.map.Map;
import model.characters.Player;
import java.util.Queue;
import java.util.LinkedList;
import java.util.HashSet;
import utilities.Collision;
import model.characters.WarriorClass;
import model.characters.MageClass;
import model.characters.RogueClass;
import model.characters.RangerClass;
import model.gameLogic.GameLogic;

/**
 * Enemy character class extending Character.
 * Handles AI behavior, loot drops, and combat patterns for dungeon enemies.
 */
public class Enemy extends Character {

    // MANDATORY: Enemy-specific attributes
    private String aiPattern;
    private List<String> weaknesses;
    protected List<Item> lootTable;
    protected int experienceValue;
    private Random random;
    private int aggroRange;
    private boolean isAggressive;
    private float pixelX, pixelY;
    private int targetTileX, targetTileY;
    private static final float MOVE_SPEED = 2.5f; // pixels per update (adjust for smoothness)
    private Map map;
    private Player player;
    private boolean chasingPlayer = false;
    private long chaseEndTime = 0;
    private List<int[]> chasePath = new ArrayList<>();
    private int lastPlayerX = -1, lastPlayerY = -1;
    private BaseClass enemyClassOOP;
    private float moveSpeed;
    private int aimDX = 0, aimDY = 1; // Default aim down (for 8-directional)
    private float preciseAimDX = 0f, preciseAimDY = 1f; // Precise aiming for projectiles
    private GameLogic gameLogic; // Add GameLogic reference
    private long lastAttackTime = 0; // Track last attack time for rate limiting
    
    // Celebratory state variables
    private boolean inCelebratoryState = false;
    private long celebratoryStartTime = 0;
    private static final long CELEBRATORY_DURATION = 2000; // 2 seconds in milliseconds
    private static final long CELEBRATORY_IMMUNITY_DURATION = 500; // 0.5 seconds immunity at start
    private boolean inCelebratoryImmunity = false;
    
    // Fallback behavior variables
    private boolean inFallbackState = false;
    private long fallbackStartTime = 0;
    private static final long FALLBACK_DURATION = 1200; // 1.2 seconds fallback
    private float fallbackTargetX, fallbackTargetY; // Target position for fallback
    
    // Hit state variables
    private boolean inHitState = false;
    private long hitStateStartTime = 0;
    public static final long BASE_HIT_STATE_DURATION = 2200; // ms, slightly longer than player immunity
    private static final long IMMUNITY_EXTENSION = 800; // ms, brief extension after hit state (doubled from 400)
    private long hitImmunityEndTime = 0;
    
    // Dying state variables
    private boolean isDying = false;
    private long dyingStartTime = 0;
    private static final long DYING_DURATION = 2000; // ms
    

    
    // Wind-up state variables for attack warning
    private boolean inWindUpState = false;
    private long windUpStartTime = 0;
    private static final long WIND_UP_DURATION = 500; // 0.5 seconds wind-up
    private int windUpAimDX = 0, windUpAimDY = 1; // Store aim direction during wind-up
    private float windUpPreciseAimDX = 0f, windUpPreciseAimDY = 1f; // Store precise aim during wind-up
    
    // State management - track previous state to return to after temporary states
    private boolean wasChasingBeforeTemporaryState = false;
    
    // Detection notification state - show exclamation mark when first detecting player
    private boolean showingDetectionNotification = false;
    private long detectionNotificationStartTime = 0;
    private static final long DETECTION_NOTIFICATION_DURATION = 500; // 0.5 seconds
    
    // Flag to track if enemy is in alerted state (set to true only when entering idle/random roaming)
    private boolean alerted = true;
    
    /**
     * MANDATORY: Constructor for Enemy
     *
     * @param name Enemy's name
     * @param characterClass Enemy's combat class
     * @param position Initial spawn position
     * @param aiPattern AI behavior pattern
     */
    public Enemy(String name, CharacterClass characterClass, Position position, String aiPattern) {
        super(name, characterClass, position);
        this.aiPattern = aiPattern;
        this.weaknesses = new ArrayList<>();
        this.lootTable = new ArrayList<>();
        this.random = new Random();
        this.aggroRange = 3;
        this.isAggressive = true;
        // Assign a random OOP class and copy stats
        switch (characterClass) {
            case WARRIOR:
                this.enemyClassOOP = new WarriorClass();
                break;
            case MAGE:
                this.enemyClassOOP = new MageClass();
                break;
            case ROGUE:
                this.enemyClassOOP = new RogueClass();
                break;
            case RANGER:
                this.enemyClassOOP = new RangerClass();
                break;
            default:
                this.enemyClassOOP = new WarriorClass();
        }
        // Copy stats from OOP class
        this.maxHp = enemyClassOOP.getBaseHp();
        this.currentHp = maxHp;
        this.baseAtk = enemyClassOOP.getBaseAtk();
        this.maxMp = enemyClassOOP.getBaseMp();
        this.currentMp = maxMp;
        this.moveSpeed = enemyClassOOP.getMoveSpeed() * 0.5f; // 50% slower than class base
        // Apply scaling modifiers
        this.maxHp = (int)(this.maxHp * 1.2);
        this.currentHp = maxHp;
        this.baseAtk = (int)(this.baseAtk * 0.8);
        // Special case: Mage enemies have infinite mana
        if (characterClass == CharacterClass.MAGE) {
            this.maxMp = Integer.MAX_VALUE;
            this.currentMp = Integer.MAX_VALUE;
        } else {
            this.maxMp = 0;
            this.currentMp = 0;
        }
        initialize_enemy_equipment();
        initialize_loot_table();
        initialize_weaknesses();
        this.experienceValue = calculate_experience_value();
        this.pixelX = position.get_x() * enums.GameConstants.TILE_SIZE;
        this.pixelY = position.get_y() * enums.GameConstants.TILE_SIZE;
        this.targetTileX = position.get_x();
        this.targetTileY = position.get_y();
    }

    // Make update_movement() public so it can be called from the main game loop
    public void update_movement() {
        updateImmunity();
        
        // Dying: cannot move, aim, or attack
        if (isDying) return;
        
        // Process pushback first
        updatePushback(map);
        if (isBeingPushed()) {
            // Skip normal movement while being pushed
            return;
        }
        
        // Handle temporary states that should return to previous state
        if (isInHitState()) {
            // Hit state ends automatically, return to previous state
            return;
        }
        
        if (isInFallbackState()) {
            handleFallbackMovement();
            // Fallback state ends automatically, return to previous state
            return;
        }
        
        if (isInCelebratoryState()) {
            // Celebratory state ends automatically, return to previous state
            return;
        }
        
        // If showing detection notification, don't move or attack
        if (isShowingDetectionNotification()) {
            return;
        }
        
        // Check if we should chase the player
        int playerX = player != null ? player.get_position().get_x() : -1;
        int playerY = player != null ? player.get_position().get_y() : -1;
        
        // Simple detection: if not chasing and player is in range, start chase directly
        if (!chasingPlayer && !inHitState && !inFallbackState && !inCelebratoryState) {
            // Check if player is in aggro range AND LOS AND player is not invisible
            if (player != null && position.manhattan_distance_to(player.get_position()) <= aggroRange && !player.is_invisibility_effect_active()) {
                int enemyTileX = position.get_x();
                int enemyTileY = position.get_y();
                int playerTileX = player.get_position().get_x();
                int playerTileY = player.get_position().get_y();
                if (utilities.Collision.hasLineOfSight(map, enemyTileX, enemyTileY, playerTileX, playerTileY)) {
                    // Start chase state directly
                    chasingPlayer = true;
                    chaseEndTime = System.currentTimeMillis() + 3000; // chase for 3 seconds
                    
                    // Show detection notification, lockdown, and send chase message only if alerted is true
                    if (alerted) {
                        // Send chase notification based on player health
                        if (gameLogic != null && player != null) {
                            int playerHealthPercent = (player.get_current_hp() * 100) / player.get_max_hp();
                            String enemyClass = get_character_class().toString().toLowerCase();
                            
                            if (playerHealthPercent <= 25) {
                                gameLogic.notify_observers("LOG_MESSAGE", "An enemy " + enemyClass + " has spotted you, run for your life!");
                            } else {
                                gameLogic.notify_observers("LOG_MESSAGE", "An enemy " + enemyClass + " has spotted you, time to fight!");
                            }
                        }
                        
                        showingDetectionNotification = true;
                        detectionNotificationStartTime = System.currentTimeMillis();
                        alerted = false; // Consume the flag, enemy is no longer alerted until idle resumes
                    }
                }
            }
        }
        if (chasingPlayer) {
            // Check if player is still within aggro range and not invisible
            boolean playerInRange = false;
            boolean playerInvisible = false;
            if (player != null) {
                int distanceToPlayer = position.manhattan_distance_to(player.get_position());
                playerInRange = distanceToPlayer <= aggroRange;
                playerInvisible = player.is_invisibility_effect_active();
            }
            
            // If player becomes invisible, stop chasing immediately
            if (playerInvisible) {
                chasingPlayer = false;
                chasePath.clear();
                showingDetectionNotification = false;
                return;
            }
            
            // If chase time expired, check if we should extend it
            if (System.currentTimeMillis() > chaseEndTime) {
                if (playerInRange) {
                    // Player is still in range, extend chase timer
                    chaseEndTime = System.currentTimeMillis() + 3000; // Extend by 3 more seconds
                } else {
                    // Player is out of range, stop chasing
                    chasingPlayer = false;
                    chasePath.clear();
                    // Reset detection notification
                    showingDetectionNotification = false;
                    // Do NOT set alerted to true here; only set it when idle/random roaming resumes
                }
            } else {
                // Check if we're already in attack range - if so, just aim but keep moving
                float px = pixelX + enums.GameConstants.TILE_SIZE / 2f;
                float py = pixelY + enums.GameConstants.TILE_SIZE / 2f;
                float tx = player.getPixelX() + enums.GameConstants.TILE_SIZE / 2f;
                float ty = player.getPixelY() + enums.GameConstants.TILE_SIZE / 2f;
                float dist = (float)Math.hypot(tx - px, ty - py) / enums.GameConstants.TILE_SIZE;
                
                boolean hasProjectile = enemyClassOOP.hasProjectile();
                boolean hasMelee = enemyClassOOP.hasMelee();
                float projRange = hasProjectile ? enemyClassOOP.getProjectileTravelDistance() : 0f;
                float meleeRange = hasMelee ? enemyClassOOP.getRange() : 0f;
                float attackRange = Math.max(projRange, meleeRange);
                
                // If we're within attack range AND have line of sight, aim at player but keep moving
                boolean inAttackRange = (dist <= attackRange);
                int enemyTileX = (int)(pixelX / enums.GameConstants.TILE_SIZE);
                int enemyTileY = (int)(pixelY / enums.GameConstants.TILE_SIZE);
                int playerTileX = (int)(player.getPixelX() / enums.GameConstants.TILE_SIZE);
                int playerTileY = (int)(player.getPixelY() / enums.GameConstants.TILE_SIZE);
                boolean hasLOS = utilities.Collision.hasLineOfSight(map, enemyTileX, enemyTileY, playerTileX, playerTileY);
                if (inAttackRange && hasLOS) {
                    // Aim at the player for attacks but don't stop movement
                    float aimDx = tx - px;
                    float aimDy = ty - py;
                    if (aimDx != 0 || aimDy != 0) {
                        // Normalize for 8-directional aiming
                        if (Math.abs(aimDx) > Math.abs(aimDy)) {
                            aimDX = aimDx > 0 ? 1 : -1;
                            aimDY = 0;
                        } else {
                            aimDX = 0;
                            aimDY = aimDy > 0 ? 1 : -1;
                        }
                        // Set precise aiming for projectiles
                        float len = (float)Math.sqrt(aimDx*aimDx + aimDy*aimDy);
                        preciseAimDX = aimDx / len;
                        preciseAimDY = aimDy / len;
                    }
                }
                
                // Continue pathfinding regardless of attack range
                if (chasePath.isEmpty() || playerX != lastPlayerX || playerY != lastPlayerY) {
                    List<int[]> path = getPathToPlayer();
                    if (path != null && !path.isEmpty()) {
                        chasePath = path;
                        lastPlayerX = playerX;
                        lastPlayerY = playerY;
                    } else {
                        // No path to player, stop chasing
                        chasingPlayer = false;
                        chasePath.clear();
                    }
                }
                // Follow the path step by step
                if (!chasePath.isEmpty() && Math.abs(pixelX - targetTileX * enums.GameConstants.TILE_SIZE) < 0.01f && Math.abs(pixelY - targetTileY * enums.GameConstants.TILE_SIZE) < 0.01f) {
                    int[] next = chasePath.remove(0);
                    if (utilities.Collision.isWalkable(map, next[0], next[1])) {
                        targetTileX = next[0];
                        targetTileY = next[1];
                    } else {
                        // Path blocked, recalculate next update
                        chasePath.clear();
                    }
                }
            }
        } else {
            // Enemy is not chasing, so is idle/random roaming
            if (!alerted) {
                alerted = true; // Only set alerted to true when returning to idle/random roaming
            }
        }
        // If not chasing, random roam with diagonal movement
        if (!chasingPlayer) {
            if ((int)(pixelX / enums.GameConstants.TILE_SIZE) == targetTileX &&
                (int)(pixelY / enums.GameConstants.TILE_SIZE) == targetTileY) {
                // Include diagonal directions for more varied movement
                int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1},{1,1},{1,-1},{-1,1},{-1,-1}};
                for (int attempt = 0; attempt < dirs.length; attempt++) {
                    int[] dir = dirs[random.nextInt(dirs.length)];
                    int nextX = targetTileX + dir[0];
                    int nextY = targetTileY + dir[1];
                    if (utilities.Collision.isWalkable(map, nextX, nextY)) {
                        targetTileX = nextX;
                        targetTileY = nextY;
                        // Set aim direction to match movement
                        aimDX = dir[0];
                        aimDY = dir[1];
                        break;
                    }
                }
            }
        }
        // Move pixelX/pixelY toward target tile using class moveSpeed
        float targetPixelX = targetTileX * enums.GameConstants.TILE_SIZE;
        float targetPixelY = targetTileY * enums.GameConstants.TILE_SIZE;
        float dx = targetPixelX - pixelX;
        float dy = targetPixelY - pixelY;
        float dist = (float)Math.sqrt(dx*dx + dy*dy);
        float speed = moveSpeed > 0 ? moveSpeed : 2.5f;
        // Apply 10% speed buff if chasing
        if (chasingPlayer) {
            speed *= 1.1f;
        }
        if (dist > speed) {
            float ndx = dx / dist;
            float ndy = dy / dist;
            pixelX += speed * ndx;
            pixelY += speed * ndy;
        } else {
            pixelX = targetPixelX;
            pixelY = targetPixelY;
            this.position = new utilities.Position(targetTileX, targetTileY);
        }
        // If chasing, set aim direction toward player with precise calculation
        if (chasingPlayer && player != null) {
            float px = pixelX + enums.GameConstants.TILE_SIZE / 2f;
            float py = pixelY + enums.GameConstants.TILE_SIZE / 2f;
            float tx = player.getPixelX() + enums.GameConstants.TILE_SIZE / 2f;
            float ty = player.getPixelY() + enums.GameConstants.TILE_SIZE / 2f;
            float adx = tx - px;
            float ady = ty - py;
            
            // Calculate precise angle for projectiles, 8-directional for melee
            double angle = Math.atan2(ady, adx);
            double angleDegrees = Math.toDegrees(angle);
            if (angleDegrees < 0) angleDegrees += 360;
            
            // For projectiles, use precise aiming; for melee, use 8-directional
            boolean hasProjectile = enemyClassOOP.hasProjectile();
            boolean hasMelee = enemyClassOOP.hasMelee();
            
            // Always calculate precise aiming for projectiles
            double length = Math.sqrt(adx * adx + ady * ady);
            if (length > 0) {
                preciseAimDX = (float)(adx / length);
                preciseAimDY = (float)(ady / length);
            } else {
                preciseAimDX = 0f;
                preciseAimDY = 1f; // Default to down if no direction
            }
            
            if (hasProjectile && !hasMelee) {
                // Projectile-only enemies: use precise aiming for display
                aimDX = (int)Math.signum(preciseAimDX);
                aimDY = (int)Math.signum(preciseAimDY);
            } else {
                // Melee enemies or mixed: use 8-directional aiming
                if (angleDegrees >= 337.5 || angleDegrees < 22.5) {
                    aimDX = 1; aimDY = 0; // Right
                } else if (angleDegrees >= 22.5 && angleDegrees < 67.5) {
                    aimDX = 1; aimDY = 1; // Down-right
                } else if (angleDegrees >= 67.5 && angleDegrees < 112.5) {
                    aimDX = 0; aimDY = 1; // Down
                } else if (angleDegrees >= 112.5 && angleDegrees < 157.5) {
                    aimDX = -1; aimDY = 1; // Down-left
                } else if (angleDegrees >= 157.5 && angleDegrees < 202.5) {
                    aimDX = -1; aimDY = 0; // Left
                } else if (angleDegrees >= 202.5 && angleDegrees < 247.5) {
                    aimDX = -1; aimDY = -1; // Up-left
                } else if (angleDegrees >= 247.5 && angleDegrees < 292.5) {
                    aimDX = 0; aimDY = -1; // Up
                } else {
                    aimDX = 1; aimDY = -1; // Up-right
                }
            }
        }
    }

    /**
     * MANDATORY: Scale enemy statistics for challenge balance
     */
    private void scale_enemy_stats() {
        // Enemies have slightly higher HP but lower attack than player equivalents
        this.maxHp = (int)(maxHp * 1.2);
        this.currentHp = maxHp;
        this.baseAtk = (int)(baseAtk * 0.8);
        this.maxMp = (int)(maxMp * 0.9);
        this.currentMp = maxMp;
    }

    /**
     * MANDATORY: Initialize enemy equipment based on class
     */
    private void initialize_enemy_equipment() {
        // Determine tier based on floor level (1-3)
        int tier = Math.min(3, Math.max(1, (int)(Math.random() * 3) + 1));
        
        // Get enemy equipment from JSON configuration
        utilities.WeaponDefinitionManager weaponManager = utilities.WeaponDefinitionManager.getInstance();
        model.equipment.Weapon randomWeapon = weaponManager.getEnemyWeapon(characterClass);
        model.equipment.Armor randomArmor = weaponManager.getEnemyArmor(characterClass);
        
        // Apply tier scaling if equipment was found
        if (randomWeapon != null) {
            // Create new weapon instance with the desired tier
            randomWeapon = new model.equipment.Weapon(
                randomWeapon.get_name(),
                randomWeapon.get_attack_power(),
                randomWeapon.get_mp_power(),
                characterClass,
                tier,
                randomWeapon.get_weapon_type(),
                randomWeapon.get_image_path(),
                randomWeapon.get_equipment_type_designation()
            );
        }
        
        if (randomArmor != null) {
            // Create new armor instance with the desired tier
            randomArmor = new model.equipment.Armor(
                randomArmor.get_name(),
                randomArmor.get_defense_value(),
                randomArmor.get_atk_defense(),
                randomArmor.get_mp_defense(),
                characterClass,
                tier,
                randomArmor.get_image_path(),
                randomArmor.get_equipment_type_designation()
            );
        }
        
        // Set the equipment
        this.equippedWeapon = randomWeapon;
        this.equippedArmor = randomArmor;
    }
    
    /**
     * Fallback method to create hardcoded equipment if random selection fails
     */
    private void createFallbackEquipment(int tier) {
        switch (characterClass) {
            case WARRIOR:
                this.equippedWeapon = new Weapon("Rusty Sword", 10, 0, characterClass, tier, Weapon.WeaponType.IMPACT, "images/weapons/Impact/hand_axe_2_old.png", "Impact");
                this.equippedArmor = new Armor("Chain Mail", 0, 6, 1, characterClass, tier, "images/armor/Guard_Chainmail.png", "Universal");
                break;
            case MAGE:
                this.equippedWeapon = new Weapon("Dark Staff", 6, 8, characterClass, tier, Weapon.WeaponType.MAGIC, "images/weapons/Magic/staff_3.png", "Magic");
                this.equippedArmor = new Armor("Dark Robes", 0, 2, 6, characterClass, tier, "images/armor/Wizard_Robes.png", "Universal");
                break;
            case ROGUE:
                this.equippedWeapon = new Weapon("Curved Blade", 9, 3, characterClass, tier, Weapon.WeaponType.BLADE, "images/weapons/Blade/Scimitar.png", "Blade");
                this.equippedArmor = new Armor("Shadow Cloak", 0, 3, 2, characterClass, tier, "images/armor/Vine_Cape.png", "Universal");
                break;
            case RANGER:
                this.equippedWeapon = new Weapon("Hunter's Bow", 7, 8, characterClass, tier, Weapon.WeaponType.DISTANCE, "images/weapons/Distance/Hunter_Bow.png", "Distance");
                this.equippedArmor = new Armor("Leather Jerkin", 0, 2, 6, characterClass, tier, "images/armor/Basic_Leather.png", "Universal");
                break;
        }
    }

    /**
     * MANDATORY: Initialize loot drop table
     */
    private void initialize_loot_table() {
        // Common drops
        lootTable.add(new Consumable("Health Potion", 25, "health"));
        lootTable.add(new Consumable("Mana Potion", 15, "mana"));
        // Class-specific equipment drops (rare)
        switch (characterClass) {
            case WARRIOR:
                if (random.nextInt(100) < 20) {
                    lootTable.add(new Weapon("Battle Axe", 20, 0, characterClass, 1, Weapon.WeaponType.IMPACT, "images/weapons/Impact/executioner_axe_4.png", "Impact"));
                }
                break;
            case MAGE:
                if (random.nextInt(100) < 20) {
                    lootTable.add(new Weapon("Crystal Staff", 10, 15, characterClass, 1, Weapon.WeaponType.MAGIC, "images/weapons/Magic/urand_brilliance.png", "Magic"));
                }
                break;
            case ROGUE:
                if (random.nextInt(100) < 20) {
                    lootTable.add(new Weapon("Shadow Blade", 15, 8, characterClass, 1, Weapon.WeaponType.BLADE, "images/weapons/Blade/Void_Blade.png", "Blade"));
                }
                break;
            case RANGER:
                if (random.nextInt(100) < 20) {
                    lootTable.add(new Weapon("Longbow", 10, 15, characterClass, 1, Weapon.WeaponType.DISTANCE, "images/weapons/Distance/Longbow.png", "Distance"));
                }
                break;
        }
    }

    /**
     * MANDATORY: Initialize character class weaknesses
     */
    private void initialize_weaknesses() {
        switch (characterClass) {
            case WARRIOR:
                weaknesses.add("MAGIC");
                weaknesses.add("POISON");
                break;
            case MAGE:
                weaknesses.add("PHYSICAL");
                weaknesses.add("SILENCE");
                break;
            case ROGUE:
                weaknesses.add("AREA_ATTACK");
                weaknesses.add("LIGHT");
                break;
            case RANGER:
                weaknesses.add("PHYSICAL");
                weaknesses.add("SILENCE");
                break;
        }
    }

    /**
     * MANDATORY: Calculate experience value based on enemy strength
     *
     * @return Experience points awarded for defeating this enemy
     */
    private int calculate_experience_value() {
        int baseExp = 25;
        int hpBonus = maxHp / 10;
        int atkBonus = (int)(baseAtk * 2);
        return baseExp + hpBonus + atkBonus;
    }

    /**
     * MANDATORY: Perform AI behavior based on pattern
     */
    public void perform_ai() {
        // This method is no longer needed as attack logic is integrated into update_movement
    }

    public void perform_melee_attack() {
        // Simulate a melee attack in the current aim direction
        // This should trigger the same hitbox logic as the player
        if (isInHitState()) return;
        if (gameLogic != null) {
            // Call GameLogic to handle enemy melee attack
            gameLogic.handle_enemy_melee_attack(this, aimDX, aimDY);
        }
    }
    public void perform_projectile_attack() {
        // Simulate a projectile attack using precise aiming
        // For Mage enemies, ignore MP cost
        if (isInHitState()) return;
        if (gameLogic != null) {
            // Call GameLogic to handle enemy projectile attack with precise aiming
            gameLogic.handle_enemy_projectile_attack(this, preciseAimDX, preciseAimDY);
        }
    }

    /**
     * MANDATORY: Drop loot when defeated
     *
     * @return List of items dropped
     */
    public List<Item> drop_loot() {
        List<Item> droppedItems = new ArrayList<>();
        // Random chance for each item in loot table
        for (Item item : lootTable) {
            if (random.nextInt(100) < 30) { // 30% drop chance
                droppedItems.add(item);
            }
        }
        // Always drop at least one item
        if (droppedItems.isEmpty() && !lootTable.isEmpty()) {
            droppedItems.add(lootTable.get(random.nextInt(lootTable.size())));
        }
        return droppedItems;
    }

    /**
     * MANDATORY: Check if enemy has specific weakness
     *
     * @param weakness The weakness type to check
     * @return true if enemy is weak to this type
     */
    public boolean is_weak_to(String weakness) {
        return weaknesses.contains(weakness.toUpperCase());
    }

    /**
     * MANDATORY: Implement attack behavior for enemy
     *
     * @param target The character to attack
     * @return Damage dealt
     */
    @Override
    public int attack(Character target) {
        int damage = get_total_attack();
        // Small chance to miss
        if (random.nextInt(100) < enums.GameConstants.MISS_CHANCE) {
            damage = 0;
            notify_observers("ATTACK_MISSED", target);
        }
        notify_observers("ENEMY_ATTACKED", target);
        return damage;
    }



    // MANDATORY: Getters
    public String get_ai_pattern() { return aiPattern; }
    public List<String> get_weaknesses() { return new ArrayList<>(weaknesses); }
    public int get_experience_value() { return experienceValue; }
    public int get_aggro_range() { return aggroRange; }
    public boolean is_aggressive() { return isAggressive; }
    public float getPixelX() { return this.pixelX; }
    public float getPixelY() { return this.pixelY; }
    public int getAimDX() { return aimDX; }
    public int getAimDY() { return aimDY; }
    public BaseClass getEnemyClassOOP() { return enemyClassOOP; }

    public void setMap(Map map) {
        this.map = map;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<int[]> getChasePath() {
        return new ArrayList<>(chasePath);
    }

    public void setGameLogic(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }
    
    public GameLogic getGameLogic() {
        return gameLogic;
    }
    
    public long getLastAttackTime() {
        return lastAttackTime;
    }
    
    public void setLastAttackTime(long time) {
        this.lastAttackTime = time;
    }
    
    // Celebratory state methods
    public void startCelebratoryState() {
        // Record previous state before entering celebratory state
        this.wasChasingBeforeTemporaryState = chasingPlayer;
        
        this.inCelebratoryState = true;
        this.celebratoryStartTime = System.currentTimeMillis();
        this.inCelebratoryImmunity = true;
        this.isImmune = true;
        this.immunityEndTime = System.currentTimeMillis() + CELEBRATORY_IMMUNITY_DURATION;
        // Reset/pause chase timer so enemy resumes full chase after celebration
        this.chaseEndTime = System.currentTimeMillis() + 3000;
    }
    
    public boolean isInCelebratoryState() {
        if (inCelebratoryState) {
            // Check if celebratory state should end
            if (System.currentTimeMillis() - celebratoryStartTime >= CELEBRATORY_DURATION) {
                inCelebratoryState = false;
                inCelebratoryImmunity = false;
                // Return to previous state (usually chase)
                if (wasChasingBeforeTemporaryState) {
                    chasingPlayer = true;
                    chaseEndTime = System.currentTimeMillis() + 3000; // Reset chase timer
                }
                return false;
            }
            // Check if immunity period should end
            if (inCelebratoryImmunity && System.currentTimeMillis() - celebratoryStartTime >= CELEBRATORY_IMMUNITY_DURATION) {
                inCelebratoryImmunity = false;
                this.isImmune = false;
            }
            return true;
        }
        return false;
    }
    
    public void forceExitCelebratoryState() {
        this.inCelebratoryState = false;
        this.inCelebratoryImmunity = false;
        this.isImmune = false;
    }

    public boolean isInCelebratoryImmunity() {
        return inCelebratoryImmunity;
    }
    

    
    public boolean isInWindUpState() {
        return inWindUpState;
    }
    
    public boolean isShowingDetectionNotification() {
        if (showingDetectionNotification) {
            if (System.currentTimeMillis() - detectionNotificationStartTime >= DETECTION_NOTIFICATION_DURATION) {
                showingDetectionNotification = false;
                return false;
            }
            return true;
        }
        return false;
    }
    
    public void setInWindUpState(boolean windUpState) {
        this.inWindUpState = windUpState;
    }
    
    public long getWindUpStartTime() {
        return windUpStartTime;
    }
    
    public void setWindUpStartTime(long windUpStartTime) {
        this.windUpStartTime = windUpStartTime;
    }
    
    public long getWindUpDuration() {
        return WIND_UP_DURATION;
    }
    
    public int getWindUpAimDX() {
        return windUpAimDX;
    }
    
    public int getWindUpAimDY() {
        return windUpAimDY;
    }
    
    public float getWindUpPreciseAimDX() {
        return windUpPreciseAimDX;
    }
    
    public float getWindUpPreciseAimDY() {
        return windUpPreciseAimDY;
    }
    
    public void setWindUpAimDirection(int aimDX, int aimDY, float preciseAimDX, float preciseAimDY) {
        this.windUpAimDX = aimDX;
        this.windUpAimDY = aimDY;
        this.windUpPreciseAimDX = preciseAimDX;
        this.windUpPreciseAimDY = preciseAimDY;
    }
    
    public float getPreciseAimDX() {
        return preciseAimDX;
    }
    
    public float getPreciseAimDY() {
        return preciseAimDY;
    }

    public boolean isInFallbackState() {
        if (inFallbackState) {
            if (System.currentTimeMillis() - fallbackStartTime >= FALLBACK_DURATION) {
                inFallbackState = false;
                // Return to previous state (usually chase)
                if (wasChasingBeforeTemporaryState) {
                    chasingPlayer = true;
                    chaseEndTime = System.currentTimeMillis() + 3000; // Reset chase timer
                }
                return false;
            }
            return true;
        }
        return false;
    }

    public void startFallbackState() {
        // Record previous state before entering fallback state
        this.wasChasingBeforeTemporaryState = chasingPlayer;
        
        this.inFallbackState = true;
        this.fallbackStartTime = System.currentTimeMillis();
        
        // Calculate fallback position (move away from player)
        if (player != null) {
            float px = pixelX + enums.GameConstants.TILE_SIZE / 2f;
            float py = pixelY + enums.GameConstants.TILE_SIZE / 2f;
            float tx = player.getPixelX() + enums.GameConstants.TILE_SIZE / 2f;
            float ty = player.getPixelY() + enums.GameConstants.TILE_SIZE / 2f;
            
            // Calculate direction away from player
            float dx = px - tx;
            float dy = py - ty;
            float dist = (float)Math.sqrt(dx*dx + dy*dy);
            
            if (dist > 0) {
                // Normalize and set fallback target (1.5 tiles away)
                float fallbackDist = 1.5f * enums.GameConstants.TILE_SIZE;
                this.fallbackTargetX = px + (dx / dist) * fallbackDist;
                this.fallbackTargetY = py + (dy / dist) * fallbackDist;
            } else {
                // If player is on top of enemy, fallback in a random direction
                this.fallbackTargetX = px + (random.nextFloat() - 0.5f) * enums.GameConstants.TILE_SIZE;
                this.fallbackTargetY = py + (random.nextFloat() - 0.5f) * enums.GameConstants.TILE_SIZE;
            }
        } else {
            // No player reference, fallback in random direction
            this.fallbackTargetX = pixelX + (random.nextFloat() - 0.5f) * enums.GameConstants.TILE_SIZE;
            this.fallbackTargetY = pixelY + (random.nextFloat() - 0.5f) * enums.GameConstants.TILE_SIZE;
        }
    }

    private void handleFallbackMovement() {
        // Move towards fallback target
        float dx = fallbackTargetX - pixelX;
        float dy = fallbackTargetY - pixelY;
        float dist = (float)Math.sqrt(dx*dx + dy*dy);
        float speed = moveSpeed > 0 ? moveSpeed * 0.8f : 2.0f; // Slightly slower during fallback
        
        if (dist > speed) {
            // Check if the next position would hit a wall
            float nextX = pixelX + (dx / dist) * speed;
            float nextY = pixelY + (dy / dist) * speed;
            
            // Convert to tile coordinates and check if walkable
            int nextTileX = (int)(nextX / enums.GameConstants.TILE_SIZE);
            int nextTileY = (int)(nextY / enums.GameConstants.TILE_SIZE);
            
            if (map != null && utilities.Collision.isWalkable(map, nextTileX, nextTileY)) {
                pixelX = nextX;
                pixelY = nextY;
            } else {
                // Hit a wall, stop fallback movement
                inFallbackState = false;
                return;
            }
        } else {
            // Check final position
            int finalTileX = (int)(fallbackTargetX / enums.GameConstants.TILE_SIZE);
            int finalTileY = (int)(fallbackTargetY / enums.GameConstants.TILE_SIZE);
            
            if (map != null && utilities.Collision.isWalkable(map, finalTileX, finalTileY)) {
                pixelX = fallbackTargetX;
                pixelY = fallbackTargetY;
            } else {
                // Final position is blocked, stop fallback
                inFallbackState = false;
                return;
            }
        }
        
        // Update position
        this.position = new utilities.Position(
            (int)(pixelX / enums.GameConstants.TILE_SIZE),
            (int)(pixelY / enums.GameConstants.TILE_SIZE)
        );
    }

    public boolean isChasingPlayer() {
        return chasingPlayer;
    }

    // Add chaseEndTime setter/getter
    public void setChaseEndTime(long time) { this.chaseEndTime = time; }
    public long getChaseEndTime() { return this.chaseEndTime; }

    public Random getRandom() { return random; }

    public void setPixelX(float x) { this.pixelX = x; }
    public void setPixelY(float y) { this.pixelY = y; }

    // Returns the full path (list of [x, y]) from enemy to player using BFS, or null if no path
    public List<int[]> getPathToPlayer() {
        if (map == null || player == null) return null;
        int width = map.get_width();
        int height = map.get_height();
        boolean[][] visited = new boolean[width][height];
        Queue<int[]> queue = new LinkedList<>();
        int[][] prev = new int[width * height][2];
        for (int i = 0; i < prev.length; i++) prev[i][0] = prev[i][1] = -1;
        int startX = targetTileX;
        int startY = targetTileY;
        int goalX = player.get_position().get_x();
        int goalY = player.get_position().get_y();
        queue.add(new int[]{startX, startY});
        visited[startX][startY] = true;
        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
        boolean found = false;
        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int cx = curr[0], cy = curr[1];
            if (cx == goalX && cy == goalY) {
                found = true;
                break;
            }
            for (int[] dir : dirs) {
                int nx = cx + dir[0];
                int ny = cy + dir[1];
                if (nx >= 0 && ny >= 0 && nx < width && ny < height && !visited[nx][ny]) {
                    if (utilities.Collision.isWalkable(map, nx, ny)) {
                        queue.add(new int[]{nx, ny});
                        visited[nx][ny] = true;
                        prev[nx + ny * width][0] = cx;
                        prev[nx + ny * width][1] = cy;
                    }
                }
            }
        }
        if (!found) return null;
        // Backtrack from goal to start to build the path
        List<int[]> path = new ArrayList<>();
        int cx = goalX, cy = goalY;
        while (!(cx == startX && cy == startY)) {
            path.add(0, new int[]{cx, cy});
            int px = prev[cx + cy * width][0];
            int py = prev[cx + cy * width][1];
            cx = px; cy = py;
            if (cx == -1 || cy == -1) return null;
        }
        return path;
    }

    // Call this when enemy is hit (instead of setImmune directly)
    public void triggerHitState(long baseImmunityDuration) {
        // Record previous state before entering hit state
        this.wasChasingBeforeTemporaryState = chasingPlayer;
        
        this.inHitState = true;
        this.hitStateStartTime = System.currentTimeMillis();
        this.hitImmunityEndTime = hitStateStartTime + baseImmunityDuration + IMMUNITY_EXTENSION;
        this.isImmune = true;
        this.immunityEndTime = hitStateStartTime + baseImmunityDuration;
    }

    public boolean isInHitState() {
        if (inHitState) {
            if (System.currentTimeMillis() - hitStateStartTime >= BASE_HIT_STATE_DURATION) {
                inHitState = false;
                // Always trigger chase state after hit state ends, regardless of previous state
                // This allows enemies to respond to attacks from outside their aggro range
                chasingPlayer = true;
                chaseEndTime = System.currentTimeMillis() + 3000; // Reset chase timer
                return false;
            }
            return true;
        }
        return false;
    }

    // Override updateImmunity to handle hit state and extended immunity
    @Override
    public void updateImmunity() {
        long now = System.currentTimeMillis();
        if (inHitState) {
            if (now - hitStateStartTime >= BASE_HIT_STATE_DURATION) {
                inHitState = false;
                // Always trigger chase state after hit state ends, regardless of previous state
                // This allows enemies to respond to attacks from outside their aggro range
                chasingPlayer = true;
                chaseEndTime = now + 3000; // Reset chase timer
                // Extend immunity for a brief period after hit state
                this.immunityEndTime = hitImmunityEndTime;
            }
        }
        if (isImmune && now >= immunityEndTime) {
            isImmune = false;
        }
    }

    @Override
    public boolean take_damage(int damage) {
        int actualDamage = Math.max(0, damage - get_total_defense());
        this.currentHp = Math.max(0, this.currentHp - actualDamage);
        notify_observers("HP_CHANGED", this.currentHp);
        if (currentHp <= 0 && !isDying) {
            startDying();
            notify_observers("CHARACTER_DEFEATED", this);
            return false;
        }
        return currentHp > 0;
    }

    public void startDying() {
        isDying = true;
        dyingStartTime = System.currentTimeMillis();
    }

    public boolean isDying() {
        return isDying;
    }

    public boolean shouldBeDeleted() {
        return isDying && (System.currentTimeMillis() - dyingStartTime >= DYING_DURATION);
    }
    
    /**
     * Check if this is a boss (for log messages and game over terms)
     * Default implementation returns false for regular enemies
     *
     * @return false (regular enemies are not bosses)
     */
    public boolean isBoss() {
        return false;
    }

    /**
     * Get the character type for display purposes
     * Default implementation returns "Enemy"
     *
     * @return "Enemy" for regular enemies
     */
    public String getCharacterType() {
        return "Enemy";
    }
    
    /**
     * Get the current move speed
     *
     * @return current move speed
     */
    public float getMoveSpeed() {
        return moveSpeed;
    }
    
    /**
     * Set the move speed
     *
     * @param speed new move speed value
     */
    public void setMoveSpeed(float speed) {
        this.moveSpeed = speed;
    }
} 