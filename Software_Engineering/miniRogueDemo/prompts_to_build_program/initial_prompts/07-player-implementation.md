## 07-player-implementation.md

# CRITICAL REQUIREMENTS - Player Class Implementation

### MANDATORY DIRECTIVE ###
You are an expert Java game developer. **CRITICAL**: Implement the Player class exactly as specified below for the Mini Rogue Demo player character system.

## PLAYER CLASS SPECIFICATION

### **CRITICAL**: Create Player.java in model/characters package with this exact implementation:

```

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

/**

* Player character class extending Character.
* Handles player-specific functionality including inventory, leveling, and class selection.
*/
public class Player extends Character {

// MANDATORY: Player-specific attributes
private List<Item> inventory;
private int inventoryCapacity;
private String selectedClass;
private Random random;

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
this.inventoryCapacity = GameConstants.UNLIMITED_INVENTORY;
this.selectedClass = characterClass.get_class_name();
this.random = new Random();

// Initialize class-specific starting equipment
initialize_starting_equipment();
}

/**
    * MANDATORY: Initialize starting equipment based on character class
*/
private void initialize_starting_equipment() {
switch (characterClass) {
case WARRIOR:
// Warrior starts with basic sword and leather armor
this.equippedWeapon = new Weapon("Iron Sword", "A sturdy iron blade", 15, 0, characterClass);
this.equippedArmor = new Armor("Leather Armor", "Basic protective gear", 0, 5, 2, characterClass);
break;
case MAGE:
// Mage starts with staff and robes
this.equippedWeapon = new Weapon("Oak Staff", "A magical focusing staff", 8, 10, characterClass);
this.equippedArmor = new Armor("Cloth Robes", "Light magical robes", 0, 2, 8, characterClass);
break;
case ROGUE:
// Rogue starts with dagger and light armor
this.equippedWeapon = new Weapon("Steel Dagger", "A sharp, lightweight blade", 12, 5, characterClass);
this.equippedArmor = new Armor("Studded Leather", "Flexible protective wear", 0, 4, 3, characterClass);
break;
}

// Add starting consumables
add_starting_items();
}

/**
    * MANDATORY: Add starting items to inventory
*/
private void add_starting_items() {
// Add health potions
collect_item(new Consumable("Health Potion", "Restores 50 HP", 50, "health"));
collect_item(new Consumable("Health Potion", "Restores 50 HP", 50, "health"));

// Add mana potions for magic users
if (characterClass == CharacterClass.MAGE || characterClass == CharacterClass.ROGUE) {
collect_item(new Consumable("Mana Potion", "Restores 30 MP", 30, "mana"));
}
}

/**
    * MANDATORY: Select character class (used during character creation)
    * 
    * @param classType The CharacterClass to select
*/
public void select_class(CharacterClass classType) {
this.characterClass = classType;
this.selectedClass = classType.get_class_name();
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

if (inventoryCapacity == GameConstants.UNLIMITED_INVENTORY ||
inventory.size() < inventoryCapacity) {
inventory.add(item);
notify_observers("ITEM_COLLECTED", item);
return true;
}
return false; // Inventory full
}

/**
    * MANDATORY: Use an item from inventory
    * 
    * @param item The Item to use
    * @return true if item was successfully used
*/
public boolean use_item(Item item) {
if (!inventory.contains(item)) return false;

boolean used = item.use(this);
if (used) {
inventory.remove(item);
notify_observers("ITEM_USED", item);
}
return used;
}

/**
    * MANDATORY: Open inventory interface
*/
public void open_inventory() {
notify_observers("INVENTORY_OPENED", inventory);
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
return !upgradeItems.isEmpty() \&\&
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
if (item.get_name().contains("Upgrade") \&\&
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
this.experience += expGained;
notify_observers("EXPERIENCE_GAINED", expGained);

// Check for level up
int requiredExp = Stats.calculate_experience_required(level);
if (experience >= requiredExp) {
level_up();
}
}

/**
    * MANDATORY: Handle level up progression
*/
private void level_up() {
if (level >= GameConstants.MAX_LEVEL) return;

level++;

// Increase stats based on class
int hpGain = Stats.calculate_hp_per_level(characterClass, level);
int atkGain = Stats.calculate_atk_per_level(characterClass, level);
int mpGain = Stats.calculate_mp_per_level(characterClass, level);

maxHp += hpGain;
currentHp = maxHp; // Full heal on level up
baseAtk += atkGain;
maxMp += mpGain;
currentMp = maxMp; // Full MP restore on level up

// Reset experience for next level
experience = 0;

notify_observers("LEVEL_UP", level);
}

/**
    * MANDATORY: Implement attack behavior for player
    * 
    * @param target The character to attack
    * @return Damage dealt
*/
@Override
public int attack(Character target) {
int damage = get_total_attack();

// Check for critical hit
int critChance = Stats.calculate_critical_chance(characterClass);
if (random.nextInt(100) < critChance) {
damage = (int)(damage * GameConstants.CRITICAL_HIT_MULTIPLIER);
notify_observers("CRITICAL_HIT", damage);
}

// Check for miss
if (random.nextInt(100) < GameConstants.MISS_CHANCE) {
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
if (random.nextInt(100) < 25) {
notify_observers("SNEAK_ATTACK", target);
return baseDamage * 2;
}
break;
}
return baseDamage;
}

/**
    * MANDATORY: Player turn processing (handled by controller input)
*/
@Override
public void perform_turn() {
// Player turns are handled by user input through controller
// This method exists to fulfill abstract contract
notify_observers("PLAYER_TURN_START", this);
}

// MANDATORY: Additional getters
public List<Item> get_inventory() {
return new ArrayList<>(inventory); // Defensive copy
}

public int get_inventory_size() {
return inventory.size();
}

public String get_selected_class() {
return selectedClass;
}

public boolean has_item(String itemName) {
return inventory.stream().anyMatch(item -> item.get_name().equals(itemName));
}

public List<Item> get_consumables() {
return inventory.stream()
.filter(item -> item instanceof Consumable)
.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
}
}

```

### **MANDATORY**: Player Class Requirements

1. **CRITICAL**: Player class must extend Character abstract class
2. **CRITICAL**: Inventory system with unlimited capacity
3. **CRITICAL**: Class-specific starting equipment and abilities
4. **CRITICAL**: Experience gain and automatic leveling system
5. **CRITICAL**: Equipment upgrade mechanics using inventory items

### **MANDATORY**: Design Principles

**CRITICAL**: Player class implements:
- **Inventory Management**: Collection, usage, and organization of items
- **Character Progression**: Level-based stat increases and experience tracking
- **Equipment System**: Weapon and armor management with upgrades
- **Class Specialization**: Unique abilities and bonuses per character class

### **MANDATORY**: Combat Specialization

**CRITICAL**: Class-specific features:
- **Warrior**: Armor-piercing attacks, high durability
- **Mage**: MP-powered magical attacks, spell critical hits
- **Rogue**: Sneak attacks, high critical hit chance, evasion

### **MANDATORY**: Verification Steps

1. **CRITICAL**: Player class compiles in model/characters package
2. **CRITICAL**: Inventory operations work correctly (add, remove, use)
3. **CRITICAL**: Level progression and stat scaling function properly
4. **CRITICAL**: Class selection initializes appropriate equipment
5. **CRITICAL**: Equipment upgrade system processes key items
6. **CRITICAL**: Combat abilities apply class-specific bonuses

### CRITICAL REQUIREMENT ###
**MANDATORY**: The Player class must be implemented exactly as specified above. This class handles all player-specific functionality including progression, inventory management, and class-based combat abilities.
```