package view.panels;

import model.equipment.Equipment;
import model.equipment.Weapon;
import model.equipment.Armor;
import model.characters.Player;
import enums.GameConstants;
import view.GameView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.List;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.FontMetrics;

/**
 * Equipment panel that displays equipment inventory and allows equipping/unequipping items.
 * Similar to SideInventoryPanel but for equipment management.
 */
public class EquipmentPanel extends JPanel {
    private static final int ICON_SIZE = 32; // Increased from 24 to 32 for larger slots
    private static final int ICON_PADDING = 20;
    private static final int GRID_COLUMNS = 4;
    private static final int GRID_ROWS = 4;
    private static final int SLOT_COUNT = GRID_COLUMNS * GRID_ROWS;
    private static final int SCRAP_BUTTON_SLOT = SLOT_COUNT - 1; // Last slot is scrap button
    
    private GameView parentView;
    private Player player;
    private List<Equipment> equipmentList;
    private int selectedIndex = 0;
    private int hoveredIndex = -1;
    private boolean keyboardNavigation = false;
    private int navRow = -1;
    private int navCol = -1;
    private boolean highlighted = false;
    private boolean isConsuming = false;
    private boolean scrapMode = false; // Track if we're in scrap mode
    
    private HashMap<String, BufferedImage> iconCache = new HashMap<>();
    private GroupedEquipment[] slots = new GroupedEquipment[SLOT_COUNT];
    private Map<String, Integer> groupToSlot = new LinkedHashMap<>(); // Persistent slot mapping
    
    public interface SelectionListener {
        void onSelectionChanged(Equipment equipment);
    }
    private SelectionListener selectionListener;
    
    public void setSelectionListener(SelectionListener listener) {
        this.selectionListener = listener;
    }
    
    public EquipmentPanel(GameView parentView, Player player) {
        this.parentView = parentView;
        this.player = player;
        this.equipmentList = player != null ? player.get_equipment_inventory() : new ArrayList<>();
        
        setBackground(Color.BLACK);
        setOpaque(true);
        setFocusable(false);
        setPreferredSize(new Dimension(220, 80)); // Reduced from 120 to 80 to push content up even further
        
        // Initialize slots array
        for (int i = 0; i < SLOT_COUNT; i++) {
            slots[i] = null;
        }
        

        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isConsuming) return;
                isConsuming = true;
                
                try {
                    int idx = getIconIndexAt(e.getPoint());
                    
                    if (idx != -1) {
                        if (idx == SCRAP_BUTTON_SLOT) {
                                                         // Toggle scrap mode
                             scrapMode = !scrapMode;
                             repaint();
                        } else if (slots[idx] != null) {
                            Equipment equipment = slots[idx].equipment;
                            Player currentPlayer = getCurrentPlayer();
                                                         if (scrapMode) {
                                 // Scrap the equipment
                                 if (currentPlayer != null && currentPlayer.scrap_equipment(equipment)) {
                                     // Update the equipment list immediately after successful scrapping
                                     updateEquipmentList();
                                     repaint();
                                 }
                             } else {
                                 // Normal equipment selection/equipping
                                 if (selectionListener != null) selectionListener.onSelectionChanged(equipment);
                                 equipSelectedItem(equipment);
                             }
                        }
                    }
                } finally {
                    isConsuming = false;
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                keyboardNavigation = false;
                repaint();
            }
        });
        
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int idx = getIconIndexAt(e.getPoint());
                if (idx != hoveredIndex) {
                    hoveredIndex = idx;
                    repaint();
                }
            }
        });
    }
    
    public void setEquipmentList(List<Equipment> equipmentList) {
        // Update the equipment list field
        this.equipmentList = equipmentList;
        
        // Clear all slots
        for (int i = 0; i < SLOT_COUNT; i++) {
            slots[i] = null;
        }
        
        // Populate slots with individual equipment (no grouping)
        for (int i = 0; i < equipmentList.size() && i < SLOT_COUNT - 1; i++) { // -1 to reserve last slot for scrap button
            Equipment equipment = equipmentList.get(i);
            slots[i] = new GroupedEquipment(equipment, 1); // Always quantity 1 since no duplicates
        }
        
        if (equipmentList == null || equipmentList.isEmpty()) {
            selectedIndex = 0;
        } else if (selectedIndex >= equipmentList.size()) {
            selectedIndex = equipmentList.size() - 1;
        }
        
        if (selectionListener != null && equipmentList != null && !equipmentList.isEmpty()) {
            selectionListener.onSelectionChanged(equipmentList.get(selectedIndex));
        }
        repaint();
    }
    
    private void equipSelectedItem(Equipment equipment) {
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer != null) {
            if (equipment.get_equipment_type() == Equipment.EquipmentType.WEAPON) {
                currentPlayer.equip_weapon(equipment);
            } else if (equipment.get_equipment_type() == Equipment.EquipmentType.ARMOR) {
                currentPlayer.equip_armor(equipment);
            }
            // Update the equipment list to reflect changes
            updateEquipmentList();
            repaint();
        }
    }
    
    private BufferedImage getIconForEquipment(Equipment equipment) {
        String key = equipment.get_name();
        if (iconCache.containsKey(key)) {
            return iconCache.get(key);
        }
        
        // Use the equipment's actual image path
        String imagePath = equipment.get_image_path();
        
        BufferedImage icon = loadImage(imagePath);
        iconCache.put(key, icon);
        return icon;
    }
    
    private BufferedImage loadImage(String path) {
        try {
            // Don't add "images/" prefix since equipment paths already include it
            InputStream is = getClass().getClassLoader().getResourceAsStream(path);
            if (is != null) {
                return javax.imageio.ImageIO.read(is);
            }
        } catch (Exception e) {
            // Fallback to default colored rectangle if image fails to load
        }
        
        // Create a default colored rectangle if image fails to load
        BufferedImage defaultIcon = new BufferedImage(ICON_SIZE, ICON_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = defaultIcon.createGraphics();
        g2d.setColor(Color.GRAY);
        g2d.fillRect(0, 0, ICON_SIZE, ICON_SIZE);
        g2d.setColor(Color.WHITE);
        g2d.drawRect(0, 0, ICON_SIZE - 1, ICON_SIZE - 1);
        g2d.dispose();
        return defaultIcon;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // 1. Draw equipped info at the very top
        drawEquippedInfoTop(g);
        
        // 2. Draw persistent 'Equipment' label in light blue
        g.setFont(parentView.getPixelFont().deriveFont(14f));
        g.setColor(new Color(100, 200, 255)); // Light blue
        g.drawString("Equipment", ICON_PADDING, 50); // Moved down from 35 to 50 to be below equipped info
        
        // 3. Draw hovered equipment info below the label
        int infoIndex = keyboardNavigation ? (navRow >= 0 && navCol >= 0 ? navRow * GRID_COLUMNS + navCol : -1) : hoveredIndex;
        if (infoIndex >= 0 && infoIndex < SLOT_COUNT && slots[infoIndex] != null) {
            GroupedEquipment hovered = slots[infoIndex];
            g.setFont(parentView.getPixelFont().deriveFont(10f)); // Reduced from 12f
            g.setColor(Color.GREEN);
            int infoY = 68; // Moved down from 53 to 68 to be below Equipment title
            g.drawString(hovered.equipment.get_name(), ICON_PADDING, infoY);
            g.setColor(Color.LIGHT_GRAY);
            g.drawString(getShortStatDescription(hovered.equipment), ICON_PADDING, infoY + 14); // Reduced spacing
        }
        
        // Draw highlight border if in equipment navigation mode
        if (highlighted) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(255, 255, 100));
            g2d.setStroke(new BasicStroke(5f));
            g2d.drawRoundRect(2, 0, getWidth()-5, getHeight()-5, 18, 18);
            g2d.setStroke(new BasicStroke(1f));
        }
        
        // 4. Draw equipment slots
        int x0 = ICON_PADDING;
        int y0 = ICON_PADDING + 85; // Moved down from 70 to 85 to be below hover info space
        int col = 0, row = 0;
        
        for (int i = 0; i < SLOT_COUNT; i++) {
            GroupedEquipment groupedEquipment = slots[i];
            int x = x0 + col * (ICON_SIZE + ICON_PADDING);
            int y = y0 + row * (ICON_SIZE + ICON_PADDING);
            
            // Highlight: only show on hover (mouse) or in keyboard nav mode
            boolean highlight = false;
            if (keyboardNavigation && navRow >= 0 && navCol >= 0 && i == navRow * GRID_COLUMNS + navCol) highlight = true;
            if (!keyboardNavigation && i == hoveredIndex) highlight = true;
            
            if (highlight) {
                g.setColor(Color.YELLOW);
                g.drawRect(x - 6, y - 6, ICON_SIZE + 11, ICON_SIZE + 11);
            }
            
            if (i == SCRAP_BUTTON_SLOT) {
                // Draw scrap button as part of the grid
                drawScrapButtonInGrid(g, x, y);
            } else if (groupedEquipment != null) {
                // Draw equipment with scrap mode background if active
                if (scrapMode) {
                    g.setColor(new Color(200, 50, 50)); // Less bright, more saturated red background
                    g.fillRect(x - 2, y - 2, ICON_SIZE + 4, ICON_SIZE + 4);
                }
                
                Equipment equipment = groupedEquipment.equipment;
                
                // Draw equipment image
                BufferedImage equipmentIcon = getIconForEquipment(equipment);
                if (equipmentIcon != null) {
                    g.drawImage(equipmentIcon, x, y, ICON_SIZE, ICON_SIZE, null);
                } else {
                    // Fallback to single letter if image fails to load
                    g.setColor(Color.WHITE);
                    g.setFont(parentView.getPixelFont().deriveFont(12f));
                    String equipmentText = equipment.get_name().substring(0, 1).toUpperCase();
                    FontMetrics fm = g.getFontMetrics();
                    int textWidth = fm.stringWidth(equipmentText);
                    int textX = x + (ICON_SIZE - textWidth) / 2;
                    int textY = y + ICON_SIZE / 2 + 8;
                    g.drawString(equipmentText, textX, textY);
                }
                
                // Draw tier indicator
                g.setColor(getTierColor(equipment.get_tier()));
                g.setFont(parentView.getPixelFont().deriveFont(8f));
                g.drawString("T" + equipment.get_tier(), x + 2, y + 10);
                
                // No quantity display since we don't allow duplicates
            } else {
                // Draw empty slot
                g.setColor(new Color(60, 60, 60));
                g.drawRect(x, y, ICON_SIZE, ICON_SIZE);
            }
            
            col++;
            if (col >= GRID_COLUMNS) {
                col = 0;
                row++;
            }
        }
        
        // Draw scrap bar below the equipment slots
        drawScrapBar(g);
    }
    
    private void drawScrapButtonInGrid(Graphics g, int x, int y) {
        // Draw button background if in scrap mode
        if (scrapMode) {
            g.setColor(new Color(200, 50, 50)); // Less bright, more saturated red background
            g.fillRect(x - 2, y - 2, ICON_SIZE + 4, ICON_SIZE + 4);
        }
        
        // Draw button border
        g.setColor(Color.WHITE);
        g.drawRect(x, y, ICON_SIZE, ICON_SIZE);
        
        // Load and draw trash.png image (centered, smaller)
        try {
            BufferedImage trashIcon = ImageIO.read(new File("C:\\Users\\diego\\OneDrive\\Escritorio\\miniRogueDemo\\src\\main\\resources\\images\\items\\trash.png"));
            if (trashIcon != null) {
                // Center the image in the button, make it smaller
                int imageSize = ICON_SIZE - 8; // Smaller image
                int imageX = x + (ICON_SIZE - imageSize) / 2; // Center horizontally
                int imageY = y + 2; // Position near top
                g.drawImage(trashIcon, imageX, imageY, imageSize, imageSize, null);
            }
        } catch (IOException e) {
            // Fallback to text if image fails to load
            g.setFont(new Font("Arial", Font.BOLD, 6));
            g.setColor(Color.BLACK);
            g.drawString("TRASH", x + 4, y + 12);
        }
        
        // Draw "Scrap" text underneath in white pixelated font (centered, smaller)
        g.setFont(parentView.getPixelFont().deriveFont(8f)); // Increased from 6f to 8f
        g.setColor(Color.WHITE);
        String text = "Scrap";
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textX = x + (ICON_SIZE - textWidth) / 2; // Center horizontally
        int textY = y + ICON_SIZE + 16; // Moved further down from +10 to +16
        g.drawString(text, textX, textY);
    }
    
    private Color getTierColor(int tier) {
        switch (tier) {
            case 1: return Color.WHITE;
            case 2: return Color.GREEN;
            case 3: return Color.ORANGE;
            default: return Color.WHITE;
        }
    }
    
    private void drawEquippedInfo(Graphics g) {
        int infoY = getHeight() - 80;
        
        g.setColor(new Color(100, 150, 255));
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString("Equipped:", 10, infoY);
        
        // Draw equipped weapon
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 10));
        Weapon equippedWeapon = player.get_equipped_weapon();
        if (equippedWeapon != null) {
            g.drawString("Weapon: " + equippedWeapon.get_name() + " [T" + equippedWeapon.get_tier() + "]", 10, infoY + 14);
        } else {
            g.drawString("Weapon: None", 10, infoY + 14);
        }
        
        // Draw equipped armor
        Armor equippedArmor = player.get_equipped_armor();
        if (equippedArmor != null) {
            g.drawString("Armor: " + equippedArmor.get_name() + " [T" + equippedArmor.get_tier() + "]", 10, infoY + 28);
        } else {
            g.drawString("Armor: None", 10, infoY + 28);
        }
    }
    
    private void drawEquippedInfoTop(Graphics g) {
        // Draw equipped info at the very top of the panel
        int startX = 10; // Start from left edge
        int iconSize = 32; // Increased from 24 to 32 for larger icons
        int iconSpacing = 40; // Increased from 30 to 40 for more space between icons
        
        // Use pixel font to match the rest of the program
        g.setColor(new Color(100, 150, 255));
        g.setFont(parentView.getPixelFont().deriveFont(10f));
        g.drawString("Equipped:", startX, 15); // At the very top
        
        Player currentPlayer = getCurrentPlayer();
        Weapon equippedWeapon = currentPlayer != null ? currentPlayer.get_equipped_weapon() : null;
        Armor equippedArmor = currentPlayer != null ? currentPlayer.get_equipped_armor() : null;
        
        // Draw equipped weapon icon and tier - moved further right
        int weaponX = startX + 120; // Increased from 85 to 120 to move further right
        if (equippedWeapon != null) {
            // Draw weapon image
            BufferedImage weaponIcon = getIconForEquipment(equippedWeapon);
            if (weaponIcon != null) {
                g.drawImage(weaponIcon, weaponX, 3, iconSize, iconSize, null); // Adjusted Y from 5 to 3
            } else {
                // Fallback to colored rectangle if image fails to load
                g.setColor(Color.GRAY);
                g.fillRect(weaponX, 3, iconSize, iconSize);
                g.setColor(Color.WHITE);
                g.drawRect(weaponX, 3, iconSize - 1, iconSize - 1);
            }
            
            // Draw tier indicator
            g.setColor(getTierColor(equippedWeapon.get_tier()));
            g.setFont(parentView.getPixelFont().deriveFont(8f));
            g.drawString("T" + equippedWeapon.get_tier(), weaponX + 2, 15);
        } else {
            // Draw "None" placeholder with properly centered text
            g.setColor(Color.DARK_GRAY);
            g.fillRect(weaponX, 3, iconSize, iconSize);
            g.setColor(Color.WHITE);
            g.setFont(parentView.getPixelFont().deriveFont(10f)); // Increased from 8f to 10f
            
            // Center the "None" text in the square
            String noneText = "None";
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(noneText);
            int textX = weaponX + (iconSize - textWidth) / 2; // Center horizontally
            int textY = weaponX + (iconSize / 2) + 5; // Center vertically (weaponX + iconSize/2 + 5 = 3 + 16 + 5 = 24)
            g.drawString(noneText, textX, textY);
        }
        
        // Draw equipped armor icon and tier
        int armorX = weaponX + iconSpacing;
        if (equippedArmor != null) {
            // Draw armor image
            BufferedImage armorIcon = getIconForEquipment(equippedArmor);
            if (armorIcon != null) {
                g.drawImage(armorIcon, armorX, 3, iconSize, iconSize, null); // Adjusted Y from 5 to 3
            } else {
                // Fallback to colored rectangle if image fails to load
                g.setColor(Color.GRAY);
                g.fillRect(armorX, 3, iconSize, iconSize);
                g.setColor(Color.WHITE);
                g.drawRect(armorX, 3, iconSize - 1, iconSize - 1);
            }
            
            // Draw tier indicator
            g.setColor(getTierColor(equippedArmor.get_tier()));
            g.setFont(parentView.getPixelFont().deriveFont(8f));
            g.drawString("T" + equippedArmor.get_tier(), armorX + 2, 15);
        } else {
            // Draw "None" placeholder with properly centered text
            g.setColor(Color.DARK_GRAY);
            g.fillRect(armorX, 3, iconSize, iconSize);
            g.setColor(Color.WHITE);
            g.setFont(parentView.getPixelFont().deriveFont(10f)); // Increased from 8f to 10f
            
            // Center the "None" text in the square
            String noneText = "None";
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(noneText);
            int textX = armorX + (iconSize - textWidth) / 2; // Center horizontally
            int textY = armorX + (iconSize / 2) + 5; // Center vertically (armorX + iconSize/2 + 5)
            g.drawString(noneText, textX, textY);
        }
    }
    
    private int getIconIndexAt(Point p) {
        int x0 = ICON_PADDING;
        int y0 = ICON_PADDING + 85; // Match the new slot positioning
        int col = (p.x - x0) / (ICON_SIZE + ICON_PADDING);
        int row = (p.y - y0) / (ICON_SIZE + ICON_PADDING);
        if (col < 0 || col >= GRID_COLUMNS || row < 0 || row >= GRID_ROWS) return -1;
        int idx = row * GRID_COLUMNS + col;
        int iconX = x0 + col * (ICON_SIZE + ICON_PADDING);
        int iconY = y0 + row * (ICON_SIZE + ICON_PADDING);
        Rectangle iconRect = new Rectangle(iconX, iconY, ICON_SIZE, ICON_SIZE);
        if (iconRect.contains(p)) return idx;
        return -1;
    }
    
    // Keyboard navigation API
    public void setKeyboardNavigation(boolean enabled, int row, int col) {
        this.keyboardNavigation = enabled;
        this.navRow = enabled ? row : -1;
        this.navCol = enabled ? col : -1;
        if (!enabled) {
            this.hoveredIndex = -1;
        }
        repaint();
    }
    
    public void setEquipmentHighlight(boolean highlight) {
        this.highlighted = highlight;
        repaint();
    }
    
    public int getGridRows() { return GRID_ROWS; }
    public int getGridCols() { return GRID_COLUMNS; }
    
    public void updateEquipmentList() {
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer != null) {
            setEquipmentList(currentPlayer.get_equipment_inventory());
        }
    }
    
    /**
     * Update the player reference (called when starting a new game)
     */
    public void setPlayer(Player player) {
        this.player = player;
        // Reset scrap mode when player changes
        scrapMode = false;
        
        // Force clear all slots and reset state
        for (int i = 0; i < SLOT_COUNT; i++) {
            slots[i] = null;
        }
        groupToSlot.clear();
        
        // Update equipment list with new player
        if (player != null) {
            setEquipmentList(player.get_equipment_inventory());
        }
        repaint();
    }
    
    /**
     * Exit scrap mode (called when exiting equipment menu)
     */
    public void exitScrapMode() {
        scrapMode = false;
        repaint();
    }
    
    /**
     * Get current scrap mode state
     */
    public boolean isScrapMode() {
        return scrapMode;
    }
    
    /**
     * Get the current player reference, ensuring it's not null
     */
    private Player getCurrentPlayer() {
        return player;
    }
    
    /**
     * Handle keyboard selection (for Q key navigation)
     */
    public void handleKeyboardSelection() {
        if (keyboardNavigation && navRow >= 0 && navCol >= 0) {
            int idx = navRow * GRID_COLUMNS + navCol;
            if (idx >= 0 && idx < SLOT_COUNT) {
                if (idx == SCRAP_BUTTON_SLOT) {
                                         // Toggle scrap mode
                     scrapMode = !scrapMode;
                     repaint();
                } else if (slots[idx] != null) {
                    Equipment equipment = slots[idx].equipment;
                    Player currentPlayer = getCurrentPlayer();
                                         if (scrapMode) {
                         // Scrap the equipment
                         if (currentPlayer != null && currentPlayer.scrap_equipment(equipment)) {
                             // Update the equipment list immediately after successful scrapping
                             updateEquipmentList();
                             repaint();
                         }
                     } else {
                        // Normal equipment selection/equipping
                        if (selectionListener != null) selectionListener.onSelectionChanged(equipment);
                        equipSelectedItem(equipment);
                    }
                }
            }
        }
    }
    
                   // Helper class for grouping
      private static class GroupedEquipment {
          Equipment equipment;
          int qty;
          GroupedEquipment(Equipment equipment, int qty) { this.equipment = equipment; this.qty = qty; }
      }
      
      /**
       * Get short stat description for equipment
       */
      private String getShortStatDescription(Equipment equipment) {
          Map<String, Float> modifiers = equipment.get_stat_modifiers();
          if (modifiers.isEmpty()) {
              return "No stats";
          }
          
          StringBuilder sb = new StringBuilder();
          for (Map.Entry<String, Float> entry : modifiers.entrySet()) {
              if (sb.length() > 0) sb.append(", ");
              float value = entry.getValue();
              String statName = entry.getKey().substring(0, Math.min(3, entry.getKey().length())).toUpperCase();
              String sign = value >= 0 ? "+" : "";
              sb.append(statName).append(sign).append((int)value);
          }
          return sb.toString();
      }

    private void drawScrapBar(Graphics g) {
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer == null) return;
        
        // Calculate position below equipment slots - positioned just below the last row
        int lastRowY = ICON_PADDING + 85 + (GRID_ROWS - 1) * (ICON_SIZE + ICON_PADDING); // Position of last row
        int barY = lastRowY + ICON_SIZE + 5; // Just below the last row with 5px gap
        
        // Draw scrap bar with same proportions as HP/MP/XP bars
        int barX = ICON_PADDING; // Start from first column instead of 80
        int barWidth = (ICON_SIZE + ICON_PADDING) * 3 - ICON_PADDING; // Span 3 columns (4 slots minus padding)
        int barHeight = 8; // Same height as other bars
        
        // Draw bar background (dark gray)
        g.setColor(new Color(50, 50, 50));
        g.fillRect(barX, barY, barWidth, barHeight);
        
        // Draw white border with thick stroke (like other bars)
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new java.awt.BasicStroke(3f));
        g2d.drawRect(barX, barY, barWidth, barHeight);
        
        // Draw scrap fill (violet color)
        if (currentPlayer.get_total_scrap() > 0) {
            double percentage = (double) currentPlayer.get_current_scrap() / currentPlayer.get_total_scrap();
            int fillWidth = (int) (barWidth * percentage);
            g2d.setColor(new Color(148, 0, 211)); // VIOLET color
            g2d.fillRect(barX + 3, barY + 3, fillWidth - 3, barHeight - 3);
        }
        
        // Reset stroke to default
        g2d.setStroke(new java.awt.BasicStroke(1f));
    }
} 