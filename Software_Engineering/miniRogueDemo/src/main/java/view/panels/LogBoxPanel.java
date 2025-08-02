package view.panels;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class LogBoxPanel extends JPanel {
    private final LinkedList<String> messages = new LinkedList<>();
    private final LinkedList<Long> messageTimestamps = new LinkedList<>();
    private final LinkedList<Color> messageColors = new LinkedList<>();
    private Font pixelFont;
    private static final int MAX_LINES = 5;
    private static final long MESSAGE_LIFETIME = 10000; // 10 seconds in milliseconds
    private Timer cleanupTimer;

    public LogBoxPanel() {
        setOpaque(false); // Make the log box transparent
        setFocusable(false);
        loadPixelFont();
        startCleanupTimer();
    }

    private void loadPixelFont() {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("fonts/PressStart2P-Regular.ttf");
            if (is != null) {
                pixelFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(16f);
            } else {
                pixelFont = new Font("Monospaced", Font.BOLD, 16);
            }
        } catch (Exception e) {
            pixelFont = new Font("Monospaced", Font.BOLD, 16);
        }
    }

    public void addMessage(String message) {
        addMessage(message, Color.GREEN); // Default to green for backward compatibility
    }
    
    public void addMessage(String message, Color color) {
        // Split message into wrapped lines
        if (message == null) return;
        java.util.List<String> wrappedLines = wrapText(message, getWidth() - 40); // 20px padding on each side
        long currentTime = System.currentTimeMillis();
        for (String line : wrappedLines) {
            messages.add(line);
            messageTimestamps.add(currentTime);
            messageColors.add(color);
        }
        while (messages.size() > MAX_LINES) {
            messages.remove(0);
            messageTimestamps.remove(0);
            messageColors.remove(0);
        }
        repaint();
    }

    // Helper to wrap text into lines that fit the given width
    private java.util.List<String> wrapText(String text, int maxWidth) {
        java.util.List<String> lines = new java.util.ArrayList<>();
        if (pixelFont == null) loadPixelFont();
        FontMetrics fm = getFontMetrics(pixelFont);
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        for (String word : words) {
            String testLine = line.length() == 0 ? word : line + " " + word;
            int width = fm.stringWidth(testLine);
            if (width > maxWidth && line.length() > 0) {
                lines.add(line.toString());
                line = new StringBuilder(word);
            } else {
                if (line.length() > 0) line.append(" ");
                line.append(word);
            }
        }
        if (line.length() > 0) lines.add(line.toString());
        return lines;
    }

    public void clearMessages() {
        messages.clear();
        messageTimestamps.clear();
        messageColors.clear();
        repaint();
    }
    
    private void startCleanupTimer() {
        cleanupTimer = new Timer();
        cleanupTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                cleanupExpiredMessages();
            }
        }, 1000, 1000); // Check every second
    }
    
    private void cleanupExpiredMessages() {
        long currentTime = System.currentTimeMillis();
        boolean needsRepaint = false;
        
        // Remove expired messages from the beginning of the list
        while (!messages.isEmpty() && !messageTimestamps.isEmpty() && !messageColors.isEmpty()) {
            if (currentTime - messageTimestamps.getFirst() > MESSAGE_LIFETIME) {
                messages.removeFirst();
                messageTimestamps.removeFirst();
                messageColors.removeFirst();
                needsRepaint = true;
            } else {
                break; // Messages are in chronological order, so we can stop here
            }
        }
        
        if (needsRepaint) {
            // Use SwingUtilities.invokeLater to ensure we're on the EDT
            SwingUtilities.invokeLater(() -> repaint());
        }
    }

    public void setPixelFont(Font font) {
        this.pixelFont = font;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        Font useFont = (pixelFont != null) ? pixelFont : new Font("Monospaced", Font.BOLD, 16);
        g2d.setFont(useFont);
        int lineHeight = g2d.getFontMetrics().getHeight();
        int controlsHelpGap = 30; // Increased gap for clear separation
        int y = getHeight() - (MAX_LINES * lineHeight) - controlsHelpGap - 5;
        int boxHeight = MAX_LINES * lineHeight + 16;
        // Draw border only
        g2d.setColor(Color.WHITE);
        g2d.drawRoundRect(5, y - 8, getWidth() - 10, boxHeight, 16, 16);
        // Draw messages
        int msgY = y + lineHeight;
        for (int i = 0; i < messages.size(); i++) {
            String msg = messages.get(i);
            Color msgColor = (i < messageColors.size()) ? messageColors.get(i) : Color.GREEN;
            g2d.setColor(msgColor);
            g2d.drawString(msg, 20, msgY);
            msgY += lineHeight;
        }
    }
} 