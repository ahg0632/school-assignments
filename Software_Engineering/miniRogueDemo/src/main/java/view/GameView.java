package view;

import interfaces.GameController;
import interfaces.GameObserver;
import enums.GameState;
import model.characters.Player;
import model.map.Map;
import view.panels.GamePanel;
import view.panels.MenuPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;
import java.awt.event.ActionEvent;
import java.util.List;
import view.panels.SideInventoryPanel;
import view.panels.EquipmentPanel;
import view.panels.ScrapPanel;
import view.panels.ScoreboardPanel;

/**
* Main game view that manages different panel displays and handles user input.
* Implements the MVC View pattern with observer notifications for model changes.
*/
public class GameView extends JFrame implements interfaces.GameView, GameObserver, KeyListener {

// MANDATORY: View components
private GameController controller;
private GamePanel gamePanel;
private MenuPanel menuPanel;
private CardLayout cardLayout;
private JPanel mainPanel;
private JPanel gameplayPanel; // New: holds gamePanel (center) and rightPanel (east)
    private SideInventoryPanel sideInventoryPanel; // Always-on inventory panel
    private EquipmentPanel equipmentPanel; // Equipment management panel
    private ScrapPanel scrapPanel; // Scrap display panel
    private ScoreboardPanel scoreboardPanel;

// MANDATORY: Current display state
private GameState currentState;

// MANDATORY: Player reference for inventory panel
private Player currentPlayer;

private boolean debugMode = false; // Debug is OFF by default
private Font pixelFont;
    private boolean inventoryNavigationMode = false;
    private int inventoryNavRow = 0;
    private int inventoryNavCol = 0;
    
    // Stats panel navigation mode
    private boolean statsNavigationMode = false;
    private int statsNavRow = 0;
    private int statsNavCol = 0;
    
    // Equipment panel navigation mode
    private boolean equipmentNavigationMode = false;
    private int equipmentNavIndex = 0;
    private int equipmentNavRow = 0;
    private int equipmentNavCol = 0;
private boolean mouseAimingMode = false;
private boolean mouseDetected = false;
private Point lastMousePosition = new Point(0,0);

/**
    * MANDATORY: Constructor for GameView
*/
public GameView() {
this.cardLayout = new CardLayout();
this.mainPanel = new JPanel(cardLayout);
this.currentState = GameState.MAIN_MENU;
// Load pixel font
try {
    java.io.InputStream is = getClass().getClassLoader().getResourceAsStream("fonts/PressStart2P-Regular.ttf");
    if (is != null) {
        pixelFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(16f);
    } else {
        pixelFont = new Font("Monospaced", Font.BOLD, 16);
    }
} catch (Exception e) {
    pixelFont = new Font("Monospaced", Font.BOLD, 16);
}
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

    // Original layout: gamePanel in center, sideInventoryPanel on right
    gameplayPanel = new JPanel(new BorderLayout());
    gamePanel.setPreferredSize(new Dimension(enums.GameConstants.WINDOW_WIDTH - 250, enums.GameConstants.WINDOW_HEIGHT)); // Reduced from 280 to 250 to match smaller window
    gameplayPanel.add(gamePanel, BorderLayout.CENTER);
    sideInventoryPanel = new SideInventoryPanel(this);
    
    // Create equipment and scrap panels (always present, like inventory panel)
    equipmentPanel = new EquipmentPanel(this, null); // Will be updated when player is set
    scrapPanel = new ScrapPanel(null); // Will be updated when player is set
    
    // Create a right panel to hold debug stats, equipment panel, scrap panel, and inventory
    JPanel rightPanel = new JPanel();
    rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
    rightPanel.setPreferredSize(new Dimension(250, 768)); // Reduced from 280 to 250 to match smaller window
    rightPanel.setBackground(Color.BLACK); // Set black background for entire panel
    rightPanel.setOpaque(true);
    rightPanel.setBorder(BorderFactory.createEmptyBorder(80, 0, 20, 0)); // Increased from 40px to 80px to push equipment panel up
    
    // Add debug stats panel at the top
    // debugStatsPanel = new DebugStatsPanel(this); // Removed
    // rightPanel.add(null, BorderLayout.NORTH); // Placeholder for debug stats panel
    
    // Add equipment panel
    rightPanel.add(equipmentPanel);
    rightPanel.add(Box.createVerticalStrut(10)); // Small gap between equipment and inventory
    
    // Add inventory panel
    rightPanel.add(sideInventoryPanel);
    
    // Add game panel and right panel to gameplay panel
    gameplayPanel.add(gamePanel, BorderLayout.CENTER);
    gameplayPanel.add(rightPanel, BorderLayout.EAST);

    // Add panels to card layout
    mainPanel.add(menuPanel, "MENU");
    mainPanel.add(gameplayPanel, "GAME");
    // Add scoreboard panel placeholder
    scoreboardPanel = null;

    add(mainPanel);

    // Wire up inventory selection change for info panel (from side panel)
    sideInventoryPanel.setSelectionListener((item) -> {
        // No-op for now
    });
    // Detect if mouse is present
    mouseDetected = java.awt.MouseInfo.getNumberOfButtons() > 0;
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
requestFocusInWindow();
break;
case CLASS_SELECTION:
cardLayout.show(mainPanel, "MENU");
menuPanel.show_class_selection();
// Reset floor number for new game
gamePanel.resetFloorNumber();
break;
case PLAYING:
cardLayout.show(mainPanel, "GAME");
gamePanel.update_display();
break;
case PAUSED:
gamePanel.show_pause_overlay();
mainPanel.requestFocusInWindow();
gamePanel.repaint();
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
if (currentState == enums.GameState.MAIN_MENU) {
menuPanel.show_main_menu();
// Reset scrap mode when returning to main menu
if (equipmentPanel != null) {
    equipmentPanel.exitScrapMode();
}
}
if (currentState == enums.GameState.PLAYING || currentState == enums.GameState.INVENTORY) {
    // Try to get player from model if possible
    try {
        Player newPlayer = ((model.gameLogic.GameLogic)controller.get_model()).get_player();
        if (newPlayer != currentPlayer) {
            currentPlayer = newPlayer;
            // Update panels with new player
            if (currentPlayer != null) {
                sideInventoryPanel.setItems(currentPlayer.get_inventory());
                equipmentPanel.updateEquipmentList();
                scrapPanel.setPlayer(currentPlayer);
                scrapPanel.repaint();
            }
        }
    } catch (Exception ex) {}
}
        // Update panels when entering PLAYING state
        if (currentState == enums.GameState.PLAYING) {
            if (currentPlayer != null) {
                // Update inventory panel
                sideInventoryPanel.setItems(currentPlayer.get_inventory());
                
                // Update equipment and scrap panels with new player
                equipmentPanel.setPlayer(currentPlayer);
                equipmentPanel.exitScrapMode();
                scrapPanel.setPlayer(currentPlayer);
                
                // Update equipment list
                equipmentPanel.setEquipmentList(currentPlayer.get_equipment_inventory());
            }
        }
update_display();
break;
case "GAME_PAUSED":
currentState = enums.GameState.PAUSED;
gamePanel.show_pause_overlay();
update_display();
break;
case "GAME_RESUMED":
currentState = enums.GameState.PLAYING;
update_display();
break;
case "NEW_GAME_STARTED":
case "GAME_RESET":
    // Reset panels for new game
    if (currentPlayer != null) {
        sideInventoryPanel.setItems(currentPlayer.get_inventory());
        
        // Update equipment and scrap panels with new player
        equipmentPanel.setPlayer(currentPlayer);
        equipmentPanel.exitScrapMode();
        scrapPanel.setPlayer(currentPlayer);
        
        // Update equipment list
        equipmentPanel.setEquipmentList(currentPlayer.get_equipment_inventory());
    }
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
if (currentPlayer != null) {
    sideInventoryPanel.setItems(currentPlayer.get_inventory());
}
break;
case "EQUIPMENT_COLLECTED":
    if (data instanceof model.equipment.Equipment) {
        model.equipment.Equipment equipment = (model.equipment.Equipment) data;
        // Handle equipment collection logic here if needed
    }
    // Update equipment panel when equipment is collected
    if (currentPlayer != null) {
        equipmentPanel.setEquipmentList(currentPlayer.get_equipment_inventory());
    }
    break;
case "WEAPON_EQUIPPED":
    if (data instanceof model.equipment.Equipment) {
        model.equipment.Equipment equipment = (model.equipment.Equipment) data;
        gamePanel.getLogBoxPanel().addMessage("Equipped " + equipment.get_name() + " [T" + equipment.get_tier() + "]!");
    }
    if (currentPlayer != null) {
        equipmentPanel.setEquipmentList(currentPlayer.get_equipment_inventory());
    }
    break;
case "ARMOR_EQUIPPED":
    if (data instanceof model.equipment.Equipment) {
        model.equipment.Equipment equipment = (model.equipment.Equipment) data;
        gamePanel.getLogBoxPanel().addMessage("Equipped " + equipment.get_name() + " [T" + equipment.get_tier() + "]!");
    }
    if (currentPlayer != null) {
        equipmentPanel.setEquipmentList(currentPlayer.get_equipment_inventory());
    }
    break;
case "WEAPON_UNEQUIPPED":
case "ARMOR_UNEQUIPPED":
case "EQUIPMENT_SCRAPPED":
    if (currentPlayer != null) {
        equipmentPanel.setEquipmentList(currentPlayer.get_equipment_inventory());
    }
    break;
case "SCRAP_GAINED":
    if (data instanceof Integer) {
        int scrapGained = (Integer) data;
        gamePanel.getLogBoxPanel().addMessage("Gained " + scrapGained + " scrap!");
    }
    gamePanel.update_player_stats();
    if (scrapPanel != null) {
        scrapPanel.repaint();
    }
    break;
case "SCRAP_CONVERTED":
    gamePanel.getLogBoxPanel().addMessage("Scrap converted to Upgrade Crystal!");
    gamePanel.update_player_stats();
    if (scrapPanel != null) {
        scrapPanel.repaint();
    }
    // Update inventory panel to show the new Upgrade Crystal
    if (currentPlayer != null) {
        sideInventoryPanel.setItems(currentPlayer.get_inventory());
    }
    break;
case "ITEM_COLLECTED":
    if (data instanceof model.items.Item) {
        model.items.Item collectedItem = (model.items.Item) data;
        // Add message to log
        gamePanel.getLogBoxPanel().addMessage("You've picked up a " + collectedItem.get_name());
        // Update inventory panel
        if (currentPlayer != null) {
            sideInventoryPanel.setItems(currentPlayer.get_inventory());
        }
    }
    break;
case "ITEM_USED":
    if (data instanceof model.items.Item) {
        model.items.Item usedItem = (model.items.Item)data;
        // Add compelling messages for special items (no generic "Used:" message)
        if (usedItem instanceof model.items.Consumable) {
            model.items.Consumable consumable = (model.items.Consumable) usedItem;
            String effect = consumable.get_effect_type().toLowerCase();
            switch (effect) {
                case "health":
                    gamePanel.getLogBoxPanel().addMessage("You feel rejuvenated! HP restored.");
                    break;
                case "mana":
                    gamePanel.getLogBoxPanel().addMessage("Arcane energy surges through you! MP restored.");
                    break;
                case "experience":
                    gamePanel.getLogBoxPanel().addMessage("The knowledge of this scroll fills you with determination!");
                    break;
                case "clarity":
                    gamePanel.getLogBoxPanel().addMessage("You use your lamp to light up the way forth!");
                    break;
                case "invisibility":
                    gamePanel.getLogBoxPanel().addMessage("The Cloak of Vanishment hides you in the shadows!");
                    break;
                case "swiftness":
                    gamePanel.getLogBoxPanel().addMessage("These Swift winds will push you away from danger!");
                    break;
                case "immortality":
                    gamePanel.getLogBoxPanel().addMessage("With this Amulet, you will be shielded from any harm!");
                    break;
                default:
                    // Fallback for any other items
                    gamePanel.getLogBoxPanel().addMessage("Used: " + usedItem.get_name());
                    break;
            }
        } else {
            // Fallback for non-consumable items
            gamePanel.getLogBoxPanel().addMessage("Used: " + usedItem.get_name());
        }
        if (currentPlayer != null) {
            sideInventoryPanel.setItems(currentPlayer.get_inventory());
        }
    }
    break;
case "ITEM_FLASH":
    if (data instanceof String) {
        String effectType = (String) data;
        gamePanel.triggerItemFlash(effectType);
    }
    break;
case "IMMORTALITY_AUTO_ACTIVATED":
    if (data instanceof model.items.Item) {
        gamePanel.getLogBoxPanel().addMessage("As a last resort, your Amulet activates on its own to save you!");
    }
    break;
case "ITEM_USE_FAILED":
    if (data instanceof model.items.Item) {
        model.items.Item failedItem = (model.items.Item)data;
        String msg = null;
        if (failedItem instanceof model.items.Consumable) {
            model.items.Consumable cons = (model.items.Consumable) failedItem;
            String effect = cons.get_effect_type().toLowerCase();
            switch (effect) {
                case "health":
                    msg = "You try to drink the potion, but you're already in perfect health! Save it for a rainy day.";
                    break;
                case "mana":
                    msg = "You feel a surge of arcane energy... but your mind is already brimming with power!";
                    break;
                case "experience":
                    msg = "You feel wise enough already. Maybe save this for later.";
                    break;
                default:
                    msg = "You can't use that right now.";
            }
        } else {
            msg = "You can't use that right now.";
        }
        gamePanel.getLogBoxPanel().addMessage(msg);
    }
    break;
case "BATTLE_VICTORY":
case "VICTORY_ACHIEVED":
    if (controller != null) {
        controller.end_game(true, "Victory!");
    }
    break;
case "BATTLE_DEFEAT":
case "PLAYER_DEATH":
    String killer = data instanceof String ? (String) data : "Unknown";
    if (controller != null) {
        controller.end_game(false, killer);
    }
    break;
case "MAP_GENERATED":
gamePanel.set_map((Map) data);
break;
case "PLAYER_ATTACKED":
    if (data instanceof model.gameLogic.AttackVisualData) {
        model.gameLogic.AttackVisualData attackData = (model.gameLogic.AttackVisualData) data;
        gamePanel.showPlayerAttack(attackData);
    }
    break;
case "ENEMY_MELEE_ATTACK":
    // Handle enemy melee attack visual (legacy)
    if (data instanceof Object[] && ((Object[])data).length == 2) {
        Object[] attackData = (Object[])data;
        if (attackData[0] instanceof model.characters.Enemy && attackData[1] instanceof Double) {
            model.characters.Enemy enemy = (model.characters.Enemy)attackData[0];
            Double attackAngle = (Double)attackData[1];
            gamePanel.showEnemyAttack(enemy, attackAngle);
        }
    }
    break;
case "ENEMY_SWING_ATTACK":
    // Handle enemy swing attack visual
    if (data instanceof Object[] && ((Object[])data).length == 2) {
        Object[] attackData = (Object[])data;
        if (attackData[0] instanceof model.characters.Enemy && attackData[1] instanceof model.gameLogic.AttackVisualData) {
            model.characters.Enemy enemy = (model.characters.Enemy)attackData[0];
            model.gameLogic.AttackVisualData swingData = (model.gameLogic.AttackVisualData)attackData[1];
            gamePanel.showEnemySwingAttack(enemy, swingData);
        }
    }
    break;
case "ENEMY_PROJECTILE_ATTACK":
    // Handle enemy projectile attack visual
    if (data instanceof Object[] && ((Object[])data).length == 2) {
        Object[] attackData = (Object[])data;
        if (attackData[0] instanceof model.characters.Enemy && attackData[1] instanceof Double) {
            model.characters.Enemy enemy = (model.characters.Enemy)attackData[0];
            Double attackAngle = (Double)attackData[1];
            gamePanel.showEnemyAttack(enemy, attackAngle);
        }
    }
    break;
case "PLAYER_RANGER_BOW_ATTACK":
    // Handle player Ranger bow attack visual
    if (data instanceof model.gameLogic.AttackVisualData) {
        model.gameLogic.AttackVisualData bowData = (model.gameLogic.AttackVisualData) data;
        gamePanel.showPlayerRangerBowAttack(bowData);
    }
    break;
case "ENEMY_RANGER_BOW_ATTACK":
    // Handle enemy Ranger bow attack visual
    if (data instanceof Object[] && ((Object[])data).length == 2) {
        Object[] attackData = (Object[])data;
        if (attackData[0] instanceof model.characters.Enemy && attackData[1] instanceof model.gameLogic.AttackVisualData) {
            model.characters.Enemy enemy = (model.characters.Enemy)attackData[0];
            model.gameLogic.AttackVisualData bowData = (model.gameLogic.AttackVisualData)attackData[1];
            gamePanel.showEnemyRangerBowAttack(enemy, bowData);
        }
    }
    break;
case "UPGRADER_GREETING":
case "UPGRADER_WARNING":
case "UPGRADER_INTERACTION":
    if (data instanceof String) {
        String message = (String) data;
        gamePanel.getLogBoxPanel().addMessage(message, Color.YELLOW);
    }
    break;
case "UPGRADER_MESSAGE":
    if (data instanceof String) {
        String message = (String) data;
        gamePanel.getLogBoxPanel().addMessage(message, Color.YELLOW);
    }
    break;
case "ENEMY_WIND_UP_STARTED":
    // Handle enemy wind-up start - trigger enemy cyan blinking
    if (data instanceof model.characters.Enemy) {
        gamePanel.startEnemyWindUpWarning((model.characters.Enemy)data);
    }
    break;

case "PLAYER_DAMAGED":
    // Trigger damage flash effect when player takes damage
    gamePanel.triggerDamageFlash();
    break;

case "BOSS_DEFEATED":
    // Boss defeated - special handling
    gamePanel.getLogBoxPanel().addMessage("Boss defeated! Floor cleared!");
    break;

case "FLOOR_TRANSITION_STARTED":
    // Floor transition started - show black screen
    gamePanel.setFloorTransitioning(true);
    // Clear the map reference to prevent old map rendering
    gamePanel.set_map(null);
    // Set the new floor number for the welcome message
    try {
        model.gameLogic.GameLogic gameLogic = (model.gameLogic.GameLogic)controller.get_model();
        int nextFloor = gameLogic.get_current_floor() + 1;
        gamePanel.setFloorNumber(nextFloor);
    } catch (Exception ex) {
        System.err.println("Error setting floor number: " + ex.getMessage());
    }
    break;

case "FLOOR_ADVANCED":
    // Floor advanced - update map and stop transition
    gamePanel.setFloorTransitioning(false);
    // Get the new map and floor number from the controller
    try {
        model.gameLogic.GameLogic gameLogic = (model.gameLogic.GameLogic)controller.get_model();
        Map newMap = gameLogic.get_current_map();
        int currentFloor = gameLogic.get_current_floor();
        gamePanel.set_map(newMap);
        gamePanel.setFloorNumber(currentFloor);
    } catch (Exception ex) {
        System.err.println("Error updating map for new floor: " + ex.getMessage());
    }
    break;

case "LOG_MESSAGE":
    String message = (String)data;
    // Check for important messages that should be red
    if (message.contains("boss has the Key") || message.contains("stairs are locked") || message.contains("need to find a Floor Key")) {
        gamePanel.getLogBoxPanel().addMessage(message, Color.RED);
    } else {
        gamePanel.getLogBoxPanel().addMessage(message);
    }
    break;
case "GAME_INITIALIZED":
    // Show welcome message when game is first initialized (not on resume)
    gamePanel.getLogBoxPanel().clearMessages();
    gamePanel.getLogBoxPanel().addMessage("Welcome to the dungeon! Survive, explore, and claim your glory.");
    break;
case "STATS_UPDATED":
    gamePanel.update_player_stats();
    gamePanel.repaint();
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

    // Debug mode toggle (O key)
    if (currentState == GameState.PLAYING && e.getKeyCode() == KeyEvent.VK_O) {
        debugMode = !debugMode;
        gamePanel.set_debug_mode(debugMode);
        // if (debugStatsPanel != null) debugStatsPanel.setDebugMode(debugMode); // Removed
        
        // Give all items and level up when debug mode is enabled
        if (debugMode && currentPlayer != null) {
            // Level up the player for debug purposes
            currentPlayer.gain_experience(1000); // Give enough XP to level up multiple times
            
            // Get GameLogic to create items
            model.gameLogic.GameLogic gameLogic = (model.gameLogic.GameLogic) controller.get_model();
            if (gameLogic != null) {
                // Add one of each effect item
                currentPlayer.collect_item(new model.items.Consumable("Lamp", 10, "clarity"));
                currentPlayer.collect_item(new model.items.Consumable("Vanish Cloak", 8, "invisibility"));
                currentPlayer.collect_item(new model.items.Consumable("Swift Winds", 5, "swiftness"));
                currentPlayer.collect_item(new model.items.Consumable("Immortality Amulet", 5, "immortality"));
                
                // Add some utility items
                currentPlayer.collect_item(new model.items.Consumable("Health Potion", 50, "health"));
                currentPlayer.collect_item(new model.items.Consumable("Mana Potion", 30, "mana"));
                currentPlayer.collect_item(new model.items.Consumable("Experience Scroll", 100, "experience"));
                currentPlayer.collect_item(new model.items.KeyItem("Upgrade Crystal", "any"));
                
                // Update inventory display
                if (sideInventoryPanel != null) {
                    sideInventoryPanel.setItems(currentPlayer.get_inventory());
                }
            }
        }
        
        gamePanel.repaint();
        return;
    }

    // Removed debug G key to allow map movement testing
    // if (currentState == GameState.PLAYING && e.getKeyCode() == KeyEvent.VK_G) {
    //     controller.end_game(false, "Debug Enemy");
    //     return;
    // }

    // Inventory navigation mode
    if (inventoryNavigationMode) {
        handle_inventory_navigation_input(e);
        return;
    }
    // Stats navigation mode
    if (statsNavigationMode) {
        handle_stats_navigation_input(e);
        return;
    }
    // Equipment navigation mode
    if (equipmentNavigationMode) {
        handle_equipment_navigation_input(e);
        return;
    }

    // Toggle mouse mode with M (no longer debug-only)
    if (currentState == GameState.PLAYING && e.getKeyCode() == KeyEvent.VK_M) {
        setMouseAimingMode(!mouseAimingMode);
        gamePanel.repaint();
        // if (debugStatsPanel != null) debugStatsPanel.repaint(); // Removed
        return;
    }
    // Debug class switching (1/2/3/4) only in debug mode
    if (currentState == GameState.PLAYING && debugMode) {
        if (e.getKeyCode() == KeyEvent.VK_1) {
            if (currentPlayer != null) currentPlayer.debug_switch_class(1);
            gamePanel.repaint();
            return;
        } else if (e.getKeyCode() == KeyEvent.VK_2) {
            if (currentPlayer != null) currentPlayer.debug_switch_class(2);
            gamePanel.repaint();
            return;
        } else if (e.getKeyCode() == KeyEvent.VK_3) {
            if (currentPlayer != null) currentPlayer.debug_switch_class(3);
            gamePanel.repaint();
            return;
        } else if (e.getKeyCode() == KeyEvent.VK_4) {
            if (currentPlayer != null) currentPlayer.debug_switch_class(4);
            gamePanel.repaint();
            return;
        }
    }
    // Handle I key for inventory navigation (any state)
    if (e.getKeyCode() == KeyEvent.VK_I && currentState == GameState.PLAYING) {
        // Enter inventory navigation mode
        inventoryNavigationMode = true;
        inventoryNavRow = 0;
        inventoryNavCol = 0;
        sideInventoryPanel.setKeyboardNavigation(true, inventoryNavRow, inventoryNavCol);
        sideInventoryPanel.setInventoryHighlight(true);
        // Pause all movement
        setAllMovementPaused(true);
        // Explicitly request focus for inventory panel
        sideInventoryPanel.requestFocusInWindow();
        repaint();
        return;
    }
    
    // Handle E key for stats navigation (any state)
    if (e.getKeyCode() == KeyEvent.VK_E && currentState == GameState.PLAYING) {
        // Enter stats navigation mode
        statsNavigationMode = true;
        statsNavRow = 0;
        statsNavCol = 0;
        // Clear held keys to prevent movement persistence (same fix as pause bug)
        gamePanel.clearAllHeldKeys();
        // Pause all movement
        setAllMovementPaused(true);
        repaint();
        return;
    }
    
    // Handle Q key for equipment navigation (any state)
    if (e.getKeyCode() == KeyEvent.VK_Q && currentState == GameState.PLAYING && equipmentPanel != null) {
        // Enter equipment navigation mode
        equipmentNavigationMode = true;
        equipmentNavIndex = 0;
        equipmentPanel.setKeyboardNavigation(true, 0, 0);
        equipmentPanel.setEquipmentHighlight(true);
        // Pause all movement
        setAllMovementPaused(true);
        repaint();
        return;
    }
    

    
    // Delegate to MainController for proper input handling
    if (controller instanceof controller.MainController) {
        ((controller.MainController) controller).handleKeyPressed(e);
    } else {
        // Fallback to old system for backward compatibility
        switch (currentState) {
        case PLAYING:
            // Handle ENTER key for upgrader interaction
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                model.gameLogic.GameLogic gameLogic = (model.gameLogic.GameLogic) controller.get_model();
                if (gameLogic != null) {
                    gameLogic.handle_upgrader_interaction();
                }
                return;
            }
            // Forward key events to GamePanel for proper movement/aiming handling
            gamePanel.processKeyEvent(e);
            break;
        case BATTLE:
            handle_battle_input(e);
            break;
        case PAUSED:
            boolean handled = gamePanel.handlePauseMenuInput(e.getKeyCode());
            if (!handled && e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                controller.handle_input("RESUME_GAME");
            }
            break;
        case MAIN_MENU:
        case CLASS_SELECTION:
        case GAME_OVER:
        case VICTORY:
            handle_menu_input(e);
            break;
        }
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
private void handle_inventory_navigation_input(KeyEvent e) {
    int maxRows = sideInventoryPanel.getGridRows();
    int maxCols = sideInventoryPanel.getGridCols();
    switch (e.getKeyCode()) {
        case KeyEvent.VK_ESCAPE:
        case KeyEvent.VK_I:
            inventoryNavigationMode = false;
            sideInventoryPanel.setKeyboardNavigation(false, -1, -1);
            sideInventoryPanel.setInventoryHighlight(false);
            gamePanel.setInventoryOverlay(false);
            // Resume all movement
            setAllMovementPaused(false);
            repaint();
            break;
        case KeyEvent.VK_UP:
            inventoryNavRow = (inventoryNavRow - 1 + maxRows) % maxRows;
            sideInventoryPanel.setKeyboardNavigation(true, inventoryNavRow, inventoryNavCol);
            repaint();
            break;
        case KeyEvent.VK_DOWN:
            inventoryNavRow = (inventoryNavRow + 1) % maxRows;
            sideInventoryPanel.setKeyboardNavigation(true, inventoryNavRow, inventoryNavCol);
            repaint();
            break;
        case KeyEvent.VK_LEFT:
            inventoryNavCol = (inventoryNavCol - 1 + maxCols) % maxCols;
            sideInventoryPanel.setKeyboardNavigation(true, inventoryNavRow, inventoryNavCol);
            repaint();
            break;
        case KeyEvent.VK_RIGHT:
            inventoryNavCol = (inventoryNavCol + 1) % maxCols;
            sideInventoryPanel.setKeyboardNavigation(true, inventoryNavRow, inventoryNavCol);
            repaint();
            break;
        case KeyEvent.VK_ENTER:
            sideInventoryPanel.consumeSelectedItem(inventoryNavRow, inventoryNavCol);
            repaint();
            break;
    }
}

/**
    * Handle input in stats panel navigation
    * 
    * @param e KeyEvent from user input
*/
private void handle_stats_navigation_input(KeyEvent e) {
    int maxCols = 6; // Stats panel has 6 columns in a single row
    switch (e.getKeyCode()) {
        case KeyEvent.VK_ESCAPE:
        case KeyEvent.VK_E:
            statsNavigationMode = false;
            // Clear held keys when exiting stats navigation (same fix as pause bug)
            gamePanel.clearAllHeldKeys();
            // Resume all movement
            setAllMovementPaused(false);
            repaint();
            break;
        case KeyEvent.VK_LEFT:
            statsNavCol = (statsNavCol - 1 + maxCols) % maxCols;
            repaint();
            break;
        case KeyEvent.VK_RIGHT:
            statsNavCol = (statsNavCol + 1) % maxCols;
            repaint();
            break;
        case KeyEvent.VK_ENTER:
            // Handle stats button selection
            int index = statsNavCol; // Single row, so index = column
            if (index < 6) {
                String[] buttonLabels = {"Health", "Attack", "Defense", "Range", "Speed", "Mana"};

                
                // Handle stat button click (same logic as mouse click)
                if (currentPlayer != null) {
                    String statType = buttonLabels[index].toLowerCase();
                    boolean success = currentPlayer.increase_stat(statType);
                    
                    if (!success) {
                        // Not enough level points
                        currentPlayer.notify_observers("LOG_MESSAGE", "You need to level up more to get stronger");
                    }
                    
                    // Update display
                    gamePanel.update_player_stats();
                    repaint();
                }
            }
            break;
    }
}

/**
    * Handle input in equipment panel navigation
    * 
    * @param e KeyEvent from user input
*/
private void handle_equipment_navigation_input(KeyEvent e) {
    int maxRows = equipmentPanel.getGridRows();
    int maxCols = equipmentPanel.getGridCols();
    
    switch (e.getKeyCode()) {
        case KeyEvent.VK_ESCAPE:
        case KeyEvent.VK_Q:
            equipmentNavigationMode = false;
            equipmentPanel.setKeyboardNavigation(false, -1, -1);
            equipmentPanel.setEquipmentHighlight(false);
            equipmentPanel.exitScrapMode(); // Exit scrap mode when leaving
            // Resume all movement
            setAllMovementPaused(false);
            repaint();
            break;
        case KeyEvent.VK_UP:
            equipmentNavRow = (equipmentNavRow - 1 + maxRows) % maxRows;
            equipmentPanel.setKeyboardNavigation(true, equipmentNavRow, equipmentNavCol);
            repaint();
            break;
        case KeyEvent.VK_DOWN:
            equipmentNavRow = (equipmentNavRow + 1) % maxRows;
            equipmentPanel.setKeyboardNavigation(true, equipmentNavRow, equipmentNavCol);
            repaint();
            break;
        case KeyEvent.VK_LEFT:
            equipmentNavCol = (equipmentNavCol - 1 + maxCols) % maxCols;
            equipmentPanel.setKeyboardNavigation(true, equipmentNavRow, equipmentNavCol);
            repaint();
            break;
        case KeyEvent.VK_RIGHT:
            equipmentNavCol = (equipmentNavCol + 1) % maxCols;
            equipmentPanel.setKeyboardNavigation(true, equipmentNavRow, equipmentNavCol);
            repaint();
            break;
        case KeyEvent.VK_ENTER:
            // Use the equipment panel's handleKeyboardSelection method
            equipmentPanel.handleKeyboardSelection();
            repaint();
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
    case KeyEvent.VK_W:
        menuPanel.move_selection_up();
        break;
    case KeyEvent.VK_DOWN:
    case KeyEvent.VK_S:
        menuPanel.move_selection_down();
        break;
    case KeyEvent.VK_ESCAPE:
        if (currentState == GameState.MAIN_MENU) {
            controller.handle_input("BACK_TO_MENU");
        }
        break;
    }
}

@Override
public void keyReleased(KeyEvent e) {
    // Delegate to MainController for proper input handling
    if (controller instanceof controller.MainController) {
        ((controller.MainController) controller).handleKeyReleased(e);
    } else {
        // Fallback to old system for backward compatibility
        // Don't forward key events if in navigation modes
        if (inventoryNavigationMode || statsNavigationMode || equipmentNavigationMode) {
            return;
        }
        
        if (currentState == GameState.PLAYING && currentPlayer != null) {
            // Forward key release events to GamePanel for proper movement/aiming handling
            gamePanel.processKeyEvent(e);
        }
    }
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
 * Sets the main controller for proper MVC architecture.
 * 
 * @param controller the main controller to set
 */
public void setController(controller.MainController controller) {
    // This method is used by the new MainController architecture
    // The original set_controller method is kept for backward compatibility
    // Note: MainController doesn't implement GameController, so we can't set it directly
    // The controller field remains null for this architecture
}

/**
 * Updates the view state.
 * Called by the main controller each frame.
 */
public void update() {
// Update the display
update_display();
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
    * MANDATORY: Show the main window
*/
public void show_window() {
setVisible(true);
setFocusable(true);
requestFocusInWindow();
}

public Font getPixelFont() {
    return pixelFont;
}

public GameState getCurrentState() {
    return currentState;
}

// Add a public setter for currentState
public void setGameState(enums.GameState state) { this.currentState = state; }

public void setAllMovementPaused(boolean paused) {
    // Actually pause movement by calling GamePanel's movement pause mechanism
    if (gamePanel != null) {
        gamePanel.setMovementPaused(paused);
    }
}
public void updateDebugStats(Player player) {
    // if (debugStatsPanel != null) debugStatsPanel.setPlayer(player); // Removed
}

public boolean isMouseAimingMode() { return mouseAimingMode; }
public void setMouseAimingMode(boolean enabled) {
    this.mouseAimingMode = enabled;
    if (gamePanel != null) gamePanel.setMouseAimingMode(enabled);
    // if (debugStatsPanel != null) debugStatsPanel.setMouseAimingMode(enabled); // Removed
}
public boolean isMouseDetected() { return mouseDetected; }
public Point getLastMousePosition() { return lastMousePosition; }
public void setLastMousePosition(Point p) { lastMousePosition = p; }

public int getCurrentFloorNumber() { 
    return gamePanel != null ? gamePanel.getCurrentFloorNumber() : 1; 
}

// Stats navigation mode getters and setters for GamePanel
public boolean isStatsNavigationMode() { return statsNavigationMode; }
public int getStatsNavRow() { return statsNavRow; }
public int getStatsNavCol() { return statsNavCol; }
public void setStatsNavRow(int row) { this.statsNavRow = row; }
public void setStatsNavCol(int col) { this.statsNavCol = col; }

public void exitStatsNavigationMode() {
    statsNavigationMode = false;
    statsNavRow = -1;
    statsNavCol = -1;
    setAllMovementPaused(false);
    // Restore focus to the main window so all key events work properly
    requestFocusInWindow();
    repaint();
}

public void showScoreboard(model.scoreEntry.ScoreEntry currentScore, java.util.List<model.scoreEntry.ScoreEntry> highScores, view.panels.ScoreboardPanel.ScoreboardListener listener, String className) {
    if (scoreboardPanel != null) mainPanel.remove(scoreboardPanel);
    scoreboardPanel = new view.panels.ScoreboardPanel(currentScore, highScores, listener, pixelFont, className);
    mainPanel.add(scoreboardPanel, "SCOREBOARD");
    cardLayout.show(mainPanel, "SCOREBOARD");
    scoreboardPanel.setVisible(true);
    scoreboardPanel.requestFocusInWindow();
}

class ItemInfoPanel extends JPanel {
    private String itemName = "";
    private String itemDesc = "";
    private Font pixelFont;
    public ItemInfoPanel(Font pixelFont) {
        this.pixelFont = pixelFont;
        setPreferredSize(new Dimension(220, 80));
        setBackground(Color.DARK_GRAY);
    }
    public void setItemInfo(String name, String desc) {
        this.itemName = name;
        this.itemDesc = desc;
        repaint();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(pixelFont.deriveFont(14f));
        g2d.setColor(Color.WHITE);
        g2d.drawString(itemName, 12, 28);
        g2d.setFont(pixelFont.deriveFont(10f));
        g2d.setColor(Color.CYAN);
        g2d.drawString(itemDesc, 12, 52);
    }
}
}