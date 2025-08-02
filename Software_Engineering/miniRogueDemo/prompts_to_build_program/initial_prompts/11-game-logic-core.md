## 11-game-logic-core.md

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