package model.domain;

/**
 * Represents a position in pixel coordinates.
 * Used for rendering and UI positioning.
 * Follows OOD principles for better code organization.
 */
public class PixelPosition {
    
    private final float x;
    private final float y;
    
    /**
     * Constructor for PixelPosition
     * @param x X coordinate in pixels
     * @param y Y coordinate in pixels
     */
    public PixelPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Constructor for PixelPosition with integer coordinates
     * @param x X coordinate in pixels
     * @param y Y coordinate in pixels
     */
    public PixelPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Get X coordinate
     * @return X coordinate in pixels
     */
    public float getX() {
        return x;
    }
    
    /**
     * Get Y coordinate
     * @return Y coordinate in pixels
     */
    public float getY() {
        return y;
    }
    
    /**
     * Get X coordinate as integer
     * @return X coordinate as integer
     */
    public int getIntX() {
        return Math.round(x);
    }
    
    /**
     * Get Y coordinate as integer
     * @return Y coordinate as integer
     */
    public int getIntY() {
        return Math.round(y);
    }
    
    /**
     * Add another pixel position
     * @param other Other pixel position
     * @return New pixel position
     */
    public PixelPosition add(PixelPosition other) {
        return new PixelPosition(x + other.x, y + other.y);
    }
    
    /**
     * Subtract another pixel position
     * @param other Other pixel position
     * @return New pixel position
     */
    public PixelPosition subtract(PixelPosition other) {
        return new PixelPosition(x - other.x, y - other.y);
    }
    
    /**
     * Add offset values
     * @param offsetX X offset
     * @param offsetY Y offset
     * @return New pixel position
     */
    public PixelPosition add(float offsetX, float offsetY) {
        return new PixelPosition(x + offsetX, y + offsetY);
    }
    
    /**
     * Subtract offset values
     * @param offsetX X offset
     * @param offsetY Y offset
     * @return New pixel position
     */
    public PixelPosition subtract(float offsetX, float offsetY) {
        return new PixelPosition(x - offsetX, y - offsetY);
    }
    
    /**
     * Scale position by a factor
     * @param scale Scale factor
     * @return New pixel position
     */
    public PixelPosition scale(float scale) {
        return new PixelPosition(x * scale, y * scale);
    }
    
    /**
     * Calculate distance to another pixel position
     * @param other Other pixel position
     * @return Distance in pixels
     */
    public float distanceTo(PixelPosition other) {
        float dx = x - other.x;
        float dy = y - other.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
    
    /**
     * Calculate squared distance to another pixel position (faster than distanceTo)
     * @param other Other pixel position
     * @return Squared distance in pixels
     */
    public float distanceSquaredTo(PixelPosition other) {
        float dx = x - other.x;
        float dy = y - other.y;
        return dx * dx + dy * dy;
    }
    
    /**
     * Check if this position is within a rectangle
     * @param x1 Top-left X coordinate
     * @param y1 Top-left Y coordinate
     * @param x2 Bottom-right X coordinate
     * @param y2 Bottom-right Y coordinate
     * @return true if within rectangle
     */
    public boolean isWithinRectangle(float x1, float y1, float x2, float y2) {
        return x >= x1 && x <= x2 && y >= y1 && y <= y2;
    }
    
    /**
     * Check if this position is within a circle
     * @param center Center of circle
     * @param radius Radius of circle
     * @return true if within circle
     */
    public boolean isWithinCircle(PixelPosition center, float radius) {
        return distanceSquaredTo(center) <= radius * radius;
    }
    
    /**
     * Convert to game position
     * @param tileSize Size of each tile in pixels
     * @return Game position
     */
    public GamePosition toGamePosition(int tileSize) {
        return new GamePosition((int) (x / tileSize), (int) (y / tileSize));
    }
    
    /**
     * Get the center of a tile at this position
     * @param tileSize Size of each tile in pixels
     * @return Center pixel position
     */
    public PixelPosition getTileCenter(int tileSize) {
        float centerX = (float) (Math.floor(x / tileSize) * tileSize + tileSize / 2.0);
        float centerY = (float) (Math.floor(y / tileSize) * tileSize + tileSize / 2.0);
        return new PixelPosition(centerX, centerY);
    }
    
    /**
     * Interpolate between this position and another
     * @param other Other position
     * @param t Interpolation factor (0.0 to 1.0)
     * @return Interpolated position
     */
    public PixelPosition interpolate(PixelPosition other, float t) {
        return new PixelPosition(
            x + (other.x - x) * t,
            y + (other.y - y) * t
        );
    }
    
    /**
     * Create a copy of this position
     * @return New pixel position with same coordinates
     */
    public PixelPosition copy() {
        return new PixelPosition(x, y);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PixelPosition other = (PixelPosition) obj;
        return Float.compare(other.x, x) == 0 && Float.compare(other.y, y) == 0;
    }
    
    @Override
    public int hashCode() {
        return Float.hashCode(x) * 31 + Float.hashCode(y);
    }
    
    @Override
    public String toString() {
        return "PixelPosition(" + x + ", " + y + ")";
    }
} 