import board.Board;
import gui.BoardGUI;

/**
 * The {@code Game} class represents a chess game.
 * It manages the game state, including the board and the current player,
 * and handles the main game loop where players take turns to make moves.
 * 
 * <p>
 * Attributes:
 * </p>
 * <ul>
 * <li>{@code board} - The chessboard on which the game is played.</li>
 * <li>{@code gui} - The graphical user interface for the chessboard.</li>
 * <li>{@code currentPlayer} - The player whose turn it is to move, either
 * "white" or "black".</li>
 * </ul>
 * 
 * <p>
 * Methods:
 * </p>
 * <ul>
 * <li>{@code Game()} - Constructor to initialize the game with a new board and
 * set the starting player to "white".</li>
 * <li>{@code main(String[] args)} - The main method to start the game.</li>
 * </ul>
 */
public class Game {
  /**
   * The board instance variable represents the chess board for the game.
   * It is used to manage the state and operations of the chess game.
   */
  private Board board;
  
  /**
   * The gui instance variable represents the graphical user interface for the chess board.
   * It is used to display the board and interact with the user.
   */
  //private BoardGUI gui;

  /**
   * Constructs a new Game instance, initializing the game board and setting the
   * current player to "white". The white player always starts first.
   * It also initializes the graphical user interface for the board.
   */
  public Game() {
    board = new Board();
    new BoardGUI(board);
  }

  /**
   * The main method serves as the entry point for the application.
   * It creates an instance of the Game class and starts the game by calling the
   * play method.
   *
   * @param args Command line arguments (not used in this application).
   */
  public static void main(String[] args) {
    new Game();
  }
}