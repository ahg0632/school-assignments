package model.domain;

/**
 * Represents a rectangular bounds area with game-specific functionality.
 * Encapsulates rectangle operations and provides utility methods.
 * Follows OOD principles for better code organization.
 */
public class Bounds {
    
    private final float x;
    private final float y;
    private final float width;
    private final float height;
    
    /**
     * Constructor for Bounds
     * @param x X coordinate of top-left corner
     * @param y Y coordinate of top-left corner
     * @param width Width of bounds
     * @param height Height of bounds
     */
    public Bounds(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    /**
     * Constructor for Bounds from two points
     * @param topLeft Top-left corner
     * @param bottomRight Bottom-right corner
     */
    public Bounds(PixelPosition topLeft, PixelPosition bottomRight) {
        this.x = topLeft.getX();
        this.y = topLeft.getY();
        this.width = bottomRight.getX() - topLeft.getX();
        this.height = bottomRight.getY() - topLeft.getY();
    }
    
    /**
     * Get X coordinate
     * @return X coordinate of top-left corner
     */
    public float getX() {
        return x;
    }
    
    /**
     * Get Y coordinate
     * @return Y coordinate of top-left corner
     */
    public float getY() {
        return y;
    }
    
    /**
     * Get width
     * @return Width of bounds
     */
    public float getWidth() {
        return width;
    }
    
    /**
     * Get height
     * @return Height of bounds
     */
    public float getHeight() {
        return height;
    }
    
    /**
     * Get right edge X coordinate
     * @return Right edge X coordinate
     */
    public float getRight() {
        return x + width;
    }
    
    /**
     * Get bottom edge Y coordinate
     * @return Bottom edge Y coordinate
     */
    public float getBottom() {
        return y + height;
    }
    
    /**
     * Get center X coordinate
     * @return Center X coordinate
     */
    public float getCenterX() {
        return x + width / 2.0f;
    }
    
    /**
     * Get center Y coordinate
     * @return Center Y coordinate
     */
    public float getCenterY() {
        return y + height / 2.0f;
    }
    
    /**
     * Get center position
     * @return Center pixel position
     */
    public PixelPosition getCenter() {
        return new PixelPosition(getCenterX(), getCenterY());
    }
    
    /**
     * Get top-left corner
     * @return Top-left pixel position
     */
    public PixelPosition getTopLeft() {
        return new PixelPosition(x, y);
    }
    
    /**
     * Get top-right corner
     * @return Top-right pixel position
     */
    public PixelPosition getTopRight() {
        return new PixelPosition(getRight(), y);
    }
    
    /**
     * Get bottom-left corner
     * @return Bottom-left pixel position
     */
    public PixelPosition getBottomLeft() {
        return new PixelPosition(x, getBottom());
    }
    
    /**
     * Get bottom-right corner
     * @return Bottom-right pixel position
     */
    public PixelPosition getBottomRight() {
        return new PixelPosition(getRight(), getBottom());
    }
    
    /**
     * Check if a position is within these bounds
     * @param position Position to check
     * @return true if position is within bounds
     */
    public boolean contains(PixelPosition position) {
        return position.getX() >= x && position.getX() <= getRight() &&
               position.getY() >= y && position.getY() <= getBottom();
    }
    
    /**
     * Check if a position is within these bounds
     * @param px X coordinate
     * @param py Y coordinate
     * @return true if position is within bounds
     */
    public boolean contains(float px, float py) {
        return px >= x && px <= getRight() && py >= y && py <= getBottom();
    }
    
    /**
     * Check if these bounds intersect with another bounds
     * @param other Other bounds
     * @return true if bounds intersect
     */
    public boolean intersects(Bounds other) {
        return !(getRight() < other.x || x > other.getRight() ||
                getBottom() < other.y || y > other.getBottom());
    }
    
    /**
     * Get the intersection of these bounds with another bounds
     * @param other Other bounds
     * @return Intersection bounds, or null if no intersection
     */
    public Bounds getIntersection(Bounds other) {
        if (!intersects(other)) {
            return null;
        }
        
        float intersectionX = Math.max(x, other.x);
        float intersectionY = Math.max(y, other.y);
        float intersectionRight = Math.min(getRight(), other.getRight());
        float intersectionBottom = Math.min(getBottom(), other.getBottom());
        
        return new Bounds(
            intersectionX,
            intersectionY,
            intersectionRight - intersectionX,
            intersectionBottom - intersectionY
        );
    }
    
    /**
     * Get the union of these bounds with another bounds
     * @param other Other bounds
     * @return Union bounds
     */
    public Bounds getUnion(Bounds other) {
        float unionX = Math.min(x, other.x);
        float unionY = Math.min(y, other.y);
        float unionRight = Math.max(getRight(), other.getRight());
        float unionBottom = Math.max(getBottom(), other.getBottom());
        
        return new Bounds(
            unionX,
            unionY,
            unionRight - unionX,
            unionBottom - unionY
        );
    }
    
    /**
     * Expand bounds by a margin
     * @param margin Margin to expand by
     * @return Expanded bounds
     */
    public Bounds expand(float margin) {
        return new Bounds(x - margin, y - margin, width + 2 * margin, height + 2 * margin);
    }
    
    /**
     * Expand bounds by different margins for each side
     * @param left Left margin
     * @param top Top margin
     * @param right Right margin
     * @param bottom Bottom margin
     * @return Expanded bounds
     */
    public Bounds expand(float left, float top, float right, float bottom) {
        return new Bounds(x - left, y - top, width + left + right, height + top + bottom);
    }
    
    /**
     * Scale bounds by a factor
     * @param scale Scale factor
     * @return Scaled bounds
     */
    public Bounds scale(float scale) {
        return new Bounds(x * scale, y * scale, width * scale, height * scale);
    }
    
    /**
     * Move bounds by an offset
     * @param offsetX X offset
     * @param offsetY Y offset
     * @return Moved bounds
     */
    public Bounds move(float offsetX, float offsetY) {
        return new Bounds(x + offsetX, y + offsetY, width, height);
    }
    
    /**
     * Move bounds to a new position
     * @param newX New X coordinate
     * @param newY New Y coordinate
     * @return Moved bounds
     */
    public Bounds moveTo(float newX, float newY) {
        return new Bounds(newX, newY, width, height);
    }
    
    /**
     * Center bounds around a position
     * @param center Center position
     * @return Centered bounds
     */
    public Bounds centerOn(PixelPosition center) {
        return new Bounds(
            center.getX() - width / 2.0f,
            center.getY() - height / 2.0f,
            width,
            height
        );
    }
    
    /**
     * Get area of bounds
     * @return Area in square pixels
     */
    public float getArea() {
        return width * height;
    }
    
    /**
     * Check if bounds are empty (zero width or height)
     * @return true if bounds are empty
     */
    public boolean isEmpty() {
        return width <= 0 || height <= 0;
    }
    
    /**
     * Create bounds from center and size
     * @param center Center position
     * @param width Width
     * @param height Height
     * @return New bounds
     */
    public static Bounds fromCenter(PixelPosition center, float width, float height) {
        return new Bounds(
            center.getX() - width / 2.0f,
            center.getY() - height / 2.0f,
            width,
            height
        );
    }
    
    /**
     * Create bounds from center and size
     * @param centerX Center X coordinate
     * @param centerY Center Y coordinate
     * @param width Width
     * @param height Height
     * @return New bounds
     */
    public static Bounds fromCenter(float centerX, float centerY, float width, float height) {
        return new Bounds(centerX - width / 2.0f, centerY - height / 2.0f, width, height);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Bounds bounds = (Bounds) obj;
        return Float.compare(bounds.x, x) == 0 &&
               Float.compare(bounds.y, y) == 0 &&
               Float.compare(bounds.width, width) == 0 &&
               Float.compare(bounds.height, height) == 0;
    }
    
    @Override
    public int hashCode() {
        return Float.hashCode(x) * 31 + Float.hashCode(y) * 31 + 
               Float.hashCode(width) * 31 + Float.hashCode(height);
    }
    
    @Override
    public String toString() {
        return "Bounds(" + x + ", " + y + ", " + width + ", " + height + ")";
    }
} 