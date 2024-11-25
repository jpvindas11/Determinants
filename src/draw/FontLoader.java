package draw;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;

public class FontLoader {

    public FontLoader() {}

    public static Font loadCustomFont(String path, float size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, FontLoader.class.getResourceAsStream(path));
            font = font.deriveFont(size);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            return font;
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
