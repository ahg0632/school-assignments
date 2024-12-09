package pieces;

import board.Player;

/**
 * The {@code Knight} class represents a knight chess piece.
 * It extends the {@code Piece} class and implements the specific movement logic
 * for a knight.
 * Knights move in an "L" shape: 2 squares in one direction, then 1 square
 * perpendicular.
 * 
 * <p>
 * Attributes:
 * </p>
 * <ul>
 * <li>{@code color} - The color of the piece, either "white" or "black".</li>
 * <li>{@code position} - The current position of the piece on the chessboard,
 * e.g., "E2".</li>
 * </ul>
 * 
 * <p>
 * Methods:
 * </p>
 * <ul>
 * <li>{@code Knight(String color, String position)} - Constructor to initialize
 * the knight with a color and position.</li>
 * <li>{@code possibleMoves()} - Determines the possible moves for the knight
 * based on its current position.</li>
 * </ul>
 */
public class Knight extends Piece {
  /**
   * Represents a Knight chess piece.
   * 
   * @param color    The color of the Knight, typically "white" or "black".
   * @param position The initial position of the Knight on the chessboard,
   *                 represented in standard chess notation (e.g., "g1", "b8").
   * @param owner    The player who owns this piece.
   * @param UNICODE_PIECE The Unicode representation of the piece.
   */
  public Knight(String color, int rank, int file, Player owner, String UNICODE_PIECE) {
    super(color, rank, file, owner, UNICODE_PIECE);
  }

  /**
   * Returns the name of the piece.
   * 
   * @return The name of the piece, which is "Knight".
   */
  public String getName() {
    return "Knight";
  }

  /**
   * Calculates and returns all possible moves for a Knight piece on a chessboard.
   * 
   * Knights move in an "L" shape: 2 squares in one direction, then 1 square
   * perpendicular.
   * This method considers all 8 possible moves a Knight can make from its current
   * position.
   * 
   * @return An array of strings representing the possible moves in standard chess
   *         notation.
   *         Each move is represented as a combination of file (A-H) and rank
   *         (1-8).
   */
  @Override
  public void possibleMoves(Piece[][] board) {
    // Knights move in "L" shape: 2 squares in one direction, then 1 square
    // perpendicular
    //String[] moves = new String[8]; // Knights have up to 8 possible moves
    //int count = 0;
    moves.clear();
    //char file = position.charAt(0);
    //int rank = Character.getNumericValue(position.charAt(1));

    int[] fileOffsets = { 2, 2, -2, -2, 1, 1, -1, -1 };
    int[] rankOffsets = { 1, -1, 1, -1, 2, -2, 2, -2 };

    for (int i = 0; i < 8; i++) {
      int newFile = file + fileOffsets[i];
      int newRank = rank + rankOffsets[i];

      if (newFile >= 0 && newFile <= 7 && newRank >= 0 && newRank <= 7) {
        moves.add("" + newRank + newFile);
      }
    }

  }
}
