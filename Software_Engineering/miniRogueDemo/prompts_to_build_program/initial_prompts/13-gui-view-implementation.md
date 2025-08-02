## 13-gui-view-implementation.md

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
