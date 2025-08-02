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