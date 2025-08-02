package model.gameLogic;

import utilities.Position;
import java.util.Set;
import java.util.HashSet;

/**
 * Data transfer object for attack visual information.
 * Used to pass attack data from Model to View without violating MVC separation.
 * Now supports swing-based attacks with rotating fan.
 */
public class AttackVisualData {
    private final int attackDX;
    private final int attackDY;
    private final float attackRange;
    private final double attackAngle;
    private final Set<Position> attackFanTiles;
    private final Position projectileStart;
    private final Position projectileEnd;
    
    // Swing-based attack properties
    private final boolean isSwingAttack;
    private final double swingStartAngle;
    private final double swingEndAngle;
    private final double swingFanWidth;
    private final long swingStartTime;
    private final long swingDuration;

    public AttackVisualData(int attackDX, int attackDY, float attackRange, double attackAngle) {
        this.attackDX = attackDX;
        this.attackDY = attackDY;
        this.attackRange = attackRange;
        this.attackAngle = attackAngle;
        this.attackFanTiles = new HashSet<>();
        this.projectileStart = null;
        this.projectileEnd = null;
        this.isSwingAttack = false;
        this.swingStartAngle = 0;
        this.swingEndAngle = 0;
        this.swingFanWidth = 0;
        this.swingStartTime = 0;
        this.swingDuration = 0;
    }

    public AttackVisualData(int attackDX, int attackDY, float attackRange, double attackAngle, 
                           Set<Position> attackFanTiles, Position projectileStart, Position projectileEnd) {
        this.attackDX = attackDX;
        this.attackDY = attackDY;
        this.attackRange = attackRange;
        this.attackAngle = attackAngle;
        this.attackFanTiles = attackFanTiles != null ? new HashSet<>(attackFanTiles) : new HashSet<>();
        this.projectileStart = projectileStart;
        this.projectileEnd = projectileEnd;
        this.isSwingAttack = false;
        this.swingStartAngle = 0;
        this.swingEndAngle = 0;
        this.swingFanWidth = 0;
        this.swingStartTime = 0;
        this.swingDuration = 0;
    }
    
    /**
     * Constructor for swing-based attacks
     */
    public AttackVisualData(int attackDX, int attackDY, float attackRange, double attackAngle,
                           double swingStartAngle, double swingEndAngle, double swingFanWidth,
                           long swingStartTime, long swingDuration) {
        this.attackDX = attackDX;
        this.attackDY = attackDY;
        this.attackRange = attackRange;
        this.attackAngle = attackAngle;
        this.attackFanTiles = new HashSet<>();
        this.projectileStart = null;
        this.projectileEnd = null;
        this.isSwingAttack = true;
        this.swingStartAngle = swingStartAngle;
        this.swingEndAngle = swingEndAngle;
        this.swingFanWidth = swingFanWidth;
        this.swingStartTime = swingStartTime;
        this.swingDuration = swingDuration;
    }

    // Getters
    public int getAttackDX() { return attackDX; }
    public int getAttackDY() { return attackDY; }
    public float getAttackRange() { return attackRange; }
    public double getAttackAngle() { return attackAngle; }
    public Set<Position> getAttackFanTiles() { return new HashSet<>(attackFanTiles); }
    public Position getProjectileStart() { return projectileStart; }
    public Position getProjectileEnd() { return projectileEnd; }
    
    // Swing-based attack getters
    public boolean isSwingAttack() { return isSwingAttack; }
    public double getSwingStartAngle() { return swingStartAngle; }
    public double getSwingEndAngle() { return swingEndAngle; }
    public double getSwingFanWidth() { return swingFanWidth; }
    public long getSwingStartTime() { return swingStartTime; }
    public long getSwingDuration() { return swingDuration; }
    
    /**
     * Get the current swing angle based on elapsed time
     */
    public double getCurrentSwingAngle(long currentTime) {
        if (!isSwingAttack) return attackAngle;
        
        long elapsed = currentTime - swingStartTime;
        if (elapsed >= swingDuration) {
            return swingEndAngle; // Swing complete
        }
        
        // Interpolate between start and end angle
        double progress = (double) elapsed / swingDuration;
        return swingStartAngle + (swingEndAngle - swingStartAngle) * progress;
    }
    
    /**
     * Check if swing is still active
     */
    public boolean isSwingActive(long currentTime) {
        return isSwingAttack && (currentTime - swingStartTime) < swingDuration;
    }
} 