package view.panels;

import model.scoreEntry.ScoreEntry;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ScoreboardPanel extends JPanel {
    public interface ScoreboardListener {
        void onPlayAgain(String initials);
        void onReturnToMenu(String initials);
    }

    private ScoreEntry currentScore;
    private List<ScoreEntry> highScores;
    private ScoreboardListener listener;
    private StringBuilder initials = new StringBuilder();
    private int selectedOption = 0; // 0 = Retry, 1 = Quit
    private boolean inputActive = true;
    private Font menuFont;
    private Font smallFont;
    private Color bgColor = new Color(48,48,48);
    private String className;

    public ScoreboardPanel(ScoreEntry currentScore, List<ScoreEntry> highScores, ScoreboardListener listener, Font pixelFont, String className) {
        this.currentScore = currentScore;
        this.highScores = highScores;
        this.listener = listener;
        this.className = className;
        setBackground(bgColor);
        setFocusable(true);
        this.menuFont = pixelFont != null ? pixelFont.deriveFont(32f) : new Font("Monospaced", Font.BOLD, 32);
        this.smallFont = pixelFont != null ? pixelFont.deriveFont(18f) : new Font("Monospaced", Font.PLAIN, 18);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (inputActive) {
                    if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && initials.length() > 0) {
                        initials.deleteCharAt(initials.length() - 1);
                        repaint();
                    } else if (initials.length() < 3 && e.getKeyChar() >= 'A' && e.getKeyChar() <= 'Z') {
                        initials.append(e.getKeyChar());
                        repaint();
                    } else if (initials.length() < 3 && e.getKeyChar() >= 'a' && e.getKeyChar() <= 'z') {
                        initials.append(Character.toUpperCase(e.getKeyChar()));
                        repaint();
                    } else if (e.getKeyCode() == KeyEvent.VK_ENTER && initials.length() == 3) {
                        inputActive = false;
                        repaint();
                    }
                } else {
                    if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
                        selectedOption = (selectedOption + 1) % 2;
                        repaint();
                    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
                        selectedOption = (selectedOption + 1) % 2;
                        repaint();
                    } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (selectedOption == 0) listener.onPlayAgain(initials.toString());
                        else listener.onReturnToMenu(initials.toString());
                    }
                }
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!inputActive) {
                    int w = getWidth();
                    int h = getHeight();
                    Rectangle playAgainRect = new Rectangle(w/2-160, h-120, 180, 50);
                    Rectangle returnRect = new Rectangle(w/2+60, h-120, 220, 50);
                    if (playAgainRect.contains(e.getPoint())) {
                        listener.onPlayAgain(initials.toString());
                    } else if (returnRect.contains(e.getPoint())) {
                        listener.onReturnToMenu(initials.toString());
                    }
                }
            }
        });
    }

    private static Color getClassColor(String className) {
        switch (className.toLowerCase()) {
            case "warrior": return Color.BLUE;
            case "mage": return Color.ORANGE;
            case "rogue": return new Color(128,0,128);
            case "ranger": return Color.GREEN;
            default: return Color.LIGHT_GRAY;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        // Title
        g2.setFont(menuFont);
        g2.setColor(Color.WHITE);
        String title = "GAME OVER";
        g2.drawString(title, w/2 - g2.getFontMetrics().stringWidth(title)/2, 80);
        // Score details
        g2.setFont(smallFont);
        int y = 140;
        g2.drawString("Total Score: " + currentScore.getTotalScore(), w/2 - 150, y);
        g2.drawString("EXP Level: " + currentScore.getFinalExpLevel(), w/2 - 150, y+30);
        g2.drawString("Enemies Slain: " + currentScore.getEnemiesSlain(), w/2 - 150, y+60);
        String items = "Items Collected: " + (currentScore.getItemsCollected() != null ? currentScore.getItemsCollected().size() : 0);
        g2.drawString(items, w/2 - 150, y+90);
        // Draw class label in white, class name in color
        String classLabel = "Class: ";
        int classLabelX = w/2 - 150;
        int classLabelY = y+150;
        g2.setColor(Color.WHITE);
        g2.drawString(classLabel, classLabelX, classLabelY);
        int classNameX = classLabelX + g2.getFontMetrics().stringWidth(classLabel) + 4;
        g2.setColor(getClassColor(className));
        g2.drawString(className, classNameX, classLabelY);
        g2.setColor(Color.WHITE);
        // For 'Killed By: Enemy', display enemy class name in color and all caps
        if (currentScore.getKiller() != null) {
            String killer = currentScore.getKiller();
            String[] parts = killer.split(":");
            String enemyLabel = "Killed By: Enemy ";
            String enemyClass = parts.length > 1 ? parts[1].trim().toUpperCase() : "";
            int enemyLabelX = w/2 - 150;
            int enemyLabelY = y+120;
            g2.setColor(Color.WHITE);
            g2.drawString(enemyLabel, enemyLabelX, enemyLabelY);
            if (!enemyClass.isEmpty()) {
                int enemyClassX = enemyLabelX + g2.getFontMetrics().stringWidth(enemyLabel);
                g2.setColor(getClassColor(enemyClass));
                g2.drawString(enemyClass, enemyClassX, enemyLabelY);
                g2.setColor(Color.WHITE);
            }
        }
        // Move high score title and list further down
        g2.setFont(menuFont.deriveFont(24f));
        g2.drawString("HIGH SCORES", w/2 - 100, y+220);
        g2.setFont(smallFont);
        int hsY = y+250;
        int rank = 1;
        for (ScoreEntry entry : highScores) {
            g2.drawString(rank + ". " + entry.getInitials() + " - Score: " + entry.getTotalScore(), w/2 - 100, hsY);
            hsY += 28;
            rank++;
            if (rank > 3) break;
        }
        while (rank <= 3) {
            g2.drawString(rank + ". ---", w/2 - 100, hsY);
            hsY += 28;
            rank++;
        }
        // Initials input
        g2.setFont(menuFont);
        String prompt = "Enter Initials: ";
        int promptX = w/2 - 200;
        int promptY = h-220;
        g2.drawString(prompt, promptX, promptY);
        String initialsStr = initials.toString();
        for (int i = initialsStr.length(); i < 3; i++) initialsStr += "_";
        int initialsX = promptX + g2.getFontMetrics().stringWidth(prompt) + 10;
        g2.setColor(Color.YELLOW);
        g2.drawString(initialsStr, initialsX, promptY);
        g2.setColor(Color.WHITE);
        // Menu options
        if (!inputActive) {
            g2.setFont(menuFont.deriveFont(28f));
            String playAgainText = "PLAY AGAIN";
            String returnText = "RETURN TO MENU";
            int playAgainWidth = g2.getFontMetrics().stringWidth(playAgainText);
            int returnWidth = g2.getFontMetrics().stringWidth(returnText);
            int buttonY = h-120+38;
            int spacing = 40;
            int totalWidth = playAgainWidth + returnWidth + spacing;
            int startX = w/2 - totalWidth/2;
            int playAgainX = startX;
            int returnX = startX + playAgainWidth + spacing;
            g2.setColor(selectedOption == 0 ? Color.YELLOW : Color.WHITE);
            g2.drawString(playAgainText, playAgainX, buttonY);
            g2.setColor(selectedOption == 1 ? Color.YELLOW : Color.WHITE);
            g2.drawString(returnText, returnX, buttonY);
            g2.setColor(Color.WHITE);
        }
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) requestFocusInWindow();
    }
} 