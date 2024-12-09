package pieces;

import board.Player;

/**
 * The {@code Pawn} class represents a pawn chess piece.
 * It extends the {@code Piece} class and implements the specific behavior for pawns.
 * 
 * <p>
 * Attributes:
 * </p>
 * <ul>
 * <li>{@code color} - The color of the pawn, either "white" or "black".</li>
 * <li>{@code position} - The current position of the pawn on the chessboard, e.g., "E2".</li>
 * </ul>
 * 
 * <p>
 * Methods:
 * </p>
 * <ul>
 * <li>{@code Pawn(String color, String position)} - Constructor to initialize the pawn with a color and position.</li>
 * <li>{@code possibleMoves()} - Determines the possible moves for the pawn based on its current position and color.</li>
 * </ul>
 */
public class Pawn extends Piece {
    private boolean initMove;
     /**
     * Represents a Pawn chess piece.
     * 
     * @param color    The color of the pawn, typically "white" or "black".
     * @param position The initial position of the pawn on the chessboard, 
     *                 represented in standard chess notation (e.g., "e2").
     * @param owner    The player who owns this pawn.
     * @param UNICODE_PIECE The Unicode representation of the pawn piece.
     */
    public Pawn(String color, int rank, int file, Player owner, String UNICODE_PIECE) {
        super(color, rank, file, owner, UNICODE_PIECE);
        initMove = true;
    }

    /**
     * Gets the name of the piece.
     * 
     * @return The name of the piece, which is "Pawn".
     */
    public String getName() {
        return "Pawn";
    }

    public void setInitMove() {
        this.initMove = false;
    }

     /**
     * Calculates the possible moves for a pawn piece based on its current position.
     * For simplicity, this method assumes that pawns can only move forward one square.
     *
     * @return An array of strings representing the possible moves for the pawn.
     *         For a white pawn, it moves one rank forward.
     *         For a black pawn, it moves one rank backward.
     */
    @Override
    public void possibleMoves(Piece[][] board) {
        this.moves.clear();

        if (color.equals("white")) {
            if (this.getRank() != 6) { setInitMove(); }
            if (this.initMove) {
                // Check if both squares are empty
                if (board[this.getRank() - 1][getFile()] == null && board[this.getRank() - 2][getFile()] == null) {
                    this.moves.add("" + (this.getRank() - 2) + getFile());
                }
            }
            if (this.getRank() - 1 >= 0) {
                if (board[this.getRank() - 1][getFile()] == null) {
                    this.moves.add("" + (this.getRank() - 1) + getFile());
                }
                if (getFile() - 1 >= 0 && board[this.getRank() - 1][getFile() - 1] != null &&
                    !board[this.getRank() - 1][getFile() - 1].getColor().equals(this.getColor())) {
                    this.moves.add("" + (this.getRank() - 1) + (getFile() - 1));
                }
                if (getFile() + 1 <= 7 && board[this.getRank() - 1][getFile() + 1] != null &&
                    !board[this.getRank() - 1][getFile() + 1].getColor().equals(this.getColor())) {
                    this.moves.add("" + (this.getRank() - 1) + (getFile() + 1));
                }
            }
        } else {
            if (this.getRank() != 1) { setInitMove(); }
            if (this.initMove) {
                // Check if both squares are empty
                if (board[this.getRank() + 1][getFile()] == null && board[this.getRank() + 2][getFile()] == null) {
                    this.moves.add("" + (this.getRank() + 2) + getFile());
                }
            }
            if (this.getRank() + 1 <= 7) {
                if (board[this.getRank() + 1][getFile()] == null) {
                    this.moves.add("" + (this.getRank() + 1) + getFile());
                }
                if (getFile() - 1 >= 0 && board[this.getRank() + 1][getFile() - 1] != null &&
                    !board[this.getRank() + 1][getFile() - 1].getColor().equals(this.getColor())) {
                    this.moves.add("" + (this.getRank() + 1) + (getFile() - 1));
                }
                if (getFile() + 1 <= 7 && board[this.getRank() + 1][getFile() + 1] != null &&
                    !board[this.getRank() + 1][getFile() + 1].getColor().equals(this.getColor())) {
                    this.moves.add("" + (this.getRank() + 1) + (getFile() + 1));
                }
            }
        }
    }
}