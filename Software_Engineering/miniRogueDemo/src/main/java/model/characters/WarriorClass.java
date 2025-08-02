package model.characters;

import enums.CharacterClass;
import model.equipment.Weapon;
import model.equipment.Armor;

public class WarriorClass extends BaseClass {
    public WarriorClass() {
        super(CharacterClass.WARRIOR);
    }

    @Override
    protected void setBaseStats() {
        baseHp = 120;
        baseMp = 0;
        baseAtk = 18;
        attackSpeed = 1.12f;
        range = 2; // double range
        hasProjectile = false;
        hasMelee = true;
        moveSpeed = 3.5f;
        defense = 10;
        attackWidth = 120;
        projectileSpeed = 5.0f;
        projectileTravelDistance = 6;
    }

    public String getClassName() { return "Warrior"; }

    @Override
    public String getDebugStats() { return getDebugStatsFull(); }

    @Override
    public model.equipment.Weapon getStartingWeapon() {
        return new Weapon("Iron Sword", 15, 0, classType, 1, Weapon.WeaponType.IMPACT, "images/weapons/Impact/hand_axe_2_new.png", "Impact");
    }

    @Override
    public model.equipment.Armor getStartingArmor() {
        return new Armor("Leather Armor", 0, 5, 2, classType, 1, "images/armor/Basic_Leather.png", "Universal");
    }
} 