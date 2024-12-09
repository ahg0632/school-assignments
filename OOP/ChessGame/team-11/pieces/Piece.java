package pieces;

import board.Player;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class representing a chess piece.
 * This class provides common properties and methods for all chess pieces.
 */
public abstract class Piece implements Serializable, Cloneable {
  private static final long serialVersionUID = 1L;
  protected String color; // "white" or "black"
  protected String position; // e.g., "E2"
  protected int rank;
  protected int file;
  protected String UNICODE_PIECE;
  protected Player owner;
  private String unicodeChar;
  protected List<String> moves;

  /**
   * Constructs a Piece with specified color, position, owner, and Unicode
   * representation.
   *
   * @param color         the color of the piece ("white" or "black")
   * @param position      the position of the piece on the board (e.g., "E2")
   * @param owner         the player who owns the piece
   * @param UNICODE_PIECE the Unicode representation of the piece
   */
  public Piece(String color, int rank, int file, Player owner, String UNICODE_PIECE) {
    this.color = color;
    this.rank = rank;
    this.file = file;
    this.owner = owner;
    this.UNICODE_PIECE = UNICODE_PIECE;
    this.moves = new ArrayList<>();
  }

  /**
   * Constructs a Piece with specified owner and Unicode character.
   *
   * @param owner       the player who owns the piece
   * @param unicodeChar the Unicode character of the piece
   */
  public Piece(Player owner, String unicodeChar) {
    this.owner = owner;
    this.unicodeChar = unicodeChar;
  }

  /**
   * Default constructor for Piece.
   */
  public Piece() {
    this.color = "";
    this.rank = -1;
    this.file = -1;
    this.position = "";
    this.UNICODE_PIECE = "";
    this.moves = new ArrayList<>();
  }

  /**
   * Returns an array of possible moves for the piece.
   *
   * @return an array of possible moves
   */
  // UPDATE: If board orientation and piece placement logic is changed
  // this logic will need to be adjusted for each piece
  public abstract void possibleMoves(Piece[][] board);

  /**
   * Returns the name of the piece.
   *
   * @return the name of the piece
   */
  public abstract String getName();

  /**
   * Moves the piece to a new position on the board.
   *
   * @param newPosition the new position to move the piece to, represented as a
   *                    string.
   */
  public void move(String newPosition) {
    this.position = newPosition;
  }

  /**
   * Retrieves the color of the chess piece.
   *
   * @return the color of the chess piece as a String.
   */
  public String getColor() {
    return color;
  }

  /**
   * Retrieves the current position of the piece on the board.
   *
   * @return the current position of the piece
   */
  public String getPosition() {
    return position;
  }

  public int getRank() {
    return this.rank;
  }

  public int getFile() {
    return this.file;
  }

  /**
   * Sets the position of the piece on the board.
   *
   * @param position the new position of the piece
   */
  public void setPosition(String position) {
    this.position = position;
  }

  public void setRank(int newRank) {
    this.rank = newRank;
  }

  public void setFile(int newFile) {
    this.file = newFile;
  }

  /**
   * Retrieves the Unicode representation of the piece.
   *
   * @return the Unicode representation of the piece
   */
  public String getUnicode() {
    return UNICODE_PIECE;
  }

  /**
   * Retrieves the owner of the piece.
   *
   * @return the player who owns the piece
   */
  public Player getOwner() {
    return owner;
  }

  /**
   * Retrieves the Unicode character of the piece.
   *
   * @return the Unicode character of the piece
   */
  public String getUnicodeChar() {
    return unicodeChar;
  }

  /**
   * Returns a string representation of the piece.
   *
   * @return the name of the piece
   */
  public String toString() {
    return getName();
  }

  public boolean isPotentialMove(int rank, int file) {
    // System.out.println(
    //     this.getColor() + " " + this.getName() + " " + this.moves + " " + this.getRank() + " " + this.getFile());
    String pair = ("" + rank + file);
    return this.moves.contains(pair);
  }

  public List<String> getMoves() {
    return moves;
  }

  /**
   * Creates and returns a copy of this piece.
   *
   * @return a clone of this piece
   */
  @Override
  public Piece clone() {
    try {
      Piece cloned = (Piece) super.clone();
      // Deep clone the moves list to prevent shared references
      cloned.moves = new ArrayList<>(this.moves);
      return cloned;
    } catch (CloneNotSupportedException e) {
      throw new AssertionError("Cloning not supported for Piece", e);
    }
  }

}