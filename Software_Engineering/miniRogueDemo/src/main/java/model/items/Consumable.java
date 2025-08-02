package model.items;

import model.characters.Character;
import model.characters.Player;

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
     * @param potency Effect strength
     * @param effectType Type of effect ("health", "mana", "experience")
     */
    public Consumable(String name, int potency, String effectType) {
        super(name, potency);
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
        if (character instanceof Player) {
            Player player = (Player) character;
            
            switch (effectType) {
                case "health":
                    // Check if player is already at full health
                    if (player.get_current_hp() >= player.get_max_hp()) {
                        return false; // Don't consume the item if already at full health
                    }
                    player.heal(potency);
                    return true;
                case "mana":
                    // Check if player is already at full mana
                    if (player.get_current_mp() >= player.get_max_mp()) {
                        return false; // Don't consume the item if already at full mana
                    }
                    player.restore_mp(potency);
                    return true;
                case "experience":
                    player.gain_experience(potency);
                    return true;
                case "invisibility":
                    player.activate_invisibility_effect(potency);
                    return true;
                case "clarity":
                    player.activate_clarity_effect(potency);
                    return true;
                case "swiftness":
                    player.activate_swiftness_effect(potency);
                    return true;
                case "immortality":
                    player.activate_immortality_effect(potency);
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }

    public String get_effect_type() {
        return effectType;
    }

    /**
     * Get the size multiplier for potion rendering based on potency
     * @return size multiplier (1.0 for normal, 0.8 for small, 1.2 for large)
     */
    public float getPotionSizeMultiplier() {
        if (effectType.equals("health") || effectType.equals("mana")) {
            if (potency <= 30) {
                return 0.8f; // Small potions (Minor Health: 25, Mana: 30)
            } else if (potency >= 60) {
                return 1.2f; // Large potions (Greater Health: 100, Greater Mana: 60)
            } else {
                return 1.0f; // Medium potions (Health: 50)
            }
        }
        return 1.0f; // Default size for non-potion items
    }

    @Override
    public String toString() {
        return super.toString() + " (+" + potency + " " + effectType + ")";
    }
} 