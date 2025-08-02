## 08-enemy-boss-classes.md

# CRITICAL REQUIREMENTS - Enemy and Boss Classes Implementation

### MANDATORY DIRECTIVE ###
You are an expert Java game developer. **CRITICAL**: Implement the Enemy and Boss classes exactly as specified below for the Mini Rogue Demo combat system.

## ENEMY AND BOSS CLASS SPECIFICATIONS

### **CRITICAL**: Create Enemy.java in model/characters package with this exact implementation:

```

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

/**

* Enemy character class extending Character.
* Handles AI behavior, loot drops, and combat patterns for dungeon enemies.
*/
public class Enemy extends Character {

// MANDATORY: Enemy-specific attributes
private String aiPattern;
private List<String> weaknesses;
private List<Item> lootTable;
private int experienceValue;
private Random random;
private int aggroRange;
private boolean isAggressive;

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

// Scale enemy stats based on difficulty
scale_enemy_stats();
initialize_enemy_equipment();
initialize_loot_table();
initialize_weaknesses();

// Calculate experience value
this.experienceValue = calculate_experience_value();
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
switch (characterClass) {
case WARRIOR:
this.equippedWeapon = new Weapon("Rusty Sword", "An old, worn blade", 10, 0, characterClass);
this.equippedArmor = new Armor("Chain Mail", "Heavy protective armor", 0, 6, 1, characterClass);
break;
case MAGE:
this.equippedWeapon = new Weapon("Dark Staff", "A staff crackling with energy", 6, 8, characterClass);
this.equippedArmor = new Armor("Dark Robes", "Mystical protective garments", 0, 2, 6, characterClass);
break;
case ROGUE:
this.equippedWeapon = new Weapon("Curved Blade", "A wickedly sharp curved sword", 9, 3, characterClass);
this.equippedArmor = new Armor("Shadow Cloak", "Light armor for stealth", 0, 3, 2, characterClass);
break;
}
}

/**
    * MANDATORY: Initialize loot drop table
*/
private void initialize_loot_table() {
// Common drops
lootTable.add(new Consumable("Health Potion", "Restores 25 HP", 25, "health"));
lootTable.add(new Consumable("Mana Potion", "Restores 15 MP", 15, "mana"));

// Class-specific equipment drops (rare)
switch (characterClass) {
case WARRIOR:
if (random.nextInt(100) < 20) {
lootTable.add(new Weapon("Battle Axe", "Heavy two-handed weapon", 20, 0, characterClass));
}
break;
case MAGE:
if (random.nextInt(100) < 20) {
lootTable.add(new Weapon("Crystal Staff", "Staff of pure magical energy", 10, 15, characterClass));
}
break;
case ROGUE:
if (random.nextInt(100) < 20) {
lootTable.add(new Weapon("Shadow Blade", "Blade that strikes from darkness", 15, 8, characterClass));
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
int atkBonus = baseAtk * 2;
return baseExp + hpBonus + atkBonus;
}

/**
    * MANDATORY: Perform AI behavior based on pattern
*/
public void perform_ai() {
switch (aiPattern.toLowerCase()) {
case "aggressive":
perform_aggressive_ai();
break;
case "defensive":
perform_defensive_ai();
break;
case "magical":
perform_magical_ai();
break;
case "sneaky":
perform_sneaky_ai();
break;
default:
perform_basic_ai();
}
}

/**
    * MANDATORY: Aggressive AI behavior - always attacks
*/
private void perform_aggressive_ai() {
// Aggressive enemies always try to attack if player is in range
notify_observers("ENEMY_ACTION", "AGGRESSIVE_MOVE");
}

/**
    * MANDATORY: Defensive AI behavior - waits and counters
*/
private void perform_defensive_ai() {
// Defensive enemies wait for player to approach
if (currentHp < maxHp * 0.3) {
// Try to heal when low on health
if (random.nextInt(100) < 40) {
heal(10);
notify_observers("ENEMY_ACTION", "HEAL");
}
}
notify_observers("ENEMY_ACTION", "DEFENSIVE_STANCE");
}

/**
    * MANDATORY: Magical AI behavior - uses MP abilities
*/
private void perform_magical_ai() {
if (currentMp >= 10 \&\& random.nextInt(100) < 60) {
// Cast spell
use_mp(10);
notify_observers("ENEMY_ACTION", "CAST_SPELL");
} else {
notify_observers("ENEMY_ACTION", "MAGICAL_ATTACK");
}
}

/**
    * MANDATORY: Sneaky AI behavior - hit and run tactics
*/
private void perform_sneaky_ai() {
if (random.nextInt(100) < 30) {
notify_observers("ENEMY_ACTION", "SNEAK_ATTACK");
} else {
notify_observers("ENEMY_ACTION", "EVASIVE_MOVE");
}
}

/**
    * MANDATORY: Basic AI behavior - random actions
*/
private void perform_basic_ai() {
String[] actions = {"ATTACK", "MOVE", "WAIT"};
String action = actions[random.nextInt(actions.length)];
notify_observers("ENEMY_ACTION", action);
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
if (droppedItems.isEmpty() \&\& !lootTable.isEmpty()) {
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

// Apply AI pattern modifiers
damage = apply_ai_attack_modifier(damage);

// Small chance to miss
if (random.nextInt(100) < GameConstants.MISS_CHANCE) {
damage = 0;
notify_observers("ATTACK_MISSED", target);
}

notify_observers("ENEMY_ATTACKED", target);
return damage;
}

/**
    * MANDATORY: Apply AI-specific attack modifiers
    * 
    * @param baseDamage Base damage before modifiers
    * @return Modified damage
*/
private int apply_ai_attack_modifier(int baseDamage) {
switch (aiPattern.toLowerCase()) {
case "aggressive":
return (int)(baseDamage * 1.1); // 10% damage boost
case "defensive":
return (int)(baseDamage * 0.8); // 20% damage reduction
case "magical":
if (currentMp >= 5) {
use_mp(5);
return (int)(baseDamage * 1.3); // Magical damage boost
}
break;
case "sneaky":
if (random.nextInt(100) < 15) {
return baseDamage * 2; // Sneak attack
}
break;
}
return baseDamage;
}

/**
    * MANDATORY: Enemy turn processing with AI
*/
@Override
public void perform_turn() {
if (is_alive()) {
perform_ai();
}
}

// MANDATORY: Getters
public String get_ai_pattern() { return aiPattern; }
public List<String> get_weaknesses() { return new ArrayList<>(weaknesses); }
public int get_experience_value() { return experienceValue; }
public int get_aggro_range() { return aggroRange; }
public boolean is_aggressive() { return isAggressive; }
}

```

### **CRITICAL**: Create Boss.java in model/characters package with this exact implementation:

```

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

/**

* Boss character class extending Enemy.
* Represents powerful floor-ending enemies with special abilities and enhanced rewards.
*/
public class Boss extends Enemy {

// MANDATORY: Boss-specific attributes
private List<String> specialAbilities;
private int abilityPhaseThreshold;
private boolean inSpecialPhase;
private int originalMaxHp;

/**
    * MANDATORY: Constructor for Boss
    * 
    * @param name Boss name
    * @param characterClass Boss combat class
    * @param position Initial spawn position
*/
public Boss(String name, CharacterClass characterClass, Position position) {
super(name, characterClass, position, "boss");
this.specialAbilities = new ArrayList<>();
this.inSpecialPhase = false;
this.abilityPhaseThreshold = (int)(maxHp * 0.5); // Activate at 50% HP
this.originalMaxHp = maxHp;

// Enhance boss statistics
enhance_boss_stats();
initialize_boss_equipment();
initialize_special_abilities();
enhance_loot_table();
}

/**
    * MANDATORY: Enhance boss statistics for increased challenge
*/
private void enhance_boss_stats() {
// Bosses are significantly stronger than regular enemies
this.maxHp = (int)(maxHp * 3.0); // 3x health
this.currentHp = maxHp;
this.baseAtk = (int)(baseAtk * 2.0); // 2x attack
this.maxMp = (int)(maxMp * 2.5); // 2.5x mana
this.currentMp = maxMp;

// Update experience value for enhanced stats
this.experienceValue = calculate_boss_experience();
}

/**
    * MANDATORY: Initialize boss-tier equipment
*/
private void initialize_boss_equipment() {
switch (characterClass) {
case WARRIOR:
this.equippedWeapon = new Weapon("Legendary Blade", "A weapon of immense power", 35, 0, characterClass);
this.equippedArmor = new Armor("Plate Armor", "Masterwork protective gear", 0, 15, 3, characterClass);
break;
case MAGE:
this.equippedWeapon = new Weapon("Staff of Power", "Channels devastating magic", 20, 30, characterClass);
this.equippedArmor = new Armor("Arcane Robes", "Robes woven with protective spells", 0, 8, 20, characterClass);
break;
case ROGUE:
this.equippedWeapon = new Weapon("Shadow Fang", "Blade that drinks light itself", 28, 15, characterClass);
this.equippedArmor = new Armor("Assassin's Garb", "Armor that bends shadow", 0, 10, 8, characterClass);
break;
}
}

/**
    * MANDATORY: Initialize boss special abilities
*/
private void initialize_special_abilities() {
switch (characterClass) {
case WARRIOR:
specialAbilities.add("BERSERKER_RAGE");
specialAbilities.add("WHIRLWIND_ATTACK");
specialAbilities.add("DEFENSIVE_STANCE");
break;
case MAGE:
specialAbilities.add("METEOR_STORM");
specialAbilities.add("MANA_DRAIN");
specialAbilities.add("TELEPORT_STRIKE");
break;
case ROGUE:
specialAbilities.add("SHADOW_CLONE");
specialAbilities.add("POISON_CLOUD");
specialAbilities.add("VANISH");
break;
}
}

/**
    * MANDATORY: Enhance loot table with boss rewards
*/
private void enhance_loot_table() {
// Clear basic loot and add boss-tier rewards
lootTable.clear();

// High-tier consumables
lootTable.add(new Consumable("Greater Health Potion", "Restores 100 HP", 100, "health"));
lootTable.add(new Consumable("Greater Mana Potion", "Restores 75 MP", 75, "mana"));

// Boss-tier equipment (guaranteed drops)
switch (characterClass) {
case WARRIOR:
lootTable.add(new Weapon("Champion's Sword", "Blade of a fallen hero", 30, 0, characterClass));
lootTable.add(new Armor("Knight's Plate", "Armor blessed by light", 0, 12, 5, characterClass));
break;
case MAGE:
                lootTable.add(new Weapon("Arcane Wand", "Wand of pure magical energy", 12, 20, characterClass));
                lootTable.add(new Armor("Mystic Robes", "Robes imbued with protective magic", 0, 6, 15, characterClass));
                break;
            case ROGUE:
                lootTable.add(new Weapon("Assassin's Blade", "Blade forged in shadow", 25, 10, characterClass));
                lootTable.add(new Armor("Shadow Mail", "Armor that bends with darkness", 0, 8, 12, characterClass));
                break;
        }
        
        // Special upgrade materials
        lootTable.add(new KeyItem("Upgrade Crystal", "Crystal used for equipment enhancement", characterClass.get_class_name()));
        lootTable.add(new Consumable("Boss Elixir", "Powerful restoration potion", 150, "health"));
    }
    
    /**
     * MANDATORY: Calculate boss experience value
     * 
     * @return Experience points for defeating this boss
     */
    private int calculate_boss_experience() {
        return experienceValue * 5; // Bosses give 5x normal experience
    }
    
    /**
     * MANDATORY: Use special ability based on current HP
     */
    public void use_special_ability() {
        if (currentHp <= abilityPhaseThreshold && !inSpecialPhase) {
            inSpecialPhase = true;
            notify_observers("BOSS_PHASE_CHANGE", "Special abilities activated");
        }
        
        if (inSpecialPhase && !specialAbilities.isEmpty()) {
            String ability = specialAbilities.get(random.nextInt(specialAbilities.size()));
            execute_special_ability(ability);
        }
    }
    
    /**
     * MANDATORY: Execute specific special ability
     * 
     * @param ability The special ability to execute
     */
    private void execute_special_ability(String ability) {
        switch (ability) {
            case "BERSERKER_RAGE":
                // Increase attack power temporarily
                baseAtk = (int)(baseAtk * 1.5);
                notify_observers("BOSS_ABILITY", "Berserker Rage activated - ATK increased!");
                break;
            case "WHIRLWIND_ATTACK":
                // Area attack ability
                notify_observers("BOSS_ABILITY", "Whirlwind Attack - Area damage incoming!");
                break;
            case "DEFENSIVE_STANCE":
                // Reduce incoming damage
                notify_observers("BOSS_ABILITY", "Defensive Stance - Damage reduction active!");
                break;
            case "METEOR_STORM":
                // High magic damage
                if (use_mp(20)) {
                    notify_observers("BOSS_ABILITY", "Meteor Storm - Massive magic damage!");
                }
                break;
            case "MANA_DRAIN":
                // Drain player MP
                notify_observers("BOSS_ABILITY", "Mana Drain - Player MP reduced!");
                break;
            case "TELEPORT_STRIKE":
                // Instant teleport attack
                if (use_mp(15)) {
                    notify_observers("BOSS_ABILITY", "Teleport Strike - Instant attack!");
                }
                break;
            case "SHADOW_CLONE":
                // Create temporary duplicates
                notify_observers("BOSS_ABILITY", "Shadow Clone - Duplicates created!");
                break;
            case "POISON_CLOUD":
                // Area poison effect
                notify_observers("BOSS_ABILITY", "Poison Cloud - Toxic damage over time!");
                break;
            case "VANISH":
                // Temporary invisibility
                notify_observers("BOSS_ABILITY", "Vanish - Boss becomes harder to hit!");
                break;
        }
    }
    
    /**
     * MANDATORY: Override perform_turn for boss AI
     */
    @Override
    public void perform_turn() {
        if (is_alive()) {
            if (random.nextInt(100) < 40) { // 40% chance for special ability
                use_special_ability();
            } else {
                perform_ai();
            }
        }
    }
    
    /**
     * MANDATORY: Check if boss is defeated
     * 
     * @return true if boss HP is 0 or below
     */
    public boolean is_defeated() {
        return currentHp <= 0;
    }
    
    // MANDATORY: Additional getters
    public List<String> get_special_abilities() {
        return new ArrayList<>(specialAbilities);
    }
    
    public boolean is_in_special_phase() {
        return inSpecialPhase;
    }
    
    public int get_ability_phase_threshold() {
        return abilityPhaseThreshold;
    }
}
```


### **MANDATORY**: Boss Class Requirements

1. **CRITICAL**: Boss class must extend Enemy class
2. **CRITICAL**: Enhanced statistics (3x HP, 2x ATK, 2.5x MP)
3. **CRITICAL**: Special abilities triggered at 50% HP threshold
4. **CRITICAL**: Boss-tier equipment and enhanced loot tables
5. **CRITICAL**: 5x experience reward for increased challenge

### **MANDATORY**: Special Abilities System

**CRITICAL**: Class-specific abilities:

- **Warrior**: Berserker Rage, Whirlwind Attack, Defensive Stance
- **Mage**: Meteor Storm, Mana Drain, Teleport Strike
- **Rogue**: Shadow Clone, Poison Cloud, Vanish


### **MANDATORY**: Verification Steps

1. **CRITICAL**: Both Enemy and Boss classes compile in model/characters package
2. **CRITICAL**: AI patterns execute correctly for different enemy types
3. **CRITICAL**: Boss special abilities activate at appropriate HP threshold
4. **CRITICAL**: Loot drop system generates appropriate rewards
5. **CRITICAL**: Weakness system affects damage calculations properly
6. **CRITICAL**: Boss statistics provide appropriate challenge scaling

### CRITICAL REQUIREMENT

**MANDATORY**: The Enemy and Boss classes must be implemented exactly as specified above. These classes provide the combat encounters and challenge progression essential to the roguelike experience.

```