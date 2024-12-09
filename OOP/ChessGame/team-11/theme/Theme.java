package theme;

import java.awt.Color;
import java.awt.Font;
import javax.swing.border.Border;

public abstract class Theme {
    // Board colors

    /**
     * Gets the color of the light squares on the board.
     * @return the color of the light squares
     */
    public abstract Color getLightSquareColor();

    /**
     * Gets the color of the dark squares on the board.
     * @return the color of the dark squares
     */
    public abstract Color getDarkSquareColor();

    // Selection/highlight colors

    /**
     * Gets the border color of the selected square.
     * @return the border color of the selected square
     */
    public abstract Color getSelectedSquareBorderColor();

    /**
     * Gets the border thickness of the selected square.
     * @return the border thickness of the selected square
     */
    public abstract int getSelectedSquareBorderThickness();

    /**
     * Gets the border color of the highlighted square.
     * @return the border color of the highlighted square
     */
    public abstract Color getHighlightSquareBorderColor();

    /**
     * Gets the border thickness of the highlighted square.
     * @return the border thickness of the highlighted square
     */
    public abstract int getHighlightSquareBorderThickness();

    // Piece font and color

    /**
     * Gets the font used for white pieces.
     * @return the font used for white pieces
     */
    public abstract Font getWhitePieceFont();

    /**
     * Gets the font color used for white pieces.
     * @return the font color used for white pieces
     */
    public abstract Color getWhitePieceFontColor();

    /**
     * Gets the font used for black pieces.
     * @return the font used for black pieces
     */
    public abstract Font getBlackPieceFont();

    /**
     * Gets the font color used for black pieces.
     * @return the font color used for black pieces
     */
    public abstract Color getBlackPieceFontColor();

    /**
     * Sets the font to bold.
     */
    public abstract void setBoldFont();

    /**
     * Sets the font to default.
     */
    public abstract void setDefaultFont();

    // Borders

    /**
     * Gets the original border.
     * @return the original border
     */
    public abstract Border getOriginalBorder();
}
