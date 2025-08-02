package model.characters;

import enums.CharacterClass;
import utilities.Position;
import model.equipment.Equipment;
import model.items.Item;
import model.items.KeyItem;
import java.util.List;
import java.util.Iterator;

/**
 * Upgrader NPC that can upgrade player equipment for upgrade crystals
 */
public class Upgrader extends Character {
    
    public enum UpgraderType {
        WEAPON("Weapon Upgrader", "Orange"),
        ARMOR("Armor Upgrader", "Green");
        
        private final String displayName;
        private final String borderColor;
        
        UpgraderType(String displayName, String borderColor) {
            this.displayName = displayName;
            this.borderColor = borderColor;
        }
        
        public String getDisplayName() { return displayName; }
        public String getBorderColor() { return borderColor; }
    }
    
    private UpgraderType upgraderType;
    private boolean hasMadeDeal = false;
    private int upgradeCost;
    
    // Field of view and visibility fields
    private boolean isVisible = true;
    private float transparency = 1.0f;
    private static final int FIELD_OF_VIEW_RANGE = 5;
    private boolean isDisappearingAfterUpgrade = false;
    private long disappearingStartTime = 0;
    private static final long DISAPPEARING_DELAY = 400; // 400ms delay before starting to fade
    
    public Upgrader(Position position, UpgraderType type) {
        super("Upgrader", CharacterClass.ROGUE, position); // Class doesn't matter for NPCs
        this.upgraderType = type;
        this.currentHp = 0; // Upgraders cannot be hurt
        this.maxHp = 0;
        this.baseAtk = 0;
        this.currentMp = 0;
        this.maxMp = 0;
        // Ensure upgrader starts fully visible
    }
    
    public UpgraderType getUpgraderType() {
        return upgraderType;
    }
    
    public String getDisplayName() {
        return upgraderType.getDisplayName();
    }
    
    public String getBorderColor() {
        return upgraderType.getBorderColor();
    }
    
    public boolean isVisible() {
        // For normal enemy detection, use isVisible field
        // For disappearing after upgrade, allow rendering and let transparency handle the fade
        if (isDisappearingAfterUpgrade) {
            return !hasMadeDeal; // Allow rendering during disappearing process
        }
        return !hasMadeDeal && isVisible; // Normal visibility check for enemy detection
    }
    
    public float getTransparency() {
        if (hasMadeDeal) {
            return 0.0f; // Completely invisible if deal made
        }
        if (isDisappearingAfterUpgrade) {
            return transparency; // Use current transparency during disappearing process
        }
        return transparency; // Use calculated transparency based on enemy detection
    }
    
    /**
     * Start the disappearing process after successful upgrade
     */
    public void startDisappearingAfterUpgrade() {
        isDisappearingAfterUpgrade = true;
        transparency = 1.0f; // Start fully visible
        disappearingStartTime = System.currentTimeMillis(); // Record when disappearing started
    }
    
    public boolean hasMadeDeal() {
        return hasMadeDeal;
    }
    
    public int getUpgradeCost() {
        return upgradeCost;
    }
    
    public void setUpgradeCost(int cost) {
        this.upgradeCost = cost;
    }
    
    /**
     * Check if player is in range (5 tiles)
     */
    public boolean isPlayerInRange(Position playerPosition) {
        int distance = Math.abs(position.get_x() - playerPosition.get_x()) + 
                      Math.abs(position.get_y() - playerPosition.get_y());
        return distance <= 5;
    }
    
    /**
     * Get field of view range
     */
    public int getFieldOfViewRange() {
        return FIELD_OF_VIEW_RANGE;
    }
    
    /**
     * Update visibility based on nearby enemies
     */
    public void updateVisibility(List<Character> nearbyEnemies) {
        if (hasMadeDeal) {
            isVisible = false;
            transparency = 0.0f;
            return;
        }
        
        if (isDisappearingAfterUpgrade) {
            // Check if we should start fading yet (add delay before fading begins)
            long currentTime = System.currentTimeMillis();
            if (currentTime - disappearingStartTime < DISAPPEARING_DELAY) {
                isVisible = true; // Still in delay period, stay fully visible
                transparency = 1.0f;
                return;
            }
            
            // Gradually fade out after successful upgrade
            transparency = Math.max(0.0f, transparency - 0.01f); // Much slower fade out for visible gradual effect
            if (transparency <= 0.0f) {
                hasMadeDeal = true; // Mark as completely gone
                isVisible = false;
            }
            return;
        }
        
        boolean enemiesInFieldOfView = nearbyEnemies.stream()
            .anyMatch(enemy -> {
                if (!(enemy instanceof Enemy || enemy instanceof Boss)) {
                    return false;
                }
                
                // Check if enemy is within field of view range
                int distance = Math.abs(position.get_x() - enemy.get_position().get_x()) + 
                             Math.abs(position.get_y() - enemy.get_position().get_y());
                return distance <= FIELD_OF_VIEW_RANGE;
            });
        
        if (enemiesInFieldOfView) {
            // Gradually become more transparent when enemies are in field of view
            transparency = Math.max(0.0f, transparency - 0.15f); // Faster fade out
            if (transparency <= 0.1f) {
                isVisible = false;
            }
        } else {
            // Gradually become more visible when no enemies are in field of view
            transparency = Math.min(1.0f, transparency + 0.15f); // Faster fade in
            if (transparency >= 0.9f) {
                isVisible = true;
            }
        }
    }
    
    /**
     * Attempt to upgrade player equipment
     */
    public boolean attemptUpgrade(Player player) {
        if (hasMadeDeal || isDisappearingAfterUpgrade) {
            return false;
        }
        
        // Get the equipment to upgrade
        Equipment equipmentToUpgrade = null;
        if (upgraderType == UpgraderType.WEAPON) {
            equipmentToUpgrade = player.get_equipped_weapon();
        } else {
            equipmentToUpgrade = player.get_equipped_armor();
        }
        
        if (equipmentToUpgrade == null) {
            return false;
        }
        
        // Check if equipment can be upgraded
        if (equipmentToUpgrade.get_tier() >= 5) { // Max tier is now 5
            return false;
        }
        
        // Upgrade the equipment (crystal consumption is handled by GameLogic)
        equipmentToUpgrade.increaseTier();
        
        // Don't mark deal as made immediately - let the disappearing process handle it
        // hasMadeDeal will be set to true when the upgrader completely fades out
        
        return true;
    }
    
    /**
     * Calculate upgrade cost based on current tier
     */
    public void calculateUpgradeCost(Player player) {
        Equipment equipment = null;
        if (upgraderType == UpgraderType.WEAPON) {
            equipment = player.get_equipped_weapon();
        } else {
            equipment = player.get_equipped_armor();
        }
        
        if (equipment == null) {
            upgradeCost = 5; // Default cost
        } else {
            int currentTier = equipment.get_tier();
            upgradeCost = (currentTier + 1) * 5; // 5, 10, 15, 20, 25
        }
    }
    
    @Override
    public int attack(Character target) {
        return 0; // Upgraders cannot attack
    }
}