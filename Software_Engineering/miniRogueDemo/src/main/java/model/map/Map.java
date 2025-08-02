package model.map;

import enums.GameConstants;
import enums.TileType;
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
 * MANDATORY: Map class for procedural dungeon generation
 */
public class Map {
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
    
    // Special floor type tracking
    public enum FloorType {
        REGULAR, BOSS, BONUS
    }
    private FloorType floorType = FloorType.REGULAR;

    /**
     * MANDATORY: Constructor for Map
     *
     * @param floor Current floor number for difficulty scaling
     * @param floorType Type of floor to generate (REGULAR, BOSS, BONUS)
     */
    public Map(int floor, FloorType floorType) {
        this.width = GameConstants.MAP_WIDTH;
        this.height = GameConstants.MAP_HEIGHT;
        this.tiles = new Tile[width][height];
        this.rooms = new ArrayList<>();
        this.itemLocations = new ArrayList<>();
        this.enemyLocations = new ArrayList<>();
        this.random = new Random();
        this.currentFloor = floor;
        this.floorType = floorType;
        generate_dungeon();
    }
    
    /**
     * Constructor for Map (backward compatibility)
     *
     * @param floor Current floor number for difficulty scaling
     */
    public Map(int floor) {
        this(floor, FloorType.REGULAR);
    }

    /**
     * MANDATORY: Generate complete dungeon floor with rooms and corridors
     */
    public void generate_dungeon() {
        // Initialize all tiles as walls
        initialize_walls();
        // Create dedicated player spawn room first
        create_player_spawn_room();
        
        // Generate rooms based on floor type
        switch (floorType) {
            case BOSS:
                generate_boss_floor();
                break;
            case BONUS:
                generate_bonus_floor();
                break;
            default:
                generate_regular_floor();
                break;
        }
    }
    
    /**
     * Generate BOSS floor (fewer, bigger rooms)
     */
    private void generate_boss_floor() {
        // Generate fewer, bigger rooms for boss floor
        generate_boss_rooms();
        // Connect rooms with corridors
        generate_corridors();
        // Place boss room (guaranteed)
        place_boss_room();
        // Place stairs in the same room as boss
        place_stairs();
        // No upgrader on boss floors (only on bonus floors)
        // Place fewer enemies and items
        populate_boss_dungeon();
    }
    
    /**
     * Generate BONUS floor (3-room layout with wide corridors)
     */
    private void generate_bonus_floor() {
        // Generate 3 rooms for bonus floor (spawn, upgrader, exit)
        generate_bonus_rooms();
        // Connect rooms with wide corridors
        generate_corridors_bonus();
        // No boss room on bonus floor
        // Place stairs in the exit room (third room)
        place_bonus_stairs();
        // Place upgrader in the middle room (second room)
        place_bonus_upgrader();
        // No enemies, only Floor Key
        populate_bonus_dungeon();
    }
    
    /**
     * Generate regular floor (normal layout)
     */
    private void generate_regular_floor() {
        // Generate remaining rooms
        generate_rooms();
        // Connect rooms with corridors
        generate_corridors();
        // No boss room on regular floors
        // Place stairs in a separate room
        place_regular_stairs();
        // No upgrader on regular floors (only on bonus floors)
        // Place enemies and items normally
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
     * Create a dedicated player spawn room (small and safe)
     */
    private void create_player_spawn_room() {
        // Create a small spawn room (5x5) in the top-left area
        int spawnRoomSize = 5;
        int x = 2; // Leave 1 tile border from edge
        int y = 2; // Leave 1 tile border from edge
        
        Room spawnRoom = new Room(x, y, spawnRoomSize, spawnRoomSize);
        rooms.add(spawnRoom);
        create_room(spawnRoom);
        
        // Set player start position in the center of spawn room
        playerStartPosition = spawnRoom.get_center();
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
     * Generate BOSS rooms (fewer, bigger rooms)
     */
    private void generate_boss_rooms() {
        int attempts = 0;
        int maxAttempts = (GameConstants.MAX_ROOMS / 2) * 3; // Fewer rooms
        while (rooms.size() < (GameConstants.MAX_ROOMS / 2) && attempts < maxAttempts) {
            attempts++;
            // Generate bigger room dimensions for boss floor
            int roomWidth = random.nextInt(GameConstants.ROOM_MAX_SIZE + 2 - GameConstants.ROOM_MIN_SIZE + 1) + GameConstants.ROOM_MIN_SIZE;
            int roomHeight = random.nextInt(GameConstants.ROOM_MAX_SIZE + 2 - GameConstants.ROOM_MIN_SIZE + 1) + GameConstants.ROOM_MIN_SIZE;
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
     * Generate BONUS rooms (3-room layout: spawn, upgrader, exit)
     */
    private void generate_bonus_rooms() {
        // Generate 2 additional rooms for bonus floor (spawn + upgrader + exit)
        int attempts = 0;
        int maxAttempts = 30; // Increase attempts for bonus floors
        
        // Generate upgrader room (bigger size)
        while (rooms.size() < 2 && attempts < maxAttempts) {
            attempts++;
            // Generate bigger room for upgrader
            int roomWidth = random.nextInt(6 - 5 + 1) + 5; // 5-6 width
            int roomHeight = random.nextInt(6 - 5 + 1) + 5; // 5-6 height
            // Generate random room position
            int x = random.nextInt(width - roomWidth - 1) + 1;
            int y = random.nextInt(height - roomHeight - 1) + 1;
            Room newRoom = new Room(x, y, roomWidth, roomHeight);
            // Check if room overlaps with existing rooms (use reduced safe distance for bonus floors)
            if (!room_overlaps_bonus(newRoom)) {
                rooms.add(newRoom);
                create_room(newRoom);
            }
        }
        
        // Generate exit room (larger size)
        attempts = 0;
        while (rooms.size() < 3 && attempts < maxAttempts) {
            attempts++;
            // Generate larger room for exit
            int roomWidth = random.nextInt(6 - 5 + 1) + 5; // 5-6 width
            int roomHeight = random.nextInt(6 - 5 + 1) + 5; // 5-6 height
            // Generate random room position
            int x = random.nextInt(width - roomWidth - 1) + 1;
            int y = random.nextInt(height - roomHeight - 1) + 1;
            Room newRoom = new Room(x, y, roomWidth, roomHeight);
            // Check if room overlaps with existing rooms (use reduced safe distance for bonus floors)
            if (!room_overlaps_bonus(newRoom)) {
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
        
        // Additional check: ensure new rooms don't get too close to player spawn
        // This creates a safe zone around the player spawn room
        if (playerStartPosition != null) {
            int safeDistance = 8; // Minimum distance from player spawn
            Position newRoomCenter = newRoom.get_center();
            double distance = Math.sqrt(
                Math.pow(newRoomCenter.get_x() - playerStartPosition.get_x(), 2) + 
                Math.pow(newRoomCenter.get_y() - playerStartPosition.get_y(), 2)
            );
            if (distance < safeDistance) {
                return true; // Too close to player spawn
            }
        }
        
        return false;
    }
    
    /**
     * Check if room overlaps with existing rooms (bonus floor version with reduced safe distance)
     *
     * @param newRoom Room to check for overlap
     * @return true if room overlaps
     */
    private boolean room_overlaps_bonus(Room newRoom) {
        for (Room existingRoom : rooms) {
            if (rooms_intersect(newRoom, existingRoom)) {
                return true;
            }
        }
        
        // Reduced safe distance for bonus floors to ensure room placement
        if (playerStartPosition != null) {
            int safeDistance = 4; // Reduced distance for bonus floors
            Position newRoomCenter = newRoom.get_center();
            double distance = Math.sqrt(
                Math.pow(newRoomCenter.get_x() - playerStartPosition.get_x(), 2) + 
                Math.pow(newRoomCenter.get_y() - playerStartPosition.get_y(), 2)
            );
            if (distance < safeDistance) {
                return true; // Too close to player spawn
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
     * Generate corridors for bonus floors (3 tiles wide)
     */
    private void generate_corridors_bonus() {
        for (int i = 1; i < rooms.size(); i++) {
            Room currentRoom = rooms.get(i);
            Room previousRoom = rooms.get(i - 1);
            connect_rooms_bonus(previousRoom, currentRoom);
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
     * Connect two rooms with L-shaped corridor for bonus floors (3 tiles wide)
     *
     * @param room1 First room
     * @param room2 Second room
     */
    private void connect_rooms_bonus(Room room1, Room room2) {
        Position center1 = room1.get_center();
        Position center2 = room2.get_center();
        // Create L-shaped corridor
        if (random.nextBoolean()) {
            // Horizontal first, then vertical
            create_horizontal_corridor_bonus(center1.get_x(), center2.get_x(), center1.get_y());
            create_vertical_corridor_bonus(center1.get_y(), center2.get_y(), center2.get_x());
        } else {
            // Vertical first, then horizontal
            create_vertical_corridor_bonus(center1.get_y(), center2.get_y(), center1.get_x());
            create_horizontal_corridor_bonus(center1.get_x(), center2.get_x(), center2.get_y());
        }
    }

    /**
     * MANDATORY: Create horizontal corridor (2 tiles wide)
     *
     * @param x1 Start x coordinate
     * @param x2 End x coordinate
     * @param y Y coordinate for corridor
     */
    private void create_horizontal_corridor(int x1, int x2, int y) {
        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        for (int x = minX; x <= maxX; x++) {
            // Create 2-tile wide corridor
            for (int offset = 0; offset < 2; offset++) {
                if (is_valid_position(x, y + offset)) {
                    tiles[x][y + offset] = new Tile(TileType.FLOOR, new Position(x, y + offset));
                }
            }
        }
    }

    /**
     * MANDATORY: Create vertical corridor (2 tiles wide)
     *
     * @param y1 Start y coordinate
     * @param y2 End y coordinate
     * @param x X coordinate for corridor
     */
    private void create_vertical_corridor(int y1, int y2, int x) {
        int minY = Math.min(y1, y2);
        int maxY = Math.max(y1, y2);
        for (int y = minY; y <= maxY; y++) {
            // Create 2-tile wide corridor
            for (int offset = 0; offset < 2; offset++) {
                if (is_valid_position(x + offset, y)) {
                    tiles[x + offset][y] = new Tile(TileType.FLOOR, new Position(x + offset, y));
                }
            }
        }
    }
    
    /**
     * Create horizontal corridor for bonus floors (3 tiles wide)
     *
     * @param x1 Start x coordinate
     * @param x2 End x coordinate
     * @param y Y coordinate for corridor
     */
    private void create_horizontal_corridor_bonus(int x1, int x2, int y) {
        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        for (int x = minX; x <= maxX; x++) {
            // Create 3-tile wide corridor
            for (int offset = 0; offset < 3; offset++) {
                if (is_valid_position(x, y + offset)) {
                    tiles[x][y + offset] = new Tile(TileType.FLOOR, new Position(x, y + offset));
                }
            }
        }
    }

    /**
     * Create vertical corridor for bonus floors (3 tiles wide)
     *
     * @param y1 Start y coordinate
     * @param y2 End y coordinate
     * @param x X coordinate for corridor
     */
    private void create_vertical_corridor_bonus(int y1, int y2, int x) {
        int minY = Math.min(y1, y2);
        int maxY = Math.max(y1, y2);
        for (int y = minY; y <= maxY; y++) {
            // Create 3-tile wide corridor
            for (int offset = 0; offset < 3; offset++) {
                if (is_valid_position(x + offset, y)) {
                    tiles[x + offset][y] = new Tile(TileType.FLOOR, new Position(x + offset, y));
                }
            }
        }
    }

    /**
     * MANDATORY: Place boss room
     */
    private void place_boss_room() {
        if (rooms.size() < 2) return; // Need at least spawn room + 1 other room
        
        // Find the largest room for the boss (excluding spawn room)
        Room largestRoom = find_largest_room_excluding_spawn();
        if (largestRoom == null) {
            largestRoom = rooms.get(rooms.size() - 1); // Fallback to last room
        }
        
        // Set boss room
        Position bossPos = largestRoom.get_center();
        tiles[bossPos.get_x()][bossPos.get_y()] = new Tile(TileType.BOSS_ROOM, bossPos);
        bossPosition = bossPos;
    }
    
    /**
     * NEW: Place stairs in the same room as the boss (farthest room from spawn)
     */
    private void place_stairs() {
        if (bossPosition == null) return; // Need boss position first
        
        // Find the boss room (largest room excluding spawn)
        Room bossRoom = find_largest_room_excluding_spawn();
        if (bossRoom == null) return;
        
        // Try to place stairs within 5x5 distance of boss tile
        int bossX = bossPosition.get_x();
        int bossY = bossPosition.get_y();
        
        // Try positions in a 5x5 area around the boss
        for (int dx = -2; dx <= 2; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                int stairsX = bossX + dx;
                int stairsY = bossY + dy;
                
                // Skip the boss position itself
                if (dx == 0 && dy == 0) continue;
                
                // Check if position is valid and within the boss room
                if (is_valid_position(stairsX, stairsY) && 
                    bossRoom.contains_position(new Position(stairsX, stairsY)) &&
                    tiles[stairsX][stairsY].get_tile_type() == TileType.FLOOR) {
                    
                    // Place stairs here
                    tiles[stairsX][stairsY] = new Tile(TileType.STAIRS, new Position(stairsX, stairsY));
                    return; // Successfully placed stairs
                }
            }
        }
        
        // Fallback: place stairs at a random floor position in the boss room
        Position randomPos = get_random_floor_position_in_room(bossRoom);
        if (randomPos != null && !randomPos.equals(bossPosition)) {
            tiles[randomPos.get_x()][randomPos.get_y()] = new Tile(TileType.STAIRS, randomPos);
        }
    }
    
    /**
     * Place stairs for BONUS floor (in the exit room - third room)
     */
    private void place_bonus_stairs() {
        if (rooms.size() < 3) return; // Need at least spawn room + upgrader room + exit room
        
        // Use the third room (exit room)
        Room stairsRoom = rooms.get(2); // Third room after spawn
        
        if (stairsRoom != null) {
            Position stairsPos = get_random_floor_position_in_room(stairsRoom);
            if (stairsPos != null) {
                tiles[stairsPos.get_x()][stairsPos.get_y()] = new Tile(TileType.STAIRS, stairsPos);
            }
        }
    }
    
    /**
     * Place stairs for REGULAR floor (in farthest room from spawn)
     */
    private void place_regular_stairs() {
        if (rooms.size() < 2) return; // Need at least spawn room + 1 other room
        
        // Find the room farthest from spawn room
        Room stairsRoom = find_farthest_room_from_spawn();
        
        if (stairsRoom != null) {
            Position stairsPos = get_random_floor_position_in_room(stairsRoom);
            if (stairsPos != null) {
                tiles[stairsPos.get_x()][stairsPos.get_y()] = new Tile(TileType.STAIRS, stairsPos);
            }
        }
    }
    
    /**
     * NEW: Place upgrader room
     */
    private void place_upgrader_room() {
        if (rooms.size() < 3) return; // Need at least spawn room + 2 other rooms
        
        // Find a medium-sized room for the upgrader (excluding spawn and boss room)
        Room upgraderRoom = find_medium_room_for_upgrader();
        if (upgraderRoom == null) {
            return; // No suitable room found
        }
        
        // Create a single upgrader spawn tile within the existing room
        int centerX = upgraderRoom.get_center().get_x();
        int centerY = upgraderRoom.get_center().get_y();
        
        // Create just one UPGRADER_SPAWN tile at the center
        if (is_valid_position(centerX, centerY)) {
            tiles[centerX][centerY] = new Tile(TileType.UPGRADER_SPAWN, new Position(centerX, centerY));
        }
    }
    
    /**
     * Place upgrader for bonus floor (in the middle room)
     */
    private void place_bonus_upgrader() {
        if (rooms.size() < 3) return; // Need at least spawn room + upgrader room + exit room
        
        // Find the middle room (second room - upgrader room)
        Room upgraderRoom = rooms.get(1); // Second room after spawn
        
        // Create a single upgrader spawn tile within the existing room
        int centerX = upgraderRoom.get_center().get_x();
        int centerY = upgraderRoom.get_center().get_y();
        
        // Create just one UPGRADER_SPAWN tile at the center
        if (is_valid_position(centerX, centerY)) {
            tiles[centerX][centerY] = new Tile(TileType.UPGRADER_SPAWN, new Position(centerX, centerY));
        }
    }
    
    /**
     * Find a medium-sized room for the upgrader (excluding spawn and boss room)
     */
    private Room find_medium_room_for_upgrader() {
        if (rooms.size() < 3) return null;
        
        Room bossRoom = find_boss_room();
        Room spawnRoom = rooms.get(0);
        
        // Find a room that's not the spawn room or boss room
        for (Room room : rooms) {
            if (room != spawnRoom && room != bossRoom) {
                // Check if room is medium-sized (not too small, not too large)
                int area = room.width * room.height;
                if (area >= 12 && area <= 25) { // Medium room size
                    return room;
                }
            }
        }
        
        // Fallback: return any room that's not spawn or boss
        for (Room room : rooms) {
            if (room != spawnRoom && room != bossRoom) {
                return room;
            }
        }
        
        return null;
    }
    
    /**
     * Find the largest room in the map (excluding spawn room)
     */
    private Room find_largest_room_excluding_spawn() {
        if (rooms.size() < 2) return null; // Need at least 2 rooms
        
        Room largestRoom = null;
        int largestArea = 0;
        
        for (Room room : rooms) {
            // Skip the spawn room (first room)
            if (room == rooms.get(0)) {
                continue;
            }
            
            int area = room.width * room.height;
            if (largestRoom == null || area > largestArea) {
                largestArea = area;
                largestRoom = room;
            }
        }
        
        return largestRoom;
    }
    
    /**
     * Find the room farthest from the spawn room (for stairs placement)
     */
    private Room find_farthest_room_from_spawn() {
        if (rooms.size() < 2) return null; // Need at least 2 rooms
        
        Room farthestRoom = null;
        double maxDistance = 0;
        
        for (Room room : rooms) {
            // Skip the spawn room (first room)
            if (room == rooms.get(0)) {
                continue;
            }
            
            // Calculate distance between room centers
            Position spawnCenter = rooms.get(0).get_center();
            Position roomCenter = room.get_center();
            double distance = Math.sqrt(
                Math.pow(roomCenter.get_x() - spawnCenter.get_x(), 2) + 
                Math.pow(roomCenter.get_y() - spawnCenter.get_y(), 2)
            );
            
            if (farthestRoom == null || distance > maxDistance) {
                maxDistance = distance;
                farthestRoom = room;
            }
        }
        
        return farthestRoom;
    }


    /**
     * MANDATORY: Populate dungeon with enemies and items
     */
    private void populate_dungeon() {
        place_enemies();
        place_items();
    }
    
    /**
     * Populate BOSS dungeon (fewer enemies and items)
     */
    private void populate_boss_dungeon() {
        place_boss_enemies();
        place_boss_items();
    }
    
    /**
     * Populate BONUS dungeon (25% chance for enemies, Floor Key)
     */
    private void populate_bonus_dungeon() {
        // 25% chance for enemies on bonus floor
        if (random.nextInt(100) < 25) {
            place_bonus_enemies();
        }
        // Always place Floor Key on bonus floor
        place_bonus_items();
    }
    
    /**
     * Place enemies for BOSS floor (minimum 2 enemies, no empty rooms)
     */
    private void place_boss_enemies() {
        // Significantly increase enemy count for boss floors
        int enemyCount = Math.max(4, Math.min(currentFloor + 3, 10)); // Increased from +2,7 to +3,10, min 4
        int placedEnemies = 0;
        
        // Get boss room
        Room bossRoom = find_boss_room();
        
        // Sort rooms by size (bigger rooms first) for enemy placement preference
        List<Room> sortedRooms = new ArrayList<>();
        for (Room room : rooms) {
            // Skip spawn room (first room) and boss room
            if (room != rooms.get(0) && room != bossRoom) {
                sortedRooms.add(room);
            }
        }
        // Sort by room area (bigger rooms first)
        sortedRooms.sort((r1, r2) -> Integer.compare(r2.width * r2.height, r1.width * r1.height));
        
        // Place enemies with room size preference
        for (Room room : sortedRooms) {
            if (placedEnemies >= enemyCount) break;
            
            // Calculate how many enemies this room can have based on size (max 2)
            int roomArea = room.width * room.height;
            int maxEnemiesInRoom = roomArea >= 12 ? 2 : 1; // Big rooms: 2, small: 1 (max 2)
            
            // 95% chance for enemy in each room (increased to eliminate empty rooms)
            if (random.nextInt(100) < 95) {
                int enemiesInThisRoom = 0;
                int attempts = 0;
                int maxAttempts = 10;
                
                // Place first enemy (always try)
                Position enemyPos = get_random_floor_position_in_room(room);
                if (enemyPos != null && is_valid_enemy_position(enemyPos) && !enemyLocations.contains(enemyPos)) {
                    enemyLocations.add(enemyPos);
                    placedEnemies++;
                    enemiesInThisRoom++;
                }
                
                // 40% chance for second enemy (reduced from always trying)
                if (enemiesInThisRoom > 0 && maxEnemiesInRoom > 1 && random.nextInt(100) < 40) {
                    while (enemiesInThisRoom < maxEnemiesInRoom && placedEnemies < enemyCount && attempts < maxAttempts) {
                        attempts++;
                        enemyPos = get_random_floor_position_in_room(room);
                        if (enemyPos != null && is_valid_enemy_position(enemyPos) && !enemyLocations.contains(enemyPos)) {
                            enemyLocations.add(enemyPos);
                            placedEnemies++;
                            enemiesInThisRoom++;
                        }
                    }
                }
            }
        }
        
        // Ensure minimum 3 enemies are placed
        while (placedEnemies < 3 && rooms.size() > 2) {
            for (Room room : rooms) {
                if (room == rooms.get(0) || room == bossRoom) {
                    continue;
                }
                
                Position enemyPos = get_random_floor_position_in_room(room);
                if (enemyPos != null && is_valid_enemy_position(enemyPos) && !enemyLocations.contains(enemyPos)) {
                    enemyLocations.add(enemyPos);
                    placedEnemies++;
                    break;
                }
            }
        }
    }
    
    /**
     * Place items for BOSS floor (more items)
     */
    private void place_boss_items() {
        int itemCount = Math.min(currentFloor + 12, 30); // Highly increased items for boss floor
        int placedItems = 0;
        
        // Get boss room
        Room bossRoom = find_boss_room();
        
        // Place items with higher chance in rooms that don't have enemies
        for (Room room : rooms) {
            // Skip spawn room (first room) and boss room
            if (room == rooms.get(0) || room == bossRoom) {
                continue;
            }
            
            // Check if this room has any enemies
            boolean roomHasEnemy = false;
            for (Position enemyPos : enemyLocations) {
                if (room.contains_position(enemyPos)) {
                    roomHasEnemy = true;
                    break;
                }
            }
            
            // Extremely favor rooms with both enemies and items (98% vs 80%)
            int itemChance = roomHasEnemy ? 98 : 80;
            if (random.nextInt(100) < itemChance && placedItems < itemCount) {
                Position itemPos = get_random_floor_position_in_room(room);
                if (itemPos != null && !itemLocations.contains(itemPos) && !enemyLocations.contains(itemPos)) {
                    itemLocations.add(itemPos);
                    placedItems++;
                }
            }
        }
        
        // Fill remaining items randomly (rooms and corridors)
        int attempts = 0;
        int maxAttempts = itemCount * 10;
        while (placedItems < itemCount && attempts < maxAttempts) {
            attempts++;
            
            // 70% chance for room placement, 30% chance for corridor placement
            Position itemPos = null;
            if (random.nextInt(100) < 70) {
                // Place in random room
                Room room = rooms.get(random.nextInt(rooms.size()));
                itemPos = get_random_floor_position_in_room(room);
            } else {
                // Place in corridor
                itemPos = get_random_corridor_position();
            }
            
            if (itemPos != null && !itemLocations.contains(itemPos) && !enemyLocations.contains(itemPos)) {
                itemLocations.add(itemPos);
                placedItems++;
            }
        }
        
        // Final pass: ensure no empty rooms by adding items to rooms without any content
        for (Room room : rooms) {
            // Skip spawn room (first room) and boss room
            if (room == rooms.get(0) || room == bossRoom) {
                continue;
            }
            
            // Check if this room has any enemies or items
            boolean roomHasEnemy = false;
            boolean roomHasItem = false;
            
            for (Position enemyPos : enemyLocations) {
                if (room.contains_position(enemyPos)) {
                    roomHasEnemy = true;
                    break;
                }
            }
            
            for (Position itemPos : itemLocations) {
                if (room.contains_position(itemPos)) {
                    roomHasItem = true;
                    break;
                }
            }
            
            // If room has no content, add an item
            if (!roomHasEnemy && !roomHasItem) {
                Position itemPos = get_random_floor_position_in_room(room);
                if (itemPos != null && !itemLocations.contains(itemPos) && !enemyLocations.contains(itemPos)) {
                    itemLocations.add(itemPos);
                    placedItems++;
                }
            }
        }
    }
    
    /**
     * Place enemies for BONUS floor (25% chance)
     */
    private void place_bonus_enemies() {
        // Place 1-2 enemies randomly in bonus floor rooms
        int enemyCount = random.nextInt(2) + 1; // 1-2 enemies
        int placedEnemies = 0;
        
        for (Room room : rooms) {
            // Skip spawn room (first room)
            if (room == rooms.get(0)) {
                continue;
            }
            
            if (placedEnemies < enemyCount) {
                Position enemyPos = get_random_floor_position_in_room(room);
                if (enemyPos != null && is_valid_enemy_position(enemyPos)) {
                    enemyLocations.add(enemyPos);
                    placedEnemies++;
                }
            }
        }
    }
    
    /**
     * Place items for BONUS floor (only Floor Key)
     */
    private void place_bonus_items() {
        // Only place Floor Key on bonus floor
        if (rooms.size() >= 3) {
            // Place Floor Key in the exit room (third room)
            Room exitRoom = rooms.get(2); // Third room after spawn
            Position keyPos = get_random_floor_position_in_room(exitRoom);
            if (keyPos != null) {
                itemLocations.add(keyPos);
            }
        }
    }
    
    /**
     * MANDATORY: Place enemies randomly in rooms (excluding spawn and boss room)
     */
    private void place_enemies() {
        // Highly increase enemy count for regular floors
        int enemyCount = Math.min(currentFloor + 6, 18); // Increased from +4,12 to +6,18
        int placedEnemies = 0;
        
        // Get boss room
        Room bossRoom = find_boss_room();
        
        // Sort rooms by size (bigger rooms first) for enemy placement preference
        List<Room> sortedRooms = new ArrayList<>();
        for (Room room : rooms) {
            // Skip spawn room (first room) and boss room
            if (room != rooms.get(0) && room != bossRoom) {
                sortedRooms.add(room);
            }
        }
        // Sort by room area (bigger rooms first)
        sortedRooms.sort((r1, r2) -> Integer.compare(r2.width * r2.height, r1.width * r1.height));
        
        // Place enemies with room size preference
        for (Room room : sortedRooms) {
            if (placedEnemies >= enemyCount) break;
            
            // Calculate how many enemies this room can have based on size (max 2)
            int roomArea = room.width * room.height;
            int maxEnemiesInRoom = roomArea >= 12 ? 2 : 1; // Big rooms: 2, small: 1 (max 2)
            
            // 90% chance for enemy in each room (increased to eliminate empty rooms)
            if (random.nextInt(100) < 90) {
                int enemiesInThisRoom = 0;
                int attempts = 0;
                int maxAttempts = 10;
                
                // Place first enemy (always try)
                Position enemyPos = get_random_floor_position_in_room(room);
                if (enemyPos != null && is_valid_enemy_position(enemyPos) && !enemyLocations.contains(enemyPos)) {
                    enemyLocations.add(enemyPos);
                    placedEnemies++;
                    enemiesInThisRoom++;
                }
                
                // 30% chance for second enemy (reduced from always trying)
                if (enemiesInThisRoom > 0 && maxEnemiesInRoom > 1 && random.nextInt(100) < 30) {
                    while (enemiesInThisRoom < maxEnemiesInRoom && placedEnemies < enemyCount && attempts < maxAttempts) {
                        attempts++;
                        enemyPos = get_random_floor_position_in_room(room);
                        if (enemyPos != null && is_valid_enemy_position(enemyPos) && !enemyLocations.contains(enemyPos)) {
                            enemyLocations.add(enemyPos);
                            placedEnemies++;
                            enemiesInThisRoom++;
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Check if enemy position is valid (not too close to player)
     */
    private boolean is_valid_enemy_position(Position enemyPos) {
        // Check if enemy is within 6 tiles of player
        double distance = Math.sqrt(
            Math.pow(enemyPos.get_x() - playerStartPosition.get_x(), 2) + 
            Math.pow(enemyPos.get_y() - playerStartPosition.get_y(), 2)
        );
        
        return distance >= 6.0;
    }
    

    
    /**
     * Find the boss room (room containing boss position)
     */
    private Room find_boss_room() {
        if (bossPosition == null) {
            return null; // No boss on this floor
        }
        for (Room room : rooms) {
            if (room.contains_position(bossPosition)) {
                return room;
            }
        }
        return null;
    }

    /**
     * MANDATORY: Place items randomly throughout the map
     */
    private void place_items() {
        // Generate items based on floor level (extremely increased counts)
        int itemCount = Math.min(currentFloor + 15, 35); // 16-35 items per map based on floor
        int placedItems = 0;
        int attempts = 0;
        int maxAttempts = itemCount * 10; // Prevent infinite loops
        
        // Get boss room
        Room bossRoom = find_boss_room();
        
        // Place items with higher chance in rooms that don't have enemies
        for (Room room : rooms) {
            // Skip spawn room (first room) and boss room
            if (room == rooms.get(0) || room == bossRoom) {
                continue;
            }
            
            // Check if this room has any enemies
            boolean roomHasEnemy = false;
            for (Position enemyPos : enemyLocations) {
                if (room.contains_position(enemyPos)) {
                    roomHasEnemy = true;
                    break;
                }
            }
            
            // Extremely favor rooms with both enemies and items (95% vs 70%)
            int itemChance = roomHasEnemy ? 95 : 70;
            if (random.nextInt(100) < itemChance && placedItems < itemCount) {
                Position itemPos = get_random_floor_position_in_room(room);
                if (itemPos != null && !itemLocations.contains(itemPos) && !enemyLocations.contains(itemPos)) {
                    itemLocations.add(itemPos);
                    placedItems++;
                }
            }
        }
        
        // Fill remaining items randomly (rooms and corridors)
        while (placedItems < itemCount && attempts < maxAttempts) {
            attempts++;
            
            // 70% chance for room placement, 30% chance for corridor placement
            Position itemPos = null;
            if (random.nextInt(100) < 70) {
                // Place in random room
                Room room = rooms.get(random.nextInt(rooms.size()));
                itemPos = get_random_floor_position_in_room(room);
            } else {
                // Place in corridor
                itemPos = get_random_corridor_position();
            }
            
            if (itemPos != null && !itemLocations.contains(itemPos) && !enemyLocations.contains(itemPos)) {
                itemLocations.add(itemPos);
                placedItems++;
            }
        }
        
        // Final pass: ensure no empty rooms by adding items to rooms without any content
        for (Room room : rooms) {
            // Skip spawn room (first room) and boss room
            if (room == rooms.get(0) || room == bossRoom) {
                continue;
            }
            
            // Check if this room has any enemies or items
            boolean roomHasEnemy = false;
            boolean roomHasItem = false;
            
            for (Position enemyPos : enemyLocations) {
                if (room.contains_position(enemyPos)) {
                    roomHasEnemy = true;
                    break;
                }
            }
            
            for (Position itemPos : itemLocations) {
                if (room.contains_position(itemPos)) {
                    roomHasItem = true;
                    break;
                }
            }
            
            // If room has no content, add an item
            if (!roomHasEnemy && !roomHasItem) {
                Position itemPos = get_random_floor_position_in_room(room);
                if (itemPos != null && !itemLocations.contains(itemPos) && !enemyLocations.contains(itemPos)) {
                    itemLocations.add(itemPos);
                    placedItems++;
                }
            }
        }
    }

    /**
     * MANDATORY: Get random floor position within a room
     *
     * @param room Room to search
     * @return Random floor position or null if none available
     */
    public Position get_random_floor_position_in_room(Room room) {
        List<Position> floorPositions = new ArrayList<>();
        for (int x = room.x; x < room.x + room.width; x++) {
            for (int y = room.y; y < room.y + room.height; y++) {
                if (tiles[x][y].get_tile_type() == TileType.FLOOR) {
                    // Check all 4 cardinal neighbors
                    boolean allFloor = true;
                    int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
                    for (int[] d : dirs) {
                        int nx = x + d[0];
                        int ny = y + d[1];
                        if (nx < 0 || ny < 0 || nx >= width || ny >= height || tiles[nx][ny].get_tile_type() != TileType.FLOOR) {
                            allFloor = false;
                            break;
                        }
                    }
                    if (allFloor) floorPositions.add(new Position(x, y));
                }
            }
        }
        if (floorPositions.isEmpty()) return null;
        return floorPositions.get(random.nextInt(floorPositions.size()));
    }
    
    /**
     * Get random floor position in corridors (outside of rooms)
     *
     * @return Random corridor floor position or null if none available
     */
    public Position get_random_corridor_position() {
        List<Position> corridorPositions = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (tiles[x][y].get_tile_type() == TileType.FLOOR) {
                    // Check if this position is not inside any room
                    boolean inRoom = false;
                    for (Room room : rooms) {
                        if (room.contains_position(new Position(x, y))) {
                            inRoom = true;
                            break;
                        }
                    }
                    
                    if (!inRoom) {
                        // Check all 4 cardinal neighbors
                        boolean allFloor = true;
                        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
                        for (int[] d : dirs) {
                            int nx = x + d[0];
                            int ny = y + d[1];
                            if (nx < 0 || ny < 0 || nx >= width || ny >= height || tiles[nx][ny].get_tile_type() != TileType.FLOOR) {
                                allFloor = false;
                                break;
                            }
                        }
                        if (allFloor) corridorPositions.add(new Position(x, y));
                    }
                }
            }
        }
        if (corridorPositions.isEmpty()) return null;
        return corridorPositions.get(random.nextInt(corridorPositions.size()));
    }

    // Add a method to set a tile as the entrance
    public void set_entrance_tile(Position pos) {
        if (is_valid_position(pos.get_x(), pos.get_y())) {
            tiles[pos.get_x()][pos.get_y()] = new Tile(TileType.ENTRANCE, pos);
        }
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
            enemy.move_to(position.get_x() * enums.GameConstants.TILE_SIZE, position.get_y() * enums.GameConstants.TILE_SIZE);
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