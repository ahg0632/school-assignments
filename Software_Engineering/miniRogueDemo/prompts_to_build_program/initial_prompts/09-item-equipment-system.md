## 09-item-equipment-system.md
```
# CRITICAL REQUIREMENTS - Item and Equipment System Implementation

### MANDATORY DIRECTIVE

You are an expert Java game developer. **CRITICAL**: Implement the Item hierarchy and Equipment system exactly as specified below for the Mini Rogue Demo.

## ITEM HIERARCHY SPECIFICATIONS

### **CRITICAL**: Create Item.java in model/items package with this exact implementation:

```
package model.items;

import enums.CharacterClass;
import model.characters.Character;

/**
 * Abstract base class for all items in the Mini Rogue Demo.
 * Provides common attributes and behavior for consumables, key items, and equipment.
 */
public abstract class Item {
    
    // MANDATORY: Core item attributes
    protected String name;
    protected String description;
    protected int potency;
    protected CharacterClass classType;
    
    /**
     * MANDATORY: Constructor for Item
     * 
     * @param name Item's display name
     * @param description Item's description text
     * @param potency Item's power/effectiveness value
     * @param classType Character class this item is designed for
     */
    public Item(String name, String description, int potency, CharacterClass classType) {
        this.name = name;
        this.description = description;
        this.potency = potency;
        this.classType = classType;
    }
    
    /**
     * MANDATORY: Constructor for class-neutral items
     * 
     * @param name Item's display name
     * @param description Item's description text
     * @param potency Item's power/effectiveness value
     */
    public Item(String name, String description, int potency) {
        this.name = name;
        this.description = description;
        this.potency = potency;
        this.classType = null; // Usable by any class
    }
    
    /**
     * MANDATORY: Use the item on a character
     * 
     * @param character The character using the item
     * @return true if item was successfully used
     */
    public abstract boolean use(Character character);
    
    /**
     * MANDATORY: Check if item can be used by character class
     * 
     * @param characterClass The class to check compatibility with
     * @return true if character can use this item
     */
    public boolean is_usable_by(CharacterClass characterClass) {
        return classType == null || classType == characterClass;
    }
    
    // MANDATORY: Getters
    public String get_name() { return name; }
    public String get_description() { return description; }
    public int get_potency() { return potency; }
    public CharacterClass get_class_type() { return classType; }
    
    @Override
    public String toString() {
        String classInfo = (classType != null) ? " (" + classType.get_class_name() + ")" : "";
        return name + classInfo + " - " + description;
    }
}
```


### **CRITICAL**: Create Consumable.java in model/items package with this exact implementation:

```
package model.items;

import enums.CharacterClass;
import model.characters.Character;

/**
 * Consumable items that provide immediate effects when used.
 * Items are consumed upon use and removed from inventory.
 */
public class Consumable extends Item {
    
    private String effectType; // "health", "mana", "experience"
    
    /**
     * MANDATORY: Constructor for Consumable
     * 
     * @param name Item name
     * @param description Item description
     * @param potency Effect strength
     * @param effectType Type of effect ("health", "mana", "experience")
     */
    public Consumable(String name, String description, int potency, String effectType) {
        super(name, description, potency);
        this.effectType = effectType;
    }
    
    /**
     * MANDATORY: Use consumable on character
     * 
     * @param character The character to apply effects to
     * @return true if item was consumed
     */
    @Override
    public boolean use(Character character) {
        if (character == null || !character.is_alive()) {
            return false;
        }
        
        switch (effectType.toLowerCase()) {
            case "health":
                character.heal(potency);
                return true;
            case "mana":
                character.restore_mp(potency);
                return true;
            case "experience":
                if (character instanceof model.characters.Player) {
                    ((model.characters.Player) character).gain_experience(potency);
                    return true;
                }
                break;
            default:
                return false;
        }
        
        return false;
    }
    
    public String get_effect_type() {
        return effectType;
    }
    
    @Override
    public String toString() {
        return super.toString() + " (+" + potency + " " + effectType + ")";
    }
}
```


### **CRITICAL**: Create KeyItem.java in model/items package with this exact implementation:

```
package model.items;

import enums.CharacterClass;
import model.characters.Character;
import model.equipment.Equipment;

/**
 * Special items used for upgrading equipment.
 * Key items are consumed when used for upgrades.
 */
public class KeyItem extends Item {
    
    private String upgradeType; // Type of equipment this can upgrade
    
    /**
     * MANDATORY: Constructor for KeyItem
     * 
     * @param name Item name
     * @param description Item description
     * @param upgradeType Type of upgrade ("weapon", "armor", "any")
     */
    public KeyItem(String name, String description, String upgradeType) {
        super(name, description, 1); // Potency not used for key items
        this.upgradeType = upgradeType;
    }
    
    /**
     * MANDATORY: Key items cannot be used directly on characters
     * 
     * @param character The character (ignored)
     * @return false - key items are used for upgrades only
     */
    @Override
    public boolean use(Character character) {
        return false; // Key items are used for equipment upgrades, not direct use
    }
    
    /**
     * MANDATORY: Check if this key item can upgrade specific equipment
     * 
     * @param equipment The equipment to check upgrade compatibility
     * @return true if this key item can upgrade the equipment
     */
    public boolean can_upgrade(Equipment equipment) {
        if (equipment == null) return false;
        
        // Check class compatibility
        if (!is_usable_by(equipment.get_class_type())) {
            return false;
        }
        
        // Check upgrade type compatibility
        switch (upgradeType.toLowerCase()) {
            case "weapon":
                return equipment instanceof model.equipment.Weapon;
            case "armor":
                return equipment instanceof model.equipment.Armor;
            case "any":
                return true;
            default:
                return false;
        }
    }
    
    public String get_upgrade_type() {
        return upgradeType;
    }
    
    @Override
    public String toString() {
        return super.toString() + " (Upgrades: " + upgradeType + ")";
    }
}
```


### **CRITICAL**: Create Equipment.java in model/equipment package with this exact implementation:

```
package model.equipment;

import enums.CharacterClass;
import enums.GameConstants;
import model.items.Item;
import model.characters.Character;

/**
 * Abstract base class for all equipment items (weapons and armor).
 * Provides upgrade functionality and stat management.
 */
public abstract class Equipment extends Item {
    
    // MANDATORY: Equipment attributes
    protected int upgradeLevel;
    protected int baseStatValue;
    
    /**
     * MANDATORY: Constructor for Equipment
     * 
     * @param name Equipment name
     * @param description Equipment description
     * @param potency Base stat value
     * @param classType Character class requirement
     */
    public Equipment(String name, String description, int potency, CharacterClass classType) {
        super(name, description, potency, classType);
        this.upgradeLevel = 0;
        this.baseStatValue = potency;
    }
    
    /**
     * MANDATORY: Equipment cannot be directly "used" like consumables
     * 
     * @param character The character (ignored)
     * @return false - equipment must be equipped, not used
     */
    @Override
    public boolean use(Character character) {
        return false; // Equipment is equipped, not used
    }
    
    /**
     * MANDATORY: Upgrade equipment using key items
     * 
     * @return true if upgrade was successful
     */
    public boolean upgrade() {
        if (upgradeLevel >= GameConstants.MAX_EQUIPMENT_LEVEL) {
            return false; // Already at maximum upgrade level
        }
        
        upgradeLevel++;
        apply_upgrade_bonus();
        return true;
    }
    
    /**
     * MANDATORY: Apply stat bonuses based on upgrade level
     */
    protected void apply_upgrade_bonus() {
        // Each upgrade level increases stats by 20% of base value
        double upgradeMultiplier = 1.0 + (upgradeLevel * 0.2);
        potency = (int)(baseStatValue * upgradeMultiplier);
    }
    
    /**
     * MANDATORY: Get current equipment statistics
     * 
     * @return Equipment stats as formatted string
     */
    public abstract String get_stats();
    
    /**
     * MANDATORY: Check if equipment can be upgraded further
     * 
     * @return true if upgrade is possible
     */
    public boolean can_upgrade() {
        return upgradeLevel < GameConstants.MAX_EQUIPMENT_LEVEL;
    }
    
    // MANDATORY: Getters
    public int get_upgrade_level() { return upgradeLevel; }
    public int get_base_stat_value() { return baseStatValue; }
    
    @Override
    public String toString() {
        String upgradeInfo = (upgradeLevel > 0) ? " (+" + upgradeLevel + ")" : "";
        return super.toString() + upgradeInfo;
    }
}
```


### **CRITICAL**: Create Weapon.java in model/equipment package with this exact implementation:

```
package model.equipment;

import enums.CharacterClass;

/**
 * Weapon equipment that increases character attack power.
 * Can also provide MP bonuses for magical weapons.
 */
public class Weapon extends Equipment {
    
    private int mpPower; // Additional MP provided by weapon
    
    /**
     * MANDATORY: Constructor for Weapon
     * 
     * @param name Weapon name
     * @param description Weapon description
     * @param atkPower Attack power bonus
     * @param mpPower MP power bonus
     * @param classType Required character class
     */
    public Weapon(String name, String description, int atkPower, int mpPower, CharacterClass classType) {
        super(name, description, atkPower, classType);
        this.mpPower = mpPower;
    }
    
    /**
     * MANDATORY: Get weapon attack power
     * 
     * @return Current attack power including upgrades
     */
    public int get_attack_power() {
        return potency; // Potency represents attack power for weapons
    }
    
    /**
     * MANDATORY: Get weapon MP power
     * 
     * @return Current MP power including upgrades
     */
    public int get_mp_power() {
        // MP power also scales with upgrades
        double upgradeMultiplier = 1.0 + (upgradeLevel * 0.2);
        return (int)(mpPower * upgradeMultiplier);
    }
    
    /**
     * MANDATORY: Get weapon statistics summary
     * 
     * @return Formatted stats string
     */
    @Override
    public String get_stats() {
        StringBuilder stats = new StringBuilder();
        stats.append("ATK: ").append(get_attack_power());
        if (get_mp_power() > 0) {
            stats.append(", MP: ").append(get_mp_power());
        }
        return stats.toString();
    }
    
    @Override
    public String toString() {
        return super.toString() + " [" + get_stats() + "]";
    }
}
```


### **CRITICAL**: Create Armor.java in model/equipment package with this exact implementation:

```
package model.equipment;

import enums.CharacterClass;

/**
 * Armor equipment that provides defense against attacks.
 * Can provide both physical and magical defense.
 */
public class Armor extends Equipment {
    
    private int atkDefense; // Physical defense value
    private int mpDefense;  // Magical defense value
    
    /**
     * MANDATORY: Constructor for Armor
     * 
     * @param name Armor name
     * @param description Armor description
     * @param atkDefense Physical defense value
     * @param mpDefense Magical defense value
     * @param classType Required character class
     */
    public Armor(String name, String description, int potency, int atkDefense, int mpDefense, CharacterClass classType) {
        super(name, description, potency, classType);
        this.atkDefense = atkDefense;
        this.mpDefense = mpDefense;
    }
    
    /**
     * MANDATORY: Get total defense value (primary stat)
     * 
     * @return Current defense value including upgrades
     */
    public int get_defense_value() {
        return potency; // Potency represents total defense for armor
    }
    
    /**
     * MANDATORY: Get physical attack defense
     * 
     * @return Current attack defense including upgrades
     */
    public int get_atk_defense() {
        double upgradeMultiplier = 1.0 + (upgradeLevel * 0.2);
        return (int)(atkDefense * upgradeMultiplier);
    }
    
    /**
     * MANDATORY: Get magical attack defense
     * 
     * @return Current MP defense including upgrades
     */
    public int get_mp_defense() {
        double upgradeMultiplier = 1.0 + (upgradeLevel * 0.2);
        return (int)(mpDefense * upgradeMultiplier);
    }
    
    /**
     * MANDATORY: Get armor statistics summary
     * 
     * @return Formatted stats string
     */
    @Override
    public String get_stats() {
        StringBuilder stats = new StringBuilder();
        stats.append("DEF: ").append(get_defense_value());
        if (get_atk_defense() > 0) {
            stats.append(", ATK DEF: ").append(get_atk_defense());
        }
        if (get_mp_defense() > 0) {
            stats.append(", MP DEF: ").append(get_mp_defense());
        }
        return stats.toString();
    }
    
    @Override
    public String toString() {
        return super.toString() + " [" + get_stats() + "]";
    }
}
```


### **MANDATORY**: Item System Requirements

1. **CRITICAL**: Item hierarchy with abstract Item base class
2. **CRITICAL**: Consumables provide immediate effects (health, mana, experience)
3. **CRITICAL**: Key items enable equipment upgrades
4. **CRITICAL**: Equipment system with upgrade levels and stat scaling
5. **CRITICAL**: Class compatibility checking for all items

### **MANDATORY**: Design Principles

**CRITICAL**: Item system implements:

- **Template Method Pattern**: Abstract Item class with concrete implementations
- **Strategy Pattern**: Different item types with unique behaviors
- **Composite Pattern**: Equipment combines multiple stat bonuses
- **Observer Pattern**: Equipment changes notify characters


### **MANDATORY**: Verification Steps

1. **CRITICAL**: All classes compile successfully in respective packages
2. **CRITICAL**: Item hierarchy inheritance works correctly
3. **CRITICAL**: Consumable effects apply to characters properly
4. **CRITICAL**: Equipment upgrade system functions with key items
5. **CRITICAL**: Class compatibility restrictions enforce properly
6. **CRITICAL**: Stat scaling calculations produce reasonable values

### CRITICAL REQUIREMENT

**MANDATORY**: The Item and Equipment system must be implemented exactly as specified above. This system provides the core progression mechanics and inventory management for the Mini Rogue Demo.

```