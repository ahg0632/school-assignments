package view.renderers;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import model.map.Map;
import model.items.Item;
import model.equipment.Equipment;
import utilities.Tile;
import enums.TileType;
import enums.GameConstants;

/**
 * Responsible for rendering the game map and tiles.
 * Follows Single Responsibility Principle - only handles map rendering.
 * 
 * This class extracts map rendering logic from GamePanel.java to improve
 * code organization and maintainability.
 */
public class MapRenderer {
    private static final Logger LOGGER = Logger.getLogger(MapRenderer.class.getName());
    
    // Map constants
    private static final int MAP_WIDTH = 50;
    private static final int MAP_HEIGHT = 30;
    private static final int RIGHT_PANEL_WIDTH = 250;
    private static final int MAP_OFFSET_Y = 35;
    
    // Tile images
    private final HashMap<TileType, BufferedImage> tileImages;
    private final HashMap<String, BufferedImage> itemIconCache;
    
    public MapRenderer() {
        this.tileImages = new HashMap<>();
        this.itemIconCache = new HashMap<>();
    }
    
    /**
     * Render the entire map
     * 
     * @param g2d Graphics context
     * @param currentMap The map to render
     * @param debugMode Whether debug mode is enabled
     * @param mapOffsetX X offset for map positioning
     * @param mapOffsetY Y offset for map positioning
     * @param panelWidth Panel width
     */
    public void renderMap(Graphics2D g2d, Map currentMap, boolean debugMode, 
                         int mapOffsetX, int mapOffsetY, int panelWidth) {
        int tileSize = GameConstants.TILE_SIZE;
        
        // Calculate map positioning
        int availableWidth = panelWidth - RIGHT_PANEL_WIDTH;
        
        // Set clipping region to prevent map content from rendering outside the frame
        Rectangle clipRect = new Rectangle(0, MAP_OFFSET_Y, 
                                         (MAP_WIDTH * tileSize) / GameConstants.SCALING_FACTOR, 
                                         (MAP_HEIGHT * tileSize) / GameConstants.SCALING_FACTOR);
        g2d.setClip(clipRect);
        
        // Render tiles with offset
        for (int x = 0; x < currentMap.get_width(); x++) {
            for (int y = 0; y < currentMap.get_height(); y++) {
                Tile tile = currentMap.get_tile(x, y);
                if (tile != null && (debugMode || tile.is_explored())) {
                    renderTile(g2d, tile, mapOffsetX + x * tileSize, mapOffsetY + y * tileSize);
                }
            }
        }
        
        // Reset clipping to allow other elements to render normally
        g2d.setClip(null);
        
        // Draw map border
        drawMapBorder(g2d, tileSize);
    }
    
    /**
     * Render a single tile
     * 
     * @param g2d Graphics context
     * @param tile The tile to render
     * @param screenX X coordinate on screen
     * @param screenY Y coordinate on screen
     */
    private void renderTile(Graphics2D g2d, Tile tile, int screenX, int screenY) {
        int tileSize = GameConstants.TILE_SIZE;
        
        // Render tile base
        renderTileBase(g2d, tile, screenX, screenY, tileSize);
        
        // Render items on tile
        if (tile.has_items()) {
            renderTileItems(g2d, tile, screenX, screenY, tileSize);
        }
    }
    
    /**
     * Render the base tile (floor, wall, etc.)
     */
    private void renderTileBase(Graphics2D g2d, Tile tile, int screenX, int screenY, int tileSize) {
        BufferedImage tileImage = tileImages.get(tile.get_tile_type());
        
        switch (tile.get_tile_type()) {
            case WALL:
                if (tileImage != null) {
                    g2d.drawImage(tileImage, screenX, screenY, tileSize, tileSize, null);
                } else {
                    // Fallback to colored rectangle
                    g2d.setColor(Color.GRAY);
                    g2d.fillRect(screenX, screenY, tileSize, tileSize);
                }
                break;
                
            case FLOOR:
                if (tileImage != null) {
                    g2d.drawImage(tileImage, screenX, screenY, tileSize, tileSize, null);
                } else {
                    // Fallback to colored rectangle
                    g2d.setColor(Color.LIGHT_GRAY);
                    g2d.fillRect(screenX, screenY, tileSize, tileSize);
                }
                break;
                
            case ENTRANCE:
                if (tileImage != null) {
                    g2d.drawImage(tileImage, screenX, screenY, tileSize, tileSize, null);
                } else {
                    // Fallback to colored rectangle
                    g2d.setColor(Color.GREEN);
                    g2d.fillRect(screenX, screenY, tileSize, tileSize);
                }
                break;
                
            case BOSS_ROOM:
                if (tileImage != null) {
                    g2d.drawImage(tileImage, screenX, screenY, tileSize, tileSize, null);
                } else {
                    // Fallback to colored rectangle
                    g2d.setColor(Color.RED);
                    g2d.fillRect(screenX, screenY, tileSize, tileSize);
                }
                break;
                
            case STAIRS:
                if (tileImage != null) {
                    g2d.drawImage(tileImage, screenX, screenY, tileSize, tileSize, null);
                } else {
                    // Fallback to colored rectangle
                    g2d.setColor(Color.YELLOW);
                    g2d.fillRect(screenX, screenY, tileSize, tileSize);
                }
                break;
                
            case UPGRADER_SPAWN:
                // Use floor image for upgrader spawn tiles
                BufferedImage floorImage = tileImages.get(TileType.FLOOR);
                if (floorImage != null) {
                    g2d.drawImage(floorImage, screenX, screenY, tileSize, tileSize, null);
                } else {
                    g2d.setColor(Color.LIGHT_GRAY);
                    g2d.fillRect(screenX, screenY, tileSize, tileSize);
                }
                break;
                
            default:
                // Unknown tile type - use dark gray
                g2d.setColor(Color.DARK_GRAY);
                g2d.fillRect(screenX, screenY, tileSize, tileSize);
                break;
        }
        
        // Draw tile border
        g2d.setColor(Color.BLACK);
        g2d.drawRect(screenX, screenY, tileSize, tileSize);
    }
    
    /**
     * Render items on a tile
     */
    private void renderTileItems(Graphics2D g2d, Tile tile, int screenX, int screenY, int tileSize) {
        List<Item> items = tile.get_items();
        if (!items.isEmpty()) {
            Item item = items.get(0); // Draw the first item
            
            BufferedImage itemImage = getItemImage(item);
            if (itemImage != null) {
                // Draw item image centered on tile
                int itemSize = tileSize / 2;
                int itemX = screenX + (tileSize - itemSize) / 2;
                int itemY = screenY + (tileSize - itemSize) / 2;
                g2d.drawImage(itemImage, itemX, itemY, itemSize, itemSize, null);
            } else {
                // Fallback to colored circle
                g2d.setColor(Color.BLUE);
                int itemSize = tileSize / 3;
                int itemX = screenX + (tileSize - itemSize) / 2;
                int itemY = screenY + (tileSize - itemSize) / 2;
                g2d.fillOval(itemX, itemY, itemSize, itemSize);
            }
        }
    }
    
    /**
     * Get item image for rendering
     */
    private BufferedImage getItemImage(Item item) {
        if (item instanceof Equipment) {
            // For equipment, use the equipment's image path
            Equipment equipment = (Equipment) item;
            String imagePath = equipment.get_image_path();
            
            if (itemIconCache.containsKey(imagePath)) {
                return itemIconCache.get(imagePath);
            } else {
                try {
                    BufferedImage image = javax.imageio.ImageIO.read(
                        getClass().getClassLoader().getResourceAsStream(imagePath));
                    if (image != null) {
                        itemIconCache.put(imagePath, image);
                        return image;
                    }
                } catch (Exception e) {
                    LOGGER.warning("Failed to load item image: " + imagePath);
                }
            }
        } else {
            // For other items, try to get icon
            String itemName = item.get_name().toLowerCase();
            if (itemIconCache.containsKey(itemName)) {
                return itemIconCache.get(itemName);
            }
        }
        
        return null;
    }
    
    /**
     * Draw map border with pixelated rounded corners
     */
    private void drawMapBorder(Graphics2D g2d, int tileSize) {
        int mapWidth = MAP_WIDTH * tileSize;
        int mapHeight = MAP_HEIGHT * tileSize;
        int cornerRadius = 8;
        
        // Draw white border
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2f));
        
        // Draw main rectangle
        g2d.drawRect(0, MAP_OFFSET_Y, mapWidth, mapHeight);
        
        // Draw pixelated corners
        drawPixelatedCorner(g2d, 0, MAP_OFFSET_Y, cornerRadius, 0); // Top-left
        drawPixelatedCorner(g2d, mapWidth, MAP_OFFSET_Y, cornerRadius, 1); // Top-right
        drawPixelatedCorner(g2d, 0, MAP_OFFSET_Y + mapHeight, cornerRadius, 2); // Bottom-left
        drawPixelatedCorner(g2d, mapWidth, MAP_OFFSET_Y + mapHeight, cornerRadius, 3); // Bottom-right
    }
    
    /**
     * Draw a pixelated corner
     * 
     * @param g2d Graphics context
     * @param centerX Center X coordinate
     * @param centerY Center Y coordinate
     * @param radius Corner radius
     * @param cornerType 0=top-left, 1=top-right, 2=bottom-left, 3=bottom-right
     */
    private void drawPixelatedCorner(Graphics2D g2d, int centerX, int centerY, int radius, int cornerType) {
        g2d.setColor(Color.WHITE);
        
        // Draw corner pixels based on corner type
        switch (cornerType) {
            case 0: // Top-left
                g2d.fillRect(centerX, centerY, radius, 2);
                g2d.fillRect(centerX, centerY, 2, radius);
                break;
            case 1: // Top-right
                g2d.fillRect(centerX - radius, centerY, radius, 2);
                g2d.fillRect(centerX - 2, centerY, 2, radius);
                break;
            case 2: // Bottom-left
                g2d.fillRect(centerX, centerY - 2, radius, 2);
                g2d.fillRect(centerX, centerY - radius, 2, radius);
                break;
            case 3: // Bottom-right
                g2d.fillRect(centerX - radius, centerY - 2, radius, 2);
                g2d.fillRect(centerX - 2, centerY - radius, 2, radius);
                break;
        }
    }
    
    /**
     * Set tile image for a specific tile type
     */
    public void setTileImage(TileType tileType, BufferedImage image) {
        tileImages.put(tileType, image);
    }
    
    /**
     * Set item icon for a specific item
     */
    public void setItemIcon(String itemName, BufferedImage image) {
        itemIconCache.put(itemName.toLowerCase(), image);
    }
    
    /**
     * Clear item icon cache
     */
    public void clearItemCache() {
        itemIconCache.clear();
    }
    
    /**
     * Get map dimensions
     */
    public int getMapWidth() {
        return MAP_WIDTH;
    }
    
    public int getMapHeight() {
        return MAP_HEIGHT;
    }
    
    public int getMapOffsetY() {
        return MAP_OFFSET_Y;
    }
} 