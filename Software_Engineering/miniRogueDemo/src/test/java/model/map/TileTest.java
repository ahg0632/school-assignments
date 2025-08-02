package model.map;

import model.items.Consumable;
import model.items.Item;
import enums.TileType;
import utilities.Position;
import utilities.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for Tile functionality.
 * Tests tile properties, item management, exploration status, and tile type behavior.
 */
@DisplayName("Tile Tests")
class TileTest {

    private Tile floorTile;
    private Tile wallTile;
    private Tile doorTile;
    private Tile stairsTile;
    private Position testPosition;

    @BeforeEach
    void setUp() {
        testPosition = new Position(5, 5);
        floorTile = new Tile(TileType.FLOOR, testPosition);
        wallTile = new Tile(TileType.WALL, testPosition);
        doorTile = new Tile(TileType.DOOR, testPosition);
        stairsTile = new Tile(TileType.STAIRS, testPosition);
    }

    /**
     * Tests tile initialization and basic properties.
     */
    @Test
    @DisplayName("Tile Initialization")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testTileInitialization() {
        // Test tile type
        assertEquals(TileType.FLOOR, floorTile.get_tile_type(), "Floor tile should have FLOOR type");
        assertEquals(TileType.WALL, wallTile.get_tile_type(), "Wall tile should have WALL type");
        assertEquals(TileType.DOOR, doorTile.get_tile_type(), "Door tile should have DOOR type");
        assertEquals(TileType.STAIRS, stairsTile.get_tile_type(), "Stairs tile should have STAIRS type");
        
        // Test position
        assertEquals(testPosition, floorTile.get_position(), "Tile should have correct position");
        assertEquals(testPosition, wallTile.get_position(), "Wall tile should have correct position");
        assertEquals(testPosition, doorTile.get_position(), "Door tile should have correct position");
        assertEquals(testPosition, stairsTile.get_position(), "Stairs tile should have correct position");
        
        // Test initial exploration status
        assertFalse(floorTile.is_explored(), "New tile should not be explored");
        assertFalse(wallTile.is_explored(), "New wall tile should not be explored");
        assertFalse(doorTile.is_explored(), "New door tile should not be explored");
        assertFalse(stairsTile.is_explored(), "New stairs tile should not be explored");
    }

    /**
     * Tests walkable property for different tile types.
     */
    @Test
    @DisplayName("Tile Walkability")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testTileWalkability() {
        // Test walkable tiles
        assertTrue(floorTile.is_walkable(), "Floor tile should be walkable");
        assertTrue(doorTile.is_walkable(), "Door tile should be walkable");
        assertTrue(stairsTile.is_walkable(), "Stairs tile should be walkable");
        
        // Test non-walkable tiles
        assertFalse(wallTile.is_walkable(), "Wall tile should not be walkable");
    }

    /**
     * Tests item management functionality.
     */
    @Test
    @DisplayName("Item Management")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testItemManagement() {
        // Test initial item state
        assertFalse(floorTile.has_items(), "New tile should not have items");
        assertEquals(0, floorTile.get_items().size(), "New tile should have no items");
        
        // Test adding items
        Consumable testItem1 = new Consumable("TestItem1", 10, "health");
        Consumable testItem2 = new Consumable("TestItem2", 20, "mana");
        
        floorTile.add_item(testItem1);
        assertTrue(floorTile.has_items(), "Tile should have items after adding");
        assertEquals(1, floorTile.get_items().size(), "Tile should have one item");
        assertTrue(floorTile.get_items().contains(testItem1), "Tile should contain added item");
        
        floorTile.add_item(testItem2);
        assertEquals(2, floorTile.get_items().size(), "Tile should have two items");
        assertTrue(floorTile.get_items().contains(testItem1), "Tile should still contain first item");
        assertTrue(floorTile.get_items().contains(testItem2), "Tile should contain second item");
        
        // Test removing items
        assertTrue(floorTile.remove_item(testItem1), "Removing existing item should return true");
        assertEquals(1, floorTile.get_items().size(), "Tile should have one item after removal");
        assertFalse(floorTile.get_items().contains(testItem1), "Tile should not contain removed item");
        assertTrue(floorTile.get_items().contains(testItem2), "Tile should still contain remaining item");
        
        // Test removing non-existent item
        assertFalse(floorTile.remove_item(testItem1), "Removing non-existent item should return false");
        assertEquals(1, floorTile.get_items().size(), "Tile item count should not change");
        
        // Test clearing items
        floorTile.clear_items();
        assertFalse(floorTile.has_items(), "Tile should not have items after clearing");
        assertEquals(0, floorTile.get_items().size(), "Tile should have no items after clearing");
    }

    /**
     * Tests adding null items.
     */
    @Test
    @DisplayName("Null Item Handling")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testNullItemHandling() {
        // Test adding null item
        assertDoesNotThrow(() -> floorTile.add_item(null), 
                          "Adding null item should not throw exception");
        
        // Test that null item is not added
        assertEquals(0, floorTile.get_items().size(), "Null item should not be added");
        assertFalse(floorTile.has_items(), "Tile should not have items after adding null");
    }

    /**
     * Tests exploration status management.
     */
    @Test
    @DisplayName("Exploration Status")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testExplorationStatus() {
        // Test initial exploration status
        assertFalse(floorTile.is_explored(), "New tile should not be explored");
        
        // Test setting exploration
        floorTile.set_explored();
        assertTrue(floorTile.is_explored(), "Tile should be explored after setting");
        
        // Test resetting exploration
        floorTile.reset_exploration();
        assertFalse(floorTile.is_explored(), "Tile should not be explored after reset");
        
        // Test multiple exploration cycles
        floorTile.set_explored();
        assertTrue(floorTile.is_explored(), "Tile should be explored after second setting");
        floorTile.set_explored();
        assertTrue(floorTile.is_explored(), "Tile should remain explored after multiple settings");
    }

    /**
     * Tests tile type properties.
     */
    @Test
    @DisplayName("Tile Type Properties")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testTileTypeProperties() {
        // Test all tile types
        Tile entranceTile = new Tile(TileType.ENTRANCE, testPosition);
        Tile bossRoomTile = new Tile(TileType.BOSS_ROOM, testPosition);
        Tile upgraderTile = new Tile(TileType.UPGRADER_SPAWN, testPosition);
        
        // Test tile type names
        assertEquals("floor", floorTile.get_tile_type().get_type_name(), "Floor tile should have correct type name");
        assertEquals("wall", wallTile.get_tile_type().get_type_name(), "Wall tile should have correct type name");
        assertEquals("door", doorTile.get_tile_type().get_type_name(), "Door tile should have correct type name");
        assertEquals("stairs", stairsTile.get_tile_type().get_type_name(), "Stairs tile should have correct type name");
        assertEquals("entrance", entranceTile.get_tile_type().get_type_name(), "Entrance tile should have correct type name");
        assertEquals("bossRoom", bossRoomTile.get_tile_type().get_type_name(), "Boss room tile should have correct type name");
        assertEquals("upgraderSpawn", upgraderTile.get_tile_type().get_type_name(), "Upgrader spawn tile should have correct type name");
        
        // Test walkability for all types
        assertTrue(entranceTile.is_walkable(), "Entrance tile should be walkable");
        assertTrue(bossRoomTile.is_walkable(), "Boss room tile should be walkable");
        assertTrue(upgraderTile.is_walkable(), "Upgrader spawn tile should be walkable");
    }

    /**
     * Tests defensive copying of item lists.
     */
    @Test
    @DisplayName("Item List Defensive Copying")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testItemListDefensiveCopying() {
        Consumable testItem = new Consumable("TestItem", 10, "health");
        floorTile.add_item(testItem);
        
        // Get items list and modify it
        List<Item> items = floorTile.get_items();
        items.clear(); // This should not affect the tile's internal list
        
        // Verify tile still has the item
        assertTrue(floorTile.has_items(), "Tile should still have items after external list modification");
        assertEquals(1, floorTile.get_items().size(), "Tile should still have one item");
        assertTrue(floorTile.get_items().contains(testItem), "Tile should still contain the item");
    }

    /**
     * Tests tile string representation.
     */
    @Test
    @DisplayName("Tile String Representation")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testTileStringRepresentation() {
        // Test basic string representation
        String floorString = floorTile.toString();
        assertNotNull(floorString, "Tile string representation should not be null");
        assertFalse(floorString.isEmpty(), "Tile string representation should not be empty");
        assertTrue(floorString.contains("floor"), "Floor tile string should contain 'floor'");
        assertTrue(floorString.contains("Position(5, 5)"), "Tile string should contain position");
        assertTrue(floorString.contains("items=0"), "Tile string should contain item count");
        
        // Test string representation with items
        Consumable testItem = new Consumable("TestItem", 10, "health");
        floorTile.add_item(testItem);
        String floorWithItemsString = floorTile.toString();
        assertTrue(floorWithItemsString.contains("items=1"), "Tile string should show correct item count");
    }

    /**
     * Tests edge cases and boundary conditions.
     */
    @Test
    @DisplayName("Edge Cases and Boundary Conditions")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testEdgeCasesAndBoundaryConditions() {
        // Test tile creation with edge positions
        Position edgePos1 = new Position(0, 0);
        Position edgePos2 = new Position(Integer.MAX_VALUE, Integer.MAX_VALUE);
        Position negativePos = new Position(-1, -1);
        
        assertDoesNotThrow(() -> new Tile(TileType.FLOOR, edgePos1), 
                          "Creating tile with (0,0) position should not throw exception");
        assertDoesNotThrow(() -> new Tile(TileType.FLOOR, edgePos2), 
                          "Creating tile with max position should not throw exception");
        assertDoesNotThrow(() -> new Tile(TileType.FLOOR, negativePos), 
                          "Creating tile with negative position should not throw exception");
        
        // Test multiple item operations
        for (int i = 0; i < 100; i++) {
            Consumable item = new Consumable("Item" + i, i, "health");
            floorTile.add_item(item);
        }
        assertEquals(100, floorTile.get_items().size(), "Tile should handle many items");
        
        // Test exploration status persistence
        floorTile.set_explored();
        assertTrue(floorTile.is_explored(), "Exploration status should persist");
        
        // Add items after exploration
        Consumable testItem = new Consumable("TestItem", 10, "health");
        floorTile.add_item(testItem);
        assertTrue(floorTile.is_explored(), "Exploration status should persist after adding items");
        assertTrue(floorTile.has_items(), "Tile should have items after exploration");
    }

    /**
     * Tests tile behavior with different tile types.
     */
    @Test
    @DisplayName("Different Tile Type Behavior")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testDifferentTileTypeBehavior() {
        // Test that all tile types can have items
        Consumable testItem = new Consumable("TestItem", 10, "health");
        
        floorTile.add_item(testItem);
        wallTile.add_item(testItem);
        doorTile.add_item(testItem);
        stairsTile.add_item(testItem);
        
        assertTrue(floorTile.has_items(), "Floor tile should have items");
        assertTrue(wallTile.has_items(), "Wall tile should have items");
        assertTrue(doorTile.has_items(), "Door tile should have items");
        assertTrue(stairsTile.has_items(), "Stairs tile should have items");
        
        // Test that all tile types can be explored
        floorTile.set_explored();
        wallTile.set_explored();
        doorTile.set_explored();
        stairsTile.set_explored();
        
        assertTrue(floorTile.is_explored(), "Floor tile should be explorable");
        assertTrue(wallTile.is_explored(), "Wall tile should be explorable");
        assertTrue(doorTile.is_explored(), "Door tile should be explorable");
        assertTrue(stairsTile.is_explored(), "Stairs tile should be explorable");
    }

    /**
     * Tests tile position immutability.
     */
    @Test
    @DisplayName("Tile Position Immutability")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testTilePositionImmutability() {
        Position originalPos = new Position(10, 10);
        Tile tile = new Tile(TileType.FLOOR, originalPos);
        
        // Verify position is returned correctly
        Position returnedPos = tile.get_position();
        assertEquals(originalPos, returnedPos, "Tile should return correct position");
        
        // Note: Position class doesn't implement defensive copying
        // The returned position is the same object as the original
        assertSame(originalPos, returnedPos, "Tile should return the same position object");
        
        // Verify position values are correct
        assertEquals(10, returnedPos.get_x(), "Returned position X should be correct");
        assertEquals(10, returnedPos.get_y(), "Returned position Y should be correct");
    }
} 