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
            case RANGER:
                baseGain = 6;
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
            case RANGER:
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
            case RANGER:
                baseGain = 8;
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
            case RANGER:
                return 12; // 12% (same as Mage for now)
            default:
                return enums.GameConstants.CRITICAL_HIT_CHANCE;
        }
    }

    private Stats() {
        // Prevent instantiation
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
} 