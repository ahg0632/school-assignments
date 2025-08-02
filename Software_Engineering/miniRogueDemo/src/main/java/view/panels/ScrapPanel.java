package view.panels;

import model.characters.Player;
import javax.swing.*;
import java.awt.*;

/**
 * Panel to display scrap information between equipment and inventory panels
 */
public class ScrapPanel extends JPanel {
    private Player player;
    
    public ScrapPanel(Player player) {
        this.player = player;
        setBackground(Color.BLACK);
        setOpaque(true);
        setPreferredSize(new Dimension(220, 40));
    }
    
    public void setPlayer(Player player) {
        this.player = player;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (player == null) return;
        
        // Draw scrap bar with same proportions as HP/MP/XP bars, positioned closer to text
        int barX = 80; // Moved closer to text (reduced from 150)
        int barY = 3;   // Closer to text (reduced from 8)
        int barWidth = 80; // Same width as other bars
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
        if (player.get_total_scrap() > 0) {
            double percentage = (double) player.get_current_scrap() / player.get_total_scrap();
            int fillWidth = (int) (barWidth * percentage);
            g2d.setColor(new Color(148, 0, 211)); // VIOLET color
            g2d.fillRect(barX + 3, barY + 3, fillWidth - 3, barHeight - 3);
        }
        
        // Reset stroke to default
        g2d.setStroke(new java.awt.BasicStroke(1f));
    }
} 