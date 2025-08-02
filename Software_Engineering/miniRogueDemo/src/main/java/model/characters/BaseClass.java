package model.characters;

import enums.CharacterClass;
import model.equipment.Weapon;
import model.equipment.Armor;

public abstract class BaseClass {
    protected int baseHp;
    protected int baseMp;
    protected float baseAtk;
    protected float attackSpeed;
    protected float range;
    protected boolean hasProjectile;
    protected float moveSpeed;
    protected float defense;
    protected CharacterClass classType;
    protected int attackWidth; // in degrees
    protected float projectileSpeed = 0f; // tiles per second
    protected int projectileTravelDistance = 0; // in tiles
    protected boolean hasMelee;

    public BaseClass(CharacterClass classType) {
        this.classType = classType;
        setBaseStats();
    }

    protected abstract void setBaseStats();

    public int getBaseHp() { return baseHp; }
    public int getBaseMp() { return baseMp; }
    public float getBaseAtk() { return baseAtk; }
    public float getAttackSpeed() { return attackSpeed; }
    public float getRange() { return range; }
    public void setRange(float range) { this.range = range; }
    public void increaseRange(float amount) { this.range += amount; }
    public boolean hasProjectile() { return hasProjectile; }
    public float getMoveSpeed() { return moveSpeed; }
    public void setMoveSpeed(float speed) { this.moveSpeed = speed; }
    public void increaseMoveSpeed(float amount) { this.moveSpeed += amount; }
    public float getDefense() { return defense; }
    public void setDefense(float defense) { this.defense = defense; }
    public void increaseDefense(float amount) { this.defense += amount; }
    public CharacterClass getClassType() { return classType; }
    public int getAttackWidth() { return attackWidth; }
    public void setAttackWidth(int width) { this.attackWidth = width; }
    public float getProjectileSpeed() { return projectileSpeed; }
    public void setProjectileSpeed(float speed) { this.projectileSpeed = speed; }
    public int getProjectileTravelDistance() { return projectileTravelDistance; }
    public void setProjectileTravelDistance(int dist) { this.projectileTravelDistance = dist; }
    public boolean hasMelee() { return hasMelee; }
    public void setHasMelee(boolean hasMelee) { this.hasMelee = hasMelee; }

    public abstract String getClassName();
    public abstract String getDebugStats();
    public String getDebugStatsFull() {
        return String.format(
            "Class: %s\nHP: %d\nMP: %d\nATK: %d\nDEF: %d\nSPD: %.1f\nAttack Speed: %.2f\nRange: %.2f\nWidth: %d\nProjectile: %s\nProjectile Speed: %.2f\nProjectile Range: %d\nMelee: %s",
            classType,
            baseHp,
            baseMp,
            baseAtk,
            defense,
            moveSpeed,
            attackSpeed,
            range,
            attackWidth,
            hasProjectile ? "Yes" : "No",
            projectileSpeed,
            projectileTravelDistance,
            hasMelee ? "Yes" : "No"
        );
    }
    public String getDebugStatsFull(model.characters.Player player) {
        StringBuilder sb = new StringBuilder();
        sb.append("Class: ").append(getClassName()).append("\n");
        sb.append("HP: ").append(player.get_current_hp()).append("/").append(player.get_max_hp()).append("\n");
        sb.append("MP: ").append(player.get_current_mp()).append("/").append(player.get_max_mp()).append("\n");
        sb.append("ATK: ").append(getBaseAtk()).append("\n");
        sb.append("DEF: ").append(getDefense()).append("\n");
        sb.append("Speed: ").append(getMoveSpeed()).append("\n");
        sb.append("Attack Speed: ").append(getAttackSpeed()).append("\n");
        sb.append("Range: ").append(String.format("%.2f", getRange())).append("\n");
        sb.append("Width: ").append(getAttackWidth()).append("\n");
        sb.append("Projectile: ").append(hasProjectile() ? "Yes" : "No").append("\n");
        sb.append("Projectile Speed: ").append(getProjectileSpeed()).append("\n");
        sb.append("Projectile Range: ").append(getProjectileTravelDistance()).append("\n");
        sb.append("Melee: ").append(hasMelee() ? "Yes" : "No");
        return sb.toString();
    }
    public abstract Weapon getStartingWeapon();
    public abstract Armor getStartingArmor();
    // Optionally: public abstract void performAttack(Player player, ...);
} 