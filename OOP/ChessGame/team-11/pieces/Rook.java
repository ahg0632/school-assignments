package pieces;

import board.Player;

/**
 * The {@code Rook} class represents a rook chess piece.
 * It extends the {@code Piece} class and implements the specific movement logic
 * for a rook.
 * A rook can move horizontally or vertically any number of squares on the
 * chessboard.
 * 
 * <p>
 * Attributes:
 * </p>
 * <ul>
 * <li>{@code color} - The color of the rook, either "white" or "black".</li>
 * <li>{@code position} - The current position of the rook on the chessboard,
 * e.g., "A1".</li>
 * </ul>
 * 
 * <p>
 * Methods:
 * </p>
 * <ul>
 * <li>{@code Rook(String color, String position)} - Constructor to initialize
 * the rook with a color and position.</li>
 * <li>{@code possibleMoves()} - Determines the possible moves for the rook
 * based on its current position.</li>
 * </ul>
 */
public class Rook extends Piece {
  /**
   * Constructs a Rook piece with the specified color, position, owner, and Unicode representation.
   *
   * @param color          the color of the Rook piece, either "white" or "black"
   * @param position       the initial position of the Rook piece on the chessboard
   * @param owner          the player who owns this piece
   * @param UNICODE_PIECE  the Unicode representation of the Rook piece
   */
  public Rook(String color, int rank, int file, Player owner, String UNICODE_PIECE) {
    super(color, rank, file, owner, UNICODE_PIECE);
  }

  /**
   * Returns the name of the piece.
   *
   * @return the name of the piece, which is "Rook"
   */
  public String getName() {
    return "Rook";
  }

  /**
   * Calculates the possible moves for a Rook piece in a chess game.
   * The Rook can move horizontally or vertically on the same file or rank.
   *
   * @return An array of strings representing the possible moves for the Rook.
   *         Each move is represented in standard chess notation (e.g., "A1",
   *         "H8").
   */
  @Override
  public void possibleMoves(Piece[][] board) {
    // Rook moves horizontally or vertically on the same file or rank
    moves.clear();
    //char file = position.charAt(0); // e.g., 'A'
    //int rank = Character.getNumericValue(position.charAt(1)); // e.g., 1
    boolean horizontalLeft = true,
            horizontalRight = true,
            verticalUp = true,
            verticalDown = true;

    // Horizontal (same rank, move across files)
    // for (char f = 'A'; f <= 'H'; f++) {
    //   if (f != file) {
    //     moves.add("" + f + rank);
    //   }
    // }

    // Vertical (same file, move across ranks)
    // for (int r = 1; r <= 8; r++) {
    //   if (r != rank) {
    //     moves.add("" + file + r);
    //   }
    // }

    for (int offset = 1; offset <= 7; offset++) {
      if (this.getRank() + offset <= 7 && verticalUp) {
          this.moves.add("" + (this.getRank() + offset) + (this.getFile()));  // Veritcal-up
          if(board[this.getRank() + offset][this.getFile()] != null) verticalUp = false;
      } else {verticalUp = false;}

      if (this.getRank() - offset >= 0 && verticalDown) {
          this.moves.add("" + (getRank() - offset) + (getFile()));  // Vertical-down
          if(board[this.getRank() - offset][this.getFile()] != null) verticalDown = false;
      } else {verticalDown = false;}
      
      if (this.getFile() - offset >= 0 && horizontalLeft) {
          this.moves.add("" + (getRank()) + (getFile() - offset));  // Horizontal-left
          if(board[this.getRank()][this.getFile() - offset] != null) horizontalLeft = false;
      } else {horizontalLeft = false;}

      if (this.getFile() + offset <= 7 && horizontalRight) {
          this.moves.add("" + (getRank()) + (getFile() + offset));  // Horizaontal-right
          if(board[this.getRank()][this.getFile() + offset] != null) horizontalRight = false;
      } else {horizontalRight = false;}

      if(!verticalUp && !verticalDown && !horizontalLeft && !horizontalRight) break;
    }
  }
}