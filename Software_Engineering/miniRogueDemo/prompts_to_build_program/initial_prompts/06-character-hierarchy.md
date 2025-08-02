## 06-character-hierarchy.md

# CRITICAL REQUIREMENTS - Character Hierarchy Implementation

### MANDATORY DIRECTIVE ###
You are an expert Java game developer. **CRITICAL**: Implement the Character hierarchy classes exactly as specified below for the Mini Rogue Demo character system.

## CHARACTER HIERARCHY SPECIFICATIONS

### **CRITICAL**: Create Character.java in model/characters package with this exact implementation:

```

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
protected int baseAtk;
protected int currentMp;
protected int maxMp;

// MANDATORY: Equipment
protected Weapon equippedWeapon;
protected Armor equippedArmor;

// MANDATORY: Observer pattern implementation
protected List<GameObserver> observers;

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

// Initialize stats based on class
initialize_stats();
}

/**
    * MANDATORY: Initialize character statistics based on class
*/
protected void initialize_stats() {
this.maxHp = characterClass.get_base_hp();
this.currentHp = maxHp;
this.baseAtk = characterClass.get_base_atk();
this.maxMp = characterClass.get_base_mp();
this.currentMp = maxMp;
}

/**
    * MANDATORY: Move character to new position
    * 
    * @param newPosition The target position
*/
public void move_to(Position newPosition) {
Position oldPosition = this.position;
this.position = newPosition;
notify_observers("CHARACTER_MOVED", this);
}

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
int weaponAttack = (equippedWeapon != null) ? equippedWeapon.get_attack_power() : 0;
return baseAtk + weaponAttack;
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

/**
    * MANDATORY: Abstract method for character-specific AI or input handling
*/
public abstract void perform_turn();

// MANDATORY: Observer pattern implementation
@Override
public void notify_observers(String event, Object data) {
for (GameObserver observer : observers) {
observer.on_model_changed(event, data);
}
}

@Override
public void add_observer(GameObserver observer) {
if (observer != null \&\& !observers.contains(observer)) {
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
public int get_base_atk() { return baseAtk; }
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

```

### **CRITICAL**: Create Stats.java in model/characters package with this exact implementation:

```

package model.characters;

/**

* Utility class for managing character statistics and calculations.
* Provides methods for stat modifications and level-based scaling.
*/
public final class Stats {

/**
    * MANDATORY: Calculate HP gain per level for character class
    * 
    * @param characterClass The character's class
    * @param level Current level
    * @return HP increase for this level
*/
public static int calculate_hp_per_level(enums.CharacterClass characterClass, int level) {
int baseGain;
switch (characterClass) {
case WARRIOR:
baseGain = 12;
break;
case MAGE:
baseGain = 6;
break;
case ROGUE:
baseGain = 8;
break;
default:
baseGain = 8;
}
return baseGain + (level / 5); // Slight scaling with level
}

/**
    * MANDATORY: Calculate ATK gain per level for character class
    * 
    * @param characterClass The character's class
    * @param level Current level
    * @return ATK increase for this level
*/
public static int calculate_atk_per_level(enums.CharacterClass characterClass, int level) {
int baseGain;
switch (characterClass) {
case WARRIOR:
baseGain = 3;
break;
case MAGE:
baseGain = 2;
break;
case ROGUE:
baseGain = 2;
break;
default:
baseGain = 2;
}
return baseGain + (level / 10); // Slight scaling with level
}

/**
    * MANDATORY: Calculate MP gain per level for character class
    * 
    * @param characterClass The character's class
    * @param level Current level
    * @return MP increase for this level
*/
public static int calculate_mp_per_level(enums.CharacterClass characterClass, int level) {
int baseGain;
switch (characterClass) {
case WARRIOR:
baseGain = 2;
break;
case MAGE:
baseGain = 8;
break;
case ROGUE:
baseGain = 4;
break;
default:
baseGain = 4;
}
return baseGain + (level / 5); // Slight scaling with level
}

/**
    * MANDATORY: Calculate experience required for next level
    * 
    * @param currentLevel The character's current level
    * @return Experience points needed for next level
*/
public static int calculate_experience_required(int currentLevel) {
return enums.GameConstants.BASE_EXPERIENCE +
(currentLevel * enums.GameConstants.EXPERIENCE_MULTIPLIER);
}

/**
    * MANDATORY: Calculate critical hit chance based on character class
    * 
    * @param characterClass The character's class
    * @return Critical hit chance as percentage
*/
public static int calculate_critical_chance(enums.CharacterClass characterClass) {
switch (characterClass) {
case WARRIOR:
return 8; // 8%
case MAGE:
return 12; // 12% (spell crits)
case ROGUE:
return 20; // 20% (high crit)
default:
return enums.GameConstants.CRITICAL_HIT_CHANCE;
}
}

private Stats() {
// Prevent instantiation
throw new UnsupportedOperationException("Utility class cannot be instantiated");
}
}

```

### **MANDATORY**: Class Design Requirements

1. **CRITICAL**: Character class must be abstract with concrete implementations
2. **CRITICAL**: Stats class must be final utility class with static methods
3. **CRITICAL**: Observer pattern implementation for stat changes
4. **CRITICAL**: Equipment integration with attack/defense calculations
5. **CRITICAL**: Level-based stat scaling and progression

### **MANDATORY**: Design Principles

**CRITICAL**: These classes implement:
- **Template Method Pattern**: Abstract Character with specific implementations
- **Observer Pattern**: Notifications for stat changes
- **Strategy Pattern**: Class-specific stat calculations
- **State Pattern**: Character state management (HP, MP, equipment)

### **MANDATORY**: Combat Integration

**CRITICAL**: Combat features:
- **Damage Calculation**: Attack minus defense mechanics
- **Critical Hits**: Class-based critical hit chances
- **MP Usage**: Ability cost management
- **Equipment Bonuses**: Weapon and armor stat modifications

### **MANDATORY**: Verification Steps

1. **CRITICAL**: Character class compiles in model/characters package
2. **CRITICAL**: Stats utility class provides correct calculations
3. **CRITICAL**: Observer pattern notifications work properly
4. **CRITICAL**: Equipment integration affects combat stats
5. **CRITICAL**: Level progression calculations are balanced
6. **CRITICAL**: Abstract methods enforce subclass implementation

### CRITICAL REQUIREMENT ###
**MANDATORY**: The Character hierarchy must be implemented exactly as specified above. This provides the foundation for all character types in the Mini Rogue Demo with proper stat management and combat integration.
```