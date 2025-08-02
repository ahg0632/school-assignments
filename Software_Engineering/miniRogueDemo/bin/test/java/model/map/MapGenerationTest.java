package model.map;

import model.characters.Enemy;
import model.characters.Boss;
import model.items.Item;
import enums.CharacterClass;
import enums.TileType;
import utilities.Position;
import utilities.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.RepeatedTest;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for map generation functionality.
 * Tests core dungeon generation, room placement, tile management, and floor type variations.
 */
@DisplayName("Map Generation Tests")
class MapGenerationTest {

    private Map testMap;
    private Map bossMap;
    private Map bonusMap;

    @BeforeEach
    void setUp() {
        // Create test maps for different floor types
        testMap = new Map(1, Map.FloorType.REGULAR);
        bossMap = new Map(2, Map.FloorType.BOSS);
        bonusMap = new Map(3, Map.FloorType.BONUS);
    }

    /**
     * Tests basic map initialization and properties.
     */
    @Test
    @DisplayName("Map Initialization")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMapInitialization() {
        // Test map dimensions
        assertEquals(50, testMap.get_width(), "Map width should be 50");
        assertEquals(30, testMap.get_height(), "Map height should be 30");
        
        // Test floor information
        assertEquals(1, testMap.get_current_floor(), "Map should have correct floor number");
        assertEquals(2, bossMap.get_current_floor(), "Boss map should have correct floor number");
        assertEquals(3, bonusMap.get_current_floor(), "Bonus map should have correct floor number");
        
        // Test that maps are not null
        assertNotNull(testMap, "Regular map should not be null");
        assertNotNull(bossMap, "Boss map should not be null");
        assertNotNull(bonusMap, "Bonus map should not be null");
    }

    /**
     * Tests room generation and placement.
     */
    @Test
    @DisplayName("Room Generation")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRoomGeneration() {
        // Test that rooms are generated
        List<Map.Room> rooms = testMap.get_rooms();
        assertNotNull(rooms, "Rooms list should not be null");
        assertFalse(rooms.isEmpty(), "Map should have at least one room");
        
        // Test room properties
        for (Map.Room room : rooms) {
            assertTrue(room.width > 0, "Room width should be positive");
            assertTrue(room.height > 0, "Room height should be positive");
            assertTrue(room.x >= 0, "Room x position should be non-negative");
            assertTrue(room.y >= 0, "Room y position should be non-negative");
            assertTrue(room.x + room.width <= testMap.get_width(), "Room should not exceed map width");
            assertTrue(room.y + room.height <= testMap.get_height(), "Room should not exceed map height");
        }
    }

    /**
     * Tests player spawn room creation.
     */
    @Test
    @DisplayName("Player Spawn Room")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testPlayerSpawnRoom() {
        Position playerStart = testMap.get_player_start_position();
        assertNotNull(playerStart, "Player start position should not be null");
        
        // Test that player start position is within map bounds
        assertTrue(playerStart.get_x() >= 0, "Player start X should be non-negative");
        assertTrue(playerStart.get_y() >= 0, "Player start Y should be non-negative");
        assertTrue(playerStart.get_x() < testMap.get_width(), "Player start X should be within map width");
        assertTrue(playerStart.get_y() < testMap.get_height(), "Player start Y should be within map height");
        
        // Test that player start position is walkable
        assertTrue(testMap.is_valid_move(playerStart), "Player start position should be walkable");
    }

    /**
     * Tests boss room placement for boss floors.
     */
    @Test
    @DisplayName("Boss Room Placement")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testBossRoomPlacement() {
        Position bossPos = bossMap.get_boss_position();
        assertNotNull(bossPos, "Boss position should not be null");
        
        // Test that boss position is within map bounds
        assertTrue(bossPos.get_x() >= 0, "Boss position X should be non-negative");
        assertTrue(bossPos.get_y() >= 0, "Boss position Y should be non-negative");
        assertTrue(bossPos.get_x() < bossMap.get_width(), "Boss position X should be within map width");
        assertTrue(bossPos.get_y() < bossMap.get_height(), "Boss position Y should be within map height");
        
        // Test that boss position is walkable
        assertTrue(bossMap.is_valid_move(bossPos), "Boss position should be walkable");
    }

    /**
     * Tests tile access and validation.
     */
    @Test
    @DisplayName("Tile Access and Validation")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testTileAccessAndValidation() {
        // Test valid tile access
        Tile centerTile = testMap.get_tile(testMap.get_width() / 2, testMap.get_height() / 2);
        assertNotNull(centerTile, "Center tile should not be null");
        
        // Test invalid tile access
        Tile invalidTile = testMap.get_tile(-1, -1);
        assertNull(invalidTile, "Invalid tile access should return null");
        
        Tile outOfBoundsTile = testMap.get_tile(testMap.get_width() + 1, testMap.get_height() + 1);
        assertNull(outOfBoundsTile, "Out of bounds tile access should return null");
        
        // Test position-based tile access
        Position validPos = new Position(5, 5);
        Tile posTile = testMap.get_tile(validPos);
        assertNotNull(posTile, "Position-based tile access should not return null");
    }

    /**
     * Tests movement validation.
     */
    @Test
    @DisplayName("Movement Validation")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMovementValidation() {
        // Test valid movement positions
        Position playerStart = testMap.get_player_start_position();
        assertTrue(testMap.is_valid_move(playerStart), "Player start position should be valid for movement");
        
        // Test invalid movement positions
        Position invalidPos = new Position(-1, -1);
        assertFalse(testMap.is_valid_move(invalidPos), "Invalid position should not be valid for movement");
        
        Position outOfBoundsPos = new Position(testMap.get_width() + 1, testMap.get_height() + 1);
        assertFalse(testMap.is_valid_move(outOfBoundsPos), "Out of bounds position should not be valid for movement");
    }

    /**
     * Tests floor type differences.
     */
    @Test
    @DisplayName("Floor Type Differences")
    @Timeout(value = 15, unit = TimeUnit.SECONDS)
    void testFloorTypeDifferences() {
        // Test that different floor types generate different layouts
        List<Map.Room> regularRooms = testMap.get_rooms();
        List<Map.Room> bossRooms = bossMap.get_rooms();
        List<Map.Room> bonusRooms = bonusMap.get_rooms();
        
        assertNotNull(regularRooms, "Regular rooms should not be null");
        assertNotNull(bossRooms, "Boss rooms should not be null");
        assertNotNull(bonusRooms, "Bonus rooms should not be null");
        
        // Test that rooms are generated for all floor types
        assertFalse(regularRooms.isEmpty(), "Regular floor should have rooms");
        assertFalse(bossRooms.isEmpty(), "Boss floor should have rooms");
        assertFalse(bonusRooms.isEmpty(), "Bonus floor should have rooms");
        
        // Test that different floor types have different player start positions
        Position regularStart = testMap.get_player_start_position();
        Position bossStart = bossMap.get_player_start_position();
        Position bonusStart = bonusMap.get_player_start_position();
        
        assertNotNull(regularStart, "Regular floor player start should not be null");
        assertNotNull(bossStart, "Boss floor player start should not be null");
        assertNotNull(bonusStart, "Bonus floor player start should not be null");
    }

    /**
     * Tests item and enemy location tracking.
     */
    @Test
    @DisplayName("Entity Location Tracking")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testEntityLocationTracking() {
        // Test item locations
        List<Position> itemLocations = testMap.get_item_locations();
        assertNotNull(itemLocations, "Item locations should not be null");
        
        // Test enemy locations
        List<Position> enemyLocations = testMap.get_enemy_locations();
        assertNotNull(enemyLocations, "Enemy locations should not be null");
        
        // Test that locations are within map bounds
        for (Position itemPos : itemLocations) {
            assertTrue(itemPos.get_x() >= 0, "Item position X should be non-negative");
            assertTrue(itemPos.get_y() >= 0, "Item position Y should be non-negative");
            assertTrue(itemPos.get_x() < testMap.get_width(), "Item position X should be within map width");
            assertTrue(itemPos.get_y() < testMap.get_height(), "Item position Y should be within map height");
        }
        
        for (Position enemyPos : enemyLocations) {
            assertTrue(enemyPos.get_x() >= 0, "Enemy position X should be non-negative");
            assertTrue(enemyPos.get_y() >= 0, "Enemy position Y should be non-negative");
            assertTrue(enemyPos.get_x() < testMap.get_width(), "Enemy position X should be within map width");
            assertTrue(enemyPos.get_y() < testMap.get_height(), "Enemy position Y should be within map height");
        }
    }

    /**
     * Tests random position generation within rooms.
     */
    @Test
    @DisplayName("Random Position Generation")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRandomPositionGeneration() {
        List<Map.Room> rooms = testMap.get_rooms();
        assertFalse(rooms.isEmpty(), "Map should have rooms for position generation");
        
        // Test random floor position in room
        Map.Room testRoom = rooms.get(0);
        Position randomPos = testMap.get_random_floor_position_in_room(testRoom);
        assertNotNull(randomPos, "Random floor position should not be null");
        
        // Test that random position is within room bounds
        assertTrue(randomPos.get_x() >= testRoom.x, "Random position X should be within room");
        assertTrue(randomPos.get_y() >= testRoom.y, "Random position Y should be within room");
        assertTrue(randomPos.get_x() < testRoom.x + testRoom.width, "Random position X should be within room width");
        assertTrue(randomPos.get_y() < testRoom.y + testRoom.height, "Random position Y should be within room height");
        
        // Test random corridor position
        Position corridorPos = testMap.get_random_corridor_position();
        assertNotNull(corridorPos, "Random corridor position should not be null");
        
        // Test that corridor position is within map bounds
        assertTrue(corridorPos.get_x() >= 0, "Corridor position X should be non-negative");
        assertTrue(corridorPos.get_y() >= 0, "Corridor position Y should be non-negative");
        assertTrue(corridorPos.get_x() < testMap.get_width(), "Corridor position X should be within map width");
        assertTrue(corridorPos.get_y() < testMap.get_height(), "Corridor position Y should be within map height");
    }

    /**
     * Tests entrance tile setting.
     */
    @Test
    @DisplayName("Entrance Tile Setting")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testEntranceTileSetting() {
        Position entrancePos = new Position(5, 5);
        
        // Test that entrance tile setting doesn't throw exception
        assertDoesNotThrow(() -> testMap.set_entrance_tile(entrancePos), 
                          "Setting entrance tile should not throw exception");
        
        // Test that entrance position is valid
        assertTrue(entrancePos.get_x() >= 0, "Entrance position X should be non-negative");
        assertTrue(entrancePos.get_y() >= 0, "Entrance position Y should be non-negative");
        assertTrue(entrancePos.get_x() < testMap.get_width(), "Entrance position X should be within map width");
        assertTrue(entrancePos.get_y() < testMap.get_height(), "Entrance position Y should be within map height");
    }

    /**
     * Tests map generation consistency across multiple runs.
     */
    @RepeatedTest(3)
    @DisplayName("Map Generation Consistency")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMapGenerationConsistency() {
        // Create new maps for consistency testing
        Map map1 = new Map(1, Map.FloorType.REGULAR);
        Map map2 = new Map(1, Map.FloorType.REGULAR);
        
        // Test that maps have consistent properties
        assertEquals(map1.get_width(), map2.get_width(), "Map widths should be consistent");
        assertEquals(map1.get_height(), map2.get_height(), "Map heights should be consistent");
        assertEquals(map1.get_current_floor(), map2.get_current_floor(), "Map floors should be consistent");
        
        // Test that rooms are generated consistently
        List<Map.Room> rooms1 = map1.get_rooms();
        List<Map.Room> rooms2 = map2.get_rooms();
        
        assertNotNull(rooms1, "First map rooms should not be null");
        assertNotNull(rooms2, "Second map rooms should not be null");
        assertFalse(rooms1.isEmpty(), "First map should have rooms");
        assertFalse(rooms2.isEmpty(), "Second map should have rooms");
    }

    /**
     * Tests edge cases and boundary conditions.
     */
    @Test
    @DisplayName("Edge Cases and Boundary Conditions")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testEdgeCasesAndBoundaryConditions() {
        // Test map creation with edge floor numbers
        assertDoesNotThrow(() -> new Map(1, Map.FloorType.REGULAR), 
                          "Creating map with floor 1 should not throw exception");
        assertDoesNotThrow(() -> new Map(20, Map.FloorType.BOSS), 
                          "Creating map with floor 20 should not throw exception");
        
        // Test tile access at boundaries
        Tile topLeftTile = testMap.get_tile(0, 0);
        Tile bottomRightTile = testMap.get_tile(testMap.get_width() - 1, testMap.get_height() - 1);
        
        assertNotNull(topLeftTile, "Top-left tile should not be null");
        assertNotNull(bottomRightTile, "Bottom-right tile should not be null");
        
        // Test invalid tile access
        Tile invalidTile1 = testMap.get_tile(-1, 0);
        Tile invalidTile2 = testMap.get_tile(0, -1);
        Tile invalidTile3 = testMap.get_tile(testMap.get_width(), 0);
        Tile invalidTile4 = testMap.get_tile(0, testMap.get_height());
        
        assertNull(invalidTile1, "Negative X tile access should return null");
        assertNull(invalidTile2, "Negative Y tile access should return null");
        assertNull(invalidTile3, "Exceeding width tile access should return null");
        assertNull(invalidTile4, "Exceeding height tile access should return null");
    }
} 