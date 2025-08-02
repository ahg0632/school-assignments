package model.domain;

/**
 * Represents a direction in 2D space using integer coordinates.
 * Encapsulates dx/dy values and provides type safety and utility methods.
 * Follows OOD principles for better code organization.
 */
public class Direction {
    
    // Static direction constants for common directions
    public static final Direction UP = new Direction(0, -1);
    public static final Direction DOWN = new Direction(0, 1);
    public static final Direction LEFT = new Direction(-1, 0);
    public static final Direction RIGHT = new Direction(1, 0);
    public static final Direction UP_LEFT = new Direction(-1, -1);
    public static final Direction UP_RIGHT = new Direction(1, -1);
    public static final Direction DOWN_LEFT = new Direction(-1, 1);
    public static final Direction DOWN_RIGHT = new Direction(1, 1);
    public static final Direction NONE = new Direction(0, 0);
    
    private final int dx;
    private final int dy;
    
    /**
     * Constructor for Direction
     * @param dx X component of direction (-1, 0, or 1)
     * @param dy Y component of direction (-1, 0, or 1)
     */
    public Direction(int dx, int dy) {
        this.dx = normalizeComponent(dx);
        this.dy = normalizeComponent(dy);
    }
    
    /**
     * Normalize a direction component to -1, 0, or 1
     * @param component The component to normalize
     * @return Normalized component
     */
    private int normalizeComponent(int component) {
        if (component > 0) return 1;
        if (component < 0) return -1;
        return 0;
    }
    
    /**
     * Get X component of direction
     * @return X component (-1, 0, or 1)
     */
    public int getDx() {
        return dx;
    }
    
    /**
     * Get Y component of direction
     * @return Y component (-1, 0, or 1)
     */
    public int getDy() {
        return dy;
    }
    
    /**
     * Check if this direction is diagonal
     * @return true if both dx and dy are non-zero
     */
    public boolean isDiagonal() {
        return dx != 0 && dy != 0;
    }
    
    /**
     * Check if this direction is cardinal (not diagonal)
     * @return true if only one component is non-zero
     */
    public boolean isCardinal() {
        return (dx != 0 && dy == 0) || (dx == 0 && dy != 0);
    }
    
    /**
     * Check if this direction is no movement
     * @return true if both components are zero
     */
    public boolean isNone() {
        return dx == 0 && dy == 0;
    }
    
    /**
     * Get the angle in radians for this direction
     * @return Angle in radians (0 to 2π)
     */
    public double getAngle() {
        if (isNone()) return 0.0;
        return Math.atan2(dy, dx);
    }
    
    /**
     * Get the angle in degrees for this direction
     * @return Angle in degrees (0 to 360)
     */
    public double getAngleDegrees() {
        return Math.toDegrees(getAngle());
    }
    
    /**
     * Create a direction from angle in radians
     * @param angle Angle in radians
     * @return Direction object
     */
    public static Direction fromAngle(double angle) {
        int dx = (int) Math.round(Math.cos(angle));
        int dy = (int) Math.round(Math.sin(angle));
        return new Direction(dx, dy);
    }
    
    /**
     * Create a direction from angle in degrees
     * @param angleDegrees Angle in degrees
     * @return Direction object
     */
    public static Direction fromAngleDegrees(double angleDegrees) {
        return fromAngle(Math.toRadians(angleDegrees));
    }
    
    /**
     * Create a direction from pixel coordinates (normalized)
     * @param pixelDx Pixel delta X
     * @param pixelDy Pixel delta Y
     * @return Direction object
     */
    public static Direction fromPixelDelta(float pixelDx, float pixelDy) {
        int dx = (Math.abs(pixelDx) > Math.abs(pixelDy)) ? (pixelDx > 0 ? 1 : -1) : 0;
        int dy = (Math.abs(pixelDy) >= Math.abs(pixelDx)) ? (pixelDy > 0 ? 1 : -1) : 0;
        return new Direction(dx, dy);
    }
    
    /**
     * Get the opposite direction
     * @return Direction pointing in the opposite direction
     */
    public Direction getOpposite() {
        return new Direction(-dx, -dy);
    }
    
    /**
     * Rotate this direction by 90 degrees clockwise
     * @return Rotated direction
     */
    public Direction rotateClockwise() {
        return new Direction(-dy, dx);
    }
    
    /**
     * Rotate this direction by 90 degrees counter-clockwise
     * @return Rotated direction
     */
    public Direction rotateCounterClockwise() {
        return new Direction(dy, -dx);
    }
    
    /**
     * Rotate this direction by 180 degrees
     * @return Rotated direction
     */
    public Direction rotate180() {
        return getOpposite();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Direction direction = (Direction) obj;
        return dx == direction.dx && dy == direction.dy;
    }
    
    @Override
    public int hashCode() {
        return 31 * dx + dy;
    }
    
    @Override
    public String toString() {
        if (equals(UP)) return "UP";
        if (equals(DOWN)) return "DOWN";
        if (equals(LEFT)) return "LEFT";
        if (equals(RIGHT)) return "RIGHT";
        if (equals(UP_LEFT)) return "UP_LEFT";
        if (equals(UP_RIGHT)) return "UP_RIGHT";
        if (equals(DOWN_LEFT)) return "DOWN_LEFT";
        if (equals(DOWN_RIGHT)) return "DOWN_RIGHT";
        if (equals(NONE)) return "NONE";
        return "Direction(" + dx + ", " + dy + ")";
    }
} 