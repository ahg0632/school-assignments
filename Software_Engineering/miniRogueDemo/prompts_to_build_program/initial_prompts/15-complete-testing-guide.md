## 15-complete-testing-guide.md

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