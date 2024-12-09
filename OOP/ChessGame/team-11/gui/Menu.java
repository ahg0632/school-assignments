package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import board.GameIO;
import theme.DarkTheme;
import theme.LightTheme;
import board.Board;
import java.io.File;

/**
 * The Menu class represents the menu bar in the chess game GUI.
 * It provides options for starting a new game, saving/loading a game,
 * changing themes, and adjusting the view size.
 */
public class Menu {
    private JMenuBar menuBar;
    private JMenu gameMenu;
    private JMenuItem newGameItem;
    private JMenuItem saveGameItem;
    private JMenuItem loadGameItem;

    private BoardGUI boardGUI;

    /**
     * Constructs a Menu object and initializes the menu bar with various options.
     *
     * @param boardGUI the BoardGUI object that this menu interacts with
     */
    public Menu(BoardGUI boardGUI) {
        this.boardGUI = boardGUI;
        menuBar = new JMenuBar();
        gameMenu = new JMenu("Game");

        newGameItem = new JMenuItem("New Game");
        saveGameItem = new JMenuItem("Save Game");
        loadGameItem = new JMenuItem("Load Game");

        newGameItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardGUI.resetBoard();
            }
        });

        GameIO gameIO = new GameIO();

        saveGameItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showSaveDialog(menuBar) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        gameIO.saveGame(boardGUI.getBoard(), file);
                        JOptionPane.showMessageDialog(null, "Game saved successfully.");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error saving game: " + ex.getMessage());
                    }
                }
            }
        });

        loadGameItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showOpenDialog(menuBar) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        Board loadedBoard = gameIO.loadGame(file);
                        boardGUI.setBoard(loadedBoard);
                        boardGUI.redrawBoard();
                        JOptionPane.showMessageDialog(null, "Game loaded successfully.");
                        boardGUI.checkWinner();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error loading game: " + ex.getMessage());
                    }
                }
            }
        });

        gameMenu.add(newGameItem);
        gameMenu.add(saveGameItem);
        gameMenu.add(loadGameItem);

        menuBar.add(gameMenu);

        JMenu themeMenu = new JMenu("Theme");
        JMenuItem lightTheme = new JMenuItem("Light");
        JMenuItem darkTheme = new JMenuItem("Dark");
        JMenuItem settingsItem = new JMenuItem("Settings");

        lightTheme.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardGUI.setTheme(new LightTheme());
            }
        });

        darkTheme.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardGUI.setTheme(new DarkTheme());
            }
        });

        settingsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SettingsWindow settingsWindow = new SettingsWindow(boardGUI);
                settingsWindow.setVisible(true);
            }
        });

        themeMenu.add(lightTheme);
        themeMenu.add(darkTheme);
        themeMenu.add(settingsItem);
        menuBar.add(themeMenu);

        JMenu viewMenu = new JMenu("View");
        JMenuItem small = new JMenuItem("Small");
        JMenuItem medium = new JMenuItem("Medium");
        JMenuItem large = new JMenuItem("Large");

        small.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardGUI.setScreenSize(0.45);
            }
        });

        medium.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardGUI.setScreenSize(0.65);
            }
        });

        large.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardGUI.setScreenSize(0.85);
            }
        });

        viewMenu.add(small);
        viewMenu.add(medium);
        viewMenu.add(large);
        menuBar.add(viewMenu);
    }

    /**
     * Returns the JMenuBar object for this menu.
     *
     * @return the JMenuBar object
     */
    public JMenuBar getMenuBar() {
        return menuBar;
    }
}
