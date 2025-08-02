package integration;

import model.gameLogic.GameLogic;
import model.characters.Player;
import model.map.Map;
import view.GameView;
import controller.MainController;
import enums.CharacterClass;
import enums.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.util.concurrent.TimeUnit;

/**
 * Final integration tests for the miniRogueDemo game.
 * Focuses on end-to-end game flow validation and renderer integration.
 * Appropriate for school project scope.
 */
public class FinalIntegrationTest {
    
    private GameLogic gameLogic;
    private GameView gameView;
    private MainController controller;
    private Player player;
    
    @BeforeEach
    void setUp() {
        // Initialize game components for integration testing
        player = new Player("TestPlayer", CharacterClass.WARRIOR, new utilities.Position(5, 5));
        gameLogic = new GameLogic(player);
        gameView = new GameView();
        controller = new MainController(gameLogic, gameView);
        
        // Fix: MainController doesn't implement GameController, so we need to use the correct approach
        // The MainController constructor already calls gameView.setController(this)
    }
    
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testCompleteGameFlow() {
        // Test complete game flow from start to finish
        assert gameLogic.get_game_state() == GameState.MAIN_MENU;
        
        // Start game through proper action flow
        gameLogic.handle_player_action("start_new_game", null);
        assert gameLogic.get_game_state() == GameState.CLASS_SELECTION;
        
        gameLogic.handle_player_action("class_selected", null);
        assert gameLogic.get_game_state() == GameState.PLAYING;
        
        // Verify player is initialized
        assert gameLogic.get_player() != null;
        assert gameLogic.get_current_map() != null;
        
        // Verify map generation
        Map map = gameLogic.get_current_map();
        assert map.get_width() > 0;
        assert map.get_height() > 0;
        
        // Verify player position is valid
        Player player = gameLogic.get_player();
        assert player.get_position().get_x() >= 0;
        assert player.get_position().get_y() >= 0;
    }
    
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testRendererIntegration() {
        // Test that renderer components work with game logic
        gameLogic.handle_player_action("start_new_game", null);
        gameLogic.handle_player_action("class_selected", null);
        
        // Verify GameView can access game components through MainController
        // MainController doesn't implement GameController, so we test the actual architecture
        assert controller.getGameLogic() != null;
        assert controller.getGameView() != null;
        
        // Verify renderer can access player data
        Player player = gameLogic.get_player();
        assert player.get_name() != null;
        assert player.get_selected_class() != null;
        
        // Verify renderer can access map data
        Map map = gameLogic.get_current_map();
        assert map.get_rooms() != null;
        assert !map.get_rooms().isEmpty();
    }
    
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testMultiClassGameFlow() {
        // Test game flow with different character classes (including Ranger)
        CharacterClass[] classes = {CharacterClass.WARRIOR, CharacterClass.MAGE, CharacterClass.ROGUE, CharacterClass.RANGER};
        
        for (CharacterClass characterClass : classes) {
            Player testPlayer = new Player("TestPlayer", characterClass, new utilities.Position(5, 5));
            GameLogic testGameLogic = new GameLogic(testPlayer);
            
            testGameLogic.handle_player_action("start_new_game", null);
            testGameLogic.handle_player_action("class_selected", null);
            assert testGameLogic.get_game_state() == GameState.PLAYING;
            
            Player player = testGameLogic.get_player();
            // Fix: Use the correct class name format from the enum
            assert player.get_selected_class().equals(characterClass.get_class_name());
        }
    }
    
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testStateTransitionIntegration() {
        // Test state transitions work with renderer
        gameLogic.handle_player_action("start_new_game", null);
        gameLogic.handle_player_action("class_selected", null);
        assert gameLogic.get_game_state() == GameState.PLAYING;
        
        // Test pause functionality
        gameLogic.pauseGame();
        assert gameLogic.is_paused();
        
        // Test resume functionality
        gameLogic.resume_game();
        assert !gameLogic.is_paused();
        
        // Test game over state
        Player player = gameLogic.get_player();
        player.take_damage(1000); // Force death
        gameLogic.check_death_condition();
        assert gameLogic.is_death();
    }
    
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testObserverPatternIntegration() {
        // Test that observer pattern works with renderer
        gameLogic.handle_player_action("start_new_game", null);
        gameLogic.handle_player_action("class_selected", null);
        
        // Verify observers are notified of game state changes
        assert gameLogic.get_game_state() == GameState.PLAYING;
        
        // Test that GameView can observe game changes through MainController
        // MainController doesn't implement GameController, so we test the actual architecture
        assert controller.getGameLogic() != null;
        assert controller.getGameView() != null;
        
        // Verify player data is accessible through observer pattern
        Player player = gameLogic.get_player();
        assert player.get_name() != null;
        assert player.get_current_hp() > 0;
    }
    
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testInputIntegration() {
        // Test that input system integrates with game logic
        gameLogic.handle_player_action("start_new_game", null);
        gameLogic.handle_player_action("class_selected", null);
        
        // Verify input can be processed
        assert gameLogic.get_game_state() == GameState.PLAYING;
        
        // Test player movement input
        Player player = gameLogic.get_player();
        player.setMoveDirection(1, 0); // Move right
        assert player.getMoveDX() == 1;
        assert player.getMoveDY() == 0;
        
        // Test player attack input
        player.setAimDirection(0, 1); // Aim down
        assert player.getAimDX() == 0;
        assert player.getAimDY() == 1;
    }
    
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testMapRendererIntegration() {
        // Test that map rendering integrates with game logic
        gameLogic.handle_player_action("start_new_game", null);
        gameLogic.handle_player_action("class_selected", null);
        
        Map map = gameLogic.get_current_map();
        assert map != null;
        
        // Verify map has valid tiles
        assert map.get_width() > 0;
        assert map.get_height() > 0;
        
        // Verify map has rooms
        assert map.get_rooms() != null;
        assert !map.get_rooms().isEmpty();
        
        // Verify player position is within map bounds
        Player player = gameLogic.get_player();
        utilities.Position playerPos = player.get_position();
        assert playerPos.get_x() >= 0 && playerPos.get_x() < map.get_width();
        assert playerPos.get_y() >= 0 && playerPos.get_y() < map.get_height();
    }
    
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testEntityRendererIntegration() {
        // Test that entity rendering integrates with game logic
        gameLogic.handle_player_action("start_new_game", null);
        gameLogic.handle_player_action("class_selected", null);
        
        // Verify player can be rendered
        Player player = gameLogic.get_player();
        assert player.get_name() != null;
        assert player.get_position() != null;
        
        // Verify enemies can be rendered
        assert gameLogic.get_current_enemies() != null;
        
        // Verify boss can be rendered (if present)
        if (gameLogic.get_current_boss() != null) {
            assert gameLogic.get_current_boss().get_name() != null;
        }
    }
    
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testUIRendererIntegration() {
        // Test that UI rendering integrates with game logic
        gameLogic.handle_player_action("start_new_game", null);
        gameLogic.handle_player_action("class_selected", null);
        
        // Verify UI components can access game data
        Player player = gameLogic.get_player();
        assert player.get_current_hp() > 0;
        assert player.get_max_hp() > 0;
        assert player.get_current_mp() >= 0;
        assert player.get_max_mp() >= 0;
        
        // Verify inventory can be rendered
        assert player.get_inventory() != null;
        assert player.get_inventory_size() >= 0;
        
        // Verify equipment can be rendered
        assert player.get_equipment_inventory() != null;
    }
    
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testPerformanceIntegration() {
        // Test that performance is acceptable for school project
        long startTime = System.currentTimeMillis();
        
        gameLogic.handle_player_action("start_new_game", null);
        gameLogic.handle_player_action("class_selected", null);
        
        // Perform basic game operations
        Player player = gameLogic.get_player();
        for (int i = 0; i < 10; i++) {
            player.setMoveDirection(1, 0);
            player.update_movement(gameLogic.get_current_map());
            player.setMoveDirection(0, 1);
            player.update_movement(gameLogic.get_current_map());
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // Should complete within 2 seconds for school project
        assert duration < 2000 : "Game operations took too long: " + duration + "ms";
    }
} 