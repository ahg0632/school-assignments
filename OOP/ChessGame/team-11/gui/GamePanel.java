package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

import pieces.Piece;

/**
 * The GamePanel class represents the panel in the GUI that displays the move history,
 * captured pieces, and provides an undo button.
 */
public class GamePanel extends JPanel {

    private JTextArea moveHistoryArea;
    private JPanel whiteCapturedPiecesPanel;
    private JPanel blackCapturedPiecesPanel;
    private JButton undoButton;
    private BoardGUI boardGUI;
    private List<String> moveHistoryList;

    /**
     * Constructs a GamePanel with the specified BoardGUI.
     *
     * @param boardGUI the BoardGUI associated with this GamePanel
     */
    public GamePanel(BoardGUI boardGUI) {
        this.boardGUI = boardGUI;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(300, 0));

        moveHistoryList = new ArrayList<>();
        moveHistoryArea = new JTextArea();
        moveHistoryArea.setEditable(false);
        JScrollPane moveHistoryScrollPane = new JScrollPane(moveHistoryArea);
        moveHistoryScrollPane.setBorder(BorderFactory.createTitledBorder("Move History"));
        moveHistoryScrollPane.setPreferredSize(new Dimension(150, 100));

        whiteCapturedPiecesPanel = new JPanel();
        whiteCapturedPiecesPanel.setBorder(BorderFactory.createTitledBorder("Captured White"));
        whiteCapturedPiecesPanel.setPreferredSize(new Dimension(150, 150));
        blackCapturedPiecesPanel = new JPanel();
        blackCapturedPiecesPanel.setBorder(BorderFactory.createTitledBorder("Captured Black"));
        blackCapturedPiecesPanel.setPreferredSize(new Dimension(150, 150));

        undoButton = new JButton("Undo");
        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardGUI.handleUndo();
            }
        });

        JPanel sidePanel = new JPanel(new BorderLayout());
        sidePanel.add(moveHistoryScrollPane, BorderLayout.CENTER);
        sidePanel.add(undoButton, BorderLayout.SOUTH);

        JPanel capturedPiecesPanel = new JPanel(new GridLayout(2, 1));
        capturedPiecesPanel.add(whiteCapturedPiecesPanel);
        capturedPiecesPanel.add(blackCapturedPiecesPanel);

        add(sidePanel, BorderLayout.WEST);
        add(capturedPiecesPanel, BorderLayout.EAST);
    }

    /**
     * Updates the move history with the specified move.
     *
     * @param move the move to add to the move history
     */
    public void updateMoveHistory(String move) {
        moveHistoryList.add(move);
        moveHistoryArea.append(move + "\n");
    }

    /**
     * Sets the move history to the specified history.
     *
     * @param history the move history to set
     */
    public void setMoveHistory(String history) {
        moveHistoryArea.setText(history);
    }

    /**
     * Returns the move history as a string.
     *
     * @return the move history
     */
    public String getMoveHistory() {
        return moveHistoryArea.getText();
    }

    /**
     * Clears the move history.
     */
    public void clearMoveHistory() {
        moveHistoryArea.setText("");
    }    

    /**
     * Updates the captured pieces panels with the specified lists of captured white and black pieces.
     *
     * @param capturedWhite the list of captured white pieces
     * @param capturedBlack the list of captured black pieces
     */
    public void updateCapturedPieces(List<Piece> capturedWhite, List<Piece> capturedBlack) {
        whiteCapturedPiecesPanel.removeAll();
        blackCapturedPiecesPanel.removeAll();

        for (Piece piece : capturedWhite) {
            JLabel label = new JLabel(piece.toString());
            whiteCapturedPiecesPanel.add(label);
        }

        for (Piece piece : capturedBlack) {
            JLabel label = new JLabel(piece.toString());
            blackCapturedPiecesPanel.add(label);
        }

        whiteCapturedPiecesPanel.revalidate();
        whiteCapturedPiecesPanel.repaint();
        blackCapturedPiecesPanel.revalidate();
        blackCapturedPiecesPanel.repaint();
    }

    /**
     * Clears the captured pieces panels.
     */
    public void clearCapturedPieces() {
        whiteCapturedPiecesPanel.removeAll();
        blackCapturedPiecesPanel.removeAll();
        whiteCapturedPiecesPanel.revalidate();
        whiteCapturedPiecesPanel.repaint();
        blackCapturedPiecesPanel.revalidate();
        blackCapturedPiecesPanel.repaint();
    }

    /**
     * Handles the undo action by delegating to the BoardGUI.
     */
    private void handleUndoAction() {
        boardGUI.handleUndo();
    }

    /**
     * Removes the last move from the move history.
     */
    public void removeLastMove() {
        if (!moveHistoryList.isEmpty()) {
            moveHistoryList.remove(moveHistoryList.size() - 1);
            refreshMoveHistoryArea();
        }
    }

    /**
     * Refreshes the move history area to reflect the current move history list.
     */
    private void refreshMoveHistoryArea() {
        moveHistoryArea.setText("");
        for (String move : moveHistoryList) {
            moveHistoryArea.append(move + "\n");
        }
    }
}
