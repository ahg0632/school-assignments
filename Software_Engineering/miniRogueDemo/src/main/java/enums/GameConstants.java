package enums;

/**
 * Static constants used throughout the Mini Rogue Demo game.
 * Centralizes configuration values for easy modification.
 */
public final class GameConstants {

    // MANDATORY: Map Configuration
    public static final int MAP_WIDTH = 50;
    public static final int MAP_HEIGHT = 30;
    public static final int ROOM_MIN_SIZE = 4;
    public static final int ROOM_MAX_SIZE = 10;
    public static final int MAX_ROOMS = 15;

    // MANDATORY: Game Balance
    public static final int STARTING_LEVEL = 1;
    public static final int MAX_LEVEL = 20;
    public static final int BASE_EXPERIENCE = 100;
    public static final int EXPERIENCE_MULTIPLIER = 150;

    // MANDATORY: Combat Constants
    public static final int CRITICAL_HIT_CHANCE = 10; // 10%
    public static final double CRITICAL_HIT_MULTIPLIER = 1.5;
    public static final int MISS_CHANCE = 5; // 5%

    // MANDATORY: UI Constants
    public static final int SCALING_FACTOR = 2;
    public static final int WINDOW_WIDTH = 1150; // Reduced from 1200 to 1150 to bring border further in
    public static final int WINDOW_HEIGHT = 768;
    public static final int TILE_SIZE = 16 * SCALING_FACTOR;
    public static final int UI_PANEL_HEIGHT = 200;

    // MANDATORY: Thread Configuration
    public static final int ENEMY_AI_DELAY = 500; // milliseconds
    public static final int GAME_UPDATE_DELAY = 16; // ~60 FPS

    // MANDATORY: Inventory Configuration
    public static final int UNLIMITED_INVENTORY = -1;
    public static final int MAX_EQUIPMENT_LEVEL = 5;

    private GameConstants() {
        // Prevent instantiation
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
} 