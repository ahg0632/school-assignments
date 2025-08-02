package view.panels;

import model.items.Item;
import view.GameView;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.HashMap;
import java.awt.event.MouseMotionAdapter;
import java.util.LinkedHashMap;
import java.util.Map;

public class SideInventoryPanel extends JPanel {
    private GameView parentView;
    private List<Item> items;
    private int selectedIndex = 0;
    private static final int ICON_SIZE = 32; // Increased from 24 to 32 for larger slots
    private static final int ICON_PADDING = 20; // Increased from 12 to 20 to match equipment panel spacing
    private static final int GRID_COLUMNS = 4; // Changed to 4 columns for 16x16 distribution
    private static final int GRID_ROWS = 4; // Changed to 4 rows for 16x16 distribution
    private static final int SLOT_COUNT = GRID_COLUMNS * GRID_ROWS;
    private GroupedItem[] slots = new GroupedItem[SLOT_COUNT];
    private int hoveredIndex = -1;
    private Map<String, Integer> groupToSlot = new LinkedHashMap<>(); // Persistent slot mapping
    private HashMap<String, java.awt.image.BufferedImage> iconCache = new HashMap<>();
    private boolean isConsuming = false;
    private boolean keyboardNavigation = false;
    private int navRow = -1;
    private int navCol = -1;
    private boolean highlighted = false;
    public interface SelectionListener {
        void onSelectionChanged(Item item);
    }
    private SelectionListener selectionListener;
    public void setSelectionListener(SelectionListener listener) {
        this.selectionListener = listener;
    }
    public SideInventoryPanel(GameView parentView) {
        this.parentView = parentView;
        setBackground(Color.BLACK);
        setOpaque(true);
        setFocusable(false);
        setPreferredSize(new Dimension(220, 80)); // Reduced from 100 to 80 to push content up even further
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isConsuming) return;
                isConsuming = true;
                int idx = getIconIndexAt(e.getPoint());
                if (idx != -1 && slots[idx] != null) {
                    if (selectionListener != null) selectionListener.onSelectionChanged(slots[idx].item);
                    // If consumable or upgrade crystal, use on click
                    Item item = slots[idx].item;
                    if (item.getClass().getSimpleName().equals("Consumable") || 
                        (item instanceof model.items.KeyItem && item.get_name().equals("Upgrade Crystal"))) {
                        parentView.get_controller().handle_input("USE_ITEM", item);
                    }
                }
                isConsuming = false;
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
    public void setItems(List<Item> items) {
        // Handle null input gracefully
        if (items == null) {
            items = new java.util.ArrayList<>();
        }
        
        // Track which groups are present this update
        Map<String, Integer> groupCounts = new LinkedHashMap<>();
        for (Item item : items) {
            String key = item.get_name() + ":" + item.get_potency();
            if (item instanceof model.items.Consumable) {
                key += ":" + ((model.items.Consumable)item).get_effect_type();
            } else if (item instanceof model.items.KeyItem) {
                // For KeyItems, also include the upgrade type to ensure proper grouping
                key += ":" + ((model.items.KeyItem)item).get_upgrade_type();
            }
            groupCounts.put(key, groupCounts.getOrDefault(key, 0) + 1);
        }
        // Remove slot assignments for groups that are no longer present
        groupToSlot.keySet().removeIf(key -> !groupCounts.containsKey(key));
        // Find available slots
        boolean[] slotUsed = new boolean[SLOT_COUNT];
        for (Integer slotIdx : groupToSlot.values()) {
            if (slotIdx != null && slotIdx >= 0 && slotIdx < SLOT_COUNT) slotUsed[slotIdx] = true;
        }
        // Assign slots to new groups
        for (String key : groupCounts.keySet()) {
            if (!groupToSlot.containsKey(key)) {
                // Find first available slot
                for (int i = 0; i < SLOT_COUNT; i++) {
                    if (!slotUsed[i]) {
                        groupToSlot.put(key, i);
                        slotUsed[i] = true;
                        break;
                    }
                }
            }
        }
        // Rebuild slots array
        slots = new GroupedItem[SLOT_COUNT];
        for (Map.Entry<String, Integer> entry : groupCounts.entrySet()) {
            String key = entry.getKey();
            int qty = entry.getValue();
            Integer slotIdx = groupToSlot.get(key);
            if (slotIdx != null && slotIdx >= 0 && slotIdx < SLOT_COUNT) {
                // Find a representative item for this group
                Item rep = null;
                for (Item item : items) {
                    String itemKey = item.get_name() + ":" + item.get_potency();
                    if (item instanceof model.items.Consumable) {
                        itemKey += ":" + ((model.items.Consumable)item).get_effect_type();
                    } else if (item instanceof model.items.KeyItem) {
                        // For KeyItems, also include the upgrade type to ensure proper grouping
                        itemKey += ":" + ((model.items.KeyItem)item).get_upgrade_type();
                    }
                    if (itemKey.equals(key)) { rep = item; break; }
                }
                if (rep != null) slots[slotIdx] = new GroupedItem(rep, qty);
            }
        }
        repaint();
    }
    private java.awt.image.BufferedImage getIconForItem(Item item) {
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
        if (iconCache.containsKey(iconFile)) return iconCache.get(iconFile);
        try {
            java.awt.image.BufferedImage img = javax.imageio.ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/items/" + iconFile));
            iconCache.put(iconFile, img);
            return img;
        } catch (java.io.IOException | IllegalArgumentException ex) {
            return null;
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw persistent 'Inventory' label in light blue
        g.setFont(parentView.getPixelFont().deriveFont(14f));
        g.setColor(new Color(100, 200, 255)); // Light blue
        g.drawString("Inventory", ICON_PADDING, 18); // Back to original position
        // Draw hovered item info below the label
        int infoIndex = keyboardNavigation ? (navRow >= 0 && navCol >= 0 ? navRow * GRID_COLUMNS + navCol : -1) : hoveredIndex;
        if (infoIndex >= 0 && infoIndex < SLOT_COUNT && slots[infoIndex] != null) {
            GroupedItem hovered = slots[infoIndex];
            g.setFont(parentView.getPixelFont().deriveFont(10f)); // Reduced from 12f
            g.setColor(Color.GREEN);
            int infoY = 36; // Back to original position
            g.drawString(hovered.item.get_name(), ICON_PADDING, infoY);
            g.setColor(Color.LIGHT_GRAY);
            g.drawString(getShortStatDescription(hovered.item), ICON_PADDING, infoY + 14); // Reduced spacing from 16
        }
        // Draw highlight border if in inventory navigation mode
        if (highlighted) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(255, 255, 100));
            g2d.setStroke(new BasicStroke(5f));
            // Top border above the label
            g2d.drawRoundRect(2, 0, getWidth()-5, getHeight()-5, 18, 18);
            g2d.setStroke(new BasicStroke(1f));
        }
        int x0 = ICON_PADDING;
        int y0 = ICON_PADDING + 56; // Back to original position
        int col = 0, row = 0;
        for (int i = 0; i < SLOT_COUNT; i++) {
            GroupedItem groupedItem = slots[i];
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
            if (groupedItem != null) {
                Item item = groupedItem.item;
                java.awt.image.BufferedImage icon = getIconForItem(item);
                if (icon != null) {
                    // Calculate size multiplier for potions
                    float sizeMultiplier = 1.0f;
                    if (item instanceof model.items.Consumable) {
                        model.items.Consumable consumable = (model.items.Consumable) item;
                        sizeMultiplier = consumable.getPotionSizeMultiplier();
                    }
                    
                    // Calculate scaled size and position
                    int scaledSize = (int)(ICON_SIZE * sizeMultiplier);
                    int offsetX = (ICON_SIZE - scaledSize) / 2;
                    int offsetY = (ICON_SIZE - scaledSize) / 2;
                    
                    g.drawImage(icon, x + offsetX, y + offsetY, scaledSize, scaledSize, null);
                } else {
                    g.setColor(Color.GRAY);
                    g.fillRect(x, y, ICON_SIZE, ICON_SIZE);
                }
                // Draw quantity (bottom right)
                if (groupedItem.qty > 1) {
                    g.setFont(parentView.getPixelFont().deriveFont(10f)); // Reduced from 12f
                    g.setColor(Color.WHITE);
                    g.drawString("x" + groupedItem.qty, x + ICON_SIZE - 18, y + ICON_SIZE - 6);
                }
            } else {
                // Draw empty slot (optional: draw faint border)
                g.setColor(new Color(60, 60, 60));
                g.drawRect(x, y, ICON_SIZE, ICON_SIZE);
            }
            col++;
            if (col >= GRID_COLUMNS) {
                col = 0;
                row++;
            }
        }
    }
    // Helper class for grouping
    private static class GroupedItem {
        Item item;
        int qty;
        GroupedItem(Item item, int qty) { this.item = item; this.qty = qty; }
    }
    
    /**
     * Get short description for items
     */
    private String getShortStatDescription(Item item) {
        if (item == null) return "";
        
        String name = item.get_name().toLowerCase();
        
        // Health potions
        if (name.contains("health") && name.contains("potion")) {
            if (name.contains("minor")) {
                return "Restores 25 HP";
            } else if (name.contains("greater")) {
                return "Restores 100 HP";
            } else {
                return "Restores 50 HP";
            }
        }
        // Mana potions
        else if (name.contains("mana") && name.contains("potion")) {
            if (name.contains("greater")) {
                return "Restores 60 MP";
            } else {
                return "Restores 30 MP";
            }
        }
        // Experience scrolls
        else if (name.contains("experience") && name.contains("scroll")) {
            return "Grants 100 XP";
        }
        // Lamp (clarity effect)
        else if (name.contains("lamp")) {
            return "Reveals hidden enemies";
        }
        // Vanish Cloak (invisibility effect)
        else if (name.contains("vanish") && name.contains("cloak")) {
            return "Makes you invisible";
        }
        // Swift Winds (speed effect)
        else if (name.contains("swift") && name.contains("winds")) {
            return "Increases movement speed";
        }
        // Upgrade crystals
        else if (name.contains("crystal")) {
            return "Upgrades any equipment";
        }
        // Floor Key
        else if (name.contains("floor") && name.contains("key")) {
            return "Move to the Next Floor";
        }
        // Immortality amulet
        else if (name.contains("immortality") && name.contains("amulet")) {
            return "Temporary invincibility";
        }
        
        // Default description for equipment
        if (item instanceof model.equipment.Equipment) {
            return "Equipment item";
        }
        
        return "Unknown item";
    }
    // Update getIconIndexAt and mouse logic to use groupedList size
    @Override
    public void addMouseMotionListener(java.awt.event.MouseMotionListener l) {
        super.addMouseMotionListener(l);
    }
    @Override
    public void addMouseListener(java.awt.event.MouseListener l) {
        super.addMouseListener(l);
    }
    private int getIconIndexAt(Point p) {
        int x0 = ICON_PADDING;
        int y0 = ICON_PADDING + 44; // Match the y0 used in paintComponent
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
    public void setInventoryHighlight(boolean highlight) {
        this.highlighted = highlight;
        repaint();
    }
    public int getGridRows() { return GRID_ROWS; }
    public int getGridCols() { return GRID_COLUMNS; }
    public void consumeSelectedItem(int row, int col) {
        int idx = row * GRID_COLUMNS + col;
        if (idx < 0 || idx >= SLOT_COUNT) return;
        GroupedItem grouped = slots[idx];
        if (grouped != null && grouped.item != null) {
            if (selectionListener != null) selectionListener.onSelectionChanged(grouped.item);
            Item item = grouped.item;
            if (item.getClass().getSimpleName().equals("Consumable")) {
                parentView.get_controller().handle_input("USE_ITEM", item);
            }
        }
    }
} 