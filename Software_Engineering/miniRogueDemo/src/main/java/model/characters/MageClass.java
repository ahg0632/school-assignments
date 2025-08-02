package model.characters;

import enums.CharacterClass;
import model.equipment.Weapon;
import model.equipment.Armor;

public class MageClass extends BaseClass {
    private int projectileManaCost = 5;
    public int getProjectileManaCost() { return projectileManaCost; }

    private long lastRegenTime = System.currentTimeMillis();

    public MageClass() {
        super(CharacterClass.MAGE);
    }

    @Override
    protected void setBaseStats() {
        baseHp = 70;
        baseMp = 100;
        baseAtk = 10;
        attackSpeed = 1.4f;
        range = 1.6f; // Set to 1.6 for Mage
        hasProjectile = true;
        hasMelee = true;
        moveSpeed = 4.0f;
        defense = 2;
        attackWidth = 100;
        projectileSpeed = 10.0f; // Should never be less than moveSpeed
        projectileTravelDistance = 8;
    }

    public String getClassName() { return "Mage"; }

    @Override
    public String getDebugStats() { return getDebugStatsFull(); }

    @Override
    public model.equipment.Weapon getStartingWeapon() {
        return new Weapon("Oak Staff", 8, 10, classType, 1, Weapon.WeaponType.MAGIC, "images/weapons/Magic/staff_0.png", "Magic");
    }
    @Override
    public model.equipment.Armor getStartingArmor() {
        return new Armor("Cloth Robes", 0, 2, 8, classType, 1, "images/armor/Wizard_Robes.png", "Universal");
    }
} 