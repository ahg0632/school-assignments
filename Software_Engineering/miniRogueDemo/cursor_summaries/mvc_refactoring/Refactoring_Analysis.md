# Refactoring Analysis - miniRogueDemo Project

## Executive Summary

This analysis examines the miniRogueDemo project for refactoring opportunities focusing on **duplicate code**, **unused methods/classes**, and **large blocks of commented code**. The project contains significant opportunities for cleanup that would improve maintainability and reduce technical debt.

## 1. Duplicate Code Analysis

### **High Priority Duplicates**

#### **1. Sprite Loading Patterns**
**Location**: `GamePanel.java` (lines 495-615)
**Problem**: Four nearly identical sprite loading methods
```java
// DUPLICATE: All four methods follow identical pattern
private void loadWarriorSprites() { ... }
private void loadRogueSprites() { ... }
private void loadRangerSprites() { ... }
private void loadMageSprites() { ... }
```

**Solution**: Create generic sprite loader
```java
private void loadClassSprites(String className, String sheetPath) {
    // Generic sprite loading logic
}
```

**Impact**: Reduce ~120 lines to ~30 lines

#### **2. Equipment Modifier Calculations**
**Location**: `Character.java` (lines 201-229)
**Problem**: Repeated equipment modifier calculation pattern
```java
// DUPLICATE: Same pattern repeated for each stat
public float getEquipmentAttackModifier() {
    float modifier = 0.0f;
    if (equippedWeapon != null) {
        Float weaponModifier = equippedWeapon.get_stat_modifiers().get("Attack");
        if (weaponModifier != null) modifier += weaponModifier;
    }
    if (equippedArmor != null) {
        Float armorModifier = equippedArmor.get_stat_modifiers().get("Attack");
        if (armorModifier != null) modifier += armorModifier;
    }
    return modifier;
}
```

**Solution**: Create generic modifier calculator
```java
public float getEquipmentModifier(String statName) {
    float modifier = 0.0f;
    if (equippedWeapon != null) {
        Float weaponModifier = equippedWeapon.get_stat_modifiers().get(statName);
        if (weaponModifier != null) modifier += weaponModifier;
    }
    if (equippedArmor != null) {
        Float armorModifier = equippedArmor.get_stat_modifiers().get(statName);
        if (armorModifier != null) modifier += armorModifier;
    }
    return modifier;
}
```

**Impact**: Reduce ~30 lines to ~15 lines

#### **3. Mouse Aiming Logic**
**Location**: `GamePanel.java` (lines 309-320, 354-375)
**Problem**: Identical mouse aiming calculation repeated
```java
// DUPLICATE: Same calculation in multiple places
Point mouse = e.getPoint();
float px = player.getPixelX() + enums.GameConstants.TILE_SIZE / 2f;
float py = player.getPixelY() + enums.GameConstants.TILE_SIZE / 2f;
float dx = mouse.x - px;
float dy = mouse.y - py;
int aimDX = (Math.abs(dx) > Math.abs(dy)) ? (dx > 0 ? 1 : -1) : 0;
int aimDY = (Math.abs(dy) >= Math.abs(dx)) ? (dy > 0 ? 1 : -1) : 0;
```

**Solution**: Extract to utility method
```java
private Point calculateAimDirection(Point mouse, Player player) {
    float px = player.getPixelX() + enums.GameConstants.TILE_SIZE / 2f;
    float py = player.getPixelY() + enums.GameConstants.TILE_SIZE / 2f;
    float dx = mouse.x - px;
    float dy = mouse.y - py;
    int aimDX = (Math.abs(dx) > Math.abs(dy)) ? (dx > 0 ? 1 : -1) : 0;
    int aimDY = (Math.abs(dy) >= Math.abs(dx)) ? (dy > 0 ? 1 : -1) : 0;
    return new Point(aimDX, aimDY);
}
```

**Impact**: Reduce ~20 lines to ~10 lines

### **Medium Priority Duplicates**

#### **4. Panel Navigation State Management**
**Location**: `GameView.java` (lines 55-68)
**Problem**: Similar navigation state variables across panels
```java
// DUPLICATE: Similar pattern in multiple panels
private boolean inventoryNavigationMode = false;
private int inventoryNavRow = 0;
private int inventoryNavCol = 0;
private boolean statsNavigationMode = false;
private int statsNavRow = 0;
private int statsNavCol = 0;
private boolean equipmentNavigationMode = false;
private int equipmentNavIndex = 0;
private int equipmentNavRow = 0;
private int equipmentNavCol = 0;
```

**Solution**: Create NavigationState class
```java
public class NavigationState {
    private boolean active = false;
    private int row = 0;
    private int col = 0;
    private int index = 0;
    // ... getters and setters
}
```

**Impact**: Reduce ~15 lines to ~5 lines per panel

#### **5. Resource Bar Drawing**
**Location**: `GamePanel.java` (lines 2934-2982)
**Problem**: Similar bar drawing logic for different resources
```java
// DUPLICATE: Similar bar drawing patterns
private void drawResourceBar(Graphics2D g2d, int x, int y, int width, int height, 
                            int current, int max, Color fillColor) { ... }
private void drawClarityBar(Graphics2D g2d, int x, int y, int width, int height, 
                           int current, int max, Color fillColor) { ... }
```

**Solution**: Consolidate into single method
```java
private void drawProgressBar(Graphics2D g2d, int x, int y, int width, int height,
                           int current, int max, Color fillColor, Color borderColor) { ... }
```

**Impact**: Reduce ~50 lines to ~25 lines

---

## 2. Unused Code Analysis

### **Unused Methods**

#### **1. Debug Methods**
**Location**: `GameView.java` (lines 605-679)
**Problem**: Debug methods that are only used in debug mode
```java
// UNUSED: Only called in debug mode
if (debugMode && currentPlayer != null) {
    currentPlayer.gain_experience(1000);
    currentPlayer.collect_item(new model.items.Consumable("Lamp", 10, "clarity"));
    // ... more debug code
}
```

**Solution**: Move to separate DebugManager class or remove entirely

#### **2. Legacy Panel Methods**
**Location**: `InventoryPanel.java` (entire file)
**Problem**: Old inventory panel replaced by SideInventoryPanel
```java
// UNUSED: Replaced by SideInventoryPanel
public class InventoryPanel extends JPanel {
    // ... entire class is unused
}
```

**Solution**: Remove entire file

#### **3. Unused Imports**
**Location**: Multiple files
**Problem**: Unused imports throughout codebase
```java
// UNUSED: Various unused imports
import java.util.Iterator; // Not used
import java.awt.Point; // Used but could be optimized
```

**Solution**: Remove unused imports

### **Unused Classes**

#### **1. BattlePanel.java**
**Location**: `src/main/java/view/panels/BattlePanel.java`
**Problem**: Minimal implementation, barely used
```java
// UNUSED: Only displays "Battle!" text
public class BattlePanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawString("Battle!", 10, 20);
    }
}
```

**Solution**: Remove or enhance functionality

#### **2. SideStatsPanel.java**
**Location**: `src/main/java/view/panels/SideStatsPanel.java`
**Problem**: Functionality moved inline to GamePanel
```java
// UNUSED: Stats now rendered inline in GamePanel
public class SideStatsPanel extends JPanel {
    // ... functionality moved to GamePanel
}
```

**Solution**: Remove entire file

---

## 3. Large Blocks of Commented Code

### **High Priority Cleanup**

#### **1. GamePanel.java - Mouse Aiming Comments**
**Location**: `GamePanel.java` (lines 309-320)
**Problem**: Large block of commented mouse aiming code
```java
// LARGE COMMENTED BLOCK: ~10 lines of commented code
// float px = player.getPixelX() + enums.GameConstants.TILE_SIZE / 2f;
// float py = player.getPixelY() + enums.GameConstants.TILE_SIZE / 2f;
// float dx = mouse.x - px;
// float dy = mouse.y - py;
// // Normalize to -1, 0, or 1 for keyboard compatibility
// int aimDX = (Math.abs(dx) > Math.abs(dy)) ? (dx > 0 ? 1 : -1) : 0;
// int aimDY = (Math.abs(dy) >= Math.abs(dx)) ? (dy > 0 ? 1 : -1) : 0;
```

**Solution**: Remove commented code

#### **2. GamePanel.java - Sprite Loading Comments**
**Location**: `GamePanel.java` (lines 500-520)
**Problem**: Extensive commented sprite loading documentation
```java
// LARGE COMMENTED BLOCK: ~20 lines of sprite documentation
// Load the 60x42 warrior sprite sheet
// Extract 15x21 sprites based on grid layout
// Column 0 (x=0): Down sprites
// warriorWalkDown1 = warriorSheet.getSubimage(0, 0, 15, 21);    // Top-left
// warriorWalkDown2 = warriorSheet.getSubimage(0, 21, 15, 21);   // Bottom-left
// ... more commented documentation
```

**Solution**: Move to separate documentation file or reduce to essential comments

#### **3. ScoreEntryDialog.java - Layout Comments**
**Location**: `ScoreEntryDialog.java` (lines 29-98)
**Problem**: Extensive commented layout code
```java
// LARGE COMMENTED BLOCK: ~15 lines of layout comments
// setPreferredSize(new Dimension(400, 280)); // Remove fixed size
// Score panel
// Removed scroll area, add scorePanel directly
// Initials entry
// High score panel
// Buttons
// Layout
// Button actions
```

**Solution**: Remove redundant comments, keep only essential ones

---

## 4. Comprehensive Refactoring Plan

### **Phase 1: Quick Wins (1-2 days)**

#### **1. Remove Unused Code**
- **Files to Delete:**
  - `src/main/java/view/panels/InventoryPanel.java` (204 lines)
  - `src/main/java/view/panels/SideStatsPanel.java` (198 lines)
  - `src/main/java/view/panels/BattlePanel.java` (24 lines)
- **Total Reduction:** ~426 lines

#### **2. Remove Commented Code**
- **Files to Clean:**
  - `GamePanel.java` (~50 lines of commented code)
  - `ScoreEntryDialog.java` (~20 lines of commented code)
  - `SideInventoryPanel.java` (~30 lines of commented code)
- **Total Reduction:** ~100 lines

#### **3. Remove Unused Imports**
- **Files to Clean:** All Java files
- **Estimated Reduction:** ~50 lines

### **Phase 2: Duplicate Code Consolidation (2-3 days)**

#### **1. Sprite Loading Refactor**
- **Files Modified:** `GamePanel.java`
- **Changes:** Consolidate 4 sprite loading methods into 1 generic method
- **Reduction:** ~90 lines

#### **2. Equipment Modifier Refactor**
- **Files Modified:** `Character.java`
- **Changes:** Create generic modifier calculator
- **Reduction:** ~15 lines

#### **3. Mouse Aiming Refactor**
- **Files Modified:** `GamePanel.java`
- **Changes:** Extract mouse aiming logic to utility method
- **Reduction:** ~10 lines

### **Phase 3: Advanced Refactoring (3-4 days)**

#### **1. Navigation State Management**
- **Files Modified:** `GameView.java`, all panel classes
- **Changes:** Create NavigationState class
- **Reduction:** ~30 lines

#### **2. Resource Bar Consolidation**
- **Files Modified:** `GamePanel.java`
- **Changes:** Consolidate bar drawing methods
- **Reduction:** ~25 lines

#### **3. Debug Code Cleanup**
- **Files Modified:** `GameView.java`, `Main.java`
- **Changes:** Move debug code to separate class or remove
- **Reduction:** ~50 lines

---

## 5. Impact Assessment

### **Total Estimated Reductions:**
- **Lines Removed:** ~796 lines
- **Files Deleted:** 3 files
- **Files Modified:** 8 files
- **New Files:** 1 utility class

### **Benefits:**
1. **Improved Maintainability:** Less code to maintain
2. **Better Readability:** Removed clutter and duplicates
3. **Reduced Technical Debt:** Cleaner codebase
4. **Faster Compilation:** Fewer files and lines
5. **Easier Testing:** Less duplicate logic to test

### **Risks:**
1. **Low Risk:** Removing unused code and comments
2. **Medium Risk:** Consolidating duplicate code (requires testing)
3. **High Risk:** Advanced refactoring (requires careful testing)

---

## 6. Implementation Strategy

### **Recommended Order:**
1. **Phase 1** (Remove unused code) - Immediate benefits, no risk
2. **Phase 2** (Consolidate duplicates) - High impact, medium risk
3. **Phase 3** (Advanced refactoring) - Long-term benefits, higher risk

### **Testing Strategy:**
- **Unit Tests:** Test consolidated methods thoroughly
- **Integration Tests:** Verify UI functionality after refactoring
- **Manual Testing:** Test all game features after each phase

### **Success Metrics:**
- Reduced codebase size by ~800 lines
- Eliminated 3 unused files
- Improved code maintainability
- Maintained all functionality

This refactoring analysis shows significant opportunities for cleanup that would improve the codebase quality while maintaining all existing functionality. The recommended approach prioritizes low-risk, high-impact changes suitable for a school project context. 