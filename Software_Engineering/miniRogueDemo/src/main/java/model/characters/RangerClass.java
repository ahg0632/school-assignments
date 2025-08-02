package model.characters;

import enums.CharacterClass;
import model.equipment.Weapon;
import model.equipment.Armor;

public class RangerClass extends BaseClass {
    private int projectileManaCost = 5;
    public int getProjectileManaCost() { return projectileManaCost; }

    private long lastRegenTime = System.currentTimeMillis();

    public RangerClass() {
        super(CharacterClass.RANGER);
    }

    @Override
    protected void setBaseStats() {
        baseHp = 70;
        baseMp = 0;
        baseAtk = 8; // Slightly less than Mage
        attackSpeed = 2.5f; // Faster fire rate
        projectileTravelDistance = 8;
        range = projectileTravelDistance / 2f; // Red arc is half the projectile range
        hasProjectile = true;
        hasMelee = false;
        moveSpeed = 4.5f; // Almost as fast as Rogue
        defense = 1; // Weak defense
        attackWidth = 20; // Slightly wider arc for red indicator
        projectileSpeed = 20.0f; // Much faster than Mage
    }

    public String getClassName() { return "Ranger"; }

    @Override
    public String getDebugStats() { return getDebugStatsFull(); }

    @Override
    public model.equipment.Weapon getStartingWeapon() {
        return new Weapon("Yew Bow", 8, 10, classType, 1, Weapon.WeaponType.DISTANCE, "images/weapons/Distance/Short_Bow.png", "Distance");
    }
    @Override
    public model.equipment.Armor getStartingArmor() {
        return new Armor("Ranger's Vest", 0, 2, 8, classType, 1, "images/armor/Iced_Vest.png", "Universal");
    }
} 