package view.renderers;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.logging.Logger;

import model.characters.Player;
import model.map.Map;
import enums.GameState;

/**
 * Responsible for rendering all UI elements in the game.
 * Follows Single Responsibility Principle - only handles UI rendering.
 * 
 * This class extracts UI rendering logic from GamePanel.java to improve
 * code organization and maintainability.
 */
public class UIRenderer {
    private static final Logger LOGGER = Logger.getLogger(UIRenderer.class.getName());
    
    // UI constants
    private static final int FLOOR_IMAGE_SIZE = 24;
    private static final int EFFECT_IMAGE_SIZE = 20;
    private static final int STATS_IMAGE_SIZE = 28;
    private static final int STATS_PADDING = 30;
    private static final int EFFECT_SPACING = 25;
    private static final int COLUMN_WIDTH = 120;
    
    // Colors
    private static final Color CLARITY_COLOR = Color.ORANGE;
    private static final Color VANISH_COLOR = new Color(128, 0, 128); // Dark purple
    private static final Color SWIFTNESS_COLOR = new Color(64, 224, 208); // Turquoise
    private static final Color UNDYING_COLOR = Color.YELLOW;
    private static final Color STATS_LABEL_COLOR = new Color(100, 200, 255); // Light blue
    private static final Color HOVER_TEXT_COLOR = Color.YELLOW;
    private static final Color HIGHLIGHT_BORDER_COLOR = new Color(255, 255, 100);
    
    // Image caches
    private final HashMap<String, BufferedImage> stateImages;
    private final HashMap<String, BufferedImage> statImages;
    private final BufferedImage bossRoomImage;
    private final BufferedImage bonusRoomImage;
    
    public UIRenderer() {
        this.stateImages = new HashMap<>();
        this.statImages = new HashMap<>();
        this.bossRoomImage = null; // TODO: Load from resources
        this.bonusRoomImage = null; // TODO: Load from resources
    }
    
    /**
     * Render all UI elements
     * 
     * @param g2d Graphics context
     * @param player The player character
     * @param currentFloorNumber Current floor number
     * @param currentFloorType Current floor type
     * @param mouseAimingMode Whether mouse aiming is enabled
     * @param currentState Current game state
     * @param isStatsNavigationMode Whether stats navigation is active
     * @param statsHoveredIndex Index of hovered stat button
     * @param panelWidth Panel width
     * @param panelHeight Panel height
     * @param pixelFont The pixel font to use
     */
    public void renderUI(Graphics2D g2d, Player player, int currentFloorNumber, 
                        Map.FloorType currentFloorType, boolean mouseAimingMode,
                        GameState currentState, boolean isStatsNavigationMode,
                        int statsHoveredIndex, int panelWidth, int panelHeight,
                        Font pixelFont) {
        
        // Render floor information
        renderFloorInfo(g2d, currentFloorNumber, currentFloorType, panelWidth, pixelFont);
        
        // Render control mode indicator
        renderControlModeIndicator(g2d, mouseAimingMode, pixelFont);
        
        // Render player stats and effects
        if (player != null) {
            renderPlayerStats(g2d, player, panelHeight, pixelFont);
            renderStatusEffects(g2d, player, panelHeight, pixelFont);
            renderStatsPanel(g2d, player, currentState, isStatsNavigationMode, 
                           statsHoveredIndex, panelHeight, pixelFont);
        }
    }
    
    /**
     * Render floor information at the top of the screen
     */
    private void renderFloorInfo(Graphics2D g2d, int currentFloorNumber, 
                               Map.FloorType currentFloorType, int panelWidth, Font pixelFont) {
        // Draw floor number
        g2d.setColor(Color.WHITE);
        g2d.setFont(pixelFont.deriveFont(18f));
        String floorText = "Floor " + currentFloorNumber;
        
        int floorWidth = g2d.getFontMetrics().stringWidth(floorText);
        int floorX = panelWidth - floorWidth - 20;
        g2d.drawString(floorText, floorX, 30);
        
        // Draw floor type indicator
        int imageX = floorX - FLOOR_IMAGE_SIZE - 5;
        int imageY = 30 - FLOOR_IMAGE_SIZE - 2;
        
        if (currentFloorType == Map.FloorType.BOSS) {
            if (bossRoomImage != null) {
                g2d.drawImage(bossRoomImage, imageX, imageY, FLOOR_IMAGE_SIZE, FLOOR_IMAGE_SIZE, null);
            } else {
                // Fallback to red square
                g2d.setColor(Color.RED);
                g2d.fillRect(imageX, imageY, FLOOR_IMAGE_SIZE, FLOOR_IMAGE_SIZE);
            }
        } else if (currentFloorType == Map.FloorType.BONUS) {
            if (bonusRoomImage != null) {
                g2d.drawImage(bonusRoomImage, imageX, imageY, FLOOR_IMAGE_SIZE, FLOOR_IMAGE_SIZE, null);
            } else {
                // Fallback to pink square
                g2d.setColor(Color.PINK);
                g2d.fillRect(imageX, imageY, FLOOR_IMAGE_SIZE, FLOOR_IMAGE_SIZE);
            }
        }
    }
    
    /**
     * Render control mode indicator
     */
    private void renderControlModeIndicator(Graphics2D g2d, boolean mouseAimingMode, Font pixelFont) {
        // Draw control mode
        g2d.setColor(mouseAimingMode ? Color.CYAN : Color.YELLOW);
        g2d.setFont(pixelFont.deriveFont(11f));
        String modeText = mouseAimingMode ? "MOUSE MODE" : "KEYBOARD MODE";
        g2d.drawString(modeText, 10, 20);
        
        // Draw switch instruction
        g2d.setColor(Color.WHITE);
        g2d.setFont(pixelFont.deriveFont(8f));
        String switchText = "Press M to switch controls";
        g2d.drawString(switchText, 10, 30);
    }
    
    /**
     * Render player stats (HP, MP)
     */
    private void renderPlayerStats(Graphics2D g2d, Player player, int panelHeight, Font pixelFont) {
        g2d.setColor(Color.WHITE);
        g2d.setFont(pixelFont.deriveFont(10f));
        
        int y = panelHeight - 180;
        
        // Draw HP
        g2d.drawString("HP: " + player.get_current_hp() + "/" + player.get_max_hp(), 10, y);
        drawResourceBar(g2d, 150, y - 12, 80, 8, 
                       player.get_current_hp(), player.get_max_hp(), Color.RED);
    }
    
    /**
     * Render status effects panel
     */
    private void renderStatusEffects(Graphics2D g2d, Player player, int panelHeight, Font pixelFont) {
        int y = panelHeight - 180;
        int effectY = y;
        int currentColumn = 0;
        int effectsInCurrentColumn = 0;
        
        // Render Clarity effect
        if (player.is_clarity_effect_active()) {
            renderStatusEffect(g2d, "Clarity", CLARITY_COLOR, player.get_clarity_effect_progress(),
                             effectY, currentColumn, pixelFont);
            effectsInCurrentColumn++;
            effectY = updateEffectPosition(effectsInCurrentColumn, y, currentColumn);
        }
        
        // Render Vanish effect
        if (player.is_invisibility_effect_active()) {
            renderStatusEffect(g2d, "Vanish", VANISH_COLOR, player.get_invisibility_effect_progress(),
                             effectY, currentColumn, pixelFont);
            effectsInCurrentColumn++;
            effectY = updateEffectPosition(effectsInCurrentColumn, y, currentColumn);
        }
        
        // Render Swiftness effect
        if (player.is_swiftness_effect_active()) {
            renderStatusEffect(g2d, "Swiftness", SWIFTNESS_COLOR, player.get_swiftness_effect_progress(),
                             effectY, currentColumn, pixelFont);
            effectsInCurrentColumn++;
            effectY = updateEffectPosition(effectsInCurrentColumn, y, currentColumn);
        }
        
        // Render Undying effect
        if (player.is_immortality_effect_active()) {
            renderStatusEffect(g2d, "Undying", UNDYING_COLOR, player.get_immortality_effect_progress(),
                             effectY, currentColumn, pixelFont);
        }
        
        // Draw status effects panel border
        drawStatusEffectsPanelBorder(g2d, y);
    }
    
    /**
     * Render a single status effect
     */
    private void renderStatusEffect(Graphics2D g2d, String effectName, Color effectColor, 
                                  float progress, int effectY, int currentColumn, Font pixelFont) {
        int effectX = 240 + (currentColumn * COLUMN_WIDTH);
        
        // Draw effect image if available
        BufferedImage effectImage = stateImages.get(effectName.toLowerCase());
        if (effectImage != null) {
            int imageX = effectX;
            int imageY = effectY - 15;
            g2d.drawImage(effectImage, imageX, imageY, EFFECT_IMAGE_SIZE, EFFECT_IMAGE_SIZE, null);
        }
        
        // Draw effect text
        g2d.setColor(effectColor);
        g2d.setFont(pixelFont.deriveFont(10f));
        g2d.drawString(effectName, effectX + 25, effectY);
        
        // Draw progress bar
        drawClarityBar(g2d, effectX, effectY + 5, 80, 6, 
                      (int)(progress * 100), 100, effectColor);
        g2d.setColor(Color.WHITE); // Reset color
    }
    
    /**
     * Update effect position for column layout
     */
    private int updateEffectPosition(int effectsInCurrentColumn, int baseY, int currentColumn) {
        if (effectsInCurrentColumn >= 2) {
            currentColumn = 1 - currentColumn; // Switch columns
            return baseY; // Reset to top
        } else {
            return baseY + EFFECT_SPACING;
        }
    }
    
    /**
     * Draw status effects panel border
     */
    private void drawStatusEffectsPanelBorder(Graphics2D g2d, int y) {
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2f));
        int statusPanelX = 240;
        int statusPanelY = y - 20;
        int statusPanelWidth = 240; // 2 columns * 120 width
        int statusPanelHeight = 80;
        g2d.drawRect(statusPanelX, statusPanelY, statusPanelWidth, statusPanelHeight);
    }
    
    /**
     * Render stats panel
     */
    private void renderStatsPanel(Graphics2D g2d, Player player, GameState currentState,
                                boolean isStatsNavigationMode, int statsHoveredIndex,
                                int panelHeight, Font pixelFont) {
        int y = panelHeight - 180;
        int statusPanelX = 240;
        int statusPanelWidth = 240;
        int statsPanelX = statusPanelX + statusPanelWidth + 10;
        int statsPanelY = y - 20;
        
        // Calculate panel dimensions
        int imagesPerRow = 6;
        int statsPanelWidth = (imagesPerRow * (STATS_IMAGE_SIZE + STATS_PADDING)) - STATS_PADDING + 16;
        int statsPanelHeight = 90;
        
        // Draw highlight border if in navigation mode
        if (currentState == GameState.PLAYING && isStatsNavigationMode) {
            g2d.setColor(HIGHLIGHT_BORDER_COLOR);
            g2d.setStroke(new BasicStroke(3f));
            g2d.drawRoundRect(statsPanelX - 5, statsPanelY - 5, statsPanelWidth + 9, statsPanelHeight + 9, 18, 18);
            g2d.setStroke(new BasicStroke(1f));
        }
        
        // Draw "Stats" label
        g2d.setFont(pixelFont.deriveFont(11f));
        g2d.setColor(STATS_LABEL_COLOR);
        g2d.drawString("Stats", statsPanelX + 15, statsPanelY + 13);
        
        // Draw hover text
        if (statsHoveredIndex >= 0 && statsHoveredIndex < 6) {
            String[] buttonLabels = {"Health", "Attack", "Defense", "Range", "Speed", "Mana"};
            String hoverText = "Increase " + buttonLabels[statsHoveredIndex] + " By 10%";
            g2d.setFont(pixelFont.deriveFont(9f));
            g2d.setColor(HOVER_TEXT_COLOR);
            g2d.drawString(hoverText, statsPanelX + 80, statsPanelY + 13);
        }
        
        // Draw stat buttons
        renderStatButtons(g2d, player, statsPanelX, statsPanelY, pixelFont);
    }
    
    /**
     * Render stat buttons
     */
    private void renderStatButtons(Graphics2D g2d, Player player, int statsPanelX, int statsPanelY, Font pixelFont) {
        String[] buttonLabels = {"Health", "Attack", "Defense", "Range", "Speed", "Mana"};
        BufferedImage[] buttonImages = {
            statImages.get("health"),
            statImages.get("attack"),
            statImages.get("defense"),
            statImages.get("range"),
            statImages.get("speed"),
            statImages.get("mana")
        };
        
        int startX = statsPanelX + 8;
        int startY = statsPanelY + 38;
        
        for (int i = 0; i < 6; i++) {
            // Skip Mana button if player has no MP
            if (i == 5 && (player == null || player.getPlayerClassOOP().getBaseMp() <= 0)) {
                continue;
            }
            
            int imageX = startX + (i * (STATS_IMAGE_SIZE + STATS_PADDING));
            int imageY = startY;
            
            // Draw stat image
            BufferedImage statImage = buttonImages[i];
            if (statImage != null) {
                g2d.drawImage(statImage, imageX, imageY, STATS_IMAGE_SIZE, STATS_IMAGE_SIZE, null);
            }
            
            // Draw stat value below image
            g2d.setColor(Color.WHITE);
            g2d.setFont(pixelFont.deriveFont(8f));
            String statValue = getStatValue(player, i);
            int textX = imageX + (STATS_IMAGE_SIZE - g2d.getFontMetrics().stringWidth(statValue)) / 2;
            g2d.drawString(statValue, textX, imageY + STATS_IMAGE_SIZE + 12);
        }
    }
    
    /**
     * Get stat value for display
     */
    private String getStatValue(Player player, int statIndex) {
        if (player == null) return "0";
        
        switch (statIndex) {
            case 0: return String.valueOf(player.get_current_hp());
            case 1: return String.valueOf(player.get_total_attack());
            case 2: return String.valueOf(player.get_total_defense());
            case 3: return String.valueOf(player.get_range_uses());
            case 4: return String.valueOf((int)player.getPlayerClassOOP().getMoveSpeed());
            case 5: return String.valueOf(player.get_current_mp());
            default: return "0";
        }
    }
    
    /**
     * Draw a resource bar (HP, MP, etc.)
     */
    private void drawResourceBar(Graphics2D g2d, int x, int y, int width, int height,
                               int current, int max, Color fillColor) {
        // Draw background
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(x, y, width, height);
        
        // Draw fill
        if (max > 0) {
            int fillWidth = (int)((float)current / max * width);
            g2d.setColor(fillColor);
            g2d.fillRect(x, y, fillWidth, height);
        }
        
        // Draw border
        g2d.setColor(Color.WHITE);
        g2d.drawRect(x, y, width, height);
    }
    
    /**
     * Draw a clarity bar (for status effects)
     */
    private void drawClarityBar(Graphics2D g2d, int x, int y, int width, int height,
                              int current, int max, Color fillColor) {
        // Draw background
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(x, y, width, height);
        
        // Draw fill
        if (max > 0) {
            int fillWidth = (int)((float)current / max * width);
            g2d.setColor(fillColor);
            g2d.fillRect(x, y, fillWidth, height);
        }
        
        // Draw border
        g2d.setColor(Color.WHITE);
        g2d.drawRect(x, y, width, height);
    }
    
    /**
     * Set state image for status effects
     */
    public void setStateImage(String effectName, BufferedImage image) {
        stateImages.put(effectName.toLowerCase(), image);
    }
    
    /**
     * Set stat image for stats panel
     */
    public void setStatImage(String statName, BufferedImage image) {
        statImages.put(statName.toLowerCase(), image);
    }
} 