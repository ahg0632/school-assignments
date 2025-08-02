package controller;

import interfaces.GameController;
import interfaces.GameModel;
import interfaces.GameView;
import enums.CharacterClass;
import enums.GameState;
import enums.Direction;
import model.gameLogic.GameLogic;
import model.characters.Player;
import utilities.Position;
import model.scoreEntry.ScoreEntry;
import view.ScoreEntryDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.SwingUtilities;

/**
* Main entry point and primary controller for the Mini Rogue Demo application.
* Coordinates between all MVC components and handles application lifecycle.
*/
public class Main implements GameController {

// MANDATORY: MVC Components
private GameModel gameLogic;
private interfaces.GameView gameView;
private Player player;
private MainController mainController;

// MANDATORY: Application state
private boolean applicationRunning;

// High score list (in-memory, top 3)
private List<ScoreEntry> highScores = new ArrayList<>();

/**
    * MANDATORY: Main method to start the application
    * 
    * @param args Command line arguments (not used)
*/
public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        try {
            new Main(); // Constructor handles initialization
        } catch (Exception e) {
            System.err.println("Application Error: " + e.getMessage());
            e.printStackTrace();
        }
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

// Create view and wire MVC components
gameView = new view.GameView();

        // Create main controller for proper MVC architecture
        mainController = new MainController((model.gameLogic.GameLogic)gameLogic, (view.GameView)gameView);

// Set up MVC relationships
gameView.set_controller(this);
gameLogic.add_observer((interfaces.GameObserver)gameView);

// Initialize the main controller
mainController.initialize();
}

/**
    * MANDATORY: Start the game application
*/
private void start_game() {
    // gameView.initialize_components(); // Removed to prevent double instantiation
    gameView.update_display();
    gameView.show_window();
}

/**
    * MANDATORY: Handle all user input and delegate to appropriate systems
    * 
    * @param input String representing user input command
*/
@Override
public void handle_input(String input) {
if (!applicationRunning) return;

// Handle null or empty input gracefully
if (input == null || input.trim().isEmpty()) {
    return; // Silently ignore null/empty input
}

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
case "select_ranger":
select_character_class(CharacterClass.RANGER);
break;
case "exit_application":
exit_application();
break;
case "back_to_menu":
((model.gameLogic.GameLogic)gameLogic).back_to_main_menu();
break;
case "pause_game":
// Pause game when ESC is pressed
((model.gameLogic.GameLogic)gameLogic).pause_game();
break;
case "resume_game":
// Resume game from pause menu
((model.gameLogic.GameLogic)gameLogic).resume_game();
break;
             
         default:
 
     }
    } catch (Exception e) {
System.err.println("Error handling input '" + input + "': " + e.getMessage());
e.printStackTrace();
}
}

/**
    * MANDATORY: Handle all user input and delegate to appropriate systems
    * 
    * @param input String representing user input command
    * @param data Additional data for the input
*/
@Override
public void handle_input(String input, Object data) {
    if (!applicationRunning) return;
    
    // Handle null or empty input gracefully
    if (input == null || input.trim().isEmpty()) {
        return; // Silently ignore null/empty input
    }
    
    try {
        switch (input.toLowerCase()) {
            case "use_item":
                handle_player_action("use_item", data);
                break;
            // Add more cases as needed for other actions with data
            default:
                handle_input(input); // Fallback to original
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
((model.gameLogic.GameLogic)gameLogic).handle_player_action("start_new_game", null);
}

/**
    * MANDATORY: Handle character class selection
    * 
    * @param characterClass The selected character class
*/
private void select_character_class(CharacterClass characterClass) {
// MEMORY LEAK PROTECTION: Clean up old GameLogic before creating new one
    if (gameLogic != null) {
        ((model.gameLogic.GameLogic) gameLogic).dispose();
    }

    // Clean up old player reference
    if (player != null) {
        player.setGameLogic(null);
    }

    // Create new player with selected class
    Position startPosition = new Position(1, 1); // Will be set by GameLogic
    player = new Player("Hero", characterClass, startPosition);

    // Create new GameLogic with new player
    gameLogic = new model.gameLogic.GameLogic(player);
    gameLogic.add_observer((interfaces.GameObserver)gameView);

    // Set player reference in view
    gameView.get_game_panel().set_player(player);

    // Start the actual game
    ((model.gameLogic.GameLogic)gameLogic).handle_player_action("class_selected", characterClass);
}

/**
    * MANDATORY: Handle general player actions
    * 
    * @param action Action string
    * @param data Additional action data
*/
private void handle_player_action(String action, Object data) {
((model.gameLogic.GameLogic)gameLogic).handle_player_action(action, data);
}

/**
    * MANDATORY: Handle application shutdown
*/
private void exit_application() {
// Clean up resources before application shutdown
if (gameLogic != null) {
    ((model.gameLogic.GameLogic) gameLogic).dispose();
}

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

/**
 * Call this method when the game ends (death or victory)
 * @param victory true if player won, false if died
 * @param killer name of enemy that killed the player (null if victory)
 */
public void end_game(boolean victory, String killer) {
    // Gather score data
    int expLevel = player.get_level();
    int tempEnemiesSlain = 0;
    List<String> tempItemsCollected = new ArrayList<>();
    try {
        tempEnemiesSlain = player.get_enemies_slain();
    } catch (Exception e) { /* fallback to 0 */ }
    try {
        tempItemsCollected = player.get_collected_items();
    } catch (Exception e) { /* fallback to empty */ }
    final int enemiesSlain = tempEnemiesSlain;
    final List<String> itemsCollected = tempItemsCollected;
    String killerName = victory ? null : killer;

    // Prepare ScoreEntry
    ScoreEntry currentScore = new ScoreEntry("---", expLevel, enemiesSlain, killerName, itemsCollected);
    // Show scoreboard panel in GameView
    String className = player.get_character_class().toString();
    ((view.GameView)gameView).showScoreboard(currentScore, highScores, new view.panels.ScoreboardPanel.ScoreboardListener() {
        @Override
        public void onPlayAgain(String initials) {
            ScoreEntry retryScore = new ScoreEntry(initials, expLevel, enemiesSlain, killerName, itemsCollected);
            addHighScore(retryScore);
            // Play Again: re-initialize game as if class was selected from menu
            select_character_class(player.get_character_class());
        }
        @Override
        public void onReturnToMenu(String initials) {
            ScoreEntry quitScore = new ScoreEntry(initials, expLevel, enemiesSlain, killerName, itemsCollected);
            addHighScore(quitScore);
            // Use the same logic as pause menu Quit
            handle_input("BACK_TO_MENU");
        }
        // For backward compatibility with old method names
        public void onRetry(String initials) { onPlayAgain(initials); }
        public void onQuit(String initials) { onReturnToMenu(initials); }
    }, className);
}

private void addHighScore(ScoreEntry entry) {
    highScores.add(entry);
    // Sort descending by exp level, then enemies slain
    Collections.sort(highScores, Comparator.comparingInt(ScoreEntry::getFinalExpLevel).reversed()
            .thenComparingInt(ScoreEntry::getEnemiesSlain).reversed());
    // Keep only top 3
    if (highScores.size() > 3) {
        highScores = new ArrayList<>(highScores.subList(0, 3));
    }
}

    /**
     * Debug mode accessors for controller (optional, for extensibility)
     */
    public void set_debug_mode(boolean debugMode) {
        gameView.get_game_panel().set_debug_mode(debugMode);
    }
    public boolean is_debug_mode() {
        return gameView.get_game_panel().is_debug_mode();
    }
} 