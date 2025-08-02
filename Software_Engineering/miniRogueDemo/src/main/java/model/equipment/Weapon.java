package model.equipment;

import enums.CharacterClass;
import model.characters.Character;

/**
 * Weapon equipment that increases character attack power.
 * Can also provide MP bonuses for magical weapons.
 */
public class Weapon extends Equipment {
    private int mpPower; // Additional MP provided by weapon
    
    // NEW: Weapon type system
    private WeaponType weaponType;

    /**
     * Weapon types enum
     */
    public enum WeaponType {
        BLADE,      // Rogue
        DISTANCE,   // Ranger
        IMPACT,     // Warrior
        MAGIC       // Mage
    }

    /**
     * MANDATORY: Constructor for Weapon
     *
     * @param name Weapon name
     * @param atkPower Attack power bonus
     * @param mpPower MP power bonus
     * @param classType Required character class
     * @param tier Equipment tier (1-3)
     * @param weaponType Type of weapon (BLADE/DISTANCE/IMPACT/MAGIC)
     * @param imagePath Path to the weapon icon image
     * @param equipmentTypeDesignation Type designation (Blade, Distance, Impact, Magic)
     */
    public Weapon(String name, int atkPower, int mpPower, CharacterClass classType, int tier, WeaponType weaponType, String imagePath, String equipmentTypeDesignation) {
        super(name, atkPower, classType, tier, EquipmentType.WEAPON, imagePath, equipmentTypeDesignation);
        this.mpPower = mpPower;
        this.weaponType = weaponType;
        // Re-initialize stat modifiers now that weaponType is set
        initializeStatModifiers();
    }

    /**
     * NEW: Initialize stat modifiers for weapons
     */
    @Override
    protected void initializeStatModifiers() {
        float tierMultiplier = getTierMultiplier();
        
        // Each weapon type gets exactly ONE positive and ONE negative stat modifier
        // These should match the actual game effects from get_attack_power()
        switch (weaponType) {
            case BLADE:
                statModifiers.put("Attack", 3.0f * tierMultiplier); // Positive attack bonus
                statModifiers.put("Speed", -2.0f * getTierPenaltyMultiplier()); // Negative speed penalty
                break;
            case DISTANCE:
                statModifiers.put("Range", 2.0f * tierMultiplier); // Positive range bonus
                statModifiers.put("Attack", -1.5f * getTierPenaltyMultiplier()); // Negative attack penalty
                break;
            case IMPACT:
                statModifiers.put("Attack", 4.0f * tierMultiplier); // Positive attack bonus
                statModifiers.put("Speed", -3.0f * getTierPenaltyMultiplier()); // Negative speed penalty
                break;
            case MAGIC:
                statModifiers.put("Mana", 5.0f * tierMultiplier); // Positive mana bonus
                statModifiers.put("Attack", -2.0f * getTierPenaltyMultiplier()); // Negative attack penalty
                break;
        }
    }

    /**
     * NEW: Apply weapon stat modifiers to character
     */
    @Override
    public void applyStatModifiers(Character character) {
        // This will be implemented when we add stat modification system to Character
        // For now, we'll just notify observers
        character.notify_observers("WEAPON_MODIFIERS_APPLIED", this);
    }

    /**
     * NEW: Remove weapon stat modifiers from character
     */
    @Override
    public void removeStatModifiers(Character character) {
        // This will be implemented when we add stat modification system to Character
        character.notify_observers("WEAPON_MODIFIERS_REMOVED", this);
    }

    /**
     * NEW: Check if weapon is compatible with character class
     */
    public boolean isCompatibleWithClass(CharacterClass characterClass) {
        switch (characterClass) {
            case ROGUE:
                return weaponType == WeaponType.BLADE;
            case RANGER:
                return weaponType == WeaponType.DISTANCE;
            case WARRIOR:
                return weaponType == WeaponType.IMPACT;
            case MAGE:
                return weaponType == WeaponType.MAGIC;
            default:
                return false;
        }
    }

    /**
     * MANDATORY: Get weapon attack power
     *
     * @return Current attack power including upgrades
     */
    public int get_attack_power() {
        // Get the Attack modifier from stat modifiers
        Float attackModifier = statModifiers.get("Attack");
        if (attackModifier != null) {
            return attackModifier.intValue();
        }
        return 0; // No attack modifier
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
        stats.append(", Type: ").append(weaponType);
        stats.append(", Tier: ").append(tier);
        return stats.toString();
    }

    // NEW: Getters for weapon type
    public WeaponType get_weapon_type() { return weaponType; }

    @Override
    public String toString() {
        return super.toString() + " [" + get_stats() + "]";
    }
} 