package enums;

/**
 * Enumeration defining player character classes with their base statistics.
 * Each class has unique HP, ATK, MP values and equipment preferences.
 */
public enum CharacterClass {
    WARRIOR("Warrior", 100, 25, 20),
    MAGE("Mage", 60, 15, 80),
    ROGUE("Rogue", 80, 20, 40),
    RANGER("Ranger", 60, 15, 80);

    private final String className;
    private final int baseHp;
    private final int baseAtk;
    private final int baseMp;

    CharacterClass(String className, int baseHp, int baseAtk, int baseMp) {
        this.className = className;
        this.baseHp = baseHp;
        this.baseAtk = baseAtk;
        this.baseMp = baseMp;
    }

    public String get_class_name() {
        return className;
    }

    public int get_base_hp() {
        return baseHp;
    }

    public int get_base_atk() {
        return baseAtk;
    }

    public int get_base_mp() {
        return baseMp;
    }
} 