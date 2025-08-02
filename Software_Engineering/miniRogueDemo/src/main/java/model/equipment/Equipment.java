package model.equipment;

import enums.CharacterClass;
import enums.GameConstants;
import model.items.Item;
import model.characters.Character;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract base class for all equipment items (weapons and armor).
 * Provides upgrade functionality, tier system, and stat management.
 */
public abstract class Equipment extends Item {
    // MANDATORY: Equipment attributes
    protected int upgradeLevel;
    protected int baseStatValue;
    
    // NEW: Tier system (1-3)
    protected int tier;
    
    // NEW: Stat modifiers system
    protected Map<String, Float> statModifiers;
    
    // NEW: Equipment type
    protected EquipmentType equipmentType;
    
    // NEW: Image path for the equipment icon
    protected String imagePath;
    
    // NEW: Equipment type designation
    protected String equipmentTypeDesignation;

    /**
     * Equipment types enum
     */
    public enum EquipmentType {
        WEAPON,
        ARMOR
    }

    /**
     * MANDATORY: Constructor for Equipment
     *
     * @param name Equipment name
     * @param potency Base stat value
     * @param classType Character class requirement
     * @param tier Equipment tier (1-5)
     * @param equipmentType Type of equipment (WEAPON/ARMOR)
     * @param imagePath Path to the equipment icon image
     * @param equipmentTypeDesignation Type designation (Blade, Distance, Impact, Magic, Universal)
     */
    public Equipment(String name, int potency, CharacterClass classType, int tier, EquipmentType equipmentType, String imagePath, String equipmentTypeDesignation) {
        super(name, potency, classType);
        this.upgradeLevel = 0;
        this.baseStatValue = potency;
        this.tier = Math.max(1, Math.min(5, tier)); // Ensure tier is 1-5
        this.equipmentType = equipmentType;
        this.imagePath = imagePath;
        this.equipmentTypeDesignation = equipmentTypeDesignation;
        this.statModifiers = new HashMap<>();
        // Don't call initializeStatModifiers() here - let subclasses call it after setting their fields
    }

    /**
     * NEW: Increase equipment tier
     * @return true if tier was increased successfully
     */
    public boolean increaseTier() {
        if (tier >= 5) {
            return false; // Already at maximum tier
        }
        tier++;
        return true;
    }

    /**
     * NEW: Initialize stat modifiers based on equipment type and tier
     */
    protected abstract void initializeStatModifiers();

    /**
     * NEW: Get tier multiplier for positive effects
     */
    protected float getTierMultiplier() {
        return 1.0f + (tier - 1) * 0.1f; // 1.0, 1.1, 1.2 for tiers 1, 2, 3
    }

    /**
     * NEW: Get tier multiplier for negative effects (reduced penalties)
     */
    protected float getTierPenaltyMultiplier() {
        return 1.0f - (tier - 1) * 0.1f; // 1.0, 0.9, 0.8 for tiers 1, 2, 3
    }

    /**
     * NEW: Apply stat modifiers to a character
     */
    public void applyStatModifiers(Character character) {
        // This will be implemented by subclasses
    }

    /**
     * NEW: Remove stat modifiers from a character
     */
    public void removeStatModifiers(Character character) {
        // This will be implemented by subclasses
    }

    /**
     * NEW: Get formatted stat modifiers string
     */
    public String getStatModifiersString() {
        if (statModifiers.isEmpty()) {
            return "No modifiers";
        }
        
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Float> entry : statModifiers.entrySet()) {
            if (sb.length() > 0) sb.append(", ");
            float value = entry.getValue();
            String sign = value >= 0 ? "+" : "";
            sb.append(entry.getKey()).append(": ").append(sign).append(String.format("%.1f", value));
        }
        return sb.toString();
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
    public int get_tier() { return tier; }
    public EquipmentType get_equipment_type() { return equipmentType; }
    public Map<String, Float> get_stat_modifiers() { return new HashMap<>(statModifiers); }
    public String get_image_path() { return imagePath; }
    public String get_equipment_type_designation() { return equipmentTypeDesignation; }

    @Override
    public String toString() {
        String upgradeInfo = (upgradeLevel > 0) ? " (+" + upgradeLevel + ")" : "";
        String tierInfo = " [T" + tier + "]";
        return super.toString() + tierInfo + upgradeInfo;
    }
} 