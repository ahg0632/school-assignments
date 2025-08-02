package model.characters;

import interfaces.GameModel;
import interfaces.GameObserver;
import enums.CharacterClass;
import utilities.Position;
import model.equipment.Weapon;
import model.equipment.Armor;
import model.items.Item;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for all characters in the Mini Rogue Demo.
 * Provides common attributes and behavior for Player, Enemy, and Boss classes.
 */
public abstract class Character implements GameModel {

    // MANDATORY: Core character attributes
    protected String name;
    protected CharacterClass characterClass;
    protected int level;
    protected int experience;
    protected Position position;

    // MANDATORY: Combat statistics
    protected int currentHp;
    protected int maxHp;
    protected float baseAtk;
    protected int currentMp;
    protected int maxMp;

    // MANDATORY: Equipment
    protected Weapon equippedWeapon;
    protected Armor equippedArmor;

    // MANDATORY: Observer pattern implementation
    protected List<GameObserver> observers;

    // Pushback state
    protected boolean isBeingPushed = false;
    protected float pushDirectionX = 0f, pushDirectionY = 0f;
    protected float pushDistanceRemaining = 0f;
    protected float pushSpeed = 0f;

    // Immunity state
    protected boolean isImmune = false;
    protected long immunityEndTime = 0;

    // Pixel-based movement fields
    protected float pixelX, pixelY;
    protected float moveDX, moveDY;

    /**
     * MANDATORY: Constructor for Character
     *
     * @param name The character's name
     * @param characterClass The character's class
     * @param position Initial position
     */
    public Character(String name, CharacterClass characterClass, Position position) {
        this.name = name;
        this.characterClass = characterClass;
        this.position = position;
        this.level = 1;
        this.experience = 0;
        this.observers = new ArrayList<>();
        // Do NOT call initialize_stats() here; subclasses must call it after their own fields are set
    }

    /**
     * MANDATORY: Initialize character statistics based on class
     */
    protected void initialize_stats() {
        this.maxHp = Math.max(1, characterClass.get_base_hp());
        this.currentHp = maxHp;
        this.baseAtk = (float) characterClass.get_base_atk();
        this.maxMp = Math.max(1, characterClass.get_base_mp());
        this.currentMp = maxMp;
    }

    /**
     * Move character to a new pixel position (pixel-based movement)
     */
    public void move_to(float x, float y) {
        this.pixelX = x;
        this.pixelY = y;
        // Optionally update logical tile position
        this.position = new Position((int)(x / enums.GameConstants.TILE_SIZE), (int)(y / enums.GameConstants.TILE_SIZE));
        notify_observers("CHARACTER_MOVED", this);
    }

    public void setPixelPosition(float x, float y) {
        this.pixelX = x;
        this.pixelY = y;
    }
    public float getPixelX() { return pixelX; }
    public float getPixelY() { return pixelY; }

    /**
     * MANDATORY: Take damage and update HP
     *
     * @param damage Amount of damage to take
     * @return true if character is still alive
     */
    public boolean take_damage(int damage) {
        int actualDamage = Math.max(0, damage - get_total_defense());
        this.currentHp = Math.max(0, this.currentHp - actualDamage);
        notify_observers("HP_CHANGED", this.currentHp);
        if (currentHp <= 0) {
            notify_observers("CHARACTER_DEFEATED", this);
            return false;
        }
        return true;
    }

    /**
     * MANDATORY: Heal character HP
     *
     * @param healAmount Amount to heal
     */
    public void heal(int healAmount) {
        int before = currentHp;
        this.currentHp = Math.min(maxHp, this.currentHp + healAmount);
        notify_observers("HP_CHANGED", this.currentHp);
    }

    /**
     * MANDATORY: Use MP for abilities
     *
     * @param mpCost Amount of MP to consume
     * @return true if MP was available and consumed
     */
    public boolean use_mp(int mpCost) {
        if (this.currentMp >= mpCost) {
            this.currentMp -= mpCost;
            notify_observers("MP_CHANGED", this.currentMp);
            return true;
        }
        return false;
    }

    /**
     * MANDATORY: Restore MP
     *
     * @param mpAmount Amount of MP to restore
     */
    public void restore_mp(int mpAmount) {
        this.currentMp = Math.min(maxMp, this.currentMp + mpAmount);
        notify_observers("MP_CHANGED", this.currentMp);
    }

    /**
     * MANDATORY: Calculate total attack power including equipment
     *
     * @return Total attack value
     */
    public int get_total_attack() {
        float equipmentModifier = getEquipmentAttackModifier();
        return (int)(baseAtk + equipmentModifier);
    }

    /**
     * MANDATORY: Calculate total defense including equipment
     *
     * @return Total defense value
     */
    public int get_total_defense() {
        int armorDefense = (equippedArmor != null) ? equippedArmor.get_defense_value() : 0;
        return armorDefense;
    }
    
    /**
     * Get equipment modifier for attack stat
     */
    public float getEquipmentAttackModifier() {
        float modifier = 0.0f;
        if (equippedWeapon != null) {
            Float weaponModifier = equippedWeapon.get_stat_modifiers().get("Attack");
            if (weaponModifier != null) modifier += weaponModifier;
        }
        if (equippedArmor != null) {
            Float armorModifier = equippedArmor.get_stat_modifiers().get("Attack");
            if (armorModifier != null) modifier += armorModifier;
        }
        return modifier;
    }
    
    /**
     * Get equipment modifier for defense stat
     */
    public float getEquipmentDefenseModifier() {
        float modifier = 0.0f;
        if (equippedWeapon != null) {
            Float weaponModifier = equippedWeapon.get_stat_modifiers().get("Defense");
            if (weaponModifier != null) modifier += weaponModifier;
        }
        if (equippedArmor != null) {
            Float armorModifier = equippedArmor.get_stat_modifiers().get("Defense");
            if (armorModifier != null) modifier += armorModifier;
        }
        return modifier;
    }
    
    /**
     * Get equipment modifier for speed stat
     */
    public float getEquipmentSpeedModifier() {
        float modifier = 0.0f;
        if (equippedWeapon != null) {
            Float weaponModifier = equippedWeapon.get_stat_modifiers().get("Speed");
            if (weaponModifier != null) modifier += weaponModifier;
        }
        if (equippedArmor != null) {
            Float armorModifier = equippedArmor.get_stat_modifiers().get("Speed");
            if (armorModifier != null) modifier += armorModifier;
        }
        return modifier;
    }
    
    /**
     * Get equipment modifier for range stat
     */
    public float getEquipmentRangeModifier() {
        float modifier = 0.0f;
        if (equippedWeapon != null) {
            Float weaponModifier = equippedWeapon.get_stat_modifiers().get("Range");
            if (weaponModifier != null) modifier += weaponModifier;
        }
        if (equippedArmor != null) {
            Float armorModifier = equippedArmor.get_stat_modifiers().get("Range");
            if (armorModifier != null) modifier += armorModifier;
        }
        return modifier;
    }
    
    /**
     * Get equipment modifier for mana stat
     */
    public float getEquipmentManaModifier() {
        float modifier = 0.0f;
        if (equippedWeapon != null) {
            Float weaponModifier = equippedWeapon.get_stat_modifiers().get("Mana");
            if (weaponModifier != null) modifier += weaponModifier;
        }
        if (equippedArmor != null) {
            Float armorModifier = equippedArmor.get_stat_modifiers().get("Mana");
            if (armorModifier != null) modifier += armorModifier;
        }
        return modifier;
    }
    
    /**
     * Get equipment modifier for health stat
     */
    public float getEquipmentHealthModifier() {
        float modifier = 0.0f;
        if (equippedWeapon != null) {
            Float weaponModifier = equippedWeapon.get_stat_modifiers().get("Health");
            if (weaponModifier != null) modifier += weaponModifier;
        }
        if (equippedArmor != null) {
            Float armorModifier = equippedArmor.get_stat_modifiers().get("Health");
            if (armorModifier != null) modifier += armorModifier;
        }
        return modifier;
    }

    /**
     * MANDATORY: Check if character is alive
     *
     * @return true if HP > 0
     */
    public boolean is_alive() {
        return currentHp > 0;
    }

    /**
     * MANDATORY: Abstract method for character-specific attack behavior
     *
     * @param target The character to attack
     * @return Damage dealt
     */
    public abstract int attack(Character target);



    public void triggerPushback(float directionX, float directionY, float distance, float speed) {
        this.isBeingPushed = true;
        float len = (float)Math.sqrt(directionX*directionX + directionY*directionY);
        if (len == 0) { directionX = 0; directionY = 1; len = 1; }
        this.pushDirectionX = directionX / len;
        this.pushDirectionY = directionY / len;
        this.pushDistanceRemaining = distance;
        this.pushSpeed = speed;
    }

    // Refactored: updatePushback with map collision check
    public void updatePushback(model.map.Map map) {
        if (isBeingPushed) {
            float move = Math.min(pushSpeed, pushDistanceRemaining);
            float nextPixelX = 0f, nextPixelY = 0f;
            if (this instanceof model.characters.Player) {
                model.characters.Player p = (model.characters.Player)this;
                nextPixelX = p.getPixelX() + move * pushDirectionX;
                nextPixelY = p.getPixelY() + move * pushDirectionY;
            } else if (this instanceof model.characters.Enemy) {
                model.characters.Enemy e = (model.characters.Enemy)this;
                nextPixelX = e.getPixelX() + move * pushDirectionX;
                nextPixelY = e.getPixelY() + move * pushDirectionY;
            }
            int nextTileX = (int)Math.floor(nextPixelX / enums.GameConstants.TILE_SIZE);
            int nextTileY = (int)Math.floor(nextPixelY / enums.GameConstants.TILE_SIZE);
            boolean canMove = (map != null) && utilities.Collision.isWalkable(map, nextTileX, nextTileY);
            if (canMove) {
                if (this instanceof model.characters.Player) {
                    model.characters.Player p = (model.characters.Player)this;
                    p.setPixelX(nextPixelX);
                    p.setPixelY(nextPixelY);
                } else if (this instanceof model.characters.Enemy) {
                    model.characters.Enemy e = (model.characters.Enemy)this;
                    e.setPixelX(nextPixelX);
                    e.setPixelY(nextPixelY);
                }
                pushDistanceRemaining -= move;
                if (pushDistanceRemaining <= 0) {
                    isBeingPushed = false;
                }
            } else {
                // Hit a wall: stop pushback and increase immunity by 50%
                isBeingPushed = false;
                if (this instanceof model.characters.Enemy) {
                    model.characters.Enemy e = (model.characters.Enemy)this;
                    if (e.isImmune) {
                        long remaining = e.immunityEndTime - System.currentTimeMillis();
                        if (remaining > 0) {
                            e.immunityEndTime += remaining / 2;
                        }
                    }
                } else if (this instanceof model.characters.Player) {
                    model.characters.Player p = (model.characters.Player)this;
                    if (p.isImmune) {
                        long remaining = p.immunityEndTime - System.currentTimeMillis();
                        if (remaining > 0) {
                            p.immunityEndTime += remaining / 2;
                        }
                    }
                }
            }
        }
    }



    public boolean isBeingPushed() { return isBeingPushed; }

    public void setImmune(long durationMs) {
        isImmune = true;
        immunityEndTime = System.currentTimeMillis() + durationMs;
    }
    public void updateImmunity() {
        if (isImmune && System.currentTimeMillis() >= immunityEndTime) {
            isImmune = false;
        }
    }
    public boolean isImmune() { return isImmune; }

    // MANDATORY: Observer pattern implementation
    @Override
    public void notify_observers(String event, Object data) {
        for (GameObserver observer : observers) {
            observer.on_model_changed(event, data);
        }
    }

    @Override
    public void add_observer(GameObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void remove_observer(GameObserver observer) {
        observers.remove(observer);
    }

    // MANDATORY: Getters and setters
    public String get_name() { return name; }
    public CharacterClass get_character_class() { return characterClass; }
    public int get_level() { return level; }
    public int get_experience() { return experience; }
    public Position get_position() { return position; }
    public int get_current_hp() { return currentHp; }
    public int get_max_hp() { return maxHp; }
    public float get_base_atk() { return baseAtk; }
    public int get_current_mp() { return currentMp; }
    public int get_max_mp() { return maxMp; }
    public Weapon get_equipped_weapon() { return equippedWeapon; }
    public Armor get_equipped_armor() { return equippedArmor; }

    public void set_equipped_weapon(Weapon weapon) {
        this.equippedWeapon = weapon;
        notify_observers("WEAPON_EQUIPPED", weapon);
    }

    public void set_equipped_armor(Armor armor) {
        this.equippedArmor = armor;
        notify_observers("ARMOR_EQUIPPED", armor);
    }
} 