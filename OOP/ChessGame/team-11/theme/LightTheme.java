package theme;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 * LightTheme class that defines the color and font settings for a light-themed chess board.
 */
public class LightTheme extends Theme {
  private Font pieceFont = new Font("Serif", Font.PLAIN, 50);

  /**
   * Gets the color of the light squares on the chess board.
   * @return the color of the light squares.
   */
  @Override
  public Color getLightSquareColor() {
    return new Color(242, 236, 223);
  }

  /**
   * Gets the color of the dark squares on the chess board.
   * @return the color of the dark squares.
   */
  @Override
  public Color getDarkSquareColor() {
    return new Color(194, 188, 169);
  }

  /**
   * Gets the border color for the selected square.
   * @return the border color for the selected square.
   */
  @Override
  public Color getSelectedSquareBorderColor() {
    return new Color(128, 125, 117);
  }

  /**
   * Gets the border thickness for the selected square.
   * @return the border thickness for the selected square.
   */
  @Override
  public int getSelectedSquareBorderThickness() {
    return 5;
  }

  /**
   * Gets the border color for the highlighted square.
   * @return the border color for the highlighted square.
   */
  @Override
  public Color getHighlightSquareBorderColor() {
    return new Color(128, 125, 117);
  }

  /**
   * Gets the border thickness for the highlighted square.
   * @return the border thickness for the highlighted square.
   */
  @Override
  public int getHighlightSquareBorderThickness() {
    return 5;
  }

  /**
   * Gets the font used for white pieces.
   * @return the font used for white pieces.
   */
  @Override
  public Font getWhitePieceFont() {
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
   * Sets the font to default (plain).
   */
  @Override
  public void setDefaultFont() {
    pieceFont = new Font("Serif", Font.PLAIN, 50);
  }

  /**
   * Gets the font color for white pieces.
   * @return the font color for white pieces.
   */
  @Override
  public Color getWhitePieceFontColor() {
    return new Color(127, 123, 108);
  }

  /**
   * Gets the font used for black pieces.
   * @return the font used for black pieces.
   */
  @Override
  public Font getBlackPieceFont() {
    return pieceFont;
  }

  /**
   * Gets the font color for black pieces.
   * @return the font color for black pieces.
   */
  @Override
  public Color getBlackPieceFontColor() {
    return Color.BLACK;
  }

  /**
   * Gets the original border for the chess board.
   * @return the original border for the chess board.
   */
  @Override
  public Border getOriginalBorder() {
    return BorderFactory.createLineBorder(Color.BLACK);
  }
}
