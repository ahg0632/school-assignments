package utilities;

import model.map.Map;
import utilities.Tile;
import enums.TileType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for Collision utility class.
 * Tests walkable tile detection and line of sight calculations.
 * Appropriate for a school project.
 */
@DisplayName("Collision Tests")
class CollisionTest {

    private Map testMap;
    private Tile walkableTile;
    private Tile nonWalkableTile;

    @BeforeEach
    void setUp() {
        // Create a test map
        testMap = new Map(1, Map.FloorType.REGULAR);
        
        // Create test tiles
        walkableTile = new Tile(TileType.FLOOR, new Position(0, 0));
        nonWalkableTile = new Tile(TileType.WALL, new Position(0, 0));
    }

    /**
     * Tests walkable tile detection with valid coordinates.
     */
    @Test
    @DisplayName("Walkable Tile Detection - Valid Coordinates")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testWalkableTileDetectionValid() {
        // Test walkable tile
        assertTrue(Collision.isWalkable(testMap, 5, 5), "Walkable tile should return true");
        
        // Test non-walkable tile (wall)
        // Note: This depends on the map generation, so we test the method logic
        // by checking that the method handles the tile correctly
        assertDoesNotThrow(() -> {
            Collision.isWalkable(testMap, 0, 0);
        }, "Walkable detection should not throw exceptions for valid coordinates");
    }

    /**
     * Tests walkable tile detection with boundary conditions.
     */
    @Test
    @DisplayName("Walkable Tile Detection - Boundary Conditions")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testWalkableTileDetectionBoundaries() {
        // Test boundary conditions
        assertFalse(Collision.isWalkable(testMap, -1, 5), "Negative x should return false");
        assertFalse(Collision.isWalkable(testMap, 5, -1), "Negative y should return false");
        assertFalse(Collision.isWalkable(testMap, testMap.get_width(), 5), "X >= width should return false");
        assertFalse(Collision.isWalkable(testMap, 5, testMap.get_height()), "Y >= height should return false");
    }

    /**
     * Tests walkable tile detection with null map.
     */
    @Test
    @DisplayName("Walkable Tile Detection - Null Map")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testWalkableTileDetectionNullMap() {
        // Test null map
        assertFalse(Collision.isWalkable(null, 5, 5), "Null map should return false");
    }

    /**
     * Tests line of sight calculation with clear path.
     */
    @Test
    @DisplayName("Line of Sight - Clear Path")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testLineOfSightClearPath() {
        // Test line of sight between two points in walkable area
        assertDoesNotThrow(() -> {
            boolean hasLOS = Collision.hasLineOfSight(testMap, 5, 5, 10, 10);
            // The result depends on the actual map layout, so we just test it doesn't throw
        }, "Line of sight calculation should not throw exceptions");
    }

    /**
     * Tests line of sight calculation with same start and end points.
     */
    @Test
    @DisplayName("Line of Sight - Same Points")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testLineOfSightSamePoints() {
        // Test line of sight from point to itself
        assertTrue(Collision.hasLineOfSight(testMap, 5, 5, 5, 5), "Line of sight to same point should be true");
    }

    /**
     * Tests line of sight calculation with horizontal path.
     */
    @Test
    @DisplayName("Line of Sight - Horizontal Path")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testLineOfSightHorizontalPath() {
        // Test horizontal line of sight
        assertDoesNotThrow(() -> {
            Collision.hasLineOfSight(testMap, 5, 5, 10, 5);
        }, "Horizontal line of sight should not throw exceptions");
    }

    /**
     * Tests line of sight calculation with vertical path.
     */
    @Test
    @DisplayName("Line of Sight - Vertical Path")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testLineOfSightVerticalPath() {
        // Test vertical line of sight
        assertDoesNotThrow(() -> {
            Collision.hasLineOfSight(testMap, 5, 5, 5, 10);
        }, "Vertical line of sight should not throw exceptions");
    }

    /**
     * Tests line of sight calculation with diagonal path.
     */
    @Test
    @DisplayName("Line of Sight - Diagonal Path")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testLineOfSightDiagonalPath() {
        // Test diagonal line of sight
        assertDoesNotThrow(() -> {
            Collision.hasLineOfSight(testMap, 5, 5, 10, 10);
        }, "Diagonal line of sight should not throw exceptions");
    }

    /**
     * Tests line of sight calculation with boundary coordinates.
     */
    @Test
    @DisplayName("Line of Sight - Boundary Coordinates")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testLineOfSightBoundaryCoordinates() {
        // Test line of sight with boundary coordinates
        assertDoesNotThrow(() -> {
            Collision.hasLineOfSight(testMap, 0, 0, testMap.get_width() - 1, testMap.get_height() - 1);
        }, "Line of sight with boundary coordinates should not throw exceptions");
    }

    /**
     * Tests line of sight calculation with invalid coordinates.
     */
    @Test
    @DisplayName("Line of Sight - Invalid Coordinates")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testLineOfSightInvalidCoordinates() {
        // Test line of sight with invalid coordinates
        assertDoesNotThrow(() -> {
            Collision.hasLineOfSight(testMap, -1, 5, 10, 10);
            Collision.hasLineOfSight(testMap, 5, -1, 10, 10);
            Collision.hasLineOfSight(testMap, 5, 5, -1, 10);
            Collision.hasLineOfSight(testMap, 5, 5, 10, -1);
        }, "Line of sight with invalid coordinates should not throw exceptions");
    }

    /**
     * Tests line of sight calculation with null map.
     */
    @Test
    @DisplayName("Line of Sight - Null Map")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testLineOfSightNullMap() {
        // Test line of sight with null map
        assertDoesNotThrow(() -> {
            Collision.hasLineOfSight(null, 5, 5, 10, 10);
        }, "Line of sight with null map should not throw exceptions");
    }

    /**
     * Tests line of sight calculation with complex paths.
     */
    @Test
    @DisplayName("Line of Sight - Complex Paths")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testLineOfSightComplexPaths() {
        // Test various complex line of sight scenarios
        assertDoesNotThrow(() -> {
            // Test long diagonal path
            Collision.hasLineOfSight(testMap, 1, 1, testMap.get_width() - 2, testMap.get_height() - 2);
            
            // Test path with large x difference
            Collision.hasLineOfSight(testMap, 1, 5, testMap.get_width() - 2, 5);
            
            // Test path with large y difference
            Collision.hasLineOfSight(testMap, 5, 1, 5, testMap.get_height() - 2);
        }, "Complex line of sight calculations should not throw exceptions");
    }

    /**
     * Tests collision detection robustness.
     */
    @Test
    @DisplayName("Collision Detection Robustness")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testCollisionDetectionRobustness() {
        // Test that collision detection is robust
        assertDoesNotThrow(() -> {
            // Test various coordinate combinations
            for (int x = -5; x < testMap.get_width() + 5; x++) {
                for (int y = -5; y < testMap.get_height() + 5; y++) {
                    Collision.isWalkable(testMap, x, y);
                }
            }
        }, "Collision detection should be robust for all coordinate ranges");
    }

    /**
     * Tests line of sight algorithm correctness.
     */
    @Test
    @DisplayName("Line of Sight Algorithm Correctness")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testLineOfSightAlgorithmCorrectness() {
        // Test that line of sight algorithm produces consistent results
        assertDoesNotThrow(() -> {
            // Test same path multiple times
            boolean result1 = Collision.hasLineOfSight(testMap, 5, 5, 10, 10);
            boolean result2 = Collision.hasLineOfSight(testMap, 5, 5, 10, 10);
            assertEquals(result1, result2, "Line of sight should be consistent for same path");
            
            // Test reverse path
            boolean result3 = Collision.hasLineOfSight(testMap, 10, 10, 5, 5);
            assertEquals(result1, result3, "Line of sight should be same for reverse path");
        }, "Line of sight algorithm should be consistent");
    }

    /**
     * Tests edge cases for collision detection.
     */
    @Test
    @DisplayName("Collision Detection Edge Cases")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testCollisionDetectionEdgeCases() {
        // Test edge cases
        assertDoesNotThrow(() -> {
            // Test maximum integer coordinates
            Collision.isWalkable(testMap, Integer.MAX_VALUE, 5);
            Collision.isWalkable(testMap, 5, Integer.MAX_VALUE);
            Collision.isWalkable(testMap, Integer.MIN_VALUE, 5);
            Collision.isWalkable(testMap, 5, Integer.MIN_VALUE);
            
            // Test line of sight with extreme coordinates
            Collision.hasLineOfSight(testMap, Integer.MAX_VALUE, Integer.MAX_VALUE, 
                                   Integer.MIN_VALUE, Integer.MIN_VALUE);
        }, "Collision detection should handle extreme coordinate values");
    }
} 