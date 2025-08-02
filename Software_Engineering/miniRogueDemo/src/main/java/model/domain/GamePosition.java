package model.domain;

import utilities.Position;

/**
 * Enhanced position class that extends the base Position utility.
 * Provides additional functionality for game-specific position operations.
 * Follows OOD principles for better code organization.
 */
public class GamePosition {
    private final Position position;
    
    /**
     * Constructor for GamePosition
     * @param x X coordinate
     * @param y Y coordinate
     */
    public GamePosition(int x, int y) {
        this.position = new Position(x, y);
    }
    
    /**
     * Constructor for GamePosition from base Position
     * @param position Base position to copy
     */
    public GamePosition(Position position) {
        this.position = position;
    }
    
    /**
     * Create a new position by adding a direction
     * @param direction Direction to add
     * @return New position
     */
    public int getX() { return position.get_x(); }
    public int getY() { return position.get_y(); }
    
    public GamePosition add(Direction direction) {
        return new GamePosition(getX() + direction.getDx(), getY() + direction.getDy());
    }
    
    /**
     * Create a new position by subtracting a direction
     * @param direction Direction to subtract
     * @return New position
     */
    public GamePosition subtract(Direction direction) {
        return new GamePosition(getX() - direction.getDx(), getY() - direction.getDy());
    }
    
    /**
     * Get the direction from this position to another position
     * @param other Other position
     * @return Direction from this to other
     */
    public Direction getDirectionTo(GamePosition other) {
        int dx = other.getX() - getX();
        int dy = other.getY() - getY();
        return Direction.fromPixelDelta(dx, dy);
    }
    
    /**
     * Calculate Manhattan distance to another position
     * @param other Other position
     * @return Manhattan distance
     */
    public int manhattanDistanceTo(GamePosition other) {
        return Math.abs(getX() - other.getX()) + Math.abs(getY() - other.getY());
    }
    
    /**
     * Calculate Euclidean distance to another position
     * @param other Other position
     * @return Euclidean distance
     */
    public double euclideanDistanceTo(GamePosition other) {
        int dx = getX() - other.getX();
        int dy = getY() - other.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    /**
     * Check if this position is adjacent to another position
     * @param other Other position
     * @return true if positions are adjacent
     */
    public boolean isAdjacentTo(GamePosition other) {
        return manhattanDistanceTo(other) == 1;
    }
    
    /**
     * Check if this position is within a certain distance of another position
     * @param other Other position
     * @param maxDistance Maximum distance
     * @return true if within distance
     */
    public boolean isWithinDistance(GamePosition other, int maxDistance) {
        return manhattanDistanceTo(other) <= maxDistance;
    }
    
    /**
     * Get all adjacent positions
     * @return Array of adjacent positions
     */
    public GamePosition[] getAdjacentPositions() {
        return new GamePosition[] {
            add(Direction.UP),
            add(Direction.DOWN),
            add(Direction.LEFT),
            add(Direction.RIGHT),
            add(Direction.UP_LEFT),
            add(Direction.UP_RIGHT),
            add(Direction.DOWN_LEFT),
            add(Direction.DOWN_RIGHT)
        };
    }
    
    /**
     * Get cardinal adjacent positions only (no diagonals)
     * @return Array of cardinal adjacent positions
     */
    public GamePosition[] getCardinalAdjacentPositions() {
        return new GamePosition[] {
            add(Direction.UP),
            add(Direction.DOWN),
            add(Direction.LEFT),
            add(Direction.RIGHT)
        };
    }
    
    /**
     * Convert to pixel coordinates
     * @param tileSize Size of each tile in pixels
     * @return Pixel position
     */
    public PixelPosition toPixelPosition(int tileSize) {
        return new PixelPosition(getX() * tileSize, getY() * tileSize);
    }
    
    /**
     * Create position from pixel coordinates
     * @param pixelX Pixel X coordinate
     * @param pixelY Pixel Y coordinate
     * @param tileSize Size of each tile in pixels
     * @return Game position
     */
    public static GamePosition fromPixelPosition(int pixelX, int pixelY, int tileSize) {
        return new GamePosition(pixelX / tileSize, pixelY / tileSize);
    }
    
    /**
     * Check if position is within bounds
     * @param width Map width
     * @param height Map height
     * @return true if within bounds
     */
    public boolean isWithinBounds(int width, int height) {
        return getX() >= 0 && getX() < width && getY() >= 0 && getY() < height;
    }
    
    /**
     * Clamp position to bounds
     * @param width Map width
     * @param height Map height
     * @return Clamped position
     */
    public GamePosition clampToBounds(int width, int height) {
        int clampedX = Math.max(0, Math.min(getX(), width - 1));
        int clampedY = Math.max(0, Math.min(getY(), height - 1));
        return new GamePosition(clampedX, clampedY);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        GamePosition other = (GamePosition) obj;
        return getX() == other.getX() && getY() == other.getY();
    }
    
    @Override
    public int hashCode() {
        return 31 * getX() + getY();
    }
    
    @Override
    public String toString() {
        return "GamePosition(" + getX() + ", " + getY() + ")";
    }
} 