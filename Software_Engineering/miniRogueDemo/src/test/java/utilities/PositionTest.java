package utilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for Position utility class.
 * Tests coordinate operations, distance calculations, and immutability.
 * Appropriate for a school project.
 */
@DisplayName("Position Tests")
class PositionTest {

    private Position position1;
    private Position position2;
    private Position position3;

    @BeforeEach
    void setUp() {
        position1 = new Position(5, 5);
        position2 = new Position(10, 10);
        position3 = new Position(0, 0);
    }

    /**
     * Tests Position constructor and basic properties.
     */
    @Test
    @DisplayName("Position Constructor and Basic Properties")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testPositionConstructor() {
        // Test constructor
        Position pos = new Position(3, 7);
        assertEquals(3, pos.get_x(), "X coordinate should be 3");
        assertEquals(7, pos.get_y(), "Y coordinate should be 7");
        
        // Test negative coordinates
        Position negPos = new Position(-5, -10);
        assertEquals(-5, negPos.get_x(), "Negative X coordinate should be -5");
        assertEquals(-10, negPos.get_y(), "Negative Y coordinate should be -10");
    }

    /**
     * Tests Position immutability.
     */
    @Test
    @DisplayName("Position Immutability")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testPositionImmutability() {
        // Test that Position is immutable
        Position original = new Position(5, 5);
        Position moved = original.move(2, 3);
        
        // Original should remain unchanged
        assertEquals(5, original.get_x(), "Original X should remain unchanged");
        assertEquals(5, original.get_y(), "Original Y should remain unchanged");
        
        // Moved should be a new object
        assertNotSame(original, moved, "Move should return a new Position object");
        assertEquals(7, moved.get_x(), "Moved X should be 7");
        assertEquals(8, moved.get_y(), "Moved Y should be 8");
    }

    /**
     * Tests Position move method.
     */
    @Test
    @DisplayName("Position Move Method")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testPositionMove() {
        // Test positive movement
        Position moved1 = position1.move(3, 4);
        assertEquals(8, moved1.get_x(), "X should be 8 after moving 3");
        assertEquals(9, moved1.get_y(), "Y should be 9 after moving 4");
        
        // Test negative movement
        Position moved2 = position1.move(-2, -3);
        assertEquals(3, moved2.get_x(), "X should be 3 after moving -2");
        assertEquals(2, moved2.get_y(), "Y should be 2 after moving -3");
        
        // Test zero movement
        Position moved3 = position1.move(0, 0);
        assertEquals(5, moved3.get_x(), "X should be unchanged after moving 0");
        assertEquals(5, moved3.get_y(), "Y should be unchanged after moving 0");
    }

    /**
     * Tests Euclidean distance calculation.
     */
    @Test
    @DisplayName("Euclidean Distance Calculation")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testEuclideanDistance() {
        // Test distance to same position
        assertEquals(0.0, position1.distance_to(position1), 0.001, "Distance to self should be 0");
        
        // Test distance to different position
        double distance = position1.distance_to(position2);
        double expectedDistance = Math.sqrt(25 + 25); // sqrt(5^2 + 5^2)
        assertEquals(expectedDistance, distance, 0.001, "Distance should be calculated correctly");
        
        // Test distance with negative coordinates
        Position negPos = new Position(-5, -5);
        double negDistance = position1.distance_to(negPos);
        double expectedNegDistance = Math.sqrt(100 + 100); // sqrt(10^2 + 10^2)
        assertEquals(expectedNegDistance, negDistance, 0.001, "Distance with negative coordinates should be correct");
    }

    /**
     * Tests Manhattan distance calculation.
     */
    @Test
    @DisplayName("Manhattan Distance Calculation")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testManhattanDistance() {
        // Test Manhattan distance to same position
        assertEquals(0, position1.manhattan_distance_to(position1), "Manhattan distance to self should be 0");
        
        // Test Manhattan distance to different position
        int manhattanDistance = position1.manhattan_distance_to(position2);
        assertEquals(10, manhattanDistance, "Manhattan distance should be 10 (5+5)");
        
        // Test Manhattan distance with negative coordinates
        Position negPos = new Position(-5, -5);
        int negManhattanDistance = position1.manhattan_distance_to(negPos);
        assertEquals(20, negManhattanDistance, "Manhattan distance with negative coordinates should be 20");
    }

    /**
     * Tests adjacent position detection.
     */
    @Test
    @DisplayName("Adjacent Position Detection")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testAdjacentPositionDetection() {
        // Test same position (not adjacent)
        assertFalse(position1.is_adjacent_to(position1), "Position should not be adjacent to itself");
        
        // Test adjacent positions (including diagonals)
        assertTrue(position1.is_adjacent_to(new Position(4, 5)), "Left adjacent");
        assertTrue(position1.is_adjacent_to(new Position(6, 5)), "Right adjacent");
        assertTrue(position1.is_adjacent_to(new Position(5, 4)), "Up adjacent");
        assertTrue(position1.is_adjacent_to(new Position(5, 6)), "Down adjacent");
        assertTrue(position1.is_adjacent_to(new Position(4, 4)), "Diagonal adjacent");
        assertTrue(position1.is_adjacent_to(new Position(6, 6)), "Diagonal adjacent");
        assertTrue(position1.is_adjacent_to(new Position(4, 6)), "Diagonal adjacent");
        assertTrue(position1.is_adjacent_to(new Position(6, 4)), "Diagonal adjacent");
        
        // Test non-adjacent positions
        assertFalse(position1.is_adjacent_to(new Position(3, 5)), "Not adjacent (too far left)");
        assertFalse(position1.is_adjacent_to(new Position(7, 5)), "Not adjacent (too far right)");
        assertFalse(position1.is_adjacent_to(new Position(5, 3)), "Not adjacent (too far up)");
        assertFalse(position1.is_adjacent_to(new Position(5, 7)), "Not adjacent (too far down)");
        assertFalse(position1.is_adjacent_to(new Position(3, 3)), "Not adjacent (too far diagonal)");
    }

    /**
     * Tests Position equality.
     */
    @Test
    @DisplayName("Position Equality")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testPositionEquality() {
        // Test same object
        assertTrue(position1.equals(position1), "Position should equal itself");
        
        // Test equal positions
        Position equalPos = new Position(5, 5);
        assertTrue(position1.equals(equalPos), "Equal positions should be equal");
        assertTrue(equalPos.equals(position1), "Equality should be symmetric");
        
        // Test different positions
        assertFalse(position1.equals(position2), "Different positions should not be equal");
        assertFalse(position1.equals(null), "Position should not equal null");
        
        // Test with different object type
        assertFalse(position1.equals("not a position"), "Position should not equal different type");
    }

    /**
     * Tests Position hashCode.
     */
    @Test
    @DisplayName("Position HashCode")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testPositionHashCode() {
        // Test that equal positions have same hash code
        Position equalPos = new Position(5, 5);
        assertEquals(position1.hashCode(), equalPos.hashCode(), "Equal positions should have same hash code");
        
        // Test that different positions have different hash codes
        assertNotEquals(position1.hashCode(), position2.hashCode(), "Different positions should have different hash codes");
    }

    /**
     * Tests Position toString.
     */
    @Test
    @DisplayName("Position ToString")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testPositionToString() {
        // Test toString format
        String str = position1.toString();
        assertEquals("Position(5, 5)", str, "ToString should format correctly");
        
        // Test toString with negative coordinates
        Position negPos = new Position(-3, -7);
        String negStr = negPos.toString();
        assertEquals("Position(-3, -7)", negStr, "ToString should handle negative coordinates");
    }

    /**
     * Tests edge cases and boundary conditions.
     */
    @Test
    @DisplayName("Edge Cases and Boundary Conditions")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testEdgeCases() {
        // Test maximum integer coordinates
        Position maxPos = new Position(Integer.MAX_VALUE, Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, maxPos.get_x(), "Should handle maximum X coordinate");
        assertEquals(Integer.MAX_VALUE, maxPos.get_y(), "Should handle maximum Y coordinate");
        
        // Test minimum integer coordinates
        Position minPos = new Position(Integer.MIN_VALUE, Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, minPos.get_x(), "Should handle minimum X coordinate");
        assertEquals(Integer.MIN_VALUE, minPos.get_y(), "Should handle minimum Y coordinate");
        
        // Test distance calculations with extreme values
        assertDoesNotThrow(() -> {
            maxPos.distance_to(minPos);
            maxPos.manhattan_distance_to(minPos);
        }, "Distance calculations should handle extreme values");
    }

    /**
     * Tests complex distance scenarios.
     */
    @Test
    @DisplayName("Complex Distance Scenarios")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testComplexDistanceScenarios() {
        // Test various distance scenarios
        Position pos1 = new Position(0, 0);
        Position pos2 = new Position(3, 4);
        
        // Pythagorean triple: 3, 4, 5
        assertEquals(5.0, pos1.distance_to(pos2), 0.001, "Distance should be 5 for 3-4-5 triangle");
        assertEquals(7, pos1.manhattan_distance_to(pos2), "Manhattan distance should be 7");
        
        // Test with larger numbers
        Position pos3 = new Position(100, 200);
        Position pos4 = new Position(300, 400);
        double expectedDistance = Math.sqrt(40000 + 40000); // sqrt(200^2 + 200^2)
        assertEquals(expectedDistance, pos3.distance_to(pos4), 0.001, "Distance with large numbers should be correct");
    }

    /**
     * Tests adjacent detection edge cases.
     */
    @Test
    @DisplayName("Adjacent Detection Edge Cases")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testAdjacentDetectionEdgeCases() {
        // Test with extreme coordinates
        Position extremePos = new Position(Integer.MAX_VALUE, Integer.MAX_VALUE);
        Position adjacentExtreme = new Position(Integer.MAX_VALUE - 1, Integer.MAX_VALUE - 1);
        
        assertTrue(extremePos.is_adjacent_to(adjacentExtreme), "Should detect adjacent with extreme coordinates");
        
        // Test with zero coordinates
        Position zeroPos = new Position(0, 0);
        Position adjacentZero = new Position(1, 1);
        
        assertTrue(zeroPos.is_adjacent_to(adjacentZero), "Should detect adjacent with zero coordinates");
        
        // Test with negative coordinates
        Position negPos = new Position(-5, -5);
        Position adjacentNeg = new Position(-4, -4);
        
        assertTrue(negPos.is_adjacent_to(adjacentNeg), "Should detect adjacent with negative coordinates");
    }

    /**
     * Tests move method edge cases.
     */
    @Test
    @DisplayName("Move Method Edge Cases")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMoveMethodEdgeCases() {
        // Test moving by zero
        Position moved = position1.move(0, 0);
        assertEquals(position1.get_x(), moved.get_x(), "Moving by 0 should not change X");
        assertEquals(position1.get_y(), moved.get_y(), "Moving by 0 should not change Y");
        
        // Test moving by large values
        Position movedLarge = position1.move(1000, -1000);
        assertEquals(1005, movedLarge.get_x(), "Should handle large positive X movement");
        assertEquals(-995, movedLarge.get_y(), "Should handle large negative Y movement");
        
        // Test moving by maximum values
        assertDoesNotThrow(() -> {
            position1.move(Integer.MAX_VALUE, Integer.MIN_VALUE);
        }, "Should handle maximum integer movements");
    }
} 