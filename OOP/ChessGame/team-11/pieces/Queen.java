package pieces;

import board.Player;

/**
 * The {@code Queen} class represents a Queen chess piece.
 * It extends the {@code Piece} class and inherits its attributes and methods.
 * The Queen can move any number of squares along a row, column, or diagonal.
 * 
 * <p>
 * Attributes:
 * </p>
 * <ul>
 * <li>{@code color} - The color of the piece, either "white" or "black".</li>
 * <li>{@code position} - The current position of the piece on the chessboard,
 * e.g., "D1".</li>
 * </ul>
 * 
 * <p>
 * Methods:
 * </p>
 * <ul>
 * <li>{@code Queen(String color, String position, Player owner, String UNICODE_PIECE)} - Constructor to initialize
 * the Queen with a color, position, owner, and Unicode representation.</li>
 * <li>{@code getName()} - Returns the name of the piece.</li>
 * <li>{@code possibleMoves()} - Determines the possible moves for the Queen
 * based on its current position.</li>
 * </ul>
 */
public class Queen extends Piece {
  /**
   * Represents a Queen chess piece.
   * 
   * @param color         The color of the Queen, typically "white" or "black".
   * @param position      The initial position of the Queen on the chessboard,
   *                      represented in standard chess notation (e.g., "d1", "d8").
   * @param owner         The player who owns this piece.
   * @param UNICODE_PIECE The Unicode representation of the piece.
   */
  public Queen(String color, int rank, int file, Player owner, String UNICODE_PIECE) {
    super(color, rank, file, owner, UNICODE_PIECE);
  }

  /**
   * Returns the name of the piece.
   * 
   * @return The name of the piece, which is "Queen".
   */
  public String getName() {
    return "Queen";
  }

  /**
   * Calculates all possible moves for the Queen piece.
   * The Queen can move like both a Rook (horizontally/vertically) and a Bishop
   * (diagonally).
   * This method combines the possible moves of both Rook and Bishop to determine
   * the Queen's moves.
   *
   * @return An array of strings representing all possible moves for the Queen.
   */
  @Override
  public void possibleMoves(Piece[][] board) {
    // Queens move like both Rooks (horizontally/vertically) and Bishops
    // (diagonally)
    moves.clear();
    //Use Bishop's diagonal logic
    Bishop tempBishop = new Bishop(color, rank, file, owner, UNICODE_PIECE);
    tempBishop.possibleMoves(board);
    this.moves.addAll(tempBishop.moves);


    // Use Rook's horizontal/vertical logic
    Rook tempRook = new Rook(color, rank, file, owner, UNICODE_PIECE);
    tempRook.possibleMoves(board);
    this.moves.addAll(tempRook.moves);

  }
}