package pieces;

import board.Player;

/**
 * The {@code King} class represents a King chess piece.
 * It extends the {@code Piece} class and implements the specific movement rules for a King.
 * 
 * <p>
 * Attributes:
 * </p>
 * <ul>
 * <li>{@code color} - The color of the piece, either "white" or "black".</li>
 * <li>{@code position} - The current position of the piece on the chessboard, e.g., "E2".</li>
 * </ul>
 * 
 * <p>
 * Methods:
 * </p>
 * <ul>
 * <li>{@code King(String color, String position)} - Constructor to initialize the King with a color and position.</li>
 * <li>{@code possibleMoves()} - Determines the possible moves for the King based on its current position.</li>
 * </ul>
 */
public class King extends Piece {
    /**
     * Constructs a King piece with the specified color, position, owner, and Unicode representation.
     *
     * @param color          the color of the King piece (e.g., "white" or "black")
     * @param position       the initial position of the King piece on the chessboard (e.g., "e1")
     * @param owner          the player who owns this piece
     * @param UNICODE_PIECE  the Unicode representation of the King piece
     */
    public King(String color, int rank, int file, Player owner, String UNICODE_PIECE) {
        super(color, rank, file, owner, UNICODE_PIECE);
    }

    /**
     * Returns the name of the piece.
     *
     * @return the name of the piece, which is "King"
     */
    public String getName() {
        return "King";
    }

    /**
     * Calculates and returns all possible moves for the King piece from its current position.
     * The King can move one square in any direction: up, down, left, right, and diagonally.
     *
     * @return An array of strings representing the possible moves for the King. Each move is 
     *         represented as a string in the format "fileRank" (e.g., "E2", "D3"). The array 
     *         will contain up to 8 possible moves, depending on the King's position on the board.
     */
    @Override
    public void possibleMoves(Piece[][] board) {
        // Kings move one square in any direction (up, down, left, right, diagonals)
        //String[] moves = new String[8];  // Maximum 8 moves for a King
        //int count = 0;
        moves.clear();
        //char file = position.charAt(0);
        //int rank = Character.getNumericValue(position.charAt(1));

        int[] fileOffsets = {1, 1, 1, 0, 0, -1, -1, -1};
        int[] rankOffsets = {1, 0, -1, 1, -1, 1, 0, -1};

        for (int i = 0; i < 8; i++) {
            int newFile = file + fileOffsets[i];
            int newRank = rank + rankOffsets[i];

            if (newFile >= 0 && newFile <= 7 && newRank >= 0 && newRank <= 7) {
                moves.add("" + newRank + newFile);
            }
        }

    }
}
