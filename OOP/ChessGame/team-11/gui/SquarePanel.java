package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import pieces.Piece;
import theme.*;

/**
 * Represents a square on the chessboard GUI.
 */
public class SquarePanel extends JPanel {
    private String unicode;
    private JLabel squareLabel;
    private BoardGUI boardGUI;

    private boolean isDragging = false;
    private JLabel draggedPiece = null;
    private int offsetX, offsetY;
    private Point initialClickPoint;
    private static final int DRAG_THRESHOLD = 5; // pixels
    private SquarePanel lastHighlightedSquare = null;

    private int row, col;

    /**
     * Constructs a SquarePanel with the specified parameters.
     *
     * @param row        the row of the square
     * @param col        the column of the square
     * @param unicode    the Unicode character representing the piece on the square
     * @param background the background color of the square
     * @param boardGUI   the BoardGUI instance
     */
    public SquarePanel(int row, int col, String unicode, Color background, BoardGUI boardGUI) {
        this.row = row;
        this.col = col;
        this.unicode = unicode;
        this.boardGUI = boardGUI;
        this.squareLabel = new JLabel(unicode, SwingConstants.CENTER);
        setBackground(background);
        
        squareLabel.setFont(getPieceFont());
        squareLabel.setForeground(getPieceFontColor());
        setLayout(new BorderLayout());
        add(squareLabel, BorderLayout.CENTER);

        setBorder(boardGUI.getTheme().getOriginalBorder());

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!isEnabled()) return; 
                handleMousePress(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!isEnabled()) return; 
                handleMouseRelease(e);
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!isEnabled()) return; 
                handleMouseDrag(e);
            }
        });
    }

    /**
     * Checks if the piece on the square is a white piece.
     *
     * @return true if the piece is white, false otherwise
     */
    private boolean isWhitePiece() {
        return unicode != null && unicode.matches("[\\u2654-\\u2659]");
    }

    /**
     * Gets the font for the piece on the square based on the theme.
     *
     * @return the font for the piece
     */
    private Font getPieceFont() {
        Theme theme = boardGUI.getTheme();
        return isWhitePiece() ? theme.getWhitePieceFont() : theme.getBlackPieceFont();
    }

    /**
     * Gets the font color for the piece on the square based on the theme.
     *
     * @return the font color for the piece
     */
    private Color getPieceFontColor() {
        Theme theme = boardGUI.getTheme();
        return isWhitePiece() ? theme.getWhitePieceFontColor() : theme.getBlackPieceFontColor();
    }

    /**
     * Handles the mouse press event.
     *
     * @param e the MouseEvent
     */
    private void handleMousePress(MouseEvent e) {
        Piece currentPiece = boardGUI.getBoard().getPieceAt(row, col);
        if (currentPiece != null && currentPiece.getOwner() == boardGUI.getBoard().getCurrentPlayer()) {
            initialClickPoint = e.getPoint();
        } else {
            initialClickPoint = null; // disallow dragging
        }
    }

    /**
     * Handles the mouse drag event.
     *
     * @param e the MouseEvent
     */
    private void handleMouseDrag(MouseEvent e) {
        if (initialClickPoint != null) {
            int deltaX = Math.abs(e.getX() - initialClickPoint.x);
            int deltaY = Math.abs(e.getY() - initialClickPoint.y);
            if (!isDragging && (deltaX > DRAG_THRESHOLD || deltaY > DRAG_THRESHOLD)) {
                // if any square already selected, deselect it
                if (boardGUI.getSelectedSquare() != null) {
                    boardGUI.getSelectedSquare().setSelected(false);
                    boardGUI.setSelectedSquare(null);
                }

                // start dragging
                isDragging = true;
                draggedPiece = new JLabel(squareLabel.getText());
                draggedPiece.setFont(squareLabel.getFont());
                draggedPiece.setForeground(squareLabel.getForeground().darker());
                draggedPiece.setSize(getSize());

                Point point = SwingUtilities.convertPoint(this, e.getPoint(), boardGUI.getLayeredPane());
                offsetX = point.x - getX();
                offsetY = point.y - getY();
                draggedPiece.setLocation(point.x - offsetX, point.y - offsetY);

                boardGUI.getLayeredPane().add(draggedPiece, JLayeredPane.DRAG_LAYER);
                boardGUI.getLayeredPane().repaint();

                // hide the piece on the original square
                squareLabel.setVisible(false);
            }

            if (isDragging && draggedPiece != null) {
                Point point = SwingUtilities.convertPoint(this, e.getPoint(), boardGUI.getLayeredPane());
                draggedPiece.setLocation(point.x - offsetX, point.y - offsetY);
                boardGUI.getLayeredPane().repaint();

                // get the component under the mouse cursor
                Point boardPoint = SwingUtilities.convertPoint(this, e.getPoint(), boardGUI.getBoardPanel());
                Component component = boardGUI.getBoardPanel().getComponentAt(boardPoint);
                if (component instanceof SquarePanel) {
                    SquarePanel targetSquare = (SquarePanel) component;

                    // if we're over a new square, update the highlight
                    if (lastHighlightedSquare != targetSquare) {
                        // remove highlight from the last square
                        if (lastHighlightedSquare != null) {
                            lastHighlightedSquare.setHighlight(false);
                        }
                        // then highlight the new square
                        targetSquare.setHighlight(true);
                        lastHighlightedSquare = targetSquare;
                    }
                } else {
                    // mouse is not over any square
                    if (lastHighlightedSquare != null) {
                        lastHighlightedSquare.setHighlight(false);
                        lastHighlightedSquare = null;
                    }
                }
            }
        }
    }

    /**
     * Handles the mouse release event.
     *
     * @param e the MouseEvent
     */
    private void handleMouseRelease(MouseEvent e) {
        if (isDragging && draggedPiece != null) {
            boardGUI.getLayeredPane().remove(draggedPiece);
            boardGUI.getLayeredPane().repaint();

            Point point = SwingUtilities.convertPoint(this, e.getPoint(), boardGUI.getBoardPanel());
            Component component = boardGUI.getBoardPanel().getComponentAt(point);
            if (component instanceof SquarePanel) {
                SquarePanel targetSquare = (SquarePanel) component;
                boardGUI.handleMove(this.row, this.col, targetSquare.getRow(), targetSquare.getCol());
            }

            // remove highlight from the last square
            if (lastHighlightedSquare != null) {
                lastHighlightedSquare.setHighlight(false);
                lastHighlightedSquare = null;
            }

            // restore the piece on the original square
            squareLabel.setVisible(true);

            isDragging = false;
            draggedPiece = null;
            initialClickPoint = null;
        } else {
            boardGUI.handleSquareClick(row, col);
            initialClickPoint = null;
        }
    }

    /**
     * Updates the Unicode character representing the piece on the square.
     *
     * @param unicode the new Unicode character
     */
    public void updateUnicode(String unicode) {
        this.unicode = unicode;
        squareLabel.setText(unicode);
        squareLabel.setForeground(getPieceFontColor()); // reapply the font color
    }

    /**
     * Sets the selection state of the square.
     *
     * @param isSelected true if the square is selected, false otherwise
     */
    public void setSelected(boolean isSelected) {
        if (isSelected) {
            setBorder(BorderFactory.createLineBorder(
                boardGUI.getTheme().getSelectedSquareBorderColor(),
                boardGUI.getTheme().getSelectedSquareBorderThickness()
            ));
        } else {
            setBorder(boardGUI.getTheme().getOriginalBorder());
        }
    }

    /**
     * Sets the highlight state of the square.
     *
     * @param highlight true if the square is highlighted, false otherwise
     */
    public void setHighlight(boolean highlight) {
        if (highlight) {
            setBorder(BorderFactory.createLineBorder(
                boardGUI.getTheme().getHighlightSquareBorderColor(),
                boardGUI.getTheme().getHighlightSquareBorderThickness()
            ));
        } else {
            setBorder(boardGUI.getTheme().getOriginalBorder());
        }
        repaint();
    }

    /**
     * Gets the row of the square.
     *
     * @return the row of the square
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the column of the square.
     *
     * @return the column of the square
     */
    public int getCol() {
        return col;
    }

    /**
     * Updates the theme of the square.
     */
    public void updateTheme() {
      Color backgroundColor = (row + col) % 2 == 0
          ? boardGUI.getTheme().getLightSquareColor()
          : boardGUI.getTheme().getDarkSquareColor();
      setBackground(backgroundColor);
      setBorder(boardGUI.getTheme().getOriginalBorder());
      squareLabel.setFont(getPieceFont());
      squareLabel.setForeground(getPieceFontColor());
      
      repaint();
  }

}