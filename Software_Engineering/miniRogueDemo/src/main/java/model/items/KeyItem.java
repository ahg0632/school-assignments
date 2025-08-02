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
     * @param upgradeType Type of upgrade ("weapon", "armor", "any")
     */
    public KeyItem(String name, String upgradeType) {
        super(name, 1);
        this.upgradeType = upgradeType;
    }

    /**
     * MANDATORY: Key items cannot be used directly on characters
     *
     * @param character The character (ignored)
     * @return true for Upgrade Crystals and Keys (they can be consumed), false for other key items
     */
    @Override
    public boolean use(Character character) {
        // Upgrade Crystals and Keys can be consumed, other key items cannot
        if (name.equals("Upgrade Crystal") || upgradeType.equals("stairs")) {
            return true; // Allow Upgrade Crystals and Keys to be consumed
        }
        return false; // Other key items are used for equipment upgrades, not direct use
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
            case "stairs":
                return false; // Keys don't upgrade equipment, they unlock stairs
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