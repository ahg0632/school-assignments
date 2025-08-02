## 12-battle-logic-system.md

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