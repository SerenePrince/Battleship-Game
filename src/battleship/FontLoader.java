package battleship;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

/**
 * The FontLoader class provides utility methods for loading fonts from file
 * paths. It supports loading fonts with specified sizes and styles.
 * 
 * <p>
 * Example usage:
 * </p>
 * 
 * <pre>
 * {@code
 * Font font = FontLoader.loadFont("path/to/font.ttf", 12f);
 * Font boldFont = FontLoader.loadFont("path/to/font.ttf", Font.BOLD, 14f);
 * }
 * </pre>
 * 
 * @see java.awt.Font
 * @see java.awt.FontFormatException
 */
public class FontLoader {

	/**
	 * Loads a font from the specified file path with the given size.
	 *
	 * @param path The path to the font file.
	 * @param size The size of the font.
	 * @return The loaded font, or {@code null} if there was an error.
	 * @throws NullPointerException     if the path is {@code null}.
	 * @throws IllegalArgumentException if the size is negative.
	 */
	public static Font loadFont(String path, float size) {
		try {
			File fontFile = new File(path);
			Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
			return font.deriveFont(size);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Loads a font from the specified file path with the given style and size.
	 *
	 * @param path  The path to the font file.
	 * @param style The style of the font (e.g., {@link Font#BOLD}).
	 * @param size  The size of the font.
	 * @return The loaded font, or {@code null} if there was an error.
	 * @throws NullPointerException     if the path is {@code null}.
	 * @throws IllegalArgumentException if the size is negative.
	 */
	public static Font loadFont(String path, int style, float size) {
		try {
			File fontFile = new File(path);
			Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
			return font.deriveFont(style, size);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
