package model.characters;

import enums.CharacterClass;
import enums.GameConstants;
import utilities.Position;
import model.items.Item;
import model.items.Consumable;
import model.equipment.Weapon;
import model.equipment.Armor;
import model.equipment.Equipment;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import utilities.Collision;
import utilities.Tile;
import model.items.KeyItem;

/**
 * Player character class extending Character.
 * Handles player-specific functionality including inventory, leveling, and class selection.
 */
public class Player extends Character {

    // MANDATORY: Player-specific attributes
    private List<Item> inventory;
    
    // NEW: Equipment management
    private List<Equipment> equipmentInventory;
    
    // Character class implementation using OOP design
    private BaseClass playerClassOOP;
    private float pixelX, pixelY;
    private int targetTileX, targetTileY;
    private int moveDX = 0, moveDY = 0;
    private model.gameLogic.GameLogic gameLogic; // Add reference to GameLogic
    private int lastTileX = -1, lastTileY = -1; // Track last tile position for item collection
    private int lastMoveDX = 0, lastMoveDY = 1; // Default to down
    // --- NEW: Independent aim direction ---
    private int aimDX = 0, aimDY = 1; // Default aim down
    private int lastAimDX = 0, lastAimDY = 1; // Track last nonzero aim direction
    private long lastManaRegenTime = System.currentTimeMillis();
    private int enemiesSlain = 0;
    
    // Clarity effect (increased field of view)
    private boolean clarityEffectActive = false;
    private long clarityEffectEndTime = 0;
    
    // Invisibility effect (enemy detection immunity)
    private boolean invisibilityEffectActive = false;
    private long invisibilityEffectEndTime = 0;
    
    // Swiftness effect (movement speed multiplier)
    private boolean swiftnessEffectActive = false;
    private long swiftnessEffectEndTime = 0;
    
    private boolean immortalityEffectActive = false;
    private long immortalityEffectEndTime = 0;
    
    // Experience tracking
    private int currentExp = 0;
    private int totalExp = 500;
    
    // Level points tracking
    private int levelPoints = 0;
    
    // Scrap tracking (similar to experience)
    private int currentScrap = 0;
    private int totalScrap = 100; // Amount needed for one upgrade crystal
    
    // Track stat usage limits (5 uses per stat)
    private int healthUses = 0;
    private int attackUses = 0;
    private int defenseUses = 0;
    private int rangeUses = 0;
    private int speedUses = 0;
    private int manaUses = 0;

    /**
     * MANDATORY: Constructor for Player
     *
     * @param name Player's chosen name
     * @param characterClass Selected character class
     * @param startPosition Initial spawn position
     */
    public Player(String name, CharacterClass characterClass, Position startPosition) {
        super(name, characterClass, startPosition);
        this.inventory = new ArrayList<>();
        this.equipmentInventory = new ArrayList<>();
        this.gameLogic = null;
        setPlayerClassOOP(characterClass);
        initialize_starting_equipment();
        this.pixelX = startPosition.get_x() * GameConstants.TILE_SIZE;
        this.pixelY = startPosition.get_y() * GameConstants.TILE_SIZE;
        this.targetTileX = startPosition.get_x();
        this.targetTileY = startPosition.get_y();
        initialize_stats(); // Call here after playerClassOOP is set
    }

    /**
     * Set the GameLogic reference for notifications
     */
    public void setGameLogic(model.gameLogic.GameLogic logic) { this.gameLogic = logic; }
    public model.gameLogic.GameLogic getGameLogic() { return this.gameLogic; }
    
    /**
     * Reset player state for a new game
     */
    public void reset_for_new_game() {
        // Reset inventory
        inventory.clear();
        equipmentInventory.clear();
        
        // Reset stats
        currentExp = 0;
        levelPoints = 0;
        currentScrap = 0;
        enemiesSlain = 0;
        
        // Reset effect timers
        clarityEffectActive = false;
        invisibilityEffectActive = false;
        swiftnessEffectActive = false;
        immortalityEffectActive = false;
        
        // Reset stat usage limits
        healthUses = 0;
        attackUses = 0;
        defenseUses = 0;
        rangeUses = 0;
        speedUses = 0;
        manaUses = 0;
        
        // Reset position
        pixelX = get_position().get_x() * GameConstants.TILE_SIZE;
        pixelY = get_position().get_y() * GameConstants.TILE_SIZE;
        targetTileX = get_position().get_x();
        targetTileY = get_position().get_y();
        lastTileX = -1;
        lastTileY = -1;
        moveDX = 0;
        moveDY = 0;
        aimDX = 0;
        aimDY = 1;
        lastAimDX = 0;
        lastAimDY = 1;
        
        // Reset character stats
        initialize_stats();
        
        // Add starting equipment and items
        initialize_starting_equipment();
        
        // Notify observers of inventory change
        notify_observers("INVENTORY_CHANGED", inventory);
    }
    
    /**
     * Resets the player for a new game.
     * Called by the main controller when resetting the game.
     */
    public void resetPlayer() {
        reset_for_new_game();
    }
    
    /**
     * Override notify_observers to also notify GameLogic for inventory changes and auto-activation
     */
    @Override
    public void notify_observers(String event, Object data) {
        // Always notify the base observers
        super.notify_observers(event, data);
        
        // Also notify GameLogic for inventory changes, auto-activation, and log messages so it can forward to GameView
        if (gameLogic != null && (event.equals("INVENTORY_CHANGED") || event.equals("IMMORTALITY_AUTO_ACTIVATED") || event.equals("LOG_MESSAGE"))) {
            gameLogic.notify_observers(event, data);
        }
    }

    /**
     * MANDATORY: Initialize starting equipment
     */
    private void initialize_starting_equipment() {
        inventory.clear();
        equipmentInventory.clear();
        
        // Add starting weapons and armor based on character class
        add_starting_equipment();
        
        // Add starting consumables
        add_starting_items();
    }
    
    /**
     * NEW: Add starting equipment based on character class
     */
    private void add_starting_equipment() {
        utilities.WeaponDefinitionManager weaponManager = utilities.WeaponDefinitionManager.getInstance();
        
        // Get starting weapon from JSON configuration
        model.equipment.Weapon startingWeapon = weaponManager.getStartingWeapon(characterClass);
        
        // Get starting armor from JSON configuration
        model.equipment.Armor startingArmor = weaponManager.getStartingArmor(characterClass);
        
        // Add equipment to inventory and equip them
        if (startingWeapon != null) {
            collect_equipment(startingWeapon);
            equip_weapon(startingWeapon);
        }
        
        if (startingArmor != null) {
            collect_equipment(startingArmor);
            equip_armor(startingArmor);
        }
    }

    /**
     * MANDATORY: Add starting items to inventory
     */
    private void add_starting_items() {
        // Add health potions
        collect_item(new model.items.Consumable("Health Potion", 50, "health"));
        collect_item(new model.items.Consumable("Health Potion", 50, "health"));
        // Add mana potions for magic users
        if (characterClass == enums.CharacterClass.MAGE || characterClass == enums.CharacterClass.ROGUE) {
            collect_item(new model.items.Consumable("Mana Potion", 30, "mana"));
        }
    }

    /**
     * MANDATORY: Select character class (used during character creation)
     *
     * @param classType The CharacterClass to select
     */
    public void select_class(CharacterClass classType) {
        setPlayerClassOOP(classType);
        initialize_stats();
        initialize_starting_equipment();
        notify_observers("CLASS_SELECTED", classType);
    }

    /**
     * MANDATORY: Collect an item and add to inventory
     *
     * @param item The Item to collect
     * @return true if item was successfully added
     */
    public boolean collect_item(Item item) {
        if (item == null) return false;
        
        // Check if item is equipment and route to equipment inventory
        if (item instanceof Equipment) {
            return collect_equipment((Equipment) item);
        }
        
        // Regular items go to normal inventory
        inventory.add(item);
        notify_observers("ITEM_COLLECTED", item);
        return true;
    }

    /**
     * MANDATORY: Use item from inventory
     *
     * @param item The item to use
     * @return true if item was successfully used and consumed
     */
    public boolean use_item(Item item) {
        if (item instanceof Consumable) {
            boolean used = item.use(this);
            if (used) {
                // Remove the item from inventory after successful use
                inventory.remove(item);
                notify_observers("INVENTORY_CHANGED", inventory);
            }
            return used;
        } else if (item instanceof KeyItem && item.get_name().equals("Upgrade Crystal")) {
            // Handle Upgrade Crystals - they can be consumed but don't have direct effects
            boolean used = item.use(this);
            if (used) {
                // Remove the item from inventory after successful use
                inventory.remove(item);
                notify_observers("INVENTORY_CHANGED", inventory);
            }
            return used;
        }
        return false;
    }
    
    /**
     * Remove a Floor Key from inventory (special case for floor progression)
     * @return true if a Floor Key was found and removed
     */
    public boolean remove_floor_key() {
        for (int i = 0; i < inventory.size(); i++) {
            Item item = inventory.get(i);
            if (item instanceof KeyItem) {
                KeyItem keyItem = (KeyItem) item;
                if (keyItem.get_name().equals("Floor Key")) {
                    inventory.remove(i);
                    notify_observers("INVENTORY_CHANGED", inventory);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * MANDATORY: Open inventory interface
     */
    public void open_inventory() {
        notify_observers("INVENTORY_OPENED", inventory);
    }

    /**
     * NEW: Open equipment interface
     */
    public void open_equipment() {
        notify_observers("EQUIPMENT_OPENED", equipmentInventory);
    }

    /**
     * NEW: Collect equipment and add to equipment inventory
     */
    public boolean collect_equipment(Equipment equipment) {
        if (equipment == null) return false;
        
        // Check if equipment inventory would be full after adding this item (15 pieces max)
        if (equipmentInventory.size() >= 15) {
            notify_observers("LOG_MESSAGE", "Equipment inventory is full! Cannot collect " + equipment.get_name() + ".");
            if (gameLogic != null) {
                gameLogic.notify_observers("LOG_MESSAGE", "Equipment inventory is full! Cannot collect " + equipment.get_name() + ".");
            }
            return false;
        }
        
        equipmentInventory.add(equipment);
        notify_observers("EQUIPMENT_COLLECTED", equipment);
        if (gameLogic != null) {
            gameLogic.notify_observers("EQUIPMENT_COLLECTED", equipment);
        }
        return true;
    }

    /**
     * NEW: Equip a weapon
     */
    public boolean equip_weapon(Equipment weapon) {
        if (weapon == null || weapon.get_equipment_type() != Equipment.EquipmentType.WEAPON) {
            return false;
        }
        
        // Check weapon class compatibility
        model.equipment.Weapon weaponItem = (model.equipment.Weapon) weapon;
        if (!weaponItem.get_class_type().get_class_name().equals(get_selected_class())) {
            // Notify observers about incompatible weapon
            notify_observers("LOG_MESSAGE", "This weapon is incompatible with your class!");
            return false;
        }
        
        // Remove modifiers from currently equipped weapon
        if (equippedWeapon != null) {
            equippedWeapon.removeStatModifiers(this);
        }
            
        // Equip new weapon and apply modifiers
        equippedWeapon = weaponItem;
        equippedWeapon.applyStatModifiers(this);

        notify_observers("WEAPON_EQUIPPED", weapon);
        return true;
    }
    

    /**
     * NEW: Equip armor
     */
    public boolean equip_armor(Equipment armor) {
        if (armor == null || armor.get_equipment_type() != Equipment.EquipmentType.ARMOR) {
            return false;
        }
        
        // Remove modifiers from currently equipped armor
        if (equippedArmor != null) {
            equippedArmor.removeStatModifiers(this);
        }
        
        // Equip new armor and apply modifiers
        equippedArmor = (model.equipment.Armor) armor;
        equippedArmor.applyStatModifiers(this);
        
        notify_observers("ARMOR_EQUIPPED", armor);
        return true;
    }

    /**
     * NEW: Unequip weapon
     */
    public boolean unequip_weapon() {
        if (equippedWeapon == null) return false;
        
        equippedWeapon.removeStatModifiers(this);
        equippedWeapon = null;
        
        notify_observers("WEAPON_UNEQUIPPED", null);
        return true;
    }

    /**
     * NEW: Unequip armor
     */
    public boolean unequip_armor() {
        if (equippedArmor == null) return false;
        
        equippedArmor.removeStatModifiers(this);
        equippedArmor = null;
        
        notify_observers("ARMOR_UNEQUIPPED", null);
        return true;
    }

    /**
     * NEW: Get equipment inventory
     */
    public List<Equipment> get_equipment_inventory() {
        return new ArrayList<>(equipmentInventory);
    }

    /**
     * NEW: Get weapons from equipment inventory
     */
    public List<Equipment> get_weapons() {
        List<Equipment> weapons = new ArrayList<>();
        for (Equipment equipment : equipmentInventory) {
            if (equipment.get_equipment_type() == Equipment.EquipmentType.WEAPON) {
                weapons.add(equipment);
            }
        }
        return weapons;
    }

    /**
     * NEW: Get armor from equipment inventory
     */
    public List<Equipment> get_armor() {
        List<Equipment> armor = new ArrayList<>();
        for (Equipment equipment : equipmentInventory) {
            if (equipment.get_equipment_type() == Equipment.EquipmentType.ARMOR) {
                armor.add(equipment);
            }
        }
        return armor;
    }

    /**
     * MANDATORY: Upgrade equipment using key items
     *
     * @param equipment The Equipment to upgrade
     * @return true if upgrade was successful
     */
    public boolean upgrade_equipment(Equipment equipment) {
        // Find required upgrade items in inventory
        List<Item> upgradeItems = find_upgrade_items_for(equipment);
        if (can_upgrade_equipment(equipment, upgradeItems)) {
            // Remove upgrade items from inventory
            for (Item item : upgradeItems) {
                inventory.remove(item);
            }
            // Perform upgrade
            boolean upgraded = equipment.upgrade();
            if (upgraded) {
                notify_observers("EQUIPMENT_UPGRADED", equipment);
                return true;
            }
        }
        return false;
    }

    /**
     * MANDATORY: Check if equipment can be upgraded
     *
     * @param equipment The Equipment to check
     * @param upgradeItems Available upgrade items
     * @return true if upgrade is possible
     */
    private boolean can_upgrade_equipment(Equipment equipment, List<Item> upgradeItems) {
        return !upgradeItems.isEmpty() &&
            equipment.get_upgrade_level() < GameConstants.MAX_EQUIPMENT_LEVEL;
    }

    /**
     * MANDATORY: Find items that can upgrade specific equipment
     *
     * @param equipment The Equipment to find upgrades for
     * @return List of applicable upgrade items
     */
    private List<Item> find_upgrade_items_for(Equipment equipment) {
        List<Item> upgradeItems = new ArrayList<>();
        for (Item item : inventory) {
            if (item.get_name().contains("Upgrade") &&
                item.get_class_type().equals(equipment.get_class_type().get_class_name())) {
                upgradeItems.add(item);
            }
        }
        return upgradeItems;
    }

    /**
     * MANDATORY: Gain experience and handle leveling
     *
     * @param expGained Amount of experience to gain
     */
    public void gain_experience(int expGained) {
        // Don't gain experience if already at max level
        if (level >= GameConstants.MAX_LEVEL) {
            return;
        }
        
        this.currentExp += expGained;
        notify_observers("EXPERIENCE_GAINED", expGained);
        // Check for level up
        if (currentExp >= totalExp) {
            level_up();
        }
    }

    /**
     * MANDATORY: Handle level up progression
     */
    private void level_up() {
        if (level >= GameConstants.MAX_LEVEL) return;
        level++;
        // Reset experience for next level and increase total required
        currentExp = 0;
        totalExp += 100; // Increase by 100 each level
        // Increment level points
        increment_level_points();
        notify_observers("LEVEL_UP", level);
        
        // Check if we just reached max level
        if (level >= GameConstants.MAX_LEVEL) {
            notify_observers("LOG_MESSAGE", "You have reached the Maximum Level! You are now a legendary hero!");
        } else {
            // Notify for level up message
            notify_observers("LOG_MESSAGE", "You have leveled up! Use this experience to increase your Abilities.");
        }
    }

    /**
     * MANDATORY: Implement attack behavior for player
     *
     * @param target The character to attack
     * @return Damage dealt
     */
    @Override
    public int attack(Character target) {
        if (!is_alive()) return 0;
        int damage = get_total_attack();
        // Check for critical hit
        int critChance = Stats.calculate_critical_chance(characterClass);
        if (new Random().nextInt(100) < critChance) {
            damage = (int)(damage * GameConstants.CRITICAL_HIT_MULTIPLIER);
            notify_observers("CRITICAL_HIT", damage);
        }
        // Check for miss
        if (new Random().nextInt(100) < GameConstants.MISS_CHANCE) {
            damage = 0;
            notify_observers("ATTACK_MISSED", target);
        }
        // Apply class-specific attack modifiers
        damage = apply_class_attack_bonus(damage, target);
        notify_observers("PLAYER_ATTACKED", target);
        return damage;
    }

    /**
     * MANDATORY: Apply class-specific attack bonuses
     *
     * @param baseDamage Base damage before modifiers
     * @param target The target being attacked
     * @return Modified damage
     */
    private int apply_class_attack_bonus(int baseDamage, Character target) {
        switch (characterClass) {
            case WARRIOR:
                // Warriors deal extra damage to armored enemies
                if (target.get_equipped_armor() != null) {
                    return (int)(baseDamage * 1.2);
                }
                break;
            case MAGE:
                // Mages deal bonus magical damage
                if (currentMp >= 5) {
                    use_mp(5);
                    return (int)(baseDamage * 1.3);
                }
                break;
            case ROGUE:
                // Rogues have chance for sneak attack
                if (new Random().nextInt(100) < 25) {
                    notify_observers("SNEAK_ATTACK", target);
                    return baseDamage * 2;
                }
                break;
        }
        return baseDamage;
    }



    public void setMoveDirection(int dx, int dy) {
        if (!is_alive()) return;
        this.moveDX = dx;
        this.moveDY = dy;
        if (dx != 0 || dy != 0) {
            this.lastMoveDX = dx;
            this.lastMoveDY = dy;
        }
    }

    // --- NEW: Set aim direction independently ---
    public void setAimDirection(int dx, int dy) {
        if (!is_alive()) return;
        // if (dx != 0 || dy != 0) {
        // }
        this.aimDX = dx;
        this.aimDY = dy;
        this.lastAimDX = dx;
        this.lastAimDY = dy;
    }

    public int getAimDX() { return aimDX; }
    public int getAimDY() { return aimDY; }
    public int getLastAimDX() { return lastAimDX; }
    public int getLastAimDY() { return lastAimDY; }

    public void update_movement(model.map.Map map) {
        if (!is_alive()) return;
        updateImmunity();
        // Process pushback first
        updatePushback(map);
        if (isBeingPushed()) {
            // Skip normal movement while being pushed
            return;
        }
        // Only allow a new move if exactly centered on the target tile
        if (Math.abs(pixelX - targetTileX * GameConstants.TILE_SIZE) < 0.01f &&
            Math.abs(pixelY - targetTileY * GameConstants.TILE_SIZE) < 0.01f) {
            if (moveDX != 0 || moveDY != 0) {
                int nextX = targetTileX + moveDX;
                int nextY = targetTileY + moveDY;
                if (Collision.isWalkable(map, nextX, nextY)) {
                    targetTileX = nextX;
                    targetTileY = nextY;
                }
            }
        }
        // Move pixelX/pixelY toward target tile (normalize for diagonal)
        float targetPixelX = targetTileX * GameConstants.TILE_SIZE;
        float targetPixelY = targetTileY * GameConstants.TILE_SIZE;
        float dx = targetPixelX - pixelX;
        float dy = targetPixelY - pixelY;
        float dist = (float)Math.sqrt(dx*dx + dy*dy);
        float moveSpeed = playerClassOOP != null ? playerClassOOP.getMoveSpeed() : 3.0f;
        // Apply swiftness multiplier if active
        if (is_swiftness_effect_active()) {
            moveSpeed *= 2.0f; // 2x speed multiplier
        }
        if (dist > moveSpeed) {
            float ndx = dx / dist;
            float ndy = dy / dist;
            pixelX += moveSpeed * ndx;
            pixelY += moveSpeed * ndy;
        } else {
            pixelX = targetPixelX;
            pixelY = targetPixelY;
            this.position = new utilities.Position(targetTileX, targetTileY);
        }
        // Mark current tile as explored and collect items
        int tileX = (int)Math.floor(pixelX / GameConstants.TILE_SIZE);
        int tileY = (int)Math.floor(pixelY / GameConstants.TILE_SIZE);
        // Only check for items when we move to a new tile
        if (tileX != lastTileX || tileY != lastTileY) {
            lastTileX = tileX;
            lastTileY = tileY;
            if (map != null && tileX >= 0 && tileY >= 0 && tileX < map.get_width() && tileY < map.get_height()) {
                // Get current field of view range (2 normally, 4 with clarity effect)
                int fovRange = get_field_of_view_range();
                // Reveal a rounded area (circle-like) with dynamic radius centered on the new tile
                for (int ix = -fovRange; ix <= fovRange; ix++) {
                    for (int iy = -fovRange; iy <= fovRange; iy++) {
                        int nx = tileX + ix;
                        int ny = tileY + iy;
                        // Use Manhattan distance to approximate a circle
                        if (Math.abs(ix) + Math.abs(iy) <= fovRange) {
                            if (nx >= 0 && ny >= 0 && nx < map.get_width() && ny < map.get_height()) {
                                utilities.Tile t = map.get_tile(nx, ny);
                                if (t != null) t.set_explored();
                            }
                        }
                    }
                }
                // Collect items if present
                Tile tile = map.get_tile(tileX, tileY);
                if (tile.has_items()) {
                    List<model.items.Item> items = tile.get_items();
                    for (model.items.Item item : new ArrayList<>(items)) {
                        if (collect_item(item)) {
                            tile.remove_item(item);
                            if (gameLogic != null) {
                                gameLogic.notify_item_collected(item);
                            }
                        }
                    }
                }
                
                // Handle special tile interactions (like STAIRS)
                if (gameLogic != null) {
                    // Use reflection to access the private handle_tile_interaction method
                    try {
                        java.lang.reflect.Method handleTileInteraction = gameLogic.getClass().getDeclaredMethod("handle_tile_interaction", utilities.Tile.class);
                        handleTileInteraction.setAccessible(true);
                        handleTileInteraction.invoke(gameLogic, tile);
                    } catch (Exception e) {
                        System.err.println("Error calling handle_tile_interaction: " + e.getMessage());
                    }
                }
            }
        }
    }

    public void syncToTilePosition(utilities.Position pos) {
        this.position = pos;
        this.pixelX = pos.get_x() * enums.GameConstants.TILE_SIZE;
        this.pixelY = pos.get_y() * enums.GameConstants.TILE_SIZE;
        this.targetTileX = pos.get_x();
        this.targetTileY = pos.get_y();
    }

    public float getPixelX() { return this.pixelX; }
    public float getPixelY() { return this.pixelY; }

    public int getMoveDX() { return moveDX; }
    public int getMoveDY() { return moveDY; }

    public int getLastMoveDX() { return lastMoveDX; }
    public int getLastMoveDY() { return lastMoveDY; }

    public void setPixelX(float x) { this.pixelX = x; }
    public void setPixelY(float y) { this.pixelY = y; }

    // MANDATORY: Additional getters
    public List<Item> get_inventory() {
        return new ArrayList<>(inventory); // Defensive copy
    }

    public int get_inventory_size() {
        return inventory.size();
    }

    public BaseClass getPlayerClassOOP() { return playerClassOOP; }

    public String get_selected_class() {
        return playerClassOOP.getClassName();
    }

    public boolean has_item(String itemName) {
        return inventory.stream().anyMatch(item -> item.get_name().equals(itemName));
    }

    public List<Item> get_consumables() {
        return inventory.stream()
            .filter(item -> item instanceof Consumable)
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public int get_enemies_slain() { return enemiesSlain; }

    public void increment_enemies_slain() { enemiesSlain++; }
    
    /**
     * Activate clarity effect (increased field of view)
     * @param durationSeconds Duration of the effect in seconds
     */
    public void activate_clarity_effect(int durationSeconds) {
        clarityEffectActive = true;
        clarityEffectEndTime = System.currentTimeMillis() + (durationSeconds * 1000L);
        notify_observers("CLARITY_EFFECT_ACTIVATED", durationSeconds);
    }
    
    /**
     * Check if clarity effect is active
     * @return true if clarity effect is active
     */
    public boolean is_clarity_effect_active() {
        if (clarityEffectActive && System.currentTimeMillis() >= clarityEffectEndTime) {
            clarityEffectActive = false;
            notify_observers("CLARITY_EFFECT_ENDED", null);
        }
        return clarityEffectActive;
    }
    
    /**
     * Get the current field of view range (2 normally, 4 with clarity)
     * @return field of view range in tiles
     */
    public int get_field_of_view_range() {
        return is_clarity_effect_active() ? 4 : 2;
    }
    
    /**
     * Get the progress of the clarity effect (0.0 to 1.0)
     * @return progress as a float from 1.0 (just started) to 0.0 (about to end)
     */
    public float get_clarity_effect_progress() {
        if (!clarityEffectActive) return 0.0f;
        long currentTime = System.currentTimeMillis();
        long totalDuration = 10000; // 10 seconds total
        long elapsed = currentTime - (clarityEffectEndTime - totalDuration);
        if (elapsed >= totalDuration) return 0.0f;
        return 1.0f - ((float) elapsed / totalDuration);
    }
    
    /**
     * Activate invisibility effect (enemy detection immunity)
     * @param durationSeconds Duration of the effect in seconds
     */
    public void activate_invisibility_effect(int durationSeconds) {
        invisibilityEffectActive = true;
        invisibilityEffectEndTime = System.currentTimeMillis() + (durationSeconds * 1000L);
        notify_observers("INVISIBILITY_EFFECT_ACTIVATED", durationSeconds);
    }
    
    /**
     * Check if invisibility effect is active
     * @return true if invisibility effect is active
     */
    public boolean is_invisibility_effect_active() {
        if (invisibilityEffectActive && System.currentTimeMillis() >= invisibilityEffectEndTime) {
            invisibilityEffectActive = false;
            notify_observers("INVISIBILITY_EFFECT_ENDED", null);
        }
        return invisibilityEffectActive;
    }
    
    /**
     * Get the progress of the invisibility effect (0.0 to 1.0)
     * @return progress as a float from 1.0 (just started) to 0.0 (about to end)
     */
    public float get_invisibility_effect_progress() {
        if (!invisibilityEffectActive) return 0.0f;
        long currentTime = System.currentTimeMillis();
        long totalDuration = 8000; // 8 seconds total
        long elapsed = currentTime - (invisibilityEffectEndTime - totalDuration);
        if (elapsed >= totalDuration) return 0.0f;
        return 1.0f - ((float) elapsed / totalDuration);
    }
    
    /**
     * Activate swiftness effect (movement speed multiplier)
     * @param durationSeconds Duration of the effect in seconds
     */
    public void activate_swiftness_effect(int durationSeconds) {
        swiftnessEffectActive = true;
        swiftnessEffectEndTime = System.currentTimeMillis() + (durationSeconds * 1000L);
        notify_observers("SWIFTNESS_EFFECT_ACTIVATED", durationSeconds);
    }
    
    /**
     * Check if swiftness effect is active
     * @return true if swiftness effect is active
     */
    public boolean is_swiftness_effect_active() {
        if (swiftnessEffectActive && System.currentTimeMillis() >= swiftnessEffectEndTime) {
            swiftnessEffectActive = false;
            notify_observers("SWIFTNESS_EFFECT_ENDED", null);
        }
        return swiftnessEffectActive;
    }
    
    /**
     * Get the progress of the swiftness effect (0.0 to 1.0)
     * @return progress as a float from 1.0 (just started) to 0.0 (about to end)
     */
    public float get_swiftness_effect_progress() {
        if (!swiftnessEffectActive) return 0.0f;
        long currentTime = System.currentTimeMillis();
        long totalDuration = 5000; // 5 seconds total
        long elapsed = currentTime - (swiftnessEffectEndTime - totalDuration);
        if (elapsed >= totalDuration) return 0.0f;
        return 1.0f - ((float) elapsed / totalDuration);
    }
    
    /**
     * Activate the immortality effect for the specified duration
     * @param durationSeconds duration in seconds
     */
    public void activate_immortality_effect(int durationSeconds) {
        immortalityEffectActive = true;
        immortalityEffectEndTime = System.currentTimeMillis() + (durationSeconds * 1000);
        notify_observers("IMMORTALITY_EFFECT_STARTED", null);
    }
    
    /**
     * Check if the immortality effect is currently active
     * @return true if the effect is active
     */
    public boolean is_immortality_effect_active() {
        if (immortalityEffectActive && System.currentTimeMillis() >= immortalityEffectEndTime) {
            immortalityEffectActive = false;
            notify_observers("IMMORTALITY_EFFECT_ENDED", null);
        }
        return immortalityEffectActive;
    }
    
    /**
     * Get the progress of the immortality effect (0.0 to 1.0)
     * @return progress as a float from 1.0 (just started) to 0.0 (about to end)
     */
    public float get_immortality_effect_progress() {
        if (!immortalityEffectActive) return 0.0f;
        long currentTime = System.currentTimeMillis();
        long totalDuration = 5000; // 5 seconds total
        long elapsed = currentTime - (immortalityEffectEndTime - totalDuration);
        if (elapsed >= totalDuration) return 0.0f;
        return 1.0f - ((float) elapsed / totalDuration);
    }
    
    /**
     * Get current experience points
     */
    public int get_current_exp() {
        return currentExp;
    }
    
    /**
     * Get total experience points needed for next level
     */
        public int get_total_exp() {
        return totalExp;
    }
    
    public int get_level_points() {
        return levelPoints;
    }
    
    // Scrap system methods
    public int get_current_scrap() {
        return currentScrap;
    }
    
    public int get_total_scrap() {
        return totalScrap;
    }
    
    /**
     * Gain scrap from scrapping equipment
     */
    public void gain_scrap(int scrapGained) {
        if (scrapGained <= 0) return;
        
        currentScrap += scrapGained;
        
        // Check if scrap meter is full
        if (currentScrap >= totalScrap) {
            convert_scrap_to_crystal();
        }
        
        notify_observers("SCRAP_GAINED", scrapGained);
        
        // Also notify GameLogic to update UI immediately
        if (gameLogic != null) {
            gameLogic.notify_observers("SCRAP_GAINED", scrapGained);
        }
    }
    
    /**
     * Convert scrap to upgrade crystal when meter is full
     */
    private void convert_scrap_to_crystal() {
        currentScrap -= totalScrap; // Consume the scrap
        
        // Create upgrade crystal from scrap
        model.items.KeyItem upgradeCrystal = new model.items.KeyItem("Upgrade Crystal", "any");
        collect_item(upgradeCrystal);
        
        notify_observers("SCRAP_CONVERTED", upgradeCrystal);
        
        // Also notify GameLogic to update UI immediately
        if (gameLogic != null) {
            gameLogic.notify_observers("SCRAP_CONVERTED", upgradeCrystal);
        }
    }
    
    /**
     * Scrap equipment to gain scrap
     */
    public boolean scrap_equipment(Equipment equipment) {
        if (equipment == null) return false;
        
        // Calculate scrap value based on tier
        int scrapValue = equipment.get_tier() * 25; // T1=25, T2=50, T3=75
        
        // Check if this equipment is currently equipped and unequip it first
        Weapon equippedWeapon = get_equipped_weapon();
        Armor equippedArmor = get_equipped_armor();
        
        if (equippedWeapon != null && equippedWeapon.equals(equipment)) {
            unequip_weapon();
        }
        if (equippedArmor != null && equippedArmor.equals(equipment)) {
            unequip_armor();
        }
        
        // Remove from equipment inventory by finding matching equipment
        Equipment equipmentToRemove = null;
        for (Equipment invEquipment : equipmentInventory) {
            if (invEquipment.get_name().equals(equipment.get_name()) &&
                invEquipment.get_potency() == equipment.get_potency() &&
                invEquipment.get_tier() == equipment.get_tier()) {
                equipmentToRemove = invEquipment;
                break;
            }
        }
        
        if (equipmentToRemove != null && equipmentInventory.remove(equipmentToRemove)) {
            gain_scrap(scrapValue);
            
            // Notify observers about the scrapping
            notify_observers("EQUIPMENT_SCRAPPED", equipment);
            
            // Also notify GameLogic to update UI immediately
            if (gameLogic != null) {
                gameLogic.notify_observers("EQUIPMENT_SCRAPPED", equipment);
            }
            
            return true;
        }
        
        return false;
    }

    public void increment_level_points() {
        levelPoints++;
    }
    
    // Stat usage getters
    public int get_health_uses() { return healthUses; }
    public int get_attack_uses() { return attackUses; }
    public int get_defense_uses() { return defenseUses; }
    public int get_range_uses() { return rangeUses; }
    public int get_speed_uses() { return speedUses; }
    public int get_mana_uses() { return manaUses; }
    
    /**
     * Increase a specific stat by 10% if level points are available
     * @param statType The type of stat to increase ("health", "attack", "defense", "range", "speed", "mana")
     * @return true if stat was increased, false if not enough level points
     */
    public boolean increase_stat(String statType) {
        if (levelPoints <= 0) {
            return false;
        }
        
        // Check usage limits for each stat
        switch (statType.toLowerCase()) {
            case "health":
                if (healthUses >= 5) {
                    notify_observers("LOG_MESSAGE", "This stat has achieved its Maximum Potential");
                    return false;
                }
                break;
            case "attack":
                if (attackUses >= 5) {
                    notify_observers("LOG_MESSAGE", "This stat has achieved its Maximum Potential");
                    return false;
                }
                break;
            case "defense":
                if (defenseUses >= 5) {
                    notify_observers("LOG_MESSAGE", "This stat has achieved its Maximum Potential");
                    return false;
                }
                break;
            case "range":
                if (rangeUses >= 5) {
                    notify_observers("LOG_MESSAGE", "This stat has achieved its Maximum Potential");
                    return false;
                }
                break;
            case "speed":
                if (speedUses >= 5) {
                    notify_observers("LOG_MESSAGE", "This stat has achieved its Maximum Potential");
                    return false;
                }
                break;
            case "mana":
                if (manaUses >= 5) {
                    notify_observers("LOG_MESSAGE", "This stat has achieved its Maximum Potential");
                    return false;
                }
                break;
            default:
                levelPoints++; // Refund the point if invalid stat type
                return false;
        }
        
        levelPoints--; // Consume one level point
        
        switch (statType.toLowerCase()) {
            case "health":
                healthUses++;
                int healthIncrease = (int)(maxHp * 0.1);
                maxHp += healthIncrease;
                currentHp += healthIncrease; // Also heal current HP
                notify_observers("LOG_MESSAGE", "Health increased by " + healthIncrease + " points!");
                break;
            case "attack":
                attackUses++;
                float attackIncrease = baseAtk * 0.1f;
                baseAtk += attackIncrease;
                
                // If player has projectiles, also increase projectile damage
                if (getPlayerClassOOP().hasProjectile()) {
                    // For now, projectiles use the same damage as melee attacks
                    // This could be enhanced later to have separate projectile damage
                    notify_observers("LOG_MESSAGE", "Attack and projectile damage increased by " + String.format("%.1f", attackIncrease) + " points!");
                } else {
                    notify_observers("LOG_MESSAGE", "Attack increased by " + String.format("%.1f", attackIncrease) + " points!");
                }
                break;
            case "defense":
                defenseUses++;
                float currentDefense = getPlayerClassOOP().getDefense();
                float defenseIncrease = currentDefense * 0.1f;
                // Note: Defense is handled by the class, so we need to update it there
                getPlayerClassOOP().increaseDefense(defenseIncrease);
                notify_observers("LOG_MESSAGE", "Defense increased by " + String.format("%.1f", defenseIncrease) + " points!");
                break;
            case "range":
                rangeUses++;
                float rangeIncrease = getPlayerClassOOP().getRange() * 0.1f;
                getPlayerClassOOP().increaseRange(rangeIncrease);
                
                // Also increase attack width (swipe width)
                int widthIncrease = (int)(getPlayerClassOOP().getAttackWidth() * 0.1);
                getPlayerClassOOP().setAttackWidth(getPlayerClassOOP().getAttackWidth() + widthIncrease);
                
                notify_observers("LOG_MESSAGE", "Range increased by " + String.format("%.1f", rangeIncrease) + " tiles and attack width by " + widthIncrease + " degrees!");
                break;
            case "speed":
                speedUses++;
                float speedIncrease = getPlayerClassOOP().getMoveSpeed() * 0.1f;
                getPlayerClassOOP().increaseMoveSpeed(speedIncrease);
                notify_observers("LOG_MESSAGE", "Movement speed increased by " + String.format("%.1f", speedIncrease) + "!");
                break;
            case "mana":
                manaUses++;
                int manaIncrease = (int)(maxMp * 0.1);
                maxMp += manaIncrease;
                currentMp += manaIncrease; // Also restore current MP
                notify_observers("LOG_MESSAGE", "Mana increased by " + manaIncrease + " points!");
                break;
            default:
                levelPoints++; // Refund the point if invalid stat type
                return false;
        }
        
        notify_observers("STATS_UPDATED", this);
        return true;
    }
    
    /**
     * Override take_damage to add auto-activation of immortality amulet
     */
    @Override
    public boolean take_damage(int damage) {
        // Check if we should auto-activate immortality amulet
        if (!is_immortality_effect_active() && currentHp > 0) {
            boolean shouldActivate = false;
            
            // Check health percentage (10% or less)
            int healthPercentage = (currentHp * 100) / maxHp;
            if (healthPercentage <= 10) {
                shouldActivate = true;
            }
            
            // Check if this attack would kill the player (even if above 10%)
            int actualDamage = Math.max(0, damage - get_total_defense());
            if (currentHp <= actualDamage) {
                shouldActivate = true;
            }
            
            if (shouldActivate) {
                // Check if we have an immortality amulet in inventory
                for (Item item : inventory) {
                    if (item instanceof Consumable && 
                        ((Consumable) item).get_effect_type().equals("immortality")) {
                        // Auto-activate the amulet
                        if (use_item(item)) {
                            // Item is already removed by use_item() method
                            notify_observers("INVENTORY_CHANGED", inventory);
                            // Send special notification for auto-activation
                            notify_observers("IMMORTALITY_AUTO_ACTIVATED", item);
                            break; // Only use one amulet
                        }
                    }
                }
            }
        }
        
        // If immortality effect is active, nullify all damage
        if (is_immortality_effect_active()) {
            return true; // Still alive, but no damage taken
        }
        
        // Call parent method to handle actual damage
        return super.take_damage(damage);
    }
    
    /**
     * Override triggerPushback to nullify pushback during immortality effect
     */
    @Override
    public void triggerPushback(float directionX, float directionY, float distance, float speed) {
        // If immortality effect is active, ignore pushback
        if (is_immortality_effect_active()) {
            return;
        }
        
        // Call parent method to handle pushback
        super.triggerPushback(directionX, directionY, distance, speed);
    }

    public List<String> get_collected_items() {
        List<String> names = new ArrayList<>();
        for (Item item : inventory) {
            names.add(item.get_name());
        }
        return names;
    }
    /**
     * Safely add MP to the player, up to maxMp, and notify observers.
     */
    public void add_mp(int amount) {
        if (amount <= 0 || maxMp == 0) return;
        int before = currentMp;
        currentMp = Math.min(maxMp, currentMp + amount);
        if (currentMp != before) {
            notify_observers("MP_CHANGED", currentMp);
        }
    }

    /**
     * Passive mana regeneration: +1 MP per second if maxMp > 0 and currentMp < maxMp.
     */
    public void passiveManaRegen() {
        if (maxMp > 0 && currentMp < maxMp) {
            long now = System.currentTimeMillis();
            if (now - lastManaRegenTime >= 1000) {
                lastManaRegenTime = now;
                add_mp(1);
            }
        } else {
            lastManaRegenTime = System.currentTimeMillis(); // reset timer if full or no MP
        }
    }

    // Add a method to switch class for debug
    public void debug_switch_class(int classNum) {
        if (classNum == 1) setPlayerClassOOP(CharacterClass.WARRIOR);
        else if (classNum == 2) setPlayerClassOOP(CharacterClass.MAGE);
        else if (classNum == 3) setPlayerClassOOP(CharacterClass.ROGUE);
        else if (classNum == 4) setPlayerClassOOP(CharacterClass.RANGER);
        initialize_stats();
        // Do NOT call initialize_starting_equipment or add items in debug mode
        notify_observers("CLASS_SELECTED", this.characterClass);
        notify_observers("INVENTORY_CHANGED", inventory); // Always update inventory UI
    }

    private void setPlayerClassOOP(CharacterClass classType) {
        switch (classType) {
            case WARRIOR:
                this.playerClassOOP = new WarriorClass();
                break;
            case MAGE:
                this.playerClassOOP = new MageClass();
                break;
            case ROGUE:
                this.playerClassOOP = new RogueClass();
                break;
            case RANGER:
                this.playerClassOOP = new RangerClass();
                break;
        }
        this.characterClass = classType;
    }

    @Override
    protected void initialize_stats() {
        this.maxHp = Math.max(1, playerClassOOP.getBaseHp());
        this.currentHp = maxHp;
        this.baseAtk = playerClassOOP.getBaseAtk();
        if (playerClassOOP.getBaseMp() == 0) {
            this.maxMp = 0;
            this.currentMp = 0;
        } else {
            this.maxMp = Math.max(1, playerClassOOP.getBaseMp());
            this.currentMp = maxMp;
        }
    }
} 