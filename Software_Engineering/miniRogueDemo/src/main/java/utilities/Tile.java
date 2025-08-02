package utilities;

import enums.TileType;
import model.characters.Character;
import model.items.Item;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single tile in the game map with its contents and properties.
 * Handles tile state, occupants, and items for map management.
 */
public class Tile {
    private final TileType tileType;
    private final Position position;
    private final List<Item> items;
    private boolean explored;

    /**
     * MANDATORY: Constructor to create a Tile
     *
     * @param tileType The type of this tile
     * @param position The position of this tile
     */
    public Tile(TileType tileType, Position position) {
        this.tileType = tileType;
        this.position = position;
        this.items = new ArrayList<>();
        this.explored = false;
    }

    /**
     * MANDATORY: Get the tile type
     *
     * @return The TileType of this tile
     */
    public TileType get_tile_type() {
        return tileType;
    }

    /**
     * MANDATORY: Get the tile position
     *
     * @return The Position of this tile
     */
    public Position get_position() {
        return position;
    }

    /**
     * MANDATORY: Check if tile is walkable
     *
     * @return true if characters can move onto this tile
     */
    public boolean is_walkable() {
        return tileType.is_walkable();
    }

    /**
     * MANDATORY: Add an item to this tile
     *
     * @param item The Item to add
     */
    public void add_item(Item item) {
        if (item != null) {
            items.add(item);
        }
    }

    /**
     * MANDATORY: Remove an item from this tile
     *
     * @param item The Item to remove
     * @return true if item was removed, false if not found
     */
    public boolean remove_item(Item item) {
        return items.remove(item);
    }

    /**
     * MANDATORY: Get all items on this tile
     *
     * @return List of Items on this tile (defensive copy)
     */
    public List<Item> get_items() {
        return new ArrayList<>(items);
    }

    /**
     * MANDATORY: Check if tile has any items
     *
     * @return true if tile contains items
     */
    public boolean has_items() {
        return !items.isEmpty();
    }

    /**
     * MANDATORY: Clear all items from this tile
     */
    public void clear_items() {
        items.clear();
    }

    /**
     * MANDATORY: Check if tile has been explored by player
     *
     * @return true if tile has been explored
     */
    public boolean is_explored() {
        return explored;
    }

    /**
     * MANDATORY: Mark tile as explored
     */
    public void set_explored() {
        this.explored = true;
    }

    /**
     * MANDATORY: Reset exploration status
     */
    public void reset_exploration() {
        this.explored = false;
    }

    @Override
    public String toString() {
        return String.format("Tile[%s at %s, items=%d]",
                tileType.get_type_name(),
                position.toString(),
                items.size());
    }
} 