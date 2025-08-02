package view;

import model.scoreEntry.ScoreEntry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Dialog for entering player initials and displaying high scores after game over or victory.
 */
public class ScoreEntryDialog extends JDialog {
    private JTextField initialsField;
    private JButton retryButton;
    private JButton quitButton;
    private boolean retrySelected = false;

    public interface ScoreEntryListener {
        void onRetry(String initials);
        void onQuit(String initials);
    }

    public ScoreEntryDialog(Frame owner, ScoreEntry currentScore, List<ScoreEntry> highScores, ScoreEntryListener listener) {
        super(owner, "Game Over - High Score", true);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        // setPreferredSize(new Dimension(400, 280)); // Remove fixed size

        // Score panel
        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
        scorePanel.setBorder(BorderFactory.createTitledBorder("Your Score"));
        scorePanel.add(new JLabel("Total Score: " + currentScore.getTotalScore()));
        scorePanel.add(new JLabel("EXP Level: " + currentScore.getFinalExpLevel()));
        scorePanel.add(new JLabel("Enemies Slain: " + currentScore.getEnemiesSlain()));
        int itemCount = currentScore.getItemsCollected() != null ? currentScore.getItemsCollected().size() : 0;
        java.util.List<String> items = currentScore.getItemsCollected();
        StringBuilder itemListBuilder = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            itemListBuilder.append(items.get(i));
            if (i < items.size() - 1) itemListBuilder.append(", ");
            if ((i + 1) % 4 == 0 && i < items.size() - 1) itemListBuilder.append("<br>");
        }
        String htmlItemList = "<html>Items Collected (" + itemCount + "):<br>" + itemListBuilder + "</html>";
        scorePanel.add(new JLabel(htmlItemList));
        if (currentScore.getKiller() != null) {
            scorePanel.add(new JLabel("Killed By: " + currentScore.getKiller()));
        }
        // Removed scroll area, add scorePanel directly

        // Initials entry
        JPanel initialsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        initialsPanel.add(new JLabel("Enter Initials: "));
        initialsField = new JTextField(3);
        initialsField.setDocument(new javax.swing.text.PlainDocument() {
            @Override
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                if (str == null) return;
                if ((getLength() + str.length()) <= 3 && str.matches("[A-Za-z]+")) {
                    super.insertString(offs, str.toUpperCase(), a);
                }
            }
        });
        initialsPanel.add(initialsField);

        // High score panel
        JPanel highScorePanel = new JPanel();
        highScorePanel.setLayout(new BoxLayout(highScorePanel, BoxLayout.Y_AXIS));
        highScorePanel.setBorder(BorderFactory.createTitledBorder("High Scores"));
        int rank = 1;
        for (ScoreEntry entry : highScores) {
            highScorePanel.add(new JLabel(rank + ". " + entry.getInitials() + " - Score: " + entry.getTotalScore()));
            rank++;
            if (rank > 3) break;
        }
        while (rank <= 3) {
            highScorePanel.add(new JLabel(rank + ". ---"));
            rank++;
        }

        // Buttons
        JPanel buttonPanel = new JPanel();
        retryButton = new JButton("Retry");
        quitButton = new JButton("Quit");
        buttonPanel.add(retryButton);
        buttonPanel.add(quitButton);

        // Layout
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(scorePanel, BorderLayout.NORTH);
        centerPanel.add(initialsPanel, BorderLayout.CENTER);
        centerPanel.add(highScorePanel, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        retryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (initialsField.getText().trim().length() == 3) {
                    retrySelected = true;
                    listener.onRetry(initialsField.getText().trim().toUpperCase());
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(ScoreEntryDialog.this, "Please enter 3 initials.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (initialsField.getText().trim().length() == 3) {
                    retrySelected = false;
                    listener.onQuit(initialsField.getText().trim().toUpperCase());
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(ScoreEntryDialog.this, "Please enter 3 initials.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        pack();
        setLocationRelativeTo(owner);
    }

    public boolean isRetrySelected() {
        return retrySelected;
    }
} 