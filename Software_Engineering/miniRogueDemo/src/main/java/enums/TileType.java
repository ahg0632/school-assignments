package enums;

/**
 * Enumeration defining all tile types in dungeon maps.
 * Used by Map class for procedural generation and collision detection.
 */
public enum TileType {
    WALL("wall", false),
    FLOOR("floor", true),
    DOOR("door", true),
    STAIRS("stairs", true),
    ENTRANCE("entrance", true),
    BOSS_ROOM("bossRoom", true),
    UPGRADER_SPAWN("upgraderSpawn", true);

    private final String typeName;
    private final boolean walkable;

    TileType(String typeName, boolean walkable) {
        this.typeName = typeName;
        this.walkable = walkable;
    }

    public String get_type_name() {
        return typeName;
    }

    public boolean is_walkable() {
        return walkable;
    }
} 