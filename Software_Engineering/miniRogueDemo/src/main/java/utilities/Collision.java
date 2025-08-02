package utilities;

import model.map.Map;

public class Collision {
    public static boolean isWalkable(Map map, int x, int y) {
        if (map == null) return false;
        if (x < 0 || y < 0 || x >= map.get_width() || y >= map.get_height()) return false;
        Tile tile = map.get_tile(x, y);
        return tile != null && tile.is_walkable();
    }

    // Line of sight check using Bresenham's algorithm
    public static boolean hasLineOfSight(Map map, int x0, int y0, int x1, int y1) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;
        int x = x0;
        int y = y0;
        while (x != x1 || y != y1) {
            if (!(isWalkable(map, x, y))) return false;
            int e2 = 2 * err;
            if (e2 > -dy) { err -= dy; x += sx; }
            if (e2 < dx) { err += dx; y += sy; }
        }
        // Check final tile
        return isWalkable(map, x1, y1);
    }
} 