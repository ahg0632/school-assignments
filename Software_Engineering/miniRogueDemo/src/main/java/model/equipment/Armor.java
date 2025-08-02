package model.equipment;

import enums.CharacterClass;
import model.characters.Character;

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
     * @param potency Total defense value
     * @param atkDefense Physical defense value
     * @param mpDefense Magical defense value
     * @param classType Required character class
     * @param tier Equipment tier (1-3)
     * @param imagePath Path to the armor icon image
     * @param equipmentTypeDesignation Type designation (Universal for all armor)
     */
    public Armor(String name, int potency, int atkDefense, int mpDefense, CharacterClass classType, int tier, String imagePath, String equipmentTypeDesignation) {
        super(name, potency, classType, tier, EquipmentType.ARMOR, imagePath, equipmentTypeDesignation);
        this.atkDefense = atkDefense;
        this.mpDefense = mpDefense;
        initializeStatModifiers(); // Initialize stat modifiers after setting classType
    }

    /**
     * NEW: Initialize stat modifiers for armor
     */
    @Override
    protected void initializeStatModifiers() {
        float tierMultiplier = getTierMultiplier();
        
        // Each armor type gets exactly ONE positive and ONE negative stat modifier
        // These should match the actual game effects from get_defense_value()
        if (classType == null) {
            // Universal armor has no stat modifiers
            return;
        }
        
        switch (classType) {
            case ROGUE:
                statModifiers.put("Defense", 2.0f * tierMultiplier); // Positive defense bonus
                statModifiers.put("Speed", -1.5f * getTierPenaltyMultiplier()); // Negative speed penalty
                break;
            case RANGER:
                statModifiers.put("Defense", 1.5f * tierMultiplier); // Positive defense bonus
                statModifiers.put("Mana", -1.0f * getTierPenaltyMultiplier()); // Negative mana penalty
                break;
            case WARRIOR:
                statModifiers.put("Defense", 3.0f * tierMultiplier); // Positive defense bonus
                statModifiers.put("Speed", -2.0f * getTierPenaltyMultiplier()); // Negative speed penalty
                break;
            case MAGE:
                statModifiers.put("Mana", 2.0f * tierMultiplier); // Positive mana bonus
                statModifiers.put("Defense", -1.0f * getTierPenaltyMultiplier()); // Negative defense penalty
                break;
        }
    }

    /**
     * NEW: Apply armor stat modifiers to character
     */
    @Override
    public void applyStatModifiers(Character character) {
        // This will be implemented when we add stat modification system to Character
        // For now, we'll just notify observers
        character.notify_observers("ARMOR_MODIFIERS_APPLIED", this);
    }

    /**
     * NEW: Remove armor stat modifiers from character
     */
    @Override
    public void removeStatModifiers(Character character) {
        // This will be implemented when we add stat modification system to Character
        character.notify_observers("ARMOR_MODIFIERS_REMOVED", this);
    }

    /**
     * MANDATORY: Get total defense value (primary stat)
     *
     * @return Current defense value including upgrades
     */
    public int get_defense_value() {
        // Get the Defense modifier from stat modifiers
        Float defenseModifier = statModifiers.get("Defense");
        if (defenseModifier != null) {
            return defenseModifier.intValue();
        }
        return 0; // No defense modifier
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
        stats.append(", ATK_DEF: ").append(get_atk_defense());
        stats.append(", MP_DEF: ").append(get_mp_defense());
        stats.append(", Tier: ").append(tier);
        return stats.toString();
    }

    @Override
    public String toString() {
        return super.toString() + " [" + get_stats() + "]";
    }
} 