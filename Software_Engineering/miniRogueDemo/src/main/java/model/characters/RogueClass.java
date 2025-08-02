package model.characters;

import enums.CharacterClass;
import model.equipment.Weapon;
import model.equipment.Armor;

public class RogueClass extends BaseClass {
    public RogueClass() {
        super(CharacterClass.ROGUE);
    }

    @Override
    protected void setBaseStats() {
        baseHp = 80;
        baseMp = 0;
        baseAtk = 14;
        attackSpeed = 2.55f;
        range = 1.4f; // Set to 1.4 for Rogue
        hasProjectile = false;
        hasMelee = true;
        moveSpeed = 5.0f;
        defense = 4;
        attackWidth = 90;
        projectileSpeed = 8.0f;
        projectileTravelDistance = 7;
    }

    @Override
    public String getClassName() { return "Rogue"; }

    @Override
    public String getDebugStats() { return getDebugStatsFull(); }

    @Override
    public model.equipment.Weapon getStartingWeapon() {
        return new Weapon("Steel Dagger", 12, 5, classType, 1, Weapon.WeaponType.BLADE, "images/weapons/Blade/Dagger.png", "Blade");
    }
    @Override
    public model.equipment.Armor getStartingArmor() {
        return new Armor("Studded Leather", 0, 4, 3, classType, 1, "images/armor/Basic_Leather.png", "Universal");
    }
} 