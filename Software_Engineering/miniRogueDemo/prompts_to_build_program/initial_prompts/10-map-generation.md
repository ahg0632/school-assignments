## 10-map-generation.md

```
# CRITICAL REQUIREMENTS - Map Generation System Implementation

### MANDATORY DIRECTIVE

You are an expert Java game developer. **CRITICAL**: Implement the Map class with procedural generation exactly as specified below for the Mini Rogue Demo.

## MAP GENERATION SPECIFICATIONS

### **CRITICAL**: Create Map.java in model/map package with this exact implementation:

```
package model.map;

import enums.TileType;
import enums.GameConstants;
import utilities.Position;
import utilities.Tile;
import model.characters.Enemy;
import model.characters.Boss;
import model.characters.Character;
import model.items.Item;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Map class handles procedural generation of dungeon floors.
 * Creates random layouts with rooms, corridors, enemies, and items.
 */
public class Map {
    
    // MANDATORY: Map attributes
    private int width;
    private int height;
    private Tile[][] tiles;
    private List<Room> rooms;
    private List<Position> itemLocations;
    private List<Position> enemyLocations;
    private Position playerStartPosition;
    private Position bossPosition;
    private Random random;
    private int currentFloor;
    
    /**
     * MANDATORY: Constructor for Map
     * 
     * @param floor Current floor number for difficulty scaling
     */
    public Map(int floor) {
        this.width = GameConstants.MAP_WIDTH;
        this.height = GameConstants.MAP_HEIGHT;
        this.tiles = new Tile[width][height];
        this.rooms = new ArrayList<>();
        this.itemLocations = new ArrayList<>();
        this.enemyLocations = new ArrayList<>();
        this.random = new Random();
        this.currentFloor = floor;
        
        generate_dungeon();
    }
    
    /**
     * MANDATORY: Generate complete dungeon floor with rooms and corridors
     */
    public void generate_dungeon() {
        // Initialize all tiles as walls
        initialize_walls();
        
        // Generate rooms
        generate_rooms();
        
        // Connect rooms with corridors
        generate_corridors();
        
        // Place entrance and boss room
        place_special_rooms();
        
        // Place enemies and items
        populate_dungeon();
    }
    
    /**
     * MANDATORY: Initialize entire map as walls
     */
    private void initialize_walls() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = new Tile(TileType.WALL, new Position(x, y));
            }
        }
    }
    
    /**
     * MANDATORY: Generate random rooms throughout the map
     */
    private void generate_rooms() {
        int attempts = 0;
        int maxAttempts = GameConstants.MAX_ROOMS * 3;
        
        while (rooms.size() < GameConstants.MAX_ROOMS && attempts < maxAttempts) {
            attempts++;
            
            // Generate random room dimensions
            int roomWidth = random.nextInt(GameConstants.ROOM_MAX_SIZE - GameConstants.ROOM_MIN_SIZE + 1) + GameConstants.ROOM_MIN_SIZE;
            int roomHeight = random.nextInt(GameConstants.ROOM_MAX_SIZE - GameConstants.ROOM_MIN_SIZE + 1) + GameConstants.ROOM_MIN_SIZE;
            
            // Generate random room position
            int x = random.nextInt(width - roomWidth - 1) + 1;
            int y = random.nextInt(height - roomHeight - 1) + 1;
            
            Room newRoom = new Room(x, y, roomWidth, roomHeight);
            
            // Check if room overlaps with existing rooms
            if (!room_overlaps(newRoom)) {
                rooms.add(newRoom);
                create_room(newRoom);
            }
        }
    }
    
    /**
     * MANDATORY: Check if room overlaps with existing rooms
     * 
     * @param newRoom Room to check for overlap
     * @return true if room overlaps
     */
    private boolean room_overlaps(Room newRoom) {
        for (Room existingRoom : rooms) {
            if (rooms_intersect(newRoom, existingRoom)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * MANDATORY: Check if two rooms intersect
     * 
     * @param room1 First room
     * @param room2 Second room
     * @return true if rooms intersect
     */
    private boolean rooms_intersect(Room room1, Room room2) {
        return room1.x < room2.x + room2.width + 1 &&
               room1.x + room1.width + 1 > room2.x &&
               room1.y < room2.y + room2.height + 1 &&
               room1.y + room1.height + 1 > room2.y;
    }
    
    /**
     * MANDATORY: Create room by setting tiles to floor
     * 
     * @param room Room to create
     */
    private void create_room(Room room) {
        for (int x = room.x; x < room.x + room.width; x++) {
            for (int y = room.y; y < room.y + room.height; y++) {
                tiles[x][y] = new Tile(TileType.FLOOR, new Position(x, y));
            }
        }
    }
    
    /**
     * MANDATORY: Generate corridors connecting all rooms
     */
    private void generate_corridors() {
        for (int i = 1; i < rooms.size(); i++) {
            Room currentRoom = rooms.get(i);
            Room previousRoom = rooms.get(i - 1);
            
            connect_rooms(previousRoom, currentRoom);
        }
    }
    
    /**
     * MANDATORY: Connect two rooms with L-shaped corridor
     * 
     * @param room1 First room
     * @param room2 Second room
     */
    private void connect_rooms(Room room1, Room room2) {
        Position center1 = room1.get_center();
        Position center2 = room2.get_center();
        
        // Create L-shaped corridor
        if (random.nextBoolean()) {
            // Horizontal first, then vertical
            create_horizontal_corridor(center1.get_x(), center2.get_x(), center1.get_y());
            create_vertical_corridor(center1.get_y(), center2.get_y(), center2.get_x());
        } else {
            // Vertical first, then horizontal
            create_vertical_corridor(center1.get_y(), center2.get_y(), center1.get_x());
            create_horizontal_corridor(center1.get_x(), center2.get_x(), center2.get_y());
        }
    }
    
    /**
     * MANDATORY: Create horizontal corridor
     * 
     * @param x1 Start x coordinate
     * @param x2 End x coordinate
     * @param y Y coordinate for corridor
     */
    private void create_horizontal_corridor(int x1, int x2, int y) {
        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        
        for (int x = minX; x <= maxX; x++) {
            if (is_valid_position(x, y)) {
                tiles[x][y] = new Tile(TileType.FLOOR, new Position(x, y));
            }
        }
    }
    
    /**
     * MANDATORY: Create vertical corridor
     * 
     * @param y1 Start y coordinate
     * @param y2 End y coordinate
     * @param x X coordinate for corridor
     */
    private void create_vertical_corridor(int y1, int y2, int x) {
        int minY = Math.min(y1, y2);
        int maxY = Math.max(y1, y2);
        
        for (int y = minY; y <= maxY; y++) {
            if (is_valid_position(x, y)) {
                tiles[x][y] = new Tile(TileType.FLOOR, new Position(x, y));
            }
        }
    }
    
    /**
     * MANDATORY: Place entrance and boss room
     */
    private void place_special_rooms() {
        if (rooms.isEmpty()) return;
        
        // First room is entrance
        Room entranceRoom = rooms.get(0);
        Position entrance = entranceRoom.get_center();
        tiles[entrance.get_x()][entrance.get_y()] = new Tile(TileType.ENTRANCE, entrance);
        playerStartPosition = entrance;
        
        // Last room contains boss
        Room bossRoom = rooms.get(rooms.size() - 1);
        Position bossPos = bossRoom.get_center();
        tiles[bossPos.get_x()][bossPos.get_y()] = new Tile(TileType.BOSS_ROOM, bossPos);
        bossPosition = bossPos;
    }
    
    /**
     * MANDATORY: Populate dungeon with enemies and items
     */
    private void populate_dungeon() {
        place_enemies();
        place_items();
    }
    
    /**
     * MANDATORY: Place enemies randomly in rooms (excluding entrance and boss room)
     */
    private void place_enemies() {
        int enemyCount = Math.min(currentFloor + 3, 8); // Scale with floor
        int placedEnemies = 0;
        
        for (int i = 1; i < rooms.size() - 1 && placedEnemies < enemyCount; i++) {
            Room room = rooms.get(i);
            
            // 70% chance for enemy in each room
            if (random.nextInt(100) < 70) {
                Position enemyPos = get_random_floor_position_in_room(room);
                if (enemyPos != null) {
                    enemyLocations.add(enemyPos);
                    placedEnemies++;
                }
            }
        }
    }
    
    /**
     * MANDATORY: Place items randomly throughout the map
     */
    private void place_items() {
        int itemCount = Math.min(currentFloor + 2, 6); // Scale with floor
        int placedItems = 0;
        
        while (placedItems < itemCount) {
            Room room = rooms.get(random.nextInt(rooms.size()));
            Position itemPos = get_random_floor_position_in_room(room);
            
            if (itemPos != null && !itemLocations.contains(itemPos) && !enemyLocations.contains(itemPos)) {
                itemLocations.add(itemPos);
                placedItems++;
            }
        }
    }
    
    /**
     * MANDATORY: Get random floor position within a room
     * 
     * @param room Room to search
     * @return Random floor position or null if none available
     */
    private Position get_random_floor_position_in_room(Room room) {
        List<Position> floorPositions = new ArrayList<>();
        
        for (int x = room.x; x < room.x + room.width; x++) {
            for (int y = room.y; y < room.y + room.height; y++) {
                if (tiles[x][y].get_tile_type() == TileType.FLOOR) {
                    floorPositions.add(new Position(x, y));
                }
            }
        }
        
        if (floorPositions.isEmpty()) return null;
        return floorPositions.get(random.nextInt(floorPositions.size()));
    }
    
    /**
     * MANDATORY: Get tile at specific coordinates
     * 
     * @param x X coordinate
     * @param y Y coordinate
     * @return Tile at position or null if invalid
     */
    public Tile get_tile(int x, int y) {
        if (is_valid_position(x, y)) {
            return tiles[x][y];
        }
        return null;
    }
    
    /**
     * MANDATORY: Get tile at position
     * 
     * @param position Position to check
     * @return Tile at position or null if invalid
     */
    public Tile get_tile(Position position) {
        return get_tile(position.get_x(), position.get_y());
    }
    
    /**
     * MANDATORY: Check if position is valid for movement
     * 
     * @param position Position to validate
     * @return true if position is valid and walkable
     */
    public boolean is_valid_move(Position position) {
        Tile tile = get_tile(position);
        return tile != null && tile.is_walkable();
    }
    
    /**
     * MANDATORY: Check if coordinates are within map bounds
     * 
     * @param x X coordinate
     * @param y Y coordinate
     * @return true if position is within bounds
     */
    private boolean is_valid_position(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
    
    /**
     * MANDATORY: Place item at specific position
     * 
     * @param item Item to place
     * @param position Position to place item
     */
    public void place_item(Item item, Position position) {
        Tile tile = get_tile(position);
        if (tile != null && tile.is_walkable()) {
            tile.add_item(item);
        }
    }
    
    /**
     * MANDATORY: Place enemy at specific position
     * 
     * @param enemy Enemy to place
     * @param position Position to place enemy
     */
    public void place_enemy(Enemy enemy, Position position) {
        Tile tile = get_tile(position);
        if (tile != null && tile.is_walkable()) {
            tile.set_occupant(enemy);
            enemy.move_to(position);
        }
    }
    
    // MANDATORY: Getters
    public int get_width() { return width; }
    public int get_height() { return height; }
    public List<Position> get_item_locations() { return new ArrayList<>(itemLocations); }
    public List<Position> get_enemy_locations() { return new ArrayList<>(enemyLocations); }
    public Position get_player_start_position() { return playerStartPosition; }
    public Position get_boss_position() { return bossPosition; }
    public int get_current_floor() { return currentFloor; }
    public List<Room> get_rooms() { return new ArrayList<>(rooms); }
    
    /**
     * MANDATORY: Inner class representing a room
     */
    public static class Room {
        public final int x, y, width, height;
        
        public Room(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
        
        public Position get_center() {
            return new Position(x + width / 2, y + height / 2);
        }
        
        public boolean contains_position(Position position) {
            return position.get_x() >= x && position.get_x() < x + width &&
                   position.get_y() >= y && position.get_y() < y + height;
        }
    }
}
```


### **MANDATORY**: Map Generation Requirements

1. **CRITICAL**: Procedural generation creates unique layouts each run
2. **CRITICAL**: Room-based generation with corridor connections
3. **CRITICAL**: Proper entrance and boss room placement
4. **CRITICAL**: Enemy and item distribution scales with floor number
5. **CRITICAL**: Collision detection and movement validation

### **MANDATORY**: Algorithm Implementation

**CRITICAL**: Generation process:

1. **Initialize**: Fill entire map with wall tiles
2. **Room Generation**: Create random non-overlapping rooms
3. **Corridor Creation**: Connect all rooms with L-shaped corridors
4. **Special Placement**: Mark entrance and boss room locations
5. **Population**: Place enemies and items with floor-based scaling

### **MANDATORY**: Verification Steps

1. **CRITICAL**: Map class compiles successfully in model/map package
2. **CRITICAL**: Room generation creates non-overlapping spaces
3. **CRITICAL**: Corridors properly connect all rooms
4. **CRITICAL**: Enemy and item placement follows scaling rules
5. **CRITICAL**: Movement validation prevents walking through walls
6. **CRITICAL**: Special rooms (entrance, boss) are properly marked

### CRITICAL REQUIREMENT

**MANDATORY**: The Map generation system must be implemented exactly as specified above. This system provides the foundation for all dungeon exploration and ensures unique experiences for each playthrough of the Mini Rogue Demo.

```