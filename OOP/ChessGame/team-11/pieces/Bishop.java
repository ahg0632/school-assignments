package pieces;

import board.Player;

/**
 * The {@code Bishop} class represents a bishop chess piece.
 * It extends the {@code Piece} class and implements the specific movement logic for a bishop.
 * Bishops move diagonally on the chessboard.
 * 
 * <p>
 * Attributes:
 * </p>
 * <ul>
 * <li>{@code color} - The color of the bishop, either "white" or "black".</li>
 * <li>{@code position} - The current position of the bishop on the chessboard, e.g., "C1".</li>
 * </ul>
 * 
 * <p>
 * Methods:
 * </p>
 * <ul>
 * <li>{@code Bishop(String color, String position)} - Constructor to initialize the bishop with a color and position.</li>
 * <li>{@code possibleMoves()} - Determines the possible moves for the bishop based on its current position. Bishops move diagonally, so both file and rank change together.</li>
 * </ul>
 */

public class Bishop extends Piece {
    /**
     * Represents a Bishop chess piece.
     * 
     * @param color    The color of the Bishop, either "white" or "black".
     * @param position The initial position of the Bishop on the chessboard, 
     *                 represented in standard chess notation (e.g., "c1", "f8").
     * @param owner    The player who owns this piece.
     * @param UNICODE_PIECE The Unicode representation of the piece.
     */
    public Bishop(String color, int rank, int file, Player owner, String UNICODE_PIECE) {
        super(color, rank, file, owner, UNICODE_PIECE);
    }

    /**
     * Returns the name of the piece.
     * 
     * @return The name of the piece, i.e., "Bishop".
     */
    public String getName() {
        return "Bishop";
    }

    /**
     * Calculates all possible moves for a Bishop piece on a chessboard.
     * Bishops move diagonally, which means both file (column) and rank (row) change together.
     * 
     * @return An array of strings representing all possible moves for the Bishop.
     *         Each move is represented as a string with the file and rank concatenated.
     *         For example, "E4" represents the file 'E' and rank '4'.
     */
    @Override
    public void possibleMoves(Piece[][] board) {
        // Bishops move diagonally, which means both file and rank change together
        this.moves.clear();
        //char file = position.charAt(0);
        //int rank = Character.getNumericValue(position.charAt(1));
        boolean topRight = true, 
                topLeft = true, 
                bottomRight = true, 
                bottomLeft = true;
        
        // Diagonal moves (top-right, top-left, bottom-right, bottom-left)
        for (int offset = 1; offset <= 7; offset++) {
            if (rank + offset <= 7 && file + offset <= 7 && topRight) {
                this.moves.add("" + (getRank() + offset) + (getFile() + offset));  // Top-right
                if(board[rank + offset][file + offset] != null) topRight = false;
            } else {topRight = false;}

            if (rank - offset >= 0 && file + offset <= 7 && bottomRight) {
                this.moves.add("" + (getRank() - offset) + (getFile() + offset));  // Bottom-right
                if(board[rank - offset][file + offset] != null) bottomRight = false;
            } else {bottomRight = false;}

            if (rank + offset <= 7 && file - offset >= 0 && topLeft) {
                this.moves.add("" + (getRank() + offset) + (getFile() - offset));  // Top-left
                if(board[rank + offset][file - offset] != null) topLeft = false;
            } else {topLeft = false;}

            if (rank - offset >= 0 && file - offset >= 0 && bottomLeft) {
                this.moves.add("" + (getRank() - offset) + (getFile() - offset));  // Bottom-left
                if(board[rank - offset][file - offset] != null) bottomLeft = false;
            } else {bottomLeft = false;}

            if(!topRight && !topLeft && !bottomRight && !bottomLeft) break;
        }
    }
}

