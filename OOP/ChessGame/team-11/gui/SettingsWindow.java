package gui;

import javax.swing.*;

import theme.*;

import java.awt.*;
import java.awt.event.*;

/**
 * The SettingsWindow class provides a GUI for changing the settings of the chess game.
 * It allows the user to change the board background color/style, chess piece style, and screen size.
 */
public class SettingsWindow extends JFrame {
    private BoardGUI boardGUI; 

    /**
     * Constructs a SettingsWindow with the specified BoardGUI.
     *
     * @param boardGUI the BoardGUI to apply the settings to
     */
    public SettingsWindow(BoardGUI boardGUI) {
        this.boardGUI = boardGUI;
        setTitle("Settings");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        
        panel.add(new JLabel("Board Background Color/Style:"));
        String[] boardStyles = {"Light", "Dark"};
        JComboBox<String> boardStyleComboBox = new JComboBox<>(boardStyles);
        panel.add(boardStyleComboBox);

        
        panel.add(new JLabel("Chess Piece Style:"));
        String[] pieceStyles = {"Default", "Bold"};
        JComboBox<String> pieceStyleComboBox = new JComboBox<>(pieceStyles);
        panel.add(pieceStyleComboBox);

        panel.add(new JLabel("Screen Size:"));
        String[] screenSizes = {"Small", "Medium", "Large"};
        JComboBox<String> screenSizeComboBox = new JComboBox<>(screenSizes);
        panel.add(screenSizeComboBox);

        
        JPanel buttonPanel = new JPanel();
        JButton applyButton = new JButton("Apply");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(applyButton);
        buttonPanel.add(cancelButton);

        
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedBoardStyle = (String) boardStyleComboBox.getSelectedItem();
                String selectedPieceStyle = (String) pieceStyleComboBox.getSelectedItem();

                
                Theme newTheme;
                if (selectedBoardStyle.equals("Light")) {
                    newTheme = new LightTheme();
                } else {
                    newTheme = new DarkTheme();
                }

                boardGUI.setTheme(newTheme);
               

                switch (selectedPieceStyle) {              
                  case "Bold":
                    boardGUI.getTheme().setBoldFont();
                    boardGUI.setTheme(boardGUI.getTheme());;

                    break;
                  default:
                    boardGUI.getTheme().setDefaultFont();
                    boardGUI.setTheme(boardGUI.getTheme());;
                    break;
                }

                String selectedScreenSize = (String) screenSizeComboBox.getSelectedItem();
                switch (selectedScreenSize) {
                    case "Small":
                        boardGUI.setScreenSize(0.45);
                        break;
                    case "Medium":
                        boardGUI.setScreenSize(0.65);
                        break;
                    case "Large":
                        boardGUI.setScreenSize(0.85);
                        break;
                    default:
                        break;
                }

                dispose();
            }
        });

        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); 
            }
        });

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
