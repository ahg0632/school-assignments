package theme;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class DarkTheme extends Theme {
  private Font pieceFont = new Font("Serif", Font.PLAIN, 50);

  /**
   * Gets the color of the light squares on the chessboard.
   * @return the color of the light squares.
   */
  @Override
  public Color getLightSquareColor() {
    return new Color(112, 111, 105);
  }

  /**
   * Gets the color of the dark squares on the chessboard.
   * @return the color of the dark squares.
   */
  @Override
  public Color getDarkSquareColor() {
    return new Color(66, 65, 62);
  }

  /**
   * Gets the border color for the selected square.
   * @return the color of the selected square border.
   */
  @Override
  public Color getSelectedSquareBorderColor() {
    return Color.BLUE;
  }

  /**
   * Gets the thickness of the border for the selected square.
   * @return the thickness of the selected square border.
   */
  @Override
  public int getSelectedSquareBorderThickness() {
    return 3;
  }

  /**
   * Gets the border color for the highlighted square.
   * @return the color of the highlighted square border.
   */
  @Override
  public Color getHighlightSquareBorderColor() {
    return Color.RED;
  }

  /**
   * Gets the thickness of the border for the highlighted square.
   * @return the thickness of the highlighted square border.
   */
  @Override
  public int getHighlightSquareBorderThickness() {
    return 3;
  }

  /**
   * Gets the font used for white pieces.
   * @return the font for white pieces.
   */
  @Override
  public Font getWhitePieceFont() {
    return pieceFont;
  }

  /**
   * Gets the font color used for white pieces.
   * @return the font color for white pieces.
   */
  @Override
  public Color getWhitePieceFontColor() {
    return Color.WHITE;
  }

  /**
   * Gets the font used for black pieces.
   * @return the font for black pieces.
   */
  @Override
  public Font getBlackPieceFont() {
    return pieceFont;
  }

  /**
   * Sets the font to bold.
   */
  @Override
  public void setBoldFont() {
    pieceFont = new Font("Serif", Font.BOLD, 50);
  }

  /**
   * Sets the font to the default style.
   */
  @Override
  public void setDefaultFont() {
    pieceFont = new Font("Serif", Font.PLAIN, 50);
  }

  /**
   * Gets the font color used for black pieces.
   * @return the font color for black pieces.
   */
  @Override
  public Color getBlackPieceFontColor() {
    return Color.BLACK;
  }

  /**
   * Gets the original border for the chessboard.
   * @return the original border.
   */
  @Override
  public Border getOriginalBorder() {
    return BorderFactory.createLineBorder(Color.WHITE);
  }
}
