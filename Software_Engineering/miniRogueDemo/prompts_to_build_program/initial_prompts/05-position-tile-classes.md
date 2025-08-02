## 05-position-tile-classes.md

# CRITICAL REQUIREMENTS - Position and Tile Utility Classes

### MANDATORY DIRECTIVE ###
You are an expert Java game developer. **CRITICAL**: Implement the Position and Tile utility classes exactly as specified below for the Mini Rogue Demo map system.

## UTILITY CLASS SPECIFICATIONS

### **CRITICAL**: Create Position.java in utilities package with this exact implementation:

```

package utilities;

import java.util.Objects;

/**

* Utility class representing a 2D coordinate position in the game world.
* Immutable class used for character positions, item locations, and map coordinates.
*/
public final class Position {
private final int x;
private final int y;

/**
    * MANDATORY: Constructor to create a Position
    * 
    * @param x The x-coordinate
    * @param y The y-coordinate
*/
public Position(int x, int y) {
this.x = x;
this.y = y;
}

/**
    * MANDATORY: Get the x-coordinate
    * 
    * @return The x-coordinate value
*/
public int get_x() {
return x;
}

/**
    * MANDATORY: Get the y-coordinate
    * 
    * @return The y-coordinate value
*/
public int get_y() {
return y;
}

/**
    * MANDATORY: Create a new Position moved by the specified offset
    * 
    * @param deltaX The x offset
    * @param deltaY The y offset
    * @return New Position object with offset applied
*/
public Position move(int deltaX, int deltaY) {
return new Position(x + deltaX, y + deltaY);
}

/**
    * MANDATORY: Calculate distance to another position
    * 
    * @param other The other Position
    * @return Euclidean distance as double
*/
public double distance_to(Position other) {
int dx = this.x - other.x;
int dy = this.y - other.y;
return Math.sqrt(dx * dx + dy * dy);
}

/**
    * MANDATORY: Calculate Manhattan distance to another position
    * 
    * @param other The other Position
    * @return Manhattan distance as integer
*/
public int manhattan_distance_to(Position other) {
return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
}

/**
    * MANDATORY: Check if this position is adjacent to another
    * 
    * @param other The other Position
    * @return true if positions are adjacent (including diagonally)
*/
public boolean is_adjacent_to(Position other) {
int dx = Math.abs(this.x - other.x);
int dy = Math.abs(this.y - other.y);
return dx <= 1 \&\& dy <= 1 \&\& !(dx == 0 \&\& dy == 0);
}

@Override
public boolean equals(Object obj) {
if (this == obj) return true;
if (obj == null || getClass() != obj.getClass()) return false;
Position position = (Position) obj;
return x == position.x \&\& y == position.y;
}

@Override
public int hashCode() {
return Objects.hash(x, y);
}

@Override
public String toString() {
return String.format("Position(%d, %d)", x, y);
}
}

```

### **CRITICAL**: Create Tile.java in utilities package with this exact implementation:

```

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
private Character occupant;
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
this.occupant = null;
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
return tileType.is_walkable() \&\& occupant == null;
}

/**
    * MANDATORY: Get the character occupying this tile
    * 
    * @return The Character on this tile, or null if empty
*/
public Character get_occupant() {
return occupant;
}

/**
    * MANDATORY: Set the character occupying this tile
    * 
    * @param character The Character to place on this tile
*/
public void set_occupant(Character character) {
this.occupant = character;
}

/**
    * MANDATORY: Remove the occupant from this tile
*/
public void clear_occupant() {
this.occupant = null;
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
return String.format("Tile[%s at %s, occupied=%s, items=%d]",
tileType.get_type_name(),
position.toString(),
occupant != null,
items.size());
}
}

```

### **MANDATORY**: Class Design Requirements

1. **CRITICAL**: Position class must be immutable (final fields, no setters)
2. **CRITICAL**: Tile class manages its own state and contents
3. **CRITICAL**: Method names must use snake_case convention
4. **CRITICAL**: Complete JavaDoc documentation for all methods
5. **CRITICAL**: Proper defensive copying for collections

### **MANDATORY**: Design Principles

**CRITICAL**: These classes implement:
- **Value Object Pattern**: Position represents immutable coordinates
- **State Object Pattern**: Tile manages mutable game state
- **Encapsulation**: Private fields with controlled access
- **Null Safety**: Proper null checking for parameters

### **MANDATORY**: Usage Context

**CRITICAL**: These classes will be:
- Used by Map class for dungeon layout representation
- Used by Character classes for position tracking
- Used by GameLogic for movement validation
- Used by GUI for rendering tile contents
- Central to pathfinding and collision detection

### **MANDATORY**: Mathematical Operations

**CRITICAL**: Position class provides:
- **Euclidean Distance**: For precise distance calculations
- **Manhattan Distance**: For grid-based movement
- **Adjacency Testing**: For interaction range checking
- **Offset Movement**: For direction-based positioning

### **MANDATORY**: Verification Steps

1. **CRITICAL**: Both classes compile successfully in utilities package
2. **CRITICAL**: Position immutability is enforced
3. **CRITICAL**: Tile state management works correctly
4. **CRITICAL**: Distance calculations are mathematically correct
5. **CRITICAL**: equals() and hashCode() implementations are proper
6. **CRITICAL**: toString() methods provide useful debugging information

### CRITICAL REQUIREMENT ###
**MANDATORY**: The Position and Tile utility classes must be implemented exactly as specified above. These classes form the foundation for all spatial operations and map representation in the Mini Rogue Demo.
```