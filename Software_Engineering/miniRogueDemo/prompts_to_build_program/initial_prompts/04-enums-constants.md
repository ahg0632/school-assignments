## 04-enums-constants.md

# CRITICAL REQUIREMENTS - Game Enums and Constants Implementation

### MANDATORY DIRECTIVE ###
You are an expert Java game developer. **CRITICAL**: Implement the game enumerations and constants exactly as specified below for the Mini Rogue Demo game.

## ENUM SPECIFICATIONS

### **CRITICAL**: Create GameState.java in enums package with this exact implementation:

```

package enums;

/**

* Enumeration defining all possible game states in Mini Rogue Demo.
* Used by GameLogic to manage game flow and transitions.
*/
public enum GameState {
MAIN_MENU("mainMenu"),
CLASS_SELECTION("classSelection"),
PLAYING("playing"),
BATTLE("battle"),
INVENTORY("inventory"),
PAUSED("paused"),
GAME_OVER("gameOver"),
VICTORY("victory");

private final String stateName;

GameState(String stateName) {
this.stateName = stateName;
}

public String get_state_name() {
return stateName;
}
}

```

### **CRITICAL**: Create CharacterClass.java in enums package with this exact implementation:

```

package enums;

/**

* Enumeration defining player character classes with their base statistics.
* Each class has unique HP, ATK, MP values and equipment preferences.
*/
public enum CharacterClass {
WARRIOR("Warrior", 100, 25, 20),
MAGE("Mage", 60, 15, 80),
ROGUE("Rogue", 80, 20, 40);

private final String className;
private final int baseHp;
private final int baseAtk;
private final int baseMp;

CharacterClass(String className, int baseHp, int baseAtk, int baseMp) {
this.className = className;
this.baseHp = baseHp;
this.baseAtk = baseAtk;
this.baseMp = baseMp;
}

public String get_class_name() {
return className;
}

public int get_base_hp() {
return baseHp;
}

public int get_base_atk() {
return baseAtk;
}

public int get_base_mp() {
return baseMp;
}
}

```

### **CRITICAL**: Create TileType.java in enums package with this exact implementation:

```

package enums;

/**

* Enumeration defining all tile types in dungeon maps.
* Used by Map class for procedural generation and collision detection.
*/
public enum TileType {
WALL("wall", false),
FLOOR("floor", true),
DOOR("door", true),
STAIRS("stairs", true),
ENTRANCE("entrance", true),
BOSS_ROOM("bossRoom", true);

private final String typeName;
private final boolean walkable;

TileType(String typeName, boolean walkable) {
this.typeName = typeName;
this.walkable = walkable;
}

public String get_type_name() {
return typeName;
}

public boolean is_walkable() {
return walkable;
}
}

```

### **CRITICAL**: Create Direction.java in enums package with this exact implementation:

```

package enums;

/**

* Enumeration defining movement directions for character positioning.
* Used for player movement, enemy AI, and battle positioning.
*/
public enum Direction {
UP("up", 0, -1),
DOWN("down", 0, 1),
LEFT("left", -1, 0),
RIGHT("right", 1, 0);

private final String directionName;
private final int deltaX;
private final int deltaY;

Direction(String directionName, int deltaX, int deltaY) {
this.directionName = directionName;
this.deltaX = deltaX;
this.deltaY = deltaY;
}

public String get_direction_name() {
return directionName;
}

public int get_delta_x() {
return deltaX;
}

public int get_delta_y() {
return deltaY;
}
}

```

### **CRITICAL**: Create GameConstants.java in enums package with this exact implementation:

```

package enums;

/**

* Static constants used throughout the Mini Rogue Demo game.
* Centralizes configuration values for easy modification.
*/
public final class GameConstants {

// MANDATORY: Map Configuration
public static final int MAP_WIDTH = 50;
public static final int MAP_HEIGHT = 30;
public static final int ROOM_MIN_SIZE = 4;
public static final int ROOM_MAX_SIZE = 10;
public static final int MAX_ROOMS = 15;

// MANDATORY: Game Balance
public static final int STARTING_LEVEL = 1;
public static final int MAX_LEVEL = 20;
public static final int BASE_EXPERIENCE = 100;
public static final int EXPERIENCE_MULTIPLIER = 150;

// MANDATORY: Combat Constants
public static final int CRITICAL_HIT_CHANCE = 10; // 10%
public static final double CRITICAL_HIT_MULTIPLIER = 1.5;
public static final int MISS_CHANCE = 5; // 5%

// MANDATORY: UI Constants
public static final int WINDOW_WIDTH = 1024;
public static final int WINDOW_HEIGHT = 768;
public static final int TILE_SIZE = 16;
public static final int UI_PANEL_HEIGHT = 200;

// MANDATORY: Thread Configuration
public static final int ENEMY_AI_DELAY = 500; // milliseconds
public static final int GAME_UPDATE_DELAY = 16; // ~60 FPS

// MANDATORY: Inventory Configuration
public static final int UNLIMITED_INVENTORY = -1;
public static final int MAX_EQUIPMENT_LEVEL = 5;

private GameConstants() {
// Prevent instantiation
throw new UnsupportedOperationException("Utility class cannot be instantiated");
}
}

```

### **MANDATORY**: Enumeration Requirements

1. **CRITICAL**: All enums must be in the `enums` package
2. **CRITICAL**: Method names must use snake_case convention
3. **CRITICAL**: Complete JavaDoc documentation for all enums and methods
4. **CRITICAL**: Immutable design with private final fields
5. **CRITICAL**: Meaningful default values for game balance

### **MANDATORY**: Design Principles

**CRITICAL**: These enums enforce:
- **Type Safety**: Prevent invalid state assignments
- **Immutability**: Enum values cannot be modified at runtime
- **Centralized Configuration**: Constants gathered in single location
- **Extensibility**: Easy to add new character classes, states, etc.

### **MANDATORY**: Usage Context

**CRITICAL**: These enums will be:
- Used by GameLogic for state management
- Used by Character classes for type checking
- Used by Map generation for tile placement
- Used by GUI components for display logic
- Referenced throughout the game architecture

### **MANDATORY**: Verification Steps

1. **CRITICAL**: All enums compile successfully in enums package
2. **CRITICAL**: Enum values have appropriate default statistics
3. **CRITICAL**: Constants provide reasonable game balance values
4. **CRITICAL**: Method naming follows snake_case convention
5. **CRITICAL**: JavaDoc documentation is complete
6. **CRITICAL**: No compilation warnings

### CRITICAL REQUIREMENT ###
**MANDATORY**: The game enums and constants must be implemented exactly as specified above. These provide the foundation for type-safe game state management and consistent configuration throughout the Mini Rogue Demo.
```