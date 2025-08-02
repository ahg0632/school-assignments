package enums;

/**
 * Enumeration defining movement directions for character positioning.
 * Used for player movement, enemy AI, and battle positioning.
 */
public enum Direction {
    UP("up", 0, -1),
    DOWN("down", 0, 1),
    LEFT("left", -1, 0),
    RIGHT("right", 1, 0);

    private final String directionName;
    private final int deltaX;
    private final int deltaY;

    Direction(String directionName, int deltaX, int deltaY) {
        this.directionName = directionName;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }

    public String get_direction_name() {
        return directionName;
    }

    public int get_delta_x() {
        return deltaX;
    }

    public int get_delta_y() {
        return deltaY;
    }
} 