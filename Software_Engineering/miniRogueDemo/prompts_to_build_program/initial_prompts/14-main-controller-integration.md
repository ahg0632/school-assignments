## 14-main-controller-integration.md

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