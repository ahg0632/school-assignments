## **00-prompt-summary.md**

```markdown
# Cursor IDE Agent Prompt Collection - Mini Rogue Demo Project

## **CRITICAL SUMMARY**: Complete Prompt Pack for Mini Rogue Demo Game Creation

### **MANDATORY**: 15 Sequential Prompts Created

This prompt pack contains **15 detailed prompts** designed to guide Cursor IDE Agent through the complete recreation of the Mini Rogue Demo game application. Each prompt follows the required formatting techniques with **Sandwich Method**, **Attention Anchoring**, **Visual Emphasis**, and **Clear Delimiters**.

### **PROMPT FILES CREATED**:

1. **01-project-setup.md** - Initial project structure and Git repository setup
2. **02-gradle-dependencies.md** - Gradle configuration with Java 17 and dependencies
3. **03-core-interfaces.md** - Core MVC interfaces for game architecture
4. **04-enums-constants.md** - Game enums and constant definitions
5. **05-position-tile-classes.md** - Position and Tile utility classes
6. **06-character-hierarchy.md** - Abstract Character class and basic structure
7. **07-player-implementation.md** - Player class with inventory and stats
8. **08-enemy-boss-classes.md** - Enemy and Boss implementations
9. **09-item-equipment-system.md** - Item hierarchy and equipment system
10. **10-map-generation.md** - Map class with procedural generation
11. **11-game-logic-core.md** - GameLogic class for game state management
12. **12-battle-logic-system.md** - BattleLogic class for combat mechanics
13. **13-gui-view-implementation.md** - Swing GUI implementation with MVC
14. **14-main-controller-integration.md** - Main class and controller integration
15. **15-complete-testing-guide.md** - Complete testing and verification guide

### **USAGE INSTRUCTIONS**:

**CRITICAL**: Deliver prompts to Cursor IDE Agent in **exact numerical order**. Each prompt builds upon previous implementations and maintains dependency relationships.

### **EXPECTED OUTCOMES**:

- **Complete MVC Game Architecture**: Separation of concerns with Model-View-Controller
- **Roguelike Game Features**: Procedural generation, permadeath, character classes
- **Java Swing GUI**: 2D pixel art style interface with game panels
- **Multithreading**: Enemy AI and game loop performance optimization
- **Gradle Build System**: Proper dependency management and executable JAR
- **Git Repository**: Version control with proper project structure

### **VERIFICATION CHECKLIST**:

✓ Project compiles without errors
✓ All game features implement according to SRS specifications
✓ MVC pattern properly implemented with clear separation
✓ GUI displays with pixel art style rendering
✓ Procedural generation creates unique dungeons
✓ Combat system works with class-specific abilities
✓ Multithreading handles enemy AI effectively
✓ Application runs as standalone executable JAR

This prompt collection provides complete guidance for creating the Mini Rogue Demo game with proper architecture, game mechanics, and functionality as specified in the SRS requirements.
```


## **README-project-overview.md**

```markdown
# Mini Rogue Demo - Complete Java Game Development Project

## **PROJECT OVERVIEW**

This repository contains **15 comprehensive prompts** designed to guide Cursor IDE Agent through the complete creation of a roguelike dungeon crawler game implemented in Java using MVC architecture, Swing GUI, and multithreading.

## **GAME SPECIFICATIONS**

The Mini Rogue Demo is a **2D pixel art roguelike dungeon crawler** featuring procedural generation, permadeath mechanics, and class-based combat system. The game demonstrates classical **Model-View-Controller architecture** with **observer pattern** implementation using **Java Swing** for the GUI.

## **PROMPT EXECUTION PHASES**

### **Phase 1: Foundation Setup (Prompts 1-2)**
- **01-project-setup.md** - Creates project structure, Git repository, and folder organization
- **02-gradle-dependencies.md** - Configures Gradle build with Java 17, Swing, and testing dependencies

### **Phase 2: Core Architecture (Prompts 3-5)**
- **03-core-interfaces.md** - Defines MVC interfaces and contracts for game architecture
- **04-enums-constants.md** - Implements game enumerations and constant definitions
- **05-position-tile-classes.md** - Creates utility classes for map positioning and tile management

### **Phase 3: Character System (Prompts 6-8)**
- **06-character-hierarchy.md** - Implements abstract Character class with stats and abilities
- **07-player-implementation.md** - Creates Player class with inventory, leveling, and class selection
- **08-enemy-boss-classes.md** - Implements Enemy and Boss classes with AI behavior patterns

### **Phase 4: Game Systems (Prompts 9-12)**
- **09-item-equipment-system.md** - Implements Item hierarchy, equipment, and upgrade mechanics
- **10-map-generation.md** - Creates Map class with procedural dungeon generation algorithms
- **11-game-logic-core.md** - Implements GameLogic for state management and game flow
- **12-battle-logic-system.md** - Creates BattleLogic for combat mechanics and multithreading

### **Phase 5: User Interface & Integration (Prompts 13-15)**
- **13-gui-view-implementation.md** - Implements Swing GUI with MVC pattern integration
- **14-main-controller-integration.md** - Creates Main class and controller coordination
- **15-complete-testing-guide.md** - Provides testing procedures and verification steps

## **TECHNICAL ARCHITECTURE**

### **MVC Pattern Implementation**
- **Model**: Game state, character data, map information, and business logic
- **View**: Swing GUI panels for game display, menus, and user interaction
- **Controller**: Input handling, game flow control, and MVC component coordination

### **Design Patterns Utilized**
- **Observer Pattern**: Model notifications to Views for real-time updates
- **Strategy Pattern**: Character class abilities and AI behavior patterns
- **Factory Pattern**: Procedural generation of maps, enemies, and items
- **State Pattern**: Game state management and battle system states
- **Command Pattern**: Player action processing and input handling

### **Core Game Features**
- **Character Classes**: Warrior, Mage, Rogue with unique stats and abilities
- **Procedural Generation**: Random dungeon layouts, enemy placement, and item distribution
- **Combat System**: Real-time battles with class-specific attacks and weaknesses
- **Inventory Management**: Unlimited storage with equipment upgrade mechanics
- **Leveling System**: Experience gain and stat progression
- **Boss Battles**: Floor-ending encounters with special abilities
- **Permadeath**: Game restart on player defeat with new procedural content

### **Technical Implementation**
- **Java 17**: Modern Java features with enhanced performance
- **Swing GUI**: 2D pixel art rendering with custom graphics
- **Multithreading**: Enemy AI behavior and game loop optimization
- **Gradle Build**: Dependency management and executable JAR packaging
- **Git Integration**: Version control with proper project organization

## **GAME MECHANICS**

### **Character System**
- **Warrior**: High HP, strong melee attacks, heavy armor specialization
- **Mage**: High MP, ranged magic attacks, spell-based combat
- **Rogue**: High speed, evasion abilities, stealth and critical strikes

### **Dungeon Exploration**
- **Procedural Maps**: Unique layouts generated for each playthrough
- **Interactive Elements**: Items, enemies, NPCs, and environmental features
- **Progressive Difficulty**: Increasing challenge through dungeon floors

### **Combat Mechanics**
- **Real-time Battles**: Active combat with timing and positioning
- **Class Abilities**: Unique attacks and defensive maneuvers per character
- **Enemy Weaknesses**: Strategic combat with type advantages
- **Equipment Effects**: Weapon and armor impact on battle outcomes

### **Progression Systems**
- **Experience Points**: Combat victories provide character advancement
- **Equipment Upgrades**: Item combination system for enhanced gear
- **Stat Increases**: HP, ATK, MP progression through leveling

## **DEVELOPMENT STANDARDS**

### **Code Organization**
- **CamelCase**: Classes, objects, variables, and file naming conventions
- **snake_case**: Function and method naming standards
- **Package Structure**: Clear separation of MVC components and game systems
- **Documentation**: Comprehensive JavaDoc for all classes and methods

### **Quality Assurance**
- **Compilation**: Error-free building with Gradle
- **Testing**: Functional verification at each development phase
- **Performance**: 30+ FPS gameplay with responsive UI
- **Distribution**: Standalone executable JAR for deployment

This project demonstrates professional game development practices while creating an engaging roguelike experience with modern software architecture principles.
```

Now, here are the individual prompt files:

## **01-project-setup.md**

```markdown
# CRITICAL REQUIREMENTS - Mini Rogue Demo Project Setup

### MANDATORY DIRECTIVE ###
You are an expert Java game developer. **CRITICAL**: Create a complete Mini Rogue Demo project structure in Cursor IDE with the exact specifications below for a roguelike dungeon crawler game.

## PROJECT STRUCTURE REQUIREMENTS

### **CRITICAL**: Create the following directory structure exactly:

```

miniRogueDemo/
├── .gitignore
├── README.md
├── build.gradle
├── prompts/
│   └── (prompt files will be stored here)
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── model/
│   │   │   │   ├── characters/
│   │   │   │   ├── items/
│   │   │   │   ├── equipment/
│   │   │   │   ├── map/
│   │   │   │   └── gameLogic/
│   │   │   ├── view/
│   │   │   │   ├── panels/
│   │   │   │   └── components/
│   │   │   ├── controller/
│   │   │   ├── interfaces/
│   │   │   ├── enums/
│   │   │   └── utilities/
│   │   └── resources/
│   │       ├── images/
│   │       │   ├── characters/
│   │       │   ├── items/
│   │       │   ├── tiles/
│   │       │   └── ui/
│   │       └── sounds/
│   └── test/
│       └── java/
│           ├── model/
│           ├── view/
│           └── controller/

```

### **MANDATORY**: Git Repository Setup

**CRITICAL**: Create `.gitignore` file with these exact contents:
```


# Build files

build/
.gradle/
gradle/
gradlew
gradlew.bat

# IDE files

.idea/
*.iml
.vscode/
.settings/
.project
.classpath

# OS files

.DS_Store
Thumbs.db

# Java files

*.class
*.jar
*.war
*.ear
*.logs
*.log

# Backup files

*.bak
*.tmp
*~

```

### **MANDATORY**: Initial README.md

**CRITICAL**: Create `README.md` with project description:
```


# Mini Rogue Demo

A 2D pixel art roguelike dungeon crawler game written in Java using MVC architecture.

## Features

- Procedural dungeon generation
- Character classes (Warrior, Mage, Rogue)
- Real-time combat system
- Inventory and equipment upgrade system
- Boss battles and permadeath mechanics


## Requirements

- Java 17+
- Gradle 7.0+


## Build and Run

```bash
gradle build
gradle run
```


## Architecture

- MVC (Model-View-Controller) pattern
- Java Swing GUI
- Multithreaded enemy AI
- 2D pixel art graphics

```

### **MANDATORY**: Naming Convention Requirements

1. **CRITICAL**: All folder names must use camelCase
2. **CRITICAL**: All package names must use camelCase
3. **CRITICAL**: All class names must use CamelCase
4. **CRITICAL**: All variable and object names must use camelCase
5. **CRITICAL**: All function names must use snake_case

### **MANDATORY**: Project Configuration

**CRITICAL**: Prepare for Gradle configuration with:
- Java version: 17
- Project name: "miniRogueDemo"
- Group: "com.rogueDemo"
- Version: "1.0.0"

### **MANDATORY**: Verification Steps

1. **CRITICAL**: Verify all directories exist as specified
2. **CRITICAL**: Verify .gitignore contains all required entries
3. **CRITICAL**: Verify README.md is created with project information
4. **CRITICAL**: Verify project structure matches diagram exactly
5. **CRITICAL**: Verify naming conventions are applied consistently

### CRITICAL REQUIREMENT ###
**MANDATORY**: Complete this setup phase before proceeding to any implementation. The project structure must be exactly as specified above to support the roguelike game architecture.
```


## **02-gradle-dependencies.md**

```markdown
# CRITICAL REQUIREMENTS - Gradle Dependencies Configuration

### MANDATORY DIRECTIVE ###
You are an expert Java game developer. **CRITICAL**: Configure Gradle build file for the Mini Rogue Demo project with exact dependencies and configurations specified below.

## BUILD.GRADLE CONFIGURATION

### **CRITICAL**: Create the following build.gradle file exactly:

```

plugins {
id 'java'
id 'application'
}

group = 'com.rogueDemo'
version = '1.0.0'

java {
sourceCompatibility = JavaVersion.VERSION_17
targetCompatibility = JavaVersion.VERSION_17
}

repositories {
mavenCentral()
}

dependencies {
// JUnit 5 for testing
testImplementation 'org.junit.jupiter:junit-jupiter-api:5.9.2'
testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.9.2'
testImplementation 'org.junit.jupiter:junit-jupiter-params:5.9.2'

    // Mockito for testing
    testImplementation 'org.mockito:mockito-core:5.1.1'
    testImplementation 'org.mockito:mockito-junit-jupiter:5.1.1'
    
    // Swing is part of JDK, no external dependency needed
    // java.awt and javax.swing are included in JDK
    }

application {
mainClass = 'controller.Main'
}

test {
useJUnitPlatform()
}

// Task to create executable JAR
jar {
archiveBaseName = 'miniRogueDemo'
archiveVersion = '1.0.0'

    manifest {
        attributes(
            'Main-Class': 'controller.Main',
            'Implementation-Title': 'Mini Rogue Demo',
            'Implementation-Version': project.version
        )
    }
    
    // Include all dependencies in JAR
    from {
        configurations.runtimeClasspath.collect { it.isDirectory() ? it : zipTree(it) }
    }
    }

// Task to run the game
task runGame(type: JavaExec) {
group = 'application'
description = 'Run the Mini Rogue Demo game'
classpath = sourceSets.main.runtimeClasspath
mainClass = 'controller.Main'

    // JVM arguments for better performance
    jvmArgs = [
        '-Xmx512m',
        '-Xms256m',
        '-Djava.awt.headless=false'
    ]
    }

// Compiler options
compileJava {
options.encoding = 'UTF-8'
options.compilerArgs += ['-Xlint:unchecked', '-Xlint:deprecation']
}

compileTestJava {
options.encoding = 'UTF-8'
}

```

### **MANDATORY**: Dependency Requirements

1. **CRITICAL**: Java 17 with Swing support (built-in)
2. **CRITICAL**: JUnit 5 for comprehensive testing framework
3. **CRITICAL**: Mockito for mock object testing
4. **CRITICAL**: No external game libraries (use java.awt and javax.swing)
5. **CRITICAL**: Maven Central repository for dependency resolution

### **MANDATORY**: Build Configuration

**CRITICAL**: The build must support:
- Java compilation with UTF-8 encoding
- JUnit 5 test execution with parameterized tests
- Application running via `gradle run` or `gradle runGame`
- Executable JAR creation with all dependencies
- Main class execution: `controller.Main`

### **MANDATORY**: Package Dependencies

**CRITICAL**: Ensure these package relationships:
- `model` package contains game data and logic
- `view` package contains GUI components
- `controller` package contains main class and input handling
- `interfaces` package defines contracts
- `enums` and `utilities` provide support classes

### **MANDATORY**: Performance Configuration

**CRITICAL**: JVM settings:
- Maximum heap size: 512MB
- Initial heap size: 256MB
- Headless mode disabled for GUI
- Compiler warnings enabled for code quality

### **MANDATORY**: Verification Steps

1. **CRITICAL**: Run `gradle clean build` - must succeed
2. **CRITICAL**: Run `gradle test` - must execute (no tests yet)
3. **CRITICAL**: Verify Java 17 compatibility
4. **CRITICAL**: Verify JUnit 5 is available for testing
5. **CRITICAL**: Verify application plugin configuration
6. **CRITICAL**: Test `gradle runGame` task creation

### **MANDATORY**: Testing Setup

**CRITICAL**: Create `src/test/java/` directory structure:
```

src/test/java/
├── model/
│   ├── characters/
│   ├── items/
│   └── gameLogic/
├── view/
└── controller/

```

### CRITICAL REQUIREMENT ###
**MANDATORY**: The Gradle configuration must compile successfully and support the roguelike game architecture with proper dependencies and build tasks as specified above.
```


## **03-core-interfaces.md**

```markdown
# CRITICAL REQUIREMENTS - Core MVC Interfaces Implementation

### MANDATORY DIRECTIVE ###
You are an expert Java game developer. **CRITICAL**: Implement the core MVC interfaces exactly as specified below for the Mini Rogue Demo game architecture.

## INTERFACE SPECIFICATIONS

### **CRITICAL**: Create GameModel.java in interfaces package with this exact implementation:

```

package interfaces;

/**

* Core interface for all game model components in the Mini Rogue Demo.
* Defines the contract for game state management and observer notifications.
*/
public interface GameModel {
/**
    * MANDATORY: Method called by model implementations to notify
    * interested observers of state changes.
    * 
    * @param event String describing the change event
    * @param data Object containing event-specific data
*/
void notify_observers(String event, Object data);

/**
    * MANDATORY: Add an observer to receive model change notifications
    * 
    * @param observer The GameObserver to add
*/
void add_observer(GameObserver observer);

/**
    * MANDATORY: Remove an observer from notifications
    * 
    * @param observer The GameObserver to remove
*/
void remove_observer(GameObserver observer);
}

```

### **CRITICAL**: Create GameView.java in interfaces package with this exact implementation:

```

package interfaces;

/**

* Core interface for all view components in the Mini Rogue Demo.
* Defines the contract for UI components that display game state.
*/
public interface GameView {
/**
    * MANDATORY: Update the view display based on model changes
*/
void update_display();

/**
    * MANDATORY: Get the controller managing this view
    * 
    * @return The GameController instance
*/
GameController get_controller();

/**
    * MANDATORY: Set the controller for this view
    * 
    * @param controller The GameController to set
*/
void set_controller(GameController controller);

/**
    * MANDATORY: Initialize view components
*/
void initialize_components();
}

```

### **CRITICAL**: Create GameController.java in interfaces package with this exact implementation:

```

package interfaces;

/**

* Core interface for all controller components in the Mini Rogue Demo.
* Defines the contract for handling user input and coordinating MVC components.
*/
public interface GameController {
/**
    * MANDATORY: Handle user input events
    * 
    * @param input String representing the user input
*/
void handle_input(String input);

/**
    * MANDATORY: Get the model this controller manages
    * 
    * @return The GameModel instance
*/
GameModel get_model();

/**
    * MANDATORY: Set the model for this controller
    * 
    * @param model The GameModel to manage
*/
void set_model(GameModel model);

/**
    * MANDATORY: Get the view this controller manages
    * 
    * @return The GameView instance
*/
GameView get_view();

/**
    * MANDATORY: Set the view for this controller
    * 
    * @param view The GameView to manage
*/
void set_view(GameView view);
}

```

### **CRITICAL**: Create GameObserver.java in interfaces package with this exact implementation:

```

package interfaces;

/**

* Observer interface for receiving notifications about game model changes.
* Implements the Observer pattern for loose coupling between model and view.
*/
public interface GameObserver {
/**
    * MANDATORY: Method called when observed model changes
    * 
    * @param event String describing what changed
    * @param data Object containing change-specific information
*/
void on_model_changed(String event, Object data);
}

```

### **MANDATORY**: Interface Design Principles

1. **CRITICAL**: All interfaces must be in the `interfaces` package
2. **CRITICAL**: Method names must use snake_case convention
3. **CRITICAL**: Complete JavaDoc documentation for all methods
4. **CRITICAL**: Observer pattern implementation for MVC communication
5. **CRITICAL**: Loose coupling between MVC components

### **MANDATORY**: Design Patterns Implemented

**CRITICAL**: These interfaces enforce:
- **Observer Pattern**: Models notify observers of state changes
- **MVC Pattern**: Clear separation of Model, View, Controller responsibilities
- **Strategy Pattern**: Interface-based polymorphism for different implementations
- **Command Pattern**: Input handling through controller interface

### **MANDATORY**: Usage Context

**CRITICAL**: These interfaces will be:
- Implemented by concrete model classes (GameLogic, BattleLogic)
- Implemented by view classes for GUI panels
- Implemented by controller classes for input handling
- Used throughout the game architecture for loose coupling

### **MANDATORY**: Verification Steps

1. **CRITICAL**: All interfaces compile successfully in interfaces package
2. **CRITICAL**: Method signatures match exactly as specified
3. **CRITICAL**: JavaDoc documentation is complete and error-free
4. **CRITICAL**: Interfaces are public and accessible
5. **CRITICAL**: No compilation warnings
6. **CRITICAL**: Observer pattern contracts are properly defined

### CRITICAL REQUIREMENT ###
**MANDATORY**: The core interfaces must be implemented exactly as specified above. These interfaces form the foundation of the MVC architecture for the Mini Rogue Demo and must be correct before proceeding to concrete implementations.
```


## **04-enums-constants.md**

```markdown
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


## **05-position-tile-classes.md**

```markdown
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


## **06-character-hierarchy.md**

```markdown
# CRITICAL REQUIREMENTS - Character Hierarchy Implementation

### MANDATORY DIRECTIVE ###
You are an expert Java game developer. **CRITICAL**: Implement the Character hierarchy classes exactly as specified below for the Mini Rogue Demo character system.

## CHARACTER HIERARCHY SPECIFICATIONS

### **CRITICAL**: Create Character.java in model/characters package with this exact implementation:

```

package model.characters;

import interfaces.GameModel;
import interfaces.GameObserver;
import enums.CharacterClass;
import utilities.Position;
import model.equipment.Weapon;
import model.equipment.Armor;
import model.items.Item;
import java.util.ArrayList;
import java.util.List;

/**

* Abstract base class for all characters in the Mini Rogue Demo.
* Provides common attributes and behavior for Player, Enemy, and Boss classes.
*/
public abstract class Character implements GameModel {

// MANDATORY: Core character attributes
protected String name;
protected CharacterClass characterClass;
protected int level;
protected int experience;
protected Position position;

// MANDATORY: Combat statistics
protected int currentHp;
protected int maxHp;
protected int baseAtk;
protected int currentMp;
protected int maxMp;

// MANDATORY: Equipment
protected Weapon equippedWeapon;
protected Armor equippedArmor;

// MANDATORY: Observer pattern implementation
protected List<GameObserver> observers;

/**
    * MANDATORY: Constructor for Character
    * 
    * @param name The character's name
    * @param characterClass The character's class
    * @param position Initial position
*/
public Character(String name, CharacterClass characterClass, Position position) {
this.name = name;
this.characterClass = characterClass;
this.position = position;
this.level = 1;
this.experience = 0;
this.observers = new ArrayList<>();

// Initialize stats based on class
initialize_stats();
}

/**
    * MANDATORY: Initialize character statistics based on class
*/
protected void initialize_stats() {
this.maxHp = characterClass.get_base_hp();
this.currentHp = maxHp;
this.baseAtk = characterClass.get_base_atk();
this.maxMp = characterClass.get_base_mp();
this.currentMp = maxMp;
}

/**
    * MANDATORY: Move character to new position
    * 
    * @param newPosition The target position
*/
public void move_to(Position newPosition) {
Position oldPosition = this.position;
this.position = newPosition;
notify_observers("CHARACTER_MOVED", this);
}

/**
    * MANDATORY: Take damage and update HP
    * 
    * @param damage Amount of damage to take
    * @return true if character is still alive
*/
public boolean take_damage(int damage) {
int actualDamage = Math.max(0, damage - get_total_defense());
this.currentHp = Math.max(0, this.currentHp - actualDamage);
notify_observers("HP_CHANGED", this.currentHp);

if (currentHp <= 0) {
notify_observers("CHARACTER_DEFEATED", this);
return false;
}
return true;
}

/**
    * MANDATORY: Heal character HP
    * 
    * @param healAmount Amount to heal
*/
public void heal(int healAmount) {
this.currentHp = Math.min(maxHp, this.currentHp + healAmount);
notify_observers("HP_CHANGED", this.currentHp);
}

/**
    * MANDATORY: Use MP for abilities
    * 
    * @param mpCost Amount of MP to consume
    * @return true if MP was available and consumed
*/
public boolean use_mp(int mpCost) {
if (this.currentMp >= mpCost) {
this.currentMp -= mpCost;
notify_observers("MP_CHANGED", this.currentMp);
return true;
}
return false;
}

/**
    * MANDATORY: Restore MP
    * 
    * @param mpAmount Amount of MP to restore
*/
public void restore_mp(int mpAmount) {
this.currentMp = Math.min(maxMp, this.currentMp + mpAmount);
notify_observers("MP_CHANGED", this.currentMp);
}

/**
    * MANDATORY: Calculate total attack power including equipment
    * 
    * @return Total attack value
*/
public int get_total_attack() {
int weaponAttack = (equippedWeapon != null) ? equippedWeapon.get_attack_power() : 0;
return baseAtk + weaponAttack;
}

/**
    * MANDATORY: Calculate total defense including equipment
    * 
    * @return Total defense value
*/
public int get_total_defense() {
int armorDefense = (equippedArmor != null) ? equippedArmor.get_defense_value() : 0;
return armorDefense;
}

/**
    * MANDATORY: Check if character is alive
    * 
    * @return true if HP > 0
*/
public boolean is_alive() {
return currentHp > 0;
}

/**
    * MANDATORY: Abstract method for character-specific attack behavior
    * 
    * @param target The character to attack
    * @return Damage dealt
*/
public abstract int attack(Character target);

/**
    * MANDATORY: Abstract method for character-specific AI or input handling
*/
public abstract void perform_turn();

// MANDATORY: Observer pattern implementation
@Override
public void notify_observers(String event, Object data) {
for (GameObserver observer : observers) {
observer.on_model_changed(event, data);
}
}

@Override
public void add_observer(GameObserver observer) {
if (observer != null \&\& !observers.contains(observer)) {
observers.add(observer);
}
}

@Override
public void remove_observer(GameObserver observer) {
observers.remove(observer);
}

// MANDATORY: Getters and setters
public String get_name() { return name; }
public CharacterClass get_character_class() { return characterClass; }
public int get_level() { return level; }
public int get_experience() { return experience; }
public Position get_position() { return position; }
public int get_current_hp() { return currentHp; }
public int get_max_hp() { return maxHp; }
public int get_base_atk() { return baseAtk; }
public int get_current_mp() { return currentMp; }
public int get_max_mp() { return maxMp; }
public Weapon get_equipped_weapon() { return equippedWeapon; }
public Armor get_equipped_armor() { return equippedArmor; }

public void set_equipped_weapon(Weapon weapon) {
this.equippedWeapon = weapon;
notify_observers("WEAPON_EQUIPPED", weapon);
}

public void set_equipped_armor(Armor armor) {
this.equippedArmor = armor;
notify_observers("ARMOR_EQUIPPED", armor);
}
}

```

### **CRITICAL**: Create Stats.java in model/characters package with this exact implementation:

```

package model.characters;

/**

* Utility class for managing character statistics and calculations.
* Provides methods for stat modifications and level-based scaling.
*/
public final class Stats {

/**
    * MANDATORY: Calculate HP gain per level for character class
    * 
    * @param characterClass The character's class
    * @param level Current level
    * @return HP increase for this level
*/
public static int calculate_hp_per_level(enums.CharacterClass characterClass, int level) {
int baseGain;
switch (characterClass) {
case WARRIOR:
baseGain = 12;
break;
case MAGE:
baseGain = 6;
break;
case ROGUE:
baseGain = 8;
break;
default:
baseGain = 8;
}
return baseGain + (level / 5); // Slight scaling with level
}

/**
    * MANDATORY: Calculate ATK gain per level for character class
    * 
    * @param characterClass The character's class
    * @param level Current level
    * @return ATK increase for this level
*/
public static int calculate_atk_per_level(enums.CharacterClass characterClass, int level) {
int baseGain;
switch (characterClass) {
case WARRIOR:
baseGain = 3;
break;
case MAGE:
baseGain = 2;
break;
case ROGUE:
baseGain = 2;
break;
default:
baseGain = 2;
}
return baseGain + (level / 10); // Slight scaling with level
}

/**
    * MANDATORY: Calculate MP gain per level for character class
    * 
    * @param characterClass The character's class
    * @param level Current level
    * @return MP increase for this level
*/
public static int calculate_mp_per_level(enums.CharacterClass characterClass, int level) {
int baseGain;
switch (characterClass) {
case WARRIOR:
baseGain = 2;
break;
case MAGE:
baseGain = 8;
break;
case ROGUE:
baseGain = 4;
break;
default:
baseGain = 4;
}
return baseGain + (level / 5); // Slight scaling with level
}

/**
    * MANDATORY: Calculate experience required for next level
    * 
    * @param currentLevel The character's current level
    * @return Experience points needed for next level
*/
public static int calculate_experience_required(int currentLevel) {
return enums.GameConstants.BASE_EXPERIENCE +
(currentLevel * enums.GameConstants.EXPERIENCE_MULTIPLIER);
}

/**
    * MANDATORY: Calculate critical hit chance based on character class
    * 
    * @param characterClass The character's class
    * @return Critical hit chance as percentage
*/
public static int calculate_critical_chance(enums.CharacterClass characterClass) {
switch (characterClass) {
case WARRIOR:
return 8; // 8%
case MAGE:
return 12; // 12% (spell crits)
case ROGUE:
return 20; // 20% (high crit)
default:
return enums.GameConstants.CRITICAL_HIT_CHANCE;
}
}

private Stats() {
// Prevent instantiation
throw new UnsupportedOperationException("Utility class cannot be instantiated");
}
}

```

### **MANDATORY**: Class Design Requirements

1. **CRITICAL**: Character class must be abstract with concrete implementations
2. **CRITICAL**: Stats class must be final utility class with static methods
3. **CRITICAL**: Observer pattern implementation for stat changes
4. **CRITICAL**: Equipment integration with attack/defense calculations
5. **CRITICAL**: Level-based stat scaling and progression

### **MANDATORY**: Design Principles

**CRITICAL**: These classes implement:
- **Template Method Pattern**: Abstract Character with specific implementations
- **Observer Pattern**: Notifications for stat changes
- **Strategy Pattern**: Class-specific stat calculations
- **State Pattern**: Character state management (HP, MP, equipment)

### **MANDATORY**: Combat Integration

**CRITICAL**: Combat features:
- **Damage Calculation**: Attack minus defense mechanics
- **Critical Hits**: Class-based critical hit chances
- **MP Usage**: Ability cost management
- **Equipment Bonuses**: Weapon and armor stat modifications

### **MANDATORY**: Verification Steps

1. **CRITICAL**: Character class compiles in model/characters package
2. **CRITICAL**: Stats utility class provides correct calculations
3. **CRITICAL**: Observer pattern notifications work properly
4. **CRITICAL**: Equipment integration affects combat stats
5. **CRITICAL**: Level progression calculations are balanced
6. **CRITICAL**: Abstract methods enforce subclass implementation

### CRITICAL REQUIREMENT ###
**MANDATORY**: The Character hierarchy must be implemented exactly as specified above. This provides the foundation for all character types in the Mini Rogue Demo with proper stat management and combat integration.
```

Continue with the remaining prompts in the next message due to length constraints...

## **07-player-implementation.md**

```markdown
# CRITICAL REQUIREMENTS - Player Class Implementation

### MANDATORY DIRECTIVE ###
You are an expert Java game developer. **CRITICAL**: Implement the Player class exactly as specified below for the Mini Rogue Demo player character system.

## PLAYER CLASS SPECIFICATION

### **CRITICAL**: Create Player.java in model/characters package with this exact implementation:

```

package model.characters;

import enums.CharacterClass;
import enums.GameConstants;
import utilities.Position;
import model.items.Item;
import model.items.Consumable;
import model.equipment.Weapon;
import model.equipment.Armor;
import model.equipment.Equipment;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**

* Player character class extending Character.
* Handles player-specific functionality including inventory, leveling, and class selection.
*/
public class Player extends Character {

// MANDATORY: Player-specific attributes
private List<Item> inventory;
private int inventoryCapacity;
private String selectedClass;
private Random random;

/**
    * MANDATORY: Constructor for Player
    * 
    * @param name Player's chosen name
    * @param characterClass Selected character class
    * @param startPosition Initial spawn position
*/
public Player(String name, CharacterClass characterClass, Position startPosition) {
super(name, characterClass, startPosition);
this.inventory = new ArrayList<>();
this.inventoryCapacity = GameConstants.UNLIMITED_INVENTORY;
this.selectedClass = characterClass.get_class_name();
this.random = new Random();

// Initialize class-specific starting equipment
initialize_starting_equipment();
}

/**
    * MANDATORY: Initialize starting equipment based on character class
*/
private void initialize_starting_equipment() {
switch (characterClass) {
case WARRIOR:
// Warrior starts with basic sword and leather armor
this.equippedWeapon = new Weapon("Iron Sword", "A sturdy iron blade", 15, 0, characterClass);
this.equippedArmor = new Armor("Leather Armor", "Basic protective gear", 0, 5, 2, characterClass);
break;
case MAGE:
// Mage starts with staff and robes
this.equippedWeapon = new Weapon("Oak Staff", "A magical focusing staff", 8, 10, characterClass);
this.equippedArmor = new Armor("Cloth Robes", "Light magical robes", 0, 2, 8, characterClass);
break;
case ROGUE:
// Rogue starts with dagger and light armor
this.equippedWeapon = new Weapon("Steel Dagger", "A sharp, lightweight blade", 12, 5, characterClass);
this.equippedArmor = new Armor("Studded Leather", "Flexible protective wear", 0, 4, 3, characterClass);
break;
}

// Add starting consumables
add_starting_items();
}

/**
    * MANDATORY: Add starting items to inventory
*/
private void add_starting_items() {
// Add health potions
collect_item(new Consumable("Health Potion", "Restores 50 HP", 50, "health"));
collect_item(new Consumable("Health Potion", "Restores 50 HP", 50, "health"));

// Add mana potions for magic users
if (characterClass == CharacterClass.MAGE || characterClass == CharacterClass.ROGUE) {
collect_item(new Consumable("Mana Potion", "Restores 30 MP", 30, "mana"));
}
}

/**
    * MANDATORY: Select character class (used during character creation)
    * 
    * @param classType The CharacterClass to select
*/
public void select_class(CharacterClass classType) {
this.characterClass = classType;
this.selectedClass = classType.get_class_name();
initialize_stats();
initialize_starting_equipment();
notify_observers("CLASS_SELECTED", classType);
}

/**
    * MANDATORY: Collect an item and add to inventory
    * 
    * @param item The Item to collect
    * @return true if item was successfully added
*/
public boolean collect_item(Item item) {
if (item == null) return false;

if (inventoryCapacity == GameConstants.UNLIMITED_INVENTORY ||
inventory.size() < inventoryCapacity) {
inventory.add(item);
notify_observers("ITEM_COLLECTED", item);
return true;
}
return false; // Inventory full
}

/**
    * MANDATORY: Use an item from inventory
    * 
    * @param item The Item to use
    * @return true if item was successfully used
*/
public boolean use_item(Item item) {
if (!inventory.contains(item)) return false;

boolean used = item.use(this);
if (used) {
inventory.remove(item);
notify_observers("ITEM_USED", item);
}
return used;
}

/**
    * MANDATORY: Open inventory interface
*/
public void open_inventory() {
notify_observers("INVENTORY_OPENED", inventory);
}

/**
    * MANDATORY: Upgrade equipment using key items
    * 
    * @param equipment The Equipment to upgrade
    * @return true if upgrade was successful
*/
public boolean upgrade_equipment(Equipment equipment) {
// Find required upgrade items in inventory
List<Item> upgradeItems = find_upgrade_items_for(equipment);

if (can_upgrade_equipment(equipment, upgradeItems)) {
// Remove upgrade items from inventory
for (Item item : upgradeItems) {
inventory.remove(item);
}

     // Perform upgrade
     boolean upgraded = equipment.upgrade();
     if (upgraded) {
         notify_observers("EQUIPMENT_UPGRADED", equipment);
         return true;
     }
    }
return false;
}

/**
    * MANDATORY: Check if equipment can be upgraded
    * 
    * @param equipment The Equipment to check
    * @param upgradeItems Available upgrade items
    * @return true if upgrade is possible
*/
private boolean can_upgrade_equipment(Equipment equipment, List<Item> upgradeItems) {
return !upgradeItems.isEmpty() \&\&
equipment.get_upgrade_level() < GameConstants.MAX_EQUIPMENT_LEVEL;
}

/**
    * MANDATORY: Find items that can upgrade specific equipment
    * 
    * @param equipment The Equipment to find upgrades for
    * @return List of applicable upgrade items
*/
private List<Item> find_upgrade_items_for(Equipment equipment) {
List<Item> upgradeItems = new ArrayList<>();
for (Item item : inventory) {
if (item.get_name().contains("Upgrade") \&\&
item.get_class_type().equals(equipment.get_class_type().get_class_name())) {
upgradeItems.add(item);
}
}
return upgradeItems;
}

/**
    * MANDATORY: Gain experience and handle leveling
    * 
    * @param expGained Amount of experience to gain
*/
public void gain_experience(int expGained) {
this.experience += expGained;
notify_observers("EXPERIENCE_GAINED", expGained);

// Check for level up
int requiredExp = Stats.calculate_experience_required(level);
if (experience >= requiredExp) {
level_up();
}
}

/**
    * MANDATORY: Handle level up progression
*/
private void level_up() {
if (level >= GameConstants.MAX_LEVEL) return;

level++;

// Increase stats based on class
int hpGain = Stats.calculate_hp_per_level(characterClass, level);
int atkGain = Stats.calculate_atk_per_level(characterClass, level);
int mpGain = Stats.calculate_mp_per_level(characterClass, level);

maxHp += hpGain;
currentHp = maxHp; // Full heal on level up
baseAtk += atkGain;
maxMp += mpGain;
currentMp = maxMp; // Full MP restore on level up

// Reset experience for next level
experience = 0;

notify_observers("LEVEL_UP", level);
}

/**
    * MANDATORY: Implement attack behavior for player
    * 
    * @param target The character to attack
    * @return Damage dealt
*/
@Override
public int attack(Character target) {
int damage = get_total_attack();

// Check for critical hit
int critChance = Stats.calculate_critical_chance(characterClass);
if (random.nextInt(100) < critChance) {
damage = (int)(damage * GameConstants.CRITICAL_HIT_MULTIPLIER);
notify_observers("CRITICAL_HIT", damage);
}

// Check for miss
if (random.nextInt(100) < GameConstants.MISS_CHANCE) {
damage = 0;
notify_observers("ATTACK_MISSED", target);
}

// Apply class-specific attack modifiers
damage = apply_class_attack_bonus(damage, target);

notify_observers("PLAYER_ATTACKED", target);
return damage;
}

/**
    * MANDATORY: Apply class-specific attack bonuses
    * 
    * @param baseDamage Base damage before modifiers
    * @param target The target being attacked
    * @return Modified damage
*/
private int apply_class_attack_bonus(int baseDamage, Character target) {
switch (characterClass) {
case WARRIOR:
// Warriors deal extra damage to armored enemies
if (target.get_equipped_armor() != null) {
return (int)(baseDamage * 1.2);
}
break;
case MAGE:
// Mages deal bonus magical damage
if (currentMp >= 5) {
use_mp(5);
return (int)(baseDamage * 1.3);
}
break;
case ROGUE:
// Rogues have chance for sneak attack
if (random.nextInt(100) < 25) {
notify_observers("SNEAK_ATTACK", target);
return baseDamage * 2;
}
break;
}
return baseDamage;
}

/**
    * MANDATORY: Player turn processing (handled by controller input)
*/
@Override
public void perform_turn() {
// Player turns are handled by user input through controller
// This method exists to fulfill abstract contract
notify_observers("PLAYER_TURN_START", this);
}

// MANDATORY: Additional getters
public List<Item> get_inventory() {
return new ArrayList<>(inventory); // Defensive copy
}

public int get_inventory_size() {
return inventory.size();
}

public String get_selected_class() {
return selectedClass;
}

public boolean has_item(String itemName) {
return inventory.stream().anyMatch(item -> item.get_name().equals(itemName));
}

public List<Item> get_consumables() {
return inventory.stream()
.filter(item -> item instanceof Consumable)
.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
}
}

```

### **MANDATORY**: Player Class Requirements

1. **CRITICAL**: Player class must extend Character abstract class
2. **CRITICAL**: Inventory system with unlimited capacity
3. **CRITICAL**: Class-specific starting equipment and abilities
4. **CRITICAL**: Experience gain and automatic leveling system
5. **CRITICAL**: Equipment upgrade mechanics using inventory items

### **MANDATORY**: Design Principles

**CRITICAL**: Player class implements:
- **Inventory Management**: Collection, usage, and organization of items
- **Character Progression**: Level-based stat increases and experience tracking
- **Equipment System**: Weapon and armor management with upgrades
- **Class Specialization**: Unique abilities and bonuses per character class

### **MANDATORY**: Combat Specialization

**CRITICAL**: Class-specific features:
- **Warrior**: Armor-piercing attacks, high durability
- **Mage**: MP-powered magical attacks, spell critical hits
- **Rogue**: Sneak attacks, high critical hit chance, evasion

### **MANDATORY**: Verification Steps

1. **CRITICAL**: Player class compiles in model/characters package
2. **CRITICAL**: Inventory operations work correctly (add, remove, use)
3. **CRITICAL**: Level progression and stat scaling function properly
4. **CRITICAL**: Class selection initializes appropriate equipment
5. **CRITICAL**: Equipment upgrade system processes key items
6. **CRITICAL**: Combat abilities apply class-specific bonuses

### CRITICAL REQUIREMENT ###
**MANDATORY**: The Player class must be implemented exactly as specified above. This class handles all player-specific functionality including progression, inventory management, and class-based combat abilities.
```


## **08-enemy-boss-classes.md**

```markdown
# CRITICAL REQUIREMENTS - Enemy and Boss Classes Implementation

### MANDATORY DIRECTIVE ###
You are an expert Java game developer. **CRITICAL**: Implement the Enemy and Boss classes exactly as specified below for the Mini Rogue Demo combat system.

## ENEMY AND BOSS CLASS SPECIFICATIONS

### **CRITICAL**: Create Enemy.java in model/characters package with this exact implementation:

```

package model.characters;

import enums.CharacterClass;
import utilities.Position;
import model.items.Item;
import model.items.Consumable;
import model.equipment.Weapon;
import model.equipment.Armor;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**

* Enemy character class extending Character.
* Handles AI behavior, loot drops, and combat patterns for dungeon enemies.
*/
public class Enemy extends Character {

// MANDATORY: Enemy-specific attributes
private String aiPattern;
private List<String> weaknesses;
private List<Item> lootTable;
private int experienceValue;
private Random random;
private int aggroRange;
private boolean isAggressive;

/**
    * MANDATORY: Constructor for Enemy
    * 
    * @param name Enemy's name
    * @param characterClass Enemy's combat class
    * @param position Initial spawn position
    * @param aiPattern AI behavior pattern
*/
public Enemy(String name, CharacterClass characterClass, Position position, String aiPattern) {
super(name, characterClass, position);
this.aiPattern = aiPattern;
this.weaknesses = new ArrayList<>();
this.lootTable = new ArrayList<>();
this.random = new Random();
this.aggroRange = 3;
this.isAggressive = true;

// Scale enemy stats based on difficulty
scale_enemy_stats();
initialize_enemy_equipment();
initialize_loot_table();
initialize_weaknesses();

// Calculate experience value
this.experienceValue = calculate_experience_value();
}

/**
    * MANDATORY: Scale enemy statistics for challenge balance
*/
private void scale_enemy_stats() {
// Enemies have slightly higher HP but lower attack than player equivalents
this.maxHp = (int)(maxHp * 1.2);
this.currentHp = maxHp;
this.baseAtk = (int)(baseAtk * 0.8);
this.maxMp = (int)(maxMp * 0.9);
this.currentMp = maxMp;
}

/**
    * MANDATORY: Initialize enemy equipment based on class
*/
private void initialize_enemy_equipment() {
switch (characterClass) {
case WARRIOR:
this.equippedWeapon = new Weapon("Rusty Sword", "An old, worn blade", 10, 0, characterClass);
this.equippedArmor = new Armor("Chain Mail", "Heavy protective armor", 0, 6, 1, characterClass);
break;
case MAGE:
this.equippedWeapon = new Weapon("Dark Staff", "A staff crackling with energy", 6, 8, characterClass);
this.equippedArmor = new Armor("Dark Robes", "Mystical protective garments", 0, 2, 6, characterClass);
break;
case ROGUE:
this.equippedWeapon = new Weapon("Curved Blade", "A wickedly sharp curved sword", 9, 3, characterClass);
this.equippedArmor = new Armor("Shadow Cloak", "Light armor for stealth", 0, 3, 2, characterClass);
break;
}
}

/**
    * MANDATORY: Initialize loot drop table
*/
private void initialize_loot_table() {
// Common drops
lootTable.add(new Consumable("Health Potion", "Restores 25 HP", 25, "health"));
lootTable.add(new Consumable("Mana Potion", "Restores 15 MP", 15, "mana"));

// Class-specific equipment drops (rare)
switch (characterClass) {
case WARRIOR:
if (random.nextInt(100) < 20) {
lootTable.add(new Weapon("Battle Axe", "Heavy two-handed weapon", 20, 0, characterClass));
}
break;
case MAGE:
if (random.nextInt(100) < 20) {
lootTable.add(new Weapon("Crystal Staff", "Staff of pure magical energy", 10, 15, characterClass));
}
break;
case ROGUE:
if (random.nextInt(100) < 20) {
lootTable.add(new Weapon("Shadow Blade", "Blade that strikes from darkness", 15, 8, characterClass));
}
break;
}
}

/**
    * MANDATORY: Initialize character class weaknesses
*/
private void initialize_weaknesses() {
switch (characterClass) {
case WARRIOR:
weaknesses.add("MAGIC");
weaknesses.add("POISON");
break;
case MAGE:
weaknesses.add("PHYSICAL");
weaknesses.add("SILENCE");
break;
case ROGUE:
weaknesses.add("AREA_ATTACK");
weaknesses.add("LIGHT");
break;
}
}

/**
    * MANDATORY: Calculate experience value based on enemy strength
    * 
    * @return Experience points awarded for defeating this enemy
*/
private int calculate_experience_value() {
int baseExp = 25;
int hpBonus = maxHp / 10;
int atkBonus = baseAtk * 2;
return baseExp + hpBonus + atkBonus;
}

/**
    * MANDATORY: Perform AI behavior based on pattern
*/
public void perform_ai() {
switch (aiPattern.toLowerCase()) {
case "aggressive":
perform_aggressive_ai();
break;
case "defensive":
perform_defensive_ai();
break;
case "magical":
perform_magical_ai();
break;
case "sneaky":
perform_sneaky_ai();
break;
default:
perform_basic_ai();
}
}

/**
    * MANDATORY: Aggressive AI behavior - always attacks
*/
private void perform_aggressive_ai() {
// Aggressive enemies always try to attack if player is in range
notify_observers("ENEMY_ACTION", "AGGRESSIVE_MOVE");
}

/**
    * MANDATORY: Defensive AI behavior - waits and counters
*/
private void perform_defensive_ai() {
// Defensive enemies wait for player to approach
if (currentHp < maxHp * 0.3) {
// Try to heal when low on health
if (random.nextInt(100) < 40) {
heal(10);
notify_observers("ENEMY_ACTION", "HEAL");
}
}
notify_observers("ENEMY_ACTION", "DEFENSIVE_STANCE");
}

/**
    * MANDATORY: Magical AI behavior - uses MP abilities
*/
private void perform_magical_ai() {
if (currentMp >= 10 \&\& random.nextInt(100) < 60) {
// Cast spell
use_mp(10);
notify_observers("ENEMY_ACTION", "CAST_SPELL");
} else {
notify_observers("ENEMY_ACTION", "MAGICAL_ATTACK");
}
}

/**
    * MANDATORY: Sneaky AI behavior - hit and run tactics
*/
private void perform_sneaky_ai() {
if (random.nextInt(100) < 30) {
notify_observers("ENEMY_ACTION", "SNEAK_ATTACK");
} else {
notify_observers("ENEMY_ACTION", "EVASIVE_MOVE");
}
}

/**
    * MANDATORY: Basic AI behavior - random actions
*/
private void perform_basic_ai() {
String[] actions = {"ATTACK", "MOVE", "WAIT"};
String action = actions[random.nextInt(actions.length)];
notify_observers("ENEMY_ACTION", action);
}

/**
    * MANDATORY: Drop loot when defeated
    * 
    * @return List of items dropped
*/
public List<Item> drop_loot() {
List<Item> droppedItems = new ArrayList<>();

// Random chance for each item in loot table
for (Item item : lootTable) {
if (random.nextInt(100) < 30) { // 30% drop chance
droppedItems.add(item);
}
}

// Always drop at least one item
if (droppedItems.isEmpty() \&\& !lootTable.isEmpty()) {
droppedItems.add(lootTable.get(random.nextInt(lootTable.size())));
}

return droppedItems;
}

/**
    * MANDATORY: Check if enemy has specific weakness
    * 
    * @param weakness The weakness type to check
    * @return true if enemy is weak to this type
*/
public boolean is_weak_to(String weakness) {
return weaknesses.contains(weakness.toUpperCase());
}

/**
    * MANDATORY: Implement attack behavior for enemy
    * 
    * @param target The character to attack
    * @return Damage dealt
*/
@Override
public int attack(Character target) {
int damage = get_total_attack();

// Apply AI pattern modifiers
damage = apply_ai_attack_modifier(damage);

// Small chance to miss
if (random.nextInt(100) < GameConstants.MISS_CHANCE) {
damage = 0;
notify_observers("ATTACK_MISSED", target);
}

notify_observers("ENEMY_ATTACKED", target);
return damage;
}

/**
    * MANDATORY: Apply AI-specific attack modifiers
    * 
    * @param baseDamage Base damage before modifiers
    * @return Modified damage
*/
private int apply_ai_attack_modifier(int baseDamage) {
switch (aiPattern.toLowerCase()) {
case "aggressive":
return (int)(baseDamage * 1.1); // 10% damage boost
case "defensive":
return (int)(baseDamage * 0.8); // 20% damage reduction
case "magical":
if (currentMp >= 5) {
use_mp(5);
return (int)(baseDamage * 1.3); // Magical damage boost
}
break;
case "sneaky":
if (random.nextInt(100) < 15) {
return baseDamage * 2; // Sneak attack
}
break;
}
return baseDamage;
}

/**
    * MANDATORY: Enemy turn processing with AI
*/
@Override
public void perform_turn() {
if (is_alive()) {
perform_ai();
}
}

// MANDATORY: Getters
public String get_ai_pattern() { return aiPattern; }
public List<String> get_weaknesses() { return new ArrayList<>(weaknesses); }
public int get_experience_value() { return experienceValue; }
public int get_aggro_range() { return aggroRange; }
public boolean is_aggressive() { return isAggressive; }
}

```

### **CRITICAL**: Create Boss.java in model/characters package with this exact implementation:

```

package model.characters;

import enums.CharacterClass;
import utilities.Position;
import model.items.Item;
import model.items.Consumable;
import model.equipment.Weapon;
import model.equipment.Armor;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**

* Boss character class extending Enemy.
* Represents powerful floor-ending enemies with special abilities and enhanced rewards.
*/
public class Boss extends Enemy {

// MANDATORY: Boss-specific attributes
private List<String> specialAbilities;
private int abilityPhaseThreshold;
private boolean inSpecialPhase;
private int originalMaxHp;

/**
    * MANDATORY: Constructor for Boss
    * 
    * @param name Boss name
    * @param characterClass Boss combat class
    * @param position Initial spawn position
*/
public Boss(String name, CharacterClass characterClass, Position position) {
super(name, characterClass, position, "boss");
this.specialAbilities = new ArrayList<>();
this.inSpecialPhase = false;
this.abilityPhaseThreshold = (int)(maxHp * 0.5); // Activate at 50% HP
this.originalMaxHp = maxHp;

// Enhance boss statistics
enhance_boss_stats();
initialize_boss_equipment();
initialize_special_abilities();
enhance_loot_table();
}

/**
    * MANDATORY: Enhance boss statistics for increased challenge
*/
private void enhance_boss_stats() {
// Bosses are significantly stronger than regular enemies
this.maxHp = (int)(maxHp * 3.0); // 3x health
this.currentHp = maxHp;
this.baseAtk = (int)(baseAtk * 2.0); // 2x attack
this.maxMp = (int)(maxMp * 2.5); // 2.5x mana
this.currentMp = maxMp;

// Update experience value for enhanced stats
this.experienceValue = calculate_boss_experience();
}

/**
    * MANDATORY: Initialize boss-tier equipment
*/
private void initialize_boss_equipment() {
switch (characterClass) {
case WARRIOR:
this.equippedWeapon = new Weapon("Legendary Blade", "A weapon of immense power", 35, 0, characterClass);
this.equippedArmor = new Armor("Plate Armor", "Masterwork protective gear", 0, 15, 3, characterClass);
break;
case MAGE:
this.equippedWeapon = new Weapon("Staff of Power", "Channels devastating magic", 20, 30, characterClass);
this.equippedArmor = new Armor("Arcane Robes", "Robes woven with protective spells", 0, 8, 20, characterClass);
break;
case ROGUE:
this.equippedWeapon = new Weapon("Shadow Fang", "Blade that drinks light itself", 28, 15, characterClass);
this.equippedArmor = new Armor("Assassin's Garb", "Armor that bends shadow", 0, 10, 8, characterClass);
break;
}
}

/**
    * MANDATORY: Initialize boss special abilities
*/
private void initialize_special_abilities() {
switch (characterClass) {
case WARRIOR:
specialAbilities.add("BERSERKER_RAGE");
specialAbilities.add("WHIRLWIND_ATTACK");
specialAbilities.add("DEFENSIVE_STANCE");
break;
case MAGE:
specialAbilities.add("METEOR_STORM");
specialAbilities.add("MANA_DRAIN");
specialAbilities.add("TELEPORT_STRIKE");
break;
case ROGUE:
specialAbilities.add("SHADOW_CLONE");
specialAbilities.add("POISON_CLOUD");
specialAbilities.add("VANISH");
break;
}
}

/**
    * MANDATORY: Enhance loot table with boss rewards
*/
private void enhance_loot_table() {
// Clear basic loot and add boss-tier rewards
lootTable.clear();

// High-tier consumables
lootTable.add(new Consumable("Greater Health Potion", "Restores 100 HP", 100, "health"));
lootTable.add(new Consumable("Greater Mana Potion", "Restores 75 MP", 75, "mana"));

// Boss-tier equipment (guaranteed drops)
switch (characterClass) {
case WARRIOR:
lootTable.add(new Weapon("Champion's Sword", "Blade of a fallen hero", 30, 0, characterClass));
lootTable.add(new Armor("Knight's Plate", "Armor blessed by light", 0, 12, 5, characterClass));
break;
case MAGE:
                lootTable.add(new Weapon("Arcane Wand", "Wand of pure magical energy", 12, 20, characterClass));
                lootTable.add(new Armor("Mystic Robes", "Robes imbued with protective magic", 0, 6, 15, characterClass));
                break;
            case ROGUE:
                lootTable.add(new Weapon("Assassin's Blade", "Blade forged in shadow", 25, 10, characterClass));
                lootTable.add(new Armor("Shadow Mail", "Armor that bends with darkness", 0, 8, 12, characterClass));
                break;
        }
        
        // Special upgrade materials
        lootTable.add(new KeyItem("Upgrade Crystal", "Crystal used for equipment enhancement", characterClass.get_class_name()));
        lootTable.add(new Consumable("Boss Elixir", "Powerful restoration potion", 150, "health"));
    }
    
    /**
     * MANDATORY: Calculate boss experience value
     * 
     * @return Experience points for defeating this boss
     */
    private int calculate_boss_experience() {
        return experienceValue * 5; // Bosses give 5x normal experience
    }
    
    /**
     * MANDATORY: Use special ability based on current HP
     */
    public void use_special_ability() {
        if (currentHp <= abilityPhaseThreshold && !inSpecialPhase) {
            inSpecialPhase = true;
            notify_observers("BOSS_PHASE_CHANGE", "Special abilities activated");
        }
        
        if (inSpecialPhase && !specialAbilities.isEmpty()) {
            String ability = specialAbilities.get(random.nextInt(specialAbilities.size()));
            execute_special_ability(ability);
        }
    }
    
    /**
     * MANDATORY: Execute specific special ability
     * 
     * @param ability The special ability to execute
     */
    private void execute_special_ability(String ability) {
        switch (ability) {
            case "BERSERKER_RAGE":
                // Increase attack power temporarily
                baseAtk = (int)(baseAtk * 1.5);
                notify_observers("BOSS_ABILITY", "Berserker Rage activated - ATK increased!");
                break;
            case "WHIRLWIND_ATTACK":
                // Area attack ability
                notify_observers("BOSS_ABILITY", "Whirlwind Attack - Area damage incoming!");
                break;
            case "DEFENSIVE_STANCE":
                // Reduce incoming damage
                notify_observers("BOSS_ABILITY", "Defensive Stance - Damage reduction active!");
                break;
            case "METEOR_STORM":
                // High magic damage
                if (use_mp(20)) {
                    notify_observers("BOSS_ABILITY", "Meteor Storm - Massive magic damage!");
                }
                break;
            case "MANA_DRAIN":
                // Drain player MP
                notify_observers("BOSS_ABILITY", "Mana Drain - Player MP reduced!");
                break;
            case "TELEPORT_STRIKE":
                // Instant teleport attack
                if (use_mp(15)) {
                    notify_observers("BOSS_ABILITY", "Teleport Strike - Instant attack!");
                }
                break;
            case "SHADOW_CLONE":
                // Create temporary duplicates
                notify_observers("BOSS_ABILITY", "Shadow Clone - Duplicates created!");
                break;
            case "POISON_CLOUD":
                // Area poison effect
                notify_observers("BOSS_ABILITY", "Poison Cloud - Toxic damage over time!");
                break;
            case "VANISH":
                // Temporary invisibility
                notify_observers("BOSS_ABILITY", "Vanish - Boss becomes harder to hit!");
                break;
        }
    }
    
    /**
     * MANDATORY: Override perform_turn for boss AI
     */
    @Override
    public void perform_turn() {
        if (is_alive()) {
            if (random.nextInt(100) < 40) { // 40% chance for special ability
                use_special_ability();
            } else {
                perform_ai();
            }
        }
    }
    
    /**
     * MANDATORY: Check if boss is defeated
     * 
     * @return true if boss HP is 0 or below
     */
    public boolean is_defeated() {
        return currentHp <= 0;
    }
    
    // MANDATORY: Additional getters
    public List<String> get_special_abilities() {
        return new ArrayList<>(specialAbilities);
    }
    
    public boolean is_in_special_phase() {
        return inSpecialPhase;
    }
    
    public int get_ability_phase_threshold() {
        return abilityPhaseThreshold;
    }
}
```


### **MANDATORY**: Boss Class Requirements

1. **CRITICAL**: Boss class must extend Enemy class
2. **CRITICAL**: Enhanced statistics (3x HP, 2x ATK, 2.5x MP)
3. **CRITICAL**: Special abilities triggered at 50% HP threshold
4. **CRITICAL**: Boss-tier equipment and enhanced loot tables
5. **CRITICAL**: 5x experience reward for increased challenge

### **MANDATORY**: Special Abilities System

**CRITICAL**: Class-specific abilities:

- **Warrior**: Berserker Rage, Whirlwind Attack, Defensive Stance
- **Mage**: Meteor Storm, Mana Drain, Teleport Strike
- **Rogue**: Shadow Clone, Poison Cloud, Vanish


### **MANDATORY**: Verification Steps

1. **CRITICAL**: Both Enemy and Boss classes compile in model/characters package
2. **CRITICAL**: AI patterns execute correctly for different enemy types
3. **CRITICAL**: Boss special abilities activate at appropriate HP threshold
4. **CRITICAL**: Loot drop system generates appropriate rewards
5. **CRITICAL**: Weakness system affects damage calculations properly
6. **CRITICAL**: Boss statistics provide appropriate challenge scaling

### CRITICAL REQUIREMENT

**MANDATORY**: The Enemy and Boss classes must be implemented exactly as specified above. These classes provide the combat encounters and challenge progression essential to the roguelike experience.

```

## **09-item-equipment-system.md**

```


# CRITICAL REQUIREMENTS - Item and Equipment System Implementation

### MANDATORY DIRECTIVE

You are an expert Java game developer. **CRITICAL**: Implement the Item hierarchy and Equipment system exactly as specified below for the Mini Rogue Demo.

## ITEM HIERARCHY SPECIFICATIONS

### **CRITICAL**: Create Item.java in model/items package with this exact implementation:

```
package model.items;

import enums.CharacterClass;
import model.characters.Character;

/**
 * Abstract base class for all items in the Mini Rogue Demo.
 * Provides common attributes and behavior for consumables, key items, and equipment.
 */
public abstract class Item {
    
    // MANDATORY: Core item attributes
    protected String name;
    protected String description;
    protected int potency;
    protected CharacterClass classType;
    
    /**
     * MANDATORY: Constructor for Item
     * 
     * @param name Item's display name
     * @param description Item's description text
     * @param potency Item's power/effectiveness value
     * @param classType Character class this item is designed for
     */
    public Item(String name, String description, int potency, CharacterClass classType) {
        this.name = name;
        this.description = description;
        this.potency = potency;
        this.classType = classType;
    }
    
    /**
     * MANDATORY: Constructor for class-neutral items
     * 
     * @param name Item's display name
     * @param description Item's description text
     * @param potency Item's power/effectiveness value
     */
    public Item(String name, String description, int potency) {
        this.name = name;
        this.description = description;
        this.potency = potency;
        this.classType = null; // Usable by any class
    }
    
    /**
     * MANDATORY: Use the item on a character
     * 
     * @param character The character using the item
     * @return true if item was successfully used
     */
    public abstract boolean use(Character character);
    
    /**
     * MANDATORY: Check if item can be used by character class
     * 
     * @param characterClass The class to check compatibility with
     * @return true if character can use this item
     */
    public boolean is_usable_by(CharacterClass characterClass) {
        return classType == null || classType == characterClass;
    }
    
    // MANDATORY: Getters
    public String get_name() { return name; }
    public String get_description() { return description; }
    public int get_potency() { return potency; }
    public CharacterClass get_class_type() { return classType; }
    
    @Override
    public String toString() {
        String classInfo = (classType != null) ? " (" + classType.get_class_name() + ")" : "";
        return name + classInfo + " - " + description;
    }
}
```


### **CRITICAL**: Create Consumable.java in model/items package with this exact implementation:

```
package model.items;

import enums.CharacterClass;
import model.characters.Character;

/**
 * Consumable items that provide immediate effects when used.
 * Items are consumed upon use and removed from inventory.
 */
public class Consumable extends Item {
    
    private String effectType; // "health", "mana", "experience"
    
    /**
     * MANDATORY: Constructor for Consumable
     * 
     * @param name Item name
     * @param description Item description
     * @param potency Effect strength
     * @param effectType Type of effect ("health", "mana", "experience")
     */
    public Consumable(String name, String description, int potency, String effectType) {
        super(name, description, potency);
        this.effectType = effectType;
    }
    
    /**
     * MANDATORY: Use consumable on character
     * 
     * @param character The character to apply effects to
     * @return true if item was consumed
     */
    @Override
    public boolean use(Character character) {
        if (character == null || !character.is_alive()) {
            return false;
        }
        
        switch (effectType.toLowerCase()) {
            case "health":
                character.heal(potency);
                return true;
            case "mana":
                character.restore_mp(potency);
                return true;
            case "experience":
                if (character instanceof model.characters.Player) {
                    ((model.characters.Player) character).gain_experience(potency);
                    return true;
                }
                break;
            default:
                return false;
        }
        
        return false;
    }
    
    public String get_effect_type() {
        return effectType;
    }
    
    @Override
    public String toString() {
        return super.toString() + " (+" + potency + " " + effectType + ")";
    }
}
```


### **CRITICAL**: Create KeyItem.java in model/items package with this exact implementation:

```
package model.items;

import enums.CharacterClass;
import model.characters.Character;
import model.equipment.Equipment;

/**
 * Special items used for upgrading equipment.
 * Key items are consumed when used for upgrades.
 */
public class KeyItem extends Item {
    
    private String upgradeType; // Type of equipment this can upgrade
    
    /**
     * MANDATORY: Constructor for KeyItem
     * 
     * @param name Item name
     * @param description Item description
     * @param upgradeType Type of upgrade ("weapon", "armor", "any")
     */
    public KeyItem(String name, String description, String upgradeType) {
        super(name, description, 1); // Potency not used for key items
        this.upgradeType = upgradeType;
    }
    
    /**
     * MANDATORY: Key items cannot be used directly on characters
     * 
     * @param character The character (ignored)
     * @return false - key items are used for upgrades only
     */
    @Override
    public boolean use(Character character) {
        return false; // Key items are used for equipment upgrades, not direct use
    }
    
    /**
     * MANDATORY: Check if this key item can upgrade specific equipment
     * 
     * @param equipment The equipment to check upgrade compatibility
     * @return true if this key item can upgrade the equipment
     */
    public boolean can_upgrade(Equipment equipment) {
        if (equipment == null) return false;
        
        // Check class compatibility
        if (!is_usable_by(equipment.get_class_type())) {
            return false;
        }
        
        // Check upgrade type compatibility
        switch (upgradeType.toLowerCase()) {
            case "weapon":
                return equipment instanceof model.equipment.Weapon;
            case "armor":
                return equipment instanceof model.equipment.Armor;
            case "any":
                return true;
            default:
                return false;
        }
    }
    
    public String get_upgrade_type() {
        return upgradeType;
    }
    
    @Override
    public String toString() {
        return super.toString() + " (Upgrades: " + upgradeType + ")";
    }
}
```


### **CRITICAL**: Create Equipment.java in model/equipment package with this exact implementation:

```
package model.equipment;

import enums.CharacterClass;
import enums.GameConstants;
import model.items.Item;
import model.characters.Character;

/**
 * Abstract base class for all equipment items (weapons and armor).
 * Provides upgrade functionality and stat management.
 */
public abstract class Equipment extends Item {
    
    // MANDATORY: Equipment attributes
    protected int upgradeLevel;
    protected int baseStatValue;
    
    /**
     * MANDATORY: Constructor for Equipment
     * 
     * @param name Equipment name
     * @param description Equipment description
     * @param potency Base stat value
     * @param classType Character class requirement
     */
    public Equipment(String name, String description, int potency, CharacterClass classType) {
        super(name, description, potency, classType);
        this.upgradeLevel = 0;
        this.baseStatValue = potency;
    }
    
    /**
     * MANDATORY: Equipment cannot be directly "used" like consumables
     * 
     * @param character The character (ignored)
     * @return false - equipment must be equipped, not used
     */
    @Override
    public boolean use(Character character) {
        return false; // Equipment is equipped, not used
    }
    
    /**
     * MANDATORY: Upgrade equipment using key items
     * 
     * @return true if upgrade was successful
     */
    public boolean upgrade() {
        if (upgradeLevel >= GameConstants.MAX_EQUIPMENT_LEVEL) {
            return false; // Already at maximum upgrade level
        }
        
        upgradeLevel++;
        apply_upgrade_bonus();
        return true;
    }
    
    /**
     * MANDATORY: Apply stat bonuses based on upgrade level
     */
    protected void apply_upgrade_bonus() {
        // Each upgrade level increases stats by 20% of base value
        double upgradeMultiplier = 1.0 + (upgradeLevel * 0.2);
        potency = (int)(baseStatValue * upgradeMultiplier);
    }
    
    /**
     * MANDATORY: Get current equipment statistics
     * 
     * @return Equipment stats as formatted string
     */
    public abstract String get_stats();
    
    /**
     * MANDATORY: Check if equipment can be upgraded further
     * 
     * @return true if upgrade is possible
     */
    public boolean can_upgrade() {
        return upgradeLevel < GameConstants.MAX_EQUIPMENT_LEVEL;
    }
    
    // MANDATORY: Getters
    public int get_upgrade_level() { return upgradeLevel; }
    public int get_base_stat_value() { return baseStatValue; }
    
    @Override
    public String toString() {
        String upgradeInfo = (upgradeLevel > 0) ? " (+" + upgradeLevel + ")" : "";
        return super.toString() + upgradeInfo;
    }
}
```


### **CRITICAL**: Create Weapon.java in model/equipment package with this exact implementation:

```
package model.equipment;

import enums.CharacterClass;

/**
 * Weapon equipment that increases character attack power.
 * Can also provide MP bonuses for magical weapons.
 */
public class Weapon extends Equipment {
    
    private int mpPower; // Additional MP provided by weapon
    
    /**
     * MANDATORY: Constructor for Weapon
     * 
     * @param name Weapon name
     * @param description Weapon description
     * @param atkPower Attack power bonus
     * @param mpPower MP power bonus
     * @param classType Required character class
     */
    public Weapon(String name, String description, int atkPower, int mpPower, CharacterClass classType) {
        super(name, description, atkPower, classType);
        this.mpPower = mpPower;
    }
    
    /**
     * MANDATORY: Get weapon attack power
     * 
     * @return Current attack power including upgrades
     */
    public int get_attack_power() {
        return potency; // Potency represents attack power for weapons
    }
    
    /**
     * MANDATORY: Get weapon MP power
     * 
     * @return Current MP power including upgrades
     */
    public int get_mp_power() {
        // MP power also scales with upgrades
        double upgradeMultiplier = 1.0 + (upgradeLevel * 0.2);
        return (int)(mpPower * upgradeMultiplier);
    }
    
    /**
     * MANDATORY: Get weapon statistics summary
     * 
     * @return Formatted stats string
     */
    @Override
    public String get_stats() {
        StringBuilder stats = new StringBuilder();
        stats.append("ATK: ").append(get_attack_power());
        if (get_mp_power() > 0) {
            stats.append(", MP: ").append(get_mp_power());
        }
        return stats.toString();
    }
    
    @Override
    public String toString() {
        return super.toString() + " [" + get_stats() + "]";
    }
}
```


### **CRITICAL**: Create Armor.java in model/equipment package with this exact implementation:

```
package model.equipment;

import enums.CharacterClass;

/**
 * Armor equipment that provides defense against attacks.
 * Can provide both physical and magical defense.
 */
public class Armor extends Equipment {
    
    private int atkDefense; // Physical defense value
    private int mpDefense;  // Magical defense value
    
    /**
     * MANDATORY: Constructor for Armor
     * 
     * @param name Armor name
     * @param description Armor description
     * @param atkDefense Physical defense value
     * @param mpDefense Magical defense value
     * @param classType Required character class
     */
    public Armor(String name, String description, int potency, int atkDefense, int mpDefense, CharacterClass classType) {
        super(name, description, potency, classType);
        this.atkDefense = atkDefense;
        this.mpDefense = mpDefense;
    }
    
    /**
     * MANDATORY: Get total defense value (primary stat)
     * 
     * @return Current defense value including upgrades
     */
    public int get_defense_value() {
        return potency; // Potency represents total defense for armor
    }
    
    /**
     * MANDATORY: Get physical attack defense
     * 
     * @return Current attack defense including upgrades
     */
    public int get_atk_defense() {
        double upgradeMultiplier = 1.0 + (upgradeLevel * 0.2);
        return (int)(atkDefense * upgradeMultiplier);
    }
    
    /**
     * MANDATORY: Get magical attack defense
     * 
     * @return Current MP defense including upgrades
     */
    public int get_mp_defense() {
        double upgradeMultiplier = 1.0 + (upgradeLevel * 0.2);
        return (int)(mpDefense * upgradeMultiplier);
    }
    
    /**
     * MANDATORY: Get armor statistics summary
     * 
     * @return Formatted stats string
     */
    @Override
    public String get_stats() {
        StringBuilder stats = new StringBuilder();
        stats.append("DEF: ").append(get_defense_value());
        if (get_atk_defense() > 0) {
            stats.append(", ATK DEF: ").append(get_atk_defense());
        }
        if (get_mp_defense() > 0) {
            stats.append(", MP DEF: ").append(get_mp_defense());
        }
        return stats.toString();
    }
    
    @Override
    public String toString() {
        return super.toString() + " [" + get_stats() + "]";
    }
}
```


### **MANDATORY**: Item System Requirements

1. **CRITICAL**: Item hierarchy with abstract Item base class
2. **CRITICAL**: Consumables provide immediate effects (health, mana, experience)
3. **CRITICAL**: Key items enable equipment upgrades
4. **CRITICAL**: Equipment system with upgrade levels and stat scaling
5. **CRITICAL**: Class compatibility checking for all items

### **MANDATORY**: Design Principles

**CRITICAL**: Item system implements:

- **Template Method Pattern**: Abstract Item class with concrete implementations
- **Strategy Pattern**: Different item types with unique behaviors
- **Composite Pattern**: Equipment combines multiple stat bonuses
- **Observer Pattern**: Equipment changes notify characters


### **MANDATORY**: Verification Steps

1. **CRITICAL**: All classes compile successfully in respective packages
2. **CRITICAL**: Item hierarchy inheritance works correctly
3. **CRITICAL**: Consumable effects apply to characters properly
4. **CRITICAL**: Equipment upgrade system functions with key items
5. **CRITICAL**: Class compatibility restrictions enforce properly
6. **CRITICAL**: Stat scaling calculations produce reasonable values

### CRITICAL REQUIREMENT

**MANDATORY**: The Item and Equipment system must be implemented exactly as specified above. This system provides the core progression mechanics and inventory management for the Mini Rogue Demo.

```

## **10-map-generation.md**

```


# CRITICAL REQUIREMENTS - Map Generation System Implementation

### MANDATORY DIRECTIVE

You are an expert Java game developer. **CRITICAL**: Implement the Map class with procedural generation exactly as specified below for the Mini Rogue Demo.

## MAP GENERATION SPECIFICATIONS

### **CRITICAL**: Create Map.java in model/map package with this exact implementation:

```
package model.map;

import enums.TileType;
import enums.GameConstants;
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
 * Map class handles procedural generation of dungeon floors.
 * Creates random layouts with rooms, corridors, enemies, and items.
 */
public class Map {
    
    // MANDATORY: Map attributes
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
    
    /**
     * MANDATORY: Constructor for Map
     * 
     * @param floor Current floor number for difficulty scaling
     */
    public Map(int floor) {
        this.width = GameConstants.MAP_WIDTH;
        this.height = GameConstants.MAP_HEIGHT;
        this.tiles = new Tile[width][height];
        this.rooms = new ArrayList<>();
        this.itemLocations = new ArrayList<>();
        this.enemyLocations = new ArrayList<>();
        this.random = new Random();
        this.currentFloor = floor;
        
        generate_dungeon();
    }
    
    /**
     * MANDATORY: Generate complete dungeon floor with rooms and corridors
     */
    public void generate_dungeon() {
        // Initialize all tiles as walls
        initialize_walls();
        
        // Generate rooms
        generate_rooms();
        
        // Connect rooms with corridors
        generate_corridors();
        
        // Place entrance and boss room
        place_special_rooms();
        
        // Place enemies and items
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
     * MANDATORY: Create horizontal corridor
     * 
     * @param x1 Start x coordinate
     * @param x2 End x coordinate
     * @param y Y coordinate for corridor
     */
    private void create_horizontal_corridor(int x1, int x2, int y) {
        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        
        for (int x = minX; x <= maxX; x++) {
            if (is_valid_position(x, y)) {
                tiles[x][y] = new Tile(TileType.FLOOR, new Position(x, y));
            }
        }
    }
    
    /**
     * MANDATORY: Create vertical corridor
     * 
     * @param y1 Start y coordinate
     * @param y2 End y coordinate
     * @param x X coordinate for corridor
     */
    private void create_vertical_corridor(int y1, int y2, int x) {
        int minY = Math.min(y1, y2);
        int maxY = Math.max(y1, y2);
        
        for (int y = minY; y <= maxY; y++) {
            if (is_valid_position(x, y)) {
                tiles[x][y] = new Tile(TileType.FLOOR, new Position(x, y));
            }
        }
    }
    
    /**
     * MANDATORY: Place entrance and boss room
     */
    private void place_special_rooms() {
        if (rooms.isEmpty()) return;
        
        // First room is entrance
        Room entranceRoom = rooms.get(0);
        Position entrance = entranceRoom.get_center();
        tiles[entrance.get_x()][entrance.get_y()] = new Tile(TileType.ENTRANCE, entrance);
        playerStartPosition = entrance;
        
        // Last room contains boss
        Room bossRoom = rooms.get(rooms.size() - 1);
        Position bossPos = bossRoom.get_center();
        tiles[bossPos.get_x()][bossPos.get_y()] = new Tile(TileType.BOSS_ROOM, bossPos);
        bossPosition = bossPos;
    }
    
    /**
     * MANDATORY: Populate dungeon with enemies and items
     */
    private void populate_dungeon() {
        place_enemies();
        place_items();
    }
    
    /**
     * MANDATORY: Place enemies randomly in rooms (excluding entrance and boss room)
     */
    private void place_enemies() {
        int enemyCount = Math.min(currentFloor + 3, 8); // Scale with floor
        int placedEnemies = 0;
        
        for (int i = 1; i < rooms.size() - 1 && placedEnemies < enemyCount; i++) {
            Room room = rooms.get(i);
            
            // 70% chance for enemy in each room
            if (random.nextInt(100) < 70) {
                Position enemyPos = get_random_floor_position_in_room(room);
                if (enemyPos != null) {
                    enemyLocations.add(enemyPos);
                    placedEnemies++;
                }
            }
        }
    }
    
    /**
     * MANDATORY: Place items randomly throughout the map
     */
    private void place_items() {
        int itemCount = Math.min(currentFloor + 2, 6); // Scale with floor
        int placedItems = 0;
        
        while (placedItems < itemCount) {
            Room room = rooms.get(random.nextInt(rooms.size()));
            Position itemPos = get_random_floor_position_in_room(room);
            
            if (itemPos != null && !itemLocations.contains(itemPos) && !enemyLocations.contains(itemPos)) {
                itemLocations.add(itemPos);
                placedItems++;
            }
        }
    }
    
    /**
     * MANDATORY: Get random floor position within a room
     * 
     * @param room Room to search
     * @return Random floor position or null if none available
     */
    private Position get_random_floor_position_in_room(Room room) {
        List<Position> floorPositions = new ArrayList<>();
        
        for (int x = room.x; x < room.x + room.width; x++) {
            for (int y = room.y; y < room.y + room.height; y++) {
                if (tiles[x][y].get_tile_type() == TileType.FLOOR) {
                    floorPositions.add(new Position(x, y));
                }
            }
        }
        
        if (floorPositions.isEmpty()) return null;
        return floorPositions.get(random.nextInt(floorPositions.size()));
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
            tile.set_occupant(enemy);
            enemy.move_to(position);
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
```


### **MANDATORY**: Map Generation Requirements

1. **CRITICAL**: Procedural generation creates unique layouts each run
2. **CRITICAL**: Room-based generation with corridor connections
3. **CRITICAL**: Proper entrance and boss room placement
4. **CRITICAL**: Enemy and item distribution scales with floor number
5. **CRITICAL**: Collision detection and movement validation

### **MANDATORY**: Algorithm Implementation

**CRITICAL**: Generation process:

1. **Initialize**: Fill entire map with wall tiles
2. **Room Generation**: Create random non-overlapping rooms
3. **Corridor Creation**: Connect all rooms with L-shaped corridors
4. **Special Placement**: Mark entrance and boss room locations
5. **Population**: Place enemies and items with floor-based scaling

### **MANDATORY**: Verification Steps

1. **CRITICAL**: Map class compiles successfully in model/map package
2. **CRITICAL**: Room generation creates non-overlapping spaces
3. **CRITICAL**: Corridors properly connect all rooms
4. **CRITICAL**: Enemy and item placement follows scaling rules
5. **CRITICAL**: Movement validation prevents walking through walls
6. **CRITICAL**: Special rooms (entrance, boss) are properly marked

### CRITICAL REQUIREMENT

**MANDATORY**: The Map generation system must be implemented exactly as specified above. This system provides the foundation for all dungeon exploration and ensures unique experiences for each playthrough of the Mini Rogue Demo.

```

## **11-game-logic-core.md**

```markdown
# CRITICAL REQUIREMENTS - Game Logic Core Implementation

### MANDATORY DIRECTIVE ###
You are an expert Java game developer. **CRITICAL**: Implement the GameLogic class exactly as specified below for the Mini Rogue Demo game state management.

## GAMELOGIC CLASS SPECIFICATION

### **CRITICAL**: Create GameLogic.java in model/gameLogic package with this exact implementation:

```

package model.gameLogic;

import interfaces.GameModel;
import interfaces.GameObserver;
import enums.GameState;
import enums.GameConstants;
import utilities.Position;
import model.characters.Player;
import model.characters.Enemy;
import model.characters.Boss;
import model.map.Map;
import model.items.Item;
import model.items.Consumable;
import model.equipment.Equipment;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**

* GameLogic class manages overall game state and coordinates between game systems.
* Handles game flow, player actions, victory/defeat conditions, and game progression.
*/
public class GameLogic implements GameModel {

// MANDATORY: Core game state attributes
private GameState gameState;
private boolean victoryStatus;
private boolean deathStatus;
private int playerProgress;
private boolean pauseStatus;
private boolean npcDialogue;

// MANDATORY: Game components
private Player player;
private Map currentMap;
private List<Enemy> currentEnemies;
private Boss currentBoss;
private int currentFloor;
private Random random;

// MANDATORY: Observer pattern implementation
private List<GameObserver> observers;

/**
    * MANDATORY: Constructor for GameLogic
    * 
    * @param player The player character
*/
public GameLogic(Player player) {
this.player = player;
this.gameState = GameState.MAIN_MENU;
this.victoryStatus = false;
this.deathStatus = false;
this.playerProgress = 0;
this.pauseStatus = false;
this.npcDialogue = false;
this.currentEnemies = new ArrayList<>();
this.currentFloor = 1;
this.random = new Random();
this.observers = new ArrayList<>();

initialize_game();
}

/**
    * MANDATORY: Initialize game components
*/
private void initialize_game() {
generate_new_floor();
place_player_on_map();
gameState = GameState.PLAYING;
notify_observers("GAME_INITIALIZED", this);
}

/**
    * MANDATORY: Handle player actions and update game state accordingly
    * 
    * @param action The action the player is attempting
    * @param data Additional data for the action
*/
public void handle_player_action(String action, Object data) {
if (pauseStatus || npcDialogue) return;

switch (action.toLowerCase()) {
case "move":
handle_movement((Position) data);
break;
case "attack":
handle_attack_action();
break;
case "use_item":
handle_item_usage((Item) data);
break;
case "open_inventory":
open_inventory();
break;
case "upgrade_equipment":
handle_equipment_upgrade((Equipment) data);
break;
case "pause":
toggle_pause();
break;
default:
notify_observers("INVALID_ACTION", action);
}

// Check game conditions after each action
check_victory_condition();
check_death_condition();
}

/**
    * MANDATORY: Handle player movement and tile interactions
    * 
    * @param targetPosition The position player wants to move to
*/
private void handle_movement(Position targetPosition) {
if (!currentMap.is_valid_move(targetPosition)) {
notify_observers("INVALID_MOVE", targetPosition);
return;
}

// Move player to new position
player.move_to(targetPosition);

// Check for tile interactions
utilities.Tile currentTile = currentMap.get_tile(targetPosition);
handle_tile_interaction(currentTile);

// Update enemy positions and AI
update_enemy_positions();

notify_observers("PLAYER_MOVED", targetPosition);
}

/**
    * MANDATORY: Handle interactions with different tile types
    * 
    * @param tile The tile the player just moved onto
*/
private void handle_tile_interaction(utilities.Tile tile) {
// Mark tile as explored
tile.set_explored();

// Handle items on tile
if (tile.has_items()) {
List<Item> items = tile.get_items();
for (Item item : items) {
if (player.collect_item(item)) {
tile.remove_item(item);
notify_observers("ITEM_COLLECTED", item);
}
}
}

// Handle enemy encounters
if (tile.get_occupant() instanceof Enemy) {
Enemy enemy = (Enemy) tile.get_occupant();
initiate_combat(enemy);
} else if (tile.get_occupant() instanceof Boss) {
Boss boss = (Boss) tile.get_occupant();
initiate_boss_battle(boss);
}

// Handle special tiles
switch (tile.get_tile_type()) {
case STAIRS:
advance_to_next_floor();
break;
case BOSS_ROOM:
if (currentBoss != null \&\& !currentBoss.is_alive()) {
advance_to_next_floor();
}
break;
}
}

/**
    * MANDATORY: Handle player attack action
*/
private void handle_attack_action() {
Position playerPos = player.get_position();

// Find adjacent enemies
for (Enemy enemy : currentEnemies) {
if (enemy.is_alive() \&\&
playerPos.is_adjacent_to(enemy.get_position())) {
initiate_combat(enemy);
return;
}
}

// Check for boss
if (currentBoss != null \&\& currentBoss.is_alive() \&\&
playerPos.is_adjacent_to(currentBoss.get_position())) {
initiate_boss_battle(currentBoss);
return;
}

notify_observers("NO_TARGET_FOUND", null);
}

/**
    * MANDATORY: Handle item usage
    * 
    * @param item The item to use
*/
private void handle_item_usage(Item item) {
if (player.use_item(item)) {
apply_item_effects(item);
notify_observers("ITEM_USED", item);
} else {
notify_observers("ITEM_USE_FAILED", item);
}
}

/**
    * MANDATORY: Apply item effects to player
    * 
    * @param item The item whose effects to apply
*/
public void apply_item_effects(Item item) {
if (item instanceof Consumable) {
Consumable consumable = (Consumable) item;
switch (consumable.get_effect_type().toLowerCase()) {
case "health":
player.heal(consumable.get_potency());
notify_observers("PLAYER_HEALED", consumable.get_potency());
break;
case "mana":
player.restore_mp(consumable.get_potency());
notify_observers("PLAYER_MP_RESTORED", consumable.get_potency());
break;
case "experience":
player.gain_experience(consumable.get_potency());
notify_observers("EXPERIENCE_GAINED", consumable.get_potency());
break;
}
}
}

/**
    * MANDATORY: Handle equipment upgrade process
    * 
    * @param equipment The equipment to upgrade
*/
private void handle_equipment_upgrade(Equipment equipment) {
if (player.upgrade_equipment(equipment)) {
notify_observers("EQUIPMENT_UPGRADED", equipment);
} else {
notify_observers("UPGRADE_FAILED", equipment);
}
}

/**
    * MANDATORY: Open inventory interface
*/
private void open_inventory() {
gameState = GameState.INVENTORY;
player.open_inventory();
notify_observers("INVENTORY_OPENED", player.get_inventory());
}

/**
    * MANDATORY: Initiate combat with enemy
    * 
    * @param enemy The enemy to fight
*/
private void initiate_combat(Enemy enemy) {
gameState = GameState.BATTLE;
notify_observers("COMBAT_INITIATED", enemy);

// Combat will be handled by BattleLogic
// For now, just set the game state
}

/**
    * MANDATORY: Initiate boss battle
    * 
    * @param boss The boss to fight
*/
private void initiate_boss_battle(Boss boss) {
gameState = GameState.BATTLE;
notify_observers("BOSS_BATTLE_INITIATED", boss);

// Boss battle will be handled by BattleLogic
// For now, just set the game state
}

/**
    * MANDATORY: Update enemy positions and AI behavior
*/
private void update_enemy_positions() {
for (Enemy enemy : currentEnemies) {
if (enemy.is_alive()) {
enemy.perform_turn();

         // Simple AI: move towards player if within aggro range
         if (player.get_position().distance_to(enemy.get_position()) 
             <= enemy.get_aggro_range()) {
             move_enemy_towards_player(enemy);
         }
     }
    }

// Update boss AI
if (currentBoss != null \&\& currentBoss.is_alive()) {
currentBoss.perform_turn();
}
}

/**
    * MANDATORY: Move enemy towards player position
    * 
    * @param enemy The enemy to move
*/
private void move_enemy_towards_player(Enemy enemy) {
Position enemyPos = enemy.get_position();
Position playerPos = player.get_position();

int deltaX = Integer.compare(playerPos.get_x(), enemyPos.get_x());
int deltaY = Integer.compare(playerPos.get_y(), enemyPos.get_y());

Position newPos = enemyPos.move(deltaX, deltaY);

if (currentMap.is_valid_move(newPos)) {
// Clear old position
currentMap.get_tile(enemyPos).clear_occupant();
// Move to new position
currentMap.get_tile(newPos).set_occupant(enemy);
enemy.move_to(newPos);
}
}

/**
    * MANDATORY: Generate new dungeon floor
*/
public void trigger_proc_generation() {
generate_new_floor();
place_entities_on_map();
notify_observers("NEW_FLOOR_GENERATED", currentFloor);
}

/**
    * MANDATORY: Generate new floor with procedural generation
*/
private void generate_new_floor() {
currentMap = new Map(currentFloor);
currentEnemies.clear();
currentBoss = null;
}

/**
    * MANDATORY: Place player on the map at entrance
*/
private void place_player_on_map() {
Position startPos = currentMap.get_player_start_position();
player.move_to(startPos);
currentMap.get_tile(startPos).set_occupant(player);
}

/**
    * MANDATORY: Place enemies and items on the map
*/
private void place_entities_on_map() {
// Place enemies
List<Position> enemyPositions = currentMap.get_enemy_locations();
for (Position pos : enemyPositions) {
Enemy enemy = create_random_enemy(pos);
currentEnemies.add(enemy);
currentMap.place_enemy(enemy, pos);
}

// Place boss
Position bossPos = currentMap.get_boss_position();
currentBoss = create_boss_for_floor(bossPos);
currentMap.get_tile(bossPos).set_occupant(currentBoss);

// Place items
List<Position> itemPositions = currentMap.get_item_locations();
for (Position pos : itemPositions) {
Item item = create_random_item();
currentMap.place_item(item, pos);
}
}

/**
    * MANDATORY: Create random enemy for current floor
    * 
    * @param position Enemy spawn position
    * @return New Enemy instance
*/
private Enemy create_random_enemy(Position position) {
enums.CharacterClass[] classes = enums.CharacterClass.values();
enums.CharacterClass randomClass = classes[random.nextInt(classes.length)];

String[] aiPatterns = {"aggressive", "defensive", "magical", "sneaky"};
String randomAI = aiPatterns[random.nextInt(aiPatterns.length)];

return new Enemy("Floor " + currentFloor + " Enemy", randomClass, position, randomAI);
}

/**
    * MANDATORY: Create boss for current floor
    * 
    * @param position Boss spawn position
    * @return New Boss instance
*/
private Boss create_boss_for_floor(Position position) {
enums.CharacterClass[] classes = enums.CharacterClass.values();
enums.CharacterClass bossClass = classes[random.nextInt(classes.length)];

return new Boss("Floor " + currentFloor + " Boss", bossClass, position);
}

/**
    * MANDATORY: Create random item for floor
    * 
    * @return New Item instance
*/
private Item create_random_item() {
if (random.nextInt(100) < 70) {
// 70% chance for consumable
if (random.nextBoolean()) {
return new Consumable("Health Potion", "Restores HP", 50, "health");
} else {
return new Consumable("Mana Potion", "Restores MP", 30, "mana");
}
} else {
// 30% chance for upgrade item
return new model.items.KeyItem("Upgrade Crystal", "Enhances equipment", "any");
}
}

/**
    * MANDATORY: Advance to next dungeon floor
*/
private void advance_to_next_floor() {
currentFloor++;
playerProgress = currentFloor - 1;

// Check for victory condition (e.g., completed 5 floors)
if (currentFloor > 5) {
achieve_victory();
} else {
trigger_proc_generation();
place_player_on_map();
notify_observers("FLOOR_ADVANCED", currentFloor);
}
}

/**
    * MANDATORY: Check victory condition
*/
public void check_victory_condition() {
if (currentFloor > 5 || (currentBoss != null \&\& !currentBoss.is_alive() \&\& currentFloor == 5)) {
achieve_victory();
}
}

/**
    * MANDATORY: Handle victory achievement
*/
private void achieve_victory() {
victoryStatus = true;
gameState = GameState.VICTORY;
notify_observers("VICTORY_ACHIEVED", playerProgress);
}

/**
    * MANDATORY: Check death condition
*/
public void check_death_condition() {
if (!player.is_alive()) {
handle_player_death();
}
}

/**
    * MANDATORY: Handle player death
*/
private void handle_player_death() {
deathStatus = true;
gameState = GameState.GAME_OVER;
notify_observers("PLAYER_DEATH", null);
}

/**
    * MANDATORY: Update overall game state
*/
public void update_game_state() {
if (!pauseStatus \&\& !npcDialogue) {
update_enemy_positions();
check_victory_condition();
check_death_condition();
notify_observers("GAME_STATE_UPDATED", gameState);
}
}

/**
    * MANDATORY: Pause the game
*/
public void pause_game() {
pauseStatus = true;
gameState = GameState.PAUSED;
notify_observers("GAME_PAUSED", null);
}

/**
    * MANDATORY: Resume the game
*/
public void resume_game() {
pauseStatus = false;
gameState = GameState.PLAYING;
notify_observers("GAME_RESUMED", null);
}

/**
    * MANDATORY: Toggle pause state
*/
private void toggle_pause() {
if (pauseStatus) {
resume_game();
} else {
pause_game();
}
}

/**
    * MANDATORY: Handle NPC dialogue
    * 
    * @param npcName The NPC being talked to
    * @param dialogue The dialogue content
*/
public void handle_npc_dialogue(String npcName, String dialogue) {
npcDialogue = true;
notify_observers("NPC_DIALOGUE_STARTED", npcName + ": " + dialogue);
}

/**
    * MANDATORY: End NPC dialogue
*/
public void end_npc_dialogue() {
npcDialogue = false;
notify_observers("NPC_DIALOGUE_ENDED", null);
}

// MANDATORY: Observer pattern implementation
@Override
public void notify_observers(String event, Object data) {
for (GameObserver observer : observers) {
observer.on_model_changed(event, data);
}
}

@Override
public void add_observer(GameObserver observer) {
if (observer != null \&\& !observers.contains(observer)) {
observers.add(observer);
}
}

@Override
public void remove_observer(GameObserver observer) {
observers.remove(observer);
}

// MANDATORY: Getters
public GameState get_game_state() { return gameState; }
public boolean is_victory() { return victoryStatus; }
public boolean is_death() { return deathStatus; }
public int get_player_progress() { return playerProgress; }
public boolean is_paused() { return pauseStatus; }
public boolean is_npc_dialogue_active() { return npcDialogue; }
public Player get_player() { return player; }
public Map get_current_map() { return currentMap; }
public List<Enemy> get_current_enemies() { return new ArrayList<>(currentEnemies); }
public Boss get_current_boss() { return currentBoss; }
public int get_current_floor() { return currentFloor; }
}

```

### **MANDATORY**: GameLogic Requirements

1. **CRITICAL**: GameLogic class must implement GameModel interface
2. **CRITICAL**: Manage all game state transitions and flow control
3. **CRITICAL**: Handle player actions and tile interactions
4. **CRITICAL**: Coordinate with Map, Character, and Item systems
5. **CRITICAL**: Implement observer pattern for state notifications

### **MANDATORY**: Game State Management

**CRITICAL**: GameLogic handles:
- **Game Flow**: Main menu, playing, battle, inventory, pause states
- **Victory/Defeat**: Win/lose condition checking and state transitions
- **Floor Progression**: Procedural generation and advancement
- **Player Actions**: Movement, combat, inventory, equipment upgrades

### **MANDATORY**: Verification Steps

1. **CRITICAL**: GameLogic class compiles successfully in model/gameLogic package
2. **CRITICAL**: Game state management handles all enum values properly
3. **CRITICAL**: Player action handling processes all input types
4. **CRITICAL**: Procedural generation integration works with Map class
5. **CRITICAL**: Observer notifications occur for all major events
6. **CRITICAL**: Victory and defeat conditions trigger appropriately

### CRITICAL REQUIREMENT ###
**MANDATORY**: The GameLogic class must be implemented exactly as specified above. This class coordinates all game systems and manages the overall game state for the Mini Rogue Demo.
```


## **12-battle-logic-system.md**

```markdown
# CRITICAL REQUIREMENTS - Battle Logic System Implementation

### MANDATORY DIRECTIVE ###
You are an expert Java game developer. **CRITICAL**: Implement the BattleLogic class exactly as specified below for the Mini Rogue Demo combat system.

## BATTLELOGIC CLASS SPECIFICATION

### **CRITICAL**: Create BattleLogic.java in model/gameLogic package with this exact implementation:

```

package model.gameLogic;

import interfaces.GameModel;
import interfaces.GameObserver;
import enums.GameConstants;
import model.characters.Character;
import model.characters.Player;
import model.characters.Enemy;
import model.characters.Boss;
import model.items.Item;
import model.items.Consumable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**

* BattleLogic class handles all combat mechanics and real-time battle processing.
* Manages combat flow, damage calculations, AI processing, and multithreaded enemy behavior.
*/
public class BattleLogic implements GameModel {

// MANDATORY: Battle state attributes
private List<Character> combatants;
private boolean targetHit;
private AttackResult attackResult;
private List<Item> rewards;
private boolean playerHit;

// MANDATORY: Combat management
private Character currentAttacker;
private Character currentTarget;
private boolean battleActive;
private int battleRound;
private Random random;
private ExecutorService enemyAiExecutor;

// MANDATORY: Observer pattern implementation
private List<GameObserver> observers;

/**
    * MANDATORY: Constructor for BattleLogic
*/
public BattleLogic() {
this.combatants = new ArrayList<>();
this.rewards = new ArrayList<>();
this.targetHit = false;
this.playerHit = false;
this.battleActive = false;
this.battleRound = 0;
this.random = new Random();
this.observers = new ArrayList<>();
this.enemyAiExecutor = Executors.newCachedThreadPool();
}

/**
    * MANDATORY: Initiate combat between characters
    * 
    * @param player The player character
    * @param enemy The enemy or boss to fight
*/
public void initiate_combat(Player player, Character enemy) {
combatants.clear();
rewards.clear();

combatants.add(player);
combatants.add(enemy);

currentAttacker = player; // Player goes first
currentTarget = enemy;

battleActive = true;
battleRound = 1;
targetHit = false;
playerHit = false;

notify_observers("BATTLE_INITIATED", combatants);

// Start enemy AI processing
start_enemy_ai_processing(enemy);
}

/**
    * MANDATORY: Start multithreaded enemy AI processing
    * 
    * @param enemy The enemy to process AI for
*/
private void start_enemy_ai_processing(Character enemy) {
if (enemy instanceof Enemy || enemy instanceof Boss) {
CompletableFuture.runAsync(() -> {
while (battleActive \&\& enemy.is_alive()) {
try {
Thread.sleep(GameConstants.ENEMY_AI_DELAY);
if (battleActive \&\& currentAttacker == enemy) {
process_enemy_turn(enemy);
}
} catch (InterruptedException e) {
Thread.currentThread().interrupt();
break;
}
}
}, enemyAiExecutor);
}
}

/**
    * MANDATORY: Process enemy AI turn
    * 
    * @param enemy The enemy taking their turn
*/
private void process_enemy_turn(Character enemy) {
if (enemy instanceof Enemy) {
apply_enemy_ai((Enemy) enemy);
} else if (enemy instanceof Boss) {
apply_boss_ai((Boss) enemy);
}

// Switch to player turn
end_character_turn();
}

/**
    * MANDATORY: Apply enemy AI behavior patterns
    * 
    * @param enemy The enemy to process AI for
*/
public void apply_enemy_ai(Enemy enemy) {
if (!enemy.is_alive()) return;

String aiPattern = enemy.get_ai_pattern();
Player player = get_player_from_combatants();

switch (aiPattern.toLowerCase()) {
case "aggressive":
// Always attack with bonus damage
int aggressiveDamage = enemy.attack(player);
aggressiveDamage = (int)(aggressiveDamage * 1.2);
resolve_attack(enemy, player, aggressiveDamage);
break;

     case "defensive":
         // Heal if low health, otherwise attack
         if (enemy.get_current_hp() < enemy.get_max_hp() * 0.3) {
             enemy.heal(20);
             notify_observers("ENEMY_HEALED", enemy);
         } else {
             int damage = enemy.attack(player);
             resolve_attack(enemy, player, damage);
         }
         break;
         
     case "magical":
         // Use MP abilities if available
         if (enemy.get_current_mp() >= 10) {
             int magicalDamage = enemy.attack(player);
             enemy.use_mp(10);
             magicalDamage = (int)(magicalDamage * 1.4);
             resolve_attack(enemy, player, magicalDamage);
             notify_observers("ENEMY_MAGICAL_ATTACK", enemy);
         } else {
             int damage = enemy.attack(player);
             resolve_attack(enemy, player, damage);
         }
         break;
         
     case "sneaky":
         // Chance for critical hit or evasion
         if (random.nextInt(100) < 25) {
             // Sneak attack
             int sneakDamage = enemy.attack(player) * 2;
             resolve_attack(enemy, player, sneakDamage);
             notify_observers("ENEMY_SNEAK_ATTACK", enemy);
         } else if (random.nextInt(100) < 15) {
             // Evasion
             notify_observers("ENEMY_EVADED", enemy);
         } else {
             int damage = enemy.attack(player);
             resolve_attack(enemy, player, damage);
         }
         break;
         
     default:
         // Basic attack
         int basicDamage = enemy.attack(player);
         resolve_attack(enemy, player, basicDamage);
         break;
    }
}

/**
    * MANDATORY: Apply boss AI with special abilities
    * 
    * @param boss The boss to process AI for
*/
private void apply_boss_ai(Boss boss) {
if (!boss.is_alive()) return;

Player player = get_player_from_combatants();

// Boss uses special abilities more frequently
if (random.nextInt(100) < 60) {
boss.use_special_ability();

     // Apply boss special ability effects
     int specialDamage = boss.attack(player);
     specialDamage = (int)(specialDamage * 1.5); // Boss abilities are stronger
     resolve_attack(boss, player, specialDamage);
     
     notify_observers("BOSS_SPECIAL_ABILITY", boss);
    } else {
// Regular boss attack
int damage = boss.attack(player);
damage = (int)(damage * 1.3); // Boss attacks are stronger
resolve_attack(boss, player, damage);
}
}

/**
    * MANDATORY: Calculate damage with all modifiers
    * 
    * @param attacker The character attacking
    * @param target The character being attacked
    * @return Final damage amount
*/
public int calculate_damage(Character attacker, Character target) {
int baseDamage = attacker.get_total_attack();
int defense = target.get_total_defense();

// Apply weakness bonuses
if (target instanceof Enemy) {
Enemy enemy = (Enemy) target;
baseDamage = apply_weakness_bonus(baseDamage, attacker, enemy);
}

// Check for critical hit
if (is_critical_hit(attacker)) {
baseDamage = (int)(baseDamage * GameConstants.CRITICAL_HIT_MULTIPLIER);
notify_observers("CRITICAL_HIT", attacker);
}

// Check for miss
if (is_attack_miss()) {
notify_observers("ATTACK_MISSED", attacker);
return 0;
}

// Apply defense
int finalDamage = Math.max(1, baseDamage - defense);

return finalDamage;
}

/**
    * MANDATORY: Apply weakness bonuses to damage
    * 
    * @param baseDamage Base damage before weakness bonus
    * @param attacker The attacking character
    * @param enemy The enemy being attacked
    * @return Modified damage with weakness bonus
*/
private int apply_weakness_bonus(int baseDamage, Character attacker, Enemy enemy) {
if (attacker instanceof Player) {
Player player = (Player) attacker;

     switch (player.get_character_class()) {
         case WARRIOR:
             if (enemy.is_weak_to("PHYSICAL")) {
                 return (int)(baseDamage * 1.5);
             }
             break;
         case MAGE:
             if (enemy.is_weak_to("MAGIC")) {
                 return (int)(baseDamage * 1.5);
             }
             break;
         case ROGUE:
             if (enemy.is_weak_to("PRECISION")) {
                 return (int)(baseDamage * 1.5);
             }
             break;
     }
    }

return baseDamage;
}

/**
    * MANDATORY: Check if attack is a critical hit
    * 
    * @param attacker The attacking character
    * @return true if critical hit occurs
*/
private boolean is_critical_hit(Character attacker) {
int critChance = GameConstants.CRITICAL_HIT_CHANCE;

if (attacker instanceof Player) {
Player player = (Player) attacker;
critChance = model.characters.Stats.calculate_critical_chance(player.get_character_class());
}

return random.nextInt(100) < critChance;
}

/**
    * MANDATORY: Check if attack misses
    * 
    * @return true if attack misses
*/
private boolean is_attack_miss() {
return random.nextInt(100) < GameConstants.MISS_CHANCE;
}

/**
    * MANDATORY: Resolve attack between characters
    * 
    * @param attacker The character attacking
    * @param target The character being attacked
    * @param damage Damage amount to apply
*/
public void resolve_attack(Character attacker, Character target, int damage) {
boolean targetStillAlive = target.take_damage(damage);

// Set battle state
targetHit = true;
if (target instanceof Player) {
playerHit = true;
}

// Create attack result
attackResult = new AttackResult(attacker, target, damage, targetStillAlive);

notify_observers("ATTACK_RESOLVED", attackResult);

// Check if target is defeated
if (!targetStillAlive) {
handle_character_defeat(target);
}
}

/**
    * MANDATORY: Handle character defeat
    * 
    * @param defeatedCharacter The character that was defeated
*/
private void handle_character_defeat(Character defeatedCharacter) {
if (defeatedCharacter instanceof Player) {
// Player defeated - battle ends
end_combat(false);
} else if (defeatedCharacter instanceof Enemy) {
// Enemy defeated - player gains rewards
Enemy enemy = (Enemy) defeatedCharacter;
distribute_rewards(enemy);
end_combat(true);
} else if (defeatedCharacter instanceof Boss) {
// Boss defeated - player gains major rewards
Boss boss = (Boss) defeatedCharacter;
distribute_boss_rewards(boss);
end_combat(true);
}
}

/**
    * MANDATORY: Handle critical hit effects
    * 
    * @param attacker The character that scored a critical hit
    * @param target The target of the critical hit
    * @param baseDamage The base damage before critical multiplier
*/
public void handle_critical_hit(Character attacker, Character target, int baseDamage) {
int criticalDamage = (int)(baseDamage * GameConstants.CRITICAL_HIT_MULTIPLIER);

// Apply additional critical hit effects based on character class
if (attacker instanceof Player) {
Player player = (Player) attacker;
switch (player.get_character_class()) {
case WARRIOR:
// Warrior crits restore some HP
player.heal(5);
notify_observers("WARRIOR_CRITICAL_HEAL", player);
break;
case MAGE:
// Mage crits restore some MP
player.restore_mp(10);
notify_observers("MAGE_CRITICAL_MP", player);
break;
case ROGUE:
// Rogue crits have chance for double damage
if (random.nextInt(100) < 25) {
criticalDamage *= 2;
notify_observers("ROGUE_DEVASTATING_CRITICAL", player);
}
break;
}
}

resolve_attack(attacker, target, criticalDamage);
}

/**
    * MANDATORY: Apply status effects during combat
    * 
    * @param effect The status effect to apply
    * @param target The character to apply effect to
    * @param duration Duration in rounds
*/
public void apply_status_effects(String effect, Character target, int duration) {
switch (effect.toLowerCase()) {
case "poison":
// Poison deals damage over time
target.take_damage(10);
notify_observers("POISON_DAMAGE", target);
break;
case "armor_pierce":
// Next attack ignores armor
notify_observers("ARMOR_PIERCED", target);
break;
case "stun":
// Target loses next turn
notify_observers("CHARACTER_STUNNED", target);
break;
case "heal_over_time":
// Healing over time
target.heal(15);
notify_observers("HEAL_OVER_TIME", target);
break;
}
}

/**
    * MANDATORY: Distribute rewards after enemy defeat
    * 
    * @param defeatedEnemy The enemy that was defeated
*/
public void distribute_rewards(Enemy defeatedEnemy) {
rewards.clear();

// Award experience
Player player = get_player_from_combatants();
int expReward = defeatedEnemy.get_experience_value();
player.gain_experience(expReward);

// Drop loot
List<Item> loot = defeatedEnemy.drop_loot();
rewards.addAll(loot);

notify_observers("REWARDS_DISTRIBUTED", rewards);
}

/**
    * MANDATORY: Distribute rewards after boss defeat
    * 
    * @param defeatedBoss The boss that was defeated
*/
private void distribute_boss_rewards(Boss defeatedBoss) {
rewards.clear();

// Award massive experience
Player player = get_player_from_combatants();
int expReward = defeatedBoss.get_experience_value();
player.gain_experience(expReward);

// Drop guaranteed rare loot
List<Item> loot = defeatedBoss.drop_loot();
rewards.addAll(loot);

// Add special boss rewards
rewards.add(new Consumable("Boss Elixir", "Full HP/MP restore", 999, "health"));
rewards.add(new model.items.KeyItem("Master Crystal", "Ultimate upgrade material", "any"));

notify_observers("BOSS_REWARDS_DISTRIBUTED", rewards);
}

/**
    * MANDATORY: End current character's turn and switch turns
*/
private void end_character_turn() {
// Switch attacker and target
if (currentAttacker == combatants.get(0)) {
currentAttacker = combatants.get(1);
currentTarget = combatants.get(0);
} else {
currentAttacker = combatants.get(0);
currentTarget = combatants.get(1);
battleRound++; // New round when it comes back to player
}

notify_observers("TURN_SWITCHED", currentAttacker);
}

/**
    * MANDATORY: End combat and clean up
    * 
    * @param playerVictory true if player won the battle
*/
public void end_combat(boolean playerVictory) {
battleActive = false;

// Shutdown enemy AI processing
if (enemyAiExecutor != null \&\& !enemyAiExecutor.isShutdown()) {
enemyAiExecutor.shutdown();
}

if (playerVictory) {
notify_observers("BATTLE_VICTORY", rewards);
} else {
notify_observers("BATTLE_DEFEAT", null);
}

// Clean up battle state
combatants.clear();
rewards.clear();
targetHit = false;
playerHit = false;
battleRound = 0;
}

/**
    * MANDATORY: Get player from combatants list
    * 
    * @return Player character or null if not found
*/
private Player get_player_from_combatants() {
for (Character character : combatants) {
if (character instanceof Player) {
return (Player) character;
}
}
return null;
}

// MANDATORY: Observer pattern implementation
@Override
public void notify_observers(String event, Object data) {
for (GameObserver observer : observers) {
observer.on_model_changed(event, data);
}
}

@Override
public void add_observer(GameObserver observer) {
if (observer != null \&\& !observers.contains(observer)) {
observers.add(observer);
}
}

@Override
public void remove_observer(GameObserver observer) {
observers.remove(observer);
}

// MANDATORY: Getters
public List<Character> get_combatants() { return new ArrayList<>(combatants); }
public boolean is_target_hit() { return targetHit; }
public AttackResult get_attack_result() { return attackResult; }
public List<Item> get_rewards() { return new ArrayList<>(rewards); }
public boolean is_player_hit() { return playerHit; }
public boolean is_battle_active() { return battleActive; }
public int get_battle_round() { return battleRound; }
public Character get_current_attacker() { return currentAttacker; }
public Character get_current_target() { return currentTarget; }

/**
    * MANDATORY: Inner class for attack results
*/
public static class AttackResult {
private final Character attacker;
private final Character target;
private final int damage;
private final boolean targetSurvived;

public AttackResult(Character attacker, Character target, int damage, boolean targetSurvived) {
this.attacker = attacker;
this.target = target;
this.damage = damage;
this.targetSurvived = targetSurvived;
}

public Character get_attacker() { return attacker; }
public Character get_target() { return target; }
public int get_damage() { return damage; }
public boolean target_survived() { return targetSurvived; }
}
}

```

### **MANDATORY**: BattleLogic Requirements

1. **CRITICAL**: BattleLogic class must implement GameModel interface
2. **CRITICAL**: Handle real-time combat with multithreaded enemy AI
3. **CRITICAL**: Implement damage calculations with critical hits and misses
4. **CRITICAL**: Support different AI patterns and boss special abilities
5. **CRITICAL**: Manage rewards distribution and status effects

### **MANDATORY**: Multithreading Implementation

**CRITICAL**: BattleLogic uses:
- **ExecutorService**: Manages enemy AI thread pool
- **CompletableFuture**: Asynchronous enemy AI processing
- **Thread Safety**: Proper synchronization for combat state
- **Performance**: Non-blocking enemy AI with configurable delays

### **MANDATORY**: Verification Steps

1. **CRITICAL**: BattleLogic class compiles successfully in model/gameLogic package
2. **CRITICAL**: Multithreaded enemy AI processes without blocking
3. **CRITICAL**: Damage calculations work with all character classes
4. **CRITICAL**: Status effects and special abilities function properly
5. **CRITICAL**: Combat state management handles all scenarios correctly
6. **CRITICAL**: Observer notifications occur for all battle events

### CRITICAL REQUIREMENT ###
**MANDATORY**: The BattleLogic class must be implemented exactly as specified above. This class handles all combat mechanics and provides the real-time battle experience essential to the Mini Rogue Demo.
```

## **13-gui-view-implementation.md**

```markdown
# CRITICAL REQUIREMENTS - GUI View Implementation

### MANDATORY DIRECTIVE ###
You are an expert Java game developer. **CRITICAL**: Implement the Swing GUI classes exactly as specified below for the Mini Rogue Demo user interface.

## GUI VIEW SPECIFICATIONS

### **CRITICAL**: Create GameView.java in view package with this exact implementation:

```

package view;

import interfaces.GameView;
import interfaces.GameController;
import interfaces.GameObserver;
import enums.GameState;
import model.characters.Player;
import model.map.Map;
import view.panels.GamePanel;
import view.panels.InventoryPanel;
import view.panels.MenuPanel;
import view.panels.BattlePanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**

* Main game view that manages different panel displays and handles user input.
* Implements the MVC View pattern with observer notifications for model changes.
*/
public class GameView extends JFrame implements GameView, GameObserver, KeyListener {

// MANDATORY: View components
private GameController controller;
private GamePanel gamePanel;
private InventoryPanel inventoryPanel;
private MenuPanel menuPanel;
private BattlePanel battlePanel;
private CardLayout cardLayout;
private JPanel mainPanel;

// MANDATORY: Current display state
private GameState currentState;

/**
    * MANDATORY: Constructor for GameView
*/
public GameView() {
this.cardLayout = new CardLayout();
this.mainPanel = new JPanel(cardLayout);
this.currentState = GameState.MAIN_MENU;

initialize_components();
setup_window();
setup_input_handling();
}

/**
    * MANDATORY: Initialize all GUI components
*/
@Override
public void initialize_components() {
// Create panels
menuPanel = new MenuPanel(this);
gamePanel = new GamePanel(this);
inventoryPanel = new InventoryPanel(this);
battlePanel = new BattlePanel(this);

// Add panels to card layout
mainPanel.add(menuPanel, "MENU");
mainPanel.add(gamePanel, "GAME");
mainPanel.add(inventoryPanel, "INVENTORY");
mainPanel.add(battlePanel, "BATTLE");

add(mainPanel);
}

/**
    * MANDATORY: Setup main window properties
*/
private void setup_window() {
setTitle("Mini Rogue Demo");
setSize(enums.GameConstants.WINDOW_WIDTH, enums.GameConstants.WINDOW_HEIGHT);
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
setLocationRelativeTo(null);
setResizable(false);

// Show menu panel initially
cardLayout.show(mainPanel, "MENU");
}

/**
    * MANDATORY: Setup keyboard input handling
*/
private void setup_input_handling() {
addKeyListener(this);
setFocusable(true);
requestFocusInWindow();
}

/**
    * MANDATORY: Update display based on current game state
*/
@Override
public void update_display() {
switch (currentState) {
case MAIN_MENU:
cardLayout.show(mainPanel, "MENU");
menuPanel.update_display();
break;
case CLASS_SELECTION:
cardLayout.show(mainPanel, "MENU");
menuPanel.show_class_selection();
break;
case PLAYING:
cardLayout.show(mainPanel, "GAME");
gamePanel.update_display();
break;
case BATTLE:
cardLayout.show(mainPanel, "BATTLE");
battlePanel.update_display();
break;
case INVENTORY:
cardLayout.show(mainPanel, "INVENTORY");
inventoryPanel.update_display();
break;
case PAUSED:
gamePanel.show_pause_overlay();
break;
case GAME_OVER:
cardLayout.show(mainPanel, "MENU");
menuPanel.show_game_over();
break;
case VICTORY:
cardLayout.show(mainPanel, "MENU");
menuPanel.show_victory();
break;
}

repaint();
requestFocusInWindow();
}

/**
    * MANDATORY: Handle model change notifications
    * 
    * @param event Event type that occurred
    * @param data Data associated with the event
*/
@Override
public void on_model_changed(String event, Object data) {
SwingUtilities.invokeLater(() -> {
switch (event) {
case "GAME_STATE_CHANGED":
currentState = (GameState) data;
update_display();
break;
case "PLAYER_MOVED":
gamePanel.update_player_position((utilities.Position) data);
break;
case "HP_CHANGED":
case "MP_CHANGED":
case "LEVEL_UP":
gamePanel.update_player_stats();
break;
case "INVENTORY_CHANGED":
inventoryPanel.update_inventory();
break;
case "BATTLE_INITIATED":
battlePanel.start_battle(data);
break;
case "BATTLE_ENDED":
update_display();
break;
case "MAP_GENERATED":
gamePanel.set_map((Map) data);
break;
}
});
}

/**
    * MANDATORY: Handle keyboard input for game controls
    * 
    * @param e KeyEvent from user input
*/
@Override
public void keyPressed(KeyEvent e) {
if (controller == null) return;

switch (currentState) {
case PLAYING:
handle_game_input(e);
break;
case BATTLE:
handle_battle_input(e);
break;
case INVENTORY:
handle_inventory_input(e);
break;
case MAIN_MENU:
case CLASS_SELECTION:
case GAME_OVER:
case VICTORY:
handle_menu_input(e);
break;
}
}

/**
    * MANDATORY: Handle input during gameplay
    * 
    * @param e KeyEvent from user input
*/
private void handle_game_input(KeyEvent e) {
String input = "";

switch (e.getKeyCode()) {
case KeyEvent.VK_UP:
case KeyEvent.VK_W:
input = "MOVE_UP";
break;
case KeyEvent.VK_DOWN:
case KeyEvent.VK_S:
input = "MOVE_DOWN";
break;
case KeyEvent.VK_LEFT:
case KeyEvent.VK_A:
input = "MOVE_LEFT";
break;
case KeyEvent.VK_RIGHT:
case KeyEvent.VK_D:
input = "MOVE_RIGHT";
break;
case KeyEvent.VK_SPACE:
input = "ATTACK";
break;
case KeyEvent.VK_I:
input = "OPEN_INVENTORY";
break;
case KeyEvent.VK_ESCAPE:
input = "PAUSE";
break;
}

if (!input.isEmpty()) {
controller.handle_input(input);
}
}

/**
    * MANDATORY: Handle input during battle
    * 
    * @param e KeyEvent from user input
*/
private void handle_battle_input(KeyEvent e) {
String input = "";

switch (e.getKeyCode()) {
case KeyEvent.VK_SPACE:
input = "BATTLE_ATTACK";
break;
case KeyEvent.VK_R:
input = "BATTLE_RUN";
break;
case KeyEvent.VK_I:
input = "BATTLE_ITEM";
break;
}

if (!input.isEmpty()) {
controller.handle_input(input);
}
}

/**
    * MANDATORY: Handle input in inventory screen
    * 
    * @param e KeyEvent from user input
*/
private void handle_inventory_input(KeyEvent e) {
switch (e.getKeyCode()) {
case KeyEvent.VK_ESCAPE:
case KeyEvent.VK_I:
controller.handle_input("CLOSE_INVENTORY");
break;
case KeyEvent.VK_UP:
inventoryPanel.move_selection_up();
break;
case KeyEvent.VK_DOWN:
inventoryPanel.move_selection_down();
break;
case KeyEvent.VK_ENTER:
inventoryPanel.use_selected_item();
break;
}
}

/**
    * MANDATORY: Handle input in menu screens
    * 
    * @param e KeyEvent from user input
*/
private void handle_menu_input(KeyEvent e) {
switch (e.getKeyCode()) {
case KeyEvent.VK_ENTER:
menuPanel.select_current_option();
break;
case KeyEvent.VK_UP:
menuPanel.move_selection_up();
break;
case KeyEvent.VK_DOWN:
menuPanel.move_selection_down();
break;
case KeyEvent.VK_ESCAPE:
if (currentState != GameState.MAIN_MENU) {
controller.handle_input("BACK_TO_MENU");
}
break;
}
}

@Override
public void keyReleased(KeyEvent e) {
// Not used in this implementation
}

@Override
public void keyTyped(KeyEvent e) {
// Not used in this implementation
}

// MANDATORY: Interface implementations
@Override
public GameController get_controller() {
return controller;
}

@Override
public void set_controller(GameController controller) {
this.controller = controller;
}

/**
    * MANDATORY: Get reference to game panel for controller access
    * 
    * @return The GamePanel instance
*/
public GamePanel get_game_panel() {
return gamePanel;
}

/**
    * MANDATORY: Get reference to battle panel for controller access
    * 
    * @return The BattlePanel instance
*/
public BattlePanel get_battle_panel() {
return battlePanel;
}

/**
    * MANDATORY: Show the main window
*/
public void show_window() {
setVisible(true);
}
}

```

### **MANDATORY**: Panel Specifications

**CRITICAL**: Create the following panel classes in view/panels package:

#### **GamePanel.java**

```

package view.panels;

import view.GameView;
import model.map.Map;
import model.characters.Player;
import utilities.Position;
import enums.GameConstants;
import enums.TileType;
import javax.swing.*;
import java.awt.*;

/**

* Panel that displays the game world, player, enemies, and items.
* Renders the 2D pixel art game environment.
*/
public class GamePanel extends JPanel {

private GameView parentView;
private Map currentMap;
private Player player;
private boolean showPauseOverlay;

/**
    * MANDATORY: Constructor for GamePanel
    * 
    * @param parentView The parent GameView
*/
public GamePanel(GameView parentView) {
this.parentView = parentView;
this.showPauseOverlay = false;
setBackground(Color.BLACK);
setPreferredSize(new Dimension(
GameConstants.WINDOW_WIDTH,
GameConstants.WINDOW_HEIGHT - GameConstants.UI_PANEL_HEIGHT
));
}

/**
    * MANDATORY: Set the current map to display
    * 
    * @param map The Map to render
*/
public void set_map(Map map) {
this.currentMap = map;
repaint();
}

/**
    * MANDATORY: Update display components
*/
public void update_display() {
repaint();
}

/**
    * MANDATORY: Update player position on screen
    * 
    * @param position New player position
*/
public void update_player_position(Position position) {
repaint();
}

/**
    * MANDATORY: Update player stats display
*/
public void update_player_stats() {
repaint();
}

/**
    * MANDATORY: Show pause overlay
*/
public void show_pause_overlay() {
showPauseOverlay = true;
repaint();
}

/**
    * MANDATORY: Custom painting method
    * 
    * @param g Graphics context
*/
@Override
protected void paintComponent(Graphics g) {
super.paintComponent(g);
Graphics2D g2d = (Graphics2D) g;

if (currentMap != null) {
render_map(g2d);
render_entities(g2d);
}

render_ui(g2d);

if (showPauseOverlay) {
render_pause_overlay(g2d);
showPauseOverlay = false;
}
}

/**
    * MANDATORY: Render the map tiles
    * 
    * @param g2d Graphics2D context
*/
private void render_map(Graphics2D g2d) {
int tileSize = GameConstants.TILE_SIZE;

for (int x = 0; x < currentMap.get_width(); x++) {
for (int y = 0; y < currentMap.get_height(); y++) {
utilities.Tile tile = currentMap.get_tile(x, y);
if (tile != null \&\& tile.is_explored()) {
render_tile(g2d, tile, x * tileSize, y * tileSize);
}
}
}
}

/**
    * MANDATORY: Render individual tile
    * 
    * @param g2d Graphics2D context
    * @param tile Tile to render
    * @param screenX X coordinate on screen
    * @param screenY Y coordinate on screen
*/
private void render_tile(Graphics2D g2d, utilities.Tile tile, int screenX, int screenY) {
int tileSize = GameConstants.TILE_SIZE;

switch (tile.get_tile_type()) {
case WALL:
g2d.setColor(Color.GRAY);
break;
case FLOOR:
g2d.setColor(Color.LIGHT_GRAY);
break;
case ENTRANCE:
g2d.setColor(Color.GREEN);
break;
case BOSS_ROOM:
g2d.setColor(Color.RED);
break;
default:
g2d.setColor(Color.DARK_GRAY);
}

g2d.fillRect(screenX, screenY, tileSize, tileSize);

// Draw tile border
g2d.setColor(Color.BLACK);
g2d.drawRect(screenX, screenY, tileSize, tileSize);

// Draw items
if (tile.has_items()) {
g2d.setColor(Color.YELLOW);
g2d.fillOval(screenX + 4, screenY + 4, tileSize - 8, tileSize - 8);
}
}

/**
    * MANDATORY: Render characters and entities
    * 
    * @param g2d Graphics2D context
*/
private void render_entities(Graphics2D g2d) {
if (player != null) {
Position playerPos = player.get_position();
int tileSize = GameConstants.TILE_SIZE;

     // Render player
     g2d.setColor(Color.BLUE);
     g2d.fillOval(
         playerPos.get_x() * tileSize + 2,
         playerPos.get_y() * tileSize + 2,
         tileSize - 4,
         tileSize - 4
     );
    }

// Render enemies (would get from controller/model)
// Simplified for demo - actual implementation would iterate through enemies
}

/**
    * MANDATORY: Render UI elements
    * 
    * @param g2d Graphics2D context
*/
private void render_ui(Graphics2D g2d) {
if (player != null) {
g2d.setColor(Color.WHITE);
g2d.setFont(new Font("Arial", Font.BOLD, 14));

     int y = getHeight() - 20;
     g2d.drawString("HP: " + player.get_current_hp() + "/" + player.get_max_hp(), 10, y);
     g2d.drawString("MP: " + player.get_current_mp() + "/" + player.get_max_mp(), 150, y);
     g2d.drawString("Level: " + player.get_level(), 290, y);
     g2d.drawString("Class: " + player.get_character_class().get_class_name(), 400, y);
    }

// Controls help
g2d.setColor(Color.LIGHT_GRAY);
g2d.setFont(new Font("Arial", Font.PLAIN, 10));
g2d.drawString("WASD/Arrows: Move | Space: Attack | I: Inventory | ESC: Pause", 10, getHeight() - 5);
}

/**
    * MANDATORY: Render pause overlay
    * 
    * @param g2d Graphics2D context
*/
private void render_pause_overlay(Graphics2D g2d) {
// Semi-transparent overlay
g2d.setColor(new Color(0, 0, 0, 128));
g2d.fillRect(0, 0, getWidth(), getHeight());

// Pause text
g2d.setColor(Color.WHITE);
g2d.setFont(new Font("Arial", Font.BOLD, 36));
FontMetrics fm = g2d.getFontMetrics();
String pauseText = "PAUSED";
int textX = (getWidth() - fm.stringWidth(pauseText)) / 2;
int textY = getHeight() / 2;
g2d.drawString(pauseText, textX, textY);

// Instructions
g2d.setFont(new Font("Arial", Font.PLAIN, 18));
fm = g2d.getFontMetrics();
String instructText = "Press ESC to resume";
int instructX = (getWidth() - fm.stringWidth(instructText)) / 2;
g2d.drawString(instructText, instructX, textY + 50);
}

/**
    * MANDATORY: Set player reference for rendering
    * 
    * @param player The Player instance
*/
public void set_player(Player player) {
this.player = player;
}
}

```

### **MANDATORY**: GUI Requirements

1. **CRITICAL**: GameView must implement GameView and GameObserver interfaces
2. **CRITICAL**: Use CardLayout for switching between different screens
3. **CRITICAL**: Handle keyboard input for all game controls
4. **CRITICAL**: Update display based on model change notifications
5. **CRITICAL**: Render 2D pixel art style graphics

### **MANDATORY**: Design Principles

**CRITICAL**: GUI system implements:
- **MVC View Pattern**: Clear separation between display and logic
- **Observer Pattern**: Responds to model changes via notifications
- **Component Architecture**: Separate panels for different game screens
- **Event-Driven Input**: Keyboard handling for user interaction

### **MANDATORY**: Verification Steps

1. **CRITICAL**: All GUI classes compile successfully in view packages
2. **CRITICAL**: Window displays with proper dimensions and layout
3. **CRITICAL**: Keyboard input handling works for all game states
4. **CRITICAL**: Panel switching works correctly with CardLayout
5. **CRITICAL**: Model change notifications update display properly
6. **CRITICAL**: 2D rendering displays game elements correctly

### CRITICAL REQUIREMENT ###
**MANDATORY**: The GUI View implementation must be exactly as specified above. This system provides the complete user interface for the Mini Rogue Demo with proper MVC integration and 2D pixel art rendering.
```


## **14-main-controller-integration.md**

```markdown
# CRITICAL REQUIREMENTS - Main Controller Integration

### MANDATORY DIRECTIVE ###
You are an expert Java game developer. **CRITICAL**: Implement the Main class and primary controller exactly as specified below for the Mini Rogue Demo application entry point.

## MAIN CLASS SPECIFICATION

### **CRITICAL**: Create Main.java in controller package with this exact implementation:

```

package controller;

import interfaces.GameController;
import interfaces.GameModel;
import interfaces.GameView;
import enums.CharacterClass;
import enums.GameState;
import enums.Direction;
import model.gameLogic.GameLogic;
import model.gameLogic.BattleLogic;
import model.characters.Player;
import utilities.Position;
import view.GameView;

/**

* Main entry point and primary controller for the Mini Rogue Demo application.
* Coordinates between all MVC components and handles application lifecycle.
*/
public class Main implements GameController {

// MANDATORY: MVC Components
private GameModel gameLogic;
private GameModel battleLogic;
private GameView gameView;
private Player player;

// MANDATORY: Application state
private boolean applicationRunning;

/**
    * MANDATORY: Main method to start the application
    * 
    * @param args Command line arguments (not used)
*/
public static void main(String[] args) {
javax.swing.SwingUtilities.invokeLater(() -> {
new Main();
});
}

/**
    * MANDATORY: Constructor initializes and starts the application
*/
public Main() {
applicationRunning = true;
initialize_application();
start_game();
}

/**
    * MANDATORY: Initialize all application components
*/
private void initialize_application() {
// Create initial player (will be recreated on class selection)
player = new Player("Player", CharacterClass.WARRIOR, new Position(0, 0));

// Create game logic components
gameLogic = new model.gameLogic.GameLogic(player);
battleLogic = new model.gameLogic.BattleLogic();

// Create view and wire MVC components
gameView = new view.GameView();

// Set up MVC relationships
gameView.set_controller(this);
gameLogic.add_observer(gameView);
battleLogic.add_observer(gameView);

System.out.println("Mini Rogue Demo initialized successfully");
}

/**
    * MANDATORY: Start the game application
*/
private void start_game() {
gameView.initialize_components();
gameView.update_display();
gameView.show_window();

System.out.println("Mini Rogue Demo started - Main menu displayed");
}

/**
    * MANDATORY: Handle all user input and delegate to appropriate systems
    * 
    * @param input String representing user input command
*/
@Override
public void handle_input(String input) {
if (!applicationRunning) return;

try {
switch (input.toLowerCase()) {
// Menu navigation
case "start_new_game":
start_new_game();
break;
case "select_warrior":
select_character_class(CharacterClass.WARRIOR);
break;
case "select_mage":
select_character_class(CharacterClass.MAGE);
break;
case "select_rogue":
select_character_class(CharacterClass.ROGUE);
break;
case "exit_application":
exit_application();
break;

         // Movement commands
         case "move_up":
             handle_player_movement(Direction.UP);
             break;
         case "move_down":
             handle_player_movement(Direction.DOWN);
             break;
         case "move_left":
             handle_player_movement(Direction.LEFT);
             break;
         case "move_right":
             handle_player_movement(Direction.RIGHT);
             break;
             
         // Action commands
         case "attack":
             handle_player_action("attack", null);
             break;
         case "open_inventory":
             handle_player_action("open_inventory", null);
             break;
         case "close_inventory":
             handle_player_action("close_inventory", null);
             break;
         case "pause":
             handle_player_action("pause", null);
             break;
             
         // Battle commands
         case "battle_attack":
             handle_battle_action("attack");
             break;
         case "battle_run":
             handle_battle_action("run");
             break;
         case "battle_item":
             handle_battle_action("use_item");
             break;
             
         default:
             System.out.println("Unknown input command: " + input);
     }
    } catch (Exception e) {
System.err.println("Error handling input '" + input + "': " + e.getMessage());
e.printStackTrace();
}
}

/**
    * MANDATORY: Start a new game session
*/
private void start_new_game() {
System.out.println("Starting new game - showing class selection");
gameLogic.handle_player_action("start_new_game", null);
}

/**
    * MANDATORY: Handle character class selection
    * 
    * @param characterClass The selected character class
*/
private void select_character_class(CharacterClass characterClass) {
System.out.println("Character class selected: " + characterClass.get_class_name());

// Create new player with selected class
Position startPosition = new Position(1, 1); // Will be set by GameLogic
player = new Player("Hero", characterClass, startPosition);

// Update GameLogic with new player
gameLogic = new model.gameLogic.GameLogic(player);
gameLogic.add_observer(gameView);

// Set player reference in view
gameView.get_game_panel().set_player(player);

// Start the actual game
gameLogic.handle_player_action("class_selected", characterClass);
}

/**
    * MANDATORY: Handle player movement input
    * 
    * @param direction Direction to move
*/
private void handle_player_movement(Direction direction) {
Position currentPos = player.get_position();
Position newPos = currentPos.move(direction.get_delta_x(), direction.get_delta_y());

gameLogic.handle_player_action("move", newPos);
}

/**
    * MANDATORY: Handle general player actions
    * 
    * @param action Action string
    * @param data Additional action data
*/
private void handle_player_action(String action, Object data) {
gameLogic.handle_player_action(action, data);
}

/**
    * MANDATORY: Handle battle-specific actions
    * 
    * @param action Battle action to perform
*/
private void handle_battle_action(String action) {
model.gameLogic.BattleLogic battle = (model.gameLogic.BattleLogic) battleLogic;

switch (action.toLowerCase()) {
case "attack":
// Get current target from battle logic
if (battle.is_battle_active()) {
model.characters.Character target = battle.get_current_target();
int damage = battle.calculate_damage(player, target);
battle.resolve_attack(player, target, damage);
}
break;
case "run":
// Attempt to flee from battle
battle.end_combat(false);
gameLogic.handle_player_action("battle_fled", null);
break;
case "use_item":
// Open battle inventory for item use
gameLogic.handle_player_action("battle_item_menu", null);
break;
}
}

/**
    * MANDATORY: Handle application shutdown
*/
private void exit_application() {
System.out.println("Exiting Mini Rogue Demo");
applicationRunning = false;
System.exit(0);
}

/**
    * MANDATORY: Handle application errors gracefully
    * 
    * @param error Error message
    * @param exception Exception that occurred
*/
private void handle_application_error(String error, Exception exception) {
System.err.println("Application Error: " + error);
if (exception != null) {
exception.printStackTrace();
}

// Show error dialog
javax.swing.JOptionPane.showMessageDialog(
null,
"An error occurred: " + error + "\nThe application will continue running.",
"Mini Rogue Demo - Error",
javax.swing.JOptionPane.ERROR_MESSAGE
);
}

/**
    * MANDATORY: Get current game model
    * 
    * @return The game logic model
*/
@Override
public GameModel get_model() {
return gameLogic;
}

/**
    * MANDATORY: Set game model (not typically used in this architecture)
    * 
    * @param model The model to set
*/
@Override
public void set_model(GameModel model) {
this.gameLogic = model;
}

/**
    * MANDATORY: Get current view
    * 
    * @return The game view
*/
@Override
public GameView get_view() {
return gameView;
}

/**
    * MANDATORY: Set game view (not typically used in this architecture)
    * 
    * @param view The view to set
*/
@Override
public void set_view(GameView view) {
this.gameView = view;
}

/**
    * MANDATORY: Get reference to battle logic for integration
    * 
    * @return BattleLogic instance
*/
public model.gameLogic.BattleLogic get_battle_logic() {
return (model.gameLogic.BattleLogic) battleLogic;
}

/**
    * MANDATORY: Get reference to player for controller operations
    * 
    * @return Current Player instance
*/
public Player get_player() {
return player;
}

/**
    * MANDATORY: Check if application is running
    * 
    * @return true if application should continue running
*/
public boolean is_running() {
return applicationRunning;
}
}

```

### **MANDATORY**: Integration Requirements

1. **CRITICAL**: Main class must implement GameController interface
2. **CRITICAL**: Coordinate all MVC components (GameLogic, BattleLogic, GameView)
3. **CRITICAL**: Handle complete input routing from view to appropriate models
4. **CRITICAL**: Manage application lifecycle and error handling
5. **CRITICAL**: Support character class selection and game state transitions

### **MANDATORY**: Design Principles

**CRITICAL**: Main controller implements:
- **MVC Controller Pattern**: Central coordination of all components
- **Command Pattern**: Input string processing and action delegation
- **Facade Pattern**: Simplified interface to complex game subsystems
- **Error Handling**: Graceful degradation and user feedback

### **MANDATORY**: Input Handling

**CRITICAL**: Controller processes:
- **Menu Navigation**: Game start, class selection, application exit
- **Movement Commands**: Directional input with position calculation
- **Action Commands**: Attack, inventory, pause game functions
- **Battle Commands**: Combat-specific actions during battles

### **MANDATORY**: Component Integration

**CRITICAL**: Main controller coordinates:
- **GameLogic**: Overall game state and world management
- **BattleLogic**: Combat system and enemy AI processing
- **GameView**: User interface and display management
- **Player**: Character state and progression tracking

### **MANDATORY**: Verification Steps

1. **CRITICAL**: Main class compiles successfully in controller package
2. **CRITICAL**: Application starts and displays main menu
3. **CRITICAL**: Character class selection works correctly
4. **CRITICAL**: All input commands route to appropriate handlers
5. **CRITICAL**: MVC component communication functions properly
6. **CRITICAL**: Error handling prevents application crashes

### CRITICAL REQUIREMENT ###
**MANDATORY**: The Main controller integration must be implemented exactly as specified above. This class serves as the central coordinator for the entire Mini Rogue Demo application and must properly integrate all MVC components.
```


## **15-complete-testing-guide.md**

```markdown
# CRITICAL REQUIREMENTS - Complete Testing and Verification Guide

### MANDATORY DIRECTIVE ###
You are an expert Java game developer. **CRITICAL**: Follow this comprehensive testing guide to verify the complete Mini Rogue Demo implementation.

## TESTING EXECUTION PHASES

### **Phase 1: Compilation Verification**

**CRITICAL**: Execute these compilation tests in order:

```


# Clean and build entire project

gradle clean build

# Verify no compilation errors

gradle compileJava

# Run any existing unit tests

gradle test

# Create executable JAR

gradle jar

```

**MANDATORY**: All commands must succeed without errors.

### **Phase 2: Application Startup Testing**

**CRITICAL**: Verify application launches correctly:

```


# Run application via Gradle

gradle run

# Alternative: Run JAR directly

java -jar build/libs/miniRogueDemo-1.0.0.jar

```

**MANDATORY**: Application must:
1. Start without exceptions
2. Display main menu window
3. Show "Mini Rogue Demo" title
4. Accept keyboard input
5. Window size: 1024x768 pixels

### **Phase 3: Core Functionality Testing**

#### **3.1 Main Menu Testing**

**CRITICAL**: Test menu navigation:
- Press UP/DOWN arrows to navigate menu options
- Press ENTER to select "Start New Game"
- Verify class selection screen appears
- Test ESCAPE to return to main menu

**MANDATORY**: Menu must be responsive and display correctly.

#### **3.2 Character Class Selection**

**CRITICAL**: Test each character class:
1. Select WARRIOR class - verify high HP, moderate ATK
2. Select MAGE class - verify moderate HP, high MP
3. Select ROGUE class - verify balanced stats
4. Each class starts with appropriate equipment

**MANDATORY**: Class selection must update player stats correctly.

#### **3.3 Map Generation and Display**

**CRITICAL**: Verify procedural generation:
- New game generates unique dungeon layout
- Map displays with tile-based graphics
- Player appears at entrance (green tile)
- Walls (gray), floors (light gray) render correctly
- Boss room (red tile) appears on map

**MANDATORY**: Each game run produces different layouts.

#### **3.4 Player Movement System**

**CRITICAL**: Test movement controls:
- WASD keys move player in cardinal directions
- Arrow keys also work for movement
- Player cannot move through walls
- Player position updates on screen
- Movement is smooth and responsive

**MANDATORY**: Movement must respect collision detection.

#### **3.5 Inventory and Items**

**CRITICAL**: Test inventory functionality:
- Press 'I' to open inventory screen
- Inventory displays collected items
- Items can be selected and used
- Health potions restore HP when used
- Mana potions restore MP when used
- ESC closes inventory screen

**MANDATORY**: Inventory operations must work without errors.

### **Phase 4: Combat System Testing**

#### **4.1 Enemy Encounters**

**CRITICAL**: Test combat initiation:
- Move player onto tile with enemy
- Battle screen appears automatically
- Enemy and player stats display
- Battle interface shows options

**MANDATORY**: Combat must trigger when appropriate.

#### **4.2 Battle Mechanics**

**CRITICAL**: Test combat actions:
- SPACE key attacks enemy
- Damage calculation includes equipment bonuses
- Critical hits occur based on character class
- HP reduction displays correctly
- Enemy AI responds with counterattacks

**MANDATORY**: Combat calculations must be accurate.

#### **4.3 Experience and Leveling**

**CRITICAL**: Test progression system:
- Defeating enemies grants experience points
- Experience accumulation triggers level ups
- Level up increases HP, ATK, MP stats
- Stat increases vary by character class
- Level progression displays correctly

**MANDATORY**: Character progression must function properly.

### **Phase 5: Advanced Feature Testing**

#### **5.1 Boss Battles**

**CRITICAL**: Test boss encounters:
- Boss appears in boss room (red tile)
- Boss has significantly higher stats
- Boss uses special abilities during combat
- Special abilities activate at 50% HP
- Boss defeat unlocks next floor

**MANDATORY**: Boss battles must be more challenging than regular enemies.

#### **5.2 Equipment Upgrade System**

**CRITICAL**: Test upgrade mechanics:
- Collect key items during exploration
- Upgrade weapons and armor using key items
- Equipment stats increase after upgrades
- Upgraded equipment shows visual indicator
- Upgrade levels cap at maximum (5)

**MANDATORY**: Upgrade system must enhance equipment effectively.

#### **5.3 Game State Management**

**CRITICAL**: Test state transitions:
- Pause game with ESC key during gameplay
- Game over triggers when HP reaches 0
- Victory condition when final boss defeated
- Permadeath restarts with new layout
- State transitions work smoothly

**MANDATORY**: All game states must transition correctly.

### **Phase 6: Performance and Quality Testing**

#### **6.1 Performance Verification**

**CRITICAL**: Measure performance metrics:
- Game maintains minimum 30 FPS during gameplay
- No frame drops during combat or movement
- Memory usage remains stable during extended play
- Enemy AI threads don't block main game loop
- Map generation completes within 2 seconds

**MANDATORY**: Performance must meet specified requirements.

#### **6.2 Multithreading Testing**

**CRITICAL**: Verify concurrent operations:
- Enemy AI processes in background threads
- UI remains responsive during AI processing
- No thread synchronization issues
- No deadlocks or race conditions
- Clean thread shutdown on application exit

**MANDATORY**: Multithreading must not cause instability.

#### **6.3 Error Handling Testing**

**CRITICAL**: Test error scenarios:
- Invalid movement attempts (into walls)
- Using items when inventory empty
- Battle actions with no valid targets
- Loading corrupted save data (if applicable)
- Memory exhaustion scenarios

**MANDATORY**: Application must handle errors gracefully.

### **Phase 7: Integration Testing**

#### **7.1 MVC Architecture Verification**

**CRITICAL**: Verify MVC pattern implementation:
- Model changes notify all registered observers
- View updates reflect model state changes
- Controller properly delegates user input
- No direct model-view coupling exists
- Observer pattern functions correctly

**MANDATORY**: MVC separation must be maintained throughout.

#### **7.2 Package Structure Verification**

**CRITICAL**: Verify project organization:

```

src/main/java/
├── controller/
│   └── Main.java
├── model/
│   ├── characters/
│   ├── items/
│   ├── equipment/
│   ├── map/
│   └── gameLogic/
├── view/
│   ├── GameView.java
│   └── panels/
├── interfaces/
├── enums/
└── utilities/

```

**MANDATORY**: All classes must be in correct packages.

### **Phase 8: User Acceptance Testing**

#### **8.1 Gameplay Experience**

**CRITICAL**: Complete gameplay verification:
1. Start new game and select character class
2. Explore generated dungeon completely
3. Collect items and manage inventory
4. Engage in multiple enemy combats
5. Level up character through experience
6. Upgrade equipment using key items
7. Face and defeat floor boss
8. Progress to next dungeon floor
9. Experience permadeath and restart

**MANDATORY**: Complete gameplay loop must be functional.

#### **8.2 Replayability Testing**

**CRITICAL**: Verify unique experiences:
- Each new game generates different layouts
- Enemy placement varies between runs
- Item distribution changes each playthrough
- Boss encounters remain challenging
- Player choices affect gameplay outcomes

**MANDATORY**: Procedural generation must ensure replayability.

### **MANDATORY**: Success Criteria Checklist

**CRITICAL**: Application passes testing when ALL criteria met:

✓ Compiles without errors or warnings
✓ Starts successfully and displays main menu
✓ Character class selection works correctly
✓ Procedural map generation creates unique layouts
✓ Player movement respects collision detection
✓ Combat system calculates damage correctly
✓ Experience and leveling function properly
✓ Inventory management works without errors
✓ Equipment upgrade system enhances items
✓ Boss battles provide appropriate challenge
✓ Game states transition smoothly
✓ Performance meets minimum requirements
✓ Multithreading operates without issues
✓ Error handling prevents crashes
✓ MVC architecture maintains separation
✓ Complete gameplay loop is functional
✓ Replayability through procedural generation

### **MANDATORY**: Final Verification

**CRITICAL**: Before considering project complete:

1. Run application for extended period (30+ minutes)
2. Complete multiple full gameplay sessions
3. Test all character classes thoroughly
4. Verify no memory leaks or performance degradation
5. Confirm all features work as specified in SRS
6. Document any known issues or limitations
7. Package application for distribution

### CRITICAL REQUIREMENT ###
**MANDATORY**: The Mini Rogue Demo must pass ALL testing phases successfully. Any failures must be resolved before the project can be considered complete. This comprehensive testing ensures a stable, functional roguelike game that meets all specified requirements.
```