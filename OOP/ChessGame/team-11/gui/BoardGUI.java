package gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;
import board.Board;
import pieces.Piece;
import theme.*;

public class BoardGUI {
  private Board board;
  private JFrame frame;
  private JLayeredPane layeredPane;
  private JPanel boardPanel;
  private SquarePanel[][] squarePanels;
  private SquarePanel selectedSquare = null;
  private boolean isBoardFrozen = false;
  private Theme theme;
  private GamePanel gamePanel;
  private Stack<Board> boardHistory;
  private Stack<List<Piece>> capturedWhiteHistory;
  private Stack<List<Piece>> capturedBlackHistory;

  /**
   * Constructs a new BoardGUI with the given board.
   *
   * @param board the chess board
   */
  public BoardGUI(Board board) {
    this.board = board;
    this.theme = new LightTheme();
    this.frame = new JFrame("Chess");
    this.layeredPane = new JLayeredPane();
    this.boardPanel = new JPanel();
    this.squarePanels = new SquarePanel[8][8];

    gamePanel = new GamePanel(this);
    boardHistory = new Stack<>();
    capturedWhiteHistory = new Stack<>();
    capturedBlackHistory = new Stack<>();

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new BorderLayout());
    frame.setResizable(false);

    boardPanel.setLayout(new GridLayout(8, 8));
    boardPanel.setDoubleBuffered(true);
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int relativeSize = (int) (Math.min(screenSize.getWidth(), screenSize.getHeight()) * 0.65);
    boardPanel.setPreferredSize(new Dimension(relativeSize, relativeSize));

    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        String unicodeChar = board.getUnicode(row, col);
        Color backgroundColor = (row + col) % 2 == 0 ? theme.getLightSquareColor() : theme.getDarkSquareColor();
        SquarePanel squarePanel = new SquarePanel(row, col, unicodeChar, backgroundColor, this);
        boardPanel.add(squarePanel);
        this.squarePanels[row][col] = squarePanel; // store reference if needed for further use
      }
    }

    boardPanel.setBounds(0, 0, relativeSize, relativeSize);
    layeredPane.setPreferredSize(new Dimension(relativeSize, relativeSize));
    layeredPane.add(boardPanel, JLayeredPane.DEFAULT_LAYER);

    Menu menu = new Menu(this);
    frame.setJMenuBar(menu.getMenuBar());

    frame.add(layeredPane, BorderLayout.CENTER);

    frame.add(gamePanel, BorderLayout.EAST);

    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

  /**
   * Sets the theme for the board.
   *
   * @param newTheme the new theme to be set
   */
  public void setTheme(Theme newTheme) {
    this.theme = newTheme;
    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        Color backgroundColor = (row + col) % 2 == 0 ? theme.getLightSquareColor() : theme.getDarkSquareColor();
        squarePanels[row][col].setBackground(backgroundColor);
        squarePanels[row][col].setBorder(theme.getOriginalBorder());
        squarePanels[row][col].updateTheme();
      }
    }
    frame.repaint();
  }

  /**
   * Redraws the board with the current state.
   */
  public void redrawBoard() {
    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        String currentUnicode = board.getUnicode(row, col);
        squarePanels[row][col].updateUnicode(currentUnicode);
      }
    }
  }

  /**
   * Handles the click event on a square.
   *
   * @param row the row of the clicked square
   * @param col the column of the clicked square
   */
  public void handleSquareClick(int row, int col) {
    if (isBoardFrozen)
      return;

    SquarePanel clickedSquare = squarePanels[row][col];
    Piece clickedPiece = board.getPieceAt(row, col);

    if (selectedSquare == null) {
      if (clickedPiece != null && clickedPiece.getOwner() == board.getCurrentPlayer()) {
        selectedSquare = clickedSquare;
        selectedSquare.setSelected(true);
      }
    } else {
      if (clickedPiece != null && clickedPiece.getOwner() == board.getCurrentPlayer()) {
        // select a different piece
        selectedSquare.setSelected(false);
        selectedSquare = clickedSquare;
        selectedSquare.setSelected(true);
      } else {
        // attempt to move to the clicked square
        selectedSquare.setSelected(false);
        handleMove(selectedSquare.getRow(), selectedSquare.getCol(), row, col);
        selectedSquare = null;
      }
    }
  }

  /**
   * Handles the move of a piece from one square to another.
   *
   * @param fromRow the starting row of the piece
   * @param fromCol the starting column of the piece
   * @param toRow   the destination row of the piece
   * @param toCol   the destination column of the piece
   */
  public void handleMove(int fromRow, int fromCol, int toRow, int toCol) {
    if (isBoardFrozen)
      return;

    boardHistory.push(board.clone());
    capturedWhiteHistory.push(new ArrayList<>(board.getCapturedWhite()));
    capturedBlackHistory.push(new ArrayList<>(board.getCapturedBlack()));

    Piece movedPiece = board.getPieceAt(fromRow, fromCol);
    if (movedPiece == null) {
      return; // there is no piece to move
    }

    // check if the piece belongs to the current player
    if (movedPiece.getOwner() != board.getCurrentPlayer()) {
      JOptionPane.showMessageDialog(frame, "It's not your turn!");
      return;
    }

    boolean wasMoved = board.movePiecePosition(fromRow, fromCol, toRow, toCol);
    if (wasMoved) {
      redrawBoard();
      checkWinner();
      String move = movedPiece.getName() + ": " + fromRow + "," + fromCol + " to " + toRow + "," + toCol;
      gamePanel.updateMoveHistory(move);
      gamePanel.updateCapturedPieces(board.getCapturedWhite(), board.getCapturedBlack());

      if (board.isCheckmate()) {
        JOptionPane.showMessageDialog(frame, "Checkmate! " + board.getCurrentPlayer().name() + " has won the game!");
        freezeBoard();
      } else {
        if (board.isWhiteInCheck()) {
          JOptionPane.showMessageDialog(frame, "White is in check!");
        } else if (board.isBlackInCheck()) {
          JOptionPane.showMessageDialog(frame, "Black is in check!");
        }
      }

      switchTurn();

    } else {
      JOptionPane.showMessageDialog(frame, "Invalid move!");
    }

    
  }

  /**
   * Undoes the last move.
   */
  public void handleUndo() {
    if (!boardHistory.isEmpty()) {
      board = boardHistory.pop();
      board.setCapturedWhite(capturedWhiteHistory.pop());
      board.setCapturedBlack(capturedBlackHistory.pop());
      redrawBoard();

      gamePanel.updateCapturedPieces(board.getCapturedWhite(), board.getCapturedBlack());

      gamePanel.removeLastMove();
      unfreezeBoard();
    }
  }

  /**
   * Switches the turn to the next player.
   */
  private void switchTurn() {
    board.switchPlayer();
    updateFrameTitle();
  }

  /**
   * Updates the frame title with the current player's turn.
   */
  private void updateFrameTitle() {
    frame.setTitle("Chess - " + board.getCurrentPlayer().name() + "'s Turn");
  }

  /**
   * Gets the layered pane.
   *
   * @return the layered pane
   */
  public JLayeredPane getLayeredPane() {
    return layeredPane;
  }

  /**
   * Gets the board panel.
   *
   * @return the board panel
   */
  public JPanel getBoardPanel() {
    return boardPanel;
  }

  /**
   * Gets the selected square.
   *
   * @return the selected square
   */
  public SquarePanel getSelectedSquare() {
    return selectedSquare;
  }

  /**
   * Sets the selected square.
   *
   * @param square the square to be selected
   */
  public void setSelectedSquare(SquarePanel square) {
    selectedSquare = square;
  }

  /**
   * Gets the board.
   *
   * @return the board
   */
  public Board getBoard() {
    return board;
  }

  /**
   * Sets the board and updates the GUI.
   *
   * @param board the new board
   */
  public void setBoard(Board board) {
    this.board = board;

    boardPanel.removeAll();

    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        String unicodeChar = board.getUnicode(row, col);
        Color backgroundColor = (row + col) % 2 == 0 ? theme.getLightSquareColor() : theme.getDarkSquareColor();
        SquarePanel squarePanel = new SquarePanel(row, col, unicodeChar, backgroundColor, this);
        boardPanel.add(squarePanel);
        this.squarePanels[row][col] = squarePanel;
      }
    }
    boardPanel.revalidate();
    boardPanel.repaint();

  }

  /**
   * Checks if there is a winner and displays a message if there is.
   */
  public void checkWinner() {
    if (board.isKingCaptured()) {
      String winner = board.getWinner();
      if (winner != null) {
        freezeBoard(); // freeze the board after a win
        JOptionPane.showMessageDialog(frame, winner + " has won the game!");
      }
      return;
    }
  }

  /**
   * Freezes the board by disabling all SquarePanel interactions.
   */
  public void freezeBoard() {
    isBoardFrozen = true;
    setSquarePanelsEnabled(false);
  }

  /**
   * Unfreezes the board by enabling all SquarePanel interactions.
   */
  public void unfreezeBoard() {
    isBoardFrozen = false;
    setSquarePanelsEnabled(true);
  }

  /**
   * Resets the board to start a new game.
   */
  public void resetBoard() {
    board = new Board();
    redrawBoard();
    setSquarePanelsEnabled(true);
    isBoardFrozen = false;
    updateFrameTitle();
    gamePanel.clearMoveHistory();
    gamePanel.clearCapturedPieces();
  }

  /**
   * Enables or disables all SquarePanel components.
   *
   * @param enabled true to enable, false to disable
   */
  private void setSquarePanelsEnabled(boolean enabled) {
    for (SquarePanel[] row : squarePanels) {
      for (SquarePanel square : row) {
        square.setEnabled(enabled);
      }
    }
  }

  /**
   * Gets the current theme.
   *
   * @return the current theme
   */
  public Theme getTheme() {
    return theme;
  }

  /**
   * Sets the screen size of the board.
   *
   * @param size the new size as a fraction of the screen size
   */
  public void setScreenSize(double size) {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int newSize = (int) (Math.min(screenSize.getWidth(), screenSize.getHeight()) * size);
    boardPanel.setPreferredSize(new Dimension(newSize, newSize));
    boardPanel.setBounds(0, 0, newSize, newSize);
    layeredPane.setPreferredSize(new Dimension(newSize, newSize));
    frame.pack();
  }

  /**
   * Gets the game panel.
   *
   * @return the game panel
   */
  public GamePanel getGamePanel() {
    return gamePanel;
  }

}
