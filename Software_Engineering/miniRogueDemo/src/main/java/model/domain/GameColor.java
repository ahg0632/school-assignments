package model.domain;

import java.awt.Color;

/**
 * Represents game-specific colors with semantic meaning.
 * Encapsulates color values and provides utility methods.
 * Follows OOD principles for better code organization.
 */
public class GameColor {
    
    // UI Colors
    public static final GameColor UI_BACKGROUND = new GameColor(40, 40, 40);
    public static final GameColor UI_PANEL = new GameColor(60, 60, 60);
    public static final GameColor UI_BORDER = new GameColor(80, 80, 80);
    public static final GameColor UI_TEXT = new GameColor(220, 220, 220);
    public static final GameColor UI_TEXT_DIM = new GameColor(150, 150, 150);
    public static final GameColor UI_HIGHLIGHT = new GameColor(100, 150, 255);
    public static final GameColor UI_WARNING = new GameColor(255, 200, 100);
    public static final GameColor UI_ERROR = new GameColor(255, 100, 100);
    public static final GameColor UI_SUCCESS = new GameColor(100, 255, 100);
    
    // Health Colors
    public static final GameColor HEALTH_FULL = new GameColor(100, 255, 100);
    public static final GameColor HEALTH_HIGH = new GameColor(255, 255, 100);
    public static final GameColor HEALTH_MEDIUM = new GameColor(255, 200, 100);
    public static final GameColor HEALTH_LOW = new GameColor(255, 100, 100);
    public static final GameColor HEALTH_CRITICAL = new GameColor(255, 50, 50);
    
    // Mana Colors
    public static final GameColor MANA_FULL = new GameColor(100, 100, 255);
    public static final GameColor MANA_HIGH = new GameColor(150, 150, 255);
    public static final GameColor MANA_MEDIUM = new GameColor(200, 150, 255);
    public static final GameColor MANA_LOW = new GameColor(255, 100, 255);
    public static final GameColor MANA_CRITICAL = new GameColor(255, 50, 255);
    
    // Status Effect Colors
    public static final GameColor STATUS_POSITIVE = new GameColor(100, 255, 100);
    public static final GameColor STATUS_NEGATIVE = new GameColor(255, 100, 100);
    public static final GameColor STATUS_NEUTRAL = new GameColor(200, 200, 200);
    public static final GameColor STATUS_BUFF = new GameColor(100, 255, 255);
    public static final GameColor STATUS_DEBUFF = new GameColor(255, 100, 255);
    
    // Combat Colors
    public static final GameColor DAMAGE_PHYSICAL = new GameColor(255, 100, 100);
    public static final GameColor DAMAGE_MAGICAL = new GameColor(100, 100, 255);
    public static final GameColor DAMAGE_CRITICAL = new GameColor(255, 255, 100);
    public static final GameColor HEAL = new GameColor(100, 255, 100);
    public static final GameColor SHIELD = new GameColor(100, 255, 255);
    
    // Map Colors
    public static final GameColor MAP_WALL = new GameColor(80, 80, 80);
    public static final GameColor MAP_FLOOR = new GameColor(120, 120, 120);
    public static final GameColor MAP_DOOR = new GameColor(150, 100, 50);
    public static final GameColor MAP_STAIRS = new GameColor(100, 150, 100);
    public static final GameColor MAP_ITEM = new GameColor(255, 255, 100);
    public static final GameColor MAP_ENEMY = new GameColor(255, 100, 100);
    public static final GameColor MAP_PLAYER = new GameColor(100, 100, 255);
    
    // Effect Colors
    public static final GameColor EFFECT_FIRE = new GameColor(255, 100, 50);
    public static final GameColor EFFECT_ICE = new GameColor(100, 200, 255);
    public static final GameColor EFFECT_LIGHTNING = new GameColor(255, 255, 100);
    public static final GameColor EFFECT_POISON = new GameColor(100, 255, 100);
    public static final GameColor EFFECT_HOLY = new GameColor(255, 255, 200);
    public static final GameColor EFFECT_DARK = new GameColor(100, 50, 100);
    
    // Debug Colors
    public static final GameColor DEBUG_PATH = new GameColor(255, 255, 100, 128);
    public static final GameColor DEBUG_RANGE = new GameColor(100, 255, 100, 64);
    public static final GameColor DEBUG_COLLISION = new GameColor(255, 100, 100, 128);
    public static final GameColor DEBUG_AI = new GameColor(100, 100, 255, 128);
    
    private final int red;
    private final int green;
    private final int blue;
    private final int alpha;
    private final Color awtColor;
    
    /**
     * Constructor for GameColor with RGB values
     * @param red Red component (0-255)
     * @param green Green component (0-255)
     * @param blue Blue component (0-255)
     */
    public GameColor(int red, int green, int blue) {
        this(red, green, blue, 255);
    }
    
    /**
     * Constructor for GameColor with RGBA values
     * @param red Red component (0-255)
     * @param green Green component (0-255)
     * @param blue Blue component (0-255)
     * @param alpha Alpha component (0-255)
     */
    public GameColor(int red, int green, int blue, int alpha) {
        this.red = clamp(red, 0, 255);
        this.green = clamp(green, 0, 255);
        this.blue = clamp(blue, 0, 255);
        this.alpha = clamp(alpha, 0, 255);
        this.awtColor = new Color(this.red, this.green, this.blue, this.alpha);
    }
    
    /**
     * Constructor for GameColor from AWT Color
     * @param color AWT Color object
     */
    public GameColor(Color color) {
        this.red = color.getRed();
        this.green = color.getGreen();
        this.blue = color.getBlue();
        this.alpha = color.getAlpha();
        this.awtColor = color;
    }
    
    /**
     * Constructor for GameColor from hex string
     * @param hex Hex color string (e.g., "#FF0000" or "FF0000")
     */
    public GameColor(String hex) {
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }
        
        if (hex.length() == 6) {
            this.red = Integer.parseInt(hex.substring(0, 2), 16);
            this.green = Integer.parseInt(hex.substring(2, 4), 16);
            this.blue = Integer.parseInt(hex.substring(4, 6), 16);
            this.alpha = 255;
        } else if (hex.length() == 8) {
            this.red = Integer.parseInt(hex.substring(0, 2), 16);
            this.green = Integer.parseInt(hex.substring(2, 4), 16);
            this.blue = Integer.parseInt(hex.substring(4, 6), 16);
            this.alpha = Integer.parseInt(hex.substring(6, 8), 16);
        } else {
            throw new IllegalArgumentException("Invalid hex color format: " + hex);
        }
        
        this.awtColor = new Color(this.red, this.green, this.blue, this.alpha);
    }
    
    /**
     * Get red component
     * @return Red component (0-255)
     */
    public int getRed() {
        return red;
    }
    
    /**
     * Get green component
     * @return Green component (0-255)
     */
    public int getGreen() {
        return green;
    }
    
    /**
     * Get blue component
     * @return Blue component (0-255)
     */
    public int getBlue() {
        return blue;
    }
    
    /**
     * Get alpha component
     * @return Alpha component (0-255)
     */
    public int getAlpha() {
        return alpha;
    }
    
    /**
     * Get AWT Color object
     * @return AWT Color object
     */
    public Color getAwtColor() {
        return awtColor;
    }
    
    /**
     * Check if color is transparent
     * @return true if alpha is less than 255
     */
    public boolean isTransparent() {
        return alpha < 255;
    }
    
    /**
     * Check if color is opaque
     * @return true if alpha is 255
     */
    public boolean isOpaque() {
        return alpha == 255;
    }
    
    /**
     * Get brightness of color (0.0 to 1.0)
     * @return Brightness value
     */
    public float getBrightness() {
        return (red * 0.299f + green * 0.587f + blue * 0.114f) / 255.0f;
    }
    
    /**
     * Check if color is dark
     * @return true if brightness is less than 0.5
     */
    public boolean isDark() {
        return getBrightness() < 0.5f;
    }
    
    /**
     * Check if color is light
     * @return true if brightness is greater than or equal to 0.5
     */
    public boolean isLight() {
        return getBrightness() >= 0.5f;
    }
    
    /**
     * Create a darker version of this color
     * @param factor Darkening factor (0.0 to 1.0)
     * @return Darker color
     */
    public GameColor darker(float factor) {
        return new GameColor(
            (int) (red * (1.0f - factor)),
            (int) (green * (1.0f - factor)),
            (int) (blue * (1.0f - factor)),
            alpha
        );
    }
    
    /**
     * Create a lighter version of this color
     * @param factor Lightening factor (0.0 to 1.0)
     * @return Lighter color
     */
    public GameColor lighter(float factor) {
        return new GameColor(
            (int) (red + (255 - red) * factor),
            (int) (green + (255 - green) * factor),
            (int) (blue + (255 - blue) * factor),
            alpha
        );
    }
    
    /**
     * Create a version with different alpha
     * @param newAlpha New alpha value (0-255)
     * @return Color with new alpha
     */
    public GameColor withAlpha(int newAlpha) {
        return new GameColor(red, green, blue, newAlpha);
    }
    
    /**
     * Create a version with different alpha
     * @param alphaFactor Alpha factor (0.0 to 1.0)
     * @return Color with new alpha
     */
    public GameColor withAlpha(float alphaFactor) {
        return withAlpha((int) (alpha * alphaFactor));
    }
    
    /**
     * Blend this color with another color
     * @param other Other color
     * @param blendFactor Blend factor (0.0 to 1.0)
     * @return Blended color
     */
    public GameColor blend(GameColor other, float blendFactor) {
        return new GameColor(
            (int) (red * (1.0f - blendFactor) + other.red * blendFactor),
            (int) (green * (1.0f - blendFactor) + other.green * blendFactor),
            (int) (blue * (1.0f - blendFactor) + other.blue * blendFactor),
            (int) (alpha * (1.0f - blendFactor) + other.alpha * blendFactor)
        );
    }
    
    /**
     * Get health color based on percentage
     * @param percentage Health percentage (0.0 to 1.0)
     * @return Appropriate health color
     */
    public static GameColor getHealthColor(float percentage) {
        if (percentage >= 0.8f) return HEALTH_FULL;
        if (percentage >= 0.6f) return HEALTH_HIGH;
        if (percentage >= 0.4f) return HEALTH_MEDIUM;
        if (percentage >= 0.2f) return HEALTH_LOW;
        return HEALTH_CRITICAL;
    }
    
    /**
     * Get mana color based on percentage
     * @param percentage Mana percentage (0.0 to 1.0)
     * @return Appropriate mana color
     */
    public static GameColor getManaColor(float percentage) {
        if (percentage >= 0.8f) return MANA_FULL;
        if (percentage >= 0.6f) return MANA_HIGH;
        if (percentage >= 0.4f) return MANA_MEDIUM;
        if (percentage >= 0.2f) return MANA_LOW;
        return MANA_CRITICAL;
    }
    
    /**
     * Clamp a value between min and max
     * @param value Value to clamp
     * @param min Minimum value
     * @param max Maximum value
     * @return Clamped value
     */
    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
    
    /**
     * Get hex string representation
     * @return Hex color string
     */
    public String toHexString() {
        return String.format("#%02X%02X%02X", red, green, blue);
    }
    
    /**
     * Get hex string representation with alpha
     * @return Hex color string with alpha
     */
    public String toHexStringWithAlpha() {
        return String.format("#%02X%02X%02X%02X", red, green, blue, alpha);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        GameColor gameColor = (GameColor) obj;
        return red == gameColor.red &&
               green == gameColor.green &&
               blue == gameColor.blue &&
               alpha == gameColor.alpha;
    }
    
    @Override
    public int hashCode() {
        return 31 * (31 * (31 * red + green) + blue) + alpha;
    }
    
    @Override
    public String toString() {
        return "GameColor(" + red + ", " + green + ", " + blue + ", " + alpha + ")";
    }
} 