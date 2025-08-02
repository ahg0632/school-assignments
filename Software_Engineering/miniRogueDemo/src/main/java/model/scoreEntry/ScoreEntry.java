package model.scoreEntry;

import java.util.List;

/**
 * Represents a single score entry for the high score table or end-of-game summary.
 */
public class ScoreEntry {
    private String initials; // 3-character player initials
    private int finalExpLevel;
    private int enemiesSlain;
    private String killer; // Name of the enemy that killed the player (optional, can be null)
    private List<String> itemsCollected;

    public ScoreEntry(String initials, int finalExpLevel, int enemiesSlain, String killer, List<String> itemsCollected) {
        this.initials = initials;
        this.finalExpLevel = finalExpLevel;
        this.enemiesSlain = enemiesSlain;
        this.killer = killer;
        this.itemsCollected = itemsCollected;
    }

    public String getInitials() {
        return initials;
    }

    public int getFinalExpLevel() {
        return finalExpLevel;
    }

    public int getEnemiesSlain() {
        return enemiesSlain;
    }

    public String getKiller() {
        return killer;
    }

    public List<String> getItemsCollected() {
        return itemsCollected;
    }

    public int getTotalScore() {
        return finalExpLevel + enemiesSlain + (itemsCollected != null ? itemsCollected.size() : 0);
    }

    @Override
    public String toString() {
        return "ScoreEntry{" +
                "initials='" + initials + '\'' +
                ", finalExpLevel=" + finalExpLevel +
                ", enemiesSlain=" + enemiesSlain +
                ", killer='" + killer + '\'' +
                ", itemsCollected=" + itemsCollected +
                '}';
    }
} 