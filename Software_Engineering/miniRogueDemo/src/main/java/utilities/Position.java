package utilities;

import java.util.Objects;

/**
 * Utility class representing a 2D coordinate position in the game world.
 * Immutable class used for character positions, item locations, and map coordinates.
 */
public final class Position {
    private final int x;
    private final int y;

    /**
     * MANDATORY: Constructor to create a Position
     *
     * @param x The x-coordinate
     * @param y The y-coordinate
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * MANDATORY: Get the x-coordinate
     *
     * @return The x-coordinate value
     */
    public int get_x() {
        return x;
    }

    /**
     * MANDATORY: Get the y-coordinate
     *
     * @return The y-coordinate value
     */
    public int get_y() {
        return y;
    }

    /**
     * MANDATORY: Create a new Position moved by the specified offset
     *
     * @param deltaX The x offset
     * @param deltaY The y offset
     * @return New Position object with offset applied
     */
    public Position move(int deltaX, int deltaY) {
        return new Position(x + deltaX, y + deltaY);
    }

    /**
     * MANDATORY: Calculate distance to another position
     *
     * @param other The other Position
     * @return Euclidean distance as double
     */
    public double distance_to(Position other) {
        int dx = this.x - other.x;
        int dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * MANDATORY: Calculate Manhattan distance to another position
     *
     * @param other The other Position
     * @return Manhattan distance as integer
     */
    public int manhattan_distance_to(Position other) {
        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
    }

    /**
     * MANDATORY: Check if this position is adjacent to another
     *
     * @param other The other Position
     * @return true if positions are adjacent (including diagonally)
     */
    public boolean is_adjacent_to(Position other) {
        int dx = Math.abs(this.x - other.x);
        int dy = Math.abs(this.y - other.y);
        return dx <= 1 && dy <= 1 && !(dx == 0 && dy == 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return String.format("Position(%d, %d)", x, y);
    }
} 