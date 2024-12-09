package board;

import pieces.Pawn;
import pieces.Rook;
import pieces.Knight;
import pieces.Bishop;
import pieces.Queen;
import pieces.King;
import pieces.Piece;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Board} class represents a chessboard for a standard game of chess.
 * It initializes the board with pieces in their starting positions and provides
 * methods to display the board, move pieces, and get pieces from specific
 * positions.
 * 
 * <p>
 * Attributes:
 * </p>
 * <ul>
 * <li>{@code board} - A 2D array representing the 8x8 chessboard, where each
 * element is a {@code Piece}.</li>
 * <li>{@code currentPlayer} - The player whose turn it is to move.</li>
 * <li>{@code whiteInCheck} - A boolean indicating if the white player is in
 * check.</li>
 * <li>{@code blackInCheck} - A boolean indicating if the black player is in
 * check.</li>
 * <li>{@code checkmate} - A boolean indicating if the game is in a checkmate
 * state.</li>
 * <li>{@code capturedWhite} - A list of white pieces that have been
 * captured.</li>
 * <li>{@code capturedBlack} - A list of black pieces that have been
 * captured.</li>
 * <li>{@code whitePieces} - A list of white pieces currently on the board.</li>
 * <li>{@code blackPieces} - A list of black pieces currently on the board.</li>
 * <li>{@code checkForCheckmate} - A boolean indicating if checkmate should be
 * checked.</li>
 * </ul>
 * 
 * <p>
 * Methods:
 * </p>
 * <ul>
 * <li>{@code Board()} - Constructor to initialize the board with pieces in
 * their starting positions.</li>
 * <li>{@code getCurrentPlayer()} - Retrieves the current player.</li>
 * <li>{@code isWhiteInCheck()} - Checks if the white player is currently in
 * check.</li>
 * <li>{@code isBlackInCheck()} - Checks if the black player is currently in
 * check.</li>
 * <li>{@code isCheckmate()} - Checks if the current game state is a
 * checkmate.</li>
 * <li>{@code switchPlayer()} - Switches the current player to the other
 * player.</li>
 * <li>{@code initialize()} - Private method to place all pieces in their
 * starting positions on the board.</li>
 * <li>{@code display()} - Displays the current state of the chess board.</li>
 * <li>{@code getPieceSymbol(Piece piece)} - Returns the symbol representation
 * of a given chess piece.</li>
 * <li>{@code getPieceAt(int row, int col)} - Retrieves the piece located at the
 * specified position on the chess board.</li>
 * <li>{@code hasPieceAt(int row, int col)} - Checks if a piece is present at
 * the specified position on the board.</li>
 * <li>{@code movePiecePosition(int fromRow, int fromCol, int toRow, int toCol)}
 * - Moves a piece from one position to another on the board.</li>
 * <li>{@code checkForCheck()} - Checks if either player's king is in
 * check.</li>
 * <li>{@code checkForCheckmate()} - Checks if the current player is in
 * checkmate.</li>
 * <li>{@code positionToArray(String position)} - Converts a chess position in
 * algebraic notation to a 2D array index.</li>
 * <li>{@code arrayToPosition(int[] array)} - Converts a 2D array index to a
 * chess position in algebraic notation.</li>
 * <li>{@code getUnicode(int row, int col)} - Retrieves the Unicode
 * representation of the piece at the specified position.</li>
 * <li>{@code isKingCaptured()} - Checks if either king has been captured.</li>
 * <li>{@code getWinner()} - Determines the winner of the game.</li>
 * <li>{@code getCapturedWhite()} - Retrieves the list of captured white
 * pieces.</li>
 * <li>{@code setCapturedWhite(List<Piece> capturedWhite)} - Sets the list of
 * captured white pieces.</li>
 * <li>{@code getCapturedBlack()} - Retrieves the list of captured black
 * pieces.</li>
 * <li>{@code setCapturedBlack(List<Piece> capturedBlack)} - Sets the list of
 * captured black pieces.</li>
 * <li>{@code clone()} - Creates a deep copy of the current Board instance.</li>
 * <li>{@code getPieceList(String player)} - Retrieves the list of pieces for
 * the specified player.</li>
 * <li>{@code updateValidMoves()} - Updates the valid moves for all pieces on
 * the board.</li>
 * </ul>
 */
public class Board implements Serializable, Cloneable {
  private static final long serialVersionUID = 1L;
  /**
   * Represents the chessboard, which is an 8x8 grid.
   * Each cell in the grid can hold a Piece object.
   */
  private Piece[][] board;
  private Player currentPlayer;
  private boolean whiteInCheck;
  private boolean blackInCheck;
  private boolean checkmate;

  private List<Piece> capturedWhite;
  private List<Piece> capturedBlack;

  private List<Piece> whitePieces;
  private List<Piece> blackPieces;

  private boolean checkForCheckmate; // checkmate checks clone the board but we don't want to check for checkmate on
                                     // the clone

  /**
   * Constructs a new Board object and initializes the board with an 8x8 grid of
   * pieces.
   * The board is set up by calling the initialize method.
   */
  public Board() {
    board = new Piece[8][8];

    capturedWhite = new ArrayList<>();
    capturedBlack = new ArrayList<>();
    whitePieces = new ArrayList<>();
    blackPieces = new ArrayList<>();

    currentPlayer = Player.WHITE;
    checkForCheckmate = true;

    initialize();
  }

  /**
   * Retrieves the current player.
   *
   * @return The current player.
   */
  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  /**
   * Checks if the white player is currently in check.
   *
   * @return true if the white player is in check, false otherwise.
   */
  public boolean isWhiteInCheck() {
    return whiteInCheck;
  }

  /**
   * Checks if the black player is currently in check.
   *
   * @return true if the black player is in check, false otherwise.
   */
  public boolean isBlackInCheck() {
    return blackInCheck;
  }

  /**
   * Checks if the current game state is a checkmate.
   *
   * @return true if the game is in a checkmate state, false otherwise.
   */
  public boolean isCheckmate() {
    return checkmate;
  }

  /**
   * Switches the current player to the other player.
   */
  public void switchPlayer() {
    currentPlayer = (currentPlayer == Player.WHITE) ? Player.BLACK : Player.WHITE;
  }

  /**
   * Initializes the chess board by placing all the pieces in their starting
   * positions.
   * 
   * The white pieces are placed on the 1st and 2nd ranks:
   * - Rooks at A1 and H1
   * - Knights at B1 and G1
   * - Bishops at C1 and F1
   * - Queen at D1
   * - King at E1
   * - Pawns at A2, B2, C2, D2, E2, F2, G2, and H2
   * 
   * The black pieces are placed on the 8th and 7th ranks:
   * - Rooks at A8 and H8
   * - Knights at B8 and G8
   * - Bishops at C8 and F8
   * - Queen at D8
   * - King at E8
   * - Pawns at A7, B7, C7, D7, E7, F7, G7, and H7
   */
  private void initialize() {
    // FEN String is not proper due to board orientation
    // TOFIX:: Flip board and return this logic to index in order and not in reverse
    String initFEN = "RNBKQBNR/PPPPPPPP/8/8/8/8/pppppppp/rnbkqbnr";
    int rank = 7;
    int file = 7;
    for (char piece : initFEN.toCharArray()) {
      if (file < 0) {
        file = 8;
      }
      String color = (Character.isUpperCase(piece) ? "white" : "black");
      Player owner = (Character.isUpperCase(piece) ? Player.WHITE : Player.BLACK);
      switch (piece) {
        case 'R':
        case 'r':
          board[rank][file] = new Rook(color, rank, file, owner, (Character.isUpperCase(piece) ? "\u2656" : "\u265c"));
          if (board[rank][file].getOwner() == Player.WHITE) {
            whitePieces.add(board[rank][file]);
          }
          if (board[rank][file].getOwner() == Player.BLACK) {
            blackPieces.add(board[rank][file]);
          }
          break;
        case 'N':
        case 'n':
          board[rank][file] = new Knight(color, rank, file, owner,
              (Character.isUpperCase(piece) ? "\u2658" : "\u265e"));
          if (board[rank][file].getOwner() == Player.WHITE) {
            whitePieces.add(board[rank][file]);
          }
          if (board[rank][file].getOwner() == Player.BLACK) {
            blackPieces.add(board[rank][file]);
          }
          break;
        case 'B':
        case 'b':
          board[rank][file] = new Bishop(color, rank, file, owner,
              (Character.isUpperCase(piece) ? "\u2657" : "\u265d"));
          if (board[rank][file].getOwner() == Player.WHITE) {
            whitePieces.add(board[rank][file]);
          }
          if (board[rank][file].getOwner() == Player.BLACK) {
            blackPieces.add(board[rank][file]);
          }
          break;
        case 'Q':
        case 'q':
          board[rank][file] = new Queen(color, rank, file, owner, (Character.isUpperCase(piece) ? "\u2655" : "\u265b"));
          if (board[rank][file].getOwner() == Player.WHITE) {
            whitePieces.add(board[rank][file]);
          }
          if (board[rank][file].getOwner() == Player.BLACK) {
            blackPieces.add(board[rank][file]);
          }
          break;
        case 'K':
        case 'k':
          board[rank][file] = new King(color, rank, file, owner, (Character.isUpperCase(piece) ? "\u2654" : "\u265a"));
          if (board[rank][file].getOwner() == Player.WHITE) {
            whitePieces.add(board[rank][file]);
          }
          if (board[rank][file].getOwner() == Player.BLACK) {
            blackPieces.add(board[rank][file]);
          }
          break;
        case 'P':
        case 'p':
          board[rank][file] = new Pawn(color, rank, file, owner, (Character.isUpperCase(piece) ? "\u2659" : "\u265f"));
          if (board[rank][file].getOwner() == Player.WHITE) {
            whitePieces.add(board[rank][file]);
          }
          if (board[rank][file].getOwner() == Player.BLACK) {
            blackPieces.add(board[rank][file]);
          }
          break;
        case '/':
          rank--;
          break;
        default:
          file -= Character.valueOf(piece);
      }
      file--;
    }

    this.updateValidMoves();
  }

  /**
   * Displays the current state of the chess board.
   * The board is displayed with column labels (A to H) at the top and bottom,
   * and row labels (8 to 1) on the left and right sides.
   * Each piece on the board is represented by its first letter (e.g., P for Pawn,
   * K for King).
   * Empty squares are represented by "##".
   */
  public void display() {
    System.out.println("  A  B  C  D  E  F  G  H"); // Column labels
    for (int row = 0; row < 8; row++) {
      System.out.print((8 - row) + " "); // Row labels (8 to 1)
      for (int col = 0; col < 8; col++) {
        if (board[row][col] != null) {
          // Display the piece using its first letter (e.g., P for Pawn, K for King)
          String pieceSymbol = getPieceSymbol(board[row][col]);
          System.out.print(pieceSymbol + " ");
        } else {
          System.out.print("## "); // Empty square
        }
      }
      System.out.println(" " + (8 - row)); // Row labels (8 to 1)
    }
    System.out.println("  A  B  C  D  E  F  G  H"); // Column labels
  }

  /**
   * Returns the symbol representation of a given chess piece.
   * The symbol consists of the piece's color (w for white, b for black)
   * followed by a single character representing the type of the piece.
   * 
   * @param piece the chess piece for which the symbol is to be generated
   * @return a string representing the piece's symbol, e.g., "wP" for a white
   *         pawn,
   *         "bK" for a black king, etc. If the piece type is unrecognized,
   *         returns "##".
   */
  private String getPieceSymbol(Piece piece) {
    String color = piece.getColor().equals("white") ? "w" : "b";
    String className = piece.getClass().getSimpleName();

    switch (className) {
      case "Pawn":
        return color + "P";
      case "Rook":
        return color + "R";
      case "Knight":
        return color + "N";
      case "Bishop":
        return color + "B";
      case "Queen":
        return color + "Q";
      case "King":
        return color + "K";
      default:
        return "##"; // Default case (shouldn't happen)
    }
  }

  /**
   * Retrieves the piece located at the specified position on the chess board.
   *
   * @param row The row index of the piece.
   * @param col The column index of the piece.
   * @return The piece located at the specified position, or null if the position
   *         is empty.
   * @throws IndexOutOfBoundsException if the position is outside the bounds of
   *                                   the board.
   */
  public Piece getPieceAt(int row, int col) {
    return board[row][col];
  }

  /**
   * Checks if a piece is present at the specified position on the board.
   *
   * @param row The row index of the position.
   * @param col The column index of the position.
   * @return {@code true} if a piece is present at the specified position,
   *         {@code false} otherwise.
   */
  public boolean hasPieceAt(int row, int col) {
    return board[row][col] != null;
  }

  /**
   * Moves a piece from one position to another on the board.
   *
   * @param fromRow The current row index of the piece.
   * @param fromCol The current column index of the piece.
   * @param toRow   The target row index of the piece.
   * @param toCol   The target column index of the piece.
   * @return {@code true} if the piece was moved successfully, {@code false}
   *         otherwise.
   */
  public boolean movePiecePosition(int fromRow, int fromCol, int toRow, int toCol) {
    Piece piece = getPieceAt(fromRow, fromCol);
    Piece targetPiece = getPieceAt(toRow, toCol);
    boolean wasMoved = false;
    if (piece != null) {
      // Check if the piece is not moving to the same position
      if (fromRow != toRow || fromCol != toCol) {
        if ((piece.isPotentialMove(toRow, toCol) && targetPiece == null) ||
            (piece.isPotentialMove(toRow, toCol) && (!piece.getColor().equals(targetPiece.getColor())))) {
          // Capture logic
          if (targetPiece != null) {
            if (targetPiece.getColor().equals("white")) {
              capturedWhite.add(targetPiece);
              whitePieces.remove(targetPiece);
            } else {
              capturedBlack.add(targetPiece);
              blackPieces.remove(targetPiece);
            }
          }

          board[toRow][toCol] = piece;
          piece.setRank(toRow);
          piece.setFile(toCol);
          board[fromRow][fromCol] = null; // Clear the old position
          this.checkForCheck();
          if ((currentPlayer == Player.WHITE && whiteInCheck) || (currentPlayer == Player.BLACK && blackInCheck)) {
            board[fromRow][fromCol] = piece;
            piece.setRank(fromRow);
            piece.setFile(fromCol);

            board[toRow][toCol] = targetPiece;

          } else {
            wasMoved = true;
          }
          // Check if king is in check
          this.checkForCheck();
          if (whiteInCheck)
            System.out.println("White King is in Check");
          if (blackInCheck)
            System.out.println("Black King is in Check");
        }
      }
    }
    updateValidMoves();
    checkForCheckmate();
    if (checkmate) {
      System.out.println("Checkmate");
    }
    return wasMoved;
  }

  /**
   * Checks if either player's king is in check.
   * 
   * This method iterates over the entire board to determine if any piece is
   * attacking the opponent's king. If a white piece is attacking the black king,
   * {@code blackInCheck} is set to {@code true}. If a black piece is attacking
   * the white king, {@code whiteInCheck} is set to {@code true}.
   */
  public void checkForCheck() {
    whiteInCheck = false;
    blackInCheck = false;
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board.length; j++) {
        if (board[i][j] != null) {
          Piece piece = board[i][j];
          piece.possibleMoves(board);
          List<String> moves = piece.getMoves();
          for (String move : moves) {
            int[] array = move.chars().map(c -> c - '0').toArray();

            Piece pieceAtMove = board[array[0]][array[1]];
            if (pieceAtMove instanceof King) {
              if (piece.getOwner() == Player.WHITE && pieceAtMove.getOwner() == Player.BLACK) { // if white piece is
                                                                                                // attacking black king
                blackInCheck = true;
              }

              if (piece.getOwner() == Player.BLACK && pieceAtMove.getOwner() == Player.WHITE) { // if black piece
                                                                                                // is attacking
                                                                                                // white king
                whiteInCheck = true;
              }
            }
          }
        }
      }
    }
  }

  /**
   * Checks if the current player is in checkmate. This method sets the checkmate
   * flag to true if the current player is in check and has no legal moves to get
   * out of check. It iterates through all the opponent's pieces and simulates
   * their possible moves to determine if any move can prevent the checkmate.
   * 
   * The method performs the following steps:
   * 1. Sets the checkmate flag to false initially.
   * 2. Checks if the current player is in check and if the checkForCheckmate flag
   * is true.
   * 3. If both conditions are met, sets the checkmate flag to true.
   * 4. Retrieves the list of pieces for the opponent.
   * 5. Iterates through each piece and its possible moves.
   * 6. Clones the current board and simulates each move.
   * 7. Checks if the simulated move results in the current player not being in
   * check.
   * 8. If any move results in the current player not being in check, sets the
   * checkmate flag to false.
   */
  private void checkForCheckmate() {
    checkmate = false;
    if (checkForCheckmate && (whiteInCheck || blackInCheck)) {
      checkmate = true;
      List<Piece> currentPieces = currentPlayer == Player.WHITE ? blackPieces : whitePieces; // Get the opponent's
                                                                                             // pieces

      for (Piece piece : currentPieces) {
        List<String> moves = piece.getMoves();
        for (String move : moves) {
          int[] target = move.chars().map(c -> c - '0').toArray();
          Board clonedBoard = this.clone();
          clonedBoard.checkForCheckmate = false;
          if (clonedBoard.movePiecePosition(
              piece.getRank(), piece.getFile(),
              target[0], target[1])) {
            if ((currentPlayer == Player.WHITE && !clonedBoard.isBlackInCheck())
                || (currentPlayer == Player.BLACK && !clonedBoard.isWhiteInCheck())) {
              checkmate = false;
            }
          }
        }
      }
    }
  }

  /**
   * Converts a chess position in algebraic notation (e.g., "E4") to a 2D array
   * index.
   *
   * @param position The position in algebraic notation (e.g., "E4").
   * @return An array containing the row and column indices of the position.
   */
  public int[] positionToArray(String position) {
    int row = 8 - Character.getNumericValue(position.charAt(1)); // Convert rank (e.g., '2') to row index
    int col = position.charAt(0) - 'A'; // Convert file (e.g., 'E') to column index
    return new int[] { row, col };
  }

  /**
   * Converts a 2D array index to a chess position in algebraic notation.
   *
   * @param array An array containing the row and column indices.
   * @return The position in algebraic notation (e.g., "E4").
   */
  public String arrayToPosition(int[] array) {
    return String.valueOf((char) ('A' + array[1])) + (8 - array[0]);
  }

  /**
   * Retrieves the Unicode representation of the piece at the specified position.
   *
   * @param row The row index of the position.
   * @param col The column index of the position.
   * @return The Unicode representation of the piece at the specified position.
   */
  public String getUnicode(int row, int col) {
    if (this.board[row][col] == null)
      return "";
    return this.board[row][col].getUnicode();
  }

  /**
   * Checks if either king has been captured.
   *
   * @return {@code true} if either king has been captured, {@code false}
   *         otherwise.
   */
  public boolean isKingCaptured() {
    boolean whiteKingPresent = false;
    boolean blackKingPresent = false;

    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        Piece piece = board[row][col];
        if (piece instanceof King) {
          if (piece.getColor().equals("white")) {
            whiteKingPresent = true;
          } else if (piece.getColor().equals("black")) {
            blackKingPresent = true;
          }
        }
      }
    }

    if (!whiteKingPresent || !blackKingPresent) {
      return true;
    }
    return false;
  }

  /**
   * Determines the winner of the game.
   *
   * @return "White" if the white king is captured, "Black" if the black king is
   *         captured, or {@code null} if neither king is captured.
   */
  public String getWinner() {
    boolean whiteKingPresent = false;
    boolean blackKingPresent = false;

    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        Piece piece = board[row][col];
        if (piece instanceof King) {
          if (piece.getColor().equals("white")) {
            whiteKingPresent = true;
          } else if (piece.getColor().equals("black")) {
            blackKingPresent = true;
          }
        }
      }
    }

    if (!whiteKingPresent) {
      return "Black";
    } else if (!blackKingPresent) {
      return "White";
    }
    return null;
  }

  /**
   * Retrieves the list of captured white pieces.
   *
   * @return A list of white pieces that have been captured.
   */
  public List<Piece> getCapturedWhite() {
    return capturedWhite;
  }

  /**
   * Sets the list of captured white pieces.
   *
   * @param capturedWhite A list of white pieces to set as captured.
   */
  public void setCapturedWhite(List<Piece> capturedWhite) {
    this.capturedWhite = capturedWhite;
  }

  /**
   * Retrieves the list of captured black pieces.
   *
   * @return A list of black pieces that have been captured.
   */
  public List<Piece> getCapturedBlack() {
    return capturedBlack;
  }

  /**
   * Sets the list of captured black pieces.
   *
   * @param capturedBlack A list of black pieces to set as captured.
   */
  public void setCapturedBlack(List<Piece> capturedBlack) {
    this.capturedBlack = capturedBlack;
  }

  /**
   * Creates a deep copy of the current Board instance.
   *
   * @return A new {@code Board} object that is a clone of the current board.
   */
  @Override
  public Board clone() {
    try {
      Board cloned = (Board) super.clone();

      // Deep clone the board
      cloned.board = new Piece[8][8];
      for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 8; j++) {
          if (this.board[i][j] != null) {
            cloned.board[i][j] = this.board[i][j].clone();
          }
        }
      }

      // Deep clone capturedWhite
      cloned.capturedWhite = new ArrayList<>();
      for (Piece p : this.capturedWhite) {
        cloned.capturedWhite.add(p.clone());
      }

      // Deep clone capturedBlack
      cloned.capturedBlack = new ArrayList<>();
      for (Piece p : this.capturedBlack) {
        cloned.capturedBlack.add(p.clone());
      }

      // Deep clone whitePieces
      cloned.whitePieces = new ArrayList<>();
      for (Piece p : this.whitePieces) {
        cloned.whitePieces.add(p.clone());
      }

      // Deep clone blackPieces
      cloned.blackPieces = new ArrayList<>();
      for (Piece p : this.blackPieces) {
        cloned.blackPieces.add(p.clone());
      }

      // Clone primitive and immutable fields
      cloned.whiteInCheck = this.whiteInCheck;
      cloned.blackInCheck = this.blackInCheck;
      cloned.currentPlayer = this.currentPlayer;
      cloned.checkForCheckmate = this.checkForCheckmate;

      return cloned;
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }

  public List<Piece> getPieceList(String player) {
    return (player == "white" ? whitePieces : blackPieces);
  }

  public void updateValidMoves() {
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board.length; j++) {
        if (board[i][j] != null) {
          board[i][j].possibleMoves(board);
        }
      }
    }
  }
}
