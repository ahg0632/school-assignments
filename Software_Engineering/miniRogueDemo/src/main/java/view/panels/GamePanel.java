package view.panels;

import view.GameView;
import model.map.Map;
import model.characters.Player;
import utilities.Position;
import enums.GameConstants;
import enums.TileType;
import enums.CharacterClass;
import model.characters.BaseClass;
import javax.swing.*;
import java.awt.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import model.gameLogic.Projectile;
import java.util.HashSet;
import java.util.Set;
import javax.imageio.ImageIO;
import java.util.List;
import java.util.HashMap;
import utilities.WeaponImageManager;


/**
* Panel that displays the game world, player, enemies, and items.
* Renders the 2D pixel art game environment.
*/
public class GamePanel extends JPanel {

private GameView parentView;
private Map currentMap;
private Player player;
private boolean showPauseOverlay;
private int pauseMenuSelection = 0; // 0 = Resume, 1 = Quit
private Rectangle resumeButtonBounds = new Rectangle();
private Rectangle quitButtonBounds = new Rectangle();
    
private boolean debugMode = false;
private Timer repaintTimer;
private LogBoxPanel logBoxPanel;
private boolean inventoryOverlay = false;
private int pauseHoveredIndex = -1;


// Add fields to store attack visuals
private java.util.Set<utilities.Position> attackFanTiles = new java.util.HashSet<>();
private utilities.Position projectileStart = null;
private utilities.Position projectileEnd = null;
private long attackVisualTime = 0;
private model.gameLogic.AttackVisualData lastAttackData = null;

    // Add fields for enemy attack visuals
    private java.util.Map<model.characters.Enemy, Long> enemyAttackTimes = new java.util.HashMap<>();
    private java.util.Map<model.characters.Enemy, Double> enemyAttackAngles = new java.util.HashMap<>();
    private java.util.Map<model.characters.Enemy, model.gameLogic.AttackVisualData> enemySwingData = new java.util.HashMap<>();
    
    // Ranger bow rendering system (for projectile attacks)
    private java.util.Map<model.characters.Enemy, model.gameLogic.AttackVisualData> enemyRangerBowData = new java.util.HashMap<>();
    private model.gameLogic.AttackVisualData playerRangerBowData = null;
    
    // Add field for enemy wind-up warning (cyan blinking on enemy border)
    private java.util.Map<model.characters.Enemy, Long> enemyWindUpStartTimes = new java.util.HashMap<>();
    private static final long ENEMY_WIND_UP_WARNING_DURATION = 500; // 0.5 seconds

    // Damage flash effect fields
    private long damageFlashStartTime = 0;
    private static final long DAMAGE_FLASH_DURATION = 600; // 0.6 seconds total (faster)
    private static final long FIRST_FLASH_DURATION = 150; // First flash duration (faster)
    private static final long SECOND_FLASH_DURATION = 200; // Second flash duration (faster)
    private static final long FLASH_GAP = 50; // Gap between flashes (shorter)
    
    // Item consumption flash system
    private long itemFlashStartTime = 0;
    private static final long ITEM_FLASH_DURATION = 300; // 0.3 seconds
    private Color itemFlashColor = null;
    
    // State effect images
    private java.awt.image.BufferedImage clarityImage = null;
    private java.awt.image.BufferedImage undyingImage = null;
    private java.awt.image.BufferedImage swiftnessImage = null;
    private java.awt.image.BufferedImage vanishImage = null;
    
    // Item image cache
    private HashMap<String, java.awt.image.BufferedImage> itemIconCache = new HashMap<>();
    private WeaponImageManager weaponImageManager = WeaponImageManager.getInstance();
    
    // Stats panel images
    private java.awt.image.BufferedImage healthImage = null;
    private java.awt.image.BufferedImage attackImage = null;
    private java.awt.image.BufferedImage defenseImage = null;
    private java.awt.image.BufferedImage rangeImage = null;
    private java.awt.image.BufferedImage speedImage = null;
    private java.awt.image.BufferedImage manaImage = null;
    private java.awt.image.BufferedImage levelPointsImage = null;
    private java.awt.image.BufferedImage deadImage = null;
    private java.awt.image.BufferedImage weaponUpgraderImage = null;
    private java.awt.image.BufferedImage armorUpgraderImage = null;

    //Tiles
    private java.awt.image.BufferedImage tileFloor = null;
    private java.awt.image.BufferedImage tileWall = null;
    private java.awt.image.BufferedImage tileEntrance = null;
    private java.awt.image.BufferedImage tileBossRoom = null;
    private java.awt.image.BufferedImage tileStairs = null;
    private java.awt.image.BufferedImage bonusRoomImage = null;
    private java.awt.image.BufferedImage bossRoomImage = null;
    
    // Player sprite images for animation (8 total: 2 frames x 4 directions) - DEFAULT SPRITES
    private java.awt.image.BufferedImage playerWalkUp1 = null;
    private java.awt.image.BufferedImage playerWalkUp2 = null;
    private java.awt.image.BufferedImage playerWalkDown1 = null;
    private java.awt.image.BufferedImage playerWalkDown2 = null;
    private java.awt.image.BufferedImage playerWalkLeft1 = null;
    private java.awt.image.BufferedImage playerWalkLeft2 = null;
    private java.awt.image.BufferedImage playerWalkRight1 = null;
    private java.awt.image.BufferedImage playerWalkRight2 = null;
    
    // Warrior sprite images for universal character rendering (extracted from sprite sheet)
    private java.awt.image.BufferedImage warriorWalkUp1 = null;
    private java.awt.image.BufferedImage warriorWalkUp2 = null;
    private java.awt.image.BufferedImage warriorWalkDown1 = null;  
    private java.awt.image.BufferedImage warriorWalkDown2 = null;
    private java.awt.image.BufferedImage warriorWalkLeft1 = null;
    private java.awt.image.BufferedImage warriorWalkLeft2 = null;
    private java.awt.image.BufferedImage warriorWalkRight1 = null;
    private java.awt.image.BufferedImage warriorWalkRight2 = null;
    
    // Rogue sprite images for universal character rendering (extracted from sprite sheet)
    private java.awt.image.BufferedImage rogueWalkUp1 = null;
    private java.awt.image.BufferedImage rogueWalkUp2 = null; 
    private java.awt.image.BufferedImage rogueWalkDown1 = null;
    private java.awt.image.BufferedImage rogueWalkDown2 = null;
    private java.awt.image.BufferedImage rogueWalkLeft1 = null;
    private java.awt.image.BufferedImage rogueWalkLeft2 = null;
    private java.awt.image.BufferedImage rogueWalkRight1 = null;
    private java.awt.image.BufferedImage rogueWalkRight2 = null;

    // Ranger sprite images for universal character rendering (extracted from sprite sheet)
    private java.awt.image.BufferedImage rangerWalkUp1 = null;
    private java.awt.image.BufferedImage rangerWalkUp2 = null; 
    private java.awt.image.BufferedImage rangerWalkDown1 = null;
    private java.awt.image.BufferedImage rangerWalkDown2 = null;
    private java.awt.image.BufferedImage rangerWalkLeft1 = null;
    private java.awt.image.BufferedImage rangerWalkLeft2 = null;
    private java.awt.image.BufferedImage rangerWalkRight1 = null;
    private java.awt.image.BufferedImage rangerWalkRight2 = null;
    
    // Mage sprite images for universal character rendering (extracted from sprite sheet)
    private java.awt.image.BufferedImage mageWalkUp1 = null;
    private java.awt.image.BufferedImage mageWalkUp2 = null;
    private java.awt.image.BufferedImage mageWalkDown1 = null;
    private java.awt.image.BufferedImage mageWalkDown2 = null;
    private java.awt.image.BufferedImage mageWalkLeft1 = null;
    private java.awt.image.BufferedImage mageWalkLeft2 = null;
    private java.awt.image.BufferedImage mageWalkRight1 = null;
    private java.awt.image.BufferedImage mageWalkRight2 = null;
    
    // Player animation timing variables
    private long lastAnimationTime = 0;
    private static final long ANIMATION_FRAME_DURATION = 400; // 400ms per frame
    private int currentAnimationFrame = 0; // 0 or 1 for the two frames
    
    // Floor transition system
    private boolean isFloorTransitioning = false;
    private long floorTransitionStartTime = 0;
    private static final long FLOOR_TRANSITION_DURATION = 2000; // 2 seconds total transition
    private static final long FLOOR_TRANSITION_FADE_DURATION = 1000; // 1 second fade
    private int currentFloorNumber = 1; // Track current floor for display
    private int statsHoveredIndex = -1; // Track which stat button is being hovered

// Track last aim direction for attack visuals
private int lastAimDX = 0, lastAimDY = 1; // Default to down

private boolean mouseAimingMode = false;
public void setMouseAimingMode(boolean enabled) { this.mouseAimingMode = enabled; }
public boolean isMouseAimingMode() { return mouseAimingMode; }

private boolean attackKeyHeld = false;
    private boolean mouseAttackHeld = false;
    
    // Movement pause state for stats navigation and other pause-like modes
    private boolean movementPaused = false;
private Timer attackHoldTimer;

private final Set<Integer> movementKeysHeld = new HashSet<>();
private final Set<Integer> aimKeysHeld = new HashSet<>();

/**
 * EDGE CASE FIX: Selective key clearing to prevent stuck movement while preserving aim direction
 * This prevents the scenario where a user holds a movement key, presses ESC to pause,
 * releases the key while paused (KEY_RELEASED event gets blocked), then resumes
 * with the key still registered as "held" causing continuous movement.
 * 
 * SOLUTION: Clear key STATES but preserve aim DIRECTION for better user experience.
 */
public void clearAllHeldKeys() {
    // MOVEMENT: Clear both key state AND player direction (fixes stuck movement bug)
    movementKeysHeld.clear();
    if (player != null) {
        player.setMoveDirection(0, 0);  // Stop movement immediately
    }
    
    // AIM: Clear key state but PRESERVE aim direction (keeps aiming direction intact)
    aimKeysHeld.clear();
    // NOTE: We DON'T call player.setAimDirection() here - preserves where player was aiming
    
    // ATTACK: Reset attack key states for consistency
    attackKeyHeld = false;
    mouseAttackHeld = false;
}

/**
 * Set movement pause state for stats navigation and other pause-like modes
 * @param paused true to pause movement, false to resume
 */
public void setMovementPaused(boolean paused) {
    this.movementPaused = paused;
}

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
    setLayout(new BorderLayout());
    setFocusable(true); // Ensure panel can receive focus and mouse events
    enableEvents(java.awt.AWTEvent.KEY_EVENT_MASK); // Explicitly enable key events for debug class switching
    enableEvents(java.awt.AWTEvent.MOUSE_EVENT_MASK); // Explicitly enable mouse events
    
    // Load state effect images
    loadStateImages();
    
    // Load player sprite images
    loadPlayerSprites();
    
    // Load class-specific sprite images
    loadWarriorSprites();
    loadRogueSprites();
    loadRangerSprites();
    loadMageSprites();

addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
    @Override
    public void mouseMoved(java.awt.event.MouseEvent e) {
        if (showPauseOverlay) {
            int prevHovered = pauseHoveredIndex;
            if (resumeButtonBounds.contains(e.getPoint())) {
                pauseHoveredIndex = 0;
            } else if (quitButtonBounds.contains(e.getPoint())) {
                pauseHoveredIndex = 1;
            } else {
                pauseHoveredIndex = -1;
            }
            if (pauseHoveredIndex != prevHovered) repaint();
        }
    }
});
addMouseListener(new java.awt.event.MouseAdapter() {
    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {
        if (pauseHoveredIndex != -1) {
            pauseHoveredIndex = -1;
            repaint();
        }
    }
});
// Start a timer to repaint at ~60 FPS
repaintTimer = new Timer(16, new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        // Skip movement updates when paused or in stats navigation - freeze all visual movement
        if (!showPauseOverlay && !movementPaused && player != null && currentMap != null) {
            player.update_movement(currentMap);
            // Update camera to keep player centered in white frame
            updateCamera();
            // Synchronize enemy movement updates with player
            try {
                java.util.List<model.characters.Enemy> enemies = ((model.gameLogic.GameLogic)parentView.get_controller().get_model()).get_current_enemies();
                if (enemies != null) {
                    for (model.characters.Enemy enemy : enemies) {
                        enemy.update_movement();
                    }
                }
            } catch (Exception ex) {}
        }
        repaint();
    }
});
repaintTimer.start();
// Initialize log box
logBoxPanel = new LogBoxPanel();
logBoxPanel.setPixelFont(parentView.getPixelFont().deriveFont(12f));
logBoxPanel.setPreferredSize(new Dimension(enums.GameConstants.WINDOW_WIDTH - 320, 120)); // Match gamePanel width instead of full window width
add(logBoxPanel, BorderLayout.SOUTH);

// Stats panel will be rendered inline in paintComponent
    this.debugMode = false;
    
    // Track mouse position for mouse aiming
addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
@Override
public void mouseMoved(java.awt.event.MouseEvent e) {
    if (parentView instanceof view.GameView) {
        ((view.GameView)parentView).setLastMousePosition(e.getPoint());
    }
    // Mouse aiming mode: only mouse sets aim
    if (mouseAimingMode && player != null) {
        Point mouse = e.getPoint();
        float px = player.getPixelX() + enums.GameConstants.TILE_SIZE / 2f;
        float py = player.getPixelY() + enums.GameConstants.TILE_SIZE / 2f;
        float dx = mouse.x - px;
        float dy = mouse.y - py;
        // Normalize to -1, 0, or 1 for keyboard compatibility
        int aimDX = (Math.abs(dx) > Math.abs(dy)) ? (dx > 0 ? 1 : -1) : 0;
        int aimDY = (Math.abs(dy) >= Math.abs(dx)) ? (dy > 0 ? 1 : -1) : 0;
        player.setAimDirection(aimDX, aimDY);
    }
    
    // Handle stats panel hover
    int y = getHeight() - 180;
    int statusPanelX = 240;
    int statusPanelWidth = 240;
    int statsPanelX = statusPanelX + statusPanelWidth + 10;
    int statsPanelY = y - 20;
    int imageSize = 28;
    int imagePadding = 30;
    int imagesPerRow = 6;
    int startX = statsPanelX + 8;
    int startY = statsPanelY + 38;
    
    // Check if mouse is within stats panel bounds
    statsHoveredIndex = -1;
    if (e.getX() >= statsPanelX && e.getX() < statsPanelX + 328 && // 328 is approximate panel width
    e.getY() >= statsPanelY && e.getY() < statsPanelY + 90) {
        
        int clickX = e.getX() - startX;
        //int clickY = e.getY() - startY;
        int col = clickX / (imageSize + imagePadding);
        
        if (col >= 0 && col < imagesPerRow) statsHoveredIndex = col;
        if (player.getPlayerClassOOP().getClassType() != enums.CharacterClass.MAGE && statsHoveredIndex == 5) statsHoveredIndex = -1;
    }
    
    repaint();
}
});
// Timer for continuous attack
attackHoldTimer = new Timer(10, new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        if (mouseAimingMode && player != null) {
            // Always poll the real mouse position for aiming
            Point mouse = java.awt.MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen(mouse, GamePanel.this);
            float px = player.getPixelX() + enums.GameConstants.TILE_SIZE / 2f;
            float py = player.getPixelY() + enums.GameConstants.TILE_SIZE / 2f;
            float dx = mouse.x - px;
            float dy = mouse.y - py;
            int aimDX = (Math.abs(dx) > Math.abs(dy)) ? (dx > 0 ? 1 : -1) : 0;
            int aimDY = (Math.abs(dy) >= Math.abs(dx)) ? (dy > 0 ? 1 : -1) : 0;
            player.setAimDirection(aimDX, aimDY);
        }
        if ((attackKeyHeld || mouseAttackHeld) && canAttack()) {
            // When attacking, poll the real mouse position for attack direction
            if (mouseAimingMode && player != null) {
                Point mouse = java.awt.MouseInfo.getPointerInfo().getLocation();
                SwingUtilities.convertPointFromScreen(mouse, GamePanel.this);
                float px = player.getPixelX() + enums.GameConstants.TILE_SIZE / 2f;
                float py = player.getPixelY() + enums.GameConstants.TILE_SIZE / 2f;
                float dx = mouse.x - px;
                float dy = mouse.y - py;
                int aimDX = (Math.abs(dx) > Math.abs(dy)) ? (dx > 0 ? 1 : -1) : 0;
                int aimDY = (Math.abs(dy) >= Math.abs(dx)) ? (dy > 0 ? 1 : -1) : 0;
                player.setAimDirection(aimDX, aimDY);
            }
            // Directly call player.attack() or attack logic here
            if (player != null && player.getGameLogic() != null) player.getGameLogic().handle_player_attack_input();
        }
    }
});
attackHoldTimer.start();
}

/**
 * Load state effect images from resources
 */
private void loadStateImages() {
    try {
        clarityImage = javax.imageio.ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/states/Clarity.png"));
        undyingImage = javax.imageio.ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/states/Undying.png"));
        swiftnessImage = javax.imageio.ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/states/Swiftness.png"));
        vanishImage = javax.imageio.ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/states/Vanish.png"));
        
        // Load stats images
        healthImage = javax.imageio.ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/stats/Health.png"));
        attackImage = javax.imageio.ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/stats/Attack.png"));
        defenseImage = javax.imageio.ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/stats/Defense.png"));
        rangeImage = javax.imageio.ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/stats/Range.png"));
        speedImage = javax.imageio.ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/stats/Speed.png"));
        manaImage = javax.imageio.ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/stats/Mana.png"));
        levelPointsImage = javax.imageio.ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/stats/Level_Points.png"));
        deadImage = javax.imageio.ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/player/Dead.png"));
        weaponUpgraderImage = javax.imageio.ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/player/WeaponUpgrader.png"));
        armorUpgraderImage = javax.imageio.ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/player/ArmorUpgrader.png"));

        // Load tile images
        tileFloor = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/tiles/floor01.png"));
        tileWall = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/tiles/wall01.png"));
        tileEntrance = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/tiles/entrance01.png"));
        tileBossRoom = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/tiles/boss_room01.png"));
        tileStairs = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/tiles/stairs01.png"));
        bonusRoomImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/tiles/BonusRoom.png"));
        bossRoomImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/tiles/BossRoom.png"));
    } catch (Exception e) {
        System.err.println("Failed to load state images: " + e.getMessage());
    }
}

/**
 * Get the appropriate icon image for an item
 */
private java.awt.image.BufferedImage getIconForItem(model.items.Item item) {
    String name = item.get_name().toLowerCase();
    String iconFile = null;
    
    // Health potions
    if (name.contains("health") && name.contains("potion")) {
        iconFile = "potion_ruby.png";
    }
    // Mana potions
    else if (name.contains("mana") && name.contains("potion")) {
        iconFile = "mana_blue.png";
    }
    // Experience scrolls
    else if (name.contains("experience") && name.contains("scroll")) {
        iconFile = "magic_scroll.png";
    }
    // Lamp (clarity effect)
    else if (name.contains("lamp")) {
        iconFile = "lamp.png";
    }
    // Vanish Cloak (invisibility effect)
    else if (name.contains("vanish") && name.contains("cloak")) {
        iconFile = "invisibility_cloak.png";
    }
    // Swift Winds (speed effect)
    else if (name.contains("swift") && name.contains("winds")) {
        iconFile = "swift_winds.png";
    }
    // Upgrade crystals
    else if (name.contains("crystal")) {
        iconFile = "upgradeCrystal.png";
    }
    // Floor Key
    else if (name.contains("floor") && name.contains("key")) {
        iconFile = "key.png";
    }
    // Immortality amulet
    else if (name.contains("immortality") && name.contains("amulet")) {
        iconFile = "undying_amulet.png";
    }
    
    if (iconFile == null) return null;
    if (itemIconCache.containsKey(iconFile)) return itemIconCache.get(iconFile);
    try {
        java.awt.image.BufferedImage img = javax.imageio.ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/items/" + iconFile));
        itemIconCache.put(iconFile, img);
        return img;
    } catch (java.io.IOException | IllegalArgumentException ex) {
        return null;
    }
}

/**
 * Unified weapon image mapping system following OOD principles
 * Single source of truth for all weapon-to-image mappings
 * Supports both exact matches and intelligent fallbacks
 */
private java.awt.image.BufferedImage getWeaponImage(model.equipment.Weapon weapon) {
    if (weapon == null) return null;
    
    String weaponName = weapon.get_name();
    return weaponImageManager.getWeaponImage(weaponName);
}







/**
 * Load default player sprite images from resources
 */
private void loadPlayerSprites() {
    try {
        playerWalkUp1 = javax.imageio.ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/player/boy_up_1.png"));
        playerWalkUp2 = javax.imageio.ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/player/boy_up_2.png"));
        playerWalkDown1 = javax.imageio.ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/player/boy_down_1.png"));
        playerWalkDown2 = javax.imageio.ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/player/boy_down_2.png"));
        playerWalkLeft1 = javax.imageio.ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/player/boy_left_1.png"));
        playerWalkLeft2 = javax.imageio.ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/player/boy_left_2.png"));
        playerWalkRight1 = javax.imageio.ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/player/boy_right_1.png"));
        playerWalkRight2 = javax.imageio.ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/player/boy_right_2.png"));
        
        System.out.println("Player sprites loaded successfully");
    } catch (Exception e) {
        System.err.println("Failed to load player sprites: " + e.getMessage());
        e.printStackTrace();
    }
}

/**
 * Load warrior class sprite images from sprite sheet
 * Grid layout: 4 columns x 2 rows (60x42 total, 15x21 per sprite)
 * Columns: down, up, right, left (left to right)
 * Rows: frame1 (top), frame2 (bottom)
 */
private void loadWarriorSprites() {
    try {
        // Load the 60x42 warrior sprite sheet
        java.awt.image.BufferedImage warriorSheet = javax.imageio.ImageIO.read(
            getClass().getClassLoader().getResourceAsStream("images/player/warrior_sheet.png"));
        
        if (warriorSheet != null) {
            // Extract 15x21 sprites based on grid layout
            // Column 0 (x=0): Down sprites
            warriorWalkDown1 = warriorSheet.getSubimage(0, 0, 15, 21);    // Top-left
            warriorWalkDown2 = warriorSheet.getSubimage(0, 21, 15, 21);   // Bottom-left
            
            // Column 1 (x=15): Up sprites  
            warriorWalkUp1 = warriorSheet.getSubimage(15, 0, 15, 21);     // Top, 2nd column
            warriorWalkUp2 = warriorSheet.getSubimage(15, 21, 15, 21);    // Bottom, 2nd column
            
            // Column 2 (x=30): Right sprites
            warriorWalkRight1 = warriorSheet.getSubimage(30, 0, 15, 21);  // Top, 3rd column
            warriorWalkRight2 = warriorSheet.getSubimage(30, 21, 15, 21); // Bottom, 3rd column
            
            // Column 3 (x=45): Left sprites
            warriorWalkLeft1 = warriorSheet.getSubimage(45, 0, 15, 21);   // Top-right
            warriorWalkLeft2 = warriorSheet.getSubimage(45, 21, 15, 21);  // Bottom-right
            
            System.out.println("Warrior sprites loaded successfully");
        } else {
            System.err.println("Warrior sprite sheet not found");
        }
    } catch (Exception e) {
        System.err.println("Failed to load warrior sprites: " + e.getMessage());
        e.printStackTrace();
    }
}

/**
 * Load rogue class sprite images from sprite sheet
 * Grid layout: 4 columns x 2 rows (56x40 total, 14x20 per sprite)
 * Columns: down, up, right, left (left to right)
 * Rows: frame1 (top), frame2 (bottom)
 */
private void loadRogueSprites() {
    try {
        // Load the rogue sprite sheet
        java.awt.image.BufferedImage rogueSheet = javax.imageio.ImageIO.read(
            getClass().getClassLoader().getResourceAsStream("images/player/rogue_sheet.png"));
        
        if (rogueSheet != null) {
            // Extract 14x20 sprites based on grid layout
            // Column 0 (x=0): Down sprites
            rogueWalkDown1 = rogueSheet.getSubimage(0, 0, 14, 20);    // Top-left
            rogueWalkDown2 = rogueSheet.getSubimage(0, 20, 14, 20);   // Bottom-left
            
            // Column 1 (x=14): Up sprites  
            rogueWalkUp1 = rogueSheet.getSubimage(14, 0, 14, 20);     // Top, 2nd column
            rogueWalkUp2 = rogueSheet.getSubimage(14, 20, 14, 20);    // Bottom, 2nd column
            
            // Column 2 (x=28): Right sprites
            rogueWalkRight1 = rogueSheet.getSubimage(28, 0, 14, 20);  // Top, 3rd column
            rogueWalkRight2 = rogueSheet.getSubimage(28, 20, 14, 20); // Bottom, 3rd column
            
            // Column 3 (x=42): Left sprites
            rogueWalkLeft1 = rogueSheet.getSubimage(42, 0, 14, 20);   // Top-right
            rogueWalkLeft2 = rogueSheet.getSubimage(42, 20, 14, 20);  // Bottom-right
            
            System.out.println("Rogue sprites loaded successfully");
        } else {
            System.err.println("Rogue sprite sheet not found");
        }
    } catch (Exception e) {
        System.err.println("Failed to load rogue sprites: " + e.getMessage());
        e.printStackTrace();
    }
}

/**
 * Load ranger class sprite images from sprite sheet
 * Grid layout: 4 columns x 2 rows (128x64 total, 32x32 per sprite)
 * Columns: down, up, right, left (left to right)
 * Rows: frame1 (top), frame2 (bottom)
 */
private void loadRangerSprites() {
    try {
        // Load the ranger sprite sheet
        java.awt.image.BufferedImage rangerSheet = javax.imageio.ImageIO.read(
            getClass().getClassLoader().getResourceAsStream("images/player/ranger_sheet.png"));
        
        if (rangerSheet != null) {
            // Extract 32x32 sprites based on grid layout
            // Column 0 (x=0): Down sprites
            rangerWalkDown1 = rangerSheet.getSubimage(8, 2, 16, 29);    // Top-left
            rangerWalkDown2 = rangerSheet.getSubimage(8, 35, 16, 29);   // Bottom-left
            
            // Column 1 (x=32): Up sprites  
            rangerWalkUp1 = rangerSheet.getSubimage(40, 2, 16, 30);     // Top, 2nd column
            rangerWalkUp2 = rangerSheet.getSubimage(40, 34, 16, 30);    // Bottom, 2nd column
            
            // Column 2 (x=64): Right sprites
            rangerWalkRight1 = rangerSheet.getSubimage(64, 0, 32,31);  // Top, 3rd column
            rangerWalkRight2 = rangerSheet.getSubimage(65, 37, 31, 26); // Bottom, 3rd column
            
            // Column 3 (x=96): Left sprites
            rangerWalkLeft1 = rangerSheet.getSubimage(96, 0, 32, 31);   // Top-right
            rangerWalkLeft2 = rangerSheet.getSubimage(96, 37, 31, 26);  // Bottom-right
            
            System.out.println("Ranger sprites loaded successfully");
        } else {
            System.err.println("Ranger sprite sheet not found");
        }
    } catch (Exception e) {
        System.err.println("Failed to load ranger sprites: " + e.getMessage());
        e.printStackTrace();
    }
}

/**
 * Load mage class sprite images from sprite sheet
 * Grid layout: 4 columns x 2 rows (128x64 total, 32x32 per sprite)
 * Columns: down, up, right, left (left to right)
 * Rows: frame1 (top), frame2 (bottom)
 */
private void loadMageSprites() {
    try {
        // Load the mage sprite sheet
        java.awt.image.BufferedImage mageSheet = javax.imageio.ImageIO.read(
            getClass().getClassLoader().getResourceAsStream("images/player/mage_spritesheet.png"));
        
        if (mageSheet != null) {
            // Extract 32x32 sprites based on grid layout
            // Column 0 (x=0): Down sprites
            mageWalkDown1 = mageSheet.getSubimage(0, 0, 18, 32);    // Top-left
            mageWalkDown2 = mageSheet.getSubimage(0, 32, 20, 32);   // Bottom-left
            
            // Column 1 (x=32): Up sprites  
            mageWalkUp1 = mageSheet.getSubimage(27, 0, 16, 32);     // Top, 2nd column
            mageWalkUp2 = mageSheet.getSubimage(27, 32, 16, 32);    // Bottom, 2nd column
            
            // Column 2 (x=64): Right sprites
            mageWalkRight1 = mageSheet.getSubimage(52, 0, 21, 31);  // Top, 3rd column
            mageWalkRight2 = mageSheet.getSubimage(51, 33, 21, 30); // Bottom, 3rd column
            
            // Column 3 (x=96): Left sprites
            mageWalkLeft1 = mageSheet.getSubimage(81, 0, 21, 31);   // Top-right
            mageWalkLeft2 = mageSheet.getSubimage(81, 34, 21, 30);  // Bottom-right
            
            System.out.println("Mage sprites loaded successfully");
        } else {
            System.err.println("Mage sprite sheet not found");
        }
    } catch (Exception e) {
        System.err.println("Failed to load mage sprites: " + e.getMessage());
        e.printStackTrace();
    }
}

/**
 * Determine player facing direction from movement data
 * Uses current movement direction, falls back to last movement direction
 * @return Direction as string: "up", "down", "left", "right"
 */
private String getPlayerFacingDirection() {
    if (player == null) return "down"; // Default
    
    // Try to get current movement direction
    int dx = 0, dy = 0;
    try {
        // Check if player is currently moving
        java.lang.reflect.Field moveDXField = player.getClass().getDeclaredField("moveDX");
        java.lang.reflect.Field moveDYField = player.getClass().getDeclaredField("moveDY");
        moveDXField.setAccessible(true);
        moveDYField.setAccessible(true);
        dx = moveDXField.getInt(player);
        dy = moveDYField.getInt(player);
        
        // If not currently moving, use last movement direction
        if (dx == 0 && dy == 0) {
            java.lang.reflect.Field lastMoveDXField = player.getClass().getDeclaredField("lastMoveDX");
            java.lang.reflect.Field lastMoveDYField = player.getClass().getDeclaredField("lastMoveDY");
            lastMoveDXField.setAccessible(true);
            lastMoveDYField.setAccessible(true);
            dx = lastMoveDXField.getInt(player);
            dy = lastMoveDYField.getInt(player);
        }
    } catch (Exception e) {
        // If reflection fails, default to down
        return "down";
    }
    
    // Convert direction to string
    if (dy < 0) return "up";
    if (dy > 0) return "down";
    if (dx < 0) return "left";
    if (dx > 0) return "right";
    
    return "down"; // Default fallback
}

/**
 * Determine enemy facing direction from aim data
 * Enemies use aimDX, aimDY for their facing direction
 * @param enemy The enemy character
 * @return Direction as string: "up", "down", "left", "right"
 */
private String getEnemyFacingDirection(model.characters.Enemy enemy) {
    if (enemy == null) return "down"; // Default
    
    try {
        // Get enemy's aim direction (which represents their facing direction)
        java.lang.reflect.Field aimDXField = enemy.getClass().getDeclaredField("aimDX");
        java.lang.reflect.Field aimDYField = enemy.getClass().getDeclaredField("aimDY");
        aimDXField.setAccessible(true);
        aimDYField.setAccessible(true);
        int dx = aimDXField.getInt(enemy);
        int dy = aimDYField.getInt(enemy);
        
        // Convert direction to string
        if (dy < 0) return "up";
        if (dy > 0) return "down"; 
        if (dx < 0) return "left";
        if (dx > 0) return "right";
        
        return "down"; // Default fallback
    } catch (Exception e) {
        // If reflection fails, default to down
        return "down";
    }
}


/**
 * Check if the player is currently moving
 * @return true if player is moving, false if stationary
 */
private boolean isPlayerMoving() {
    if (player == null) return false;
    
    try {
        // Check if player is currently moving
        java.lang.reflect.Field moveDXField = player.getClass().getDeclaredField("moveDX");
        java.lang.reflect.Field moveDYField = player.getClass().getDeclaredField("moveDY");
        moveDXField.setAccessible(true);
        moveDYField.setAccessible(true);
        int dx = moveDXField.getInt(player);
        int dy = moveDYField.getInt(player);
        
        // Player is moving if either dx or dy is non-zero
        return (dx != 0 || dy != 0);
    } catch (Exception e) {
        // If reflection fails, assume not moving
        return false;
    }
}

/**
 * Determine boss facing direction from aim data  
 * Bosses inherit from Enemy, so they also use aimDX, aimDY
 * @param boss The boss character
 * @return Direction as string: "up", "down", "left", "right"
 */
private String getBossFacingDirection(model.characters.Boss boss) {
    if (boss == null) return "down"; // Default
    
    try {
        // First try to get boss's aim direction (which represents their facing direction)
        java.lang.reflect.Field aimDXField = boss.getClass().getDeclaredField("aimDX");
        java.lang.reflect.Field aimDYField = boss.getClass().getDeclaredField("aimDY");
        aimDXField.setAccessible(true);
        aimDYField.setAccessible(true);
        int dx = aimDXField.getInt(boss);
        int dy = aimDYField.getInt(boss);
        
        // If boss has a valid aim direction, use it
        if (dx != 0 || dy != 0) {
            // Convert direction to string
            if (dy < 0) return "up";
            if (dy > 0) return "down"; 
            if (dx < 0) return "left";
            if (dx > 0) return "right";
        }
        
        // If no aim direction, try to face the player
        if (player != null) {
            float bossX = boss.getPixelX();
            float bossY = boss.getPixelY();
            float playerX = player.getPixelX();
            float playerY = player.getPixelY();
            
            float deltaX = playerX - bossX;
            float deltaY = playerY - bossY;
            
            // Determine which direction has the larger difference
            if (Math.abs(deltaY) > Math.abs(deltaX)) {
                return deltaY < 0 ? "up" : "down";
            } else {
                return deltaX < 0 ? "left" : "right";
            }
        }
        
        return "down"; // Default fallback
    } catch (Exception e) {
        // If reflection fails, try to face the player
        if (player != null) {
            float bossX = boss.getPixelX();
            float bossY = boss.getPixelY();
            float playerX = player.getPixelX();
            float playerY = player.getPixelY();
            
            float deltaX = playerX - bossX;
            float deltaY = playerY - bossY;
            
            // Determine which direction has the larger difference
            if (Math.abs(deltaY) > Math.abs(deltaX)) {
                return deltaY < 0 ? "up" : "down";
            } else {
                return deltaX < 0 ? "left" : "right";
            }
        }
        
        return "down"; // Final fallback
    }
}

/**
 * Get the appropriate player sprite based on direction and animation frame
 * Uses universal character sprite system based on player's class
 * @param direction The facing direction ("up", "down", "left", "right")
 * @return BufferedImage of the appropriate sprite, or null if not available
 */
private java.awt.image.BufferedImage getPlayerSprite(String direction) {
    if (player == null) return null;
    
    // Get the player's character class
    enums.CharacterClass characterClass = player.get_character_class();
    
    // Update animation frame based on timing - only animate when moving AND not paused
    if (!showPauseOverlay && isPlayerMoving()) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAnimationTime > ANIMATION_FRAME_DURATION) {
            currentAnimationFrame = (currentAnimationFrame + 1) % 2; // Toggle between 0 and 1
            lastAnimationTime = currentTime;
        }
    }
    
    // Use universal character sprite system
    if (player != null) {
        return getCharacterSprite(player.get_character_class(), direction, currentAnimationFrame);
    }
    
    return getDefaultSprite(direction, currentAnimationFrame); // Fallback to default sprites
}

/**
 * Update animation frame for all characters (player, enemies, bosses)
 * This ensures all characters animate consistently
 */
private void updateAnimationFrame() {
    if (!showPauseOverlay) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAnimationTime > ANIMATION_FRAME_DURATION) {
            currentAnimationFrame = (currentAnimationFrame + 1) % 2; // Toggle between 0 and 1
            lastAnimationTime = currentTime;
        }
    }
}

/**
 * Universal character sprite system - gets sprite for any character class
 * @param characterClass The character's class (WARRIOR, MAGE, ROGUE, RANGER)
 * @param direction The facing direction ("up", "down", "left", "right")
 * @param animationFrame The animation frame (0 or 1)
 * @return BufferedImage of the appropriate sprite
 */
private java.awt.image.BufferedImage getCharacterSprite(enums.CharacterClass characterClass, String direction, int animationFrame) {
    switch (characterClass) {
        case WARRIOR:
            return getWarriorSprite(direction, animationFrame);
        case ROGUE:
            return getRogueSprite(direction, animationFrame);
        case RANGER:
            return getRangerSprite(direction, animationFrame);
        case MAGE:
            return getMageSprite(direction, animationFrame);
        default:
            return getDefaultSprite(direction, animationFrame); // Fallback to default boy sprites
    }
}

/**
 * Get warrior class sprite based on direction and animation frame
 * @param direction The facing direction ("up", "down", "left", "right")
 * @param animationFrame The animation frame (0 or 1)
 * @return BufferedImage of the warrior sprite
 */
private java.awt.image.BufferedImage getWarriorSprite(String direction, int animationFrame) {
    switch (direction) {
        case "up":
            return animationFrame == 0 ? warriorWalkUp1 : warriorWalkUp2;
        case "down":
            return animationFrame == 0 ? warriorWalkDown1 : warriorWalkDown2;
        case "left":
            return animationFrame == 0 ? warriorWalkLeft1 : warriorWalkLeft2;
        case "right":
            return animationFrame == 0 ? warriorWalkRight1 : warriorWalkRight2;
        default:
            return getDefaultSprite(direction, animationFrame); // Fallback to default
    }
}

/**
 * Get rogue class sprite based on direction and animation frame
 * @param direction The facing direction ("up", "down", "left", "right")
 * @param animationFrame The animation frame (0 or 1)
 * @return BufferedImage of the rogue sprite
 */
private java.awt.image.BufferedImage getRogueSprite(String direction, int animationFrame) {
    switch (direction) {
        case "up":
            return animationFrame == 0 ? rogueWalkUp1 : rogueWalkUp2;
        case "down":
            return animationFrame == 0 ? rogueWalkDown1 : rogueWalkDown2;
        case "left":
            return animationFrame == 0 ? rogueWalkLeft1 : rogueWalkLeft2;
        case "right":
            return animationFrame == 0 ? rogueWalkRight1 : rogueWalkRight2;
        default:
            return getDefaultSprite(direction, animationFrame); // Fallback to default
    }
}

/**
 * Get ranger class sprite based on direction and animation frame
 * @param direction The facing direction ("up", "down", "left", "right")
 * @param animationFrame The animation frame (0 or 1)
 * @return BufferedImage of the ranger sprite
 */
private java.awt.image.BufferedImage getRangerSprite(String direction, int animationFrame) {
    switch (direction) {
        case "up":
            return animationFrame == 0 ? rangerWalkUp1 : rangerWalkUp2;
        case "down":
            return animationFrame == 0 ? rangerWalkDown1 : rangerWalkDown2;
        case "left":
            return animationFrame == 0 ? rangerWalkLeft1 : rangerWalkLeft2;
        case "right":
            return animationFrame == 0 ? rangerWalkRight1 : rangerWalkRight2;
        default:
            return getDefaultSprite(direction, animationFrame); // Fallback to default
    }
}

/**
 * Get mage class sprite based on direction and animation frame
 * @param direction The facing direction ("up", "down", "left", "right")
 * @param animationFrame The animation frame (0 or 1)
 * @return BufferedImage of the mage sprite
 */
private java.awt.image.BufferedImage getMageSprite(String direction, int animationFrame) {
    switch (direction) {
        case "up":
            return animationFrame == 0 ? mageWalkUp1 : mageWalkUp2;
        case "down":
            return animationFrame == 0 ? mageWalkDown1 : mageWalkDown2;
        case "left":
            return animationFrame == 0 ? mageWalkLeft1 : mageWalkLeft2;
        case "right":
            return animationFrame == 0 ? mageWalkRight1 : mageWalkRight2;
        default:
            return getDefaultSprite(direction, animationFrame); // Fallback to default
    }
}

/**
 * Get default (boy) sprite based on direction and animation frame
 * @param direction The facing direction ("up", "down", "left", "right")
 * @param animationFrame The animation frame (0 or 1)
 * @return BufferedImage of the default sprite
 */
private java.awt.image.BufferedImage getDefaultSprite(String direction, int animationFrame) {
    switch (direction) {
        case "up":
            return animationFrame == 0 ? playerWalkUp1 : playerWalkUp2;
        case "down":
            return animationFrame == 0 ? playerWalkDown1 : playerWalkDown2;
        case "left":
            return animationFrame == 0 ? playerWalkLeft1 : playerWalkLeft2;
        case "right":
            return animationFrame == 0 ? playerWalkRight1 : playerWalkRight2;
        default:
            return playerWalkDown1; // Final fallback
    }
}

/**
 * Render the player using sprite animation
 * @param g2d Graphics2D context
 */
private void renderPlayerSprite(Graphics2D g2d) {
    if (player == null) return;
    
    String direction = getPlayerFacingDirection();
    java.awt.image.BufferedImage sprite = getPlayerSprite(direction);
    
    if (sprite != null) {
        int screenX = mapToScreenX(player.getPixelX());
        int screenY = mapToScreenY(player.getPixelY());
        int tileSize = GameConstants.TILE_SIZE;


        g2d.drawImage(sprite, screenX, screenY, tileSize, tileSize, null);
    } else {
        // Fallback to circle if sprite is not available
        int tileSize = GameConstants.TILE_SIZE;
        g2d.setColor(Color.CYAN); // Use cyan to indicate fallback rendering
        g2d.fillOval(
            mapToScreenX(player.getPixelX()) + 2,
            mapToScreenY(player.getPixelY()) + 2,
            tileSize - 4,
            tileSize - 4
        );
    }
}

/**
 * Render enemy using universal character sprite system
 * @param g2d Graphics2D context
 * @param enemy The enemy to render
 */
private void renderEnemySprite(Graphics2D g2d, model.characters.Enemy enemy) {
    if (enemy == null) return;
    
    String direction = getEnemyFacingDirection(enemy);
    // Use universal character sprite system - enemies share sprites with players of same class
    java.awt.image.BufferedImage sprite = getCharacterSprite(enemy.get_character_class(), direction, currentAnimationFrame);
    
    if (sprite != null) {
        int screenX = mapToScreenX(enemy.getPixelX());
        int screenY = mapToScreenY(enemy.getPixelY());
        int tileSize = GameConstants.TILE_SIZE;

        
        // Draw the sprite scaled to tile size
        g2d.drawImage(sprite, screenX, screenY, tileSize, tileSize, null);
    } else {
        // Fallback to old circle rendering if sprite fails
        int tileSize = GameConstants.TILE_SIZE;
        g2d.setColor(Color.RED);
        g2d.fillOval(mapToScreenX(enemy.getPixelX()), mapToScreenY(enemy.getPixelY()), tileSize, tileSize);
    }
}

/**
 * Render boss using universal character sprite system (larger size)
 * @param g2d Graphics2D context
 * @param boss The boss to render
 */
private void renderBossSprite(Graphics2D g2d, model.characters.Boss boss) {
    if (boss == null) return;
    
    String direction = getBossFacingDirection(boss);
    // Use universal character sprite system - bosses share sprites with players/enemies of same class
    java.awt.image.BufferedImage sprite = getCharacterSprite(boss.get_character_class(), direction, currentAnimationFrame);
    
    if (sprite != null) {
        int screenX = mapToScreenX(boss.getPixelX());
        int screenY = mapToScreenY(boss.getPixelY());
        int tileSize = GameConstants.TILE_SIZE;
        
        // Bosses are rendered 2x larger (as per existing boss rendering)
        int bossSize = (int)(tileSize * 2.0f);
        int offsetX = -tileSize / 2; // Center the larger sprite (half tile offset)
        int offsetY = -tileSize / 2;
        
        // Draw the sprite scaled to boss size
        g2d.drawImage(sprite, screenX + offsetX, screenY + offsetY, bossSize, bossSize, null);
    } else {
        // Fallback to old circle rendering if sprite fails
        int tileSize = GameConstants.TILE_SIZE;
        int bossSize = (int)(tileSize * 2.0f);
        int offsetX = -tileSize / 2;
        int offsetY = -tileSize / 2;
        g2d.setColor(Color.MAGENTA);
        g2d.fillOval(mapToScreenX(boss.getPixelX()) + offsetX, mapToScreenY(boss.getPixelY()) + offsetY, bossSize, bossSize);
    }
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

// Set background to black
g2d.setColor(Color.BLACK);
g2d.fillRect(0, 0, getWidth(), getHeight());

// If floor transitioning, show black overlay in map area and keep UI
if (isFloorTransitioning) {
    // Render map and entities first (they might be null/empty)
    if (currentMap != null) {
        render_map(g2d);
        render_entities(g2d);
    }
    
    // Render UI on top
    render_ui(g2d);
    
    // Add black overlay to map area (inside border) - similar to damage flash
    renderFloorTransitionOverlay(g2d);
    
    return; // Don't render anything else during transition
}

if (currentMap != null) {
render_map(g2d);
render_entities(g2d);
}

render_ui(g2d);

    // Render damage flash effect (on top of everything)
    renderDamageFlash(g2d);
    
    // Render item consumption flash effect (on top of everything)
    renderItemFlash(g2d);
    
    if (showPauseOverlay) {
render_pause_overlay(g2d);
}
if (inventoryOverlay) {
    g2d.setColor(new Color(0, 0, 0, 180));
    g2d.fillRect(0, 0, getWidth(), getHeight());
}

}

/**
    * MANDATORY: Render the map tiles
    * 
    * @param g2d Graphics2D context
*/
private void render_map(Graphics2D g2d) {
    int tileSize = GameConstants.TILE_SIZE;

        // Calculate map positioning using parenting system
        int mapWidth = 50 * tileSize; // 50 tiles wide
        int mapHeight = 30 * tileSize; // 30 tiles tall
        int rightPanelWidth = 250; // Width of the right inventory panel (updated from 280)
        int availableWidth = getWidth() - rightPanelWidth; // Available space for map
        int mapOffsetX = getMapOffsetX(); // Use parenting system
        int mapOffsetY = getMapOffsetY(); // Use parenting system

        // Set clipping region to white frame boundaries (viewport size, not full map size)
        // This prevents map content from rendering outside the white frame
        Rectangle clipRect = new Rectangle(0, 35, (50 * tileSize) / GameConstants.SCALING_FACTOR, (30 * tileSize) / GameConstants.SCALING_FACTOR);
        g2d.setClip(clipRect);

    // Render tiles with offset
    for (int x = 0; x < currentMap.get_width(); x++) {
        for (int y = 0; y < currentMap.get_height(); y++) {
            utilities.Tile tile = currentMap.get_tile(x, y);
            if (tile != null && (debugMode || tile.is_explored())) {
                render_tile(g2d, tile, mapOffsetX + x * tileSize, mapOffsetY + y * tileSize);
            }
        }
    }

        // Reset clipping to allow other elements to render normally
        g2d.setClip(null);

    // Draw white border around map boundaries with pixelated rounded corners
    // Draw white frame without camera offset so it stays fixed
    // Move it down by 35 pixels to avoid covering floor text
    drawMapBorder(g2d, tileSize, 0, 35);
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
// int tileSize = GameConstants.TILE_SIZE * 3;
int tileSize = GameConstants.TILE_SIZE;

switch (tile.get_tile_type()) {
case WALL:
g2d.drawImage(tileWall, screenX, screenY, tileSize, tileSize, null);
break;
case FLOOR:
g2d.drawImage(tileFloor, screenX, screenY, tileSize, tileSize, null);
break;
case ENTRANCE:
g2d.drawImage(tileEntrance, screenX, screenY, tileSize, tileSize, null);
break;
case BOSS_ROOM:
g2d.drawImage(tileBossRoom, screenX, screenY, tileSize, tileSize, null);
break;
case STAIRS:
g2d.drawImage(tileStairs, screenX, screenY, tileSize, tileSize, null);
break;
case UPGRADER_SPAWN:
g2d.drawImage(tileFloor, screenX, screenY, tileSize, tileSize, null);
break;
default:
g2d.setColor(Color.DARK_GRAY);
g2d.fillRect(screenX, screenY, tileSize, tileSize);

// Draw tile border
g2d.setColor(Color.BLACK);
g2d.drawRect(screenX, screenY, tileSize, tileSize);
}


// Draw items
if (tile.has_items()) {
    List<model.items.Item> items = tile.get_items();
    if (!items.isEmpty()) {
        model.items.Item item = items.get(0); // Draw the first item
        
        // Try to get the item's image
        java.awt.image.BufferedImage itemImage = null;
        
        if (item instanceof model.equipment.Weapon || item instanceof model.equipment.Armor) {
            // For equipment, use the equipment's image path
            model.equipment.Equipment equipment = (model.equipment.Equipment) item;
            String imagePath = equipment.get_image_path();
            if (itemIconCache.containsKey(imagePath)) {
                itemImage = itemIconCache.get(imagePath);
            } else {
                try {
                    itemImage = javax.imageio.ImageIO.read(getClass().getClassLoader().getResourceAsStream(imagePath));
                    itemIconCache.put(imagePath, itemImage);
                } catch (Exception e) {
                    // Image loading failed, will use fallback
                }
            }
        } else {
            // For consumables and key items, use the icon system
            itemImage = getIconForItem(item);
        }
        
        if (itemImage != null) {
            // Calculate size multiplier for potions
            float sizeMultiplier = 1.0f;
            if (item instanceof model.items.Consumable) {
                model.items.Consumable consumable = (model.items.Consumable) item;
                sizeMultiplier = consumable.getPotionSizeMultiplier();
            }
            
            // Draw the item image with size scaling
            int baseItemSize = tileSize - 4; // Slightly smaller than tile
            int itemSize = (int)(baseItemSize * sizeMultiplier);
            int offsetX = (tileSize - itemSize) / 2;
            int offsetY = (tileSize - itemSize) / 2;
            g2d.drawImage(itemImage, screenX + offsetX, screenY + offsetY, itemSize, itemSize, null);
        } else {
            // Fallback to colored dot if image not available
            Color itemColor = Color.YELLOW; // Default color
            
            if (item instanceof model.items.Consumable) {
                model.items.Consumable consumable = (model.items.Consumable) item;
                String effectType = consumable.get_effect_type();
                
                // Set color based on effect type
                switch (effectType) {
                    case "health":
                        itemColor = Color.RED;
                        break;
                    case "mana":
                        itemColor = Color.BLUE;
                        break;
                    case "experience":
                        itemColor = Color.GREEN;
                        break;
                    case "invisibility":
                        itemColor = Color.MAGENTA;
                        break;
                    case "clarity":
                        itemColor = Color.CYAN;
                        break;
                    case "swiftness":
                        itemColor = Color.ORANGE;
                        break;
                    case "immortality":
                        itemColor = Color.YELLOW;
                        break;
                    default:
                        itemColor = Color.YELLOW;
                        break;
                }
            } else if (item instanceof model.equipment.Weapon) {
                itemColor = Color.ORANGE;
            } else if (item instanceof model.equipment.Armor) {
                itemColor = Color.GRAY;
            } else if (item instanceof model.items.KeyItem) {
                itemColor = Color.PINK;
            }
            
            g2d.setColor(itemColor);
            g2d.fillOval(screenX + 4, screenY + 4, tileSize - 8, tileSize - 8);
        }
    }
}
}

/**
    * MANDATORY: Render characters and entities
    * 
    * @param g2d Graphics2D context
*/
private void render_entities(Graphics2D g2d) {
    // Update animation frame for all characters (player, enemies, bosses)
    updateAnimationFrame();
    
    int tileSize = GameConstants.TILE_SIZE;
    
    // Calculate map offset for entity positioning
    int mapOffsetX = getMapOffsetX();
    int mapOffsetY = getMapOffsetY();
    
    // Set clipping region to white frame boundaries for entities (viewport size, not full map size)
    // This prevents entities from rendering outside the white frame
    Rectangle clipRect = new Rectangle(0, 35, (50 * tileSize) / GameConstants.SCALING_FACTOR, (30 * tileSize) / GameConstants.SCALING_FACTOR);
    g2d.setClip(clipRect);
    
    // Draw aiming arc (always visible)
    if (player != null) {
        float px = mapToScreenX(player.getPixelX()) + tileSize / 2f;
        float py = mapToScreenY(player.getPixelY()) + tileSize / 2f;
        float range = player.getPlayerClassOOP().getRange();
        int width = player.getPlayerClassOOP().getAttackWidth();
        double angleCenter;
        if (mouseAimingMode) {
            // Always poll the real mouse position for the arc
            Point mouse = java.awt.MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen(mouse, this);
            float mx = mouse.x;
            float my = mouse.y;
            angleCenter = Math.atan2(my - py, mx - px);
        } else {
            // Use quantized aim direction for keyboard mode
            int dx = player.getAimDX();
            int dy = player.getAimDY();
            if (dx == 0 && dy == 0) { dx = player.getLastAimDX(); dy = player.getLastAimDY(); }
            if (dx == 0 && dy == 0) { dx = 0; dy = 1; }
            angleCenter = Math.atan2(dy, dx);
        }
        float arcRadius = range * tileSize;
        float arcDiameter = arcRadius * 2;
        double startAngle = Math.toDegrees(angleCenter) - width / 2.0;
        g2d.setColor(new Color(255, 0, 0, 128)); // 50% opacity red
        g2d.setStroke(new BasicStroke(3f));
        g2d.drawArc((int)(px - arcRadius), (int)(py - arcRadius), (int)(arcDiameter), (int)(arcDiameter), (int)-startAngle, -width);
        g2d.setStroke(new BasicStroke(1f));
    }
    // Draw attack fan (yellow arc, lasts 200ms)
    if (System.currentTimeMillis() - attackVisualTime < 200 && player != null && player.getPlayerClassOOP().hasMelee()) {
        float range = player.getPlayerClassOOP().getRange();
        float px = mapToScreenX(player.getPixelX()) + tileSize / 2f;
        float py = mapToScreenY(player.getPixelY()) + tileSize / 2f;
        
        // Check if we have swing-based attack data
        if (lastAttackData != null && lastAttackData.isSwingAttack()) {
            // Render swing-based attack
            renderSwingAttack(g2d, px, py, range, lastAttackData);
        } else {
            // Fallback to old static fan rendering
            double angleCenter = Math.atan2(lastAimDY, lastAimDX); // Use last aim direction
            int width = player.getPlayerClassOOP().getAttackWidth();
            float arcRadius = range * tileSize;
            float arcDiameter = arcRadius * 2;
            double startAngle = Math.toDegrees(angleCenter) - width / 2.0;
            g2d.setColor(new Color(255, 255, 0, 120));
            g2d.fillArc((int)(px - arcRadius), (int)(py - arcRadius), (int)(arcDiameter), (int)(arcDiameter), (int)-startAngle, -width);
        }
    }
    // Draw projectile (cyan line, lasts 200ms)
    if (projectileStart != null && projectileEnd != null && System.currentTimeMillis() - attackVisualTime < 200) {
        g2d.setColor(Color.CYAN);
        int x1 = mapToScreenX(projectileStart.get_x() * tileSize) + tileSize/2;
        int y1 = mapToScreenY(projectileStart.get_y() * tileSize) + tileSize/2;
        int x2 = mapToScreenX(projectileEnd.get_x() * tileSize) + tileSize/2;
        int y2 = mapToScreenY(projectileEnd.get_y() * tileSize) + tileSize/2;
        g2d.drawLine(x1, y1, x2, y2);
    }
    // Draw all active projectiles (skip during floor transition to avoid concurrent modification)
    if (!isFloorTransitioning) {
        model.gameLogic.GameLogic logic = (model.gameLogic.GameLogic) parentView.get_controller().get_model();
        if (logic != null) {
            for (Projectile p : logic.getProjectiles()) {
                // Apply parenting system to projectile rendering
                Graphics2D translatedG2d = (Graphics2D) g2d.create();
                translatedG2d.translate(getMapOffsetX(), getMapOffsetY());
                p.render(translatedG2d);
                translatedG2d.dispose();
            }
        }
    }
    // Render enemies (skip during floor transition to avoid concurrent modification)
    if (currentMap != null && parentView != null && parentView.get_controller() != null && !isFloorTransitioning) {
        try {
            // Try to get all enemies from the game logic
            java.util.List<model.characters.Enemy> enemies = null;
            try {
                enemies = new java.util.ArrayList<>(((model.gameLogic.GameLogic)parentView.get_controller().get_model()).get_current_enemies());
            } catch (Exception e) {}
            if (enemies != null) {
                java.util.List<model.characters.Enemy> enemiesCopy = new java.util.ArrayList<>(enemies);
                for (model.characters.Enemy enemy : enemiesCopy) {
                    // Skip bosses here - they are rendered separately in the boss section
                    if (enemy instanceof model.characters.Boss) {
                        continue;
                    }
                    
                    int ex = (int)(enemy.getPixelX() / tileSize);
                    int ey = (int)(enemy.getPixelY() / tileSize);
                    if (debugMode || (ex >= 0 && ey >= 0 && ex < currentMap.get_width() && ey < currentMap.get_height() && currentMap.get_tile(ex, ey).is_explored())) {
                        int x = (int)enemy.getPixelX();
                        int y = (int)enemy.getPixelY();
                        int size = enums.GameConstants.TILE_SIZE;
                        // Dying state: render as solid black
                        if (enemy.isDying()) {
                            if (deadImage != null) {
                                g2d.drawImage(deadImage, mapToScreenX(x), mapToScreenY(y), size, size, null);
                            } else {
                                g2d.setColor(java.awt.Color.BLACK);
                                g2d.fillOval(mapToScreenX(x), mapToScreenY(y), size, size);
                            }
                            continue;
                        }
                        // Draw border based on enemy state
                        if (enemy.isInHitState()) {
                            g2d.setColor(java.awt.Color.BLACK);
                        } else if (enemy.isInWindUpState()) {
                            // Cyan blinking border during wind-up
                            Long windUpStartTime = enemyWindUpStartTimes.get(enemy);
                            if (windUpStartTime != null && (System.currentTimeMillis() - windUpStartTime) < ENEMY_WIND_UP_WARNING_DURATION) {
                                long now = System.currentTimeMillis();
                                if ((now / 100) % 2 == 0) {
                                    g2d.setColor(java.awt.Color.CYAN);
                                } else {
                                    g2d.setColor(java.awt.Color.WHITE);
                                }
                            } else {
                                g2d.setColor(java.awt.Color.RED);
                            }
                        } else if (enemy.isInCelebratoryState()) {
                            // Blinking border during celebratory immunity period
                            if (enemy.isInCelebratoryImmunity()) {
                                long now = System.currentTimeMillis();
                                if ((now / 100) % 2 == 0) {
                                    g2d.setColor(java.awt.Color.YELLOW);
                                } else {
                                    g2d.setColor(java.awt.Color.WHITE);
                                }
                            } else {
                                g2d.setColor(java.awt.Color.YELLOW);
                            }
                        } else if (enemy.isInFallbackState()) {
                            g2d.setColor(java.awt.Color.BLUE);
                        } else {
                            g2d.setColor(java.awt.Color.RED);
                        }
                        // Flash color if immune
                        if (enemy.isImmune()) {
                            long now = System.currentTimeMillis();
                            if ((now / 100) % 2 == 0) {
                                g2d.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 0.3f));
                            } else {
                                g2d.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 1.0f));
                            }
                        }
                        /* COMMENTED OUT: Original circle rendering for safe rollback
                        g2d.fillOval(mapToScreenX(enemy.getPixelX()), mapToScreenY(enemy.getPixelY()), tileSize, tileSize);
                        g2d.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 1.0f));
                        // Draw class color
                        Color classColor;
                        String className = "";
                        if (enemy.getEnemyClassOOP() != null) {
                            className = enemy.getEnemyClassOOP().getClassName().toLowerCase();
                        }
                        switch (className) {
                            case "warrior": classColor = Color.BLUE; break;
                            case "mage": classColor = Color.ORANGE; break;
                            case "rogue": classColor = new Color(128, 0, 128); break;
                            case "ranger": classColor = Color.GREEN; break;
                            default: classColor = Color.GRAY; break;
                        }
                        g2d.setColor(classColor);
                        g2d.fillOval(mapToScreenX(enemy.getPixelX()) + 4, mapToScreenY(enemy.getPixelY()) + 4, tileSize - 8, tileSize - 8);
                        */
                        
                        // NEW: Universal character sprite rendering
                        renderEnemySprite(g2d, enemy);
                        
                        // Draw red exclamation mark if showing detection notification
                        if (enemy.isShowingDetectionNotification()) {
                            g2d.setColor(Color.RED);
                            g2d.setFont(new Font("Arial", Font.BOLD, 16));
                            String exclamationMark = "!";
                            int textX = mapToScreenX(enemy.getPixelX()) + tileSize / 2 - g2d.getFontMetrics().stringWidth(exclamationMark) / 2;
                            int textY = mapToScreenY(enemy.getPixelY()) - 10; // Position above the enemy
                            g2d.drawString(exclamationMark, textX, textY);
                        }
                        
                        // Draw chase path in debug mode
                        if (debugMode) {
                            java.util.List<int[]> path = enemy.getChasePath();
                            g2d.setColor(Color.MAGENTA);
                            for (int[] step : path) {
                                g2d.drawRect(mapToScreenX(step[0] * tileSize) + tileSize/4, mapToScreenY(step[1] * tileSize) + tileSize/4, tileSize/2, tileSize/2);
                            }
                            // Draw enemy detection (aggro) range as a yellow or purple circle
                            int aggroRange = enemy.get_aggro_range();
                            float enemyX = mapToScreenX(enemy.getPixelX()) + tileSize / 2f;
                            float enemyY = mapToScreenY(enemy.getPixelY()) + tileSize / 2f;
                            float aggroRadius = aggroRange * tileSize;
                            if (enemy.isChasingPlayer()) {
                                g2d.setColor(new Color(128, 0, 128, 128)); // semi-transparent purple
                            } else {
                                g2d.setColor(new Color(255, 255, 0, 128)); // semi-transparent yellow
                            }
                            g2d.setStroke(new BasicStroke(2f));
                            g2d.drawOval((int)(enemyX - aggroRadius), (int)(enemyY - aggroRadius), (int)(aggroRadius * 2), (int)(aggroRadius * 2));
                            g2d.setStroke(new BasicStroke(1f));
                            // Draw enemy aiming arc in debug mode
                            if (enemy.getEnemyClassOOP() != null) {
                                float range = enemy.getEnemyClassOOP().getRange();
                                int width = enemy.getEnemyClassOOP().getAttackWidth();
                                int aimDX = enemy.getAimDX();
                                int aimDY = enemy.getAimDY();
                                if (aimDX == 0 && aimDY == 0) { aimDX = 0; aimDY = 1; } // Default down
                                double angleCenter = Math.atan2(aimDY, aimDX);
                                
                                // Adjust range based on entity size and boss modifiers
                                float sizeMultiplier = 1.0f;
                                float rangeModifier = 1.0f;
                                if (enemy instanceof model.characters.Boss) {
                                    sizeMultiplier = ((model.characters.Boss) enemy).getSizeMultiplier();
                                    rangeModifier = ((model.characters.Boss) enemy).getRangeModifier();
                                }
                                float adjustedRange = range * sizeMultiplier * rangeModifier;
                                
                                float arcRadius = adjustedRange * tileSize;
                                float arcDiameter = arcRadius * 2;
                                double startAngle = Math.toDegrees(angleCenter) - width / 2.0;
                                g2d.setColor(new Color(255, 0, 0, 100)); // Semi-transparent red
                                g2d.setStroke(new BasicStroke(2f));
                                g2d.drawArc((int)(enemyX - arcRadius), (int)(enemyY - arcRadius), (int)(arcDiameter), (int)(arcDiameter), (int)-startAngle, -width);
                                g2d.setStroke(new BasicStroke(1f));
                            }
                        }
                    }
                }
            }
        } catch (java.util.ConcurrentModificationException e) {
            // Skip enemy rendering this frame if there's a concurrent modification
        }
    }
    
    // Draw enemy attack visuals (swing-based) - always show, not just debug mode
    if (currentMap != null && parentView != null && parentView.get_controller() != null) {
        java.util.List<model.characters.Enemy> enemies = null;
        try {
            enemies = new java.util.ArrayList<>(((model.gameLogic.GameLogic)parentView.get_controller().get_model()).get_current_enemies());
        } catch (Exception e) {}
        if (enemies != null) {
            java.util.List<model.characters.Enemy> enemiesCopy = new java.util.ArrayList<>(enemies);
            for (model.characters.Enemy enemy : enemiesCopy) {
                model.gameLogic.AttackVisualData enemyAttackData = enemySwingData.get(enemy);
                
                if (enemyAttackData != null && enemyAttackData.isSwingActive(System.currentTimeMillis()) && enemy.getEnemyClassOOP() != null && enemy.getEnemyClassOOP().hasMelee()) {
                    
                    float enemyX = mapToScreenX(enemy.getPixelX()) + enums.GameConstants.TILE_SIZE / 2f;
                    float enemyY = mapToScreenY(enemy.getPixelY()) + enums.GameConstants.TILE_SIZE / 2f;
                    float range = enemy.getEnemyClassOOP().getRange();
                    
                    // Adjust range based on entity size and boss modifiers
                    float sizeMultiplier = 1.0f;
                    float rangeModifier = 1.0f;
                    if (enemy instanceof model.characters.Boss) {
                        sizeMultiplier = ((model.characters.Boss) enemy).getSizeMultiplier();
                        rangeModifier = ((model.characters.Boss) enemy).getRangeModifier();
                    }
                    float adjustedRange = range * sizeMultiplier * rangeModifier;
                    
                    // Render enemy swing attack using unified weapon rendering system
                    model.equipment.Weapon enemyWeapon = enemy.get_equipped_weapon();
                    renderWeaponSwingAttack(g2d, enemyX, enemyY, adjustedRange, enemyAttackData, enemyWeapon, true);
                }
                

            }
        }
    }
    
    // Render enemy Ranger bow attacks (for projectile attacks) - AFTER all enemy sprites are rendered
    if (currentMap != null && parentView != null && parentView.get_controller() != null && !isFloorTransitioning) {
        try {
            java.util.List<model.characters.Enemy> enemies = null;
            try {
                enemies = new java.util.ArrayList<>(((model.gameLogic.GameLogic)parentView.get_controller().get_model()).get_current_enemies());
            } catch (Exception e) {}
            if (enemies != null) {
                java.util.List<model.characters.Enemy> enemiesCopy = new java.util.ArrayList<>(enemies);
                for (model.characters.Enemy enemy : enemiesCopy) {
                    // Skip bosses here - they are rendered separately in the boss section
                    if (enemy instanceof model.characters.Boss) {
                        continue;
                    }
                    
                    // Render enemy Ranger bow attacks (for projectile attacks)
                    model.gameLogic.AttackVisualData enemyRangerBowAttackData = enemyRangerBowData.get(enemy);
                    if (enemyRangerBowAttackData != null && enemyRangerBowAttackData.isSwingActive(System.currentTimeMillis()) && 
                        enemy.getEnemyClassOOP() != null && enemy.getEnemyClassOOP().hasProjectile() && 
                        enemy.getEnemyClassOOP() instanceof model.characters.RangerClass) {
                        
                        float enemyX = mapToScreenX(enemy.getPixelX()) + enums.GameConstants.TILE_SIZE / 2f;
                        float enemyY = mapToScreenY(enemy.getPixelY()) + enums.GameConstants.TILE_SIZE / 2f;
                        double aimAngle = enemyRangerBowAttackData.getCurrentSwingAngle(System.currentTimeMillis());
                        
                        // Render enemy Ranger bow using unified bow rendering system
                        model.equipment.Weapon enemyWeapon = enemy.get_equipped_weapon();
                        renderRangerBow(g2d, enemyX, enemyY, aimAngle, enemyWeapon, true);
                    }
                }
            }
        } catch (Exception e) {
            // Handle any exceptions during enemy bow rendering
        }
    }
    
    // Render player Ranger bow attacks (for projectile attacks)
    if (playerRangerBowData != null && playerRangerBowData.isSwingActive(System.currentTimeMillis()) && 
        player != null && player.getPlayerClassOOP() != null && player.getPlayerClassOOP().hasProjectile() && 
        player.getPlayerClassOOP() instanceof model.characters.RangerClass) {
        
        float playerX = mapToScreenX(player.getPixelX()) + enums.GameConstants.TILE_SIZE / 2f;
        float playerY = mapToScreenY(player.getPixelY()) + enums.GameConstants.TILE_SIZE / 2f;
        double aimAngle = playerRangerBowData.getCurrentSwingAngle(System.currentTimeMillis());
        
        // Render player Ranger bow using unified bow rendering system
        model.equipment.Weapon playerWeapon = player.get_equipped_weapon();
        renderRangerBow(g2d, playerX, playerY, aimAngle, playerWeapon, false);
    }
    // Render player
    if (player != null) {
        // Draw golden glow for immortality effect
        if (player.is_immortality_effect_active()) {
            // Draw golden glow circle around player
            int glowSize = 32; // Larger than player
            int glowX = mapToScreenX(player.getPixelX()) - glowSize/2 + tileSize/2;
            int glowY = mapToScreenY(player.getPixelY()) - glowSize/2 + tileSize/2;
            
            // Create radial gradient for glow effect
            java.awt.GradientPaint glowGradient = new java.awt.GradientPaint(
                glowX + glowSize/2, glowY + glowSize/2, new Color(255, 255, 0, 100), // Center: semi-transparent gold
                glowX + glowSize/2, glowY + glowSize/2, new Color(255, 255, 0, 0)    // Edge: transparent
            );
            g2d.setPaint(glowGradient);
            g2d.fillOval(glowX, glowY, glowSize, glowSize);
            g2d.setPaint(null); // Reset paint
        }
        
        // Apply invisibility transparency (50% opacity)
        if (player.is_invisibility_effect_active()) {
            g2d.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 0.5f));
        }
        
        // Flash color if immune
        if (player.isImmune()) {
            long now = System.currentTimeMillis();
            if ((now / 100) % 2 == 0) {
                g2d.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 0.3f));
            } else {
                g2d.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 1.0f));
            }
        }
        
        // OLD RENDERING (COMMENTED OUT): Player now rendered using animated sprites instead of circles
        /*
        g2d.setColor(getPlayerColor());
        g2d.fillOval(
            mapToScreenX(player.getPixelX()) + 2,
            mapToScreenY(player.getPixelY()) + 2,
            tileSize - 4,
            tileSize - 4
        );
        */
        
        // NEW RENDERING: Player rendered using animated sprites
        renderPlayerSprite(g2d);
        
        g2d.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 1.0f));
    }
    
    // Render boss (if exists)
    try {
        model.characters.Boss boss = ((model.gameLogic.GameLogic)parentView.get_controller().get_model()).get_current_boss();
        if (boss != null && boss.is_alive()) {
            int bx = (int)(boss.getPixelX() / tileSize);
            int by = (int)(boss.getPixelY() / tileSize);
            if (debugMode || (bx >= 0 && by >= 0 && bx < currentMap.get_width() && by < currentMap.get_height())) {
                int x = (int)boss.getPixelX();
                int y = (int)boss.getPixelY();
                float sizeMultiplier = boss.getSizeMultiplier();
                int bossSize = (int)(tileSize * sizeMultiplier);
                int offsetX = (int)((tileSize - bossSize) / 2f);
                int offsetY = (int)((tileSize - bossSize) / 2f);
                
                // Dying state: render as dead image
                if (boss.isDying()) {
                    if (deadImage != null) {
                        g2d.drawImage(deadImage, mapToScreenX(x) + offsetX, mapToScreenY(y) + offsetY, bossSize, bossSize, null);
                    } else {
                        g2d.setColor(java.awt.Color.BLACK);
                        g2d.fillOval(mapToScreenX(x) + offsetX, mapToScreenY(y) + offsetY, bossSize, bossSize);
                    }
                } else {
                    // No borders for bosses - removed all border drawing
                    // Bosses will only show their sprite without any rings
                    
                    // Apply immunity transparency effect
                    if (boss.isImmune()) {
                        long now = System.currentTimeMillis();
                        if ((now / 100) % 2 == 0) {
                            g2d.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 0.3f));
                        } else {
                            g2d.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 1.0f));
                        }
                    }
                    
                    // Render boss sprite with proper animation
                    renderBossSprite(g2d, boss);
                    
                    // Reset composite
                    g2d.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 1.0f));
                    
                    // Draw red exclamation mark if showing detection notification
                    if (boss.isShowingDetectionNotification()) {
                        g2d.setColor(Color.RED);
                        g2d.setFont(new Font("Arial", Font.BOLD, 20)); // Larger font for boss
                        String exclamationMark = "!";
                        int textX = mapToScreenX(x) + offsetX + bossSize / 2 - g2d.getFontMetrics().stringWidth(exclamationMark) / 2;
                        int textY = mapToScreenY(y) + offsetY - 15; // Position above the boss
                        g2d.drawString(exclamationMark, textX, textY);
                    }
                    
                    // Draw chase path in debug mode
                    if (debugMode) {
                        java.util.List<int[]> path = boss.getChasePath();
                        g2d.setColor(Color.MAGENTA);
                        for (int[] step : path) {
                            g2d.drawRect(mapToScreenX(step[0] * tileSize) + tileSize/4, mapToScreenY(step[1] * tileSize) + tileSize/4, tileSize/2, tileSize/2);
                        }
                        
                        // Draw boss detection (aggro) range as a yellow or purple circle
                        int aggroRange = boss.get_aggro_range();
                        float bossX = mapToScreenX(x) + offsetX + bossSize / 2f;
                        float bossY = mapToScreenY(y) + offsetY + bossSize / 2f;
                        float aggroRadius = aggroRange * tileSize;
                        if (boss.isChasingPlayer()) {
                            g2d.setColor(new Color(128, 0, 128, 128)); // semi-transparent purple
                        } else {
                            g2d.setColor(new Color(255, 255, 0, 128)); // semi-transparent yellow
                        }
                        g2d.setStroke(new BasicStroke(2f));
                        g2d.drawOval((int)(bossX - aggroRadius), (int)(bossY - aggroRadius), (int)(aggroRadius * 2), (int)(aggroRadius * 2));
                        g2d.setStroke(new BasicStroke(1f));
                    }
                }
            }
        }
    } catch (Exception e) {
        // Boss not available or error occurred
    }

    // Render upgrader NPC
    try {
        model.characters.Upgrader upgrader = ((model.gameLogic.GameLogic)parentView.get_controller().get_model()).get_current_upgrader();
        if (upgrader != null && upgrader.isVisible()) {
            int ux = (int)(upgrader.getPixelX() / tileSize);
            int uy = (int)(upgrader.getPixelY() / tileSize);
            if (debugMode || (ux >= 0 && uy >= 0 && ux < currentMap.get_width() && uy < currentMap.get_height() && currentMap.get_tile(ux, uy).is_explored())) {
                int x = (int)upgrader.getPixelX();
                int y = (int)upgrader.getPixelY();
                int size = enums.GameConstants.TILE_SIZE;
                
                // Draw field of view in debug mode
                if (debugMode) {
                    int fovRange = upgrader.getFieldOfViewRange();
                    float upgraderX = mapToScreenX(x) + size / 2f;
                    float upgraderY = mapToScreenY(y) + size / 2f;
                    float fovRadius = fovRange * tileSize;
                    
                    // Draw field of view circle
                    g2d.setColor(new Color(255, 255, 0, 80)); // Semi-transparent yellow
                    g2d.setStroke(new BasicStroke(2f));
                    g2d.drawOval((int)(upgraderX - fovRadius), (int)(upgraderY - fovRadius), 
                               (int)(fovRadius * 2), (int)(fovRadius * 2));
                    g2d.setStroke(new BasicStroke(1f));
                }
                
                // Apply transparency (but always fully visible in debug mode)
                float alpha = debugMode ? 1.0f : upgrader.getTransparency();
                g2d.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, alpha));
                
                int screenX = mapToScreenX(x);
                int screenY = mapToScreenY(y);
                
                // Draw the upgrader image based on type
                java.awt.image.BufferedImage currentUpgraderImage = null;
                if (upgrader.getUpgraderType() == model.characters.Upgrader.UpgraderType.WEAPON) {
                    currentUpgraderImage = weaponUpgraderImage;
                } else {
                    currentUpgraderImage = armorUpgraderImage;
                }
                
                if (currentUpgraderImage != null) {
                    g2d.drawImage(currentUpgraderImage, screenX, screenY, size, size, null);
                } else {
                    // Fallback to rhombus if image not available
                    int centerX = screenX + size / 2;
                    int centerY = screenY + size / 2;
                    int rhombusSize = (int) (size * 0.7);
                    int half = rhombusSize / 2;
                    
                    // Border color
                    Color borderColor = upgrader.getUpgraderType() == model.characters.Upgrader.UpgraderType.WEAPON ? new Color(255, 140, 0) : new Color(0, 220, 80);
                    g2d.setColor(borderColor);
                    int[] bx = {centerX, centerX + half, centerX, centerX - half};
                    int[] by = {centerY - half, centerY, centerY + half, centerY};
                    g2d.setStroke(new BasicStroke(4f));
                    g2d.drawPolygon(bx, by, 4);
                    
                    // Fill rhombus
                    g2d.setColor(new Color(255, 105, 180)); // Pink
                    g2d.setStroke(new BasicStroke(1f));
                    g2d.fillPolygon(bx, by, 4);
                }
                
                // Reset alpha
                g2d.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 1.0f));
            }
        }
    } catch (Exception e) {}
    
    // Reset clipping to allow other elements to render normally
    g2d.setClip(null);
}

/**
    * MANDATORY: Render UI elements
    * 
    * @param g2d Graphics2D context
*/
private void render_ui(Graphics2D g2d) {
    Font pixelFont = parentView.getPixelFont();
    
    // Draw floor number at the top of the screen
    g2d.setColor(Color.WHITE);
    g2d.setFont(pixelFont.deriveFont(18f)); // Large font for floor number
    String floorText;
    
    // Always show the floor number
    floorText = "Floor " + currentFloorNumber;
    
    int floorWidth = g2d.getFontMetrics().stringWidth(floorText);
    int floorX = getWidth() - floorWidth - 20;
    g2d.drawString(floorText, floorX, 30);
    
    // Draw floor type indicator (image) to the left of floor number
    if (parentView != null && parentView.get_controller() != null && parentView.get_controller().get_model() != null) {
        model.gameLogic.GameLogic gameLogic = (model.gameLogic.GameLogic) parentView.get_controller().get_model();
        model.map.Map.FloorType floorType = gameLogic.getCurrentFloorType();
        
        int imageSize = 24; // Increased from 16 to 24 for better visibility
        int imageX = floorX - imageSize - 5; // 5px gap between image and text
        int imageY = 30 - imageSize - 2; // Move image higher up
        
        if (floorType == model.map.Map.FloorType.BOSS) {
            // Boss room image for boss floor
            if (bossRoomImage != null) {
                g2d.drawImage(bossRoomImage, imageX, imageY, imageSize, imageSize, null);
            } else {
                // Fallback to red square if image not available
                g2d.setColor(Color.RED);
                g2d.fillRect(imageX, imageY, imageSize, imageSize);
            }
        } else if (floorType == model.map.Map.FloorType.BONUS) {
            // Bonus room image for bonus floor
            if (bonusRoomImage != null) {
                g2d.drawImage(bonusRoomImage, imageX, imageY, imageSize, imageSize, null);
            } else {
                // Fallback to pink square if image not available
                g2d.setColor(Color.PINK);
                g2d.fillRect(imageX, imageY, imageSize, imageSize);
            }
        }
        // No image for regular floors
    }
    
    // Attack rate circle will be drawn next to level number in the player stats section
    
    // Draw control mode indicator where debug stats would go
    g2d.setColor(mouseAimingMode ? Color.CYAN : Color.YELLOW);
    g2d.setFont(pixelFont.deriveFont(11f));
    String modeText = mouseAimingMode ? "MOUSE MODE" : "KEYBOARD MODE";
    g2d.drawString(modeText, 10, 20);
    
    // Draw "Press M to switch controls" text below mode indicator
    g2d.setColor(Color.WHITE);
    g2d.setFont(pixelFont.deriveFont(8f));
    String switchText = "Press M to switch controls";
    g2d.drawString(switchText, 10, 30);
    
    if (player != null) {
        g2d.setColor(Color.WHITE);
        g2d.setFont(pixelFont.deriveFont(12f)); // Smaller font for stats
        int y = getHeight() - 180;
        int lineHeight = g2d.getFontMetrics().getHeight() + 2;
        // Draw HP amount and bar on same line
        g2d.setFont(pixelFont.deriveFont(10f)); // Smaller font for HP/MP amounts
        g2d.drawString("HP: " + player.get_current_hp() + "/" + player.get_max_hp(), 10, y);
        drawResourceBar(g2d, 150, y - 12, 80, 8, 
                       player.get_current_hp(), player.get_max_hp(), Color.RED);
        // Draw "Clarity" text in pink if clarity effect is active
        // Draw active effects with 2-column layout
        int effectY = y;
        int effectSpacing = 25; // Increased space between effects
        int columnWidth = 120; // Width of each column
        int currentColumn = 0; // 0 = left column, 1 = right column
        int effectsInCurrentColumn = 0;
        
        // Draw "Clarity" text in orange if clarity effect is active
        if (player.is_clarity_effect_active()) {
            int effectX = 240 + (currentColumn * columnWidth);
            
            // Draw clarity state image to the left of the text
            if (clarityImage != null) {
                int imageSize = 20;
                int imageX = effectX;
                int imageY = effectY - 15; // Align with text
                g2d.drawImage(clarityImage, imageX, imageY, imageSize, imageSize, null);
            }
            
            g2d.setColor(Color.ORANGE);
            g2d.setFont(pixelFont.deriveFont(10f));
            g2d.drawString("Clarity", effectX + 25, effectY); // Push text to the right
            
            // Draw clarity duration bar
            float clarityProgress = player.get_clarity_effect_progress();
            drawClarityBar(g2d, effectX, effectY + 5, 80, 6, 
                          (int)(clarityProgress * 100), 100, Color.ORANGE);
            g2d.setColor(Color.WHITE); // Reset color
            effectsInCurrentColumn++;
            if (effectsInCurrentColumn >= 2) {
                currentColumn = 1 - currentColumn; // Switch columns
                effectY = y; // Reset to top
                effectsInCurrentColumn = 0;
            } else {
                effectY += effectSpacing;
            }
        }
        
        // Draw "Vanish" text in dark purple if invisibility effect is active
        if (player.is_invisibility_effect_active()) {
            int effectX = 240 + (currentColumn * columnWidth);
            
            // Draw vanish state image to the left of the text
            if (vanishImage != null) {
                int imageSize = 20;
                int imageX = effectX;
                int imageY = effectY - 15; // Align with text
                g2d.drawImage(vanishImage, imageX, imageY, imageSize, imageSize, null);
            }
            
            g2d.setColor(new Color(128, 0, 128)); // Dark purple
            g2d.setFont(pixelFont.deriveFont(10f));
            g2d.drawString("Vanish", effectX + 25, effectY); // Push text to the right
            
            // Draw invisibility duration bar
            float invisibilityProgress = player.get_invisibility_effect_progress();
            drawClarityBar(g2d, effectX, effectY + 5, 80, 6, 
                          (int)(invisibilityProgress * 100), 100, new Color(128, 0, 128));
            g2d.setColor(Color.WHITE); // Reset color
            effectsInCurrentColumn++;
            if (effectsInCurrentColumn >= 2) {
                currentColumn = 1 - currentColumn; // Switch columns
                effectY = y; // Reset to top
                effectsInCurrentColumn = 0;
            } else {
                effectY += effectSpacing;
            }
        }
        
        // Draw "Swiftness" text in turquoise if swiftness effect is active
        if (player.is_swiftness_effect_active()) {
            int effectX = 240 + (currentColumn * columnWidth);
            
            // Draw swiftness state image to the left of the text
            if (swiftnessImage != null) {
                int imageSize = 20;
                int imageX = effectX;
                int imageY = effectY - 15; // Align with text
                g2d.drawImage(swiftnessImage, imageX, imageY, imageSize, imageSize, null);
            }
            
            g2d.setColor(new Color(64, 224, 208)); // Turquoise green
            g2d.setFont(pixelFont.deriveFont(10f));
            g2d.drawString("Swiftness", effectX + 25, effectY); // Push text to the right
            
            // Draw swiftness duration bar
            float swiftnessProgress = player.get_swiftness_effect_progress();
            drawClarityBar(g2d, effectX, effectY + 5, 80, 6, 
                          (int)(swiftnessProgress * 100), 100, new Color(64, 224, 208));
            g2d.setColor(Color.WHITE); // Reset color
            effectsInCurrentColumn++;
            if (effectsInCurrentColumn >= 2) {
                currentColumn = 1 - currentColumn; // Switch columns
                effectY = y; // Reset to top
                effectsInCurrentColumn = 0;
            } else {
                effectY += effectSpacing;
            }
        }
        
        // Draw "Undying" text in gold if immortality effect is active
        if (player.is_immortality_effect_active()) {
            int effectX = 240 + (currentColumn * columnWidth);
            
            // Draw undying state image to the left of the text
            if (undyingImage != null) {
                int imageSize = 20;
                int imageX = effectX;
                int imageY = effectY - 15; // Align with text
                g2d.drawImage(undyingImage, imageX, imageY, imageSize, imageSize, null);
            }
            
            g2d.setColor(Color.YELLOW); // Gold color
            g2d.setFont(pixelFont.deriveFont(10f));
            g2d.drawString("Undying", effectX + 25, effectY); // Push text to the right
            
            // Draw immortality duration bar
            float immortalityProgress = player.get_immortality_effect_progress();
            drawClarityBar(g2d, effectX, effectY + 5, 80, 6, 
                          (int)(immortalityProgress * 100), 100, Color.YELLOW);
            g2d.setColor(Color.WHITE); // Reset color
        }
        
        // Draw white border around status effects panel
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new java.awt.BasicStroke(2f));
        int statusPanelX = 240;
        int statusPanelY = y - 20;
        int statusPanelWidth = 240; // 2 columns * 120 width
        int statusPanelHeight = 80; // Approximate height for status effects
        g2d.drawRect(statusPanelX, statusPanelY, statusPanelWidth, statusPanelHeight);
        
        // Draw Stats Panel (right next to status panel) - balanced single row layout
        int statsPanelX = statusPanelX + statusPanelWidth + 10; // 10px gap between panels
        int statsPanelY = statusPanelY;
        int imageSize = 28; // Medium-sized icons (between 24px and 32px)
        int imagePadding = 30; // Increased spacing between buttons to prevent overlap
        int imagesPerRow = 6; // All 6 buttons in one row
        int startX = statsPanelX + 8; // Reduced left padding (Health closer to border)
        int startY = statsPanelY + 38; // Medium top spacing for label (between 32px and 44px)
        
        // Calculate balanced panel width: 6 buttons * (28px icon + 30px padding) + 8px left padding + 8px right padding
        int statsPanelWidth = (imagesPerRow * (imageSize + imagePadding)) - imagePadding + 16; // 16 = 8px left + 8px right padding
        int statsPanelHeight = 90; // Increased height to accommodate stat values under icons
        
        // Draw yellow border around stats panel if in navigation mode (like inventory highlight)
        if (parentView.getCurrentState() == enums.GameState.PLAYING && parentView.isStatsNavigationMode()) {
            g2d.setColor(new Color(255, 255, 100)); // Same yellow as inventory
            g2d.setStroke(new java.awt.BasicStroke(3f)); // Thinner border
            g2d.drawRoundRect(statsPanelX - 5, statsPanelY - 5, statsPanelWidth + 9, statsPanelHeight + 9, 18, 18); // Pushed outwards slightly
            g2d.setStroke(new java.awt.BasicStroke(1f)); // Reset stroke
        }
        
        // Draw "Stats" label at the top (like "Inventory" in inventory panel)
        g2d.setFont(pixelFont.deriveFont(11f)); // Medium header text (between 10f and 12f)
        g2d.setColor(new Color(100, 200, 255)); // Light blue like inventory
        g2d.drawString("Stats", statsPanelX + 15, statsPanelY + 13);
        
        // Draw hover text next to "Stats" if hovering over a button
        if (statsHoveredIndex >= 0 && statsHoveredIndex < 6) {
            String[] buttonLabels = {"Health", "Attack", "Defense", "Range", "Speed", "Mana"};
            String hoverText = "Increase " + buttonLabels[statsHoveredIndex] + " By 10%";
            g2d.setFont(pixelFont.deriveFont(9f)); // Smaller font for hover text
            g2d.setColor(Color.YELLOW); // Yellow color for hover text
            g2d.drawString(hoverText, statsPanelX + 80, statsPanelY + 13); // Moved further right to avoid overlap
        }
        

        
        String[] buttonLabels = {"Health", "Attack", "Defense", "Range", "Speed", "Mana"};
        java.awt.image.BufferedImage[] buttonImages = {healthImage, attackImage, defenseImage, rangeImage, speedImage, manaImage};
        
        for (int i = 0; i < 6; i++) {
            // Skip Mana button if player has no MP
            if (i == 5 && (player == null || player.getPlayerClassOOP().getBaseMp() <= 0)) {
                continue;
            }
            
            int col = i; // Single row, so col = i
            int imageX = startX + (col * (imageSize + imagePadding));
            int imageY = startY;
            
            // Draw yellow border if hovered (mouse) or in keyboard nav mode
            boolean highlight = false;
            if (parentView.getCurrentState() == enums.GameState.PLAYING && parentView.isStatsNavigationMode()) {
                int navRow = parentView.getStatsNavRow();
                int navCol = parentView.getStatsNavCol();
                if (navRow >= 0 && navCol >= 0 && i == navRow * 6 + navCol) highlight = true;
            }
            if (highlight) {
                g2d.setColor(Color.YELLOW);
                g2d.drawRect(imageX - 6, imageY - 6, imageSize + 11, imageSize + 11); // Match inventory highlight style
            }
            
            // Draw golden background for maxed stats (5 uses)
            if (player != null) {
                boolean isMaxed = false;
                switch (i) {
                    case 0: // Health
                        isMaxed = player.get_health_uses() >= 5;
                        break;
                    case 1: // Attack
                        isMaxed = player.get_attack_uses() >= 5;
                        break;
                    case 2: // Defense
                        isMaxed = player.get_defense_uses() >= 5;
                        break;
                    case 3: // Range
                        isMaxed = player.get_range_uses() >= 5;
                        break;
                    case 4: // Speed
                        isMaxed = player.get_speed_uses() >= 5;
                        break;
                    case 5: // Mana
                        isMaxed = player.get_mana_uses() >= 5;
                        break;
                }
                
                if (isMaxed) {
                    // Draw golden, pixelated, rounded background
                    g2d.setColor(new Color(255, 215, 0)); // Golden color
                    g2d.fillRoundRect(imageX - 4, imageY - 4, imageSize + 8, imageSize + 8, 8, 8);
                    // Draw golden border
                    g2d.setColor(new Color(218, 165, 32)); // Darker gold for border
                    g2d.setStroke(new java.awt.BasicStroke(2f));
                    g2d.drawRoundRect(imageX - 4, imageY - 4, imageSize + 8, imageSize + 8, 8, 8);
                    g2d.setStroke(new java.awt.BasicStroke(1f)); // Reset stroke
                }
            }
            
            // Draw stat name on top of the icon (medium white text)
            g2d.setFont(pixelFont.deriveFont(8f)); // Medium font size (between 7f and 8f)
            g2d.setColor(Color.WHITE); // White text instead of green
            String statName = buttonLabels[i];
            // Center the text above the icon
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(statName);
            int textX = imageX + (imageSize - textWidth) / 2;
            int textY = imageY - 6; // 6px above the icon (moved up to avoid selection square)
            g2d.drawString(statName, textX, textY);
            
            // Draw stat image (if available)
            if (buttonImages[i] != null) {
                g2d.drawImage(buttonImages[i], imageX, imageY, imageSize, imageSize, null);
            }
            
            // Draw stat value under the icon (smaller white text)
            if (player != null) {
                g2d.setFont(pixelFont.deriveFont(7f)); // Smaller font for values
                g2d.setColor(Color.WHITE);
                String statValue = "";
                
                // Get the appropriate stat value based on button index
                switch (i) {
                    case 0: // Health
                        statValue = String.valueOf(player.get_max_hp()); // Total only
                        break;
                    case 1: // Attack
                        statValue = String.valueOf((int)player.get_base_atk());
                        break;
                    case 2: // Defense
                        statValue = String.format("%.1f", player.getPlayerClassOOP().getDefense());
                        break;
                    case 3: // Range
                        statValue = String.format("%.1f", player.getPlayerClassOOP().getRange());
                        break;
                    case 4: // Speed
                        statValue = String.format("%.1f", player.getPlayerClassOOP().getMoveSpeed());
                        break;
                    case 5: // Mana
                        if (player.getPlayerClassOOP().getBaseMp() > 0) {
                            statValue = String.valueOf(player.get_max_mp());
                        }
                        break;
                }
                
                // Draw stat value with equipment modifiers
                FontMetrics fmValue = g2d.getFontMetrics();
                
                // Check if we have equipment modifiers to display
                boolean hasModifiers = false;
                String baseValue = "";
                String modifierValue = "";
                Color modifierColor = Color.WHITE;
                
                if (i == 1) { // Attack
                    int currentBaseAttack = (int)player.get_base_atk();
                    float attackEquipmentModifier = player.getEquipmentAttackModifier();
                    
                    if (Math.abs(attackEquipmentModifier) > 0.01f) {
                        hasModifiers = true;
                        baseValue = String.valueOf(currentBaseAttack);
                        modifierValue = (attackEquipmentModifier > 0 ? "+" : "") + String.valueOf((int)attackEquipmentModifier);
                        modifierColor = attackEquipmentModifier > 0 ? new Color(100, 150, 255) : Color.RED; // Blue for positive, red for negative
                    }
                } else if (i == 2) { // Defense
                    float currentBaseDefense = player.getPlayerClassOOP().getDefense();
                    float defenseEquipmentModifier = player.getEquipmentDefenseModifier();
                    
                    if (Math.abs(defenseEquipmentModifier) > 0.01f) {
                        hasModifiers = true;
                        baseValue = String.format("%.1f", currentBaseDefense);
                        modifierValue = (defenseEquipmentModifier > 0 ? "+" : "") + String.format("%.1f", defenseEquipmentModifier);
                        modifierColor = defenseEquipmentModifier > 0 ? new Color(100, 150, 255) : Color.RED; // Blue for positive, red for negative
                    }
                } else if (i == 3) { // Range
                    float currentRange = player.getPlayerClassOOP().getRange();
                    float rangeEquipmentModifier = player.getEquipmentRangeModifier();
                    
                    if (Math.abs(rangeEquipmentModifier) > 0.01f) {
                        hasModifiers = true;
                        baseValue = String.format("%.1f", currentRange);
                        modifierValue = (rangeEquipmentModifier > 0 ? "+" : "") + String.format("%.1f", rangeEquipmentModifier);
                        modifierColor = rangeEquipmentModifier > 0 ? new Color(100, 150, 255) : Color.RED; // Blue for positive, red for negative
                    }
                } else if (i == 4) { // Speed
                    float currentSpeed = player.getPlayerClassOOP().getMoveSpeed();
                    float speedEquipmentModifier = player.getEquipmentSpeedModifier();
                    
                    if (Math.abs(speedEquipmentModifier) > 0.01f) {
                        hasModifiers = true;
                        baseValue = String.format("%.1f", currentSpeed);
                        modifierValue = (speedEquipmentModifier > 0 ? "+" : "") + String.format("%.1f", speedEquipmentModifier);
                        modifierColor = speedEquipmentModifier > 0 ? new Color(100, 150, 255) : Color.RED; // Blue for positive, red for negative
                    }
                } else if (i == 5) { // Mana
                    if (player.getPlayerClassOOP().getBaseMp() > 0) {
                        int currentMana = player.get_max_mp();
                        float manaEquipmentModifier = player.getEquipmentManaModifier();
                        
                        if (Math.abs(manaEquipmentModifier) > 0.01f) {
                            hasModifiers = true;
                            baseValue = String.valueOf(currentMana);
                            modifierValue = (manaEquipmentModifier > 0 ? "+" : "") + String.valueOf((int)manaEquipmentModifier);
                            modifierColor = manaEquipmentModifier > 0 ? new Color(100, 150, 255) : Color.RED; // Blue for positive, red for negative
                        }
                    }
                }
                
                if (hasModifiers) {
                    // Draw base value in white
                    g2d.setColor(Color.WHITE);
                    int baseTextWidth = fmValue.stringWidth(baseValue);
                    int baseTextX = imageX + (imageSize - baseTextWidth) / 2;
                    int baseTextY = imageY + imageSize + 14;
                    g2d.drawString(baseValue, baseTextX, baseTextY);
                    
                    // Draw modifier in colored text UNDER the base value
                    g2d.setColor(modifierColor);
                    int modifierTextWidth = fmValue.stringWidth(modifierValue);
                    int modifierTextX = imageX + (imageSize - modifierTextWidth) / 2;
                    int modifierTextY = baseTextY + 10; // 10 pixels below base value (reduced from 16)
                    g2d.drawString(modifierValue, modifierTextX, modifierTextY);
                } else {
                    // Draw single value in white (no modifiers)
                    g2d.setColor(Color.WHITE);
                    int valueTextWidth = fmValue.stringWidth(statValue);
                    int valueTextX = imageX + (imageSize - valueTextWidth) / 2;
                    int valueTextY = imageY + imageSize + 14;
                    g2d.drawString(statValue, valueTextX, valueTextY);
                }
            }
        }
        
        g2d.setStroke(new java.awt.BasicStroke(1f)); // Reset stroke
        
        g2d.setFont(pixelFont.deriveFont(12f)); // Reset font size
        if (player.getPlayerClassOOP().getBaseMp() > 0) {
            g2d.setColor(Color.WHITE);
            // Draw MP amount and bar on same line
            g2d.setFont(pixelFont.deriveFont(10f)); // Smaller font for HP/MP amounts
            g2d.drawString("MP: " + player.get_current_mp() + "/" + player.get_max_mp(), 10, y + lineHeight);
            drawResourceBar(g2d, 150, y + lineHeight - 12, 80, 8, 
                           player.get_current_mp(), player.get_max_mp(), Color.BLUE);
            // Draw experience bar first
            g2d.setFont(pixelFont.deriveFont(10f)); // Smaller font for exp text
            g2d.setColor(Color.WHITE); // Reset color to white for XP text
            g2d.drawString("XP: " + player.get_current_exp() + "/" + player.get_total_exp(), 10, y + 2 * lineHeight);
            if (player.get_level() >= enums.GameConstants.MAX_LEVEL) {
                // At max level, show golden full bar
                drawResourceBar(g2d, 150, y + 2 * lineHeight - 12, 80, 8, 
                               player.get_total_exp(), player.get_total_exp(), new Color(255, 215, 0));
            } else {
                drawResourceBar(g2d, 150, y + 2 * lineHeight - 12, 80, 8, 
                               player.get_current_exp(), player.get_total_exp(), Color.GREEN);
            }
            
            // Draw level number underneath the EXP bar
            g2d.setFont(pixelFont.deriveFont(12f)); // Reset font size
            if (player.get_level() >= enums.GameConstants.MAX_LEVEL) {
                g2d.setColor(new Color(255, 215, 0)); // Golden color for max level
            } else {
                g2d.setColor(Color.WHITE);
            }
            g2d.drawString("Level: " + player.get_level(), 10, y + 2 * lineHeight + 20);
            
            // Draw level points underneath the level number
            g2d.setFont(pixelFont.deriveFont(10f)); // Smaller font for level points
            g2d.setColor(Color.GREEN); // Green color for level points
            
            // Draw level points icon
            if (levelPointsImage != null) {
                g2d.drawImage(levelPointsImage, 10, y + 2 * lineHeight + 20, 42, 42, null); // 42x42 icon (3x larger) - moved up more
            }
            
            // Draw level points text next to the icon
            g2d.drawString("Level points: " + player.get_level_points(), 58, y + 2 * lineHeight + 45); // Positioned next to larger icon - aligned with image center
            

            
            // Draw attack rate indicator next to the level number
            int circleX = 120, circleY = y + 2 * lineHeight + 5, circleR = 20;
            float progress = getAttackCooldownProgress();
            g2d.setColor(Color.WHITE);
            g2d.drawOval(circleX, circleY, circleR, circleR);
            g2d.setColor(Color.WHITE);
            int startAngle = 90;
            int arcAngle = (int)(-360 * progress);
            g2d.fillArc(circleX, circleY, circleR, circleR, startAngle, arcAngle);
            g2d.setColor(Color.BLACK);
            g2d.drawOval(circleX, circleY, circleR, circleR);
        } else {
            // Draw experience bar first
            g2d.setFont(pixelFont.deriveFont(10f)); // Smaller font for exp text
            g2d.setColor(Color.WHITE); // Reset color to white for XP text
            g2d.drawString("XP: " + player.get_current_exp() + "/" + player.get_total_exp(), 10, y + lineHeight);
            if (player.get_level() >= enums.GameConstants.MAX_LEVEL) {
                // At max level, show golden full bar
                drawResourceBar(g2d, 150, y + lineHeight - 12, 80, 8, 
                               player.get_total_exp(), player.get_total_exp(), new Color(255, 215, 0));
            } else {
                drawResourceBar(g2d, 150, y + lineHeight - 12, 80, 8, 
                               player.get_current_exp(), player.get_total_exp(), Color.GREEN);
            }
            
            // Draw level number underneath the EXP bar
            g2d.setFont(pixelFont.deriveFont(12f)); // Reset font size
            if (player.get_level() >= enums.GameConstants.MAX_LEVEL) {
                g2d.setColor(new Color(255, 215, 0)); // Golden color for max level
            } else {
                g2d.setColor(Color.WHITE);
            }
            g2d.drawString("Level: " + player.get_level(), 10, y + lineHeight + 20);
            
            // Draw level points underneath the level number
            g2d.setFont(pixelFont.deriveFont(10f)); // Smaller font for level points
            g2d.setColor(Color.GREEN); // Green color for level points
            
            // Draw level points icon
            if (levelPointsImage != null) {
                g2d.drawImage(levelPointsImage, 10, y + lineHeight + 20, 42, 42, null); // 42x42 icon (3x larger) - moved up more
            }
            
            // Draw level points text next to the icon
            g2d.drawString("Level points: " + player.get_level_points(), 58, y + lineHeight + 45); // Positioned next to larger icon - aligned with image center
            

            
            // Draw attack rate indicator next to the level number
            int circleX = 120, circleY = y + lineHeight + 5, circleR = 20;
            float progress = getAttackCooldownProgress();
            g2d.setColor(Color.WHITE);
            g2d.drawOval(circleX, circleY, circleR, circleR);
            g2d.setColor(Color.WHITE);
            int startAngle = 90;
            int arcAngle = (int)(-360 * progress);
            g2d.fillArc(circleX, circleY, circleR, circleR, startAngle, arcAngle);
            g2d.setColor(Color.BLACK);
            g2d.drawOval(circleX, circleY, circleR, circleR);
        }
    }
    // Controls help
    g2d.setColor(Color.LIGHT_GRAY);
    g2d.setFont(pixelFont.deriveFont(10f));
    String controlText = mouseAimingMode ? 
        "WASD: Move | Mouse: Aim | Space/LMB: Attack | I: Inventory | ESC: Pause" :
        "WASD: Move | Arrows: Aim | Space: Attack | I: Inventory | ESC: Pause";
    g2d.drawString(controlText, 10, getHeight() - 5);
    
    // TEMPORARY: Map movement boundary indicator - DISABLED (camera system now handles positioning)
    // g2d.setColor(Color.YELLOW);
    // g2d.setFont(pixelFont.deriveFont(8f));
    // String boundaryText = "Camera System Active - Player Centered";
    // g2d.drawString(boundaryText, 10, getHeight() - 20);
    // Debug info in right panel
    if (debugMode && player != null) {
        parentView.updateDebugStats(player);
    }
}

/**
    * MANDATORY: Render pause overlay
    * 
    * @param g2d Graphics2D context
*/
private void render_pause_overlay(Graphics2D g2d) {
    Font pixelFont = parentView.getPixelFont();
    g2d.setColor(new Color(0, 0, 0, 180));
    g2d.fillRect(0, 0, getWidth(), getHeight());
    g2d.setColor(Color.WHITE);
    g2d.setFont(pixelFont.deriveFont(24f));
    String pauseText = "PAUSED";
    int pauseWidth = g2d.getFontMetrics().stringWidth(pauseText);
    g2d.drawString(pauseText, (getWidth() - pauseWidth) / 2, getHeight() / 2 - 40);
    g2d.setFont(pixelFont.deriveFont(16f));
    String resume = "Resume";
    String quit = "Quit";
    int resumeWidth = g2d.getFontMetrics().stringWidth(resume);
    int quitWidth = g2d.getFontMetrics().stringWidth(quit);
    int y = getHeight() / 2;
    // Set bounds for mouse detection
    int buttonHeight = 32;
    int buttonPadding = 12;
    resumeButtonBounds.setBounds((getWidth() - resumeWidth) / 2 - buttonPadding, y - 24, resumeWidth + 2 * buttonPadding, buttonHeight);
    quitButtonBounds.setBounds((getWidth() - quitWidth) / 2 - buttonPadding, y + 16, quitWidth + 2 * buttonPadding, buttonHeight);
    // Highlight logic: only one yellow at a time
    if (pauseHoveredIndex != -1) {
        g2d.setColor(pauseHoveredIndex == 0 ? Color.YELLOW : Color.LIGHT_GRAY);
        g2d.drawString(resume, (getWidth() - resumeWidth) / 2, y);
        g2d.setColor(pauseHoveredIndex == 1 ? Color.YELLOW : Color.LIGHT_GRAY);
        g2d.drawString(quit, (getWidth() - quitWidth) / 2, y + 40);
    } else {
        g2d.setColor(pauseMenuSelection == 0 ? Color.YELLOW : Color.LIGHT_GRAY);
        g2d.drawString(resume, (getWidth() - resumeWidth) / 2, y);
        g2d.setColor(pauseMenuSelection == 1 ? Color.YELLOW : Color.LIGHT_GRAY);
        g2d.drawString(quit, (getWidth() - quitWidth) / 2, y + 40);
    }
}

/**
    * MANDATORY: Set player reference for rendering
    * 
    * @param player The Player instance
*/
public void set_player(Player player) {
    this.player = player;
    // Clear weapon image cache when a new player is set (new game/class)
    weaponImageManager.clearCache();
}

public void set_debug_mode(boolean debugMode) {
    this.debugMode = debugMode;
    repaint();
}
public boolean is_debug_mode() {
    return debugMode;
}

    public void setInventoryOverlay(boolean overlay) {
        this.inventoryOverlay = overlay;
        repaint();
    }
    

// Handle pause menu navigation and selection
public boolean handlePauseMenuInput(int keyCode) {
    if (!showPauseOverlay) return false;
    if (keyCode == java.awt.event.KeyEvent.VK_UP || keyCode == java.awt.event.KeyEvent.VK_W) {
        pauseMenuSelection = (pauseMenuSelection - 1 + 2) % 2;
        repaint();
        return true;
    } else if (keyCode == java.awt.event.KeyEvent.VK_DOWN || keyCode == java.awt.event.KeyEvent.VK_S) {
        pauseMenuSelection = (pauseMenuSelection + 1) % 2;
        repaint();
        return true;
    } else if (keyCode == java.awt.event.KeyEvent.VK_ENTER || keyCode == java.awt.event.KeyEvent.VK_ESCAPE) {
        if (pauseMenuSelection == 0 || keyCode == java.awt.event.KeyEvent.VK_ESCAPE) {
            showPauseOverlay = false;
            parentView.get_controller().handle_input("RESUME_GAME");
        } else if (pauseMenuSelection == 1) {
            showPauseOverlay = false;
            parentView.get_controller().handle_input("BACK_TO_MENU");
        }
        pauseMenuSelection = 0;
        repaint();
        return true;
    }
    return false;
}

@Override
protected void processMouseEvent(java.awt.event.MouseEvent e) {
    if (showPauseOverlay && e.getID() == java.awt.event.MouseEvent.MOUSE_CLICKED) {
        if (resumeButtonBounds.contains(e.getPoint())) {
            pauseMenuSelection = 0;
            showPauseOverlay = false;
            repaint();
            parentView.get_controller().handle_input("RESUME_GAME");
            return;
        } else if (quitButtonBounds.contains(e.getPoint())) {
            pauseMenuSelection = 1;
            showPauseOverlay = false;
            repaint();
            parentView.get_controller().handle_input("BACK_TO_MENU");
            return;
        }
    }
    
    // Handle stats panel button clicks
    if (e.getID() == java.awt.event.MouseEvent.MOUSE_CLICKED && e.getButton() == java.awt.event.MouseEvent.BUTTON1) {
        // Calculate stats panel position (same as in render_ui)
        int y = getHeight() - 180;
        int statusPanelX = 240;
        int statusPanelWidth = 240;
        int statsPanelX = statusPanelX + statusPanelWidth + 10;
        int statsPanelY = y - 20;
        // Calculate balanced panel width (same calculation as in render_ui)
        int imageSize = 28;
        int imagePadding = 30;
        int imagesPerRow = 6;
        int statsPanelWidth = (imagesPerRow * (imageSize + imagePadding)) - imagePadding + 16; // 16 = 8px left + 8px right padding
        int statsPanelHeight = 90; // Updated to match new height with stat values
        
        // Check if click is within stats panel bounds
        if (e.getX() >= statsPanelX && e.getX() < statsPanelX + statsPanelWidth &&
            e.getY() >= statsPanelY && e.getY() < statsPanelY + statsPanelHeight) {
            
            // Calculate which stat button was clicked
            int startX = statsPanelX + 8; // Updated to match new reduced padding
            int startY = statsPanelY + 38; // Updated to match new balanced positioning
            
            int clickX = e.getX() - startX;
            int clickY = e.getY() - startY;
            
            int col = clickX / (imageSize + imagePadding);
            
            if (col >= 0 && col < imagesPerRow) {
                int index = col; // Single row, so index = column
                if (index < 6) {
                    String[] buttonLabels = {"Health", "Attack", "Defense", "Range", "Speed", "Mana"};
                    // Skip Mana button if player has no MP
                    if (index == 5 && (player == null || player.getPlayerClassOOP().getBaseMp() <= 0)) {
                        return;
                    }
    
                    
                    // Handle stat button click
                    if (player != null) {
                        String statType = buttonLabels[index].toLowerCase();
                        boolean success = player.increase_stat(statType);
                        
                        if (!success) {
                            // Not enough level points
                            if (player != null) {
                                player.notify_observers("LOG_MESSAGE", "You need to level up more to get stronger");
                            }
                        }
                        
                        // Update display
                        repaint();
                    }
                    return;
                }
            }
        }
    }
    // Left-click attack in mouse aiming mode (continuous)
    if (mouseAimingMode && (e.getButton() == java.awt.event.MouseEvent.BUTTON1)) {
        if (e.getID() == java.awt.event.MouseEvent.MOUSE_PRESSED) {
            mouseAttackHeld = true;
            if (canAttack()) player.getGameLogic().handle_player_attack_input();
        } else if (e.getID() == java.awt.event.MouseEvent.MOUSE_RELEASED) {
            mouseAttackHeld = false;
        }
        // Don't call super for these events
        return;
    }
    super.processMouseEvent(e);
}

@Override
public void processKeyEvent(java.awt.event.KeyEvent e) {
    boolean handled = false;
    // --- ESCAPE (PAUSE/RESUME) ---
    if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE && e.getID() == java.awt.event.KeyEvent.KEY_PRESSED) {
        if (showPauseOverlay) {
            // If pause menu is open, ESC resumes the game (acts like Resume)
            showPauseOverlay = false;
            
            // EDGE CASE FIX: Clear held keys on resume as well for extra safety
            clearAllHeldKeys();
            
            parentView.get_controller().handle_input("RESUME_GAME");
            pauseMenuSelection = 0;
            repaint();
            handled = true;
        } else {
            // If pause menu is not open, ESC opens it
            showPauseOverlay = true;
            
            // EDGE CASE FIX: Clear all held keys to prevent stuck movement after resume
            clearAllHeldKeys();
            
            parentView.get_controller().handle_input("PAUSE_GAME");
            repaint();
            handled = true;
        }
    }
    if (showPauseOverlay && !handled) {
        // Pause menu navigation
        if (e.getID() == java.awt.event.KeyEvent.KEY_PRESSED) {
            if (e.getKeyCode() == java.awt.event.KeyEvent.VK_UP || e.getKeyCode() == java.awt.event.KeyEvent.VK_W) {
                pauseMenuSelection = (pauseMenuSelection - 1 + 2) % 2;
                repaint();
                handled = true;
            } else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_DOWN || e.getKeyCode() == java.awt.event.KeyEvent.VK_S) {
                pauseMenuSelection = (pauseMenuSelection + 1) % 2;
                repaint();
                handled = true;
            } else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                if (pauseMenuSelection == 0) {
                    showPauseOverlay = false;
                    
                    // EDGE CASE FIX: Clear held keys when resuming via menu selection
                    clearAllHeldKeys();
                    
                    parentView.get_controller().handle_input("RESUME_GAME");
                } else if (pauseMenuSelection == 1) {
                    showPauseOverlay = false;
                    parentView.get_controller().handle_input("BACK_TO_MENU");
                }
                pauseMenuSelection = 0;
                repaint();
                handled = true;
            }
        }
        // Block ALL input when paused (not just unhandled input)
        return;
    }
    if (debugMode && player != null && e.getID() == java.awt.event.KeyEvent.KEY_PRESSED) {
        if (e.getKeyCode() == java.awt.event.KeyEvent.VK_1) {
            player.debug_switch_class(1);
            handled = true;
        } else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_2) {
            player.debug_switch_class(2);
            handled = true;
        } else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_3) {
            player.debug_switch_class(3);
            handled = true;
        } else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_4) {
            player.debug_switch_class(4);
            handled = true;
        }
    }
    
    // Manual map movement removed - camera system now handles player centering automatically
    // Stats navigation is now handled entirely by GameView
    
    if (player != null && !handled) {
        // --- MOVEMENT (WASD) ---
        boolean movementChanged = false;
        if (e.getKeyCode() == java.awt.event.KeyEvent.VK_W ||
            e.getKeyCode() == java.awt.event.KeyEvent.VK_A ||
            e.getKeyCode() == java.awt.event.KeyEvent.VK_S ||
            e.getKeyCode() == java.awt.event.KeyEvent.VK_D) {
            if (e.getID() == java.awt.event.KeyEvent.KEY_PRESSED) {
                if (movementKeysHeld.add(e.getKeyCode())) movementChanged = true;
            } else if (e.getID() == java.awt.event.KeyEvent.KEY_RELEASED) {
                if (movementKeysHeld.remove(e.getKeyCode())) movementChanged = true;
            }
            if (movementChanged) {
                int dx = 0, dy = 0;
                if (movementKeysHeld.contains(java.awt.event.KeyEvent.VK_W)) dy -= 1;
                if (movementKeysHeld.contains(java.awt.event.KeyEvent.VK_S)) dy += 1;
                if (movementKeysHeld.contains(java.awt.event.KeyEvent.VK_A)) dx -= 1;
                if (movementKeysHeld.contains(java.awt.event.KeyEvent.VK_D)) dx += 1;
                player.setMoveDirection(dx, dy);
            }
            handled = true;
        }
        // --- AIMING (ARROWS) ---
        if (!mouseAimingMode &&
            (e.getKeyCode() == java.awt.event.KeyEvent.VK_UP ||
             e.getKeyCode() == java.awt.event.KeyEvent.VK_DOWN ||
             e.getKeyCode() == java.awt.event.KeyEvent.VK_LEFT ||
             e.getKeyCode() == java.awt.event.KeyEvent.VK_RIGHT)) {
            boolean aimChanged = false;
            if (e.getID() == java.awt.event.KeyEvent.KEY_PRESSED) {
                if (aimKeysHeld.add(e.getKeyCode())) aimChanged = true;
            } else if (e.getID() == java.awt.event.KeyEvent.KEY_RELEASED) {
                if (aimKeysHeld.remove(e.getKeyCode())) aimChanged = true;
            }
            if (aimChanged) {
                int adx = 0, ady = 0;
                if (aimKeysHeld.contains(java.awt.event.KeyEvent.VK_UP)) ady -= 1;
                if (aimKeysHeld.contains(java.awt.event.KeyEvent.VK_DOWN)) ady += 1;
                if (aimKeysHeld.contains(java.awt.event.KeyEvent.VK_LEFT)) adx -= 1;
                if (aimKeysHeld.contains(java.awt.event.KeyEvent.VK_RIGHT)) adx += 1;
                // If no arrow keys held, do not update aim (keep last direction)
                if (adx != 0 || ady != 0) {
                    player.setAimDirection(adx, ady);
                }
            }
            handled = true;
        }
    }
    // --- ATTACK (SPACE) ---
    if (e.getKeyCode() == java.awt.event.KeyEvent.VK_SPACE) {
        if (e.getID() == java.awt.event.KeyEvent.KEY_PRESSED) {
            attackKeyHeld = true;
            if (canAttack()) player.getGameLogic().handle_player_attack_input();
        } else if (e.getID() == java.awt.event.KeyEvent.KEY_RELEASED) {
            attackKeyHeld = false;
        }
        handled = true;
    }
    // Forward all other keys to parentView for global handling
    if (!handled && e.getID() == java.awt.event.KeyEvent.KEY_PRESSED) {
    }
    super.processKeyEvent(e);
}

@Override
public void setVisible(boolean aFlag) {
    super.setVisible(aFlag);
    enableEvents(java.awt.AWTEvent.MOUSE_EVENT_MASK);
}

public LogBoxPanel getLogBoxPanel() {
    return logBoxPanel;
}



// Listen for PLAYER_ATTACKED events (add to constructor or via observer pattern)
// For now, add a public method to be called by GameLogic observer
public void showPlayerAttack(java.util.Set<utilities.Position> fan, utilities.Position projStart, utilities.Position projEnd) {
    this.attackFanTiles = fan != null ? new java.util.HashSet<>(fan) : new java.util.HashSet<>();
    this.projectileStart = projStart;
    this.projectileEnd = projEnd;
    this.attackVisualTime = System.currentTimeMillis();
    repaint();
}

// Overloaded method to accept AttackVisualData
public void showPlayerAttack(model.gameLogic.AttackVisualData attackData) {
    this.attackFanTiles = attackData.getAttackFanTiles();
    this.projectileStart = attackData.getProjectileStart();
    this.projectileEnd = attackData.getProjectileEnd();
    this.lastAimDX = attackData.getAttackDX();
    this.lastAimDY = attackData.getAttackDY();
    this.attackVisualTime = System.currentTimeMillis();
    this.lastAttackData = attackData; // Store for swing-based rendering
    repaint();
}

    // Method to show enemy attack visuals
    public void showEnemyAttack(model.characters.Enemy enemy, double attackAngle) {
        enemyAttackTimes.put(enemy, System.currentTimeMillis());
        enemyAttackAngles.put(enemy, attackAngle);
        repaint();
    }
    
    // Method to show enemy swing attack with AttackVisualData
    public void showEnemySwingAttack(model.characters.Enemy enemy, model.gameLogic.AttackVisualData swingData) {
    
        enemySwingData.put(enemy, swingData);
        repaint();
    }
    
    /**
     * Show Ranger bow attack for player projectile attacks
     * Uses OOD principles by extending the existing attack system
     */
    public void showPlayerRangerBowAttack(model.gameLogic.AttackVisualData bowData) {
        playerRangerBowData = bowData;
        repaint();
    }
    
    /**
     * Show Ranger bow attack for enemy projectile attacks
     * Uses OOD principles by extending the existing attack system
     */
    public void showEnemyRangerBowAttack(model.characters.Enemy enemy, model.gameLogic.AttackVisualData bowData) {
        enemyRangerBowData.put(enemy, bowData);
        repaint();
    }
    
    // Method to start enemy wind-up warning (cyan blinking on enemy border)
    public void startEnemyWindUpWarning(model.characters.Enemy enemy) {
        enemyWindUpStartTimes.put(enemy, System.currentTimeMillis());
        repaint();
    }
    
    /**
     * Trigger damage flash effect when player takes damage
     */
    public void triggerDamageFlash() {
        damageFlashStartTime = System.currentTimeMillis();
        repaint();
    }
    
    /**
     * Trigger a colored flash when consuming an item
     * @param itemType The type of item consumed ("health", "mana", "experience", "clarity")
     */
    public void triggerItemFlash(String itemType) {
        itemFlashStartTime = System.currentTimeMillis();
        switch (itemType.toLowerCase()) {
            case "health":
                itemFlashColor = Color.PINK;
                break;
            case "mana":
                itemFlashColor = Color.BLUE;
                break;
            case "experience":
                itemFlashColor = Color.GREEN;
                break;
            case "clarity":
                itemFlashColor = Color.ORANGE;
                break;
            case "invisibility":
                itemFlashColor = new Color(128, 0, 128); // Dark purple
                break;
            case "swiftness":
                itemFlashColor = new Color(64, 224, 208); // Turquoise green
                break;
            case "immortality":
                itemFlashColor = Color.YELLOW; // Gold
                break;
            default:
                itemFlashColor = Color.WHITE;
                break;
        }
        repaint();
    }
    
    public void setFloorTransitioning(boolean transitioning) {
        this.isFloorTransitioning = transitioning;
        if (transitioning) {
            floorTransitionStartTime = System.currentTimeMillis();
        }
        repaint();
    }
    
    public void setFloorNumber(int floorNumber) {
        this.currentFloorNumber = floorNumber;
        repaint();
    }
    
    public int getCurrentFloorNumber() {
        return currentFloorNumber;
    }
    
    public void resetFloorNumber() {
        this.currentFloorNumber = 1;
    }
    


    private long getLastAttackTime() {
        // Try to get from GameLogic
        try {
            model.gameLogic.GameLogic logic = (model.gameLogic.GameLogic) parentView.get_controller().get_model();
            java.lang.reflect.Field f = logic.getClass().getDeclaredField("lastAttackTime");
            f.setAccessible(true);
            return f.getLong(logic);
        } catch (Exception e) { return 0; }
    }
    private float getAttackSpeed() {
        if (player != null && player.getPlayerClassOOP() != null) {
            return player.getPlayerClassOOP().getAttackSpeed();
        }
        return 1.0f;
    }
    private boolean canAttack() {
        long now = System.currentTimeMillis();
        float attackSpeed = getAttackSpeed();
        long lastAttack = getLastAttackTime();
        return (now - lastAttack) >= (1000.0f / attackSpeed);
    }
    private float getAttackCooldownProgress() {
        long now = System.currentTimeMillis();
        float attackSpeed = getAttackSpeed();
        long lastAttack = getLastAttackTime();
        float cooldown = 1000.0f / attackSpeed;
        float elapsed = now - lastAttack;
        return Math.min(1.0f, Math.max(0f, elapsed / cooldown));
    }
    
    /**
     * Get the original base attack value (without level point bonuses)
     */
    private int getOriginalBaseAttack() {
        if (player == null || player.getPlayerClassOOP() == null) return 0;
        // Get the original base attack from the class (before level point modifications)
        BaseClass playerClass = player.getPlayerClassOOP();
        CharacterClass classType = playerClass.getClassType();
        
        // These are the original base attack values for each class
        switch (classType) {
            case ROGUE: return 8;
            case RANGER: return 6;
            case WARRIOR: return 10;
            case MAGE: return 4;
            default: return 6;
        }
    }
    
    /**
     * Get the original base defense value (without level point bonuses)
     */
    private float getOriginalBaseDefense() {
        if (player == null || player.getPlayerClassOOP() == null) return 0f;
        // Get the original base defense from the class (before level point modifications)
        BaseClass playerClass = player.getPlayerClassOOP();
        CharacterClass classType = playerClass.getClassType();
        
        // These are the original base defense values for each class
        switch (classType) {
            case ROGUE: return 3.0f;
            case RANGER: return 2.5f;
            case WARRIOR: return 5.0f;
            case MAGE: return 2.0f;
            default: return 3.0f;
        }
    }

private Color getPlayerColor() {
    if (player == null || player.getPlayerClassOOP() == null) return Color.BLUE;
    String className = player.getPlayerClassOOP().getClassName().toLowerCase();
    switch (className) {
        case "warrior": return Color.BLUE;
        case "mage": return Color.ORANGE;
        case "rogue": return new Color(128, 0, 128); // Purple
        case "ranger": return Color.GREEN;
        default: return Color.GRAY;
    }
}

public void requestPanelFocus() {
    this.requestFocusInWindow();
}

/**
 * Unified weapon rendering system for all entities (player, enemies, bosses)
 * Uses OOD principles to eliminate code duplication and ensure consistency
 */
private void renderWeaponSwingAttack(Graphics2D g2d, float px, float py, float range, 
                                   model.gameLogic.AttackVisualData swingData, 
                                   model.equipment.Weapon weapon, 
                                   boolean isEnemy) {
    long currentTime = System.currentTimeMillis();
    if (!swingData.isSwingActive(currentTime)) {
        return; // Swing finished
    }
    
    // Get current swing angle
    double currentSwingAngle = swingData.getCurrentSwingAngle(currentTime);
    
    // Get weapon image
    java.awt.image.BufferedImage weaponImage = getWeaponImage(weapon);
    
    if (weaponImage != null) {
        // Render weapon image pivoting around its base (bottom-left corner)
        int weaponSize = 32; // Size of weapon image
        float weaponRadius = range * enums.GameConstants.TILE_SIZE * 0.25f; // Position weapon base closer to entity
        
        // Calculate weapon base position (pivot point) - closer to the entity
        // Adjust position slightly to align with fan center
        float weaponBaseX = px + (float)(Math.cos(currentSwingAngle) * weaponRadius) - 8; // Shift left more
        float weaponBaseY = py + (float)(Math.sin(currentSwingAngle) * weaponRadius) - 8; // Shift up more
        
        // Save current transform
        java.awt.geom.AffineTransform oldTransform = g2d.getTransform();
        
        // Translate to weapon base position and rotate around that point
        g2d.translate(weaponBaseX, weaponBaseY);
        // Rotate weapon to point in the swing direction
        // Weapon sprites are oriented horizontally, so we need to rotate them to point outward
        // Add 55 degrees clockwise (π/4 + π/18 radians) to align weapon base with fan center
        g2d.rotate(currentSwingAngle + Math.PI/4 + Math.PI/18);
        
        // Draw weapon image with adjusted position relative to base - this moves the weapon relative to the fan
        if (isEnemy) {
            g2d.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 0.8f));
        }
        g2d.drawImage(weaponImage, 10, -35, weaponSize, weaponSize, null);
        if (isEnemy) {
            g2d.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 1.0f));
        }
        
        // Restore transform
        g2d.setTransform(oldTransform);
    }
    
    // Fan visibility removed - only weapon images are shown now
}

/**
 * Render Ranger bow for projectile attacks
 * Special case for ranged weapons that need different positioning
 * Uses OOD principles by extending the existing weapon rendering system
 */
private void renderRangerBow(Graphics2D g2d, float px, float py, double aimAngle, model.equipment.Weapon weapon, boolean isEnemy) {
    // Rangers always use Simple Bow image regardless of equipped weapon
    java.awt.image.BufferedImage bowImage = getWeaponImage(new model.equipment.Weapon("Simple Bow", 0, 0, enums.CharacterClass.RANGER, 1, model.equipment.Weapon.WeaponType.DISTANCE, "images/weapons/Distance/Simple_Bow.png", "Distance"));
    
    if (bowImage != null) {
        int bowSize = 32;
        float orbitRadius = 24; // Distance from player/enemy center to bow center
        
        // Save current transform
        java.awt.geom.AffineTransform oldTransform = g2d.getTransform();
        
        // Create a single, clean transform that positions and rotates the bow
        // This prevents sliding by avoiding complex transform chains
        java.awt.geom.AffineTransform bowTransform = new java.awt.geom.AffineTransform();
        
        // Calculate the final bow position
        float bowX = px + (float)(Math.cos(aimAngle) * orbitRadius);
        float bowY = py + (float)(Math.sin(aimAngle) * orbitRadius);
        
        // Set the transform: translate to position, then rotate, then flip horizontally
        bowTransform.translate(bowX, bowY);
        bowTransform.rotate(aimAngle);
        bowTransform.scale(-1, 1); // Flip horizontally so string faces inward
        
        // Apply the clean transform
        g2d.setTransform(bowTransform);
        
        // Draw bow centered on the orbit position
        if (isEnemy) {
        g2d.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 0.8f));
        }
        g2d.drawImage(bowImage, -bowSize/2, -bowSize/2, bowSize, bowSize, null);
        if (isEnemy) {
        g2d.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 1.0f));
        }
        
        // Restore original transform
        g2d.setTransform(oldTransform);
    }
}

/**
 * Render player swing-based attack with weapon image
 * Uses unified weapon rendering system for consistency
 */
private void renderSwingAttack(Graphics2D g2d, float px, float py, float range, model.gameLogic.AttackVisualData swingData) {
    model.equipment.Weapon weapon = (player != null) ? player.get_equipped_weapon() : null;
    renderWeaponSwingAttack(g2d, px, py, range, swingData, weapon, false);
}

/**
 * Render enemy swing-based attack with weapon image
 * Uses unified weapon rendering system for consistency
 */
private void renderEnemySwingAttack(Graphics2D g2d, float px, float py, float range, model.gameLogic.AttackVisualData swingData) {
    // Find enemy weapon from swing data map
    model.equipment.Weapon weapon = null;
    for (java.util.Map.Entry<model.characters.Enemy, model.gameLogic.AttackVisualData> entry : enemySwingData.entrySet()) {
        if (entry.getValue() == swingData) {
            weapon = entry.getKey().get_equipped_weapon();
            break;
        }
    }
    renderWeaponSwingAttack(g2d, px, py, range, swingData, weapon, true);
}

    /**
     * Draw a resource bar with white border and colored fill
     */
    private void drawResourceBar(Graphics2D g2d, int x, int y, int width, int height, 
                                int current, int max, Color fillColor) {
        // Draw white border with even thicker stroke
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3f));
        g2d.drawRect(x, y, width, height);
        
        // Calculate fill width based on percentage
        if (max > 0) {
            double percentage = (double) current / max;
            int fillWidth = (int) (width * percentage);
            
            // Draw colored fill (accounting for thicker border)
            g2d.setColor(fillColor);
            g2d.fillRect(x + 3, y + 3, fillWidth - 3, height - 3);
        }
        
        // Reset stroke to default
        g2d.setStroke(new BasicStroke(1f));
    }
    
    /**
     * Draw clarity bar with thinner border (specialized for clarity effect)
     */
    private void drawClarityBar(Graphics2D g2d, int x, int y, int width, int height, 
                                int current, int max, Color fillColor) {
        // Draw white border with thinner stroke for clarity bar
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRect(x, y, width, height);
        
        // Calculate fill width based on percentage
        if (max > 0) {
            double percentage = (double) current / max;
            int fillWidth = (int) (width * percentage);
            
            // Draw colored fill (accounting for thinner border)
            g2d.setColor(fillColor);
            g2d.fillRect(x + 2, y + 2, fillWidth - 2, height - 2);
        }
        
        // Reset stroke to default
        g2d.setStroke(new BasicStroke(1f));
    }
    
    /**
     * Draw white border around map boundaries with pixelated rounded corners
     */
    private void drawMapBorder(Graphics2D g2d, int tileSize, int offsetX, int offsetY) {
        if (currentMap == null) return;
        
        // Drawing white border viewport (800x480px) around the game area
        
        // White frame viewport size: scale down to maintain original viewport size
        // Full map is 50x30 tiles, but viewport shows original size regardless of tile scaling
        int mapWidth = (50 * tileSize) / GameConstants.SCALING_FACTOR; // (50 * 32) / 2 = 800px
        int mapHeight = (30 * tileSize) / GameConstants.SCALING_FACTOR; // (30 * 32) / 2 = 480px
        int cornerRadius = 8; // Pixelated corner radius
        
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(4f)); // Thicker border
        
        // Draw main rectangle with rounded corners (with offset)
        // Top edge
        g2d.drawLine(offsetX + cornerRadius, offsetY, offsetX + mapWidth - cornerRadius, offsetY);
        // Bottom edge
        g2d.drawLine(offsetX + cornerRadius, offsetY + mapHeight, offsetX + mapWidth - cornerRadius, offsetY + mapHeight);
        // Left edge
        g2d.drawLine(offsetX, offsetY + cornerRadius, offsetX, offsetY + mapHeight - cornerRadius);
        // Right edge
        g2d.drawLine(offsetX + mapWidth, offsetY + cornerRadius, offsetX + mapWidth, offsetY + mapHeight - cornerRadius);
        
        // Draw pixelated rounded corners (with offset)
        // Top-left corner
        drawPixelatedCorner(g2d, offsetX + cornerRadius, offsetY + cornerRadius, cornerRadius, 0);
        // Top-right corner
        drawPixelatedCorner(g2d, offsetX + mapWidth - cornerRadius, offsetY + cornerRadius, cornerRadius, 1);
        // Bottom-right corner
        drawPixelatedCorner(g2d, offsetX + mapWidth - cornerRadius, offsetY + mapHeight - cornerRadius, cornerRadius, 2);
        // Bottom-left corner
        drawPixelatedCorner(g2d, offsetX + cornerRadius, offsetY + mapHeight - cornerRadius, cornerRadius, 3);
        
        g2d.setStroke(new BasicStroke(1f));
    }
    
    // Camera system to keep player centered in white frame
    private float cameraX = 0;
    private float cameraY = 0;
    
    /**
     * Update camera to keep player centered in white frame
     */
    private void updateCamera() {
        if (player == null || currentMap == null) {
            return;
        }
        
        int tileSize = GameConstants.TILE_SIZE;
        
        // Simple approach: calculate where the white frame center is
        int rightPanelWidth = 250;
        int mapAreaWidth = getWidth() - rightPanelWidth;
        int mapAreaHeight = getHeight();
        
        // White frame is drawn at (0, 35), so its center is:
        // Use scaling factor to maintain original viewport size
        int whiteFrameWidth = (50 * tileSize) / GameConstants.SCALING_FACTOR; // (50 * 32) / 2 = 800px
        int whiteFrameHeight = (30 * tileSize) / GameConstants.SCALING_FACTOR; // (30 * 32) / 2 = 480px
        int whiteFrameCenterX = whiteFrameWidth / 2; // 400 pixels
        int whiteFrameCenterY = 35 + (whiteFrameHeight / 2); // 35 + 240 = 275 pixels
        
        // Get player's current position
        float playerX = player.getPixelX();
        float playerY = player.getPixelY();
        
        // Calculate camera to put player at white frame center
        cameraX = whiteFrameCenterX - playerX;
        cameraY = whiteFrameCenterY - playerY;
        
        // Adjust for player's visual center (they're drawn with +2 offset and smaller size)
        cameraX -= 2 + (tileSize - 4) / 2f;
        cameraY -= 2 + (tileSize - 4) / 2f;
    }
    
    /**
     * Get the horizontal offset for map positioning
     */
    public int getMapOffsetX() {
        return (int)cameraX;
    }
    
    /**
     * Get the vertical offset for map positioning
     */
    public int getMapOffsetY() {
        return (int)cameraY;
    }
    
    /**
     * Convert map coordinates to screen coordinates (parenting system)
     * All entities and visual elements use this to stay relative to the map
     */
    private int mapToScreenX(float mapX) {
        return (int)mapX + getMapOffsetX();
    }
    
    /**
     * Convert map coordinates to screen coordinates (parenting system)
     * All entities and visual elements use this to stay relative to the map
     */
    private int mapToScreenY(float mapY) {
        return (int)mapY + getMapOffsetY();
    }
    
    /**
     * Draw a pixelated rounded corner
     * cornerType: 0=top-left, 1=top-right, 2=bottom-right, 3=bottom-left
     */
    private void drawPixelatedCorner(Graphics2D g2d, int centerX, int centerY, int radius, int cornerType) {
        // Draw corner pixels in a pixelated arc pattern
        for (int i = 0; i <= radius; i++) {
            for (int j = 0; j <= radius; j++) {
                double distance = Math.sqrt(i * i + j * j);
                if (distance <= radius && distance > radius - 2) {
                    int x = centerX;
                    int y = centerY;
                    
                    switch (cornerType) {
                        case 0: // top-left
                            x = centerX - i;
                            y = centerY - j;
                            break;
                        case 1: // top-right
                            x = centerX + i;
                            y = centerY - j;
                            break;
                        case 2: // bottom-right
                            x = centerX + i;
                            y = centerY + j;
                            break;
                        case 3: // bottom-left
                            x = centerX - i;
                            y = centerY + j;
                            break;
                    }
                    
                    g2d.drawLine(x, y, x, y);
                }
            }
        }
    }
    
    /**
     * Render damage flash effect over the map area
     */
    private void renderDamageFlash(Graphics2D g2d) {
        if (damageFlashStartTime == 0) return;
        
        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - damageFlashStartTime;
        
        if (elapsed >= DAMAGE_FLASH_DURATION) {
            damageFlashStartTime = 0; // Reset
            return;
        }
        
        // Calculate map area bounds (inside the white border)
        int tileSize = GameConstants.TILE_SIZE;
        int mapWidth = 50 * tileSize; // 50 tiles wide
        int mapHeight = 30 * tileSize; // 30 tiles tall
        int mapOffsetX = getMapOffsetX();
        int mapOffsetY = getMapOffsetY();
        
        // Add border offset (4px on each side)
        int borderOffset = 4;
        int flashX = mapOffsetX + borderOffset;
        int flashY = mapOffsetY + borderOffset;
        int flashWidth = mapWidth - (borderOffset * 2);
        int flashHeight = mapHeight - (borderOffset * 2);
        
        // Determine flash intensity based on timing
        float alpha = 0.0f;
        
        if (elapsed < FIRST_FLASH_DURATION) {
            // First flash: 0.3 alpha
            alpha = 0.3f;
        } else if (elapsed < FIRST_FLASH_DURATION + FLASH_GAP) {
            // Gap between flashes: no flash
            alpha = 0.0f;
        } else if (elapsed < FIRST_FLASH_DURATION + FLASH_GAP + SECOND_FLASH_DURATION) {
            // Second flash: 0.15 alpha (more transparent)
            alpha = 0.15f;
        }
        
        if (alpha > 0.0f) {
            // Set composite for transparency
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.setColor(Color.RED);
            g2d.fillRect(flashX, flashY, flashWidth, flashHeight);
            // Reset composite
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
    }
    
    /**
     * Render item consumption flash effect over the map area
     */
    private void renderItemFlash(Graphics2D g2d) {
        if (itemFlashStartTime == 0 || itemFlashColor == null) return;
        
        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - itemFlashStartTime;
        
        if (elapsed >= ITEM_FLASH_DURATION) {
            itemFlashStartTime = 0; // Reset
            itemFlashColor = null;
            return;
        }
        
        // Calculate map area bounds (inside the white border) - same as damage flash
        int tileSize = GameConstants.TILE_SIZE;
        int mapWidth = 50 * tileSize; // 50 tiles wide
        int mapHeight = 30 * tileSize; // 30 tiles tall
        int mapOffsetX = getMapOffsetX();
        int mapOffsetY = getMapOffsetY();
        
        // Add border offset (4px on each side)
        int borderOffset = 4;
        int flashX = mapOffsetX + borderOffset;
        int flashY = mapOffsetY + borderOffset;
        int flashWidth = mapWidth - (borderOffset * 2);
        int flashHeight = mapHeight - (borderOffset * 2);
        
        // Calculate alpha based on elapsed time (fade out)
        float alpha = 0.4f * (1.0f - (float)elapsed / ITEM_FLASH_DURATION);
        
        if (alpha > 0.0f) {
            // Set composite for transparency
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.setColor(itemFlashColor);
            g2d.fillRect(flashX, flashY, flashWidth, flashHeight);
            // Reset composite
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
    }
    
    /**
     * Render floor transition overlay (black screen with welcome text and fade effect)
     */
    private void renderFloorTransitionOverlay(Graphics2D g2d) {
        if (floorTransitionStartTime == 0) return;
        
        long elapsed = System.currentTimeMillis() - floorTransitionStartTime;
        if (elapsed >= FLOOR_TRANSITION_DURATION) {
            floorTransitionStartTime = 0;
            return;
        }
        
        // Use screen dimensions instead of map area for consistent positioning
        int screenWidth = getWidth();
        int screenHeight = getHeight();
        
        // Calculate fade effect
        float alpha = 0.9f; // Start with 90% opacity
        if (elapsed > FLOOR_TRANSITION_DURATION - FLOOR_TRANSITION_FADE_DURATION) {
            // Start fading out
            long fadeElapsed = elapsed - (FLOOR_TRANSITION_DURATION - FLOOR_TRANSITION_FADE_DURATION);
            float fadeProgress = (float) fadeElapsed / FLOOR_TRANSITION_FADE_DURATION;
            alpha = 0.9f * (1.0f - fadeProgress);
        }
        
        // Apply black overlay to entire screen with fade
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, screenWidth, screenHeight);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        
        // Add welcome text with UI font and larger size, centered on screen
        g2d.setColor(Color.WHITE);
        Font uiFont = parentView.getPixelFont().deriveFont(24f); // Much larger font
        g2d.setFont(uiFont);
        // Get the actual floor display text from GameLogic for the welcome message
        String welcomeText;
        if (parentView != null && parentView.get_controller() != null && parentView.get_controller().get_model() != null) {
            model.gameLogic.GameLogic gameLogic = (model.gameLogic.GameLogic) parentView.get_controller().get_model();
            String displayText = gameLogic.getFloorDisplayText();
            if (displayText.equals("BOSS") || displayText.equals("BONUS")) {
                welcomeText = "Welcome to Floor " + displayText;
            } else {
                welcomeText = "Welcome to Floor " + currentFloorNumber;
            }
        } else {
            welcomeText = "Welcome to Floor " + currentFloorNumber;
        }
        FontMetrics fm = g2d.getFontMetrics();
        int textX = (screenWidth - fm.stringWidth(welcomeText)) / 2;
        int textY = (screenHeight + fm.getAscent()) / 2;
        
        // Apply fade to text as well
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.drawString(welcomeText, textX, textY);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }
} 