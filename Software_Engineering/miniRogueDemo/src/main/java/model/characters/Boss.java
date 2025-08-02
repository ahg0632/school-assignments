package model.characters;

import enums.CharacterClass;
import utilities.Position;
import model.items.Item;
import model.items.Consumable;
import model.items.KeyItem;
import model.equipment.Weapon;
import model.equipment.Armor;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Boss character class extending Enemy.
 * Represents powerful floor-ending enemies with enhanced stats and visual differences.
 */
public class Boss extends Enemy {

    // Boss-specific attributes
    private static final float BOSS_SIZE_MULTIPLIER = 2.0f; // Twice as big visually
    private static final float BOSS_HP_MODIFIER = 1.5f; // Additional 1.5x HP on top of enemy base
    private static final float BOSS_ATK_MODIFIER = 1.5f; // Additional 1.5x ATK on top of enemy base
    private static final float BOSS_RANGE_MODIFIER = 0.8f; // 20% shorter range (0.8x)
    private static final float BOSS_SPEED_MODIFIER = 0.8f; // 20% slower (0.8x)
    private static final float BOSS_VISION_MODIFIER = 1.3f; // 30% increased vision range (1.3x)

    /**
     * Constructor for Boss
     *
     * @param name Boss name
     * @param characterClass Boss combat class
     * @param position Initial spawn position
     */
    public Boss(String name, CharacterClass characterClass, Position position) {
        super(name, characterClass, position, "boss");
        // Apply boss-specific stat enhancements on top of enemy base stats
        enhance_boss_stats();
    }

    /**
     * Enhance boss statistics beyond regular enemy stats
     */
    private void enhance_boss_stats() {
        // Apply additional boss modifiers on top of enemy base stats
        this.maxHp = (int)(maxHp * BOSS_HP_MODIFIER);
        this.currentHp = maxHp;
        this.baseAtk = (int)(baseAtk * BOSS_ATK_MODIFIER);
        // Apply speed modifier (20% slower) - use the setter method
        this.setMoveSpeed(this.getMoveSpeed() * BOSS_SPEED_MODIFIER);
        // Update experience value for enhanced stats
        this.experienceValue = calculate_boss_experience();
    }
    
    /**
     * Get boss range modifier
     *
     * @return Range modifier (0.8f for 20% shorter range)
     */
    public float getRangeModifier() {
        return BOSS_RANGE_MODIFIER;
    }
    
    /**
     * Get boss speed modifier
     *
     * @return Speed modifier (0.8f for 20% slower)
     */
    public float getSpeedModifier() {
        return BOSS_SPEED_MODIFIER;
    }

    /**
     * Calculate boss experience value
     *
     * @return Experience points for defeating this boss
     */
    private int calculate_boss_experience() {
        return experienceValue * 3; // Bosses give 3x normal enemy experience
    }

    /**
     * Get boss size multiplier for visual rendering
     *
     * @return Size multiplier (2.0f for bosses)
     */
    public float getSizeMultiplier() {
        return BOSS_SIZE_MULTIPLIER;
    }

    /**
     * Get boss aggro range with vision modifier
     *
     * @return Enhanced aggro range (30% increased vision)
     */
    @Override
    public int get_aggro_range() {
        int baseRange = super.get_aggro_range();
        int enhancedRange = (int)Math.round(baseRange * BOSS_VISION_MODIFIER);
        return enhancedRange;
    }
    
    /**
     * Get boss vision modifier
     *
     * @return Vision modifier (1.3f for 30% increased vision)
     */
    public float getVisionModifier() {
        return BOSS_VISION_MODIFIER;
    }
    
    /**
     * Check if this is a boss (for log messages and game over terms)
     *
     * @return true (bosses are always referred to as "Boss")
     */
    @Override
    public boolean isBoss() {
        return true;
    }

    /**
     * Get the character type for display purposes
     *
     * @return "Boss" instead of "Enemy"
     */
    @Override
    public String getCharacterType() {
        return "Boss";
    }
} 