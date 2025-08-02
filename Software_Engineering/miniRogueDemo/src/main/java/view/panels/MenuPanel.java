package view.panels;

import view.GameView;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MenuPanel extends JPanel {
    private static final String[] MENU_OPTIONS = {"Start New Game", "Toggle Mouse Aiming", "Exit"};
    private static final String[] CLASS_OPTIONS = {"Warrior", "Mage", "Rogue", "Ranger"};
    private static final Color[] CLASS_COLORS = {Color.BLUE, Color.ORANGE, new Color(128,0,128), Color.GREEN};
    private static final int SPRITE_PREVIEW_SCALE = 3; // Scale factor for character preview sprites
    
    // Simple sprite column configuration
    private static final int SPRITE_OFFSET_X = -100;        // Distance left of text for sprite
    private static final int SPRITE_SIZE = 48;             // Size to render sprite (3x scale for 16x16)
    private static final int SPRITE_VERTICAL_ADJUST = -40; // Vertical adjustment to align with text baseline
    
    private int selectedIndex = 0;
    private int classSelectedIndex = 0;
    private GameView parentView;
    private enum Mode { MAIN_MENU, CLASS_SELECTION }
    private Mode mode = Mode.MAIN_MENU;
    private static final int BACK_OPTION_INDEX = 4;
    private Rectangle backButtonBounds = new Rectangle();
    private int hoveredIndex = -1;
    private boolean mouseAimingMenuState = false;
    
    // Character sprite previews for menu (down1 frame for each class)
    private BufferedImage warriorPreview = null;
    private BufferedImage magePreview = null;      // Future: will use default until sprite sheet added
    private BufferedImage roguePreview = null;     // Future: will use default until sprite sheet added  
    private BufferedImage rangerPreview = null;    // Future: will use default until sprite sheet added
    
    // Default sprite previews (boy sprites)
    private BufferedImage defaultPreview = null;

    public MenuPanel(GameView parentView) {
        this.parentView = parentView;
        setFocusable(true); // Changed from false to true
        setBackground(Color.DARK_GRAY);
        setPreferredSize(new Dimension(1024, 768));
        enableEvents(java.awt.AWTEvent.MOUSE_EVENT_MASK); // Explicitly enable mouse events
        
        // Load character sprite previews for menu
        loadCharacterPreviews();
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                if (mode == Mode.MAIN_MENU) {
                    int prevHovered = hoveredIndex;
                    hoveredIndex = -1;
                    for (int i = 0; i < MENU_OPTIONS.length; i++) {
                        int optionY = 300 + i * 60;
                        int optionHeight = 40;
                        int optionWidth = getFontMetrics(new Font("Arial", Font.PLAIN, 32)).stringWidth(MENU_OPTIONS[i]);
                        int optionX = (getWidth() - optionWidth) / 2;
                        Rectangle optionRect = new Rectangle(optionX - 20, optionY - 32, optionWidth + 40, optionHeight);
                        if (optionRect.contains(e.getPoint())) {
                            hoveredIndex = i;
                            break;
                        }
                    }
                    if (hoveredIndex != prevHovered) repaint();
                } else if (mode == Mode.CLASS_SELECTION) {
                    int prevHovered = hoveredIndex;
                    hoveredIndex = -1;
                    for (int i = 0; i < CLASS_OPTIONS.length; i++) {
                        int optionY = 350 + i * 60;
                        int optionHeight = 40;
                        int optionWidth = getFontMetrics(new Font("Arial", Font.PLAIN, 32)).stringWidth(CLASS_OPTIONS[i]);
                        int optionX = (getWidth() - optionWidth) / 2;
                        Rectangle optionRect = new Rectangle(optionX - 20, optionY - 32, optionWidth + 40, optionHeight);
                        if (optionRect.contains(e.getPoint())) {
                            hoveredIndex = i;
                            break;
                        }
                    }
                    // Back button
                    int backY = 350 + CLASS_OPTIONS.length * 60 + 40;
                    String backText = "Back";
                    int backWidth = getFontMetrics(new Font("Arial", Font.PLAIN, 28)).stringWidth(backText);
                    int backX = (getWidth() - backWidth) / 2;
                    Rectangle backRect = new Rectangle(backX - 20, backY - 32, backWidth + 40, 40);
                    if (backRect.contains(e.getPoint())) {
                        hoveredIndex = BACK_OPTION_INDEX;
                    }
                    if (hoveredIndex != prevHovered) repaint();
                }
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (hoveredIndex != -1) {
                    hoveredIndex = -1;
                    repaint();
                }
            }
        });
    }
    
    /**
     * Load character sprite previews for the character selection menu
     * Uses down1 frame (direction "down", frame 0) for each character class
     */
    private void loadCharacterPreviews() {
        try {
            // Load default sprite (boy_down_1.png) for fallback
            defaultPreview = javax.imageio.ImageIO.read(
                getClass().getClassLoader().getResourceAsStream("images/player/boy_down_1.png"));
            
            // Load warrior sprite from sprite sheet
            BufferedImage warriorSheet = javax.imageio.ImageIO.read(
                getClass().getClassLoader().getResourceAsStream("images/player/warrior_sheet.png"));
            
            if (warriorSheet != null) {
                // Extract warrior down1 sprite (column 0, row 0 = top-left)
                warriorPreview = warriorSheet.getSubimage(0, 0, 15, 21);
                System.out.println("Warrior preview sprite loaded successfully");
            } else {
                System.err.println("Warrior sprite sheet not found, using default");
                warriorPreview = defaultPreview;
            }
            
            // Load rogue sprite from sprite sheet
            BufferedImage rogueSheet = javax.imageio.ImageIO.read(
                getClass().getClassLoader().getResourceAsStream("images/player/rogue_sheet.png"));
            
            if (rogueSheet != null) {
                // Extract rogue down1 sprite (column 0, row 0 = top-left)
                roguePreview = rogueSheet.getSubimage(0, 0, 14, 20);
                System.out.println("Rogue preview sprite loaded successfully");
            } else {
                System.err.println("Rogue sprite sheet not found, using default");
                roguePreview = defaultPreview;
            }
            
            // Load ranger sprite from sprite sheet
            BufferedImage rangerSheet = javax.imageio.ImageIO.read(
                getClass().getClassLoader().getResourceAsStream("images/player/ranger_sheet.png"));
            
            if (rangerSheet != null) {
                // Extract ranger down1 sprite (column 0, row 0 = top-left)
                rangerPreview = rangerSheet.getSubimage(8, 2, 16, 29);
                System.out.println("Ranger preview sprite loaded successfully");
            } else {
                System.err.println("Ranger sprite sheet not found, using default");
                rangerPreview = defaultPreview;
            }
            
            // Future classes will use default preview until their sprite sheets are added  
            //magePreview = defaultPreview;      // TODO: Load from mage_sheet.png when available
            BufferedImage mageSheet = javax.imageio.ImageIO.read(
                getClass().getClassLoader().getResourceAsStream("images/player/mage_spritesheet.png"));
            
            if (rangerSheet != null) {
                // Extract mage down1 sprite (column 0, row 0 = top-left)
                magePreview = mageSheet.getSubimage(0, 0, 18, 32);
                System.out.println("Mage preview sprite loaded successfully");
            } else {
                System.err.println("Mage sprite sheet not found, using default");
                magePreview = defaultPreview;
            }

            System.out.println("Character preview sprites loaded successfully");
            
        } catch (Exception e) {
            System.err.println("Failed to load character preview sprites: " + e.getMessage());
            e.printStackTrace();
            
            // Create fallback colored rectangles if sprite loading fails
            warriorPreview = createFallbackPreview(Color.BLUE);
            magePreview = createFallbackPreview(Color.ORANGE);
            roguePreview = createFallbackPreview(new Color(128, 0, 128));
            rangerPreview = createFallbackPreview(Color.GREEN);
        }
    }
    
    /**
     * Create a fallback colored rectangle if sprite loading fails
     * @param color The color for the fallback preview
     * @return BufferedImage with colored rectangle
     */
    private BufferedImage createFallbackPreview(Color color) {
        BufferedImage fallback = new BufferedImage(15, 21, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = fallback.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, 15, 21);
        g.setColor(Color.WHITE);
        g.drawRect(0, 0, 14, 20);
        g.dispose();
        return fallback;
    }
    
    /**
     * Get character preview sprite for the given class index
     * @param classIndex Index of character class (0=Warrior, 1=Mage, 2=Rogue, 3=Ranger)
     * @return BufferedImage of the character preview sprite
     */
    private BufferedImage getCharacterPreview(int classIndex) {
        switch (classIndex) {
            case 0: return warriorPreview != null ? warriorPreview : defaultPreview;
            case 1: return magePreview != null ? magePreview : defaultPreview;
            case 2: return roguePreview != null ? roguePreview : defaultPreview;
            case 3: return rangerPreview != null ? rangerPreview : defaultPreview;
            default: return defaultPreview;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        Font pixelFont = parentView.getPixelFont();
        g2d.setColor(Color.WHITE);
        g2d.setFont(pixelFont.deriveFont(48f));
        String title = "Mini Rogue Demo";
        int titleWidth = g2d.getFontMetrics().stringWidth(title);
        g2d.drawString(title, (getWidth() - titleWidth) / 2, 150);

        g2d.setFont(pixelFont.deriveFont(32f));
        if (mode == Mode.MAIN_MENU) {
            for (int i = 0; i < MENU_OPTIONS.length; i++) {
                if (hoveredIndex != -1) {
                    // Only highlight hovered option
                    g2d.setColor(i == hoveredIndex ? Color.YELLOW : Color.LIGHT_GRAY);
                } else {
                    // Only highlight selected option
                    g2d.setColor(i == selectedIndex ? Color.YELLOW : Color.LIGHT_GRAY);
                }
                String option = MENU_OPTIONS[i];
                if (option.startsWith("Toggle Mouse Aiming")) {
                    option = "Mouse Aiming: " + (mouseAimingMenuState ? "ON" : "OFF");
                }
                int optionWidth = g2d.getFontMetrics().stringWidth(option);
                g2d.drawString(option, (getWidth() - optionWidth) / 2, 300 + i * 60);
            }
        } else if (mode == Mode.CLASS_SELECTION) {

            // g2d.setColor(Color.CYAN);
            // String selectClass = "Select Character Class";
            // int selectClassWidth = g2d.getFontMetrics().stringWidth(selectClass);
            // g2d.drawString(selectClass, (getWidth() - selectClassWidth) / 2, 250);
            
            // Render subtitle "Select Character"  
            g2d.setFont(pixelFont.deriveFont(20f));
            String subtitleText = "Select Character";
            FontMetrics subtitleFm = g2d.getFontMetrics();
            int subtitleWidth = subtitleFm.stringWidth(subtitleText);
            int subtitleX = (getWidth() - subtitleWidth) / 2;
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.drawString(subtitleText, subtitleX, 220);
            
            // Reset font for character options
            g2d.setFont(pixelFont.deriveFont(32f));
            for (int i = 0; i < CLASS_OPTIONS.length; i++) {
                if (hoveredIndex != -1) {
                    // Only highlight hovered option
                    g2d.setColor(i == hoveredIndex ? Color.YELLOW : Color.LIGHT_GRAY);
                } else {
                    // Only highlight selected option
                    g2d.setColor(i == classSelectedIndex ? Color.YELLOW : Color.LIGHT_GRAY);
                }
                String option = CLASS_OPTIONS[i];
                int optionWidth = g2d.getFontMetrics().stringWidth(option);
                int optionX = (getWidth() - optionWidth) / 2;
                int optionY = 350 + i * 60;
                
                // === NEW: CHARACTER SPRITE RENDERING ===
                BufferedImage preview = getCharacterPreview(i);  // Uses existing method
                if (preview != null) {
                    int spriteX = optionX + SPRITE_OFFSET_X;  // Position to left of text
                    int spriteY = optionY + SPRITE_VERTICAL_ADJUST;  // Align with text baseline
                    g2d.drawImage(preview, spriteX, spriteY, SPRITE_SIZE, SPRITE_SIZE, null);
                }
                
                // Existing text rendering (UNCHANGED)
                g2d.setColor(i == classSelectedIndex ? Color.YELLOW : Color.LIGHT_GRAY);
                g2d.drawString(option, optionX, optionY);
            }
            // Draw Back button
            int backY = 350 + CLASS_OPTIONS.length * 60 + 60;  // Extra 20px spacing
            String backText = "Back";
            g2d.setFont(pixelFont.deriveFont(28f));
            int backWidth = g2d.getFontMetrics().stringWidth(backText);
            int backX = (getWidth() - backWidth) / 2;
            if (hoveredIndex != -1) {
                g2d.setColor(hoveredIndex == BACK_OPTION_INDEX ? Color.YELLOW : Color.LIGHT_GRAY);
            } else {
                g2d.setColor(classSelectedIndex == BACK_OPTION_INDEX ? Color.YELLOW : Color.LIGHT_GRAY);
            }
            backButtonBounds.setBounds(backX - 20, backY - 32, backWidth + 40, 40);
            g2d.drawString(backText, backX, backY);
        }
    }

    public void update_display() {
        repaint();
    }

    public void show_class_selection() {
        mode = Mode.CLASS_SELECTION;
        classSelectedIndex = 0;
        repaint();
    }

    public void show_game_over() {
        mode = Mode.MAIN_MENU;
        repaint();
    }

    public void show_victory() {
        mode = Mode.MAIN_MENU;
        repaint();
    }

    public void show_main_menu() {
        mode = Mode.MAIN_MENU;
        selectedIndex = 0;
        // If a mouse is detected, enable mouse aiming by default
        if (parentView.isMouseDetected()) {
            parentView.setMouseAimingMode(true);
            mouseAimingMenuState = true;
        } else {
            mouseAimingMenuState = parentView.isMouseAimingMode();
        }
        repaint();
    }

    public void select_current_option() {
        if (mode == Mode.MAIN_MENU) {
            if (selectedIndex == 0) {
                parentView.get_controller().handle_input("START_NEW_GAME");
            } else if (selectedIndex == 1) {
                // Toggle mouse aiming
                mouseAimingMenuState = !mouseAimingMenuState;
                parentView.setMouseAimingMode(mouseAimingMenuState);
                repaint();
            } else if (selectedIndex == 2) {
                parentView.get_controller().handle_input("EXIT_APPLICATION");
            }
        } else if (mode == Mode.CLASS_SELECTION) {
            if (classSelectedIndex == 0) {
                parentView.get_controller().handle_input("SELECT_WARRIOR");
            } else if (classSelectedIndex == 1) {
                parentView.get_controller().handle_input("SELECT_MAGE");
            } else if (classSelectedIndex == 2) {
                parentView.get_controller().handle_input("SELECT_ROGUE");
            } else if (classSelectedIndex == 3) {
                parentView.get_controller().handle_input("SELECT_RANGER");
            } else if (classSelectedIndex == BACK_OPTION_INDEX) {
                parentView.get_controller().handle_input("BACK_TO_MENU");
            }
        }
    }

    public void move_selection_up() {
        if (mode == Mode.MAIN_MENU) {
            selectedIndex = (selectedIndex - 1 + MENU_OPTIONS.length) % MENU_OPTIONS.length;
        } else if (mode == Mode.CLASS_SELECTION) {
            classSelectedIndex = (classSelectedIndex - 1 + (CLASS_OPTIONS.length + 1)) % (CLASS_OPTIONS.length + 1);
        }
        repaint();
    }

    public void move_selection_down() {
        if (mode == Mode.MAIN_MENU) {
            selectedIndex = (selectedIndex + 1) % MENU_OPTIONS.length;
        } else if (mode == Mode.CLASS_SELECTION) {
            classSelectedIndex = (classSelectedIndex + 1) % (CLASS_OPTIONS.length + 1);
        }
        repaint();
    }

    // Mouse support for Back button
    @Override
    protected void processMouseEvent(java.awt.event.MouseEvent e) {
        // Remove mouse move and exit logic from here, handled by listeners now
        if (e.getID() == java.awt.event.MouseEvent.MOUSE_CLICKED) {
            if (mode == Mode.MAIN_MENU) {
                // Detect click on main menu options
                for (int i = 0; i < MENU_OPTIONS.length; i++) {
                    int optionY = 300 + i * 60;
                    int optionHeight = 40;
                    String option = MENU_OPTIONS[i];
                    if (option.startsWith("Toggle Mouse Aiming")) {
                        option = "Mouse Aiming: " + (mouseAimingMenuState ? "ON" : "OFF");
                    }
                    int optionWidth = getFontMetrics(new Font("Arial", Font.PLAIN, 32)).stringWidth(option);
                    int optionX = (getWidth() - optionWidth) / 2;
                    Rectangle optionRect = new Rectangle(optionX - 20, optionY - 32, optionWidth + 40, optionHeight);
                    if (optionRect.contains(e.getPoint())) {
                        selectedIndex = i;
                        repaint();
                        select_current_option();
                        return;
                    }
                }
            } else if (mode == Mode.CLASS_SELECTION) {
                // Detect click on class options
                for (int i = 0; i < CLASS_OPTIONS.length; i++) {
                    int optionY = 350 + i * 60;
                    int optionHeight = 40;
                    int optionWidth = getFontMetrics(new Font("Arial", Font.PLAIN, 32)).stringWidth(CLASS_OPTIONS[i]);
                    int optionX = (getWidth() - optionWidth) / 2;
                    Rectangle optionRect = new Rectangle(optionX - 20, optionY - 32, optionWidth + 40, optionHeight);
                    if (optionRect.contains(e.getPoint())) {
                        classSelectedIndex = i;
                        repaint();
                        select_current_option();
                        return;
                    }
                }
                // Detect click on Back button
                if (backButtonBounds.contains(e.getPoint())) {
                    classSelectedIndex = BACK_OPTION_INDEX;
                    repaint();
                    select_current_option();
                    return;
                }
            }
        }
        super.processMouseEvent(e);
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        enableEvents(java.awt.AWTEvent.MOUSE_EVENT_MASK);
    }


} 